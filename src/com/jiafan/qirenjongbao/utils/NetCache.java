package com.jiafan.qirenjongbao.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

/**
 * @author jiafan
 * 网络缓存类
 */
public class NetCache {

	public static final int SUCCESS = 0;
	public static final int FAILED = 1;
	private Handler mHandler; 
	private MemoryCache mMemoryCache; // 内存缓存对象
	private ExecutorService mExecutorService; // 线程池对象

	public NetCache(Handler handler, MemoryCache memoryCache) {
		this.mHandler = handler;
		this.mMemoryCache = memoryCache;
		
		// 构建一个内部有5个线程的线程池
		mExecutorService = Executors.newFixedThreadPool(5);
	}
	
	/**
	 * 获取图片从网络中
	 * @param url
	 */
	public void getBitmapFromNet(String url, int tag) {
//		new Thread(new InternalRunnable(url, tag)).start();
		mExecutorService.execute(new InternalRunnable(url, tag));
	}
	
	class InternalRunnable implements Runnable {
		
		private String url; // 当前任务需要请求的网络地址
		private int tag; // 当前这次请求的图片的标识
		
		public InternalRunnable(String url, int tag) {
			this.url = url;
			this.tag = tag;
		}

		@Override
		public void run() {
			// 访问网络, 抓取图片
			HttpURLConnection conn = null;
			try {
				conn = (HttpURLConnection) new URL(url).openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(5000);
				conn.setReadTimeout(5000);
				conn.connect();
				int responseCode = conn.getResponseCode();
				if(responseCode == 200) {
					InputStream is = conn.getInputStream();
					// 把流转换成图片
					Bitmap bm = BitmapFactory.decodeStream(is);
					
					Message msg = mHandler.obtainMessage();
					msg.obj = bm;
					msg.arg1 = tag;
					msg.what = SUCCESS;
					msg.sendToTarget();
					
					// 向本地存一份
					LocalCache.putBitmap(url, bm);
					
					// 向内存存一份
					mMemoryCache.putBitmap(url, bm);
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(conn != null) {
					conn.disconnect(); // 断开连接
				}
			}
			mHandler.obtainMessage(FAILED).sendToTarget();
		}
	}
}