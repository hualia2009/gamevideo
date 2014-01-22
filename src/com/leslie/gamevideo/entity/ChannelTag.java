package com.leslie.gamevideo.entity;

import java.io.Serializable;

public class ChannelTag implements Serializable {
	/**
	 * 频道标签实体类
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String description;
	private String vieworder;
	private String ishidden;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVieworder() {
		return vieworder;
	}

	public void setVieworder(String vieworder) {
		this.vieworder = vieworder;
	}

	public String getIshidden() {
		return ishidden;
	}

	public void setIshidden(String ishidden) {
		this.ishidden = ishidden;
	}
}
