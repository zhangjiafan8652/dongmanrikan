package com.jiafan.qirenjongbao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import com.baseproject.utils.Logger;
import com.google.gson.Gson;
import com.jiafan.qirenjongbao.application.MyConstants;
import com.jiafan.qirenjongbao.domain.Loginpicturebean;
import com.jiafan.qirenjongbao.domain.Loginrespose;
import com.jiafan.qirenjongbao.utils.ImageoomUtils;
import com.jiafan.qirenjongbao.utils.IntentUtils;
import com.jiafan.qirenjongbao.utils.SPUtils;
import com.jiafan.qirenjongbao.utils.StringUtils;
import com.jiafan.qirenjongbao.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.listener.CloudCodeListener;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginAcivity extends Activity implements OnClickListener {

	private EditText et_username;
	private EditText et_password;
	private Button but_login;
	private Button but_register;
	private int k = 2;
	private String loginpictureurl = "http://zhangjiafan.oss-cn-shenzhen.aliyuncs.com/dongmanzouka/loginpicture/loginpicture.json";
	// 填写从短信SDK应用后台注册得到的APPKEY
	private static String APPKEY = "4c193cadb906";

	// 填写从短信SDK应用后台注册得到的APPSECRET
	private static String APPSECRET = "7e47ded3aeb3e40e2510f6d4377606b0";

	// 短信注册，随机产生头像
	private static final String[] AVATARS = {
			"http://tupian.qqjay.com/u/2011/0729/e755c434c91fed9f6f73152731788cb3.jpg",
			"http://99touxiang.com/public/upload/nvsheng/125/27-011820_433.jpg",
			"http://img1.touxiang.cn/uploads/allimg/111029/2330264224-36.png",
			"http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339485237265.jpg",
			"http://diy.qqjay.com/u/files/2012/0523/f466c38e1c6c99ee2d6cd7746207a97a.jpg",
			"http://img1.touxiang.cn/uploads/20121224/24-054837_708.jpg",
			"http://img1.touxiang.cn/uploads/20121212/12-060125_658.jpg",
			"http://img1.touxiang.cn/uploads/20130608/08-054059_703.jpg",
			"http://diy.qqjay.com/u2/2013/0422/fadc08459b1ef5fc1ea6b5b8d22e44b4.jpg",
			"http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339510584349.jpg",
			"http://img1.touxiang.cn/uploads/20130515/15-080722_514.jpg",
			"http://diy.qqjay.com/u2/2013/0401/4355c29b30d295b26da6f242a65bcaad.jpg" };

	private boolean ready;
	private TextView tvNum;
	private ImageView ll_background;
	private AlphaAnimation alphaAnima01;
	private ArrayList<String> list;
	private BitmapUtils bitmapUtils;
	private SharedPreferences sp;
	private Editor edit;
	private MediaPlayer mediaPlayer;
	private RelativeLayout re_loginbackground;
	private Activity mactivity;

	private Context mcontext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		mactivity=this;
		mcontext=this;
		initview();

	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void initview() {
		et_username = (EditText) findViewById(R.id.et_login_username);
		et_password = (EditText) findViewById(R.id.et_login_password);
		
		but_login = (Button) findViewById(R.id.but_login_login);
		but_register = (Button) findViewById(R.id.but_login_register);
		but_login.setOnClickListener(this);
		but_register.setOnClickListener(this);
		ll_background = (ImageView) findViewById(R.id.ll_background_animation);
		re_loginbackground = (RelativeLayout) findViewById(R.id.re_login_background);
		
		//BitmapUtils bitmapUtils2 = new BitmapUtils(this);
		//Bitmap imageOomutils = ImageoomUtils.imageOomutils(mactivity, mcontext, R.drawable.loginbackground);
		//re_loginbackground.setBackground(new BitmapDrawable(imageOomutils));
		
		alphaAnima01 = new AlphaAnimation(0, 1);
		alphaAnima01.setDuration(3000);
		alphaAnima01.setFillAfter(true);
		alphaAnima01.setRepeatMode(Animation.REVERSE);
		alphaAnima01.setRepeatCount(1);
		
		/*new Thread(){
			public void run() {
				mediaPlayer = new MediaPlayer();
				try {
					mediaPlayer.setDataSource("http://zhangjiafan.oss-cn-shenzhen.aliyuncs.com/dongmanzouka/loginpicture/zai.mp3");
					mediaPlayer.prepare();
					mediaPlayer.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			};
		}.start();*/
		loadSharePrefrence();
		setSharePrefrence();
		initSDK();
		list = new ArrayList<String>();
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, loginpictureurl,
				new RequestCallBack<String>() {
					private Loginpicturebean fromJson;

					// TODO
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// System.out.println(responseInfo.result);
						Gson gson = new Gson();
						fromJson = gson.fromJson(responseInfo.result,
								Loginpicturebean.class);
						// System.out.println(fromJson.toString());
						ArrayList<String> arrayList = new ArrayList<String>();
						arrayList = (ArrayList<String>) fromJson
								.getPicturelist();
						boolean flag = SPUtils.putObject("list", arrayList);
						//System.out.println(flag);
						onSuccessStartshow(fromJson);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						//System.out.println("失败了");
						ArrayList<String> object2 = SPUtils.getObject("list");
						if (object2 == null) {
							//System.out.println("没有数据");
						} else {

							if (object2 != null) {
								for (int i = 0; i < object2.size(); i++) {
									list.add(object2.get(i));
									//System.out.println("obj" + object2.get(i));
								}
							}
							BitmapUtils bitmapUtils2 = new BitmapUtils(LoginAcivity.this);
							bitmapUtils2.display(ll_background, list.get(0),
									new MydefaultBitmapLoadCallBack<View>());

						}
					}
				});

	

		alphaAnima01.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {

				int y = k % list.size();
				bitmapUtils = new BitmapUtils(LoginAcivity.this);
				bitmapUtils.display(ll_background, list.get(y),
						new MydefaultBitmapLoadCallBack<View>());
				k = k + 1;
			}
		});

	}

	protected void onSuccessStartshow(Loginpicturebean fromJson) {

		if (fromJson.getStatus().equals("1")) {
			list = (ArrayList<String>) fromJson.getPicturelist();
			BitmapUtils bitmapUtils3 = new BitmapUtils(LoginAcivity.this);
			bitmapUtils3.display(ll_background, list.get(1),
					new MydefaultBitmapLoadCallBack<View>());
		} else {
			// TODO播放默认的
			// System.out.println(fromJson.toString());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.but_login_login:
			// TODO 登陆
			UIUtils.showProgresssafe("正在登陆。。。", LoginAcivity.this);
			String username=et_username.getText().toString().trim();
			String password=et_password.getText().toString().trim();
			if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
				UIUtils.showToastSafe("账号密码不能为空哦~！");
				UIUtils.stopProgresssafe();
				return;
			}
			password=StringUtils.string2Unicode(password);
			//test对应你刚刚创建的云端代码名称
			String cloudCodeName = "login";
			JSONObject params = new JSONObject();
			//name是上传到云端的参数名称，值是bmob，云端代码可以通过调用request.body.name获取这个值 
			try {
				params.put("username", username);
				params.put("password", password);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			//创建云端代码对象
			AsyncCustomEndpoints cloudCode = new AsyncCustomEndpoints();
			//异步调用云端代码
			cloudCode.callEndpoint(LoginAcivity.this, cloudCodeName, params, new CloudCodeListener() {

				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					UIUtils.stopProgresssafe();
					System.out.println("登陆fail"+arg1);
				}

				@Override
				public void onSuccess(Object arg0) {
					// TODO Auto-generated method stub
					//System.out.println("登陆succ"+arg0);
					Gson gson = new Gson();
					Loginrespose fromJson = gson.fromJson(arg0.toString(), Loginrespose.class);
					//System.out.println(fromJson.toString());
					if(fromJson.getResults().getCode()==null){
						IntentUtils.startActivityAndFinish(LoginAcivity.this, MainUI.class);
						MyConstants.username=fromJson.getResults().getUsername();
						SPUtils.putSPstring("username", MyConstants.username);
					}else{
						UIUtils.showToastSafe("账号不存在或者密码错误~！");
					}
					UIUtils.stopProgresssafe();
				}

			});
			
			
			break;

		case R.id.but_login_register:
			// 打开注册页面
			RegisterPage registerPage = new RegisterPage();
		
			registerPage.setRegisterCallback(new EventHandler() {
				public void afterEvent(int event, int result, Object data) {
					// 解析注册结果
					if (result == SMSSDK.RESULT_COMPLETE) {
						@SuppressWarnings("unchecked")
						HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
						String country = (String) phoneMap.get("country");
						String phone = (String) phoneMap.get("phone");
						// 提交用户信息
						registerUser(country, phone);
						Intent intent = new Intent(LoginAcivity.this,Register.class);
						intent.putExtra("phonenumber", phone);
						startActivity(intent);
					}
				}
			});
			registerPage.show(this);
			break;

		default:
			break;
		}
	}

	private void initSDK() {
		// 初始化短信SDK
		SMSSDK.initSDK(this, APPKEY, APPSECRET);
		final Handler handler = new Handler();
		EventHandler eventHandler = new EventHandler() {
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		};
		// 注册回调监听接口
		SMSSDK.registerEventHandler(eventHandler);
		ready = true;

	}

	private void loadSharePrefrence() {
		SharedPreferences p = getSharedPreferences("SMSSDK_SAMPLE",
				Context.MODE_PRIVATE);
		APPKEY = p.getString("APPKEY", APPKEY);
		APPSECRET = p.getString("APPSECRET", APPSECRET);
	}

	private void setSharePrefrence() {
		SharedPreferences p = getSharedPreferences("SMSSDK_SAMPLE",
				Context.MODE_PRIVATE);
		Editor edit = p.edit();
		edit.putString("APPKEY", APPKEY);
		edit.putString("APPSECRET", APPSECRET);
		edit.commit();
	}

	protected void onDestroy() {
		if (ready) {
			// 销毁回调监听接口
			SMSSDK.unregisterAllEventHandler();
		}
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (ready) {
			// 获取新好友个数
			SMSSDK.getNewFriendsCount();
		}
	}

	@Override
	protected void onPause() {
		if (mediaPlayer!=null) {
			mediaPlayer.stop();
			mediaPlayer=null;
		}
		super.onPause();
	}

	public boolean handleMessage(Message msg) {

		int event = msg.arg1;
		int result = msg.arg2;
		Object data = msg.obj;
		if (event == SMSSDK.EVENT_SUBMIT_USER_INFO) {
			// 短信注册成功后，返回MainActivity,然后提示新好友
			if (result == SMSSDK.RESULT_COMPLETE) {
				Toast.makeText(this, R.string.smssdk_user_info_submited,
						Toast.LENGTH_SHORT).show();
			} else {
				((Throwable) data).printStackTrace();
			}
		} else if (event == SMSSDK.EVENT_GET_NEW_FRIENDS_COUNT) {
			if (result == SMSSDK.RESULT_COMPLETE) {
				refreshViewCount(data);
			} else {
				((Throwable) data).printStackTrace();
			}
		}
		return false;
	}

	// 更新，新好友个数
	private void refreshViewCount(Object data) {
		int newFriendsCount = 0;
		try {
			newFriendsCount = Integer.parseInt(String.valueOf(data));
		} catch (Throwable t) {
			newFriendsCount = 0;
		}
		if (newFriendsCount > 0) {
			tvNum.setVisibility(View.VISIBLE);
			tvNum.setText(String.valueOf(newFriendsCount));
		} else {
			tvNum.setVisibility(View.GONE);
		}
	}

	// 提交用户信息
	//TODO
	private void registerUser(String country, String phone) {
		Random rnd = new Random();
		int id = Math.abs(rnd.nextInt());
		String uid = String.valueOf(id);
		String nickName = "SmsSDK_User_" + uid;
		String avatar = AVATARS[id % 12];
		SMSSDK.submitUserInfo(uid, nickName, avatar, country, phone);
		startActivity(new Intent(this, Register.class));
		finish();
	}

	public class MydefaultBitmapLoadCallBack<ImageView extends View> extends
			BitmapLoadCallBack<ImageView> {

		@Override
		public void onLoadCompleted(ImageView container, String uri,
				Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
			this.setBitmap(container, bitmap);
			Animation animation = config.getAnimation();

			container.startAnimation(alphaAnima01);
		}

		@Override
		public void onLoadFailed(ImageView container, String uri,
				Drawable drawable) {
			// TODO Auto-generated method stub
			//Logger.e("我拿图片", msg)
		}

	}

	public class Myset extends HashSet<String> {
		@Override
		public boolean add(String object) {
			super.add(object);
			return true;
		}
	}
}
