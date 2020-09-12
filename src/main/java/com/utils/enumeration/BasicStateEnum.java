package com.utils.enumeration;


/**
 * 基本枚举类
 * @author Shen
 *
 */
public enum BasicStateEnum {

    /**
     * 否 0
     */
    FALSE(BasicEnumConstants.FALSE),

    /**
     * 是 1
     */
    TRUE(BasicEnumConstants.TRUE),
    
    /**
     * cost-待办类型
     */
    TODO_COST(BasicEnumConstants.TODO_COST),

    /**
     * 报价采纳-待办类型
     */
    TODO_QUOTE_PASS(BasicEnumConstants.TODO_QUOTE_PASS),

    /**
     * 询价物料-导入模板类型
     */
    ENQUIRY_MATE(BasicEnumConstants.ENQUIRY_MATE);

    private int value;

    BasicStateEnum(int value){ this.value = value; }

    public int intValue() {
        return this.value;
    }
}
