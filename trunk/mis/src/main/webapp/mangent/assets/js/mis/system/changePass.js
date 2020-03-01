// JavaScript Document
$(document).ready(function() {
	$(".a1").each(function() {
		var hdw = $("<strong class='reda'>*</strong>");
		$(this).parent().append(hdw);
	});
	$("#form :input").blur(function() {
		$(this).parent().find(".a2").remove();
//		if($(this).is("#username")) {
//			var accNum = localStorage.loginName;
//			if(this.value == "") {
//				var hdw1 = $("<span class='a2 error'>用户名不得为空</span>");
//				$(this).parent().append(hdw1);
//			}else if(this.value!=accNum){
//				var hdw1 = $("<span class='a2 error'>账号与登陆账号不一致！</span>");
//				$(this).parent().append(hdw1);
//			}else{
//				var hdw1 = $("<span class='a2 righta'>正确</span>");
//				$(this).parent().append(hdw1);
//			}
//		}
		//旧密码
		if($(this).is("#oldPassword")) {
			if(this.value == "") {
				var hdw1 = $("<span class='a2 error'>密码不得为空</span>");
				$(this).parent().append(hdw1);
			} else {
				isLogin()
			}
		}
		//新密码
		if($(this).is("#password")) {
			if(this.value == "") {
				var hdw1 = $("<span class='a2 error'>密码不得为空</span>");
				$(this).parent().append(hdw1);
			} else {
				var hdw1 = $("<span class='a2 righta'>正确</span>");
				$(this).parent().append(hdw1);
			}
		}
		//确认密码
		if($(this).is("#passwords")) {
			if(this.value == "" || this.value != $("#password").val()) {
				var hdw1 = $("<span class='a2 error'>两次密码不一样</span>");
				$(this).parent().append(hdw1);
			} else {
				var hdw1 = $("<span class='a2 righta'>正确</span>");
				$(this).parent().append(hdw1);
			}
		}
	});
	$("#send").click(function() {
		$("#form :input").trigger("blur");
		var hdw3 = $(".error").length;
		if(hdw3) {
			return false;
		}
		isEdit()
	});
	$("#res").click(function() {
		$(".a2").remove();
	});
});

function isEdit() {
	var accNum = localStorage.loginAccNum;
	var passwords = $('#password').val();
	var save = {
		"reqHead": reqHead,
		"reqBody": {
			"accNum": accNum,
			"password": passwords,
		}
	}
	var saveJson = JSON.stringify(save);
	$.ajax({
		type: "post",
		url: url + "/mis/system/misUser/edit",
		async: true,
		data: saveJson,
		dataType: 'json',
		contentType: 'application/json',
		success: function(data) {
			alert(data.respHead.message)
			if(data.respHead.isSuccess == true){
				top.location.href='../../Login.html';
			}	
		},
		error: function() {
			alert("修改失败")
		}
	});
}

function isLogin() {
	var accNum = localStorage.loginAccNum;
	var passwords = $('#oldPassword').val();
	var save = {
		"reqHead": reqHead,
		"reqBody": {
			"accNum": accNum,
			"password": passwords,
			"accSet": "",
			"loginTime": localStorage.loginTime
		}
	}
	var saveJson = JSON.stringify(save);
	$.ajax({
		type: "post",
		url: url + "/mis/system/misUser/login",
		async: true,
		data: saveJson,
		dataType: 'json',
		contentType: 'application/json',
		success: function(data) {
			if(data.respHead.isSuccess == false) {
					var hdw1 = $("<span class='a2 error'>密码错误</span>");
					$("#oldPassword").parent().append(hdw1);	
			}
			if(data.respHead.isSuccess == true) {
				var hdw1 = $("<span class='a2 righta'>正确</span>");
				$("#oldPassword").parent().append(hdw1);
			}
		},
		error: function() {
			var hdw1 = $("<span class='a2 error'>请检查网络</span>");
			$("#oldPassword").parent().append(hdw1);
		}
	});
}