var count;
var pages;
var page = 1;
var rowNum;
//表格初始化
$(function() {
	allHeight();
	$("#jqGrids_list").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['发货单号', '发货日期','业务类型id', '业务类型', '业务员id','业务员', '部门id','部门', '客户id','客户名称', '发货地址', '备注', '是否审核', '是否退货'],
		colModel: [{
				name: 'delvSnglId',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'delvSnglDt',
				editable: true,
				align: 'center'
			},
			{
				name: 'bizTypId',
				editable: false,
				align: 'center',
				hidden:true
			},
			{
				name: 'bizTypNm',
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
				name: 'custId',
				editable: false,
				align: 'center',
				hidden: true,
			},
			{
				name: 'custNm',
				editable: false,
				align: 'center',
				hidden: true
			},
			{
				name: 'recvrAddr',
				editable: false,
				align: 'center',
			},
			{
				name: 'memo',
				editable: true,
				align: 'center'
			},
			{
				name: 'isNtChk',
				editable: true,
				align: 'center'
			},
			{
				name: 'isNtRtnGoods',
				editable: true,
				align: 'center'
			}
		],
		autowidth: true,
		height: 150,
		autoScroll: true,
		shrinkToFit: false,
		rownumbers: true,
		loadonce: false,
		forceFit: true,
		multiselect: true,
		rowList: [10, 20, 30], //可供用户选择一页显示多少条
		pager: '#jqGridPager_list', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "委托代销发货单列表", //表格的标题名字
		onSelectRow: function() {
			deteil_search();
		},
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
			entr_Search();
		},
	})
	$("#jqGrids_list_cb").hide()
})

$(function() {
	$("#deteil_list").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ["序号","单据号",'仓库编码','仓库名称','存货编码', '存货名称', '规格型号', '主计量单位', '主计量单位编码', '数量', '含税单价', '价税合计',
			'无税单价', '无税金额', '税率', '税额', '箱规', '箱数', '对应条形码', '批次', '生产日期', '失效日期', '保质期', '国际批次', '备注', '是否退货'
		],
		colModel: [{
				name: 'ordrNum',
				editable: true,
				align: 'center',
				hidden: true
			},
			{
				name: 'delvSnglId',
				editable: true,
				align: 'center',
				sortable: false,
				hidden: true
			},
			{
				name: 'whsEncd',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'whsNm',
				editable: false,
				sortable: false,
				align: 'center',
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
				name: 'cntnTaxUprc',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'prcTaxSum',
				editable: false,
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
		autoScroll: true,
		shrinkToFit: false,
		forceFit: true,
		rowNum: 99999,
		altclass: true,
		pgbuttons: false,
		pginput: false,
		pager: '#deteilPager_list', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "委托代销发货明细", //表格的标题名字
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

var isNtRtnGoods = "";

//查询按钮
$(document).on('click', '.searcher', function() {
	entr_Search()
})

function entr_Search() {
	var rowNum1 = $("#jqGridPager_list td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var delvSnglId = $(".delvSnglId").val();
	var delvSnglDt1 = $(".delvSnglDt1").val();
	var delvSnglDt2 = $(".delvSnglDt2").val();
	var custId = $(".custId").val();
	var whsEncd = $(".whsEncd").val();

	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"delvSnglId": delvSnglId,
			"delvSnglDt1": delvSnglDt1,
			"delvSnglDt2": delvSnglDt2,
			"custId": custId,
			"whsEncd": whsEncd,
			"unBllgRtnGoodsQty":1,
			"isNtChk": 1,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/purc/EntrsAgnDelv/queryEntrsAgnDelvLists',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			var list = data.respBody.list;
			var mydata = {};
			mydata.rows = list;
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
		var data = $("#jqGrids_list").getCell(ids[i], "delvSnglId");
		//建一个数组，把选中行的id添加到这个数组中去。
		rowData[i] = data;
	}
	var delvSnglId = rowData.toString();
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"delvSnglId": delvSnglId,
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/purc/EntrsAgnDelv/selectEntDeSubUnBllgRtnGoodsQty',
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