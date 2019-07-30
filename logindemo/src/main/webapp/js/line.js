function line() {
	var dom = document.getElementById("container");
	var myChart = echarts.init(dom);
	myChart.showLoading()
	// var namey = []; // 类别数
	var numo = []; // 数量数组
	var app = {};
	option = null;
	option = {
		title : {
			text : '年龄数量折线图',
			subtext : 'by zx',
		// sublink: 'http://e.weibo.com/1341556070/AjwF2AgQm'
		},
		color : [ '#3398DB' ],
		tooltip : {
			trigger : 'axis',
			axisPointer : { // 坐标轴指示器，坐标轴触发有效
				type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
			}
		},
		grid : {
			left : '0%',
			right : '0%',
			bottom : '30%',
			containLabel : true
		},
		xAxis : {
			type : 'category',
			data : [ '0-20', '20-40', '40-60', '60-80', '80-100' ]
		},
		yAxis : {
			type : 'value'
		},
		series : [ {
			data : getdata(),
			type : 'line'
		} ]
	};
	myChart.hideLoading(); // 隐藏加载动画
	if (option && typeof option === "object") {
		myChart.setOption(option, true);
	}

}