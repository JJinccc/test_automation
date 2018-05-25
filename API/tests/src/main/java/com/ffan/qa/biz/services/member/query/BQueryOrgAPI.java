package com.ffan.qa.biz.services.member.query;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIRequest;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class BQueryOrgAPI extends ServiceBase {
    public BQueryOrgAPI(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "toBQueryMemberCar");
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
    public void setURL(String orgCode){
        url = url + "/" + orgCode;
    }
}
