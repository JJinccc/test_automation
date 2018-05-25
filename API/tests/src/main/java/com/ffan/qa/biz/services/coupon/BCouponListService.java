package com.ffan.qa.biz.services.coupon;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class BCouponListService extends ServiceBase {
    public BCouponListService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanCouponList");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
