package com.ffan.qa.biz.services.member;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIRequest;
import com.ffan.qa.network.APIResponse;
import com.ffan.qa.utils.JsonUtil;

import javax.ws.rs.core.MediaType;
import java.util.Map;

public class WanliLoginService extends ServiceBase {
    public WanliLoginService(Map<String, Object> urlData) {
        super(urlData, "userBaseUrl", "login");
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
        respBody = String.format("{\"userName\":\"%s\",\"password\":\"%s\"}", user, password);
        return request();
    }

    public String getToken() {
        Object token = JsonUtil.getValue(respBody, "data.token");
        return token == null ? "" : token.toString();
    }
}
