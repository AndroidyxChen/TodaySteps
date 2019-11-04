package com.example.yanxu.todaysteps.util;

import android.content.Context;

/**
 * @ClassName: StepSPHelper
 * @Description: SharedPreferences工具类
 * @Author: yanxu5
 * @Date: 2019/8/19
 */
public class StepSPHelper {

    // 上一次计步器的步数
    private static final String LAST_SENSOR_TIME = "last_sensor_time";
    // 步数补偿数值，每次传感器返回的步数-offset=当前步数
    private static final String STEP_OFFSET = "step_offset";
    // 当天，用来判断是否跨天
    private static final String STEP_TODAY = "step_today";
    // 清除步数
    private static final String CLEAN_STEP = "clean_step";
    // 当前步数
    private static final String CURR_STEP = "curr_step";
    // 手机关机监听
    private static final String SHUTDOWN = "shutdown";
    // 系统运行时间
    private static final String ELAPSED_REAL_TIME = "elapsed_real_time";
    // 是否支持计步功能
    private static final String IS_SUPPORT_STEP = "is_support_step";

    /**
     * 保存上一次计步器的步数
     *
     * @param context        上下文
     * @param lastSensorStep 上一次步数
     */
    protected static void setLastSensorStep(Context context, float lastSensorStep) {
        StepSharedPreferencesUtil.setParam(context, LAST_SENSOR_TIME, lastSensorStep);
    }

    /**
     * 读取上一次计步器的步数
     *
     * @param context 上下文
     * @return 上一次计步器的步数
     */
    protected static float getLastSensorStep(Context context) {
        return (float) StepSharedPreferencesUtil.getParam(context, LAST_SENSOR_TIME, 0.0f);
    }

    /**
     * 保存步数补偿数值
     *
     * @param context    上下文
     * @param stepOffset 补偿数值
     */
    protected static void setStepOffset(Context context, float stepOffset) {
        StepSharedPreferencesUtil.setParam(context, STEP_OFFSET, stepOffset);
    }

    /**
     * 读取步数补偿数值
     *
     * @param context 上下文
     * @return 补偿数值
     */
    protected static float getStepOffset(Context context) {
        return (float) StepSharedPreferencesUtil.getParam(context, STEP_OFFSET, 0.0f);
    }

    /**
     * 保存当天日期
     *
     * @param context   上下文
     * @param stepToday 当天日期
     */
    protected static void setStepToday(Context context, String stepToday) {
        StepSharedPreferencesUtil.setParam(context, STEP_TODAY, stepToday);
    }

    /**
     * 读取当天日期
     *
     * @param context 上下文
     * @return 当天日期
     */
    protected static String getStepToday(Context context) {
        return (String) StepSharedPreferencesUtil.getParam(context, STEP_TODAY, "");
    }

    /**
     * 保存是否清楚步数 true清除步数从0开始，false否
     *
     * @param context   context
     * @param cleanStep cleanStep
     */
    protected static void setCleanStep(Context context, boolean cleanStep) {
        StepSharedPreferencesUtil.setParam(context, CLEAN_STEP, cleanStep);
    }

    /**
     * 保存是否清楚步数 true清除步数，false否
     *
     * @param context context
     * @return boolean
     */
    protected static boolean getCleanStep(Context context) {
        return (boolean) StepSharedPreferencesUtil.getParam(context, CLEAN_STEP, true);
    }

    /**
     * 保存当前步数
     *
     * @param context  上下文
     * @param currStep 当前步数
     */
    protected static void setCurrentStep(Context context, float currStep) {
        StepSharedPreferencesUtil.setParam(context, CURR_STEP, currStep);
    }

    /**
     * 读取当前步数
     *
     * @param context 上下文
     * @return 当前步数
     */
    protected static float getCurrentStep(Context context) {
        return (float) StepSharedPreferencesUtil.getParam(context, CURR_STEP, 0.0f);
    }

    /**
     * 保存是否关机
     *
     * @param context  上下文
     * @param shutdown 是否关机
     */
    protected static void setShutdown(Context context, boolean shutdown) {
        StepSharedPreferencesUtil.setParam(context, SHUTDOWN, shutdown);
    }

    /**
     * 读取是否关机
     *
     * @param context 上下文
     * @return 是否关机
     */
    protected static boolean getShutdown(Context context) {
        return (boolean) StepSharedPreferencesUtil.getParam(context, SHUTDOWN, false);
    }

    /**
     * 保存系统运行时间
     *
     * @param context         上下文
     * @param elapsedRealTime 系统运行时间
     */
    protected static void setElapsedRealTime(Context context, long elapsedRealTime) {
        StepSharedPreferencesUtil.setParam(context, ELAPSED_REAL_TIME, elapsedRealTime);
    }

    /**
     * 读取系统运行时间
     *
     * @param context 上下文
     * @return 系统运行时间
     */
    protected static long getElapsedRealTime(Context context) {
        return (long) StepSharedPreferencesUtil.getParam(context, ELAPSED_REAL_TIME, 0L);
    }

    /**
     * 保存是否支持计步
     *
     * @param context       上下文
     * @param isSupportStep 是否支持计步
     */
    protected static void setSupportStep(Context context, boolean isSupportStep) {
        StepSharedPreferencesUtil.setParam(context, IS_SUPPORT_STEP, isSupportStep);
    }

    /**
     * 读取是否支持计步
     *
     * @param context 上下文
     * @return 是否支持计步
     */
    protected static boolean getSupportStep(Context context) {
        return (boolean) StepSharedPreferencesUtil.getParam(context, IS_SUPPORT_STEP, false);
    }

}
