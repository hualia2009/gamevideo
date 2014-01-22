package com.leslie.gamevideo.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.leslie.gamevideo.R;
import com.leslie.gamevideo.db.MainDbHelper;
import com.leslie.gamevideo.entity.Video;
import com.leslie.gamevideo.utils.AsyncImageLoader;
import com.leslie.gamevideo.utils.AsyncImageLoader.ImageCallback;

public class HistoryListAdatpter extends BaseAdapter
{
	private List<Video> videos;
	private Context context;
	private boolean isDelete;
	private MainDbHelper dbHelper;
	private ListView listView;
	private Handler handler;
	private static final int EDIT_DISABLE = 0;
	public HistoryListAdatpter(List<Video> videos,Context context, ListView listView,Handler handler){
		this.videos = videos;
		this.context = context;
		this.listView = listView;
		this.handler = handler;
	}
	
	public int getCount()
	{
		return videos.size();
	}

	public Video getItem(int position) {
		return videos.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	
	public void setIsDelete(boolean flag)
	{
		isDelete = flag;
	}

	public View getView(final int p, View convertView, ViewGroup viewgroup)
	{
		HistoryViewHolder holder = null;
		if (convertView == null) {
			holder = new HistoryViewHolder();
			LayoutInflater layoutinflater = (LayoutInflater)context.getSystemService("layout_inflater");
			convertView = layoutinflater.inflate(R.layout.playhistory_list_item, null);
			
			holder.mThumb=(ImageView) convertView.findViewById(R.id.history_Thumb);
			holder.lblTitle = (TextView) convertView.findViewById(R.id.lblVideoName);
			holder.lblCollectDate = (TextView) convertView.findViewById(R.id.lblHistoryDate);
			holder.playTimes=(TextView) convertView.findViewById(R.id.history_playtimes);
			holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.mCheckBox);
			convertView.setTag(holder);
		} else {
			holder = (HistoryViewHolder) convertView.getTag();
		}
		final Video video = videos.get(p);
		String imageUrl = video.getThumbnail();
		holder.mThumb.setTag(imageUrl);
		Bitmap cachedImage = AsyncImageLoader.loadDrawable(imageUrl,
				new ImageCallback() {
					public void imageLoaded(Bitmap imageDrawable,
							String imageUrl) {
						 ImageView imageViewByTag = (ImageView) listView.findViewWithTag(imageUrl);  
						 if (imageViewByTag != null) {
					        	if(imageDrawable != null){
					        		imageViewByTag.setImageBitmap(imageDrawable);  
					        	}
					        }  
					}
				});
		if (cachedImage == null) {
			holder.mThumb.setImageResource(R.drawable.default_thumbnail);
		} else {
			holder.mThumb.setImageBitmap(cachedImage);
		}
		holder.lblTitle.setText(video.getTitle());
		holder.lblCollectDate.setText("时长："+video.getDuration());
		if(isDelete){
			holder.mCheckBox.setVisibility(View.VISIBLE);
		}else{
			holder.mCheckBox.setVisibility(View.GONE);
		}
		holder.playTimes.setText("播放："+video.getPlaytimes());
		holder.mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
					dbHelper = MainDbHelper.getInstance(context);
					dbHelper.deleteHistoryById(video.getId());
					videos.remove(p);
					if(videos.isEmpty()){
						//发送消息给ui线程，编辑不可用
						handler.sendEmptyMessage(EDIT_DISABLE);
					}
					HistoryListAdatpter adapter = (HistoryListAdatpter) listView.getAdapter();
					adapter.notifyDataSetChanged();
				
			}
		});
		return convertView;
		
	}
}
