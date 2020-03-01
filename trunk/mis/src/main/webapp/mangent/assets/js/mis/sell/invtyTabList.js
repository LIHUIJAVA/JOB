//库存明细表
var count;
var pages;
var page = 1;
var rowNum;
$(function() {
	//点击表格图标显示存货列表
	$(document).on('click', '.biao_invtyEncd', function() {
		window.open("../../Components/baseDoc/invtyList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	$(document).on('click', '#whsNm', function() {
		window.open("../../Components/baseDoc/whsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	$(document).on('click', '.biao_batNum', function() {
		window.open("../../Components/baseDoc/batNum.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})
//表格初始化
$(function() {
	allHeight();
	localStorage.removeItem("whsEncd");

	$("#jqGrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json",
		colNames: ["存货编码", '存货名称', '仓库名称', '批次', '现存量', '可用量', '无税单价', '无税金额', '含税单价', '含税金额', '税率', ],
		colModel: [{
				name: 'invtyEncd',
				editable: true,
				align: 'center',
			},
			{
				name: 'invtyNm',
				editable: true,
				align: 'center',
			},
			{
				name: 'whsNm',
				editable: true,
				align: 'center'

			},
			{
				name: 'batNum',
				editable: true,
				align: 'center'

			},
			{
				name: 'nowStok',
				editable: true,
				align: 'center'

			},
			{
				name: 'avalQty',
				editable: false,
				align: 'center',
			},
			{
				name: 'unTaxUprc',
				editable: false,
				align: 'center'
			},
			{
				name: 'unTaxAmt',
				editable: false,
				align: 'center'
			},
			{
				name: 'cntnTaxUprc',
				editable: false,
				align: 'center'
			},
			{
				name: 'cntnTaxAmt',
				editable: true,
				align: 'center'
			},
			{
				name: 'TaxRate',
				editable: true,
				align: 'center'
			},
		],
		sortable:true,
		autowidth: true,
		height: height,
		autoScroll: true,
		shrinkToFit: false,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 500,
		rowList: [500, 1000, 3000,5000], //可供用户选择一页显示多少条
		pager: '#jqgridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		multiboxonly: true,
		caption: "库存明细表", //表格的标题名字
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
})

//查询按钮
$(document).on('click', '.search', function() {
	search()
})

function search() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)

	var invtyEncd = localStorage.invtyEncd;
	var whsEncd = localStorage.whsEncd;
	var batNum = $(".batNum").val();
	var isNtChk = $("#isNtChk").val();

	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": invtyEncd,
			"whsEncd": whsEncd,
			"batNum": batNum,
			"isNtChk": isNtChk,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/whs/invty_tab/queryInvtyTabList',
		async: true,
		data: saveData,
		dataType: 'json',
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
			alert(error)
		}
	});

}