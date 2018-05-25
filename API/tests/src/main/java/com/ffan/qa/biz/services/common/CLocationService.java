package com.ffan.qa.biz.services.common;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIRequest;
import com.ffan.qa.network.APIResponse;

import java.util.HashMap;
import java.util.Map;

public class CLocationService extends ServiceBase {
    public CLocationService(Map<String, Object> urlData) {
        super(urlData, "ffanAPIBaseUrl", "toCLocation");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
