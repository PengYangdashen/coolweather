package com.pengyang.coolweather.entity;

public class Province {

	private int id;
	private String provincerName;
	private String provincerCode;

	public Province() {
		super();
	}

	public Province(int id, String provincerName, String provincerCode) {
		super();
		this.id = id;
		this.provincerName = provincerName;
		this.provincerCode = provincerCode;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProvincerName() {
		return provincerName;
	}

	public void setProvincerName(String provincerName) {
		this.provincerName = provincerName;
	}

	public String getProvincerCode() {
		return provincerCode;
	}

	public void setProvincerCode(String provincerCode) {
		this.provincerCode = provincerCode;
	}

}
