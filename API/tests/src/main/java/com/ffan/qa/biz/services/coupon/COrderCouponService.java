package com.ffan.qa.biz.services.coupon;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;
import com.ffan.qa.utils.TestConfigUtil;

import java.util.Map;

public class COrderCouponService extends ServiceBase {
    public COrderCouponService(Map<String, Object> urlData) {
        super(urlData, "ffanAPIBaseUrl", "toCCouponProxy");
    }

    public COrderCouponService() {
        this(TestConfigUtil.getUrlData());
    }

    @Override
    public APIResponse request() {
        return httpFormPost();
    }
}
