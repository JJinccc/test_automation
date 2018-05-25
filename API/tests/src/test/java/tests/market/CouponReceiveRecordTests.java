package tests.market;

import com.ffan.qa.network.APIResponse;
import tests.TestBase;
import org.testng.annotations.Test;

public class CouponReceiveRecordTests extends TestBase{
    @Test(testName = "获取记录默认选项测试", priority = 0, groups = {"Basic", "BCouponRecord"})
    public void b_coupon_record_default() {
        // 执行登录
        qfLogin();

        //CouponReceiveRecordTests couponRecordListTests = new CouponReceiveRecordTests(getUrlData());

        String body = "orgId=1000343&tenantId=2017091300001&pageNum=1&pageSize=10&useTool=&businessTagId=&buyBeginTime=1509465600000&buyEndTime=1520179200000&timestr=1520219690717";

//        APIResponse resp = couponRecordListTests
//                .setHeaders(buildQfHeader())
//                .setRequestBody(body)
//                .request()
//                .assertJValue("status", "200");
    }
}
