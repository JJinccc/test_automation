package com.ffan.qa.utils;

import com.ffan.qa.common.Logger;
import com.ffan.qa.common.constants.RedisKeys;
import com.ffan.qa.common.model.TestData;
import com.ffan.qa.common.model.TestDataList;
import com.ffan.qa.common.model.tests.UrlModel;
import com.ffan.qa.settings.TestConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestConfigUtil {
    private static TestDataList urlDataList = null;
    private static TestDataList baseDataList = null;

    public static Map<String, Object> getUrlData() {
        try {
            String testEnv = TestConfig.getCurrent().getTestEnv();
            return getUrlData(testEnv);
        } catch (Exception ex) {
            Logger.error(ex.getMessage());
            return null;
        }
    }

    public static  Map<String, Object> getUrlData(String testEnv) {
        if (null == urlDataList || !urlDataList.hasData(testEnv)) {
            if (TestConfig.getCurrent().getDataSource().equals("excel")) {
                // 从Excel数据源中读取数据
                urlDataList = ExcelUtil.readTestData("data/baseData.xls", "baseUrl");
            } else {
                // 从Redis中读取数据
                TestData td = JedisUtil.get(RedisKeys.getUrlDataKey(testEnv), TestData.class);
                if (null == urlDataList) {
                    urlDataList = new TestDataList();
                }
                urlDataList.addData(td);
            }
        }
        return urlDataList.getTestData(testEnv);
    }

    public static Map<String, Object> getBaseData() {
        try {
            String testEnv = TestConfig.getCurrent().getTestEnv();
            return getBaseData(testEnv);
        } catch (Exception ex) {
            Logger.error(ex.getMessage());
            return null;
        }
    }

    public static Map<String, Object> getBaseData(String testEnv) {
        if (null == baseDataList || !baseDataList.hasData(testEnv)) {
            if (TestConfig.getCurrent().getDataSource().equals("excel")) {
                // 从Excel中读取数据
                baseDataList = ExcelUtil.readTestData("data/baseData.xls", "baseData");
            } else {
                // 从Redis中读取数据
                TestData td = JedisUtil.get(RedisKeys.getBaseDataKey(testEnv), TestData.class);
                if (null == baseDataList) {
                    baseDataList = new TestDataList();
                }
                baseDataList.addData(td);
            }
        }
        return baseDataList.getTestData(testEnv);
    }

    public static Map<String, Object> getTestData(String className, String testName) {
        try {
            String testEnv = TestConfig.getCurrent().getTestEnv();
            return getTestData(testEnv, className, testName);
        } catch (Exception ex) {
            Logger.error(ex.getMessage());
            return null;
        }
    }

    public static Map<String, Object> getTestData(String testEnv, String className, String testName) {
        TestDataList testDataList = new TestDataList();
        if (TestConfig.getCurrent().getDataSource().equals("excel")) {
            // 从Excel中读取数据
            testDataList = ExcelUtil.readTestData("data/" + className + ".xls", testName);
        } else {
            // 从Redis中读取数据
            TestData td = JedisUtil.get(RedisKeys.getTestDataKey(testEnv, className + "." + testName), TestData.class);
            testDataList.addData(td);
        }
        return testDataList.getTestData(testEnv);
    }
}
