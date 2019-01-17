package com.ayc.framework.common;

import java.io.Serializable;
import java.util.List;

public class PageDto<T> implements Serializable {


	private static final long serialVersionUID = -1922752549548720862L;
	private List<T> records;
	
	private Integer num;

	public List<T> getRecords() {
		return records;
	}

	public void setRecords(List<T> records) {
		this.records = records;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

}
