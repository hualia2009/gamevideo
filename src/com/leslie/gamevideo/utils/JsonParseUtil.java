package com.leslie.gamevideo.utils;

public class JsonParseUtil {

	public static boolean isEmptyOrNull(String returnData) {
		if (returnData == null || "".equals(returnData.trim())
				|| "null".equalsIgnoreCase(returnData)
				|| returnData.length() <= 3) {
			return true;
		}
		return false;
	}
}
