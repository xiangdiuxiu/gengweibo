/**
 * http://auzll.iteye.com/
 */
package com.gengweibo.weibo;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthMessage;

/**
 * OAuthMessage Wrapper
 * @author auzll@msn.com
 * @since 2011-3-16
 */
public class Response {
	private OAuthMessage response;
	public String method;
    public String URL;

	public Response(OAuthMessage response) {
		this.response = response;
		this.method = response.method;
		this.URL = response.URL;
	}

	public List<Entry<String, String>> getParameters() {
		try {
			return response.getParameters();
		} catch (Exception e) {
			throw new WeiException(e);
		}
	}

	
	public void addParameter(String key, String value) {
		response.addParameter(key, value);
	}

	
	public void addParameter(Entry<String, String> parameter) {
		response.addParameter(parameter);
	}

	
	public void addParameters(Collection<? extends Entry<String, String>> parameters) {
		response.addParameters(parameters);
	}

	
	public String getParameter(String name) {
		try {
			return response.getParameter(name);
		} catch (Exception e) {
			throw new WeiException(e);
		}
	}

	
	public String getConsumerKey() {
		try {
			return response.getConsumerKey();
		} catch (Exception e) {
			throw new WeiException(e);
		}
	}

	
	public String getToken() {
		try {
			return response.getToken();
		} catch (Exception e) {
			throw new WeiException(e);
		}
	}

	
	public String getSignatureMethod() {
		try {
			return response.getSignatureMethod();
		} catch (Exception e) {
			throw new WeiException(e);
		}
	}

	
	public String getSignature() {
		try {
			return response.getSignature();
		} catch (Exception e) {
			throw new WeiException(e);
		}
	}

	
	public String getBodyType() {
		return response.getBodyType();
	}

	
	public String getBodyEncoding() {
		return response.getBodyEncoding();
	}

	
	public String getHeader(String name) {
		return response.getHeader(name);
	}

	
	public List<Entry<String, String>> getHeaders() {
		return response.getHeaders();
	}

	
	public String readBodyAsString() {
		try {
			return response.readBodyAsString();
		} catch (Exception e) {
			throw new WeiException(e);
		}
	}

	
	public InputStream getBodyAsStream() {
		try {
			return response.getBodyAsStream();
		} catch (Exception e) {
			throw new WeiException(e);
		}
	}

	
	public Map<String, Object> getDump() {
		try {
			return response.getDump();
		} catch (Exception e) {
			throw new WeiException(e);
		}
	}

	
	public void requireParameters(String... names) {
		try {
			response.requireParameters(names);
		} catch (Exception e) {
			throw new WeiException(e);
		}
	}

	
	public void addRequiredParameters(OAuthAccessor accessor) {
		try {
			response.addRequiredParameters(accessor);
		} catch (Exception e) {
			throw new WeiException(e);
		}
	}

	
	public void sign(OAuthAccessor accessor) {
		try {
			response.sign(accessor);
		} catch (Exception e) {
			throw new WeiException(e);
		}
	}

	
	public String getAuthorizationHeader(String realm) {
		try {
			return response.getAuthorizationHeader(realm);
		} catch (Exception e) {
			throw new WeiException(e);
		}
	}
	
}
