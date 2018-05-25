package tests.coupon.toC;

import com.ffan.qa.biz.services.coupon.CAggregationCouponService;
import org.testng.annotations.Test;
import tests.coupon.CouponTestBase;

import java.util.HashMap;
import java.util.Map;

public class CAggregationCouponTests extends CouponTestBase {
    @Test(
            testName = "聚合页券列表",
            description = "测试聚合页券列表",
            priority = 1,
            groups = {"wxCoupon", "Online", "SIT"}
    )
    public void aggre_coupon_list() {
        String channelId = "1003";
        String typeId = "1001";
        String cityId = getBaseData("shCityId").toString();
        String wxLat = getBaseData("wxLat").toString();
        String wxLng = getBaseData("wxLng").toString();
        Map<String, String> params = new HashMap<>();
        params.put("resourceType", "couponList");
        params.put("isMore", "0");
        params.put("plazaId", "undefined");
        params.put("lat", wxLat);
        params.put("lng", wxLng);
        params.put("cityId", cityId);
        params.put("channelId", channelId);
        params.put("type", typeId);
        params.put("pageNum", "1");
        params.put("pageSize", "10");

        CAggregationCouponService service = new CAggregationCouponService(getUrlData());
        service
                .buildUrl(params)
                .request()
                .assertJValue("status", "200", "聚合页券接口返回状态异常")
                .assertJListNotEmpty("data.resource", "聚合页券接口resource为空，存在白页风险");
    }

    @Test(
            testName = "聚合页闪购券列表",
            description = "测试聚合页闪购券列表",
            priority = 1,
            groups = {"wxCoupon", "Online", "SIT"}
    )
    public void flash_sale_aggre_coupons() {
        String cityId = getBaseData("shCityId").toString();
        String wxLat = getBaseData("wxLat").toString();
        String wxLng = getBaseData("wxLng").toString();
        Map<String, String> params = new HashMap<>();
        params.put("resourceType", "flashSale");
        params.put("isMore", "0");
        params.put("lat", wxLat);
        params.put("lng", wxLng);
        params.put("cityId", cityId);
        params.put("pageNum", "1");
        params.put("pageSize", "7");

        CAggregationCouponService service = new CAggregationCouponService(getUrlData());
        service
                .buildUrl(params)
                .request()
                .assertJValue("status", "200", "聚合页闪购券接口返回状态异常")
                .assertJListNotEmpty("data.resource", "聚合页闪购券接口返回resource为空，存在白页风险");
    }
}
