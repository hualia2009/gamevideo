package com.leslie.gamevideo.jsonparser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

import com.leslie.gamevideo.entity.Video;
import com.leslie.gamevideo.entity.VideoList;
import com.leslie.gamevideo.utils.JsonParseUtil;
import com.leslie.gamevideo.utils.Utils;

/**
 * 首页视频资源解析类
 * @author liz
 *
 */
public class VideoParser{
	public static VideoList parse(String returnData){
		VideoList lists=new VideoList();
		if(JsonParseUtil.isEmptyOrNull(returnData)){
			return null;
		}
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(returnData);
			if(jsonObject.has("next")){
				int next=jsonObject.getInt("next");
				lists.setNext(next);
			}
			String videoList=jsonObject.getString("videos");
			if(JsonParseUtil.isEmptyOrNull(videoList)){
				return null;
			}
			JSONArray jsonArray = new JSONArray(videoList);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject videoJson = (JSONObject) jsonArray.opt(i);
				Video video=new Video();
				video.setId(videoJson.getString("id"));
				video.setTitle(videoJson.getString("title"));
				video.setPlaytimes( videoJson.getString("playtimes"));
				video.setVideoflag(videoJson.getString("videoflag"));
				int duration=Integer.parseInt(videoJson.getString("duration"));
				video.setDuration(Utils.formatDuration(duration));
				video.setDescription(videoJson.getString("description"));
				video.setChannel(videoJson.getString("channel"));
				video.setThumbnail( videoJson.getString("thumbnail"));
				lists.getVideoList().add(video);
			}
			
		} catch (JSONException e) {
			Log.e("VideoParser", "json数据解析失败");
		}
		return lists;
	}
}
