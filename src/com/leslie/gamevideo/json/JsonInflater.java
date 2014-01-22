package com.leslie.gamevideo.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.leslie.gamevideo.entity.VideoCollectionInfo;

public class JsonInflater {
	static String TAG = "JsonInflater";
	private int next = 1;
	private int total = 0;
	String content;

	public JsonInflater(String content) {
		this.content = content;
	}

	public int getNext() {
		return this.next;
	}

	public void setNext(int next) {
		this.next = next;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public void inflat(VideoCollectionInfo tempvideos) {

		if (tempvideos == null || content == null) {
			Log.i(TAG, "::inflat No Item");
			return;
		}

		// 循环遍历reader，然后设置item
		try {
			if (content.length() <= 3) {
				return;
			}
			JSONObject indexJson = new JSONObject(content);
			if (indexJson.has("next")) {
				setNext(indexJson.getInt("next"));
			}
			if (indexJson.has("total")) {
				setTotal(indexJson.getInt("total"));
			}
			JSONArray videosJsonArray = indexJson.getJSONArray("videos");
			for (int i = 0; i < videosJsonArray.length(); i++) {
				tempvideos.setData(videosJsonArray.getJSONObject(i));
			}
		} catch (JSONException e) {
			Log.e(TAG, "JSONException");
		}
	}

}
