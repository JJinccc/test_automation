package com.ffan.qa.errands;

import com.ffan.qa.common.model.BLoginModel;
import com.ffan.qa.utils.EncoderUtil;
import com.ffan.qa.utils.TestConfigUtil;

import java.util.HashMap;
import java.util.Map;

public class ErrandsBase {
    protected static Map<String, String> getQianfanHeader() {
        BLoginModel loginModel = MemberErrands.getCurrent().getQianfanLoginModel();
        Map<String, Object> baseData = TestConfigUtil.getBaseData(); // 读取base数据

        // header params
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put("code", baseData.get("qianfanOrgCode").toString());
        headerParams.put("orgcode", baseData.get("qianfanOrgCode").toString());
        headerParams.put("orgname", EncoderUtil.urlEncode(baseData.get("qianfanOrgName").toString()));
        headerParams.put("orgTypeCode", baseData.get("qianfanOrgTypeCode").toString());
        headerParams.put("orgTypeName", EncoderUtil.urlEncode(baseData.get("qianfanOrgTypeName").toString()));
        headerParams.put("tenantId", loginModel.getTenantId());
        headerParams.put("token", loginModel.getToken());
        headerParams.put("userid", loginModel.getUserId());
        headerParams.put("username", baseData.get("qianfanAdmin").toString());
        headerParams.put("workingOrgCode", baseData.get("qianfanOrgCode").toString());

        return headerParams;
    }
}
