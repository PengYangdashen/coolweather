package com.pengyang.coolweather.util;

import android.text.TextUtils;

import com.pengyang.coolweather.entity.City;
import com.pengyang.coolweather.entity.County;
import com.pengyang.coolweather.entity.Province;
import com.pengyang.coolweather.model.CoolWeatherDB;

public class Utility {
	
	/**
	 * �����ʹ�����������ص�ʡ������
	 */
	public synchronized static boolean handleProvinceResponse(CoolWeatherDB coolWeatherDB, String response) {
		if (!TextUtils.isEmpty(response)) {
			String [] allProvince = response.split(",");
			if (allProvince != null && allProvince.length > 0) {
				for (String p : allProvince) {
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvincerCode(array[0]);
					province.setProvincerName(array[1]);
					// ���������������ݴ洢�����ݿ�Province����
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * �����ʹ�����������ص��м�����
	 */
	public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB, String response, int provinceId) {
		if (!TextUtils.isEmpty(response)) {
			String [] allCities = response.split(",");
			if (allCities != null && allCities.length > 0) {
				for (String p : allCities) {
					String[] array = p.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					// ���������������ݴ洢�����ݿ�Province����
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * �����ʹ�����������ص��ؼ�����
	 */
	public synchronized static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB, String response, int cityId) {
		if (!TextUtils.isEmpty(response)) {
			String [] allCounties = response.split(",");
			if (allCounties != null && allCounties.length > 0) {  
				for (String p : allCounties) {
					String[] array = p.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					// ���������������ݴ洢�����ݿ�County����
					coolWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		
		return false;
	}
}
