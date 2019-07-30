function load(str) {
	var loadingIndex = layer.load(2, { // icon支持传入0-2
		shade : [ 0.5, 'gray' ], // 0.5透明度的灰色背景
		content : str,
		success : function(layero) {
			layero.find('.layui-layer-content').css({
				'padding-top' : '39px',
				'width' : '60px'
			});
		}
	});
	
	return loadingIndex;
}
