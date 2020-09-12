package com.utils.enumeration;


/**
 * 基本（枚举）常量类
 * @author Shen
 *
 */
public class BasicEnumConstants {

    /**
     * Boolean类型枚举值<br/>
     * 否
     * @see BasicStateEnum
     */
    public static final int FALSE = 0;
    /**
     * Boolean类型枚举值<br/>
     * 是
     * @see BasicStateEnum
     */
    public static final int TRUE = 1;
    
    /**
	 * 待办系统类型 - cost  <br/>
	 * @see BasicStateEnum
	 */
	public static final int TODO_COST = 1;

    /**
     * 待办系统类型 - 报价采纳 <br/>
     * @see BasicStateEnum
     */
    public static final int TODO_QUOTE_PASS = 2;

    /**
     * 导入模板类型 - 询价物料枚举值<br/>
     * 询价物料
     * @see BasicStateEnum
     */
    public static final int ENQUIRY_MATE = 1;
}
