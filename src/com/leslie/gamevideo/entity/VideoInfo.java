package com.leslie.gamevideo.entity;

import java.io.Serializable;
import java.util.HashMap;

@SuppressWarnings("serial")
public class VideoInfo implements Serializable {
	private HashMap<String, String> info;
	private String id;
	private String title;
	private String videoflag;
	private String playtimes;
	private String duration;
	private String description;
	private String channel;
	private String thumbnail;
	private String bannerandroid;
	private String smallThumbbnail;
	
	
	public String getSmallThumbbnail() {
		return smallThumbbnail;
	}

	public void setSmallThumbbnail(String smallThumbbnail) {
		this.smallThumbbnail = smallThumbbnail;
	}

	public HashMap<String, String> getInfo() {
		return info;
	}

	public void setInfo(HashMap<String, String> info) {
		this.info = info;
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

	public String getBannerandroid() {
		return bannerandroid;
	}

	public void setBannerandroid(String bannerandroid) {
		this.bannerandroid = bannerandroid;
	}


}
