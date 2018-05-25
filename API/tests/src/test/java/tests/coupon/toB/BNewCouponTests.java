package tests.coupon.toB;

import com.ffan.qa.biz.services.coupon.BAuditListService;
import com.ffan.qa.biz.services.coupon.BCouponAuditService;
import com.ffan.qa.biz.services.coupon.BNewCouponService;
import com.ffan.qa.biz.services.coupon.BLaunchCouponService;
import com.ffan.qa.network.APIResponse;
import com.ffan.qa.utils.StringUtil;
import org.testng.annotations.Test;
import tests.coupon.CouponTestBase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BNewCouponTests extends CouponTestBase {
    @Override
    public void setup() {
        qfLogin();
    }

    @Test(testName = "代金券默认选项测试", priority = 0, groups = {"Basic", "BCoupon"})
    public void b_cash_coupon_default() {
        // 执行登录
        //qfLogin();

        BNewCouponService newCouponService = new BNewCouponService(getUrlData());

        // Body封装
        String url = getUrlData().get("qianfanBaseUrl").toString();
        String title = "代金券-" + StringUtil.getRandomChar(5);
        Random rnd = new Random(100);
        int denomination = (rnd.nextInt(100) + 1) * 100;

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,+10);//把日期往后增加10天，若想把日期向前推则将正数改为负数
        date = calendar.getTime();

        String beginTime = df.format(System.currentTimeMillis());
        String endTime = df.format(date);
        String sendBeginTime = df.format(System.currentTimeMillis());
        String sendEndTime = df.format(date);

        String body = "{\"source\":2," +
                "\"scheme\":" +
                "{\"title\":\"" +
                title + "\"," +
                "\"subTitle\":\"" +
                title + "\"," +
                "\"tag\":\"\"," +
                "\"remark\":\"\"," +
                "\"img1\":\"http://img1.ffan.com/T19QhgBjJg1RCvBVdK\"," +
                "\"img2\":\"\"," +
                "\"img3\":\"\"," +
                "\"img4\":\"\"," +
                "\"img5\":\"\"," +
                "\"type\":14," +
                "\"storeId\":\"\"," +
                "\"storeLogo\":\"\"," +
                "\"storeName\":\"\"," +
                "\"storeAddress\":\"\"," +
                "\"storeTel\":\"\"," +
                "\"url\":\"" +
                url + "\"," +
                "\"status\":1," +
                "\"validBegin\":\"\"," +
                "\"validEnd\":\"\"," +
                "\"" +
                "beginTime\":\"" +
                "\"," +
                "\"" +
                "endTime\":\"" +
                "\"," +
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
                "\"shareContent\":\"代金券2\"," +
                "\"shareImage\":\"http://img1.ffan.com/T1PLWgB7Jg1RCvBVdK\"," +
                "\"shareTitle\":\"代金券2\"," +
                "\"businessId\":1," +
                "\"couponType\":4}," +
                "\"bonus\":{\"bonus\":0," +
                "\"oaCode\":\"\"}," +
                "\"certificateFile\":{\"fileKey\":\"\"," +
                "\"fileName\":\"\"," +
                "\"fileUrl\":\"\"}," +
                "\"coupon\":{\"picUrl\":\"\"," +
                "\"stock\":{\"totalCount\":\"10\"," +
                "\"soldCount\":0," +
                "\"lockedCount\":0," +
                "\"surplusCount\":\"10\"}," +
                "\"rule\":{\"buyTotalLimit\":\"2\"," +
                "\"buyDayLimit\":0," +
                "\"orderAmount\":0," +
                "\"startType\":1," +
                "\"periodType\":1," +
                "\"relativeDay\":\"2\"," +
                "\"" +
                "sendBeginTime\":\"" +
                sendBeginTime + "\"," +
                "\"" +
                "sendEndTime\":\"" +
                sendEndTime + "\"}," +
                "\"faceValue\":1000," +
                "\"couponType\":4," +
                "\"schemeType\":14," +
                "\"salesPrice\":1," +
                "\"marketValue\":0," +
                "\"saleType\":3," +
                "\"text\":\"不可与其他促销叠加，最终归属权归门店所有\"," +
                "\"hasBonus\":0," +
                "\"createCertificateType\":1," +
                "\"sourceMerchant\":\"\"," +
                "\"sourceMerchantName\":\"\"," +
                "\"bizTagId\":\"1\"}," +
                "\"stores\":[{\"address\":\"d15103\",\"brandMerchantId\":null,\"brandMerchantName\":null,\"cityId\":310100,\"cityName\":\"上海市\",\"countyId\":null,\"countyName\":null,\"logo\":\"http://img1.ffan.com/T1rSDTB_Jg1RCvBVdK\",\"merchantType\":null,\"name\":\"王者归来\",\"normalMerchantId\":2078472,\"normalMerchantName\":\"小康\",\"plazaId\":1000343,\"plazaName\":\"上海宝山万达广场开开心心一点也不开心不ka\",\"provinceId\":310000,\"provinceName\":null,\"storeId\":10365864,\"storeType\":1,\"tel\":\"13300011513\",\"selected\":true}]}";

        APIResponse resp = newCouponService
                .setHeaders(buildQfHeader())
                .buildBody(body)
                .request()
                .assertJValue("status", "200")
                .assertJNotEmpty("data.coupon.couponNo");

        String schemeId = resp.getJValue("data.scheme.id").toString();

        BLaunchCouponService newLaunchCouponService = new BLaunchCouponService(getUrlData());
        body = "{\"contentType\":\"coupon\"," +
                "\"channelId\":1003," +
                "\"showContentList\":[{" +
                "\"schemeId\":" +
                schemeId + "," +
                "\"advertisingId\":26," +
                "\"beginTime\":\"" +
                transferTime(sendBeginTime) + "\"," +
                "\"endTime\":\"" +
                transferTime(sendEndTime) + "\"," +
                "\"coverImg\":\"http://img1.ffan.com/T1.uZgBvV_1RCvBVdK\"," +
                "\"plazaId\":\"1000343\"}]}";
        resp = newLaunchCouponService
                .setHeaders(buildQfHeader())
                .setRequestBody(body)
                .request()
                .assertJValue("status", "200");

        BAuditListService auditListService = new BAuditListService(getUrlData());
        body = "contentName=&advertisingId=&channelId=&status=1&pageNum=1&pageSize=10&beginTime=&endTime=&timestr=1519889048803";
        resp = auditListService
                .setHeaders(buildQfHeader())
                .buildBody(body)
                .request()
                .assertJValue("status","200");
        String couponId = resp.getJValue("data[0].id").toString();

        BCouponAuditService couponAuditService = new BCouponAuditService(getUrlData());
        body = "[{\"id\":" +
                couponId + "," +
                "\"checked\":true,\"sit\":\"惠品空间\",\"imgWidth\":\"950\",\"imgHeight\":\"520\",\"imgUrl\":\"http://img1.ffan.com/T1wfJgBQV_1RCvBVdK\",\"img\":\"http://img1.ffan.com/T1wfJgBQV_1RCvBVdK\",\"date1\":\"2018-02-01 15:45:00.0\",\"date2\":\"2018-03-15 15:45:00.0\",\"dates\":[\"2018-02-01T07:45:00.000Z\",\"2018-03-15T07:45:00.000Z\"],\"beginTime\":\"2018-02-01T07:45:00.000Z\",\"endTime\":\"2018-03-15T07:45:00.000Z\",\"status\":4}]";
        resp = couponAuditService
                .setHeaders(buildQfHeader())
                .buildBody(body)
                .request()
                .assertJValue("status","200");
    }

    @Test(testName = "团购券默认选项测试", priority = 0, groups = {"Basic", "BCoupon"})
    public void b_group_purchase_coupon_default() {
        // 执行登录
        //login();

        BNewCouponService newCouponService = new BNewCouponService(getUrlData());

        // Body封装
        String title = "团购券-" + StringUtil.getRandomChar(5);
        Random rnd = new Random(100);
        int denomination = (rnd.nextInt(100) + 1) * 100;

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,+10);//把日期往后增加10天，若想把日期向前推则将正数改为负数
        date=calendar.getTime();

        String beginTime = df.format(System.currentTimeMillis());
        String endTime = df.format(date);
        String sendBeginTime = df.format(System.currentTimeMillis());
        String sendEndTime = df.format(date);

        String body = "{\"source\":2," +
                "\"scheme\":" +
                "{\"title\":\"" +
                title + "\"," +
                "\"subTitle\":\"" +
                title + "\"," +
                "\"tag\":\"\"," +
                "\"remark\":\"\"," +
                "\"img1\":\"http://img1.ffan.com/T1oDLgBbhg1RCvBVdK\"," + "\"img2\":\"\",\"img3\":\"\",\"img4\":\"\",\"img5\":\"\"," +
                "\"type\":13," +
                "\"storeId\":\"\"," +
                "\"storeLogo\":\"\"," +
                "\"storeName\":\"\"," +
                "\"storeAddress\":\"\"," +
                "\"storeTel\":\"\"," +
                "\"url\":\"http://qianfan.uat.ffan.com\"," +
                "\"status\":1," +
                "\"validBegin\":\"\"," +
                "\"validEnd\":\"\"," +
                "\"beginTime\":\"\"," +
                "\"endTime\":\"\"," +
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
                "\"shareContent\":\"团购券测试1\"," +
                "\"shareImage\":\"http://img1.ffan.com/T1oDLgBbhg1RCvBVdK\"," +
                "\"shareTitle\":\"团购券测试1\"," +
                "\"businessId\":1," +
                "\"couponType\":3}," +
                "\"bonus\":{\"bonus\":0,\"oaCode\":\"\"}," +
                "\"certificateFile\":{\"fileKey\":\"\"," +
                "\"fileName\":\"\"," +
                "\"fileUrl\":\"\"}," +
                "\"coupon\":{\"picUrl\":\"\"," +
                "\"stock\":{\"totalCount\":\"10\"," +
                "\"soldCount\":0," +
                "\"lockedCount\":0," +
                "\"surplusCount\":\"10\"}," +
                "\"rule\":{\"buyTotalLimit\":\"2\"," +
                "\"buyDayLimit\":0,\"orderAmount\":0," +
                "\"startType\":1," +
                "\"periodType\":1," +
                "\"relativeDay\":\"2\"," +
                "\"" +
                "sendBeginTime\":\"" +
                sendBeginTime + "\"," +
                "\"" +
                "sendEndTime\":\"" +
                sendEndTime + "\"}," +
                "\"faceValue\":0," +
                "\"couponType\":3," +
                "\"schemeType\":13," +
                "\"salesPrice\":1," +
                "\"marketValue\":1000," +
                "\"saleType\":3," +
                "\"text\":\"不可与其他促销叠加，最终归属权归门店所有\"," +
                "\"hasBonus\":0," +
                "\"createCertificateType\":1," +
                "\"sourceMerchant\":\"\"," +
                "\"sourceMerchantName\":\"\"," +
                "\"bizTagId\":\"1\"}," +
                "\"stores\":[{\"address\":\"d15103\"," +
                "\"brandMerchantId\":null," +
                "\"brandMerchantName\":null," +
                "\"cityId\":310100," +
                "\"cityName\":\"上海市\"," +
                "\"countyId\":null," +
                "\"countyName\":null," +
                "\"logo\":\"http://img1.ffan.com/T1rSDTB_Jg1RCvBVdK\"," +
                "\"merchantType\":null," +
                "\"name\":\"王者归来\"," +
                "\"normalMerchantId\":2078472," +
                "\"normalMerchantName\":\"小康\"," +
                "\"plazaId\":1000343," +
                "\"plazaName\":\"上海宝山万达广场开开心心一点也不开心不ka\"," +
                "\"provinceId\":310000," +
                "\"provinceName\":null," +
                "\"storeId\":10365864," +
                "\"storeType\":1," +
                "\"tel\":\"13300011513\"," +
                "\"selected\":true}]}";

        APIResponse resp = newCouponService
                .setHeaders(buildQfHeader())
                .buildBody(body)
                .request()
                .assertJValue("status", "200")
                .assertJNotEmpty("data.coupon.couponNo");

        String schemeId = resp.getJValue("data.scheme.id").toString();

        BLaunchCouponService newLaunchCouponService = new BLaunchCouponService(getUrlData());
        body = "{\"contentType\":\"coupon\"," +
                "\"channelId\":1003," +
                "\"showContentList\":[{" +
                "\"schemeId\":" +
                schemeId + "," +
                "\"advertisingId\":26," +
                "\"beginTime\":\"" +
                transferTime(sendBeginTime) + "\"," +
                "\"endTime\":\"" +
                transferTime(sendEndTime) + "\"," +
                "\"coverImg\":\"http://img1.ffan.com/T1.uZgBvV_1RCvBVdK\"," +
                "\"plazaId\":\"1000343\"}]}";
        resp = newLaunchCouponService
                .setHeaders(buildQfHeader())
                .setRequestBody(body)
                .request()
                .assertJValue("status", "200");

        BAuditListService auditListService = new BAuditListService(getUrlData());
        body = "contentName=&advertisingId=&channelId=&status=1&pageNum=1&pageSize=10&beginTime=&endTime=&timestr=1519889048803";
        resp = auditListService
                .setHeaders(buildQfHeader())
                .buildBody(body)
                .request()
                .assertJValue("status","200");
        String couponId = resp.getJValue("data[0].id").toString();

        BCouponAuditService couponAuditService = new BCouponAuditService(getUrlData());
        body = "[{\"id\":" +
                couponId + "," +
                "\"checked\":true,\"sit\":\"惠品空间\",\"imgWidth\":\"950\",\"imgHeight\":\"520\",\"imgUrl\":\"http://img1.ffan.com/T1wfJgBQV_1RCvBVdK\",\"img\":\"http://img1.ffan.com/T1wfJgBQV_1RCvBVdK\",\"date1\":\"2018-02-01 15:45:00.0\",\"date2\":\"2018-03-15 15:45:00.0\",\"dates\":[\"2018-02-01T07:45:00.000Z\",\"2018-03-15T07:45:00.000Z\"],\"beginTime\":\"2018-02-01T07:45:00.000Z\",\"endTime\":\"2018-03-15T07:45:00.000Z\",\"status\":4}]";
        resp = couponAuditService
                .setHeaders(buildQfHeader())
                .buildBody(body)
                .request()
                .assertJValue("status","200");
    }

    @Test(testName = "停车券默认选项测试", priority = 0, groups = {"Basic", "BCoupon"})
    public void b_park_coupon_default() {
        // 执行登录
        //login();

        BNewCouponService newCouponService = new BNewCouponService(getUrlData());

        // Body封装
        String title = "停车券-" + StringUtil.getRandomChar(5);
        Random rnd = new Random(100);
        int denomination = (rnd.nextInt(100) + 1) * 100;

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,+10);//把日期往后增加10天，若想把日期向前推则将正数改为负数
        date=calendar.getTime();

        String beginTime = df.format(System.currentTimeMillis());
        String endTime = df.format(date);
        String sendBeginTime = df.format(System.currentTimeMillis());
        String sendEndTime = df.format(date);

        String body = "{\"source\":2," +
                "\"scheme\":" +
                "{\"title\":\"" +
                title + "\"," +
                "\"subTitle\":\"" +
                title + "\"," +
                "\"tag\":\"\"," +
                "\"remark\":\"\"," +
                "\"img1\":\"http://img1.ffan.com/T1lfZgBQWT1RCvBVdK\"," +
                "\"img2\":\"\"," +
                "\"img3\":\"\"," +
                "\"img4\":\"\"," +
                "\"img5\":\"\"," +
                "\"type\":15," +
                "\"storeId\":\"\"," +
                "\"storeLogo\":\"\"," +
                "\"storeName\":\"\"," +
                "\"storeAddress\":\"\"," +
                "\"storeTel\":\"\"," +
                "\"url\":\"http://qianfan.uat.ffan.com\"," +
                "\"status\":1," +
                "\"validBegin\":\"\"," +
                "\"validEnd\":\"\"," +
                "\"" +
                "beginTime\":\"" +
                beginTime + "\"," +
                "\"" +
                "endTime\":\"" +
                endTime + "\"," +
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
                "\"shareContent\":" +
                "\"Brandy停车券\"," +
                "\"shareImage\":" +
                "\"http://img1.ffan.com/T1lfZgBQWT1RCvBVdK\"," +
                "\"shareTitle\":\"Brandy停车券\"," +
                "\"businessId\":2," +
                "\"couponType\":5}," +
                "\"bonus\":{\"bonus\":0," +
                "\"oaCode\":\"\"}," +
                "\"certificateFile\":{\"fileKey\":\"\"," +
                "\"fileName\":\"\"," +
                "\"fileUrl\":\"\"}," +
                "\"coupon\":{\"picUrl\":\"\"," +
                "\"stock\":{\"totalCount\":\"10\"," +
                "\"soldCount\":0,\"lockedCount\":0," +
                "\"surplusCount\":\"10\"}," +
                "\"rule\":{\"buyTotalLimit\":\"2\"," +
                "\"buyDayLimit\":0," +
                "\"orderAmount\":0," +
                "\"startType\":1," +
                "\"periodType\":0," +
                "\"relativeDay\":\"2\"," +
                "\"" +
                "sendBeginTime\":\"" +
                sendBeginTime + "\"," +
                "\"" +
                "sendEndTime\":\"" +
                sendEndTime + "\"}," +
                "\"faceValue\":1," +
                "\"couponType\":5," +
                "\"schemeType\":15," +
                "\"salesPrice\":0," +
                "\"marketValue\":0," +
                "\"saleType\":1," +
                "\"text\":\"不可与其他促销叠加，最终归属权归门店所有\"," +
                "\"hasBonus\":0," +
                "\"createCertificateType\":1," +
                "\"sourceMerchant\":\"\"," +
                "\"sourceMerchantName\":\"\"," +
                "\"bizTagId\":\"\"}," +
                "\"stores\":[{\"merchantId\":10027427,\"storeId\":10365984," +
                "\"storeName\":\"宝山万达停车场\"," +
                "\"brandIds\":[10001790]," +
                "\"oriBrandIds\":\"10001790\"," +
                "\"brandNames\":\"ETCP\"," +
                "\"plazaId\":\"1000343\"," +
                "\"plazaName\":\"上海宝山万达广场开开心心一点也不开心不ka\"," +
                "\"startDate\":1505182824000," +
                "\"endDate\":1523499624000," +
                "\"subContractId\":33718," +
                "\"contractId\":33717," +
                "\"sourceId\":\"default\"," +
                "\"name\":\"宝山万达停车场\"," +
                "\"logo\":\"http://img1.ffan.com/T1sgJTB5Cj1RCvBVdK\"," +
                "\"tel\":\"021-88889999\"," +
                "\"address\":\"上海市黄埔区\"," +
                "\"selected\":true}]}";

        APIResponse resp = newCouponService
                .setHeaders(buildQfHeader())
                .buildBody(body)
                .request()
                .assertJValue("status", "200")
                .assertJNotEmpty("data.coupon.couponNo");

        String schemeId = resp.getJValue("data.scheme.id").toString();

        BLaunchCouponService newLaunchCouponService = new BLaunchCouponService(getUrlData());
        body = "{\"contentType\":\"coupon\"," +
                "\"channelId\":1003," +
                "\"showContentList\":[{" +
                "\"schemeId\":" +
                schemeId + "," +
                "\"advertisingId\":26," +
                "\"beginTime\":\"" +
                transferTime(sendBeginTime) + "\"," +
                "\"endTime\":\"" +
                transferTime(sendEndTime) + "\"," +
                "\"coverImg\":\"http://img1.ffan.com/T1.uZgBvV_1RCvBVdK\"," +
                "\"plazaId\":\"1000343\"}]}";
        resp = newLaunchCouponService
                .setHeaders(buildQfHeader())
                .setRequestBody(body)
                .request()
                .assertJValue("status", "200");

        BAuditListService auditListService = new BAuditListService(getUrlData());
        body = "contentName=&advertisingId=&channelId=&status=1&pageNum=1&pageSize=10&beginTime=&endTime=&timestr=1519889048803";
        resp = auditListService
                .setHeaders(buildQfHeader())
                .buildBody(body)
                .request()
                .assertJValue("status","200");
        String couponId = resp.getJValue("data[0].id").toString();

        BCouponAuditService couponAuditService = new BCouponAuditService(getUrlData());
        body = "[{\"id\":" +
                couponId + "," +
                "\"checked\":true,\"sit\":\"惠品空间\",\"imgWidth\":\"950\",\"imgHeight\":\"520\",\"imgUrl\":\"http://img1.ffan.com/T1wfJgBQV_1RCvBVdK\",\"img\":\"http://img1.ffan.com/T1wfJgBQV_1RCvBVdK\",\"date1\":\"2018-02-01 15:45:00.0\",\"date2\":\"2018-03-15 15:45:00.0\",\"dates\":[\"2018-02-01T07:45:00.000Z\",\"2018-03-15T07:45:00.000Z\"],\"beginTime\":\"2018-02-01T07:45:00.000Z\",\"endTime\":\"2018-03-15T07:45:00.000Z\",\"status\":4}]";
        resp = couponAuditService
                .setHeaders(buildQfHeader())
                .buildBody(body)
                .request()
                .assertJValue("status","200");
    }

    @Test(testName = "礼品券默认选项测试", priority = 0, groups = {"Basic", "BCoupon"})
    public void b_gift_coupon_default() {
        // 执行登录
        //login();

        BNewCouponService newCouponService = new BNewCouponService(getUrlData());

        // Body封装
        String title = "礼品券-" + StringUtil.getRandomChar(5);
        Random rnd = new Random(100);
        int denomination = (rnd.nextInt(100) + 1) * 100;

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,+10);//把日期往后增加10天，若想把日期向前推则将正数改为负数
        date=calendar.getTime();

        String beginTime = df.format(System.currentTimeMillis());
        String endTime = df.format(date);
        String sendBeginTime = df.format(System.currentTimeMillis());
        String sendEndTime = df.format(date);

        String body = "{\"source\":2," +
                "\"scheme\":" +
                "{\"title\":\"" +
                title + "\"," +
                "\"subTitle\":\"" +
                title + "\"," +
                "\"tag\":\"\"," +
                "\"remark\":\"\"," +
                "\"img1\":\"http://img1.ffan.com/T1puJgBgx_1RCvBVdK\"," +
                "\"img2\":\"\"," +
                "\"img3\":\"\"," +
                "\"img4\":\"\"," +
                "\"img5\":\"\"," +
                "\"type\":12," +
                "\"storeId\":\"\"," +
                "\"storeLogo\":\"\"," +
                "\"storeName\":\"\"," +
                "\"storeAddress\":\"\"," +
                "\"storeTel\":\"\"," +
                "\"url\":\"\"," +
                "\"status\":1," +
                "\"validBegin\":\"\"," +
                "\"validEnd\":\"\"," +
                "\"" +
                "beginTime\":\"" +
                beginTime + "\"," +
                "\"" +
                "endTime\":\"" +
                endTime + "\"," +
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
                "\"shareContent\":\"礼品券\"," +
                "\"shareImage\":\"http://img1.ffan.com/T1puJgBgx_1RCvBVdK\"," +
                "\"shareTitle\":\"礼品券\"," +
                "\"businessId\":1," +
                "\"couponType\":2}," +
                "\"bonus\":{\"bonus\":0," +
                "\"oaCode\":\"\"}," +
                "\"certificateFile\":{\"fileKey\":\"\"," +
                "\"fileName\":\"\"," +
                "\"fileUrl\":\"\"}," +
                "\"coupon\":{\"picUrl\":\"\"," +
                "\"stock\":{\"totalCount\":\"10\"," +
                "\"soldCount\":0," +
                "\"lockedCount\":0," +
                "\"surplusCount\":\"10\"}," +
                "\"rule\":{\"buyTotalLimit\":\"2\"," +
                "\"buyDayLimit\":0," +
                "\"orderAmount\":0," +
                "\"startType\":1," +
                "\"periodType\":1," +
                "\"relativeDay\":\"2\"," +
                "\"" +
                "sendBeginTime\":\"" +
                sendBeginTime + "\"," +
                "\"" +
                "sendEndTime\":\"" +
                sendEndTime + "\"}," +
                "\"faceValue\":0," +
                "\"couponType\":3," +
                "\"schemeType\":13," +
                "\"salesPrice\":1," +
                "\"marketValue\":1000," +
                "\"saleType\":1," +
                "\"text\":\"不可与其他促销叠加，最终归属权归门店所有\"," +
                "\"hasBonus\":0," +
                "\"createCertificateType\":1," +
                "\"sourceMerchant\":\"\"," +
                "\"sourceMerchantName\":\"\"," +
                "\"bizTagId\":\"1\"}," +
                "\"stores\":[{\"address\":\"d15103\"," +
                "\"brandMerchantId\":null," +
                "\"brandMerchantName\":null," +
                "\"cityId\":310100," +
                "\"cityName\":\"上海市\"," +
                "\"countyId\":null," +
                "\"countyName\":null," +
                "\"logo\":\"http://img1.ffan.com/T1rSDTB_Jg1RCvBVdK\"," +
                "\"merchantType\":null," +
                "\"name\":\"王者归来\"," +
                "\"normalMerchantId\":2078472," +
                "\"normalMerchantName\":\"小康\"," +
                "\"plazaId\":1000343," +
                "\"plazaName\":\"上海宝山万达广场开开心心一点也不开心不ka\"," +
                "\"provinceId\":310000," +
                "\"provinceName\":null," +
                "\"storeId\":10365864," +
                "\"storeType\":1," +
                "\"tel\":\"13300011513\"," +
                "\"selected\":true}]}";

        APIResponse resp = newCouponService
                .setHeaders(buildQfHeader())
                .buildBody(body)
                .request()
                .assertJValue("status", "200")
                .assertJNotEmpty("data.coupon.couponNo");

        String schemeId = resp.getJValue("data.scheme.id").toString();

        BLaunchCouponService newLaunchCouponService = new BLaunchCouponService(getUrlData());
        body = "{\"contentType\":\"coupon\"," +
                "\"channelId\":1003," +
                "\"showContentList\":[{" +
                "\"schemeId\":" +
                schemeId + "," +
                "\"advertisingId\":26," +
                "\"beginTime\":\"" +
                transferTime(sendBeginTime) + "\"," +
                "\"endTime\":\"" +
                transferTime(sendEndTime) + "\"," +
                "\"coverImg\":\"http://img1.ffan.com/T1.uZgBvV_1RCvBVdK\"," +
                "\"plazaId\":\"1000343\"}]}";
        resp = newLaunchCouponService
                .setHeaders(buildQfHeader())
                .setRequestBody(body)
                .request()
                .assertJValue("status", "200");

        BAuditListService auditListService = new BAuditListService(getUrlData());
        body = "contentName=&advertisingId=&channelId=&status=1&pageNum=1&pageSize=10&beginTime=&endTime=&timestr=1519889048803";
        resp = auditListService
                .setHeaders(buildQfHeader())
                .buildBody(body)
                .request()
                .assertJValue("status","200");
        String couponId = resp.getJValue("data[0].id").toString();

        BCouponAuditService couponAuditService = new BCouponAuditService(getUrlData());
        body = "[{\"id\":" +
                couponId + "," +
                "\"checked\":true,\"sit\":\"惠品空间\",\"imgWidth\":\"950\",\"imgHeight\":\"520\",\"imgUrl\":\"http://img1.ffan.com/T1wfJgBQV_1RCvBVdK\",\"img\":\"http://img1.ffan.com/T1wfJgBQV_1RCvBVdK\",\"date1\":\"2018-02-01 15:45:00.0\",\"date2\":\"2018-03-15 15:45:00.0\",\"dates\":[\"2018-02-01T07:45:00.000Z\",\"2018-03-15T07:45:00.000Z\"],\"beginTime\":\"2018-02-01T07:45:00.000Z\",\"endTime\":\"2018-03-15T07:45:00.000Z\",\"status\":4}]";
        resp = couponAuditService
                .setHeaders(buildQfHeader())
                .buildBody(body)
                .request()
                .assertJValue("status","200");
    }

    @Test(testName = "满减券默认选项测试", priority = 0, groups = {"Basic", "BCoupon"})
    public void b_discount_coupon_default() {
        // 执行登录
        //login();

        BNewCouponService newCouponService = new BNewCouponService(getUrlData());

        // Body封装
        String title = "满减券-" + StringUtil.getRandomChar(5);
        Random rnd = new Random(100);
        int denomination = (rnd.nextInt(100) + 1) * 100;
        String url = "";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,+10);//把日期往后增加10天，若想把日期向前推则将正数改为负数
        date=calendar.getTime();

        String beginTime = df.format(System.currentTimeMillis());
        String endTime = df.format(date);
        String sendBeginTime = df.format(System.currentTimeMillis());
        String sendEndTime = df.format(date);

        String body = "{\"source\":2," +
                "\"scheme\":" +
                "{\"title\":\"" +
                title + "\"," +
                "\"subTitle\":\"" +
                title + "\"," +
                "\"tag\":\"\"," +
                "\"remark\":\"\"," +
                "\"img1\":\"http://img1.ffan.com/T1f_LgBmKg1RCvBVdK\"," +
                "\"img2\":\"\"," +
                "\"img3\":\"\"," +
                "\"img4\":\"\"," +
                "\"img5\":\"\"," +
                "\"type\":10," +
                "\"storeId\":\"\"," +
                "\"storeLogo\":\"\"," +
                "\"storeName\":\"\"," +
                "\"storeAddress\":\"\"," +
                "\"storeTel\":\"\"," +
                "\"url\":\"http://qianfan.uat.ffan.com\"," +
                "\"status\":1," +
                "\"validBegin\":\"\"," +
                "\"validEnd\":\"\"," +
                "\"" +
                "beginTime\":\"" +
                beginTime + "\"," +
                "\"" +
                "endTime\":\"" +
                endTime + "\"," +
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
                "\"shareContent\":\"价值0.01元优惠券，满10元使用\"," +
                "\"shareImage\":\"http://img1.ffan.com/T1f_LgBmKg1RCvBVdK\"," +
                "\"shareTitle\":\"满减券\"," +
                "\"businessId\":1," +
                "\"couponType\":1}," +
                "\"bonus\":{\"bonus\":0,\"oaCode\":\"\"}," +
                "\"certificateFile\":{\"fileKey\":\"\"," +
                "\"fileName\":\"\"," +
                "\"fileUrl\":\"\"}," +
                "\"coupon\":{\"picUrl\":\"\"," +
                "\"stock\":{\"totalCount\":\"10\"," +
                "\"soldCount\":0," +
                "\"lockedCount\":0," +
                "\"surplusCount\":\"10\"}," +
                "\"rule\":{\"buyTotalLimit\":\"2\"," +
                "\"buyDayLimit\":0," +
                "\"orderAmount\":1000," +
                "\"startType\":1," +
                "\"periodType\":1," +
                "\"relativeDay\":\"2\"," +
                "\"" +
                "sendBeginTime\":\"" +
                sendBeginTime + "\"," +
                "\"" +
                "sendEndTime\":\"" +
                sendEndTime + "\"}," +
                "\"faceValue\":1," +
                "\"couponType\":1," +
                "\"schemeType\":10," +
                "\"salesPrice\":0," +
                "\"marketValue\":0," +
                "\"saleType\":1," +
                "\"text\":\"不可与其他促销叠加，最终归属权归门店所有\"," +
                "\"hasBonus\":0," +
                "\"createCertificateType\":1," +
                "\"sourceMerchant\":\"\"," +
                "\"sourceMerchantName\":\"\"," +
                "\"bizTagId\":\"1\"}," +
                "\"stores\":[{\"address\":\"d15103\"," +
                "\"brandMerchantId\":null," +
                "\"brandMerchantName\":null," +
                "\"cityId\":310100," +
                "\"cityName\":\"上海市\"," +
                "\"countyId\":null," +
                "\"countyName\":null," +
                "\"logo\":\"http://img1.ffan.com/T1rSDTB_Jg1RCvBVdK\"," +
                "\"merchantType\":null," +
                "\"name\":\"王者归来\"," +
                "\"normalMerchantId\":2078472," +
                "\"normalMerchantName\":\"小康\"," +
                "\"plazaId\":1000343," +
                "\"plazaName\":\"上海宝山万达广场开开心心一点也不开心不ka\"," +
                "\"provinceId\":310000," +
                "\"provinceName\":null," +
                "\"storeId\":10365864," +
                "\"storeType\":1," +
                "\"tel\":\"13300011513\"," +
                "\"selected\":true}]}";

        APIResponse resp = newCouponService
                .setHeaders(buildQfHeader())
                .buildBody(body)
                .request()
                .assertJValue("status", "200")
                .assertJNotEmpty("data.coupon.couponNo");

        String schemeId = resp.getJValue("data.scheme.id").toString();

        BLaunchCouponService newLaunchCouponService = new BLaunchCouponService(getUrlData());
        body = "{\"contentType\":\"coupon\"," +
                "\"channelId\":1003," +
                "\"showContentList\":[{" +
                "\"schemeId\":" +
                schemeId + "," +
                "\"advertisingId\":26," +
                "\"beginTime\":\"" +
                transferTime(sendBeginTime) + "\"," +
                "\"endTime\":\"" +
                transferTime(sendEndTime) + "\"," +
                "\"coverImg\":\"http://img1.ffan.com/T1.uZgBvV_1RCvBVdK\"," +
                "\"plazaId\":\"1000343\"}]}";
        resp = newLaunchCouponService
                .setHeaders(buildQfHeader())
                .setRequestBody(body)
                .request()
                .assertJValue("status", "200");

        BAuditListService auditListService = new BAuditListService(getUrlData());
        body = "contentName=&advertisingId=&channelId=&status=1&pageNum=1&pageSize=10&beginTime=&endTime=&timestr=1519889048803";
        resp = auditListService
                .setHeaders(buildQfHeader())
                .buildBody(body)
                .request()
                .assertJValue("status","200");
        String couponId = resp.getJValue("data[0].id").toString();

        BCouponAuditService couponAuditService = new BCouponAuditService(getUrlData());
        body = "[{\"id\":" +
                couponId + "," +
                "\"checked\":true,\"sit\":\"惠品空间\",\"imgWidth\":\"950\",\"imgHeight\":\"520\",\"imgUrl\":\"http://img1.ffan.com/T1wfJgBQV_1RCvBVdK\",\"img\":\"http://img1.ffan.com/T1wfJgBQV_1RCvBVdK\",\"date1\":\"2018-02-01 15:45:00.0\",\"date2\":\"2018-03-15 15:45:00.0\",\"dates\":[\"2018-02-01T07:45:00.000Z\",\"2018-03-15T07:45:00.000Z\"],\"beginTime\":\"2018-02-01T07:45:00.000Z\",\"endTime\":\"2018-03-15T07:45:00.000Z\",\"status\":4}]";
        resp = couponAuditService
                .setHeaders(buildQfHeader())
                .buildBody(body)
                .request()
                .assertJValue("status","200");
    }

    @Test(testName = "停车券精准发券测试", priority = 0, groups = {"Basic", "BCoupon"})
    public void b_park_coupon_precise_default() {
        // 执行登录
        //login();

        BNewCouponService newCouponService = new BNewCouponService(getUrlData());

        // Body封装
        String title = "停车券-" + StringUtil.getRandomChar(5);
        Random rnd = new Random(100);
        int denomination = (rnd.nextInt(100) + 1) * 100;

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,+10);//把日期往后增加10天，若想把日期向前推则将正数改为负数
        date=calendar.getTime();

        String beginTime = df.format(System.currentTimeMillis());
        String endTime = df.format(date);
        String sendBeginTime = df.format(System.currentTimeMillis());
        String sendEndTime = df.format(date);

        String body = "{\"source\":2," +
                "\"scheme\":" +
                "{\"title\":\"" +
                title + "\"," +
                "\"subTitle\":\"" +
                title + "\"," +
                "\"tag\":\"\"," +
                "\"remark\":\"\"," +
                "\"img1\":\"http://img1.ffan.com/T1lfZgBQWT1RCvBVdK\"," +
                "\"img2\":\"\"," +
                "\"img3\":\"\"," +
                "\"img4\":\"\"," +
                "\"img5\":\"\"," +
                "\"type\":15," +
                "\"storeId\":\"\"," +
                "\"storeLogo\":\"\"," +
                "\"storeName\":\"\"," +
                "\"storeAddress\":\"\"," +
                "\"storeTel\":\"\"," +
                "\"url\":\"http://qianfan.uat.ffan.com\"," +
                "\"status\":1," +
                "\"validBegin\":\"\"," +
                "\"validEnd\":\"\"," +
                "\"" +
                "beginTime\":\"" +
                beginTime + "\"," +
                "\"" +
                "endTime\":\"" +
                endTime + "\"," +
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
                "\"shareContent\":" +
                "\"Brandy停车券\"," +
                "\"shareImage\":" +
                "\"http://img1.ffan.com/T1lfZgBQWT1RCvBVdK\"," +
                "\"shareTitle\":\"Brandy停车券\"," +
                "\"businessId\":2," +
                "\"couponType\":5}," +
                "\"bonus\":{\"bonus\":0," +
                "\"oaCode\":\"\"}," +
                "\"certificateFile\":{\"fileKey\":\"\"," +
                "\"fileName\":\"\"," +
                "\"fileUrl\":\"\"}," +
                "\"coupon\":{\"picUrl\":\"\"," +
                "\"stock\":{\"totalCount\":\"10\"," +
                "\"soldCount\":0,\"lockedCount\":0," +
                "\"surplusCount\":\"10\"}," +
                "\"rule\":{\"buyTotalLimit\":\"2\"," +
                "\"buyDayLimit\":0," +
                "\"orderAmount\":0," +
                "\"startType\":1," +
                "\"periodType\":0," +
                "\"relativeDay\":\"2\"," +
                "\"" +
                "sendBeginTime\":\"" +
                sendBeginTime + "\"," +
                "\"" +
                "sendEndTime\":\"" +
                sendEndTime + "\"}," +
                "\"faceValue\":1," +
                "\"couponType\":5," +
                "\"schemeType\":15," +
                "\"salesPrice\":0," +
                "\"marketValue\":0," +
                "\"saleType\":0," +
                "\"text\":\"不可与其他促销叠加，最终归属权归门店所有\"," +
                "\"hasBonus\":0," +
                "\"createCertificateType\":1," +
                "\"sourceMerchant\":\"\"," +
                "\"sourceMerchantName\":\"\"," +
                "\"bizTagId\":\"\"}," +
                "\"stores\":[{\"merchantId\":10027427,\"storeId\":10365984," +
                "\"storeName\":\"宝山万达停车场\"," +
                "\"brandIds\":[10001790]," +
                "\"oriBrandIds\":\"10001790\"," +
                "\"brandNames\":\"ETCP\"," +
                "\"plazaId\":\"1000343\"," +
                "\"plazaName\":\"上海宝山万达广场开开心心一点也不开心不ka\"," +
                "\"startDate\":1505182824000," +
                "\"endDate\":1523499624000," +
                "\"subContractId\":33718," +
                "\"contractId\":33717," +
                "\"sourceId\":\"default\"," +
                "\"name\":\"宝山万达停车场\"," +
                "\"logo\":\"http://img1.ffan.com/T1sgJTB5Cj1RCvBVdK\"," +
                "\"tel\":\"021-88889999\"," +
                "\"address\":\"上海市黄埔区\"," +
                "\"selected\":true}]}";

        APIResponse resp = newCouponService
                .setHeaders(buildQfHeader())
                .buildBody(body)
                .request()
                .assertJValue("status", "200")
                .assertJNotEmpty("data.coupon.couponNo");

        String schemeId = resp.getJValue("data.scheme.id").toString();

        BLaunchCouponService newLaunchCouponService = new BLaunchCouponService(getUrlData());
        body = "{\"contentType\":\"coupon\"," +
                "\"channelId\":1003," +
                "\"showContentList\":[{" +
                "\"schemeId\":" +
                schemeId + "," +
                "\"advertisingId\":26," +
                "\"beginTime\":\"" +
                transferTime(sendBeginTime) + "\"," +
                "\"endTime\":\"" +
                transferTime(sendEndTime) + "\"," +
                "\"coverImg\":\"http://img1.ffan.com/T1.uZgBvV_1RCvBVdK\"," +
                "\"plazaId\":\"1000343\"}]}";
        resp = newLaunchCouponService
                .setHeaders(buildQfHeader())
                .setRequestBody(body)
                .request()
                .assertJValue("status", "200");

        BAuditListService auditListService = new BAuditListService(getUrlData());
        body = "contentName=&advertisingId=&channelId=&status=1&pageNum=1&pageSize=10&beginTime=&endTime=&timestr=1519889048803";
        resp = auditListService
                .setHeaders(buildQfHeader())
                .buildBody(body)
                .request()
                .assertJValue("status","200");
        String couponId = resp.getJValue("data[0].id").toString();

        BCouponAuditService couponAuditService = new BCouponAuditService(getUrlData());
        body = "[{\"id\":" +
                couponId + "," +
                "\"checked\":true,\"sit\":\"惠品空间\",\"imgWidth\":\"950\",\"imgHeight\":\"520\",\"imgUrl\":\"http://img1.ffan.com/T1wfJgBQV_1RCvBVdK\",\"img\":\"http://img1.ffan.com/T1wfJgBQV_1RCvBVdK\",\"date1\":\"2018-02-01 15:45:00.0\",\"date2\":\"2018-03-15 15:45:00.0\",\"dates\":[\"2018-02-01T07:45:00.000Z\",\"2018-03-15T07:45:00.000Z\"],\"beginTime\":\"2018-02-01T07:45:00.000Z\",\"endTime\":\"2018-03-15T07:45:00.000Z\",\"status\":4}]";
        resp = couponAuditService
                .setHeaders(buildQfHeader())
                .buildBody(body)
                .request()
                .assertJValue("status","200");
    }

    @Test(testName = "礼品券精准发券测试", priority = 0, groups = {"Basic", "BCoupon"})
    public void b_gift_coupon_precise_default() {
        // 执行登录
        //login();

        BNewCouponService newCouponService = new BNewCouponService(getUrlData());

        // Body封装
        String title = "礼品券-" + StringUtil.getRandomChar(5);
        Random rnd = new Random(100);
        int denomination = (rnd.nextInt(100) + 1) * 100;

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,+10);//把日期往后增加10天，若想把日期向前推则将正数改为负数
        date=calendar.getTime();

        String beginTime = df.format(System.currentTimeMillis());
        String endTime = df.format(date);
        String sendBeginTime = df.format(System.currentTimeMillis());
        String sendEndTime = df.format(date);

        String body = "{\"source\":2," +
                "\"scheme\":" +
                "{\"title\":\"" +
                title + "\"," +
                "\"subTitle\":\"" +
                title + "\"," +
                "\"tag\":\"\"," +
                "\"remark\":\"\"," +
                "\"img1\":\"http://img1.ffan.com/T1puJgBgx_1RCvBVdK\"," +
                "\"img2\":\"\"," +
                "\"img3\":\"\"," +
                "\"img4\":\"\"," +
                "\"img5\":\"\"," +
                "\"type\":12," +
                "\"storeId\":\"\"," +
                "\"storeLogo\":\"\"," +
                "\"storeName\":\"\"," +
                "\"storeAddress\":\"\"," +
                "\"storeTel\":\"\"," +
                "\"url\":\"\"," +
                "\"status\":1," +
                "\"validBegin\":\"\"," +
                "\"validEnd\":\"\"," +
                "\"" +
                "beginTime\":\"" +
                beginTime + "\"," +
                "\"" +
                "endTime\":\"" +
                endTime + "\"," +
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
                "\"shareContent\":\"礼品券\"," +
                "\"shareImage\":\"http://img1.ffan.com/T1puJgBgx_1RCvBVdK\"," +
                "\"shareTitle\":\"礼品券\"," +
                "\"businessId\":1," +
                "\"couponType\":2}," +
                "\"bonus\":{\"bonus\":0," +
                "\"oaCode\":\"\"}," +
                "\"certificateFile\":{\"fileKey\":\"\"," +
                "\"fileName\":\"\"," +
                "\"fileUrl\":\"\"}," +
                "\"coupon\":{\"picUrl\":\"\"," +
                "\"stock\":{\"totalCount\":\"10\"," +
                "\"soldCount\":0," +
                "\"lockedCount\":0," +
                "\"surplusCount\":\"10\"}," +
                "\"rule\":{\"buyTotalLimit\":\"2\"," +
                "\"buyDayLimit\":0," +
                "\"orderAmount\":0," +
                "\"startType\":1," +
                "\"periodType\":1," +
                "\"relativeDay\":\"2\"," +
                "\"" +
                "sendBeginTime\":\"" +
                sendBeginTime + "\"," +
                "\"" +
                "sendEndTime\":\"" +
                sendEndTime + "\"}," +
                "\"faceValue\":0," +
                "\"couponType\":3," +
                "\"schemeType\":13," +
                "\"salesPrice\":1," +
                "\"marketValue\":1000," +
                "\"saleType\":0," +
                "\"text\":\"不可与其他促销叠加，最终归属权归门店所有\"," +
                "\"hasBonus\":0," +
                "\"createCertificateType\":1," +
                "\"sourceMerchant\":\"\"," +
                "\"sourceMerchantName\":\"\"," +
                "\"bizTagId\":\"1\"}," +
                "\"stores\":[{\"address\":\"d15103\"," +
                "\"brandMerchantId\":null," +
                "\"brandMerchantName\":null," +
                "\"cityId\":310100," +
                "\"cityName\":\"上海市\"," +
                "\"countyId\":null," +
                "\"countyName\":null," +
                "\"logo\":\"http://img1.ffan.com/T1rSDTB_Jg1RCvBVdK\"," +
                "\"merchantType\":null," +
                "\"name\":\"王者归来\"," +
                "\"normalMerchantId\":2078472," +
                "\"normalMerchantName\":\"小康\"," +
                "\"plazaId\":1000343," +
                "\"plazaName\":\"上海宝山万达广场开开心心一点也不开心不ka\"," +
                "\"provinceId\":310000," +
                "\"provinceName\":null," +
                "\"storeId\":10365864," +
                "\"storeType\":1," +
                "\"tel\":\"13300011513\"," +
                "\"selected\":true}]}";

        APIResponse resp = newCouponService
                .setHeaders(buildQfHeader())
                .buildBody(body)
                .request()
                .assertJValue("status", "200")
                .assertJNotEmpty("data.coupon.couponNo");

        String schemeId = resp.getJValue("data.scheme.id").toString();

        BLaunchCouponService newLaunchCouponService = new BLaunchCouponService(getUrlData());
        body = "{\"contentType\":\"coupon\"," +
                "\"channelId\":1003," +
                "\"showContentList\":[{" +
                "\"schemeId\":" +
                schemeId + "," +
                "\"advertisingId\":26," +
                "\"beginTime\":\"" +
                transferTime(sendBeginTime) + "\"," +
                "\"endTime\":\"" +
                transferTime(sendEndTime) + "\"," +
                "\"coverImg\":\"http://img1.ffan.com/T1.uZgBvV_1RCvBVdK\"," +
                "\"plazaId\":\"1000343\"}]}";
        resp = newLaunchCouponService
                .setHeaders(buildQfHeader())
                .setRequestBody(body)
                .request()
                .assertJValue("status", "200");

        BAuditListService auditListService = new BAuditListService(getUrlData());
        body = "contentName=&advertisingId=&channelId=&status=1&pageNum=1&pageSize=10&beginTime=&endTime=&timestr=1519889048803";
        resp = auditListService
                .setHeaders(buildQfHeader())
                .buildBody(body)
                .request()
                .assertJValue("status","200");
        String couponId = resp.getJValue("data[0].id").toString();

        BCouponAuditService couponAuditService = new BCouponAuditService(getUrlData());
        body = "[{\"id\":" +
                couponId + "," +
                "\"checked\":true,\"sit\":\"惠品空间\",\"imgWidth\":\"950\",\"imgHeight\":\"520\",\"imgUrl\":\"http://img1.ffan.com/T1wfJgBQV_1RCvBVdK\",\"img\":\"http://img1.ffan.com/T1wfJgBQV_1RCvBVdK\",\"date1\":\"2018-02-01 15:45:00.0\",\"date2\":\"2018-03-15 15:45:00.0\",\"dates\":[\"2018-02-01T07:45:00.000Z\",\"2018-03-15T07:45:00.000Z\"],\"beginTime\":\"2018-02-01T07:45:00.000Z\",\"endTime\":\"2018-03-15T07:45:00.000Z\",\"status\":4}]";
        resp = couponAuditService
                .setHeaders(buildQfHeader())
                .buildBody(body)
                .request()
                .assertJValue("status","200");
    }

    public String transferTime(String time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        try {
            date = df.parse(time);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }

        calendar.setTime(date);
        calendar.add(calendar.HOUR,-8);//把日期往后增加10天，若想把日期向前推则将正数改为负数
        date = calendar.getTime();
        time = df.format(date);

        String stringDate = time.substring(0,10);
        String stringHH = time.substring(11,13);
        String stringMM = time.substring(14,16);
        String stringSS = time.substring(17,19);
        return stringDate + "T" + stringHH + ":" + stringMM + ":" + stringSS + ".000Z";
    }
}
