package com.ffan.qa.biz.services.member;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class BMemberExpandingAnalyseService extends ServiceBase{
    public BMemberExpandingAnalyseService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanExpandingAnalyse");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
