package com.ffan.qa.biz.services.member;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class BMemberChildinfosService extends ServiceBase{
    public BMemberChildinfosService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanMemberChildinfos");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
