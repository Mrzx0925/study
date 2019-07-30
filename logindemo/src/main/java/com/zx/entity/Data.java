package com.zx.entity;

public class Data {
	private String agecontent;
	private int num;
	public String getAgecontent() {
		return agecontent;
	}
	public void setAgecontent(String agecontent) {
		this.agecontent = agecontent;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public void addNum() {
		this.num = num+1;
	}
	@Override
	public String toString() {
		return "Data [agecontent=" + agecontent + ", num=" + num + ", getAgecontent()=" + getAgecontent()
				+ ", getNum()=" + getNum() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	public Data(String agecontent, int num) {
		this.agecontent = agecontent;
		this.num = num;
	}
	
	
}
