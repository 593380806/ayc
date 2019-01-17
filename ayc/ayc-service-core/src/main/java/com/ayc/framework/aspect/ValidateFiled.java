package com.ayc.framework.aspect;



import com.ayc.framework.common.BizCode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字段
 * @author ysj
 * @Data 2019-01-15
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ValidateFiled {
	
	/**
	 * 参数索引位置
	 */
	public int index() default -1 ;
	
	/**
	 * 如果参数是基本数据类型或String ，就不用指定该参数，如果参数是对象，要验证对象里面某个属性，就用该参数指定属性名
	 */
	public String filedName() default "" ;
	
	/**
	 * 正则验证
	 */
	public String regStr() default "";
	
	/**
	 * 是否能为空  ， 为true表示不能为空 ， false表示能够为空
	 */
	public boolean notNull() default false;
	
	/**
	 * 最大长度 ， 用户验证字符串
	 */
	public int maxLen() default -1 ;
	
	/**
	 * 最小长度 ， 用户验证字符串
	 */
	public int minLen() default -1 ;
	
	/**
	 *最大值 ，用于验证数字类型数据
	 */
	public int maxVal() default -1 ;
	
	/**
	 *最小值 ，用于验证数值类型数据
	 */
	public int minVal() default -1 ;
	
	
	
	/**  各自系统定义code,message  **/
	/**
	 * 返回编码
	 * @return
	 */
	public BizCode icode() default BizCode.CLIENT_ERROR;
	
	/**
	 *描述，用于校验不通过时参数描述
	 */
	public String message() default  "" ;
	
	
}
