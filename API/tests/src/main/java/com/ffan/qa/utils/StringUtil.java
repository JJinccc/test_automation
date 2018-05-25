package com.ffan.qa.utils;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class StringUtil {
    public static boolean isNullOrEmpty(String input) {
        return null == input || input.equals("") || input.replaceAll("[\\s　]+", "").equals("");
    }

    //随机生成常见汉字
    public static String getRandomChar(int count) {
        StringBuilder sb = new StringBuilder("");
        int highCode;
        int lowCode;

        Random random = new Random();

        for (int i = 0; i < count; i++) {
            highCode = (176 + Math.abs(random.nextInt(39))); //B0 + 0~39(16~55) 一级汉字所占区
            lowCode = (161 + Math.abs(random.nextInt(93))); //A1 + 0~93 每区有94个汉字

            byte[] b = new byte[2];
            b[0] = (Integer.valueOf(highCode)).byteValue();
            b[1] = (Integer.valueOf(lowCode)).byteValue();

            try {
                sb.append(new String(b, "GBK"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    public static int stringToInt(String input) {
        double lInput = Double.parseDouble(input);
        return (int) lInput;
    }

    public static boolean contains(String[] inputs, String expectedStr) {
        for (String s : inputs) {
            if (s.equals(expectedStr)) {
                return true;
            }
        }
        return false;
    }

    public static String getDateString() {
        Calendar calendar = Calendar.getInstance();
        String year = Integer.toString(calendar.get(Calendar.YEAR));
        String month = Integer.toString(calendar.get(Calendar.MONTH) + 1);
        if (month.length() == 1) {
            month = "0" + month;
        }
        String day = Integer.toString(calendar.get(Calendar.DATE));
        if (day.length() == 1) {
            day = "0" + day;
        }

        String hour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
        if (hour.length() == 1) {
            hour = "0" + hour;
        }

        String minute = Integer.toString(calendar.get(Calendar.MINUTE));
        if (minute.length() == 1) {
            minute = "0" + minute;
        }

        String second = Integer.toString(calendar.get(Calendar.SECOND));
        if (second.length() == 1) {
            second = "0" + second;
        }

        String millsecond = Integer.toString(calendar.get(Calendar.MILLISECOND));

        return year + month + day + hour + minute + second + millsecond;
    }

    public static String parseToJsonString(Object input) {
        Map<String, Object> obj = (LinkedHashMap<String, Object>) input;
        String ret = "{";

        for (Map.Entry<String, Object> entry : obj.entrySet()) {
            ret += "\"" + entry.getKey() + "\":";
            if (null == entry.getValue()) {
                ret += "null,";
            } else {
                ret += "\"" + entry.getValue().toString() + "\",";
            }
        }
        ret = ret.substring(0, ret.length() - 1);
        ret += "}";
        return ret;
    }
}
