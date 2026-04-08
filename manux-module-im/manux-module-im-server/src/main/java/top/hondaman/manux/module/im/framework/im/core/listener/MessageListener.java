package top.hondaman.manux.module.im.framework.im.core.listener;

import top.hondaman.manux.module.im.framework.im.model.IMSendResult;
import java.util.List;

public interface MessageListener<T> {

     void process(List<IMSendResult<T>> result);

}
