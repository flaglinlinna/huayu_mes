package com.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeChangeUtils {


    public static <T> List<T> objectToList(Object object, Class<T> clazz){
        try {
            List<T> result = new ArrayList<>();
            if (object instanceof List<?>){
                for (Object o : (List<?>) object) {
                    String string = JSONObject.toJSONString(o);
                    T t = JSONObject.parseObject(string, clazz);
                    result.add(t);
                }
                return result;
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


}
