/**
 * http://auzll.iteye.com/
 */
package com.gengweibo.weibo;

import static com.gengweibo.weibo.Weibo.Method.GET;
import static com.gengweibo.weibo.Weibo.Method.POST;

import org.json.JSONException;
import org.json.JSONObject;



/**
 * 
 * @author auzll@msn.com
 * @since 2011-3-16
 */
public class WeiboSohu extends Weibo implements IWeibo {

	
	/**  */
	private static final long serialVersionUID = 3375792387585554576L;

	public WeiboSohu() {
		super("http://api.t.sohu.com/oauth/request_token",
				"http://api.t.sohu.com/oauth/authorize",
				"http://api.t.sohu.com/oauth/access_token",
				"http://api.t.sohu.com", OAuthProperty.getCallbackUrl(), OAuthProperty.CONSUMER_KEY_SOHU,
				OAuthProperty.CONSUMER_SECRET_SOHU,  WeiboType.T_SOHU);
	}

	public void bindWeiboAccountContext() {
		if (null == weiboId && null == weiboAccountName) {
			Response response = verifyCredentials();
			try {
				JSONObject jsonObject = new JSONObject(response.readBodyAsString());
				weiboId = getType().getEnName() + "_" + jsonObject.getString("id");
				weiboAccountName = jsonObject.getString("screen_name");
				if (null != weiboAccountName) {
					weiboAccountName = weiboAccountName.trim();
				}
			} catch (JSONException e) {
				throw new WeiException(e);
			}
		}
	}
	
	public Response favoritesCreate(IParam param) {
		String id = param.getParamValue("statusId");
		return sendRequest(null, urlResource + "favourites/create/" + id + ".json", POST);
	}

	public Response friendshipsCreate(IParam param) {
		String userId = param.getParamValue("userId");
		return sendRequest(null, urlResource + "friendships/create/" + userId + ".json", POST);
	}

	public Response friendshipsDestroy(IParam param) {
		String userId = param.getParamValue("userId");
		return sendRequest(null, urlResource + "friendships/destroy/" + userId + ".json", POST);
	}

	public Response homeTimeline(IParam param) {
		RequestParam reqParam = toRequestParam("count", parseLegalPageCount(param.getParamValue("count")));
		
		if (null != param.getParamValue("max_id")) {
			reqParam.add("max_id", param.getParamValue("max_id"));
		}
		
		return sendRequest(reqParam, urlResource + "statuses/friends_timeline.json", GET);
	}
	
	public Response statusesComments(IParam param) {
		RequestParam reqParam = toRequestParam("count", PAGE_MAX);
		String page = "1";
		if (null != param.getParamValue("page")) {
			page = param.getParamValue("page");
		}
		reqParam.add("page", page);
		return sendRequest(reqParam, urlResource + "statuses/comments/" + param.getParamValue("statusId") + ".json", GET);
	}

	public Response statusesCounts(IParam param) {
		return sendRequest(toRequestParam("ids", param.getParamValue("statusIds")), urlResource + "statuses/counts.json", GET);
	}

	public Response statusesReply(IParam param) {
		return sendRequest(toRequestParam("comment", param.getParamValue("status")).add("id", param.getParamValue("statusId")), urlResource + "statuses/comment.json", POST);
	}

	public Response statusesRetweet(IParam param) {
		String id = param.getParamValue("statusId");
		
		RequestParam reqParam = null;
		if (null != param.getParamValue("status")) {
			reqParam = toRequestParam("status", param.getParamValue("status"));
		}
		
		return sendRequest(reqParam, urlResource + "statuses/transmit/" + id + ".json", POST);
	}
}
