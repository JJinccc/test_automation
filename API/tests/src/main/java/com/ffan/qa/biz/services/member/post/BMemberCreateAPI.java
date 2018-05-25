package com.ffan.qa.biz.services.member.post;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIRequest;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class BMemberCreateAPI extends ServiceBase {
    public BMemberCreateAPI(Map<String, Object> urlData) {
        super(urlData, "QianFanBaseURL", "toBQueryMemberDetails");
    }

    @Override
    public APIResponse request() {
        APIResponse resp = APIRequest
                .POST(url)
                .headers(headers)
                .body(requestBody)
                .invoke();
        this.respBody = resp.getBody();
        return resp;
    }
}
