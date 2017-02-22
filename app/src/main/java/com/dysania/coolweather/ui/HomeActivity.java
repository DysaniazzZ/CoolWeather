package com.dysania.coolweather.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dysania.coolweather.R;
import com.dysania.coolweather.base.BaseActivity;

/**
 * Created by DysaniazzZ on 21/02/2017.
 * 主页
 */
public class HomeActivity extends BaseActivity {

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
}
