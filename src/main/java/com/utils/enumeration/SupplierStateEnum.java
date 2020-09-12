package com.utils.enumeration;

/**
 * 供应商枚举类
 * @author Shen
 */
public enum SupplierStateEnum {

    /**
     * 待审核 1
     */
    SUPP_GRADE_TOBE(SupplierEnumConstants.SUPP_GRADE_TOBE),

    /**
     * 合格 2
     */
    SUPP_GRADE_PASS(SupplierEnumConstants.SUPP_GRADE_PASS),

    /**
     * 禁用 3
     */
    SUPP_GRADE_NOPASS(SupplierEnumConstants.SUPP_GRADE_NOPASS);

    private int value;

    SupplierStateEnum(int value){ this.value = value; }

    public int intValue() {
        return this.value;
    }
}
