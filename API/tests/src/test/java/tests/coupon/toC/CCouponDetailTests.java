package tests.coupon.toC;

import com.ffan.qa.biz.services.coupon.CCouponDetailService;
import com.ffan.qa.common.type.AdvertisementType;
import com.ffan.qa.common.type.CouponType;
import com.ffan.qa.errands.CouponErrands;
import com.ffan.qa.network.APIResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.coupon.CouponTestBase;
import com.ffan.qa.common.model.CLoginModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CCouponDetailTests extends CouponTestBase {
    @Test(
            testName = "库存验证",
            description = "领取/购买成功后库存-1",
            priority = 1,
            groups = {"wxCoupon", "Online", "SIT"}
    )
    public void stock_num_test() {
        Map<String, String> coupon = null;

        // 创建满减券
        coupon = CouponErrands.createPublishedCoupon(CouponType.DiscountCoupon, AdvertisementType.CouponList);
        String couponNo = coupon.get("couponNo");
        String plazaId = getBaseData("wxPlazaId").toString();
        String cityId = getBaseData("wxCityId").toString();
        String adSpaceId = "couponList";

        // 等待券出现在列表中
        CouponErrands.waitList(plazaId, adSpaceId, coupon.get("couponNo"), 300);

        //参数构造  https://api.sit.ffan.com/wechatxmt/v1/coupon?couponNo=20180424085106&plazaId=1104495&cityId=522600
        Map<String, String> params = new HashMap<>();
        params.put("couponNo", couponNo);
        params.put("plazaId", plazaId);
        params.put("cityId", cityId);

        //发起请求并进行验证
        CCouponDetailService cCouponDetailService = new CCouponDetailService(getUrlData());
        APIResponse respBefore = cCouponDetailService
                .buildUrl(params)
                .request() //发起请求
                .printResponse()
                .assertJValue("status", "200"); //验证状态码为200，返回正常状态
        Integer stockNumBefore = Integer.parseInt(respBefore.getValue("data.stockNum").toString());
        Integer stockExpect = stockNumBefore - 1;

        // 登录，准备领券
        wxLogin(getBaseData("wxPhone").toString(), getBaseData("wxOpenId").toString(), plazaId);
        CLoginModel login = new CLoginModel(
                cLoginModel.getPhoneNumber(),
                cLoginModel.getCookieStr(),
                cLoginModel.getMemberId(),
                cLoginModel.getPuid(),
                cLoginModel.getPloginToken());
        //领券
        CouponErrands.orderCoupon(login, adSpaceId, plazaId, couponNo, "0");


        // TODO: add wait method
        APIResponse respAfter = cCouponDetailService
                .buildUrl(params)
                .request() //发起请求
                .printResponse()
                .assertJValue("status", "200"); //验证状态码为200，返回正常状态
        Integer stockNumAfter = Integer.parseInt(respAfter.getValue("data.stockNum").toString());
        Assert.assertEquals(stockNumAfter, stockExpect, "领取券后，券库存未-1");
    }
}
