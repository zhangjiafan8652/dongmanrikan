package com.jiafan.qirenjongbao.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class IntentUtils {
	/**
	 * 传入context和class名可以跳转activity
	 * 
	 * @param context
	 * @param cls
	 */
	public static void startActivity(Activity mactivity, Class<?> cls) {
		Intent intent = new Intent(mactivity.getApplicationContext(), cls);
		mactivity.startActivity(intent);
	}

	/**
	 * 传入context和class可以跳转activity并且关闭当前activity
	 * 
	 * @param context
	 * @param cls
	 */
	public static void startActivityAndFinish(Activity context, Class<?> cls) {
		Intent intent = new Intent(context, cls);
		context.startActivity(intent);
		context.finish();
	}

	/**
	 * 延迟跳转activity
	 * 
	 * @param context
	 * @param cls
	 * @param delaytime
	 */
	public static void startActivityForDelay(final Activity context,
			final Class<?> cls, final long delaytime) {
		new Thread() {
			public void run() {
				try {
					Thread.sleep(delaytime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Intent intent = new Intent(context, cls);
				context.startActivity(intent);
			};
		}.start();
	}

	/**
	 * 延迟跳转activity并且关闭当前activity
	 * 
	 * @param context
	 * @param cls
	 * @param delaytime
	 */
	public static void startActivityForDelayAndFinish(final Activity context,
			final Class<?> cls, final long delaytime) {
		new Thread() {
			public void run() {
				try {
					Thread.sleep(delaytime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Intent intent = new Intent(context, cls);
				context.startActivity(intent);
				context.finish();
			};
		}.start();
	}
}
