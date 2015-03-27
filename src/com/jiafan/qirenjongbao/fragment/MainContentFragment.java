package com.jiafan.qirenjongbao.fragment;

import com.jiafan.qirenjongbao.NewsCenterPager;

import android.view.LayoutInflater;
import android.view.View;



/**
 * @author jiafan
 * 主界面的Fragment
 */
public class MainContentFragment extends BaseFragment  {
	
	

	private NewsCenterPager newsconterpager;

	//这个类是用来初始化数据的.程序启动时候会调用下面的init方法
	@Override
	public View initView(LayoutInflater inflater) {
		newsconterpager = new NewsCenterPager(mActivity); 
		return newsconterpager.getRootView();
	}

	@Override
	public void initData() {
		newsconterpager.initData();
		
	}

	public NewsCenterPager getNewsCenterPager() {
		// TODO Auto-generated method stub
		return newsconterpager;
	}


	
	
	
}
