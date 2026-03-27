package top.hondaman.manux.module.promotion.service.coupon;

import top.hondaman.manux.framework.common.enums.CommonStatusEnum;
import top.hondaman.manux.framework.common.pojo.PageResult;
import top.hondaman.manux.module.product.api.category.ProductCategoryApi;
import top.hondaman.manux.module.product.api.spu.ProductSpuApi;
import top.hondaman.manux.module.promotion.controller.admin.coupon.vo.template.CouponTemplateCreateReqVO;
import top.hondaman.manux.module.promotion.controller.admin.coupon.vo.template.CouponTemplatePageReqVO;
import top.hondaman.manux.module.promotion.controller.admin.coupon.vo.template.CouponTemplateUpdateReqVO;
import top.hondaman.manux.module.promotion.convert.coupon.CouponTemplateConvert;
import top.hondaman.manux.module.promotion.dal.dataobject.coupon.CouponTemplateDO;
import top.hondaman.manux.module.promotion.dal.mysql.coupon.CouponTemplateMapper;
import top.hondaman.manux.module.promotion.enums.common.PromotionProductScopeEnum;
import top.hondaman.manux.module.promotion.enums.coupon.CouponTakeTypeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static top.hondaman.manux.framework.common.exception.util.ServiceExceptionUtil.exception;
import static top.hondaman.manux.module.promotion.enums.ErrorCodeConstants.*;

/**
 * 优惠劵模板 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class CouponTemplateServiceImpl implements CouponTemplateService {

    @Resource
    private CouponTemplateMapper couponTemplateMapper;

    @Resource
    private ProductCategoryApi productCategoryApi;
    @Resource
    private ProductSpuApi productSpuApi;

    @Override
    public boolean isTakeLimitCountUnlimited(Integer takeLimitCount) {
        return CouponTemplateDO.TAKE_LIMIT_COUNT_MAX.equals(takeLimitCount);
    }

    @Override
    public boolean isTotalCountUnlimited(Integer totalCount) {
        return CouponTemplateDO.TOTAL_COUNT_MAX.equals(totalCount);
    }

    @Override
    public Long createCouponTemplate(CouponTemplateCreateReqVO createReqVO) {
        // 校验商品范围
        validateProductScope(createReqVO.getProductScope(), createReqVO.getProductScopeValues());
        // 插入
        CouponTemplateDO couponTemplate = CouponTemplateConvert.INSTANCE.convert(createReqVO)
                .setStatus(CommonStatusEnum.ENABLE.getStatus());
        couponTemplateMapper.insert(couponTemplate);
        // 返回
        return couponTemplate.getId();
    }

    @Override
    public void updateCouponTemplate(CouponTemplateUpdateReqVO updateReqVO) {
        // 校验存在
        CouponTemplateDO couponTemplate = validateCouponTemplateExists(updateReqVO.getId());
        // 校验发放数量不能过小（仅在 CouponTakeTypeEnum.USER 用户领取时）
        if (CouponTakeTypeEnum.isUser(couponTemplate.getTakeType())
                && !isTotalCountUnlimited(updateReqVO.getTotalCount()) // 非不限制总发放数量
                && updateReqVO.getTotalCount() < couponTemplate.getTakeCount()) {
            throw exception(COUPON_TEMPLATE_TOTAL_COUNT_TOO_SMALL, couponTemplate.getTakeCount());
        }
        // 校验商品范围
        validateProductScope(updateReqVO.getProductScope(), updateReqVO.getProductScopeValues());

        // 更新
        CouponTemplateDO updateObj = CouponTemplateConvert.INSTANCE.convert(updateReqVO);
        couponTemplateMapper.updateById(updateObj);
    }

    @Override
    public void updateCouponTemplateStatus(Long id, Integer status) {
        // 校验存在
        validateCouponTemplateExists(id);
        // 更新
        couponTemplateMapper.updateById(new CouponTemplateDO().setId(id).setStatus(status));
    }

    @Override
    public void deleteCouponTemplate(Long id) {
        // 校验存在
        validateCouponTemplateExists(id);
        // 删除
        couponTemplateMapper.deleteById(id);
    }

    private CouponTemplateDO validateCouponTemplateExists(Long id) {
        CouponTemplateDO couponTemplate = couponTemplateMapper.selectById(id);
        if (couponTemplate == null) {
            throw exception(COUPON_TEMPLATE_NOT_EXISTS);
        }
        return couponTemplate;
    }

    private void validateProductScope(Integer productScope, List<Long> productScopeValues) {
        if (Objects.equals(PromotionProductScopeEnum.SPU.getScope(), productScope)) {
            productSpuApi.validateSpuList(productScopeValues).checkError();
        } else if (Objects.equals(PromotionProductScopeEnum.CATEGORY.getScope(), productScope)) {
            productCategoryApi.validateCategoryList(productScopeValues).checkError();
        }
    }

    @Override
    public CouponTemplateDO getCouponTemplate(Long id) {
        return couponTemplateMapper.selectById(id);
    }

    @Override
    public PageResult<CouponTemplateDO> getCouponTemplatePage(CouponTemplatePageReqVO pageReqVO) {
        return couponTemplateMapper.selectPage(pageReqVO);
    }

    @Override
    public void updateCouponTemplateTakeCount(Long id, int incrCount) {
        int updateCount = couponTemplateMapper.updateTakeCount(id, incrCount);
        if (updateCount == 0) {
            throw exception(COUPON_TEMPLATE_NOT_ENOUGH);
        }
    }

    @Override
    public List<CouponTemplateDO> getCouponTemplateListByTakeType(CouponTakeTypeEnum takeType) {
        return couponTemplateMapper.selectListByTakeType(takeType.getType());
    }

    @Override
    public List<CouponTemplateDO> getCouponTemplateList(List<Integer> canTakeTypes, Integer productScope,
                                                        Long productScopeValue, Integer count) {
        return couponTemplateMapper.selectList(canTakeTypes, productScope, productScopeValue, count);
    }

    @Override
    public List<CouponTemplateDO> getCouponTemplateList(Collection<Long> ids) {
        return couponTemplateMapper.selectByIds(ids);
    }

}
