package com.jiafan.qirenjongbao;

import java.util.ArrayList;
import java.util.List;

import com.example.menulibrary.ContextMenuDialogFragment;
import com.example.menulibrary.MenuObject;
import com.example.menulibrary.interfaces.OnMenuItemClickListener;
import com.example.menulibrary.interfaces.OnMenuItemLongClickListener;
import com.jiafan.qirenjongbao.fragment.AuthorFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Authoractivity extends ActionBarActivity implements
		OnMenuItemClickListener, OnMenuItemLongClickListener {

	private FragmentManager fragmentManager;
	private DialogFragment mMenuDialogFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.author);
		fragmentManager = getSupportFragmentManager();

	
		initToolbar();
		mMenuDialogFragment = ContextMenuDialogFragment.newInstance(
				(int) getResources().getDimension(R.dimen.tool_bar_height),
				getMenuObjects());
		addFragment(new AuthorFragment(), true, R.id.container);
	
	}

	private List<MenuObject> getMenuObjects() {
		List<MenuObject> menuObjects = new ArrayList<>();
		menuObjects.add(new MenuObject(R.drawable.icn_close));
		menuObjects.add(new MenuObject(R.drawable.icn_1, "发消息"));
		menuObjects.add(new MenuObject(R.drawable.icn_2, "非常喜欢"));
		menuObjects.add(new MenuObject(R.drawable.icn_3, "添加朋友"));
		menuObjects.add(new MenuObject(R.drawable.icn_4, "点赞"));
		menuObjects.add(new MenuObject(R.drawable.icn_5, "取消"));
		return menuObjects;
	}

	private void initToolbar() {
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		TextView mToolBarTextView = (TextView) findViewById(R.id.text_view_toolbar_title);

		setSupportActionBar(mToolbar);

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		mToolbar.setNavigationIcon(R.drawable.btn_back);
		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		mToolBarTextView.setText("张珈凡");
	}

	protected void addFragment(Fragment fragment, boolean addToBackStack,
			int containerId) {
		invalidateOptionsMenu();
		String backStackName = fragment.getClass().getName();
		boolean fragmentPopped = fragmentManager.popBackStackImmediate(
				backStackName, 0);
		if (!fragmentPopped) {
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			transaction.add(containerId, fragment, backStackName)
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			if (addToBackStack)
				transaction.addToBackStack(backStackName);
			transaction.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.context_menu:
			mMenuDialogFragment.show(fragmentManager, "DropDownMenuFragment");
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (fragmentManager.getBackStackEntryCount() == 1) {
			finish();
			// 设置activity动画，第一个参数进入。第二个退出
//			overridePendingTransition(R.anim.enter,
//					R.anim.out);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onMenuItemClick(View clickedView, int position) {
		Toast.makeText(this, "亲，本功能未开放哦~！",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onMenuItemLongClick(View clickedView, int position) {
		Toast.makeText(this, "亲，本功能未开放哦~！ ",
				Toast.LENGTH_SHORT).show();
	}

}
