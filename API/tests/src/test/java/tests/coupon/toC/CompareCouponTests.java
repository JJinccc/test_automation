package tests.coupon.toC;

import com.ffan.qa.biz.services.coupon.CPlaCouponListService;
import com.ffan.qa.biz.services.coupon.CPlaSearchCouponService;
import com.ffan.qa.common.Assertion;
import com.ffan.qa.common.Logger;
import com.ffan.qa.listeners.AssertionListener;
import com.ffan.qa.network.APIResponse;
import com.ffan.qa.utils.StringUtil;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import tests.coupon.CouponTestBase;

import java.net.URLEncoder;
import java.util.*;

@Listeners({AssertionListener.class})
public class CompareCouponTests extends CouponTestBase {
    @Test(
            testName = "比较广场券列表默认请求",
            description = "比较新老接口广场券列表返回的数据",
            groups = {"CompareCoupon"},
            dataProvider = "data"
    )
    public void cmp_plaza_coupons_default(Map<String, Object> data) {
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

        Integer pageSize = 10;
        Integer pageNum = 1;
        List<Object> preResource = new ArrayList<>();
        List<Object> newResource = new ArrayList<>();
        List<Object> preTags = new ArrayList<>();
        List<Object> newTags = new ArrayList<>();

        while (true) {
            params.replace("pageNum", pageNum.toString());
            // 老接口
            CPlaCouponListService couponListService = new CPlaCouponListService(getUrlData());
            APIResponse preResp = couponListService
                    .buildUrl(params) // 将参数合并到URL中
                    .request() // 发起请求
                    .assertJValue("status", "200", String.format("状态码返回非200，小程序首页列表%s-%s存在白页风险", city, plazaName)) // 验证状态码为200，返回正常状态
                    .assertJListNotEmpty("data.tagList", String.format("小程序首页列表%s-%s, data.tagList为空", city, plazaName))
                    .assertJValue("data.adSpaceId", adSpaceId, String.format("数据data.adSpaceId与请求不符，小程序首页列表%s-%s数据正确性风险", city, plazaName))
                    .assertJValue("data.channelId", channelId, String.format("数据data.channelId与请求不符，小程序首页列表%s-%s数据正确性风险", city, plazaName))
                    .assertJValue("data.plazaId", plazaId, String.format("数据data.plazaId与请求不符，小程序首页列表%s-%s数据正确性风险", city, plazaName))
                    .assertJValue("data.type", typeId, String.format("数据data.type与请求不符，小程序首页列表%s-%s数据正确性风险", city, plazaName));

            Integer total = Integer.parseInt(preResp.getJValue("data.total").toString());
            preResource.addAll((ArrayList<Object>) preResp.getJValue("data.resource"));
            if (preTags.size() == 0) {
                preTags.addAll((ArrayList<Object>) preResp.getJValue("data.tagList"));
            }
            if (pageNum * pageSize >= total) {
                break;
            }
            pageNum++;
        }


        pageNum = 1;
        while (true) {
            params.replace("pageNum", pageNum.toString());
            // 新接口
            CPlaSearchCouponService searchCouponService = new CPlaSearchCouponService(getUrlData());
            APIResponse newResp = searchCouponService
                    .buildUrl(params)
                    .request()
                    .assertJValue("status", "200", String.format("状态码返回非200，小程序首页列表%s-%s存在白页风险", city, plazaName)) // 验证状态码为200，返回正常状态
                    .assertJListNotEmpty("data.tagList", String.format("小程序首页列表%s-%s, data.tagList为空", city, plazaName))
                    .assertJValue("data.adSpaceId", adSpaceId, String.format("数据data.adSpaceId与请求不符，小程序首页列表%s-%s数据正确性风险", city, plazaName))
                    .assertJValue("data.channelId", channelId, String.format("数据data.channelId与请求不符，小程序首页列表%s-%s数据正确性风险", city, plazaName))
                    .assertJValue("data.plazaId", plazaId, String.format("数据data.plazaId与请求不符，小程序首页列表%s-%s数据正确性风险", city, plazaName))
                    .assertJValue("data.type", typeId, String.format("数据data.type与请求不符，小程序首页列表%s-%s数据正确性风险", city, plazaName));

            Integer total = Integer.parseInt(newResp.getJValue("data.total").toString());
            newResource.addAll((ArrayList<Object>) newResp.getJValue("data.resource"));
            if (newTags.size() == 0) {
                newTags.addAll((ArrayList<Object>) newResp.getJValue("data.tagList"));
            }
            if (pageNum * pageSize >= total) {
                break;
            }
            pageNum++;
        }

        // 比较字段
        compareResource(preResource, newResource, "");

        // 比较标签
        compareTags(preTags, newTags);
    }

    @Test(
            testName = "比较新用户领券请求",
            description = "比较新老接口新用户领券返回的数据",
            groups = {"CompareCoupon"},
            dataProvider = "data"
    )
    public void cmp_new_user_coupons(Map<String, Object> data) {
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

        List<Object> preResource = new ArrayList<>();
        List<Object> newResource = new ArrayList<>();

        // 老接口
        CPlaCouponListService couponListService = new CPlaCouponListService(getUrlData());
        APIResponse preResp = couponListService
                .buildUrl(params) // 将参数合并到URL中
                .request() // 发起请求
                .assertJValue("status", "200", String.format("%s-%s，新用户领券接口返回状态码非200，存在白页风险", city, plazaName)) // 验证状态码为200，返回正常状态
                .assertJNotEmpty("data.total", String.format("%s-%s，新用户领券接口返回data.total为空", city, plazaName));

        Integer preTotal = Integer.parseInt(preResp.getJValue("data.total").toString());
        if (preTotal > 0) {
            preResource.addAll((ArrayList<Object>) preResp.getJValue("data.resource"));
        }

        // 新接口
        CPlaSearchCouponService searchCouponService = new CPlaSearchCouponService(getUrlData());
        APIResponse newResp = searchCouponService
                .buildUrl(params)
                .request()
                .assertJValue("status", "200", String.format("%s-%s，新用户领券接口返回状态码非200，存在白页风险", city, plazaName)) // 验证状态码为200，返回正常状态
                .assertJNotEmpty("data.total", String.format("%s-%s，新用户领券接口返回data.total为空", city, plazaName));

        Integer newTotal = Integer.parseInt(newResp.getJValue("data.total").toString());
        if (newTotal > 0) {
            newResource.addAll((ArrayList<Object>) newResp.getJValue("data.resource"));
        }

        // 比较字段
        compareResource(preResource, newResource, "");
    }

    @Test(
            testName = "比较进行中的限时抢购请求",
            description = "比较新老接口进行中限时抢购返回的数据",
            groups = {"CompareCoupon"},
            dataProvider = "data"
    )
    public void cmp_flash_sale_coupons_started(Map<String, Object> data) {
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

        Integer pageSize = 10;
        Integer pageNum = 1;
        List<Object> preResource = new ArrayList<>();
        List<Object> newResource = new ArrayList<>();

        while (true) {
            params.replace("pageNum", pageNum.toString());
            // 老接口
            CPlaCouponListService couponListService = new CPlaCouponListService(getUrlData());
            APIResponse preResp = couponListService
                    .buildUrl(params) // 将参数合并到URL中
                    .request() // 发起请求
                    .assertJValue("status", "200", String.format("%s-%s，进行中的闪购接口状态码返回非200，存在白页风险", city, plazaName)) // 验证状态码为200，返回正常状态
                    .assertJValue("data.adSpaceId", adSpaceId, String.format("%s-%s，进行中的闪购接口data.adSpaceId与传入参数不符，存在数据准确性风险", city, plazaName))
                    .assertJValue("data.channelId", channelId, String.format("%s-%s，进行中的闪购接口data.channelId与传入参数不符，存在数据准确性风险", city, plazaName))
                    .assertJValue("data.plazaId", plazaId, String.format("%s-%s，进行中的闪购接口data.plazaId与传入参数不符，存在数据准确性风险", city, plazaName))
                    .assertJValue("data.type", typeId, String.format("%s-%s，进行中的闪购接口data.type与传入参数不符，存在数据准确性风险", city, plazaName))
                    .assertJNotEmpty("data.total", String.format("%s-%s，进行中的闪购接口data.total为空", city, plazaName)); // 验证券总量有值并大于0

            Integer total = Integer.parseInt(preResp.getJValue("data.total").toString());
            if (total > 0) {
                preResource.addAll((ArrayList<Object>) preResp.getJValue("data.resource"));
            }

            if (pageNum * pageSize >= total) {
                break;
            }
            pageNum++;
        }

        pageNum = 1;
        while (true) {
            params.replace("pageNum", pageNum.toString());
            // 新接口
            CPlaSearchCouponService searchCouponService = new CPlaSearchCouponService(getUrlData());
            APIResponse newResp = searchCouponService
                    .buildUrl(params)
                    .request()
                    .assertJValue("status", "200", String.format("%s-%s，进行中的闪购接口状态码返回非200，存在白页风险", city, plazaName)) // 验证状态码为200，返回正常状态
                    .assertJValue("data.adSpaceId", adSpaceId, String.format("%s-%s，进行中的闪购接口data.adSpaceId与传入参数不符，存在数据准确性风险", city, plazaName))
                    .assertJValue("data.channelId", channelId, String.format("%s-%s，进行中的闪购接口data.channelId与传入参数不符，存在数据准确性风险", city, plazaName))
                    .assertJValue("data.plazaId", plazaId, String.format("%s-%s，进行中的闪购接口data.plazaId与传入参数不符，存在数据准确性风险", city, plazaName))
                    .assertJValue("data.type", typeId, String.format("%s-%s，进行中的闪购接口data.type与传入参数不符，存在数据准确性风险", city, plazaName))
                    .assertJNotEmpty("data.total", String.format("%s-%s，进行中的闪购接口data.total为空", city, plazaName)); // 验证券总量有值并大于0

            Integer total = Integer.parseInt(newResp.getJValue("data.total").toString());
            if (total > 0) {
                newResource.addAll((ArrayList<Object>) newResp.getJValue("data.resource"));
            }
            if (pageNum * pageSize >= total) {
                break;
            }
            pageNum++;
        }

        // 比较字段
        compareResource(preResource, newResource, "");
    }

    @Test(
            testName = "比较广场券列表类型筛选请求",
            description = "比较新老接口广场券列表类型筛选返回的数据",
            groups = {"CompareCoupon"},
            dataProvider = "data"
    )
    public void cmp_plaza_coupons_bizTypes(Map<String, Object> data) {
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

            // 发起请求进行验证
            Integer pageSize = 10;
            Integer pageNum = 1;
            List<Object> preResource = new ArrayList<>();
            List<Object> newResource = new ArrayList<>();

            while (true) {
                params.replace("pageNum", pageNum.toString());
                // 老接口
                couponListService = new CPlaCouponListService(getUrlData());
                APIResponse preResp = couponListService
                        .buildUrl(params) // 将参数合并到URL中
                        .request() // 发起请求
                        .assertJValue("status", "200", "请求Coupons接口失败，无法获取TagList列表"); // 验证状态码为200，返回正常状态

                Integer total = Integer.parseInt(preResp.getJValue("data.total").toString());
                preResource.addAll((ArrayList<Object>) preResp.getJValue("data.resource"));
                if (pageNum * pageSize >= total) {
                    break;
                }
                pageNum++;
            }


            pageNum = 1;
            while (true) {
                params.replace("pageNum", pageNum.toString());
                // 新接口
                CPlaSearchCouponService searchCouponService = new CPlaSearchCouponService(getUrlData());
                APIResponse newResp = searchCouponService
                        .buildUrl(params)
                        .request()
                        .assertJValue("status", "200", "请求Coupons接口失败，无法获取TagList列表"); // 验证状态码为200，返回正常状态

                Integer total = Integer.parseInt(newResp.getJValue("data.total").toString());
                newResource.addAll((ArrayList<Object>) newResp.getJValue("data.resource"));
                if (pageNum * pageSize >= total) {
                    break;
                }
                pageNum++;
            }

            // 比较字段
            compareResource(preResource, newResource, "TagId为" + id + "时,");
        }


    }

    public void compareResource(List<Object> preResource, List<Object> newResource, String exMessage) {
        Assertion.verifyEquals(newResource.size(), preResource.size(), exMessage + "data.total数据不一致");
        if (newResource.size() == preResource.size()) {
            // 验证排序
            String orderOutputs = "";
            boolean isDiff = false;
            for (Integer i = 0; i < newResource.size(); i++) {
                Map<String, Object> newCoupon = (LinkedHashMap<String, Object>) newResource.get(i);
                Integer preIndex = findCouponIndex(preResource, newCoupon.get("couponNo").toString());
                orderOutputs += newCoupon.get("couponNo").toString();
                if (!preIndex.equals(-1)) {
                    if (!preIndex.equals(i)) {
                        isDiff = true;
                        orderOutputs += String.format("[新接口位置%d,老接口位置%d]", i, preIndex);

                    } else if (preIndex == -1) {
                        isDiff = true;
                        orderOutputs += "[老接口不存在]";
                    }
                    orderOutputs += ",";
                }
            }
//            if (isDiff) {
//                Assertion.fail("排序异常：" + orderOutputs);
//            }
        }

        // 比较详情
        List<String> finishedCoupons = new ArrayList<>();

        // 新接口存在的券，在老接口找不到
        String notExistCoupons = "";
        for (Integer i = 0; i < newResource.size(); i++) {
            Map<String, Object> newCoupon = (LinkedHashMap<String, Object>) newResource.get(i);
            Integer preIndex = findCouponIndex(preResource, newCoupon.get("couponNo").toString());
            if (preIndex.equals(-1)) {
                notExistCoupons += newCoupon.get("couponNo").toString() + ",";
            } else {
                compareDetail((LinkedHashMap<String, Object>) preResource.get(preIndex), newCoupon);
                finishedCoupons.add(newCoupon.get("couponNo").toString());
            }
        }

        // 老接口存在的券，但新接口找不到的券
        String notExistCoupons1 = "";
        for (Integer i = 0; i < preResource.size(); i++) {
            Map<String, Object> preCoupon = (LinkedHashMap<String, Object>) preResource.get(i);
            String couponNo = preCoupon.get("couponNo").toString();
            Integer newIndex = findCouponIndex(newResource, couponNo);
            if (newIndex.equals(-1)) {
                notExistCoupons1 += couponNo + ",";
            } else if (!finishedCoupons.contains(couponNo)) {
                compareDetail(preCoupon, (LinkedHashMap<String, Object>) newResource.get(newIndex));
            }
        }

        if (!StringUtil.isNullOrEmpty(notExistCoupons)) {
            Assertion.fail(exMessage + "新接口存在，但老接口未找到的券：" + notExistCoupons.substring(0, notExistCoupons.length() - 1));
        }

        if (!StringUtil.isNullOrEmpty(notExistCoupons1)) {
            Assertion.fail(exMessage + "老接口存在，但新接口未找到的券：" + notExistCoupons1.substring(0, notExistCoupons1.length() - 1));
        }

        // 验证内容
    }

    public void compareDetail(Map<String, Object> preCoupon, Map<String, Object> newCoupon) {
        String couponNo = newCoupon.get("couponNo").toString();
        Assertion.verifyEquals(newCoupon.get("denomination"), preCoupon.get("denomination"), String.format("券%s字段denomination比较不一致", couponNo));
        Assertion.verifyEquals(newCoupon.get("salesPrice"), preCoupon.get("salesPrice"), String.format("券%s字段salesPrice比较不一致", couponNo));
        Assertion.verifyEquals(newCoupon.get("marketValue"), preCoupon.get("marketValue"), String.format("券%s字段marketValue比较不一致", couponNo));
        Assertion.verifyEquals(newCoupon.get("couponType"), preCoupon.get("couponType"), String.format("券%s字段couponType比较不一致", couponNo));
        Assertion.verifyEquals(newCoupon.get("title"), preCoupon.get("title"), String.format("券%s字段title比较不一致", couponNo));
        Assertion.verifyEquals(newCoupon.get("subTitle"), preCoupon.get("subTitle"), String.format("券%s字段subTitle比较不一致", couponNo));
        Assertion.verifyEquals(newCoupon.get("img1"), preCoupon.get("img1"), String.format("券%s字段img1比较不一致", couponNo));
        try {
            Assertion.verifyEquals(URLEncoder.encode(newCoupon.get("remark").toString(), "utf-8"), URLEncoder.encode(preCoupon.get("remark").toString(), "utf-8"), String.format("券%s字段remark比较不一致", couponNo));
        } catch (Exception ex) {
            Logger.error(ex.getMessage());
        }
        //Assertion.verifyEquals(newCoupon.get("stock"), preCoupon.get("stock"), String.format("券%s字段stock比较不一致", couponNo));


        Assertion.verifyEquals(newCoupon.get("couponStock.totalCount"), preCoupon.get("couponStock.totalCount"), String.format("券%s字段couponStock.totalCount比较不一致", couponNo));
        Assertion.verifyEquals(newCoupon.get("couponStock.soldCount"), preCoupon.get("couponStock.soldCount"), String.format("券%s字段couponStock.soldCount比较不一致", couponNo));
        Assertion.verifyEquals(newCoupon.get("couponStock.lockedCount"), preCoupon.get("couponStock.lockedCount"), String.format("券%s字段couponStock.lockedCount比较不一致", couponNo));
        Assertion.verifyEquals(newCoupon.get("couponStock.surplusCount"), preCoupon.get("couponStock.surplusCount"), String.format("券%s字段couponStock.surplusCount比较不一致", couponNo));
        Assertion.verifyEquals(newCoupon.get("couponStock.useCount"), preCoupon.get("couponStock.useCount"), String.format("券%s字段couponStock.useCount比较不一致", couponNo));
        Assertion.verifyEquals(newCoupon.get("couponRule.buySingleLimit"), preCoupon.get("couponRule.buySingleLimit"), String.format("券%s字段couponRule.buySingleLimit比较不一致", couponNo));
        Assertion.verifyEquals(newCoupon.get("couponRule.buyDayLimit"), preCoupon.get("couponRule.buyDayLimit"), String.format("券%s字段couponRule.buyDayLimit比较不一致", couponNo));
        Assertion.verifyEquals(newCoupon.get("couponRule.buyTotalLimit"), preCoupon.get("couponRule.buyTotalLimit"), String.format("券%s字段couponRule.buyTotalLimit比较不一致", couponNo));
        Assertion.verifyEquals(newCoupon.get("couponRule.sendBeginTime"), preCoupon.get("couponRule.sendBeginTime"), String.format("券%s字段couponRule.sendBeginTime比较不一致", couponNo));
        Assertion.verifyEquals(newCoupon.get("couponRule.sendEndTime"), preCoupon.get("couponRule.sendEndTime"), String.format("券%s字段couponRule.sendEndTime比较不一致", couponNo));
        if (preCoupon.get("bizTagId").toString().equals("")) {
            Assertion.verifyEquals(newCoupon.get("bizTagId"), "0", String.format("券%s字段bizTagId比较不一致", couponNo));
        } else {
            Assertion.verifyEquals(newCoupon.get("bizTagId"), preCoupon.get("bizTagId"), String.format("券%s字段bizTagId比较不一致", couponNo));
        }
        //Assertion.verifyEquals(newCoupon.get("adtBeginTime"), preCoupon.get("adtBeginTime"), String.format("券%s字段adtBeginTime比较不一致", couponNo));
        //Assertion.verifyEquals(newCoupon.get("adtEndTime"), preCoupon.get("adtEndTime"), String.format("券%s字段adtEndTime比较不一致", couponNo));
    }

    public void compareTags(List<Object> preTags, List<Object> newTags) {
        Assertion.verifyEquals(newTags.size(), preTags.size(), "data.tagList数据总数不一致");

        String notInclude = "";
        for (Object obj : newTags) {
            Map<String, Object> tag = (LinkedHashMap<String, Object>) obj;
            if (!containsTag(tag, preTags)) {
                notInclude = String.format("tagId:%s|tagName:%s,", tag.get("tagId").toString(), tag.get("tagName").toString());
            }
        }
        if (!StringUtil.isNullOrEmpty(notInclude)) {
            Assertion.fail("新接口存在Tag，但老接口不存在： " + notInclude);
            notInclude = "";
        }

        for (Object obj : preTags) {
            Map<String, Object> tag = (LinkedHashMap<String, Object>) obj;
            if (!containsTag(tag, newTags)) {
                notInclude = String.format("tagId:%s|tagName:%s,", tag.get("tagId").toString(), tag.get("tagName").toString());
            }
        }
        if (!StringUtil.isNullOrEmpty(notInclude)) {
            Assertion.fail("老接口存在Tag，但新接口不存在： " + notInclude);
            notInclude = "";
        }
    }

    public boolean containsTag(Map<String, Object> expected, List<Object> tags) {
        for (Object obj : tags) {
            Map<String, Object> tag = (LinkedHashMap<String, Object>) obj;
            if (tag.get("tagId").equals(expected.get("tagId")) && tag.get("tagName").equals(expected.get("tagName"))) {
                return true;
            }
        }
        return false;
    }

    public Map<String, Object> findCoupon(List<Object> list, String couponNo) {
        for (Object obj : list) {
            Map<String, Object> coupon = (LinkedHashMap<String, Object>) obj;
            if (coupon.get("couponNo").toString().equals(couponNo)) {
                return coupon;
            }
        }

        return null;
    }

    public Integer findCouponIndex(List<Object> list, String couponNo) {
        for (Integer i = 0; i < list.size(); i++) {
            Map<String, Object> coupon = (LinkedHashMap<String, Object>) list.get(i);
            if (coupon.get("couponNo").toString().equals(couponNo)) {
                return i;
            }
        }

        return -1;
    }
}
