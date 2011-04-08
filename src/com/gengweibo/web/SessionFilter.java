/**
 * http://auzll.iteye.com/
 */
package com.gengweibo.web;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gengweibo.entity.Account;

/**
 * 
 * @author auzll@msn.com
 * @since 2011-3-21
 */
public class SessionFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filter) throws IOException, ServletException {
		String api = request.getParameter("api");
		if (null == api) {
			String queryString = ((HttpServletRequest)request).getQueryString();
			if (queryString.length() > 0) {
				Pattern pattern = Pattern.compile("(\\?|&){0,1}api=(.*?)&{0,1}");
	            Matcher matcher = pattern.matcher( queryString );
	            if (matcher.matches() && matcher.groupCount() >= 2) {
	            	api = matcher.group(2);
	            }
			}
		}
		
		if (null != api 
				&& !"link".equalsIgnoreCase(api) 
				&& !"logout".equalsIgnoreCase(api)
				&& !"flush".equalsIgnoreCase(api)
				&& null == ((HttpServletRequest)request).getSession().getAttribute(Account.ACCOUNT_SESSION_KEY)) {
			((HttpServletResponse) response).sendRedirect(((HttpServletRequest)request).getContextPath() + "/error.jsp?desc=UnLogin");
			return;
		}
		
		filter.doFilter(request, response);
	}

	public void init(FilterConfig config) throws ServletException {
	}
}
