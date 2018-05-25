package tests.members;

import com.ffan.qa.biz.services.member.*;
import org.testng.annotations.Test;
import tests.TestBase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BMemberDetailTests extends TestBase{
    @Override
    public void setup() {
        qfLogin();
    }

    @Test(
            testName = "会员详情",
            description = "会员详情-TotalDetail",
            priority = 0,
            groups = {"MemberDetail", "BMember", "Online"}
    )
    public void member_detail_total_detail() {

        BMemberTotalDetailService bMemberTotalDetailService = new BMemberTotalDetailService(getUrlData());

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("memberCode", getBaseData("qianfanMemberCode").toString());
        urlParams.put("plazaId", getBaseData("wxPlazaId").toString());
        urlParams.put("timestr", String.valueOf(new Date().getTime()));

        bMemberTotalDetailService
                .setHeaders(buildQfHeader())
                .buildUrl(urlParams)
                .request()
                .assertJValue("status", "0000");
    }

    @Test(
            testName = "会员详情",
            description = "会员详情-PointAccountInfo",
            priority = 0,
            groups = {"MemberDetail", "BMember", "Online"}
    )
    public void member_detail_total_accountInfo() {

        BPointAccountInfoService bPointAccountInfoService = new BPointAccountInfoService(getUrlData());

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("memberCode", getBaseData("qianfanMemberCode").toString());
        urlParams.put("timestr", String.valueOf(new Date().getTime()));

        bPointAccountInfoService
                .setHeaders(buildQfHeader())
                .buildUrl(urlParams)
                .request()
                .assertJValue("status", "0000");
    }

    @Test(
            testName = "会员详情",
            description = "会员详情-queryByCondition",
            priority = 0,
            groups = {"MemberDetail", "BMember", "Online"}
    )
    public void member_detail_total_queryByCondition() {

        BMemberQueryByConditionService bMemberQueryByConditionService = new BMemberQueryByConditionService(getUrlData());

        Map<String, String> urlParams = new HashMap<>();

        urlParams.put("memberCodes", getBaseData("qianfanMemberCode").toString());
        urlParams.put("pageIndex", "1");
        urlParams.put("pageSize", "1");
        urlParams.put("timestr", String.valueOf(new Date().getTime()));

        bMemberQueryByConditionService
                .setHeaders(buildQfHeader())
                .buildUrl(urlParams)
                .request()
                .assertJValue("status", "0000");
    }

    @Test(
            testName = "会员详情",
            description = "会员详情-queryByCondition",
            priority = 0,
            groups = {"MemberDetail", "BMember", "Online"}
    )
    public void member_detail_total_parkDetail() {

        BMemberParkDetailService bMemberParkDetailService = new BMemberParkDetailService(getUrlData());

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("memberCode", getBaseData("qianfanMemberCode").toString());
        urlParams.put("timestr", String.valueOf(new Date().getTime()));

        bMemberParkDetailService
                .setHeaders(buildQfHeader())
                .buildUrl(urlParams)
                .request()
                .assertJValue("status", "0000");
    }

    @Test(
            testName = "会员详情",
            description = "会员详情-wifiDetail",
            priority = 0,
            groups = {"MemberDetail", "BMember", "Online"}
    )
    public void member_detail_total_wifiDetail() {

        BMemberWifiDetailService bMemberWifiDetailService = new BMemberWifiDetailService(getUrlData());

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("memberCode", getBaseData("qianfanMemberCode").toString());
        urlParams.put("timestr", String.valueOf(new Date().getTime()));

        bMemberWifiDetailService
                .setHeaders(buildQfHeader())
                .buildUrl(urlParams)
                .request()
                .assertJValue("status", "0000");
    }

    @Test(
            testName = "会员详情",
            description = "会员详情-wifiDetail",
            priority = 0,
            groups = {"MemberDetail", "BMember", "Online"}
    )
    public void member_detail_total_memberMeta() {

        BMemberMetaService bMemberMetaService = new BMemberMetaService(getUrlData());

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("timestr", String.valueOf(new Date().getTime()));

        bMemberMetaService
                .setHeaders(buildQfHeader())
                .buildUrl(urlParams)
                .request()
                .assertJValue("status", "0000");
    }

    @Test(
            testName = "会员详情",
            description = "会员详情-memberDetail",
            priority = 0,
            groups = {"MemberDetail", "BMember", "Online"}
    )
    public void member_detail_total_memberDetail() {

        BMemberDetailService bMemberDetailService = new BMemberDetailService(getUrlData());

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("memberCode", getBaseData("qianfanMemberCode").toString());
        urlParams.put("plazaId", getBaseData("wxPlazaId").toString());
        urlParams.put("type", "1");
        urlParams.put("timestr", String.valueOf(new Date().getTime()));

        bMemberDetailService
                .setHeaders(buildQfHeader())
                .buildUrl(urlParams)
                .request()
                .assertJValue("status", "0000");
    }

    @Test(
            testName = "会员详情",
            description = "会员详情-carinfos",
            priority = 0,
            groups = {"MemberDetail", "BMember", "Online"}
    )
    public void member_detail_total_carinfos() {

        BMemberCarinfosService bMemberCarinfosService = new BMemberCarinfosService(getUrlData());

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("memberCode", getBaseData("qianfanMemberCode").toString());
        urlParams.put("timestr", String.valueOf(new Date().getTime()));

        bMemberCarinfosService
                .setHeaders(buildQfHeader())
                .buildUrl(urlParams)
                .request()
                .assertJValue("status", "0000");
    }

    @Test(
            testName = "会员详情",
            description = "会员详情-childInfos",
            priority = 0,
            groups = {"MemberDetail", "BMember", "Online"}
    )
    public void member_detail_total_childInfos() {

        BMemberChildinfosService bMemberChildinfosService = new BMemberChildinfosService(getUrlData());

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("memberCode", getBaseData("qianfanMemberCode").toString());
        urlParams.put("timestr", String.valueOf(new Date().getTime()));

        bMemberChildinfosService
                .setHeaders(buildQfHeader())
                .buildUrl(urlParams)
                .request()
                .assertJValue("status", "0000");
    }

    @Test(
            testName = "会员详情",
            description = "会员详情-membertrade",
            priority = 0,
            groups = {"MemberDetail", "BMember", "Online"}
    )
    public void member_detail_total_membertrade() {

        BMemberTradeService bMemberTradeService = new BMemberTradeService(getUrlData());

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("memberId", getBaseData("qianfanMemberCode").toString());
        urlParams.put("timestr", String.valueOf(new Date().getTime()));

        bMemberTradeService
                .setHeaders(buildQfHeader())
                .buildUrl(urlParams)
                .request()
                .assertJValue("status", "0000");
    }

    @Test(
            testName = "会员详情",
            description = "会员详情-membertradecode",
            priority = 0,
            groups = {"MemberDetail", "BMember", "Online"}
    )
    public void member_detail_total_membertradecode() {

        BMemberTradeCodeService bMemberTradeCodeService = new BMemberTradeCodeService(getUrlData());

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("timestr", String.valueOf(new Date().getTime()));

        bMemberTradeCodeService
                .setHeaders(buildQfHeader())
                .buildUrl(urlParams)
                .request()
                .assertJValue("status", "0000");
    }

    @Test(
            testName = "会员详情",
            description = "会员详情-membercoupons",
            priority = 0,
            groups = {"MemberDetail", "BMember", "Online"}
    )
    public void member_detail_total_membercoupons() {

        BMemberCouponsService bMemberCouponsService = new BMemberCouponsService(getUrlData());

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("index", "1");
        urlParams.put("limit", "10");
        urlParams.put("orgId", getBaseData("qianfanOrgCode").toString());
        urlParams.put("tenantId", qfLoginModel.getTenantId());
        urlParams.put("mobile", getBaseData("qianfanMemberMobile").toString());
        urlParams.put("source", "2");
        urlParams.put("timestr", String.valueOf(new Date().getTime()));

        bMemberCouponsService
                .setHeaders(buildQfHeader())
                .buildUrl(urlParams)
                .request()
                .assertJValue("status", "200");
    }

    @Test(
            testName = "会员详情",
            description = "会员详情-couponStores",
            priority = 0,
            groups = {"MemberDetail", "BMember", "Online"}
    )
    public void member_detail_total_couponstores() {

        BMemberCouponStoresService bMemberCouponStoresService = new BMemberCouponStoresService(getUrlData());

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("couponNo", "20180318161150");
        urlParams.put("timestr", String.valueOf(new Date().getTime()));

        bMemberCouponStoresService
                .setHeaders(buildQfHeader())
                .buildUrl(urlParams)
                .request()
                .assertJValue("status", "200");
    }
}
