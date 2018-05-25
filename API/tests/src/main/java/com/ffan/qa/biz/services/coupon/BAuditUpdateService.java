package com.ffan.qa.biz.services.coupon;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;
import com.ffan.qa.utils.TestConfigUtil;

import java.util.Map;

public class BAuditUpdateService extends ServiceBase {
    public BAuditUpdateService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanAuditUpdate");
    }

    public BAuditUpdateService() {
        this(TestConfigUtil.getUrlData());
    }

    @Override
    public APIResponse request() {
        return httpJsonPut();
    }
}
