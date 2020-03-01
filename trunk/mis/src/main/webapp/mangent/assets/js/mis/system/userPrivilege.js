//部门
var userTree = [];
$(function() {
	var data3 = {
		"reqHead": reqhead,
		"reqBody": {
			"roleId": 'root',
		}
	};
	var postD3 = JSON.stringify(data3);
	$.ajax({
		type: "post",
		data: postD3,
		url: url + "/mis/system/misUser/userTree",
		async: true,
		dataType: 'json',
		contentType: "application/json; charset=utf-8",
		success: function(data) {
			userTree = data.respBody.tree;
			console.log(userTree)
		},
		error: function() {
			alert("获取失败")
		},
	});
})

//菜单
var roleMenuList = [];
$(function() {
	var data3 = {
		"reqHead": reqhead,
		"reqBody": {
			"roleId": 'root',
		}
	};
	var postD3 = JSON.stringify(data3);
	$.ajax({
		type: "post",
		data: postD3,
		url: url + "/mis/system/menu/roleMenuList",
		async: true,
		dataType: 'json',
		contentType: "application/json; charset=utf-8",
		success: function(data) {
			roleMenuList = data.respBody.roleMenuList;
			console.log(data.respBody.roleMenuList)
		},
		error: function() {
			alert("获取失败")
		},
	});
})

layui.use('tree', function() {
	/*第二课树延时一会，在本地由于数据不多可能没什么效果*/
	var tree2 = null;
	setTimeout(function() {
		tree2 = layui.tree({
			elem: '#demo1', //指定元素，生成的树放到哪个元素上
//			check: 'checkbox', //勾选风格
			skin: 'as', //设定皮肤
			drag: true, //点击每一项时是否生成提示信息
			checkboxName: 'aa[]', //复选框的name属性值
			checkboxStyle: "", //设置复选框的样式，必须为字符串，css样式怎么写就怎么写
			click: function(item) { //点击节点回调
				console.log(item)
			},
			nodes: userTree
		});
	}, 200);


	setTimeout(function() {
		var tree=null;
		tree= layui.tree({
			elem: '#demo', //指定元素，生成的树放到哪个元素上
			check: 'checkbox', //勾选风格
			skin: 'as', //设定皮肤
			drag: true, //点击每一项时是否生成提示信息
			checkboxName: 'aa[]', //复选框的name属性值
			checkboxStyle: "", //设置复选框的样式，必须为字符串，css样式怎么写就怎么写
			click: function(item) { //点击节点回调
				console.log(item)
			},
			onchange: function() { //当当前input发生变化后所执行的回调
				console.log(this);
			},
			nodes: roleMenuList
		});
	}, 200);
});