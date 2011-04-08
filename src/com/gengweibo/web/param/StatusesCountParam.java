/**
 * http://auzll.iteye.com/
 */
package com.gengweibo.web.param;

import z.action.Context;

import com.gengweibo.weibo.IParam;
import com.gengweibo.weibo.IWeibo;

/**
 * 
 * @author auzll@msn.com
 * @since 2011-3-28
 */
public class StatusesCountParam implements IParam {
	private IWeibo weibo;
	private Context context;
	
	public StatusesCountParam(IWeibo weibo, Context context) {
		this.weibo = weibo;
		this.context = context;
	}

	public String getParamValue(String key) {
		if (null == key) {
			return null;
		}
		
		return context.getRequestString(weibo.getWeiboId());
	}

}
