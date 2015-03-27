package com.jiafan.qirenjongbao.domain;

import java.util.Arrays;

public class Nnewsdetail {

	private String newsname;
	private String titil;
	private String time;
	private String fronttextcount;
	private String imagecount;
	private String aftertextcount;
	private String textfront[];
	private String textafter[];
	private String imagerurl[];
	
	public String getNewsname() {
		return newsname;
	}
	public void setNewsname(String newsname) {
		this.newsname = newsname;
	}
	public String getTitil() {
		return titil;
	}
	public void setTitil(String titil) {
		this.titil = titil;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getFronttextcount() {
		return fronttextcount;
	}
	public void setFronttextcount(String fronttextcount) {
		this.fronttextcount = fronttextcount;
	}
	public String getImagecount() {
		return imagecount;
	}
	public void setImagecount(String imagecount) {
		this.imagecount = imagecount;
	}
	public String getAftertextcount() {
		return aftertextcount;
	}
	public void setAftertextcount(String aftertextcount) {
		this.aftertextcount = aftertextcount;
	}
	public String[] getTextfront() {
		return textfront;
	}
	public void setTextfront(String[] textfront) {
		this.textfront = textfront;
	}
	public String[] getTextafter() {
		return textafter;
	}
	public void setTextafter(String[] textafter) {
		this.textafter = textafter;
	}
	public String[] getImagerurl() {
		return imagerurl;
	}
	public void setImagerurl(String[] imagerurl) {
		this.imagerurl = imagerurl;
	}
	@Override
	public String toString() {
		return "Nnewsdetail [titil=" + titil + ", time=" + time
				+ ", fronttextcount=" + fronttextcount + ", imagecount="
				+ imagecount + ", aftertextcount=" + aftertextcount
				+ ", textfront=" + Arrays.toString(textfront) + ", textafter="
				+ Arrays.toString(textafter) + ", imagerurl="
				+ Arrays.toString(imagerurl) + ", getTitil()=" + getTitil()
				+ ", getTime()=" + getTime() + ", getFronttextcount()="
				+ getFronttextcount() + ", getImagecount()=" + getImagecount()
				+ ", getAftertextcount()=" + getAftertextcount()
				+ ", getTextfront()=" + Arrays.toString(getTextfront())
				+ ", getTextafter()=" + Arrays.toString(getTextafter())
				+ ", getImagerurl()=" + Arrays.toString(getImagerurl())
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	
	
	
}
