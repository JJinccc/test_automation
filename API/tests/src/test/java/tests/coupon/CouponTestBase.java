package tests.coupon;

import com.ffan.qa.biz.services.common.CGetVerifyCodeService;
import com.ffan.qa.biz.services.common.CVerifyCodeService;
import com.ffan.qa.biz.services.member.BLoginService;
import com.ffan.qa.biz.services.member.CLoginService;
import com.ffan.qa.biz.services.member.CWxLoginForTestService;
import com.ffan.qa.common.Logger;
import com.ffan.qa.common.model.BLoginModel;
import com.ffan.qa.common.model.CLoginModel;
import com.ffan.qa.network.APIResponse;
import tests.TestBase;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class CouponTestBase extends TestBase {
    protected CLoginModel cLoginModel;

    public void wxLogin(String mobile, String openId, String plazaId) {
        // 发送验证码
        CVerifyCodeService verifyCodeService = new CVerifyCodeService(getUrlData(), mobile);
        verifyCodeService.request();

        // 获取验证码
        CGetVerifyCodeService getVerifyCodeService = new CGetVerifyCodeService(getUrlData(), mobile);
        getVerifyCodeService.request();
        Logger.log("获取到验证码" + getVerifyCodeService.getVerifyCode());

        // 获取token
        CWxLoginForTestService wxLoginForTestService = new CWxLoginForTestService(getUrlData());
        Map<String, String> wxParams = new HashMap<>();
        wxParams.put("appId", getBaseData("wxAppId").toString());
        wxParams.put("openId", openId);
        wxLoginForTestService
                .buildBody(wxParams)
                .request();
        String wxFfanToken = wxLoginForTestService.getFfanToken();

        // 手机号 & 验证码登录
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("verifyCode", getVerifyCodeService.getVerifyCode());
        params.put("plazaId", plazaId);
        params.put("source", "MINA");
        params.put("wxFfanToken", wxFfanToken);
        CLoginService loginService = new CLoginService(getUrlData());
        APIResponse resp = loginService
                .buildBody(params)
                .request();
        Logger.log(resp.getBody());
        cLoginModel = new CLoginModel(
                mobile,
                resp.getJValue("data.cookieStr").toString(),
                resp.getJValue("data.uid").toString(),
                resp.getJValue("data.puid").toString(),
                resp.getJValue("data.ploginToken").toString());
    }
}
