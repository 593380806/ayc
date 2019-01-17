package com.ayc.framework.util;


import com.ayc.framework.security.SessionUser;

/**
 * web 上下文容器
 * @author ysj
 * @Data 2019-01-15
 */
public class WebContextUtils {

	private static final ThreadLocal<Object> currentSessionUser = new ThreadLocal<>();

	public static <T extends SessionUser> void putObj2Session(T currentUser) {
		currentSessionUser.set(currentUser);
	}

	@SuppressWarnings("unchecked")
	public static <T extends SessionUser> T getCurrentSessionUser() {
		return (T) currentSessionUser.get();
	}

	public static void clearSessionUser() {
		currentSessionUser.remove();
	}

}
