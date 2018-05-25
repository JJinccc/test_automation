package com.ffan.qa.biz.services.member;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class BMemberNumSummaryService extends ServiceBase{
    public BMemberNumSummaryService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanMemberNumSummary");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
