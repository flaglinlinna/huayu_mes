package com.utils;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;

public class SearchFilter {
    public String fieldName;
    public Object value;
    public SearchFilter.Operator operator;

    public SearchFilter(String fieldName, SearchFilter.Operator operator, Object value) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
    }

    public static Map<String, SearchFilter> parse(Map<String, Object> searchParams){
        Map<String, SearchFilter> filters = Maps.newHashMap();
        Iterator var2 = searchParams.entrySet().iterator();

        while(var2.hasNext()) {
            Entry<String, Object> entry = (Entry)var2.next();
            String key = (String)entry.getKey();
            Object value = entry.getValue();
            if (!StringUtils.isBlank((String)value)) {
                String[] names = StringUtils.split(key, "_");
                if (names.length != 2) {
                    throw new IllegalArgumentException(key + " is not a valid search filter name");
                }

                String filedName = names[1];
                SearchFilter.Operator operator = SearchFilter.Operator.valueOf(names[0]);
                SearchFilter filter = new SearchFilter(filedName, operator, value);
                filters.put(key, filter);
            }
        }

        return filters;
    }

    public static enum Operator {
        EQ,
        NEQ,
        LIKE,
        LLIKE,
        RLIKE,
        GT,
        LT,
        GTE,
        LTE,
        NULL,
        NOTNULL;

        private Operator() {
        }
    }
}
