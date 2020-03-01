var count;
var pages;
var page = 1;
var rowNum;
var tableSums = {};
$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	pageInit();
});

function pageInit() {
	localStorage.removeItem("index")
	localStorage.removeItem("sortorder")
	allHeight()
	jQuery("#sellProcurement_jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['存货编码','存货名称','仓库编码','仓库名称', '批次', '箱规', '待入库数量', '调拨入库数量','待发货数量',
		'调拨出库数量','现存量','可用数量','参考成本','生产日期','保质期','失效日期','规格','计量单位'], //jqGrid的列显示名字
		colModel: [
			{
				name: "invtyEncd",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'invtyNm',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'whsEncd',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'whsNm',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: "batNum",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: "bxQty",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'intoWhsQty',
				align: "center",
				editable: true,
				sortable: true
			},
			{
				name: 'intoCannibQty',
				align: "center",
				editable: true,
				sortable: true
			},
			{
				name: 'sellOutWhsQty',
				align: "center",
				editable: true,
				sortable: true
			},
			{
				name: 'outCannibQty',
				align: "center",
				editable: true,
				sortable: true
			},
			{
				name: 'nowStok',
				align: "center",
				editable: true,
				sortable: true
			},
			{
				name: 'avalQty',
				align: "center",
				editable: true,
				sortable: true
			},
			{
				name: 'refCost',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'prdcDt',
				align: "center",
				editable: true,
				sortable: true
			},
			{
				name: 'baoZhQi',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'invldtnDt',
				align: "center",
				editable: true,
				sortable: true
			},
			{
				name: 'spcModel',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'measrCorpNm',
				align: "center",
				editable: true,
				sortable: false
			}
		],
		autowidth: true,
		rownumbers: true,
		rownumWidth:50,
		autoScroll:true,
		sortable:true,// 列拖拽
		shrinkToFit:false,
		height:height,
		loadonce: true,
		forceFit: true,
		rowNum:500,
		rowList: [500, 1000, 3000,5000], //可供用户选择一页显示多少条
		pager: '#sellProcurement_jqgridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "采购现存量查询", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#sellProcurement_jqGrids").getGridParam('records'); //获取返回的记录数
			page = $("#sellProcurement_jqGrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#sellProcurement_jqGrids").getGridParam('rowNum'); //获取显示配置记录数量

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
		footerrow: true,
		gridComplete: function() {
			$("#sellProcurement_jqGrids").footerData('set', { 
				"invtyEncd": "总计",
				"intoWhsQty": tableSums.intoWhsQty,
				"intooutCannibQty":tableSums.intooutCannibQty,
				"outCannibQty": tableSums.outCannibQty,
				"sellOutWhsQty": tableSums.sellOutWhsQty

			}          );
		},
		onSortCol: function(index, colindex, sortorder) {
			localStorage.setItem("index",index)
			localStorage.setItem("sortorder",sortorder)
			window.index = index;
			window.sortorder = sortorder;
			search(index, sortorder)
		}
	})
}


//查询按钮
$(document).on('click', '#find1', function() {
	var index = ''
	var sortorder = 'desc'
	search(index, sortorder)
})

//条件查询
function search(index, sortorder) {
	var whsEncd = $("input[name='whsEncd']").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var batNum = $("input[name='batNum']").val();
	var invtyClsEncd = $("#invtyCls").val();
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"whsEncd": whsEncd,
			"invtyEncd": invtyEncd,
			"batNum": batNum,
			"invtyClsEncd": invtyClsEncd,
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
		url: url + '/mis/sell/Report/sellProcurementNowStokReport',
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
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#sellProcurement_jqGrids").jqGrid("clearGridData");
			$("#sellProcurement_jqGrids").jqGrid("setGridParam", {
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

//导出
$(document).on('click', '.exportExcel', function() {
	var whsEncd = $("input[name='whsEncd']").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var batNum = $("input[name='batNum']").val();
	var invtyClsEncd = $("input[name='invtyCls']").val();
	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"whsEncd": whsEncd,
			"invtyEncd": invtyEncd,
			"batNum": batNum,
			"invtyClsEncd": invtyClsEncd
		}
	};
	var Data = JSON.stringify(data2);
	$.ajax({
		url: url + "/mis/sell/Report/sellProcurementNowStokReport",
		type: 'post',
		data: Data,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		success: function(data) {
			var list = data.respBody.list;
			var execlName = '采购现存量报表'
			ExportData(list, execlName)
		},
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");
		},
		complete: function() {
			$(".zhezhao").css("display", "none");
			$("#loader").css("display", "none");
		},
		error: function() {
			alert("导出失败")
		}
	})
	
})
