/**
 * http://auzll.iteye.com/
 */
package com.gengweibo.dao.mem;

import java.util.List;
import java.util.Map;

import z.action.Common;

import com.gengweibo.dao.WeiboDao;
import com.gengweibo.weibo.IWeibo;

/**
 * @author auzll@msn.com
 * @since 2011-3-18
 */
public class WeiboDaoMemoryImpl implements WeiboDao {
    private static final Map<String, List<IWeibo>> accountMap = Common.newMap();

    private static final Map<String, IWeibo> weiboMap = Common.newMap();

    public void save(IWeibo weibo) {
        String accountId = weibo.getAccountId();
        String weiboId = weibo.getWeiboId();

        // 先尝试删除
        delete(weiboId);

        weiboMap.put(weiboId, weibo);
        List<IWeibo> weiboList = accountMap.get(accountId);
        if (null == weiboList) {
            weiboList = Common.newList();
            accountMap.put(accountId, weiboList);
        }
        weiboList.add(weibo);
    }

    public void delete(String weiboId) {
        IWeibo weibo = weiboMap.get(weiboId);
        if (null != weibo) {
            weiboMap.remove(weiboId);
            String accountId = weibo.getAccountId();
            List<IWeibo> weiboList = accountMap.get(accountId);
            if (null != weiboList && weiboList.size() > 0) {
                int find = -1;
                for (int i = 0; i < weiboList.size(); i++) {
                    IWeibo w = weiboList.get(i);
                    if (w.getWeiboId().equals(weiboId)) {
                        find = i;
                        break;
                    }
                }
                weiboList.remove(find);
            }
        }
    }

    public List<IWeibo> queryRelatedList(String weiboId) {
        IWeibo weibo = weiboMap.get(weiboId);
        if (null != weibo) {
            return accountMap.get(weibo.getAccountId());
        }
        return null;
    }

    public void updateSyn(String weiboId, boolean synUpdate) {
        IWeibo weibo = weiboMap.get(weiboId);
        if (null != weibo) {
            weibo.setSynUpdate(synUpdate);
        }
    }

}
