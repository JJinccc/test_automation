package tests.coupon.toB;

import com.ffan.qa.biz.services.market.BCouponRecordListService;
import com.ffan.qa.network.APIResponse;
import org.testng.annotations.Test;
import tests.coupon.CouponTestBase;

public class BCouponRecordListTests extends CouponTestBase {
    @Test(testName = "获取记录默认选项测试", priority = 0, groups = {"Basic", "BCouponRecord"})
    public void b_coupon_record_default() {
        // 执行登录
        qfLogin();

        BCouponRecordListService couponRecordListTests = new BCouponRecordListService(getUrlData());

        String body = "orgId=1000343&tenantId=2017091300001&pageNum=1&pageSize=10&useTool=&businessTagId=&buyBeginTime=1509465600000&buyEndTime=1520179200000&timestr=1520219690717";

        APIResponse resp = couponRecordListTests
                .setHeaders(buildQfHeader())
                .setRequestBody(body)
                .request()
                .assertJValue("status", "200");
    }
}
