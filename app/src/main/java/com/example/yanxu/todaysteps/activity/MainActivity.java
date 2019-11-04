package com.example.yanxu.todaysteps.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.yanxu.todaysteps.R;
import com.example.yanxu.todaysteps.util.StepUtil;
import com.example.yanxu.todaysteps.util.StepService;

public class MainActivity extends AppCompatActivity {

    private TextView mTvSteps;
    private Button mBtRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initStepService();
        initView();
        initData();
    }

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

    /**
     * 初始化view
     */
    private void initView() {
        mTvSteps = findViewById(R.id.tv_steps);
        mBtRefresh = findViewById(R.id.bt_refresh);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (!StepUtil.isSupportStep(this)) {
            mTvSteps.setText("手机暂不支持计步功能");
            return;
        }
        mBtRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String steps = StepUtil.getTodayStep(MainActivity.this) + "步";
                mTvSteps.setText(steps);
            }
        });
    }

}
