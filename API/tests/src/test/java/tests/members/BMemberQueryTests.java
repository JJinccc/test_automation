package tests.members;

import com.ffan.qa.biz.services.member.query.BMemberQueryListService;
import com.ffan.qa.network.APIResponse;
import tests.TestBase;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BMemberQueryTests extends BMemberTestBase{

    @Test(
            testName = "会员列表查询",
            description = "返回会员列表,默认选项",
            priority = 0,
            groups = {"Query", "BMember", "Online"},
            dataProvider = "memberData"
    )
    public void query_member_list(Map<String, Object> data) {
        qfLoginForDP(data);

        BMemberQueryListService bQueryListService = new BMemberQueryListService(getUrlData());

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("pageIndex", "1");
        urlParams.put("pageSize", "10");
        urlParams.put("scopes[]", data.get("plazaId").toString());
        urlParams.put("scope", data.get("plazaId").toString());
        urlParams.put("orgType", "10003");
        urlParams.put("wechatBind", "3");
        urlParams.put("drainageTypeshow", "false");
        urlParams.put("drainagedateshow", "false");
        urlParams.put("timestr", String.valueOf(new Date().getTime()));

        bQueryListService
                .setHeaders(buildQfHeader())
                .buildUrl(urlParams)
                .request()
                .assertJValue("status", "0000");
    }
}
