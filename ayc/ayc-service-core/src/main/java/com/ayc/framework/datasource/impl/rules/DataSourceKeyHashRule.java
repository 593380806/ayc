package com.ayc.framework.datasource.impl.rules;


import com.ayc.framework.datasource.DataSourceKeyRule;

/**
 * 
 * @author ysj
 *
 */
public class DataSourceKeyHashRule extends ADataSourceKeyRule implements
		DataSourceKeyRule {
	private Long from;
	private Long to;

	public Long getFrom() {
		return this.from;
	}

	public void setFrom(Long from) {
		this.from = from;
	}

	public Long getTo() {
		return this.to;
	}

	public void setTo(Long to) {
		this.to = to;
	}

	public boolean applyRule(String field, Long value) {
		return applyRule(field, value, false);
	}

	public boolean applyRule(String field, Long value, boolean readonly) {
		if ((field == null) || (value == null)) {
			return false;
		}
		if (field.compareTo(getField()) != 0) {
			return false;
		}
		if ((!isReadonly()) && (readonly)) {
			return false;
		}
		if ((isReadonly()) && (!readonly)) {
			return false;
		}
		if ((this.from == null) && (this.to != null)
				&& (value.longValue() < this.to.longValue())) {
			return true;
		}
		if ((this.from != null) && (this.to == null)
				&& (value.longValue() >= this.from.longValue())) {
			return true;
		}
		if ((this.from != null) && (this.to != null)
				&& (value.longValue() >= this.from.longValue())
				&& (value.longValue() < this.to.longValue())) {
			return true;
		}
		return false;
	}
}
