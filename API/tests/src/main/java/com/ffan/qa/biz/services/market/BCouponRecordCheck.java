package com.ffan.qa.biz.services.market;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;
import java.util.Map;

public class BCouponRecordCheck extends ServiceBase{
    public BCouponRecordCheck(Map<String, Object> urlData)
    {
        super(urlData, "qianfanBaseUrl", "qianfanCouponRecordCheck");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}

