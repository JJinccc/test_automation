package com.ffan.qa.biz.services.member;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class BMemberCouponsService extends ServiceBase{
    public BMemberCouponsService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanMemberCoupons");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
