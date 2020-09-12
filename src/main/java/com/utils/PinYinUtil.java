package com.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

/**
 * 汉字拼音转换
 */
public class PinYinUtil {

    /**
     * 汉字转为拼音首字母大写
     * 注意：汉字会取首字母，英文字母保持不变，数字和其他符号忽略
     * @param chinese
     * @return
     */
    public static String toFirstChar(String chinese){
        String pinyinStr = "";
        char[] newChar = chinese.toCharArray();//转为单个字符
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < newChar.length; i++){
            if (newChar[i] > 128){
                try {
                    pinyinStr += PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat)[0].charAt(0);
                } catch (Exception e) {
                    //转换错误跳过
                    continue;
                }
            }else if ((newChar[i] >= 65 && newChar[i] <= 90) || (newChar[i] >= 97 && newChar[i] <= 122)){
                pinyinStr += newChar[i];
            }else{
                continue;
            }
        }

        //转换成大写字母
        if(pinyinStr.length() > 0){
            pinyinStr = pinyinStr.toUpperCase();
        }

        return pinyinStr;
    }

    /**
     * 公司名称去除“有限责任公司”或者“有限公司”或者“公司”
     * @param companyName
     * @return
     */
    public static String subCompanyName(String companyName){
        if(companyName.contains("有限责任公司")){
            companyName = companyName.replace("有限责任公司", "");
        }
        if(companyName.contains("有限公司")){
            companyName = companyName.replace("有限公司", "");
        }
        if(companyName.contains("公司")){
            companyName = companyName.replace("公司", "");
        }
        return companyName;
    }
}
