package com.ffan.qa.biz.services.common;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIRequest;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class CWeXmtBannersService extends ServiceBase {
    public CWeXmtBannersService(Map<String, Object> urlData) {
        super(urlData, "ffanAPIBaseUrl", "toCBanners");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
