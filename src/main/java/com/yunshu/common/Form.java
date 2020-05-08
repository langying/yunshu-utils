package com.yunshu.common;

public class Form {

    public static String ifEmptyError(Object map, String... keys) {
        if (map == null) {
            return "object is null";
        }
        for (String key : keys) {
            Object val = JM.get(map, key);
            if (val == null) {
                return key + " is null";
            }
            if (FF.isEmpty(val.toString())) {
                return key + " is empty";
            }
        }
        return null;
    }
}
