package top.hondaman.manux.module.im.service.fileinfo;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import top.hondaman.manux.module.im.controller.admin.fileinfo.vo.UploadImageVO;
import top.hondaman.manux.module.im.dal.dataobject.fileinfo.FileInfoDO;

public interface FileInfoService extends IService<FileInfoDO> {

    String uploadFile(MultipartFile file);

    UploadImageVO uploadImage(MultipartFile file, Boolean isPermanent, Long thumbSize);


}
