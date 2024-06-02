package top.hondaman.cloud.infra.asyncImport.controller;

import top.hondaman.cloud.framework.common.pojo.CommonResult;
import top.hondaman.cloud.infra.asyncImport.dto.ImportTaskParam;

import java.io.IOException;

public interface AsyncImportApi {

    /**
     * 根据 systemCode+taskCode 查询最新一条导入任务
     * @param param
     * @return
     */
    CommonResult getLastTaskByCode(ImportTaskParam param);

    /**
     *  新增一条导入任务
     * @param param
     * @return
     */
    CommonResult insertTask(ImportTaskParam param,String userId) throws IOException;
}
