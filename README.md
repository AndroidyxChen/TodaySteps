# TodaySteps
Android原生计步功能的实现，记录当日步数（仿微信运动），不需要后台service
##### 概述：通过调用Android4.4以上系统自带的计步传感器Sensor.TYPE_STEP_COUNTER，实现记录当日步数的逻辑，不需要后台service，因自带计步传感器记录的是开机以来所有的步数，所以需要自己处理跨天分割以及开关机的问题。
##### 1.封装了StepUtil供外界调用
```
    /**
     * 今日步数
     *
     * @param context 上下文
     * @return 今日步数
     */
    public static int getTodayStep(Context context) {
        return (int) StepSPHelper.getCurrentStep(context);
    }
```
##### 2.在项目开启的入口处进行计步功能的初始化
```
    /**
     * 初始化计步服务
     * 注：因初始化需要过程，正常项目中，初始化应该放在进入到主界面之前的activity中，比如闪屏页中进行初始化
     * 因此本demo在第一次安装时会提示"手机暂不支持计步功能"，杀死进程再次打开即可正常显示
     */
    private void initStepService() {
        Intent intent = new Intent(this, StepService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }
```
