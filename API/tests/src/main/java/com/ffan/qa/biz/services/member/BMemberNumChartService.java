package com.ffan.qa.biz.services.member;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class BMemberNumChartService extends ServiceBase{
    public BMemberNumChartService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanMemberNumChart");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
