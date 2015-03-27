package com.jiafan.qirenjongbao.newscenterimpl;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jiafan.qirenjongbao.MenuDetailBasePager;
import com.jiafan.qirenjongbao.NewsDetailUI;
import com.jiafan.qirenjongbao.R;
import com.jiafan.qirenjongbao.domain.NewsCenterBean.ChildRen;
import com.jiafan.qirenjongbao.domain.TabDetailBean;
import com.jiafan.qirenjongbao.domain.TabDetailBean.News;
import com.jiafan.qirenjongbao.domain.TabDetailBean.TopNew;
import com.jiafan.qirenjongbao.utils.CacheUtils;
import com.jiafan.qirenjongbao.utils.Constants;
import com.jiafan.qirenjongbao.view.HorizontalScrollViewPager;
import com.jiafan.qirenjongbao.view.RefreshListView;
import com.jiafan.qirenjongbao.view.RefreshListView.OnRefreshListener;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;


/**
 * @author jiafan
 * 新闻菜单-页签详情页
 */
public class NewsMenuTabDetailPager extends MenuDetailBasePager implements OnPageChangeListener, 
	OnRefreshListener, OnItemClickListener {

	@ViewInject(R.id.hsvp_news_menu_tab_detail_top_news)
	private HorizontalScrollViewPager mViewPager;
	
	@ViewInject(R.id.tv_news_menu_tab_detail_description)
	private TextView tvDescription;
	
	@ViewInject(R.id.ll_news_menu_tab_detail_point_group)
	private LinearLayout llPointGroup;
	
	@ViewInject(R.id.rlv_news_menu_tab_detail_list_news)
	private RefreshListView mListView;

	private ChildRen mChildRen; // 当前页签详情页面的数据.
 
	private String url; // 当前页面的url
	
	private List<TopNew> topNewList; // 顶部轮播图新闻的数据

	private BitmapUtils bitmapUtils; // 图片访问框架
	
	private int previousEnabledPosition; // 前一个选中点的索引
	
	private InternalHandler mHandler;

	private List<News> newsList; // 列表新闻的数据

	private HttpUtils httpUtils;

	private TopNewAdapter topNewAdapter; // 顶部轮播图的适配器

	private NewsAdapter newsAdapter; // 列表新闻的适配器

	private String moreUrl; // 更多数据的url
	
	// 读过的新闻id数组
	private final String READ_NEWS_ID_ARRAY_KEY = "read_news_id_array";
	
	public NewsMenuTabDetailPager(Context context) {
		super(context);
	}

	public NewsMenuTabDetailPager(Context context, ChildRen childRen) {
		super(context);
		
		this.mChildRen = childRen;
		
		bitmapUtils = new BitmapUtils(mContext);
		bitmapUtils.configDefaultBitmapConfig(Config.ARGB_4444);
	}

	@Override
	public View initView() {
		View view = View.inflate(mContext, R.layout.news_menu_tab_detail, null);
		ViewUtils.inject(this, view); // 把view注入到xutils框架中
		
		View topNewsView = View.inflate(mContext, R.layout.news_menu_tab_detail_topnews, null);
		ViewUtils.inject(this, topNewsView); // 把view注入到xutils框架中
		
		// 在ListView的第0个条目的上面添加一个布局.
		// mListView.addHeaderView(topNewsView);
		mListView.addCustomHeaderView(topNewsView);
		mListView.isEnabledPullDownRefresh(true); // 启用下拉刷新
		mListView.isEnabledLoadingMore(true); // 启用下拉刷新
		mListView.setOnRefreshListener(this);
		mListView.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void initData() {
		url = Constants.SERVICE_URL + mChildRen.url;
//		System.out.println(mChildRen.title + "的数据连接地址: " + url);
		
		String json = CacheUtils.getString(mContext, url, null);
		if(!TextUtils.isEmpty(json)) {
			processData(json);
		}
		
		getDataFromNet();
	}

	/**
	 * 从网路获取数据
	 */
	private void getDataFromNet() {
		httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				System.out.println(mChildRen.title + "数据请求成功了: " + responseInfo.result);
				
				// 把数据存储起来
				CacheUtils.putString(mContext, url, responseInfo.result);
			//	System.out.println("我要初始化图像了");
				processData(responseInfo.result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				System.out.println(mChildRen.title + "数据请求失败了: " + msg);
			}
		});
	}
	
	/**
	 * 解析json的数据, 封装成一个bean, 返回.
	 * @param json
	 * @return
	 */
	private TabDetailBean parserJSON(String json) {
		Gson gson = new Gson();
		TabDetailBean bean = gson.fromJson(json, TabDetailBean.class);
		moreUrl = bean.data.more;
		if(!TextUtils.isEmpty(moreUrl)) {
			moreUrl = Constants.SERVICE_URL + moreUrl;
		}
		return bean;
	}

	/**
	 * 解析并处理数据
	 * @param result
	 */
	protected void processData(String result) {
		TabDetailBean bean = parserJSON(result);
		
		// 初始化顶部新闻的数据
		topNewList = bean.data.topnews;
		if(topNewAdapter == null) {
			topNewAdapter = new TopNewAdapter();
			mViewPager.setAdapter(topNewAdapter);
			mViewPager.setOnPageChangeListener(this);
		} else {
			topNewAdapter.notifyDataSetChanged();
		}
		
		// 初始化图片的描述和点
		llPointGroup.removeAllViews();
		for (int i = 0; i < topNewList.size(); i++) {
			View view = new View(mContext);
			view.setBackgroundResource(R.drawable.tab_detail_top_news_point_bg);
			LayoutParams params = new LayoutParams(5, 5);
			if(i != 0) {
				params.leftMargin = 10;
			}
			view.setLayoutParams(params);
			view.setEnabled(false);
			llPointGroup.addView(view);
		}
		previousEnabledPosition = 0;
		tvDescription.setText(topNewList.get(previousEnabledPosition).title);
		llPointGroup.getChildAt(previousEnabledPosition).setEnabled(true);
		
		// 动态的让轮播图切换起来.
		/**
		 * -> 1.使用handler执行一个延时任务: postDelayed
		 * -> 2.任务类runnable的run方法会被执行
		 * -> 3.使用handler发送一个消息 
		 * -> 4.Handler类的handleMessage方法接收到消息.
		 * -> 5.在handleMessage方法中, 把ViewPager的页面切换到下一个, 同时:1.使用handler执行一个延时任务 
		 */
		if(mHandler == null) {
			mHandler = new InternalHandler();
		}
		
		mHandler.removeCallbacksAndMessages(null); // 把所有的消息和任务清空
		mHandler.postDelayed(new AutoSwitchPagerRunnable(), 4000);
		
		// 初始化列表新闻的数据
		newsList = bean.data.news;
		if(newsAdapter == null) {
			newsAdapter = new NewsAdapter();
			mListView.setAdapter(newsAdapter);
		} else {
			newsAdapter.notifyDataSetChanged();
		}
	}
	
	class NewsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return newsList.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			NewsViewHolder mHolder = null;
			if(convertView == null) {
				convertView = View.inflate(mContext, R.layout.news_menu_tab_detail_news_item, null);
				mHolder = new NewsViewHolder();
				mHolder.ivImage = (ImageView) convertView.findViewById(R.id.iv_news_menu_tab_detail_news_item_image);
				mHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_news_menu_tab_detail_news_item_title);
				mHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_news_menu_tab_detail_news_item_date);
				convertView.setTag(mHolder);
			} else {
				mHolder = (NewsViewHolder) convertView.getTag();
			}
			
			// 给控件赋值.
			News news = newsList.get(position);
			mHolder.tvTitle.setText(news.title);
			mHolder.tvDate.setText(news.pubdate);
			BitmapUtils bitmapUtils2 = new BitmapUtils(mContext);
			bitmapUtils2.display(mHolder.ivImage, news.listimage);
			
			// 判断当前这条新闻是否已经读过
			String readIDArray = CacheUtils.getString(mContext, READ_NEWS_ID_ARRAY_KEY, null);
			if(!TextUtils.isEmpty(readIDArray)
					&& readIDArray.contains(news.id)) {
				mHolder.tvTitle.setTextColor(Color.GRAY);
			} else {
				mHolder.tvTitle.setTextColor(Color.BLACK);
			}
			
			return convertView;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}
	
	class NewsViewHolder {
		
		public ImageView ivImage;
		public TextView tvTitle;
		public TextView tvDate;
	}
	
	class TopNewAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return topNewList.size();
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
			ImageView iv = new ImageView(mContext);
			iv.setScaleType(ScaleType.FIT_XY);
			iv.setImageResource(R.drawable.edefault);
			iv.setOnTouchListener(new TopNewItemTouchListener());
			
			String topImageUrl = topNewList.get(position).topimage;
			/**
			 * 第一个参数是 把图片将要显示在哪一个控件上: iv.setImageBitmap
			 * 第二个参数是 图片的url地址
			 */
			//System.out.println("这里是bitmaputils初始化数据"+topImageUrl);
			BitmapUtils bitmapUtils3 = new BitmapUtils(mContext);
			bitmapUtils3.display(iv, topImageUrl);
			
			container.addView(iv);
			return iv;
		}
	}
	
	class TopNewItemTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				System.out.println("停止播放");
				mHandler.removeCallbacksAndMessages(null);
				break;
			case MotionEvent.ACTION_UP:
				System.out.println("开始播放");
				mHandler.postDelayed(new AutoSwitchPagerRunnable(), 4000);
				break;
			default:
				break;
			}
			return true;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int position) {
		llPointGroup.getChildAt(previousEnabledPosition).setEnabled(false);
		llPointGroup.getChildAt(position).setEnabled(true);
		tvDescription.setText(topNewList.get(position).title);
		previousEnabledPosition = position;
	}
	
	/**
	 * @author jiafan
	 * 内部的handler
	 */
	class InternalHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
//			System.out.println("轮播图切换页面了");
			int currentItem = mViewPager.getCurrentItem() + 1;
			mViewPager.setCurrentItem(currentItem % topNewList.size());
			
			mHandler.postDelayed(new AutoSwitchPagerRunnable(), 4000);
		}
	}
	
	/**
	 * @author jiafan
	 * 自动切换页面任务类
	 */
	class AutoSwitchPagerRunnable implements Runnable {

		@Override
		public void run() {
			mHandler.obtainMessage().sendToTarget();
		}
	}

	@Override
	public void onPullDownRefresh() {
		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				mListView.onRefreshFinish(); // 刷新完成把头隐藏了.
			//	System.out.println(mChildRen.title + "数据加载成功: " + responseInfo.result);
				
				CacheUtils.putString(mContext, url, responseInfo.result);
				
				
				processData(responseInfo.result);
				
				Toast.makeText(mContext, "下拉刷新完成", 0).show();
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				mListView.onRefreshFinish(); // 刷新完成把头隐藏了.
				//System.out.println(mChildRen.title + "数据加载失败: " + msg);
			}
		});
	}

	@Override
	public void onLoadingMore() {
		// 查看是否还有更多数据
		if(TextUtils.isEmpty(moreUrl)) {
			mListView.onRefreshFinish(); // 把脚布局隐藏
			Toast.makeText(mContext, "没有更多数据了", 0).show();
		} else {
			// 加载更多数据
			httpUtils.send(HttpMethod.GET, moreUrl, new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					mListView.onRefreshFinish(); // 把脚布局隐藏
					Toast.makeText(mContext, "加载更多成功", 0).show();
					
					// 把最新加载出来的数据, 添加到newsList集合中. 刷新ListView
					TabDetailBean bean = parserJSON(responseInfo.result);
					newsList.addAll(bean.data.news);
					newsAdapter.notifyDataSetChanged();
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					mListView.onRefreshFinish(); // 把脚布局隐藏
					Toast.makeText(mContext, "加载更多失败", 0).show();
				}
			});
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
//		System.out.println("position: " + position);
		
		News news = newsList.get(position -1);
		// 把当前被点击的这条新闻的id存起来 1#2#3#4
		
		String readNewsIDArray = CacheUtils.getString(mContext, READ_NEWS_ID_ARRAY_KEY, null);
		
		String idArray = null;
		if(TextUtils.isEmpty(readNewsIDArray)) {
			idArray = news.id;
		} else {
			idArray = readNewsIDArray + "#" + news.id;
		}
		CacheUtils.putString(mContext, READ_NEWS_ID_ARRAY_KEY, idArray);
		
		// 通知ListView刷新
		newsAdapter.notifyDataSetChanged();
		
		Intent intent = new Intent(mContext, NewsDetailUI.class);
		intent.putExtra("url", news.url);
		mContext.startActivity(intent);
	}
}