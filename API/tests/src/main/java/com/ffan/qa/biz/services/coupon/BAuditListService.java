package com.ffan.qa.biz.services.coupon;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;
import com.ffan.qa.utils.TestConfigUtil;

import java.util.Map;

public class BAuditListService extends ServiceBase {
    public BAuditListService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanAuditList");
    }

    public BAuditListService() {
        this(TestConfigUtil.getUrlData());
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }

    public BAuditListService setHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    public BAuditListService buildBody(String body) {
        this.requestBody = body;
        return this;
    }
}
