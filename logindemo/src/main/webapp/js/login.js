function checkimage(styleflag) {
	var imagecode = $("#imagecode").val();
	$.ajax({
		url : 'http://localhost:8080/logindemo/zx/resteasy/getimage/'
				+ imagecode,
		type : 'post',
		success : function(data) {
			if (1 == data) {
				if (styleflag == "getphone") {
					dogetphone();
				} else if (styleflag == "getemail") {
					dogetemail();
				} else if (styleflag == "accountlogin") {
					doacclogin();
				} else if (styleflag == "codelogin") {
					docdelogin();
				}
			} else {
				layer.msg('图片验证码输入错误', {
					time:1000,
					icon : 5
				});
			}
		},
	});
}

function dogetphone() {
	var index = load("发送中......");
	$('#codeform').ajaxSubmit({
		type : 'post',
		url : 'http://localhost:8080/logindemo/zx/resteasy/phonesend',
		success : function(data) {
			layer.msg('发送成功', {
				time:1000,
				icon : 1
			});
			layer.close(index);
		}
	});
}

// 获取邮箱验证码
function dogetemail() {
	var index = load("发送中......");
	$('#codeform').ajaxSubmit({
		type : 'post',
		url : 'http://localhost:8080/logindemo/zx/resteasy/emailsend',
		success : function(data) {
			layer.msg('发送成功', {
				time:1000,
				icon : 1
			});
			layer.close(index);
		}
	});

}

function doacclogin() {
	var index = load("登录中.....");
	$('#accountform').ajaxSubmit(
			{
				type : 'post',
				url : 'http://localhost:8080/logindemo/zx/resteasy/cclg',
				success : function(data) {
					if (data == 1) {
						window.location = "index.html";
					} else if (data == 0) {
						layer.msg('无此用户，请注册！', {
							time:1000,
							icon : 5
						});
						$("#images").attr(
								"src",
								"http://localhost:8080/logindemo/zx/resteasy/image?"
										+ Math.random());
					} else {
						layer.msg('密码错误', {
							time:1000,
							icon : 5
						});
						$("#images").attr(
								"src",
								"http://localhost:8080/logindemo/zx/resteasy/image?"
										+ Math.random());
					}
					layer.close(index);
				}
			});
}

function docdelogin() {
	var index = load("登录中...");
	$('#codeform').ajaxSubmit({
		type : 'post',
		url : 'http://localhost:8080/logindemo/zx/resteasy/codelg',
		success : function(data) {
			if (data == 1) {
				window.location = "index.html";
			} else if(data == 1123){
				layer.msg('已为你自动注册账号，账号为你输入的手机号或者邮箱号，密码初始为1123', {
					time:5000,
					icon : 5
				});
				setTimeout(window.location = "index.html", 5000);
			}
			else {
				layer.msg('短信验证码错误', {
					time:1000,
					icon : 5
				});
			}
			layer.close(index);
		}
	});
}
/** 判断是否是手机号* */
function isPhoneNumber(tel) {
	var reg = /^0?1[3|4|5|6|7|8][0-9]\d{8}$/;
	return reg.test(tel);
}
/** 判断是否是邮箱* */
function isEmail(email) {
	var reg = new RegExp(
			"^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$"); // 正则表达式
	if (!reg.test(email)) { // 正则验证不通过，格式不对
		return false;
	} else {
		return true;
	}
}
/** 账号登录* */
function accountlogin() {
	$("#imagecode").val("");
	$("#accountform").show();
	$("#codeform").hide();
	$("#loginflag").val("accountlogin");
	$("#images").attr(
			"src",
			"http://localhost:8080/logindemo/zx/resteasy/image?"
					+ Math.random());
}
/** 验证码登录* */
function codelogin() {
	$("#imagecode").val("");
	$("#accountform").hide();
	$("#codeform").show();
	$("#loginflag").val("codelogin");
	$("#images").attr(
			"src",
			"http://localhost:8080/logindemo/zx/resteasy/image?"
					+ Math.random());
}

$('#confirm').click(function() {
	var style = "getphone";
	var account = $("#account_2").val();
	var imagecode = $("#imagecode").val();
	if (account == "") {
		layer.msg('邮箱或者手机号为空', {
			time:1000,
			icon : 5
		});
	} else if (isPhoneNumber(account)) {
		style = "getphone";
		if (imagecode == "") {
			layer.msg('请输入图片验证码', {
				time:1000,
				icon : 5
			});
		} else {
			checkimage(style);
		}
	} else if (isEmail(account)) {
		style = "getemail";
		if (imagecode == "") {
			layer.msg('请输入图片验证码', {
				time:1000,
				icon : 5
			});
		} else {
			checkimage(style);
		}
	} else {
		layer.msg('手机/邮箱输入错误', {
			time:1000,
			icon : 5
		});
	}
});
$('#btn').click(function() {
	var loginflag = $("#loginflag").val();
	if (loginflag == "accountlogin") {
		var account = $("#account_1").val();
		var password = $("#password").val();
		var imagecode = $("#imagecode").val();
		if (account == "" || password == "") {
			layer.msg('账号或者密码为空', {
				time:1000,
				icon : 5
			});
		} else if (imagecode == "") {
			layer.msg('请输入图片验证码', {
				time:1000,
				icon : 5
			});
		} else if (isPhoneNumber(account)) {
			doaccountlogin();
		} else if (isEmail(account)) {
			doaccountlogin();
		} else if (account == "admin") {
			doaccountlogin();
		} else {
			layer.msg('账号不合法', {
				time:700,
				icon : 5
			});
		}
	} else {
		var account = $("#account_2").val();
		var imagecode = $("#imagecode").val();
		var code = $("#code").val();
		// var password = $("#password").val();
		if (account == "") {
			layer.msg('手机或者邮箱输入为空', {
				time:1000,
				icon : 5
			});
		} else if (code == "") {
			layer.msg('手机/邮箱验证码为空', {
				time:1000,
				icon : 5
			});
		} else if (imagecode == "") {
			layer.msg('图片验证码为空', {
				time:1000,
				icon : 5
			});
		} else if (isPhoneNumber(account) || isEmail(account)) {
			docodelogin();
		} else {

			layer.msg('账号非法', {
				time:1000,
				icon : 5
			});
		}
	}
});

function doaccountlogin() {
	var style = "accountlogin";
	checkimage(style);
}

function docodelogin() {
	var style = "codelogin";
	checkimage(style);

}

$(document).ready(function() {
	$("#loginflag").val("accountlogin");
});