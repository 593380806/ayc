package com.ayc.framework.datasource;

/**
 * 
 * @author ysj
 *
 */
public class DynamicDataSourceHolder {
	 private static final ThreadLocal<DataSourceNode> originalHolder = new ThreadLocal<DataSourceNode>();

	 private static final ThreadLocal<String> currentHolder = new ThreadLocal<String>();

	 private static final ThreadLocal<Boolean> manualSwitchHolder = new ThreadLocal<Boolean>();

	public static void putDataSource(String name) {
		 currentHolder.set(name);
	}

	public static void putOriginalDataSource(String name) {
		 DataSourceNode out = (DataSourceNode) originalHolder.get();
		 if (out == null) {
			 originalHolder.set(new DataSourceNode(name));
		} else {
			 DataSourceNode child = new DataSourceNode(name, out);
			 originalHolder.set(child);
		}
	}

	public static void setOriginalDataSource(DataSourceNode dataSourceNode) {
		 originalHolder.set(dataSourceNode);
	}

	public static String getDataSouce() {
		 return (String) currentHolder.get();
	}

	public static DataSourceNode getOriginalDataSource() {
		return (DataSourceNode) originalHolder.get();
	}

	public static boolean getManualSwitch() {
		if (manualSwitchHolder.get() == null) {
			return false;
		}
		return ((Boolean) manualSwitchHolder.get()).booleanValue();
	}

	public static void setManualSwitch(Boolean manualSwitchValue) {
		manualSwitchHolder.set(manualSwitchValue);
	}
}
