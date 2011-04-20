<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我的评论</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css<jsp:include page="css_version.jsp" />" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/jquery-impromptu.3.1.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/block.css" type="text/css" />
</head>
<body onload="initCommentsPage();">

<div class="page">
<div class='header'>
	<span class="f-left">
		<a class='f-left headerA' style='margin-left: 5px;' href='javascript:void(0);' onclick="newStatuses();" title="发布新微博">发微博</a>
		<a class='f-left headerA' href='javascript:void(0);' onclick="commentsFlush();" title='刷新我的评论'>刷新</a>
	</span>
	<span class="f-right">
		<a class='f-left headerA' href='<%=request.getContextPath()%>/execute.do?api=main' title="我的主页">主页</a>
		<a class='f-left headerA' href='<%=request.getContextPath()%>/execute.do?api=atMe' title="@我的">@我的</a>
		<a class='f-left headerA headerAOn' title="我的评论">评论</a>
		<a class='f-left headerA' href='<%=request.getContextPath()%>/execute.do?api=setting' title="帐户设置">设置</a>
		<a class='f-left headerA' href='<%=request.getContextPath()%>/execute.do?api=logout' style='margin-right: 0px;' title="退出本系统">退出</a>
	</span>
</div> 

<%--
<div style='overflow: hidden;padding-left: 5px;'>
	<div class='f-right'>
		<input class='mBtn' type="button" value="刷新" onclick='commentsFlush();' />
	</div>
</div>
--%>

<div id='statuseComments' class='itemWrapper'>
	<div id='items'></div>
	
	<div id='moreItem' >
		<input id='moreBtn' type='button' value='查看更多' onclick='moreStatuseComments();' />
	</div>
</div>

</div>

</body>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-impromptu.3.1.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.blockUI.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/gwb.js<jsp:include page="js_version.jsp" />"></script> 
</html>