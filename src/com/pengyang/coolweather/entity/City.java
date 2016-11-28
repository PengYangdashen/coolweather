package com.pengyang.coolweather.entity;

public class City {

	private int id;
	private String cityCode;
	private String cityName;
	private int provinceId;

	public City(int id, String cityCode, String cityName, int provinceId) {
		super();
		this.id = id;
		this.cityCode = cityCode;
		this.cityName = cityName;
		this.provinceId = provinceId;
	}

	public City() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}

}
