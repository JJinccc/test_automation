package com.ffan.qa.biz.services.member;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class BMemberConsumeTotalService extends ServiceBase{
    public BMemberConsumeTotalService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanConsumeTotal");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
