/**
 * http://auzll.iteye.com/
 */
package com.gengweibo.weibo;

/**
 * 
 * @author auzll@msn.com
 * @since 2011-3-16
 */
public class WeiException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5804811989255862380L;

	/**
	 * 
	 */
	public WeiException() {
	}

	/**
	 * @param message
	 */
	public WeiException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public WeiException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public WeiException(String message, Throwable cause) {
		super(message, cause);
	}

}
