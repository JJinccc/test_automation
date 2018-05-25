package com.ffan.qa.common.constants;

public class RedisKeys {
    // Prefix
    private static final String DATAPREFIX = "DATA_";
    private static final String URLPREFIX =  DATAPREFIX + "URL_";
    private static final String BASEDATAPREFIX = DATAPREFIX + "BASE_";
    private static final String TESTDATAPREFIX = DATAPREFIX + "TEST_";
    private static final String DATAPROVIDERPREFIX = DATAPREFIX + "DP_";

    private static final String RESULTPREFIX = "RESULT_";
    private static final String DIFFCOUPONRESULTPREFIX = RESULTPREFIX + "DIFFCOUPON_";


    // methods

    /**
     * 返回URL数据Key
     * @param envName
     * @return
     */
    public static String getUrlDataKey(String envName) {
        switch (envName) {
            case "sit":
                return URLPREFIX + "TEST";
            case "test":
                return URLPREFIX + "TEST";
            case "uat":
                return URLPREFIX + "UAT";
            case "prod":
                return URLPREFIX + "PROD";
        }
        return "";
    }

    /**
     * 返回基础数据Key
     * @param envName
     * @return
     */
    public static String getBaseDataKey(String envName) {
        switch (envName) {
            case "sit":
                return BASEDATAPREFIX + "TEST";
            case "test":
                return BASEDATAPREFIX + "TEST";
            case "uat":
                return BASEDATAPREFIX + "UAT";
            case "prod":
                return BASEDATAPREFIX + "PROD";
        }
        return "";
    }

    /**
     * 返回测试数据Key
     * @param envName
     * @param testName
     * @return
     */
    public static String getTestDataKey(String envName, String testName) {
        switch (envName) {
            case "sit":
                return TESTDATAPREFIX + testName + "_TEST";
            case "test":
                return TESTDATAPREFIX + testName + "_TEST";
            case "uat":
                return TESTDATAPREFIX + testName + "_UAT";
            case "prod":
                return TESTDATAPREFIX + testName + "_PROD";
        }
        return "";
    }

    /**
     * 返回DataProvider的Key
     * @param envName
     * @param dpType
     * @return
     */
    public static String getDataProviderKey(String envName, String dpType) {
        switch (envName) {
            case "sit":
                return DATAPROVIDERPREFIX + dpType + "_TEST";
            case "test":
                return DATAPROVIDERPREFIX + dpType + "_TEST";
            case "uat":
                return DATAPROVIDERPREFIX + dpType + "_UAT";
            case "prod":
                return DATAPROVIDERPREFIX + dpType + "_PROD";
        }
        return "";
    }

    /**
     * 返回券数据比对结果存放Key
     * @param plazaId
     * @param channel
     * @param platform
     * @param isLastDiffResult
     * @return
     */
    public static String getDiffCouponResultKey(String plazaId, String channel, String platform, Boolean isLastDiffResult) {
        if (!isLastDiffResult) {
            return DIFFCOUPONRESULTPREFIX + plazaId + "_" + channel + "_" + platform;
        } else {
            return DIFFCOUPONRESULTPREFIX + plazaId + "_" + channel + "_" + platform + "_DIFF";
        }
    }
}
