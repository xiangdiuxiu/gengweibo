/**
 * http://auzll.iteye.com/
 */
package com.gengweibo.weibo;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import z.action.Context;

/**
 * oauth相关参数
 * @author auzll@msn.com
 * @since 2011-3-16
 */
public final class OAuthProperty {
	public static String getCallbackUrl() {
		String callback = "/execute.do?api=callback";
			
		Context context = Context.getActionContext();
		if (null != context) {
			HttpServletRequest request = context.getRequest();
			String url = request.getScheme() + "://" + request.getServerName();
			if (80 != request.getServerPort() && 443 != request.getServerPort()) {
				url += (":" + request.getServerPort());
			}
			url += (request.getContextPath() + callback);
			return url;
		}
		
		return callback;
	}
	
	private static final String OAUTH_PROPERTIES = "oauth.properties";
	
	/** 网易微博oauth key，从src目录下的oauth.properties加载 */
	public static final String CONSUMER_KEY_163;
	
	/** 网易微博oauth secret，从src目录下的oauth.properties加载 */
	public static final String CONSUMER_SECRET_163;
	
	/** 腾讯微博oauth key，从src目录下的oauth.properties加载 */
	public static final String CONSUMER_KEY_QQ;
	
	/** 腾讯微博oauth secret，从src目录下的oauth.properties加载 */
	public static final String CONSUMER_SECRET_QQ;
	
	/** 新浪微博oauth key，从src目录下的oauth.properties加载 */
	public static final String CONSUMER_KEY_SINA;
	
	/** 新浪微博oauth secret，从src目录下的oauth.properties加载 */
	public static final String CONSUMER_SECRET_SINA;
	
	/** 搜狐微博oauth key，从src目录下的oauth.properties加载 */
	public static final String CONSUMER_KEY_SOHU;
	
	/** 搜狐微博oauth secret，从src目录下的oauth.properties加载 */
	public static final String CONSUMER_SECRET_SOHU;
	
	private static String trimAndGetProperty(Properties props, String key) {
		String value = props.getProperty(key);
		return null != value ? value.trim() : null;
	}
	
	static {
		InputStream is = null;
        try {
        	is = Thread.currentThread().getContextClassLoader().getResourceAsStream(OAUTH_PROPERTIES);
    		Properties props = new Properties();
			props.load(is);
			
			CONSUMER_KEY_163 = trimAndGetProperty(props, "CONSUMER_KEY_163");
			CONSUMER_SECRET_163 = trimAndGetProperty(props, "CONSUMER_SECRET_163");
			
			CONSUMER_KEY_QQ = trimAndGetProperty(props, "CONSUMER_KEY_QQ");
			CONSUMER_SECRET_QQ = trimAndGetProperty(props, "CONSUMER_SECRET_QQ");
			
			CONSUMER_KEY_SINA = trimAndGetProperty(props, "CONSUMER_KEY_SINA");
			CONSUMER_SECRET_SINA = trimAndGetProperty(props, "CONSUMER_SECRET_SINA");
			
			CONSUMER_KEY_SOHU = trimAndGetProperty(props, "CONSUMER_KEY_SOHU");
			CONSUMER_SECRET_SOHU = trimAndGetProperty(props, "CONSUMER_SECRET_SOHU");
			
		} catch (IOException e) {
			throw new ExceptionInInitializerError(e);
		} finally {
			if (null != is) {
                try {
                	is.close();
                } catch (IOException e) {}
            }
		}
	}
	
}
