<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Date"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>主页</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css<jsp:include page="css_version.jsp" />" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/jquery-impromptu.3.1.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/block.css" type="text/css" />
</head>
<body onload="initMainPage();">

<div class="page">
<div class='header'>
	<span class="f-left">
		<a class='f-left headerA' style='margin-left: 5px;' href='javascript:void(0);' onclick="newStatuses();" title="发布新微博">发微博</a>
		<a class='f-left headerA' href='javascript:void(0);' onclick="statusesFlush();" title='刷新我的主页'>刷新</a>
	</span>
	<span class="f-right">
		<a class='f-left headerA headerAOn' title="我的主页">主页</a>
		<a class='f-left headerA' href='<%=request.getContextPath()%>/execute.do?api=atMe' title="@我的">@我的</a>
		<a class='f-left headerA' href='<%=request.getContextPath()%>/execute.do?api=comments2Me' title="我的评论">评论</a>
		<a class='f-left headerA' href='<%=request.getContextPath()%>/execute.do?api=setting' title="帐户设置">设置</a>
		<a class='f-left headerA' href='<%=request.getContextPath()%>/execute.do?api=logout' style='margin-right: 0px;' title="退出本系统">退出</a>
	</span>
</div> 

<%--
<div>
	<textarea id='statusTextarea' onkeyup='statusTextareaChange();'></textarea>
</div>
<div style='overflow: hidden;padding-left: 5px;'>
	<div class='f-left' style="margin-top: 3px;"><span id='remainWordCount'>还可以输入140个字</span></div>
	<div class='f-right'>
		<input class='mBtn' type="button" value="发布" onclick='statusesUpdate();' />
	</div>
	<div class='f-right' style="margin-right: 10px;">
		<input class='mBtn' type="button" value="刷新" onclick='statusesFlush();' />
	</div>
</div>
 --%>

<div id='homeTimeline' class='itemWrapper'>
	<div id='newItem' style="display: none;">
		<input type="button" value="查看新微博" />
	</div>

	<div id='items'></div>
	
	<div id='moreItem' >
		<input id='moreBtn' type='button' value='查看更多' onclick='moreHomeTimeline();' />
	</div>
</div>

</div>

</body>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-impromptu.3.1.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.blockUI.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/gwb.js<jsp:include page="js_version.jsp" />"></script> 
</html>