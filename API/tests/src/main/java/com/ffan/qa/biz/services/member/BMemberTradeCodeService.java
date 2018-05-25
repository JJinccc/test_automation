package com.ffan.qa.biz.services.member;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class BMemberTradeCodeService extends ServiceBase{
    public BMemberTradeCodeService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanMemberTradeCode");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
