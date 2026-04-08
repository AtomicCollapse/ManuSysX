package top.hondaman.manux.module.im.framework.im.core.listener.impl;

import lombok.extern.slf4j.Slf4j;
import top.hondaman.manux.module.im.controller.admin.systemmessage.vo.SystemMessageVO;
import top.hondaman.manux.module.im.enums.IMListenerType;
import top.hondaman.manux.module.im.enums.IMSendCode;
import top.hondaman.manux.module.im.framework.im.annotation.IMListener;
import top.hondaman.manux.module.im.framework.im.core.listener.MessageListener;
import top.hondaman.manux.module.im.framework.im.model.IMSendResult;

import java.util.List;

@Slf4j
@IMListener(type = IMListenerType.SYSTEM_MESSAGE)
public class SystemMessageListener implements MessageListener<SystemMessageVO> {

    @Override
    public void process(List<IMSendResult<SystemMessageVO>> results) {
        for(IMSendResult<SystemMessageVO> result : results){
            SystemMessageVO messageInfo = result.getData();
            if (result.getCode().equals(IMSendCode.SUCCESS.code())) {
                log.info("消息送达，消息id:{},接收者:{},终端:{}", messageInfo.getId(), result.getReceiver().getId(), result.getReceiver().getTerminal());
            }
        }
    }
}
