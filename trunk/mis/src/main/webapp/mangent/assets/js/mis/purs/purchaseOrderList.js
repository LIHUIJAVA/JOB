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
	localStorage.removeItem("index")
	localStorage.removeItem("sortorder")
	allHeight();
	$("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['订单编码', '订单日期', '采购类型', '业务员', '供应商', '部门', '供应商订单号', '是否审核', '表头备注','表体备注', '存货编码', '存货名称', '规格型号', '主计量单位', '数量', '含税单价', '价税合计',
			'无税单价', '无税金额', '税率', '税额', '计划到货时间', '箱规', '箱数', '对应条形码', '主计量单位编码'
		],
		colModel: [{
				name: 'pursOrdrId',
				editable: true,
				align: 'center',
				sortable: true,
			},
			{
				name: 'pursOrdrDt',
				editable: true,
				align: 'center',
				sortable: true
			},
			{
				name: 'pursTypNm',
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
				name: 'memos',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'invtyEncd',
				editable: true,
				align: 'center',
				sortable: false

			},
			{
				name: 'invtyNm',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'spcModel',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'measrCorpNm',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'qty',
				editable: true,
				align: 'center',
				sortable: true
			},
			{
				name: 'cntnTaxUprc', //含税单价
				editable: false,
				align: 'center',
				sortable: true
			},
			{
				name: 'prcTaxSum', //价税合计
				editable: true,
				align: 'center',
				sortable: true,
			},
			{
				name: 'noTaxUprc', //无税单价
				editable: false,
				align: 'center',
				sortable: true
			},
			{
				name: 'noTaxAmt', //无税金额
				editable: false,
				align: 'center',
				sortable: true
			},
			{
				name: 'taxRate', //税率
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'taxAmt', //税额
				editable: false,
				align: 'center',
				sortable: true
			},
			{
				name: 'planToGdsDt',
				editable: true,
				align: 'center',
				sortable: true
			},
			{
				name: 'bxRule',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'bxQty',
				editable: true,
				align: 'center',
				sortable: true
			},
			{
				name: 'crspdBarCd',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'measrCorpId',
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
		caption: "采购订单列表", //表格的标题名字
		footerrow: true,
		gridComplete: function() { 
			$("#jqGrids").footerData('set', { 
				"pursOrdrId": "总计",
				"qty": tableSums.qty,
				"bxQty": tableSums.bxQty,
				"prcTaxSum": tableSums.prcTaxSum,
				"noTaxAmt": tableSums.noTaxAmt,
				"taxAmt": tableSums.taxAmt

			}          );    
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
			var index = localStorage.getItem("index")
			var sortorder = localStorage.getItem("sortorder")
			if(index == null) {
				var index = ''
			}
			if(sortorder == null) {
				var sortorder = 'desc'
			}
			search(index, sortorder)     
		},

		//双击弹出采购订单
		ondblClickRow: function(rowid) {
			if(orde == 1) {
				order(rowid);
			}
		},
		onSortCol: function(index, colindex, sortorder) {
			switch(index) {
				case "pursOrdrId":
					index = "po.purs_ordr_id"
					break;
				case "pursOrdrDt":
					index = "po.purs_ordr_dt"
					break;
				case "qty":
					index = "pos.qty"
					break;
				case "cntnTaxUprc":
					index = "pos.cntn_tax_uprc"
					break;
				case "prcTaxSum":
					index = "pos.prc_tax_sum"
					break;
				case "noTaxUprc":
					index = "pos.no_tax_uprc"
					break;
				case "noTaxAmt":
					index = "pos.no_tax_amt"
					break;
				case "taxAmt":
					index = "pos.tax_amt"
					break;
				case "planToGdsDt":
					index = "pos.plan_to_gds_dt"
					break;
				case "bxQty":
					index = "pos.bx_qty"
					break;
				case "pursOrdrId":
					index = "po.purs_ordr_id"
					break;
			}
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

	var invtyEncd = $("#invtyEncd").val();
	var pursOrdrId = $(".pursOrdrId").val();
	var provrId = $("#provrId").val();
	var pursOrdrDt1 = $(".pursOrdrDt1").val();
	var pursOrdrDt2 = $(".pursOrdrDt2").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var isNtChk = $("#isNtChk").val();
	var deptId = $("#deptId").val();
	var invtyCd = $(".invtyCd").val();
	var accNum = $("#user").val();
	var provrOrdrNum = $(".supplierNumber").val();
	var memo = $("#memo").val();
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": invtyEncd,
			"pursOrdrId": pursOrdrId,
			"provrId": provrId,
			"pursOrdrDt1": pursOrdrDt1,
			"invtyClsEncd": invtyClsEncd,
			"pursOrdrDt2": pursOrdrDt2,
			"isNtChk": isNtChk,
			"deptId": deptId,
			"accNum": accNum,
			"provrOrdrNum": provrOrdrNum,
			"invtyCd": invtyCd,
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
		url: url + '/mis/purc/PursOrdr/queryPursOrdrListOrderBy',
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
			for(var i = 0; i < list.length; i++) {
				if(list[i].isNtChk == 0) {
					list[i].isNtChk = "否"
				} else if(list[i].isNtChk == 1) {
					list[i].isNtChk = "是"
				}
			}
			mydata.rows = list;
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
	localStorage.setItem("pursOrdrId", rowDatas.pursOrdrId);
	localStorage.setItem("isNtChk", rowDatas.isNtChk);
	localStorage.setItem("ntChkNo", "0");
	window.open("../../Components/purs/purchaseOrder.html?1", 'height=700, width=1000, top=200, left=300,location=no, status=no');
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
		obj.pursOrdrId = $("#jqGrids").getCell(ids[i], "pursOrdrId").toString();
		obj.isNtChk = x;
		//建一个数组，把单个对象添加到数组中
		rowData[i] = obj;
	}

	var result = [];
	var obj = {};
	for(var i = 0; i < rowData.length; i++) {
		if(!obj[rowData[i].pursOrdrId]) {
			result.push(rowData[i]);
			obj[rowData[i].pursOrdrId] = true;
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
			url: url + '/mis/purc/PursOrdr/updatePursOrdrIsNtChk',
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
			var data = $("#jqGrids").getCell(ids[i], "pursOrdrId");
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
			var pursOrdrId = result.toString();
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"pursOrdrId": pursOrdrId,
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/purc/PursOrdr/deletePursOrdrList',
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
function importExcel(x) {
	var files = $("#FileUpload").val()
	var fileObj = document.getElementById("FileUpload").files[0];
	var formFile = new FormData();
	formFile.append("action", "UploadVMKImagePath");
	formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
	var data = formFile;
	if(files != "") {
		$.ajax({
			type: 'post',
			url: url + x,
			headers:{
				"accNum": loginAccNum,
				'loginTime': loginTime,
			},
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
}

//导入
$(function() {
	$(".dropdown-menu .li2").click(function() {
		var x = "/mis/purc/PursOrdr/uploadPursOrdrFile"
		importExcel(x)
	});
	$(".dropdown-menu .li1").click(function() {
		var x = "/mis/purc/PursOrdr/uploadPursOrdrFileU8"
		importExcel(x)
	});
});

//导出
$(document).on('click', '.exportExcel', function() {
	var invtyEncd = $("#invtyEncd").val();
	var pursOrdrId = $(".pursOrdrId").val();
	var provrId = $("#provrId").val();
	var pursOrdrDt1 = $(".pursOrdrDt1").val();
	var pursOrdrDt2 = $(".pursOrdrDt2").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var isNtChk = $("#isNtChk").val();
	var deptId = $("#deptId").val();
	var invtyCd = $(".invtyCd").val();
	var accNum = $("#user").val();
	var provrOrdrNum = $(".supplierNumber").val();
	var memo = $("#memo").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": invtyEncd,
			"pursOrdrId": pursOrdrId,
			"provrId": provrId,
			"pursOrdrDt1": pursOrdrDt1,
			"invtyClsEncd": invtyClsEncd,
			"pursOrdrDt2": pursOrdrDt2,
			"isNtChk": isNtChk,
			"deptId": deptId,
			"accNum": accNum,
			"provrOrdrNum": provrOrdrNum,
			"invtyCd": invtyCd,
			"memo":memo
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/purc/PursOrdr/printingPursOrdrList',
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
			var execlName = '采购订单列表'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
})