package com.leslie.gamevideo.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leslie.gamevideo.AppConnect;
import com.leslie.gamevideo.R;
import com.leslie.gamevideo.db.MainDbHelper;
import com.leslie.gamevideo.entity.Video;
import com.leslie.gamevideo.utils.AsyncImageLoader;
import com.leslie.gamevideo.utils.AsyncImageLoader.ImageCallback;
import com.leslie.gamevideo.utils.Config;
import com.leslie.gamevideo.utils.Controller;

/**
 * 视频列表-》视频详细信息
 * 
 * @author huang
 * 
 */
public class Detail extends Activity {

	private Video videoInfo;
	private ImageButton btnPlayN;
	private TextView tvTitle, tvChannel, tvDuration, briefIntro,
			longDesc;
	private ImageView imgThumb;
	private MainDbHelper dbHelper;
	private Button btnFav,btnPlay;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
		//插屏广告
//		AppConnect.getInstance(this).initPopAd(this);
//		boolean hasPopAd = AppConnect.getInstance(this).hasPopAd(this);
//		if(hasPopAd){
//			AppConnect.getInstance(this).showPopAd(this);
//		}
		//互动广告
		if(!MainTabsActivity.is_ad_on.equalsIgnoreCase("0")){
			LinearLayout adlayout =(LinearLayout)findViewById(R.id.AdLinearLayout); 
			AppConnect.getInstance(this).showBannerAd(this, adlayout);
			//迷你广告
			//设置迷你广告背景颜色
			AppConnect.getInstance(this).setAdBackColor(Color.argb(50, 120, 240, 120)); //设置迷你广告广告语颜色 
			AppConnect.getInstance(this).setAdForeColor(Color.YELLOW); //若未设置以上两个颜色,则默认为黑底白字
			LinearLayout miniLayout =(LinearLayout)findViewById(R.id.miniAdLinearLayout); 
			AppConnect.getInstance(this).showMiniAd(this, miniLayout, 10); //默认 10 秒切换一次广告
		}
		
		findViews();
		Config.flag = "detail";
		Bundle extra = getIntent().getExtras();
		videoInfo = (Video) extra.getSerializable("info");
		initVideoInfo();
		initOnclickListener();
	}

	private void findViews() {
		tvTitle = (TextView) findViewById(R.id.detail_title);
		tvChannel = (TextView) findViewById(R.id.detail_channel);
		tvDuration = (TextView) findViewById(R.id.detail_duration);
		imgThumb = (ImageView) findViewById(R.id.detail_thumb);
		btnPlayN = (ImageButton) findViewById(R.id.detail_nplay);
		briefIntro = (TextView) findViewById(R.id.detail_brief_intro);
		longDesc = (TextView) findViewById(R.id.detail_longdesc);
		btnFav = (Button) findViewById(R.id.btn_mark);
		btnPlay = (Button) findViewById(R.id.detail_play);
	}
	private void initVideoInfo() {
		// 对其进行赋值
		dbHelper = MainDbHelper.getInstance(Detail.this);
		tvTitle.setText(videoInfo.getTitle());
		tvChannel.setText("频道：" + videoInfo.getChannel());
		tvDuration.setText("时长：" + videoInfo.getDuration());
		String desc = videoInfo.getDescription().trim();

		if (desc.length() == 0) {
			longDesc.setText("暂无");
		} else {
			longDesc.setText(desc.replace("<br />", ""));
		}
		briefIntro.setText("简介：");

		if (dbHelper.inFavorite(videoInfo.getId())) {
			btnFav.setText("已收藏");
			btnFav.setClickable(false);
		}
		
		String imageUrl = videoInfo.getThumbnail();
		Bitmap cachedImage = AsyncImageLoader.loadDrawable(imageUrl,
				new ImageCallback() {
					public void imageLoaded(Bitmap imageDrawable,
							String imageUrl) {
						imgThumb.setImageBitmap(imageDrawable);
					}
				});
		if (cachedImage == null) {
			imgThumb.setImageResource(R.drawable.default_thumbnail);
		} else {
			imgThumb.setImageBitmap(cachedImage);
		}

	}

	private void initOnclickListener() {
		VideoBtnclickListener listener = new VideoBtnclickListener();
		btnFav.setOnClickListener(listener);
		btnPlayN.setOnClickListener(listener);
		btnPlay.setOnClickListener(listener);
	}

	public class VideoBtnclickListener implements OnClickListener {

		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_mark:
				String toastMsg = "";
				if (dbHelper.inFavorite(videoInfo.getId())) {
					btnFav.setText("已收藏");
					btnFav.setClickable(false);
					return;
				}
				if (dbHelper.addFavorite(videoInfo)) {
					toastMsg = "收藏成功";
					btnFav.setText("已收藏");
					btnFav.setClickable(false);
				}
				Toast toast = Toast.makeText(Detail.this, toastMsg,
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
				toast.show();
				break;
			case R.id.detail_nplay:
				play(v);
				break;
			case R.id.detail_thumb:
				play(v);
			case R.id.detail_play:
				play(v);
			}
		}
	}
	
	private void play(View view){
		dbHelper.addHistory(videoInfo);
		Controller.instance().play(view, videoInfo);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
