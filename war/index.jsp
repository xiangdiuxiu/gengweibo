<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.gengweibo.weibo.WeiboType,com.gengweibo.entity.Account" %>
<%
	Account account = (Account) session.getAttribute(Account.ACCOUNT_SESSION_KEY);
	if (null != account) {
		response.sendRedirect(request.getContextPath() + "/execute.do?api=main");
		return;
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="keywords" content="微博,更简约的微博" /> 
<meta name="description" content="来试试更简约的微博，无需注册，连接即可使用！" /> 
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script> 
<style>
<!--
.about li {
list-style: disc;
}
.about {
padding-left: 15px;
margin-bottom: 30px;
}
-->
</style>
<title>更微博 - 更简约的微博查看工具</title> 
</head>
<body><div class='page' style="width: 790px;">

<div style="margin-top: 150px;font-size: 14px;">

<div style="text-indent: 2em;font-weight: bold;margin-bottom: 15px;font-size: 16px;">
关于这个“更微博”小程序的一些说明，之前想要一款比较简便查看微博的工具(类似RSS Reader那么简洁方便)，但是市面上没有，于是就想自己做一个玩玩了。以下是它的一些特点和缺点：
</div>
<ul class='about'>
<li>微博视图更简约，尝试模仿手机客户端的界面。</li>
<li>无须注册，直接连接即可使用(oauth方式)，目前支持网易、腾讯、新浪和搜狐微博。连接进入系统后，还可以在“我的账户”里面设置连接更多的账户哦。</li>
<li>混合显示，同时把各微博厂商的微博混合在同一个页面按时间降序来显示，区别于其他多微博查看工具的tab切换方式。</li>
<li>功能较少，只方便查看“我”或“我所关注的人”的微博，不利于网友互动。</li>
</ul>

<div style="text-align: right;">
<form action="execute.do?api=link" method="post">
	<select name="type">
		<%
			StringBuilder content = new StringBuilder();
			WeiboType[] types = WeiboType.getPublicTypes();
			for (WeiboType w : types) {
				content.append("<option value='").append(w.getEnName()).append("'>")
				.append(w.getCnName()).append("</option>");
			}
			out.print(content.toString());
		%>
	</select>
	<input type="submit" value="连接" style="width: 80px;height: 25px;" />
</form>
</div>

</div>
	
</div></body>
</html>