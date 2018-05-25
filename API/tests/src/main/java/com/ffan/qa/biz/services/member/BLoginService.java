package com.ffan.qa.biz.services.member;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIRequest;
import com.ffan.qa.network.APIResponse;

import javax.ws.rs.core.MediaType;
import java.util.Map;

public class BLoginService extends ServiceBase {
    public BLoginService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanLogin");
    }

    @Override
    public APIResponse request() {
        APIResponse resp = APIRequest
                .POST(url)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .body(requestBody)
                .invoke();
        respBody = resp.getBody();
        return resp;
    }

    public APIResponse request(String user, String password) {
        requestBody = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", user, password);
        return request();
    }

}
