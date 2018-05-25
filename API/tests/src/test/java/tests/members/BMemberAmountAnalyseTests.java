package tests.members;

import com.ffan.qa.biz.services.member.BMemberNumChartService;
import com.ffan.qa.biz.services.member.BMemberNumSummaryService;
import com.ffan.qa.network.APIResponse;
import org.testng.annotations.Test;
import tests.TestBase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BMemberAmountAnalyseTests extends BMemberTestBase{
    @Test(
            testName = "会员数量分析",
            description = "会员数量分析页面加载正确-NumChart",
            priority = 0,
            groups = {"Analyse", "BMember", "Online"},
            dataProvider = "memberData"
    )
    public void num_chart_load(Map<String, Object> data) {
        qfLoginForDP(data);

        BMemberNumChartService bMemberNumChartService = new BMemberNumChartService(getUrlData());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("orgCode", data.get("plazaId").toString());
        urlParams.put("orgType", "10003");
        urlParams.put("timeType", "day");
        urlParams.put("startDate", df.format(new Date()));
        urlParams.put("endDate", df.format(new Date()));
        urlParams.put("pageSize", "10");
        urlParams.put("pageIndex", "1");
        urlParams.put("timestr", String.valueOf(new Date().getTime()));

        bMemberNumChartService
                .setHeaders(buildQfHeader())
                .buildUrl(urlParams)
                .request()
                .assertJValue("status", "0000");
    }

    @Test(
            testName = "会员数量分析",
            description = "会员数量分析页面加载正确-NumSummary",
            priority = 0,
            groups = {"Analyse", "BMember", "Online"},
            dataProvider = "memberData"
    )
    public void num_summary_load(Map<String, Object> data) {
        qfLoginForDP(data);

        BMemberNumSummaryService bMemberNumSummaryService = new BMemberNumSummaryService(getUrlData());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("orgCode", data.get("plazaId").toString());
        urlParams.put("orgType", "10003");
        urlParams.put("timestr", String.valueOf(new Date().getTime()));

        bMemberNumSummaryService
                .setHeaders(buildQfHeader())
                .buildUrl(urlParams)
                .request()
                .assertJValue("status", "0000");
    }
}
