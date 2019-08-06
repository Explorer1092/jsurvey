/**
 * server端接口主域名
*/
var jmpurl="http://jmpotal.hanweb.com/jmp_3.0";

/**
 * 判断容器类型
*/

function containerT() {
	
	var sUserAgent = navigator.userAgent.toLowerCase();
	var bIsIpad = sUserAgent.match(/ipad/i) == "ipad";
	var bIsIphoneOs = sUserAgent.match(/iphone os/i) == "iphone os";
	var bIsMidp = sUserAgent.match(/midp/i) == "midp";
	var bIsUc7 = sUserAgent.match(/rv:1.2.3.4/i) == "rv:1.2.3.4";
	var bIsUc = sUserAgent.match(/ucweb/i) == "ucweb";
	var bIsAndroid = sUserAgent.match(/android/i) == "android";
	var bIsCE = sUserAgent.match(/windows ce/i) == "windows ce";
	var bIsWM = sUserAgent.match(/windows mobile/i) == "windows mobile";
	var bIsApp = sUserAgent.indexOf('hanweb') > -1;
	
	if (bIsApp && bIsIpad) {
		return "ipad";
	}if (bIsApp && bIsIphoneOs) {
		return "iphone";
	}if (bIsApp && bIsAndroid) {
		return "android";
	}else{
		return "web";
	}
}




/**
 * 获取url中"?"符后的字串
*/
function GetRequest() {
	var url = window.location.search; 
	var theRequest = new Object();
	if (url.indexOf("?") != -1) {
		var str = url.substr(1);
		strs = str.split("&");
		for (var i = 0; i < strs.length; i++) {
			theRequest[strs[i].split("=")[0]] = (strs[i].split("=")[1]);
		}
	}
	return theRequest;
}

/**
 * 格式化时间
*/
function getLocalTime(nS) {
	return new Date(parseInt(nS)).toLocaleString().replace(/:\d{1,2}$/,' ');      
} 

function delHtmlTag(str){
	return str.replace(/<[^>]+>/g,"");//去掉所有的html标记
}
/**
 * POST请求方式向服务器发送数据
*/
function post(url, settings, type){
	mui.ajax( url,{
		data: settings,
		dataType: type,//服务器返回json格式数据
		type:'post',//HTTP请求类型
		async: false,  
		timeout:10000,//超时时间设置为10秒；
		success:function(data){
			//服务器返回响应，根据响应结果，分析是否登录成功；
			return data;
		},
		error:function(xhr,type,errorThrown){
			//异常处理；
			return console.log(type);
		}
	});
}

/**
 * GET请求方式向服务器发送数据
*/
function get(url, settings, type){
	mui.ajax( url,{
		data: settings,
		dataType: type,//服务器返回json格式数据
		type:'get',//HTTP请求类型
		async: false,  
		timeout:10000,//超时时间设置为10秒；
		success:function(data){
			//服务器返回响应，根据响应结果，分析是否登录成功；
			return data;
		},
		error:function(xhr,type,errorThrown){
			//异常处理；
			return console.log(type);
		}
	});
}


/**
 * 限定返回json格式的数据
*/
function getJson(url, settings){
	var result;
	mui.ajax( url,{
		data: settings,
		dataType: 'json',//服务器返回json格式数据
		type:'get',//HTTP请求类型
		async: false,  
		timeout:10000,//超时时间设置为10秒；
		success:function(data){
			//服务器返回响应，根据响应结果，分析是否登录成功；
		    result = data;
		},
	
	});
	return result;
}


function delHtmlTag(str){
return str.replace(/<[^>]+>/g,"");//去掉所有的html标记
}