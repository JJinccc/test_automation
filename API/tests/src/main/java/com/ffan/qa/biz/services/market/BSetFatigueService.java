package com.ffan.qa.biz.services.market;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;
import java.util.Map;

public class BSetFatigueService extends ServiceBase {
    public BSetFatigueService(Map<String, Object> urlData)
    {
        super(urlData, "qianfanBaseUrl", "qianfanSetFatigue");
    }

    @Override
    public APIResponse request() {
        return httpJsonPut();
    }
}
