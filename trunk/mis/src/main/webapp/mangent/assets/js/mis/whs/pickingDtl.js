var count;
var pages;
var page = 1;
var rowNum;
$(function() {
	allHeight();
	pageInit();
});

function pageInit() {
	jQuery("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['单据号', '仓库名称', '存货编码', '存货名称', '规格型号', '主计量单位', '条形码', '批次', '生产日期', '保质期', '失效日期', '拣货日期', '货位', '销售类型', '业务类型', '数量', '制作人'], //jqGrid的列显示名字
		colModel: [{
				name: "pickSnglNum",
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: "whsNm",
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: "invtyEncd",
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'invtyNm',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'spcModel',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'measrCorpNm',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'crspdBarCd',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'batNum',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'prdcDt',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'baoZhiQi',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'invldtnDt',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'pickSnglTm',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'gdsBitNm',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'sellTypNm',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'bizTypNm',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'qty',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'pickPers',
				align: "center",
				editable: false,
				sortable: false
			}
		],
		rowNum: 10,
		autowidth: true,
		viewrecords: true,
		rownumbers: true,
		loadonce: true,
		autoScroll: true,
		shrinkToFit: false,
		forceFit: true,
		rowList: [10, 20, 30], //可供用户选择一页显示多少条
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		height: height,
		multiselect: true, //复选框
		caption: "拣货明细表", //表格的标题名字
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
			chaxun()
		},
	})
}
$(function() {
	$("#finish").click(function() {
		var pickSnglNum = localStorage.pickSnglNum
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				"pickSnglNum": pickSnglNum
			}
		};
		var Data = JSON.stringify(data);
		$.ajax({
			url: url + '/mis/whs/pick_sngl/updateTabPC',
			type: 'post',
			data: Data,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			success: function(data) {
				alert(data.respHead.message);
			},
			error: function() {
				console.log(error)
			}
		})

	})
})

//查询详细信息
$(function() {
	var afterUrl = window.location.search.substring(1);
	var b = [];
	b = afterUrl;
	var a = 1
	if(a == b) {
		chaxun()
	}
})

function chaxun() {
	var pickSnglNum = localStorage.pickSnglNum
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"pickSnglNum": pickSnglNum,
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/whs/pick_sngl/selectPSubTabById',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			var list = data.respBody.list;
			var myData = data.respBody;
			for(var i = 0; i < list.length; i++) {
				$.extend(list[i], myData)
				delete list[i].list
			}
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
		}
	})

}