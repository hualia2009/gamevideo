package com.leslie.gamevideo.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.leslie.gamevideo.R;
import com.leslie.gamevideo.utils.AsyncImageLoader;
import com.leslie.gamevideo.utils.AsyncImageLoader.ImageCallback;
import com.leslie.gamevideo.utils.Utils;

public class MyAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<String> items;
	private List<String> paths;
	private ListView listView;

	public MyAdapter(Context context, List<String> it, List<String> pa,
			final ListView listView) {
		mInflater = LayoutInflater.from(context);
		items = it;
		paths = pa;
		this.listView = listView;
	}

	public int getCount() {
		return items.size();
	}

	public Object getItem(int position) {
		return items.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.file_row, null);
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.text);
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		File f = new File(paths.get(position));
		holder.text.setText(f.getName());
		final String filePath = paths.get(position);
		holder.icon.setTag(filePath);

		Bitmap cachedImage = AsyncImageLoader.loadDrawable(filePath,
				new ImageCallback() {
					public void imageLoaded(Bitmap imageBitmap, String imageUrl) {
						ImageView imageViewByTag = (ImageView) listView
								.findViewWithTag(imageUrl);
						if (imageViewByTag != null) {
							if (imageBitmap != null) {
								imageViewByTag.setImageBitmap(imageBitmap);
							}
						}
					}
				});
		if (cachedImage == null) {
			holder.icon.setImageResource(R.drawable.default_thumbnail);
		} else {
			holder.icon.setImageBitmap(cachedImage);
		}
		String timeStr;
		MediaMetadataRetriever mmr = new MediaMetadataRetriever();
		mmr.setDataSource(filePath);
		timeStr = mmr
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

		if (timeStr == null || timeStr.equals("")) {
			holder.time.setText(Utils.stringForTime(0));
		} else {
			holder.time.setText(Utils.stringForTime(Integer.parseInt(timeStr)));
		}
		return convertView;
	}

	private class ViewHolder {
		TextView text;
		ImageView icon;
		TextView time;
	}
}
