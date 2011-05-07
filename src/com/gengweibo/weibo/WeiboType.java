/**
 * http://auzll.iteye.com/
 */
package com.gengweibo.weibo;

import java.util.List;

import com.gengweibo.util.Utils;
import com.google.inject.internal.Lists;

/**
 * @author auzll@msn.com
 * @since 2011-3-17
 */
public enum WeiboType {
    T_163(0), T_QQ(1), T_SINA(2), T_SOHU(3);
    private static final String[] CN = {
        "网易微博", "腾讯微博", "新浪微博", "搜狐微博"
    };

    private int index;

    private WeiboType(int index) {
        this.index = index;
    }

    public String getEnName() {
        return this.toString();
    }

    public String getCnName() {
        return CN[index];
    }

    private static WeiboType[] weiboTypeArray = null;

    public static WeiboType[] getPublicTypes() {
        if (null == weiboTypeArray) {
            List<WeiboType> list = Lists.newArrayList();
            if (!Utils.isStringEmpty(OAuthProperty.CONSUMER_KEY_163)
                    && !Utils.isStringEmpty(OAuthProperty.CONSUMER_SECRET_163)) {
                list.add(T_163);
            }
            if (!Utils.isStringEmpty(OAuthProperty.CONSUMER_KEY_QQ)
                    && !Utils.isStringEmpty(OAuthProperty.CONSUMER_SECRET_QQ)) {
                list.add(T_QQ);
            }
            if (!Utils.isStringEmpty(OAuthProperty.CONSUMER_KEY_SINA)
                    && !Utils.isStringEmpty(OAuthProperty.CONSUMER_SECRET_SINA)) {
                list.add(T_SINA);
            }
            if (!Utils.isStringEmpty(OAuthProperty.CONSUMER_KEY_SOHU)
                    && !Utils.isStringEmpty(OAuthProperty.CONSUMER_SECRET_SOHU)) {
                list.add(T_SOHU);
            }
            weiboTypeArray = list.toArray(new WeiboType[list.size()]);
        }
        return weiboTypeArray;
    }

    public static WeiboType of(String value) {
        if (null == value) {
            return null;
        }
        try {
            return valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
