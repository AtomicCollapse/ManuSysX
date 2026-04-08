package top.hondaman.manux.module.im.framework.im.core.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.hondaman.manux.module.im.enums.IMListenerType;
import top.hondaman.manux.module.im.enums.IMRedisKey;
import top.hondaman.manux.module.im.framework.im.core.listener.MessageListenerMulticaster;
import top.hondaman.manux.module.im.framework.im.model.IMSendResult;
import top.hondaman.manux.module.im.framework.redis.mq.core.RedisMQListener;

import java.util.List;


@Component
@RequiredArgsConstructor
@RedisMQListener(queue = IMRedisKey.IM_RESULT_PRIVATE_QUEUE, batchSize = 100)
public class PrivateMessageResultResultTask extends AbstractMessageResultTask<IMSendResult> {

    private final MessageListenerMulticaster listenerMulticaster;

    @Override
    public void onMessage(List<IMSendResult> results) {
        listenerMulticaster.multicast(IMListenerType.PRIVATE_MESSAGE, results);
    }

}
