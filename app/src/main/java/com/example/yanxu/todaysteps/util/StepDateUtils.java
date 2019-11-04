package com.example.yanxu.todaysteps.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName: StepDateUtils
 * @Description: 时间工具类（时间格式转换工具类）
 * @Author: yanxu5
 * @Date: 2019/8/19
 */
class StepDateUtils {

    private static ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT = new ThreadLocal<>();

    private static SimpleDateFormat getDateFormat() {
        SimpleDateFormat df = SIMPLE_DATE_FORMAT.get();
        if (df == null) {
            df = new SimpleDateFormat();
            SIMPLE_DATE_FORMAT.set(df);
        }
        return df;
    }

    /**
     * 返回一定格式的当前时间
     *
     * @param pattern "yyyy-MM-dd HH:mm:ss E"
     * @return String
     */
    public static String getCurrentDate(String pattern) {
        getDateFormat().applyPattern(pattern);
        Date date = new Date(System.currentTimeMillis());
        return getDateFormat().format(date);
    }

    private static long getDateMillis(String dateString, String pattern) {
        long millionSeconds = 0;
        getDateFormat().applyPattern(pattern);
        try {
            millionSeconds = getDateFormat().parse(dateString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }// 毫秒
        return millionSeconds;
    }

    /**
     * 格式化输入的millis
     *
     * @param millis  millis
     * @param pattern yyyy-MM-dd HH:mm:ss E
     * @return String
     */
    private static String dateFormat(long millis, String pattern) {
        getDateFormat().applyPattern(pattern);
        Date date = new Date(millis);
        return getDateFormat().format(date);
    }

    /**
     * 将dateString原来old格式转换成new格式
     *
     * @param dateString dateString
     * @param oldPattern yyyy-MM-dd HH:mm:ss E
     * @param newPattern newPattern
     * @return oldPattern和dateString形式不一样直接返回dateString
     */
    private static String dateFormat(String dateString, String oldPattern, String newPattern) {
        long millis = getDateMillis(dateString, oldPattern);
        if (0 == millis) {
            return dateString;
        }
        return dateFormat(millis, newPattern);
    }

}
