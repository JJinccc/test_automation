package com.ffan.qa.common;

import org.testng.Reporter;

import java.util.Calendar;

public class Logger {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Logger.class);
    public static void log(String message) {
        Reporter.log(Calendar.getInstance().getTime().toString() + ":" + message, true);
    }

    public static void info(Object message) {
        logger.info(message);
    }

    public static void error(Object message) {
        logger.error(message);
    }
}
