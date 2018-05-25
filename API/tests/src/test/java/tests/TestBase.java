package tests;

import com.ffan.qa.biz.services.member.BLoginService;
import com.ffan.qa.common.Logger;
import com.ffan.qa.common.model.BLoginModel;
import com.ffan.qa.common.model.TestDataList;
import com.ffan.qa.network.APIResponse;
import com.ffan.qa.settings.TestConfig;
import com.ffan.qa.utils.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class TestBase {
    public String testName;
    public String testEnv;
    public Map<String, Object> caseData;
    protected BLoginModel qfLoginModel;

    @BeforeClass(alwaysRun = true)
    public void classInit() {
        try {
            testEnv = TestConfig.getCurrent().getTestEnv();
        } catch (Exception ex) {
            // TODO: Fail the case
            Logger.log("读取测试配置异常，使用默认UAT环境");
            testEnv = "sit";
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void setup(Method method) {
        testName = method.getName();
        caseData = null;

        setup();
    }

    public void setup() {
    }

    public Object getBaseData(String key) {
        Map<String, Object> data = TestConfigUtil.getBaseData(testEnv);
        Assert.assertTrue(data.containsKey(key), String.format("未找到测试基础数据，Key: %s", key));
        return data.get(key);
    }

    public Object getTestData(String key) {
        if (null == caseData) {
            try {
                caseData = TestConfigUtil.getTestData(testEnv, this.getClass().getName(), testName);
            } catch (Exception ex) {
                Logger.log(String.format("测试用例%s未检测到数据文件", testName));
            }
        }

        Assert.assertTrue(caseData.containsKey(key), String.format("未找到测试数据，Key: %s", key));
        return caseData.get(key);
    }

    public Map<String, Object> getUrlData() {
        return TestConfigUtil.getUrlData(testEnv);
    }

    @DataProvider(name = "data")
    public Object[][] getData(Method method) {
        try {
            testEnv = TestConfig.getCurrent().getTestEnv();
            testName = method.getName();
        } catch (Exception ex) {
            Logger.log(ex.getMessage());
        }

        String excelPath = "data/" + this.getClass().getName() + ".xls";
        String baseExcelPath = "data/baseData.xls";
        String sheetName = "dp_" + testEnv + "_" + testName;
        String classSheetName = "dp_" + testEnv;

        if (ExcelUtil.sheetExist(excelPath, sheetName)) {
            return ExcelUtil.readProviderData(excelPath, sheetName);
        } else if (ExcelUtil.sheetExist(excelPath, classSheetName)) {
            return ExcelUtil.readProviderData(excelPath, classSheetName);
        } else {
            return ExcelUtil.readProviderData(baseExcelPath, classSheetName);
        }

    }

    public void qfLogin(String user, String pwd) {
        qfLogin(user, pwd, true, 5);
    }

    public void qfLogin(String user, String pwd, Boolean failRetry, Integer retryTimes) {
        BLoginService loginService = new BLoginService(getUrlData());

        APIResponse resp = loginService
                .request(user, pwd);

        if (!resp.getJValue("status").equals("0000") && failRetry) {
            // 重新登录
            for (Integer i = 0; i< retryTimes;i++){
                resp = loginService
                        .request(user, pwd);
                if (resp.getJValue("status").equals("0000")) {
                    break;
                } else {
                    // 等待5秒钟
                    SystemUtil.sleep(5000);
                }
            }
        } else if (!resp.getJValue("status").equals("0000")) {
            Assert.fail("登录失败，返回报文：" + resp.getBody());
        }

        qfLoginModel = new BLoginModel(resp.getJValue("data.userId").toString(),
                resp.getJValue("data.tenantId").toString(),
                resp.getJValue("data.token").toString());
    }

    public void qfLoginForDP(Map<String, Object> data) {
        qfLogin(data.get("user").toString(), StringUtil.isNullOrEmpty(data.get("enPwd").toString()) ? EncryptUtil.getMD5(data.get("pwd").toString()) : data.get("enPwd").toString());
    }

    public void qfLogin() {
        qfLogin(getBaseData("qianfanAdmin").toString(), getBaseData("qianfanPassword").toString());
    }

    public Map<String, String> buildQfHeader() {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("token", qfLoginModel.getToken());
            headers.put("orgcode", getBaseData("qianfanOrgCode").toString());
            headers.put("orgname", URLEncoder.encode(getBaseData("qianfanOrgName").toString(),"utf-8"));
            headers.put("orgTypeCode", getBaseData("qianfanOrgTypeCode").toString());
            headers.put("orgTypeName", EncoderUtil.urlEncode(getBaseData("qianfanOrgTypeName").toString()));
            headers.put("tenantId", qfLoginModel.getTenantId());
            headers.put("userid", qfLoginModel.getUserId());
            headers.put("username", getBaseData("qianfanAdmin").toString());
            headers.put("workingOrgCode", getBaseData("qianfanOrgCode").toString());

            return headers;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public Map<String, String> buildQfHeaderForDP( Map <String, Object> data) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("token", qfLoginModel.getToken());
            headers.put("orgcode", data.get("plazaId").toString());
            headers.put("orgname", URLEncoder.encode(data.get("plazaName").toString(),"utf-8"));
            headers.put("orgTypeCode", getBaseData("qianfanOrgTypeCode").toString());
            headers.put("orgTypeName", EncoderUtil.urlEncode(getBaseData("qianfanOrgTypeName").toString()));
            headers.put("tenantId", qfLoginModel.getTenantId());
            headers.put("userid", qfLoginModel.getUserId());
            headers.put("username", data.get("user").toString());
            headers.put("workingOrgCode", data.get("plazaId").toString());

            return headers;
        }
        catch (Exception ex) {
            return null;
        }
    }
}
