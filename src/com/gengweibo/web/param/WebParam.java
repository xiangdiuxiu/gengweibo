/**
 * http://auzll.iteye.com/
 */
package com.gengweibo.web.param;

import z.action.Context;

import com.gengweibo.weibo.IParam;

/**
 * 
 * @author auzll@msn.com
 * @since 2011-3-28
 */
public class WebParam implements IParam {
	private Context context;
	
	public WebParam(Context context) {
		this.context = context;
	}
	
	public String getParamValue(String key) {
		if (null != key && key.endsWith("clientip")) {
			return context.getRequestIp();
		}
		return context.getRequestString(key);
	}
}
