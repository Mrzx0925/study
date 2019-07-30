//查询所有的信息
function queryall() {
	$.post(
	// 服务器地址
	"zx/resteasy/queryall",
	// "http://localhost:8080/RestOne/rest/message/zxzx",
	// 数据类型
	function(data) {
		$("#tb  tr:not(:first)").html("");
		var s = '';
		for (var i = 0; i < data.length; i++) {
			s += "<tr><td>" + data[i].id + '</td><td>' + data[i].name
					+ '</td><td>' + data[i].age + "</td></tr>";
		}
		$('#tb').append(s);
	},
	// 预期返回值类型 xml json text
	"json");
	$("#id").val("");
	$("#name").val("");
	$("#age").val("");
	$("#queryid").val("");
}
// 对应点击图按钮
$(function() {
	var method = "";// 到底点击了谁，执行谁的局部刷新

	$('#add').click(
			function() {
				var name = form.name.value.trim();
				var age = form.age.value.trim();
				var url = encodeURI("zx/resteasy/add?name=" + name);
				// var url = "zx/resteasy/add?name="+name;
				// alert(url);
				if (name == "" || age == "") {
					layer.msg('姓名或者年龄应不为空', {
						icon : 5
					});
				} else {
					if (isNaN(age) == true || age < 0 || age > 100) {
						layer.msg('年龄应该为数字,且大于0小于100', {
							icon : 5
						});
					} else if (!judgename(name)) {
						layer.msg('姓名不要含有特殊字符，且姓名的长度小于8', {
							icon : 5
						});
					} else {

						// form.action = url;
						// form.submit();
						$('#form').ajaxSubmit(
								{
									type : 'post',
									url : url,
									success : function(data) {

										setTimeout("queryall();", 400);
										setTimeout(getmethod(), 400);
										var msg = data.split(":");
										if (msg[0] == "true") {
											var title = "增加成功，ID为：" + msg[1];
											layer.alert(title, {
												icon : 1
											});
										} else {
											var title = "增加成功，ID为：" + msg[0]
													+ "<br/>注意：此条信息已经有 "
													+ msg[1] + "条重复";
											layer.alert(title, {
												icon : 3
											});
										}
									}
								});
						// setTimeout("queryall();", 400);
						// setTimeout(getmethod(), 400);

					}
				}
			});
	$('#delete').click(function() {
		var id = form.id.value.trim();
		if (id == "") {
			layer.msg('未选择数据', {
				icon : 5
			});
		} else {
			form.action = "zx/resteasy/delete";
			form.submit();
			// location.reload(true);
			setTimeout("queryall();", 400);
			setTimeout(getmethod(), 400);

			setTimeout("layer.msg('删除成功', {icon: 1});", 600);

		}
	});
	$('#update').click(function() {
		var age = form.age.value.trim();
		var id = form.id.value.trim();
		var name = form.name.value.trim();
		var url = encodeURI("zx/resteasy/update?name=" + name);
		if (id == "") {
			layer.msg('未选择数据', {
				icon : 5
			});
		} else if (name == "" || age == "") {
			layer.msg('姓名或者年龄应不为空', {
				icon : 5
			});
		} else if (isNaN(age) == true || age < 0 || age > 100) {
			layer.msg('年龄应该为数字,且大于0小于100', {
				icon : 5
			});
		} else if (!judgename(name)) {
			layer.msg('姓名不要含有特殊字符，且姓名的长度小于8', {
				icon : 5
			});
		} else {

			form.action = url;
			form.submit();
			// alert("zx");
			// location.reload(true);
			setTimeout("queryall();", 400);
			setTimeout(getmethod(), 400);
			setTimeout("layer.msg('修改成功', {icon: 1});", 600);
		}
	});
	$('#query').click(function() {
		var id = form.queryid.value.trim();

		if (id == "" || isNaN(id) == true) {
			layer.msg('未添加查询数据或者输入查询的ID不为数字', {
				icon : 5
			});
		} else {
			$.ajax({
				type : "post", // 数据提交方式（post/get）
				url : "zx/resteasy/querysingle", // 提交到的url
				data : id, // 提交的数据
				dataType : "json", // 返回的数据类型格式
				success : function(msg) {
					if (msg == null) {
						layer.msg('无此数据', {
							icon : 5
						});
					} else {
						setTimeout("layer.msg('查询成功', {icon: 1});", 600);
						eachtr(id);
						$("#id").val(msg.id);
						$("#name").val(msg.name);
						$("#age").val(msg.age);
						$("#queryid").val("");
					}
				}
			});
		}
	});
	//修改密码
	$('#password').click(function() {
		layer.prompt({
			title : '输入新密码',
			formType : 1
		}, function(pass, index) {
			layer.close(index);
			layer.prompt({
				title : '确认密码',
				formType : 1
			}, function(text, index) {
				layer.close(index);
				if (text == pass) {
					$.ajax({
						url : 'zx/resteasy/updatepass/'+text,
						type : 'post',
						success : function(data) {
							if (data == 1) {
								layer.msg('修改成功', {
									icon : 1
								});
							}
						},
					});
				} else {
					layer.msg('密码不一致', {
						icon : 5
					});
				}
			});
		});
	});
	$('#admin').click(function() {
		window.location = "permissions.html";
	});
	$('#loginout').click(function() {	
		window.location = "zx/resteasy/loginout";
		setTimeout("window.location = 'login.html'", 1000);
	
	});
	$(document).ready(function() {
		$.ajax({
			url : 'zx/resteasy/getaccount',
			type : 'post',
			success : function(data) {
				if (data == 0) {
					layer.msg('请先登录', {
						time:500,
						icon : 3
					});
					setTimeout("window.location = 'login.html'", 1000);
				} else {
					dogetinfo(data);

					$('#columns').show();
					$('#line').show();
					$('#radar').show();
					$('#pie').show();
					// alert(data);
					// $("#welcome").text("欢迎你,"+data);
					queryall();
					columns(); // 默认加载柱状图
				}
			},
		});

		// broke();
		// pie();
	});
});

// 获得点击事件
$(function() {
	$("#tb").on("click", "tr", function(event) {
		// $(this).closest("tr").find("td").eq(0).text()
		// 获取点击的id
		if ($(this).attr("id") == "head") {
			// $(this).addClass("color").siblings();
			$("tr").removeClass("color");
		} else {
			$(this).addClass("color").siblings().removeClass("color");
		}
		$("#id").val($(this).closest("tr").find("td").eq(0).text());
		$("#name").val($(this).closest("tr").find("td").eq(1).text());
		$("#age").val($(this).closest("tr").find("td").eq(2).text());
	});
	$("#pie").click(function() {
		pie();
		$("#flag").val("pie");
	});
	$("#columns").click(function() {
		columns();
		$("#flag").val("columns");
	});
	$("#line").click(function() {
		line();
		$("#flag").val("line");
	});
	$("#radar").click(function() {
		radar();
		$("#flag").val("radar");
	});
});
// 得到点击了哪个按钮，返回对应方法
function getmethod() {
	var method = "";
	// alert($("#flag").val());
	var flag = $("#flag").val();
	// alert($("#flag").val() == "columns");
	if (flag == "columns") {
		method = "columns();";
		// alert(method);
	} else if (flag == "pie") {
		method = "pie();";
	} else if (flag == "line") {
		method = "line();";
	} else if (flag == "radar") {
		method = "radar();";
	}
	return method;
}
// 得到作图数据
function getdata() {
	var numo = []; // 数量数组
	$.ajax({
		type : 'post',
		url : 'zx/resteasy/get',// 请求数据的地址
		async : false,// 设置为同步操作就可以给全局变量赋值成功
		dataType : "json", // 返回数据形式为json
		success : function(result) {
			// 请求成功时执行该函数内容，result即为服务器返回的json对象
			$.each(result, function(index, item) {
				// namey.push(item.agecontent); // 挨个取出类别并 填入类别数组
				numo.push(item.num); // 挨个取出数量并填入数量数组
			})

		},
	});
	return numo;
}
// 得到数据中最大值给雷达图用
function getdatamax() {
	var numo = getdata();
	var max = 0;
	for (var i = 0; i < numo.length; i++) {
		if (max < numo[i]) {
			max = numo[i];
		}
	}
	return max;
}

// 点击导入导出按钮
$(function() {
	$('#in').click(function() {
		display();
		// alert("导入");
		// $("#file").trigger("click");
		// var filename = $("#file").val();
		// alert(filename);

	});
	$('#out').click(function() {
		// alert("导出");
		window.location.href = "zx/download/test";
	});
});
// 展示上传文件确认
function display() {
	var title = "上传文件你要知道几点<br/>①：文件类型为excel类文件即xls，xlsx<br/>"
			+ "②：文件上传内容为姓名为和年龄（name，age）<br/>"
			+ "③：如果你第一行无相关对应标题我们会默认为第一行为姓名，第二行为年龄<br/>"
			+ "④：如果你第一行有多个相关标题我们会选择最前面的两列，注意我们不读取超过100列的Excel文件<br/>"
			+ "⑤：我们会对上传文件的内容进行判断，姓名不含有特殊字符和数字最大长度为8，年龄为数字，限制范围为0-100，同时我们不读取空行，内容不符合要求我们会在导入中取出，请注意，当连续五行出错的时候我们就会终止导入，谢谢<br/>"
			+ "⑥：我们会返回相关错误信息，不包括空行"
	layer.confirm(title, {
		btn : [ '选择文件', '再去看看文件' ]
	// 按钮
	}, function() {
		// alert($("#file").val());
		// confirmfile();
		$("#file").trigger("click");
		layer.closeAll();
	}, function() {
		layer.msg('好的', {
			icon : 1
		});
	});
}
function confirmfile() {
	var filename = $("#file").val();
	alert(filename);
	var filetype = filename.substring(filename.lastIndexOf(".") + 1);
	if (filetype == "") {
	} else if (filetype != "xls" && filetype != "xlsx") {
		layer.msg('请上传xls或者xlsx的Excel文件', {
			icon : 5
		});
	} else {
		// alert(upfile.file.value);
		// upfile.action = "zx/upload";
		// upfile.submit();
		$('#upfile').ajaxSubmit({
			type : 'post',
			url : "zx/upload",
			success : function(data) {
				// layer.msg(data, {icon: 1});
				setTimeout("queryall();", 400);
				setTimeout(getmethod(), 400);
				var msg = getmsg(data);
				layer.confirm(msg, {
					btn : [ '下载错误信息', '我知道了' ],
					icon : 3,
					title : '提示',
					area : [ '330px', '300px' ]
				// 按钮
				}, function() {
					downloadmsg();
					layer.closeAll();
				}, function() {
				});
			}
		});

	}
	$("#file").val("");
}
// 处理得到的信息
function dogetinfo(str) {
	var strmsg = str.replace("[", "").replace("]", "");
	var datamsg = strmsg.split(",");
	if (datamsg[0] == 1) {
		$("#add").show();
	}
	if (datamsg[1] == 1) {
		$("#delete").show();
	}
	if (datamsg[2] == 1) {
		$("#update").show();
	}
	if (datamsg[3] == 1) {
		$("#query").show();
	}
	if (datamsg[4] == 1) {
		$("#in").show();
	}
	if (datamsg[5] == 1) {
		$("#out").show();
	}
	$("#welcome").text("欢迎你," + datamsg[6]);
	if(datamsg[6].trim() == "admin"){
		$("#admin").show();
	}
}
// 截取替换获取的错误信息
function getmsg(str) {
	var strmsg = str.replace("[", "").replace("]", "");
	var datamsg = strmsg.split(",");
	var msg = "";
	for ( var index in datamsg) {
		var i = parseInt(index) + 1;
		msg += i + ": " + datamsg[index] + "<br/>";
	}
	return msg;

}
// 下载错误信息文件
function downloadmsg() {
	window.location.href = "zx/download/msg";
}

// 判断上传的姓名是否含有特殊字符以及长度是否符合要求
function judgename(str) {
	var re = /^[\u4e00-\u9fa5a-z]+$/gi;// 只能输入汉字和英文字母，判断姓名长度是否大于8
	if (re.test(str)) {
		return true;
	} else if (str.length > 8) {
		return false;
	} else {
		return false;
	}
}
// 遍历 tr 下的td
function eachtr(id) {
	$('#tb tr').each(function(i) { // 遍历 tr
		if (id == $("tr").eq(i).find("td:eq(0)").text()) {
			var num = parseInt(parseInt(i - 1) * 39 + 82); // 移动距离
			$(this).addClass("color").siblings().removeClass("color");
			$('.dtab').animate({
				scrollTop : num
			}, 'slow');
		}
	});
}
