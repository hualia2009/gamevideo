package com.leslie.gamevideo.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页视频实体
 */
public class VideoList implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int next;
	private List<Video> videoList = new ArrayList<Video>();
	public int getNext() {
		return next;
	}
	public void setNext(int next) {
		this.next = next;
	}
	public List<Video> getVideoList() {
		return videoList;
	}
	public void setVideoList(List<Video> videoList) {
		this.videoList = videoList;
	}
}
