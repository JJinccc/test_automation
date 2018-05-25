package com.ffan.qa.biz.services.market;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;
import java.util.Map;

public class BCouponReceiveRecord extends ServiceBase{
    public BCouponReceiveRecord(Map<String, Object> urlData)
    {
        super(urlData, "qianfanBaseUrl", "qianfanCouponReceiveRecord");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }

//    public BCouponReceiveRecord buildBody(String body) {
//        this.requestBody = body;
//        return this;
//    }
}

