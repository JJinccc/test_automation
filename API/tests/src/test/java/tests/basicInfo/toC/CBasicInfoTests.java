package tests.basicInfo.toC;

import com.ffan.qa.biz.services.common.*;
import com.ffan.qa.biz.services.coupon.CPlaCouponListService;
import com.ffan.qa.common.Logger;
import com.ffan.qa.network.APIResponse;
import com.ffan.qa.utils.EncryptUtil;
import com.ffan.qa.utils.SmsSendUtil;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.TestBase;

import java.util.*;

public class CBasicInfoTests extends TestBase {
    @Test(
            testName = "地理位置信息接口测试",
            description = "测试地理位置信息接口",
            priority = 1,
            groups = {"Basics", "wxBasicInfo", "Online"}
    )
    public void location_test() {
        // 参数构造 https://api.sit.ffan.com/wechatxmt/v1/location?lng=121.54409&lat=31.22114
        Map<String, String> params = new HashMap<>();
        params.put("lng", getTestData("lng").toString());
        params.put("lat", getTestData("lat").toString());

        // 发起请求并进行验证
        CLocationService locationService = new CLocationService(getUrlData());
        locationService
                .buildUrl(params) // 将参数合并到URL中
                .request() // 发起请求
                .printResponse()
                .assertJValue("status", "200") // 验证状态码为200，返回正常状态
                .assertJNotEmpty("data.cityId")
                .assertJNotEmpty("data.cityName")
                .assertJNotEmpty("data.distance")
                .assertJNotEmpty("data.latitude")
                .assertJNotEmpty("data.longitude")
                .assertJNotEmpty("data.plazaId")
                .assertJNotEmpty("data.plazaName");
    }

    @Test(
            testName = "动态配置信息接口测试",
            description = "测试动态配置信息接口",
            priority = 1,
            groups = {"Basics", "wxBasicInfo", "Online"},
            dataProvider = "data"
    )
    public void configs_test(Map<String, Object> data) {
        String plazaId = data.get("plazaId").toString();
        String plazaName = data.get("plazaName").toString();
        String city = data.get("city").toString();

        // 参数构造 https://api.sit.ffan.com/wechatxmt/v1/configs/?configId=2&plazaId=1000343
        Map<String, String> params = new HashMap<>();
        params.put("configId", "2");
        params.put("plazaId", plazaId);

        // 发起请求并进行验证
        CWeXmtConfigsService weXmtConfigsService = new CWeXmtConfigsService(getUrlData());
        weXmtConfigsService
                .buildUrl(params) // 将参数合并到URL中
                .request() // 发起请求
                .printResponse()
                .assertJValue("status", "200", String.format("%s-%s, 动态配置信息接口返回非200状态码，接口存在异常", city, plazaName)) // 验证状态码为200，返回正常状态
                .assertJNotEmpty("data.enable", String.format("%s-%s, 动态配置信息接口返回data.enable没有值，接口存在异常", city, plazaName))
                .assertJNotEmpty("data.endTime", String.format("%s-%s, 动态配置信息接口返回data.endTime没有值，接口存在异常", city, plazaName))
                .assertJNotEmpty("data.startTime", String.format("%s-%s, 动态配置信息接口返回data.startTime没有值，接口存在异常", city, plazaName));
    }

    @Test(
            testName = "聚合页banner列表接口测试",
            description = "测试聚合页banner列表接口",
            priority = 1,
            groups = {"Basics", "wxBasicInfo", "Online"}
    )
    public void aggre_banners_test() {
        String city = getBaseData("wxCityId").toString();

        Map<String, String> params = new HashMap<>();
        params.put("cityId", city);

        // 发起请求并进行验证
        CWeAggregationBannersService service = new CWeAggregationBannersService(getUrlData());
        service
                .formatUrl(params) // 将参数合并到URL中
                .request() // 发起请求
                .printResponse()
                .assertJValue("status", "200", "聚合页banner列表接口返回非200状态码，接口存在异常"); // 验证状态码为200，返回正常状态
    }

    @Test(
            testName = "banner列表接口测试",
            description = "测试banner列表接口",
            priority = 1,
            groups = {"Basics", "wxBasicInfo", "Online"},
            dataProvider = "data"
    )
    public void banners_test(Map<String, Object> dp) {
        String plazaId = dp.get("plazaId").toString();
        String plazaName = dp.get("plazaName").toString();
        String city = dp.get("city").toString();

        // 参数构造 https://api.sit.ffan.com/wechatxmt/v1/xcx/banners/1000343
        Map<String, String> params = new HashMap<>();
        params.put("plazaId", plazaId);

        // 发起请求并进行验证
        CWeXmtBannersService bannersService = new CWeXmtBannersService(getUrlData());
        APIResponse resp = bannersService
                .formatUrl(params) // 将参数合并到URL中
                .request() // 发起请求
                .printResponse()
                .assertJValue("status", "200", String.format("%s-%s, banner列表接口返回非200状态码，接口存在异常", city, plazaName)); // 验证状态码为200，返回正常状态

        ArrayList<Object> data = resp.getValue("data");
        for (Object obj: data) {
            Map<String, Object> banner = (LinkedHashMap<String, Object>)obj;
            // 验证每条数据对应的广场都是我们请求的
            Assert.assertEquals(banner.get("plaza").toString(), plazaId,
                    String.format("%s-%s，banner所属的广场与期望不符，bannerId: %s，plaza: %s，期望plaza: %s", city, plazaName, banner.get("id").toString(), banner.get("plaza").toString(), plazaId));
        }
    }

    @Test(
            testName = "广场列表接口测试",
            description = "测试广场列表接口",
            priority = 1,
            groups = {"Basics", "wxBasicInfo", "Online"}
    )
    public void plazas_test() {
        // 参数构造 https://api.sit.ffan.com/wechatxmt/v1/plazas?cityId=310100&lng=121.54409&lat=31.22114
        Map<String, String> params = new HashMap<>();
        params.put("cityId", getTestData("cityId").toString());
        params.put("lng", getTestData("lng").toString());
        params.put("lat", getTestData("lat").toString());

        // 发起请求并进行验证
        CWeXmtPlazasService weXmtPlazasService = new CWeXmtPlazasService(getUrlData());
        APIResponse resp = weXmtPlazasService
                .buildUrl(params) // 将参数合并到URL中
                .request() // 发起请求
                .printResponse()
                .assertJValue("status", "200") // 验证状态码为200，返回正常状态
                .assertJSizeBigger("data", 0, "data内广场数量为0");

        ArrayList<Object> data = resp.getValue("data");
        for (Object obj: data) {
            Map<String, Object> plaza = (LinkedHashMap<String, Object>)obj;
            // 验证每条数据对应的广场信息都不为空
            Assert.assertNotNull(plaza.get("plazaId").toString(),
                    String.format("plazaId为空，请求地址：%s", weXmtPlazasService.getUrl()));
            Assert.assertNotNull(plaza.get("plazaName").toString(),
                    String.format("plazaName为空，请求地址：%s", weXmtPlazasService.getUrl()));
        }
    }

    @Test(
            testName = "城市列表接口测试",
            description = "测试城市列表接口",
            priority = 1,
            groups = {"Basics", "wxBasicInfo", "Online"}
    )
    public void cities_test() {
        // 发起请求并进行验证 https://api.sit.ffan.com/wechatxmt/v1/cities
        CWeXmtCitiesService citiesService = new CWeXmtCitiesService(getUrlData());
        APIResponse resp = citiesService
                .request() // 发起请求
                .printResponse()
                .assertJValue("status", "200") // 验证状态码为200，返回正常状态
                .assertJSizeBigger("data", 0, "data内城市数量为0");

        ArrayList<Object> data = resp.getValue("data");
        for (Object obj: data) {
            Map<String, Object> plaza = (LinkedHashMap<String, Object>)obj;
            // 验证每条数据对应的广场信息都不为空
            Assert.assertNotNull(plaza.get("cityId").toString(),
                    String.format("cityId为空，请求地址：%s", citiesService.getUrl()));
            Assert.assertNotNull(plaza.get("cityName").toString(),
                    String.format("cityName为空，请求地址：%s", citiesService.getUrl()));
        }
    }

    @Test(
            testName = "适用门店接口测试",
            description = "测试适用门店接口",
            priority = 1,
            groups = {"Basics", "wxBasicInfo"}
    )
    public void apply_stores_test() {
        // 参数构造 https://api.sit.ffan.com/wechatxmt/v1/Product/applyStores?productNo=20171206003747&cityId=310100
        Map<String, String> params = new HashMap<>();
        params.put("cityId", getTestData("cityId").toString());
        params.put("productNo", getTestData("productNo").toString());

        // 发起请求并进行验证
        CWeXmtApplyStoresService applyStoresService = new CWeXmtApplyStoresService(getUrlData());
        APIResponse resp = applyStoresService
                .buildUrl(params)
                .request() // 发起请求
                .printResponse()
                .assertJValue("status", "200") // 验证状态码为200，返回正常状态
                .assertJSizeBigger("data", 0, "data内门店数量为0");

        ArrayList<Object> data = resp.getValue("data");
        for (Object obj: data) {
            Map<String, Object> plaza = (LinkedHashMap<String, Object>)obj;
            // 验证每条数据对应的门店名称都不为空
            Assert.assertNotNull(plaza.get("storeName").toString(),
                    String.format("storeName为空，请求地址：%s", applyStoresService.getUrl()));
        }
    }
}
