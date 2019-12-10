package com.example.yanxu.todaysteps.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * @ClassName: StepZeroAlarmReceiver
 * @Description: 零点的定时广播
 * @Author: yanxu5
 * @Date: 2019/8/23
 */
public class StepZeroAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("alarm_0_separate")) {
            Intent todayStepIntent = new Intent(context, StepService.class);
            todayStepIntent.putExtra(StepService.INTENT_ALARM_0_SEPARATE, true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(todayStepIntent);
            } else {
                context.startService(todayStepIntent);
            }
        }
    }

}
