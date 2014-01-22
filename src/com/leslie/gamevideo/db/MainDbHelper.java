package com.leslie.gamevideo.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.leslie.gamevideo.entity.Video;
import com.leslie.gamevideo.entity.VideoCollectionInfo;
import com.leslie.gamevideo.entity.VideoInfo;
import com.leslie.gamevideo.utils.Config;

/**
 * 封装了全部数据库操作
 * 
 * @author Administrator
 * 
 */
public class MainDbHelper extends SQLiteOpenHelper {
	
	/**表名**/
	public static final String TABLE_SEARCH_HISTORY = "search_history";
	public static final String TABLE_VIDEO_HISTORY = "video_history";
	public static final String TABLE_VIDEO_FAVORITE = "video_favorite";

	/**字段名**/
	public static final String KEY_ID = "_id";
	public static final String KEY_KEYWORD = "keyword";
	public static final String KEY_VIDEO_ID = "videoid";
	public static final String KEY_TITLE = "title";
	public static final String KEY_VIDEO_FLAG = "videoflag";
	public static final String KEY_PLAY_URL_H = "playurlh";
	public static final String KEY_PLAY_TIMES = "playtimes";
	public static final String KEY_AUTHOR = "author";
	public static final String KEY_DURATION = "duration";
	public static final String KEY_DIGG = "digg";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_CHANNEL = "channel";
	public static final String KEY_THUMBNAIL = "thumbnail";
	public static final String KEY_LAST_UPDATE = "lastupdate";

	private static MainDbHelper instance;

	private MainDbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, Config.dbName, factory, version);
	}

	private MainDbHelper(Context context) {
		super(context, Config.dbName, null, Config.dbVersion);
	}

	public static MainDbHelper getInstance(Context context) {
		if (instance == null) {
			instance = new MainDbHelper(context);
		}
		return instance;
	}

	/**
	 * 创建数据表
	 */
	@Override
	public void onCreate(SQLiteDatabase sqlitedatabase) {
		String sql1 = "CREATE TABLE " + TABLE_SEARCH_HISTORY + " (" + KEY_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_KEYWORD
				+ " TEXT, " + KEY_LAST_UPDATE + " DATE)";
		sqlitedatabase.execSQL(sql1);

		String	sql2 = "CREATE TABLE " + TABLE_VIDEO_HISTORY + " (" + KEY_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_VIDEO_ID
				+ "  VERCHAR, " + KEY_TITLE + " TEXT, " + KEY_VIDEO_FLAG
				+ " TEXT, " + KEY_PLAY_URL_H + " TEXT, " + KEY_PLAY_TIMES
				+ " INTEGER, " + KEY_AUTHOR + " TEXT, " + KEY_DURATION
				+ " TEXT, " + KEY_DIGG + " INTEGER, " + KEY_DESCRIPTION
				+ " TEXT, " + KEY_CHANNEL + " TEXT, " + KEY_THUMBNAIL
				+ " TEXT, " + KEY_LAST_UPDATE + " DATE)";
		sqlitedatabase.execSQL(sql2);

		String	sql3 = "CREATE TABLE " + TABLE_VIDEO_FAVORITE + " (" + KEY_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_VIDEO_ID
				+ "  VERCHAR, " + KEY_TITLE + " TEXT, " + KEY_VIDEO_FLAG
				+ " TEXT, " + KEY_PLAY_URL_H + " TEXT, " + KEY_PLAY_TIMES
				+ " INTEGER, " + KEY_AUTHOR + " TEXT, " + KEY_DURATION
				+ " TEXT, " + KEY_DIGG + " INTEGER, " + KEY_DESCRIPTION
				+ " TEXT, " + KEY_CHANNEL + " TEXT, " + KEY_THUMBNAIL
				+ " TEXT, " + KEY_LAST_UPDATE + " DATE)";
		sqlitedatabase.execSQL(sql3);


	}

	/**
	 * 每个版本的数据库自动更新部分，从版本 1 直到当前版本的转换程序
	 */
	@Override
	public void onUpgrade(SQLiteDatabase sqlitedatabase, int oldVersion, int newVersion) {
		// do nothing for version 1
		if (newVersion > oldVersion)
		{
			
		}
	}

	/**
	 * 增加一个搜索关键字
	 * 
	 * @param keyword
	 */
	public void addKeyword(String keyword) {
		String sql;
		SQLiteDatabase db;
		Cursor cursor;
		boolean exists = true;

		db = this.getReadableDatabase();
		try {
			cursor = db.query(TABLE_SEARCH_HISTORY, new String[] { KEY_ID },
					KEY_KEYWORD + "=" + keyword, null, null, null, null);
			exists = (cursor.getCount() != 0);

			if (exists) {
				ContentValues values = new ContentValues();
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date date = new Date();
				values.put(KEY_LAST_UPDATE, dateFormat.format(date));
				db.update(TABLE_SEARCH_HISTORY, values, KEY_KEYWORD + "="
						+ keyword, null);
			} else {
				sql = "DELETE FROM " + TABLE_SEARCH_HISTORY + " ORDER BY "
						+ KEY_LAST_UPDATE + " DESC LIMIT 1 OFFSET "
						+ (Config.maxSearchHistory - 1);
				db.execSQL(sql);

				ContentValues values = new ContentValues();
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date date = new Date();

				values.put(KEY_LAST_UPDATE, dateFormat.format(date));
				values.put(KEY_KEYWORD, keyword);

				db.insert(TABLE_SEARCH_HISTORY, null, values);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 增加观看历史，重复观看则会将最近观看的放到前列
	 * 
	 * @param info
	 */
	public void addHistory(Video info) {
		SQLiteDatabase db;
		String videoId = "";
		boolean exists = true;

		videoId = info.getId();

		try {
			exists = inHistory(videoId);

			if (exists) {
				ContentValues values = new ContentValues();
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date date = new Date();
				values.put(KEY_LAST_UPDATE, dateFormat.format(date));
				db = this.getReadableDatabase();
				db.update(TABLE_VIDEO_HISTORY, values, KEY_VIDEO_ID + "='"
						+ videoId+"'", null);
				db.close();
			} else {
				addVideo(info, TABLE_VIDEO_HISTORY, Config.maxVideoHistory);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 增加收藏。重复收藏不管
	 * 
	 * @param info
	 * @return
	 */
	public boolean addFavorite(Video info) {
		String videoId = "";
		boolean exists = true;
		boolean isAddSuccess = false;

		videoId = info.getId();

		try {
			exists = inFavorite(videoId);
			if (!exists) {
				isAddSuccess = addVideo(info, TABLE_VIDEO_FAVORITE,
						Config.maxFavorite);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isAddSuccess;
	}

	/**
	 * 增加视频信息
	 * 
	 * @param info
	 * @param tableName
	 * @param maxRows
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public boolean addVideo(Video info, String tableName, int maxRows) {
		SQLiteDatabase db;

		db = this.getReadableDatabase();

		ContentValues values = new ContentValues();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date = new Date();

		values.put(KEY_VIDEO_ID, info.getId());
		values.put(KEY_TITLE, info.getTitle());
		values.put(KEY_VIDEO_FLAG, info.getVideoflag());
		values.put(KEY_PLAY_URL_H, "");
		values.put(KEY_PLAY_TIMES, info.getPlaytimes());
		values.put(KEY_AUTHOR, "");
		values.put(KEY_DURATION, info.getDuration());
		values.put(KEY_DIGG, "");
		values.put(KEY_DESCRIPTION, info.getDescription());
		values.put(KEY_CHANNEL, info.getChannel());
		values.put(KEY_THUMBNAIL, info.getThumbnail());
		values.put(KEY_LAST_UPDATE, dateFormat.format(date));

		db.insert(tableName, null, values);
		db.close();
		return true;
	}

	/**
	 * 清空历史记录
	 */
	public void clearHistory() {
		deleteAllVideos(TABLE_VIDEO_HISTORY);
	}

	public void clearFavorite() {
		deleteAllVideos(TABLE_VIDEO_FAVORITE);
	}

	public void deleteFavoriteById(String id) {
		deleteVideo(id, TABLE_VIDEO_FAVORITE);
	}

	public void deleteHistoryById(String id) {
		deleteVideo(id, TABLE_VIDEO_HISTORY);
	}

	/**
	 * 根据id删除单个视频
	 * 
	 * @param info
	 * @param tableName
	 * @return
	 */
	public boolean deleteVideo(String id, String tableName) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "DELETE FROM " + tableName + " WHERE " + KEY_VIDEO_ID
				+ "='" + id + "'";
		Log.i("dbhelper", ""+sql);
		db.execSQL(sql);
		return true;
	}

	/**
	 * 删除所有视频
	 * 
	 * @param tableName
	 * @return
	 */
	public void deleteAllVideos(String tableName) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "DELETE FROM " + tableName;
		db.execSQL(sql);
	}

	/**
	 * 读关键字列表
	 * 
	 * @return
	 */
	public Cursor getKeywords() {
		return getList(TABLE_SEARCH_HISTORY);
	}

	/**
	 * 读播放历史列表
	 * 
	 * @return
	 */
	public Cursor getVideoHistories() {
		return getList(TABLE_VIDEO_HISTORY);
	}

	/**
	 * 读视频收藏列表
	 * 
	 * @return
	 */
	public Cursor getVideoFavorites() {
		return getList(TABLE_VIDEO_FAVORITE);
	}

	/**
	 * 读出全部信息
	 * 
	 * @param tableName
	 * @return
	 */
	private Cursor getList(String tableName) {
		SQLiteDatabase db;
		db = this.getReadableDatabase();
		return db.query(tableName, null, null, null, null, null,
				KEY_LAST_UPDATE + " DESC");
	}

//	/**
//	 * 更新表中存在的视频信息
//	 * 
//	 * @param videoId
//	 * @param views
//	 * @param diggs
//	 */
//	public void updateVideoInfo(String videoId, int views, int diggs) {
//		SQLiteDatabase db;
//		db = this.getReadableDatabase();
//		ContentValues value = new ContentValues();
//		value.put(KEY_PLAY_TIMES, views);
//		value.put(KEY_DIGG, diggs);
//
//		db.update(TABLE_VIDEO_HISTORY, value, KEY_VIDEO_ID + "=" + videoId,
//				null);
//		db.update(TABLE_VIDEO_FAVORITE, value, KEY_VIDEO_ID + "=" + videoId,
//				null);
//	}

	/**
	 * 是否已收藏
	 * 
	 * @param videoId
	 * @return
	 */
	public boolean inFavorite(String videoId) {
		return videoInTable(videoId, TABLE_VIDEO_FAVORITE);
	}

	/**
	 * 是否已观看过
	 * 
	 * @param videoId
	 * @return
	 */
	public boolean inHistory(String videoId) {
		return videoInTable(videoId, TABLE_VIDEO_HISTORY);
	}

	/**
	 * 在某表中是否存在视频信息
	 * 
	 * @param videoId
	 * @param tableName
	 * @return
	 */
	private boolean videoInTable(String videoId, String tableName) {
		SQLiteDatabase db;
		db = this.getReadableDatabase();
		Cursor cursor;
		cursor = db.query(tableName, null, KEY_VIDEO_ID
				+ "='" + videoId+"'", null, null, null, null);
		boolean flag = false;
		if (cursor != null && cursor.getCount() != 0) {
			flag = true;
		}
		cursor.close();
		db.close();
		return flag;
	}

	public List<Video> getFavoriteVideos() {
		SQLiteDatabase db;
		db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_VIDEO_FAVORITE, null, null, null, null,
				null, KEY_LAST_UPDATE + " DESC");
		List<Video> videos = new ArrayList<Video>();
		if (cursor == null || cursor.getCount() == 0) {
			cursor.close();
			db.close();
			return new ArrayList<Video>();
		}
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			String id = cursor.getString(cursor
					.getColumnIndex(MainDbHelper.KEY_VIDEO_ID));
			String title = cursor.getString(cursor
					.getColumnIndex(MainDbHelper.KEY_TITLE));
			String videoFlag = cursor.getString(cursor
					.getColumnIndex(MainDbHelper.KEY_VIDEO_FLAG));
			String play_times = cursor.getString(cursor
					.getColumnIndex(MainDbHelper.KEY_PLAY_TIMES));
			String duration = cursor.getString(cursor
					.getColumnIndex(MainDbHelper.KEY_DURATION));
			String description = cursor.getString(cursor
					.getColumnIndex(MainDbHelper.KEY_DESCRIPTION));
			String channel = cursor.getString(cursor
					.getColumnIndex(MainDbHelper.KEY_CHANNEL));
			String thumbnail = cursor.getString(cursor
					.getColumnIndex(MainDbHelper.KEY_THUMBNAIL));
			Video video = new Video();
			video.setId(id + "");
			video.setTitle(title);
			video.setVideoflag(videoFlag);
			video.setPlaytimes(play_times);
			video.setDuration(duration + "");
			video.setDescription(description);
			video.setChannel(channel);
			video.setThumbnail(thumbnail);
			videos.add(video);
		}
		cursor.close();
		db.close();
		return videos;
	}

	public List<Video> getHistoryVideosInfo() {
		SQLiteDatabase db;
		db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_VIDEO_HISTORY, null, null, null, null,
				null, KEY_LAST_UPDATE + " DESC");
		List<Video> videos = new ArrayList<Video>();
		if (cursor == null || cursor.getCount() == 0) {
			cursor.close();
			db.close();
			return new ArrayList<Video>();
		}
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			String id = cursor.getString(cursor
					.getColumnIndex(MainDbHelper.KEY_VIDEO_ID));
			String title = cursor.getString(cursor
					.getColumnIndex(MainDbHelper.KEY_TITLE));
			String videoFlag = cursor.getString(cursor
					.getColumnIndex(MainDbHelper.KEY_VIDEO_FLAG));
			String play_times = cursor.getString(cursor
					.getColumnIndex(MainDbHelper.KEY_PLAY_TIMES));
			String duration = cursor.getString(cursor
					.getColumnIndex(MainDbHelper.KEY_DURATION));
			String description = cursor.getString(cursor
					.getColumnIndex(MainDbHelper.KEY_DESCRIPTION));
			String channel = cursor.getString(cursor
					.getColumnIndex(MainDbHelper.KEY_CHANNEL));
			String thumbnail = cursor.getString(cursor
					.getColumnIndex(MainDbHelper.KEY_THUMBNAIL));
			Video video = new Video();
			video.setId(id + "");
			video.setTitle(title);
			video.setVideoflag(videoFlag);
			video.setPlaytimes(play_times);
			video.setDuration(duration + "");
			video.setDescription(description);
			video.setChannel(channel);
			video.setThumbnail(thumbnail);
			videos.add(video);
		}
		cursor.close();
		db.close();
		return videos;
	}
}
