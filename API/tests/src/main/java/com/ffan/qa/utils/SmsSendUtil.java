package com.ffan.qa.utils;

import com.ffan.qa.common.Logger;
import com.ffan.qa.network.APIRequest;
import com.ffan.qa.network.APIResponse;

import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

public class SmsSendUtil {
    public static void sendSMS(List<String> phoneList, String message) {
        for (String phoneNum :
                phoneList) {
            Long time = new Date().getTime() + 3000000;
            String requestBody = "templateId=171&deviceList=['" + phoneNum + "']&deviceType=0&argsList=[[\"" + message + "-" + new Date().toString() + "\"]]&validTime=" + time.toString() + "&contentType=0";

            APIResponse resp = APIRequest
                    .POST("http://api.ffan.com/msgcenter/v1/smsOutboxes")
                    .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                    .body(requestBody)
                    .invoke();

            String ret = resp.getBody();
            Logger.log(ret);
        }

    }
}
