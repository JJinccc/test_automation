package com.ffan.qa.executor;

import com.alibaba.fastjson.JSON;
import com.ffan.qa.common.constants.RedisKeys;
import com.ffan.qa.common.model.TestData;
import com.ffan.qa.common.model.tests.TestDataModel;
import com.ffan.qa.common.model.tests.UrlModel;
import com.ffan.qa.settings.TestConfig;
import com.ffan.qa.utils.DBUtil;
import com.ffan.qa.utils.JedisUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySQLToRedis {
    private static DBUtil dbUtil = new DBUtil(TestConfig.getCurrent().getMysqlUrl(), TestConfig.getCurrent().getMysqlUser(), TestConfig.getCurrent().getMysqlPassword());
    private static Logger logger = Logger.getLogger(MySQLToRedis.class);
    private static String[] envNames = {"test", "uat", "prod"};

    public static void main(String[] args) throws Exception {
        buildTestData();
        buildDataProvider();
        buildUrlData();
    }

    private static void buildUrlData() throws Exception {
        logger.trace("准备生成URL数据");

        for (String env : envNames) {
            buildEnvUrlData(env);
        }

        logger.trace("URL数据建立完成");
    }

    private static void buildEnvUrlData(String envName) throws Exception {
        List<UrlModel> urlList = dbUtil.queryExList("SELECT base_url_id as baseUrlId, base_url_key as baseUrlKey, base_url_" + envName + " as baseUrl, expired, creator, created_time as createdTime from base_url where expired = 0", UrlModel.class);
        Map<String, Object> params = new HashMap<>();
        for (UrlModel um: urlList) {
            params.put(um.getBaseUrlKey(), um.getBaseUrl());
        }
        TestData td = new TestData(envName, params);
        JedisUtil.setEx(RedisKeys.getUrlDataKey(envName), td, 30 * 24 * 60 * 60);
    }

    private static void buildTestData() throws Exception {
        logger.debug("准备生成测试数据");

        List<Map<String, Object>> results = dbUtil.queryList("select DISTINCT(data_type) as dataType from test_data WHERE expired = 0");
        for (Map<String, Object> r : results) {
            for (String env : envNames) {
                buildTestData(env, r.get("dataType").toString());
            }
        }

        logger.debug("测试数据生成完毕");
    }

    private static void buildTestData(String envName, String dataType) throws Exception {
        List<TestDataModel> testDataList = dbUtil.queryExList("select data_id as dataId, data_key as dataKey, data_" + envName + " as dataValue, expired, creator, created_time as createdTime from test_data WHERE expired = 0 and data_type = '" + dataType + "'", TestDataModel.class);

        Map<String, Object> dataMap = new HashMap<>();
        for (TestDataModel td: testDataList) {
            dataMap.put(td.getDataKey(), td.getDataValue());
        }
        TestData td = new TestData(envName, dataMap);
        if (dataType.equals("baseData")) {
            JedisUtil.setEx(RedisKeys.getBaseDataKey(envName), td, 30 * 24 * 60 * 60);
        } else {
            JedisUtil.setEx(RedisKeys.getTestDataKey(envName, dataType), td, 30 * 24 * 60 * 60);
        }
    }

    private static void buildDataProvider() throws Exception {
        logger.debug("准备生成DataProvider数据");

        for (String env : envNames) {
            buildDataProvider(env);
        }

        logger.debug("DataProvider数据生成完毕");
    }

    private static void buildDataProvider(String envName) throws Exception {
        List<Map<String, Object>> result = dbUtil.queryList("select dp_type as dpType, dp_" + envName + " as dp, creator, created_time as createdTime from data_provider where expired = 0");

        for (Map<String, Object> dp: result) {
            List<Map<String, Object>> dpData = new ArrayList<>();
            if (!dp.get("dp").toString().equals("[]")) {
                dpData = (List<Map<String, Object>>)JSON.parse(dp.get("dp").toString());
            }

            JedisUtil.setEx(RedisKeys.getDataProviderKey(envName, dp.get("dpType").toString()), dp.get("dp").toString(),30 * 24 * 60 * 60);

        }
        
        
    }
}
