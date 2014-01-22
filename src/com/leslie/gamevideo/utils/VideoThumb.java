package com.leslie.gamevideo.utils;

import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

public class VideoThumb {

	public static void getVideoList(Context mContext, List<String> names,
			List<String> paths) {
		ContentResolver contentResolver = mContext.getContentResolver();
		String[] projection = new String[] { MediaStore.Video.Media.TITLE,
				MediaStore.Video.Media.DATA };
		Cursor cursor = contentResolver.query(
				MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null,
				null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
		cursor.moveToFirst();
		int fileNum = cursor.getCount();

		for (int counter = 0; counter < fileNum; counter++) {
			names.add(cursor.getString(cursor
							.getColumnIndex(MediaStore.Video.Media.TITLE)));
			paths.add(cursor.getString(cursor
					.getColumnIndex(MediaStore.Video.Media.DATA)));
			cursor.moveToNext();
		}
		cursor.close();
	}
}
