package com.leslie.gamevideo.listener;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.leslie.gamevideo.R;
import com.leslie.gamevideo.utils.AsyncImageLoader;

public class FootBtnListener implements OnClickListener{

	public FootBtnListener(){
	}
	public void onClick(View view) {
		Intent intent = new Intent();
		switch(view.getId()){
		case R.id.btn_index:
			
			intent.setAction("com.tangdou.action.main");
			break;
		case R.id.btn_channel:
			intent.setAction("com.tangdou.action.channel");
			break;
		case R.id.btn_watch:
			intent.setAction("com.tangdou.action.search");
			break;
		case R.id.btn_more:
			intent.setAction("com.tangdou.action.more");
			intent.setFlags(1);
			break;
		}
		AsyncImageLoader.delKey();
		view.getContext().startActivity(intent);
		Activity activity = (Activity)view.getContext();
		activity.overridePendingTransition(R.anim.activity_no_anim, R.anim.activity_no_anim);
	}

}
