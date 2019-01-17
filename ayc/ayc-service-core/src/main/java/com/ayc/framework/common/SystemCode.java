package com.ayc.framework.common;

/**
 * 系统编码:对应错误编码
 * @author ysj
 * @Data 2019-01-15
 *
 */
public enum SystemCode implements ICode {

	DEFAULT_SYS("0","微信小程序"),
	MINIAPP_SELLER_SYS("1","APP"),;
	
	private String code;
	
	private String message;
	
	private SystemCode(String code, String message){
		this.code = code;
		this.message = message;
	}

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
	
	
}
