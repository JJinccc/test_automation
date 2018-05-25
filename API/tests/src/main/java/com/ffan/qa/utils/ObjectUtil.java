package com.ffan.qa.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ObjectUtil {
    public static List<Object> convertToList(Object object) {
        return (ArrayList<Object>)object;
    }

    public static Map<String, Object> convertToMap(Object object) {
        return (LinkedHashMap<String, Object>)object;
    }
}
