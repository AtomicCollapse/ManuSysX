package top.hondaman.manux.module.promotion.convert.banner;

import top.hondaman.manux.framework.common.pojo.PageResult;
import top.hondaman.manux.module.promotion.controller.admin.banner.vo.BannerCreateReqVO;
import top.hondaman.manux.module.promotion.controller.admin.banner.vo.BannerRespVO;
import top.hondaman.manux.module.promotion.controller.admin.banner.vo.BannerUpdateReqVO;
import top.hondaman.manux.module.promotion.controller.app.banner.vo.AppBannerRespVO;
import top.hondaman.manux.module.promotion.dal.dataobject.banner.BannerDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BannerConvert {

    BannerConvert INSTANCE = Mappers.getMapper(BannerConvert.class);

    List<BannerRespVO> convertList(List<BannerDO> list);

    PageResult<BannerRespVO> convertPage(PageResult<BannerDO> pageResult);

    BannerRespVO convert(BannerDO banner);

    BannerDO convert(BannerCreateReqVO createReqVO);

    BannerDO convert(BannerUpdateReqVO updateReqVO);

    List<AppBannerRespVO> convertList01(List<BannerDO> bannerList);

}
