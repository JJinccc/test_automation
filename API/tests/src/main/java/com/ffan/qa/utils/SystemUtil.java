package com.ffan.qa.utils;

import com.ffan.qa.common.Logger;

import java.io.File;
import java.util.Properties;

public class SystemUtil {
    public static boolean isOSLinux() {
        Properties prop = System.getProperties();

        String os = prop.getProperty("os.name");
        Logger.info(os);
        if (os != null && os.toLowerCase().indexOf("windows") > -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取应用程序当前执行路径
     *
     * @return
     */
    public static String getExecutePath() {
        try {
            File dir = new File(".");
            return dir.getCanonicalPath();
        } catch (Exception ex) {
            Logger.info(ex.getMessage());
            return "";
        }
    }

    public static void sleep(Integer millSec) {
        try {
            Thread.sleep(millSec);
        } catch (Exception ex) {

        }
    }
}
