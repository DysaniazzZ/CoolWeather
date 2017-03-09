package com.dysania.coolweather.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dysania.coolweather.R;
import com.dysania.coolweather.base.BaseFragment;
import com.dysania.coolweather.constant.IAppConstant;
import com.dysania.coolweather.db.City;
import com.dysania.coolweather.db.County;
import com.dysania.coolweather.db.Province;
import com.dysania.coolweather.util.HttpUtil;
import com.dysania.coolweather.util.JsonUtil;
import com.dysania.coolweather.util.UIUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by DysaniazzZ on 22/02/2017.
 * 选择地区碎片
 */
public class AreaFragment extends BaseFragment {

    @BindView(R.id.tv_title_text)
    TextView mTvTitleText;
    @BindView(R.id.btn_title_left)
    Button mBtnTitleLeft;
    @BindView(R.id.lv_area_details)
    ListView mLvAreaDetails;
    @BindView(R.id.ll_area_error)
    LinearLayout mLlAreaError;

    private Unbinder mUnbinder;
    private ProgressDialog mProgressDialog;

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ArrayAdapter<String> mAreaAdapter;
    private List<String> mDataList = new ArrayList<>();

    /**
     * 省列表
     */
    private List<Province> mProvinceList;
    /**
     * 市列表
     */
    private List<City> mCityList;
    /**
     * 县列表
     */
    private List<County> mCountyList;

    /**
     * 选择的省份
     */
    private Province mSelectedProvince;
    /**
     * 选择的城市
     */
    private City mSelectedCity;

    /**
     * 当前选中的级别
     */
    private int mCurrentLevel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_area, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mAreaAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mDataList);
        mLvAreaDetails.setAdapter(mAreaAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        queryProvinces();
        mLvAreaDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mCurrentLevel == LEVEL_PROVINCE) {
                    mSelectedProvince = mProvinceList.get(position);
                    queryCities();
                } else if (mCurrentLevel == LEVEL_CITY) {
                    mSelectedCity = mCityList.get(position);
                    queryCounties();
                } else if (mCurrentLevel == LEVEL_COUNTY) {
                    String weatherId = mCountyList.get(position).getWeatherId();
                    if(getActivity() instanceof AreaActivity) {
                        WeatherActivity.actionStart(getContext(), weatherId);
                        getActivity().finish();
                    } else if(getActivity() instanceof WeatherActivity) {
                        WeatherActivity activity = (WeatherActivity) getActivity();
                        activity.mDlWeatherLayout.closeDrawers();
                        activity.mSrlWeatherLayout.setRefreshing(true);
                        activity.setWeatherId(weatherId);
                        activity.requestWeather(weatherId);
                    }
                }
            }
        });
        mBtnTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentLevel == LEVEL_COUNTY) {
                    queryCities();
                } else if (mCurrentLevel == LEVEL_CITY) {
                    queryProvinces();
                }
            }
        });
    }

    /**
     * 查询全国所有的省，优先从数据库查询，如果没有查到再从网络获取
     */
    private void queryProvinces() {
        mTvTitleText.setText(R.string.area_choose_area);
        mBtnTitleLeft.setVisibility(View.GONE);
        mCurrentLevel = LEVEL_PROVINCE;
        showLoadError(false);
        mProvinceList = DataSupport.findAll(Province.class);
        if (mProvinceList != null && !mProvinceList.isEmpty()) {
            mDataList.clear();
            for (Province province : mProvinceList) {
                mDataList.add(province.getProvinceName());
            }
            mAreaAdapter.notifyDataSetChanged();
            mLvAreaDetails.setSelection(0);
        } else {
            queryFromServer(IAppConstant.AREA_URL, "province");
        }
    }

    /**
     * 查询全国所有的市，优先从数据库查询，如果没有查到再从网络获取
     */
    private void queryCities() {
        mTvTitleText.setText(mSelectedProvince.getProvinceName());
        mBtnTitleLeft.setVisibility(View.VISIBLE);
        mCurrentLevel = LEVEL_CITY;
        showLoadError(false);
        mCityList = DataSupport.where("provinceid = ?", String.valueOf(mSelectedProvince.getId())).find(City.class);
        if (mCityList != null && !mCityList.isEmpty()) {
            mDataList.clear();
            for (City city : mCityList) {
                mDataList.add(city.getCityName());
            }
            mAreaAdapter.notifyDataSetChanged();
            mLvAreaDetails.setSelection(0);
        } else {
            int provinceCode = mSelectedProvince.getProvinceCode();
            String address = IAppConstant.AREA_URL + "/" + provinceCode;
            queryFromServer(address, "city");
        }
    }

    /**
     * 查询全国所有的县，优先从数据库查询，如果没有查到再从网络获取
     */
    private void queryCounties() {
        mTvTitleText.setText(mSelectedCity.getCityName());
        mBtnTitleLeft.setVisibility(View.VISIBLE);
        mCurrentLevel = LEVEL_COUNTY;
        showLoadError(false);
        mCountyList = DataSupport.where("cityid = ?", String.valueOf(mSelectedCity.getId())).find(County.class);
        if (mCountyList != null && !mCountyList.isEmpty()) {
            mDataList.clear();
            for (County county : mCountyList) {
                mDataList.add(county.getCountyName());
            }
            mAreaAdapter.notifyDataSetChanged();
            mLvAreaDetails.setSelection(0);
        } else {
            int provinceCode = mSelectedProvince.getProvinceCode();
            int cityCode = mSelectedCity.getCityCode();
            String address = IAppConstant.AREA_URL + "/" + provinceCode + "/" + cityCode;
            queryFromServer(address, "county");
        }
    }

    /**
     * 根据传入的地址和类型从服务器获取地区数据
     *
     * @param address
     * @param type
     */
    private void queryFromServer(String address, final String type) {
        showLoadingDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissLoadingDialog();
                        UIUtil.createToast(getContext(), R.string.network_load_error);
                        showLoadError(true);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string();
                boolean result = false;
                if ("province".equals(type)) {
                    result = JsonUtil.handleProvinceResponse(responseString);
                } else if ("city".equals(type)) {
                    result = JsonUtil.handleCityResponse(responseString, mSelectedProvince.getId());
                } else if ("county".equals(type)) {
                    result = JsonUtil.handleCountyResponse(responseString, mSelectedCity.getId());
                }
                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dismissLoadingDialog();
                            if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)) {
                                queryCities();
                            } else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });
    }

    private void showLoadError(boolean showLoadError) {
        if (showLoadError) {
            mLvAreaDetails.setVisibility(View.GONE);
            mLlAreaError.setVisibility(View.VISIBLE);
        } else {
            mLvAreaDetails.setVisibility(View.VISIBLE);
            mLlAreaError.setVisibility(View.GONE);
        }
    }

    public void showLoadingDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getString(R.string.network_is_loading));
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    public void dismissLoadingDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        mUnbinder.unbind();
        super.onDestroyView();
    }
}
