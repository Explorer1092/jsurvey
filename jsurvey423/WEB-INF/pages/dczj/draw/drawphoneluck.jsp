<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="width=device-width,height=device-height,initial-scale=1,minimum-scale=1,user-scalable=no">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>抽奖</title>
<script src="../../resources/dczj/draw/js/jquery.min.js"></script>
<script src="../../resources/dczj/draw/js/phoneturntable.js"></script>
<style type="text/css">
.lottery {
	position: relative;
	display: inline-block;
	text-align: center
}

.lottery img {
	position: absolute;
	top: 50%;
	left: 50%;
	margin-left: -76px;
	margin-top: -82px;
	cursor: pointer;
}

#message {
	position: absolute;
	top: 0px;
	left: 10%;
}

.reward_bar {
	height: 60px;
	line-height: 60px;
	background-color: #4c9fe3;
	text-align: center;
	font-size: 16px;
	color: #fefefe;
}

.btn_reward {
	color: #fefefe;
	text-decoration: underline;
}
</style>
</head>
<body>
	<center>
		<input type="hidden" id="formId" value="${formId}">
		<div class="lottery">
			<canvas id="myCanvas" width="370" height="380">
			当前浏览器版本过低，请使用其他浏览器尝试 </canvas>
			<p id="message"></p>
			<img src="../../resources/dczj/draw/images/start.png" id="start">
		</div>
	</center>
	<script type="text/javascript">
	var prizelist;
	$.ajax({
		type:"post",
		async:false, 
		url:"../../front/draw/getPrizeList.do?",
		data:"dczjid=${dczjid}",
		success:function(result){
			if(result.success) {
				prizelist = JSON.parse(result.params.prizelist); 
			}else {
				alert(result.message);
			}
		}
	});
	
	var wheelSurf;
	// 初始化装盘数据 正常情况下应该由后台返回
	var initData = {
		"success": true,
		"list": prizelist
	};

	// 计算分配获奖概率(前提所有奖品概率相加100%)
	function getGift(){
// 		var percent = Math.random()*100;
// 		var totalPercent = 0;
// 		for(var i = 0 ,l = initData.list.length;i<l;i++){
// 			totalPercent += initData.list[i].percent;
// 			if(percent<=totalPercent){
// 				return initData.list[i];
// 			}
// 		}     
	}

	var list = {};
	
	var angel = 360 / initData.list.length;
	// 格式化成插件需要的奖品列表格式
	for (var i = 0, l = initData.list.length; i < l; i++) {
		list[initData.list[i].rank] = {
			id:initData.list[i].id,
			name: initData.list[i].name,
			image: initData.list[i].image
		};
	}
	// 查看奖品列表格式
	
	// 定义转盘奖品
	wheelSurf = $('#myCanvas').WheelSurf({
		list: list, // 奖品 列表，(必填)
		outerCircle: {
			color: '#df1e15' // 外圈颜色(可选)
		},
		innerCircle: {
			color: '#f4ad26' // 里圈颜色(可选)
		},
		dots: ['#fbf0a9', '#fbb936'], // 装饰点颜色(可选)
		disk: ['#ffb933', '#ffe8b5', '#ffb933', '#ffd57c', '#ffb933', '#ffe8b5', '#ffd57c'], //中心奖盘的颜色，默认7彩(可选)
		title: {
			color: '#5c1e08',
			font: '19px Arial'
		} // 奖品标题样式(可选)
	});

	// 初始化转盘
	wheelSurf.init();
	// 抽奖
	var throttle = true;
	$("#start").on('click', function () {
// 		var winData = getGift() // 正常情况下获奖信息应该由后台返回
         $.ajax({
			type:"post",
			url:"../../front/draw/godraw.do?",
			data:"dczjid=${dczjid}",
			success:function(result){
				if(result.success) {
					var prizeId = result.params.prizeId;
					var prizeName = result.params.prizeName;
					if(!throttle){
						return false;
					}
					throttle = false;
					var count = 0;
					// 计算奖品角度

					for (const key in list) {
						if (list.hasOwnProperty(key)) {                    
							if (prizeId == list[key].id) {
								break;
							}
							count++;    
						}
					}
			  
					// 转盘抽奖，
					wheelSurf.lottery((count * angel + angel / 2), function () {
						alert("恭喜你抽中"+prizeName);
						throttle = true;
					})
				}else {
					alert(result.message);
				}
			}
		});
	})

	
</script>
</body>
</html>