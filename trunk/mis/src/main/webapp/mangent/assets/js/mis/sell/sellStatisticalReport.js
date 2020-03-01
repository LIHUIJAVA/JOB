
var pages;
var page = 1;
var rowNum;
var tableSums = {};
//点击表格图标显示仓库列表
$(function() {
//	仓库
	$(document).on('click', '.whsEncd_biaoge', function() {
		window.open("../../Components/baseDoc/whsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
//	业务员
	$(document).on('click', '.accNum_biaoge', function() {
		window.open("../../Components/baseDoc/userList.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
//	收发类别
	$(document).on('click', '.recvSendCateId_biaoge', function() {
		window.open("../../Components/baseDoc/recvSendCateTree.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})
$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	pageInit();
});

//查询按钮
$(document).on('click', '#find', function() {
	search()
})

//条件查询
function search(index, sortorder) {
	if(localStorage.getItem("custId") == null) {
		var custId = $("input[name='custId']").val();
	} else {
		var custId = localStorage.getItem("custId")
	}
	var fromDt1 = $("input[name='fromDt1']").val();
	var fromDt2 = $("input[name='fromDt2']").val();
	var deptId = $("input[name='deptId']").val();
	var whsEncd = $("input[name='whsEncd']").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var batNum = $("input[name='batNum']").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var accNum = localStorage.getItem("accNum")
	var recvSendCateId = $("input[name='recvSendCateId']").val();
	var bizTypId = $("select[name='bizTypId']").val();
	var sellTypId = $("select[name='sellTypId']").val();
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"fromDt1": fromDt1,
			"fromDt2": fromDt2,
			"depId": deptId,
			"whsEncd": whsEncd,
			"invtyEncd": invtyEncd,
			"batNum": batNum,
			"custId": custId,
			"invtyClsEncd": invtyClsEncd,
			"accNum": accNum,
			"bizTypId": bizTypId,
			"sellTypId": sellTypId,
			"recvSendCateId": recvSendCateId,
			"sort":index,
			"sortOrder":sortorder,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/sell/Report/sellStatisticalReport',
		async: true,
		data: postD2,
		dataType: 'json',
		//开始加载动画  添加到ajax里面
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");

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
		//结束加载动画
		complete: function() {
			$(".zhezhao").css("display", "none");
			$("#loader").css("display", "none");
		},
		error: function() {
			console.log(error)
		}
	});

}


function pageInit() {
	allHeight()
	var rowNum = $("#_input").val()
	jQuery("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['客户编号', '客户名称', '部门编号', '部门名称', '业务员编号', '业务员名称','存货编码',
		'存货名称','仓库编码','仓库名称','存货分类编码','存货分类名称','箱规','批号','期初数量'
		,'期初金额','期初税额','期初价税合计','发货数量','发货金额(无税金额)','发货税额',
		'发货价税合计','开票数量','开票金额','开票税额','开票价税合计','结存数量','结存金额','结存税额','结存价税合计','数量差异','金额差异','税额差异',
		'价税合计差异'], //jqGrid的列显示名字
		colModel: [{
				name: 'custId',
				align: "center",
				editable: true,
			},
			{
				name: 'custNm',
				align: "center",
				editable: true,
			},
			{
				name: "depId",
				align: "center",
				editable: true,
			},
			{
				name: "deptName",
				align: "center",
				editable: true,
			},
			{
				name: 'accNumTaxSum',
				align: "center",
				editable: true,
			},
			{
				name: "userName",
				align: "center",
				editable: true,
			},
			{
				name: 'invtyEncd',
				align: "center",
				editable: true,
			},
			{
				name: 'invtyNm',
				align: "center",
				editable: true,
			},
			{
				name: 'whsEncd',
				align: "center",
				editable: true,
			},
			{
				name: 'whsNm',
				align: "center",
				editable: true,
			},
			{
				name: 'invtyClsEncd',
				align: "center",
				editable: true,
			},
			{
				name: 'invtyClsNm',
				align: "center",
				editable: true,
			},
			{
				name: 'bxRule',
				align: "center",
				editable: true,
			},
			{
				name: 'batNum',
				align: "center",
				editable: true,
			},
			{
				name: 'qiChuQty',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'qiChuNoTaxAmt',
				align: "center",
				editable: true,
			},
			{
				name: 'qiChuTaxAmt',
				align: "center",
				editable: true,
			},
			{
				name: 'qiChuPrcTaxSum',
				align: "center",
				editable: true,
			},
			{
				name: 'sellQty',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'sellNoTaxAmt',
				align: "center",
				editable: true,
			},
			{
				name: 'sellTaxAmt',
				align: "center",
				editable: true,
			},
			{
				name: 'sellPrcTaxSum',
				align: "center",
				editable: true,
			},
			{
				name: 'KPQty',
				align: "center",
				editable: true,
			},
			{
				name: 'KPAmt',
				align: "center",
				editable: true,
			},
			{
				name: 'KPTaxAmt',
				align: "center",
				editable: true,
			},
			{
				name: 'KPAmtSum',
				align: "center",
				editable: true,
			},
			{
				name: 'cumulativeQty',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'cumulativeNoTaxAmt',
				align: "center",
				editable: true,
			},
			{
				name: 'cumulativeTaxAmt',
				align: "center",
				editable: true,
			},
			{
				name: 'cumulativePrcTaxSum',
				align: "center",
				editable: true,
			},
			{
				name: 'differencesQty',
				align: "center",
				editable: true,
				sorttype:'integer',
				hidden:true
			},
			{
				name: 'differencesNoTaxAmt',
				align: "center",
				editable: true,
				hidden:true
			},
			{
				name: 'differencesTaxAmt',
				align: "center",
				editable: true,
				hidden:true
			},
			{
				name: 'differencesPrcTaxSum',
				align: "center",
				editable: true,
				hidden:true
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
		pager: '#statistical_jqgridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "发货统计表", //表格的标题名字
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
		footerrow: true,
		gridComplete: function() {
			$("#jqGrids").footerData('set', { 
				"custId": "总计",
				"KPAmt": tableSums.KPAmt,
				"KPAmtSum": tableSums.KPAmtSum,
				"KPQty": tableSums.KPQty,
				"KPTaxAmt": tableSums.KPTaxAmt,
				"cumulativeNoTaxAmt": tableSums.cumulativeNoTaxAmt,
				"cumulativePrcTaxSum": tableSums.cumulativePrcTaxSum,
				"cumulativeQty": tableSums.cumulativeQty,
				"cumulativeTaxAmt": tableSums.cumulativeTaxAmt,
				"differencesNoTaxAmt": tableSums.differencesNoTaxAmt,
				"differencesPrcTaxSum": tableSums.differencesPrcTaxSum,
				"differencesQty": tableSums.differencesQty,
				"differencesTaxAmt": tableSums.differencesTaxAmt,
				"qiChuNoTaxAmt": tableSums.qiChuNoTaxAmt,
				"qiChuPrcTaxSum": tableSums.qiChuPrcTaxSum,
				"qiChuQty": tableSums.qiChuQty,
				"qiChuTaxAmt": tableSums.qiChuTaxAmt,
				"sellNoTaxAmt": tableSums.sellNoTaxAmt,
				"sellPrcTaxSum": tableSums.sellPrcTaxSum,
				"sellQty": tableSums.sellQty,
				"sellTaxAmt": tableSums.sellTaxAmt,
			}          );
		},
		onSortCol: function(index, colindex, sortorder) {
			search(index, sortorder)
		}
	})
}

function getJQAllData() {
	//拿到grid对象
	var obj = $("#jqGrids");
	//获取grid表中所有的rowid值
	var rowIds = obj.getDataIDs();
	//初始化一个数组arrayData容器，用来存放rowData
	var arrayData = new Array();
	if(rowIds.length > 0) {
		for(var i = 0; i < rowIds.length; i++) {
//			if(obj.getRowData(rowIds[i]).whsNm == "") {
//				continue;
//			} else {
				arrayData.push(obj.getRowData(rowIds[i]));
//			}
		}
	}
	return arrayData;
}
