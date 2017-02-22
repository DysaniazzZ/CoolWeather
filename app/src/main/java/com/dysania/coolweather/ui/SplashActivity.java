package com.dysania.coolweather.ui;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.dysania.coolweather.R;
import com.dysania.coolweather.base.BaseActivity;
import com.dysania.coolweather.util.DateUtil;
import com.dysania.coolweather.util.DeviceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by DysaniazzZ on 21/02/2017.
 * 启动页
 */
public class SplashActivity extends BaseActivity {

    @BindView(R.id.tv_splash_countdown)
    TextView mTvSplashCountdown;
    @BindView(R.id.tv_splash_date)
    TextView mTvSplashDate;
    @BindView(R.id.tv_splash_hello)
    TextView mTvSplashHello;
    @BindView(R.id.tv_splash_version)
    TextView mTvSplashVersion;

    private Unbinder mUnbinder;
    private CountDownTimer mCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorBgWhite_ff));
        }
        setContentView(R.layout.activity_splash);
        mUnbinder = ButterKnife.bind(this);
        init();
    }

    private void init() {
        long currentTimeMillis = System.currentTimeMillis();
        String dateString = DateUtil.getDateString(currentTimeMillis);
        String weekString = DateUtil.getWeekString(currentTimeMillis);
        mTvSplashDate.setText(getString(R.string.splash_this_date, dateString, weekString));
        String helloWords = DateUtil.getHelloWords(currentTimeMillis);
        mTvSplashHello.setText(helloWords);
        String versionName = DeviceUtil.getVersionName();
        String appLabel = DeviceUtil.getAppLabel();
        mTvSplashVersion.setText(getString(R.string.splash_app_version, appLabel, versionName));
        mCountDownTimer = new CountDownTimer(4000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                mTvSplashCountdown.setText(getString(R.string.splash_skip_countdown, millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                HomeActivity.actionStart(mContext);
                mCountDownTimer.cancel();
                finish();
            }
        }.start();
    }

    @OnClick(R.id.ll_splash_countdown)
    public void onClick() {
        //点击了跳过
        mCountDownTimer.onFinish();
    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        super.onDestroy();
    }
}
