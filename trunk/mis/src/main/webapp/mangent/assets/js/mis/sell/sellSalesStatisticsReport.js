
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
	//	存货编码
	$(document).on('click', '.invtyEncd_biaoge', function() {
		window.open("../../Components/baseDoc/invtyList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	//	批号
	$(document).on('click', '.batNum_biaoge', function() {
		window.open("../../Components/baseDoc/batNum.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	//	存货分类
	$(document).on('click', '.invtyClsEncd_biaoge', function() {
		window.open("../../Components/baseDoc/invtyTree.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	//	客户
	$(document).on('click', '.custId_biaoge', function() {
		window.open("../../Components/baseDoc/custDocList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	//	业务员
	$(document).on('click', '.accNum_biaoge', function() {
		window.open("../../Components/baseDoc/userList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
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
	var deptId = $("input[name='deptId']").val();
	var fromDt1 = $("input[name='fromDt1']").val();
	var fromDt2 = $("input[name='fromDt2']").val();
	var whsEncd = $("input[name='whsEncd']").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var batNum = $("input[name='batNum']").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var custId = $("input[name='custId']").val();
	var accNum = $("input[name='accNum']").val();
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
		url: url + '/mis/sell/Report/sellSalesStatisticsReport',
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
			$("#sales_jqGrids").jqGrid("clearGridData");
			$("#sales_jqGrids").jqGrid("setGridParam", {
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

function pageInit() {
	allHeight()
//	var rowNum = $("#_input").val()
	jQuery("#sales_jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['存货编码','存货名称', '客户编号', '客户名称', '部门编码', '部门名称', '业务员编码', '业务员名称','发货数量', '发货金额', '发货价税合计',
			'出库数量', '出库金额', '出库价税合计','开票数量', '开票金额', '开票价税合计'
			
		], //jqGrid的列显示名字
		colModel: [
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
				name: 'depId',
				align: "center",
				editable: true,
			},
			{
				name: 'depName',
				align: "center",
				editable: true,
			},
			{
				name: 'accNum',
				align: "center",
				editable: true,
			},
			{
				name: 'userName',
				align: "center",
				editable: true,
			},
			{
				name: "sellQty",
				align: "center",
				editable: true,
				sorttype: 'integer', // 数量排序
			},
			{
				name: "sellnoTaxAmt",
				align: "center",
				editable: true,
			},
			{
				name: 'sellprcTaxSum',
				align: "center",
				editable: true,
			},
			{
				name: 'sellOutQty',
				align: "center",
				editable: true,
				sorttype: 'integer', // 数量排序
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
				name: 'invQty',
				align: "center",
				editable: true,
				sorttype: 'integer', // 数量排序
			},
			{
				name: 'invNoTaxAmt',
				align: "center",
				editable: true,
			},
			{
				name: 'invPrcTaxSum',
				align: "center",
				editable: true,
			}
		],
		sortable: true,
		autowidth: true,
		rownumbers: true,
		autoScroll: true,
		sortable: true, // 列拖拽
		shrinkToFit: false,
		height: height,
		loadonce: true,
		forceFit: true,
		rowNum: 500,
		rowList: [500, 1000, 3000, 5000],
		pager: '#sales_jqgridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "销售综合统计表", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#sales_jqGrids").getGridParam('records'); //获取返回的记录数
			page = $("#sales_jqGrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#sales_jqGrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			$("#sales_jqGrids").footerData('set', { 
				"invtyEncd": "总计",
				"invNoTaxAmt": tableSums.invNoTaxAmt,
				"invPrcTaxSum": tableSums.invPrcTaxSum,
				"invQty": tableSums.invQty,
				"sellOutNoTaxAmt": tableSums.sellOutNoTaxAmt,
				"noTaxAmt": tableSums.noTaxAmt,
				"sellOutPrcTaxSum": tableSums.sellOutPrcTaxSum,
				"sellOutQty": tableSums.sellOutQty,
				"sellQty": tableSums.sellQty,
				"sellprcTaxSum": tableSums.sellprcTaxSum,
				"sellnoTaxAmt": tableSums.sellnoTaxAmt
			}          );
		},
		onSortCol: function(index, colindex, sortorder) {
			search(index, sortorder)
		}
	})
}

//导出
$(document).on('click', '.exportExcel', function() {
	
	var deptId = $("input[name='deptId']").val();
	var fromDt1 = $("input[name='fromDt1']").val();
	var fromDt2 = $("input[name='fromDt2']").val();
	var whsEncd = $("input[name='whsEncd']").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var batNum = $("input[name='batNum']").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var custId = $("input[name='custId']").val();
	var accNum = $("input[name='accNum']").val();
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
			"accNum": accNum
		}
	};
	var Data = JSON.stringify(data2);
	$.ajax({
		url: url + "/mis/sell/Report/sellSalesStatisticsReport",
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
			var execlName = '销售综合统计报表'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
	
})