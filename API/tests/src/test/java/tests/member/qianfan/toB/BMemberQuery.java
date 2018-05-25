package tests.member.qianfan.toB;

import com.ffan.qa.biz.services.member.query.*;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2018/1/29.
 */
public class BMemberQuery extends BMemberTestBase{

    @Test(
            testName = "会员列表查询",
            description = "返回会员列表",
            priority = 0,
            groups = {"Query", "BMember"}
    )
    public void queryMemberList() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("tenantId", getTestData("tenantId").toString());
        headers.put("orgcode", getTestData("orgcode").toString());
        headers.put("orgTypeCode", getTestData("orgTypeCode").toString());
        headers.put("token", login());

        Map<String, String> params = new HashMap<>();   // url params
        params.put("pageIndex", getTestData("pageIndex").toString());
//        params.put("pageSize", getTestData("pageSize").toString());
        params.put("pageSize", "10");
        params.put("scope", getTestData("scope").toString());
        params.put("orgType", getTestData("orgType").toString());
        params.put("mobileNo", getTestData("mobileNo").toString());

        // 发起请求并进行验证
        BMemberQueryListService bMemberQueryListService = new BMemberQueryListService(getUrlData());
        bMemberQueryListService.setHeaders(headers);
        bMemberQueryListService
                .buildUrl(params) // 将参数合并到URL中
                .request() // 发起请求
                .assertJValue("status", "0000")   // 验证状态码为200，返回正常状态
                .assertJValue("_metadata.totalCount", "1");
                //.assertJValue("data.mobileNo", getTestData("mobileNo").toString());
    }

    @Test(
            testName = "会员车牌号查询",
            description = "返回车牌号",
            priority = 0,
            groups = {"Query", "BMember"}
    )
    public void queryMemberCar() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("tenantId", getTestData("tenantId").toString());
        headers.put("orgcode", getTestData("orgcode").toString());
        headers.put("orgTypeCode", getTestData("orgTypeCode").toString());
        headers.put("token", login());

        Map<String, String> params = new HashMap<>();   // url params
        params.put("memberCode", getTestData("memberCode").toString());

        // 发起请求并进行验证
        BQueryCarAPI bMemberQueryCarService = new BQueryCarAPI(getUrlData());
        bMemberQueryCarService.setHeaders(headers);
        bMemberQueryCarService
                .buildUrl(params) // 将参数合并到URL中
                .request() // 发起请求
                .assertJValue("status", "0000")   // 验证状态码为200，返回正常状态
                .assertJValue("data.carNo", "沪A12345");
    }

    @Test(
            testName = "会员组织信息查询",
            description = "返回组织信息",
            priority = 0,
            groups = {"Query", "BMember"}
    )
    public void queryMemberOrg() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("tenantId", getTestData("tenantId").toString());
        headers.put("orgcode", getTestData("orgcode").toString());
        headers.put("orgTypeCode", getTestData("orgTypeCode").toString());
        headers.put("token", login());

        // 发起请求并进行验证
        BQueryOrgAPI bMemberQueryOrgService = new BQueryOrgAPI(getUrlData());
        bMemberQueryOrgService.setURL(getTestData("orgTypeCode").toString());
        bMemberQueryOrgService.setHeaders(headers);
        bMemberQueryOrgService
                .request() // 发起请求
                .assertJValue("status", "0000");   // 验证状态码为200，返回正常状态
    }

    @Test(
            testName = "会员详情查询",
            description = "返回详情",
            priority = 0,
            groups = {"Query", "BMember"}
    )
    public void queryMemberDetails() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("tenantId", getTestData("tenantId").toString());
        headers.put("orgcode", getTestData("orgcode").toString());
        headers.put("orgTypeCode", getTestData("orgTypeCode").toString());
        headers.put("token", login());

        Map<String, String> params = new HashMap<>();
        params.put("memberCode", getTestData("memberCode").toString());
        params.put("plazaId", getTestData("plazaId").toString());


        // 发起请求并进行验证
        BQueryDetailsAPI bMemberQueryDetailsAPI = new BQueryDetailsAPI(getUrlData());
        bMemberQueryDetailsAPI.setHeaders(headers);
        bMemberQueryDetailsAPI
                .buildUrl(params)
                .request() // 发起请求
                .assertJValue("status", "0000") ;  // 验证状态码为200，返回正常状态
    }

    @Test(
            testName = "会员积分账户信息",
            description = "返回积分账户信息",
            priority = 0,
            groups = {"Query", "BMember"}
    )
    public void queryMemberAccount() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("tenantId", getTestData("tenantId").toString());
        headers.put("orgcode", getTestData("orgcode").toString());
        headers.put("orgTypeCode", getTestData("orgTypeCode").toString());
        headers.put("token", login());

        Map<String, String> params = new HashMap<>();
        params.put("memberCode", getTestData("memberCode").toString());

        // 发起请求并进行验证
        BQueryAccountAPI bMemberQueryAccountAPI = new BQueryAccountAPI(getUrlData());
        bMemberQueryAccountAPI.setHeaders(headers);
        bMemberQueryAccountAPI
                .buildUrl(params)
                .request() // 发起请求
                .assertJValue("status", "0000")   // 验证状态码为200，返回正常状态
                .assertJValue("data.carNo", "沪A12345");
    }

    @Test(
            testName = "会员停车信息",
            description = "返回停车信息",
            priority = 0,
            groups = {"Query", "BMember"}
    )
    public void queryMemberPark() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("tenantId", getTestData("tenantId").toString());
        headers.put("orgcode", getTestData("orgcode").toString());
        headers.put("orgTypeCode", getTestData("orgTypeCode").toString());
        headers.put("token", login());

        Map<String, String> params = new HashMap<>();
        params.put("memberCode", getTestData("memberCode").toString());

        // 发起请求并进行验证
        BQueryParkAPI bMemberQueryParkAPI = new BQueryParkAPI(getUrlData());
        bMemberQueryParkAPI.setHeaders(headers);
        bMemberQueryParkAPI
                .buildUrl(params)
                .request() // 发起请求
                .assertJValue("status", "0000") ;  // 验证状态码为200，返回正常状态
    }

    @Test(
            testName = "会员停车信息",
            description = "返回停车信息",
            priority = 0,
            groups = {"Query", "BMember"}
    )
    public void queryMemberWifi() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("tenantId", getTestData("tenantId").toString());
        headers.put("orgcode", getTestData("orgcode").toString());
        headers.put("orgTypeCode", getTestData("orgTypeCode").toString());
        headers.put("token", login());

        Map<String, String> params = new HashMap<>();
        params.put("memberCode", getTestData("memberCode").toString());

        // 发起请求并进行验证
        BQueryWifiAPI bMemberQueryWifiAPI = new BQueryWifiAPI(getUrlData());
        bMemberQueryWifiAPI.setHeaders(headers);
        bMemberQueryWifiAPI
                .buildUrl(params)
                .request() // 发起请求
                .assertJValue("status", "0000") ;  // 验证状态码为200，返回正常状态
    }

    @Test(
            testName = "会员的子女信息",
            description = "返回子女信息",
            priority = 0,
            groups = {"Query", "BMember"}
    )
    public void queryMemberChild() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("tenantId", getTestData("tenantId").toString());
        headers.put("orgcode", getTestData("orgcode").toString());
        headers.put("orgTypeCode", getTestData("orgTypeCode").toString());
        headers.put("token", login());

        Map<String, String> params = new HashMap<>();
        params.put("memberCode", getTestData("memberCode").toString());

        // 发起请求并进行验证
        BQueryChildAPI bMemberQueryChildAPI = new BQueryChildAPI(getUrlData());
        bMemberQueryChildAPI.setHeaders(headers);
        bMemberQueryChildAPI
                .buildUrl(params)
                .request() // 发起请求
                .assertJValue("status", "0000") ;  // 验证状态码为200，返回正常状态
    }

    @Test(
            testName = "会员的积分流水",
            description = "返回积分流水信息",
            priority = 0,
            groups = {"Query", "BMember"}
    )
    public void queryMemberPoint() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("tenantId", getTestData("tenantId").toString());
        headers.put("orgcode", getTestData("orgcode").toString());
        headers.put("orgTypeCode", getTestData("orgTypeCode").toString());
        headers.put("token", login());

        Map<String, String> params = new HashMap<>();
        params.put("pageIndex", getTestData("pageIndex").toString());
        params.put("pageSize", getTestData("pageSize").toString());
        params.put("memberCode", getTestData("memberCode").toString());

        // 发起请求并进行验证
        BQueryPointAPI bMemberQueryPointAPI = new BQueryPointAPI(getUrlData());
        bMemberQueryPointAPI.setHeaders(headers);
        bMemberQueryPointAPI
                .buildUrl(params)
                .request() // 发起请求
                .assertJValue("status", "0000") ;  // 验证状态码为200，返回正常状态
    }

    @Test(
            testName = "会员的交易明细",
            description = "返回交易明细信息",
            priority = 0,
            groups = {"Query", "BMember"}
    )
    public void queryMemberTrade() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("tenantId", getTestData("tenantId").toString());
        headers.put("orgcode", getTestData("orgcode").toString());
        headers.put("orgTypeCode", getTestData("orgTypeCode").toString());
        headers.put("token", login());

        Map<String, String> params = new HashMap<>();
        params.put("memberCode", getTestData("memberCode").toString());
        params.put("pageIndex", getTestData("pageIndex").toString());
        params.put("pageSize", getTestData("pageSize").toString());

        // 发起请求并进行验证
        BQueryTradeAPI bMemberQueryTradeAPI = new BQueryTradeAPI(getUrlData());
        bMemberQueryTradeAPI.setHeaders(headers);
        bMemberQueryTradeAPI
                .buildUrl(params)
                .request() // 发起请求
                .assertJValue("status", "0000") ;  // 验证状态码为200，返回正常状态
    }

    @Test(
            testName = "会员的交易明细",
            description = "返回交易明细信息",
            priority = 0,
            groups = {"Query", "BMember"}
    )
    public void queryMemberVisit() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("tenantId", getTestData("tenantId").toString());
        headers.put("orgcode", getTestData("orgcode").toString());
        headers.put("orgTypeCode", getTestData("orgTypeCode").toString());
        headers.put("token", login());

        Map<String, String> params = new HashMap<>();
        params.put("memberId", getTestData("memberId").toString());
        params.put("pageIndex", getTestData("pageIndex").toString());
        params.put("pageSize", getTestData("pageSize").toString());

        // 发起请求并进行验证
        BQueryVisitAPI bMemberQueryVisitAPI = new BQueryVisitAPI(getUrlData());
        bMemberQueryVisitAPI.setHeaders(headers);
        bMemberQueryVisitAPI
                .buildUrl(params)
                .request() // 发起请求
                .assertJValue("status", "0000") ;  // 验证状态码为200，返回正常状态
    }

    @Test(
            testName = "会员的券账户",
            description = "返回券账户信息",
            priority = 0,
            groups = {"Query", "BMember"}
    )
    public void queryMemberCoupon() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("tenantId", getTestData("tenantId").toString());
        headers.put("orgcode", getTestData("orgcode").toString());
        headers.put("orgTypeCode", getTestData("orgTypeCode").toString());
        headers.put("token", login());

        Map<String, String> params = new HashMap<>();
        params.put("mobile", getTestData("mobile").toString());
        params.put("orgId", getTestData("orgId").toString());
        params.put("tenantId", getTestData("tenantId").toString());
        params.put("index", getTestData("index").toString());
        params.put("limit", getTestData("limit").toString());

        // 发起请求并进行验证
        BQueryCouponAPI bMemberQueryCouponAPI = new BQueryCouponAPI(getUrlData());
        bMemberQueryCouponAPI.setHeaders(headers);
        bMemberQueryCouponAPI
                .buildUrl(params)
                .request() // 发起请求
                .assertJValue("status", "0000") ;  // failed 提示header里没有token
    }


    @Test(
            testName = "会员的券账户",
            description = "返回券账户信息",
            priority = 0,
            groups = {"Query", "BMember"}
    )
    public void queryMemberStore() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("tenantId", getTestData("tenantId").toString());
        headers.put("orgcode", getTestData("orgcode").toString());
        headers.put("orgTypeCode", getTestData("orgTypeCode").toString());
        headers.put("token", login());

        Map<String, String> params = new HashMap<>();
        params.put("memberId", getTestData("memberId").toString());
        params.put("pageIndex", getTestData("pageIndex").toString());
        params.put("pageSize", getTestData("pageSize").toString());

        // 发起请求并进行验证
        BQueryStoreAPI bMemberQueryStoreAPI = new BQueryStoreAPI(getUrlData());
        bMemberQueryStoreAPI.setHeaders(headers);
        bMemberQueryStoreAPI
                .buildUrl(params)
                .request() // 发起请求
                .assertJValue("status", "0000") ;
    }
}
