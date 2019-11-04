package com.example.yanxu.todaysteps.util;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * @ClassName: StepService
 * @Description: 当日步数的Service
 * @Author: yanxu5
 * @Date: 2019/8/19
 */
public class StepService extends Service {

    private static final int SAMPLING_PERIOD_US = SensorManager.SENSOR_DELAY_FASTEST; //传感器刷新频率
    public static final String INTENT_ALARM_0_SEPARATE = "intent_alarm_0_separate";
    public static final String INTENT_BOOT_COMPLETED = "intent_boot_completed";
    private SensorManager mSensorManager;
    private StepCounter mStepCounter; //Sensor.TYPE_STEP_COUNTER 计步传感器计算当天步数，不需要后台Service
    private boolean mIsSeparate = false;
    private boolean mIsBoot = false;
    private int alarmCount;

    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        initAlarm();//开启零点定时
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(1, new Notification());
        }
        if (null != intent) {
            mIsSeparate = intent.getBooleanExtra(INTENT_ALARM_0_SEPARATE, false);
            mIsBoot = intent.getBooleanExtra(INTENT_BOOT_COMPLETED, false);
        }
        startStepDetector();//注册传感器
        return START_STICKY;
    }

    private void startStepDetector() {
        //android4.4以后如果有StepDetector可以使用计步传感器
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && getStepCounter()) {
            addStepCounterListener();
        } else {
            StepSPHelper.setSupportStep(this, false);
        }
    }

    /**
     * 是否带有计步传感器
     *
     * @return 返回结果
     */
    private boolean getStepCounter() {
        Sensor countSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        boolean isHaveStepCounter = getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER);
        return null != countSensor && isHaveStepCounter;
    }

    private void addStepCounterListener() {
        StepSPHelper.setSupportStep(this, true);
        if (null != mStepCounter) {
            mStepCounter.setZeroAndBoot(mIsSeparate, mIsBoot);
            return;
        }
        Sensor countSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mStepCounter = new StepCounter(getApplicationContext(), mIsSeparate, mIsBoot);
        mSensorManager.registerListener(mStepCounter, countSensor, SAMPLING_PERIOD_US);//注册监听
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 定义一个零点闹钟
     */
    public void initAlarm() {
        Intent intent = new Intent(this, StepZeroAlarmReceiver.class);
        intent.setAction("alarm_0_separate");
        PendingIntent pi = PendingIntent.getBroadcast(this, alarmCount++, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstTime = SystemClock.elapsedRealtime();//获取系统当前时间
        long systemTime = System.currentTimeMillis();//java.lang.System.currentTimeMillis()，它返回从UTC1970年1月1午夜开始经过的毫秒数。
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));//这里时区需要设置一下，不然会有8个小时的时间差
        calendar.set(Calendar.HOUR_OF_DAY, 0);//设置为8：00点提醒
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        //选择的定时时间
        long selectTime = calendar.getTimeInMillis();//计算出设定的时间
        //如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if (systemTime > selectTime) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = calendar.getTimeInMillis();
        }
        long time = selectTime - systemTime;//计算现在时间到设定时间的时间差
        long alarmTime = firstTime + time;//系统当前的时间+时间差
        // 进行闹铃注册
        AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        if (am != null) {
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, alarmTime, AlarmManager.INTERVAL_DAY, pi);
        }
    }

}
