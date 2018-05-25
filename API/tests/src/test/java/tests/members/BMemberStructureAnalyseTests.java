package tests.members;

import com.ffan.qa.biz.services.member.BStructureAnalyseStatAgeService;
import com.ffan.qa.biz.services.member.BStructureAnalyseStatSexService;
import com.ffan.qa.biz.services.member.BStructurePlazaTypeRoleService;
import com.ffan.qa.network.APIResponse;
import org.testng.annotations.Test;
import tests.TestBase;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BMemberStructureAnalyseTests extends BMemberTestBase{
    @Test(
            testName = "会员结构分析",
            description = "会员结构分析加载-组织",
            priority = 0,
            groups = {"Analyse", "BMember", "Online"},
            dataProvider = "memberData"
    )
    public void plaza_type_role(Map<String, Object> data) {
        qfLoginForDP(data);

        BStructurePlazaTypeRoleService bStructurePlazaTypeRoleService = new BStructurePlazaTypeRoleService(getUrlData());

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("timestr", String.valueOf(new Date().getTime()));

        bStructurePlazaTypeRoleService
                .setHeaders(buildQfHeader())
                .buildUrl(urlParams)
                .request()
                .assertJValue("status", "0000");
    }

    @Test(
            testName = "会员结构分析",
            description = "会员结构分析加载-性别",
            priority = 0,
            groups = {"Analyse", "BMember", "Online"},
            dataProvider = "memberData"
    )
    public void analyse_stat_sex(Map<String, Object> data) {
        qfLoginForDP(data);

        BStructureAnalyseStatSexService bStructureAnalyseStatSexService = new BStructureAnalyseStatSexService(getUrlData());

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("plazaIds", data.get("plazaId").toString());
        urlParams.put("timestr", String.valueOf(new Date().getTime()));

        bStructureAnalyseStatSexService
                .setHeaders(buildQfHeader())
                .buildUrl(urlParams)
                .request()
                .assertJValue("status", "200");
    }

    @Test(
            testName = "会员结构分析",
            description = "会员结构分析加载-年龄",
            priority = 0,
            groups = {"Analyse", "BMember", "Online"},
            dataProvider = "memberData"
    )
    public void analyse_stat_age(Map<String, Object> data) {
        qfLoginForDP(data);

        BStructureAnalyseStatAgeService bStructureAnalyseStatAgeService = new BStructureAnalyseStatAgeService(getUrlData());

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("plazaIds", data.get("plazaId").toString());
        urlParams.put("timestr", String.valueOf(new Date().getTime()));

        bStructureAnalyseStatAgeService
                .setHeaders(buildQfHeader())
                .buildUrl(urlParams)
                .request()
                .assertJValue("status", "200");
    }
}
