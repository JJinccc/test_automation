package com.ffan.qa.errands;

import com.ffan.qa.biz.services.channels.BWxAdvertisingService;
import com.ffan.qa.common.type.AdvertisementType;
import com.ffan.qa.network.APIResponse;
import com.ffan.qa.utils.TestConfigUtil;
import org.testng.Assert;

import java.util.*;

public class AdvertisementErrands extends ErrandsBase {
    public static Map<String, Object> findAdvdertisement(AdvertisementType adType) {
        String adTypeName = "";
        switch (adType) {
            case CouponList:
                adTypeName = "券列表";
                break;
            case NewUser:
                adTypeName = "新用户专享";
                break;
            case FlashSale:
                adTypeName = "限时抢购";
                break;
        }
        Map<String, String> wxAdParams = new HashMap<>();
        wxAdParams.put("pageSize", "1000");
        wxAdParams.put("pageNum", "1");
        wxAdParams.put("channelId", "1003");
        wxAdParams.put("status", "1");
        wxAdParams.put("plazaId", TestConfigUtil.getBaseData().get("wxPlazaId").toString());
        wxAdParams.put("timestr", String.valueOf(new Date().getTime()));
        BWxAdvertisingService wxAdvertisingService = new BWxAdvertisingService();
        APIResponse wxAdResp = wxAdvertisingService
                .setHeaders(getQianfanHeader())
                .buildUrl(wxAdParams)
                .request()
                .assertJValue("status", "200", "获取小程序投放位置列表服务异常");
        List<Object> wxAds = (ArrayList<Object>) wxAdResp.getJValue("data");
        Map<String, Object> ad = findAd(wxAds, adTypeName);

        return ad;
    }

    private static Map<String, Object> findAd(List<Object> wxAds, String expectedAdName) {
        for (Object obj : wxAds) {
            Map<String, Object> ad = (LinkedHashMap<String, Object>) obj;
            if (ad.get("advertisingName").toString().equals(expectedAdName)) {
                return ad;
            }
        }
        Assert.fail("广告位类型未找到：" + expectedAdName);
        return null;
    }
}
