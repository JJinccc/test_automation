package tests.members;

import com.ffan.qa.biz.services.member.BMemberExpandingAnalyseService;
import com.ffan.qa.biz.services.member.query.BMemberQueryListService;
import com.ffan.qa.network.APIResponse;
import org.testng.annotations.Test;
import tests.TestBase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BMemberExpandingAnalyseTests extends BMemberTestBase {
    @Test(
            testName = "会员拓客分析",
            description = "拓客分析页面加载正确",
            priority = 0,
            groups = {"Analyse", "BMember", "Online"},
            dataProvider = "memberData"
    )
    public void expanding_analyse_load(Map<String, Object> data) {
        qfLoginForDP(data);

        BMemberExpandingAnalyseService bMemberExpandingAnalyseService = new BMemberExpandingAnalyseService(getUrlData());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("scope", data.get("plazaId").toString());
        urlParams.put("orgType", "10003");
        urlParams.put("startDate", df.format(new Date()));
        urlParams.put("endDate", df.format(new Date()));
        urlParams.put("pageIndex", "1");
        urlParams.put("pageSize", "10");
        urlParams.put("timestr", String.valueOf(new Date().getTime()));

        bMemberExpandingAnalyseService
                .setHeaders(buildQfHeader())
                .buildUrl(urlParams)
                .request()
                .assertJValue("status", "0000");
    }
}
