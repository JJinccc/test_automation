package com.ffan.qa.biz.services.channels;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;
import com.ffan.qa.utils.TestConfigUtil;

import java.util.Map;

public class BWxAdvertisingService extends ServiceBase {
    public BWxAdvertisingService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanWxAdList");
    }

    public BWxAdvertisingService() {
        this(TestConfigUtil.getUrlData());
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
