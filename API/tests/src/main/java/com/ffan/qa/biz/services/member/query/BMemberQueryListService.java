package com.ffan.qa.biz.services.member.query;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIRequest;
import com.ffan.qa.network.APIResponse;
import java.util.Map;

public class BMemberQueryListService extends ServiceBase {
    public BMemberQueryListService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanQueryMemberList");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
