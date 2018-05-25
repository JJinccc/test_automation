package com.ffan.qa.common.model;

import com.ffan.qa.common.Logger;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Map;

public class TestDataList {
    private ArrayList<TestData> datas;

    public TestDataList() {
        datas = new ArrayList<>();
    }

    /**
     * 添加测试数据
     * @param data
     */
    public void addData(TestData data) {
        datas.add(data);
    }

    /**
     * 是否包含环境测试数据
     * @param env
     * @return
     */
    public boolean hasData(String env) {
        for (TestData t : datas) {
            if (t.getEnv() == env) {
                return true;
            }
        }
        return false;
    }

    public Map<String, Object> getTestData(String env) {
        for (TestData t : datas) {
            if (t.getEnv().equals(env)) {
                return t.getParams();
            }
        }
        return null;
    }

    public void addParam(String env, String key, Object value) {
        for (TestData t : datas) {
            if (t.getEnv().equals(env)) {
                t.add(key, value);
                break;
            }
        }
    }
}
