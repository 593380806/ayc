package com.ayc.framework.aspect;


import com.alibaba.fastjson.JSON;
import com.ayc.framework.common.AjaxResult;
import com.ayc.framework.common.BizCode;
import com.ayc.framework.common.BizException;
import com.ayc.framework.redis.RedisUtil;
import com.ayc.framework.security.SessionUser;
import com.ayc.framework.util.WebContextUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户登录 权限验证
 * ysj
 * 2019-01-15
 */
@Component
@Aspect
public class AppAuthAspectHandel implements Ordered {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	RedisUtil redisUtil;
	/**
	 * 使用AOP对使用了AppAuth的方法进行代理校验
	 * @throws Throwable 
	 */
	@SuppressWarnings({ "finally", "rawtypes" })
	@Around("@annotation(com.ayc.framework.aspect.AppAuth)")
	public Object validateBefore(ProceedingJoinPoint joinPoint) throws Throwable  {
		Object[] args = joinPoint.getArgs();		//方法的参数	
		Object target = joinPoint.getTarget();
		String methodName = joinPoint.getSignature().getName();
		Method method = getMethodByClassAndName(target.getClass(), methodName);	//得到拦截的方法
		AppAuth an = (AppAuth)getAnnotationByMethod(method ,AppAuth.class );
		
		Map<String,String> resultMap = new HashMap<String,String>();
		try{
			validate(an , args , resultMap);
		}catch(BizException e){
			resultMap.put("respCode", e.getCode().getCode());
			resultMap.put("respMsg",  e.getCode().getMessage());
		} catch(Exception e){
			logger.error("AppAuth处理异常",e);
			resultMap.put("respCode", BizCode.SERVER_ERROR.getCode());
			resultMap.put("respMsg", BizCode.SERVER_ERROR.getMessage() + "原因1：" + e.getMessage());
		}finally{
			try{
				if(BizCode.SUCCESS.getCode().equals(resultMap.get("respCode"))){
					Object obj =  joinPoint.proceed();
					return obj;
				
			}else{
				Class<?> returnType = method.getReturnType();	//得到方法返回值类型
				if(returnType == AjaxResult.class){	//如果返回值为AjaxResult
					AjaxResult result = AjaxResult.failed();
					result.setCode(resultMap.get("respCode"));
					result.setMessage(resultMap.get("respMsg"));
					return result;
				}else{	
					Constructor con = returnType.getConstructor(String.class, String.class);    
					Object obj = con.newInstance(resultMap.get("respCode"),resultMap.get("respMsg"));
					return obj;
				}
			}
			}catch(Throwable t){
				logger.error("处理校验返回对象时候出现异常",t);
				throw t;
			}finally{
				WebContextUtils.clearSessionUser();
			}
		}
	}

	/**
	 * 验证参数是否合法
	 */
	private void validate( AppAuth auth , Object[] args,Map<String,String> resultMap)throws Exception{

		Object arg = null;
		if("".equals(auth.fieldName()) ){
			arg = args[auth.index()];
		}else{
			arg = getFieldByObjectAndFileName(args[auth.index()] ,
					auth.fieldName() );
		}
		
		//从redis中获取 对象
		String result  = redisUtil.get(String.valueOf(arg));
		if(null == result){
			logger.warn("从reids获取对象结果为空，参数:{}" , arg);
			throw new BizException(BizCode.TOKEN_ERROR);
		}
		
		//权限校验
		int roleCode = auth.roleCode();
		SessionUser currentUser= JSON.parseObject(result, auth.clazz());
		if((currentUser.getRoleCode() & roleCode) > 0 ){
			WebContextUtils.putObj2Session(currentUser);
		}else{
			logger.warn("当前用户访问权限不足,当前roleCode:{},访问roleCode:{}",currentUser.getRoleCode(), roleCode);
			throw new BizException(BizCode.SECURITY_ERROR);
		}
		
		resultMap.put("respCode",BizCode.SUCCESS.getCode());
		resultMap.put("respMsg",BizCode.SUCCESS.getMessage());
	}

	/**
	 * 根据对象和属性名得到 属性
	 */
	public Object getFieldByObjectAndFileName(Object targetObj , String fileName) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		String tmp[] = fileName.split("\\.");
		Object arg = targetObj ;
		for (int i = 0; i < tmp.length; i++) {
			Method methdo = arg.getClass().
					getMethod(getGetterNameByFiledName(tmp[i]));
			arg = methdo.invoke(arg);
		}
		return arg ;
	}

	/**
	 * 根据属性名 得到该属性的getter方法名
	 */
	public String getGetterNameByFiledName(String fieldName){
		return "get" + fieldName.substring(0 ,1).toUpperCase() + fieldName.substring(1) ;
	}

	/**
	 * 根据目标方法和注解类型  得到该目标方法的指定注解
	 */
	public Annotation getAnnotationByMethod(Method method , Class<?> annoClass){
		Annotation all[] = method.getAnnotations();
		for (Annotation annotation : all) {
			if (annotation.annotationType() == annoClass) {
				return annotation;
			}
		}
		return null;
	}

	/**
	 * 根据类和方法名得到方法
	 */
	public Method getMethodByClassAndName(Class<?> c , String methodName){
		Method[] methods = c.getDeclaredMethods();
		for (Method method : methods) {
			if(method.getName().equals(methodName)){
				return method ;
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println((9 & 3) > 0);
	}

	@Override
	public int getOrder() {
		return 2;
	}

}
