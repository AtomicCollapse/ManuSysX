package top.hondaman.manux.module.im.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import top.hondaman.manux.module.im.api.user.dto.RegisterDTO;
import top.hondaman.manux.module.im.controller.admin.user.vo.IMUserVO;
import top.hondaman.manux.module.im.controller.admin.user.vo.OnlineTerminalVO;
import top.hondaman.manux.module.im.dal.dataobject.user.IMUserDO;

import java.util.List;

public interface UserService extends IService<IMUserDO> {


    /**
     * 用户注册
     *
     * @param dto 注册dto
     */
    void register(RegisterDTO dto);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    IMUserDO findUserByUserName(String username);

    /**
     * 更新用户信息，好友昵称和群聊昵称等冗余信息也会更新
     *
     * @param vo 用户信息vo
     */
    void update(IMUserVO vo);

    /**
     * 根据用户昵id查询用户以及在线状态
     *
     * @param id 用户id
     * @return 用户信息
     */
    IMUserVO findUserById(Long id);

    /**
     * 根据用户昵称查询用户，最多返回20条数据
     *
     * @param name 用户名或昵称
     * @return 用户列表
     */
    List<IMUserVO> findUserByName(String name);

    /**
     * 获取用户在线的终端类型
     *
     * @param userIds 用户id，多个用‘,’分割
     * @return 在线用户终端
     */
    List<OnlineTerminalVO> getOnlineTerminals(String userIds);


}
