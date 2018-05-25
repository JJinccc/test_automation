package com.ffan.qa.biz.services.coupon;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.common.Logger;
import com.ffan.qa.network.APIRequest;
import com.ffan.qa.network.APIResponse;
import com.ffan.qa.settings.TestConfig;
import com.ffan.qa.utils.TestConfigUtil;

import java.util.Map;

public class CPlaCouponListService extends ServiceBase {
    public CPlaCouponListService(Map<String, Object> urlData) {
        super(urlData, "ffanAPIBaseUrl", "toCPlazaCouponsV5");
    }

    public CPlaCouponListService() {
        this(TestConfigUtil.getUrlData());
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
