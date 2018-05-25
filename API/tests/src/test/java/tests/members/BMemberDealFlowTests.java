package tests.members;

import com.ffan.qa.biz.services.member.BMemberDealFlowService;
import com.ffan.qa.biz.services.member.BMemberNumChartService;
import com.ffan.qa.network.APIResponse;
import org.testng.annotations.Test;
import tests.TestBase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BMemberDealFlowTests extends BMemberTestBase {
    @Test(
            testName = "交易流水",
            description = "交易流水页面加载正确",
            priority = 0,
            groups = {"Analyse", "BMember", "Online"},
            dataProvider = "memberData"
    )
    public void deal_flow_load(Map<String, Object> data) {
        qfLoginForDP(data);

        BMemberDealFlowService bMemberDealFlowService = new BMemberDealFlowService(getUrlData());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("orgCode", data.get("plazaId").toString());
        urlParams.put("orgType", "10003");
        urlParams.put("selectOrg[]:", data.get("plazaId").toString());
        urlParams.put("pageIndex", "1");
        urlParams.put("pageSize", "10");
        urlParams.put("timestr", String.valueOf(new Date().getTime()));

        bMemberDealFlowService
                .setHeaders(buildQfHeader())
                .buildUrl(urlParams)
                .request()
                .assertJValue("status", "0000");
    }
}
