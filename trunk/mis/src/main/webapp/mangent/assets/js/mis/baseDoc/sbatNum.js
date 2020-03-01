var count;
var pages;
var page = 1;
var rowNum;

$(function() {
	pageInit();
})

function pageInit() {
	jQuery("#batNum_jqgrids").jqGrid({
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		url: '../../assets/js/json/order.json',
		colNames: ['批次', '现存量', '可用量', '生产日期', '保质期', '失效日期','无税单价'], //jqGrid的列显示名字
		colModel: [{
				name: 'batNum',
				align: "center",
				editable: true,
			},
			{
				name: "nowStok",
				align: "center",
				editable: true,
			},
			{
				name: "avalQty",
				align: "center",
				editable: true,
			},
			{
				name: "prdcDt",
				align: "center",
				editable: true,
			},
			{
				name: "baoZhiQi",
				align: "center",
				editable: true,
			},
			{
				name: 'invldtnDt',
				align: "center",
				editable: true,
			},
			{
				name: 'unTaxUprc',
				align: "center",
				editable: true,
			}
		],
		autowidth: true,
		rownumbers: true,
		loadonce: true,
		height: 320,
		forceFit: true,
		rowList: [500, 1000, 3000,5000],
		rowNum: 500,
		pager: '#batNum_jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		multiboxonly: true,
		caption: "批次", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#batNum_jqgrids").getGridParam('records'); //获取返回的记录数
			page = $("#batNum_jqgrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#batNum_jqgrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			if(localStorage.getItem('invtyEncd') != null) {
				var invtyEncd = localStorage.invtyEncd
			}
			if(localStorage.getItem('whsEncd') != null) {
				var whsEncd = localStorage.whsEncd
			}
			searcherBatNum(whsEncd, invtyEncd)
		},
	});
}
//var invtyClsEncd;

function searcherBatNum(whsEncd, invtyEncd) {
	if(localStorage.getItem('invtyEncd') != null) {
		var invtyEncd = localStorage.invtyEncd
	}
	if(localStorage.getItem('whsEncd') != null) {
		var whsEncd = localStorage.whsEncd
	}
	var rowNum1 = $("#gbox_batNum_jqgrids td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var myData = {};
	var batNum = $("input[name='batNum6']").val()
	var Data = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": invtyEncd,
			"batNum": batNum,
			"whsEncd": whsEncd,
			"pageNo": page,
			"pageSize": rowNum
		}
	}
	var changeData = JSON.stringify(Data)
	$.ajax({
		type: "post",
		url: url + "/mis/whs/invty_tab/queryInvtyTabList",
		async: true,
		data: changeData,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		success: function(data) {
			var arr = eval(data); //数组
			var list = arr.respBody.list;
			var arr1 = [];
			var length = list.length;
			for(var i = 0; i < length; i++) {
				arr1.push(list[i]);
			}
			var arr2 = [];

			function fun(arr) {
				for(var i = 0; i < arr.length; i++) {
					if(Array.isArray(arr[i])) {
						fun(arr[i]);
					} else {
						arr2.push(arr[i]);
					}
				}
			}
			fun(arr1);
			//			var arr2 = arr1.flat(Infinity)
			var rootData = JSON.stringify(arr2);

			var obj = JSON.parse(rootData)
			myData = obj;
			var mydata = {};
			mydata.rows = myData;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#batNum_jqgrids").jqGrid("clearGridData");
			$("#batNum_jqgrids").jqGrid("setGridParam", {
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
		error: function() {
			alert("条件查询失败")
		}
	});
	$("#batNum_jqgrids").jqGrid('clearGridData')
}

$(function() {
	$(".batNum_search").click(function() {
		searcherBatNum()
	})
})

$(function() {
	$(".true1").click(function() {
		//获得行号
		var gr = $("#batNum_jqgrids").jqGrid('getGridParam', 'selarrrow');
		if(gr.length == 0) {
			alert("请选择单据")
		} else {
			var rowData = [];
			var min = [];
			for(i = 0; i < gr.length; i++) {
				var rowDatas = $("#batNum_jqgrids").jqGrid('getRowData', gr[i]); //获取行数据
				var dat = rowDatas.batNum
				min[i] = dat
				//选中行的id
				var jstime = $("#batNum_jqgrids").getCell(gr[i], "batNum");
				rowData[i] = jstime
			}
			var rowIds = rowData.toString();
			var mins = min.join(',')
			window.parent.opener.document.getElementById("sbatNum").value = mins;
			window.close()
		}
	})
	$(".false1").click(function() {
		window.parent.opener.document.getElementById("sbatNum").value = "";
		window.close()
		localStorage.removeItem(batNum);
	})
})