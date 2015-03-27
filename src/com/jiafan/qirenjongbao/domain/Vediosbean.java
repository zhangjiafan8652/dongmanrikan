package com.jiafan.qirenjongbao.domain;

import java.io.Serializable;

public class Vediosbean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6653929127330348035L;
	private String duration;
	private String id;
	private String title;
	private String view_count;
	private String bigThumbnail;
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getView_count() {
		return view_count;
	}
	public void setView_count(String view_count) {
		this.view_count = view_count;
	}
	public String getBigThumbnail() {
		return bigThumbnail;
	}
	public void setBigThumbnail(String bigThumbnail) {
		this.bigThumbnail = bigThumbnail;
	}
	@Override
	public String toString() {
		return "Vediosbean [duration=" + duration + ", id=" + id + ", title="
				+ title + ", view_count=" + view_count + ", bigThumbnail="
				+ bigThumbnail + "]";
	}
	
	
}
