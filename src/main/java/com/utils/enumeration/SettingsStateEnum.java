package com.utils.enumeration;

/**
 * 配置枚举类
 * @author Shen
 */
public enum SettingsStateEnum {

    /**
     * 匹配率编码 customer_bom_check
     */
    CUSTOMER_BOM_CHECK(SettingsEnumConstants.CUSTOMER_BOM_CHECK),

    /**
     * 选中编码 customer_bom_limit
     */
    CUSTOMER_BOM_LIMIT(SettingsEnumConstants.CUSTOMER_BOM_LIMIT),

    /**
     * 匹配数量编码 customer_bom_number
     */
    CUSTOMER_BOM_NUMBER(SettingsEnumConstants.CUSTOMER_BOM_NUMBER),

    /**
     * 排序方案限制比例编码 customer_sort_plan
     */
    CUSTOMER_SORT_PLAN(SettingsEnumConstants.CUSTOMER_SORT_PLAN);

    private String value;

    SettingsStateEnum(String value){ this.value = value; }

    public String stringValue() {
        return this.value;
    }
}
