var page = 1;
var rowNum;

//页面初始化
$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	pageInit();
	loadLocalData(page)
});

function pageInit() {
	jQuery("#jqgrids").jqGrid({
		datatype: 'local',
		colNames: ['orderNum', '会计月份', '开始日期', '结束日期', '是否结账', ], //jqGrid的列显示名字
		colModel: [{
				name: "orderNum",
				align: "center",
				editable: true,
				sortable: false,
				hidden: true
			},
			{
				name: "acctMth",
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: "mthBgn",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: "mthEnd",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'isMthEndStl',
				align: "center",
				editable: true,
				sortable: false
			}
		],
		height: 480,
		autowidth: true,
		viewrecords: true,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 12,
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		caption: "月末结账", //表格的标题名字
	})
}

function loadLocalData(page) {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var data = {
		"reqHead": reqhead,
		"reqBody": {}
	};
	var postData = JSON.stringify(data);
	$.ajax({
		type: "post",
		url: url + '/mis/account/form/final/dealMonthList',
		async: true,
		data: postData,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");
	
		},
		//结束加载动画
		complete: function() {
			$(".zhezhao").css("display", "none");
			$("#loader").css("display", "none");
		},
		success: function(data) {
			list = data.respBody.list
			for(var i = 0; i < list.length; i++) {
				if(list[i].isMthEndStl == 0) {
					list[i].isMthEndStl = "否"
				} else if(list[i].isMthEndStl == 1) {
					list[i].isMthEndStl = "是"
				}
			}
			var mydata = {};
			mydata.rows = list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#jqgrids").jqGrid("clearGridData");
			$("#jqgrids").jqGrid("setGridParam", {
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
		error: function(error) {
			alert('error')
		}
	})
}

//结账
$(function() {
	$(".deal").click(function() {
		var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
		var rowDatas = $("#jqgrids").jqGrid('getRowData', gr); //获取行数据
		var orderNum = rowDatas.orderNum;
		if(orderNum) {
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"orderNum": orderNum,
				}
			};
			var postData = JSON.stringify(data);
			$.ajax({
				type: "post",
				url: url + '/mis/account/form/final/dealMonth',
				async: true,
				data: postData,
				dataType: 'json',
				contentType: 'application/json; charset=utf-8',
				//开始加载动画  添加到ajax里面
				beforeSend: function() {
					$(".zhezhao").css("display", "block");
					$("#loader").css("display", "block");

				},
				//结束加载动画
				complete: function() {
					$(".zhezhao").css("display", "none");
					$("#loader").css("display", "none");
				},
				success: function(data) {
					alert(data.respHead.message)
					if(data.respHead.isSuccess == true) {
						loadLocalData(page)
					}
				},
				error: function(data) {
					alert("error")
				}
			})
		}
	})
})
//取消结账
$(function() {
	$(".cancelDeal").click(function() {
		var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
		var rowDatas = $("#jqgrids").jqGrid('getRowData', gr); //获取行数据
		var orderNum = rowDatas.orderNum;
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				"orderNum": orderNum,
			}
		};
		var postData = JSON.stringify(data);
		$.ajax({
			type: "post",
			url: url + '/mis/account/form/final/backDealMonth',
			async: true,
			data: postData,
			dataType: 'json',
			contentType: 'application/json; charset=utf-8',
			success: function(data) {
				alert(data.respHead.message)
				if(data.respHead.isSuccess == true) {
					loadLocalData(page)
				}
			},
			//开始加载动画  添加到ajax里面
			beforeSend: function() {
				$(".zhezhao").css("display", "block");
				$("#loader").css("display", "block");

			},
			//结束加载动画
			complete: function() {
				$(".zhezhao").css("display", "none");
				$("#loader").css("display", "none");
			},
			error: function(data) {
				alert("error")
			}
		})
	})
})

//一键设置
$(function() {
	$(".set").click(function() {
		var accNum = localStorage.loginName;
		var loginTime = localStorage.loginTime;
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				"accNum": accNum,
				"loginTime": loginTime,
			}
		};
		var postData = JSON.stringify(data);
		$.ajax({
			type: "post",
			url: url + '/mis/account/form/final/settingDealMonth',
			async: true,
			data: postData,
			dataType: 'json',
			contentType: 'application/json; charset=utf-8',
			//开始加载动画  添加到ajax里面
			beforeSend: function() {
				$(".zhezhao").css("display", "block");
				$("#loader").css("display", "block");

			},
			//结束加载动画
			complete: function() {
				$(".zhezhao").css("display", "none");
				$("#loader").css("display", "none");
			},
			success: function(data) {
				alert(data.respHead.message)
				if(data.respHead.isSuccess == true) {
					window.location.reload()
				}
			},
			error: function(data) {
				alert("error")
			}
		})
	})
})