package com.ffan.qa.biz.services.coupon;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class BCouponService extends ServiceBase {
    public BCouponService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanCoupon");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
