package com.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//时间处理工具类
public class DateUtil {

    /**
     *使用Calendar对象计算时间差，可以按照需求定制自己的计算逻辑
     * @param beginTime
     * @param endTime
     * @throws ParseException
     */
    public static String subtractTime(Date beginTime,Date endTime) throws ParseException {
            if(endTime==null) {
                endTime = new Date();
            }
        BigDecimal endDecimal = new BigDecimal(endTime.getTime());
        BigDecimal beginDecimal = new BigDecimal(beginTime.getTime());
        BigDecimal subTime =  endDecimal.subtract(beginDecimal).divide(new BigDecimal(1000),2).divide(new BigDecimal(60),2).divide(new BigDecimal(60),2);
//        BigDecimal bigDecimal = new BigDecimal(((endTime.getTime() - beginTime.getTime()) / (60* 1000)));
        return subTime.toString();
//        System.out.println((((d2.getTime() - d1.getTime()) / (24 * 60 * 60 * 1000))) / 365 + "年");
//        System.out.println((d2.getTime() - d1.getTime()) / (24 * 60 * 60 * 1000) + "天");
//        System.out.println((((d2.getTime() - d1.getTime()) / (60 * 60 * 1000)) % 24) + "小时");
//        System.out.println((((d2.getTime() - d1.getTime()) / 1000) % 60) + "分钟");
//        System.out.println(((d2.getTime() - d1.getTime()) / (60 * 1000)) % 60 + "秒");
//        System.out.println((((d2.getTime() - d1.getTime())) % 1000) + "毫秒");


//        Calendar c1 = Calendar.getInstance();   //当前日期
//        Calendar c2 = Calendar.getInstance();
//        c1.setTime(beginTime);
//        c2.setTime(endTime);   //设置为另一个时间


        //这里只是简单的对两个年份数字进行相减，而没有考虑月份的情况
//        System.out.println("传入的日期与今年的年份差为：" + (year - oldYear));
    }
}
