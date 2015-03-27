package com.jiafan.qirenjongbao.domain;

import cn.bmob.v3.BmobObject;

public class Sugestbean extends BmobObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String username;
	private String sugest;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSugest() {
		return sugest;
	}
	public void setSugest(String sugest) {
		this.sugest = sugest;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "Sugestbean [username=" + username + ", sugest=" + sugest + "]";
	}
	
	
}
