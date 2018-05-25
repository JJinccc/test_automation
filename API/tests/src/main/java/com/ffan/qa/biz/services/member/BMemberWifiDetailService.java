package com.ffan.qa.biz.services.member;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class BMemberWifiDetailService extends ServiceBase{
    public BMemberWifiDetailService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanMemberWifiDetail");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
