/**
 * http://auzll.iteye.com/
 */
package com.gengweibo.weibo;

/**
 * @author auzll@msn.com
 * @since 2011-3-16
 */
public class Weibo163 extends Weibo implements IWeibo {

    /**  */
    private static final long serialVersionUID = -793867621702415464L;

    public Weibo163() {
        super("http://api.t.163.com/oauth/request_token",
                "http://api.t.163.com/oauth/authenticate",
                "http://api.t.163.com/oauth/access_token",
                "http://api.t.163.com", OAuthProperty.getCallbackUrl(),
                OAuthProperty.CONSUMER_KEY_163,
                OAuthProperty.CONSUMER_SECRET_163, WeiboType.T_163);
    }
}
