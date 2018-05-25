package com.ffan.qa.biz.services.member;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class BMemberDealFlowService extends ServiceBase{
    public BMemberDealFlowService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanDealFlow");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
