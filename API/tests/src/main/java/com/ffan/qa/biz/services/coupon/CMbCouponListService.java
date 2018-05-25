package com.ffan.qa.biz.services.coupon;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIRequest;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class CMbCouponListService extends ServiceBase {
    public CMbCouponListService(Map<String, Object> urlData) {
        super(urlData, "ffanAPIBaseUrl", "toCMemberCoupons");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
