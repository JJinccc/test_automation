package com.ffan.qa.biz.services.coupon;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;
import com.ffan.qa.utils.TestConfigUtil;

import java.util.Map;

public class BQueryCouponsService extends ServiceBase {
    public BQueryCouponsService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanNewCouponV2");
    }

    public BQueryCouponsService() {
        this(TestConfigUtil.getUrlData());
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
