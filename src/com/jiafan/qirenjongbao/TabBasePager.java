package com.jiafan.qirenjongbao;



import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore.Images;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
public class TabBasePager {
	
	public Context mContext;
	public TextView tvTitle;
	public ImageButton ibMenu;
	public ImageButton ibListOrGrid;
	public FrameLayout flContent;
	private View rootView;
	public RelativeLayout wanmingview;
	public ImageView paobu;
	public AnimationDrawable background;
	public RelativeLayout rl_faillianjie;
	public ImageView im_lanpangzhi;

	public TabBasePager(Context context) {
		this.mContext = context;
		
		rootView = initView();
		
	}

	private View initView() {
		View view = View.inflate(mContext, R.layout.tab_base_pager, null);
		tvTitle = (TextView) view.findViewById(R.id.tv_title_bar_title);
		ibMenu = (ImageButton) view.findViewById(R.id.ib_title_bar_menu);
		flContent = (FrameLayout) view.findViewById(R.id.fl_tab_base_pager_content);
		wanmingview = (RelativeLayout) view.findViewById(R.id.tv_wanmingjiazai);
		rl_faillianjie = (RelativeLayout) view.findViewById(R.id.rl_faillianjie);
		im_lanpangzhi = (ImageView) view.findViewById(R.id.im_lanpangzhi);
		
		rl_faillianjie.setVisibility(View.GONE);
		wanmingview.setVisibility(View.VISIBLE);
		paobu = (ImageView) view.findViewById(R.id.imageView1);
		background = (AnimationDrawable) paobu.getBackground();
		
		return view;
	}
	
	/**
	 * 获得当前页面布局对象
	 * @return
	 */
	public View getRootView() {
		return rootView;
	}
	
	public void initData() {
		
	}
}