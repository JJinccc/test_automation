package com.ffan.qa.common.model;

import com.ffan.qa.common.Logger;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class TestData {
    private String env;
    private Map<String, Object> params;

    public void add(String key, Object value) {
        if (params.containsKey(key)) {
            Logger.log(String.format("Key %s 已存在，将覆盖处理。", key));
            params.replace(key, value);
        } else {
            params.put(key, value);
        }
    }
}
