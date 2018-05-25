package com.ffan.qa.biz.services.coupon;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.common.Logger;
import com.ffan.qa.network.APIRequest;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class CCouponDetailService extends ServiceBase{

    public CCouponDetailService(Map<String, Object> urlData){
        super(urlData, "ffanAPIBaseUrl", "toCCoupon");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
