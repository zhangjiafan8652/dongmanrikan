package com.jiafan.qirenjongbao.domain;

import java.util.List;

public class Loginpicturebean {

	String status;
	String size;
	List<String> picturelist;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public List<String> getPicturelist() {
		return picturelist;
	}
	public void setPicturelist(List<String> picturelist) {
		this.picturelist = picturelist;
	}
	@Override
	public String toString() {
		return "loginpicturebean [status=" + status + ", size=" + size
				+ ", picturelist=" + picturelist + "]";
	}
	
	
}
