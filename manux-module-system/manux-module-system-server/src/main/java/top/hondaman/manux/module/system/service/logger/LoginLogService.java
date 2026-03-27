package top.hondaman.manux.module.system.service.logger;

import top.hondaman.manux.framework.common.pojo.PageResult;
import top.hondaman.manux.module.system.api.logger.dto.LoginLogCreateReqDTO;
import top.hondaman.manux.module.system.controller.admin.logger.vo.loginlog.LoginLogPageReqVO;
import top.hondaman.manux.module.system.dal.dataobject.logger.LoginLogDO;

import jakarta.validation.Valid;

/**
 * 登录日志 Service 接口
 */
public interface LoginLogService {

    /**
     * 获得登录日志
     *
     * @param id 编号
     * @return 登录日志
     */
    LoginLogDO getLoginLog(Long id);

    /**
     * 获得登录日志分页
     *
     * @param pageReqVO 分页条件
     * @return 登录日志分页
     */
    PageResult<LoginLogDO> getLoginLogPage(LoginLogPageReqVO pageReqVO);

    /**
     * 创建登录日志
     *
     * @param reqDTO 日志信息
     */
    void createLoginLog(@Valid LoginLogCreateReqDTO reqDTO);

}
