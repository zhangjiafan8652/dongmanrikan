package com.jiafan.qirenjongbao;

import java.util.LinkedList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.ValueEventListener;

import com.google.gson.Gson;
import com.jiafan.qirenjongbao.application.MyConstants;
import com.jiafan.qirenjongbao.domain.DiscussNotifyResponse;
import com.jiafan.qirenjongbao.domain.Newsdiscussbean;
import com.jiafan.qirenjongbao.domain.Newsdisscussresponse;
import com.jiafan.qirenjongbao.domain.Nnewsdetail;
import com.jiafan.qirenjongbao.utils.UIUtils;
import com.jiafan.qirenjongbao.view.RatioLayout;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.meg7.widget.CircleImageView;

public class NewsDetailUI extends Activity {

	private String url;
	private LinearLayout ll_news_detail;
	private ListView lv_news_detail;
	private LinearLayout ll_news_textandimager;
	private TextView textView;
	private ImageView imageView;
	private TextView textView2;
	private ProgressBar pb;
	private RatioLayout ratioLayout;
	private TextView title;
	private TextView time;
	private EditText ed_discuss;
	private String discussneirong;
	//private ArrayList<Newsdiscussbean> discussList;
	private int STATE = 1;
	private int LISTVIEWONE = 1;
	private int LISTVIEWTWO = 2;
	private DiscusslistAdapter discusslistAdapter;
	
	private String newsname;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				discusslistAdapter = new DiscusslistAdapter();

				lv_news_detail.setAdapter(discusslistAdapter);
				break;
				
			case 2:
				System.out.println("我是来过这里的额");
				//discusslistAdapter.notifyDataSetChanged();
				discusslistAdapter.notifyDataSetChanged();
				/*discusslistAdapter = new DiscusslistAdapter();

				lv_news_detail.setAdapter(discusslistAdapter);*/
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	private Nnewsdetail nnewsdetail;
	private BmobRealTimeData rtd;
	private LinkedList<Newsdiscussbean> discussList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.copynews_detail);

		Intent intent = getIntent();
		url = intent.getStringExtra("url");
		// url = "http://10.0.2.2:8080";
		initView();
	}

	private void initView() {
		ll_news_detail = (LinearLayout) View.inflate(NewsDetailUI.this,
				R.layout.news_detial_head, null);

		lv_news_detail = (ListView) findViewById(R.id.lv_news_detail);

		title = (TextView) ll_news_detail
				.findViewById(R.id.tv_newsdetail_title);
		time = (TextView) ll_news_detail.findViewById(R.id.tv_newsdetail_time);
		pb = (ProgressBar) findViewById(R.id.pb_news_detail);
		ll_news_textandimager = (LinearLayout) ll_news_detail
				.findViewById(R.id.ll_news_textandimager);
		// scrollview = (ScrollView)
		// findViewById(R.id.sl_newdetail_scollerview);
		ed_discuss = (EditText) ll_news_detail
				.findViewById(R.id.ed_news_detail_discuss);

		initdata();
	}

	private void initdata() {
		HttpUtils hp = new HttpUtils();
		hp.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// System.out.println(responseInfo.result.toString());
				pb.setVisibility(View.GONE);
				lv_news_detail.setVisibility(View.VISIBLE);
				proseedata(responseInfo.result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				System.out.println("失败了吧");
			}
		});

	}

	private void proseedata(String json) {
		Gson gson = new Gson();

		nnewsdetail = gson.fromJson(json, Nnewsdetail.class);
		// System.out.println("++++++++++++++" + nnewsdetail.toString());

		title.setText(nnewsdetail.getTitil());
		time.setText(nnewsdetail.getTime());
		int fronttext = Integer.parseInt(nnewsdetail.getFronttextcount());
		int imagecount = Integer.parseInt(nnewsdetail.getImagecount());
		int aftertextcount = Integer.parseInt(nnewsdetail.getAftertextcount());
		for (int i = 0; i < fronttext; i++) {
			textView2 = new TextView(getApplicationContext());
			LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT);
			textView2.setTextSize(15);
			textView2.setLayoutParams(params);
			textView2.setTextColor(getResources().getColor(R.color.black));
			textView2.setText(nnewsdetail.getTextfront()[i]);
			ll_news_textandimager.addView(textView2);
		}

		for (int i = 0; i < imagecount; i++) {
			imageView = new ImageView(getApplicationContext());
			ratioLayout = new RatioLayout(getApplicationContext());
			ratioLayout.setRatio(150 / 210);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			ratioLayout.setLayoutParams(params);
			imageView.setScaleType(ScaleType.FIT_CENTER);
			BitmapUtils bitmapUtils = new BitmapUtils(getApplicationContext());
			System.out.println("+++++++++" + nnewsdetail.getImagerurl()[i]);
			bitmapUtils.display(imageView, nnewsdetail.getImagerurl()[i]);

			ll_news_textandimager.addView(ratioLayout);
			ratioLayout.addView(imageView);
		}
		for (int i = 0; i < aftertextcount; i++) {
			textView = new TextView(getApplicationContext());
			LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT);
			textView.setLayoutParams(params);
			textView.setTextSize(15);
			textView.setTextColor(getResources().getColor(R.color.black));
			textView.setText(nnewsdetail.getTextafter()[i]);
			ll_news_textandimager.addView(textView);
		}

		lv_news_detail.addHeaderView(ll_news_detail);

		initlistviewdata();
	}

	private void initlistviewdata() {
		discussList = new LinkedList<Newsdiscussbean>();
		//discussList = new linkedList<Newsdiscussbean>();
		newsname=nnewsdetail.getNewsname();
		String cloudCodeName = "finddiscuss";
		JSONObject params = new JSONObject();
		// name是上传到云端的参数名称，值是bmob，云端代码可以通过调用request.body.name获取这个值
		try {
			params.put("newsname", newsname);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// 创建云端代码对象
		AsyncCustomEndpoints cloudCode = new AsyncCustomEndpoints();
		// 异步调用云端代码
		cloudCode.callEndpoint(this, cloudCodeName, params,
				new CloudCodeListener() {

					@Override
					public void onSuccess(Object arg0) {
						// TODO Auto-generated method stub

						Gson gson = new Gson();

						String k = arg0.toString().replace("\\n", "");
						k = k.replace("\\", "");
						k = k.replace(" ", "");
						k = k.replace("\"[", "[");
						k = k.replace("]\"", "]");

						System.out.println(k);
						Newsdisscussresponse fromJson = gson.fromJson(k,
								Newsdisscussresponse.class);
						discussList = fromJson.getResults();
						//System.out.println("我是第一条的objectid"+ discussList2.get(0).getObjectId());
						
						sendmessage();
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						// System.out.println(arg1+"ssssssssssssssss");
						sendmessage();
					}
				});

		/*
		 * BmobQuery<Newsdiscussbean> query = new BmobQuery<Newsdiscussbean>();
		 * 
		 * query.addWhereNotEqualTo("newsname", "2015");
		 * 
		 * query.findObjects(this, new FindCallback() {
		 * 
		 * @Override public void onSuccess(JSONArray arg0) { // TODO
		 * Auto-generated method stub System.out.println(arg0 +
		 * "wowowoooooooooooooooooooo");
		 * 
		 * Gson gson = new Gson(); discussList = gson.fromJson(arg0.toString(),
		 * new TypeToken<ArrayList<Newsdiscussbean>>() { }.getType());
		 * 
		 * sendmessage(); }
		 * 
		 * @Override public void onFailure(int arg0, String arg1) { // TODO
		 * Auto-generated method stub System.out.println(arg1
		 * +"newsname"+nnewsdetail.getNewsname()+"失败了！！！！！！！！！！！！！！");
		 * 
		 * sendmessage(); } });
		 */
		/*
		 * BmobQuery<Newsdiscussbean> query = new BmobQuery<Newsdiscussbean>();
		 * query.setLimit(10); query.order("-createdAt");
		 * query.findObjects(getApplicationContext(), new
		 * FindListener<Newsdiscussbean>() {
		 * 
		 * @Override public void onSuccess(List<Newsdiscussbean> arg0) {
		 * discussList = new ArrayList<Newsdiscussbean>(); discussList =
		 * (ArrayList<Newsdiscussbean>) arg0; for (int i = 0; i < arg0.size();
		 * i++) { System.out.println(arg0.get(i).toString()); }
		 * 
		 * sendmessage();
		 * 
		 * }
		 * 
		 * @Override public void onError(int arg0, String arg1) { // TODO
		 * Auto-generated method stub System.out.println("失败了~！"); } });
		 */

	}

	private void sendmessage() {
		if (STATE == LISTVIEWONE) {
			handler.sendEmptyMessage(1);
			STATE = LISTVIEWTWO;
		} else {
			discusslistAdapter.notifyDataSetChanged();
		}

	}

	/**
	 * 关闭当前网页
	 * 
	 * @param view
	 */
	public void callback(View view) {
		finish();
	}

	/**
	 * 提交评论
	 * 
	 * @param view
	 */
	public void submitdiscuss(View view) {
		discussneirong = ed_discuss.getText().toString();
		
		if(MyConstants.username.equals("123456789")){
			Toast.makeText(this, "抱歉噢~！游客不能评论", 0).show();
			return;
		}
		
		if (TextUtils.isEmpty(discussneirong)) {
			Toast.makeText(getApplicationContext(), "评论内容不能为空噢~！亲", 0).show();

		} else {
			Newsdiscussbean newsdiscussbean = new Newsdiscussbean();
			newsdiscussbean.setNewsname(nnewsdetail.getNewsname());
			newsdiscussbean.setDiscuss(discussneirong);
			newsdiscussbean.setUserid(MyConstants.userid);
			newsdiscussbean.setUserimageurl(MyConstants.userheadurl);
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘

			ed_discuss.setHint("亲~！你还要评论吗？");
			newsdiscussbean.save(this, new SaveListener() {

				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "提交成功", 0).show();
					ed_discuss.setText("");
					//initlistviewdata();
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					System.out.println("++++++" + arg1 + "   " + arg0 + "   "
							+ nnewsdetail.getNewsname().substring(0, 8));
					Toast.makeText(getApplicationContext(), "提交失败", 0).show();
				}
			});
		}
	}

	class DiscusslistAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return discussList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View contentview, ViewGroup arg2) {
			// Holder holder = new Holder();
			View view;
			if (contentview != null) {
				view = contentview;
				// holder = (Holder) view.getTag();
			} else {
				view = View.inflate(getApplicationContext(),
						R.layout.newsdiscuss, null);

				// view.setTag(holder);
			}
			TextView tv_discussneirong = (TextView) view
					.findViewById(R.id.tv_newsdiscuss_discuss);
			TextView tv_discusstimelist = (TextView) view
					.findViewById(R.id.tv_newsdiscuss_time);
			CircleImageView headimage = (CircleImageView) view
					.findViewById(R.id.ci_newsdiscuss_headphoto);
			tv_discussneirong.setText(discussList.get(position).getDiscuss());

			BitmapUtils bitmapUtils = new BitmapUtils(getApplicationContext());

			bitmapUtils.display(headimage, discussList.get(position)
					.getUserimageurl());

			tv_discusstimelist
					.setText(discussList.get(position).getCreatedAt());

			return view;
		}

	}

	class Holder {
		TextView tv_discussneirong;
		TextView tv_discusstimelist;
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		rtd = new BmobRealTimeData();

		rtd.start(this, new ValueEventListener() {
			@Override
			public void onDataChange(JSONObject data) {
				// TODO Auto-generated method stub
				Log.d("bmob", "(" + data.optString("action") + ")" + "数据："
						+ data);
				Gson gson = new Gson();
				DiscussNotifyResponse fromJson = gson.fromJson(data.toString(), DiscussNotifyResponse.class);
				System.out.println(fromJson.getData().toString());
				dillwithnotifyData(fromJson.getData());
			}

			

			@Override
			public void onConnectCompleted() {
				// TODO Auto-generated method stub
				Log.d("bmob", "连接成功:" + rtd.isConnected());
				if (rtd.isConnected()) {
					// 监听表更新
					rtd.subTableUpdate("Newsdiscussbean");
				}
			}
		});

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(rtd!=null){
			rtd.unsubTableUpdate("Newsdiscussbean");
		}
		
	}

	
	private void dillwithnotifyData(Newsdiscussbean newsdiscussbean) {
		// TODO Auto-generated method stub
		System.out.println("我是外面的username"+newsname+"    newsdis"+newsdiscussbean.getNewsname());
		if(newsdiscussbean.getNewsname().equals(newsname)){
			System.out.println("我是里面是username"+newsname);
			discussList.addFirst(newsdiscussbean);
			handler.sendEmptyMessage(2);
		}
		
	}

	
	
}
