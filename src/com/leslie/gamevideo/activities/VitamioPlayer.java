package com.leslie.gamevideo.activities;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnErrorListener;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

import com.leslie.gamevideo.R;

public class VitamioPlayer extends Activity {
	private VideoView mVideoView = null;
	private String path = "";
	private long mPosition = 0;
	private int mVideoLayout = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!LibsChecker.checkVitamioLibs(this))
			return;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.play_vitamio);
		
		Bundle extra = getIntent().getExtras();
		String vid = extra.getString("vid");
		String title = extra.getString("title");
		//url = getResources().getString(R.string.play_header_url) + vid + "/type/mp4/v.m3u8";
		Log.i("vid", vid);
		path = "http://v.youku.com/player/getRealM3U8/vid/"+vid+"/type/video.m3u8";
		
		mVideoView = (VideoView) findViewById(R.id.surface_view);
		mVideoView.setVideoPath(path);
		MediaController mediaController = new MediaController(this);
		mediaController.setFileName(title);
		mVideoView.setMediaController(mediaController);
		mVideoView.requestFocus();

		mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			public void onPrepared(MediaPlayer mediaPlayer) {
				mediaPlayer.setPlaybackSpeed(1.0f);
			}
		});
		
		mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		mVideoView.setOnErrorListener(new OnErrorListener() {
			
			public boolean onError(MediaPlayer mp, int what, int extra) {
				return false;
			}
		});
	
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
		}else {   
	        return super.onKeyUp(keyCode, event);   
	    }
		
		return true;
	}
	
	@Override
	protected void onPause() {
		mPosition = mVideoView.getCurrentPosition();
		mVideoView.stopPlayback();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		if (mPosition > 0) {
			mVideoView.seekTo(mPosition);
			mPosition = 0;
		}
		mVideoView.start();
		super.onResume();
	}
	
	public void changeLayout(View view) {
		mVideoLayout++;
		if (mVideoLayout == 4) {
			mVideoLayout = 0;
		}
		switch (mVideoLayout) {
		case 0:
			mVideoLayout = VideoView.VIDEO_LAYOUT_ORIGIN;
			break;
		case 1:
			mVideoLayout = VideoView.VIDEO_LAYOUT_SCALE;
			break;
		case 2:
			mVideoLayout = VideoView.VIDEO_LAYOUT_STRETCH;
			break;
		case 3:
			mVideoLayout = VideoView.VIDEO_LAYOUT_ZOOM;

			break;
		}
		mVideoView.setVideoLayout(mVideoLayout, 0);
	}
}
