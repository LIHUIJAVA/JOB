var count;
var pages;
var page = 1;
var rowNum;
$(function() {
	$('.end').addClass("gray");
	$(".end").attr('disabled', true);
	$('.end_r').addClass("gray");
	$(".end_r").attr('disabled', true);
	$('.saveOrder').addClass("gray") //增加
	$('.saveOrder').attr('disabled', true);
	$('.start').addClass("gray");
	$(".start").attr('disabled', true);
	$('.start_r').addClass("gray");
	$(".start_r").attr('disabled', true);
	$('.clean').addClass("gray");
	$(".clean").attr('disabled', true);
	$('.clean_r').addClass("gray");
	$(".clean_r").attr('disabled', true);
})
//初始化表格
$(function() {
	//	allHeight()
	$("#time_jqgrids").jqGrid({
		//		url: '../../assets/js/json/order.json',
		datatype: "local",
		//				data: data,
		colNames: ['下载设置编号', '店铺编号', '店铺名称', '间隔小时', '最近小时', '下次执行时间'], //jqGrid的列显示名字
		colModel: [{
				name: 'id',
				align: "center",
				index: 'invdate',
				editable: false,
				hidden: true
			},
			{
				name: 'shopId',
				align: "center",
				index: 'invdate',
				editable: false
			},
			{
				name: 'shopName',
				align: "center",
				index: 'invdate',
				editable: false
			},
			{
				name: 'intervalTime',
				align: "center",
				index: 'invdate',
				editable: false,
				hidden: true
			},
			{
				name: 'recentHours',
				editable: true,
				align: 'center',

			},
			{
				name: 'nextTime',
				align: "center",
				index: 'invdate',
				editable: false,
				hidden: true
			}
		],
		loadonce: false,
		//		rownumbers: true,
		autowidth: true,
		caption: '店铺',
		altclass: true,
		viewrecords: true,
		rowNum: 99999, //一页显示多少条
		//		rowList: [10, 20, 30], //可供用户选择一页显示多少条	
		height: 550,
		autoScroll: true,
		pager: "#time_jqgridPager",
		shrinkToFit: false,
		forceFit: true,
		sortable: false,
		multiselect: true, //复选框
		multiboxonly: true,
		cellEdit: false,
		rownumWidth: 30, //序列号列宽度
		rownumbers: true,
		cellsubmit: "clientArray",
		ondblClickRow: function() {
			$('.saveOrder').removeClass("gray") //增加
			$('.saveOrder').attr('disabled', false);
			var gr = $("#time_jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
			$('#time_jqgrids').editRow(gr, true);
			$("#" + gr + "_recentHours").attr("type","number")
		},
	});
	search()
})
//初始化订单表格
$(function() {
	$("#message_jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json",
		//				data: data,
		colNames: ['时间', '店铺名称', '提示'], //jqGrid的列显示名字
		colModel: [{
				name: 'time',
				align: "center",
				index: 'invdate',
				editable: true
			}, {
				name: 'shopName',
				align: "center",
				index: 'invdate',
				editable: true
			},
			{
				name: 'message',
				align: "center",
				index: 'invdate',
				editable: false
			}
		],
		autowidth: true,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 10,
		scrollrows: true,
		autoScroll: true,
		rownumWidth: 15,  //序列号列宽度
		height: 218,
		shrinkToFit: false,
//		rowNum: 10, //一页显示多少条
		rowNum: 99999, //一页显示多少条
//		rowList: [10, 20, 30], //可供用户选择一页显示多少条
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		multiboxonly: true,
		caption: '订单下载提示',
		pager: "#message_jqgridPager",
	});
})

//初始化订单表格
$(function() {
	$("#message_return_jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json",
		//				data: data,
		colNames: ['时间', '店铺名称', '提示'], //jqGrid的列显示名字
		colModel: [{
				name: 'time',
				align: "center",
				index: 'invdate',
				editable: true
			}, {
				name: 'shopName',
				align: "center",
				index: 'invdate',
				editable: true
			},
			{
				name: 'message',
				align: "center",
				index: 'invdate',
				editable: false
			}
		],
		autowidth: true,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 10,
		scrollrows: true,
		autoScroll: true,
		rownumWidth: 15,  //序列号列宽度
		height: 218,
		shrinkToFit: false,
//		rowNum: 10, //一页显示多少条
		rowNum: 99999, //一页显示多少条
//		rowList: [10, 20, 30], //可供用户选择一页显示多少条
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		multiboxonly: true,
		caption: '退款单下载提示',
		pager: "#message_return_jqgridPager",
	});
})


//添加新数据
$(function() {
	$(".saveOrder").click(function() {
		var selectedId = $("#time_jqgrids").jqGrid("getGridParam", "selrow");
//		var num = /^[0-9]\d?$/;
		var recentHours = $("#" + selectedId + "_recentHours").val()
//		if(recentHours > 24) {
		if(!(/^[0-9]\d?$/.test(recentHours)) || recentHours > 24) {
			alert("请输入0-24之间的整数")
		} else {
			var rowDatas = $("#time_jqgrids").jqGrid('getRowData', selectedId);
			var id = rowDatas.id;
			var shopId = rowDatas.shopId;
			var shopName = rowDatas.shopName;
			var intervalTime = rowDatas.intervalTime;
			var nextTime = rowDatas.nextTime;
			var recentHours = $("#" + selectedId + "_recentHours").val()
			var save = {
				reqHead,
				"reqBody": {
					"id": id,
					"shopId": shopId,
					"shopName": shopName,
					"intervalTime": intervalTime,
					"nextTime": nextTime,
					"recentHours": recentHours,
				}
			}
			var saveJson = JSON.stringify(save);
			$.ajax({
				type: "post",
				url: url3 + "/mis/ec/downloadSet/edit",
				async: true,
				data: saveJson,
				dataType: 'json',
				contentType: 'application/json',
				success: function(data) {
					alert(data.respHead.message)
					$('.saveOrder').addClass("gray") //增加
					$('.saveOrder').attr('disabled', true);
					search()
				},
				error: function(err) {
					console.log("失败")
				}
			});
		}
	})
})


function search() {
	var savedata = {
		"reqHead": {
			"accNum": "root",
			"loginTime": ""
		},
		"reqBody": {
			"pageNo": 1,
			"pageSize": 999
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/ec/downloadSet/queryList',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			var mydata = {};
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#time_jqgrids").jqGrid("clearGridData");
			$("#time_jqgrids").jqGrid("setGridParam", {
				data: mydata.rows,
				localReader: {
					root: function(object) {
						return mydata.rows;
					},
					page: function(object) {
						return mydata.page;
					},
					total: function(object) {
						return mydata.total;
					},
					records: function(object) {
						return mydata.records;
					},
					repeatitems: false
				}
			}).trigger("reloadGrid");
		},
		error: function() {
			alert('查询失败')
		}
	});
}

function getJQAllData1() {
	//拿到grid对象
	var obj1 = $("#time_jqgrids");
	//获取grid表中所有的rowid值
	var rowIds1 = obj1.getDataIDs();
	//初始化一个数组arrayData容器，用来存放rowData
	var arrayData1 = new Array();
	if(rowIds1.length > 0) {
		for(var i = 0; i < rowIds1.length; i++) {
			arrayData1.push(obj1.getRowData(rowIds1[i]));
		}
	}
	return arrayData1;
}

var time2;
$(document).on('click', '.suer1', function() {
	var blankings = $("input[name='order']").val()
	if(blankings < 30 || blankings > 720) {
		alert("间隔时间不可大于12个小时（720分钟），不可小于半个小时（30分钟）")
		$("input[name='order']").val('')
	} else {
		window.blanking = blankings * 60000
		$('.start').removeClass("gray");
		$(".start").attr('disabled', false);
	}
})
$(document).on('click', '.start', function() {
	$("#mengban").show()
	$('.end').removeClass("gray");
	$(".end").attr('disabled', false);
	$('.clean').removeClass("gray");
	$(".clean").attr('disabled', false);
	$('.start').addClass("gray");
	$(".start").attr('disabled', true);
	var num1 = [];
	ds(num1)
	$("#message_jqgrids").jqGrid("clearGridData");
	$("#message_jqgrids").jqGrid("setGridParam", {
		data: num1
	}).trigger("reloadGrid");
	time2 = window.setInterval(function() {
		ds(num1)
		$("#message_jqgrids").jqGrid("clearGridData");
		$("#message_jqgrids").jqGrid("setGridParam", {
			data: num1
		}).trigger("reloadGrid");
	}, blanking);
})





function ds(num1) {
	var listData = getJQAllData1()
	for(var i = 0; i < listData.length; i++) {
		var storeId = listData[i].shopId
		var shopName = listData[i].shopName
		var recentHours = listData[i].recentHours
		if(recentHours == 0) {
			continue
		} else {
			var frontOneHour = new Date(new Date().getTime() - recentHours * 60 * 60 * 1000);
			function getNow(s) {
				return s < 10 ? '0' + s : s;
			}
			var myDate = new Date();
			//获取当前年
			var year = myDate.getFullYear();
			//获取当前月
			var month = myDate.getMonth() + 1;
			//获取当前日
			var date = myDate.getDate();
			var h = myDate.getHours(); //获取当前小时数(0-23)
			var m = myDate.getMinutes(); //获取当前分钟数(0-59)
			var s = myDate.getSeconds();

			now = year + '-' + getNow(month) + "-" + getNow(date) + " " + getNow(h) + ':' + getNow(m) + ":" + getNow(s);
			var myDate1 = frontOneHour;
			//获取当前年
			var year1 = myDate1.getFullYear();
			//获取当前月
			var month1 = myDate1.getMonth() + 1;
			//获取当前日
			var date1 = myDate1.getDate();
			var h1 = myDate1.getHours(); //获取当前小时数(0-23)
			var m1 = myDate1.getMinutes(); //获取当前分钟数(0-59)
			var s1 = myDate1.getSeconds();
			now1 = year1 + '-' + getNow(month1) + "-" + getNow(date1) + " " + getNow(h1) + ':' + getNow(m1) + ":" + getNow(s1);
			var savedata = {
				"reqHead": {
					"accNum": "root",
					"loginTime": ""
				},
				"reqBody": {
					"storeId": storeId,
					"startDate": now1,
					"endDate": now,
				}
			};
			var saveData = JSON.stringify(savedata);
			$.ajax({
				type: "post",
				contentType: 'application/json; charset=utf-8',
				url: url + '/mis/ec/platOrder/download',
				async: false,
				cache: true,
				data: saveData,
				dataType: 'json',
				success: function(data) {
					var mydata = {};
					mydata = data.respHead;
					message = mydata.message
					function createPerson(now, shopName,message) {　　　　
						var sum = new Object();　　　　
						sum.time = now;　　　　
						sum.shopName = shopName;　　
						sum.message = message;　　
						return sum;　　
					}
					var sum1 = createPerson(now, shopName, message);
					num1.push(sum1)
				},
				error: function() {
					alert('查询失败')
				}
			});
		}
	}
}

$(document).on('click', '.clean', function() {
//	window.location.reload()
	$("#message_jqgrids").jqGrid("clearGridData");
})

$(document).on('click', '.end', function() {
	$("#mengban").hide()
	$('.end').addClass("gray");
	$(".end").attr('disabled', true);
	$('.start').removeClass("gray");
	$(".start").attr('disabled', false);
	window.clearTimeout(time2);
})



//退款单
function getJQAllData1() {
	//拿到grid对象
	var obj1 = $("#time_jqgrids");
	//获取grid表中所有的rowid值
	var rowIds1 = obj1.getDataIDs();
	//初始化一个数组arrayData容器，用来存放rowData
	var arrayData1 = new Array();
	if(rowIds1.length > 0) {
		for(var i = 0; i < rowIds1.length; i++) {
			arrayData1.push(obj1.getRowData(rowIds1[i]));
		}
	}
	return arrayData1;
}
$(document).on('click', '.suer2', function() {
	var blankings1 = $("input[name='refund']").val()
	if(blankings1 < 30 || blankings1 > 720) {
		alert("间隔时间不可大于12个小时（720分钟），不可小于半个小时（30分钟）")
		$("input[name='refund']").val('')
	} else {
		window.blanking1 = blankings1 * 60000
		$('.start_r').removeClass("gray");
		$(".start_r").attr('disabled', false);
	}
})
var time3;
$(document).on('click', '.start_r', function() {
	$("#mengban").show()
	$('.end_r').removeClass("gray");
	$(".end_r").attr('disabled', false);
	$('.clean_r').removeClass("gray");
	$(".clean_r").attr('disabled', false);
	$('.start_r').addClass("gray");
	$(".start_r").attr('disabled', true);
	var num2 = [];
	ds_r(num2)
	$("#message_return_jqgrids").jqGrid("clearGridData");
	$("#message_return_jqgrids").jqGrid("setGridParam", {
		data: num2
	}).trigger("reloadGrid");
	time3 = window.setInterval(function() {
		ds_r(num2)
		$("#message_return_jqgrids").jqGrid("clearGridData");
		$("#message_return_jqgrids").jqGrid("setGridParam", {
			data: num2
		}).trigger("reloadGrid");
	}, blanking1);
})

function ds_r(num2) {
	var listData = getJQAllData1()
	for(var i = 0; i < listData.length; i++) {
		var storeId = listData[i].shopId
		var shopName = listData[i].shopName
		var recentHours = listData[i].recentHours
		if(recentHours == 0) {
			continue
		} else {
			var frontOneHour = new Date(new Date().getTime() - recentHours * 60 * 60 * 1000 - 1 * 60 * 60 * 1000);
			function getNow(s) {
				return s < 10 ? '0' + s : s;
			}
			var myDate = new Date();
			//获取当前年
			var year = myDate.getFullYear();
			//获取当前月
			var month = myDate.getMonth() + 1;
			//获取当前日
			var date = myDate.getDate();
			var h = myDate.getHours(); //获取当前小时数(0-23)
			var m = myDate.getMinutes(); //获取当前分钟数(0-59)
			var s = myDate.getSeconds();

			now = year + '-' + getNow(month) + "-" + getNow(date) + " " + getNow(h) + ':' + getNow(m) + ":" + getNow(s);
			var myDate1 = frontOneHour;
			//获取当前年
			var year1 = myDate1.getFullYear();
			//获取当前月
			var month1 = myDate1.getMonth() + 1;
			//获取当前日
			var date1 = myDate1.getDate();
			var h1 = myDate1.getHours(); //获取当前小时数(0-23)
			var m1 = myDate1.getMinutes(); //获取当前分钟数(0-59)
			var s1 = myDate1.getSeconds();
			now1 = year1 + '-' + getNow(month1) + "-" + getNow(date1) + " " + getNow(h1) + ':' + getNow(m1) + ":" + getNow(s1);
			var savedata = {
				"reqHead": {
					"accNum": "root",
					"loginTime": ""
				},
				"reqBody": {
					"storeId": storeId,
					"startDate": now1,
					"endDate": now,
					"ecOrderId": ''
				}
			};
			var saveData = JSON.stringify(savedata);
			$.ajax({
				type: "post",
				contentType: 'application/json; charset=utf-8',
				url: url + '/mis/ec/refundOrder/download',
				async: false,
				cache: true,
				data: saveData,
				dataType: 'json',
				success: function(data) {
					var mydata = {};
					mydata = data.respHead;
					message = mydata.message
					function createPerson(now, shopName,message) {　　　　
						var sum = new Object();　　　　
						sum.time = now;　　　　
						sum.shopName = shopName;　　
						sum.message = message;　　
						return sum;　　
					}
					var sum2 = createPerson(now, shopName, message);
					num2.push(sum2)
				},
				error: function() {
					alert('查询失败')
				}
			});
		}
	}
}

$(document).on('click', '.clean_r', function() {
//	window.location.reload()
	$("#message_return_jqgrids").jqGrid("clearGridData");
})

$(document).on('click', '.end_r', function() {
	$("#mengban").hide()
	$('.end_r').addClass("gray");
	$(".end_r").attr('disabled', true);
	$('.start_r').removeClass("gray");
	$(".start_r").attr('disabled', false);
	window.clearTimeout(time3);
})