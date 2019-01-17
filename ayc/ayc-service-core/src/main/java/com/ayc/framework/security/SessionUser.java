package com.ayc.framework.security;

import java.io.Serializable;

/**
 * Session 用户对象
 * @author ysj
 * @Data 2019-01-15
 */
public class SessionUser implements Serializable{

	private static final long serialVersionUID = 6254531229669227269L;
	/**
	 * 
	 */


	private Integer userId;
	private Integer roleCode;
	private String token;
	private String mobile;
	public Integer getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(Integer roleCode) {
		this.roleCode = roleCode;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}
