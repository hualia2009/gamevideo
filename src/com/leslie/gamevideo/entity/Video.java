package com.leslie.gamevideo.entity;

import java.io.Serializable;
/**
 * 首页视频实体
 */
public class Video implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String id;
	private String title;
	private String videoflag;
	private String playtimes;
	private String duration;
	private String description;
	private String channel;
	private String thumbnail;
	private String imgHd;

	public String getImgHd() {
		return imgHd;
	}

	public void setImgHd(String imgHd) {
		this.imgHd = imgHd;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getPlaytimes() {
		return playtimes;
	}

	public void setPlaytimes(String playtimes) {
		this.playtimes = playtimes;
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

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	
	
}
