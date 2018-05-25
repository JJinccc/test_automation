package com.ffan.qa.biz.services.member;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class BMemberParkDetailService extends ServiceBase{
    public BMemberParkDetailService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanMemberParkDetail");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
