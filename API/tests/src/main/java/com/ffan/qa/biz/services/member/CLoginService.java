package com.ffan.qa.biz.services.member;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIRequest;
import com.ffan.qa.network.APIResponse;

import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

public class CLoginService extends ServiceBase {
    public CLoginService(Map<String, Object> urlData) {
        super(urlData, "ffanAPIBaseUrl", "toCLogin");
    }

    @Override
    public APIResponse request() {
        return httpFormPost();
    }
}
