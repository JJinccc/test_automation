package tests.member.qianfan.toB;

import com.ffan.qa.biz.services.member.BLoginService;
import com.ffan.qa.network.APIResponse;
import com.ffan.qa.utils.EncryptUtil;
import org.testng.annotations.Test;

public class BLoginTest extends BMemberTestBase {
    @Test(
            testName = "我的券-未使用",
            description = "我未使用的券列表",
            priority = 1,
            groups = {"qianfanMember", "Online"}
    )
    public void qianfan_login() {
        BLoginService loginService = new BLoginService(getUrlData());
        APIResponse resp = loginService
                .request(getTestData("user").toString(), EncryptUtil.getMD5(getTestData("pwd").toString()))
                .assertJValue("status", "0000", "千帆后台登录异常，接口返回非0000状态")
                .assertJNotEmpty("data.tenantId", "千帆登录接口异常，data.tenantId返回空数据")
                .assertJNotEmpty("data.userId", "千帆登录接口异常，data.userId返回空数据")
                .assertJNotEmpty("data.token", "千帆登录接口异常，data.token返回空数据");
    }
}
