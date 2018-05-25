package com.ffan.qa.biz.services.coupon;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class CPlaSearchCouponService extends ServiceBase {
    public CPlaSearchCouponService(Map<String, Object> urlData) {
        super(urlData, "couponSearchBaseUrl", "toCSearchPublishedCoupon");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
