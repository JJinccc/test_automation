package tests.coupon.toC;

import com.alibaba.fastjson.JSON;
import com.ffan.qa.biz.services.common.CWeXmtCitiesService;
import com.ffan.qa.biz.services.coupon.*;
import com.ffan.qa.common.Logger;
import com.ffan.qa.common.constants.RedisKeys;
import com.ffan.qa.common.model.runner.DiffResultItem;
import com.ffan.qa.common.model.runner.DiffStockItem;
import com.ffan.qa.common.type.AdvertisementType;
import com.ffan.qa.common.type.CouponType;
import com.ffan.qa.errands.CouponErrands;
import com.ffan.qa.network.APIResponse;
import com.ffan.qa.utils.*;
import jersey.repackaged.com.google.common.base.Joiner;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.coupon.CouponTestBase;

import java.net.URLEncoder;
import java.util.*;

public class CCouponListTests extends CouponTestBase {
    @Test(
            testName = "广场券列表默认请求",
            description = "广场券列表默认请求，页码1，数量10，验证返回正常，并能够返回券数据",
            priority = 0,
            groups = {"Basics", "wxCoupon", "Online", "SIT"},
            dataProvider = "data"
    )
    public void plaza_coupons_default(Map<String, Object> data) {
        String plazaId = data.get("plazaId").toString();
        String plazaName = data.get("plazaName").toString();
        String city = data.get("city").toString();

        // 参数构造 https://api.sit.ffan.com/wechatxmt/v5/plaza/coupons?adSpaceId=couponList&plazaId=1000343&channelId=1003&type=1001&pageNum=1&pageSize=10
        String adSpaceId = "couponList";
        //String plazaId = getBaseData("wxPlazaId").toString();
        String channelId = "1003";
        String typeId = "1001";
        Map<String, String> params = new HashMap<>();
        params.put("adSpaceId", adSpaceId);
        params.put("plazaId", plazaId);
        params.put("channelId", channelId);
        params.put("type", typeId);
        params.put("pageNum", "1");
        params.put("pageSize", "10");

        // 发起请求并进行验证
        CPlaCouponListService couponListService = new CPlaCouponListService(getUrlData());
        APIResponse resp = couponListService
                .buildUrl(params) // 将参数合并到URL中
                .request() // 发起请求
                .assertJValue("status", "200", String.format("状态码返回非200，小程序首页列表%s-%s存在白页风险", city, plazaName)) // 验证状态码为200，返回正常状态
                .assertJValue("data.adSpaceId", adSpaceId, String.format("数据data.adSpaceId与请求不符，小程序首页列表%s-%s数据正确性风险", city, plazaName))
                .assertJValue("data.channelId", channelId, String.format("数据data.channelId与请求不符，小程序首页列表%s-%s数据正确性风险", city, plazaName))
                .assertJValue("data.plazaId", plazaId, String.format("数据data.plazaId与请求不符，小程序首页列表%s-%s数据正确性风险", city, plazaName))
                .assertJValue("data.type", typeId, String.format("数据data.type与请求不符，小程序首页列表%s-%s数据正确性风险", city, plazaName))
                .assertJNotEmpty("data.total", String.format("数据data.total数据异常，小程序首页列表%s-%s存在白页风险", city, plazaName)) // 验证券总量有值并大于0
                ;

        if (plazaId.equals("1105299")) {
            Integer total = Integer.parseInt(resp.getJValue("data.total").toString());
            if (total > 0) {
                resp
                        .assertJListNotEmpty("data.tagList", String.format("数据data.tag标签数据，小程序首页列表%s-%s存在白页风险", city, plazaName))
                        .assertJListNotEmpty("data.resource", String.format("%s-%s，小程序列表接口数据data.resource返回数据为空，存在白页风险", city, plazaName));
            } else {
                resp
                        .assertJListNullOrEmpty("data.tagList", String.format("%s-%s，当data.total=0时，小程序列表接口换页数据data.tagList返回数据不为Null", city, plazaName))
                        .assertJListNullOrEmpty("data.resource", String.format("%s-%s，当data.total=0时，小程序列表接口换页数据data.resource返回数据不为Null", city, plazaName));
            }
        } else {
            resp
                    .assertJListNotEmpty("data.tagList", String.format("数据data.tag标签数据，小程序首页列表%s-%s存在白页风险", city, plazaName))
                    .assertJListNotEmpty("data.resource", String.format("%s-%s，小程序列表接口数据data.resource返回数据为空，存在白页风险", city, plazaName));
        }

    }

    @Test(
            testName = "广场券列表翻页请求",
            description = "广场券列表翻页请求，页码2，数量1，验证返回正常，并能够返回至少1条券数据",
            priority = 2,
            groups = {"wxCoupon", "Online", "SIT"},
            dataProvider = "data"
    )
    public void plaza_coupons_next_page(Map<String, Object> data) {
        String plazaId = data.get("plazaId").toString();
        String plazaName = data.get("plazaName").toString();
        String city = data.get("city").toString();

        // 参数构造 https://api.sit.ffan.com/wechatxmt/v5/plaza/coupons?adSpaceId=couponList&plazaId=1000343&channelId=1003&type=1001&pageNum=2&pageSize=1
        String adSpaceId = "couponList";
        String channelId = "1003";
        String typeId = "1001";
        Map<String, String> params = new HashMap<>();
        params.put("adSpaceId", adSpaceId);
        params.put("plazaId", plazaId);
        params.put("channelId", channelId);
        params.put("type", typeId);
        params.put("pageNum", "2");
        params.put("pageSize", "10");

        // 发起请求并进行验证
        CPlaCouponListService couponListService = new CPlaCouponListService(getUrlData());
        APIResponse resp = couponListService
                .buildUrl(params) // 将参数合并到URL中
                .request() // 发起请求
                .assertJValue("status", "200", String.format("状态码返回非200，小程序首页列表%s-%s翻页存在风险", city, plazaName)) // 验证状态码为200，返回正常状态
                .assertJValue("data.adSpaceId", adSpaceId, String.format("数据data.adSpaceId与请求不符，小程序首页列表%s-%s数据正确性风险", city, plazaName))
                .assertJValue("data.channelId", channelId, String.format("数据data.channelId与请求不符，小程序首页列表%s-%s数据正确性风险", city, plazaName))
                .assertJValue("data.plazaId", plazaId, String.format("数据data.plazaId与请求不符，小程序首页列表%s-%s数据正确性风险", city, plazaName))
                .assertJValue("data.type", typeId, String.format("数据data.type与请求不符，小程序首页列表%s-%s数据正确性风险", city, plazaName))
                .assertJNotEmpty("data.total", String.format("数据data.total数据异常，小程序首页列表%s-%s翻页存在风险", city, plazaName)); // 验证券总量有值并大于0

        Integer total = Integer.parseInt(resp.getJValue("data.total").toString());
        if (total > 0) {
            resp
                    .assertJListNotEmpty("data.resource", String.format("%s-%s，小程序列表接口换页数据data.resource返回数据为空，存在白页风险", city, plazaName)); // 验证券列表返回至少1条数据
        } else {
            resp
                    .assertJListNullOrEmpty("data.resource", String.format("%s-%s，当data.total=0时，小程序列表接口换页数据data.resource返回数据不为Null", city, plazaName));
        }
    }

    @Test(
            testName = "广场券列表类型筛选",
            description = "广场券列表类型帅选测试，验证餐饮、超时、娱乐等Tag标签晒选",
            priority = 0,
            groups = {"wxCoupon", "Online", "SIT"},
            dataProvider = "data"
    )
    public void plaza_coupons_bizTypes(Map<String, Object> data) {
        String plazaId = data.get("plazaId").toString();
        String plazaName = data.get("plazaName").toString();
        String city = data.get("city").toString();

        // 参数构造 https://api.sit.ffan.com/wechatxmt/v5/plaza/coupons?adSpaceId=couponList&plazaId=1000343&channelId=1003&type=1001&pageNum=2&pageSize=10
        String adSpaceId = "couponList";
        String channelId = "1003";
        String typeId = "1001";
        Map<String, String> params = new HashMap<>();
        params.put("adSpaceId", adSpaceId);
        params.put("plazaId", plazaId);
        params.put("channelId", channelId);
        params.put("type", typeId);
        params.put("pageNum", "1");
        params.put("pageSize", "10");

        // 发起全部类型请求，获取TagList
        CPlaCouponListService couponListService = new CPlaCouponListService(getUrlData());
        APIResponse resp = couponListService
                .buildUrl(params) // 将参数合并到URL中
                .request() // 发起请求
                .assertJValue("status", "200", "请求Coupons接口失败，无法获取TagList列表"); // 验证状态码为200，返回正常状态

        // 遍历TagList，逐个请求并进行验证
        ArrayList<Object> tagList = (ArrayList<Object>) resp.getJValue("data.tagList");
        for (Object obj : tagList) {
            // 获取TagId 和 TagName
            Map<String, Object> tag = (LinkedHashMap<String, Object>) obj;
            String id = tag.get("tagId").toString();
            String name = tag.get("tagName").toString();
            if (params.containsKey("bizType")) {
                params.replace("bizType", id);
            } else {
                params.put("bizType", id);
            }

            // 发起请求并进行验证
            CPlaCouponListService bizCouponsService = new CPlaCouponListService(getUrlData());
            APIResponse bizResp = bizCouponsService
                    .buildUrl(params)
                    .request()
                    .assertJValue("status", "200", String.format("小程序%s-%s类型筛选页面数据接口（类型为%s的券列表）返回状态码非200，存在白页风险", city, plazaName, name))
                    .assertJListNotEmpty("data.resource", String.format("小程序%s-%s类型筛选页面数据接口（类型为%s的券列表）返回data.resource为空，存在白页风险", city, plazaName, name));
            ArrayList<Object> resources = (ArrayList<Object>) bizResp.getJValue("data.resource");
            for (Object or : resources) {
                // 验证返回的券对象TagId都包含过滤条件的bizType
                Map<String, Object> coupon = (LinkedHashMap<String, Object>) or;
                Assert.assertNotNull(coupon.get("bizTagId"), String.format("%s-%s，bizTagId为空，无法进行后续验证", city, plazaName));
                String bizTagId = coupon.get("bizTagId").toString();
                String[] tl = bizTagId.split(",");
                Assert.assertTrue(StringUtil.contains(tl, id),
                        String.format("%s-%s, 类型%s的券列表返回中，券%s(%s)的bizTagId不包含%s, bizTagId的值为%s. 请求地址：%s", city, plazaName, name, coupon.get("title"), coupon.get("couponNo").toString(), id, bizTagId, bizCouponsService.getUrl()));
            }
        }
    }

    @Test(
            testName = "新用户领券",
            description = "验证新用户领券接口返回正常，并能够返回默认3条券数据",
            priority = 1,
            groups = {"wxCoupon", "Online", "SIT"},
            dataProvider = "data"
    )
    public void new_user_coupons(Map<String, Object> data) {
        String plazaId = data.get("plazaId").toString();
        String plazaName = data.get("plazaName").toString();
        String city = data.get("city").toString();

        // 参数构造 https://api.sit.ffan.com/wechatxmt/v5/plaza/coupons?channelId=1003&plazaId=1000343&adSpaceId=newUserReward&type=1001&pageNum=1&pageSize=3
        Map<String, String> params = new HashMap<>();
        params.put("channelId", "1003");
        params.put("plazaId", plazaId);
        params.put("adSpaceId", "newUserReward");
        params.put("type", "1001");
        params.put("pageNum", "1");
        params.put("pageSize", "3");

        // 发起请求并进行验证
        CPlaCouponListService couponListService = new CPlaCouponListService(getUrlData());
        APIResponse resp = couponListService
                .buildUrl(params) // 将参数合并到URL中
                .request() // 发起请求
                .assertJValue("status", "200", String.format("%s-%s，新用户领券接口返回状态码非200，存在白页风险", city, plazaName)) // 验证状态码为200，返回正常状态
                .assertJNotEmpty("data.total", String.format("%s-%s，新用户领券接口返回data.total为空", city, plazaName));
        Integer total = Integer.parseInt(resp.getJValue("data.total").toString());
        if (total > 0) {
            resp.assertJListNotEmpty("data.resource", String.format("%s-%s，新用户领券接口数据data.resource返回数据为空，存在白页风险", city, plazaName)); // 验证券列表返回至少1条数据
        } else {
            resp.assertJListNullOrEmpty("data.resource", String.format("%s-%s，当data.total=0时，新用户领券接口数据data.resource返回数据不为Null，存在白页风险", city, plazaName));
        }
    }

    @Test(
            testName = "进行中的限时抢购券列表",
            description = "正在进行中的限时抢购列表",
            priority = 1,
            groups = {"wxCoupon", "Online", "SIT"},
            dataProvider = "data"
    )
    public void flash_sale_coupons_started(Map<String, Object> data) {
        String plazaId = data.get("plazaId").toString();
        String plazaName = data.get("plazaName").toString();
        String city = data.get("city").toString();

        // 参数构造 https://api.sit.ffan.com/wechatxmt/v5/plaza/coupons?adSpaceId=flashSale&plazaId=1000343&channelId=1003&type=1001&pageNum=1&pageSize=10&expressionType=1
        String adSpaceId = "flashSale";
        String channelId = "1003";
        String typeId = "1001";
        String expressionType = "1"; // 闪购进行中
        Map<String, String> params = new HashMap<>();
        params.put("adSpaceId", adSpaceId);
        params.put("plazaId", plazaId);
        params.put("channelId", channelId);
        params.put("type", typeId);
        params.put("pageNum", "1");
        params.put("pageSize", "10");
        params.put("expressionType", expressionType);

        // TODO: 通过接口生成限时抢购券

        // 发起请求并进行验证
        CPlaCouponListService couponListService = new CPlaCouponListService(getUrlData());
        APIResponse resp = couponListService
                .buildUrl(params) // 将参数合并到URL中
                .request() // 发起请求
                .assertJValue("status", "200", String.format("%s-%s，进行中的闪购接口状态码返回非200，存在白页风险", city, plazaName)) // 验证状态码为200，返回正常状态
                .assertJValue("data.adSpaceId", adSpaceId, String.format("%s-%s，进行中的闪购接口data.adSpaceId与传入参数不符，存在数据准确性风险", city, plazaName))
                .assertJValue("data.channelId", channelId, String.format("%s-%s，进行中的闪购接口data.channelId与传入参数不符，存在数据准确性风险", city, plazaName))
                .assertJValue("data.plazaId", plazaId, String.format("%s-%s，进行中的闪购接口data.plazaId与传入参数不符，存在数据准确性风险", city, plazaName))
                .assertJValue("data.type", typeId, String.format("%s-%s，进行中的闪购接口data.type与传入参数不符，存在数据准确性风险", city, plazaName))
                .assertJNotEmpty("data.total", String.format("%s-%s，进行中的闪购接口data.total为空", city, plazaName)); // 验证券总量有值并大于0

        if (resp.getValue("data.total").toString().equals("0")) {
            resp
                    .assertJSize("data.tagList", 0, String.format("%s-%s，进行中的闪购接口data.tagList在total为0时应为[]", city, plazaName))
                    .assertJListNullOrEmpty("data.resource", String.format("%s-%s，进行中的闪购接口data.resource在total为0时应为null", city, plazaName));
        } else {
            resp
                    .assertJListNotEmpty("data.tagList", String.format("%s-%s，进行中的闪购接口data.tagList为空", city, plazaName)) // 验证标签列表存在
                    .assertJListNotEmpty("data.resource", String.format("%s-%s，进行中的闪购接口data.resource为空，存在白页风险", city, plazaName)); // 验证券列表返回10条数据
        }
    }

    @Test(
            testName = "未开始的限时抢购券列表",
            description = "未开始的限时抢购列表",
            priority = 1,
            groups = {"wxCoupon", "Online", "SIT"},
            dataProvider = "data"
    )
    public void flash_sale_coupons_holding(Map<String, Object> data) {
        // 参数构造 https://api.sit.ffan.com/wechatxmt/v5/plaza/coupons?adSpaceId=flashSale&plazaId=1000343&channelId=1003&type=1001&pageNum=1&pageSize=10&expressionType=2
        String adSpaceId = "flashSale";
        String channelId = "1003";
        String typeId = "1001";
        String expressionType = "2"; // 闪购未开始
        Map<String, String> params = new HashMap<>();
        params.put("adSpaceId", adSpaceId);
        params.put("plazaId", data.get("plazaId").toString());
        params.put("channelId", channelId);
        params.put("type", typeId);
        params.put("pageNum", "1");
        params.put("pageSize", "10");
        params.put("expressionType", expressionType);

        // TODO: 通过接口生成限时抢购券

        // 发起请求并进行验证
        CPlaCouponListService couponListService = new CPlaCouponListService(getUrlData());
        couponListService
                .buildUrl(params) // 将参数合并到URL中
                .request() // 发起请求
                .assertJValue("status", "200", String.format("%s-%s，未开始的闪购接口状态码返回非200", data.get("city").toString(), data.get("plazaName").toString())) // 验证状态码为200，返回正常状态
                .assertJValue("data.adSpaceId", adSpaceId, String.format("%s-%s，未开始的闪购接口data.adSpaceId与传入参数不符，存在数据准确性风险", data.get("city").toString(), data.get("plazaName").toString()))
                .assertJValue("data.channelId", channelId, String.format("%s-%s，未开始的闪购接口data.channelId与传入参数不符，存在数据准确性风险", data.get("city").toString(), data.get("plazaName").toString()))
                .assertJValue("data.plazaId", data.get("plazaId").toString(), String.format("%s-%s，未开始的闪购接口data.plazaId与传入参数不符，存在数据准确性风险", data.get("city").toString(), data.get("plazaName").toString()))
                .assertJValue("data.type", typeId, String.format("%s-%s，未开始的闪购接口data.type与传入参数不符，存在数据准确性风险", data.get("city").toString(), data.get("plazaName").toString()))
                .assertJNotEmpty("data.total", String.format("%s-%s，未开始的闪购接口data.total为空", data.get("city").toString(), data.get("plazaName").toString())); // 验证券总量有值
    }

    @Test(
            testName = "我的券-未使用",
            description = "我未使用的券列表",
            priority = 1,
            groups = {"wxCoupon", "SIT"}
    )
    public void my_coupons_active() {
        // 参数构造 https://api.sit.ffan.com/wechatxmt/v1/member/coupons?cookieStr=cookieStr&plazaId=1000343&memberId=15000000000992599&status=3&offset=0&limit=10
        String plazaId = getBaseData("wxPlazaId").toString();
        String status = "3"; // 未使用
        Map<String, String> params = constructMyCouponsHeader(plazaId, status);

        // TODO: 通过接口生成未使用的券
        //CouponErrands.createPublishedCoupon(CouponType.GroupCoupon, AdvertisementType.CouponList);

        // 发起请求并进行验证
        CMbCouponListService couponListService = new CMbCouponListService(getUrlData());
        APIResponse resp = couponListService
                .buildUrl(params) // 将参数合并到URL中
                .request() // 发起请求
                .assertJValue("status", "200") // 验证状态码为200，返回正常状态
                .assertJSizeBigger("coupons", 0, "券列表返回数量为空") // 验证未使用的券有数据
                .assertJValueBigger("totalCount", 0, "未使用券的数量异常"); // 验证券总量有值并大于0
        ArrayList<Object> coupons = (ArrayList<Object>) resp.getJValue("coupons");
        for (Object obj :
                coupons) {
            Map<String, Object> c = (LinkedHashMap<String, Object>) obj;
            // 验证券的所有人
            Assert.assertEquals(c.get("memberId").toString(), cLoginModel.getMemberId());
            // 验证券的状态
            Assert.assertEquals(c.get("couponStatus").toString(), status);
        }
    }

    public Map<String, String> constructMyCouponsHeader(String plazaId, String status) {
        // 模拟登陆
        wxLogin(getBaseData("wxPhone").toString(), getBaseData("wxOpenId").toString(), plazaId);

        // 参数构造 https://api.sit.ffan.com/wechatxmt/v1/member/coupons?cookieStr=cookieStr&plazaId=1000343&memberId=15000000000992599&status=3&offset=0&limit=10
        try{
            Map<String, String> params = new HashMap<>();
            params.put("cookieStr", URLEncoder.encode(cLoginModel.getCookieStr(), "utf-8"));
            params.put("plazaId", plazaId);
            params.put("memberId", cLoginModel.getMemberId());
            params.put("status", status);
            params.put("offset", "0");
            params.put("limit", "10");
            return params;
        } catch (Exception ex) {
            Assert.fail("构造我的券接口Header过程中报错：" + ex.getMessage());
            return null;
        }
    }

//    @Test(
//            testName = "券数据校验",
//            description = "比对小程序端和千帆后台的数据，保证数据一致性",
//            priority = 0,
//            groups = {"wxCoupon", "OnlineDataCheck"},
//            dataProvider = "data"
//    )
//    public void check_coupons_data(Map<String, Object> data) {
//        Assert.assertEquals(testEnv, "prod", "本用例仅支持线上执行!");
//
//        List<DiffResultItem> diffList = new ArrayList<>();
//
//        // 千帆平台登录
//        qfLogin(data.get("user").toString(), StringUtil.isNullOrEmpty(data.get("enPwd").toString()) ? EncryptUtil.getMD5(data.get("pwd").toString()) : data.get("enPwd").toString());
//
//        List<Object> bCoupons = new ArrayList<>();
//        Integer pageNum = 1;
//        Integer pageSize = 1000;
//
//        // 券列表数据
//        APIResponse bResp = requestBCouponList(data.get("plazaId").toString(), data.get("plazaName").toString(), data.get("user").toString(), pageNum, pageSize, "2");
//        if (null != (ArrayList<Object>) bResp.getJValue("data")) {
//            bCoupons.addAll((ArrayList<Object>) bResp.getJValue("data"));
//        }
//
//        List<Map<String, Object>> bCouponList = convertCoupons(bCoupons);
//
//        // 比较券列表
//        pageNum = 1;
//        pageSize = 1000;
//        List<Object> cListCoupons = new ArrayList<>();
//        List<Integer> bStocks = new ArrayList<>();
//        while (true) {
//            APIResponse cResp = requestCCouponList(data.get("plazaId").toString(), data.get("plazaName").toString(), pageNum, pageSize);
//            Integer total = Integer.parseInt(cResp.getJValue("data.total").toString());
//            List<Object> resources = (ArrayList<Object>) cResp.getJValue("data.resource");
//            if (null != resources) {
//                bStocks.addAll(getBStocks(resources));
//                cListCoupons.addAll(resources);
//            }
//            if (pageNum * pageSize >= total) {
//                break;
//            }
//            pageNum++;
//        }
//        compare(data.get("plazaId").toString(), "couponList", bCouponList, cListCoupons);
//
//        DiffResultItem listDiff = compare(bCouponList, cListCoupons, bStocks);
//        listDiff.setSortName("券列表数据比对");
//        if (!listDiff.getIsSame()) {
//            diffList.add(listDiff);
//        }
//
//        // 新用户专享数据
//        bResp = requestBCouponList(data.get("plazaId").toString(), data.get("plazaName").toString(), data.get("user").toString(), pageNum, pageSize, "1");
//        bCoupons.clear();
//        if (null != (ArrayList<Object>) bResp.getJValue("data")) {
//            bCoupons.addAll((ArrayList<Object>) bResp.getJValue("data"));
//        }
//
//        bCouponList = convertCoupons(bCoupons);
//
//        // 比较新用户专享
//        pageNum = 1;
//        List<Object> cNewUserCoupons = new ArrayList<>();
//        bStocks.clear();
//        while (true) {
//            APIResponse cResp = requestCNewUserCouponList(data.get("plazaId").toString(), data.get("plazaName").toString(), pageNum, pageSize);
//            Integer total = Integer.parseInt(cResp.getJValue("data.total").toString());
//            List<Object> resources = (ArrayList<Object>) cResp.getJValue("data.resource");
//            if (null != resources && resources.size() > 0) {
//                bStocks.addAll(getBStocks(resources));
//                cNewUserCoupons.addAll(resources);
//            }
//            if (pageNum * pageSize >= total) {
//                break;
//            }
//            pageNum++;
//        }
//
//        DiffResultItem newUserDiff = compare(bCouponList, cNewUserCoupons, bStocks);
//        newUserDiff.setSortName("新用户专享数据比对");
//        if (!newUserDiff.getIsSame()) {
//            diffList.add(newUserDiff);
//        }
//
//        // 限时抢购数据
//        bResp = requestBCouponList(data.get("plazaId").toString(), data.get("plazaName").toString(), data.get("user").toString(), pageNum, pageSize, "3");
//        bCoupons.clear();
//        if (null != (ArrayList<Object>) bResp.getJValue("data")) {
//            bCoupons.addAll((ArrayList<Object>) bResp.getJValue("data"));
//        }
//
//        bCouponList = convertCoupons(bCoupons);
//
//        // 比较限时抢购
//        pageNum = 1;
//        List<Object> cFlashSaleCoupons = new ArrayList<>();
//        bStocks.clear();
//        while (true) {
//            APIResponse cResp = requestCFlashSaleCouponList(data.get("plazaId").toString(), data.get("plazaName").toString(), pageNum, pageSize);
//            Integer total = Integer.parseInt(cResp.getJValue("data.total").toString());
//            List<Object> resources = (ArrayList<Object>) cResp.getJValue("data.resource");
//            if (null != resources) {
//                bStocks.addAll(getBStocks(resources));
//                cFlashSaleCoupons.addAll(resources);
//            }
//            if (pageNum * pageSize >= total) {
//                break;
//            }
//            pageNum++;
//        }
//
//        DiffResultItem flashSaleDiff = compare(bCouponList, cFlashSaleCoupons, bStocks);
//        flashSaleDiff.setSortName("限时抢购数据比对");
//        if (!flashSaleDiff.getIsSame()) {
//            diffList.add(flashSaleDiff);
//        }
//
////        // 比对2折专区
////        pageNum = 1;
////        List<Object> cDiscountCoupons = new ArrayList<>();
////        bStocks.clear();
////        while (true) {
////            APIResponse cResp = requestCDiscountCouponList(data.get("plazaId").toString(), data.get("plazaName").toString(), pageNum, pageSize);
////            Integer total = Integer.parseInt(cResp.getJValue("data.total").toString());
////            List<Object> resources = (ArrayList<Object>) cResp.getJValue("data.resource");
////            if (null != resources) {
////                bStocks.addAll(getBStocks(resources));
////                cDiscountCoupons.addAll(resources);
////            }
////            if (pageNum * pageSize >= total) {
////                break;
////            }
////            pageNum++;
////        }
////
////        DiffResultItem discountDiff = compare(groupedCoupons.get("discount"), cDiscountCoupons, bStocks);
////        discountDiff.setSortName("2折专区数据比对");
////        if (!discountDiff.getIsSame()) {
////            diffList.add(discountDiff);
////        }
//
//        if (diffList.size() > 0) {
//            DiffResultUtil.writeCouponResult(data.get("city").toString(), data.get("plazaId").toString(), data.get("plazaName").toString(), diffList, true);
//            Assert.fail(String.format("%s-%s，券数据前后台不一致", data.get("city").toString(), data.get("plazaName").toString()));
//        } else {
//            DiffResultUtil.writeCouponResult(data.get("city").toString(), data.get("plazaId").toString(), data.get("plazaName").toString(), diffList, false);
//        }
//    }

    @Test(
            testName = "券数据B、C端比对",
            description = "比对小程序端和千帆后台的数据，保证数据一致性",
            priority = 0,
            groups = {"wxCoupon", "OnlineDataCheck"},
            dataProvider = "data"
    )
    public void check_coupons(Map<String, Object> data) {
        Assert.assertEquals(testEnv, "prod", "本用例仅支持线上执行!");

        // 千帆平台登录
        qfLogin(data.get("user").toString(), StringUtil.isNullOrEmpty(data.get("enPwd").toString()) ? EncryptUtil.getMD5(data.get("pwd").toString()) : data.get("enPwd").toString());

        List<Object> bCoupons;
        Integer pageNum = 1;
        Integer pageSize = 1000;

        // 券列表数据
        bCoupons = requestBCouponList(data.get("plazaId").toString(), data.get("plazaName").toString(), data.get("user").toString(), "2");
        List<Map<String, Object>> bCouponList = convertCoupons(bCoupons);

        // 比较券列表
        List<Object> cListCoupons = new ArrayList<>();
        List<Integer> bStocks = new ArrayList<>();
        while (true) {
            APIResponse cResp = requestCCouponList(data.get("plazaId").toString(), data.get("plazaName").toString(), pageNum, pageSize);
            Integer total = Integer.parseInt(cResp.getJValue("data.total").toString());
            List<Object> resources = (ArrayList<Object>) cResp.getJValue("data.resource");
            if (null != resources) {
                cListCoupons.addAll(resources);
            }
            if (pageNum * pageSize >= total) {
                break;
            }
            pageNum++;
        }
        compare(data.get("plazaId").toString(), "couponList", bCouponList, cListCoupons);

        // 新用户专享数据
        bCoupons.clear();
        bCoupons = requestBCouponList(data.get("plazaId").toString(), data.get("plazaName").toString(), data.get("user").toString(), "1");
        bCouponList = convertCoupons(bCoupons);

        // 比较新用户专享
        pageNum = 1;
        List<Object> cNewUserCoupons = new ArrayList<>();
        bStocks.clear();
        while (true) {
            APIResponse cResp = requestCNewUserCouponList(data.get("plazaId").toString(), data.get("plazaName").toString(), pageNum, pageSize);
            Integer total = Integer.parseInt(cResp.getJValue("data.total").toString());
            List<Object> resources = (ArrayList<Object>) cResp.getJValue("data.resource");
            if (null != resources && resources.size() > 0) {
                cNewUserCoupons.addAll(resources);
            }
            if (pageNum * pageSize >= total) {
                break;
            }
            pageNum++;
        }

        compare(data.get("plazaId").toString(), "newUser", bCouponList, cNewUserCoupons);

        // 限时抢购数据
        bCoupons.clear();
        bCoupons = requestBCouponList(data.get("plazaId").toString(), data.get("plazaName").toString(), data.get("user").toString(), "3");

        bCouponList = convertCoupons(bCoupons);

        // 比较限时抢购
        pageNum = 1;
        List<Object> cFlashSaleCoupons = new ArrayList<>();
        bStocks.clear();
        while (true) {
            APIResponse cResp = requestCFlashSaleCouponList(data.get("plazaId").toString(), data.get("plazaName").toString(), pageNum, pageSize);
            Integer total = Integer.parseInt(cResp.getJValue("data.total").toString());
            List<Object> resources = (ArrayList<Object>) cResp.getJValue("data.resource");
            if (null != resources) {
                cFlashSaleCoupons.addAll(resources);
            }
            if (pageNum * pageSize >= total) {
                break;
            }
            pageNum++;
        }

        compare(data.get("plazaId").toString(), "flashSale", bCouponList, cFlashSaleCoupons);
    }

    public DiffResultItem compare(List<Map<String, Object>> bCoupons, List<Object> cCoupons, List<Integer> bStocks) {
        DiffResultItem result = new DiffResultItem();
        result.setBTotal(bCoupons.size());
        result.setCTotal(cCoupons.size());
        result.setIsSame(true);

        List<Map<String, Object>> diffBHave = new ArrayList<>();
        List<Map<String, Object>> diffCHave = new ArrayList<>();
        List<DiffStockItem> diffStocks = new ArrayList<>();

        // 拿千帆数据循环在小程序中找
        for (Map<String, Object> bc : bCoupons) {
            String couponNO = bc.get("couponNo").toString();
            boolean found = false;
            for (Object cc : cCoupons) {
                Map<String, Object> coupon = (LinkedHashMap<String, Object>) cc;
                if (coupon.get("couponNo").toString().equals(couponNO)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                if (isCouponOnline(bc.get("schemeId").toString())) {
                    // 券状态还生效那么就加进去
                    diffBHave.add(bc);
                }
            }
        }

        // 拿小程序数据循环在千帆中找
        for (Object cc : cCoupons) {
            Map<String, Object> coupon = (LinkedHashMap<String, Object>) cc;
            String couponNO = coupon.get("couponNo").toString();
            boolean found = false;
            for (Map<String, Object> bc : bCoupons) {
                if (bc.get("couponNo").toString().equals(couponNO)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                diffCHave.add(coupon);
            }
        }

        // 拿小程序数据中的券，去对比千帆中券详情的余量，数据不一致则报警
        for (int i = 0; i < cCoupons.size(); i++) {
            Map<String, Object> coupon = (LinkedHashMap<String, Object>) cCoupons.get(i);
            String couponNo = coupon.get("couponNo").toString();
            //String schemeId = coupon.get("schemeId").toString(); // schemeId找不到，先注释掉
            String title = coupon.get("title").toString();
            Integer cStock = Integer.parseInt(coupon.get("stock").toString());

            if (cStock.equals(0) && !cStock.equals(bStocks.get(i))) {
                boolean ok = cStock == 0;
                DiffStockItem ds = new DiffStockItem();
                ds.setSchemeId("");
                ds.setCouponNo(couponNo);
                ds.setTitle(title);
                ds.setCStock(cStock);
                ds.setBStock(bStocks.get(i));
                ds.setTime(new Date().toString());
                diffStocks.add(ds);
            }
        }

        result.setDiffBInclude(diffBHave);
        result.setDiffCInclude(diffCHave);
        result.setDiffStocks(diffStocks);

        result.setIsSame((diffBHave.size() > 0 || diffCHave.size() > 0 || diffStocks.size() > 0) ? false : true);

        return result;
    }

    public void compare(String plazaId, String channel, List<Map<String, Object>> bCoupons, List<Object> cCoupons) {
        List<String> diffBHave = new ArrayList<>();
        List<String> diffCHave = new ArrayList<>();

        // 拿千帆数据循环在小程序中找
        for (Map<String, Object> bc : bCoupons) {
            String couponNO = bc.get("couponNo").toString();
            boolean found = false;
            for (Object cc : cCoupons) {
                Map<String, Object> coupon = (LinkedHashMap<String, Object>) cc;
                if (coupon.get("couponNo").toString().equals(couponNO)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                if (isCouponOnline(bc.get("schemeId").toString())) {
                    // 券状态还生效那么就加进去
                    diffBHave.add(bc.get("couponNo").toString());
                }
            }
        }

        // 拿小程序数据循环在千帆中找
        for (Object cc : cCoupons) {
            Map<String, Object> coupon = (LinkedHashMap<String, Object>) cc;
            String couponNO = coupon.get("couponNo").toString();
            boolean found = false;
            for (Map<String, Object> bc : bCoupons) {
                if (bc.get("couponNo").toString().equals(couponNO)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                diffCHave.add(couponNO);
            }
        }

        String bDiffResultKey = RedisKeys.getDiffCouponResultKey(plazaId, channel, "B", false);
        String cDiffResultKey = RedisKeys.getDiffCouponResultKey(plazaId, channel, "C", false);
        String bLastDiffResultKey = RedisKeys.getDiffCouponResultKey(plazaId, channel, "B", true);
        String cLastDiffResultKey = RedisKeys.getDiffCouponResultKey(plazaId, channel, "C", true);

        // 删除上次公共问题
        JedisUtil.del(bLastDiffResultKey);
        JedisUtil.del(cLastDiffResultKey);

        // 处理B端券
        String bDiffResult = JedisUtil.get(bDiffResultKey);

        if (null != bDiffResult && diffBHave.size() > 0) {
            List<String> sameCoupons = findCommon(diffBHave, Arrays.asList(bDiffResult.split(",")));
            if (sameCoupons.size() > 0) {
                // 同上次问题券，那么保存到公共问题券中
                JedisUtil.setEx(bLastDiffResultKey, Joiner.on(",").join(sameCoupons), 30 * 60);
            }
        }

        JedisUtil.del(bDiffResultKey);

        if (diffBHave.size() > 0) {
            JedisUtil.setEx(bDiffResultKey, Joiner.on(",").join(diffBHave), 30 * 60);
        }

        // 处理C端券
        String cDiffResult = JedisUtil.get(cDiffResultKey);

        if (null != cDiffResult && diffCHave.size() > 0) {
            List<String> sameCoupons = findCommon(diffCHave, Arrays.asList(cDiffResult.split(",")));
            if (sameCoupons.size() > 0) {
                // 同上次问题券，那么保存到公共问题券中
                JedisUtil.setEx(cLastDiffResultKey, Joiner.on(",").join(sameCoupons), 30 * 60);
            }
        }

        JedisUtil.del(cDiffResultKey);
        if (diffCHave.size() > 0) {
            JedisUtil.setEx(cDiffResultKey, Joiner.on(",").join(diffCHave), 30 * 60);
        }
    }

    private List<String> findCommon(List<String> list1, List<String> list2) {
        List<String> ret = new ArrayList<>();
        list1.forEach(o-> {
            if (list2.contains(o)) {
                ret.add(o);
            }
        });

        return ret;
    }

    public Map<String, String> buildQfHeader(String plazaId, String plazaName, String user) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("token", qfLoginModel.getToken());
            headers.put("orgcode", plazaId);
            headers.put("orgname", URLEncoder.encode(plazaName, "utf-8"));
            headers.put("orgTypeCode", "10003");
            headers.put("orgTypeName", URLEncoder.encode("广场", "utf-8"));
            headers.put("tenantId", qfLoginModel.getTenantId());
            headers.put("userid", qfLoginModel.getUserId());
            headers.put("username", user);
            headers.put("workingOrgCode", plazaId);

            return headers;
        } catch (Exception ex) {
            Logger.error(ex.getMessage());
            return null;
        }
    }

    public List<Object> requestBCouponList(String plazaId, String plazaName, String user, String adId) {
        List<Object> bCoupons = new ArrayList<>();
        Integer pageNum = 1;
        Integer pageSize = 50;

        while (true) {
            Map<String, String> params = new HashMap<>();
            params.put("advertisingId", adId);
            params.put("status", "7"); // 进行中
            params.put("pageNum", pageNum.toString());
            params.put("pageSize", pageSize.toString());
            params.put("timestr", String.valueOf(new Date().getTime()));

            BCouponListService bCouponListService = new BCouponListService(getUrlData());
            APIResponse bResp = bCouponListService
                    .buildUrl(params)
                    .setHeaders(buildQfHeader(plazaId, plazaName, user))
                    .request()
                    .assertJValue("status", "200", String.format("%s, 获取千帆券服务失败，返回状态码非200", plazaName));

            if (null != (ArrayList<Object>) bResp.getJValue("data")) {
                bCoupons.addAll((ArrayList<Object>) bResp.getJValue("data"));
            }

            Integer total = Integer.parseInt(bResp.getJValue("total").toString());
            List<Object> resources = (ArrayList<Object>) bResp.getJValue("data");

            if (pageNum * pageSize >= total) {
                break;
            }
            pageNum++;
        }


        return bCoupons;
    }

    public APIResponse requestCCouponList(String plazaId, String plazaName, Integer pageNum, Integer pageSize) {
        Map<String, String> params = new HashMap<>();
        params.put("adSpaceId", "couponList");
        params.put("plazaId", plazaId);
        params.put("channelId", "1003");
        params.put("type", "1001");
        params.put("pageNum", pageNum.toString());
        params.put("pageSize", pageSize.toString());

        CPlaCouponListService couponListService = new CPlaCouponListService(getUrlData());
        APIResponse resp = couponListService
                .buildUrl(params) // 将参数合并到URL中
                .request() // 发起请求
                .assertJValue("status", "200", String.format("%s, 请求小程序端列表接口返回非200状态", plazaName));

        return resp;
    }

    public APIResponse requestCNewUserCouponList(String plazaId, String plazaName, Integer pageNum, Integer pageSize) {
        Map<String, String> params = new HashMap<>();
        params.put("channelId", "1003");
        params.put("plazaId", plazaId);
        params.put("adSpaceId", "newUserReward");
        params.put("type", "1001");
        params.put("pageNum", pageNum.toString());
        params.put("pageSize", pageSize.toString());

        // 发起请求并进行验证
        CPlaCouponListService couponListService = new CPlaCouponListService(getUrlData());
        APIResponse resp = couponListService
                .buildUrl(params) // 将参数合并到URL中
                .request() // 发起请求
                .assertJValue("status", "200", String.format("%s，新用户领券接口返回状态码非200", plazaName)); // 验证状态码为200，返回正常状态

        return resp;
    }

    public APIResponse requestCFlashSaleCouponList(String plazaId, String plazaName, Integer pageNum, Integer pageSize) {
        Map<String, String> params = new HashMap<>();
        params.put("adSpaceId", "flashSale");
        params.put("plazaId", plazaId);
        params.put("channelId", "1003");
        params.put("type", "1001");
        params.put("pageNum", pageNum.toString());
        params.put("pageSize", pageSize.toString());
        params.put("expressionType", "1"); // 闪购进行中

        // 发起请求并进行验证
        CPlaCouponListService couponListService = new CPlaCouponListService(getUrlData());
        APIResponse resp = couponListService
                .buildUrl(params) // 将参数合并到URL中
                .request() // 发起请求
                .assertJValue("status", "200", String.format("%s，进行中的闪购接口状态码返回非200", plazaName)); // 验证状态码为200，返回正常状态

        return resp;
    }

    public APIResponse requestCDiscountCouponList(String plazaId, String plazaName, Integer pageNum, Integer pageSize) {
        Map<String, String> params = new HashMap<>();
        params.put("adSpaceId", "sale201801");
        params.put("plazaId", plazaId);
        params.put("channelId", "1003");
        params.put("type", "1001");
        params.put("pageNum", pageNum.toString());
        params.put("pageSize", pageSize.toString());
        params.put("expressionType", "1"); // 闪购进行中

        // 发起请求并进行验证
        CPlaCouponListService couponListService = new CPlaCouponListService(getUrlData());
        APIResponse resp = couponListService
                .buildUrl(params) // 将参数合并到URL中
                .request() // 发起请求
                .assertJValue("status", "200", String.format("%s，2折专区券列表接口状态码返回非200", plazaName)); // 验证状态码为200，返回正常状态

        return resp;
    }

    public APIResponse requestBCoupon(String schemeId) {
        Map<String, String> headers = new HashMap<>();
        headers.put("tenantId", qfLoginModel.getTenantId());

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("schemeId", schemeId);

        BCouponService service = new BCouponService(getUrlData());
        APIResponse resp = service
                .formatUrl(urlParams)
                .buildUrl(urlParams)
                .setHeaders(headers)
                .request()
                .assertJValue("status", "200", String.format("schemeId为%s的券获取失败，状态码返回非200", schemeId));
        return resp;
    }

    public APIResponse requestBCouponStock(String couponNos) {
        Map<String, String> urlParams = new HashMap<>();
        try {
            urlParams.put("productNos", URLEncoder.encode(couponNos, "utf-8"));
            urlParams.put("puid", "DF1BA4045BEA41EBAD99DAA3229915F0");
        } catch (Exception ex) {
            Logger.error(ex.getMessage());
        }

        BCouponStockService service = new BCouponStockService(getUrlData());
        return service
                .buildUrl(urlParams)
                .request()
                .assertJValue("status", "200", String.format("CouponStock接口返回非200状态，CouponNos: %s", couponNos));
    }

    public List<Integer> getBStocks(List<Object> resources) {
        List<Integer> bStocks = new ArrayList<>();

        String couponNos = "";
        for (Object obj :
                resources) {
            Map<String, Object> res = (LinkedHashMap<String, Object>) obj;
            couponNos += res.get("couponNo").toString() + ",";
        }
        couponNos = couponNos.substring(0, couponNos.length() - 1);
        APIResponse bres = requestBCouponStock(couponNos);
        List<Object> stocks = (ArrayList<Object>) bres.getJValue("data");
        for (Object obj :
                stocks) {
            Map<String, Object> s = (LinkedHashMap<String, Object>) obj;
            bStocks.add(Integer.parseInt(s.get("leftNum").toString()));
        }
        return bStocks;
    }

    public boolean isCouponOnline(String schemeId) {
        APIResponse resp = requestBCoupon(schemeId);
        return resp.getJValue("data.scheme.status").toString().equals("200");
    }


    public List<Map<String, Object>> convertCoupons(List<Object> allCoupons) {
        List<Map<String, Object>> coupons = new ArrayList<>();

        for (Object obj : allCoupons) {
            Map<String, Object> mObj = (LinkedHashMap<String, Object>) obj;
            coupons.add(mObj);
        }

        return coupons;
    }

//    @Test(
//            testName = "广场列表",
//            description = "",
//            priority = 0,
//            groups = {"wxCoupon"}
//    )
//    public void check_coupons_data() {
//
//        CWeXmtCitiesService citySvc = new CWeXmtCitiesService(getUrlData());
//        APIResponse resp = citySvc.request();
//        ArrayList<Object> data = (ArrayList<Object>)resp.getJValue("data");
//        List<String> cityIds = new ArrayList<>();
//        List<String> cities = new ArrayList<>();
//        List<String> plazas = new ArrayList<>();
//        List<String> plazaIds = new ArrayList<>();
//
//        for (Object obj:
//             data) {
//            Map<String, Object> city = (LinkedHashMap<String, Object>)obj;
//            cityIds.add(city.get("cityId").toString());
//            cities.add(city.get("cityName").toString() + "-" + city.get("cityId").toString());
//
//            Map<String, String> params = new HashMap<>();
//            params.put("cityId", city.get("cityId").toString());
//            params.put("lng", "121.54409");
//            params.put("lat", "31.22114");
//
//            // 发起请求并进行验证
////            CWeXmtPlazasService weXmtPlazasService = new CWeXmtPlazasService(getUrlData());
////            APIResponse resp1 = weXmtPlazasService
////                    .buildUrl(params) // 将参数合并到URL中
////                    .request(); // 发起请求
////            ArrayList<Object> plazaData = (ArrayList<Object>)resp1.getJValue("data");
////            for (Object p:
////                    plazaData) {
////                Map<String, Object> plaza = (LinkedHashMap<String, Object>)p;
////                plazas.add(plaza.get("plazaName").toString() + "-" + plaza.get("plazaId").toString());
////                plazaIds.add(plaza.get("plazaId").toString());
////            }
//        }
//
//
//
//        String plazasStr = "";
//        for (String p:
//             plazaIds) {
//            plazasStr+="'" + p + "',";
//        }
//
//        String cityStr = "";
//        for (String c:
//                cities) {
//            cityStr+="'" + c + "',";
//        }
//
//        System.out.println(cityStr);
//
//
//    }
}
