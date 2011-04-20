<%@page import="com.gengweibo.entity.Account"%>
<%@page import="java.util.Collection"%>
<%@page import="com.gengweibo.weibo.IWeibo,com.gengweibo.util.Utils"%>
<%@page import="java.util.Collection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.gengweibo.weibo.WeiboType" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css<jsp:include page="css_version.jsp" />" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/jquery-impromptu.3.1.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/block.css" type="text/css" />
<title>设置</title>
</head>
<body>

<div class="page">
<div class='header'>
	<span class="f-left">
		<a class='f-left headerA' style='margin-left: 5px;' href='javascript:void(0);' onclick="newStatuses();" title="发布新微博">发微博</a>
	</span>
	<span class="f-right">
		<a class='f-left headerA' href='<%=request.getContextPath()%>/execute.do?api=main' title="我的主页">主页</a>
		<a class='f-left headerA' href='<%=request.getContextPath()%>/execute.do?api=atMe' title="@我的">@我的</a>
		<a class='f-left headerA' href='<%=request.getContextPath()%>/execute.do?api=comments2Me' title="我的评论">评论</a>
		<a class='f-left headerA headerAOn' title="帐户设置">设置</a>
		<a class='f-left headerA' href='<%=request.getContextPath()%>/execute.do?api=logout' style='margin-right: 0px;' title="退出本系统">退出</a>
	</span>
</div>

<div class='itemWrapper' style="padding-left: 10px;padding-bottom: 10px;">
	<div style="font-size:20px;margin-bottom: 5px;font-weight: bold;">
	一、可在此连接更多的微博帐户(最多<%=Utils.getMaxLinkingNum()%>个)：
	</div>
	<%
		Account currentAcc = (Account)session.getAttribute(Account.ACCOUNT_SESSION_KEY);
		if (null != currentAcc && currentAcc.getWeiboMap().size() < Utils.getMaxLinkingNum()) {
			StringBuilder content = new StringBuilder("<form action='execute.do?api=link' method='post'><select name='type'>");
			WeiboType[] types = WeiboType.getPublicTypes();
			for (WeiboType w : types) {
				content.append("<option value='").append(w.getEnName()).append("'>")
				.append(w.getCnName()).append("</option>");
			}
			content.append("</select><input type='submit' value='连接' style='width: 80px;' />&nbsp;<span class='gray'>同一个厂商的微博都可以连接多个哦，例如多个网易微博...</span></form>");
			out.print(content.toString());
		}
	%>
	
	<div style="height:20px;"></div>
	
	<div style="font-size:20px;margin-bottom: 5px;font-weight: bold;">
	二、以下是已连接的微博帐户列表：
	</div>
	<%
		Collection<IWeibo> weiboList = (Collection<IWeibo>)request.getAttribute("weiboList");
		if (null != weiboList) {
			StringBuilder content = new StringBuilder();
			for (IWeibo weibo : weiboList) {
				content.append("<div style='font-size:14px;font-weight:bold;'>").append(weibo.getType().getCnName()).append("&nbsp;")
				.append("@" + weibo.getWeiboAccountName()).append("&nbsp;")
				.append("<label><input").append(weibo.isSynUpdate() ? " checked='checked' " : "").append(" weiboId='").append(weibo.getWeiboId()).append("' type='checkbox' onclick='changeSyn(this);'/>同步发微博</label>&nbsp;")
				.append("<a href='" + request.getContextPath() + "/execute.do?api=unlink&weiboId=").append(weibo.getWeiboId()).append("'>").append("解除连接").append("</a>")
				.append("</div>");
			}
			out.print(content.toString());
		}
	%>
</div>



</div>
</body>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-impromptu.3.1.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.blockUI.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/gwb.js<jsp:include page="js_version.jsp" />"></script> 
</html>