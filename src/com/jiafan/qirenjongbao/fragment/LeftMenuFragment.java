package com.jiafan.qirenjongbao.fragment;


import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.listener.CloudCodeListener;

import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jiafan.qirenjongbao.Authoractivity;
import com.jiafan.qirenjongbao.MainUI;
import com.jiafan.qirenjongbao.R;
import com.jiafan.qirenjongbao.SugestActivity;
import com.jiafan.qirenjongbao.application.MyConstants;
import com.jiafan.qirenjongbao.domain.Myuserresponse;
import com.jiafan.qirenjongbao.utils.CacheUtils;
import com.jiafan.qirenjongbao.utils.IntentUtils;
import com.jiafan.qirenjongbao.utils.SPUtils;
import com.lidroid.xutils.BitmapUtils;
import com.meg7.widget.CircleImageView;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;



/**
 * @author jiafan
 * 左侧菜单的Fragment
 */
public class LeftMenuFragment extends BaseFragment implements OnItemClickListener {
	
	private CircleImageView head;
	private TextView username;
	private TextView level;
	private TextView indivisign;
	private Button leftmenu_logout;
	private Button leftmenu_sugest;
	@Override
	public View initView(LayoutInflater inflater) {
		
		View view=inflater.inflate(R.layout.menu_leftfragment, null);
		head = (CircleImageView) view.findViewById(R.id.im_leftfragment_head);
		username = (TextView) view.findViewById(R.id.tv_leftfragment_username);
		level = (TextView) view.findViewById(R.id.tv_leftfragment_level);
		indivisign = (TextView) view.findViewById(R.id.tv_leftfragment_indivisign);
		Button leftmenu_authorfragment=(Button) view.findViewById(R.id.but_leftfragment_author);
		
		leftmenu_authorfragment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				IntentUtils.startActivity(mActivity, Authoractivity.class);
			}
		});
		
		leftmenu_sugest = (Button) view.findViewById(R.id.but_leftfragment_sugest);
		
		leftmenu_sugest.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				IntentUtils.startActivity(mActivity, SugestActivity.class);
			}
		});
		
		
		leftmenu_logout = (Button) view.findViewById(R.id.but_leftfragment_logout);
		
		leftmenu_logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog();
			}
		});
		
		
		initData();
		return view;
	}


	@Override
	public void initData() {
		// TODO Auto-generated method stub
		String cloudCodeName = "findone";
		JSONObject params = new JSONObject();
		//name是上传到云端的参数名称，值是bmob，云端代码可以通过调用request.body.name获取这个值 
		MyConstants.username=SPUtils.getSPstring("username", "");
		try {
			params.put("username", MyConstants.username);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//创建云端代码对象
		AsyncCustomEndpoints cloudCode = new AsyncCustomEndpoints();
		//异步调用云端代码
		cloudCode.callEndpoint(mActivity, cloudCodeName,params, new CloudCodeListener() {
			
			@Override
			public void onSuccess(Object arg0) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				Myuserresponse fromJson = gson.fromJson(arg0.toString(), Myuserresponse.class);
				System.out.println(MyConstants.username+"++++我是刚才测试的1111"+fromJson);
				BitmapUtils bitmapUtils = new BitmapUtils(mActivity);
				bitmapUtils.display(head, fromJson.getResults().getHeadphoto());
				username.setText(fromJson.getResults().getUsername());
				level.setText(fromJson.getResults().getUserlevel());
				indivisign.setText(fromJson.getResults().getIndivisign());
				MyConstants.username=fromJson.getResults().getUsername();
				MyConstants.userheadurl=fromJson.getResults().getHeadphoto();
			
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		
		super.initData();
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
	
		
		// 把左侧菜单收回去
		SlidingMenu slidingMenu = ((MainUI) mActivity).getSlidingMenu();
		slidingMenu.toggle();
		
		
	}
	
	protected void dialog() {
		AlertDialog.Builder builder = new Builder(mActivity);
		builder.setMessage("退出登录");
		builder.setTitle("提示");
		builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				CacheUtils.putBoolean(mActivity, "IS_OPEN_MAIN_PAGER", false);
				dialog.dismiss();
				mActivity.finish();
			}
		});
		
		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				 dialog.dismiss();
			}
		});
		
		
		builder.create().show();
	}


}
