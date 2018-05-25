package com.ffan.qa.biz.services.market;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;
import java.util.Map;

public class BExportRecords extends ServiceBase{
    public BExportRecords(Map<String, Object> urlData)
    {
        super(urlData, "qianfanBaseUrl", "qianfanExportRecords");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}

