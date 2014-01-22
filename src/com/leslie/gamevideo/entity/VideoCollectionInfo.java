package com.leslie.gamevideo.entity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;


public class VideoCollectionInfo extends ArrayList<VideoInfo>{

	private static final long serialVersionUID = 1L;
	private VideoInfo video = null;
	
	@SuppressWarnings("unused")
	private static final String TAG = "VideoCollectionInfo";

	public void setData(JSONObject s) {
		video = new VideoInfo();
		try {
			video.setId(s.getString("mid"));
			video.setTitle(s.getString("title"));
			video.setVideoflag(s.getString("videoflag"));
			video.setPlaytimes(s.getString("viewcount"));
			int duration = s.getInt("duration");
			video.setDuration(formatDuration(duration));
			video.setDescription(s.getString("description"));
			video.setChannel(s.getString("channel"));
			video.setThumbnail(s.getString("normalthumbnail"));
			if(s.has("bannerandroid")){
				video.setBannerandroid(s.getString("bannerandroid"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		this.add(video);
	}
	
	/**
	 * 时间处理
	 * @param duration
	 * @return
	 */
	private String formatDuration(int duration){
		int min = duration / 60;
		int sec = duration % 60;
		if(sec < 10){
			return min + ":0" + sec;
		}
		return min + ":" + sec;
	}

}
