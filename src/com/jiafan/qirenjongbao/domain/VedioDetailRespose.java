package com.jiafan.qirenjongbao.domain;

import java.util.List;

public class VedioDetailRespose {

	private String total;

	private List<Vediosbean> videos;
	
	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public List<Vediosbean> getVideos() {
		return videos;
	}

	public void setVideos(List<Vediosbean> videos) {
		this.videos = videos;
	}

	@Override
	public String toString() {
		return "VedioDetailRespose [total=" + total + ", videos=" + videos.toString()
				+ "]";
	}
	
	
}
