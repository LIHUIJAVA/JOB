var page = 1;
var rowNum;

//表格初始化
$(function() {
	$(".sure").removeClass("gray")
	$('.searcher').removeClass("gray")
	$('.cancel').removeClass("gray")
	$('button').attr('disabled', false);
	$(".gray").attr('disabled', true);
	$("#jqGrids_list").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['订单编码', '订单日期', '采购类型', '业务员id', '业务员', '供应商id', '供应商', '预付比率（%）', '预付金额', '部门id', '部门', '供应商订单号', '备注'],
		colModel: [{
				name: 'pursOrdrId',
				editable: false,
				align: 'center'

			},
			{
				name: 'pursOrdrDt',
				editable: false,
				align: 'center'

			},
			{
				name: 'pursTypNm',
				editable: false,
				align: 'center',
			},
			{
				name: 'accNum',
				editable: false,
				align: 'center',
				hidden: true
			},
			{
				name: 'userName',
				editable: false,
				align: 'center'
			},
			{
				name: 'provrId',
				editable: false,
				align: 'center',
				hidden: true
			},
			{
				name: 'provrNm',
				editable: false,
				align: 'center'
			},
			{
				name: 'adRatio',
				editable: true,
				align: 'center'
			},
			{
				name: 'adAmt',
				editable: true,
				align: 'center',
			},
			{
				name: 'deptId',
				editable: false,
				align: 'center',
				hidden: true
			},
			{
				name: 'deptName',
				editable: false,
				align: 'center'
			},
			{
				name: 'provrOrdrNum',
				editable: false,
				align: 'center',
			},
			{
				name: 'memo',
				editable: false,
				align: 'center'
			},

		],
		autowidth: true,
		rownumbers: true,
		autoScroll: true,
		shrinkToFit: false,
		loadonce: false,
		height: 150,
		forceFit: true,
		rowNum: 500,
		cellEdit: true,
		cellsubmit: "clientArray",
		rowList: [500, 1000, 3000, 5000],
		pager: '#jqGridPager_list', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true,
		caption: "采购单列表", //表格的标题名字
		onSelectRow: function() {
			deteil_search();
		},
		afterSaveCell: function(rowid, cellname, val, iRow, iCol) {
			//获取商品信息
			if(cellname == "adRatio" || cellname == "adAmt") {
				adRatio(rowid, cellname, val);
			}
			gridSelected()
		},
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#jqGrids_list").getGridParam('records'); //获取返回的记录数
			page = $("#jqGrids_list").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#jqGrids_list").getGridParam('rowNum');

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
			searchList()
		},
	})
	$("#jqGrids_list_cb").hide()
})

function gridSelected() {
	var rowIds = $("#deteil_list").jqGrid('getDataIDs');
	for(var i = 0; i < rowIds.length; i++) {
		var curRowData = $("#deteil_list").jqGrid('getRowData', rowIds[i]);
		if(curRowData.applPayAmt) {
			$("#deteil_list").setSelection(rowIds[i], false);
		}
	}
}

function adRatio(rowid, cellname, val) {
	var adRatio, adAmt;
	if(cellname == "adRatio") {
		adAmt = val * 0.01 * unApplPayAmt1; //预付金额
		$("#jqGrids_list").setRowData(rowid, {
			adAmt: adAmt,
		});
	} else if(cellname == "adAmt") {
		adRatio = parseFloat(val / unApplPayAmt1) * 100;
		$("#jqGrids_list").setRowData(rowid, {
			adRatio: adRatio,
		});
	}
	aa(adRatio)

}

function aa(adRatio) {
	var rowid = $("#jqGrids_list").jqGrid('getGridParam', 'selrow');
	var rowDatas = $("#jqGrids_list").jqGrid('getRowData', rowid);
	adAmt = parseFloat(rowDatas.adAmt)
	var add = 0;
	var j = 0;
	var chae = 0;
	var payAmt = $("#deteil_list").getCol("unApplPayAmt", false); //可申请金额
	for(var i = 0; i < payAmt.length; i++) {
		add += parseFloat(payAmt[i]);
		if(adRatio == 100) {
			$("#deteil_list").setRowData(i + 1, {
				applPayAmt: payAmt[i],
			});
		}
		if(j == 0 && adAmt < payAmt[0]) {
			var aa = chae - adAmt;
			$("#deteil_list").setRowData(j + 1, {
				applPayAmt: -aa,
			});
		} else if(adAmt > add) {
			chae += parseFloat(payAmt[i]);
			$("#deteil_list").setRowData(i + 1, {
				applPayAmt: payAmt[i],
			});
			j = i
		}
	}
	var aa = chae - adAmt;
	if(j != 0 || j == 0 && adAmt > payAmt[0]) {
		$("#deteil_list").setRowData(j + 2, {
			applPayAmt: -aa,
		});
	}

	$("#deteil_list").footerData('set', { 
		"applPayAmt": precision(adAmt,2)
	}          );
}

function allHeight1() {
	$(window).resize(function() {
		$("#deteil_list").setGridHeight($(window).height() - $('#purs_list .purchaseTit').outerHeight(true) - $('#purs_list .order-title').outerHeight(true) - 119 - $("#gview_jqGrids_list").height());

	});
	var height1 = $('#purs_list .purchaseTit').outerHeight(true);
	var height2 = $('#purs_list .order-title').outerHeight(true);
	if(acc == 1) {
		height2 = height2
	} else if(acc == 2) {
		height2 = 0
	}
	var height3 = $(window).height() // 浏览器当前窗口可视区域高度
	var height4 = $("#gview_jqGrids_list").height() // 浏览器当前窗口可视区域高度
	window.height = height3 - height1 - height2 - 119 - height4;
}

var unApplPayAmt1 = 0;
$(function() {
	allHeight1()
	$("#deteil_list").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ["序号", '订单编码', '存货编码', '存货名称', '规格型号', '主计量单位', '数量', '可申请金额', '本次申请金额', '含税单价', '价税合计',
			 '预计付款时间','无税单价', '无税金额', '税率', '税额', '计划到货时间', '箱规', '箱数', '对应条形码', '备注', '主计量单位编码','保质期'
		],
		colModel: [{
				name: 'ordrNum',
				editable: false,
				align: 'center',
				hidden: true
			},
			{
				name: 'pursOrdrId',
				editable: false,
				align: 'center',
				sortable: false

			}, {
				name: 'invtyEncd',
				editable: false,
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
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'unApplPayAmt',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'applPayAmt', //本次申请金额
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'cntnTaxUprc', //含税单价
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'prcTaxSum', //价税合计
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'planPayDt',
				editable: true,
				align: 'center',
				editoptions: {
					dataInit: function(element) {
						$(element).datepicker({
							dateFormat: 'yy-mm-dd'
						})
					}
				},
				sortable: false
			},
			{
				name: 'noTaxUprc', //无税单价
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'noTaxAmt', //无税金额
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'taxRate', //税率
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'taxAmt', //税额
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'planToGdsDt',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'bxRule',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'bxQty',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'crspdBarCd',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'memo',
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
			{
				name: 'baoZhiQi',
				editable: false,
				align: 'center',
				sortable: false
			},
		],
		cellEdit: true,
		cellsubmit: "clientArray",
		autowidth: true,
		rownumbers: true,
		autoScroll: true,
		shrinkToFit: false,
		loadonce: false,
		height: 150,
		forceFit: true,
		rowNum: 99999,
		altclass: true,
		pgbuttons: false,
		pginput: false,
		pager: '#deteilPager_list', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "采购单明细", //表格的标题名字
		multiselect: true, //复选框
		footerrow: true,
		gridComplete: function() { 
			sumAdd()
		},
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#deteil_list").getGridParam('records'); //获取返回的记录数
			page = $("#deteil_list").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#deteil_list").getGridParam('rowNum');

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
			searchList()
		},
	})
})

function sumAdd() {
	var qty = 0;
	var prc_tax_sum = 0;
	var no_tax_amt = 0;
	var unApplPayAmt = 0;
	var ids = $("#deteil_list").getDataIDs();
	var rowData = $("#deteil_list").getRowData(ids[i]);
	for(var i = 0; i < ids.length; i++) {
		qty += parseFloat($("#deteil_list").getCell(ids[i], "qty"));
		prc_tax_sum += parseFloat($("#deteil_list").getCell(ids[i], "prcTaxSum"));
		no_tax_amt += parseFloat($("#deteil_list").getCell(ids[i], "noTaxAmt"));
		unApplPayAmt += parseFloat($("#deteil_list").getCell(ids[i], "unApplPayAmt"));
	}; 
	unApplPayAmt1=unApplPayAmt;
	if(isNaN(qty)) {
		qty = 0
	}
	if(isNaN(prc_tax_sum)) {
		prc_tax_sum = 0
	}
	if(isNaN(no_tax_amt)) {
		no_tax_amt = 0
	}
	if(isNaN(unApplPayAmt)) {
		unApplPayAmt = 0
	}
	unApplPayAmt = precision(unApplPayAmt,2)
	prc_tax_sum = precision(prc_tax_sum,2)
	no_tax_amt = precision(no_tax_amt,2)
	$("#deteil_list").footerData('set', { 
		"pursOrdrId": "本页合计",
		"qty": qty,
		"prcTaxSum": prc_tax_sum,
		"noTaxAmt": no_tax_amt,
		"unApplPayAmt": unApplPayAmt

	}          ); 
}

//查询按钮
$(document).on('click', '.searcher', function() {
	searchList()
})

function searchList() {
	var rowNum1 = $("#jqGridPager_list td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var myDate = {};

	var invtyEncd = $(".invtyEncd").val();
	var pursOrdrId = $(".pursOrdrId").val();
	var provrId = $("#provrId1").val();
	var pursOrdrDt1 = $(".pursOrdrDt1").val();
	var pursOrdrDt2 = $(".pursOrdrDt2").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var accNum = $("#fuze").val();
	var provrOrdrNum = $(".supplierNumber").val();
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": invtyEncd,
			"pursOrdrId": pursOrdrId,
			"provrId": provrId,
			"pursOrdrDt1": pursOrdrDt1,
			"pursOrdrDt2": pursOrdrDt2,
			"invtyClsEncd": invtyClsEncd,
			"isNtChk": 1,
			"accNum": accNum,
			"unApplPayAmt": 1,
			"provrOrdrNum": provrOrdrNum,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/purc/PursOrdr/queryPursOrdrLists',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			var list = data.respBody.list;
			for(var i = 0; i < list.length; i++) {
				if(list[i].isNtChk == 0) {
					list[i].isNtChk = "否"
				} else if(list[i].isNtChk == 1) {
					list[i].isNtChk = "是"
				}
			}
			myDate = list;
			var mydata = {};
			mydata.rows = myDate;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#jqGrids_list").jqGrid("clearGridData");
			$("#jqGrids_list").jqGrid("setGridParam", {
				data: mydata.rows,
				jsonReader: {
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
			alert("查询失败")
		}
	});
}

function deteil() {
	var ids = $('#jqGrids_list').jqGrid('getGridParam', 'selarrrow');
	var rowData = [];
	for(var i = 0; i < ids.length; i++) {
		var data = $("#jqGrids_list").getCell(ids[i], "pursOrdrId");
		//建一个数组，把选中行的id添加到这个数组中去。
		rowData[i] = data;
	}
	var pursOrdrId = rowData.toString();
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"pursOrdrId": pursOrdrId,
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/purc/PursOrdr/selectUnApplPayQtyByPursOrdrId',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			var list = data.respBody.list;
			for(var i = 0; i < list.length; i++) {
				if(list[i].isNtChk == 0) {
					list[i].isNtChk = "否"
				} else if(list[i].isNtChk == 1) {
					list[i].isNtChk = "是"
				}
			}
			$("#deteil_list").jqGrid("clearGridData");
			$("#deteil_list").jqGrid("setGridParam", {
				datatype: 'local',
				data: list, //newData是符合格式要求的重新加载的数据
				page: 1 //哪一页的值
			}).trigger("reloadGrid");
		},
		error: function() {
			//			alert("查询失败")
		}
	});
}

//$(function() {
//	$("#last_jqGridPager_list").after('<input id="_input" type="text" value="10"/>')
//})