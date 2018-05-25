package com.ffan.qa.biz.services.coupon;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class CAggregationCouponService extends ServiceBase {
    public CAggregationCouponService(Map<String, Object> urlData) {
        super(urlData, "ffanAPIBaseUrl", "toCAggregationCoupon");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
