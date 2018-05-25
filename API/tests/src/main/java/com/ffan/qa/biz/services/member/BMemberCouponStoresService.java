package com.ffan.qa.biz.services.member;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class BMemberCouponStoresService extends ServiceBase{
    public BMemberCouponStoresService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanMemberCouponStores");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
