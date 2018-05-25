package tests.member.qianfan.toB;

import com.ffan.qa.biz.services.member.query.BMemberQueryListService;
import com.ffan.qa.utils.StringUtil;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2018/1/29.
 */
public class BMemberPost extends BMemberTestBase{
    @Test(
            testName = "新增会员",
            description = "新增会员",
            priority = 0,
            groups = {"Post", "BMember"}
    )
    public void createMember() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("tenantId", getTestData("tenantId").toString());
        headers.put("orgcode", getTestData("orgcode").toString());
        headers.put("orgTypeCode", getTestData("orgTypeCode").toString());
        headers.put("token", login());

        String dateStr = StringUtil.getDateString();
        String phone = "18" + dateStr.substring(8);
        String qq = "3" + phone ;
        String email = phone + "@qq.com";
        String weChat = "Wx" + phone;

        // req body
        String body = "{" +
                "\"plazaId\":\"" + getTestData("orgcode").toString() + "\"," +
                "\"storeId\":\"\"," +
                "\"forms\":{\"expanding_channel\":\"1\"," +
                "\"birthday\":\"2018-01-01\"," +
                "\"spouse_birthday\":\"2018-01-01\"," +
                "\"married_date\":\"2018-01-02\"," +
                "\"checkin_date\":\"2018-01-01\"," +
                "\"memorial_day\":\"2018-01-01\"," +
                "\"certificate_code\":\"\"," +
                "\"certificate_type\":\"IDCARD\"," +
                "\"mobile_no\":\"" + phone +"\"," +
                "\"sex\":\"MALE\"," +
                "\"member_name\":\""+getTestData("memberName").toString() +"\"," +
                "\"rating_id\":\"1\"," +
                "\"wechat_nickname\":\"昵称\"," +
                "\"wechat_id\":\""+ weChat +"\"," +
                "\"qq\":\""+ qq +"\"," +
                "\"email\":\""+ email +"\"," +
                "\"tel_no\":\"021-2345611\"," +
                "\"is_married\":\"Y\"," +
                "\"child_birthday\":\"\"," +
                "\"child_sex\":\"\"," +
                "\"child_name\":\"\"," +
                "\"spouse_name\":\"配偶姓名\"," +
                "\"edu_back\":\"2\"," +
                "\"description\":\"说明\"," +
                "\"nationality\":\"中国\"," +
                "\"trip_mode\":\"1\"," +
                "\"hobby\":\"喜好\"," +
                "\"salary\":\"5001\"," +
                "\"reg_org\":\"单位\"," +
                "\"card_license\":\"\"," +
                "\"nation\":\"汉族\"," +
                "\"skilled\":\"特长\"," +
                "\"occupation\":\"职业\"," +
                "\"memorial_day_desc\":\"纪念日说明\"," +
                "\"name_pinyin\":\"pinyin\"," +
                "\"nickname\":\"昵称\"}," +
                "\"type\":1," +
                "\"refereeUserId\":null," +
                "\"salesUserId\":null," +
                "\"tags\":[{\"name\":\"男\"," +
                "\"value\":123}]}";

        // 发起请求并进行验证
        BMemberQueryListService bMemberQueryListService = new BMemberQueryListService(getUrlData());
        bMemberQueryListService.setHeaders(headers);
        bMemberQueryListService
                .request() // 发起请求
                .assertJValue("status", "0000")   // 验证状态码为200，返回正常状态
                .assertJValue("_metadata.totalCount", "1")
                .assertJValue("data.mobileNo", getTestData("mobileNo").toString());
    }
}
