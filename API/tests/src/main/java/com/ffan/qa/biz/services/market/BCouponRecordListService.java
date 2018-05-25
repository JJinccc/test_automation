package com.ffan.qa.biz.services.market;
import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;
import java.util.Map;

public class BCouponRecordListService extends ServiceBase{
    public BCouponRecordListService(Map<String, Object> urlData)
    {
        super(urlData, "qianfanBaseUrl", "qianfanCouponRecordList");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }

    public BCouponRecordListService buildBody(String body) {
        this.requestBody = body;
        return this;
    }
}

