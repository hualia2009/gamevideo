package com.leslie.gamevideo.activities;

import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;

import com.leslie.gamevideo.R;

public abstract class BaseActivity extends Activity{
	/** 当前activity */
	protected Activity mThis;
	/** 标题栏 BACK */
	protected Button back;
	/** 标题栏 TITLE */
	protected TextView title;
	
	/** 设置标题;子类集成后会在 setMenu方法执行后 */
	protected abstract void setTitles();
	
	/** 设置顶部菜单 */
	public void setTopMenu() {
		mThis = this;
		title = (TextView) findViewById(R.id.more_title_tv);
		// 设置标题
		setTitles();
	}
}
