package com.jiafan.qirenjongbao;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.bmob.BmobPro;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.google.gson.Gson;
import com.jiafan.qirenjongbao.domain.Loginrespose;
import com.jiafan.qirenjongbao.domain.MyUserbmob;
import com.jiafan.qirenjongbao.utils.IntentUtils;
import com.jiafan.qirenjongbao.utils.StringUtils;
import com.jiafan.qirenjongbao.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;
import com.meg7.widget.CircleImageView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Register extends Activity implements OnClickListener {

	private EditText et_register_username;
	private EditText et_register_password;
	private EditText et_register_repassword;
	private EditText et_register_constellation;
	private EditText et_register_indivisign;
	private Button but_register_ok;
	private File tempFile;
	private File sdcardTempFile;
	private AlertDialog dialog;
	private Activity context;

	private String headphoto;
	private static final String[] mCountries = { "请选择性别", "男", "女", "男男", "女女" };
	private String phonenumber;
	private Spinner sp_register_sex;
	private CircleImageView imageview;
	private String username;
	private String indivisign;
	private String sex;
	private String userlevel;
	private String password;
	private String repassword;
	private EditText et_register_email;
	private String email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regist);
		context = this;
		phonenumber = getIntent().getStringExtra("phonenumber");
		if(phonenumber==null){
			UIUtils.showToastSafe("验证短信失败");
			IntentUtils.startActivityAndFinish(context, LoginAcivity.class);
		}
		
		initview();
	}

	private void initview() {
		et_register_username = (EditText) findViewById(R.id.et_register_username);
		et_register_password = (EditText) findViewById(R.id.et_register_password);
		et_register_repassword = (EditText) findViewById(R.id.et_register_repassword);
		et_register_indivisign = (EditText) findViewById(R.id.et_register_indivisign);
		et_register_email = (EditText) findViewById(R.id.et_register_email);
		
		
		imageview = (CircleImageView) findViewById(R.id.im_register_head);
		imageview.setOnClickListener(this);
		sp_register_sex = (Spinner) findViewById(R.id.sp_register_sex);
		ArrayAdapter<String> ad = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mCountries);
		ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_register_sex.setAdapter(ad);

		but_register_ok = (Button) findViewById(R.id.but_register_ok);

		but_register_ok.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.im_register_head:
			if (dialog == null) {
				dialog = new AlertDialog.Builder(this).setItems(
						new String[] { "相机", "相册" },
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (which == 0) {
									// 选择拍照
									Intent cameraintent = new Intent(
											MediaStore.ACTION_IMAGE_CAPTURE);
									tempFile = new File(Environment
											.getExternalStorageDirectory(),
											getPhotoFileName());
									sdcardTempFile = new File(
											"/mnt/sdcard/",
											"tmp_pic_wo_"
													+ SystemClock
															.currentThreadTimeMillis()
													+ ".jpg");
									// 指定调用相机拍照后照片的储存路径
									cameraintent.putExtra(
											MediaStore.EXTRA_OUTPUT,
											Uri.fromFile(tempFile));
									startActivityForResult(cameraintent, 101);

								} else {
									Intent intent = new Intent(
											"android.intent.action.PICK");
									sdcardTempFile = new File(
											"/mnt/sdcard/",
											"tmp_pic_wo_"
													+ SystemClock
															.currentThreadTimeMillis()
													+ ".jpg");
									intent.setDataAndType(
											MediaStore.Images.Media.INTERNAL_CONTENT_URI,
											"image/*");
									intent.putExtra("output",
											Uri.fromFile(sdcardTempFile));
									intent.putExtra("crop", "true");
									intent.putExtra("aspectX", 1);// 裁剪框比例
									intent.putExtra("aspectY", 1);
									intent.putExtra("outputX", 300);// 输出图片大小
									intent.putExtra("outputY", 300);
									startActivityForResult(intent, 100);
								}
							}
						}).create();
			}
			if (!dialog.isShowing()) {
				dialog.show();
			}
			break;

		case R.id.but_register_ok:
			username = et_register_username.getText().toString().trim();
			indivisign = et_register_indivisign.getText().toString().trim();
			sex = mCountries[sp_register_sex.getSelectedItemPosition()];
			userlevel = "初生牛犊";

			password = et_register_password.getText().toString().trim();
			repassword = et_register_repassword.getText().toString().trim();
			email = et_register_email.getText().toString().trim();
			
			if (!password.equals(repassword)) {
				UIUtils.showToastSafe("两次输入的密码不同");
				return;
			}
			if(TextUtils.isEmpty(username)){
				UIUtils.showToastSafe("用户名不能为空");
				return;
			}
			if(TextUtils.isEmpty(password)){
				UIUtils.showToastSafe("用户名不能为空");
				return;
			}
			
			password=StringUtils.string2Unicode(password);

			if (sdcardTempFile == null) {
				UIUtils.showToastSafe("请设置头像挖！");
				return;
			}
			if(!StringUtils.isEmailAddress(email)){
				UIUtils.showToastSafe("请输入正确的邮箱地址");
				return;
			}
			
			if (sdcardTempFile.length() < 5000) {
				UIUtils.showToastSafe("请选择头像，或者头像太小");
				return;
			} else {
				final BmobFile bmobFile = new BmobFile(sdcardTempFile);
				bmobFile.uploadblock(this, new UploadFileListener() {
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						// bmobFile.getUrl()---返回的上传文件的地址（不带域名）
						// bmobFile.getFileUrl(context)--返回的上传文件的完整地址（带域名）
						System.out.println(bmobFile.getFileUrl(context));
						headphoto = bmobFile.getFileUrl(context);
						
						uploapuserdata();
					}
					
					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						UIUtils.showProgresssafe("正在提交数据，请稍等等~", Register.this);
						
						super.onStart();
					}

					@Override
					public void onProgress(Integer value) {
						// TODO Auto-generated method stub
						// 返回的上传进度（百分比）
						System.out.println(value);
					}

					@Override
					public void onFailure(int code, String msg) {
						// TODO Auto-generated method stub
						System.out.println(code + "+++++++" + msg);
						
						UIUtils.stopProgresssafe();
						UIUtils.showToastSafe("上传图片失败，请检查网络");
					}
				});
			}
			break;

		default:
			break;
		}

	}

	private void uploapuserdata() {

		//test对应你刚刚创建的云端代码名称
		String cloudCodeName = "test";
		JSONObject params = new JSONObject();
		//name是上传到云端的参数名称，值是bmob，云端代码可以通过调用request.body.name获取这个值 
		try {
			params.put("username", username);
			params.put("email", email);
			
			params.put("password", password);
			params.put("userlevel", userlevel);
			params.put("indivisign", indivisign);
			
			params.put("headphoto", headphoto);
			params.put("sex", sex);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//创建云端代码对象
		AsyncCustomEndpoints cloudCode = new AsyncCustomEndpoints();
		//异步调用云端代码
		cloudCode.callEndpoint(Register.this, cloudCodeName, params, new CloudCodeListener() {

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				UIUtils.stopProgresssafe();
				UIUtils.showToastSafe("注册成功");
				
			}

			@Override
			public void onSuccess(Object arg0) {
				// TODO Auto-generated method stub
				System.out.println(arg0);
				
				Gson gson = new Gson();
				Loginrespose fromJson = gson.fromJson(arg0.toString(), Loginrespose.class);
				System.out.println("+++++++++"+fromJson.toString());
				if(fromJson.getResults().getCode()==null){
					System.out.println("");
					MyUserbmob myUserbmob = new MyUserbmob();
					myUserbmob.setUsername(username);
					myUserbmob.setPassword(password);
					myUserbmob.setEmail(email);
					myUserbmob.setPhonenumber(phonenumber);
					myUserbmob.setUserlevel(userlevel);
					myUserbmob.setIndivisign(indivisign);
					myUserbmob.setHeadphoto(headphoto);
					myUserbmob.setSex(sex);
					
					myUserbmob.save(context, new SaveListener() {
						
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							UIUtils.stopProgresssafe();
							UIUtils.showToastSafe("注册成功");
							IntentUtils.startActivityAndFinish(context, LoginAcivity.class);
						}
						
						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							UIUtils.stopProgresssafe();
						}
					});
					
				}else if(fromJson.getResults().getCode().equals("202")){
					UIUtils.showToastSafe("此用户名已经注册");
					UIUtils.stopProgresssafe();
					return;
				}else if(fromJson.getResults().getCode().equals("203")){
					UIUtils.showToastSafe("此邮箱已经被注册了");
					UIUtils.stopProgresssafe();
					return;
				}
				
			}

		});
	}

	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 100) {
			if (sdcardTempFile.length() < 10000) {
				UIUtils.showToastSafe("请选择文件");
			} else {
				new BitmapUtils(this).display(imageview,
						sdcardTempFile.getAbsolutePath());
			}

		} else if (requestCode == 101) {

			startPhotoZoom(Uri.fromFile(tempFile));
		} else if (requestCode == 102) {

			if (sdcardTempFile.length() < 10000) {
				UIUtils.showToastSafe("请选择文件");
			} else {
				new BitmapUtils(this).display(imageview,
						sdcardTempFile.getAbsolutePath());
			}

		}
	}

	private void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");
		intent.putExtra("output", Uri.fromFile(sdcardTempFile));
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		// Log.i("sdcardTempFile:", sdcardTempFile);
		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", false);
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, 102);

	}

	// 使用系统当前日期加以调整作为照片的名称
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}
}
