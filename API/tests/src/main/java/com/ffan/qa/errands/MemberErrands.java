package com.ffan.qa.errands;

import com.ffan.qa.biz.services.member.BLoginService;
import com.ffan.qa.common.model.BLoginModel;
import com.ffan.qa.network.APIResponse;
import com.ffan.qa.utils.TestConfigUtil;

import java.util.Map;

public class MemberErrands {
    private static MemberErrands current = null;
    private BLoginModel qianfanLoginModel = null;
    private Map<String, Object> urlData;
    private String user;
    private String password;

    public MemberErrands(String user, String password) {
        this.urlData = TestConfigUtil.getUrlData();
        this.user = user;
        this.password = password;
    }

    /**
     * 千帆登录
     * @return
     */
    private BLoginModel qianfanLogin() {
        BLoginService loginService = new BLoginService(urlData);
        APIResponse resp = loginService.request(user, password);

        BLoginModel loginModel = new BLoginModel(resp.getJValue("data.userId").toString(),
                resp.getJValue("data.tenantId").toString(),
                resp.getJValue("data.token").toString());

        return loginModel;
    }

    /**
     * 获取千帆登录信息
     * @return
     */
    public BLoginModel getQianfanLoginModel() {
        return getQianfanLoginModel(false);
    }

    /**
     * 获取千帆登录信息
     * @param forceLogin
     * @return
     */
    public BLoginModel getQianfanLoginModel(boolean forceLogin) {
        if (null == qianfanLoginModel || forceLogin) {
            qianfanLoginModel = qianfanLogin();
        }
        return qianfanLoginModel;
    }

    /**
     * 获取当前公用的MemberErrands
     * @return
     */
    public static MemberErrands getCurrent() {
        if (null == current) {
            Map<String, Object> baseData = TestConfigUtil.getBaseData();
            current = new MemberErrands(baseData.get("qianfanAdmin").toString(), baseData.get("qianfanPassword").toString());
        }
        return current;
    }

    /**
     * 设置当前公用的MemberErrands
     * @param user
     * @param password
     */
    public static void setCurrent(String user, String password) {
        current = new MemberErrands(user, password);
    }
}
