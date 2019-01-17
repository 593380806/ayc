package com.ayc.framework.aspect;



import com.ayc.framework.security.SessionUser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AppAuth {
	/**
	 * 参数索引位置
	 */
	public int index() default -1 ;
	
	/**
	 * 如果参数是基本数据类型或String ，就不用指定该参数，如果参数是对象，要验证对象里面某个属性，就用该参数指定属性名
	 */
	public String fieldName() default "" ;
	
	
	/**
	 * 允许的角色
	 * @return
	 */
	public int roleCode() default 1;
	
	public Class<? extends SessionUser> clazz() default SessionUser.class;
	
}

