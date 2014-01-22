package com.leslie.gamevideo.jsonparser;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

import com.leslie.gamevideo.entity.ChannelTag;
import com.leslie.gamevideo.utils.JsonParseUtil;

/**
 * 频道标签解析类
 * @author liz
 *
 */
public class ChannelTagParser {
	public static List<ChannelTag> parse(String returnData) {
		List<ChannelTag> lists = new ArrayList<ChannelTag>();
		if (JsonParseUtil.isEmptyOrNull(returnData)) {
			return null;
		}
		try {
			JSONObject jsonObject = new JSONObject(returnData);
			String channelTag = jsonObject.getString("mchannls");
			if (JsonParseUtil.isEmptyOrNull(channelTag)) {
				return null;
			}
			JSONArray jsonArray = new JSONArray(channelTag);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject tagJson = (JSONObject) jsonArray.opt(i);
				ChannelTag tag = new ChannelTag();
				tag.setId(tagJson.getString("cid"));
				tag.setName(tagJson.getString("name"));
				tag.setDescription(tagJson.getString("description"));
				tag.setVieworder(tagJson.getString("vieworder"));
				lists.add(tag);
			}
		} catch (JSONException e) {
			Log.e("ChannelTagParser", "json数据解析失败");
		}
		return lists;
	}

}
