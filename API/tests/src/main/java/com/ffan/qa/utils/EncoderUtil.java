package com.ffan.qa.utils;

import com.ffan.qa.common.Logger;

import java.net.URLEncoder;

public class EncoderUtil {
    public static String urlEncode(String string) {
        try {
            return URLEncoder.encode(string, "utf-8");
        }
        catch (Exception ex) {
            Logger.error(ex.getMessage());
            return "";
        }
    }
}
