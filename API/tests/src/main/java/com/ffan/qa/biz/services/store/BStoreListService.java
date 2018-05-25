package com.ffan.qa.biz.services.store;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;
import com.ffan.qa.utils.TestConfigUtil;

import java.util.Map;

public class BStoreListService extends ServiceBase {
    public BStoreListService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanStoreList");
    }
    public BStoreListService() {
        this(TestConfigUtil.getUrlData());
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
