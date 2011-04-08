/**
 * http://auzll.iteye.com/
 */
package com.gengweibo.weibo;


/**
 * 
 * @author auzll@msn.com
 * @since 2011-3-17
 */
public final class WeiboManager {
	public static IWeibo newWeibo(WeiboType type) {
		switch (type) {
		case T_163:
			return new Weibo163();
		case T_QQ:
			return new WeiboQQ();
		case T_SINA:
			return new WeiboSina();
		case T_SOHU:
			return new WeiboSohu();
		default:
			throw new WeiException("Unknow weibo type[" + type.getEnName() + "]");
		}
	}
}
