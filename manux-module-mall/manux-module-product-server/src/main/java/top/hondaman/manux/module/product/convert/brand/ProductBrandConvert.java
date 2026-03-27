package top.hondaman.manux.module.product.convert.brand;

import top.hondaman.manux.framework.common.pojo.PageResult;
import top.hondaman.manux.module.product.controller.admin.brand.vo.ProductBrandCreateReqVO;
import top.hondaman.manux.module.product.controller.admin.brand.vo.ProductBrandRespVO;
import top.hondaman.manux.module.product.controller.admin.brand.vo.ProductBrandSimpleRespVO;
import top.hondaman.manux.module.product.controller.admin.brand.vo.ProductBrandUpdateReqVO;
import top.hondaman.manux.module.product.dal.dataobject.brand.ProductBrandDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 品牌 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface ProductBrandConvert {

    ProductBrandConvert INSTANCE = Mappers.getMapper(ProductBrandConvert.class);

    ProductBrandDO convert(ProductBrandCreateReqVO bean);

    ProductBrandDO convert(ProductBrandUpdateReqVO bean);

    ProductBrandRespVO convert(ProductBrandDO bean);

    List<ProductBrandSimpleRespVO> convertList1(List<ProductBrandDO> list);

    List<ProductBrandRespVO> convertList(List<ProductBrandDO> list);

    PageResult<ProductBrandRespVO> convertPage(PageResult<ProductBrandDO> page);

}
