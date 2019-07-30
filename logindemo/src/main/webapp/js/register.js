  function checkimage(styleflag) {
        var imagecode = $("#imagecode").val();
        $.ajax({
            url: 'http://localhost:8080/logindemo/zx/resteasy/getimage/' + imagecode,
            type: 'post',
            success: function (data) {
                if (1 == data) {
                    if (styleflag == "getphone") {
                        dogetphone();
                    } else if (styleflag == "getemail") {
                        dogetemail();
                    } else if (styleflag == "emailregister") {
                        doemailregister();
                    } else if (styleflag == "phoneregister") {
                        dophoneregister();
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
    	var index = load("发送中....");
        $('#phoneform')
            .ajaxSubmit({
                type: 'post',
                url: 'http://localhost:8080/logindemo/zx/resteasy/phonesend',
                success: function (data) {
                    if(data == "0"){
                    	layer.msg('用户存在，验证码已经发送，请直接根据验证码进行登录', {
            				time:1000,
            				icon : 3
            			});
                    }else{
                    	layer.msg('发送成功', {
            				time:1000,
            				icon : 1
            			});
                    }
                    layer.close(index);
                }
            });
    }

    //获取邮箱验证码
    function dogetemail() {
    	var index = laod("发送中....");
        $('#emailform')
            .ajaxSubmit({
                type: 'post',
                url: 'http://localhost:8080/logindemo/zx/resteasy/emailsend',
                success: function (data) {
                	 if(data == "0"){
                		 layer.msg('用户存在，验证码已经发送，请直接根据验证码进行登录', {
             				time:1000,
             				icon : 3
             			});
                     }else{
                    	 layer.msg('发送成功', {
             				time:1000,
             				icon : 1
             			});
                     }
                	 layer.close(index);
                }
            });
    }

    function doemailregister() {
    	var index = load("发送中.....");
        $('#emailform')
            .ajaxSubmit({
                type: 'post',
                url: 'http://localhost:8080/logindemo/zx/resteasy/emailregister',
                success: function (data) {
                	 if(data == "0"){
                		 layer.msg('用户存在请直接登录', {
              				time:1000,
              				icon : 3
              			});
                      }else{
                    	  layer.msg('注册方式已经发送至邮箱，请按照邮箱操作', {
               				time:2000,
               				icon : 3
               			});
                      }
                	layer.close(index);
                }
            });
    }

    function dophoneregister() {
        $('#phoneform')
            .ajaxSubmit({
                type: 'post',
                url: 'http://localhost:8080/logindemo/zx/resteasy/phoneregister',
                success: function (data) {
                	if(data == "0"){
                		layer.msg('短信验证码错误', {
            				time:1000,
            				icon : 5
            			});
                      }else{
                    	  layer.msg('注册成功', {
              				time:1000,
              				icon : 1
              			});
                      }
                }
            });
    }



    /**判断是否是手机号**/
    function isPhoneNumber(tel) {
        var reg = /^0?1[3|4|5|6|7|8][0-9]\d{8}$/;
        return reg.test(tel);
    }
    /**判断是否是邮箱**/
    function isEmail(email) {
        var reg = new RegExp(
            "^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$"); //正则表达式
        if (!reg.test(email)) { //正则验证不通过，格式不对
            return false;
        } else {
            return true;
        }
    }
    /**账号登录**/
    function emailregister() {
        $("#imagecode").val("");
        $("#emailform").show();
        $("#phoneform").hide();
        $("#registerflag").val("emailregister");
        $("#images").attr(
            "src",
            "http://localhost:8080/logindemo/zx/resteasy/image?" +
            Math.random());
    }
    /**验证码登录**/
    function phoneregister() {
        $("#imagecode").val("");
        $("#emailform").hide();
        $("#phoneform").show();
        $("#registerflag").val("phoneregister");
        $("#images").attr(
            "src",
            "http://localhost:8080/logindemo/zx/resteasy/image?" +
            Math.random());
    }
    $('#confirm').click(function () {
        var style = "getphone";
        var account = $("#account_2").val();
        if (account == "") {
        	 layer.msg('手机号为空', {
  				time:1000,
  				icon : 5
  			});
        } else if (!isPhoneNumber(account)) {
        	 layer.msg('手机号格式错误', {
  				time:1000,
  				icon : 5
  			});
        } else {
        	 var imagecode = $("#imagecode").val();
            style = "getphone";
            if (imagecode == "") {
            	 layer.msg('请输入图片验证码', {
       				time:1000,
       				icon : 5
       			});
            } else {
                checkimage(style);
            }
        }
    });
    $('#btn').click(function () {
        var registerflag = $("#registerflag").val();
        if (registerflag == "emailregister") {
            var account = $("#account_1").val();
            var password_1 = $("#password_1_1").val();
            var password_2 = $("#password_1_2").val();
            var imagecode = $("#imagecode").val();
            if (account == "" || password_1 == "" || password_2 == "") {
            	layer.msg('账号或者密码为空', {
       				time:1000,
       				icon : 5
       			});
            } else if (password_1 != password_2) {
            	layer.msg('密码不一致', {
       				time:1000,
       				icon : 5
       			});
            } else if (imagecode == "") {
            	layer.msg('请输入图片验证码', {
       				time:1000,
       				icon : 5
       			});
            } else if (isEmail(account)) {
                var style = "emailregister";
                checkimage(style);
            } else {
            	layer.msg('输入非法', {
       				time:1000,
       				icon : 5
       			});
            }
        } else {
            var account = $("#account_2").val();
            var code = $("#code").val();
            var password_1 = $("#password_2_1").val();
            var password_2 = $("#password_2_2").val();
            var imagecode = $("#imagecode").val();
            if (account == "" || password_1 == "" || password_2 == "") {
            	layer.msg('手机或者密码为空', {
       				time:1000,
       				icon : 5
       			});
            } else if (code == "") {
            	layer.msg('手机验证码为空', {
       				time:1000,
       				icon : 5
       			});
            } else if (password_1 != password_2) {
            	layer.msg('密码不一致', {
       				time:1000,
       				icon : 5
       			});
            } else if (imagecode == "") {
            	layer.msg('图片验证码为空', {
       				time:1000,
       				icon : 5
       			});
            } else if (isPhoneNumber(account)) {
                var style = "phoneregister";
                checkimage(style);
            } else {
            	layer.msg('输入非法', {
       				time:1000,
       				icon : 5
       			});
            }
        }
    });
    $(document).ready(function () {
        $("#registerflag").val("emailregister");
    });