package com.jiafan.qirenjongbao.youku;

import java.util.LinkedList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.ValueEventListener;

import com.google.gson.Gson;
import com.jiafan.qirenjongbao.R;
import com.jiafan.qirenjongbao.application.MyConstants;
import com.jiafan.qirenjongbao.domain.DiscussNotifyResponse;
import com.jiafan.qirenjongbao.domain.Newsdiscussbean;
import com.jiafan.qirenjongbao.domain.VediodiscussNoty;
import com.jiafan.qirenjongbao.domain.Vediodiscussbean;
import com.jiafan.qirenjongbao.domain.Vediosdisscussresponse;
import com.lidroid.xutils.BitmapUtils;
import com.meg7.widget.CircleImageView;
import com.youku.player.ApiManager;
import com.youku.player.VideoQuality;
import com.youku.player.base.YoukuBasePlayerActivity;
import com.youku.player.base.YoukuPlayer;
import com.youku.player.base.YoukuPlayerView;
import com.youku.service.download.DownloadManager;
import com.youku.service.download.OnCreateDownloadListener;

/**
 * 播放器播放界面，需要继承自YoukuBasePlayerActivity基类
 * 
 */
public class PlayerActivity extends YoukuBasePlayerActivity {
	// 播放器控件
	private YoukuPlayerView mYoukuPlayerView;

	// 需要播放的视频id
	private String vid;

	// 清晰度相关按钮
	private Button btn_standard, btn_hight, btn_super, btn_1080;

	// 下载视频按钮
	private Button btn_download;

	// 需要播放的本地视频的id
	private String local_vid;

	// 标示是否播放的本地视频
	private boolean isFromLocal = false;

	private String id = "";

	// YoukuPlayer实例，进行视频播放控制
	private YoukuPlayer youkuPlayer;

	private LinkedList<Vediodiscussbean> discussList;

	private String vedio_id;

	private VedioslistAdapter discusslistAdapter;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				discusslistAdapter = new VedioslistAdapter();

				lv_youkuplayer_disslistview.setAdapter(discusslistAdapter);
				break;

			case 2:
				System.out.println("我是来过这里的额");
				// discusslistAdapter.notifyDataSetChanged();
				discusslistAdapter.notifyDataSetChanged();
				/*
				 * discusslistAdapter = new DiscusslistAdapter();
				 * 
				 * lv_news_detail.setAdapter(discusslistAdapter);
				 */
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.second);

		// 通过上个页面传递过来的Intent获取播放参数
		getIntentData(getIntent());
		iniView();

		if (TextUtils.isEmpty(id)) {
			vid = "XODQwMTY4NDg0"; // 默认视频
		} else {
			vid = id;
		}

		// 播放器控件
		mYoukuPlayerView = (YoukuPlayerView) this
				.findViewById(R.id.full_holder);

		// 初始化播放器相关数据
		mYoukuPlayerView.initialize(this);
		// mYoukuPlayerView.initialize(this,pid); //有pid的用户可以使用此接口配置pid参数

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);

		// 通过Intent获取播放需要的相关参数
		getIntentData(intent);

		// 进行播放
		goPlay();
	}

	/**
	 * 获取上个页面传递过来的数据
	 */
	private void getIntentData(Intent intent) {

		if (intent != null) {
			// 判断是不是本地视频
			isFromLocal = intent.getBooleanExtra("isFromLocal", false);

			if (isFromLocal) { // 播放本地视频
				local_vid = intent.getStringExtra("video_id");
			} else { // 在线播放
				id = intent.getStringExtra("vid");
			}
		}

	}

	@Override
	public void setPadHorizontalLayout() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitializationSuccess(YoukuPlayer player) {
		// TODO Auto-generated method stub
		// 初始化成功后需要添加该行代码
		addPlugins();

		// 实例化YoukuPlayer实例
		youkuPlayer = player;

		// 进行播放
		goPlay();
	}

	private void goPlay() {
		if (isFromLocal) { // 播放本地视频
			youkuPlayer.playLocalVideo(local_vid);
		} else { // 播放在线视频
			youkuPlayer.playVideo(vid);
		}

		// XNzQ3NjcyNDc2
		// XNzQ3ODU5OTgw
		// XNzUyMzkxMjE2
		// XNzU5MjMxMjcy 加密视频
		// XNzYxNzQ1MDAw 万万没想到
		// XNzgyODExNDY4 魔女范冰冰扑倒黄晓明
		// XNDcwNjUxNzcy 姐姐立正向前走
		// XNDY4MzM2MDE2 向着炮火前进
		// XODA2OTkwMDU2 卧底韦恩突出现 劫持案愈发棘手
		// XODUwODM2NTI0 会员视频
		// XODQwMTY4NDg0 一个人的武林
	}

	@Override
	public void onFullscreenListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSmallscreenListener() {
		// TODO Auto-generated method stub

	}

	private void iniView() {
		btn_standard = (Button) this.findViewById(R.id.biaoqing);
		btn_hight = (Button) this.findViewById(R.id.gaoqing);
		btn_super = (Button) this.findViewById(R.id.chaoqing);
		btn_1080 = (Button) this.findViewById(R.id.most);
		// btn_download = (Button)this.findViewById(R.id.download);

		ll_youkuplayer_disshead = (LinearLayout) View.inflate(
				PlayerActivity.this, R.layout.vedioplay_detial_head, null);
		et_youkuplayer_disscuss = (EditText) ll_youkuplayer_disshead
				.findViewById(R.id.ed_youkuplayer_discuss);

		lv_youkuplayer_disslistview = (ListView) findViewById(R.id.lv_youkuplayer_discuss);
		lv_youkuplayer_disslistview.addHeaderView(ll_youkuplayer_disshead);

		btn_standard.setOnClickListener(listener);
		btn_hight.setOnClickListener(listener);
		btn_super.setOnClickListener(listener);
		btn_1080.setOnClickListener(listener);
		// btn_download.setOnClickListener(listener);

		initlistviewdata();
	}

	private void initlistviewdata() {
		discussList = new LinkedList<Vediodiscussbean>();
		// discussList = new linkedList<Newsdiscussbean>();
		vedio_id = id;
		String cloudCodeName = "findvediodiscuss";
		JSONObject params = new JSONObject();
		// name是上传到云端的参数名称，值是bmob，云端代码可以通过调用request.body.name获取这个值
		try {
			params.put("vedio_id", vedio_id);
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
						System.out.println("我是arg0" + arg0.toString());
						Gson gson = new Gson();

						String k = arg0.toString().replace("\\n", "");
						k = k.replace("\\", "");
						k = k.replace(" ", "");
						k = k.replace("\"[", "[");
						k = k.replace("]\"", "]");

						System.out.println(k);
						Vediosdisscussresponse fromJson = gson.fromJson(k,
								Vediosdisscussresponse.class);
						discussList = fromJson.getResults();
						// System.out.println("我是第一条的objectid"+
						// discussList2.get(0).getObjectId());

						sendmessage();
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						// System.out.println(arg1+"ssssssssssssssss");
						sendmessage();
					}
				});

	}

	private void sendmessage() {
		// TODO Auto-generated method stub
		handler.sendEmptyMessage(1);

	}

	public View.OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch (view.getId()) {
			case R.id.biaoqing: // 切换标清
				change(VideoQuality.STANDARD);
				break;
			case R.id.gaoqing: // 切换高清
				change(VideoQuality.HIGHT);
				break;
			case R.id.chaoqing: // 切换超清
				change(VideoQuality.SUPER);
				break;
			case R.id.most: // 切换为1080P
				change(VideoQuality.P1080);
				break;
			/*
			 * case R.id.download: //下载视频接口测试 doDownload(); break;
			 */
			}

		}
	};

	private LinearLayout ll_youkuplayer_disshead;

	private EditText et_youkuplayer_disscuss;

	private ListView lv_youkuplayer_disslistview;

	private String discussneirong;

	private BmobRealTimeData rtd;

	/**
	 * 更改视频的清晰度
	 * 
	 * @param quality
	 *            VideoQuality有四种枚举值：{STANDARD,HIGHT,SUPER,P1080}，分别对应：标清，高清，超清，
	 *            1080P
	 */

	private void change(VideoQuality quality) {
		try {
			// 通过ApiManager实例更改清晰度设置，返回值（1):成功；（0): 不支持此清晰度
			// 接口详细信息可以参数使用文档
			int result = ApiManager.getInstance().changeVideoQuality(quality,
					PlayerActivity.this);
			if (result == 0)
				Toast.makeText(PlayerActivity.this, "不支持此清晰度", 2000).show();
		} catch (Exception e) {
			Toast.makeText(PlayerActivity.this, e.getMessage(), 2000).show();
		}
	}

	/**
	 * 简单展示下载接口的使用方法，用户可以根据自己的 通过DownloadManager下载视频
	 */
	private void doDownload() {
		// 通过DownloadManager类实现视频下载
		DownloadManager d = DownloadManager.getInstance();
		/**
		 * 第一个参数为需要下载的视频id 第二个参数为该视频的标题title 第三个对下载视频结束的监听，可以为空null
		 */
		d.createDownload("XNzgyODExNDY4", "魔女范冰冰扑倒黄晓明",
				new OnCreateDownloadListener() {

					@Override
					public void onfinish(boolean isNeedRefresh) {
						// TODO Auto-generated method stub

					}
				});
	}

	/**
	 * 提交评论
	 * 
	 * @param view
	 */
	public void submitdiscuss(View view) {
		discussneirong = et_youkuplayer_disscuss.getText().toString();
		if(MyConstants.username.equals("123456789")){
			Toast.makeText(this, "抱歉噢~！游客不能评论", 0).show();
			return;
		}
		
		if (TextUtils.isEmpty(discussneirong)) {
			Toast.makeText(getApplicationContext(), "评论内容不能为空噢~！亲", 0).show();

		} else {
			Vediodiscussbean vediosdiscussbean = new Vediodiscussbean();
			vediosdiscussbean.setVedio_id(vedio_id);
			vediosdiscussbean.setDiscuss(discussneirong);
			vediosdiscussbean.setUser_id(MyConstants.userid);
			vediosdiscussbean.setUserimageurl(MyConstants.userheadurl);
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘

			et_youkuplayer_disscuss.setHint("亲~！你还要评论吗？");
			vediosdiscussbean.save(this, new SaveListener() {

				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "提交成功", 0).show();
					et_youkuplayer_disscuss.setText("");
					// initlistviewdata();
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub

					Toast.makeText(getApplicationContext(), "提交失败", 0).show();
				}
			});
		}
	}

	class VedioslistAdapter extends BaseAdapter {

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
				VediodiscussNoty fromJson = gson.fromJson(data.toString(),
						VediodiscussNoty.class);
				System.out.println(fromJson.getData().toString());
				dillwithnotifyData(fromJson.getData());
			}

			@Override
			public void onConnectCompleted() {
				// TODO Auto-generated method stub
				Log.d("bmob", "连接成功:" + rtd.isConnected());
				if (rtd.isConnected()) {
					// 监听表更新
					rtd.subTableUpdate("Vediodiscussbean");
				}
			}
		});

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		rtd.unsubTableUpdate("Newsdiscussbean");
	}

	private void dillwithnotifyData(Vediodiscussbean vediodiscussbean) {
		// TODO Auto-generated method stub
	
		if (vediodiscussbean.getVedio_id().equals(vedio_id)) {
			//System.out.println("我是里面是username" + newsname);
			discussList.addFirst(vediodiscussbean);
			handler.sendEmptyMessage(2);
		}

	}

}
