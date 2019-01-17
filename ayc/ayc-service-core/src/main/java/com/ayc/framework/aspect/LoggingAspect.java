package com.ayc.framework.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Aspect
//@Component
@Deprecated
public class LoggingAspect {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 统计方法执行耗时Around环绕通知
	 * 
	 * @param joinPoint
	 * @return
	 * @throws Throwable 
	 */
//	@Around("execution (* cn.buguauto.service.*.controller.*.*(..))")
	public Object loggingAround(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();
		// 定义返回对象、得到方法需要的参数
		Object resultData = null;
		String targetName = joinPoint.getTarget().getClass().getSimpleName();
		String methodName = joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();
		try {
			// 调用接口
			logger.info("======>请求{}.{}接口开始,参数:{}", targetName, methodName, args);
			resultData = joinPoint.proceed(args);
			long endTime = System.currentTimeMillis();
			logger.info("======>请求{}.{}接口完成,耗时:{}", targetName, methodName, (endTime - startTime));
		} catch (Throwable e) {
			// 记录异常信息
			long endTime = System.currentTimeMillis();
			logger.error("======>请求{}.{}接口异常！耗时:{}", targetName, methodName, (endTime - startTime));
			throw e;
		}
		return resultData;
	}
}