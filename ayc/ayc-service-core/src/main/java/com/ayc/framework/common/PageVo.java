package com.ayc.framework.common;

import java.io.Serializable;
import java.util.List;

public class PageVo<T> implements Serializable {


	private static final long serialVersionUID = 9118615438888496115L;
	private List<T> list;
	
	private int pages;
	
	private long total;
	
	private int pageNum;
	
	private int pageSize;

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
