package com.pengyang.coolweather.util;

import android.text.TextUtils;

import com.pengyang.coolweather.entity.City;
import com.pengyang.coolweather.entity.County;
import com.pengyang.coolweather.entity.Province;
import com.pengyang.coolweather.model.CoolWeatherDB;

public class Utility {
	
	/**
	 * 解析和处理服务器返回的省级数据
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
					// 将解析出来的数据存储到数据库Province表中
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 解析和处理服务器返回的市级数据
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
					// 将解析出来的数据存储到数据库Province表中
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 解析和处理服务器返回的县级数据
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
					// 将解析出来的数据存储到数据库County表中
					coolWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		
		return false;
	}
}
