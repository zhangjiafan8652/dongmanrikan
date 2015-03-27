package com.jiafan.qirenjongbao.newscenterimpl;

import in.srain.cube.image.CubeImageView;
import in.srain.cube.image.ImageLoader;
import in.srain.cube.image.ImageLoaderFactory;

import java.util.LinkedList;

import com.google.gson.Gson;
import com.jiafan.qirenjongbao.MenuDetailBasePager;
import com.jiafan.qirenjongbao.R;
import com.jiafan.qirenjongbao.application.MyConstants;
import com.jiafan.qirenjongbao.domain.VedioDetailRespose;
import com.jiafan.qirenjongbao.domain.VedioResponse;
import com.jiafan.qirenjongbao.domain.Vediosbean;
import com.jiafan.qirenjongbao.domain.NewsCenterBean.ChildRen;
import com.jiafan.qirenjongbao.newscenterimpl.Gameshipin.Myholder;
import com.jiafan.qirenjongbao.newscenterimpl.Gameshipin.VedioAdapter;
import com.jiafan.qirenjongbao.view.RefreshListView;
import com.jiafan.qirenjongbao.view.RefreshListView.OnRefreshListener;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;

public class Daomeixiong extends MenuDetailBasePager {

	private ChildRen children;

	private String vediourl;

	private LinearLayout view;

	private RefreshListView rl_listview;

	private String vediodetailurl = "https://openapi.youku.com/v2/videos/show_batch.json";

	private LinkedList<Vediosbean> vedios;

	private VedioAdapter vedioAdapter;

	private String moreurl;

	public Daomeixiong(Context context, ChildRen children) {
		super(context);
		// TODO Auto-generated constructor stub
		this.children = children;
	}

	// XNTI0MjA0NDU2
	// XNzQ0NDIyMjM2
	// XNTk4OTM5OTQ4
	// XNjAwNjA2ODUy
	// XODc0MTU2MzM2
	// XNzMxMDczNjIw

	@Override
	public View initView() {
		// System.out.println("我测试活的的的"+children.url);

		view = (LinearLayout) View.inflate(mContext, R.layout.myshipin, null);
		rl_listview = (RefreshListView) view
				.findViewById(R.id.rflv_gamesshipin_listview);

		rl_listview.isEnabledLoadingMore(true);

		rl_listview.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onPullDownRefresh() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLoadingMore() {
				// TODO Auto-generated method stub
				if (moreurl != null) {
					getVedioid(moreurl);
				}

			}
		});

		return view;
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();

		// vediourl=MyConstants.URL+children.url;

		// System.out.println("我测试活的的的"+vediourl);

		vedios = new LinkedList<Vediosbean>();
		vedioAdapter = new VedioAdapter();

		rl_listview.setAdapter(vedioAdapter);

		rl_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent i = new Intent(mContext,
						com.jiafan.qirenjongbao.youku.PlayerActivity.class);
				i.putExtra("vid", vedios.get(position - 1).getId());
				mContext.startActivity(i);
			}
		});
		getVedioid(children.url);
	}

	private void getVedioid(String url) {
		// TODO Auto-generated method stub
		HttpUtils httpUtils = new HttpUtils();

		vediourl = MyConstants.URL + url;
		httpUtils.send(HttpMethod.GET, vediourl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				VedioResponse fromJson = gson.fromJson(responseInfo.result,
						VedioResponse.class);
				System.out.println(fromJson.toString());
				rl_listview.onRefreshFinish();
				stardata(fromJson);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				System.out.println("我是拿视频的东东" + msg);
				rl_listview.onRefreshFinish();
				Toast.makeText(mContext, "加载更多失败~！", 0).show();
			}
		});

	}

	// https://openapi.youku.com/v2/videos/show.json?client_id=e034072028e19a1b&video_id=XNzQ0NDIyMjM2
	// https://openapi.youku.com/v2/videos/show_basic_batch.json?client_id=e034072028e19a1b&video_ids=XNTI0MjA0NDU2
	private void stardata(VedioResponse fromJson) {
		// TODO Auto-generated method stub
		HttpUtils httpUtils = new HttpUtils();
		StringBuffer sb = new StringBuffer();
		moreurl = fromJson.getMore();

		sb.append(fromJson.getVediolist().get(0));
		for (int i = 1; i < fromJson.getVediolist().size(); i++) {
			sb.append("," + fromJson.getVediolist().get(i));

		}
		vediodetailurl = vediodetailurl
				+ "?client_id=e034072028e19a1b&video_ids=" + sb.toString();
		// System.out.println("我是sb 啊啊啊啊啊啊"+sb.toString());
		RequestParams params = new RequestParams();
		// params.addBodyParameter("client_id", "e034072028e19a1b");
		// params.addBodyParameter("video_ids", "XNTI0MjA0NDU2,XNzQ0NDIyMjM2");
		httpUtils.send(HttpMethod.GET, vediodetailurl,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// TODO Auto-generated method stub
						System.out.println("我得到的是视频详情" + responseInfo.result);

						Gson gson = new Gson();
						VedioDetailRespose fromJson2 = gson.fromJson(
								responseInfo.result, VedioDetailRespose.class);
						System.out.println(fromJson2.toString());
						vedios.addAll(fromJson2.getVideos());
						vedioAdapter.notifyDataSetChanged();
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// TODO Auto-generated method stub
						System.out.println("得到的错误视频详情" + error + msg);
					}
				});

	}

	class VedioAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return vedios.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view;
			Myholder myholder = new Myholder();
			ImageLoader imageloader = ImageLoaderFactory.create(mContext);
			if (convertView != null) {
				view = convertView;
				myholder = (Myholder) view.getTag();
			} else {
				view = View.inflate(mContext, R.layout.myshipin_item, null);
				view.setTag(myholder);
				myholder.iv_gameshipin_vedioimage = (CubeImageView) view
						.findViewById(R.id.iv_gameshipin_item);
				myholder.tv_gameshipin_vediotextview = (TextView) view
						.findViewById(R.id.tv_gameshipin_discrtion);
			}

			int screenwidth = MyConstants.screenWidth;
			int vedioimageheigt = (screenwidth / 45) * 25;
			LayoutParams layoutParams = new LayoutParams(screenwidth,
					vedioimageheigt);
			myholder.iv_gameshipin_vedioimage.setLayoutParams(layoutParams);
			myholder.iv_gameshipin_vedioimage.loadImage(imageloader, vedios
					.get(position).getBigThumbnail());
			myholder.tv_gameshipin_vediotextview.setText(vedios.get(position)
					.getTitle());
			return view;
		}

	}

	class Myholder {
		CubeImageView iv_gameshipin_vedioimage;
		TextView tv_gameshipin_vediotextview;
	}

}
