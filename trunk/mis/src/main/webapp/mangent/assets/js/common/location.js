
var invtyData;
var getData = function() {
	var Data = {
		"reqHead": reqhead,
		"reqBody": {}
	}
	var changeData = JSON.stringify(Data)
	$.ajax({
		type: "post",
		url: url + "/mis/purc/InvtyDoc/selectInvtyEncdLike",
		async: true,
		data: changeData,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		success: function(data) {
			console.log(data)
			invtyData = data.respBody.list;
		}
	})
}

$(function() {
//	getData()
	//	时间插件修改
	jQuery('div.calendar .full-year a').click(function(e) {
		jQuery('div.calendar .full-year').attr('class', 'full-year false')
	})
	jQuery('div.calendar .full-month a').click(function(e) {
		jQuery('div.calendar .full-month').attr('class', 'full-year false')
	})
	//	鼠标指向变色
	$("table").delegate(".jqgrow", "mouseover", function() {
		$(this).css("background-color", "rgb(252, 248, 227)");
	});
	$("table").delegate(".jqgrow", "mouseout", function() {
		$(this).css("background-color", "");
	});
})

var cloneObj = function(obj) {
	var str, newobj = obj.constructor === Array ? [] : {};
	if(typeof obj !== 'object') {
		return;
	} else if(window.JSON) {
		str = JSON.stringify(obj), //序列化对象
		newobj = JSON.parse(str); //还原
	} else {
		for(var i in obj) {
			newobj[i] = typeof obj[i] === 'object' ? cloneObj(obj[i]) : obj[i];
		}
	}
	return newobj;
};

Date.prototype.format = function(fmt) {
	var o = {
		"M+": this.getMonth() + 1, //月份 
		"d+": this.getDate(), //日 
		"h+": this.getHours(), //小时 
		"m+": this.getMinutes(), //分 
		"s+": this.getSeconds(), //秒 
		"q+": Math.floor((this.getMonth() + 3) / 3), //季度 
		"S": this.getMilliseconds() //毫秒 
	};
	if(/(y+)/.test(fmt)) {
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	}
	for(var k in o) {
		if(new RegExp("(" + k + ")").test(fmt)) {
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
		}
	}
	return fmt;
}
//本月第一天
function getCurrentMonthFirst() {
	var date = new Date();
	date.setDate(1);
	var month = parseInt(date.getMonth() + 1);
	var day = date.getDate();
	if(month < 10) {
		month = '0' + month
	}
	if(day < 10) {
		day = '0' + day
	}
	var firstDate = date.getFullYear() + '-' + month + '-' + day;
	return firstDate
}
$(function() {
	var date1 = getCurrentMonthFirst();
	$(".date1").val(date1)
	$(".date2").val(new Date().format("yyyy-MM-dd"))
})


//var bookSet = localStorage.bookSet;

//if(!bookSet){
//	var bookSet1 = localStorage.bookSet;
//	var url3 = 'http://192.168.1.55:' + bookSet;
//	var url = 'http://192.168.1.55:' + bookSet;
//}

//var url = "http://192.168.3.84:8080"; //郝静 
//var url3 = "http://192.168.3.84:8080";

//var url = "http://169.254.130.115:8080"; //张庆美
//var url3 = "http://169.254.130.115:8080";

//var url3 = "http://192.168.3.224:8080"; //顺哥
//var url = "http://192.168.3.224:8080"; //顺哥

//var url = "http://192.168.3.33:8080";
//var url3 = 'http://192.168.3.33:8080'; // 常隆威

//var url = "http://192.168.3.169:8080"
//var url3 = 'http://192.168.3.169:8080'; // 美丽

//var url = "http://192.168.3.193:8080"
//var url3 = 'http://192.168.3.193:8080'; // 沈
//
//var url = "http://47.100.168.100:8088"
var url = "http://localhost:8080"
//var url3 = 'http://47.100.168.100:8088';
var url3 = 'http://localhost:8080';

//var url = "http://192.168.1.110:"+bookSet;
//var url3 = 'http://192.168.1.110:'+bookSet;

//var url3 = 'http://192.168.2.153:8080';
//var url = 'http://192.168.2.153:8080';//服务器
var jingdu = 8
var loginAccNum = localStorage.loginAccNum;
var loginUserName = localStorage.loginUserName;
var loginTime = localStorage.loginTime;
var whsId = localStorage.whsId;

var reqHead = {
	"accNum": loginAccNum,
	"userName":loginUserName,
	'loginTime': loginTime,
	'whsId':whsId
}
var reqhead = {
	"accNum": loginAccNum,
	"userName":loginUserName,
	'loginTime': loginTime,
	'whsId':whsId
}

