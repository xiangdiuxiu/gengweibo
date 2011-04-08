/**
 * http://auzll.iteye.com/
 */
package com.gengweibo.weibo;

import static com.gengweibo.weibo.Weibo.Method.GET;
import static com.gengweibo.weibo.Weibo.Method.POST;

/**
 * 
 * @author auzll@msn.com
 * @since 2011-3-16
 */
public class WeiboSina extends Weibo implements IWeibo {
	/**  */
	private static final long serialVersionUID = -1882537453955736664L;

	public WeiboSina() {
		super("http://api.t.sina.com.cn/oauth/request_token",
				"http://api.t.sina.com.cn/oauth/authorize",
				"http://api.t.sina.com.cn/oauth/access_token",
				"http://api.t.sina.com.cn", OAuthProperty.getCallbackUrl(), OAuthProperty.CONSUMER_KEY_SINA,
				OAuthProperty.CONSUMER_SECRET_SINA, WeiboType.T_SINA);
	}
	
	public Response statusesComments(IParam param) {
		RequestParam reqParam = new RequestParam();
		reqParam.add("count", PAGE_MAX).add("id", param.getParamValue("statusId"));
		String page = "1";
		if (null != param.getParamValue("page")) {
			page = param.getParamValue("page");
		}
		reqParam.add("page", page);
		return sendRequest(reqParam, urlResource + "statuses/comments.json", GET);
	}
	
	public Response statusesRetweet(IParam param) {
		return sendRequest(toRequestParam("id", param.getParamValue("statusId")), urlResource + "statuses/retweet.json", POST);
	}
	
	public Response statusesReply(IParam param) {
		return sendRequest(toRequestParam("comment", param.getParamValue("status")).add("id", param.getParamValue("statusId")), urlResource + "statuses/comment.json", POST);
	}
	
	public Response statusesCounts(IParam param) {
		return sendRequest(toRequestParam("ids", param.getParamValue("statusIds")), urlResource + "statuses/counts.json", GET);
	}
}
