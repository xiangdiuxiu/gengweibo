<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<a id='navMain' class='f-left headerA headerAOn' href='javascript:void(0);' style='margin-left: 5px;'>我的主页</a>
<a id='navAtMe' class='f-left headerA' href='<%=request.getContextPath()%>/execute.do?api=atMe'>@我的</a>
<a id='navSetting' class='f-left headerA' href='<%=request.getContextPath()%>/execute.do?api=setting'>我的帐户</a>
<a class='f-right headerA' href='<%=request.getContextPath()%>/execute.do?api=logout' style='margin-right: 0px;'>退出</a>
