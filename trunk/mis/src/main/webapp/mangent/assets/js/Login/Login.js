$(function() {
	var time = new Date().format("yyyy-MM-dd");
	$('#dt').val(time); //日期
})

function getNow(s) {
	return s < 10 ? '0' + s : s;
}

function Time() {
	var d = new Date();
	
	var h = d.getHours()//获取小时
	
	var m = d.getMinutes()//获取分钟
	
	var s = d.getSeconds()//获取秒
	
	var Time = getNow(h) + ':' + getNow(m) + ':' + getNow(s)
	
	return Time;
}

//点击登陆
$(function() {
	$('.submit_btn').click(function() {
		var accNum = $('#userName').val();
		var passwords = $('#password').val();
		var accSet = $("#content").text();
		var loginDate = $('#dt').val()
		var Date = Time()
		var loginTime = $('#dt').val() + ' ' + Date;
		localStorage.setItem("loginDate", loginDate); //存储名字为logindate值为login_time的变量
		localStorage.setItem("loginTime", loginTime); //存储名字为logindate值为login_time的变量
		localStorage.setItem("loginAccNum", accNum);//登陆用户编码
		var save = {
			"reqHead":reqHead,
			"reqBody": {
				"accNum": accNum,
				"password": passwords,
				"accSet": "",
				"loginTime": loginTime
			}
		}
		var saveJson = JSON.stringify(save);
		if(accNum == "") {
			alert("请输入账号")
		} else if(passwords == "") {
			alert("请输入密码")
		} else if(accSet == ""||accSet == "请选择账套") {
			alert("请选择账套")
		}  else if(loginTime == "") {
			alert("请选择时间")
		} else {
			$.ajax({
				type: "post",
				url: url + "/mis/system/misUser/login",
				async: true,
				data: saveJson,
				dataType: 'json',
				contentType: 'application/json',
				success: function(data) {
					console.log(data)
					var whsId = data.respBody.whsId;
					localStorage.setItem("whsId", whsId);
					if(data.respHead.isSuccess == false) {
						alert(data.respHead.message);
					}
					if(data.respHead.isSuccess == true) {
						location.href = 'index_iframe.html';
					}
				},
				error: function(err) {
					console.log(err)
				}
			});
		}
	})

})

$(document).keydown(function(event) {
	if(event.keyCode == 13) {
		$('.submit_btn').click();
	}
});

//点击x图标
$(function() {
	$(".fa-times-circle-o").click(function() {
		$('input').val("");
		$("#content").text("请输入账套");
		$("#content").removeClass("block");
		$("#content").addClass("gray");
	})
})

//点击下拉图标
$(function() {
	$(".ul").click(function() {
		if($("#div").css("display") == "none") { //判断下拉还是上拉
			$("#div").slideDown(); //设置下拉动画
		} else {
			$("#div").slideUp(); //设置上拉动画
		}
	})
	$("#content").addClass("gray");
})

function change(e) { //把内容写入输入的div中
	var a = $(e).text();
	var b = $(e).attr("value");
	$("#content").text(a);
	$("#content").removeClass("gray");
	$("#content").addClass("block");
	if($("#div").css("display") !== "none") {
		$("#div").slideUp();
	}
}
//点击下拉框之外
$(function() {　　
	$(document).bind("click", function(e) {
		if($(e.target).closest(".ul").length == 0 && $(e.target).closest("li").length == 0) {　　
			$("#div").slideUp();　　　　
		}　　
	})
})
//点击日历弹出日历
$(function() {
	$(".fa-calendar-o").click(function() {
		$("#dt").focus()
	});
})

//点击图标显示密码
$(function() {
	$(".fa.fa-eye").mousedown(function() {
		$(".password").attr("type", "text");
	});
	$(".fa.fa-eye").mouseup(function() {
		$(".password").attr("type", "password");
	});
})