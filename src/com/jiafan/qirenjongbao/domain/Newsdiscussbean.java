package com.jiafan.qirenjongbao.domain;

import cn.bmob.v3.BmobObject;

public class Newsdiscussbean extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1705875358472767214L;

	private String discuss;
	private String userid;
	private String userimageurl;
	private String newsname;
	

	public String getNewsname() {
		return newsname;
	}

	public void setNewsname(String newsname) {
		this.newsname = newsname;
	}

	public String getDiscuss() {
		return discuss;
	}

	public void setDiscuss(String discuss) {
		this.discuss = discuss;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserimageurl() {
		return userimageurl;
	}

	public void setUserimageurl(String userimageurl) {
		this.userimageurl = userimageurl;
	}

	@Override
	public String toString() {
		return "Newsdiscussbean [discuss=" + discuss + ", userid=" + userid
				+ ", userimageurl=" + userimageurl + ", newsname=" + newsname
				+ "]";
	}

}
