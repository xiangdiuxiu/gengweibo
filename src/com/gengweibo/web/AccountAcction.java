/**
 * http://auzll.iteye.com/
 */
package com.gengweibo.web;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import z.action.ActionSupport;
import z.action.Api;
import z.action.Context;
import z.action.Result;

import com.gengweibo.dao.WeiboDao;
import com.gengweibo.entity.Account;
import com.gengweibo.util.Utils;
import com.gengweibo.web.param.MoreHomeTimelineParam;
import com.gengweibo.web.param.StatusesCountParam;
import com.gengweibo.web.param.WebParam;
import com.gengweibo.weibo.IParam;
import com.gengweibo.weibo.IWeibo;
import com.gengweibo.weibo.Response;
import com.gengweibo.weibo.WeiboManager;
import com.gengweibo.weibo.WeiboSina;
import com.gengweibo.weibo.WeiboSohu;
import com.gengweibo.weibo.WeiboType;
import com.google.inject.Inject;

/**
 * 
 * @author auzll@msn.com
 * @since 2011-3-10
 */
public class AccountAcction extends ActionSupport {
	private static final Log LOG = LogFactory.getLog(AccountAcction.class);
	
	private Account getCurrentAccount() {
		return (Account) getSession().getAttribute(Account.ACCOUNT_SESSION_KEY);
	}
	
	private void setCurrentAccount(Account account) {
		getSession().setAttribute(Account.ACCOUNT_SESSION_KEY, account);
	}
	
	private Result newErrorResult() {
		return new Result(getRequest().getContextPath() + "/error.jsp", true);
	}
	
	@Inject
	private WeiboDao weiboDao;
	
	@Api("link")
	public Result link() {
		String typeString = getRequestString("type");
		WeiboType type = WeiboType.of(typeString);
		
		if (null == type) {
			return newErrorResult().addValue("desc", "UnknowType[" + typeString + "]");
		} else {
			if (null != getCurrentAccount() && getCurrentAccount().getWeiboMap().size() > Utils.getMaxLinkingNum()) {
				return newErrorResult().addValue("desc", "Over capacity[" + Utils.getMaxLinkingNum() + "]");
			} 
			
			IWeibo weibo = WeiboManager.newWeibo(type);
			Result result = new Result(weibo.makeClickUrl(), true);
			
			Account account = getCurrentAccount();
			if (null == account) {
				account = new Account();
			}
			account.setLinkingWeibo(weibo);
			setCurrentAccount(account);
			
			return result;
		}
	}
	
	@Api("main")
	public Result main() {
		return new Result("/WEB-INF/jsp/main.jsp");
	}
	
	@Api("atMe")
	public Result atMe() {
		return new Result("/WEB-INF/jsp/atMe.jsp");
	}
	
	@Api("callback")
	public Result callback() {
		Account account = getCurrentAccount();
		
		IWeibo weibo = account.getLinkingWeibo();
		if (null != weibo) {
			weibo.convertAccessToken(new WebParam(getContext()));
			account.setLinkingWeibo(null);
			
			weibo.bindWeiboAccountContext();
			String weiboId = weibo.getWeiboId();
			
			if (!account.isInit()) {
				// 初始化
				List<IWeibo> weiboList = weiboDao.queryRelatedList(weiboId);
				if (null == weiboList || weiboList.size() < 1) {
					account.setId(UUID.randomUUID().toString());
				} else {
					account.setId(weiboList.get(0).getAccountId());
				}
				
				weibo.setAccountId(account.getId());
				account.loadWeibo(weiboList);
			}
			
			if (null == weibo.getAccountId() && null != account.getId()) {
				weibo.setAccountId(account.getId());
			}
			
			// 保存
			weiboDao.save(weibo);
			account.getWeiboMap().put(weiboId, weibo);
			
			setCurrentAccount(account);
			
			return new Result(getRequest().getContextPath() + "/execute.do?api=main", true);
		}
		
		return newErrorResult().addValue("desc", "LinkingWeiboNotFound");
	}
	
	@Api("homeTimeline")
	public Result homeTimeline() {
		Account account = getCurrentAccount();
		StringBuilder content = new StringBuilder();
		Collection<IWeibo> weiboList = account.getWeiboMap().values();
		if (null != weiboList) {
			for (IWeibo w : weiboList) {
				Response response = w.homeTimeline(new WebParam(getContext()));
				String bodyString = null;
				if (null != response) {
					bodyString = response.readBodyAsString();
				}
//				System.out.println("bodyString---" + bodyString);
				content.append("{\"weiboId\":\"").append(w.getWeiboId()).append("\",\"list\":")
				.append(bodyString).append(",\"weiboAccountName\":\"").append(w.getWeiboAccountName())
				.append("\"},");
			}
			if (content.length() > 0) {
				content.deleteCharAt(content.length()-1);
			}
		}
		content.insert(0, "[").append("]");
		return new Result("/WEB-INF/jsp/result.jsp").addValue("result", content.toString());
	}
	
	@Api("moreHomeTimeline")
	public Result moreHomeTimeline() {
		Account account = getCurrentAccount();
		StringBuilder content = new StringBuilder();
		Collection<IWeibo> weiboList = account.getWeiboMap().values();
		if (null != weiboList) {
			Context context = getContext();
			for (IWeibo w : weiboList) {
				Response response = w.homeTimeline(new MoreHomeTimelineParam(w, context));
				String bodyString = null;
				if (null != response) {
					bodyString = response.readBodyAsString();
				}
				//System.out.println(bodyString);
				content.append("{\"weiboId\":\"").append(w.getWeiboId()).append("\",\"list\":")
				.append(bodyString).append(",\"weiboAccountName\":\"").append(w.getWeiboAccountName())
				.append("\"},");
			}
			if (content.length() > 0) {
				content.deleteCharAt(content.length()-1);
			}
		}
		content.insert(0, "[").append("]");
		return new Result("/WEB-INF/jsp/result.jsp").addValue("result", content.toString());
	}
	
	@Api("statusesUpdate")
	public Result statusesUpdate() {
		Account account = getCurrentAccount();
//		StringBuilder content = new StringBuilder();
		Collection<IWeibo> weiboList = account.getWeiboMap().values();
		if (null != weiboList) {
			Response response = null;
			IParam iParam = new WebParam(getContext());
//			System.out.println("iParam:" + iParam.getParamValue("status"));
//			System.out.println("Content-Type:" + getRequest().getHeader("Content-Type"));
			for (IWeibo w : weiboList) {
				if (!w.isSynUpdate()) {
					continue;
				}
				response = null;
				try {
					response = w.statusesUpdate(iParam);
				} catch (Exception e) {
					if (LOG.isDebugEnabled()) {
						if (null != response) {
							String respContent = "";
							try {
								respContent = response.readBodyAsString();
							} catch (Exception e1) {
							}
							LOG.debug("method:statusesUpdate,respContent:" + respContent, e);
						} else {
							LOG.debug("method:statusesUpdate", e);
						}
					}
				}
			}
//			if (content.length() > 0) {
//				content.deleteCharAt(content.length()-1);
//			}
		}
//		content.insert(0, "[").append("]");
		return new Result("/WEB-INF/jsp/result.jsp").addValue("result", "{\"status\":\"true\",\"desc\":\"success\"}");
	}
	
	@Api("favoritesCreate")
	public Result favoritesCreate() {
		Account account = getCurrentAccount();
		if (account.getWeiboMap().size() > 0) {
			String weiboId = getRequestString("weiboId");
			String statusId = getRequestString("statusId");
			if (null != weiboId && null != statusId) {
				IWeibo weibo = account.getWeiboMap().get(weiboId);
				if (null != weibo) {
					weibo.favoritesCreate(new WebParam(getContext()));
					// TODO 判断是否失败
				}
			}
		}
		return new Result("/WEB-INF/jsp/result.jsp").addValue("result", "{\"status\":\"true\",\"desc\":\"success\"}");
	}
	
	@Api("statusesRetweet")
	public Result statusesRetweet() {
		Account account = getCurrentAccount();
		if (account.getWeiboMap().size() > 0) {
			String weiboId = getRequestString("weiboId");
			String statusId = getRequestString("statusId");
			if (null != weiboId && null != statusId) {
				IWeibo weibo = account.getWeiboMap().get(weiboId);
				if (null != weibo) {
					weibo.statusesRetweet(new WebParam(getContext()));
					// TODO 判断是否失败
				}
			}
		}
		return new Result("/WEB-INF/jsp/result.jsp").addValue("result", "{\"status\":\"true\",\"desc\":\"success\"}");
	}
	
	@Api("statusesReply")
	public Result statusesReply() {
		Account account = getCurrentAccount();
		if (account.getWeiboMap().size() > 0) {
			String weiboId = getRequestString("weiboId");
			String statusId = getRequestString("statusId");
			String status = getRequestString("status");
			if (null != weiboId && null != statusId && null != status) {
				IWeibo weibo = account.getWeiboMap().get(weiboId);
				if (null != weibo) {
					weibo.statusesReply(new WebParam(getContext()));
					// TODO 判断是否失败
				}
			}
		}
		return new Result("/WEB-INF/jsp/result.jsp").addValue("result", "{\"status\":\"true\",\"desc\":\"success\"}");
	}
	
	@Api("sinaStatusesCounts")
	public Result sinaStatusesCounts() {
		Account account = getCurrentAccount();
		StringBuilder content = new StringBuilder();
		Collection<IWeibo> weiboList = account.getWeiboMap().values();
		if (null != weiboList) {
			Context context = getContext();
			for (IWeibo w : weiboList) {
				if (null != context.getRequestString(w.getWeiboId()) && w instanceof WeiboSina) {
					WeiboSina sina = (WeiboSina) w;
					Response response = sina.statusesCounts(new StatusesCountParam(w, context));
					String bodyString = null;
					if (null != response) {
						bodyString = response.readBodyAsString();
					}
					content.append("{\"weiboId\":\"").append(w.getWeiboId()).append("\",\"list\":")
					.append(bodyString).append(",\"weiboAccountName\":\"").append(w.getWeiboAccountName())
					.append("\"},");
				}
			}
			if (content.length() > 0) {
				content.deleteCharAt(content.length()-1);
			}
		}
		content.insert(0, "[").append("]");
		return new Result("/WEB-INF/jsp/result.jsp").addValue("result", content.toString());
	}
	
	@Api("sohuStatusesCounts")
	public Result sohuStatusesCounts() {
		Account account = getCurrentAccount();
		StringBuilder content = new StringBuilder();
		Collection<IWeibo> weiboList = account.getWeiboMap().values();
		if (null != weiboList) {
			Context context = getContext();
			for (IWeibo w : weiboList) {
				if (null != context.getRequestString(w.getWeiboId()) && w instanceof WeiboSohu) {
					WeiboSohu sohu = (WeiboSohu) w;
					Response response = sohu.statusesCounts(new StatusesCountParam(w, context));
					String bodyString = null;
					if (null != response) {
						bodyString = response.readBodyAsString();
					}
					content.append("{\"weiboId\":\"").append(w.getWeiboId()).append("\",\"list\":")
					.append(bodyString).append(",\"weiboAccountName\":\"").append(w.getWeiboAccountName())
					.append("\"},");
				}
			}
			if (content.length() > 0) {
				content.deleteCharAt(content.length()-1);
			}
		}
		content.insert(0, "[").append("]");
		return new Result("/WEB-INF/jsp/result.jsp").addValue("result", content.toString());
	}
	
	@Api("statusesComments")
	public Result statusesComments() {
		Account account = getCurrentAccount();
		StringBuilder content = new StringBuilder();
		if (account.getWeiboMap().size() > 0) {
			String weiboId = getRequestString("weiboId");
			String statusId = getRequestString("statusId");
			if (null != weiboId && null != statusId) {
				IWeibo weibo = account.getWeiboMap().get(weiboId);
				if (null != weibo) {
					Response response = weibo.statusesComments(new WebParam(getContext()));
					String bodyString = null;
					if (null != response) {
						bodyString = response.readBodyAsString();
					}
					content.append("{\"weiboId\":\"").append(weibo.getWeiboId()).append("\",\"list\":")
					.append(bodyString).append(",\"weiboAccountName\":\"").append(weibo.getWeiboAccountName())
					.append("\"},");
				}
			}
		}
		if (content.length() > 0) {
			content.deleteCharAt(content.length()-1);
		}
		content.insert(0, "[").append("]");
		return new Result("/WEB-INF/jsp/result.jsp").addValue("result", content.toString());
	}
	
	@Api("statusesMentions")
	public Result statusesMentions() {
		Account account = getCurrentAccount();
		StringBuilder content = new StringBuilder();
		Collection<IWeibo> weiboList = account.getWeiboMap().values();
		if (null != weiboList) {
			for (IWeibo w : weiboList) {
				Response response = w.statusesMentions(new WebParam(getContext()));
				String bodyString = null;
				if (null != response) {
					bodyString = response.readBodyAsString();
				}
				content.append("{\"weiboId\":\"").append(w.getWeiboId()).append("\",\"list\":")
				.append(bodyString).append(",\"weiboAccountName\":\"").append(w.getWeiboAccountName())
				.append("\"},");
			}
			if (content.length() > 0) {
				content.deleteCharAt(content.length()-1);
			}
		}
		content.insert(0, "[").append("]");
		return new Result("/WEB-INF/jsp/result.jsp").addValue("result", content.toString());
	}
	
	@Api("moreStatusesMentions")
	public Result moreStatusesMentions() {
		Account account = getCurrentAccount();
		StringBuilder content = new StringBuilder();
		Collection<IWeibo> weiboList = account.getWeiboMap().values();
		if (null != weiboList) {
			Context context = getContext();
			for (IWeibo w : weiboList) {
				Response response = w.statusesMentions(new MoreHomeTimelineParam(w, context));
				String bodyString = null;
				if (null != response) {
					bodyString = response.readBodyAsString();
				}
				content.append("{\"weiboId\":\"").append(w.getWeiboId()).append("\",\"list\":")
				.append(bodyString).append(",\"weiboAccountName\":\"").append(w.getWeiboAccountName())
				.append("\"},");
			}
			if (content.length() > 0) {
				content.deleteCharAt(content.length()-1);
			}
		}
		content.insert(0, "[").append("]");
		return new Result("/WEB-INF/jsp/result.jsp").addValue("result", content.toString());
	}
	
	@Api("setting")
	public Result setting() {
		Account account = getCurrentAccount();
		Collection<IWeibo> weiboList = account.getWeiboMap().values();
		return new Result("/WEB-INF/jsp/setting.jsp").addValue("weiboList", weiboList);
	}
	
	@Api("unlink")
	public Result unlink() {
		String weiboId = getRequestString("weiboId");
		if (null != weiboId) {
			weiboDao.delete(weiboId);
			getCurrentAccount().getWeiboMap().remove(weiboId);
		}
		return setting();
	}
	
	@Api("logout")
	public Result logout() {
		getSession().removeAttribute(Account.ACCOUNT_SESSION_KEY);
		return new Result(getRequest().getContextPath() + "/index.jsp", true);
	}
	
	@Api("synUpdate")
	public Result synUpdate() {
		Account account = getCurrentAccount();
		if (account.getWeiboMap().size() > 0) {
			String weiboId = getRequestString("weiboId");
			if (null != weiboId) {
				boolean synUpdate = getRequestBoolean("syn");
				account.getWeiboMap().get(weiboId).setSynUpdate(synUpdate);
				
				// TODO 目前是即时更新db，或者改到队列什么的去做？
				weiboDao.updateSyn(weiboId, synUpdate);
			}
		}
		return new Result("/WEB-INF/jsp/result.jsp").addValue("result", "{\"status\":\"true\",\"desc\":\"success\"}");
	}

}
 