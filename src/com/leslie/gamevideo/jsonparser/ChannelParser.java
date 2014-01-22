package com.leslie.gamevideo.jsonparser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

import com.leslie.gamevideo.entity.ChannelVideo;
import com.leslie.gamevideo.entity.ChannelVideoList;
import com.leslie.gamevideo.utils.JsonParseUtil;
import com.leslie.gamevideo.utils.Utils;

/**
 * 频道视频资源解析类
 * @author liz
 *
 */
public class ChannelParser {
	public static ChannelVideoList parse(String returnData) {
		
		ChannelVideoList channels = new ChannelVideoList();
		if (JsonParseUtil.isEmptyOrNull(returnData)) {
			return null;
		}
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(returnData);
			if (jsonObject.has("next")) {
				int next = jsonObject.getInt("next");
				channels.setNext(next);
			}
			String videoList = jsonObject.getString("videos");
			if (JsonParseUtil.isEmptyOrNull(videoList)) {
				return null;
			}
			JSONArray jsonArray = new JSONArray(videoList);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject videoJson = (JSONObject) jsonArray.opt(i);
				ChannelVideo video = new ChannelVideo();
				video.setId(videoJson.getString("mid"));
				video.setTitle(videoJson.getString("title"));
				video.setPlaytimes(videoJson.getString("viewcount"));
				video.setVideoflag(videoJson.getString("videoflag"));
				int duration = Integer
						.parseInt(videoJson.getString("duration"));
				video.setDuration(Utils.formatDuration(duration));
				video.setDescription(videoJson.getString("description"));
				video.setChannel(videoJson.getString("channel"));
				video.setThumbnail(videoJson.getString("normalthumbnail"));
				video.setSmallThumbbnail(videoJson.getString("smallthumbnail"));
				channels.getChannelList().add(video);
			}
		} catch (JSONException e) {
			Log.e("ChannelParser", "json数据解析失败");
		}
		return channels;
	}

}
