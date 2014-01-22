package com.leslie.gamevideo.entity;

import java.io.Serializable;


public class Banner implements Serializable {
	/**
	 * 首页gallery资源实体类
	 */
	private static final long serialVersionUID = 6750475092626463217L;
	private String mid;
	private String title;
	private String videoflag;
	private String viewcount;
	private String duration;
	private String description;
	private String channel;
	private String bigthumbnail;
	private String smallthumbnail;
	private String normalthumbnail;
    
	public VideoInfo getVideoInfo(){
		VideoInfo videoInfo = new VideoInfo();
		videoInfo.setId(this.mid);
		videoInfo.setTitle(this.title);
		videoInfo.setVideoflag(this.videoflag);
		videoInfo.setPlaytimes(this.viewcount);
		videoInfo.setDuration(this.duration);
		videoInfo.setDescription(this.description);
		videoInfo.setChannel(this.channel);
		videoInfo.setThumbnail(this.normalthumbnail);
		videoInfo.setBannerandroid(this.bigthumbnail);
		return videoInfo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getVideoflag() {
		return videoflag;
	}

	public void setVideoflag(String videoflag) {
		this.videoflag = videoflag;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}


	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getViewcount() {
		return viewcount;
	}

	public void setViewcount(String viewcount) {
		this.viewcount = viewcount;
	}

	public String getBigthumbnail() {
		return bigthumbnail;
	}

	public void setBigthumbnail(String bigthumbnail) {
		this.bigthumbnail = bigthumbnail;
	}

	public String getSmallthumbnail() {
		return smallthumbnail;
	}

	public void setSmallthumbnail(String smallthumbnail) {
		this.smallthumbnail = smallthumbnail;
	}

	public String getNormalthumbnail() {
		return normalthumbnail;
	}

	public void setNormalthumbnail(String normalthumbnail) {
		this.normalthumbnail = normalthumbnail;
	}


}
