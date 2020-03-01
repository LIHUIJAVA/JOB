$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	pageInit();
})

var count;
var pages;
var page = 1;
var rowNum;
var tableSums = {};
//表格初始化
function pageInit() {
	allHeight();
	$("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['客户编码', '客户名称', '单据日期', '仓库编码', '仓库名称', '存货编码', '存货名称', '存货代码', '规格型号', '主计量单位',
			'编码', '数量', '无税单价', '无税金额', '税率', '含税单价', '价税合计', '箱规', '箱数', '批次'
		], //jqGrid的列显示名字
		colModel: [{
				name: 'custId',
				editable: true,
				align: 'center',
			},
			{
				name: 'custNm',
				editable: true,
				align: 'center',
			},
			{
				name: 'formDt',
				editable: true,
				align: 'center',
				sorttype: 'date', // 时间排序
			},
			{
				name: 'whsEncd',
				editable: true,
				align: 'center',
			},
			{
				name: 'whsNm',
				editable: true,
				align: 'center',
			},
			{
				name: 'invtyEncd',
				editable: true,
				align: 'center',
			},
			{
				name: 'invtyNm',
				editable: false,
				align: 'center',
			},
			{
				name: 'invtyCd',
				editable: false,
				align: 'center',
			},
			{
				name: 'spcModel',
				editable: false,
				align: 'center',
			},
			{
				name: 'measrCorpNm',
				editable: false,
				align: 'center',
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
				sorttype: 'integer', // 数量排序
			},
			{
				name: 'noTaxUprc',
				editable: true,
				align: 'center',
				sorttype: 'integer',
			},
			{
				name: 'noTaxAmt',
				editable: false,
				align: 'center',
				sorttype: 'integer',
			},
			{
				name: 'taxRate',
				editable: false,
				align: 'center',
				sorttype: 'integer',
			},
			{
				name: 'cntnTaxUprc',
				editable: false,
				align: 'center',
				sorttype: 'integer',
			},
			{
				name: 'prcTaxSum',
				editable: false,
				align: 'center',
				sorttype: 'integer',
			},
			{
				name: 'bxRule',
				editable: false,
				align: 'center',
				sorttype: 'integer',
			},
			{
				name: 'bxQty',
				editable: true,
				align: 'center',
				sorttype: 'integer', // 数量排序
			},
			{
				name: 'batNum',
				editable: false,
				align: 'center',
			}
		],
		sortable: true,
		loadonce: true,
		autowidth: true,
		sortable: true, // 列拖拽
		height: height,
		autoScroll: true,
		shrinkToFit: false,
		rownumbers: true,
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条
		rowNum: 500, // 每页多少行，用于分页
		forceFit: true,
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		footerrow: true,
		caption: "销售明细表", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
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
			search();
		},
		gridComplete: function() {
			$("#jqGrids").footerData('set', { 
				"custId": "总计",
				"bxQty": tableSums.bxQty,
				"qty": tableSums.qty,
				"prcTaxSum": tableSums.prcTaxSum,
				"noTaxAmt": tableSums.noTaxAmt,
				"taxAmt": tableSums.taxAmt
			}          );
		},
		onSortCol: function(index, colindex, sortorder) {
			search(index, sortorder)
		}
	})
}

//查询按钮
$(function() {
	$(".search").click(function() {
		search()
	})
})

function search(index, sortorder) {
//	var rowNum1 = $("#jqGrids").getGridParam('rowNum'); //获取显示配置记录数量
//	rowNum = parseInt(rowNum1)
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var myData = {};

	var invtyEncd = $(".invtyEncd").val();
	var whsEncd = $("whsEncd").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var accNum = $("#user").val();
	var deptId = $("#deptId").val();
	var custId = $("#custId").val();
	var sellSnglDt1 = $(".sellSnglDt1").val();
	var sellSnglDt2 = $(".sellSnglDt2").val();

	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"isNtChk": 1,
			"invtyEncd": invtyEncd,
			"whsEncd": whsEncd,
			"invtyClsEncd": invtyClsEncd,
			"accNum": accNum,
			"deptId": deptId,
			"custId": custId,
			"sellSnglDt1": sellSnglDt1,
			"sellSnglDt2": sellSnglDt2,
			"sort":index,
			"sortOrder":sortorder,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/purc/SellSngl/querySellSnglByInvtyEncd',
		async: true,
		data: saveData,
		dataType: 'json',
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
			var list = data.respBody.list;
			var mydata = {};
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

//导出

$(document).on('click', '.exportExcel', function() {
	var invtyEncd = $(".invtyEncd").val();
	var whsEncd = $("whsEncd").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var accNum = $("#user").val();
	var deptId = $("#deptId").val();
	var custId = $("#custId").val();
	var sellSnglDt1 = $(".sellSnglDt1").val();
	var sellSnglDt2 = $(".sellSnglDt2").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"isNtChk": 1,
			"invtyEncd": invtyEncd,
			"whsEncd": whsEncd,
			"invtyClsEncd": invtyClsEncd,
			"accNum": accNum,
			"deptId": deptId,
			"custId": custId,
			"sellSnglDt1": sellSnglDt1,
			"sellSnglDt2": sellSnglDt2,
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/purc/SellSngl/querySellSnglByInvtyEncd',
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
			var execlName = '销售单明细'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
})
