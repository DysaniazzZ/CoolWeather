package com.dysania.coolweather.ui;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.TextView;

import com.dysania.coolweather.R;
import com.dysania.coolweather.base.BaseActivity;
import com.dysania.coolweather.constant.IAppConstant;
import com.dysania.coolweather.util.DateUtil;
import com.dysania.coolweather.util.DeviceUtil;
import com.dysania.coolweather.util.SPUtil;

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
    @BindView(R.id.tv_splash_welcome)
    TextView mTvSplashWelcome;
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
        String helloWords = DateUtil.getHelloWords(currentTimeMillis);
        mTvSplashWelcome.setText(getString(R.string.splash_app_welcome, dateString, weekString, helloWords));
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
                goToNextActivity();
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

    private void goToNextActivity() {
        //判断是否有缓存
        String weatherCache = SPUtil.getString(mContext, IAppConstant.WEATHER_CACHE, null);
        String weatherIdCache = SPUtil.getString(mContext, IAppConstant.WEATHER_ID, null);
        if (!TextUtils.isEmpty(weatherCache) && !TextUtils.isEmpty(weatherIdCache)) {
            WeatherActivity.actionStart(mContext, weatherIdCache);
        } else {
            AreaActivity.actionStart(mContext);
        }
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
