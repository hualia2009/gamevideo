package com.leslie.gamevideo.jsonparser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

import com.leslie.gamevideo.entity.Banner;
import com.leslie.gamevideo.utils.Config;
import com.leslie.gamevideo.utils.JsonParseUtil;
import com.leslie.gamevideo.utils.Utils;

/**
 * 首页gallery资源解析类
 */
public class BannerParser implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public static List<Banner> parse(String returnData){
		List<Banner> banners=new ArrayList<Banner>();
		if(JsonParseUtil.isEmptyOrNull(returnData)){
			return null;
		}
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(returnData);
			
				String sparkId = jsonObject.getString("sparkid");
				if(JsonParseUtil.isEmptyOrNull(sparkId)){
					Log.e("BannerParser", "sparkid is wrong!");
				}else{
					Config.uid = sparkId;
				}
				
			String bannerList=jsonObject.getString("videos");
			if(JsonParseUtil.isEmptyOrNull(bannerList)){
				return null;
			}
			JSONArray jsonArray = new JSONArray(bannerList);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject bannerJson = (JSONObject) jsonArray.opt(i);
				Banner banner=new Banner();
				banner.setMid(bannerJson.getString("mid"));
				banner.setTitle(bannerJson.getString("bannertitle"));
				banner.setViewcount( bannerJson.getString("viewcount"));
				banner.setVideoflag(bannerJson.getString("videoflag"));
				int duration=Integer.parseInt(bannerJson.getString("duration"));
				banner.setDuration(Utils.formatDuration(duration));
				banner.setDescription(bannerJson.getString("description"));
				banner.setChannel(bannerJson.getString("channel"));
				banner.setBigthumbnail( bannerJson.getString("bigthumbnail"));
				banner.setSmallthumbnail (bannerJson.getString("smallthumbnail"));
				banner.setNormalthumbnail (bannerJson.getString("normalthumbnail"));
				banners.add(banner);
			}
			
			
		} catch (JSONException e) {
		Log.e("BannerParser", "json数据解析失败");
		}
		return banners;
	}
	
}
