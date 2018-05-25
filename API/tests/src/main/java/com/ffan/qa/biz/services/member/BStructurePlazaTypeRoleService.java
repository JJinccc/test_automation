package com.ffan.qa.biz.services.member;

import com.ffan.qa.biz.services.ServiceBase;
import com.ffan.qa.network.APIResponse;

import java.util.Map;

public class BStructurePlazaTypeRoleService extends ServiceBase{
    public BStructurePlazaTypeRoleService(Map<String, Object> urlData) {
        super(urlData, "qianfanBaseUrl", "qianfanPlazaTypeRole");
    }

    @Override
    public APIResponse request() {
        return httpGet();
    }
}
