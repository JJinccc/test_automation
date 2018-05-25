package com.ffan.qa.biz.services.member;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class BHomePageTradeService extends ServiceBase{
    public BHomePageTradeService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanHomePageTrade");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
