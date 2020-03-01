$(function() {
	//点击表格图标显示存货列表
	$(document).on('click', '.biao_invtyEncd', function() {
		window.open("../../Components/baseDoc/invtyList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	//点击表格图标显示仓库列表
	$(document).on('click', '.biao_whsEncd', function() {
		window.open("../../Components/baseDoc/whsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	//点击表格图标显示批次列表
	$(document).on('click', '.biao_batNum', function() {
		window.open("../../Components/baseDoc/batNum.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})
var count;
var pages;
var rowNum;
$(function() {
	pageInit();
});

function pageInit() {
	localStorage.removeItem("invtyEncd");
	localStorage.removeItem("whsEncd");
	localStorage.removeItem("index")
	localStorage.removeItem("sortorder")
	allHeight();
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	jQuery("#summary_jqGrids").jqGrid({
		url: '../../assets/js/json/order.json', //组件创建完成之后请求数据的url
		height: height - 30,
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['编码', '名称', '编码', '名称', '分类', '批次', '规格型号', 
		'计量单位', '结存数量', '结存金额(无税)', '结存数量', '结存金额（无税）', 
		'数量', '金额(无税)', '数量', 
		'金额(无税)'],
		colModel: [{
				name: "whsEncd",
				align: "center",
				sortable: false,
			},
			{
				name: "whsNm",
				align: "center",
				sortable: false
			},
			{
				name: "invtyEncd",
				align: "center",
				sortable: false
			},
			{
				name: 'invtyNm',
				align: "center",
				sortable: false
			},
			{
				name: 'invtyClsNm',
				align: "center",
				sortable: false
			},
			{
				name: 'batNum',
				align: "center",
				sortable: false
			},
			{
				name: 'spcModel',
				align: "center",
				sortable: false
			},
			{
				name: 'measrCorpNm',
				align: "center",
				sortable: false,
			},
			{
				name: 'qiChuQty',
				align: "center",
			},
			{
				name: 'qiChuAmt',
				align: "center",
			},
			{
				name: 'jieCunQty',
				align: "center",
			},
			{
				name: 'jieChuAmt',
				align: "center",
			},
			{
				name: 'intoQty',
				align: "center",
			},
			{
				name: 'intoAmt',
				align: "center",
			},
			{
				name: 'outQty',
				align: "center",
			},
			{
				name: 'outAmt',
				align: "center",
			}
		],
		autowidth: true,
		viewrecords: true,
		autoScroll: true,
		shrinkToFit: false,
		rownumbers: true,
		loadonce: true,
		rowNum:500,
		forceFit: true,
		rowList: [500, 1000, 3000,5000], //可供用户选择一页显示多少条
		pager: '#jqgridPager', //表格页脚的占位符(一般是div)的id
		multiselect: true, //复选框
		caption: "收发存汇总表", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#summary_jqGrids").getGridParam('records'); //获取返回的记录数
			page = $("#summary_jqGrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#summary_jqGrids").getGridParam('rowNum'); //获取显示配置记录数量

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
				var sortorder = ''
			}
			window.newPage = page;
			search(index, sortorder, page)
		},
		footerrow: true,
		sortable:true,
		gridComplete: function() { 
			var qiChuQty = $("#summary_jqGrids").getCol('qiChuQty', false, 'sum');
			var qiChuAmt = $("#summary_jqGrids").getCol('qiChuAmt', false, 'sum');
			var intoAmt = $("#summary_jqGrids").getCol('intoAmt', false, 'sum');
			var intoQty = $("#summary_jqGrids").getCol('intoQty', false, 'sum');
			var outQty = $("#summary_jqGrids").getCol('outQty', false, 'sum');
			var outAmt = $("#summary_jqGrids").getCol('outAmt', false, 'sum');
			var jieChuAmt = $("#summary_jqGrids").getCol('jieChuAmt', false, 'sum');
			var jieCunQty = $("#summary_jqGrids").getCol('jieCunQty', false, 'sum');
			
			$("#summary_jqGrids").footerData('set', { 
				"whsEncd": "本页合计",
				qiChuQty: qiChuQty.toFixed(prec),
				qiChuAmt : precision(qiChuAmt,2),
				intoAmt: intoAmt.toFixed(prec),
				intoQty : precision(intoQty,2),
				outQty: outQty.toFixed(prec),
				outAmt : precision(outAmt,2),
				jieCunQty: jieCunQty.toFixed(prec),
				jieChuAmt : precision(jieChuAmt,2),
				
			}          );    
		},
		onSortCol: function(index, colindex, sortorder) {
			localStorage.setItem("index",index)
			localStorage.setItem("sortorder",sortorder)
			window.index = index;
			window.sortorder = sortorder;
			search(index, sortorder, newPage)
		}
	})
	jQuery("#summary_jqGrids").jqGrid('setGroupHeaders', {
		useColSpanStyle: false,
		groupHeaders: [{
				startColumnName: 'whsEncd',
				numberOfColumns: 2,
				titleText: '<em>仓库</em>'
			},
			{
				startColumnName: 'invtyEncd',
				numberOfColumns: 3,
				titleText: '<em>存货</em>'
			},
			{
				startColumnName: 'qiChuQty',
				numberOfColumns: 2,
				titleText: '<em>期初</em>'
			},
			{
				startColumnName: 'intoQty',
				numberOfColumns: 2,
				titleText: '<em>入库</em>'
			},
			{
				startColumnName: 'outQty',
				numberOfColumns: 2,
				titleText: '<em>出库</em>'
			},
			{
				startColumnName: 'jieCunQty',
				numberOfColumns: 2,
				titleText: '<em>期末</em>'
			}
		]
	});
	//	鼠标指向变色
	$("table").delegate(".jqgrow", "mouseover", function() {
		$(this).css("background-color", "rgb(252, 248, 227)");
	});
	$("table").delegate(".jqgrow", "mouseout", function() {
		$(this).css("background-color", "");
	});

	//点击表格图标显示存货列表
	$(document).on('click', '.invtyClsEncd', function() {
		window.open("../../Components/baseDoc/invtyTree.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
}

$(document).on('click', '#find', function() {
	var initial = 1;
	window.newPage = initial;
	var index = '';
	var sortorder = '';
	search(index, sortorder,initial)
})

function search(index, sortorder, page){
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var formDt1 = $("input[name='formDt1']").val();
	var formDt2 = $("input[name='formDt2']").val();
	var chkTm1 = $("input[name='chkTm1']").val();
	var chkTm2 = $("input[name='chkTm2']").val();
	var batNum = $("input[name='batNum']").val();

	var invtyEncd = $("input[name='invtyEncd']").val();
	var whsEncd = $("input[name='whsEncd']").val();
	var invtyCls = $("input[name='invtyCls']").val();
	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"chkTm1": chkTm1,
			"chkTm2": chkTm2,
			"formDt1": formDt1,
			"formDt2": formDt2,
			"whsEncd": whsEncd,
			"invtyClsEncd": invtyCls,
			"invtyEncd": invtyEncd,
			"batNum": batNum,
			"sort":index,
			"sortOrder":sortorder,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(data2);
	if(formDt1 != '' && formDt2 != '') {
		$.ajax({
			type: "post",
			contentType: 'application/json; charset=utf-8',
			url: url + '/mis/whs/invty_tab/selectTSummaryList',
			async: true,
			data: saveData,
			dataType: 'json',
			//开始加载动画  添加到ajax里面
			beforeSend: function() {
				$(".zhezhao").css("display", "block");
				$("#loader").css("display", "block");
	
			},
			success: function(data) {
				var list = data.respBody.list;
				var mydata = {};
				myDate = list;
				mydata.rows = myDate;
				mydata.page = page;
				mydata.records = data.respBody.count;
				mydata.total = data.respBody.pages;
				$("#summary_jqGrids").jqGrid("clearGridData");
				$("#summary_jqGrids").jqGrid("setGridParam", {
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
	} else {
		alert("请选择单据日期")
	}
}

var arr=[];
var obj={}
//导出
$(document).on('click', '.exportExcel', function() {
	var formDt1 = $("input[name='formDt1']").val();
	var formDt2 = $("input[name='formDt2']").val();
	var chkTm1 = $("input[name='chkTm1']").val();
	var chkTm2 = $("input[name='chkTm2']").val();
	var batNum = $("input[name='batNum']").val();

	var invtyEncd = $("input[name='invtyEncd']").val();
	var whsEncd = $("input[name='whsEncd']").val();
	var invtyCls = $("input[name='invtyCls']").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"chkTm1": chkTm1,
			"chkTm2": chkTm2,
			"formDt1": formDt1,
			"formDt2": formDt2,
			"whsEncd": whsEncd,
			"invtyCls": invtyCls,
			"invtyEncd": invtyEncd,
			"batNum": batNum,
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + "/mis/whs/invty_tab/selectTSummaryList",
		type: 'post',
		data: Data,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		//开始加载动画  添加到ajax里面
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");

		},
		success: function(data) {
			var list = data.respBody.list;
			var execlName = '收发存汇总表'
			ExportData(list, execlName)
		},
		//结束加载动画
		complete: function() {
			$(".zhezhao").css("display", "none");
			$("#loader").css("display", "none");
		},
		error: function() {
			alert("导出失败")
		}
	})
	
})