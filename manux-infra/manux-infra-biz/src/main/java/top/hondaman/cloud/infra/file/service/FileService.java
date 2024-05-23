package top.hondaman.cloud.infra.file.service;


public interface FileService {

    /**
     * 上传文件，返回文件的访问路径
     * @param name
     * @param path
     * @param content
     * @return
     */
    String uploadFile(String name,String path,byte[] content);
}
