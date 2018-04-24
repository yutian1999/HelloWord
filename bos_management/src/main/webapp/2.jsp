<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
		<script type="text/javascript" src="/js/jquery-1.8.3.js"></script>
		<script type="text/javascript" src="/js/easyui/jquery.easyui.min.js"></script>
		<link rel="stylesheet" href="/js/easyui/themes/default/easyui.css" />
		<link rel="stylesheet" href="/js/easyui/themes/icon.css" />
	</head>

	<body>
		<shiro:hasPermission name="courier:add">
			<a href="#" class="easyui-linkbutton">增加</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="courier:edit">
			<a href="#" class="easyui-linkbutton">修改</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="courier:delete">
			<a href="#" class="easyui-linkbutton">删除</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="courier:list">
			<a href="#" class="easyui-linkbutton">查询</a>
		</shiro:hasPermission>
		
	</body>
</html>