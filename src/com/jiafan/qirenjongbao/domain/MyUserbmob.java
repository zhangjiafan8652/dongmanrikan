package com.jiafan.qirenjongbao.domain;

import cn.bmob.v3.BmobObject;

public class MyUserbmob  extends BmobObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String email;
	private String username;
	private String password;
	private String phonenumber;
	private String userlevel;
	private String indivisign;
	private String headphoto;
	private String sex;
	
	
	public MyUserbmob() {
        this.setTableName("MyUser");
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUserlevel() {
		return userlevel;
	}

	public void setUserlevel(String userlevel) {
		this.userlevel = userlevel;
	}

	public String getIndivisign() {
		return indivisign;
	}

	public void setIndivisign(String indivisign) {
		this.indivisign = indivisign;
	}

	public String getHeadphoto() {
		return headphoto;
	}

	public void setHeadphoto(String headphoto) {
		this.headphoto = headphoto;
	}


	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Override
	public String toString() {
		return "MyUserbmob [email=" + email + ", username=" + username
				+ ", password=" + password + ", phonenumber=" + phonenumber
				+ ", userlevel=" + userlevel + ", indivisign=" + indivisign
				+ ", headphoto=" + headphoto + ", sex=" + sex + "]";
	}




	

	
}
