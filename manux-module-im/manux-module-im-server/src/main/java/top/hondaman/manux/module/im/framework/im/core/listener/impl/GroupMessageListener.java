package top.hondaman.manux.module.im.framework.im.core.listener.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import top.hondaman.manux.module.im.controller.admin.groupmessage.vo.GroupMessageVO;
import top.hondaman.manux.module.im.enums.IMListenerType;
import top.hondaman.manux.module.im.enums.IMSendCode;
import top.hondaman.manux.module.im.framework.im.annotation.IMListener;
import top.hondaman.manux.module.im.framework.im.core.listener.MessageListener;
import top.hondaman.manux.module.im.framework.im.model.IMSendResult;

import java.util.List;

@Slf4j
@IMListener(type = IMListenerType.GROUP_MESSAGE)
@AllArgsConstructor
public class GroupMessageListener implements MessageListener<GroupMessageVO> {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void process(List<IMSendResult<GroupMessageVO>> results) {
        for(IMSendResult<GroupMessageVO> result:results){
            GroupMessageVO messageInfo = result.getData();
            if (result.getCode().equals(IMSendCode.SUCCESS.code())) {
                log.info("消息送达，消息id:{}，发送者:{},接收者:{},终端:{}", messageInfo.getId(), result.getSender().getId(), result.getReceiver().getId(), result.getReceiver().getTerminal());
            }
        }
    }

}
