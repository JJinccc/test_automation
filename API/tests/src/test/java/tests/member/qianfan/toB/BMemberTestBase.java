package tests.member.qianfan.toB;

import com.ffan.qa.biz.services.member.BLoginService;
import com.ffan.qa.biz.services.member.QFLoginAPI;
import com.ffan.qa.common.model.BLoginModel;
import com.ffan.qa.network.APIResponse;
import com.ffan.qa.utils.JsonUtil;
import tests.TestBase;

public class BMemberTestBase extends TestBase {
    public BLoginModel qfLoginModel;

    public String login() {
        QFLoginAPI lgSvc = new QFLoginAPI(getUrlData());
        String userName = getBaseData("qianfanAdmin").toString();
        String userPwd = getBaseData("qianfanPassword").toString();
        APIResponse resp = lgSvc.request(userName, userPwd);
//        resp.assertJValue("status", "200");
        String body = resp.getBody();

        String token = JsonUtil.getValue(body, "data.token").toString();
        return token;
    }

    public void qfLogin(String user, String pwd) {
        BLoginService loginService = new BLoginService(getUrlData());
        APIResponse resp = loginService.request(user, pwd);

        qfLoginModel = new BLoginModel(resp.getJValue("data.userId").toString(),
                resp.getJValue("data.tenantId").toString(),
                resp.getJValue("data.token").toString());
    }
}
