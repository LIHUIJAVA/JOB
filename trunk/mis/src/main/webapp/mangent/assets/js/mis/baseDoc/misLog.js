
var count;
var pages;
var page = 1;
var rowNum;

$(function() {
	pageInit();
})

//表格初始化
function pageInit() {
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	allHeight()
	jQuery("#jqgrids").jqGrid({
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		url: '../../assets/js/json/order.json',
		colNames: ['id', '操作人id', '操作人IP', '操作类型名称', '操作模块名称', '操作时间', '操作状态', '操作结果','账套登陆时间','操作账套', '操作url', '备注'], //jqGrid的列显示名字
		colModel: [{
				name: "id",
				align: "center",
				sortable: false,
				hidden:true
			},
			{
				name: "accNum",
				align: "center",
				sortable: false,
			},
			{
				name: "accNumIp", 
				align: "center",
				sortable: false,
			},
			{
				name: 'operationName',
				align: "center",
				sortable: false,
			},
			{
				name: 'operationModule',
				align: "center",
				sortable: false,
			},
			{
				name: "operatingTime",
				align: "center",
				sortable: false,
			},
			{
				name: "operatingState",//操作状态
				align: "center",
				sortable: false,
				edittype: 'select',
			},
			{
				name: "operatingResults",//操作结果
				align: "center",
//				width:600,
				sortable: false,
			},
			{
				name: 'accountLandingTime',
				align: "center",
				sortable: false,
			},
			{
				name: 'operatingAccount',
				align: "center",
				sortable: false,
			},
			{
				name: 'operatingUrl',
				align: "center",
				sortable: false,
			},
			{
				name: 'remark',
				align: "center",
				sortable: false,
			}
		],
		loadonce: false,
		rownumbers: true,
		autowidth: true,
		height: height,
		autoScroll: true,
		shrinkToFit: false,
		multiselect: true, //复选框 
		altclass: true,
		rowNum:100,
		viewrecords: true,
		forceFit: true,
		sortorder: "desc",
		rowList: [100, 300, 500],
		pager: '#jqGridPager',
		caption: "日志记录", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#jqgrids").getGridParam('records'); //获取返回的记录数
			page = $("#jqgrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#jqgrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			loadLocalData(page);
		},
	})
}

$(function(){
	$(".search").click(function(){
		loadLocalData()
	})
})

function loadLocalData() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var accNum = $(".accNum").val();
	var accNumIp = $(".accNumIp").val();
	var operationModule = $(".operationModule").val();
//	var operationName = $("input[name='operationName']").val();
	var operatingState = $(".operatingState").val();
	var operatingAccount = $(".operatingAccount").val();
	var operatingUrl = $(".operatingUrl").val();
	var operatingTimeStart = $(".operatingTimeStart").val();
	var operatingTimeEnd = $(".operatingTimeEnd").val();
	var accountLandingTimeStart = $(".accountLandingTimeStart").val();
	var accountLandingTimeEnd = $(".accountLandingTimeEnd").val();
	var showData = {
		"reqHead": reqhead,
		"reqBody": {
			"accNum":accNum,
			"accNumIp":accNumIp,
			"operationModule":operationModule,
			"operatingState":operatingState,
			"operatingAccount":operatingAccount,
			"operatingUrl":operatingUrl,
			"operatingTimeStart" :operatingTimeStart,
			"operatingTimeEnd":operatingTimeEnd,
			"accountLandingTimeStart":accountLandingTimeStart,
			"accountLandingTimeEnd":accountLandingTimeEnd,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var postData = JSON.stringify(showData);
	console.log(postData)
	$.ajax({
		type: "post",
		url: url + "/mis/system/misLog/queryList", //列表
		async: true,
		data: postData,
		dataType: 'json',
		contentType: 'application/json',
		//开始加载动画  添加到ajax里面
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");

		},
		success: function(data) {
			var mydata = {};
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#jqgrids").jqGrid("clearGridData");
			$("#jqgrids").jqGrid("setGridParam", {
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
		//结束加载动画
		complete: function() {
			$(".zhezhao").css("display", "none");
			$("#loader").css("display", "none");
		},
		error: function() {
			alert("加载失败")
		}
	})
}

//删除行
$(function() {
	$(".delOrder").click(function() {
		var ids = $("#jqgrids").jqGrid('getGridParam', 'selarrrow'); //获取选中行id
		var rowData = []
		for(i = 0; i < ids.length; i++) {
			var jstime = $("#jqgrids").getCell(ids[i], "id");
			rowData[i] = jstime
		}
		var rowDatas = rowData.toString();
		var deleteAjax = {
			"reqHead": reqhead,
			"reqBody": {
				"list": rowDatas
			}
		}
		var deleteData = JSON.stringify(deleteAjax)
		if(ids.length == 0) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + "/mis/system/misLog/delete",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(data) {
					if(data.respHead.isSuccess == true) {
						window.location.reload()
					}
				},
				error: function() {
					alert("删除失败")
				}
			});

		}
	})
})

//导出
var obj={}
$(document).on('click', '.exportExcel', function() {
	var data = {
		"reqHead": reqhead,
		"reqBody": {}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/system/misLog/queryPrint',
		type: 'post',
		data: Data,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		beforeSend: function() {
			$("#mengban1").css("display", "block");
			$("#loader").css("display", "block");
		},
		complete: function() {
			$("#mengban1").css("display", "none");
			$("#loader").css("display", "none");
		},
		success: function(data) {
			var list = data.respBody.list;
			var execlName = '日志记录'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
	
})