package tests.coupon.toC;

import com.ffan.qa.biz.services.coupon.CCouponTopService;
import org.testng.annotations.Test;
import tests.coupon.CouponTestBase;

import java.util.HashMap;
import java.util.Map;

public class CCouponCommonTests extends CouponTestBase {
    @Test(
            testName = "首页置顶的券",
            description = "测试首页置顶的券接口",
            priority = 0,
            groups = {"Basics", "wxCoupon", "Online"}
    )
    public void coupon_top() {
        // 参数构造 https://api.sit.ffan.com/wechatxmt/v1/coupon/top?plazaId=1000343
        String plazaId = getBaseData("wxPlazaId").toString();
        Map<String, String> params = new HashMap<>();
        params.put("plazaId", plazaId);

        // TODO: 通过接口加入置顶券

        // 发起请求并进行验证
        CCouponTopService couponTopService = new CCouponTopService(getUrlData());
        couponTopService
                .buildUrl(params) // 将参数合并到URL中
                .request() // 发起请求
                .assertJValue("status", "200"); // 验证状态码为200，返回正常状态
    }
}
