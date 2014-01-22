package com.leslie.gamevideo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.leslie.gamevideo.R;

public class More extends Activity {

	ListView lv;
	private RelativeLayout favoriteRl;
	private RelativeLayout historyRl;
	private RelativeLayout feedBackRl;
	private RelativeLayout aboutRl;
	private ImageButton imgbtnHistory;
	private String email;
	private String about_us;
	private String webName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);
		findAllViews();
		initOnclickerListener();
	}

	private void initOnclickerListener() {
		imgbtnHistory.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(More.this, History.class);
				startActivity(intent);
			}
		});

	}

	private void findAllViews() {
		imgbtnHistory = (ImageButton) findViewById(R.id.imgbtnHistory);
		favoriteRl = (RelativeLayout) findViewById(R.id.more_favorite);
		historyRl = (RelativeLayout) findViewById(R.id.more_history);
		feedBackRl = (RelativeLayout) findViewById(R.id.more_feedback);
		aboutRl = (RelativeLayout) findViewById(R.id.more_about);
		initListener();

	}

	private void initListener() {
		OnItemListener listener = new OnItemListener();
		favoriteRl.setOnClickListener(listener);
		historyRl.setOnClickListener(listener);
		feedBackRl.setOnClickListener(listener);
		aboutRl.setOnClickListener(listener);

	}

	private class OnItemListener implements OnClickListener {

		public void onClick(View v) {
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.more_favorite:
				intent.setClass(More.this, Favorite.class);
				startActivity(intent);
				break;
			case R.id.more_history:
				intent.setClass(More.this, History.class);
				startActivity(intent);
				break;
			case R.id.more_feedback:
				intent.setClass(More.this, FeedBack.class);
				intent.putExtra("email", email);
				intent.putExtra("webname", webName);
				startActivity(intent);
				break;
			case R.id.more_about:
				intent.setClass(More.this, About.class);
				intent.putExtra("about_us", about_us);
				intent.putExtra("website_name", webName);
				startActivity(intent);
				break;
			default:
				break;
			}

		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(More.this, MainTabsActivity.class);
		startActivity(intent);
	}

}
