//出库明细表

$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	//点击表格图标显示存货列表
	$(document).on('click', '.biao_invtyEncd', function() {
		window.open("../../Components/baseDoc/invtyList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	//点击表格图标显示仓库列表
	$(document).on('click', '.biao_whsEncd', function() {
		window.open("../../Components/baseDoc/whsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
//	存货分类
	$(document).on('click', '.invtyClsEncd_biaoge', function() {
		window.open("../../Components/baseDoc/invtyTree.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})

var pages;
var page = 1;
var rowNum;
var tableSums = {};

//表格初始化
$(function() {
	allHeight();
	var rowNum = $("#_input").val()
	$("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['单据日期', '仓库名称', '存货编码', '存货名称', '存货代码', '规格型号', '主计量单位',
			'编码', '总(数量)', '总(含税单价)', '总(价税合计)', '总(无税单价)', '总(无税金额)', '税率', '箱规',
			'总(箱数)', '批次', '生产日期', '保质期', '失效日期'
		], //jqGrid的列显示名字
		colModel: [{
				name: 'outWhsDt',
				editable: true,
				align: 'center',
				sorttype:'date' // 时间排序

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
			},
			{
				name: 'qty',
				editable: true,
				align: 'center',
				sorttype:'integer', // 数量排序
			},
			{
				name: 'cntnTaxUprc',
				editable: false,
				align: 'center',
				sorttype:'integer',
			},
			{
				name: 'prcTaxSum',
				editable: false,
				align: 'center',
				sorttype:'integer',
			},
			{
				name: 'noTaxUprc',
				editable: true,
				align: 'center',
				sorttype:'integer',
			},
			{
				name: 'noTaxAmt',
				editable: false,
				align: 'center',
				sorttype:'integer',
			},
			{
				name: 'taxRate',
				editable: false,
				align: 'center',
				sorttype:'integer',
			},
			{
				name: 'bxRule',
				editable: false,
				align: 'center',
				sorttype:'integer',
			},
			{
				name: 'bxQty',
				editable: true,
				align: 'center',
				sorttype:'integer', // 数量排序
			},
			{
				name: 'batNum',
				editable: false,
				align: 'center',
				sorttype:'integer',
			},
			{
				name: 'prdcDt',
				editable: false,
				align: 'center',
				sorttype:'date' // 时间排序
			},
			{
				name: 'baoZhiQi',
				editable: true,
				align: 'center',
				sorttype:'integer',
			},
			{
				name: 'invldtnDt',
				editable: true,
				align: 'center',
				sorttype:'date' // 时间排序
			},
		],
		sortable:true,
		loadonce: true,
		autowidth: true,
		sortable:true,// 列拖拽
		height: height,
		autoScroll: true,
		shrinkToFit: false,
		rownumbers: true,
		forceFit: true,
		rowList: [500, 1000, 3000,5000],
		rowNum: 500, // 每页多少行，用于分页
		footerrow: true,
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "出库明细表", //表格的标题名字
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
				"outWhsDt": "总计",
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
})

//查询按钮
$(function() {
	$(".search").click(function() {
		search()
	})
})

function search(index, sortorder) {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var myData = {};

	var invtyEncd = $("#invtyEncd").val();
	var whsEncd = $("#whsEncd").val();
	var sellSnglDt1 = $(".sellSnglDt1").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var sellSnglDt2 = $(".sellSnglDt2").val();

	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"isNtChk": 1,
			"invtyEncd": invtyEncd,
			"whsEncd": whsEncd,
			"outWhsDt1": sellSnglDt1,
			"outWhsDt2": sellSnglDt2,
			"invtyClsEncd": invtyClsEncd,
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
		url: url + '/mis/purc/SellOutWhs/querySellOutWhsByInvtyEncd',
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
			alert("查询失败")
		}
	});
}


//导出
$(document).on('click', '.exportExcel', function() {
	var invtyEncd = $("#invtyEncd").val();
	var whsEncd = $("#whsEncd").val();
	var sellSnglDt1 = $(".sellSnglDt1").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var sellSnglDt2 = $(".sellSnglDt2").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"isNtChk": 1,
			"invtyEncd": invtyEncd,
			"whsEncd": whsEncd,
			"outWhsDt1": sellSnglDt1,
			"outWhsDt2": sellSnglDt2,
			"invtyClsEncd": invtyClsEncd,
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/purc/SellOutWhs/querySellOutWhsByInvtyEncdPrint',
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
			var execlName = '出库明细表'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
})