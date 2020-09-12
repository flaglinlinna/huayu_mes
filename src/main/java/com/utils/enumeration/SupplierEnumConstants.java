package com.utils.enumeration;

/**
 * 供应商（枚举）常量类
 * @author Shen
 *
 */
public class SupplierEnumConstants {

    /**
     * 供应商级别类型 - 待审核类型枚举类<br/>
     * 待审核
     * @see SupplierStateEnum
     */
    public static final int SUPP_GRADE_TOBE = 1;

    /**
     * 供应商级别类型 - 合格类型枚举类<br/>
     * 合格
     * @see SupplierStateEnum
     */
    public static final int SUPP_GRADE_PASS = 2;

    /**
     * 供应商级别类型 - 禁用类型枚举类<br/>
     * 禁用
     * @see SupplierStateEnum
     */
    public static final int SUPP_GRADE_NOPASS = 3;
}
