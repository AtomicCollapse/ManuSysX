package top.hondaman.manux.module.member.controller.app.level;

import top.hondaman.manux.framework.common.pojo.CommonResult;
import top.hondaman.manux.framework.common.pojo.PageParam;
import top.hondaman.manux.framework.common.pojo.PageResult;
import top.hondaman.manux.module.member.controller.app.level.vo.experience.AppMemberExperienceRecordRespVO;
import top.hondaman.manux.module.member.convert.level.MemberExperienceRecordConvert;
import top.hondaman.manux.module.member.dal.dataobject.level.MemberExperienceRecordDO;
import top.hondaman.manux.module.member.service.level.MemberExperienceRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import static top.hondaman.manux.framework.common.pojo.CommonResult.success;
import static top.hondaman.manux.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 App - 会员经验记录")
@RestController
@RequestMapping("/member/experience-record")
@Validated
public class AppMemberExperienceRecordController {

    @Resource
    private MemberExperienceRecordService experienceLogService;

    @GetMapping("/page")
    @Operation(summary = "获得会员经验记录分页")
    public CommonResult<PageResult<AppMemberExperienceRecordRespVO>> getExperienceRecordPage(
            @Valid PageParam pageParam) {
        PageResult<MemberExperienceRecordDO> pageResult = experienceLogService.getExperienceRecordPage(
                getLoginUserId(), pageParam);
        return success(MemberExperienceRecordConvert.INSTANCE.convertPage02(pageResult));
    }

}
