var count;
var pages;
var rowNum;
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
	jQuery("#jqGrids").jqGrid({
		url: '../../assets/js/json/order.json', //组件创建完成之后请求数据的url

		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: [ '仓库名称', '仓库编码', '存货名称','存货编码', '待发货数量', '待到货数量', '调拨在途数量',
		'预计入库数量', '预计出库数量', '调拨待入数量', '调拨待出数量', '现存量', '失效日期', '生产日期', '批号', '可用量',
		'未税单价', '含税单价', '未税金额', '含税金额',
		'保质期', '主计量单位名称', '最低售价', '主计量单位编码', '重量', '箱规',
		'体积', '规格型号', '参考售价', '参考成本', '存货代码'], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'whsNm',
				align: "center",
				editable: true,
			},
			{
				name: 'whsEncd',
				align: "center",
				editable: true,
			},
			{
				name: 'invtyNm',
				align: "center",
				editable: true,
			},
			{
				name: 'invtyEncd',
				align: "center",
				editable: true,
			},
			{
				name: 'sellWhsQty',

				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'intoWhsQty',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'inTransitQty',
				align: "center",
				editable: true,
				sortable: false,

			},
			{
				name: 'othIntoQty',
				align: "center",
				editable: true,
				sortable: false,

			},
			{
				name: 'othOutQty',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'cannibIntoQty',
				align: "center",
				editable: true,
			},
			{
				name: 'cannibOutQty',
				align: "center",
				editable: true,
			},
			{
				name: 'nowStok',
				align: "center",
				editable: true,
			},
			{
				name: 'invldtnDt',
				align: "center",
				editable: true,
			},
			{
				name: 'prdcDt',
				align: "center",
				editable: true,
			},

			{
				name: 'batNum',
				align: "center",
				editable: true,
			},
			{
				name: 'avalQty',
				align: "center",
				editable: true,
			},
			{
				name: 'unTaxUprc',
				align: "center",
				editable: true,
			},
			{
				name: 'cntnTaxUprc',
				align: "center",
				editable: true,
			},
			{
				name: 'unTaxAmt',
				align: "center",
				editable: true,
			},
			{
				name: 'cntnTaxAmt',
				align: "center",
				editable: true,
			},
			{
				name: 'baoZhiQi',
				align: "center",
				editable: true,
			},
			{
				name: 'measrCorpNm',
				align: "center",
				editable: true,
			},
			{
				name: 'loSellPrc',
				align: "center",
				editable: true,
			},
			{
				name: 'measrCorpId',
				align: "center",
				editable: true,
			},
			{
				name: 'weight',
				align: "center",
				editable: true,
			},
			{
				name: 'bxRule',
				align: "center",
				editable: true,
			},
			{
				name: 'vol',
				align: "center",
				editable: true,
			},
			{
				name: 'spcModel',
				align: "center",
				editable: true,
			},
			{
				name: 'refSellPrc',
				align: "center",
				editable: true,
			},
			{
				name: 'refCost',
				align: "center",
				editable: true,
			},
			{
				name: 'invtyCd',
				align: "center",
				editable: true,
			}
		],
		autowidth: true,
		height: height,
		autoScroll: true,
		//		scroll:true,
		shrinkToFit: false,
		viewrecords: true,
		rownumbers: true,
		autoScroll: true,
		shrinkToFit: false,
		sortable: true,
		loadonce: true,
		forceFit: true,
		rowNum: 100, //一页显示多少条
		rowList: [100, 1000, 3000, 5000], //可供用户选择一页显示多少条	
		pager: '#jqgridPager', //表格页脚的占位符(一般是div)的id

		multiselect: true, //复选框
		caption: "现存量查询", //表格的标题名字
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
			var index = localStorage.getItem("index")
			var sortorder = localStorage.getItem("sortorder")
			if(index == null) {
				var index = ''
			}
			if(sortorder == null) {
				var sortorder = ''
			}
			window.newPage = page;
			loadLocalData(index, sortorder, page)
		},
		footerrow: true,
		gridComplete: function() { 
			var sellWhsQty = $("#jqGrids").getCol('sellWhsQty', false, 'sum');
			var intoWhsQty = $("#jqGrids").getCol('intoWhsQty', false, 'sum');
			var inTransitQty = $("#jqGrids").getCol('inTransitQty', false, 'sum');
			var othIntoQty = $("#jqGrids").getCol('othIntoQty', false, 'sum');
			var othOutQty = $("#jqGrids").getCol('othOutQty', false, 'sum');
			var cannibIntoQty = $("#jqGrids").getCol('cannibIntoQty', false, 'sum');
			var cannibOutQty = $("#jqGrids").getCol('cannibOutQty', false, 'sum');
			var nowStok = $("#jqGrids").getCol('nowStok', false, 'sum');
			var avalQty = $("#jqGrids").getCol('avalQty', false, 'sum');
			var unTaxAmt = $("#jqGrids").getCol('unTaxAmt', false, 'sum');
			var cntnTaxAmt = $("#jqGrids").getCol('cntnTaxAmt', false, 'sum');
			$("#jqGrids").footerData('set', { 
				"whsNm": "本页合计",
				inTransitQty: inTransitQty.toFixed(prec),
				intoWhsQty: intoWhsQty.toFixed(prec),
				sellWhsQty: sellWhsQty.toFixed(prec),
				othIntoQty: othIntoQty.toFixed(prec),
				othOutQty: othOutQty.toFixed(prec),
				cannibIntoQty: cannibIntoQty.toFixed(prec),
				cannibOutQty: cannibOutQty.toFixed(prec),
				nowStok: nowStok.toFixed(prec),
				avalQty: avalQty.toFixed(prec),
				unTaxAmt: unTaxAmt.toFixed(prec),
				cntnTaxAmt: cntnTaxAmt.toFixed(prec),
			});    
		},
		onSortCol: function(index, colindex, sortorder) {
			localStorage.setItem("index",index)
			localStorage.setItem("sortorder",sortorder)
			window.index = index;
			window.sortorder = sortorder;
			loadLocalData(index, sortorder, newPage)      
		}
	})
}

$(function() {
	//	仓库档案
	$('.whs').click(function() {
		window.open("../../Components/baseDoc/whsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');

	})

	//	存货编码invtyEncd1
	$('.inv').click(function() {
		window.open("../../Components/baseDoc/invtyList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');

	})
	//	存货编码invtyEncd1
	$('.batNum').click(function() {
		window.open("../../Components/baseDoc/batNum.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');

	})
	//	存货分类
	$(document).on('click', '.invtyClsEncd_biaoge', function() {
		window.open("../../Components/baseDoc/invtyTree.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});

})
//查询按钮
$(document).on('click', '#find', function() {
	var initial = 1;
	window.newPage = initial;
	var index = '';
	var sortorder = '';
	loadLocalData(index, sortorder,initial)
})

//条件查询
function loadLocalData(index, sortorder,page) {
	var whsEncd = $("input[name='whsEncd']").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var batNum = $("input[name='batNum']").val();
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"whsEncd": whsEncd,
			"invtyEncd": invtyEncd,
			"invtyClsEncd": invtyClsEncd,
			"batNum": batNum,
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
		url: url + '/mis/whs/invty_tab/selectExtantQtyList',
		async: true,
		data: postD2,
		dataType: 'json',
		//开始加载动画  添加到ajax里面
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
			var list = data.respBody.list;
			console.log(list)
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

var arr = [];
var obj = {}
//导出
$(document).on('click', '.exportExcel', function() {
	var whsEncd = $("input[name='whsEncd']").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var batNum = $("input[name='batNum']").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"whsEncd": whsEncd,
			"invtyEncd": invtyEncd,
			"invtyClsEncd": invtyClsEncd,
			"batNum": batNum,
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url3 + "/mis/whs/invty_tab/selectExtantQtyList",
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
			var execlName = '现存量查询'
			ExportData(list, execlName)
		},
		error: function() {
			console.log(error)
		}
	})

})