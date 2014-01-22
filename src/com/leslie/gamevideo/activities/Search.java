package com.leslie.gamevideo.activities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leslie.gamevideo.AppConnect;
import com.leslie.gamevideo.R;
import com.leslie.gamevideo.adapter.HotKeyWordsAdapter;
import com.leslie.gamevideo.adapter.YoukuVideoAdapter;
import com.leslie.gamevideo.entity.Video;
import com.leslie.gamevideo.jsonparser.YoukuParser;
import com.leslie.gamevideo.listener.BindTopbarBtnListener;
import com.leslie.gamevideo.thread.ThreadPoolWrap;
import com.leslie.gamevideo.utils.Utils;

/**
 * 搜索
 * 
 * @author huangl
 * 
 */
// e.getMessage()
@SuppressLint("HandlerLeak")
public class Search extends Activity {

	private EditText searchContent;
	private Button searchButton;
	private ImageButton searchDelete;
	private ListView listView;
	private static final int ERROR = -1;

	private static final int LOAD_VIDEOS = 41;
	private static final int LOAD_MORE_VIDEOS = 22;
	private static final int LOAD_HOT_KEYWORDS = 23;
	private static final int NO_VIDOES = 0;

	private InputMethodManager imm;
	/** 热门词汇的容器 **/
	private RelativeLayout searchListLayout;
	private RelativeLayout netLoading;
	private Handler handler;

	private LinearLayout layoutKong;
	private int pageNum = 1;
	private RelativeLayout loadMoreLayout;
	private YoukuVideoAdapter adapter;
	//private VideoCollectionInfo videos = new VideoCollectionInfo();
	private List<Video> videos;
	private ArrayList<String> HotKeylist = new ArrayList<String>();
	private GridView gridView;
	private LinearLayout layoutHotKey;
	private TextView totalSearch;
	private RelativeLayout netError;
	private Toast toast;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		findViews();
		
//		AppConnect.getInstance(this).setAdBackColor(Color.argb(50, 120, 240, 120)); //设置迷你广告广告语颜色 
//		AppConnect.getInstance(this).setAdForeColor(Color.YELLOW); //若未设置以上两个颜色,则默认为黑底白字
		if(!MainTabsActivity.is_ad_on.equalsIgnoreCase("0")){
			LinearLayout miniLayout =(LinearLayout)findViewById(R.id.miniAdLinearLayout); 
			AppConnect.getInstance(this).showMiniAd(this, miniLayout, 10); //默认 10 秒切换一次广告
		}
		LogoActivity.activities.add(Search.this);
		BindTopbarBtnListener.bindAllBtnListener(this);

		loadMoreLayout.setVisibility(View.GONE);
		listView.addFooterView(loadMoreLayout);
		listView.setOnScrollListener(new scrollListener());
		// 判断网络情况
		handler = new Handler() {
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == LOAD_VIDEOS && msg.obj != null) {
					listView.setAdapter((YoukuVideoAdapter) msg.obj);
					searchListLayout.removeAllViews();
					searchListLayout.addView(listView);
					searchListLayout.addView(totalSearch);
				} else if (msg.what == LOAD_MORE_VIDEOS) {
					// 分页
					videos.addAll((List<Video>) msg.obj);
					adapter.notifyDataSetChanged();
					totalSearch.setText("共搜索到" + videos.size() + "个结果");
				} else if (msg.what == LOAD_HOT_KEYWORDS) {
					// 加载热门搜索词
					layoutHotKey.setVisibility(View.VISIBLE);
					searchListLayout.removeAllViews();
				} else if (msg.what == ERROR) {
					searchListLayout.removeAllViews();
				} else if(msg.what == NO_VIDOES){
					setNoMoreVideos();
				} else {
					// 搜索结果为空
					searchListLayout.removeAllViews();
					searchListLayout.addView(layoutKong,
							Utils.getRelativeLayoutParams());
				}
			}
		};
		if (!Utils.checkNetworkAvalible(Search.this)) {
			searchListLayout.removeAllViews();
			netError = (RelativeLayout) LayoutInflater.from(Search.this)
					.inflate(R.layout.net_error, null);
			ImageView imageView = (ImageView) netError
					.findViewById(R.id.network_error_image);
			imageView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					init();
				}
			});
			searchListLayout.addView(netError, Utils.getRelativeLayoutParams());
			return;
		}
		init();
	}

	private void init() {
		if (!Utils.checkNetworkAvalible(Search.this)) {
			searchListLayout.removeAllViews();
			netError = (RelativeLayout) LayoutInflater.from(Search.this)
					.inflate(R.layout.net_error, null);
			ImageView imageView = (ImageView) netError
					.findViewById(R.id.network_error_image);
			imageView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					init();
				}
			});
			searchListLayout.addView(netError, Utils.getRelativeLayoutParams());
			return;
		}
		// 加载热门搜索词
		searchListLayout.removeAllViews();
		searchListLayout.addView(netLoading, Utils.getRelativeLayoutParams());
		loadHotKeyWords();
		gridView.setAdapter(new HotKeyWordsAdapter(HotKeylist, Search.this));
		// 点击热门关键词触发事件
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view,
					int arg2, long arg3) {
				searchContent.setText((CharSequence) adapterView
						.getItemAtPosition(arg2));
				// 设置光标
				searchContent.setSelection(searchContent.getText().length());
				search();
			}
		});

		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		// 点击“搜索”后触发事件
		searchButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				synchronized (searchButton) {
					if (searchContent.getText().toString().trim().equals("")) {
						loadHotKeyWords();
						if (toast == null) {
							toast = Toast.makeText(Search.this, "请输入内容",
									Toast.LENGTH_SHORT);
						}
						toast.show();
						return;
					}
					imm.hideSoftInputFromWindow(searchContent.getWindowToken(),
							0);
					pageNum = 1;
					search();
				}
			}
		});
		
		searchDelete.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				searchContent.getText().clear();
				loadHotKeyWords();
			}
		});
	}

	/**
	 * 查找所有的view
	 */
	private void findViews() {
		// 加载中layout
		loadMoreLayout = (RelativeLayout) LayoutInflater.from(Search.this)
				.inflate(R.layout.video_list_more, null);
		layoutKong = (LinearLayout) LayoutInflater.from(Search.this).inflate(
				R.layout.search_no_result, null);
		// 输入框相关
		searchContent = (EditText) findViewById(R.id.searchContent);
		searchButton = (Button) findViewById(R.id.searchButton);
		searchDelete = (ImageButton) findViewById(R.id.searchDelete);
		// 中间list
		netLoading = (RelativeLayout) LayoutInflater.from(Search.this).inflate(
				R.layout.net_loading, null);
		searchListLayout = (RelativeLayout) findViewById(R.id.search_list_layout);
		listView = (ListView) findViewById(R.id.search_video_list);
		// 热门搜索
		gridView = (GridView) findViewById(R.id.gridView);
		layoutHotKey = (LinearLayout) findViewById(R.id.hotkeyLine);
		// 总个数
		totalSearch = (TextView) findViewById(R.id.total_txt_search);
	}

	private void search() {
		layoutHotKey.setVisibility(View.GONE);
		loadMoreLayout.setVisibility(View.GONE);
		searchListLayout.removeAllViews();
		searchListLayout.addView(netLoading, Utils.getRelativeLayoutParams());
		ThreadPoolWrap.getThreadPool().executeTask(new MyThread());
	}

	private Runnable searchRunnable = new Runnable() {
		public void run() {
			try {
				HttpClient client = new DefaultHttpClient();
				client.getParams().setParameter(
						CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
				HttpGet httpGet = new HttpGet(getSearchUrl());
				HttpResponse response = client.execute(httpGet);
				HttpEntity entity = response.getEntity();
				String results = EntityUtils.toString(entity);
				videos = YoukuParser.parse(results);
				totalSearch.setText("共搜索到" + videos.size() + "个结果");
				adapter = new YoukuVideoAdapter(videos, listView);
				listView.setAdapter(adapter);
				Message msg = new Message();
				if (adapter != null && adapter.getCount() > 0) {
					msg.what = LOAD_VIDEOS;
				} else {
					msg.what = NO_VIDOES;
				}
				msg.obj = adapter;
				Search.this.handler.sendMessage(msg);
			} catch (MalformedURLException e) {
				sendErrorMessage();
			} catch (IOException e) {
				sendErrorMessage();
			}
		}

	};

	private void sendErrorMessage() {
		Message msg = new Message();
		msg.what = ERROR;
		Search.this.handler.sendMessage(msg);
	}

	private class MyThread implements Runnable {
		public void run() {
			Search.this.handler.post(searchRunnable);
		}
	}

	private void loadMoreVideoList() {
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					HttpClient client = new DefaultHttpClient();
					client.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpGet httpGet = new HttpGet(getSearchUrl());
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
					Search.this.handler.sendMessage(msg);
				} catch (MalformedURLException e) {
					Log.e("Main", "MalformedURLException");
				} catch (IOException e) {
					Log.e("Main", "IOException" );
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(runnable);
	}

	private String getSearchUrl() {
		String url = Search.this.getResources().getString(R.string.url_search);
		url = url + URLEncoder.encode(searchContent.getText() + "") + "?pid=69b81504767483cf&pg="+pageNum;
		return url;
	}

	private class scrollListener implements OnScrollListener {

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
	}

	private void loadHotKeyWords() {
		HotKeylist.clear();
		HotKeylist.add(AppConnect.getInstance(this).getConfig("search_key1", "2009"));
		HotKeylist.add(AppConnect.getInstance(this).getConfig("search_key2", "pis"));
		HotKeylist.add(AppConnect.getInstance(this).getConfig("search_key3", "Dota2"));
		HotKeylist.add(AppConnect.getInstance(this).getConfig("search_key4", "LOL"));
		HotKeylist.add(AppConnect.getInstance(this).getConfig("search_key5", "SKY"));
		HotKeylist.add(AppConnect.getInstance(this).getConfig("search_key6", "星际"));
		HotKeylist.add(AppConnect.getInstance(this).getConfig("search_key7", "KOF"));
		HotKeylist.add(AppConnect.getInstance(this).getConfig("search_key8", "魔兽世界"));
		HotKeylist.add(AppConnect.getInstance(this).getConfig("search_key9", "我叫MT"));
		HotKeylist.add(AppConnect.getInstance(this).getConfig("search_key10", "暗黑破坏神"));
		handler.sendEmptyMessage(LOAD_HOT_KEYWORDS);
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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(Search.this, MainTabsActivity.class);
		startActivity(intent);
	}

}
