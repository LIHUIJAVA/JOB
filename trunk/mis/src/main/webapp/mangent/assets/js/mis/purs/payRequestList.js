$(function() {
	//加载动画html
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div> ");
	$("#mengban").addClass("zhezhao");
})
var page = 1;
var rowNum;
var orde = 0;
var tableSums = {};
//表格初始化
$(function() {
	allHeight();
	$("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['订单编码', '订单日期', '单据类型', '业务员', '供应商', '部门', '供应商订单号', '是否审核', '备注', '来源单据号', '数量', '源单本次申请金额', '金额', '实际付款日期', '预计付款日期', '行关闭人'],
		colModel: [{
				name: 'payApplId',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'payApplDt',
				editable: true,
				align: 'center',
				sorttype: "date",
				sortable: true
			},
			{
				name: 'formTypName',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'userName',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'provrNm',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'deptName',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'provrOrdrNum',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'isNtChk',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'memo',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'srcFormNum',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'qty',
				editable: false,
				align: 'center',
				sortable: true,
			},
			{
				name: 'orgnlSnglCurrApplAmt',
				editable: false,
				align: 'center',
				sortable: true,

			},
			{
				name: 'amt',
				editable: true,
				align: 'center',
				sortable: true,
			},
			{
				name: 'actlPayTm',
				editable: false,
				align: 'center',
				sortable: true,
			},
			{
				name: 'expctPayDt',
				editable: false,
				align: 'center',
				sortable: true,
			},
			{
				name: 'lineClosPers',
				editable: false,
				align: 'center',
				hidden: true,
				sortable: false
			},
		],
		autowidth: true,
		autoScroll: true,
		shrinkToFit: false,
		rownumbers: true,
		loadonce: true,
		sortable: true,
		forceFit: true,
		rowNum: 500,
		rowList: [500, 1000, 3000, 5000],
		height: height,
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		//		multiboxonly: true,
		footerrow: true,
		caption: "付款申请单列表", //表格的标题名字
		gridComplete: function() {
			if(tableSums) {
				$("#jqGrids").footerData('set', { 
					"payApplId": "总计",
					"qty": tableSums.qty,
					"amt": tableSums.amt,
					"orgnlSnglCurrApplAmt": tableSums.orgnlSnglCurrApplAmt,
				}          );
			}

		},
		onPaging: function(pageBtn) {
			var records = $("#jqGrids").getGridParam('records'); //获取返回的记录数
			page = $("#jqGrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#jqGrids").getGridParam('rowNum'); //获取显示配置记录数量

			rowNum = parseInt(rowNum1)
			var total = Math.ceil(records / rowNum); //$("#jqGrid").getGridParam('total');//获取总页数
			if(pageBtn === "next" && page < total) {
				page = parseInt(page) + 1;
			}
			if(pageBtn === "prev" && page > 1) {
				page = parseInt(page) - 1;
			}
			if(pageBtn === "last") {
				page = total;
			}
			if(pageBtn === "first") {
				page = 1;
			}
			search(index, sortorder)
		},
		ondblClickRow: function(rowid) {
			if(orde == 1) {
				order(rowid);
			}
		},
		onSortCol: function(index, colindex, sortorder) {
			switch(index) {
				case "payApplDt":
					index = "pay_appl_form.pay_appl_dt"
					break;
				case "qty":
					index = "qty"
					break;
				case "orgnlSnglCurrApplAmt":
					index = "orgnl_sngl_curr_appl_amt"
					break;
				case "amt":
					index = "amt"
					break;
				case "actlPayTm":
					index = "pay_appl_form_sub.actl_pay_tm"
					break;
				case "expctPayDt":
					index = "pay_appl_form_sub.expct_pay_dt"
					break;
			}
			window.index = index;
			window.sortorder = sortorder;
			search(index, sortorder)      
		}
	})
})

//查询按钮
$(document).on('click', '.search', function() {
	var index = ''
	var sortorder = 'desc'
	search(index, sortorder)

})

function search(index, sortorder) {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	orde = 1;
	var myDate = {};
	var payApplId = $(".payApplId").val();
	var provrId = $("#provrId").val();
	var deptId = $("#deptId").val();
	var accNum = $("#user").val();
	var provrOrdrNum = $(".supplierNumber").val();
	var payApplDt1 = $(".payApplDt1").val();
	var payApplDt2 = $(".payApplDt2").val();
	var isNtChk = $("#isNtChk").val();
	var memo = $("#memo").val();
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"payApplId": payApplId,
			"provrId": provrId,
			"deptId": deptId,
			"accNum": accNum,
			"provrOrdrNum": provrOrdrNum,
			"payApplDt1": payApplDt1,
			"payApplDt2": payApplDt2,
			"isNtChk":isNtChk,
			"memo":memo,
			"sort": index,
			"sortOrder": sortorder,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/purc/PayApplForm/queryPayApplFormListOrderBy',
		async: true,
		data: saveData,
		dataType: 'json',
		//开始加载动画
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
			tableSums = data.respBody.tableSums;
			var mydata = {};
			var list = data.respBody.list;
			if(list) {
				for(var i = 0; i < list.length; i++) {
					if(list[i].isNtChk == 0) {
						list[i].isNtChk = "否"
					} else if(list[i].isNtChk == 1) {
						list[i].isNtChk = "是"
					}
				}
			}
			myDate = list;
			mydata.rows = myDate;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#jqGrids").jqGrid("clearGridData");
			$("#jqGrids").jqGrid("setGridParam", {
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
			alert("error")
		}
	});
}

function order(rowid) {
	//获得行数据
	var rowDatas = $("#jqGrids").jqGrid('getRowData', rowid);
	localStorage.setItem("payApplId", rowDatas.payApplId);
	localStorage.setItem("isNtChk", rowDatas.isNtChk);
	window.open("../../Components/purs/payRequest.html?1", 'height=700, width=1000, top=200, left=300,location=no, status=no');
}

function ntChk(x) {
	//获得选中行的行号
	var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
	//获取选择行的provrId
	//对象数组
	var rowData = [];
	for(var i = 0; i < ids.length; i++) {
		var obj = {}; //对象
		//选中行的id
		//把选中行的i添加到数据对象中
		obj.payApplId = $("#jqGrids").getCell(ids[i], "payApplId").toString();
		obj.isNtChk = x;
		//建一个数组，把单个对象添加到数组中
		rowData[i] = obj;
	}
	var result = [];
	var obj = {};
	for(var i = 0; i < rowData.length; i++) {
		if(!obj[rowData[i].payApplId]) {
			result.push(rowData[i]);
			obj[rowData[i].payApplId] = true;
		}
	}
	if(result.length == 0) {
		alert("请选择单据!")
	} else {
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				"list": result
			}
		};
		var Data = JSON.stringify(data);
		$.ajax({
			url: url + '/mis/purc/PayApplForm/updatePayApplFormIsNtChk',
			type: 'post',
			data: Data,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			success: function(data) {
				alert(data.respHead.message);
				if(data.respHead.isSuccess == true) {
					search()
				}
			},
			error: function() {
				alert("审核失败")
			},
			//开始加载动画
			beforeSend: function() {
				$(".zhezhao").css("display", "block");
				$("#loader").css("display", "block");

			},
			//结束加载动画
			complete: function() {
				$(".zhezhao").css("display", "none");
				$("#loader").css("display", "none");
			},
		})
	}
}

//审核与弃审
$(function() {
	var isclick = true;
	//审核
	$(".toExamine").click(function() {
		if(isclick) {
			isclick = false;
			ntChk(1);
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	});
	//弃审
	$(".noTo").click(function() {
		if(isclick) {
			isclick = false;
			ntChk(0);
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	})
})

//删除
$(function() {
	$(".delOrder").click(function() {
		//获得选中行的行号
		var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
		//获取选择行的provrId
		var rowData = [];
		for(var i = 0; i < ids.length; i++) {
			var gr = $("#jqGrids").getGridParam('selrow');
			//选中行的id
			var data = $("#jqGrids").getCell(ids[i], "payApplId");
			//建一个数组，把选中行的id添加到这个数组中去。
			rowData[i] = data;
		}
		var result = [];
		var obj = {};
		for(var i = 0; i < rowData.length; i++) {
			if(!obj[rowData[i]]) {
				result.push(rowData[i]);
				obj[rowData[i]] = true;
			}
		}
		if(result.length == 0) {
			alert("请选择单据!")
		} else if(confirm("确定删除？")) {
			var payApplId = result.toString();
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"payApplId": payApplId,
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/purc/PayApplForm/deleteEntrsAgnAdj',
				type: 'post',
				data: Data,
				dataType: 'json',
				async: true,
				contentType: 'application/json;charset=utf-8',
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
					alert(data.respHead.message);
					if(data.respHead.isSuccess == true) {
						search()
					}
				},
				error: function() {
					alert("删除失败")
				}
			})
		}
	})
})

//$(function() {
//	$("#last_jqGridPager").after('<input id="_input" type="text" value="500"/>')
//})

//导入
$(function() {
	$(".importExcel").click(function() {
		var files = $("#FileUpload").val()
		var fileObj = document.getElementById("FileUpload").files[0];
		var formFile = new FormData();
		formFile.append("action", "UploadVMKImagePath");
		formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
		var data = formFile;
		if(files != "") {
			$.ajax({
				type: 'post',
				url: url + "/mis/purc/PursOrdr/uploadPursOrdrFile",
				data: data,
				dataType: "json",
				cache: false, //上传文件无需缓存
				processData: false, //用于对data参数进行序列化处理 这里必须false
				contentType: false, //必须
				success: function(data) {
					alert(data.respHead.message)
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
			});
		} else {
			alert("请选择文件")
		}
	});
});

var arr = [];
var obj = {}
//导出
$(document).on('click', '.exportExcel', function() {
	var payApplId = $(".payApplId").val();
	var provrId = $("#provrId").val();
	var deptId = $("#deptId").val();
	var accNum = $("#user").val();
	var provrOrdrNum = $(".supplierNumber").val();
	var payApplDt1 = $(".payApplDt1").val();
	var payApplDt2 = $(".payApplDt2").val();
	var isNtChk = $("#isNtChk").val();
	var memo = $("#memo").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"payApplId": payApplId,
			"provrId": provrId,
			"deptId": deptId,
			"accNum": accNum,
			"provrOrdrNum": provrOrdrNum,
			"payApplDt1": payApplDt1,
			"payApplDt2": payApplDt2,
			"isNtChk":isNtChk,
			"memo":memo
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/purc/PayApplForm/printPayApplFormList',
		type: 'post',
		data: Data,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		beforeSend: function() {
			$("#mengban").css("display", "block");
			$("#loader").css("display", "block");
		},
		complete: function() {
			$("#mengban").css("display", "none");
			$("#loader").css("display", "none");
		},
		success: function(data) {
			var list = data.respBody.list;
			var execlName = '付款申请单列表'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
})

//推送
$(function() {
	$(".push").click(function() {
		//获得选中行的行号
		var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
		//获取选择行的provrId
		var rowData = [];
		for(var i = 0; i < ids.length; i++) {
			var gr = $("#jqGrids").getGridParam('selrow');
			//选中行的id
			var data = $("#jqGrids").getCell(ids[i], "payApplId");
			//建一个数组，把选中行的id添加到这个数组中去。
			rowData[i] = data;
		}
		var result = [];
		var obj = {};
		for(var i = 0; i < rowData.length; i++) {
			if(!obj[rowData[i]]) {
				result.push(rowData[i]);
				obj[rowData[i]] = true;
			}
		}
		if(result.length == 0) {
			alert("请选择单据!")
		} else{
			var payApplId = result.toString();
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"ids": payApplId,
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/purc/PayApplForm/pushToU8',
				type: 'post',
				data: Data,
				dataType: 'json',
				async: true,
				contentType: 'application/json;charset=utf-8',
				success: function(data) {
					if(data.respHead.isSuccess == true) {
						alert(data.respHead.message);
					}
				},
				error: function() {
					alert("推送失败")
				}
			})
		}
	})
})