/**
 * http://auzll.iteye.com/
 */
package com.gengweibo.web.param;

import z.action.Context;

import com.gengweibo.weibo.IParam;
import com.gengweibo.weibo.IWeibo;

/**
 * @author auzll@msn.com
 * @since 2011-3-28
 */
public class MoreHomeTimelineParam implements IParam {
    private IWeibo weibo;

    private Context context;

    public MoreHomeTimelineParam(IWeibo weibo, Context context) {
        this.weibo = weibo;
        this.context = context;
    }

    public String getParamValue(String key) {
        if (null == key) {
            return null;
        }

        String value = context.getRequestString(weibo.getWeiboId());

        switch (weibo.getType()) {
            case T_163:
                if (key.endsWith("since_id")) {
                    return value;
                }
                break;
            case T_QQ:
                if (key.endsWith("pagetime")) {
                    return value;
                }
                if (key.endsWith("pageflag")) {
                    return "1";
                }
                break;
            case T_SINA:
                if (key.endsWith("max_id")) {
                    return value;
                }
                break;
            case T_SOHU:
                if (key.endsWith("max_id")) {
                    return value;
                }
                break;
            default:
                break;
        }

        if (key.endsWith("count")) {
            return context.getRequestString(weibo.getWeiboId() + "_count");
        }

        return context.getRequestString(key);
    }

}
