package tests.members;

import com.ffan.qa.biz.services.member.BMemberConsumeListService;
import com.ffan.qa.biz.services.member.BMemberConsumeTotalService;
import com.ffan.qa.network.APIResponse;
import org.testng.annotations.Test;
import tests.TestBase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BMemberConsumeAnalysis extends BMemberTestBase {
    @Test(
            testName = "会员消费分析",
            description = "会员消费分析页面加载正确-consumeTotal",
            priority = 0,
            groups = {"Analyse", "BMember", "Online"},
            dataProvider = "memberData"
    )
    public void consume_total_load(Map<String, Object> data) {
        qfLoginForDP(data);

        BMemberConsumeTotalService bMemberConsumeTotalService = new BMemberConsumeTotalService(getUrlData());

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("timestr", String.valueOf(new Date().getTime()));

        bMemberConsumeTotalService
                .setHeaders(buildQfHeader())
                .buildUrl(urlParams)
                .request()
                .assertJValue("status", "0000");
    }

    @Test(
            testName = "会员消费分析",
            description = "会员消费分析页面加载正确-consumeList",
            priority = 0,
            groups = {"Analyse", "BMember", "Online"},
            dataProvider = "memberData"
    )
    public void consume_list_load(Map<String, Object> data) {
        qfLoginForDP(data);

        BMemberConsumeListService bMemberConsumeListService = new BMemberConsumeListService(getUrlData());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("queryOrgCode", data.get("plazaId").toString());
        urlParams.put("timeType", "day");
        urlParams.put("startDate", df.format(new Date()));
        urlParams.put("endDate", df.format(new Date()));
        urlParams.put("orgType", "10003");
        urlParams.put("timestr", String.valueOf(new Date().getTime()));

        bMemberConsumeListService
                .setHeaders(buildQfHeader())
                .buildUrl(urlParams)
                .request()
                .assertJValue("status", "0000");
    }
}
