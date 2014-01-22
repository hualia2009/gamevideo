package com.leslie.gamevideo.adapter;

import java.util.List;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.leslie.gamevideo.R;
import com.leslie.gamevideo.activities.Detail;
import com.leslie.gamevideo.entity.Video;
import com.leslie.gamevideo.utils.AsyncImageLoader;
import com.leslie.gamevideo.utils.AsyncImageLoader.ImageCallback;
import com.leslie.gamevideo.utils.Config;

public class ChannelVideoAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<Video> videos;
	private OnLayoutCompleteListener mLayoutCompleteListener = null;
	private static String TAG = "VideoAdapter";
	private ListView listView;
	
	public ChannelVideoAdapter(List<Video> videos,ListView listView) {
		mInflater = LayoutInflater.from(Config.main);
		this.videos = videos;
		this.listView = listView;
		this.listView.setOnItemClickListener(new VideoItemClickListener());
	}

	public List<Video> getVideos() {
		return videos;
	}

	public void setVideos(List<Video> videos) {
		this.videos = videos;
	}

	public int getCount() {
		return videos.size();
	}

	public Video getItem(int position) {
		return videos.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, final ViewGroup parent) {
		VideoViewHolder holder = null;
		boolean isComplete = false;
		final View view = convertView;
		if (convertView == null) {
			holder = new VideoViewHolder();
			convertView = mInflater.inflate(R.layout.video_list_item, null);
			
			holder.imageViewThumb = (ImageView) convertView.findViewById(R.id.ImageViewThumb);
			holder.TextData = (TextView) convertView.findViewById(R.id.TextData);
			holder.TextDuration = (TextView) convertView.findViewById(R.id.TextDuration);
			holder.TextTitle = (TextView) convertView.findViewById(R.id.TextTitle);
			
			if (position == getCount() - 1) {
				isComplete = true;
			}
			convertView.setTag(holder);
			
		} else {
			holder = (VideoViewHolder) convertView.getTag();
		}
		convertView.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				((ListView)parent).performItemClick(view, position, 0);
			}});

		holder.TextTitle.setText((String)videos.get(position).getTitle());
		holder.TextData.setText("播放:"+videos.get(position).getPlaytimes());
		holder.TextDuration.setText("时长:"+videos.get(position).getDuration());
		//异步加载图片
		String imageUrl = videos.get(position).getThumbnail();
		holder.imageViewThumb.setTag(imageUrl);
		Bitmap cachedImage = AsyncImageLoader.loadDrawable(imageUrl, new ImageCallback() {  
		    public void imageLoaded(Bitmap imageBitmap, String imageUrl) {  
		        ImageView imageViewByTag = (ImageView) listView.findViewWithTag(imageUrl);  
		        if (imageViewByTag != null) {
		        	if(imageBitmap != null){
		        		imageViewByTag.setImageBitmap(imageBitmap);  
		        	}
		        }  
		    }  
		});  
		if (cachedImage == null) {  
			holder.imageViewThumb.setImageResource(R.drawable.default_thumbnail); 
		}else{  
			holder.imageViewThumb.setImageBitmap(cachedImage);  
		}
		
		if (isComplete) {
			performOnLayoutComplete(getCount());
		}
		
		return convertView;
	}

	private class VideoItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View arg1, int position,
				long childId) {
			if(position >= getCount()){
				return;
			}
			Intent intent = new Intent(parent.getContext(), Detail.class);
			Bundle extras = new Bundle();
			extras.putSerializable("info", getItem(position));
			intent.putExtras(extras);
			parent.getContext().startActivity(intent);
		}
	}
	
	private void performOnLayoutComplete(int count) {
		if (this.mLayoutCompleteListener != null) {
			this.mLayoutCompleteListener.complete(count);
		} else {
			Log.i(TAG, "Not binding");
		}
	}
	
	/**
	 * 设置事件回调对象
	 * @param l
	 */
	public void setOnLayoutCompleteListener(OnLayoutCompleteListener l) {
		this.mLayoutCompleteListener  = l;
	}
	
	/**
	 * 布局结束事件
	 * 当adapter将数据完全填充到listview中时触发
	 * @author Administrator
	 *
	 */
	public interface OnLayoutCompleteListener {
		public void complete(int count);
	}
	
	
	public void loadImage(){
		int start = listView.getFirstVisiblePosition();
		int end =listView.getLastVisiblePosition();
		if(end >= getCount()){
			end = getCount() -1;
		}
		for(int pos=start;pos<=end;pos++){
			String imageUrl = videos.get(pos).getThumbnail();
			ImageView imageViewByTag = (ImageView) listView.findViewWithTag(imageUrl);
			if(imageViewByTag == null || imageViewByTag.getDrawable() == null){
				continue;
			}
			Resources res=listView.getContext().getResources();
			Drawable drawable = res.getDrawable(R.drawable.default_thumbnail);
			if(!drawable.getConstantState().equals(imageViewByTag.getDrawable().getConstantState())){
				continue;
			}
			ImageView imageView = (ImageView) listView.findViewWithTag(imageUrl);
			if(imageView == null){
				continue;
			}
			imageView.setImageBitmap(AsyncImageLoader.loadBitmapFromUrl(imageUrl));
		}
	}
	
}
