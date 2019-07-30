//$("input[name='add']").get(0).checked = true;
//var zx = $("input[name='add']:checked").val();
// alert($("#id").text().trim());

$(document).ready(function() {
	$.ajax({
		url : 'zx/resteasy/getper',
		type : 'post',
		success : function(data) {
			if (data == 0) {		
				layer.msg('请先登录', {
					time:1000,
					icon : 5
				});
				setTimeout("window.location = 'login.html'", 1000);
			} else if(data == "666"){
				layer.msg('你没有权限', {
					time:1000,
					icon : 5
				});
					window.location = "zx/resteasy/loginout";
					
					setTimeout("window.location = 'login.html'", 1000);
			}
			else {
				$("#id").text("admin");
				$("input[name='add']").get(0).checked = true;
				$("input[name='delete']").get(0).checked = true;
				$("input[name='update']").get(0).checked = true;
				$("input[name='query']").get(0).checked = true;
				$("input[name='in']").get(0).checked = true;
				$("input[name='out']").get(0).checked = true;
			}
		},
	});
});

$('#query').click(function () {
	var cond = $("#content").val();
	if(cond == ""){
		layer.msg('输入为空', {
			time:1000,
			icon : 5
		});
	}else{
		var index = load("查询中...");
		$.ajax({
			url : 'zx/resteasy/permissions/'+cond,
			type : 'post',
			success : function(data) {
				var json = eval('(' + data + ')'); 
				
				if (data == 0) {
					layer.msg('无此用户', {
						time:1000,
						icon : 5
					});
				} else {
					$("#btid").val(json.id);
					if(json.userid == 1){
						$("#id").text("admin");
					}else{
						$("#id").text(json.userid);
					}
					if(json.add == 1){
						$("input[name='add']").get(0).checked = true;
					}else{
						$("input[name='add']").get(1).checked = true;
					}
					if(json.delete == 1){
						$("input[name='delete']").get(0).checked = true;
					}else{
						$("input[name='delete']").get(1).checked = true;
					}
					if(json.update == 1){
						$("input[name='update']").get(0).checked = true;
					}else{
						$("input[name='update']").get(1).checked = true;
					}
					if(json.query == 1){
						$("input[name='query']").get(0).checked = true;
					}else{
						$("input[name='query']").get(1).checked = true;
					}
					
					if(json.in == 1){
						$("input[name='in']").get(0).checked = true;
					}else{
						$("input[name='in']").get(1).checked = true;
					}
					if(json.out == 1){
						$("input[name='out']").get(0).checked = true;
					}else{
						$("input[name='out']").get(1).checked = true;
					}
				}
				layer.close(index);
			},
		});
	}
});
$('#update').click(function () {
	var index = load("修改中...");
	var btid = $("#btid").val();
	if(btid == "" || btid == "admin"){
		layer.msg('admin不要修改哦', {
			time:1000,
			icon : 3
		});
		layer.close(index);
	}else{
		$('#myform').ajaxSubmit({
			type : 'post',
			url : 'zx/resteasy/updateper/'+btid,
			success : function(data) {
				if(data == 1){
					layer.msg('修改成功', {
						time:1000,
						icon : 1
					});
				}
				layer.close(index);
			}
	});
	}

	

});
