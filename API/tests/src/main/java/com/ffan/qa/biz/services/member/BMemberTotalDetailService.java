package com.ffan.qa.biz.services.member;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class BMemberTotalDetailService extends ServiceBase{
    public BMemberTotalDetailService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanMemberTotalDetail");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
