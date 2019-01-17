package com.ayc.framework.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 远程调用返回结果
 *
 * @author ysj
 * @Data 2019-01-15
 *
 */
public class AjaxResult implements Serializable {

	private static final long serialVersionUID = -1875636223604772405L;
	/**
	 * 
	 */


	
	private String code;
	
	private String message;
	
	private Object data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public AjaxResult setData(Object data) {
		this.data = data;
		return this;
	}
	
	public static AjaxResult success(Object data){
		return AjaxResult.success().setData(data);
	}
	
	@SuppressWarnings("unchecked")
	public AjaxResult putDataItem(String key , Object value){
		if(this.data == null){
			this.data = new HashMap<String,Object>();
		}
		((Map<String,Object>)this.data).put(key, value);
		return this;
	}
	
	public AjaxResult putObj(Object data){
		this.data = data;
		
		return this;
	}
	
	public static AjaxResult success(){
		AjaxResult result = new AjaxResult();
		result.setCode(BizCode.SUCCESS.getCode());
		result.setMessage(BizCode.SUCCESS.getMessage());
		return result;
	}
	
	public static AjaxResult failed(ICode ibizCode){
		AjaxResult result = new AjaxResult();
		String code = BizCode.SERVER_ERROR.getCode();
		String message = BizCode.SERVER_ERROR.getMessage();
		if(null != ibizCode){
			code = ibizCode.getCode();
			message = ibizCode.getMessage();
		}
		result.setCode(code);
		result.setMessage(message);
		
		return result;
	}
	
	public static AjaxResult failed() {
		AjaxResult result = new AjaxResult();
		result.setCode(BizCode.SERVER_ERROR.getCode());
		result.setMessage(BizCode.SERVER_ERROR.getMessage());
		return result;
	}

	
}
