package com.ayc.framework.datasource.impl.rules;


import com.ayc.framework.datasource.DataSourceKeyRule;

/**
 * 
 * @author ysj
 *
 */
public abstract class ADataSourceKeyRule implements DataSourceKeyRule {
	private String field;
	 private boolean readonly = false;
	private String dataSourceKey;

	public String getField() {
		 return this.field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public boolean isReadonly() {
		return this.readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public String getDataSourceKey() {
		return this.dataSourceKey;
	}

	public void setDataSourceKey(String dataSourceKey) {
		this.dataSourceKey = dataSourceKey;
	}
}
