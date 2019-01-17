package com.ayc.framework.aspect;

import com.ayc.framework.common.AjaxResult;
import com.ayc.framework.common.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 全局异常处理
 * @author ysj
 * @Data 2019-01-15
 */
//@ControllerAdvice
@Deprecated
public class GlobalExceptionHandler {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

//	@ExceptionHandler(BizException.class)
//	@ResponseBody
	public AjaxResult handleBizException(BizException e) {
		logger.error(e.getCode().getMessage(), e);
		return AjaxResult.failed(e.getCode());
	}

//	@ExceptionHandler(Exception.class)
//	@ResponseBody
	public AjaxResult handleSysException(Exception e) {
		logger.error(e.getMessage(), e);
		return AjaxResult.failed();
	}

}
