package com.ayc.framework.datasource;

public abstract interface DataSourceKeyDeterminer
{
  public abstract String determine(String paramString, Long paramLong, boolean paramBoolean);

  public abstract String determine(String paramString, Integer paramInteger, boolean paramBoolean);

  public abstract String getDefaultDataSourceKey();
}

