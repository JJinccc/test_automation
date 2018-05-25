package com.ffan.qa.biz.services.coupon;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;
import com.ffan.qa.utils.TestConfigUtil;

import java.util.Map;

public class BCouponAuditService extends ServiceBase {
    public BCouponAuditService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanCouponAudit");
    }

    public BCouponAuditService() {
        this(TestConfigUtil.getUrlData());
    }

    @Override
    public APIResponse request() {
        return httpJsonPut();
    }

    public BCouponAuditService setHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    public BCouponAuditService buildBody(String body) {
        this.requestBody = body;
        return this;
    }
}
