package com.ffan.qa.executor;

import com.alibaba.fastjson.JSON;
import com.ffan.qa.common.model.TestDataList;
import com.ffan.qa.settings.TestConfig;
import com.ffan.qa.utils.DBUtil;
import com.ffan.qa.utils.ExcelUtil;
import com.ffan.qa.utils.StringUtil;
import com.ffan.qa.utils.TestConfigUtil;

import java.util.*;

public class ExcelToMysql {
    private static DBUtil dbUtil = new DBUtil(TestConfig.getCurrent().getMysqlUrl(), TestConfig.getCurrent().getMysqlUser(), TestConfig.getCurrent().getMysqlPassword());

    /**
     * url
     * data className testName,e.g. data baseData baseData, data come.ffan.qa.class test
     * dp className testName
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            return;
        }

        switch (args[0]) {
            case "url":
                syncUrl();
                break;
            case "data":
                syncData(args[1], args[2]);
                break;
            case "dp":
                syncDataProvider(args[1], args.length <= 2 ? "" : args[2]);
        }

    }

    private static void syncUrl() throws Exception {
        System.out.println("同步URL数据");
        List<Map<String, Object>> list = dbUtil.queryList("select * from base_url");

        // 同步url
        Map<String, Object> tuTest = TestConfigUtil.getUrlData("sit");
        Map<String, Object> tuUat = TestConfigUtil.getUrlData("uat");
        Map<String, Object> tuProd = TestConfigUtil.getUrlData("prod");

        for (Map.Entry<String, Object> entry : tuTest.entrySet()
                ) {
            String key = entry.getKey();

            if (!list.stream().anyMatch(o -> o.get("base_url_key").equals(key))) {
                String val = entry.getValue().toString();
                String uatVal = tuUat.get(key).toString();
                String prodVal = tuProd.get(key).toString();
                String sql = String.format("insert into base_url(base_url_key, base_url_test, base_url_uat, base_url_prod, creator) values ('%s', '%s','%s','%s','%s')",
                        key, val, uatVal, prodVal, "liqianyang");
                dbUtil.insertWithReturnPrimeKey(sql);
            }
        }
    }

    private static void syncData(String classPath, String testName) throws Exception {
        System.out.println("同步TestData数据");
        List<Map<String, Object>> list = dbUtil.queryList("select * from test_data");

        String dataType = "baseData";
        if (!testName.equals("baseData")) {
            dataType = classPath + "." + testName;
        }
        final String dt = dataType;

        TestDataList testDataList = ExcelUtil.readTestData("data/" + classPath + ".xls", testName);

        Map<String, Object> testData = testDataList.getTestData("sit");
        Map<String, Object> uatData = testDataList.getTestData("uat");
        Map<String, Object> prodData = testDataList.getTestData("prod");

        for (Map.Entry<String, Object> entry : testData.entrySet()
                ) {
            String key = entry.getKey();

            if (!list.stream().anyMatch(o -> o.get("data_key").equals(key) && o.get("data_type").equals(dt))) {
                String val = entry.getValue().toString();
                String uatVal = uatData.get(key).toString();
                String prodVal = prodData.get(key).toString();
                String sql = String.format("insert into test_data(data_key, data_type, data_test, data_uat, data_prod, creator) values ('%s', '%s','%s','%s','%s', '%s')",
                        key, dt, val, uatVal, prodVal, "liqianyang");
                dbUtil.insertWithReturnPrimeKey(sql);
            }
        }
    }

    private static void syncDataProvider(String classPath, String testName) throws Exception {
        System.out.println("同步DataProvider数据");

        String sitStr = "[]";
        String uatStr = "[]";
        String prodStr = "[]";

        String excelPath = "data/" + classPath + ".xls";
        String testSheetName = "dp_" + "sit";
        String uatSheetName = "dp_" + "uat";
        String prodSheetName = "dp_" + "prod";
        if (!StringUtil.isNullOrEmpty(testName)) {
            testSheetName += "_" + testName;
            uatSheetName += "_" + testName;
            prodSheetName += "_" + testName;
        }

        sitStr = readDpDataAsJsonString(excelPath, testSheetName);
        uatStr = readDpDataAsJsonString(excelPath, uatSheetName);
        prodStr = readDpDataAsJsonString(excelPath, prodSheetName);

        String sql = String.format("insert into data_provider(dp_type, dp_test, dp_uat, dp_prod, creator) values ('%s', '%s','%s','%s', '%s')",
                classPath, sitStr, uatStr, prodStr, "liqianyang");
        dbUtil.insertWithReturnPrimeKey(sql);
    }

    private static String readDpDataAsJsonString(String excelPath, String sheetName) {
        if (ExcelUtil.sheetExist(excelPath, sheetName)) {
            Object[][] dp = ExcelUtil.readProviderData(excelPath, sheetName);
            List<Map<String, Object>> dpList = new ArrayList<>();
            for (int i = 0; i < dp.length; i++) {
                dpList.add((HashMap<String, Object>) dp[i][0]);
            }
            return JSON.toJSONString(dpList);
        }

        return "[]";
    }
}
