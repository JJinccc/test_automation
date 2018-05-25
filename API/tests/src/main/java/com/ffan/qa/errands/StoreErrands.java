package com.ffan.qa.errands;

import com.ffan.qa.biz.services.store.BStoreListService;
import com.ffan.qa.common.model.BLoginModel;
import com.ffan.qa.network.APIResponse;
import com.ffan.qa.utils.EncoderUtil;
import com.ffan.qa.utils.TestConfigUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreErrands extends ErrandsBase {
    public static List<Object> getBStores(Integer count) {
        // url params
        Map<String, String> params = new HashMap<>();
        params.put("normalMerchantName", "");
        params.put("brandMerchantName", "");
        params.put("limit", count.toString());
        params.put("index", "1");
        params.put("plazaName", EncoderUtil.urlEncode(TestConfigUtil.getBaseData().get("qianfanOrgName").toString()));
        params.put("storeName", "");
        params.put("cityId", "");
        params.put("businessId", "");
        params.put("plazaId", TestConfigUtil.getBaseData().get("wxPlazaId").toString());
        params.put("merchantType", "1");
        params.put("timestr", String.valueOf(new Date().getTime()));

        // header params
        Map<String, String> headerParams = getQianfanHeader();

        // 通过service请求
        BStoreListService storeListService = new BStoreListService();
        APIResponse resp = storeListService
                .setHeaders(headerParams)
                .buildUrl(params)
                .request()
                .assertJValue("status", "200", "获取Store列表失败");
        return (List<Object>)resp.getJValue("data");
    }
}
