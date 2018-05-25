package com.ffan.qa.biz.services.coupon;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;
import com.ffan.qa.utils.TestConfigUtil;

import java.util.Map;

public class BCouponOffsaleService extends ServiceBase {
    public BCouponOffsaleService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanCouponOffsale");
    }

    public BCouponOffsaleService() {
        this(TestConfigUtil.getUrlData());
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
