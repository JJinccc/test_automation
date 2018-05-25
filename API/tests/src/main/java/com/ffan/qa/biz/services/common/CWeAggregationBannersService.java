package com.ffan.qa.biz.services.common;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class CWeAggregationBannersService extends ServiceBase {
    public CWeAggregationBannersService(Map<String, Object> urlData) {
        super(urlData, "ffanAPIBaseUrl", "toCAggregationBanners");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
