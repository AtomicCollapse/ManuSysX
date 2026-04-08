package top.hondaman.manux.module.im.service.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.hondaman.manux.framework.security.core.util.SecurityFrameworkUtils;
import top.hondaman.manux.module.im.api.user.dto.ModifyPwdDTO;
import top.hondaman.manux.module.im.api.user.dto.RegisterDTO;
import top.hondaman.manux.module.im.controller.admin.user.vo.IMUserVO;
import top.hondaman.manux.module.im.controller.admin.user.vo.OnlineTerminalVO;
import top.hondaman.manux.module.im.dal.dataobject.friend.FriendDO;
import top.hondaman.manux.module.im.dal.dataobject.groupmember.GroupMemberDO;
import top.hondaman.manux.module.im.dal.dataobject.user.IMUserDO;
import top.hondaman.manux.module.im.dal.mysql.user.IMUserMapper;
import top.hondaman.manux.module.im.enums.IMTerminalType;
import top.hondaman.manux.module.im.framework.im.core.IMClient;
import top.hondaman.manux.module.im.service.friend.FriendService;
import top.hondaman.manux.module.im.service.groupmember.GroupMemberService;
import top.hondaman.manux.module.im.util.SensitiveFilterUtil;

import java.util.*;
import java.util.stream.Collectors;

import static top.hondaman.manux.framework.common.exception.util.ServiceExceptionUtil.exception;
import static top.hondaman.manux.module.im.enums.ErrorCodeConstants.*;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<IMUserMapper, IMUserDO> implements UserService {

    @Resource
    private GroupMemberService groupMemberService;
    @Resource
    private FriendService friendService;
    @Resource
    private IMClient imClient;
    @Resource
    private SensitiveFilterUtil sensitiveFilterUtil;

    @Override
    public void register(RegisterDTO dto) {
        // 昵称默认跟用户名保持一致
        if(StrUtil.isEmpty(dto.getNickName())){
            dto.setUserName(dto.getUserName());
        }
        IMUserDO user = this.findUserByUserName(dto.getUserName());
        if(!dto.getUserName().equals(sensitiveFilterUtil.filter(dto.getUserName()))){
            throw exception(NAME_CONTAINS_SENSITIVE_CHARACTERS);
        }
        if(!dto.getNickName().equals(sensitiveFilterUtil.filter(dto.getNickName()))){
            throw exception(NICKNAME_CONTAINS_SENSITIVE_CHARACTERS);
        }
        if (!Objects.isNull(user)) {
            throw exception(USERNAME_ALREADY_REGISTER);
        }
        user = BeanUtil.copyProperties(dto, IMUserDO.class);
        user.setPassword(BCrypt.hashpw(user.getPassword()));
        this.save(user);
        log.info("注册用户，用户id:{},用户名:{},昵称:{}", user.getId(), dto.getUserName(), dto.getNickName());
    }

    @Override
    public IMUserDO findUserByUserName(String username) {
        LambdaQueryWrapper<IMUserDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(IMUserDO::getUserName, username);
        return this.getOne(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(IMUserVO vo) {
        if(!vo.getNickName().equals(sensitiveFilterUtil.filter(vo.getNickName()))){
            throw exception(NICKNAME_CONTAINS_SENSITIVE_CHARACTERS);
        }
        if (!SecurityFrameworkUtils.getLoginImUserId().equals(vo.getId())) {
            throw exception(CANT_CHANGE_OTHER_USER);
        }
        IMUserDO user = this.getById(vo.getId());
        if (Objects.isNull(user)) {
            throw exception(USER_NOT_EXISTS);
        }
        if (!user.getNickName().equals(vo.getNickName()) || !user.getHeadImageThumb().equals(vo.getHeadImageThumb())) {
            // 更新好友昵称和头像
            LambdaUpdateWrapper<FriendDO> wrapper1 = Wrappers.lambdaUpdate();
            wrapper1.eq(FriendDO::getFriendId, SecurityFrameworkUtils.getLoginImUserId());
            wrapper1.set(FriendDO::getFriendNickName,vo.getNickName());
            wrapper1.set(FriendDO::getFriendHeadImage,vo.getHeadImageThumb());
            friendService.update(wrapper1);
            // 更新群聊中的昵称和头像
            LambdaUpdateWrapper<GroupMemberDO> wrapper2 = Wrappers.lambdaUpdate();
            wrapper2.eq(GroupMemberDO::getUserId, SecurityFrameworkUtils.getLoginImUserId());
            wrapper2.set(GroupMemberDO::getHeadImage,vo.getHeadImageThumb());
            wrapper2.set(GroupMemberDO::getUserNickName,vo.getNickName());
            groupMemberService.update(wrapper2);
        }
        // 更新用户信息
        user.setNickName(vo.getNickName());
        user.setSex(vo.getSex());
        user.setSignature(vo.getSignature());
        user.setHeadImage(vo.getHeadImage());
        user.setHeadImageThumb(vo.getHeadImageThumb());
        this.updateById(user);
        log.info("用户信息更新，用户:{}}", user);
    }

    @Override
    public IMUserVO findUserById(Long id) {
        IMUserDO user = this.getById(id);
        IMUserVO vo = BeanUtil.copyProperties(user, IMUserVO.class);
        vo.setOnline(imClient.isOnline(id));
        return vo;
    }

    @Override
    public List<IMUserVO> findUserByName(String name) {
        LambdaQueryWrapper<IMUserDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(IMUserDO::getUserName, name).or().like(IMUserDO::getNickName, name).last("limit 20");
        List<IMUserDO> users = this.list(queryWrapper);
        List<Long> userIds = users.stream().map(IMUserDO::getId).collect(Collectors.toList());
        List<Long> onlineUserIds = imClient.getOnlineUser(userIds);
        return users.stream().map(u -> {
            IMUserVO vo = BeanUtil.copyProperties(u, IMUserVO.class);
            vo.setOnline(onlineUserIds.contains(u.getId()));
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<OnlineTerminalVO> getOnlineTerminals(String userIds) {
        List<Long> userIdList = Arrays.stream(userIds.split(",")).map(Long::parseLong).collect(Collectors.toList());
        // 查询在线的终端
        Map<Long, List<IMTerminalType>> terminalMap = imClient.getOnlineTerminal(userIdList);
        // 组装vo
        List<OnlineTerminalVO> vos = new LinkedList<>();
        terminalMap.forEach((userId, types) -> {
            List<Integer> terminals = types.stream().map(IMTerminalType::code).collect(Collectors.toList());
            vos.add(new OnlineTerminalVO(userId, terminals));
        });
        return vos;
    }
}
