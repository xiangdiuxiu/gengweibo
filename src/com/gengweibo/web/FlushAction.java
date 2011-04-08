/**
 * http://auzll.iteye.com/
 */
package com.gengweibo.web;

import z.action.ActionSupport;
import z.action.Api;
import z.action.Result;

/**
 * 
 * @author auzll@msn.com
 * @since 2011-3-10
 */
public class FlushAction extends ActionSupport {
	private static final Result RESULT = new Result("/index.jsp");
	
	@Api("flush")
	public Result flush() {
		return RESULT;
	}
}
 