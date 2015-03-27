package com.jiafan.qirenjongbao;



import u.aly.cv;
import in.srain.cube.image.CubeImageView;
import in.srain.cube.image.ImageLoader;
import in.srain.cube.image.ImageLoaderFactory;
import in.srain.cube.image.impl.DefaultImageLoadHandler;

import com.jiafan.qirenjongbao.utils.CacheUtils;
import com.jiafan.qirenjongbao.utils.IntentUtils;
import com.umeng.update.UmengUpdateAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

public class WelcomeUI extends Activity implements AnimationListener {
	
	public static final String IS_OPEN_MAIN_PAGER = "IS_OPEN_MAIN_PAGER";
	private CubeImageView civ_welcome_background;
	private ImageView iv_welcome_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        //初始化
        UmengUpdateAgent.update(this);
        init();
    }

	@SuppressLint("ResourceAsColor")
	private void init() {
		View rootView = findViewById(R.id.rl_welcome_root);
		civ_welcome_background = (CubeImageView) findViewById(R.id.civ_welcomeui_background);
		iv_welcome_icon = (ImageView) findViewById(R.id.iv_welcomeui_icon);
		
		DisplayMetrics dm = new DisplayMetrics();
		// 获取屏幕信息
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		int screenWidth = dm.widthPixels;
		
		int vedioimageheigt=(screenWidth/3);
		LayoutParams layoutParams = new LayoutParams(screenWidth/3, vedioimageheigt);
		iv_welcome_icon.setLayoutParams(layoutParams);
		
		
		DefaultImageLoadHandler handler = new DefaultImageLoadHandler(this);
		handler.setLoadingImageColor(R.color.yellow);
		
		ImageLoader imageloader = ImageLoaderFactory.create(this, handler);
		civ_welcome_background.loadImage(imageloader, "http://zhangjiafan.oss-cn-shenzhen.aliyuncs.com/dongmanzouka/welcome.jpg");
		
		//civ_welcome_background.loadImage(imageloader, url)
		/*
		RotateAnimation rotateAnima = new RotateAnimation(
				0, 360, 
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnima.setDuration(1000);
		rotateAnima.setFillAfter(true); // 设置动画执行完毕时, 停留在完毕的状态下.
		
		ScaleAnimation scaleAnima = new ScaleAnimation(
				0, 1, 
				0, 1, 
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnima.setDuration(1000);
		scaleAnima.setFillAfter(true);*/
		
		AlphaAnimation alphaAnima = new AlphaAnimation(0, 1);
		alphaAnima.setDuration(3000);
		alphaAnima.setFillAfter(true);
		
		// 把三个动画合在一起, 组成一个集合动画
		AnimationSet setAnima = new AnimationSet(false);
		//setAnima.addAnimation(rotateAnima);
		//setAnima.addAnimation(scaleAnima);
		setAnima.addAnimation(alphaAnima);
		setAnima.setAnimationListener(this);
		rootView.startAnimation(setAnima);
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// 去文件中取是否打开过程序的值
		boolean isOpenMainPager = CacheUtils.getBoolean(WelcomeUI.this, IS_OPEN_MAIN_PAGER, false);
		Intent intent = new Intent();
		if(isOpenMainPager) {
			// 已经打开过一次主界面, 直接进入主界面.
			IntentUtils.startActivityForDelayAndFinish(WelcomeUI.this, MainUI.class, 2000);
			//intent.setClass(WelcomeUI.this, MainUI.class);
			//finish();
		} else {
			// 没有打开过主界面, 进入引导页面.
			IntentUtils.startActivityForDelayAndFinish(WelcomeUI.this, GuideUI.class,2000);
			//finish();
		}
		//startActivity(intent);
		// 关闭掉欢迎界面
		
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		civ_welcome_background=null;
	}
}
