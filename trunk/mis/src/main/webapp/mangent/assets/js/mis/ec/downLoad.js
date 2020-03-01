var count;
var pages;
var page = 1;
var rowNum;
$(function() {
	$('.start').addClass("gray")
	$('.clean').addClass("gray")
	$('.start_r').addClass("gray")
	$('.clean_r').addClass("gray")
	$('button').attr('disabled', false);
	$(".gray").attr('disabled', true)
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
		height: 500,
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
			console.log(gr)
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
		height: 200,
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
		height: 190,
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
			console.log($("#" + selectedId + "_recentHours").val())
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
			console.log(saveJson)
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
//	console.log(saveData)
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/ec/downloadSet/queryList',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			console.log(data)
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
var d_startTime;
var d_endTime;
var d_jgTime;
var d_timer;
$(document).on('click', '.suer1', function() {
	$('.start').removeClass("gray")
	$('.clean').removeClass("gray")
	$('button').attr('disabled', false);
	$(".gray").attr('disabled', true)
	var d_startDate = $("input[name='d_startDate']").val()
	var d_endDate = $("input[name='d_endDate']").val()
	var d_jgData= $("input[name='d_jg']").val()
	d_startTime = d_startDate
	d_endTime = d_endDate
	d_jgTime = d_jgData
})
$(document).on('click', '.start', function() {
	if(d_jgTime == '') {
		alert("订单下载未设置间隔时间")
	}else {
		console.log(d_jgTime)
		Date.prototype.Format = function(fmt) {//日期格式 
	        var o = {
	            "M+" : this.getMonth()+1,                 //月份
	            "d+" : this.getDate(),                    //日
	            "h+" : this.getHours(),                   //小时
	            "m+" : this.getMinutes(),                 //分
	            "s+" : this.getSeconds(),                 //秒
	            "q+" : Math.floor((this.getMonth()+3)/3), //季度
	            "S"  : this.getMilliseconds()             //毫秒
	        };
	        if(/(y+)/.test(fmt))  {
	            fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
	        }
	        for(var k in o) {
	            if(new RegExp("("+ k +")").test(fmt)) {
	                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
	            }
	        }
	        return fmt;
	    };
	    function getTimeArr(begin,end,type,interval){  //获取两个时间相邻的数组  type:目前可传"yyyy-MM-dd hh" "yyyy-MM-dd" "yyyy-MM"格式的时间
	        var arr=[];
	        if(type=="yyyy-MM-dd hh"){//设置小时间隔
	            var beginDate=new Date(begin+" 00:00:00");
	            var endDate=new Date(end+" 00:00:00");
	            var msCount=interval*60*60*1000;  //1小时 2小时 3小时
	        }
	        var beginMs=beginDate.getTime();
	        var endMs=endDate.getTime();
	        for(var i=beginMs;i<=endMs;i+=msCount){
	            if(type=="yyyy-MM-dd hh"){
	                arr.push(new Date(i).Format(type)+":00:00");
	            }
	        }
	        return arr;
	    }
	    function getBeforMonth(date){
	        var lastMonth="";
	        var year=date.split("-")[0],month=date.split("-")[1];
	        if(month==1){
	            lastMonth=(year-1)+"-"+12;
	        }else{
	            lastMonth=year+"-"+add0(month-1);
	        }
	        return lastMonth;
	    }
	    const num = getTimeArr(d_startTime,d_endTime,'yyyy-MM-dd hh',d_jgTime)
		let index = 0;
	    var now1,now,arr;
		var num1 = []
		var blankings = $("input[name='d_qq']").val()
		window.d_blanking = blankings * 60000
		if(d_blanking == '') {
			d_blanking = 0
		}
		var count_d = function() {
			var startDate = num[index];
			var endDate = num[index + 1];
			console.log("开始" + " " + startDate)
			console.log("结束" + " " + endDate)
		    index ++
		    if(startDate == undefined || endDate == undefined) {
		    	clearInterval(d_timer)
		    	return;
		    }else {
		    	var listData = getJQAllData1()
				for(var i = 0; i < listData.length; i++) {
					var storeId = listData[i].shopId
					var shopName = listData[i].shopName
					var recentHours = listData[i].recentHours
					if(recentHours == 0) {
						continue
					} else {
						var savedata = {
							"reqHead": {
								"accNum": "root",
								"loginTime": ""
							},
							"reqBody": {
								"storeId": storeId,
								"startDate": startDate,
								"endDate": endDate,
							}
						};
						var saveData = JSON.stringify(savedata);
//						console.log(saveData)
						$.ajax({
							type: "post",
							contentType: 'application/json; charset=utf-8',
							url: url + '/mis/ec/platOrder/download',
							async: false,
							cache: true,
							data: saveData,
							dataType: 'json',
							success: function(data) {
								console.log(data)
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
				$("#message_jqgrids").jqGrid("clearGridData");
				$("#message_jqgrids").jqGrid("setGridParam", {
					data: num1
				}).trigger("reloadGrid");
		    }
		    return count_d;
		}
//		d_timer = setInterval(() => {
//			count()
//		}, d_blanking)
		d_timer = setInterval(count_d(),d_blanking);
	}

	
})

$(document).on('click', '.clean', function() {
//	window.location.reload()
	$("#message_jqgrids").jqGrid("clearGridData");
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

var r_startTime;
var r_endTime;
var r_jgTime;
var d_timer;
$(document).on('click', '.suer2', function() {
	$('.start_r').removeClass("gray")
	$('.clean_r').removeClass("gray")
	$('button').attr('disabled', false);
	$(".gray").attr('disabled', true)
	var r_startDate = $("input[name='t_startDate']").val()
	var r_endDate = $("input[name='t_endDate']").val()
	var t_jg = $("input[name='t_jg']").val()
	r_startTime = r_startDate
	r_endTime = r_endDate
	r_jgTime = t_jg
})
$(document).on('click', '.start_r', function() {
	if(r_jgTime == "") {
		alert("退款单下载未设置间隔时间")
	}else {
		console.log(r_jgTime)
		Date.prototype.Format = function(fmt) {//日期格式 
	        var o = {
	            "M+" : this.getMonth()+1,                 //月份
	            "d+" : this.getDate(),                    //日
	            "h+" : this.getHours(),                   //小时
	            "m+" : this.getMinutes(),                 //分
	            "s+" : this.getSeconds(),                 //秒
	            "q+" : Math.floor((this.getMonth()+3)/3), //季度
	            "S"  : this.getMilliseconds()             //毫秒
	        };
	        if(/(y+)/.test(fmt))  {
	            fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
	        }
	        for(var k in o) {
	            if(new RegExp("("+ k +")").test(fmt)) {
	                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
	            }
	        }
	        return fmt;
	    };
	    function getTimeArr(begin,end,type,interval){  //获取两个时间相邻的数组  type:目前可传"yyyy-MM-dd hh" "yyyy-MM-dd" "yyyy-MM"格式的时间
	        var arr=[];
	        if(type=="yyyy-MM-dd hh"){//设置小时间隔
	            var beginDate=new Date(begin+" 00:00");
	            var endDate=new Date(end+" 00:00");
	            var msCount=interval*60*60*1000;  //1小时 2小时 3小时
	        }
	        var beginMs=beginDate.getTime();
	        var endMs=endDate.getTime();
	        for(var i=beginMs;i<=endMs;i+=msCount){
	            if(type=="yyyy-MM-dd hh"){
	                arr.push(new Date(i).Format(type)+":00");
	            }
	        }
	        return arr;
	    }
	    function getBeforMonth(date){
	        var lastMonth="";
	        var year=date.split("-")[0],month=date.split("-")[1];
	        if(month==1){
	            lastMonth=(year-1)+"-"+12;
	        }else{
	            lastMonth=year+"-"+add0(month-1);
	        }
	        return lastMonth;
	    }
	    const num = getTimeArr(r_startTime,r_endTime,'yyyy-MM-dd hh',12)
	    let index = 0
	    var now1,now;
	    var blankings = $("input[name='t_qq']").val()
		window.r_blanking = blankings * 60000
		if(r_blanking == '') {
			r_blanking = 0
		}
		var count_r = function() {
			var startDate = num[index];
			var endDate = num[index + 1];
		    index ++
//			console.log("开始" + " " + startDate)
//			console.log("结束" + " " + endDate)
		    if(startDate == undefined || endDate == undefined) {
		    	clearInterval(r_timer)
		    	return;
		    }else {
				var listData = getJQAllData1()
				for(var i = 0; i < listData.length; i++) {
					var storeId = listData[i].shopId
					var shopName = listData[i].shopName
					var recentHours = listData[i].recentHours
					if(recentHours == 0) {
						continue
					} else {
						var savedata = {
							"reqHead": {
								"accNum": "root",
								"loginTime": ""
							},
							"reqBody": {
								"storeId": storeId,
								"startDate": startDate,
								"endDate": endDate,
								"ecOrderId": ''
							}
						};
						var saveData = JSON.stringify(savedata);
//						console.log(saveData)
						$.ajax({
							type: "post",
							contentType: 'application/json; charset=utf-8',
							url: url + '/mis/ec/refundOrder/download',
							async: false,
							cache: true,
							data: saveData,
							dataType: 'json',
							success: function(data) {
								console.log(data)
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
				$("#message_return_jqgrids").jqGrid("clearGridData");
				$("#message_return_jqgrids").jqGrid("setGridParam", {
					data: num2
				}).trigger("reloadGrid");
		    }
		    return count_r;
		}
//		r_timer = setInterval(() => {
//			
//		}, r_blanking)
		r_timer = setInterval(count_r(),r_blanking);
    }   
})

$(document).on('click', '.clean_r', function() {
//	window.location.reload()
	$("#message_return_jqgrids").jqGrid("clearGridData");
})
