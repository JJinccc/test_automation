package com.ffan.qa.biz.services.member;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class BMemberTradeService extends ServiceBase{
    public BMemberTradeService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanMemberTrade");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
