package tests.members;

import com.ffan.qa.biz.services.member.BHomePageMemberService;
import com.ffan.qa.biz.services.member.BHomePageTradeService;
import com.ffan.qa.network.APIResponse;
import org.testng.annotations.Test;
import tests.TestBase;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BMemberHomePageTests extends BMemberTestBase {
    @Test(
            testName = "会员首页",
            description = "会员首页加载正确",
            priority = 0,
            groups = {"Home", "BMember"},
            dataProvider = "memberData"
    )
    public void home_page_load(Map<String, Object> data) {
        qfLoginForDP(data);

        BHomePageMemberService bHomePageMemberService = new BHomePageMemberService(getUrlData());

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("orgType", "10003");
        urlParams.put("timestr", String.valueOf(new Date().getTime()));

        bHomePageMemberService
                .setHeaders(buildQfHeader())
                .buildUrl(urlParams)
                .request()
                .assertJValue("status", "0000");

        BHomePageTradeService bHomePageTradeService = new BHomePageTradeService(getUrlData());

        bHomePageTradeService
                .setHeaders(buildQfHeader())
                .buildUrl(urlParams)
                .request()
                .assertJValue("status", "0000");
    }
}
