package com.ffan.qa.biz.services.member.query;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIRequest;
import com.ffan.qa.network.APIResponse;

import javax.ws.rs.core.MediaType;
import java.util.Map;

public class BQueryAccountAPI extends ServiceBase {
    public BQueryAccountAPI(Map<String, Object> urlData) {
        super(urlData, "QianFanBaseURL", "toBQueryMemberDetails");
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
