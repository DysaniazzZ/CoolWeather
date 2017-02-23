package com.dysania.coolweather.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dysania.coolweather.R;
import com.dysania.coolweather.base.BaseActivity;
import com.dysania.coolweather.constant.IAppConstant;
import com.dysania.coolweather.gson.Forecast;
import com.dysania.coolweather.gson.Weather;
import com.dysania.coolweather.util.HttpUtil;
import com.dysania.coolweather.util.JsonUtil;
import com.dysania.coolweather.util.SPUtil;
import com.dysania.coolweather.util.UIUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by DysaniazzZ on 21/02/2017.
 * 主页
 */
public class HomeActivity extends BaseActivity {

    @BindView(R.id.sv_weather_layout)
    ScrollView mSvWeatherLayout;
    @BindView(R.id.ll_weather_error)
    LinearLayout mLlWeatherError;
    @BindView(R.id.tv_title_text)
    TextView mTvTitleText;
    @BindView(R.id.btn_title_left)
    Button mBtnTitleLeft;
    @BindView(R.id.tv_title_right)
    TextView mTvTitleRight;
    @BindView(R.id.v_title_divider)
    View mVTitleDivider;
    @BindView(R.id.tv_weather_temperature)
    TextView mTvWeatherTemperature;
    @BindView(R.id.tv_weather_condition)
    TextView mTvWeatherCondition;
    @BindView(R.id.ll_weather_forecast)
    LinearLayout mLlWeatherForecast;
    @BindView(R.id.tv_weather_aqi)
    TextView mTvWeatherAqi;
    @BindView(R.id.tv_weather_pm25)
    TextView mTvWeatherPm25;
    @BindView(R.id.tv_weather_comfort)
    TextView mTvWeatherComfort;
    @BindView(R.id.tv_weather_wash)
    TextView mTvWeatherWash;
    @BindView(R.id.tv_weather_sport)
    TextView mTvWeatherSport;

    private Unbinder mUnbinder;
    private static final String WEATHER_ID = "weather_id";
    private static final String WEATHER_CACHE = "weather_cache";

    public static void actionStart(Context context, String weatherId) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra(WEATHER_ID, weatherId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mUnbinder = ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        String weatherId = getIntent().getStringExtra(WEATHER_ID);
        String weatherCache = SPUtil.getString(mContext, WEATHER_CACHE, null);
        String weatherIdCache = SPUtil.getString(mContext, WEATHER_ID, null);
        if (!TextUtils.isEmpty(weatherCache) && weatherId.equals(weatherIdCache)) {
            //有缓存，直接解析
            Weather weather = JsonUtil.handleWeatherResponse(weatherCache);
            showWeather(weather);
        } else {
            //无缓存，联网获取
            requestWeather(weatherId);
        }
    }

    private void initView() {
        mBtnTitleLeft.setVisibility(View.VISIBLE);
        mTvTitleRight.setVisibility(View.VISIBLE);
        mVTitleDivider.setVisibility(View.GONE);
        mBtnTitleLeft.setBackgroundResource(R.drawable.ic_menu);
        mTvTitleText.setTextColor(getResources().getColor(R.color.colorTextWhite_ff));
        mTvTitleRight.setTextColor(getResources().getColor(R.color.colorTextWhite_ff));
    }

    private void requestWeather(final String weatherId) {
        String weatherUrl = IAppConstant.WEATHER_URL + "?cityid=" + weatherId + "&key=" + IAppConstant.WEATHER_KEY;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSvWeatherLayout.setVisibility(View.GONE);
                        mLlWeatherError.setVisibility(View.VISIBLE);
                        UIUtil.createToast(mContext, R.string.network_load_error);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseString = response.body().string();
                final Weather weather = JsonUtil.handleWeatherResponse(responseString);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SPUtil.putString(mContext, WEATHER_ID, weatherId);
                            SPUtil.putString(mContext, WEATHER_CACHE, responseString);
                            showWeather(weather);
                        } else {
                            UIUtil.createToast(mContext, R.string.network_load_error);
                        }
                    }
                });
            }
        });
    }

    private void showWeather(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        mTvTitleText.setText(cityName);
        mTvTitleRight.setText(updateTime);

        String temperature = weather.now.temperature;
        String weatherInfo = weather.now.more.info;
        mTvWeatherTemperature.setText(getString(R.string.weather_now_temperature, temperature));
        mTvWeatherCondition.setText(weatherInfo);

        mLlWeatherForecast.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_forecast_detail, mLlWeatherForecast, false);
            TextView dateText = (TextView) view.findViewById(R.id.tv_weather_item_date);
            TextView conditionText = (TextView) view.findViewById(R.id.tv_weather_item_condition);
            TextView diffText = (TextView) view.findViewById(R.id.tv_weather_item_diff);
            dateText.setText(forecast.date);
            conditionText.setText(forecast.more.info);
            diffText.setText(getString(R.string.weather_temperature_diff, forecast.temperature.min, forecast.temperature.max));
            mLlWeatherForecast.addView(view);
        }

        if (weather.aqi != null) {
            mTvWeatherAqi.setText(weather.aqi.city.aqi);
            mTvWeatherPm25.setText(weather.aqi.city.pm25);
        } else {
            mTvWeatherAqi.setText(R.string.weather_empty_data);
            mTvWeatherPm25.setText(R.string.weather_empty_data);
        }

        mTvWeatherComfort.setText(getString(R.string.weather_comfort_info, weather.suggestion.comfort.info));
        mTvWeatherWash.setText(getString(R.string.weather_wash_info, weather.suggestion.carWash.info));
        mTvWeatherSport.setText(getString(R.string.weather_sport_info, weather.suggestion.sport.info));
    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }
}
