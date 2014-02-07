package com.leslie.gamevideo.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leslie.gamevideo.AppConnect;
import com.leslie.gamevideo.R;
import com.leslie.gamevideo.adapter.YoukuVideoAdapter;
import com.leslie.gamevideo.entity.Video;
import com.leslie.gamevideo.jsonparser.YoukuParser;
import com.leslie.gamevideo.thread.ThreadPoolWrap;
import com.leslie.gamevideo.utils.Utils;

/**
 * 频道
 * 
 * @author huang
 * 
 */
@SuppressLint("HandlerLeak")
public class Channel extends Activity {
	private ListView videoListView;
	private ChannelBtnListener channelBtnListener;
	private RelativeLayout channelListLayout;
	private RelativeLayout netLoading;
	private RelativeLayout netError;
	private List<Video> videos = new ArrayList<Video>();
	LinearLayout channelbar;
	
	private static final int LOAD_MORE_VIDEOS = 22;
	private static final int NO_VIDOES = 0;
	public static List<String> channels=new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.channel);
		findAllViews(); 
		if (!Utils.checkNetworkAvalible(Channel.this)) {
			channelListLayout.removeAllViews();
			netError = (RelativeLayout) LayoutInflater.from(Channel.this)
					.inflate(R.layout.net_error, null);
			ImageView imageView = (ImageView) netError
					.findViewById(R.id.network_error_image);
			imageView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					init();
				}
			});
			channelListLayout.addView(netError, Utils.getRelativeLayoutParams());
			return;
		}
		init();
	}

	/**
	 * 设置动态加载的button属性
	 * 
	 * @param button
	 */
	private void setBtnParams(Button button) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		params.gravity = Gravity.CENTER;
		button.setLayoutParams(params);
		button.setTextSize(14);
		button.setBackgroundResource(R.drawable.menu_bg);
		button.setTextColor(this.getResources().getColor(R.color.white));

	}

	private void addChannelTags() {
		if (channels.size() == 0) {
			netLoading.setVisibility(View.GONE);
			channelListLayout.removeAllViews();
			return;
		}
		channelBtnListener = new ChannelBtnListener(getApplication(),
				videoListView, channelListLayout, netLoading, netError,channels, videos);
		int i = 100;
		for (String channel: channels) {
			Button button = new Button(Channel.this);
			button.setTag(channel);
			button.setText(channel);
			button.setId(i);
			setBtnParams(button);
			channelbar.addView(button);
			button.setOnClickListener(channelBtnListener);
			if(i == 100){
				button.setSelected(true);
			}
			i++;
		}
	}

	private void init() {
		addChannelTags();
		if (!Utils.checkNetworkAvalible(Channel.this)) {
			channelListLayout.removeAllViews();
			netError = (RelativeLayout) LayoutInflater.from(Channel.this)
					.inflate(R.layout.net_error, null);
			ImageView imageView = (ImageView) netError
					.findViewById(R.id.network_error_image);
			imageView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					init();
				}
			});
			channelListLayout.addView(netError, Utils.getRelativeLayoutParams());
			return;
		}
		channelListLayout.removeAllViews();
		channelListLayout.addView(netLoading, Utils.getRelativeLayoutParams());
	}

	private void findAllViews() {
		netLoading = (RelativeLayout) LayoutInflater.from(Channel.this)
				.inflate(R.layout.net_loading, null);
		channelbar = (LinearLayout) findViewById(R.id.channel_bar);
		netError = (RelativeLayout) LayoutInflater.from(Channel.this).inflate(
				R.layout.net_error, null);
		videoListView = (ListView) findViewById(R.id.video_list);
		channelListLayout = (RelativeLayout) findViewById(R.id.channel_list_layout);
	}

//	@Override
//	public void onBackPressed() {
//		super.onBackPressed();
//		Intent intent = new Intent("com.tangdou.action.main");
//		startActivity(intent);
//	}

	public class ChannelBtnListener implements OnClickListener,
			OnScrollListener {
		private ListView videoListView;
		private RelativeLayout channelListLayout;
		private RelativeLayout netLoading;
		private Handler handler;
		private int pageNum = 1;
		private RelativeLayout loadMoreLayout;
		private List<String> list;
		private YoukuVideoAdapter adapter;
		private List<Video> videos;
		private String channelStr;

		public ChannelBtnListener(Context context,
				final ListView videoListView,final RelativeLayout channelListLayout,
				final RelativeLayout netLoading, final RelativeLayout netError, List<String> list,
				final List<Video> tempVideos) {
			this.list = list;
			this.videoListView = videoListView;
			this.channelListLayout = channelListLayout;
			this.netLoading = netLoading;
			loadMoreLayout = (RelativeLayout) LayoutInflater.from(context)
					.inflate(R.layout.video_list_more, null);
			loadMoreLayout.setVisibility(View.GONE);
			this.videos = tempVideos;
			channelStr = list.get(0);
			videoListView.addFooterView(loadMoreLayout);
			this.handler = new Handler() {
				@SuppressWarnings("unchecked")
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					if (msg.what == 21) {
						channelListLayout.removeAllViews();
						channelListLayout.addView(videoListView);
					} else if (msg.what == LOAD_MORE_VIDEOS) {
						videos.addAll((List<Video>) msg.obj);
						adapter.notifyDataSetChanged();
					} else if (msg.what == -1) {
						channelListLayout.removeAllViews();
						channelListLayout.addView(netError,
								Utils.getRelativeLayoutParams());
					} else if (msg.what == NO_VIDOES) {
						setNoMoreVideos();
					} else if (msg.what == 88) {
						//互动广告
						if(!MainTabsActivity.is_ad_on.equalsIgnoreCase("0")){
							LinearLayout adlayout =(LinearLayout)findViewById(R.id.AdLinearLayout); 
							AppConnect.getInstance(Channel.this).showBannerAd(Channel.this, adlayout);
						}
					}
				}
			};
			new Thread(new MyThread()).start();
			handler.sendEmptyMessage(88);
			videoListView.setOnScrollListener(this);
		}

		public void onClick(View view) {
			if(videos != null){
				videos.clear();
			}
			channelListLayout.removeAllViews();
			channelListLayout.addView(netLoading,Utils.getRelativeLayoutParams());
			Activity activity = Channel.this;
			channelStr = (String) view.getTag();
			Log.i("view.getId()",""+view.getId());
			setSelected(activity,view.getId());
			pageNum = 1;
			ThreadPoolWrap.getThreadPool().executeTask(new MyThread());
		}

		private Runnable buttonRunnable = new Runnable() {
			public void run() {
				try {
					HttpClient client = new DefaultHttpClient();
					client.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpGet httpGet = new HttpGet(getChannelUrl());
					HttpResponse response = client.execute(httpGet);
					HttpEntity entity = response.getEntity();
					String results = EntityUtils.toString(entity);
					videos = YoukuParser.parse(results);
					adapter = new YoukuVideoAdapter(videos, videoListView);
					videoListView.setAdapter(adapter);
					Message msg = new Message();
					msg.what = 21;
					ChannelBtnListener.this.handler.sendMessage(msg);
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}


		};

		private String getChannelUrl() {
			String url = Channel.this.getResources().getString(R.string.url_search);
			url = url + channelStr + "?pid=69b81504767483cf&pg="+pageNum;
			return url;
		}
		
		private class MyThread extends Thread {
			public void run() {
				handler.post(buttonRunnable);
			}
		}

		private void setSelected(Activity activity, int id) {
			for (int i = 100; i < (list.size()+100); i++) {
				activity.findViewById(i).setSelected(false);
			}
			activity.findViewById(id).setSelected(true);
		}

		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
		}

		public void onScrollStateChanged(AbsListView view, int scrollState) {

			if (scrollState == SCROLL_STATE_IDLE
					&& 1 + view.getLastVisiblePosition() == view.getCount()) {
				// 判断是否有下一页
				loadMoreLayout.setVisibility(View.VISIBLE);
				pageNum++;
				loadMoreVideoList();
			}
		}

		private void setNoMoreVideos() {
			loadMoreLayout.setVisibility(View.VISIBLE);
			ProgressBar pb = (ProgressBar) loadMoreLayout
					.findViewById(R.id.list_more_progressbar);
			pb.setVisibility(View.GONE);
			TextView tv = (TextView) loadMoreLayout
					.findViewById(R.id.list_more_textview);
			tv.setText("没有更多视频");
		}

		private void loadMoreVideoList() {
			Runnable runnable = new Runnable() {
				public void run() {
					try {
						HttpClient client = new DefaultHttpClient();
						client.getParams().setParameter(
								CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
						HttpGet httpGet = new HttpGet(getChannelUrl());
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
	}

}
