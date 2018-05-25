package com.ffan.qa.biz.services.market;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;
import java.util.Map;

public class BExportUseRecords extends ServiceBase{
    public BExportUseRecords(Map<String, Object> urlData)
    {
        super(urlData, "qianfanBaseUrl", "qianfanExportUseRecords");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}

