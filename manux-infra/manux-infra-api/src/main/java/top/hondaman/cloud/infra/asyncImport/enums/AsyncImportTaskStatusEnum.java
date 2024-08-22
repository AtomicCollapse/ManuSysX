package top.hondaman.cloud.infra.asyncImport.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.hondaman.cloud.framework.common.core.IntArrayValuable;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum AsyncImportTaskStatusEnum {
    STATUS_CHECK_00("C00","待校验"),
    STATUS_CHECK_A1("CA1","校验中"),
    STATUS_CHECK_S1("CS1","检验完成,全部正确"),
    STATUS_CHECK_S2("CS2","检验完成,部分正确"),
    STATUS_CHECK_S3("CS3","检验完成,全部错误"),
    STATUS_CHECK_E1("CE1","检验异常"),
    STATUS_PERSIST_00("P00","待导入"),
    STATUS_PERSIST_A1("PA1","导入中"),
    STATUS_PERSIST_S1("PS1","导入完成"),
    STATUS_PERSIST_E1("PE1","导入异常"),
    ;

    /**
     * 状态
     */
    private final String value;
    /**
     * 状态名称
     */
    private final String name;
}
