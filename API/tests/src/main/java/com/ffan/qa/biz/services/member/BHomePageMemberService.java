package com.ffan.qa.biz.services.member;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class BHomePageMemberService extends ServiceBase{
    public BHomePageMemberService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanHomePageMember");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
