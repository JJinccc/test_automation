package com.ffan.qa.biz.services.member;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;
import com.ffan.qa.utils.JsonUtil;

import java.util.Map;

public class CWxLoginForTestService extends ServiceBase {
    public CWxLoginForTestService(Map<String, Object> urlData) {
        super(urlData, "ffanWxBaseUrl", "wxLoginForTest");
    }

    @Override
    public APIResponse request() {
        return httpFormPost();
    }

    public String getFfanToken() {
        return JsonUtil.getValue(this.respBody, "data.wxFfanToken").toString();
    }
}
