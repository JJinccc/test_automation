package com.ffan.qa.biz.services.member;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class BMemberQueryByConditionService extends ServiceBase{
    public BMemberQueryByConditionService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanMemberQueryByCondition");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
