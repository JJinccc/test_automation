package com.ffan.qa.biz.services.member;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class BMemberConsumeListService extends ServiceBase{
    public BMemberConsumeListService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanConsumeList");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
