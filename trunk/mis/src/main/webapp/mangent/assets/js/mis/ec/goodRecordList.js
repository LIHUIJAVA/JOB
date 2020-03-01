var count;
var pages;
var page = 1;
var rowNum;
var cli = 0;
$(function() {
	//点击表格图标显示店铺列表
	$(document).on('click', '.storeId1_biaoge', function() {
		cli = 1
		$("#wwrap").hide()
		$("#purchaseOrder").hide()
		$(".list_box").show()
		$(".list_box").css("opacity",1)
	});
})

//刚开始时可点击的按钮
$(function() {
	$(".saveOrder").addClass("gray") //参照
	$(".gray").attr("disabled", true)
	
	$(".download").removeClass("gray")
	$("#mengban").hide()
	$(".down").hide()
})
var myData = {};
//页面初始化
$(function() {
//	allHeight()
	//初始化表格
	jQuery("#good_jqgrids_a").jqGrid({
		height: 450,
		url: '../../assets/js/json/order.json',
		autoScroll: true,
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		shrinkToFit: false,
		colNames: ['商品id', '商品编码', '店铺编码', '电商平台编码', '商品名称', '商品sku', '平台商品编码',
			'平台商品名称', '规格型号', '最低售价', '安全库存', 'sku属性', '线上状态', '是否二销', '备注'
		], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'id',
				align: "center",
				editable: true,
				sortable: false,
				hidden: true
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
				edittype: 'select',
				formatter: 'select',
				editoptions: {
					value: "0:请选择; 1:在售; 2:下架; 3:待售"
				},
			},
			{
				name: 'isSecSale',
				align: "center",
				editable: true,
				sortable: false,
				edittype: 'select',
				formatter: 'select',
				editoptions: {
					value: "2:请选择; 0:否; 1:是"
				},
			},
			{
				name: 'memo',
				align: "center",
				editable: true,
				sortable: false,
			}

		],
		rowNum: 500, //一页显示多少条
		rowList: [500, 1000, 3000, 5000],//可供用户选择一页显示多少条			
		autowidth: true,
		loadonce: true,
		multiboxonly: true,
		cellsubmit: "clientArray",
		rownumbers: true,
		pager: '#good_jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'storeId', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
//		multiselect: true, //复选框
		caption: "商品档案", //表格的标题名字	
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#good_jqgrids_a").getGridParam('records'); //获取返回的记录数
			page = $("#good_jqgrids_a").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#good_jqgrids_a").getGridParam('rowNum');

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
			search12();
		},
	});
	search12();

})


//查询按钮
$(document).on('click', '#finds_sea', function() {
	search12()
})

//条件查询
function search12() {
	var rowNum1 = $("#good_jqGridPager_center .ui-pg-selbox.ui-widget-content.ui-corner-all").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var goodId = $(".goodId").val();
	var storeId = $("input[name='storeId2']").val();
	
	var goodSku = $("input[name='goodSku']").val();
	var goodName = $("input[name='goodName']").val();
	var ecGoodId = $("input[name='ecGoodId']").val();
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
		url: url3 + "/mis/ec/goodRecord/queryList",
		data: postD2,
		dataType: 'json',
		async: true,
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
			var mydata = {};
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#good_jqgrids_a").jqGrid("clearGridData");
			$("#good_jqgrids_a").jqGrid("setGridParam", {
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
			console.log(error)
		}
	});

}
