package com.ffan.qa.biz.services.market;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;
import java.util.Map;

public class BStoresContract extends ServiceBase{
    public BStoresContract(Map<String, Object> urlData)
    {
        super(urlData, "qianfanBaseUrl", "qianfanStoresContract");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}

