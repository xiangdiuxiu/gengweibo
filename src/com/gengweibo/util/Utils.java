/**
 * http://auzll.iteye.com/
 */
package com.gengweibo.util;



/**
 * 
 * @author auzll@msn.com
 * @since 2011-3-23
 */
public final class Utils {
	/**
	 * 每个用户最多可以同时连接多少个微博账户
	 */
	public static final int MAX_LINKING_NUM = 20;
	
	public static int getMaxLinkingNum() {
		return MAX_LINKING_NUM;
	}
	
	public static boolean isStringEmpty(String str) {
		if (null == str) {
			return true;
		}
		str = str.trim();
		if (0 == str.length()) {
			return true;
		}
		return false;
	}
	
}
