package top.hondaman.manux.module.im.enums;

import top.hondaman.manux.framework.common.exception.ErrorCode;

/**
 * IM 错误码枚举类
 * <p>
 * erp 系统，使用 1-060-000-000 段
 */
public interface ErrorCodeConstants {
    ErrorCode IM_OFFLINE = new ErrorCode(1_060_000_000, "您当前的网络连接已断开,请稍后重试");
    ErrorCode REPEAT_SUBMIT = new ErrorCode(1_060_000_001, "您当前的网络连接已断开,请稍后重试");
    ErrorCode FILE_OVERSIZE = new ErrorCode(1_060_000_002,"文件大小不能超过20M");
    ErrorCode FILE_UPLOAD_ERROR = new ErrorCode(1_060_000_003,"文件上传失败");
    ErrorCode IMG_FORMAT_INVALID = new ErrorCode(1_060_000_004,"图片格式不合法");
    ErrorCode FILE_NAME_OVER_LONG = new ErrorCode(1_060_000_005,"文件名长度不能超过{}");

    // ========== IM应用配置 1-023-001-000 ==========
    ErrorCode APP_NOT_EXISTS = new ErrorCode(1_060_001_000, "IM应用配置不存在");
    // ========== IM用户 1-023-002-000 ==========
    ErrorCode USER_NOT_EXISTS = new ErrorCode(1_060_002_000, "IM用户不存在");
    ErrorCode OLD_PWD_WRONG = new ErrorCode(1_060_002_001, "旧密码不正确");
    ErrorCode NICKNAME_CONTAINS_SENSITIVE_CHARACTERS = new ErrorCode(1_060_002_002, "昵称包含敏感字符");
    ErrorCode NAME_CONTAINS_SENSITIVE_CHARACTERS = new ErrorCode(1_060_002_003, "用户名包含敏感字符");
    ErrorCode CANT_CHANGE_OTHER_USER = new ErrorCode(1_060_002_004, "不允许修改其他用户的信息");
    ErrorCode USERNAME_ALREADY_REGISTER = new ErrorCode(1_060_002_005,"用户名已被注册");

    // ========== IM敏感词 1-023-003-000 ==========
    ErrorCode SENSITIVE_WORD_NOT_EXISTS = new ErrorCode(1_060_003_000, "IM敏感词不存在");
    // ========== IM文件 1-023-004-000 ==========
    ErrorCode FILE_INFO_NOT_EXISTS = new ErrorCode(1_060_004_000, "IM文件不存在");
    // ========== IM好友 1-023-005-000 ==========
    ErrorCode FRIEND_NOT_EXISTS = new ErrorCode(1_060_005_000, "IM好友不存在");
    ErrorCode ADD_SELF_FRIEND = new ErrorCode(1_060_005_001,"不允许添加自己为好友");
    ErrorCode NOT_YOUR_FRIEND = new ErrorCode(1_060_005_002,"对方不是您的好友");

    // ========== IM群聊 1-023-006-000 ==========
    ErrorCode GROUP_NOT_EXISTS = new ErrorCode(1_060_006_000, "IM群聊不存在");
    ErrorCode NOT_JOINED_GROUP = new ErrorCode(1_060_006_001,"您未加入群聊");
    ErrorCode NOT_MEMBER_OF_GROUP = new ErrorCode(1_060_006_002,"您不是群聊的成员");
    ErrorCode GROUP_OWNER_QUITE = new ErrorCode(1_060_006_003,"您是群主，不可退出群聊");
    ErrorCode GROUP_NOT_OWNER_KICK = new ErrorCode(1_060_006_004,"您不是群主，没有权限踢人");
    ErrorCode GROUP_KICK_SELF = new ErrorCode(1_060_006_005,"不允许移除自己");
    ErrorCode GROUP_REMOVE_OWNER = new ErrorCode(1_060_006_006,"不允许移除群主");
    ErrorCode NOT_IN_GROUP_INVITE = new ErrorCode(1_060_006_007,"您不在群聊中,邀请失败");
    ErrorCode GROUP_MAX_OVER = new ErrorCode(1_060_006_008,"群聊人数不能大于 {} 人");
    ErrorCode PART_NOT_IN_GROUP_INVITE = new ErrorCode(1_060_006_009,"部分用户不是您的好友，邀请失败");
    ErrorCode GROUP_DISSOLVED = new ErrorCode(1_060_006_010,"群组 {} 已解散");
    ErrorCode GROUP_IS_BANNED = new ErrorCode(1_060_006_010,"群组 {} 已被封禁,原因: {}");
    ErrorCode NOT_GROUP_OWNER_DELETE = new ErrorCode(1_060_006_011,"只有群主才有权限解除群聊");
    ErrorCode NO_PERMISSION = new ErrorCode(1_060_006_012,"您没有权限");

    // ========== IM群成员 1-023-007-000 ==========
    ErrorCode GROUP_MEMBER_NOT_EXISTS = new ErrorCode(1_060_007_000, "IM群成员不存在");

    // ========== IM群消息 1-023-008-000 ==========
    ErrorCode GROUP_MESSAGE_NOT_EXISTS = new ErrorCode(1_060_008_000, "IM群消息不存在");
    ErrorCode NOT_IN_GROUP = new ErrorCode(1_060_008_001, "您已不在群聊中");
    ErrorCode GROUP_SIZE_OVER_RECEIPT = new ErrorCode(1_060_008_002, "当前群聊大于 {} 人,不支持发送回执消息");
    ErrorCode GROUP_MESSAGE_RECALL_NOT_SELF = new ErrorCode(1_060_008_003, "这条消息不是由您发送,无法撤回");
    ErrorCode GROUP_MESSAGE_RECALL_OVER_TIME = new ErrorCode(1_060_008_004, "消息已发送超过5分钟，无法撤回");
    ErrorCode GROUP_MESSAGE_RECALL_NOT_IN_GROUP = new ErrorCode(1_060_008_005, "您已不在群聊里面，无法撤回消息");
    ErrorCode GROUP_MESSAGE_PULL_OFF_LINE = new ErrorCode(1_060_008_006, "网络连接失败，无法拉取离线消息");



    // ========== IM私聊消息 1-023-009-000 ==========
    ErrorCode PRIVATE_MESSAGE_NOT_EXISTS = new ErrorCode(1_060_009_000, "IM私聊消息不存在");
    ErrorCode NOT_FRIEND = new ErrorCode(1_060_009_001,"您已不是对方好友，无法发送消息");
    ErrorCode PRIVATE_MESSAGE_RECALL_OVER_TIME = new ErrorCode(1_060_009_002, "消息已发送超过5分钟，无法撤回");
    ErrorCode PRIVATE_MESSAGE_RECALL_NOT_SELF = new ErrorCode(1_060_008_003, "这条消息不是由您发送,无法撤回");
}
