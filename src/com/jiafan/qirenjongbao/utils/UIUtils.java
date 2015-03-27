package com.jiafan.qirenjongbao.utils;

import com.jiafan.qirenjongbao.application.Myaplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;


public class UIUtils {

	
	public static Context getContext() {
		return Myaplication.getApplication();
	}

	public static Thread getMainThread() {
		return Myaplication.getMainThread();
	}

	public static long getMainThreadId() {
		return Myaplication.getMainThreadId();
	}
	
	static ProgressDialog progressDlg = null;

	/** dip转换px */
	public static int dip2px(int dip) {
		final float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}

	/** pxz转换dip */
	public static int px2dip(int px) {
		final float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	/** 获取主线程的handler */
	public static Handler getHandler() {
		return Myaplication.getMainThreadHandler();
	}

	/** 延时在主线程执行runnable */
	public static boolean postDelayed(Runnable runnable, long delayMillis) {
		return getHandler().postDelayed(runnable, delayMillis);
	}

	/** 在主线程执行runnable */
	public static boolean post(Runnable runnable) {
		return getHandler().post(runnable);
	}

	/** 从主线程looper里面移除runnable */
	public static void removeCallbacks(Runnable runnable) {
		getHandler().removeCallbacks(runnable);
	}

	public static View inflate(int resId){
		return LayoutInflater.from(getContext()).inflate(resId,null);
	}

	/** 获取资源 */
	public static Resources getResources() {
		
		return getContext().getResources();
	}

	/** 获取文字 */
	public static String getString(int resId) {
		return getResources().getString(resId);
	}

	/** 获取文字数组 */
	public static String[] getStringArray(int resId) {
		return getResources().getStringArray(resId);
	}

	/** 获取dimen */
	public static int getDimens(int resId) {
		return getResources().getDimensionPixelSize(resId);
	}

	/** 获取drawable */
	public static Drawable getDrawable(int resId) {
		return getResources().getDrawable(resId);
	}

	/** 获取颜色 */
	public static int getColor(int resId) {
		return getResources().getColor(resId);
	}

	/** 获取颜色选择器 */
	public static ColorStateList getColorStateList(int resId) {
		return getResources().getColorStateList(resId);
	}
	//判断当前的线程是不是在主线程 
	public static boolean isRunInMainThread() {
		return android.os.Process.myTid() == getMainThreadId();
	}
    
	public static void runInMainThread(Runnable runnable) {
		if (isRunInMainThread()) {
			runnable.run();
		} else {
			post(runnable);
		}
	}

	public static void startActivity(Intent intent){
//		BaseActivity activity = BaseActivity.getForegroundActivity();
//		if(activity != null){
//			activity.startActivity(intent);
//		}else{
//			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			getContext().startActivity(intent);
//		}
	}

	/** 对toast的简易封装。线程安全，可以在非UI线程调用。 */
	public static void showToastSafe(final int resId) {
		showToastSafe(getString(resId));
	}

	/** 对toast的简易封装。线程安全，可以在非UI线程调用。 */
	public static void showToastSafe(final String str) {
		if (isRunInMainThread()) {
			showToast(str);
		} else {
			post(new Runnable() {
				@Override
				public void run() {
					showToast(str);
				}
			});
		}
	}

	private static void showToast(String str) {
		Context frontActivity = Myaplication.getApplication();
		if (frontActivity != null) {
			Toast.makeText(frontActivity, str, 0).show();
		}
	}
	
	
	/** 对progress等待进度条的简易封装。线程安全，可以在非UI线程调用。 */
	public static void showProgresssafe(final String str,final Context con) {
		if (isRunInMainThread()) {
			showProgress(str,con);
		} else {
			post(new Runnable() {
				@Override
				public void run() {
					showProgress(str,con);
				}
			});
		}
	}

	private static void showProgress(String str,Context con) {
		Context frontActivity = Myaplication.getApplication();
		if (frontActivity != null) {
			showProgressDlg(str, con);
		}
	}
	
	/** 对progress等待进度条停止的简易封装。线程安全，可以在非UI线程调用。 */
	public static void stopProgresssafe() {
		if (isRunInMainThread()) {
			stopProgress();
		} else {
			post(new Runnable() {
				@Override
				public void run() {
					stopProgress();
				}
			});
		}
	}

	private static void stopProgress() {
		Context frontActivity = Myaplication.getApplication();
		if (frontActivity != null) {
			stopProgressDlg();
		}
	}
	
	 /**
     * 启动进度条
     *
     * @param strMessage
     *            进度条显示的信息
     * @param activity
     *            当前的activity
     */
    private static void showProgressDlg(String strMessage, Context ctx) {
  
    	stopProgressDlg();
        if (null == progressDlg) {
            progressDlg = new ProgressDialog(ctx);
            //设置进度条样式
            progressDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //提示的消息
            progressDlg.setMessage(strMessage);
            progressDlg.setIndeterminate(false);
            progressDlg.setCancelable(false);
            progressDlg.show();
        }
    }
  
    /**
     * 结束进度条
     */
    private static void stopProgressDlg() {
        if (null != progressDlg) {
            progressDlg.dismiss();
            progressDlg = null;
        }
    }
	
}
