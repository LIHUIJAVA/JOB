var count;
var pages;
var page = 1;
var rowNum;
//点击表格图标显示仓库列表
$(function() {
//	仓库
	$(document).on('click', '.whsEncd_biaoge', function() {
		window.open("../../Components/baseDoc/whsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
//	存货编码
	$(document).on('click', '.invtyEncd_biaoge', function() {
		window.open("../../Components/baseDoc/invtyList.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
//	批号
	$(document).on('click', '.batNum_biaoge', function() {
		window.open("../../Components/baseDoc/batNum.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
//	存货分类
	$(document).on('click', '.invtyClsEncd_biaoge', function() {
		window.open("../../Components/baseDoc/invtyTree.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
//	客户
	$(document).on('click', '.custId_biaoge', function() {
		window.open("../../Components/baseDoc/custDocList.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
//	业务员
	$(document).on('click', '.accNum_biaoge', function() {
		window.open("../../Components/baseDoc/userList.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
//	收发类别
//	$(document).on('click', '.recvSendCateId_biaoge', function() {
//		window.open("../../Components/baseDoc/recvSendCateTree.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
//	});
})
$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	pageInit();
});

function pageInit() {
	allHeight()
	jQuery("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['客户编号', '客户简称', '客户名称','存货编码', '存货名称 ', '规格型号', '净退货数量','净退货无税金额',
		'净退货价税合计','净发货数量','净发货无税金额','净发货价税合计','开票数量','开票无税金额','开票价税合计','出库数量'
		,'出库无税金额','出库价税合计','发货数量','发货无税金额','发货价税合计'], //jqGrid的列显示名字
		colModel: [{
				name: 'custId',
				align: "center",
				editable: true,
			},
			{
				name: 'custShtNm',
				align: "center",
				editable: true,
			},
			{
				name: 'custNm',
				align: "center",
				editable: true,
			},
			{
				name: "invtyEncd",
				align: "center",
				editable: true,
			},
			{
				name: "invtyNm",
				align: "center",
				editable: true,
			},
			{
				name: 'spcModel',
				align: "center",
				editable: true,
			},
			{
				name: "rtnChuQty",
				align: "center",
				editable: true,
			},
			{
				name: 'rtnNoTaxAmt',
				align: "center",
				editable: true,
			},
			{
				name: 'rtnPrcTaxSum',
				align: "center",
				editable: true,
			},
			{
				name: 'sellQty',
				align: "center",
				editable: true,
			},
			{
				name: 'sellNoTaxAmt',
				align: "center",
				editable: true,
			},
			{
				name: 'sellPrcTaxSum',
				align: "center",
				editable: true,
			},
			{
				name: 'kpQty',
				align: "center",
				editable: true,
			},
			{
				name: 'kpAmt',
				align: "center",
				editable: true,
			},
			{
				name: 'kpAmtSum',
				align: "center",
				editable: true,
			},
			{
				name: 'sellOutQty',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'sellOutNoTaxAmt',
				align: "center",
				editable: true,
			},
			{
				name: 'sellOutPrcTaxSum',
				align: "center",
				editable: true,
			},
			{
				name: 'rtnSellQty',
				align: "center",
				editable: true,
			},
			{
				name: 'rtnSellNoTaxAmt',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'rtnSellPrcTaxSum',
				align: "center",
				editable: true,
			}
		],
		sortable:true,
		autowidth: true,
		rownumbers: true,
		sortable:true,// 列拖拽
		autoScroll:true,
		shrinkToFit:false,
		height:height,
		loadonce: true,
		forceFit: true,
		rowNum:500,
		rowList: [500, 1000, 3000,5000],
		pager: '#count_jqgridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "发货汇总表", //表格的标题名字
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
			search()
		},
		footerrow: true,

		gridComplete: function() { 
			var rtnChuQty = $("#jqGrids").getCol('rtnChuQty', false, 'sum');
			var rtnNoTaxAmt = $("#jqGrids").getCol('rtnNoTaxAmt', false, 'sum');
			var rtnPrcTaxSum = $("#jqGrids").getCol('rtnPrcTaxSum', false, 'sum');
			var sellQty = $("#jqGrids").getCol('sellQty', false, 'sum');
			var sellNoTaxAmt = $("#jqGrids").getCol('sellNoTaxAmt', false, 'sum');
			var sellPrcTaxSum = $("#jqGrids").getCol('sellPrcTaxSum', false, 'sum');
			var kpQty = $("#jqGrids").getCol('kpQty', false, 'sum');
			var kpAmt = $("#jqGrids").getCol('kpAmt', false, 'sum');
			var kpAmtSum = $("#jqGrids").getCol('kpAmtSum', false, 'sum');
			var sellOutQty = $("#jqGrids").getCol('sellOutQty', false, 'sum');
			var sellOutNoTaxAmt = $("#jqGrids").getCol('sellOutNoTaxAmt', false, 'sum');
			var sellOutPrcTaxSum = $("#jqGrids").getCol('sellOutPrcTaxSum', false, 'sum');
			var rtnSellQty = $("#jqGrids").getCol('rtnSellQty', false, 'sum');
			var rtnSellNoTaxAmt = $("#jqGrids").getCol('rtnSellNoTaxAmt', false, 'sum');
			var rtnSellPrcTaxSum = $("#jqGrids").getCol('rtnSellPrcTaxSum', false, 'sum');
			
			
			$("#jqGrids").footerData('set', { 
				"custId": "本页合计",
				rtnChuQty: rtnChuQty.toFixed(prec),
				rtnNoTaxAmt:precision(prcTaxSum,2),
				rtnPrcTaxSum: precision(prcTaxSum,2),
				sellQty: sellQty.toFixed(prec),
				sellNoTaxAmt:precision(prcTaxSum,2),
				sellPrcTaxSum: precision(prcTaxSum,2),
				kpQty: kpQty.toFixed(prec),
				kpAmt: precision(prcTaxSum,2),
				kpAmtSum: precision(prcTaxSum,2),
				sellOutQty: sellOutQty.toFixed(prec),
				sellOutNoTaxAmt: precision(prcTaxSum,2),
				sellOutPrcTaxSum: precision(prcTaxSum,2),
				rtnSellQty: rtnSellQty.toFixed(prec),
				rtnSellNoTaxAmt: precision(prcTaxSum,2),
				rtnSellNoTaxAmt: precision(prcTaxSum,2),
				rtnSellPrcTaxSum: precision(prcTaxSum,2),
				
			}          );    
		},
	})
}
//查询按钮
$(document).on('click', '#find', function() {
	search()
})

//条件查询
function search() {
	var fromDt1 = $("input[name='fromDt1']").val();
	var fromDt2 = $("input[name='fromDt2']").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var batNum = $("input[name='batNum']").val();
	var whsEncd = $("input[name='whsEncd']").val();
	var deptId = $("input[name='deptId']").val();
	var custId = $("input[name='custId']").val();
	var accNum = $("select[name='accNum']").val();
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"fromDt1": fromDt1,
			"fromDt2": fromDt2,
			"invtyClsEncd": invtyClsEncd,
			"invtyEncd": invtyEncd,
			"batNum": batNum,
			"whsEncd": whsEncd,
			"deptId": deptId,
			"custId": custId,
			"accNum": accNum,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/sell/Report/sellStatisticalCount',
		async: true,
		data: postD2,
		dataType: 'json',
		//开始加载动画  添加到ajax里面
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");
		},
		success: function(data) {
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
		//结束加载动画
		complete: function() {
			$(".zhezhao").css("display", "none");
			$("#loader").css("display", "none");
		},
		error: function() {
			alert("查询失败")
		}
	});

}


var arr=[];
var obj={}
//导出
$(document).on('click', '.exportExcel', function() {
	var fromDt1 = $("input[name='fromDt1']").val();
	var fromDt2 = $("input[name='fromDt2']").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var batNum = $("input[name='batNum']").val();
	var whsEncd = $("input[name='whsEncd']").val();
	var deptId = $("input[name='deptId']").val();
	var custId = $("input[name='custId']").val();
	var accNum = $("select[name='accNum']").val();
	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"fromDt1": fromDt1,
			"fromDt2": fromDt2,
			"invtyClsEncd": invtyClsEncd,
			"invtyEncd": invtyEncd,
			"batNum": batNum,
			"whsEncd": whsEncd,
			"deptId": deptId,
			"custId": custId,
			"accNum": accNum
		}
	};
	var Data = JSON.stringify(data2);
	$.ajax({
		url: url + "/mis/sell/Report/sellStatisticalCount",
		type: 'post',
		data: Data,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		success: function(data) {
			var list = data.respBody.list;
			var execlName = '发货汇总表'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
})
