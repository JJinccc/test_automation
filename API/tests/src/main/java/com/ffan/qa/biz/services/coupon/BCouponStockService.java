package com.ffan.qa.biz.services.coupon;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class BCouponStockService extends ServiceBase {
    public BCouponStockService(Map<String, Object> urlData) {
        super(urlData, "ffanAPIBaseUrl", "qianfanCouponStock");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
