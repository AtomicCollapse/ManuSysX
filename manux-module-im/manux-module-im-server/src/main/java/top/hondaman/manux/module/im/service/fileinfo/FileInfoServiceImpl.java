package top.hondaman.manux.module.im.service.fileinfo;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import top.hondaman.manux.framework.security.core.util.SecurityFrameworkUtils;
import top.hondaman.manux.module.im.controller.admin.fileinfo.vo.UploadImageVO;
import top.hondaman.manux.module.im.dal.dataobject.fileinfo.FileInfoDO;
import top.hondaman.manux.module.im.dal.mysql.fileinfo.FileInfoMapper;
import top.hondaman.manux.module.im.enums.IMPlatformConstant;
import top.hondaman.manux.module.im.enums.FileType;
import top.hondaman.manux.module.im.framework.im.config.IMFileProperties;
import top.hondaman.manux.module.im.util.FileUtil;
import top.hondaman.manux.module.im.util.ImageUtil;
import top.hondaman.manux.module.infra.api.file.FileApi;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static top.hondaman.manux.framework.common.exception.util.ServiceExceptionUtil.exception;
import static top.hondaman.manux.module.im.enums.ErrorCodeConstants.*;

/**
 * 文件上传服务
 *
 * author: Blue date: 2024-09-28 version: 1.0
 */
@Slf4j
@Service
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfoDO> implements FileInfoService {
    @Resource
    private FileApi fileApi;
    @Resource
    private IMFileProperties imFileProperties;
    @Resource
    private FileInfoMapper fileInfoMapper;

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            Long userId = SecurityFrameworkUtils.getLoginImUserId();
            // 文件名长度校验
            checkFileNameLength(file);
            // 大小校验
            if (file.getSize() > IMPlatformConstant.MAX_FILE_SIZE) {
                throw exception(FILE_OVERSIZE);
            }
            // 如果文件已存在，直接复用
            String md5 = DigestUtils.md5DigestAsHex(file.getInputStream());
            FileInfoDO fileInfo = findByMd5(md5, FileType.FILE.code());
            if (!Objects.isNull(fileInfo)) {
                // 更新上传时间
                fileInfo.setUploadTime(LocalDateTime.now());
                this.updateById(fileInfo);
                // 返回
                return fileInfo.getFilePath();
            }
            // 上传
            String filePath = fileApi.createFile(
                    file.getBytes(),
                    file.getOriginalFilename(),
                    getBucketPath(FileType.FILE) + "/" + UUID.randomUUID().toString(),
                    null
            );
            // 保存文件
            saveFileInfo(file, md5, filePath);
            log.info("文件文件成功，用户id:{},url:{}", userId, filePath);
            return filePath;
        } catch (IOException e) {
            log.error("上传图片失败，{}", e.getMessage(), e);
            throw exception(FILE_UPLOAD_ERROR);
        }
    }

    @Transactional
    @Override
    public UploadImageVO uploadImage(MultipartFile file, Boolean isPermanent, Long thumbSize) {
        try {
            Long userId = SecurityFrameworkUtils.getLoginImUserId();
            // 文件名长度校验
            checkFileNameLength(file);
            // 大小校验
            if (file.getSize() > IMPlatformConstant.MAX_IMAGE_SIZE) {
                throw exception(FILE_OVERSIZE);
            }
            // 图片格式校验
            if (!FileUtil.isImage(file.getOriginalFilename())) {
                throw exception(IMG_FORMAT_INVALID);
            }
            UploadImageVO vo = new UploadImageVO();
            // 获取图片长度和宽度
            BufferedImage bufferedImage =  ImageIO.read(file.getInputStream());
            if(!Objects.isNull(bufferedImage)){
                vo.setWidth(bufferedImage.getWidth());
                vo.setHeight(bufferedImage.getHeight());
            }
            // 如果文件已存在，直接复用
            String md5 = DigestUtils.md5DigestAsHex(file.getInputStream());
            FileInfoDO fileInfo = findByMd5(md5, FileType.IMAGE.code());
            if (!Objects.isNull(fileInfo)) {
                // 更新上传时间和持久化标记
                fileInfo.setIsPermanent(isPermanent || fileInfo.getIsPermanent());
                fileInfo.setUploadTime(LocalDateTime.now());
                this.updateById(fileInfo);
                // 返回
                vo.setOriginUrl(fileInfo.getFilePath());
                vo.setThumbUrl(fileInfo.getCompressedPath());
                return vo;
            }
            // 上传原图
            String originUrl = fileApi.createFile(
                    file.getBytes(),
                    file.getOriginalFilename(),
                    getBucketPath(FileType.IMAGE) + "/" + UUID.randomUUID().toString(),
                    null
            );
            vo.setOriginUrl(originUrl);
            if (file.getSize() > thumbSize * 1024) {
                // 大于50K的文件需上传缩略图
                byte[] imageByte = ImageUtil.compressForScale(file.getBytes(), thumbSize);
                String thumbUrl = fileApi.createFile(
                        imageByte,
                        file.getOriginalFilename(),
                        getBucketPath(FileType.IMAGE) + "/" + UUID.randomUUID().toString(),
                        null
                );
                vo.setThumbUrl(thumbUrl);
                // 保存文件信息
                saveImageFileInfo(file, md5, vo.getOriginUrl(), vo.getThumbUrl(), isPermanent);
            } else {
                // 小于50k，用原图充当缩略图
                vo.setThumbUrl(originUrl);
                // 保存文件信息,由于缩略图不允许删除，此时原图也不允许删除
                saveImageFileInfo(file, md5, vo.getOriginUrl(), vo.getThumbUrl(), true);
            }
            log.info("文件图片成功，用户id:{},url:{}", userId, vo.getOriginUrl());
            return vo;
        } catch (IOException e) {
            log.error("上传图片失败，{}", e.getMessage(), e);
            throw exception(FILE_UPLOAD_ERROR);
        }
    }

    private String getBucketPath(FileType fileType) {
        return switch (fileType) {
            case FILE -> imFileProperties.getFilePath();
            case IMAGE -> imFileProperties.getImagePath();
            case VIDEO -> imFileProperties.getVideoPath();
        };
    }

    private FileInfoDO findByMd5(String md5, Integer fileType) {
        LambdaQueryWrapper<FileInfoDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(FileInfoDO::getMd5, md5);
        wrapper.eq(FileInfoDO::getFileType, fileType);
        wrapper.last("limit 1");
        return getOne(wrapper);
    }

    private void saveImageFileInfo(MultipartFile file, String md5, String filePath, String compressedPath,
                                   Boolean isPermanent) throws IOException {
        FileInfoDO fileInfo = new FileInfoDO();
        fileInfo.setFileName(file.getOriginalFilename());
        fileInfo.setFileSize(file.getSize());
        fileInfo.setFileType(FileType.IMAGE.code());
        fileInfo.setFilePath(filePath);
        fileInfo.setCompressedPath(compressedPath);
        fileInfo.setMd5(md5);
        fileInfo.setIsPermanent(isPermanent);
        fileInfo.setUploadTime(LocalDateTime.now());
        fileInfoMapper.insert(fileInfo);
    }

    private void saveFileInfo(MultipartFile file, String md5, String filePath) throws IOException {
        FileInfoDO fileInfo = new FileInfoDO();
        fileInfo.setFileName(file.getOriginalFilename());
        fileInfo.setFileSize(file.getSize());
        fileInfo.setFileType(FileType.FILE.code());
        fileInfo.setFilePath(filePath);
        fileInfo.setMd5(md5);
        fileInfo.setIsPermanent(false);
        fileInfo.setUploadTime(LocalDateTime.now());
        fileInfoMapper.insert(fileInfo);
    }

    private void checkFileNameLength(MultipartFile file){
        if(file.getOriginalFilename().length() > IMPlatformConstant.MAX_FILE_NAME_LENGTH){
            throw exception(FILE_NAME_OVER_LONG, IMPlatformConstant.MAX_FILE_NAME_LENGTH);
        }
    }
}
