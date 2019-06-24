<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>张喜---2048</title>
<style>
* {
	padding: 0px;
	margin: 0px;
}

body {
	font-size: 30px;
	background-color: rgb(23, 44, 60);
	overflow: hidden;
}

.readme {
	position: absolute;
	left: 10%;
	top: 100px;
	width: 250px;
}

.readme span {
	display: block;
	width: 100%;
	text-align: center;
	font-size: 25px;
	color: red;
}

.readme p {
	display: block;
	font-size: 25px;
	text-indent: 20px;
	color: #0ff1cc;
}

.tab {
	margin: 100px auto 0;
	border-radius: 15px;
	text-align: center;
	font-weight: bold;
	background-color: #999;
}

td {
	width: 100px;
	height: 100px;
	border-radius: 15px;
}

.text {
	position: absolute;
	right: 15%;
	right: 15%;
	top: 100px;
	color: red;
}

.restart {
	font-size: 22px;
	margin: 25px;
	color: red;
	text-align: center;
	cursor: pointer;
}

.score {
	color: #75ffe7;
	font-size: 36px;
}
.submit{
	position:absolute;
	color: red;
	font-size: 20px;
	position: absolute;
	left: 82%;
	top:600px;
}
.show{
	color: red;
	font-size: 15px;
	
}
.bu{
	position:absolute;
	color: red;
	font-size: 20px;
	position: absolute;
	left: 82%;
	top:550px;
}
</style>
</head>

<body>
<form action = "rank.jsp" method = "post">
 <input type = "submit" class= "bu"  value = "排行榜"></input>
</form>
	<form action="Chengji" method="post">
	 <%
    	String name =(String)request.getAttribute("name");
	 	String score= (String)request.getAttribute("score");
	 	String rank = (String)request.getAttribute("rank");
    %>
   	<input type="hidden" name = "name" id = "nowname" value="<%= name%>" />
	<input type="hidden" name = "nowscore" id = "nowscore"value="<%= score%>" />
		<input type="hidden" name = "nowrank" id = "nowrank"value="<%= rank%>" />
	<div  id = "sname" class = "show"></div> 
	<div  id = "sscore" class = "show"></div>
	<div  id = "srank" class = "show"></div>
		<input type="hidden"  name = "score" id = "ss" />
		<div class="readme">
			<span>2048玩法说明</span>
			<p>键盘上下左右控制移动，相同的格子会累加起来重叠为新的 格子，当所有格子均占有并不能移动时游戏结束。
				回车键（Enter）或点击重新开始重新开始游戏。加油吧，看谁能玩到2048.点击退出游戏提交数据哦！</p>
		</div>
		<table class="tab" cellspacing="6"></table>
		<div class="restart" id="re">重新开始</div>
		<div class="text" style="font-size: 16px">
			分数: <span class="score" id="ss"> </span> 分
		</div>
		<script type="text/javascript">
		document.getElementById("sname").innerHTML = "用户名："+document.getElementById("nowname").value ;
		document.getElementById("sscore").innerHTML = "我的最高分:"+document.getElementById("nowscore").value ;
		document.getElementById("srank").innerHTML = "我的名次:"+document.getElementById("nowrank").value ;
			var top_flag = true;
			var right_flag = true;
			var buttom_flag = true;
			var left_flag = true;
			var json = {
				//初始化函数
				init : function() {
					top_flag = true;
					right_flag = true;
					buttom_flag = true;
					left_flag = true;
					var flag = false;
					var oTab = document.querySelector('.tab'), str = '', id = 1;
					for (var i = 0; i < 4; i++) {
						str += '<tr>';
						for (var j = 0; j < 4; j++) {
							str += '<td id = "' + id++ + '"></td>';
						}
						str += '</tr>';
					}
					oTab.innerHTML = str;
					this.randomNum();
					this.randomNum();
					this.result();
				},
				//随机函数
				myRandom : function(max, min) {
					return Math.round(Math.random() * (max - min) + min);
				},
				//随机在格子上生成一个数字
				randomNum : function() {
					var num = this.myRandom(1, 16), oGrid = document
							.getElementById(num);
					console.log(oGrid)
					if (oGrid.innerHTML == '') {
						oGrid.innerHTML = this.myRandom(1, 2) * 2;
					} else {
						this.randomNum();
					}

				},
				//上键
				top : function() {
					for (var i = 1; i <= 4; i++) {
						for (var j = i; j <= i + 12; j += 4) {
							for (var k = j; k > 4; k -= 4) {
								this.change(document.getElementById(k - 4),
										document.getElementById(k));
								if (flag == false) {
									top_flag = false;
								} else
									top_flag = true;
							}
						}
					}

				},
				//下键
				buttom : function() {
					for (var i = 1; i <= 4; i++) {
						for (var j = i + 12; j >= i; j -= 4) {
							for (var k = j; k <= 12; k += 4) {
								this.change(document.getElementById(k + 4),
										document.getElementById(k));
								if (flag == false) {
									buttom_flag = false;
								} else
									buttom_flag = true;
							}
						}
					}

				},
				//左键
				left : function() {
					for (var i = 1; i <= 13; i += 4) {
						for (var j = i; j <= i + 3; j++) {
							for (var k = j; k > i; k--) {
								this.change(document.getElementById(k - 1),
										document.getElementById(k));
								if (flag == false) {
									left_flag = false;
								} else
									left_flag = true;
							}
						}
					}

				},
				//右键
				right : function() {
					for (var i = 1; i <= 13; i += 4) {
						for (var j = i + 4; j >= i; j--) {
							for (var k = j; k < i + 3; k++) {
								this.change(document.getElementById(k + 1),
										document.getElementById(k));
								if (flag == false) {
									right_flag = false;
								} else
									right_flag = true;
							}
						}
					}
				},
				//移动和合并检测函数
				change : function(before, after) {
					//移动
					if (before.innerHTML == '' && after.innerHTML != '') {
						before.innerHTML = after.innerHTML;
						after.innerHTML = '';
						flag = true;
					}
					//合并
					if (before.innerHTML != ''
							&& after.innerHTML == before.innerHTML) {
						before.innerHTML *= 2;
						after.innerHTML = '';
						flag = true;
					}
				},
				//改变颜色计算结果函数
				result : function() {
					flag = false;
					var color = {
						'' : '#fff',
						'2' : '#0f0',
						'4' : '#00ccff',
						'8' : '#ff9900',
						'16' : '#00cc66',
						'32' : '#ffcccc',
						'64' : '##ff33ff',
						'128' : '#0066cc',
						'256' : '#6633cc',
						'512' : '#ff0099',
						'1024' : '#990033',
						'2048' : '#6600ff',
						'4096' : '#cc0066'
					};
					score = 0;
					for (var i = 1; i <= 16; i++) {
						var oGrid = document.getElementById(i);
						oGrid.style.backgroundColor = color[oGrid.innerHTML];
						score += oGrid.innerHTML * 10;
						document.querySelector('.score').innerHTML = score;
						document.getElementById('ss').value = score;
					}
					if (top_flag == false && buttom_flag == false
							&& left_flag == false && right_flag == false) {
						document.getElementById('ss').value = score;
						alert("游戏结束！！！！！你的最后得分  "
								+ score
								+ "\n"
								+ "                                                                               by张喜");
					}

				}
			};
			window.onload = function() {
				json.init();
				var div = document.getElementById('re');
				div.onclick = function() {
					json.init();
				}
			}
			document.onkeydown = function(e) {
				if (e.keyCode == '38')
					json.top();
				if (e.keyCode == '40')
					json.buttom();
				if (e.keyCode == '37')
					json.left();
				if (e.keyCode == '39')
					json.right();
				if (e.keyCode == '13') {
					json.init();
					flag = false;
				}
				if (flag == true) {
					json.randomNum();
				}
				json.result();SS

			}
		</script>
		<input class="submit" type="submit" name="over" value="结束游戏" /><br />
		<br/>
	</form>
</body>

</html>