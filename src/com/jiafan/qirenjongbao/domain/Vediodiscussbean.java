package com.jiafan.qirenjongbao.domain;


import cn.bmob.v3.BmobObject;

public class Vediodiscussbean extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3367369936214865624L;

	private String discuss;
	private String vedio_id;
	private String userimageurl;
	private String username;
	private String user_id;

	public String getDiscuss() {
		return discuss;
	}

	public void setDiscuss(String discuss) {
		this.discuss = discuss;
	}

	public String getVedio_id() {
		return vedio_id;
	}

	public void setVedio_id(String vedio_id) {
		this.vedio_id = vedio_id;
	}

	public String getUserimageurl() {
		return userimageurl;
	}

	public void setUserimageurl(String userimageurl) {
		this.userimageurl = userimageurl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	@Override
	public String toString() {
		return "Vediodiscussbean [discuss=" + discuss + ", vedio_id="
				+ vedio_id + ", userimageurl=" + userimageurl + ", username="
				+ username + "]";
	}

}
