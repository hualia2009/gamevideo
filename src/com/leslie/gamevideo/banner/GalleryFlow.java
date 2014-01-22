package com.leslie.gamevideo.banner;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Gallery;
import android.widget.SpinnerAdapter;
  
public class GalleryFlow extends Gallery   
{  
	private Handler mHandler;
	private Runnable rGallery = new Runnable() {
		public void run()
		{
			int i = getSelectedItemPosition();
			if (i >= getCount() - 1) {
				i = 0;
			}else{
				i++;
			}
			setSelection(i);
			mHandler.postDelayed(rGallery, 3000L);
		}
	};
	
    public GalleryFlow(Context context)  
    {  
        super(context);
        mHandler = new Handler();
        setStaticTransformationsEnabled(true);
    }  
  
    public GalleryFlow(Context context, AttributeSet attrs)  
    {  
        super(context, attrs);  
        mHandler = new Handler();
        setStaticTransformationsEnabled(true);
    }  
  
    public GalleryFlow(Context context, AttributeSet attrs, int defStyle)   
    {  
        super(context, attrs, defStyle); 
        mHandler = new Handler();
        setStaticTransformationsEnabled(true);
    }  
    
    @Override
	public boolean onFling(MotionEvent motionevent, MotionEvent motionevent1, float f,
			float f1) {
    	boolean flag;
		if (Math.abs(motionevent.getX() - motionevent1.getX()) >= 160F || (double)Math.abs(f) <= 1.5D * (double)Math.abs(f1) || motionevent.getX() - motionevent1.getX() <= 0F)
		{
			if (Math.abs(motionevent.getX() - motionevent1.getX()) >= 160F || (double)Math.abs(f) <= 1.5D * (double)Math.abs(f1) || motionevent.getX() - motionevent1.getX() >= 0F)
				flag = true;
			else
				flag = super.onScroll(motionevent, motionevent1, -160F, motionevent.getY() - motionevent1.getY());
		} else
		{
			flag = super.onScroll(motionevent, motionevent1, 160F, motionevent.getY() - motionevent1.getY());
		}
		return flag;
	}
    
	@Override
	public boolean onTouchEvent(MotionEvent motionevent) {
		switch (motionevent.getAction())
		{
		case 0: 
			mHandler.removeCallbacks(rGallery);
			break;
		case 1: 
		case 3: 
			mHandler.postDelayed(rGallery, 3000L);
			break;
		}
		return super.onTouchEvent(motionevent);
	}
	
	public void setAdapter(SpinnerAdapter spinneradapter)
	{
		super.setAdapter(spinneradapter);
		mHandler.removeCallbacks(rGallery);
		mHandler.postDelayed(rGallery, 3000L);
	}

	public void startFlow()
	{
		mHandler.removeCallbacks(rGallery);
		mHandler.postDelayed(rGallery, 3000L);
	}

	public void stopFlow()
	{
		mHandler.removeCallbacks(rGallery);
	}
	
	
}  
