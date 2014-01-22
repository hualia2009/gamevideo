package com.leslie.gamevideo.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 频道视频实体
 */
public class ChannelVideoList implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int next;
	private List<ChannelVideo> channelList = new ArrayList<ChannelVideo>();
	public int getNext() {
		return next;
	}

	public void setNext(int next) {
		this.next = next;
	}

	public List<ChannelVideo> getChannelList() {
		return channelList;
	}

	public void setChannelList(List<ChannelVideo> channelList) {
		this.channelList = channelList;
	}

}
