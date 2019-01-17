package com.ayc.framework.aspect;


import com.ayc.framework.common.AjaxResult;
import com.ayc.framework.common.BizCode;
import com.ayc.framework.common.BizException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证注解处理类
 * @author ysj
 * @Data 2019-01-15
 */
@Component
@Aspect
public class ValidateAspectHandel implements Ordered {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 使用AOP对使用了ValidateGroup的方法进行代理校验
	 * @throws Throwable 
	 */
	@SuppressWarnings({ "finally", "rawtypes" })
	@Around("@annotation(com.ayc.framework.aspect.ValidateGroup)")
	public Object validateAround(ProceedingJoinPoint joinPoint) throws Throwable  {
		Object[] args = joinPoint.getArgs();		//方法的参数	
		Object target = joinPoint.getTarget();
		String methodName = joinPoint.getSignature().getName();
		Method method = getMethodByClassAndName(target.getClass(), methodName);	//得到拦截的方法
		ValidateGroup an = (ValidateGroup)getAnnotationByMethod(method ,ValidateGroup.class );
		
		Map<String,String> resultMap = new HashMap<String,String>();
		try{
			validateFiled(an.fileds() , args , resultMap);
		}catch(Exception e){
			logger.error("系统校验参数异常:",e);
			resultMap.put("respCode", BizCode.SERVER_ERROR.getCode());
			resultMap.put("respMsg", BizCode.SERVER_ERROR.getMessage() + "原因0：" + e.getMessage());
		}finally{
			if(BizCode.SUCCESS.getCode().equals(resultMap.get("respCode"))){
				return joinPoint.proceed();
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
			
		}
	}

	/**
	 * 验证参数是否合法
	 */
	public void validateFiled( ValidateFiled[] valiedatefiles , Object[] args,Map<String,String> resultMap){
		for (ValidateFiled validateFiled : valiedatefiles) {
			try{
				Object arg = null;
				if("".equals(validateFiled.filedName()) ){
					arg = args[validateFiled.index()];
				}else{
					arg = getFieldByObjectAndFileName(args[validateFiled.index()] ,
							validateFiled.filedName() );
				}

				if(validateFiled.notNull()){		//判断参数是否为空
					if(arg == null ){
						resultMap.put("respCode", validateFiled.icode().getCode());
						resultMap.put("respMsg",   validateFiled.message() + "为空");
						return;
					}
				}else{		//如果该参数能够为空，并且当参数为空时，就不用判断后面的了 
					if(arg == null ){
						continue;
					}
				}

				if(validateFiled.maxLen() > 0){		//判断字符串最大长度
					if(((String)arg).length() > validateFiled.maxLen()){
						resultMap.put("respCode", validateFiled.icode().getCode());
						resultMap.put("respMsg",  validateFiled.message() + "长度超过允许最大长度");
						return;
					}
				}

				if(validateFiled.minLen() > 0){		//判断字符串最小长度
					if(((String)arg).length() < validateFiled.minLen()){
						resultMap.put("respCode", validateFiled.icode().getCode());
						resultMap.put("respMsg",   validateFiled.message() + "长度小于最小长度");
						return;
					}
				}

				if(arg != null) {
					if(validateFiled.maxVal() != -1){	//判断数值最大值
						if( (Integer)arg > validateFiled.maxVal()) {
							resultMap.put("respCode", validateFiled.icode().getCode());
							resultMap.put("respMsg",   validateFiled.message() + "大小大于允许最大值");
							return;
						}
					}
					
					if(validateFiled.minVal() != -1){	//判断数值最小值
						if((Integer)arg < validateFiled.minVal()){
							resultMap.put("respCode", validateFiled.icode().getCode());
							resultMap.put("respMsg",  validateFiled.message() + "小于小大于允许最小值");
							return;
						}
					}
				}

				if(!"".equals(validateFiled.regStr())){	//判断正则
					if(arg instanceof String){
						if(!((String)arg).matches(validateFiled.regStr())){
							resultMap.put("respCode", validateFiled.icode().getCode());
							resultMap.put("respMsg",  validateFiled.message() + "不满足验证的表达式");
							return;
						}
					}else{
						resultMap.put("respCode", validateFiled.icode().getCode());
						resultMap.put("respMsg",  validateFiled.message());
						return;
					}
				}
			}catch(Exception e){
				logger.error("校验字段时候出现异常,字段信息:{},{}", validateFiled.index(), validateFiled.filedName(), e);
				throw new BizException(BizCode.SERVER_ERROR);
			}
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

	@Override
	public int getOrder() {
		return 1;
	}

}
