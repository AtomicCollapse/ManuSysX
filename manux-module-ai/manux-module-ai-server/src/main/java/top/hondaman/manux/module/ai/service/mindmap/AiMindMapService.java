package top.hondaman.manux.module.ai.service.mindmap;

import top.hondaman.manux.framework.common.pojo.CommonResult;
import top.hondaman.manux.framework.common.pojo.PageResult;
import top.hondaman.manux.module.ai.controller.admin.mindmap.vo.AiMindMapGenerateReqVO;
import top.hondaman.manux.module.ai.controller.admin.mindmap.vo.AiMindMapPageReqVO;
import top.hondaman.manux.module.ai.dal.dataobject.mindmap.AiMindMapDO;
import reactor.core.publisher.Flux;

/**
 * AI 思维导图 Service 接口
 *
 * @author xiaoxin
 */
public interface AiMindMapService {

    /**
     * 生成思维导图内容
     *
     * @param generateReqVO 请求参数
     * @param userId        用户编号
     * @return 生成结果
     */
    Flux<CommonResult<String>> generateMindMap(AiMindMapGenerateReqVO generateReqVO, Long userId);

    /**
     * 删除思维导图
     *
     * @param id 编号
     */
    void deleteMindMap(Long id);

    /**
     * 获得思维导图分页
     *
     * @param pageReqVO 分页查询
     * @return 思维导图分页
     */
    PageResult<AiMindMapDO> getMindMapPage(AiMindMapPageReqVO pageReqVO);

}
