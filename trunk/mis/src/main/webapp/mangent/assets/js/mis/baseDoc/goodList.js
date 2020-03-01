var count;
var pages;
var page = 1;
var rowNum;

//页面初始化
$(function() {
	allHeight()
	jQuery("#good_jqgrids").jqGrid({
		datatype: "local", //请求数据返回的类型。可选json,xml,txt
		colNames: ['id', '商品编码', '店铺编码', '电商平台编码', '商品名称', '商品sku', '平台商品编码',
			'平台商品名称', '规格型号', '最低售价', '安全库存', 'sku属性', '线上状态', '是否二销', '备注'
		], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'id',
				align: "center",
				editable: true,
				sortable: false,
				hidden: true,
			},
			{
				name: 'goodId',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'storeId',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'ecId',
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: 'goodName',
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: 'goodSku',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'ecGoodId',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'ecGoodName',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'goodMode',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'upsetPrice',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'safeInv',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'skuProp',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'onlineStatus',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'isSecSale',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'memo',
				align: "center",
				editable: true,
				sortable: false,
			}

		],
		rowNum: 100, //一页显示多少条
		rowList: [100, 300, 500],
		autowidth: true,
		loadonce: true,
		height:height,
		cellsubmit: "clientArray",
		rownumbers: true,
		pager: '#good_jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'storeId', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "商品列表查询", //表格的标题名字});
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#good_jqgrids").getGridParam('records'); //获取返回的记录数
			page = $("#good_jqgrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#good_jqgrids").getGridParam('rowNum'); //获取显示配置记录数量

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
	})
	search()
})

$(function() {
	$(document).on('click', '.storeId1_biaoge', function() {
		window.open("../../Components/baseDoc/storeRecordList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})
//查询按钮
$(document).on('click', '#find', function() {
	search()
})

//条件查询
function search() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var storeId = $("input[name='storeId']").val();
	var goodId = localStorage.goodId;
	var goodSku = $("input[name='goodSku']").val();
	var goodName = $("input[name='refStatusId1']").val();
	var ecGoodId = $("input[name='refStatusName1']").val();
	var ecGoodName = $("input[name='ecGoodName']").val();
	var data2 = {
		reqHead,
		"reqBody": {
			"storeId": storeId,
			"goodId": goodId,
			"goodSku": goodSku,
			"goodName": goodName,
			"ecGoodId": ecGoodId,
			"ecGoodName": ecGoodName,
			"pageNo": page,
			"pageSize": rowNum
		}

	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + "/mis/ec/goodRecord/queryList",
		data: postD2,
		dataType: 'json',
		async: true,
		success: function(data) {
			var mydata = {};
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#good_jqgrids").jqGrid("clearGridData");
			$("#good_jqgrids").jqGrid("setGridParam", {
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
			alert("查询失败")
		}
	});
}

$(document).on('click', '.sure', function() {
	//获得行号
	var gr = $("#good_jqgrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowDatas = $("#good_jqgrids").jqGrid('getRowData', gr);
	window.parent.opener.document.getElementById("goodId").value = rowDatas.goodId;
	window.parent.opener.document.getElementById("goodName").value = rowDatas.goodName;
	localStorage.setItem("goodId", rowDatas.goodId);
	localStorage.setItem("goodName", rowDatas.goodName);
	window.close()
})
$(document).on('click', '.false', function() {
	window.parent.opener.document.getElementById("goodName").value = "";
	window.parent.opener.document.getElementById("goodId").value = "";
	window.close()
	localStorage.clear();
})