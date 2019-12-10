package com.example.yanxu.todaysteps.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * @ClassName: StepBootCompleteReceiver
 * @Description: 开机完成广播
 * @Author: yanxu5
 * @Date: 2019/8/19
 */
public class StepBootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            try {
                Intent todayStepIntent = new Intent(context, StepService.class);
                todayStepIntent.putExtra(StepService.INTENT_BOOT_COMPLETED, true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(todayStepIntent);
                } else {
                    context.startService(todayStepIntent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
