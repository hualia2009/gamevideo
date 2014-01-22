
package com.leslie.gamevideo.player;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.AbsSeekBar;
/**
 * 
 * @author Administrator
 * 相关知识：seekbar允许用户改变他的滑块外观，通过如下方法来实现：android:thumb 指定
 * 一个drawable对象，将对象作为自定义滑块 为了让程序能响应拖动条滑块位置的改变，可以绑定
 * OnSeekBarChangeListener来进行相应的监听处理。
 *  
 */
public class VerticalSeekBar extends AbsSeekBar
{
	public static interface OnSeekBarChangeListener
	{
		//当拖动条的滑块位置发生改变时触发该方法（fromUser如果是用户触发的改变则返回True
		//seekBar 当前被修改进度的SeekBar  progress          当前的进度值。此值的取值范围为0到max之间。
		//Max为用户通过setMax(int)设置的值，默认为100。此方法用来通知进度已经被修改）
		public abstract void onProgressChanged(VerticalSeekBar verticalseekbar, int i, boolean flag);
		//通知用户已经开始一个触摸拖动手势。客户端可能需要使用这个来禁用seekbar的滑动功能。（seekBar 触摸的SeekBar）
		public abstract void onStartTrackingTouch(VerticalSeekBar verticalseekbar);
		//通知用户触摸手势已经结束。户端可能需要使用这个来启用seekbar的滑动功能。(seekBar 触摸的SeekBar)
		public abstract void onStopTrackingTouch(VerticalSeekBar verticalseekbar);
	}


	private int height;
	private OnSeekBarChangeListener mOnSeekBarChangeListener;
	private Drawable mThumb;
	private int width;

	public VerticalSeekBar(Context context)
	{
		this(context, null);
	}

	public VerticalSeekBar(Context context, AttributeSet attributeset)
	{
		this(context, attributeset, 0x101007b);
	}

	public VerticalSeekBar(Context context, AttributeSet attributeset, int i)
	{
		super(context, attributeset, i);
	}
	 //  让父类不用onInterceptTouchEvent(MotionEvent)来拦截触屏事件。true表示child不让父类拦截触屏事件
	private void attemptClaimDrag()
	{
		if (getParent() != null)
			getParent().requestDisallowInterceptTouchEvent(true);
	}
	/**
	 * 设置滑块的进度值
	 * @param i
	 * @param drawable
	 * @param f
	 * @param j
	 */
	private void setThumbPos(int i, Drawable drawable, float f, int j)
	{
		int l = (i + getPaddingLeft()) - getPaddingRight();
		int k = drawable.getIntrinsicWidth();
		int j1 = drawable.getIntrinsicHeight();
		int i1 = (int)(f * (float)((l - k) + 2 * getThumbOffset()));
		if (j != 0x80000000)
		{
			l = j;
			j1 = j + j1;
		} else
		{
			Rect rect = drawable.getBounds();
			l = rect.top;
			j1 = rect.bottom;
		}
		drawable.setBounds(i1, l, i1 + k, j1);
	}
	/**
	 * 监听触摸拖动事件
	 * @param motionevent
	 */
	private void trackTouchEvent(MotionEvent motionevent)
	{
		int i = getHeight();
		int j = i - getPaddingBottom() - getPaddingTop();
		float f = (int)motionevent.getY();
		if (f <= i - getPaddingBottom())
		{
			if (f >= getPaddingTop())
				f = (float)(i - getPaddingBottom() - f) / (float)j;
			else
				f = 1F;
		} else
		{
			f = 0F;
		}
		setProgress((int)(f * (float)getMax()));//设置进度条
	}

	public boolean dispatchKeyEvent(KeyEvent keyevent)
	{
		boolean flag;
		KeyEvent keyEvent;
		if (keyevent.getAction() != 0)
		{
			flag = false;
		} else
		{
			switch (keyevent.getKeyCode())
			{
			default:
				keyEvent = new KeyEvent(0, keyevent.getKeyCode());
				break;
			case 19: 
				keyEvent = new KeyEvent(0, 22);
				break;
			case 20:
				keyEvent = new KeyEvent(0, 21);
				break;
			case 21: 
				keyEvent = new KeyEvent(0, 20);
				break;
			case 22: 
				keyEvent = new KeyEvent(0, 19);
				break;
			}
			flag = keyEvent.dispatch(this);
		}
		return flag;
	}

	protected void onDraw(Canvas canvas)
	{
		canvas.rotate(-90F);
		canvas.translate(-height, 0F);
		super.onDraw(canvas);
	}

	protected void onMeasure(int i, int j)
	{
		height = android.view.View.MeasureSpec.getSize(j);
		width = android.view.View.MeasureSpec.getSize(i);
		setMeasuredDimension(width, height);
	}
	void onProgressRefresh(float f, boolean flag)
	{
		Drawable drawable = mThumb;
		if (drawable != null)
		{
			setThumbPos(getHeight(), drawable, f, 0x80000000);
			invalidate();
		}
		if (mOnSeekBarChangeListener != null)
			mOnSeekBarChangeListener.onProgressChanged(this, getProgress(), flag);
	}

	protected void onSizeChanged(int i, int j, int k, int l)
	{
		super.onSizeChanged(j, i, k, l);
	}

	void onStartTrackingTouch()
	{
		if (mOnSeekBarChangeListener != null)
			mOnSeekBarChangeListener.onStartTrackingTouch(this);
	}

	void onStopTrackingTouch()
	{
		if (mOnSeekBarChangeListener != null)
			mOnSeekBarChangeListener.onStopTrackingTouch(this);
	}

	public boolean onTouchEvent(MotionEvent motionevent)
	{
		boolean flag;
		if (isEnabled())
		{
			switch (motionevent.getAction())
			{
			case 0:
				setPressed(true);
				onStartTrackingTouch();
				trackTouchEvent(motionevent);
				break;
			case 1: 
				trackTouchEvent(motionevent);
				onStopTrackingTouch();
				setPressed(false);
				break;
			case 2: 
				trackTouchEvent(motionevent);
				attemptClaimDrag();
				break;
			case 3: 
				onStopTrackingTouch();
				setPressed(false);
				break;
			}
			flag = true;
		} else
		{
			flag = false;
		}
		return flag;
	}

	public void setOnSeekBarChangeListener(OnSeekBarChangeListener onseekbarchangelistener)
	{
		mOnSeekBarChangeListener = onseekbarchangelistener;
	}

	public void setThumb(Drawable drawable)
	{
		mThumb = drawable;
		super.setThumb(drawable);
	}
}
