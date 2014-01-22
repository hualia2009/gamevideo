package com.leslie.gamevideo.activities;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.leslie.gamevideo.R;
import com.leslie.gamevideo.adapter.CollectListAdatpter;
import com.leslie.gamevideo.db.MainDbHelper;
import com.leslie.gamevideo.entity.Video;

/**
 * 我的糖豆-》收藏
 * 
 * @author huangl
 * 
 */
public class Favorite extends BaseActivity {

	private static final int EDIT_DISABLE = 0;
	private ListView favoriteVideoList;
	private LinearLayout favorite_layout;
	private CollectListAdatpter adapter;
	private MainDbHelper dbHelper;
	private Button deleteButton;
	private Button clearButton;
	private Handler handler;

	@SuppressLint("HandlerLeak")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favorite);
		favoriteVideoList = (ListView) findViewById(R.id.favorite_video_list);
		deleteButton = (Button) findViewById(R.id.deleteButton);
		clearButton = (Button) findViewById(R.id.favourite_clear);
		favorite_layout = (LinearLayout) findViewById(R.id.favourite_list_layout);
		setTopMenu();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == EDIT_DISABLE) {// 编辑不可用
					setEditDisable();
				}
			}
		};

		dbHelper = MainDbHelper.getInstance(Favorite.this);
		final List<Video> videos = dbHelper.getFavoriteVideos();
		if (videos != null && !videos.isEmpty()) {
			adapter = new CollectListAdatpter(videos, Favorite.this,
					favoriteVideoList, handler);
			favoriteVideoList.setAdapter(adapter);
			favoriteVideoList.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					if (deleteButton.getText().toString().trim().equals("完成")) {
						dbHelper.deleteFavoriteById(videos.get(position)
								.getId());
						videos.remove(position);
						adapter.notifyDataSetChanged();
						if (videos == null || videos.isEmpty()) {
							setEditDisable();
						}
						return;
					}
					Intent intent = new Intent(Favorite.this, Detail.class);
					Bundle extras = new Bundle();
					extras.putSerializable("info", videos.get(position));
					intent.putExtras(extras);
					Favorite.this.startActivity(intent);
				}
			});
		}
		deleteButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 1.当没有收藏数据时
				if (videos != null && !videos.isEmpty()) {
					if (deleteButton.getText().toString().trim().equals("完成")) {
						deleteButton.setText("编辑");
						adapter.setIsDelete(false);
						adapter.notifyDataSetChanged();
					} else {
						deleteButton.setText("完成");
						adapter.setIsDelete(true);
						adapter.notifyDataSetChanged();
					}
				}
			}
		});
		if (videos == null || videos.isEmpty()) {
			setEditDisable();
		}
		clearButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (videos == null || videos.isEmpty()) {
					(new android.app.AlertDialog.Builder(Favorite.this))
							.setMessage("没有历史记录")
							.setCancelable(false)
							.setPositiveButton(
									"确定",
									new android.content.DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialoginterface,
												int i) {
											dialoginterface.dismiss();
										}
									}).create().show();
				} else {
					(new android.app.AlertDialog.Builder(Favorite.this))
							.setMessage("确认要清除全部吗")
							.setPositiveButton(
									"确定",
									new android.content.DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialoginterface,
												int i) {
											MainDbHelper dbHelper = MainDbHelper
													.getInstance(Favorite.this);
											dbHelper.clearFavorite();
											favorite_layout.removeAllViews();
											videos.clear();
											handler.sendEmptyMessage(0);
										}
									})
							.setNegativeButton(
									"取消",
									new android.content.DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialoginterface,
												int i) {
										}
									}).create().show();
				}

			}
		});
		if (videos == null || videos.isEmpty()) {
			setEditDisable();
		}

	}

	/**
	 * 编辑不可用
	 */
	private void setEditDisable() {
		deleteButton.setClickable(false);
		deleteButton.setText("编辑");
		deleteButton.setBackgroundResource(R.drawable.btn_red_disabled);
		clearButton.setClickable(false);
		clearButton.setBackgroundResource(R.drawable.btn_red_disabled);
	}

	@Override
	public void onRestart() {
		super.onRestart();
	}

	// public void onBackPressed() {
	// super.onBackPressed();
	// this.finish();
	// }

	@Override
	protected void setTitles() {
		title.setText("收藏");
	}

}
