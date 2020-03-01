$(function() {
	//点击表格图标显示仓库列表
	$(document).on('click', '.biao_whsEncd', function() {
		window.open("../../Components/baseDoc/whsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	//点击表格图标显示存货列表
	$(document).on('click', '.biao_invtyEncd', function() {
		window.open("../../Components/baseDoc/invtyList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	$(document).on('click', '.biao_batNum', function() {
		window.open("../../Components/baseDoc/batNum.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})
var count;
var pages;
var page = 1;
var rowNum;
$(function() {
	pageInit();
});

function pageInit() {
	allHeight();
	jQuery("#jqGrids").jqGrid({
		url: '../../assets/js/json/order.json', //组件创建完成之后请求数据的url
		height: height,
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['仓库编码', '存货编码', '存货名称', '规格型号', '主计量单位', '现存量', '可用量', '含税单价', '未税单价',
			'含税金额', '未税金额', '批次', '生产日期', '保质期(天)', '失效日期', '区域编码', '货位编码'
		], //jqGrid的列显示名字
		colModel: [{
				name: "whsNm",
				align: "center",
				editable: true,
				sortable: false,
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
				name: 'nowStok',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'avalQty',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'cntnTaxUprc', //含税单价
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'unTaxUprc', //未税单价
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'cntnTaxAmt',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'unTaxAmt',
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
				name: 'prdcDt',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'baoZhiQi',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'invldtnDt',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'regnEncd',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'gdsBitEncd',
				align: "center",
				editable: true,
				sortable: false
			}
		],
		autowidth: true,
		viewrecords: true,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		autoScroll: true,
		shrinkToFit: false,
		rowNum: 10,
		rowList: [10, 20, 30], //可供用户选择一页显示多少条
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		caption: "库存管理", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = mydata.records //获取返回的记录数
			page = $("#jqGrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#jqGrids").getGridParam('rowNum'); //获取显示配置记录数量

			rowNum = parseInt(rowNum1)
			var total = Math.ceil(records / rowNum); 
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
	})
}
//条件查询
$(function() {
	$('#find').click(function() {
		search()
	})
})
var mydata={};
function search() {
	var rowNum1 = $("td select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var whsEncd = $("#whsEncd").val();
	var invtyEncd = $("#invtyEncd").val();
	var batNum = $("#batNum").val();

	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"whsEncd": whsEncd,
			"invtyEncd": invtyEncd,
			"batNum": batNum,
			"pageNo": page,
			"pageSize": rowNum,
		}
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/whs/invty_tab/queryInvtyTabList',
		async: true,
		data: postD2,
		dataType: 'json',
		success: function(data) {
			var list = data.respBody.list;
			mydata.rows = list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#jqGrids").jqGrid("clearGridData");
			$("#jqGrids").jqGrid("setGridParam", {
				data: mydata.rows,
				jsonReader: {
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
			console.log(error)
		}
	});

}