package com.ffan.qa.biz.services.member;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class BMemberDetailService extends ServiceBase{
    public BMemberDetailService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanMemberDetail");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
