package com.jiafan.qirenjongbao.domain;

import java.io.Serializable;

public class VediodiscussNoty implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1843539948444198198L;

	private String tableName;

	private Vediodiscussbean data;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Vediodiscussbean getData() {
		return data;
	}

	public void setData(Vediodiscussbean data) {
		this.data = data;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "VediodiscussNoty [tableName=" + tableName + ", data=" + data
				+ "]";
	}
	
	
}
