<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>2048小游戏登录</title>
<meta name="description" content="登录界面">
<meta name="keywords" content="登录界面">
<link href="" rel="stylesheet">
<style>
	body,p,div,ul,li,h1,h2,h3,h4,h5,h6{
		margin:0;
		padding: 0;
	}
	body{
		background: #5F9EA0; 
	}
	#login{
		width: 400px;
		height: 250px;
		background: #FFF;
		margin:200px auto;
		position: relative;
	}
	#login h1{
		text-align:center;
		position:absolute;
		left:120px;
		top:-40px;
		font-size:21px;
	}
	#login form p{
		text-align: center;
	}
	#user{
		background:url(images/user.png) rgba(0,0,0,.1) no-repeat;
		width: 200px;
		height: 30px;
		border:solid #ccc 1px;
		border-radius: 3px;
		padding-left: 32px;
		margin-top: 50px;
		margin-bottom: 30px;
	}
	#password{
		background: url(images/pwd.png) rgba(0,0,0,.1) no-repeat;
		width: 200px;
		height: 30px;
		border:solid #ccc 1px;
		border-radius: 3px;
		padding-left: 32px;
		margin-bottom: 30px;
	}
	#submit{
		width: 232px;
		height: 30px;
		background: rgba(0,0,0,.1);
		border:solid #ccc 1px;
		border-radius: 3px;
	}
	#submit:hover{
		cursor: pointer;
		background:#D8D8D8;
	}
	.show{
		color: red;
		padding-left: 300px;
	}
</style>
</head>
<body>
<div id="login">
<h1 >2048小游戏登录</h1>	
	<form action="Check" method="post">
		<p><input type="text" name="uname" id="user" placeholder="用户名"></p>
		<p><input type="password" name="upwd" id="password" placeholder="密码"></p>
		<p><input type="submit" id="submit" value="登录"></p>
		<a class = "show">${message}</a>
		<p><a href="register.jsp">没有账号？立即注册</a></p>
	</form>
</div>
<div style="text-align:center;">
</div>
</body>
</html>