/**
 * http://auzll.iteye.com/
 */
package com.gengweibo.web;

import z.action.Dispatcher;

import com.google.inject.Injector;

/**
 * 
 * @author auzll@msn.com
 * @since 2011-3-23
 */
public class WeiDispatcher extends Dispatcher {
	
	/**  */
	private static final long serialVersionUID = -4978339273790400207L;
	
	protected Object getInstance(Class<?> action) {
		return ( (Injector) config.getServletContext().getAttribute(Injector.class.getName()) ).getInstance(action);
	}
}
