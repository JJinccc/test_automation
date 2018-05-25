package com.ffan.qa.biz.services.coupon;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;
import com.ffan.qa.utils.TestConfigUtil;

import java.util.Map;

public class CCouponOrderService extends ServiceBase {
    public CCouponOrderService(Map<String, Object> urlData) {
        super(urlData, "ffanAPIBaseUrl", "toCCouponOrder");
    }

    public CCouponOrderService() {
        this(TestConfigUtil.getUrlData());
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
