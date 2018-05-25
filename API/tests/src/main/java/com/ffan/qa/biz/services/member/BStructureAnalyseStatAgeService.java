package com.ffan.qa.biz.services.member;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class BStructureAnalyseStatAgeService extends ServiceBase{
    public BStructureAnalyseStatAgeService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanAnalyseStatAge");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
