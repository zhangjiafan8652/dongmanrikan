package com.jiafan.qirenjongbao.application;

import org.json.JSONObject;

import com.jiafan.qirenjongbao.youku.CachedActivity;
import com.jiafan.qirenjongbao.youku.CachingActivity;
import com.youku.player.YoukuPlayerBaseApplication;


import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetCallback;
import android.app.Activity;
import android.app.Application;
import android.os.Handler;
import android.os.Looper;

public class Myaplication extends YoukuPlayerBaseApplication {

	// 获取到主线程的上下文
	private static Myaplication mContext = null;
	// 获取到主线程的handler
	private static Handler mMainThreadHandler = null;
	// 获取到主线程的looper
	private static Looper mMainThreadLooper = null;
	// 获取到主线程
	private static Thread mMainThead = null;
	// 获取到主线程的id
	private static int mMainTheadId;

	// 友盟推送
	/**
	 * 全局异常处理将异常保存到sdcard/crash中
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		/*
		 * CrashHandler crashHandler = CrashHandler.getInstance();
		 * crashHandler.init(getApplicationContext());
		 */
		this.mContext = this;
		this.mMainThreadHandler = new Handler();
		this.mMainThreadLooper = getMainLooper();
		this.mMainThead = Thread.currentThread();
		// android.os.Process.myUid() 获取到用户id
		// android.os.Process.myPid()获取到进程id
		// android.os.Process.myTid()获取到调用线程的id
		this.mMainTheadId = android.os.Process.myTid();

		// 初始化 Bmob SDK
		// 使用时请将第二个参数Application ID替换成你在Bmob服务器端创建的Application ID
		Bmob.initialize(this, "04a8cd0172a9c723241f27d4c2b7c944");

		
		
	}

	public static Myaplication getApplication() {
		return mContext;
	}

	public static Handler getMainThreadHandler() {
		return mMainThreadHandler;
	}

	public static Looper getMainThreadLooper() {
		return mMainThreadLooper;
	}

	public static Thread getMainThread() {
		return mMainThead;
	}

	public static int getMainThreadId() {
		return mMainTheadId;
	}

	@Override
	public String configDownloadPath() {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 通过覆写该方法，返回“正在缓存视频信息的界面”，
	 * 则在状态栏点击下载信息时可以自动跳转到所设定的界面.
	 * 用户需要定义自己的缓存界面
	 */
	@Override
	public Class<? extends Activity> getCachingActivityClass() {
		// TODO Auto-generated method stub
		return CachingActivity.class;
	}
	
	/**
	 * 通过覆写该方法，返回“已经缓存视频信息的界面”，
	 * 则在状态栏点击下载信息时可以自动跳转到所设定的界面.
	 * 用户需要定义自己的已缓存界面
	 */
	
	@Override
	public Class<? extends Activity> getCachedActivityClass() {
		// TODO Auto-generated method stub
		return CachedActivity.class;
	}

	
	

}
