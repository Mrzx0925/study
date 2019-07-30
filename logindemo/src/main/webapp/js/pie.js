function pie() {
	var dom = document.getElementById("container");
	var myChart = echarts.init(dom);
	myChart.showLoading()
	// var namey = []; // 类别数
	var numo = getdata();
	var app = {};
	option = null;
	app.title = '环形图';

	option = {
		title : {
			text : '年龄数量饼图',
			subtext : 'by zx',
		// sublink: 'http://e.weibo.com/1341556070/AjwF2AgQm'
		},
		tooltip : {
			trigger : 'item',
			formatter : "{a} <br/>{b}: {c} ({d}%)"
		},
		legend : {
			orient : 'vertical',
			x : 'right',
			data : [ '0-20', '20-40', '40-60', '60-80', '80-100' ]
		},

		series : [ {
			name : '年龄数量统计',
			type : 'pie',
			radius : [ '50%', '70%' ],
			avoidLabelOverlap : false,
			label : {
				normal : {
					show : false,
					position : 'center'
				},
				emphasis : {
					show : true,
					textStyle : {
						fontSize : '20',
						fontWeight : 'bold'
					}
				}
			},
			labelLine : {
				normal : {
					show : false
				}
			},
			data : [ {
				value : numo[0],
				name : '0-20'
			}, {
				value : numo[1],
				name : '20-40'
			}, {
				value : numo[2],
				name : '40-60'
			}, {
				value : numo[3],
				name : '60-80'
			}, {
				value : numo[4],
				name : '80-100'
			} ]
		} ]
	};
	myChart.hideLoading(); // 隐藏加载动画
	if (option && typeof option === "object") {
		myChart.setOption(option, true);
	}

}