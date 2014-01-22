package com.leslie.gamevideo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckNetUtil {

	public CheckNetUtil() {
	}

	public static boolean checkNetState(Context context) {
		boolean flag = false;
		NetworkInfo anetworkinfo[];
		anetworkinfo = ((ConnectivityManager) context
				.getSystemService("connectivity")).getAllNetworkInfo();
		if (anetworkinfo != null) {
			for (int i = 0; i < anetworkinfo.length; i++) {
				if (anetworkinfo[i].getState() != android.net.NetworkInfo.State.CONNECTED) {
					flag = false;
				} else if (anetworkinfo[i].getTypeName().toLowerCase()
						.equals("mobile")) {
					flag = true;
				}
			}
		}
		return flag;
	}
}
