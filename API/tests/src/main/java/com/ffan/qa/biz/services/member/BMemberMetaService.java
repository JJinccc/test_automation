package com.ffan.qa.biz.services.member;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class BMemberMetaService extends ServiceBase{
    public BMemberMetaService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanMemberMeta");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
