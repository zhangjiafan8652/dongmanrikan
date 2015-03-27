package com.jiafan.qirenjongbao.domain;

import java.util.List;

public class VedioResponse {

	private String more;
	
	private List<String> vediolist;

	public String getMore() {
		return more;
	}

	public void setMore(String more) {
		this.more = more;
	}

	public List<String> getVediolist() {
		return vediolist;
	}

	public void setVediolist(List<String> vediolist) {
		this.vediolist = vediolist;
	}

	@Override
	public String toString() {
		return "VedioResponse [more=" + more + ", vediolist=" + vediolist
				+ ", getMore()=" + getMore() + ", getVediolist()="
				+ getVediolist() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
	

}
