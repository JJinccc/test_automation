package com.ffan.qa.biz.services.coupon;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;
import com.ffan.qa.utils.TestConfigUtil;

import java.util.Map;

public class CCheckCouponService extends ServiceBase {
    public CCheckCouponService(Map<String, Object> urlData) {
        super(urlData, "ffanAPIBaseUrl", "toCCheckCoupon");
    }

    public CCheckCouponService() {
        this(TestConfigUtil.getUrlData());
    }

    @Override
    public APIResponse request() {
        return httpFormPost();
    }
}
