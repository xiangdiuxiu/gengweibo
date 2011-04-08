/**
 * http://auzll.iteye.com/
 */
package com.gengweibo.weibo;


/**
 * 主要是本系统需要用到的一些微博操作的接口
 * @author auzll@msn.com
 * @since 2011-3-16
 */
public interface IWeibo {
	/**
	 * 从原微博厂商获取微博账户的信息，绑定到本系统中
	 */
	void bindWeiboAccountContext();
	
	/**
	 * 根据requestToken转换accessToken
	 * @param param 获取参数的接口，具体实现类根据实际需要从中获取
	 */
	void convertAccessToken(IParam param);

	/**
	 * 添加收藏
	 * @param param 获取参数的接口，具体实现类根据实际需要从中获取
	 */
	Response favoritesCreate(IParam param);
	
	/**
	 * 关注指定用户
	 * @param param 获取参数的接口，具体实现类根据实际需要从中获取
	 */
	Response friendshipsCreate(IParam param);
	
	/**
	 * 取消关注指定用户
	 * @param param 获取参数的接口，具体实现类根据实际需要从中获取
	 */
	Response friendshipsDestroy(IParam param);
	
	/**
	 * 获取此微博所关联的账户ID
	 */
	String getAccountId();
	
	/**
	 * 获取微博类型
	 */
	WeiboType getType();
	
	/**
	 * 获取此微博所关联的账户昵称
	 */
	String getWeiboAccountName();
	
	/**
	 * 获取此微博在本系统中的ID，注意区别微博内容的ID
	 */
	String getWeiboId();
	
	/**
	 * 获取当前登录用户的微博列表
	 * @param param 获取参数的接口，具体实现类根据实际需要从中获取
	 */
	Response homeTimeline(IParam param);
	
	/**
	 * 生成用于oauth登录的url地址
	 */
	String makeClickUrl();
	
	/**
	 * 设置此微博所关联的账户ID(如果该账户是第一次使用，用UUID来生成)
	 */
	void setAccountId(String accountId);
	
	/**
	 * 设置此微博所关联的账户昵称
	 */
	void setWeiboAccountName(String accountName);
	
	/**
	 * 设置此微博在本系统中的ID(由算法决定了每个账户的同一个微博的ID是相同的，每个ID
	 * 包含了微博类型和微博账户在原微博厂商的userId，例如T_163_123123, T_QQ_zzzz,  T_SINA_1231235)
	 */
	void setWeiboId(String weiboId);
	
	/**
	 * 获取指定微博的所有评论
	 * @param param 获取参数的接口，具体实现类根据实际需要从中获取
	 */
	Response statusesComments(IParam param);
	
	/**
	 * 删除一条新微博
	 * @param param 获取参数的接口，具体实现类根据实际需要从中获取
	 */
	Response statusesDestroy(IParam param);
	
	/**
	 * 评论一条微博
	 * @param param 获取参数的接口，具体实现类根据实际需要从中获取
	 */
	Response statusesReply(IParam param);
	
	/**
	 * 转发一条微博
	 * @param param 获取参数的接口，具体实现类根据实际需要从中获取
	 */
	Response statusesRetweet(IParam param);
	
	/**
	 * 发布一条新微博
	 * @param param 获取参数的接口，具体实现类根据实际需要从中获取
	 */
	Response statusesUpdate(IParam param);
	
	/**
	 * 判断当前用户是否验证成功并返回该用户信息
	 */
	Response verifyCredentials();
}