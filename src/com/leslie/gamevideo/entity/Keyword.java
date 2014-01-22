package com.leslie.gamevideo.entity;

import java.io.Serializable;

/**
 * 搜索关键字实体
 */
public class Keyword implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int mkid;
	private String keyword;
	private int sort;
	
	public int getMkid() {
		return mkid;
	}
	public void setMkid(int mkid) {
		this.mkid = mkid;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}

}
