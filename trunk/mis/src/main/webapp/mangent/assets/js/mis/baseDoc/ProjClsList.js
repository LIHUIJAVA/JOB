var count;
var pages;
var page = 1;
var rowNum;
var mType = 0;

//刚开始时可点击的按钮
$(function() {
	$(".saveOrder").addClass("gray") //参照
	$(".gray").attr("disabled", true)
	
	$(".pro_sure").removeClass("gray")
	$(".pro_cancel").removeClass("gray")
	$(".search_pro").removeClass("gray")
})

var myData = {};
//页面初始化
$(function() {
	allHeight()
	//初始化表格
	jQuery("#insertProjCls_jqgrids").jqGrid({
		height: height,
		autoScroll: true,
		datatype: "local", //请求数据返回的类型。可选json,xml,txt
		shrinkToFit: false,
		colNames: ['序号','项目编码','项目名称', '备注'], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'ordrNum',
				align: "center",
				editable: false,
				sortable: false,
				hidden:true
			},
			{
				name: 'projEncd',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'projNm',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'memo',
				align: "center",
				editable: true,
				sortable: false,
			}
		],
		rowList: [50, 100, 300], //可供用户选择一页显示多少条			
		autowidth: true,
		sortable:true,
		loadonce: true,
		rowNum: 50,
		rownumWidth: 15,  //序列号列宽度
		multiboxonly: true,
		cellsubmit: "clientArray",
		rownumbers: true,
		pager: '#insertProjCls_jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'storeId', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		caption: "商品档案", //表格的标题名字	
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#insertProjCls_jqgrids").getGridParam('records'); //获取返回的记录数
			page = $("#insertProjCls_jqgrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#insertProjCls_jqgrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			search6()
		},

	});
	search6()
})

//查询按钮
$(document).on('click', '.search_pro', function() {
	search6()
})

//条件查询
function search6() {
	var projEncd = $("input[name='projEncd1']").val();
	var projNm = $("input[name='projNm1']").val();
	
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var data2 = {
		reqHead,
		"reqBody": {
			"projEncd": projEncd,
			"projNm": projNm,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url3 + "/mis/ec/projCls/queryList",
		data: postD2,
		dataType: 'json',
		async: true,
		success: function(data) {
			var mydata = {};
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#insertProjCls_jqgrids").jqGrid("clearGridData");
			$("#insertProjCls_jqgrids").jqGrid("setGridParam", {
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
			alert("error")
		}
	});

}
