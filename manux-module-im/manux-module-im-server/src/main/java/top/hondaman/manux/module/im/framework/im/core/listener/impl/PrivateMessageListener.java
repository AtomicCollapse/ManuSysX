package top.hondaman.manux.module.im.framework.im.core.listener.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import top.hondaman.manux.module.im.controller.admin.privatemessage.vo.PrivateMessageVO;
import top.hondaman.manux.module.im.dal.dataobject.privatemessage.PrivateMessageDO;
import top.hondaman.manux.module.im.enums.IMListenerType;
import top.hondaman.manux.module.im.enums.IMSendCode;
import top.hondaman.manux.module.im.enums.MessageStatus;
import top.hondaman.manux.module.im.framework.im.annotation.IMListener;
import top.hondaman.manux.module.im.framework.im.core.listener.MessageListener;
import top.hondaman.manux.module.im.framework.im.model.IMSendResult;
import top.hondaman.manux.module.im.service.privatemessage.PrivateMessageService;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@IMListener(type = IMListenerType.PRIVATE_MESSAGE)
public class PrivateMessageListener implements MessageListener<PrivateMessageVO> {
    @Lazy
    @Autowired
    private PrivateMessageService privateMessageService;

    @Override
    public void process(List<IMSendResult<PrivateMessageVO>> results) {
        Set<Long> messageIds = new HashSet<>();
        for (IMSendResult<PrivateMessageVO> result : results) {
            PrivateMessageVO messageInfo = result.getData();
            // 更新消息状态,这里只处理成功消息，失败的消息继续保持未读状态
            if (result.getCode().equals(IMSendCode.SUCCESS.code()) && !Objects.isNull(messageInfo.getId())) {
                if (result.getReceiver().getId().equals(messageInfo.getRecvId())) {
                    messageIds.add(messageInfo.getId());
                    log.info("消息送达，消息id:{}，发送者:{},接收者:{},终端:{}", messageInfo.getId(),
                        result.getSender().getId(), result.getReceiver().getId(), result.getReceiver().getTerminal());
                }
            }
        }
        // 批量修改状态
        if (CollUtil.isNotEmpty(messageIds)) {
            UpdateWrapper<PrivateMessageDO> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().in(PrivateMessageDO::getId, messageIds)
                .eq(PrivateMessageDO::getStatus, MessageStatus.PENDING.code())
                .set(PrivateMessageDO::getStatus, MessageStatus.DELIVERED.code());
            privateMessageService.update(updateWrapper);
        }
    }
}
