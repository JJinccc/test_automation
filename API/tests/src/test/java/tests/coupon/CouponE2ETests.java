package tests.coupon;

import com.ffan.qa.biz.services.coupon.BQueryCouponsService;
import com.ffan.qa.biz.services.coupon.CPlaCouponListService;
import com.ffan.qa.common.type.AdvertisementType;
import com.ffan.qa.common.type.CouponType;
import com.ffan.qa.errands.CouponErrands;
import com.ffan.qa.network.APIResponse;
import com.ffan.qa.utils.ObjectUtil;
import com.ffan.qa.utils.SystemUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

public class CouponE2ETests extends CouponTestBase {
    private Integer waitSec = 500;

    @Test(
            testName = "满减券E2E",
            description = "新建满减券->投放券列表->审核->小程序领用",
            priority = 0,
            groups = {"E2E", "Online"}
    )
    public void discount_coupon_list_E2E() {
        Map<String, String> coupon = null;
        try {
            // 创建券
            coupon = CouponErrands.createCoupon(CouponType.DiscountCoupon);
            String schemeId = coupon.get("schemeId");
            String couponNo = coupon.get("couponNo");

            // 投放到券列表
            CouponErrands.launchCoupon(AdvertisementType.CouponList, schemeId);

            // 审核
            CouponErrands.auditCoupon(couponNo);

            // 验证小程序券列表可以找到刚刚创建的券
            boolean hasCoupon = CouponErrands.waitList(getBaseData("wxPlazaId").toString(), "couponList", couponNo, waitSec);
            Assert.assertTrue(hasCoupon, "小程序券列表未发现券：" + coupon.get("couponNo"));

            // 登录
            wxLogin(getBaseData("wxPhone").toString(), getBaseData("wxOpenId").toString(), getBaseData("wxPlazaId").toString());

            // 领券
            String orderNo = CouponErrands.orderCoupon(cLoginModel, "couponList", getBaseData("wxPlazaId").toString(), couponNo, "0");

            // 获取订单内CouponNo
            String productCouponNo = CouponErrands.getOrderCouponNo(cLoginModel, orderNo);

            // 核销
            CouponErrands.checkCoupon(coupon.get("storeId"), productCouponNo, cLoginModel.getMemberId());
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (null != coupon) {
                CouponErrands.offsaleCoupon(coupon.get("schemeId"));
            }
        }
    }

    @Test(
            testName = "代金券E2E",
            description = "新建代金券->投放限时抢购->审核->小程序领用",
            priority = 0,
            groups = {"E2E", "Online"}
    )
    public void cash_coupon_flash_sale_E2E() {
        Map<String, String> coupon = null;
        String auditId = null;
        try {
            // 创建券
            coupon = CouponErrands.createCoupon(CouponType.CashCoupon);
            String schemeId = coupon.get("schemeId");
            String couponNo = coupon.get("couponNo");

            // 投放到券列表
            CouponErrands.launchCoupon(AdvertisementType.FlashSale, schemeId);

            // 审核
            auditId = CouponErrands.auditCoupon(couponNo);

            // 验证小程序券列表可以找到刚刚创建的券
            boolean hasCoupon = CouponErrands.waitList(getBaseData("wxPlazaId").toString(), "flashSale", coupon.get("couponNo"), waitSec);
            Assert.assertTrue(hasCoupon, "小程序券列表未发现券：" + coupon.get("couponNo"));
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (null != auditId) {
                CouponErrands.finishAudit(auditId);
            }

            if (null != coupon) {
                CouponErrands.offsaleCoupon(coupon.get("schemeId"));
            }
        }
    }

    @Test(
            testName = "停车券E2E",
            description = "新建满减券->投放新人领券->审核->小程序领用",
            priority = 0,
            groups = {"E2E", "Online"}
    )
    public void parking_coupon_new_user_E2E() {
        Map<String, String> coupon = null;
        String auditId = null;
        try {
            // 创建券
            coupon = CouponErrands.createCoupon(CouponType.ParkingCoupon);
            String schemeId = coupon.get("schemeId");
            String couponNo = coupon.get("couponNo");

            // 投放到券列表
            CouponErrands.launchCoupon(AdvertisementType.NewUser, schemeId);

            // 审核
            auditId = CouponErrands.auditCoupon(couponNo);

            // 验证小程序券列表可以找到刚刚创建的券
            boolean hasCoupon = CouponErrands.waitList(getBaseData("wxPlazaId").toString(), "newUserReward", coupon.get("couponNo"), waitSec);
            Assert.assertTrue(hasCoupon, "小程序券列表未发现券：" + coupon.get("couponNo"));
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (null != auditId) {
                CouponErrands.finishAudit(auditId);
            }
            if (null != coupon) {
                CouponErrands.offsaleCoupon(coupon.get("schemeId"));
            }
        }
    }

    @Test(
            testName = "团购券E2E",
            description = "新建团购券->投放券列表->审核->小程序领用",
            priority = 0,
            groups = {"E2E", "Online"}
    )
    public void group_coupon_list_E2E() {
        Map<String, String> coupon = null;
        String auditId = null;
        try {
            // 创建券
            coupon = CouponErrands.createCoupon(CouponType.GroupCoupon);
            String schemeId = coupon.get("schemeId");
            String couponNo = coupon.get("couponNo");

            // 投放到券列表
            CouponErrands.launchCoupon(AdvertisementType.CouponList, schemeId);

            // 审核
            auditId = CouponErrands.auditCoupon(couponNo);

            // 验证小程序券列表可以找到刚刚创建的券
            boolean hasCoupon = CouponErrands.waitList(getBaseData("wxPlazaId").toString(), "couponList", coupon.get("couponNo"), waitSec);
            Assert.assertTrue(hasCoupon, "小程序券列表未发现券：" + coupon.get("couponNo"));

//            // 登录
//            wxLogin(getBaseData("wxPhone").toString(), getBaseData("wxOpenId").toString(), getBaseData("wxPlazaId").toString());
//
//            // 领券
//            String orderNo = CouponErrands.orderCoupon(cLoginModel, "couponList", getBaseData("wxPlazaId").toString(), couponNo, "0.01");

            // 待付款，后续暂不做
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (null != auditId) {
                CouponErrands.finishAudit(auditId);
            }
            if (null != coupon) {
                CouponErrands.offsaleCoupon(coupon.get("schemeId"));
            }
        }
    }

    @Test(
            testName = "礼品券E2E",
            description = "新建礼品券->投放券列表->审核->小程序领用",
            priority = 0,
            groups = {"E2E", "Online"}
    )
    public void gift_coupon_list_E2E() {
        Map<String, String> coupon = null;
        String auditId = null;
        try {
            // 创建券
            coupon = CouponErrands.createCoupon(CouponType.GiftCoupon);
            String schemeId = coupon.get("schemeId");
            String couponNo = coupon.get("couponNo");

            // 投放到券列表
            CouponErrands.launchCoupon(AdvertisementType.CouponList, schemeId);

            // 审核
            auditId = CouponErrands.auditCoupon(couponNo);

            // 验证小程序券列表可以找到刚刚创建的券
            boolean hasCoupon = CouponErrands.waitList(getBaseData("wxPlazaId").toString(), "couponList", coupon.get("couponNo"), waitSec);
            Assert.assertTrue(hasCoupon, "小程序券列表未发现券：" + coupon.get("couponNo"));

            // 登录
            wxLogin(getBaseData("wxPhone").toString(), getBaseData("wxOpenId").toString(), getBaseData("wxPlazaId").toString());

            // 领券
            String orderNo = CouponErrands.orderCoupon(cLoginModel, "couponList", getBaseData("wxPlazaId").toString(), couponNo, "0");

            // 获取订单内CouponNo
            String productCouponNo = CouponErrands.getOrderCouponNo(cLoginModel, orderNo);

            // 核销
            CouponErrands.checkCoupon(coupon.get("storeId"), productCouponNo, cLoginModel.getMemberId());
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (null != auditId) {
                CouponErrands.finishAudit(auditId);
            }
            if (null != coupon) {
                CouponErrands.offsaleCoupon(coupon.get("schemeId"));
            }
        }
    }

    //    @Test
//    public void create1500() {
//        for(int i = 0; i< 1; i++){
//            Map<String, String> coupon = null;
//            String auditId = null;
//            try {
//                // 创建券
//                coupon = CouponErrands.createCoupon(CouponType.CashCoupon);
//                String schemeId = coupon.get("schemeId");
//                String couponNo = coupon.get("couponNo");
//
//                // 投放到券列表
//                CouponErrands.launchCoupon(AdvertisementType.CouponList, schemeId);
//
//                // 审核
//                auditId = CouponErrands.auditCoupon(couponNo);
//
////                // 验证小程序券列表可以找到刚刚创建的券
////                boolean hasCoupon = waitList("flashSale", coupon.get("couponNo"), waitSec);
////                Assert.assertTrue(hasCoupon, "小程序券列表未发现券：" + coupon.get("couponNo"));
//            } catch (Exception ex) {
//
//            }
//        }
//    }
//
    @Test
    public void cleanup() {
        qfLogin();

        for (Integer i = 1; i < 6; i++) {
            while (true) {
                BQueryCouponsService service = new BQueryCouponsService();
                Map<String, String> params = new HashMap<>();
                params.put("beginTime", "0");
                params.put("endTime", "0");
                params.put("couponName", "");
                params.put("couponNo", "");
                params.put("pageSize", "100");
                params.put("pageNum", "1");
                params.put("status", "2");
                params.put("couponType", i.toString()); //1: 满减，4：代金，3：团购，2：礼品， 5：停车
                params.put("ext", "true");
                params.put("timestr", String.valueOf(new Date().getTime()));
                params.put("source", "2");

                APIResponse resp = service
                        .buildUrl(params)
                        .setHeaders(buildQfHeader())
                        .request();

                List<String> ss = new ArrayList<>();
                List<Object> data = ObjectUtil.convertToList(resp.getJValue("data"));
                if (data.size() == 0) {
                    break;
                }
                for (Object obj : data) {
                    Map<String, Object> coupon = ObjectUtil.convertToMap(obj);
                    //Map<String, Object> scheme = ObjectUtil.convertToMap(coupon.get("scheme"));
                    ss.add(coupon.get("id").toString());

                    CouponErrands.offsaleCoupon(coupon.get("id").toString());
                }
            }
        }



    }
}
