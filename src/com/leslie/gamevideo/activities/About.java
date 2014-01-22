package com.leslie.gamevideo.activities;

import android.os.Bundle;
import android.widget.TextView;

import com.leslie.gamevideo.R;

/**
 * 我的糖豆-》关于
 * 
 * @author huangl
 * 
 */
// start
public class About extends BaseActivity {
	private TextView content;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

		content = (TextView) findViewById(R.id.about_content);
		content.setText(getString(R.string.declare_content));
		setTopMenu();
	}

	// @Override
	// public void onBackPressed() {
	// super.onBackPressed();
	// finish();
	// }

	@Override
	protected void setTitles() {
		title.setText("声明");

	}
}
