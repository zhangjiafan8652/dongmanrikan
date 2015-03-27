package com.jiafan.qirenjongbao.domain;

import java.io.Serializable;

public class DiscussNotifyResponse implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3730532949320235726L;


	private String tableName;
	
	
	private Newsdiscussbean data;


	public String getTableName() {
		return tableName;
	}


	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


	public Newsdiscussbean getData() {
		return data;
	}


	public void setData(Newsdiscussbean data) {
		this.data = data;
	}


	@Override
	public String toString() {
		return "DiscussNotifyResponse [tableName=" + tableName + ", data="
				+ data + "]";
	}
	
	
	
}
