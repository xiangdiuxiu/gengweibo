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
	<a id='navMain' class='f-left headerA' href='<%=request.getContextPath()%>/execute.do?api=main' style='margin-left: 5px;'>我的主页</a>
	<a id='navAtMe' class='f-left headerA headerAOn' >@我的</a>
	<a id='navComments2Me' class='f-left headerA' href='<%=request.getContextPath()%>/execute.do?api=comments2Me'>我的评论</a>
	
	<a class='f-right headerA' href='<%=request.getContextPath()%>/execute.do?api=logout' style='margin-right: 0px;'>退出</a>
	<a id='navSetting' class='f-right headerA' href='<%=request.getContextPath()%>/execute.do?api=setting'>设置</a>
</div> 

<div style='overflow: hidden;padding-left: 5px;'>
	<div class='f-right'>
		<input class='mBtn' type="button" value="刷新" onclick='mentionsFlush();' />
	</div>
</div>

<div id='statusesMentions'>
	<div id='items'></div>
	
	<div id='moreItem' >
		<input id='moreBtn' type='button' value='查看更多' onclick='moreStatusesMentions();' />
	</div>
</div>

</div>

</body>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-impromptu.3.1.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.blockUI.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/gwb.js<jsp:include page="js_version.jsp" />"></script> 
</html>