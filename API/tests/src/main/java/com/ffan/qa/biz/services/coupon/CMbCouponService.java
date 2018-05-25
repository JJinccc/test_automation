package com.ffan.qa.biz.services.coupon;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;
import com.ffan.qa.utils.TestConfigUtil;

import java.util.Map;

public class CMbCouponService extends ServiceBase {
    public CMbCouponService(Map<String, Object> urlData) {
        super(urlData, "ffanAPIBaseUrl", "toCMemberCoupon");
    }

    public CMbCouponService() {
        this(TestConfigUtil.getUrlData());
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
