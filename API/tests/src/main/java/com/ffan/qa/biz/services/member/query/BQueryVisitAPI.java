package com.ffan.qa.biz.services.member.query;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIRequest;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class BQueryVisitAPI extends ServiceBase {
    public BQueryVisitAPI(Map<String, Object> urlData) {
        super(urlData, "QianFanBaseURL", "toBQueryMemberVisit");
    }

    @Override
    public APIResponse request() {
        APIResponse resp = APIRequest
                .GET(url)
                .headers(headers)
                .invoke();
        this.respBody = resp.getBody();
        return resp;
    }
}
