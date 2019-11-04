package com.example.yanxu.todaysteps.util;

import android.content.Context;

/**
 * @ClassName: StepUtil
 * @Description: 计步相关的工具类
 * @Author: yanxu5
 * @Date: 2019/9/10
 */
public class StepUtil {

    /**
     * 是否支持计步
     *
     * @param context 上下文
     * @return 是否支持计步
     */
    public static boolean isSupportStep(Context context) {
        return StepSPHelper.getSupportStep(context);
    }

    /**
     * 今日步数（每日有效步数30000步-->服务端处理）
     *
     * @param context 上下文
     * @return 今日步数
     */
    public static int getTodayStep(Context context) {
        return (int) StepSPHelper.getCurrentStep(context);
    }

}
