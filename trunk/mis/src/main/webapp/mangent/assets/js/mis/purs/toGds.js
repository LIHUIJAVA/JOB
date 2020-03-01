function allHeight1() {
	$(window).resize(function(){
		var height1 = $('.purchaseTit').outerHeight(true);
		var height2 = $('.order-title').outerHeight(true);
		var height4 = $("#gbox_jqGrids_list").height()
		// 宽度自动适应
		$("#jqGrids_list").setGridWidth($(window).width());
		$("#deteil_list").setGridWidth($(window).width());
		// 高度自动适应
		$(window).unbind("onresize");
		$("#deteil_list").setGridHeight($(window).height() - height1 - height4 - 200); // grid_selector 是 DIV 的 ID
		$(window).bind("onresize", this);
	})
	var height1 = $('.purchaseTit').outerHeight(true);
	var height2 = $('.order-title').outerHeight(true);
	var height3 = $(window).height()  // 浏览器当前窗口可视区域高度
	var height4 = $("#gbox_jqGrids_list").height()
	window.height_deteil = height3 - height1 - height2 - 20 - height4;
}

var page = 1;
var rowNum;
var toGds;
//表格初始化
$(function() {
	$(".sure").removeClass("gray");
	$('.searcher').removeClass("gray");
	$('.cancel').removeClass("gray");
	$('button').attr('disabled', false);
	$(".gray").attr('disabled', true);
	$("#jqGrids_list").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['单据类型编码','到货编码', '到货日期', '采购类型', '业务员id', '业务员', '供应商id', '供应商', '部门id', '部门', '供应商订单号', '备注', '订单号', '是否审核'],
		colModel: [{
				name: 'formTypEncd',
				editable: true,
				sortable: false,
				width:100,
				align: 'center'
			},
			{
				name: 'toGdsSnglId',
				editable: true,
				sortable: false,
				align: 'center'
			},
			{
				name: 'toGdsSnglDt',
				editable: true,
				align: 'center',
				sortable: false,

			},
			{
				name: 'pursTypNm',
				editable: false,
				align: 'center',
				width:100,
				sortable: false,
			},
			{
				name: 'accNum',
				editable: false,
				align: 'center',
				sortable: false,
				hidden: true
			},
			{
				name: 'userName',
				editable: false,
				align: 'center',
				width:100,
				sortable: false,
			},
			{
				name: 'provrId',
				editable: false,
				align: 'center',
				sortable: false,
				hidden: true
			},
			{
				name: 'provrNm',
				editable: false,
				align: 'center',
				sortable: false,
			},
			{
				name: 'deptId',
				editable: false,
				align: 'center',
				sortable: false,
				hidden: true
			},
			{
				name: 'deptName',
				editable: false,
				align: 'center',
				sortable: false,
				width:100,
			},
			{
				name: 'provrOrdrNum',
				editable: false,
				align: 'center',
				sortable: false,
			},
			{
				name: 'memo',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'pursOrdrId',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'isNtChk',
				editable: true,
				align: 'center',
				sortable: false,
				hidden:true
			},

		],
		rowNum: 500,
		autowidth: true,
		rownumbers: true,
		loadonce: false,
		forceFit: true,
		height: 150,
		multiselect: true,
		autoScroll: true,
		shrinkToFit: false,
		rowList: [500, 1000, 3000, 5000],
		pager: '#jqGridPager_list', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "到货单列表", //表格的标题名字
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
			to_search();
		},
	})
	$("#cb_jqGrids_list").hide()
})

$(function() {
	allHeight1()
	$("#deteil_list").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ["序号","来源序号",  '备注','仓库编码', '仓库名称', '存货编码', '存货名称', '规格型号', '主计量单位', '编码', '数量','未拒收数量','未入库数量', '含税单价', '价税合计',
			'无税单价', '无税金额', '税率', '税额', '箱规', '箱数', '对应条形码', '批次', '生产日期', '失效日期', '保质期', '国际批次', '是否拒收'
		],
		colModel: [{
				name: 'ordrNum',
				editable: true,
				align: 'center',
				hidden: true
			},
			{
				name: 'toOrdrNum',
				editable: true,
				align: 'center',
				hidden: true
			},
			{
				name: 'memo',
				editable: true,
				align: 'center',
				sortable: false
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
				name: 'returnQty',//未拒收
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'unIntoWhsQty',//未入库
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
				editable: false,
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
		height: height_deteil,
		altclass: true,
		pgbuttons: false,
		pginput: false,
		autoScroll: true,
		shrinkToFit: false,
		forceFit: true,
		rowNum: 99999,
		pager: '#deteilPager_list', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "到货单明细", //表格的标题名字
		multiselect: true, //复选框
		footerrow: true,
		gridComplete: function() { 
			var qty = $("#deteil_list").getCol('qty', false, 'sum');
			var prcTaxSum = $("#deteil_list").getCol('prcTaxSum', false, 'sum');
			var noTaxAmt = $("#deteil_list").getCol('noTaxAmt', false, 'sum');
			var taxAmt = $("#deteil_list").getCol('taxAmt', false, 'sum');
			var bxQty = $("#deteil_list").getCol('bxQty', false, 'sum');
			qty = qty.toFixed(prec);
			prcTaxSum = precision(prcTaxSum,2)
			noTaxAmt = precision(noTaxAmt,2)
			taxAmt = precision(taxAmt,2)
			bxQty = bxQty.toFixed(prec)
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
$(document).on('click', '.searcher_return', function() {
	toGds = 1;
	to_search()
})
$(document).on('click', '.searcher', function() {
	toGds = 2;
	to_search()
})

function to_search() {
	var rowNum1 = $("#jqGridPager_list td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var invtyEncd = $("#invtyEncd").val();
	var toGdsSnglId = $(".toGdsSnglId1").val();
	var provrId = $(".provrId1").val();
	var whsEncd = $("#whsEncd").val();
	var toGdsSnglDt1 = $(".toGdsSnglDt1").val();
	var toGdsSnglDt2 = $(".toGdsSnglDt2").val();
	var accNum = $("#user").val();
	var savedata;
	if(toGds == 2) {
		savedata = {
			"reqHead": reqhead,
			"reqBody": {
				"invtyEncd": invtyEncd,
				"toGdsSnglId": toGdsSnglId,
				"provrId": provrId,
				"whsEncd":whsEncd,
				"toGdsSnglDt1": toGdsSnglDt1,
				"toGdsSnglDt2": toGdsSnglDt2,
				"isNtChk": 1,
				"unIntoWhsQty": 1, //入库
				"isNtRtnGood":"0",
				"accNum":accNum,
				"pageNo": page,
				"pageSize": rowNum
			}
		};
	} else if(toGds == 1) {
		savedata = {
			"reqHead": reqhead,
			"reqBody": {
				"invtyEncd": invtyEncd,
				"toGdsSnglId": toGdsSnglId,
				"provrId": provrId,
				"toGdsSnglDt1": toGdsSnglDt1,
				"toGdsSnglDt2": toGdsSnglDt2,
				"returnQty": 1, //拒收
				"isNtRtnGood":"0",
				"isNtChk": 1,
				"pageNo": page,
				"pageSize": rowNum
			}
		};
	}
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/purc/ToGdsSngl/queryToGdsSnglLists',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			var list = data.respBody.list;
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

function deteil(rtn) {
	var ids = $('#jqGrids_list').jqGrid('getGridParam', 'selarrrow');
	var rowData = [];
	for(var i = 0; i < ids.length; i++) {
		var data = $("#jqGrids_list").getCell(ids[i], "toGdsSnglId");
		//建一个数组，把选中行的id添加到这个数组中去。
		rowData[i] = data;
	}
	var toGdsSnglId = rowData.toString();
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"toGdsSnglId": toGdsSnglId,
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/'+rtn,
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

$(function() {
	$('#gview_jqGrids_list tr th span').remove('.ui-jqgrid-resize');
	$('#gview_deteil_list tr th span').remove('.ui-jqgrid-resize');
})