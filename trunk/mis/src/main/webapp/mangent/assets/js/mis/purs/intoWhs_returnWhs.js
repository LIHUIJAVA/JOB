
var page = 1;
var rowNum;

//表格初始化
$(function() {
	$("#jqGrids_list").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['入库单号', '入库日期', '采购类型', '业务员Id', '业务员', '供应商id','供应商','部门id', '部门',
			'供应商订单号', '备注', '订单号', '到货单号',
		],
		colModel: [{
				name: 'intoWhsSnglId',
				editable: false,
				align: 'center',
				sortable: false,
			},
			{
				name: 'intoWhsDt',
				editable: false,
				align: 'center'

			},
			{
				name: 'pursTypNm',
				editable: false,
				align: 'center'

			},
			{
				name: 'accNum',
				editable: false,
				align: 'center',
				hidden:true
			},
			{
				name: 'userName',
				editable: false,
				align: 'center',
			},
			{
				name: 'provrId',
				editable: false,
				align: 'center',
				hidden:true
			},
			{
				name: 'provrNm',
				editable: false,
				align: 'center'
			},
			{
				name: 'deptId',
				editable: false,
				align: 'center',
				hidden:true
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
			{
				name: 'pursOrdrId',
				editable: false,
				align: 'center'
			},
			{
				name: 'toGdsSnglId',
				editable: false,
				align: 'center'
			}
		],
		autowidth: true,
		rownumbers: true,
		height: 150,
		autoScroll:true,
		shrinkToFit:false,
		loadonce: false,
		forceFit: true,
		rowNum: 500,
		rowList: [500, 1000, 3000, 5000],
		pager: '#jqGridPager_list', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
//		multiboxonly: true,
		caption: "采购入库(退货)单主表", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#jqGrids_list").getGridParam('records'); //获取返回的记录数
			page = $("#jqGrids_list").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#jqGrids_list").getGridParam('rowNum'); //获取显示配置记录数量

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
			into_search();
		},
		onSelectRow: function() {
			deteil_search();
		},
	})
	$("#jqGrids_list_cb").hide()
})
$(function() {
	$("#deteil_list").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ["序号",'仓库编码', '仓库名称', '存货编码', '存货名称', '规格型号', '主计量单位', '编码', '数量','未退货数量', '含税单价', '价税合计',
			'无税单价', '无税金额', '税率', '税额', '箱规', '箱数', '对应条形码', '批次', '生产日期', '失效日期', '保质期', '国际批次', '备注', '是否退货'
		],
		colModel: [{
				name: 'ordrNum',
				editable: true,
				align: 'center',
				hidden:true
			},
			{
				name: 'whsEncd',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'whsNm',
				editable: false,
				align: 'center',
				sortable: false,
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
				name: 'measrCorpId',
				editable: false,
				align: 'center',
				hidden: true,
				sortable: false
			},
			{
				name: 'qty',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'returnQty',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'cntnTaxUprc',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'prcTaxSum',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'noTaxUprc',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'noTaxAmt',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'taxRate',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'taxAmt',
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
				editable: true,
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
				name: 'batNum',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'prdcDt', //生产日期
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'invldtnDt', //失效日期
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'baoZhiQi',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'intlBat', //国际批次
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
				name: 'isNtRtnGoods',
				editable: true,
				align: 'center',
				hidden: true,
				sortable: false
			},
		],
		autowidth: true,
		rownumbers: true,
		loadonce: false,
		height: 200,
		autoScroll:true,
		shrinkToFit:false,
		forceFit: true,
		rowNum: 99999,
		altclass: true,
		pgbuttons: false,
		pginput: false,
		pager: '#deteilPager_list', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "入库单明细", //表格的标题名字
		multiselect: true, //复选框
		footerrow: true,
		gridComplete: function() { 
			var qty = $("#deteil_list").getCol('qty', false, 'sum');
			var prcTaxSum = $("#deteil_list").getCol('prcTaxSum', false, 'sum');
			var noTaxAmt = $("#deteil_list").getCol('noTaxAmt', false, 'sum');
			var taxAmt = $("#deteil_list").getCol('taxAmt', false, 'sum');
			var bxQty = $("#deteil_list").getCol('bxQty', false, 'sum');
			qty = qty.toFixed(2);
			prcTaxSum = precision(prcTaxSum,2)
			noTaxAmt =precision(noTaxAmt,2)
			taxAmt = precision(taxAmt,2)
			bxQty = bxQty.toFixed(2)
			$("#deteil_list").footerData('set', { 
				"whsEncd": "本页合计",
				"qty": qty,
				"prcTaxSum": prcTaxSum,
				"noTaxAmt": noTaxAmt,
				"taxAmt": taxAmt,
				"bxQty": bxQty,

			}          );    
		},
	})
})

//查询按钮
$(document).on('click', '.searcher', function() {
	into_search();
})
function into_search() {
	var rowNum1 = $("#jqGridPager_list td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var myDate = {};

	var invtyEncd = $("#invtyEncd").val();
	var intoWhsSnglId = $(".intoWhsSnglId").val();
	var provrId = $(".provrId1").val();
	var intoWhsDt1 = $(".intoWhsSnglDt1").val();
	var intoWhsDt2 = $(".intoWhsSnglDt2").val();
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": invtyEncd,
			"intoWhsSnglId": intoWhsSnglId,
			"provrId": provrId,
			"intoWhsDt1": intoWhsDt1,
			"intoWhsDt2": intoWhsDt2,
			"isNtChk":1,
			"isNtBlle":0,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/purc/IntoWhs/queryIntoWhsList',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			var mydata = {};
			mydata.rows = data.respBody.list;
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
			alert("error")
		}
	});
}

function deteil() {
	var ids = $('#jqGrids_list').jqGrid('getGridParam', 'selarrrow');
	var rowData = [];
	for(var i = 0; i < ids.length; i++) {
		var data = $("#jqGrids_list").getCell(ids[i], "intoWhsSnglId");
		//建一个数组，把选中行的id添加到这个数组中去。
		rowData[i] = data;
	}
	var intoWhsSnglId = rowData.toString();
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"intoWhsSnglId": intoWhsSnglId,
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/purc/IntoWhs/selectIntoWhsSubByUnReturnQty',
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