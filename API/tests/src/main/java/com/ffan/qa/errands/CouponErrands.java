package com.ffan.qa.errands;

import com.ffan.qa.biz.services.coupon.*;
import com.ffan.qa.common.model.CLoginModel;
import com.ffan.qa.common.type.AdvertisementType;
import com.ffan.qa.common.type.CouponType;
import com.ffan.qa.network.APIResponse;
import com.ffan.qa.utils.*;

import java.util.*;

public class CouponErrands extends ErrandsBase {
    private static final String imgUrl = "http://img1.ffan.com/T16RKgBvd_1RCvBVdK";

    public static Map<String, String> createPublishedCoupon(CouponType couponType, AdvertisementType adType) {
        // 创建券
        Map<String, String> coupon = createCoupon(couponType);
        String schemeId = coupon.get("schemeId");
        String couponNo = coupon.get("couponNo");

        // 投放
        launchCoupon(adType, schemeId);

        // 审核
        auditCoupon(couponNo);

        return coupon;
    }

    public static Map<String, String> createCoupon(CouponType couponType) {
        Map<String, Object> urlData = TestConfigUtil.getUrlData();

        String body = getCouponBody(couponType);

        // 创建代金券
        BNewCouponService couponService = new BNewCouponService(urlData);
        APIResponse resp = couponService
                .setHeaders(getQianfanHeader())
                .buildBody(body)
                .request()
                .assertJValue("status", "200");

        Map<String, String> retCoupon = new HashMap<>();
        retCoupon.put("schemeId", resp.getJValue("data.scheme.id").toString());
        retCoupon.put("couponId", resp.getJValue("data.coupon.id").toString());
        retCoupon.put("couponNo", resp.getJValue("data.coupon.couponNo").toString());
        retCoupon.put("title", resp.getJValue("data.scheme.title").toString());

        Map<String, Object> store = ObjectUtil.convertToMap(resp.getJValue("data.stores[0]"));
        retCoupon.put("storeId", store.get("storeId").toString());

        return retCoupon;
    }

    /**
     * 投放券
     *
     * @param adType
     * @param schemeId
     */
    public static void launchCoupon(AdvertisementType adType, String schemeId) {
        // 获取小程序投放位置列表
        Map<String, Object> ad = AdvertisementErrands.findAdvdertisement(adType);
        String adId = ad.get("id").toString();

        Date date = DateUtil.addDays(new Date(), 10);
        String reviewBeginTime = DateUtil.DateToStr(DateUtil.addHours(new Date(), -8)).replace(" ", "T") + ".000Z";
        String reviewEndTime = DateUtil.DateToStr(DateUtil.addHours(date, -8)).replace(" ", "T") + ".000Z";

        BLaunchCouponService newLaunchCouponService = new BLaunchCouponService();
        String body = "{\"contentType\":\"coupon\"," +
                "\"channelId\":1003," +
                "\"showContentList\":[{" +
                "\"schemeId\":" + schemeId + "," +
                "\"advertisingId\":" + adId + "," +
                "\"beginTime\":\"" +
                reviewBeginTime + "\"," +
                "\"endTime\":\"" +
                reviewEndTime + "\"," +
                "\"coverImg\":\"" + imgUrl + "\"," +
                "\"plazaId\":\"" + TestConfigUtil.getBaseData().get("wxPlazaId").toString() + "\"}]}";
        newLaunchCouponService
                .setHeaders(getQianfanHeader())
                .setRequestBody(body)
                .request()
                .assertJValue("status", "200");
    }

    /**
     * 审核券
     *
     * @param couponNo
     */
    public static String auditCoupon(String couponNo) {
        Date date = DateUtil.addDays(new Date(), 10);
        String beginTime = DateUtil.DateToStr(new Date());
        String endTime = DateUtil.DateToStr(date);
        String reviewBeginTime = DateUtil.DateToStr(DateUtil.addHours(new Date(), -8)).replace(" ", "T") + ".000Z";
        String reviewEndTime = DateUtil.DateToStr(DateUtil.addHours(date, -8)).replace(" ", "T") + ".000Z";

        String auditId = findAuditId(couponNo);
        BCouponAuditService couponAuditService = new BCouponAuditService();
        String body = "[{" +
                "\"id\":" + auditId + "," +
                "\"checked\":true," +
                "\"sit\":\"券列表\"," +
                "\"imgWidth\":\"950\"," +
                "\"imgHeight\":\"520\"," +
                "\"imgUrl\":\"" + imgUrl + "\"," +
                "\"img\":\"" + imgUrl + "\"," +
                "\"date1\":\"" + beginTime + ".0\"," +
                "\"date2\":\"" + endTime + ".0\"," +
                "\"dates\":[\"" + reviewBeginTime + "\",\"" + reviewEndTime + "\"]," +
                "\"beginTime\":\"" + reviewBeginTime + "\"," +
                "\"endTime\":\"" + reviewEndTime + "\"," +
                "\"status\":4}]";
        APIResponse resp = couponAuditService
                .setHeaders(getQianfanHeader())
                .buildBody(body)
                .request()
                .assertJValue("status", "200");

        return auditId;
    }

    /**
     * 小程序端领券
     *
     * @param model
     * @param adSpaceId
     * @param plazaId
     * @param couponNo
     * @return
     */
    public static String orderCoupon(CLoginModel model, String adSpaceId, String plazaId, String couponNo, String price) {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("cookieStr", EncoderUtil.urlEncode(model.getCookieStr()));

        String remark = "{\"orderType\":\"coupon\",\"plazaId\":" + plazaId + ",\"adSpaceId\":\"" + adSpaceId + "\"}";
        String productInfos = "[{\"productId\":\"" + couponNo + "\",\"count\":1}]";
        String clientInfo = "{\"clientVersion\":\"" + TestConfigUtil.getBaseData().get("wxAppId") + "\",\"ipAddr\":\"\",\"clientType\":\"11\"}";

        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("memberId", model.getMemberId());
        bodyParams.put("actionType", "create");
        bodyParams.put("remark", EncoderUtil.urlEncode(remark));
        bodyParams.put("productInfos", EncoderUtil.urlEncode(productInfos));
        bodyParams.put("clientInfo", EncoderUtil.urlEncode(clientInfo));
        bodyParams.put("tradeCode", "7010");
        bodyParams.put("phoneNo", model.getPhoneNumber());
        bodyParams.put("paymentFlag", "0");
        bodyParams.put("orderSrc", "2010");
        bodyParams.put("puid", model.getPuid());
        bodyParams.put("totalPrice", price);

        COrderCouponService service = new COrderCouponService();
        APIResponse resp = service
                .buildUrl(urlParams)
                .buildBody(bodyParams)
                .request()
                .assertJValue("status", "200", "领券失败");

        return resp.getJValue("orderNo").toString();
    }

    /**
     * 获取订单内CouponNo
     *
     * @param model
     * @param orderId
     * @return
     */
    public static String getOrderCouponNo(CLoginModel model, String orderId) {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("cookieStr", EncoderUtil.urlEncode(model.getCookieStr()));
        urlParams.put("oid", orderId);

        String orderCouponNo = null;
        Integer currentTimes = 0;

        while (null == orderCouponNo && currentTimes < 3) {
            CCouponOrderService service = new CCouponOrderService();
            APIResponse resp = service
                    .buildUrl(urlParams)
                    .request()
                    .assertJValue("status", "200", "获取订单详情接口失败");

            if (null == resp.getJValue("data.product[0].couponNo")) {
                SystemUtil.sleep(5 * 1000);
            } else {
                orderCouponNo = resp.getJValue("data.product[0].couponNo").toString();
            }
            currentTimes++;
        }

        return orderCouponNo;
    }

    /**
     * 核销
     *
     * @param storeId
     * @param productCouponNo
     * @param memberId
     */
    public static void checkCoupon(String storeId, String productCouponNo, String memberId) {
        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("storeid", storeId);
        bodyParams.put("certificateno", productCouponNo);
        bodyParams.put("memberId", memberId);

        CCheckCouponService service = new CCheckCouponService();
        service
                .buildBody(bodyParams)
                .request()
                .assertJValue("status", "200", "核销失败");
    }

    /**
     * 结束投放
     *
     * @param auditId
     */
    public static void finishAudit(String auditId) {
        String body = "[{\"id\":" + auditId + ",\"status\":6}]";

        BAuditUpdateService service = new BAuditUpdateService();
        service
                .setRequestBody(body)
                .setHeaders(getQianfanHeader())
                .request()
                .assertJValue("status", "200", "结束已投放券操作失败");
    }

    /**
     * 失效券
     *
     * @param couponId
     */
    public static void offsaleCoupon(String couponId) {
        Map<String, String> params = new HashMap<>();
        params.put("timestr", String.valueOf(new Date().getTime()));

        Map<String, String> urlParts = new HashMap<>();
        urlParts.put("id", couponId);

        BCouponOffsaleService service = new BCouponOffsaleService();
        service
                .formatUrl(urlParts)
                .buildUrl(params)
                .setHeaders(getQianfanHeader())
                .request()
                .assertJValue("status", "200", "券失效操作失败");
    }

    private static String findAuditId(String couponNo) {
        // contentName=&advertisingId=&channelId=&status=1&pageNum=1&pageSize=10&beginTime=&endTime=&title=&couponNo=20180502090327&timestr=1525224613857
        BAuditListService auditListService = new BAuditListService();
        Map<String, String> params = new HashMap<>();
        params.put("contentName", "");
        params.put("advertisingId", "");
        params.put("channelId", "");
        params.put("status", "1");
        params.put("pageNum", "1");
        params.put("pageSize", "10");
        params.put("beginTime", "");
        params.put("endTime", "");
        params.put("title", "");
        params.put("couponNo", couponNo);
        params.put("timestr", String.valueOf(new Date().getTime()));
        APIResponse resp = auditListService
                .setHeaders(getQianfanHeader())
                .buildBody(params)
                .request()
                .assertJValue("status", "200", "审核列表接口返回状态码异常")
                .assertJListNotEmpty("data", "审核列表通过CouponNo" + couponNo + "查询，返回空");

        List<Object> ads = (ArrayList<Object>) resp.getJValue("data");
        return ((LinkedHashMap<String, Object>)ads.get(0)).get("id").toString();
//        for (Object obj : ads) {
//            Map<String, Object> ad = (LinkedHashMap<String, Object>) obj;
//            if (ad.get("couponNo").toString().equals(couponNo)) {
//                return ad.get("id").toString();
//            }
//        }
//
//        return "";
    }

    private static String getCouponBody(CouponType couponType) {
        Map<String, String> params = new HashMap<>();
        String title = "未知";
        switch (couponType) {
            case CashCoupon:
                params.put("schemeType", "14");
                params.put("couponType", "4");
                params.put("businessId", "1");
                params.put("faceValue", "1000");
                params.put("saleType", "3");
                params.put("salePrice", "1");
                params.put("marketValue", "0");
                params.put("orderAmount", "0");
                params.put("bizTagId", "1,2,3,4,5");
                title = "代金券-" + StringUtil.getRandomChar(5);
                break;
            case GiftCoupon:
                params.put("schemeType", "12");
                params.put("couponType", "2");
                params.put("businessId", "1");
                params.put("faceValue", "0");
                params.put("saleType", "1");
                params.put("salePrice", "0");
                params.put("marketValue", "9000");
                params.put("orderAmount", "0");
                params.put("bizTagId", "1,2,3,4,5");
                title = "礼品券-" + StringUtil.getRandomChar(5);
                break;
            case GroupCoupon:
                params.put("schemeType", "13");
                params.put("couponType", "3");
                params.put("businessId", "1");
                params.put("faceValue", "0");
                params.put("saleType", "3");
                params.put("salePrice", "1");
                params.put("marketValue", "2000");
                params.put("orderAmount", "0");
                params.put("bizTagId", "1,2,3,4,5");
                title = "团购券-" + StringUtil.getRandomChar(5);
                break;
            case DiscountCoupon:
                params.put("schemeType", "10");
                params.put("couponType", "1");
                params.put("businessId", "1");
                params.put("faceValue", "1000");
                params.put("saleType", "1");
                params.put("salePrice", "0");
                params.put("marketValue", "0");
                params.put("orderAmount", "10000");
                params.put("bizTagId", "1,2,3,4,5");
                title = "满减券-" + StringUtil.getRandomChar(5);
                break;
            case ParkingCoupon:
                params.put("schemeType", "15");
                params.put("couponType", "5");
                params.put("businessId", "2");
                params.put("faceValue", "500");
                params.put("saleType", "1");
                params.put("salePrice", "0");
                params.put("marketValue", "0");
                params.put("orderAmount", "0");
                params.put("bizTagId", "");
                title = "停车券-" + StringUtil.getRandomChar(5);
                break;
        }

        List<Object> stores = StoreErrands.getBStores(10); // 获取门店列表
        String storeStr = StringUtil.parseToJsonString(stores.get(0));
        String url = TestConfigUtil.getUrlData().get("qianfanBaseUrl").toString();
        Date date = DateUtil.addDays(new Date(), 10);
        String beginTime = DateUtil.DateToStr(new Date());
        String endTime = DateUtil.DateToStr(date);

        String body = "{\"source\":2," +
                "\"scheme\":{" +
                "\"title\":\"" + title + "\"," +
                "\"subTitle\":\"\"," +
                "\"tag\":\"\"," +
                "\"remark\":\"\"," +
                "\"img1\":\"" + imgUrl + "\"," +
                "\"img2\":\"\",\"img3\":\"\",\"img4\":\"\",\"img5\":\"\"," +
                "\"type\":" + params.get("schemeType") + "," +
                "\"storeId\":\"\"," +
                "\"storeLogo\":\"\"," +
                "\"storeName\":\"\"," +
                "\"storeAddress\":\"\"," +
                "\"storeTel\":\"\"," +
                "\"url\":\"" + url + "\"," +
                "\"status\":1," +
                "\"validBegin\":\"\"," +
                "\"validEnd\":\"\"," +
                "\"beginTime\":\"" + beginTime + "\"," +
                "\"endTime\":\"" + endTime + "\"," +
                "\"beginDayTime\":\"\"," +
                "\"endDayTime\":\"\"," +
                "\"parentOrgId\":\"\"," +
                "\"parentOrgName\":\"\"," +
                "\"orgId\":\"\"," +
                "\"orgName\":\"\"," +
                "\"tenantId\":\"\"," +
                "\"tenantName\":\"\"," +
                "\"pagesId\":\"\"," +
                "\"condition\":1," +
                "\"shareContent\":\"" + title + "\"," +
                "\"shareImage\":\"" + imgUrl + "\"," +
                "\"shareTitle\":\"" + title + "\"," +
                "\"businessId\":" + params.get("businessId") + "," +
                "\"couponType\":" + params.get("couponType") + "}," +
                "\"bonus\":{\"bonus\":0,\"oaCode\":\"\"},\"certificateFile\":{\"fileKey\":\"\",\"fileName\":\"\",\"fileUrl\":\"\"}," +
                "\"coupon\":{\"picUrl\":\"\",\"stock\":{\"totalCount\":\"100\",\"soldCount\":0,\"lockedCount\":0,\"surplusCount\":\"100\"}," +
                "\"rule\":{" +
                "\"buyTotalLimit\":\"1\"," +
                "\"buyDayLimit\":0," +
                "\"orderAmount\":" + params.get("orderAmount") + "," +
                "\"startType\":1," +
                "\"periodType\":0," +
                "\"relativeDay\":\"\"," +
                "\"sendBeginTime\":\"" + beginTime + "\"," +
                "\"sendEndTime\":\"" + endTime + "\"}," +
                "\"faceValue\":" + params.get("faceValue") + "," +
                "\"couponType\":" + params.get("couponType") + "," +
                "\"schemeType\":" + params.get("schemeType") + "," +
                "\"salesPrice\":" + params.get("salePrice") + "," +
                "\"marketValue\":" + params.get("marketValue") + "," +
                "\"saleType\":" + params.get("saleType") + "," +
                "\"text\":\"不可与其他促销叠加，最终归属权归门店所有\"," +
                "\"hasBonus\":0," +
                "\"createCertificateType\":1," +
                "\"sourceMerchant\":\"\"," +
                "\"sourceMerchantName\":\"\"," +
                "\"bizTagId\":\"" + params.get("bizTagId") + "\"}," +
                "\"stores\":[" + storeStr + "]}";

        return body;
    }

    public static Boolean waitList(String plazaId, String adSpaceId, String couponNo, Integer timeoutSec) {
        String channelId = "1003";
        String typeId = "1001";
        Map<String, String> params = new HashMap<>();
        params.put("adSpaceId", adSpaceId);
        params.put("plazaId", plazaId);
        params.put("channelId", channelId);
        params.put("type", typeId);
        params.put("pageNum", "1");
        params.put("pageSize", "10");

        Long start = new Date().getTime();

        while (true) {
            // 发起请求并进行验证
            CPlaCouponListService couponListService = new CPlaCouponListService();
            APIResponse resp = couponListService
                    .buildUrl(params) // 将参数合并到URL中
                    .request() // 发起请求
                    .assertJValue("status", "200", "获取小程序广场列表失败"); // 验证状态码为200，返回正常状态

            List<Object> resources = (ArrayList<Object>) resp.getJValue("data.resource");
            for (Object obj : resources) {
                Map<String, Object> coupon = ObjectUtil.convertToMap(obj);
                if (coupon.get("couponNo").equals(couponNo)) {
                    return true;
                }
            }

            if (new Date().getTime() - start > timeoutSec * 1000) {
                break;
            } else {
                SystemUtil.sleep(5 * 1000);
            }
        }

        return false;
    }

}
