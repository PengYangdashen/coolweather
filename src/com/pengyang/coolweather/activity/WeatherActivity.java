package com.pengyang.coolweather.activity;

import com.pengyang.coolweather.R;
import com.pengyang.coolweather.R.layout;
import com.pengyang.coolweather.util.HttpCallbackListener;
import com.pengyang.coolweather.util.HttpUtil;
import com.pengyang.coolweather.util.Utility;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity {

	
	private LinearLayout llWeatherInfo;
	/**
	 * ��ʾ������
	 */
	private TextView tvCityName;
	/**
	 * ��ʾ����ʱ��
	 */
	private TextView tvPublish;
	/**
	 * ��ʾ����������Ϣ
	 */
	private TextView tvWeatherDesp;
	/**
	 * ��ʾ����1
	 */
	private TextView tvTemp1;
	/**
	 * ��ʾ����2
	 */
	private TextView tvTemp2;
	/**
	 * ��ʾ��ǰʱ��
	 */
	private TextView tvCurrentDate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		
		initView();
		
		String countyCode = getIntent().getStringExtra("county_code");
		if (!TextUtils.isEmpty(countyCode)) {
			Log.i("PY", "weatheractivity.oncreate");
			tvPublish.setText("ͬ����...");
			llWeatherInfo.setVisibility(View.INVISIBLE);
			tvCityName.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);
		} else {
			showWeather();
		}
	}

	/**
	 * ��ѯ�ؼ���������Ӧ����������
	 */
	private void queryWeatherCode(String countyCode) {
		Log.i("PY", "weatheractivity.queryWeatherCode");
		String address = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
		queryFromServer(address, "countyCode");
	}
	
	/**
	 * ��ѯ�������Ŷ�Ӧ������
	 */
	protected void queryWeatherInfo(String weatherCode) {
		String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
		queryFromServer(address, "weatherCode");
	}

	/**
	 * ���ݴ���ĵ�ַ���������������ѯ�������Ż���������Ϣ
	 */
	private void queryFromServer(final String address, final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				if ("countyCode".equals(type)) {
					if (!TextUtils.isEmpty(response)) {
						String[] array = response.split("\\|"); 
						if (array != null && array.length ==2) {
							String weatherCode = array[1];
							queryWeatherInfo(weatherCode);
						}
					}
				} else if ("weatherCode".equals(type)) {
					Utility.handleWeatherResponse(WeatherActivity.this, response);
					runOnUiThread(new Runnable() {
						public void run() {
							showWeather();
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					public void run() {
						tvPublish.setText("ͬ��ʧ��!");
					}
				});
			}
		});
		
	}

	private void showWeather() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		tvCityName.setText(prefs.getString("city_name", ""));
		tvTemp1.setText(prefs.getString("temp1", ""));
		tvTemp2.setText(prefs.getString("temp2", ""));
		tvWeatherDesp.setText(prefs.getString("weather_desp", ""));
		tvCurrentDate.setText(prefs.getString("current_date", ""));
		tvPublish.setText("����" + prefs.getString("publish_time", "") + "����");
		llWeatherInfo.setVisibility(View.VISIBLE);
		tvCityName.setVisibility(View.VISIBLE);
	}

	private void initView() {
		llWeatherInfo = (LinearLayout) findViewById(R.id.ll_weather_info);
		
		tvCityName = (TextView) findViewById(R.id.tv_city_name);
		tvPublish = (TextView) findViewById(R.id.tv_publish);
		tvWeatherDesp = (TextView) findViewById(R.id.tv_weather_desp);
		tvTemp1 = (TextView) findViewById(R.id.tv_temp1);
		tvTemp2 = (TextView) findViewById(R.id.tv_temp2);
		tvCurrentDate = (TextView) findViewById(R.id.tv_current_date);
	}

}
