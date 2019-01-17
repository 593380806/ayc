package com.ayc.framework.datasource;

public interface DataSourceKeyRule
{
  public abstract String getDataSourceKey();

  public abstract boolean applyRule(String paramString, Long paramLong);

  public abstract boolean applyRule(String paramString, Long paramLong, boolean paramBoolean);

  public abstract boolean isReadonly();
}
