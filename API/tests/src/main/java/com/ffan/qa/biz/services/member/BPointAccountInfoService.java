package com.ffan.qa.biz.services.member;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class BPointAccountInfoService extends ServiceBase{
    public BPointAccountInfoService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanPointAccountInfo");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
