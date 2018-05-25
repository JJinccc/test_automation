package tests.settings.toB;

import com.ffan.qa.biz.services.market.BFatigueManagementsService;
import com.ffan.qa.biz.services.market.BSetFatigueService;
import com.ffan.qa.network.APIResponse;
import org.testng.annotations.Test;
import tests.TestBase;

public class BFatigueManagementsTests extends TestBase {
    @Override
    public void setup() {
        qfLogin();
    }

    @Test(testName = "疲劳度设置测试", priority = 0, groups = {"Basic", "BFatigueManagements"})
    public void b_fatigue_management_default() {
        BFatigueManagementsService fatigueManagementsService = new BFatigueManagementsService(getUrlData());

        String body = "timestr=1520562745781";

        APIResponse resp = fatigueManagementsService
                .setHeaders(buildQfHeader())
                .setRequestBody(body)
                .request()
                .assertJValue("status", "200");
    }

    @Test(testName = "疲劳度设置测试", priority = 0, groups = {"Basic", "BFatigueManagements"})
    public void b_set_fatigue_default() {
        BSetFatigueService setFatigueService = new BSetFatigueService(getUrlData());

        String body = "{\"sms_week_limit\":\"9\",\"coupon_week_limit\":\"9\",\"coupon_member_limit\":\"7\",\"coupon_start_sendTime_limit\":\"01:00\",\"coupon_end_sendTime_limit\":\"08:00\",\"sms_start_sendTime_limit\":\"01:00\",\"sms_end_sendTime_limit\":\"08:00\"}";

        APIResponse resp = setFatigueService
                .setHeaders(buildQfHeader())
                .setRequestBody(body)
                .request()
                .assertJValue("status", "200");
    }
}
