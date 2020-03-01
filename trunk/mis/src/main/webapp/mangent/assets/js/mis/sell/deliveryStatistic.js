
var pages;
var page = 1;
var rowNum;
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
		colNames: ['单据编号', '单据日期', '客户编码', '客户名称', '仓库名称','存货编码', '存货名称','规格',
		'主计量单位','发货数量','发货价税合计','开票数量','出库数量','出库成本','净发货','净退货'], //jqGrid的列显示名字
		colModel: [{
				name: 'sellSnglId',
				align: "center",
				editable: true,
			},
			{
				name: 'sellSnglDt',
				align: "center",
				editable: true,
			},
			{
				name: "custId",
				align: "center",
				editable: true,
			},
			{
				name: "custNm",
				align: "center",
				editable: true,
			},
			{
				name: 'whsNm',
				align: "center",
				editable: true,
			},
			{
				name: "invtyEncd",
				align: "center",
				editable: true,
			},
			{
				name: 'invtyNm',
				align: "center",
				editable: true,
			},
			{
				name: 'spcModel',
				align: "center",
				editable: true,
			},
			{
				name: 'measrCorpNm',
				align: "center",
				editable: true,
			},
			{
				name: 'qty',
				align: "center",
				editable: true,
			},
			{
				name: 'prcTaxSum',
				align: "center",
				editable: true,
			},
			{
				name: 'bllgQty',
				align: "center",
				editable: true,
			},
			{
				name: 'outQty',
				align: "center",
				editable: true,
			},
			{
				name: 'outNoTaxAmt',
				align: "center",
				editable: true,
			},
			{
				name: 'netDelivery',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'netReturn',
				align: "center",
				editable: true,
			}],
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
		pager: '#jqgridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "发货汇总表统计报表", //表格的标题名字
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
	var formId = $("#formId").val();
	var invtyEncd = $("#invtyEncd").val();
	var custId = $("#custId").val();
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"formDt1": fromDt1,
			"formDt2": fromDt2,
			"formId":formId,
			"invtyEncd":invtyEncd,
			"custId":custId,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/sell/Report/SelectDeliveryStatistic',
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
		}
	});
}

//导出
$(document).on('click', '.exportExcel', function() {
	var fromDt1 = $("input[name='fromDt1']").val();
	var fromDt2 = $("input[name='fromDt2']").val();
	var formId = $("#formId").val();
	var invtyEncd = $("#invtyEncd").val();
	var custId = $("#custId").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"formDt1": fromDt1,
			"formDt2": fromDt2,
			"formId":formId,
			"invtyEncd":invtyEncd,
			"custId":custId,
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/sell/Report/printDeliveryStatistic',
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
			var execlName = '发货汇总表统计报表'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})

})
