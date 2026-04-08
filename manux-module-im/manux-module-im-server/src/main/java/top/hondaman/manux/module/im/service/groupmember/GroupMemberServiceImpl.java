package top.hondaman.manux.module.im.service.groupmember;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.hondaman.manux.framework.common.util.date.LocalDateTimeUtils;
import top.hondaman.manux.module.im.dal.dataobject.groupmember.GroupMemberDO;
import top.hondaman.manux.module.im.dal.mysql.groupmember.GroupMemberMapper;
import top.hondaman.manux.module.im.enums.IMPlatformRedisKey;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = IMPlatformRedisKey.IM_CACHE_GROUP_MEMBER_ID)
public class GroupMemberServiceImpl extends ServiceImpl<GroupMemberMapper, GroupMemberDO> implements GroupMemberService {
    @CacheEvict(key = "#member.getGroupId()")
    @Override
    public boolean save(GroupMemberDO member) {
        return super.save(member);
    }

    @CacheEvict(key = "#groupId")
    @Override
    public boolean saveOrUpdateBatch(Long groupId, List<GroupMemberDO> members) {
        return super.saveOrUpdateBatch(members);
    }

    @Override
    public GroupMemberDO findByGroupAndUserId(Long groupId, Long userId) {
        LambdaQueryWrapper<GroupMemberDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(GroupMemberDO::getGroupId, groupId);
        wrapper.eq(GroupMemberDO::getUserId, userId);
        return this.getOne(wrapper);
    }


    @Override
    public List<GroupMemberDO> findByUserId(Long userId) {
        LambdaQueryWrapper<GroupMemberDO> memberWrapper = Wrappers.lambdaQuery();
        memberWrapper.eq(GroupMemberDO::getUserId, userId).eq(GroupMemberDO::getQuit, false);
        return this.list(memberWrapper);
    }

    @Override
    public List<GroupMemberDO> findQuitInMonth(Long userId) {
        LocalDateTime monthTime = LocalDateTime.now().minusMonths(1);
        LambdaQueryWrapper<GroupMemberDO> memberWrapper = Wrappers.lambdaQuery();
        memberWrapper.eq(GroupMemberDO::getUserId, userId).eq(GroupMemberDO::getQuit, true)
            .ge(GroupMemberDO::getQuitTime, monthTime);
        return this.list(memberWrapper);
    }

    @Override
    public List<GroupMemberDO> findByGroupId(Long groupId) {
        LambdaQueryWrapper<GroupMemberDO> memberWrapper = Wrappers.lambdaQuery();
        memberWrapper.eq(GroupMemberDO::getGroupId, groupId);
        return this.list(memberWrapper);
    }

    @Cacheable(key = "#groupId")
    @Override
    public List<Long> findUserIdsByGroupId(Long groupId) {
        LambdaQueryWrapper<GroupMemberDO> memberWrapper = Wrappers.lambdaQuery();
        memberWrapper.eq(GroupMemberDO::getGroupId, groupId).eq(GroupMemberDO::getQuit, false)
            .select(GroupMemberDO::getUserId);
        List<GroupMemberDO> members = this.list(memberWrapper);
        return members.stream().map(GroupMemberDO::getUserId).collect(Collectors.toList());
    }

    @CacheEvict(key = "#groupId")
    @Override
    public void removeByGroupId(Long groupId) {
        LambdaUpdateWrapper<GroupMemberDO> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(GroupMemberDO::getGroupId, groupId).set(GroupMemberDO::getQuit, true)
            .set(GroupMemberDO::getQuitTime, new Date());
        this.update(wrapper);
    }

    @CacheEvict(key = "#groupId")
    @Override
    public void removeByGroupAndUserId(Long groupId, Long userId) {
        LambdaUpdateWrapper<GroupMemberDO> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(GroupMemberDO::getGroupId, groupId);
        wrapper.eq(GroupMemberDO::getUserId, userId);
        wrapper.set(GroupMemberDO::getQuit, true);
        wrapper.set(GroupMemberDO::getQuitTime, new Date());
        this.update(wrapper);
    }

    @CacheEvict(key = "#groupId")
    @Override
    public void removeByGroupAndUserIds(Long groupId, List<Long> userId) {
        LambdaUpdateWrapper<GroupMemberDO> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(GroupMemberDO::getGroupId, groupId);
        wrapper.in(GroupMemberDO::getUserId, userId);
        wrapper.set(GroupMemberDO::getQuit, true);
        wrapper.set(GroupMemberDO::getQuitTime, new Date());
        this.update(wrapper);
    }


    @Override
    public Boolean isInGroup(Long groupId, List<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return true;
        }
        LambdaQueryWrapper<GroupMemberDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(GroupMemberDO::getGroupId, groupId);
        wrapper.eq(GroupMemberDO::getQuit, false);
        wrapper.in(GroupMemberDO::getUserId, userIds);
        return userIds.size() == this.count(wrapper);
    }

    @Override
    public void setDnd(Long groupId, Long userId, Boolean isDnd) {
        LambdaUpdateWrapper<GroupMemberDO> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(GroupMemberDO::getGroupId, groupId);
        wrapper.eq(GroupMemberDO::getUserId, userId);
        wrapper.set(GroupMemberDO::getIsDnd, isDnd);
        this.update(wrapper);
    }
}
