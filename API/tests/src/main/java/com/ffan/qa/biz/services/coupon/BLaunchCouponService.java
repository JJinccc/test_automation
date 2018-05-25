package com.ffan.qa.biz.services.coupon;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;
import com.ffan.qa.utils.TestConfigUtil;

import java.util.Map;

public class BLaunchCouponService extends ServiceBase {
    public BLaunchCouponService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanLaunchCoupon");
    }

    public BLaunchCouponService() {
        this(TestConfigUtil.getUrlData());
    }

    @Override
    public APIResponse request() {
        return httpJsonPost();
    }
}
