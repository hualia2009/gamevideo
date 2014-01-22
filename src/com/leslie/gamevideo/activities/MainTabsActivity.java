package com.leslie.gamevideo.activities;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.leslie.gamevideo.AppConnect;
import com.leslie.gamevideo.R;
import com.leslie.gamevideo.utils.Config;
import com.leslie.gamevideo.utils.Utils;

@SuppressWarnings("deprecation")
public class MainTabsActivity extends TabActivity implements
		CompoundButton.OnCheckedChangeListener {

	public static TabHost tabHost;

	public static String is_ad_on = "";
	private static RadioButton[] radioButtons;
	private Intent homeIntent;
	private Intent channelIntent;
	private Intent searchIntent;
	private Intent downloadIntent;
	private Intent moreIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_tabs);
		LogoActivity.activities.add(MainTabsActivity.this);
		tabHost = getTabHost();

		Config.flag = "main";
		homeIntent = new Intent(this, Main.class);
		channelIntent = new Intent(this, Channel.class);
		searchIntent = new Intent(this, Search.class);
		// downloadIntent = new Intent(this, PreLoadActivity.class);
		downloadIntent = new Intent(this, AppWall.class);
		moreIntent = new Intent(this, More.class);

		initRadios();
		/** 给tabhost添加tab **/
		setupIntent();
		/** 设置默认选项setChecked(true)为当前选中的选项卡tabHost.getCurrentTab() **/
		Intent intent = getIntent();
		if (intent != null && intent.getAction() != null) {
			tabHost.setCurrentTab(4);
		}
		radioButtons[tabHost.getCurrentTab()].setChecked(true);
	}

	public static void setRadioButtons() {
		radioButtons[tabHost.getCurrentTab()].setChecked(true);
	}

	/**
	 * 往里面添加选项卡 将选项卡和意图对象绑定
	 */
	private void setupIntent() {

		tabHost.clearAllTabs();

		tabHost.addTab(tabHost.newTabSpec("home").setIndicator("home")
				.setContent(homeIntent));
		tabHost.addTab(tabHost.newTabSpec("channel").setIndicator("channel")
				.setContent(channelIntent));
		tabHost.addTab(tabHost.newTabSpec("search").setIndicator("search")
				.setContent(searchIntent));
		tabHost.addTab(tabHost.newTabSpec("download").setIndicator("download")
				.setContent(downloadIntent));
		tabHost.addTab(tabHost.newTabSpec("more").setIndicator("more")
				.setContent(moreIntent));

	}

	private void initRadios() {
		RadioGroup radioGroup = (RadioGroup) this.findViewById(R.id.radioGroup);
		radioButtons = new RadioButton[5];
		for (int i = 0; i < 5; i++) {
			radioButtons[i] = (RadioButton) radioGroup
					.findViewWithTag("radio_button" + i);
			radioButtons[i].setOnCheckedChangeListener(this);
		}
	}

	/**
	 * 仅在点Home键退出Activity而再次启动新的Intent进来才被调用到;确保默认选中推送列表界面
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		// tabHost.setCurrentTab(0);
		// radioButtons[tabHost.getCurrentTab()].setChecked(true);
	}

	/**
	 * 根据选项卡的Tab标签来设置当前的选项卡 参数 tag 想要被设置为当前选项卡的tag标签值
	 */
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked && tabHost != null) {
			if (buttonView == radioButtons[0]) {
				tabHost.setCurrentTabByTag("home");
			} else if (buttonView == radioButtons[1]) {
				tabHost.setCurrentTabByTag("channel");
			} else if (buttonView == radioButtons[2]) {
				tabHost.setCurrentTabByTag("search");
			} else if (buttonView == radioButtons[3]) {
				tabHost.setCurrentTabByTag("download");
			} else if (buttonView == radioButtons[4]) {
				tabHost.setCurrentTabByTag("more");
			}
		}
	}

	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(R.drawable.icon_tang_new);
			Dialog dialog = builder
					.setTitle(R.string.exit_title)
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// UploadActivity.saveUploading();
									// updateDb();
									// 关闭所有Activity
									pureExit();
								}

							})
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// Do nothing
								}
							}).create();
			dialog.show();
			return false;
		}
		return super.dispatchKeyEvent(event);
	}

	private void pureExit() {
		for (int i = 0; i < LogoActivity.activities.size(); i++) {
			if (null != LogoActivity.activities.get(i)) {
				LogoActivity.activities.get(i).finish();
			}
		}
		ActivityManager activityManager = (ActivityManager) getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		activityManager.restartPackage("com.leslie.gamevideo");
		AppConnect.getInstance(this).close();
		System.exit(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_xml_file, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuitem) {
		switch (menuitem.getItemId()) {
		default:
			break;
		case R.menuitem.about:
			String s = Utils.getVerName(MainTabsActivity.this);
			if (s.equals(""))
				s = "1.0";
			(new android.app.AlertDialog.Builder(this))
					.setIcon(R.drawable.icon_tang_new)
					.setTitle("关于")
					.setPositiveButton("确定", null)
					.setMessage(
							(new StringBuilder("游戏视频\nAndroid V"))
									.append(s)
									.append("\nLeslie版权所有\nCopyright\2512014\nAll Rights Reserved")
									.toString()).show();
			break;
		case R.menuitem.checkupdate:
			AppConnect.getInstance(this).checkUpdate(this);
			break;
		case R.menuitem.cancel:
			exit();
			break;
		}
		return super.onOptionsItemSelected(menuitem);
	}

	private void exit() {
		Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.icon_tang_new);
		Dialog dialog = builder
				.setTitle(R.string.exit_title)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								pureExit();
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
							}
						}).create();
		dialog.show();
	}

}
