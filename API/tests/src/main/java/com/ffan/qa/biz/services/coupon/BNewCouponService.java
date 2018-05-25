package com.ffan.qa.biz.services.coupon;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIRequest;
import com.ffan.qa.network.APIResponse;

import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

public class BNewCouponService extends ServiceBase {
    public BNewCouponService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanNewCoupon");
    }

    @Override
    public APIResponse request() {
        APIResponse resp = APIRequest
                .POST(url)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .headers(headers)
                .body(requestBody)
                .invoke();
        return resp;
    }

    public BNewCouponService setHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    public BNewCouponService buildBody(String body) {
        this.requestBody = body;
        return this;
    }
}
