package com.ayc.framework.datasource.impl;


import com.ayc.framework.datasource.DataSourceKeyDeterminer;
import com.ayc.framework.datasource.DataSourceKeyRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认路由处理逻辑<br>
 * 
 * @author ysj
 *
 */
public class DefaultDataSourceKeyDeterminer implements DataSourceKeyDeterminer {
	private Logger log = LoggerFactory
			.getLogger(DefaultDataSourceKeyDeterminer.class);
	private String defaultDataSourceKey;
	private List<DataSourceKeyRule> rules = new ArrayList<DataSourceKeyRule>();

	public String determine(String field, Long value, boolean readonly) {
		for (DataSourceKeyRule dataSourceKeyRule : this.rules) {
			if (dataSourceKeyRule.applyRule(field, value, readonly)) {
				this.log.debug("============determine to use datasource: field-"
						+ field + "/value-" + value + "/datasource-"
						+ dataSourceKeyRule.getDataSourceKey());
				return dataSourceKeyRule.getDataSourceKey();
			}
		}
		return getDefaultDataSourceKey();
	}

	public String determine(String field, Integer value, boolean readonly) {
		return determine(field, new Long(value.intValue()), readonly);
	}

	public String getDefaultDataSourceKey() {
		return this.defaultDataSourceKey;
	}

	public void setDefaultDataSourceKey(String defaultDataSourceKey) {
		this.defaultDataSourceKey = defaultDataSourceKey;
	}

	public List<DataSourceKeyRule> getRules() {
		return this.rules;
	}

	public void setRules(List<DataSourceKeyRule> rules) {
		this.rules = rules;
	}
}
