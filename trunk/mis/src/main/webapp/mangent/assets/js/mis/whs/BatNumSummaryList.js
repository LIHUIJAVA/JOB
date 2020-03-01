var count;
var pages;
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
})
$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	pageInit();
});

//查询按钮
$(document).on('click', '#find', function() {
	var initial = 1;
	window.newPage = initial;
	var index = '';
	var sortorder = '';
	search(index, sortorder,newPage)
})

//条件查询
function search(index, sortorder,page) {
	var formDt1 = $("input[name='formDt1']").val();
	var formDt2 = $("input[name='formDt2']").val();
	var chkTm1 = $("input[name='chkTm1']").val();
	var chkTm2 = $("input[name='chkTm2']").val();
	var whsEncd = $("input[name='whsEncd']").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var batNum = $("input[name='batNum']").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"formDt1": formDt1,
			"formDt2": formDt2,
			"chkTm1": chkTm1,
			"chkTm2": chkTm2,
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
		url: url + '/mis/whs/invty_tab/queryBatNumSummaryList',
		async: true,
		data: postD2,
		dataType: 'json',
		//开始加载动画  添加到ajax里面
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");
		},
		success: function(data) {
			var mydata = {};
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#bsl_jqGrids").jqGrid("setGridParam", {
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
	localStorage.removeItem("index")
	localStorage.removeItem("sortorder")
	allHeight()
	jQuery("#bsl_jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['仓库编码', '仓库名称', '存货编码', '存货名称', '批号', '规格型号', '主计量单位', '存货分类', '生产日期',
		'保质期', '失效日期', ' 箱规', '期初结存数量', '期初结存金额(无税)', '期初结存金额(含税)', '总计入库金额(含税)', '总计入库金额(无税)', '总计入库数量', '总计出库数量', '总计出库金额(无税)', '总计出库金额(含税)',
		'期末结存金额（含税）','期末结存金额（无税）','期末结存数量'], //jqGrid的列显示名字
		colModel: [{
				name: 'whsEncd',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: "whsNm",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: "invtyEncd",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: "invtyNm",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'batNum',
				align: "center",
				editable: true,
				sortable: false
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
			},
			{
				name: 'invtyClsNm',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'prdcDt',
				align: "center",
				editable: true,
			},
			{
				name: 'baoZhiQi',
				align: "center",
				editable: true,
			},
			{
				name: 'invldtnDt',
				align: "center",
				editable: true,
			},
			{
				name: 'bxRule',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'qiChuQty',
				align: "center",
				editable: true,
			},
			{
				name: 'qiChuAmt',
				align: "center",
				editable: true,
			},
			{
				name: 'qiChuSum',
				align: "center",
				editable: true,
			},
			{
				name: 'intoSum',
				align: "center",
				editable: true,
			},
			{
				name: 'intoAmt',
				align: "center",
				editable: true,
			},
			{
				name: 'intoQty',
				align: "center",
				editable: true,
			},
			{
				name: 'outQty',
				align: "center",
				editable: true,
			},
			{
				name: 'outAmt',
				align: "center",
				editable: true,
			},
			{
				name: 'outSum',
				align: "center",
				editable: true,
			},
			{
				name: 'jieChuSum',
				align: "center",
				editable: true,
			},
			{
				name: 'jieChuAmt',
				align: "center",
				editable: true,
			},
			{
				name: 'jieCunQty',
				align: "center",
				editable: true,
			}
		],
		autowidth:true,
		rownumbers:true,
		autoScroll:true,
		shrinkToFit:false,
		height:height,
		loadonce: true,
		forceFit: true,
		rowNum: 500, //一页显示多少条
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条	
		pager: '#bsl_jqgridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "批次存货汇总表", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#bsl_jqGrids").getGridParam('records'); //获取返回的记录数
			page = $("#bsl_jqGrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#bsl_jqGrids").getGridParam('rowNum'); //获取显示配置记录数量

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
		gridComplete: function() { 
			var qiChuQty = $("#bsl_jqGrids").getCol('qiChuQty', false, 'sum');
			var qiChuAmt = $("#bsl_jqGrids").getCol('qiChuAmt', false, 'sum');
			var qiChuSum = $("#bsl_jqGrids").getCol('qiChuSum', false, 'sum');
			var intoSum = $("#bsl_jqGrids").getCol('intoSum', false, 'sum');
			var intoAmt = $("#bsl_jqGrids").getCol('intoAmt', false, 'sum');
			var intoQty = $("#bsl_jqGrids").getCol('intoQty', false, 'sum');
			var outQty = $("#bsl_jqGrids").getCol('outQty', false, 'sum');
			var outAmt = $("#bsl_jqGrids").getCol('outAmt', false, 'sum');
			var outSum = $("#bsl_jqGrids").getCol('outSum', false, 'sum');
			var jieChuSum = $("#bsl_jqGrids").getCol('jieChuSum', false, 'sum');
			var jieChuAmt = $("#bsl_jqGrids").getCol('jieChuAmt', false, 'sum');
			var jieCunQty = $("#bsl_jqGrids").getCol('jieCunQty', false, 'sum');
			
			$("#bsl_jqGrids").footerData('set', { 
				"whsEncd": "本页合计",
				qiChuQty: qiChuQty.toFixed(prec),
				qiChuAmt : precision(qiChuAmt,2),
				qiChuSum : precision(qiChuSum,2),
				intoSum : precision(intoSum,2),
				intoAmt : precision(intoAmt,2),
				intoQty:intoQty.toFixed(prec),
				outQty: outQty.toFixed(prec),
				outAmt : precision(outAmt,2),
				outSum : precision(outSum,2),
				jieChuSum : precision(jieChuSum,2),
				jieChuAmt : precision(jieChuAmt,2),
				jieCunQty: jieCunQty.toFixed(prec),
				
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
//	search()
}

var arr=[];
var obj={}
//导出
$(document).on('click', '.exportExcel', function() {
	var formDt1 = $("input[name='formDt1']").val();
	var formDt2 = $("input[name='formDt2']").val();
	var chkTm1 = $("input[name='chkTm1']").val();
	var chkTm2 = $("input[name='chkTm2']").val();
	var whsEncd = $("input[name='whsEncd']").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var batNum = $("input[name='batNum']").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"formDt1": formDt1,
			"formDt2": formDt2,
			"chkTm1": chkTm1,
			"chkTm2": chkTm2,
			"whsEncd": whsEncd,
			"invtyEncd": invtyEncd,
			"batNum": batNum,
			"invtyClsEncd": invtyClsEncd
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url3 + "/mis/whs/invty_tab/queryBatNumSummaryList",
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
			var execlName = '批次存货汇总表'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
	
})