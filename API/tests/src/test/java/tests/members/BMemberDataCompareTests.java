package tests.members;

import com.ffan.qa.biz.services.member.BHomePageMemberService;
import com.ffan.qa.biz.services.member.BMemberNumSummaryService;
import com.ffan.qa.biz.services.member.query.BMemberQueryListService;
import com.ffan.qa.network.APIResponse;
import com.ffan.qa.utils.DateUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class BMemberDataCompareTests extends BMemberTestBase{
    @Test(
            testName = "会员数据对比",
            description = "会员首页与会员查询页面，会员数量分析页面的会员总数相同",
            priority = 0,
            groups = {"DataCompare", "BMember", "Online"},
            dataProvider = "memberData"
    )
    public void compare_member_total(Map<String, Object> data){
        qfLoginForDP(data);
        BHomePageMemberService bHomePageMemberService = new BHomePageMemberService(getUrlData());

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("orgType", "10003");
        urlParams.put("timestr", String.valueOf(new Date().getTime()));

        APIResponse resp = bHomePageMemberService
                .setHeaders(buildQfHeaderForDP(data))
                .buildUrl(urlParams)
                .request()
                .assertJValue("status", "0000");

        Integer homepageMemberCount = Integer.valueOf(resp.getJValue("data.memberCount").toString());

        BMemberQueryListService bQueryListService = new BMemberQueryListService(getUrlData());

        Map<String, String> urlParamsQuery = new HashMap<>();
        urlParamsQuery.put("pageIndex", "1");
        urlParamsQuery.put("pageSize", "10");
        urlParamsQuery.put("scopes[]", data.get("plazaId").toString());
        urlParamsQuery.put("scope", data.get("plazaId").toString());
        urlParamsQuery.put("orgType", "10003");
        urlParamsQuery.put("wechatBind", "3");
        urlParamsQuery.put("drainageTypeshow", "false");
        urlParamsQuery.put("drainagedateshow", "false");
        urlParamsQuery.put("timestr", String.valueOf(new Date().getTime()));

        resp = bQueryListService
                .setHeaders(buildQfHeaderForDP(data))
                .buildUrl(urlParamsQuery)
                .request()
                .assertJValue("status", "0000");

        Integer querypageMemberCount = Integer.valueOf(resp.getJValue("_metadata.totalCount").toString());
        Assert.assertTrue(homepageMemberCount.equals(querypageMemberCount) || homepageMemberCount.equals(querypageMemberCount + 1) || homepageMemberCount.equals(querypageMemberCount - 1));

        BMemberNumSummaryService bMemberNumSummaryService = new BMemberNumSummaryService(getUrlData());
        Map<String, String> urlParamsMemberNum = new HashMap<>();
        urlParamsMemberNum.put("orgCode", data.get("plazaId").toString());
        urlParamsMemberNum.put("orgType", "10003");
        urlParamsMemberNum.put("timestr", String.valueOf(new Date().getTime()));

        resp = bMemberNumSummaryService
                .setHeaders(buildQfHeaderForDP(data))
                .buildUrl(urlParamsMemberNum)
                .request()
                .assertJValue("status", "0000");

        Integer membernumMemberCount = Integer.valueOf(resp.getJValue("data.memberNum").toString());
        Assert.assertTrue(homepageMemberCount.equals(membernumMemberCount) || homepageMemberCount.equals(membernumMemberCount + 1) || homepageMemberCount.equals(membernumMemberCount - 1));
    }

    @Test(
            testName = "会员数据对比",
            description = "会员数量分析-今日新增会员数与首页今日新增会员数，会员查询今日注册会员数相同",
            priority = 0,
            groups = {"DataCompare", "BMember", "Online"},
            dataProvider = "memberData"
    )
    public void compare_new_member_total(Map<String, Object> data) {
        qfLoginForDP(data);
        BHomePageMemberService bHomePageMemberService = new BHomePageMemberService(getUrlData());

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("orgType", "10003");
        urlParams.put("timestr", String.valueOf(new Date().getTime()));

        APIResponse resp = bHomePageMemberService
                .setHeaders(buildQfHeaderForDP(data))
                .buildUrl(urlParams)
                .request()
                .assertJValue("status", "0000");

        Integer homepageNewMemberCount = Integer.valueOf(resp.getJValue("data.todayMemberCount").toString());

        BMemberQueryListService bQueryListService = new BMemberQueryListService(getUrlData());

        Map<String, String> urlParamsQuery = new HashMap<>();
        urlParamsQuery.put("pageIndex", "1");
        urlParamsQuery.put("pageSize", "10");
        urlParamsQuery.put("scopes[]", data.get("plazaId").toString());
        urlParamsQuery.put("scope", data.get("plazaId").toString());
        urlParamsQuery.put("orgType", "10003");
        urlParamsQuery.put("regStartTime", DateUtil.DateToStrSimple(new Date()));
        urlParamsQuery.put("regEndTime", DateUtil.DateToStrSimple(new Date()));
        urlParamsQuery.put("wechatBind", "3");
        urlParamsQuery.put("drainageTypeshow", "false");
        urlParamsQuery.put("drainagedateshow", "false");
        urlParamsQuery.put("timestr", String.valueOf(new Date().getTime()));

        resp = bQueryListService
                .setHeaders(buildQfHeaderForDP(data))
                .buildUrl(urlParamsQuery)
                .request()
                .assertJValue("status", "0000");

        Integer querypageNewMemberCount = Integer.valueOf(resp.getJValue("_metadata.totalCount").toString());
        Assert.assertTrue(homepageNewMemberCount.equals(querypageNewMemberCount) || homepageNewMemberCount.equals(querypageNewMemberCount + 1) || homepageNewMemberCount.equals(querypageNewMemberCount - 1));

        BMemberNumSummaryService bMemberNumSummaryService = new BMemberNumSummaryService(getUrlData());
        Map<String, String> urlParamsMemberNum = new HashMap<>();
        urlParamsMemberNum.put("orgCode", data.get("plazaId").toString());
        urlParamsMemberNum.put("orgType", "10003");
        urlParamsMemberNum.put("timestr", String.valueOf(new Date().getTime()));

        resp = bMemberNumSummaryService
                .setHeaders(buildQfHeaderForDP(data))
                .buildUrl(urlParamsMemberNum)
                .request()
                .assertJValue("status", "0000");

        Integer membernumNewMemberCount = Integer.valueOf(resp.getJValue("data.numAddedToday").toString());
        Assert.assertTrue(homepageNewMemberCount.equals(membernumNewMemberCount) || homepageNewMemberCount.equals(membernumNewMemberCount + 1) || homepageNewMemberCount.equals(membernumNewMemberCount - 1));
    }

    @Test(
            testName = "会员数据对比",
            description = "会员分析-会员洞察-会员数量分析-昨日新增会员数与会员查询昨日注册会员数相同",
            priority = 0,
            groups = {"DataCompare", "BMember", "Online"},
            dataProvider = "memberData"
    )
    public void compare_yesterday_member_total(Map<String, Object> data){
        qfLoginForDP(data);

        BMemberQueryListService bQueryListService = new BMemberQueryListService(getUrlData());

        Map<String, String> urlParamsQuery = new HashMap<>();
        urlParamsQuery.put("pageIndex", "1");
        urlParamsQuery.put("pageSize", "10");
        urlParamsQuery.put("scopes[]", data.get("plazaId").toString());
        urlParamsQuery.put("scope", data.get("plazaId").toString());
        urlParamsQuery.put("orgType", "10003");
        urlParamsQuery.put("regStartTime", DateUtil.DateToStrSimple(DateUtil.addDays(new Date(),-1)));
        urlParamsQuery.put("regEndTime", DateUtil.DateToStrSimple(DateUtil.addDays(new Date(),-1)));
        urlParamsQuery.put("wechatBind", "3");
        urlParamsQuery.put("drainageTypeshow", "false");
        urlParamsQuery.put("drainagedateshow", "false");
        urlParamsQuery.put("timestr", String.valueOf(new Date().getTime()));

        APIResponse resp = bQueryListService
                .setHeaders(buildQfHeaderForDP(data))
                .buildUrl(urlParamsQuery)
                .request()
                .assertJValue("status", "0000");

        Integer querypageMemberCount = Integer.valueOf(resp.getJValue("_metadata.totalCount").toString());

        BMemberNumSummaryService bMemberNumSummaryService = new BMemberNumSummaryService(getUrlData());
        Map<String, String> urlParamsMemberNum = new HashMap<>();
        urlParamsMemberNum.put("orgCode", data.get("plazaId").toString());
        urlParamsMemberNum.put("orgType", "10003");
        urlParamsMemberNum.put("timestr", String.valueOf(new Date().getTime()));

        resp = bMemberNumSummaryService
                .setHeaders(buildQfHeaderForDP(data))
                .buildUrl(urlParamsMemberNum)
                .request()
                .assertJValue("status", "0000");

        Integer membernumYesterdayMemberCount = Integer.valueOf(resp.getJValue("data.numAddedYesterday").toString());
        Assert.assertTrue(querypageMemberCount.equals(membernumYesterdayMemberCount) || querypageMemberCount.equals(membernumYesterdayMemberCount + 1) || querypageMemberCount.equals(membernumYesterdayMemberCount - 1));
    }
}
