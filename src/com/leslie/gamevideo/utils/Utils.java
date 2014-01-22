package com.leslie.gamevideo.utils;

import java.io.InputStream;
import java.util.Formatter;
import java.util.Locale;
import java.util.Set;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class Utils {
	private static final String TAG = "Utils";

	public static String getNetworkType(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			// 获取网络类型失败
			Log.e(TAG, "No Network");
			return null;
		} else {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info == null) {
				// 获取网络类型失败
				Log.e(TAG, "No Network");
				return null;
			}

			if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
				TelephonyManager tm = (TelephonyManager) Config.main
						.getSystemService(Context.TELEPHONY_SERVICE);
				int type = tm.getNetworkType();
				switch (type) {
				case TelephonyManager.NETWORK_TYPE_UMTS:
					return "UMTS";
				case TelephonyManager.NETWORK_TYPE_EDGE:
					return "EDGE";
				case TelephonyManager.NETWORK_TYPE_CDMA:
					return "CDMA";
				case TelephonyManager.NETWORK_TYPE_GPRS:
					return "GPRS";
				case TelephonyManager.NETWORK_TYPE_UNKNOWN:
					return "unknown";
				}
			} else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
				return "WIFI";
			}
		}

		Log.e(TAG, "No Network");
		return null;
	}

	public static boolean checkNetworkAvalible(Context context) {
		boolean flag;
		if (context != null) {
			if (((WifiManager) context.getSystemService("wifi")).getWifiState() != 3) {
				NetworkInfo networkinfo = ((ConnectivityManager) context
						.getSystemService("connectivity"))
						.getActiveNetworkInfo();
				if (networkinfo == null || !networkinfo.isAvailable())
					flag = false;
				else
					flag = true;
			} else {
				flag = true;
			}
		} else {
			flag = false;
		}
		return flag;
	}

	public static LayoutParams getRelativeLayoutParams() {
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		return lp;
	}

	public static android.widget.LinearLayout.LayoutParams getLinearLayoutParams() {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER;
		return lp;
	}

	public static String getUrl(int url, Bundle b) {
		String sUrl = Config.main.getString(url);
		return getUrl(sUrl, b);
	}

	public static String getUrl(String url, Bundle b) {
		url = url.replaceAll("%version%", "" + getVersion());

		if (b != null) {
			Set<String> keys = b.keySet();
			for (String key : keys) {
				url = url.replaceFirst("%" + key + "%", b.getString(key));
			}
		}

		return url;
	}

	private static int getVersion() {
		return Config.appInfo != null ? Config.appInfo.versionCode : 0;
	}

	public static int getVerCode(Context context) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(
					"com.leslie.gamevideo", 0).versionCode;
		} catch (NameNotFoundException e) {
			Log.e(TAG, "NameNotFoundException");
		}
		return verCode;
	}

	public static String getVerName(Context context) {

		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(
					"com.leslie.gamevideo", 0).versionName;
		} catch (NameNotFoundException e) {
			Log.e(TAG, "NameNotFoundException");
		}
		return verName;

	}

	public static Bitmap readBitMap(InputStream inputstream) {
		Bitmap bitmap = null;
		android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
		options.inPreferredConfig = android.graphics.Bitmap.Config.RGB_565;
		options.inPurgeable = true;
		options.inInputShareable = true;
		bitmap = BitmapFactory.decodeStream(inputstream, null, options);
		return bitmap;
	}

	/**
	 * 时间处理
	 * 
	 * @param duration
	 * @return
	 */
	public static String formatDuration(int duration) {
		int min = duration / 60;
		int sec = duration % 60;
		if (sec < 10) {
			return min + ":0" + sec;
		}
		return min + ":" + sec;
	}
	
	public static int durationInt(String duration){
		String[] times = duration.split(":");
		if(times.length == 0){
			return 0;
		}
		if(times.length == 1){
			return Integer.parseInt(times[0]);
		}
		if(times.length == 2){
			return Integer.parseInt(times[0])*60+Integer.parseInt(times[1]);
		}
		if(times.length == 3){
			return Integer.parseInt(times[0])*3600+Integer.parseInt(times[1])*60+Integer.parseInt(times[2]);
		}
		return 0;
	}

	public static int checkNetState(Context context) {
		NetworkInfo anetworkinfo[];
		anetworkinfo = ((ConnectivityManager) context
				.getSystemService("connectivity")).getAllNetworkInfo();
		if (anetworkinfo != null) {
			for (int i = 0; i < anetworkinfo.length; i++) {
				if (anetworkinfo[i].getState() != android.net.NetworkInfo.State.CONNECTED) {
					return -1;
				} else if (anetworkinfo[i].getTypeName().toLowerCase()
						.equals("mobile")) {
					return 1;
				}
			}
		}
		return 2;
	}

	public static int getProgress(long range, long size) {
		return Math.round(range * 1.0f / size * 1.0f * 100.0f);
	}

	public static String stringForTime(int i) {
		int l = i / 1000;
		int k = l % 60;
		int j = (l / 60) % 60;
		l /= 3600;
		String result;
		StringBuilder mFormatBuilder = new StringBuilder();
		Formatter mFormatter = new Formatter(mFormatBuilder,
				Locale.getDefault());
		mFormatBuilder.setLength(0);
		if (l <= 0) {
			Object aobj[] = new Object[2];
			aobj[0] = Integer.valueOf(j);
			aobj[1] = Integer.valueOf(k);
			result = mFormatter.format("%02d:%02d", aobj).toString();
		} else {
			Formatter formatter = mFormatter;
			Object aobj1[] = new Object[3];
			aobj1[0] = Integer.valueOf(l);
			aobj1[1] = Integer.valueOf(j);
			aobj1[2] = Integer.valueOf(k);
			result = formatter.format("%d:%02d:%02d", aobj1).toString();
		}
		return result;
	}

}
