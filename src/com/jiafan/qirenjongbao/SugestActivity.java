package com.jiafan.qirenjongbao;

import cn.bmob.v3.listener.SaveListener;

import com.jiafan.qirenjongbao.application.MyConstants;
import com.jiafan.qirenjongbao.domain.Sugestbean;
import com.jiafan.qirenjongbao.utils.UIUtils;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SugestActivity extends Activity{

	
	private EditText et_sugest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.sugestactivity);
		
		et_sugest = (EditText) findViewById(R.id.et_sugest);
		
		
	}
	
	public void sumit(View view){
		String text = et_sugest.getText().toString().trim();
		if(TextUtils.isEmpty(text)){
			UIUtils.showToastSafe("意见不能为空");
		}else {
			Sugestbean sugestbean = new Sugestbean();
			sugestbean.setUsername(MyConstants.username);
			sugestbean.setSugest(text);
			sugestbean.save(SugestActivity.this, new SaveListener() {
				
				@Override
				public void onSuccess() {
					UIUtils.showToastSafe("提交成功");
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					UIUtils.showToastSafe("提交失败");
				}
			});
		}
	}
	
	public void callback(View view) {
		finish();
	}
}
