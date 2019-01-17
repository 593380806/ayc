package com.ayc.framework.common;

/**
 * 服务业务异常
 * @author ysj
 * @Data 2019-01-15
 *
 */
public class BizException  extends RuntimeException{

	private static final long serialVersionUID = -5757211549353937789L;
	/**
	 * 
	 */


	
	private ICode code;
	
	
	private String errMsg;
	

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public ICode getCode() {
		return code;
	}

	public void setCode(BizCode code) {
		this.code = code;
	}
	
	

	public BizException(){
		super();
	}
	
	
	public BizException(ICode code){
		this.code = code;
	}
	
}
