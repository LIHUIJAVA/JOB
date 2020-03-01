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
		colNames: ['单据类型编码','订单编码', '订单日期', '采购类型','业务员id', '业务员', '供应商id','供应商','部门id', '部门', '供应商订单号', '备注'],
		colModel: [{
				name: 'formTypEncd',
				editable: true,
				sortable: false,
				align: 'center',
				hidden:true
			},
			{
				name: 'pursOrdrId',
				editable: true,
				align: 'center'

			},
			{
				name: 'pursOrdrDt',
				editable: true,
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
				hidden:true
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
				editable: true,
				align: 'center'
			},

		],
		autowidth: true,
		rownumbers: true,
		loadonce: false,
		height: 150,
		forceFit: true,
		rowNum: 500,
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

$(function() {
	$("#deteil_list").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ["序号", '备注', '订单编码', '存货编码', '存货名称', '规格型号', '主计量单位', '数量','未到货数量', '含税单价', '价税合计',
			'无税单价', '无税金额', '税率', '税额', '计划到货时间', '箱规', '箱数', '对应条形码','主计量单位编码','保质期'
		],
		colModel: [{
				name: 'ordrNum',
				editable: true,
				align: 'center',
				hidden:true
			},
			{
				name: 'memo',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'pursOrdrId',
				editable: true,
				align: 'center',
				sortable: false

			},{
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
				sortable: false
			},
			{
				name: 'unToGdsQty',
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
				editable: true,
				align: 'center',
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
				editable: true,
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
				editable: true,
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
//				hidden: true,
				sortable: false
			},

		],
		autowidth: true,
		rownumbers: true,
		autoScroll:true,
		shrinkToFit:false,
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
			var qty = $("#deteil_list").getCol('qty', false, 'sum');
			var prcTaxSum = $("#deteil_list").getCol('prcTaxSum', false, 'sum');
			var noTaxAmt = $("#deteil_list").getCol('noTaxAmt', false, 'sum');
			var taxAmt = $("#deteil_list").getCol('taxAmt', false, 'sum');
			var bxQty = $("#deteil_list").getCol('bxQty', false, 'sum');
			qty = qty.toFixed(2);
			prcTaxSum = precision(prcTaxSum,2)
			noTaxAmt = precision(noTaxAmt,2)
			taxAmt = precision(taxAmt,2)
			bxQty = bxQty.toFixed(2)
			$("#deteil_list").footerData('set', { 
				"pursOrdrId": "本页合计",
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
	var accNum = $("input[name='accNum1']").val();
	var provrOrdrNum = $(".supplierNumber").val();
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": invtyEncd,
			"unToGdsQty":'1',
			"pursOrdrId": pursOrdrId,
			"provrId": provrId,
			"pursOrdrDt1": pursOrdrDt1,
			"pursOrdrDt2": pursOrdrDt2,
			"invtyClsEncd": invtyClsEncd,
			"isNtChk": 1,
			"accNum": accNum,
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
	var rowNum1 = $("#deteil_list").getGridParam('rowNum');
	rowNum = parseInt(rowNum1);
	var myDate = {};

	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"pursOrdrId": pursOrdrId,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/purc/PursOrdr/selectUnToGdsQtyByPursOrdrId',
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