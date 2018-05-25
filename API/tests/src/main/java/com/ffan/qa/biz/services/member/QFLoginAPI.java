package com.ffan.qa.biz.services.member;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIRequest;
import com.ffan.qa.network.APIResponse;
import com.ffan.qa.utils.JsonUtil;

import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

public class QFLoginAPI extends ServiceBase {
    public QFLoginAPI(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanLogin");
    }

    @Override
    public APIResponse request() {
        Map<String, String> headers = new HashMap();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json;chartset=utf-8");

        APIResponse resp = APIRequest
                .POST(url)
                .headers(headers)
                .body(requestBody)
                .invoke();
        respBody = resp.getBody();
        return resp;
    }

    public APIResponse request(String user, String password) {
        requestBody = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", user, password);
        return request();
    }

    public String getToken() {
        Object token = JsonUtil.getValue(respBody, "data.token");
        return token == null ? "" : token.toString();
    }
}
