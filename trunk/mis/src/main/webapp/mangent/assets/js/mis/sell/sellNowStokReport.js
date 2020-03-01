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
function search() {
	var whsEncd = $("input[name='whsEncd']").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var batNum = $("input[name='batNum']").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"whsEncd": whsEncd,
			"invtyEncd": invtyEncd,
			"batNum": batNum,
			"invtyClsEncd": invtyClsEncd,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/sell/Report/sellNowStokReport',
		async: true,
		data: postD2,
		dataType: 'json',
		//开始加载动画  添加到ajax里面
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");
		},
		success: function(data) {
			localStorage.removeItem("custId")
			localStorage.removeItem("deptId")
			var list = data.respBody.list;
			var mydata = {};
			mydata.rows = list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#sellNowStokReport_jqGrids").jqGrid("clearGridData");
			$("#sellNowStokReport_jqGrids").jqGrid("setGridParam", {
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
	jQuery("#sellNowStokReport_jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['仓库编码', '仓库名称', '存货编码', '存货名称', '规格型号','计量单位名称',
		'箱规','现存量','可用数量','待入库数量','待发货数量','调拨待入数量','批号','生产日期'
		,'保质期'
		,'失效日期','参考售价','最低售价','调拨待出数量','无税单价','无税金额','重量','体积'], //jqGrid的列显示名字
		colModel: [{
				name: 'whsEncd',
				align: "center",
				editable: true,
			},
			{
				name: "whsNm",
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
				name: 'bxRule',
				align: "center",
				editable: true,
			},
			{
				name: 'nowStok',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'avalQty',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'intoWhsQty',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'sellWhsQty',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'outIntoQty',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'batNum',
				align: "center",
				editable: true,
			},
			{
				name: 'prdcDt',
				align: "center",
				editable: true,
				sorttype:'date',
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
				sorttype:'date',
			},
			{
				name: 'refSellPrc',
				align: "center",
				editable: true,
			},
			{
				name: 'loSellPrc',
				align: "center",
				editable: true,
			},
			{
				name: 'outWhsQty',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'noTaxUprc',
				align: "center",
				editable: true,
			},
			{
				name: 'noTaxAmt',
				align: "center",
				editable: true,
			},
			{
				name: 'weight',
				align: "center",
				editable: true,
			},
			{
				name: 'vol',
				align: "center",
				editable: true,
			}
		],
		sortable:true,
		autowidth: true,
		rownumbers: true,
		autoScroll:true,
		shrinkToFit:false,
		height:height,
		sortable:true,// 列拖拽
		loadonce: true,
		forceFit: true,
		rowNum: 500, //一页显示多少条
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条	
		pager: '#sellNowStokReport_jqgridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "销售现存量报表", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#sellNowStokReport_jqGrids").getGridParam('records'); //获取返回的记录数
			page = $("#sellNowStokReport_jqGrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#sellNowStokReport_jqGrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			var qty = $("#sellNowStokReport_jqGrids").getCol('qty', false, 'sum');
			var nowStok = $("#sellNowStokReport_jqGrids").getCol('nowStok', false, 'sum');
			var avalQty = $("#sellNowStokReport_jqGrids").getCol('avalQty', false, 'sum');
			var intoWhsQty = $("#sellNowStokReport_jqGrids").getCol('intoWhsQty', false, 'sum');
			var sellWhsQty = $("#sellNowStokReport_jqGrids").getCol('sellWhsQty', false, 'sum');
			var outIntoQty = $("#sellNowStokReport_jqGrids").getCol('outIntoQty', false, 'sum');
			var outWhsQty = $("#sellNowStokReport_jqGrids").getCol('outWhsQty', false, 'sum');
//			var prcTaxSum = $("#sellNowStokReport_jqGrids").getCol('prcTaxSum', false, 'sum');
			
			$("#sellNowStokReport_jqGrids").footerData('set', { 
				"whsEncd": "本页合计",
				nowStok:nowStok.toFixed(prec),
				avalQty: avalQty.toFixed(prec),
				intoWhsQty:intoWhsQty.toFixed(prec),
				sellWhsQty: sellWhsQty.toFixed(prec),
				outIntoQty:outIntoQty.toFixed(prec),
				outWhsQty: outWhsQty.toFixed(prec),
//				prcTaxSum: precision(prcTaxSum,2),
				
			}          );    
		},
	})
//	search()
}

//导出
$(document).on('click', '.exportExcel', function() {
	var whsEncd = $("input[name='whsEncd']").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var batNum = $("input[name='batNum']").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
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
		url: url + "/mis/sell/Report/sellNowStokReport",
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
			var execlName = '销售现存量报表'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
	
})