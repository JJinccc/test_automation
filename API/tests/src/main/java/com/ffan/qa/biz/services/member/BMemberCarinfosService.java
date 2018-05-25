package com.ffan.qa.biz.services.member;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class BMemberCarinfosService extends ServiceBase{
    public BMemberCarinfosService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanMemberCarinfos");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
