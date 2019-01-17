package com.ayc.framework.datasource;

/**
 * 数据源节点
 * @author ysj
 *
 */
public class DataSourceNode {
	private String name;
	private DataSourceNode parent;

	public DataSourceNode() {
	}

	public DataSourceNode(String name) {
		this.name = name;
	}

	public DataSourceNode(String name, DataSourceNode parent) {
		 this.name = name;
		 this.parent = parent;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DataSourceNode getParent() {
		return this.parent;
	}

	public void setParent(DataSourceNode parent) {
		this.parent = parent;
	}
}