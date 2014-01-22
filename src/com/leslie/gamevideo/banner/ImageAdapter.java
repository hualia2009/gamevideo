package com.leslie.gamevideo.banner;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.leslie.gamevideo.entity.Video;
import com.leslie.gamevideo.entity.VideoInfo;
import com.leslie.gamevideo.utils.AsyncImageLoader;
import com.leslie.gamevideo.utils.AsyncImageLoader.ImageCallback;

public class ImageAdapter extends BaseAdapter {
	int mGalleryItemBackground;
	private Context mContext;
	private List<Video> videos;
	public static int width;
	private GalleryFlow gallery;

	public ImageAdapter(Context context, List<Video> videos,
			TextView textView, GalleryFlow gallery) {
		this.videos = videos;
		mContext = context;
		Activity activity = (Activity)context;
		DisplayMetrics displaymetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		width = displaymetrics.widthPixels;
		this.gallery = gallery;
	}

	public int getCount() {
		return videos.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView i = new ImageView(mContext);
		i.setLayoutParams(new android.widget.Gallery.LayoutParams(width, LayoutParams.FILL_PARENT));
		String imageUrl = videos.get(position % 5).getImgHd();
		i.setTag(imageUrl);
		Bitmap cachedImage = AsyncImageLoader.loadDrawable(imageUrl, new ImageCallback() {  
		    public void imageLoaded(Bitmap imageBitmap, String imageUrl) {  
		        ImageView imageViewByTag = (ImageView) gallery.findViewWithTag(imageUrl);  
		        if (imageViewByTag != null) {  
		            imageViewByTag.setImageBitmap(imageBitmap);  
		        }  
		    }  
		});  
		if (cachedImage == null) {  
			i.setImageDrawable(null); 
		}else{  
		    i.setImageBitmap(cachedImage);  
		}
		i.setBackgroundResource(mGalleryItemBackground);
		i.setScaleType(ImageView.ScaleType.FIT_XY);
		return i;
	}

}
