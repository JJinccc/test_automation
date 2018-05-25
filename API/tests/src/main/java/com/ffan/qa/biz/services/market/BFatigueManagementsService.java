package com.ffan.qa.biz.services.market;
import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;
import java.util.Map;

public class BFatigueManagementsService extends ServiceBase {
    public BFatigueManagementsService(Map<String, Object> urlData)
    {
        super(urlData, "qianfanBaseUrl", "qianfanFatigueManagements");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
