package com.jiafan.qirenjongbao;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jiafan.qirenjongbao.domain.NewsCenterBean;
import com.jiafan.qirenjongbao.newscenterimpl.NewsMenuDetailPager;
import com.jiafan.qirenjongbao.utils.CacheUtils;
import com.jiafan.qirenjongbao.utils.Constants;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @author jiafan 新闻中心的页面
 */
public class NewsCenterPager extends TabBasePager implements OnClickListener {

	private List<MenuDetailBasePager> pagerList; // 左侧菜单对应的页面
	private String json;

	public NewsCenterPager(Context context) {
		super(context);
	}

	// 父类已经初始化了布局。。上面一个头部局。下面一个帧布局
	@Override
	public void initData() {
		System.out.println("新闻中心数据初始化了.");
		tvTitle.setText("动漫日刊");
		ibMenu.setVisibility(View.VISIBLE);
		ibMenu.setOnClickListener(this);
		im_lanpangzhi.setOnClickListener(this);
		background.start();
		json = CacheUtils.getString(mContext, Constants.NEWSCENTER_URL,
				null);
		if (!TextUtils.isEmpty(json)) {
			processData(json); // 先把旧的新闻显示出来, 下面继续使用网络请求最新数据.
		}
		getDataFromNet();
	}

	/**
	 * 抓取数据
	 */
	private void getDataFromNet() {
		// 去服务端抓取数据.
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, Constants.NEWSCENTER_URL,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						System.out
								.println("新闻中心数据请求成功: " + responseInfo.result);

						// 把数据存储起来.http://zhangjiafan.oss-cn-shenzhen.aliyuncs.com/myjson.txt
						// Constants.NEWSCENTER_URL
						CacheUtils
								.putString(
										mContext,
										"http://zhangjiafan.oss-cn-shenzhen.aliyuncs.com/myjson.json",
										responseInfo.result);
						
						processData(responseInfo.result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						if(TextUtils.isEmpty(json)){
							background.stop();
							wanmingview.setVisibility(View.GONE);
							rl_faillianjie.setVisibility(View.VISIBLE);
						}
					}
				});
	}

	/**
	 * 解析和处理数据
	 * 
	 * @param result
	 */
	protected void processData(String result) {

		
		background.stop();
		wanmingview.setVisibility(View.GONE);
		Gson gson = new Gson();
		NewsCenterBean bean = gson.fromJson(result, NewsCenterBean.class);
	//	System.out.println(bean.toString());
		
		NewsMenuDetailPager newsMenuDetailPager = new NewsMenuDetailPager(mContext, bean.data.get(0));
		
		// 把pager中的view对象添加到FrameLayout中.
		View view = newsMenuDetailPager.getRootView();
		flContent.removeAllViews();
		flContent.addView(view);
		newsMenuDetailPager.initData();
		
	}

	@Override
	public void onClick(View v) {
		// 把左侧菜单收回去
		switch (v.getId()) {
		case R.id.ib_title_bar_menu:
			SlidingMenu slidingMenu = ((MainUI) mContext).getSlidingMenu();
			slidingMenu.toggle();
			break;
		case R.id.im_lanpangzhi:
			getDataFromNet();
			rl_faillianjie.setVisibility(View.GONE);
			wanmingview.setVisibility(View.VISIBLE);
			background.start();
			break;
		default:
			break;
		}
		
	}
}
