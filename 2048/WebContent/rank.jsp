<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.user.dao.UserDao"%>
<%@ page import="com.user.dao.Info" import="java.util.*"%>
<%
	List<Info> Info = UserDao.getSelect();
%>
<!DOCTYPE html PUBLIC 	 "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>排行榜</title>
	<style>
	body{
		background: #5F9EA0; 
	}
	.re{
		position:absolute;
		color: red;
		font-size: 20px;
		position: absolute;
		left: 60%;
		top:145px;
	}
	.sre{
	position:absolute;
	color: red;
	font-size: 20px;
	position: absolute;
	left: 60%;
	top:100px;
	}
	
</style>
</head>
<body>
	<form action="javascript:history.back();">
		<input class = "re" type="submit" value="返回游戏"></input>
	</form>
	<form action="login.jsp">
		<input class = "sre" type="submit" value="返回登录界面	"></input>
	</form>
	<table border="20" bgcolor="#0099FF" align="center">
		<tr>
			<td>用户名</td>
			<td>成绩</td>
			<td>排名</td>
		</tr>
		<%
			for (int i = 0; i < Info.size(); i++) {
		%>
		<tr>
			<td><%=Info.get(i).getName()%></td>
			<td><%=Info.get(i).getScore()%></td>
			<td><%=(i + 1)%></td>
		</tr>
		<%
			}
		%>
	</table>
</body>
</html>