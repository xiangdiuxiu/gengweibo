/**
 * http://auzll.iteye.com/
 */
package com.gengweibo.weibo;

import static com.gengweibo.weibo.Weibo.Method.GET;
import static com.gengweibo.weibo.Weibo.Method.POST;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.oauth.OAuth;
import net.oauth.OAuth.Parameter;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthMessage;
import net.oauth.ParameterStyle;
import net.oauth.client.OAuthClient;
import net.oauth.client.URLConnectionClient;
import net.oauth.http.HttpMessage;

import org.json.JSONException;
import org.json.JSONObject;

import z.action.Common;

/**
 * @author auzll@msn.com
 * @since 2011-3-16
 */
public class WeiboQQ extends Weibo implements IWeibo {
    /**  */
    private static final long serialVersionUID = 5345772573791707652L;

    public WeiboQQ() {
        super("https://open.t.qq.com/cgi-bin/request_token",
                "https://open.t.qq.com/cgi-bin/authorize",
                "https://open.t.qq.com/cgi-bin/access_token",
                "http://open.t.qq.com/api/", OAuthProperty.getCallbackUrl(),
                OAuthProperty.CONSUMER_KEY_QQ,
                OAuthProperty.CONSUMER_SECRET_QQ, WeiboType.T_QQ);
    }

    public Response favoritesCreate(IParam param) {
        String id = param.getParamValue("statusId");
        return sendRequest(toRequestParam("format", "json").add("id", id),
                urlResource + "fav/addt", POST);
    }

    public Response friendshipsCreate(IParam param) {
        String name = param.getParamValue("userId");
        return sendRequest(toRequestParam("format", "json").add("name", name),
                urlResource + "friends/add", POST);
    }

    public Response friendshipsDestroy(IParam param) {
        String name = param.getParamValue("userId");
        return sendRequest(toRequestParam("format", "json").add("name", name),
                urlResource + "friends/del", POST);
    }

    public Response homeTimeline(IParam param) {
        RequestParam reqParam = toRequestParam("reqnum",
                parseLegalPageCount(param.getParamValue("count"))).add(
                "format", "json");

        if (null != param.getParamValue("pagetime")) {
            reqParam.add("pagetime", param.getParamValue("pagetime"));
        }

        if (null != param.getParamValue("pageflag")) {
            reqParam.add("pageflag", param.getParamValue("pageflag"));
        }

        /*
         * pageflag 分页标识（0：第一页，1：向下翻页，2向上翻页） pagetime 本页起始时间（第一页
         * 0，继续：根据返回记录时间决定）
         */
        return sendRequest(reqParam, urlResource + "statuses/home_timeline",
                GET);
    }

    public String makeClickUrl() throws WeiException {
        OAuthClient client = new OAuthClient(new URLConnectionClient());
        try {
            List<Map.Entry<?, ?>> parameters = Common.newList();
            String callback = accessor.consumer.callbackURL;
            if (null == callback) {
                callback = "null";
            }
            parameters.add(new OAuth.Parameter("oauth_callback", callback));
            client.getRequestToken(accessor, null, parameters);
        } catch (Exception e) {
            throw new WeiException(e);
        }

        Collection<Parameter> parameters = Common.newList();
        parameters.add(new Parameter("oauth_token", accessor.requestToken));

        try {
            OAuthMessage request = accessor.newRequestMessage(GET.toString(),
                    accessor.consumer.serviceProvider.userAuthorizationURL,
                    parameters);
            Object accepted = accessor.consumer
                    .getProperty(OAuthConsumer.ACCEPT_ENCODING);
            if (accepted != null) {
                request.getHeaders().add(
                        new OAuth.Parameter(HttpMessage.ACCEPT_ENCODING,
                                accepted.toString()));
            }
            Object ps = accessor.consumer
                    .getProperty(OAuthClient.PARAMETER_STYLE);
            ParameterStyle style = (ps == null) ? ParameterStyle.BODY : Enum
                    .valueOf(ParameterStyle.class, ps.toString());
            HttpMessage httpRequest = HttpMessage.newRequest(request, style);
            return httpRequest.url.toString();
        } catch (Exception e) {
            throw new WeiException(e);
        }
    }

    public Response statusesComments(IParam param) {
        RequestParam reqParam = toRequestParam("reqnum", PAGE_MAX).add(
                "format", "json")
                .add("rootid", param.getParamValue("statusId"));

        // TODO
        /**
         * rootid 转发或者回复根结点id pageflag （根据dwtime），0：第一页，1：向下翻页，2向上翻页； pagetime
         * 起始时间戳，上下翻页时才有用，取第一页时忽略；
         */
        if (null != param.getParamValue("pagetime")) {
            reqParam.add("pagetime", param.getParamValue("pagetime"));
        }

        if (null != param.getParamValue("pageflag")) {
            reqParam.add("pageflag", param.getParamValue("pageflag"));
        }

        return sendRequest(reqParam, urlResource + "t/re_list", GET);
    }

    public Response statusesDestroy(IParam param) {
        String id = param.getParamValue("statusId");
        return sendRequest(toRequestParam("format", "json").add("id", id),
                urlResource + "t/del", POST);
    }

    public Response statusesReply(IParam param) {
        String id = param.getParamValue("statusId");
        String status = param.getParamValue("status");
        return sendRequest(
                toRequestParam("format", "json").add("reid", id)
                        .add("content", status)
                        .add("clientip", param.getParamValue("clientip")),
                urlResource + "t/re_add", POST);
    }

    public Response statusesRetweet(IParam param) {
        String id = param.getParamValue("statusId");

        RequestParam reqParam = toRequestParam("format", "json")
                .add("reid", id).add("clientip",
                        param.getParamValue("clientip"));
        if (null != param.getParamValue("status")) {
            reqParam.add("content", param.getParamValue("status"));
        }

        return sendRequest(reqParam, urlResource + "t/re_add", POST);
    }

    public Response statusesUpdate(IParam param) {
        return sendRequest(
                toRequestParam("format", "json").add("content",
                        param.getParamValue("status")).add("clientip",
                        param.getParamValue("clientip")),
                urlResource + "t/add", POST);
    }

    public Response verifyCredentials() {
        return sendRequest(toRequestParam("format", "json"), urlResource
                + "user/info", GET);
    }

    public void bindWeiboAccountContext() {
        if (null == weiboId && null == weiboAccountName) {
            Response response = verifyCredentials();
            try {
                JSONObject jsonObject = new JSONObject(
                        response.readBodyAsString());
                weiboId = type.getEnName() + "_"
                        + jsonObject.getJSONObject("data").getString("name");
                weiboAccountName = jsonObject.getJSONObject("data").getString(
                        "nick");
            } catch (JSONException e) {
                throw new WeiException(e);
            }
        }
    }

    public Response statusesMentions(IParam param) {
        RequestParam reqParam = toRequestParam("reqnum",
                parseLegalPageCount(param.getParamValue("count"))).add(
                "format", "json");

        if (null != param.getParamValue("pagetime")) {
            reqParam.add("pagetime", param.getParamValue("pagetime"));
        }

        if (null != param.getParamValue("pageflag")) {
            reqParam.add("pageflag", param.getParamValue("pageflag"));
        }

        return sendRequest(reqParam,
                urlResource + "statuses/mentions_timeline", GET);
    }

    public Response statusesCommentsToMe(IParam param) {
        // 腾讯无此api
        return null;
    }
}
