package com.jiafan.qirenjongbao;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;

import cn.bmob.v3.Bmob;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.jiafan.qirenjongbao.application.MyConstants;
import com.jiafan.qirenjongbao.fragment.LeftMenuFragment;
import com.jiafan.qirenjongbao.fragment.MainContentFragment;
import com.jiafan.qirenjongbao.utils.IntentUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * alt + shift + J
 * 
 * @author jiafan
 * 
 *         主界面
 */
public class MainUI extends SlidingFragmentActivity {

	// 左侧菜单fragment的tag
	private final String LEFT_MENU_FRAGMENT_TAG = "left_menu";
	// 主界面fragment的tag
	private final String MAIN_CONTENT_FRAGMENT_TAG = "main_content";
	private SlidingMenu slidingMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_content); // 主界面布局
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// 设置activity动画，第一个参数进入。第二个退出
		//overridePendingTransition(R.anim.enter, R.anim.out);

		setBehindContentView(R.layout.left_menu); // 左侧菜单布局

		slidingMenu = getSlidingMenu();
		slidingMenu.setMode(SlidingMenu.LEFT); // 设置左侧菜单可用.
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN); // 整个屏幕都可以拖拽出菜单.

		DisplayMetrics dm = new DisplayMetrics();
		// 获取屏幕信息
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		int screenWidth = dm.widthPixels;
		
		MyConstants.screenWidth=screenWidth;

		if (screenWidth < 500) {
			slidingMenu.setBehindOffset(100); // 设置主界面留在屏幕上的宽度
		} else {
			slidingMenu.setBehindOffset(200); // 设置主界面留在屏幕上的宽度
		}

		initFragment();

	}

	/**
	 * 初始化菜单和主界面Fragment
	 */
	private void initFragment() {
		// 获取Fragment管理器对象
		FragmentManager fm = getSupportFragmentManager();

		// 开启事物
		FragmentTransaction ft = fm.beginTransaction(); // 得到事物操作对象

		// 替换左侧菜单布局
		ft.replace(R.id.fl_left_menu, new LeftMenuFragment(),
				LEFT_MENU_FRAGMENT_TAG);
		// 替换主界面布局
		ft.replace(R.id.fl_main_content, new MainContentFragment(),
				MAIN_CONTENT_FRAGMENT_TAG);

		// 提交
		ft.commit();

	}

	/**
	 * 获取左侧菜单的Fragment对象
	 * 
	 * @return
	 */
	public LeftMenuFragment getLeftMenuFragment() {
		FragmentManager fm = getSupportFragmentManager();
		LeftMenuFragment leftMenuFragment = (LeftMenuFragment) fm
				.findFragmentByTag(LEFT_MENU_FRAGMENT_TAG);
		return leftMenuFragment;
	}

	/**
	 * 获取主页面的Fragment对象
	 * 
	 * @return
	 */
	public MainContentFragment getMainContentFragment() {
		FragmentManager fm = getSupportFragmentManager();
		MainContentFragment mainContentFragment = (MainContentFragment) fm
				.findFragmentByTag(MAIN_CONTENT_FRAGMENT_TAG);
		return mainContentFragment;
	}

	public SlidingMenu getslidingmenu() {
		return slidingMenu;
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
