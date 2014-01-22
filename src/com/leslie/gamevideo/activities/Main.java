package com.leslie.gamevideo.activities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leslie.gamevideo.AppConnect;
import com.leslie.gamevideo.R;
import com.leslie.gamevideo.adapter.YoukuVideoAdapter;
import com.leslie.gamevideo.banner.GalleryFlow;
import com.leslie.gamevideo.entity.Video;
import com.leslie.gamevideo.jsonparser.YoukuParser;
import com.leslie.gamevideo.listener.BindTopbarBtnListener;
import com.leslie.gamevideo.thread.ThreadPoolWrap;
import com.leslie.gamevideo.utils.Config;
import com.leslie.gamevideo.utils.Controller;
import com.leslie.gamevideo.utils.DialogTools;
import com.leslie.gamevideo.utils.Utils;

/**
 * 首页
 * 
 * @author huangl
 * 
 */
@SuppressLint("HandlerLeak")
public class Main extends Activity implements OnScrollListener {
	private GalleryFlow gallery;
	private LinearLayout pointLinear;
	Timer autoGallery = new Timer();
	private static final int ERROR = -1;
	private static final int LOAD_MORE_VIDEOS = 12;
	private static final int LOAD_BANNER_VIDEOS = 11;
	private static final int NO_VIDOES = 0;

	/**
	 * UI加载完成
	 */
	private OnUIFirstLoadedListener mOnUIFirstLoadedListener;
	private ListView listViewVideo;
	private Handler handler;
	private TextView textView;
	private RelativeLayout loadMoreLayout;
	private RelativeLayout galleryLayout;
	private RelativeLayout homeListLayout;
	private YoukuVideoAdapter adapter;
	private List<Video> videos = new ArrayList<Video>();
	private Dialog dialog = null;
	// 频道标签
	private int pageNum = 1;
	private RelativeLayout netLoading;
	private RelativeLayout netError;
	private LinearLayout galleryPoint;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		addChannelTags();
		Config.main = this;
		Config.init();
		BindTopbarBtnListener.bindAllBtnListener(this);
		Controller.instance();
		findViews();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case LOAD_BANNER_VIDEOS:
					homeListLayout.removeAllViews();
					listViewVideo.setAdapter(adapter);
					homeListLayout.addView(listViewVideo);
					break;
				case LOAD_MORE_VIDEOS:
					videos.addAll((List<Video>) msg.obj);
					adapter.notifyDataSetChanged();
					break;
				case ERROR:
					homeListLayout.removeAllViews();
					netError = (RelativeLayout) LayoutInflater.from(Main.this)
							.inflate(R.layout.net_error, null);
					homeListLayout.addView(netError,
							Utils.getRelativeLayoutParams());
					break;
				default:
					break;
				}
			}
		};

		listViewVideo.addHeaderView(galleryLayout);
		listViewVideo.addFooterView(loadMoreLayout);
		listViewVideo.setOnScrollListener(this);

		homeListLayout.addView(netLoading, Utils.getRelativeLayoutParams());
		// 1.判断网络类型
		dialog = getNetworkDialog();
		if (dialog != null) {
			dialog.show();
		}
		if (!Utils.checkNetworkAvalible(Main.this)) {
			homeListLayout.removeAllViews();
			netError = (RelativeLayout) LayoutInflater.from(Main.this).inflate(
					R.layout.net_error, null);
			ImageView imageView = (ImageView) netError
					.findViewById(R.id.network_error_image);
			imageView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					init();
				}
			});
			homeListLayout.addView(netError, Utils.getRelativeLayoutParams());
			return;
		}
		if (dialog == null || !dialog.isShowing()) {// dialog == null 说明是wifi
			init();
		}
	}

	/**
	 * 动态添加标签
	 */
	private void addChannelTags() {
		MainTabsActivity.is_ad_on = AppConnect.getInstance(this).getConfig("is_ad_on","");
		Channel.channels.add(AppConnect.getInstance(this).getConfig("channel1", "DOTA"));
		Channel.channels.add(AppConnect.getInstance(this).getConfig("channel2", "DOTA2"));
		Channel.channels.add(AppConnect.getInstance(this).getConfig("channel3", "LOL"));
		Channel.channels.add(AppConnect.getInstance(this).getConfig("channel4", "WAR3"));
		Channel.channels.add(AppConnect.getInstance(this).getConfig("channel5", "星际"));
		Channel.channels.add(AppConnect.getInstance(this).getConfig("channel6", "KOF"));
		Channel.channels.add(AppConnect.getInstance(this).getConfig("channel7", "魔兽世界"));
		Channel.channels.add(AppConnect.getInstance(this).getConfig("channel8", "我叫MT"));
	}

	public void init() {
		if (!Utils.checkNetworkAvalible(Main.this)) {
			homeListLayout.removeAllViews();
			netError = (RelativeLayout) LayoutInflater.from(Main.this).inflate(
					R.layout.net_error, null);
			ImageView imageView = (ImageView) netError
					.findViewById(R.id.network_error_image);
			imageView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					init();
				}
			});
			homeListLayout.addView(netError, Utils.getRelativeLayoutParams());
			return;
		}
		new Thread(new MyThread()).start();
	}

	private Dialog getNetworkDialog() {
		if (!Utils.checkNetworkAvalible(Main.this)) {
			// 没有网络
			return DialogTools.buildNetworkErrorDialog(Main.this);
		} else if (!"WIFI".equalsIgnoreCase(Utils.getNetworkType(Main.this))) {
			// 友好提示 2G/3G
			return DialogTools.build3G_2GDialog(Main.this);
		}
		return null;
	}

	public void findViews() {
		homeListLayout = (RelativeLayout) findViewById(R.id.homeListLayout);
		netLoading = (RelativeLayout) LayoutInflater.from(Main.this).inflate(
				R.layout.net_loading, null);
		galleryLayout = (RelativeLayout) LayoutInflater.from(Main.this)
				.inflate(R.layout.gallerypage, null);
		gallery = (GalleryFlow) galleryLayout.findViewById(R.id.gallery);
		pointLinear = (LinearLayout) galleryLayout
				.findViewById(R.id.gallery_point_linear);
		textView = (TextView) pointLinear.findViewById(R.id.bannerTitle);
		textView.setGravity(Gravity.CENTER);
		galleryPoint = (LinearLayout) pointLinear
				.findViewById(R.id.gallery_point);
		listViewVideo = (ListView) findViewById(R.id.video_list);
		loadMoreLayout = (RelativeLayout) LayoutInflater.from(Main.this)
				.inflate(R.layout.video_list_more, null);
	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE && 
				1 + view.getLastVisiblePosition() == view.getCount()) {
			loadMoreLayout.setVisibility(View.VISIBLE);
			pageNum++;
			loadMoreVideoList();
		}

	}

	private class MyThread implements Runnable {
		public void run() {
			String bannerURL = AppConnect.getInstance(Main.this).getConfig("banner_url", getString(R.string.url_video_index));
			Controller.instance().manageBanner(gallery,bannerURL, Main.this, textView,galleryPoint);
			try {
				HttpClient client = new DefaultHttpClient();
				client.getParams().setParameter(
						CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
				HttpGet httpGet = new HttpGet(getUrl());
				HttpResponse response = client.execute(httpGet);
				HttpEntity entity = response.getEntity();
				String results = EntityUtils.toString(entity);
				videos = YoukuParser.parse(results);
				adapter = new YoukuVideoAdapter(videos, listViewVideo);
			} catch (MalformedURLException e) {
				setErrorMessage();
			} catch (IOException e) {
				setErrorMessage();
			}
			Message msg = new Message();
			msg.what = LOAD_BANNER_VIDEOS;
			Main.this.handler.sendMessage(msg); // 向Handler发送消息,更新UI
		}
	}
	
	private String getUrl() {
		String url = Main.this.getResources().getString(R.string.url_search);
		url = url + AppConnect.getInstance(this).getConfig("main_key", "游戏") + "?pid=69b81504767483cf&pg="+pageNum;
		return url;
	}
	
	private void loadMoreVideoList() {
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					HttpClient client = new DefaultHttpClient();
					client.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpGet httpGet = new HttpGet(getUrl());
					HttpResponse response = client.execute(httpGet);
					HttpEntity entity = response.getEntity();
					String results = EntityUtils.toString(entity);
					List<Video> tempvideos = YoukuParser.parse(results);
					Message msg = new Message();
					msg.what = LOAD_MORE_VIDEOS;
					if(tempvideos == null){
						msg.what = NO_VIDOES;
					}
					msg.obj = tempvideos;
					handler.sendMessage(msg);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(runnable);
	}
	

	private void setErrorMessage() {
		Message msg = new Message();
		msg.what = ERROR;
		Main.this.handler.sendMessage(msg);
	}

	public void onPause() {
		super.onPause();
		gallery.stopFlow();
	}

	@Override
	public void onResume() {
		super.onResume();
		gallery.startFlow();
	}

	/**
	 * 初始化各控件 本方法仅调用一次
	 */
	public void initialize() {
		performOnUIFirstLoaded();
	}

	public void alert(String msg, String Caption, final Boolean exit) {
		Builder builder = new AlertDialog.Builder(this);
		Dialog dialog = builder
				.setTitle(Caption)
				.setMessage(msg)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (exit) {
									Main.this.finish();
								}
							}
						}).create();
		dialog.show();
	}

	private void performOnUIFirstLoaded() {
		if (mOnUIFirstLoadedListener != null) {
			mOnUIFirstLoadedListener.onLoad(this);
		}
	}

	public void setOnUIFirstLoadedListener(OnUIFirstLoadedListener l) {
		mOnUIFirstLoadedListener = l;
	}

	/**
	 * 首次界面加载，有数据，并初步绘制了界面才会被触发 用于开始切换画面
	 */
	public interface OnUIFirstLoadedListener {
		void onLoad(Main context);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			onCreate(null);
		}
	}

}