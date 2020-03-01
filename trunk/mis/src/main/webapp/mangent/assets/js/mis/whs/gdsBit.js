$(function() {
	//点击表格图标显示仓库列表
	$(document).on('click', '.biao_whsEncd', function() {
		window.open("../../Components/baseDoc/whsList.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	$(document).on('click', '.biao_invtyEncd', function() {
		window.open("../../Components/baseDoc/invtyList.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	$(document).on('click', '.biao_batNum', function() {
		window.open("../../Components/baseDoc/batNum.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})
var count;
var pages;
var page = 1;
var rowNum;
//表格初始化
$(function() {
	allHeight()
	$("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['仓库编码', '仓库名称', '货位编码', '货位名称', '存货编码', '存货名称', '区域编码', '区域名称', '移位编码', '货位存放量', '规格型号', '主计量编码', '计量单位名称', '批次号', '入库时间', '生产日期', '数量', '箱规', '保质期天数', '保质期预警天数', '备注', ],
		colModel: [{
				name: 'whs_encd',
				editable: false,
				align: 'center',
			},
			{
				name: 'whs_nm',
				editable: false,
				align: 'center'
			},
			{
				name: 'gds_bit_encd',
				editable: true,
				align: 'center'
			},
			{
				name: 'gds_bit_nm',
				editable: true,
				align: 'center'

			},
			{
				name: 'invty_encd',
				editable: false,
				align: 'center'
			},
			{
				name: 'invty_nm',
				editable: false,
				align: 'center'
			},
			{
				name: 'regn_encd',
				editable: false,
				align: 'center'
			},
			{
				name: 'regn_nm',
				editable: false,
				align: 'center'
			},
			{
				name: 'mov_bit_encd',
				editable: true,
				align: 'center',
			},
			{
				name: 'gds_bit_qty',
				editable: true,
				align: 'center',
			},
			{
				name: 'spc_model',
				editable: false,
				align: 'center'
			},
			{
				name: 'measr_corp_id',
				editable: false,
				align: 'center'
			},
			{
				name: 'measr_corp_nm',
				editable: true,
				align: 'center',
			},
			{
				name: 'bat_num',
				editable: true,
				align: 'center',
			},
			{
				name: 'into_dt',
				editable: false,
				align: 'center'
			},
			{
				name: 'prdc_dt',
				editable: false,
				align: 'center'
			},
			{
				name: 'qty',
				editable: true,
				align: 'center',
			},
			{
				name: 'bx_rule',
				editable: true,
				align: 'center',
			},
			{
				name: 'bao_zhi_qi_dt',
				editable: false,
				align: 'center'
			},
			{
				name: 'bao_zhi_qi_ear_warn',
				editable: false,
				align: 'center'
			},
			{
				name: 'memo',
				editable: true,
				align: 'center',
			}
		],
		autowidth: true,
		height: height,
		autoScroll: true,
		shrinkToFit: false,
		rownumbers: true,
		loadonce: false,
		forceFit: true,
		rowNum: rowNum,
		rowList: [10, 20, 30], //可供用户选择一页显示多少条
		pager: '#gdsBit_jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
//		multiselect: true, //复选框
//		multiboxonly: true,
		caption: "货位展示", //表格的标题名字
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
			search()
		},
		onSelectRow: function() {
			searchAll();
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
	var gdsBitEncd = $("#gdsBitEncd").val();
//	var regnEncd = $("#regnEncd").val();
	var whsEncd = $("input[name='whsEncd']").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var batNum = $(".batNum").val();

	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"gdsBitEncd": gdsBitEncd,
//			"regnEncd": regnEncd,
			"whsEncd": whsEncd,
			"invtyEncd": invtyEncd,
			"batNum": batNum,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/whs/gds_bit_distion/selectInvtyWhs',
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
			alert("查询失败")
		}
	});
}
function searchAll() {
	var gr = $("#jqGrids").jqGrid('getGridParam', 'selrow'); //获取行id
	var rowDatas = $("#jqGrids").jqGrid('getRowData', gr); //获取行数据
	localStorage.setItem("whsEncd",rowDatas.whs_encd)
}