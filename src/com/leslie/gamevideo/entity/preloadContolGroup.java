package com.leslie.gamevideo.entity;

import android.widget.*;
import java.io.Serializable;

public class preloadContolGroup implements Serializable
{
	private static final long serialVersionUID = 7033399190127042388L;
	private ImageView preloadImg;
	//显示下载的进度条
	private ProgressBar ProgressBarDown;
	//控制下载|暂停的按钮
	private Button btnControl;
	//删除按钮
	private Button btnDelete;
	//播放按钮
	private Button btnPlay;
	//显示已完成的百分比
	private TextView lblPercent;
	//显示文件存放的位置
	private TextView lblWeizhi;
	//进度layout
	private LinearLayout linearLayProcess;
	//状态layout
	private LinearLayout linearLayProcessStatus;
	//视频id
	private Long playId;

	public preloadContolGroup()
	{
	}


	public ImageView getPreloadImg() {
		return preloadImg;
	}

	public void setPreloadImg(ImageView preloadImg) {
		this.preloadImg = preloadImg;
	}

	public Button getBtnControl()
	{
		return btnControl;
	}

	public Button getBtnDelete()
	{
		return btnDelete;
	}

	public Button getBtnPlay()
	{
		return btnPlay;
	}

	public TextView getLblPercent()
	{
		return lblPercent;
	}

	public TextView getLblWeizhi()
	{
		return lblWeizhi;
	}

	public LinearLayout getLinearLayProcess()
	{
		return linearLayProcess;
	}

	public LinearLayout getLinearLayProcessStatus()
	{
		return linearLayProcessStatus;
	}

	public Long getPlayId()
	{
		return playId;
	}

	public ProgressBar getProgressBarDown()
	{
		return ProgressBarDown;
	}

	public void setBtnControl(Button button)
	{
		btnControl = button;
	}

	public void setBtnDelete(Button button)
	{
		btnDelete = button;
	}

	public void setBtnPlay(Button button)
	{
		btnPlay = button;
	}

	public void setLblPercent(TextView textview)
	{
		lblPercent = textview;
	}

	public void setLblWeizhi(TextView textview)
	{
		lblWeizhi = textview;
	}

	public void setLinearLayProcess(LinearLayout linearlayout)
	{
		linearLayProcess = linearlayout;
	}

	public void setLinearLayProcessStatus(LinearLayout linearlayout)
	{
		linearLayProcessStatus = linearlayout;
	}

	public void setPlayId(Long long1)
	{
		playId = long1;
	}

	public void setProgressBarDown(ProgressBar progressbar)
	{
		ProgressBarDown = progressbar;
	}
}
