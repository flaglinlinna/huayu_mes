package com.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 常用贴片封装
 */
public class PackageUtil {

    /**
     * 获取封装（英制）
     * @return
     */
    public static List<String> getPackage(){
        List<String> list = new ArrayList<>();
        list.add("0201");
        list.add("0402");
        list.add("0603");
        list.add("0805");
        list.add("1206");
        list.add("1210");
        list.add("1806");
        list.add("1812");
        list.add("1825");
        list.add("2010");
        list.add("2225");
        list.add("2512");
        return list;
    }
}
