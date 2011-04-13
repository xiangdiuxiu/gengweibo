/**
 * http://auzll.iteye.com/
 */
package com.gengweibo.dao;

import java.util.List;

import com.gengweibo.weibo.IWeibo;

/**
 * 
 * @author auzll@msn.com
 * @since 2011-3-18
 */
public interface WeiboDao {
	/**
	 * 保存新连接的微博帐户，如果原本有weiboId相同的，覆盖之(为了保障token的有效性)。
	 */
	void save(IWeibo weibo);
	
	/**
	 * 更新微博帐户的同步状态，也就是是否同步推送微博
	 */
	void updateSyn(String weiboId, boolean synUpdate);
	
	/**
	 * 删除已连接的微博帐户
	 */
	void delete(String weiboId);
	
	/**
	 * 根据weiboId查询相关的已连接的微博帐户。weiboId找到accountId，再根据accountId加载所有的微博帐户。
	 */
	List<IWeibo> queryRelatedList(String weiboId);
}
