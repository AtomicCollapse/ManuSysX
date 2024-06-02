package top.hondaman.cloud.infra.file.service;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;
import top.hondaman.cloud.framework.common.util.file.FileTypeUtils;
import top.hondaman.cloud.framework.common.util.file.FileUtils;
import top.hondaman.cloud.infra.framework.file.core.client.local.LocalFileClient;
import top.hondaman.cloud.infra.framework.file.core.client.local.LocalFileClientConfig;

@Service
public class FileServiceImpl implements FileService{

    @Override
    public String uploadFile(String name, String path, byte[] content) {
        // 计算默认的 path 名
        String type = FileTypeUtils.getMineType(content, name);
        if (StrUtil.isEmpty(path)) {
            path = FileUtils.generatePath(content, name);
        }
        // 如果 name 为空，则使用 path 填充
        if (StrUtil.isEmpty(name)) {
            name = path;
        }

        // 上传到文件存储器
        // 创建客户端
        LocalFileClientConfig config = new LocalFileClientConfig();
        config.setDomain("http://127.0.0.1:48080");
        config.setBasePath("D:/fileUpload");
        LocalFileClient client = new LocalFileClient(0L, config);
        client.init();
        // 上传文件
        String fullPath = client.upload(content, path, type);
        System.out.println("访问地址：" + fullPath);
        //client.delete(path);
        return fullPath;

        //TODO 保存到数据库
    }
}
