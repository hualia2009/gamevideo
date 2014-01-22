package com.leslie.gamevideo.utils;

import com.leslie.gamevideo.activities.Main;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class Config {
	public static String flag = "";
	public static boolean loadThumb = true;
	public static boolean loadMore = false;
	public static int loadNum = 20;
	public static PackageInfo appInfo;
	public static Main main = null;
	public static Controller ctrl;
	public static String dbName = "main";
	public static int dbVersion = 2;
	public static int maxSearchHistory = 10;
	public static int maxFavorite = 30;
	public static int maxVideoHistory = 30;
	public static String uid = "";

	public static void init() {
		if (main != null) {
			try {
				appInfo = main.getPackageManager().getPackageInfo(
						main.getPackageName(), 0);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}

			SharedPreferences setting = main.getSharedPreferences("setting", 0);
			loadThumb = setting.getBoolean("load_thumb", true);
			loadMore = setting.getBoolean("load_more", false);
			loadNum = setting.getInt("load_num", 20);
		}
	}
}
