<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>@我的</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css<jsp:include page="css_version.jsp" />" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/jquery-impromptu.3.1.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/block.css" type="text/css" />
</head>
<body onload="initStatusesMentionsPage();">

<div class="page">
<div class='header'>
	<span class="f-left">
		<a class='f-left headerA' style='margin-left: 5px;' href='javascript:void(0);' onclick="newStatuses();" title="发布新微博">发布</a>
		<a class='f-left headerA' href='javascript:void(0);' onclick="mentionsFlush();" title='刷新@我的'>刷新</a>
	</span>
	<span class="f-right">
		<a class='f-left headerA' href='<%=request.getContextPath()%>/execute.do?api=main' title="我的主页">主页</a>
		<a class='f-left headerA headerAOn' title="@我的">@我的</a>
		<a class='f-left headerA' href='<%=request.getContextPath()%>/execute.do?api=comments2Me' title="我的评论">评论</a>
		<a class='f-left headerA' href='<%=request.getContextPath()%>/execute.do?api=setting' title="帐户设置">设置</a>
		<a class='f-left headerA' href='<%=request.getContextPath()%>/execute.do?api=logout' style='margin-right: 0px;' title="退出本系统">退出</a>
	</span>
</div> 

<%--
<div style='overflow: hidden;padding-left: 5px;'>
	<div class='f-right'>
		<input class='mBtn' type="button" value="刷新" onclick='mentionsFlush();' />
	</div>
</div>
 --%>

<div id='statusesMentions' class='itemWrapper'>
	<div id='items'></div>
	
	<div id='moreItem' >
		<input id='moreBtn' type='button' value='查看更多' onclick='moreStatusesMentions();' />
	</div>
</div>

<div class='gFooter'>
	&copy;&nbsp;
	更简约的微博&nbsp;
	开源项目&nbsp;
	<a href='https://github.com/auzll/gengweibo' target="_blank">源码</a>&nbsp;
	<a href="http://weibo.com/zhengll" target="_blank">联系</a>
</div> 

</div>

</body>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-impromptu.3.1.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.blockUI.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/gwb.js<jsp:include page="js_version.jsp" />"></script> 
</html>