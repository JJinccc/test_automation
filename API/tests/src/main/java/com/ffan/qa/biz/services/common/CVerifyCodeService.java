package com.ffan.qa.biz.services.common;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.HashMap;
import java.util.Map;

public class CVerifyCodeService extends ServiceBase {
    public CVerifyCodeService(Map<String, Object> urlData, String mobile) {
        super(urlData, "ffanAPIBaseUrl", "toCVerifyCode");

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("sign", "xcx");
        buildUrl(urlParams);

        this.requestBody = "mobile=" + mobile;
    }

    @Override
    public APIResponse request() {
        return httpFormPost();
    }
}
