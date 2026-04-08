package top.hondaman.manux.module.im.enums;

public interface IMPlatformConstant {

    /**
     * 系统用户id
     */
    Long SYS_USER_ID = 0L;
    /**
     * 最大图片上传大小
     */
    Long MAX_IMAGE_SIZE = 20 * 1024 * 1024L;
    /**
     * 最大上传文件大小
     */
    Long MAX_FILE_SIZE = 20 * 1024 * 1024L;

    /**
     * 最大文件名长度
     */
    Long MAX_FILE_NAME_LENGTH = 128L;

    /**
     * 大群人数上限
     */
    Long MAX_LARGE_GROUP_MEMBER = 3000L;

    /**
     * 普通群人数上限
     */
    Long MAX_NORMAL_GROUP_MEMBER = 500L;

}
