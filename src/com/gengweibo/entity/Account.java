/**
 * http://auzll.iteye.com/
 */
package com.gengweibo.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.gengweibo.weibo.IWeibo;
import com.google.inject.internal.Maps;

/**
 * @author auzll@msn.com
 * @since 2011-3-17
 */
public class Account implements Serializable {
    /**  */
    private static final long serialVersionUID = 2608646627786083846L;

    /** 用户ID */
    private String id;

    private Map<String, IWeibo> weiboMap = Maps.newHashMap();

    private boolean init = false;

    public static final String ACCOUNT_SESSION_KEY = "__SESSION_ACCOUNT";

    private IWeibo linkingWeibo;

    public IWeibo getLinkingWeibo() {
        return linkingWeibo;
    }

    public void setLinkingWeibo(IWeibo linkingWeibo) {
        this.linkingWeibo = linkingWeibo;
    }

    public Map<String, IWeibo> getWeiboMap() {
        return weiboMap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void loadWeibo(List<IWeibo> weiboList) {
        if (!init) {
            init = true;
            if (null != weiboList) {
                for (IWeibo w: weiboList) {
                    weiboMap.put(w.getWeiboId(), w);
                }
            }
        }
    }

    public boolean isInit() {
        return init;
    }

}
