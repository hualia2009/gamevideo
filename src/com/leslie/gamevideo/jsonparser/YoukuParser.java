package com.leslie.gamevideo.jsonparser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.leslie.gamevideo.entity.Video;
import com.leslie.gamevideo.utils.JsonParseUtil;

/**
 * 首页视频资源解析类
 * @author liz
 *
 */
public class YoukuParser{
	public static List<Video> parse(String returnData){
		List<Video> lists= new ArrayList<Video>();
		if(JsonParseUtil.isEmptyOrNull(returnData)){
			return null;
		}
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(returnData);
			
			String videoList=jsonObject.getString("results");
			if(JsonParseUtil.isEmptyOrNull(videoList)){
				return null;
			}
			JSONArray jsonArray = new JSONArray(videoList);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject videoJson = (JSONObject) jsonArray.opt(i);
				Video video=new Video();
				video.setId(videoJson.getString("videoid"));
				video.setTitle(videoJson.getString("title"));
				video.setPlaytimes("");
				video.setVideoflag("");
				//int duration=Integer.parseInt(videoJson.getString("duration"));
				video.setDuration(videoJson.getString("duration"));
				video.setDescription(videoJson.getString("desc"));
				video.setChannel(videoJson.getString("cats"));
				video.setThumbnail( videoJson.getString("img"));
				video.setImgHd(videoJson.getString("img_hd"));
				lists.add(video);
			}
			
		} catch (JSONException e) {
			Log.i("VideoParser", "数据解析失败");
		}
		return lists;
	}
}
