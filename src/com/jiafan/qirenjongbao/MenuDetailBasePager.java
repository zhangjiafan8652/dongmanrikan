package com.jiafan.qirenjongbao;

import android.content.Context;
import android.view.View;


public abstract class MenuDetailBasePager {
	
	public Context mContext;
	private View rootView;

	public MenuDetailBasePager(Context context) {
		this.mContext = context;
		
		rootView = initView();
	}
	
	public abstract View initView();
	
	public View getRootView() {
		return rootView;
	}
	
	
	public void initData() {
		
	}
}
