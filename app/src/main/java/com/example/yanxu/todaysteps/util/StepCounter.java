package com.example.yanxu.todaysteps.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.SystemClock;

/**
 * @ClassName: StepCounter
 * @Description: Sensor.TYPE_STEP_COUNTER(Android 4.4以上) 通过计步传感器计算当天步数，不需要后台Service
 * @Author: yanxu5
 * @Date: 2019/8/19
 */

public class StepCounter implements SensorEventListener {
    private int sOffsetStep;
    private int sCurrStep;
    private String mTodayDate;
    private boolean mIsCleanStep;
    private boolean mIsShutdown;
    private boolean mIsCounterStepReset = true;//用来标识对象第一次创建
    private Context mContext;
    private boolean mIsSeparate;
    private boolean mIsBoot;

    public StepCounter(Context context, boolean separate, boolean boot) {
        mContext = context;
        mIsSeparate = separate;
        mIsBoot = boot;
        sCurrStep = (int) StepSPHelper.getCurrentStep(mContext);
        mIsCleanStep = StepSPHelper.getCleanStep(mContext);
        mTodayDate = StepSPHelper.getStepToday(mContext);
        sOffsetStep = (int) StepSPHelper.getStepOffset(mContext);
        mIsShutdown = StepSPHelper.getShutdown(mContext);
        boolean isShutdown = shutdownBySystemRunningTime();//开机启动监听到，一定是关机开机了
        if (mIsBoot || isShutdown || mIsShutdown) {
            mIsShutdown = true;
            StepSPHelper.setShutdown(mContext, true);
        }
        initBroadcastReceiver();
        dateChangeCleanStep();
    }

    private void initBroadcastReceiver() {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                if (Intent.ACTION_TIME_TICK.equals(intent.getAction())
                        || Intent.ACTION_TIME_CHANGED.equals(intent.getAction())
                        || Intent.ACTION_DATE_CHANGED.equals(intent.getAction())) {
                    dateChangeCleanStep();//service存活做0点分隔
                }
            }
        };
        mContext.registerReceiver(mBatInfoReceiver, filter);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            int counterStep = (int) event.values[0];
            if (mIsCleanStep) {
                cleanStep(counterStep);
            } else if (mIsShutdown || shutdownByCounterStep(counterStep)) {
                shutdown(counterStep);//处理关机启动
            }
            sCurrStep = counterStep - sOffsetStep;
            if (sCurrStep < 0) {
                cleanStep(counterStep);//容错处理，无论任何原因步数不能小于0，如果小于0，直接清零
            }
            StepSPHelper.setCurrentStep(mContext, sCurrStep);
            StepSPHelper.setElapsedRealTime(mContext, SystemClock.elapsedRealtime());
            StepSPHelper.setLastSensorStep(mContext, counterStep);
            dateChangeCleanStep();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void cleanStep(int counterStep) {
        sCurrStep = 0;//清除步数，步数归零，优先级最高
        sOffsetStep = counterStep;
        StepSPHelper.setStepOffset(mContext, sOffsetStep);
        mIsCleanStep = false;
        StepSPHelper.setCleanStep(mContext, false);
    }

    private void shutdown(int counterStep) {
        int tmpCurrStep = (int) StepSPHelper.getCurrentStep(mContext);
        //重新设置offset
        sOffsetStep = counterStep - tmpCurrStep;
        StepSPHelper.setStepOffset(mContext, sOffsetStep);
        mIsShutdown = false;
        StepSPHelper.setShutdown(mContext, false);
    }

    private boolean shutdownByCounterStep(int counterStep) {
        if (mIsCounterStepReset) {
            //只判断一次
            mIsCounterStepReset = false;
            //当前传感器步数小于上次传感器步数
            if (counterStep < StepSPHelper.getLastSensorStep(mContext)) {
                //当前传感器步数小于上次传感器步数肯定是重新启动了，只是用来增加精度不是绝对的
                return true;
            }
        }
        return false;
    }

    private boolean shutdownBySystemRunningTime() {
        //本地记录的时间，判断进行了关机操作
        if (StepSPHelper.getElapsedRealTime(mContext) > SystemClock.elapsedRealtime()) {
            //上次运行的时间大于当前运行时间判断为重启，只是增加精度，极端情况下连续重启，会判断不出来
            return true;
        }
        return false;
    }

    private synchronized void dateChangeCleanStep() {
        //时间改变了清零，或者0点分隔回调
        if (!getTodayDate().equals(mTodayDate) || mIsSeparate) {
            mIsCleanStep = true;
            StepSPHelper.setCleanStep(mContext, true);
            mTodayDate = getTodayDate();
            StepSPHelper.setStepToday(mContext, mTodayDate);
            mIsShutdown = false;
            StepSPHelper.setShutdown(mContext, false);
            mIsBoot = false;
            mIsSeparate = false;
            sCurrStep = 0;
            StepSPHelper.setCurrentStep(mContext, sCurrStep);
        }
    }

    private String getTodayDate() {
        return StepDateUtils.getCurrentDate("yyyy-MM-dd");
    }

    /**
     * 是否跨越零点、是否开机
     *
     * @param separate
     * @param boot
     */
    public void setZeroAndBoot(boolean separate, boolean boot) {
        mIsSeparate = separate;
        mIsBoot = boot;
    }

}
