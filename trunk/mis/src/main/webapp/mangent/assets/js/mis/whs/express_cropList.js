var count;
var pages;
var page = 1;
var rowNum;
$(function(){
	pageInit7();	
})

function pageInit7(){
	allHeight()
//初始化表格
	jQuery("#ex_jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		height: height,
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['快递公司编码', '快递公司名称', '快递类型', '是否支持到货付款', '货到付款服务费', '是否停用', '首重', '首重起价', '备注', '创建人', '修改人'],
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'expressEncd',
				align: "center",
				index: 'invdate',
				editable: false,
			},
			{
				name: 'expressNm',
				align: "center",
				index: 'id',
				editable: true,
			},
			{
				name: 'expressTyp',
				align: "center",
				index: 'invdate',
				editable: true,
			},
			{
				name: 'isNtSprtGdsToPay',
				align: "center",
				index: 'invdate',
				editable: true,
				edittype: 'select',
				formatter: 'select',
				editoptions: {
					value: "0:否;1:是"
				},
			},
			{
				name: 'gdsToPayServCost',
				align: "center",
				index: 'invdate',
				editable: true,
			},
			{
				name: 'isNtStpUse',
				align: "center",
				index: 'invdate',
				editable: true,
				edittype: 'select',
				formatter: 'select',
				editoptions: {
					value: "0:否;1:是"
				},
			},
			{
				name: 'firstWet',
				align: "center",
				index: 'invdate',
				editable: true,
			},
			{
				name: 'firstWetStrPrice',
				align: "center",
				index: 'invdate',
				editable: true,
			},
			{
				name: 'memo',
				align: "center",
				index: 'invdate',
				editable: true,
			},
			{
				name: 'setupPers',
				align: "center",
				index: 'invdate',
				editable: false,
			},
			{
				name: 'mdfr',
				align: "center",
				index: 'invdate',
				editable: false,
			},
	
		],
		rowNum: 100, //一页显示多少条
		rowList: [100, 200, 300], //可供用户选择一页显示多少条			
		autowidth: true,
		rownumbers: true,
		pager: '#ex_jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'expressEncd', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		autoScroll: true,
		shrinkToFit: false,
		caption: "快递公司列表查询", //表格的标题名字	
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#ex_jqgrids").getGridParam('records'); //获取返回的记录数
			page = $("#ex_jqgrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#ex_jqgrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			search7()
		}
	});
	search7()
}

$(document).on('click', '#findg', function() {
	search7()
})

function search7() {
	var rowNum1 = $("#gbox_ex_jqgrids td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var expressEncd = $("input[name='expressEncd1']").val();
	var expressNm = $("input[name='expressNm1']").val();
	var expressTyp = $("input[name='expressTyp1']").val();    
	var setupPers = $("input[name='setupPers1']").val();
	
	var data2 = {			
		reqHead,
		"reqBody":{
			"expressEncd" : expressEncd,
			"expressNm":expressNm,
			"expressTyp" : expressTyp,				
			"setupPers" : setupPers,
			"pageNo": page,
			"pageSize": rowNum
		}			
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		url:url3+"/mis/whs/express_crop/queryList",
		contentType: 'application/json; charset=utf-8',
		data: postD2,
		dataType: 'json',
		async: true,
		success: function(data) {
			var mydata = {};
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#ex_jqgrids").jqGrid("clearGridData");
			$("#ex_jqgrids").jqGrid("setGridParam", {
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
};


$(document).on('click', '.true', function() {
	//获得行号
	var gr = $("#ex_jqgrids").jqGrid('getGridParam', 'selrow');
	if(gr == null) {
		alert("未选择")
	} else  {
		window.localStorage.removeItem("expressEncd")
		//获得行数据
		var rowDatas = $("#ex_jqgrids").jqGrid('getRowData', gr);
		window.parent.opener.document.getElementById("expressName").value= rowDatas.expressNm;
		window.parent.opener.document.getElementById("expressId").value= rowDatas.expressEncd;
		localStorage.setItem("expressEncd",rowDatas.expressEncd)
		window.close()
		localStorage.setItem("outInEncd", 0)
	}
})

$(document).on('click', '.false', function() {
	localStorage.clear();
	window.parent.opener.document.getElementById("expressName").value= "";
	window.parent.opener.document.getElementById("expressId").value= "";
	window.close()
})
