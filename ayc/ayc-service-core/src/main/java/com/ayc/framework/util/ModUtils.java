package com.ayc.framework.util;


import com.ayc.framework.common.BizCode;
import com.ayc.framework.common.BizException;

public class ModUtils {

	public static String mod(Integer modId, Integer mod){
		if(null == modId) throw new BizException(BizCode.CLIENT_ERROR);
		if(null == mod) mod = 10;
		int result = modId % mod;
		return   ""+result ;
	}
	
	public static void main(String[] args) {
		System.out.println(ModUtils.mod(18, 30));
	}
}
