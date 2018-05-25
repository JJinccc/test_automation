package com.ffan.qa.biz.services.member;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class BStructureAnalyseStatSexService extends ServiceBase{
    public BStructureAnalyseStatSexService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanAnalyseStatSex");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
