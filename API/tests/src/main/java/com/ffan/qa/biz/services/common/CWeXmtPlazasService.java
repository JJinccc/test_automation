package com.ffan.qa.biz.services.common;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIRequest;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class CWeXmtPlazasService extends ServiceBase {
    public CWeXmtPlazasService(Map<String, Object> urlData) {
        super(urlData, "ffanAPIBaseUrl", "toCPlazas");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
