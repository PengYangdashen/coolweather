package com.pengyang.coolweather.activity;

import java.util.ArrayList;
import java.util.List;

import com.pengyang.coolweather.R;
import com.pengyang.coolweather.entity.City;
import com.pengyang.coolweather.entity.County;
import com.pengyang.coolweather.entity.Province;
import com.pengyang.coolweather.model.CoolWeatherDB;
import com.pengyang.coolweather.util.HttpCallbackListener;
import com.pengyang.coolweather.util.HttpUtil;
import com.pengyang.coolweather.util.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity{

	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;
	
	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB coolWeatherDB;
	private List<String> dataList = new ArrayList<String>();
	/**
	 * ʡ�б�
	 */
	private List<Province> provinceList;
	/**
	 * ���б�
	 */
	private List<City> cityList;
	/**
	 * ���б�
	 */
	private List<County> countyList;
	/**
	 * ѡ�е�ʡ��
	 */
	private Province selectedProvince;
	/**
	 * ѡ�еĳ���
	 */
	private City selectedCity;
	/**
	 * ��ǰѡ�еļ���
	 */
	private int currentLevel;
	/**
	 * �Ƿ��WeatherActivity����ת����
	 */
	private boolean isFromWeatherActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_area);
		
		isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if (prefs.getBoolean("city_selected", false) && !isFromWeatherActivity) {
			Intent intent = new Intent(this, WeatherActivity.class);
			startActivity(intent);
			finish();
			return;
		}
		
		listView = (ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		coolWeatherDB = CoolWeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.i("PY", "position:" + position);
				if (currentLevel == LEVEL_PROVINCE) {
					selectedProvince = provinceList.get(position);
					queryCities();
				} else if (currentLevel == LEVEL_CITY) {
					selectedCity = cityList.get(position);
					queryCounties();
				} else if (currentLevel == LEVEL_COUNTY) {
					String countyCode = countyList.get(position).getCountyCode();
					Intent intent = new Intent(ChooseAreaActivity.this, WeatherActivity.class);
					intent.putExtra("county_code", countyCode);
					startActivity(intent);
					finish();
				}
			}
		});
		queryProvinces();
	}

	/**
	 * ��ѯȫ�����е�ʡ�����ȴ����ݿ��в�ѯ�����û�в�ѯ���ٵ��������ϲ�ѯ
	 */
	private void queryProvinces() {
		provinceList = coolWeatherDB.loadProvinces();
		if (provinceList.size() > 0) {
			dataList.clear();
			for (Province province : provinceList) {
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("�й�");
			currentLevel = LEVEL_PROVINCE;
		} else {
			queryFromServer(null, "province");
		}
	}

	/**
	 * ��ѯѡ��ʡ�����е��У����ȴ����ݿ��в�ѯ�����û�в�ѯ���ٵ��������ϲ�ѯ
	 */
	private void queryCities() {
		cityList = coolWeatherDB.loadCities(selectedProvince.getId());
		Log.i("PY", "queryCities.loadCities" + selectedProvince.getProvinceName());
		if (cityList.size() > 0) {
			dataList.clear();
			for (City city : cityList) {
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		} else {
			queryFromServer(selectedProvince.getProvinceCode(), "city");
		}
	}
	
	/**
	 * ��ѯѡ���������е��أ����ȴ����ݿ��в�ѯ�����û�в�ѯ���ٵ��������ϲ�ѯ
	 */
	private void queryCounties() {
		countyList = coolWeatherDB.loadCounties(selectedCity.getId());
		Log.i("PY", "queryCities.loadCities" + selectedCity.getCityName());
		if (countyList.size() > 0) {
			dataList.clear();
			for (County county : countyList) {
				dataList.add(county.getCountyName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedCity.getCityName());
			currentLevel = LEVEL_COUNTY;
		} else {
			queryFromServer(selectedCity.getCityCode(), "county");
		}
	}
	
	/**
	 * ���ݴ���Ĵ��ź����ʹӷ������ϲ�ѯ��ʡ��������
	 */
	private void queryFromServer (final String code, final String type) {
		String address;
		if (!TextUtils.isEmpty(code)) {
			address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
			Log.i("PY", "queryFromServer -> bycode");
		} else {
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				boolean result = false;
				if ("province".equals(type)) {
					result = Utility.handleProvinceResponse(coolWeatherDB, response);
				} else if ("city".equals(type)) {
					result = Utility.handleCitiesResponse(coolWeatherDB, response, selectedProvince.getId());
				} else {
					result = Utility.handleCountiesResponse(coolWeatherDB, response, selectedCity.getId());
				}
				if (result) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							closeProgressDialog();
							if ("province".equals(type)) {
								queryProvinces();
							} else if ("city".equals(type)) {
								queryCities();
							} else {
								queryCounties();
							}
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}
	
	/**
	 * ��ʾ���ȶԻ���
	 */
	private void showProgressDialog(){
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("���ڼ���...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	};
	
	/**
	 * �رնԻ���
	 */
	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}
	
	@Override
	public void onBackPressed() {
		if (currentLevel == LEVEL_COUNTY) {
			queryCities();
		} else if (currentLevel == LEVEL_CITY) {
			queryProvinces();
		} else {
			if (isFromWeatherActivity) {
				Intent intent = new Intent(this, WeatherActivity.class);
				startActivity(intent);
			}
			finish();
		}
	}

}
