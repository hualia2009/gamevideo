package com.leslie.gamevideo.jsonparser;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.util.Log;

import com.leslie.gamevideo.entity.Keyword;
import com.leslie.gamevideo.utils.JsonParseUtil;

@SuppressLint({ "UseValueOf", "UseValueOf" })
public class KeywordsParser{
	public static List<Keyword> parse(String returnData){
		List<Keyword> keywords=new ArrayList<Keyword>();
		if(JsonParseUtil.isEmptyOrNull(returnData)){
			return null;
		}
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(returnData);
			
			String mkeywords=jsonObject.getString("mkeywords");
			if(JsonParseUtil.isEmptyOrNull(mkeywords)){
				return null;
			}
			JSONArray jsonArray = new JSONArray(mkeywords);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject keywordJson = (JSONObject) jsonArray.opt(i);
				Keyword keyword=new Keyword();
				keyword.setMkid(new Integer(keywordJson.getString("mkid")));
				keyword.setKeyword(keywordJson.getString("keyword"));
				keyword.setSort(new Integer(keywordJson.getString("sort")));
				keywords.add(keyword);
			}
		} catch (JSONException e) {
			Log.e("KeywordsParser", "json数据解析失败");
		}
		return keywords;
	}
	
}
