package com.jiafan.qirenjongbao.newscenterimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jiafan.qirenjongbao.MainUI;
import com.jiafan.qirenjongbao.MenuDetailBasePager;
import com.jiafan.qirenjongbao.R;

import com.jiafan.qirenjongbao.domain.NewsCenterBean.ChildRen;
import com.jiafan.qirenjongbao.domain.NewsCenterBean.NewsCenterData;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;



/**
 * @author jiafan
 * 新闻菜单对应的页面
 */


public class NewsMenuDetailPager extends MenuDetailBasePager implements OnPageChangeListener {
	
	@ViewInject(R.id.tpi_news_menu)
	private TabPageIndicator mIndicator;

	@ViewInject(R.id.vp_news_menu_content)
	private ViewPager mViewPager;
	
	private List<ChildRen> childrenList; // 当前页面页签的数据
	private List<MenuDetailBasePager> tabPagerList; // 页签详情页面
	
	public NewsMenuDetailPager(Context context) {
		super(context);
	}

	public NewsMenuDetailPager(Context context, NewsCenterData newsCenterData) {
		super(context);
		
		childrenList = newsCenterData.children;
	}

	@Override
	public View initView() {
		View view = View.inflate(mContext, R.layout.news_menu, null);
		ViewUtils.inject(this, view); // 把view对象注入到xUtils框架中.
		return view;
	}
	
	@Override
	public void initData() {
		// 把页签对应的详情页面准备出来.
		tabPagerList = new ArrayList<MenuDetailBasePager>();
	
		
		tabPagerList.add(new NewsMenuTabDetailPager(mContext, childrenList.get(0)));
		tabPagerList.add(new Gameshipin(mContext,childrenList.get(1)));
		tabPagerList.add(new Daomeixiong(mContext,childrenList.get(2)));
		tabPagerList.add(new Laofuzi(mContext,childrenList.get(3)));
		if(mViewPager==null){
			Log.i("----------", "viewpager是空的");
		}else {
			Log.i("----------", "viewpagerb");
		}
		NewsMenuAdapter mAdapter = new NewsMenuAdapter();
		mViewPager.setAdapter(mAdapter);
		// 把ViewPager对象设置给TabPageIndicator
		// 关联完之后页签的数据都来自于mViewPager的数据适配NewsMenuAdapter
		mIndicator.setViewPager(mViewPager);
		mIndicator.setOnPageChangeListener(this);
	}
	
	class NewsMenuAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return tabPagerList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			MenuDetailBasePager pager = (MenuDetailBasePager) tabPagerList.get(position);
			View rootView = pager.getRootView();
			container.addView(rootView);
			
			// 初始化数据
			pager.initData();
			return rootView;
		}

		/**
		 * 返回的字符串会作为TabPageIndicator对应position的页签数据来展示.
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			return childrenList.get(position).title;
		}
	}
	
	@OnClick(R.id.ib_news_menu_next_tab)
	public void nextTab(View v) {
		// 下一个页签按钮的点击事件
		mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int position) {
		isEnableSlidingMenu(position == 0);
	}

	/**
	 * 是否启用侧滑菜单
	 * @param isEnable true启用
	 */
	private void isEnableSlidingMenu(boolean isEnable) {
		if(isEnable) {
			((MainUI) mContext).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			((MainUI) mContext).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}
	
}
