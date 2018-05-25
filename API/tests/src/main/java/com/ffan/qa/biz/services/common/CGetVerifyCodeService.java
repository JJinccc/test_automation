package com.ffan.qa.biz.services.common;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;
import com.ffan.qa.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;

public class CGetVerifyCodeService extends ServiceBase {
    public CGetVerifyCodeService(Map<String, Object> urlData, String mobile) {
        super(urlData, "userPlatformBaseUrl", "getVerifyCode");
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("operatorId", "burenshi");
        params.put("secretKey", "ddj2008");
        buildUrl(params);
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }

    public String getVerifyCode() {
        if (JsonUtil.getValue(respBody, "status").toString().equals("200")) {
            return JsonUtil.getValue(respBody, "data.verifyCode").toString();
        }
        return null;
    }
}
