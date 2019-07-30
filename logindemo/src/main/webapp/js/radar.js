function radar() {
	var dom = document.getElementById("container");
	var myChart = echarts.init(dom);
	myChart.showLoading()
	// var namey = []; // 类别数
	var numo = getdata();
	option = {
			title : {
				text : '年龄数量雷达图',
				subtext : 'by zx',
			// sublink: 'http://e.weibo.com/1341556070/AjwF2AgQm'
			},
		    tooltip: {},
		    legend: {
		        data: ['数量']
		    },
		    grid : {
				left : '0%',
				right : '0%',
				bottom : '30%',
				containLabel : true
			},
		    radar: {
		        // shape: 'circle',
		        name: {
		            textStyle: {
		                color: '#fff',
		                backgroundColor: '#999',
		                borderRadius: 3,
		                padding: [3, 5]
		           }
		        },
		        indicator: [
		           { name: '0-20岁', max: getdatamax()},
		           { name: '20-40岁', max: getdatamax()},
		           { name: '40-60岁）', max: getdatamax()},
		           { name: '60-80岁', max: getdatamax()},
		           { name: '80-100岁', max: getdatamax()}
		           ]
		    },
		    series: [{
		        name: '年龄数量',
		        type: 'radar',
		        // areaStyle: {normal: {}},
		        data : [
		            {
		                value : numo,
		                name : '年龄人数'
		            }
		          
		        ]
		    }]
		};;
	myChart.hideLoading(); // 隐藏加载动画
	if (option && typeof option === "object") {
		myChart.setOption(option, true);
	}

}