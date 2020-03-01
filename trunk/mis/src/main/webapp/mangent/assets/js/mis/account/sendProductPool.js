//发出商品汇总表
var count;
var pages;
var page = 1;
var rowNum;

$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	//点击表格图标显示存货分类列表
	$(document).on('click', '.momKitNm', function() {
		window.open("../../Components/baseDoc/outIntoWhs.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})

//右列表格的操作
$(function() {
	pageInit();
})

//页面初始化
function pageInit() {
	allHeight()
	jQuery("#send_jqgrids").jqGrid({
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		url: '../../assets/js/json/order.json',
		colNames: ['客户编码', '客户名称', '存货编码', '存货名称', '数量', '金额',
			'数量', '金额', '数量', '金额', '数量', '金额'
		], //jqGrid的列显示名字
		colModel: [{
				name: 'custId',
				align: "center",
				editable: true,
			},
			{
				name: "custNm",
				align: "center",
				editable: true,
			},
			{
				name: "invtyEncd",
				align: "center",
				editable: true,
			},
			{
				name: "invtyNm",
				align: "center",
				editable: true,
			},
			{
				name: 'qty',
				align: "center",
				editable: true,
			},
			{
				name: 'amt',
				align: "center",
				editable: true,
				sorttype: "integer"
			},
			{
				name: 'inQty',
				align: "center",
				editable: true,
				sorttype: "integer"
			},
			{
				name: 'inMoeny',
				align: "center",
				editable: true,
				sorttype: "integer"
			},
			{
				name: "sendQty",
				align: "center",
				editable: true,
				sorttype: "integer"
			},
			{
				name: "sendMoeny",
				align: "center",
				editable: true,
				sorttype: "integer"
			},
			{
				name: "othQty", //收入
				align: "center",
				editable: true,
				sorttype: "integer"
			},
			{
				name: 'othMoeny',
				align: "center",
				editable: true,
				sorttype: "integer"
			}
		],
		autowidth: true,
		autoScroll: true,
		shrinkToFit: false,
		height: height - 30,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 500,
		footerrow: true,
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "发出商品汇总表", //表格的标题名字
		//cellEdit:true, //单元格是否可编辑
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#send_jqgrids").getGridParam('records'); //获取返回的记录数
			page = $("#send_jqgrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#send_jqgrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			loadLocalData(page)
		},
		gridComplete: function() { 
			var qtys = 0;
			var amts = 0;
			var inqtys = 0;
			var inamts = 0;
			var outqtys = 0;
			var outamts = 0;
			var othqtys = 0;
			var othamts = 0;
			qtys = $("#send_jqgrids").getCol('qty', false, 'sum');
			amts = $("#send_jqgrids").getCol('amt', false, 'sum');
			inqtys = $("#send_jqgrids").getCol('inQty', false, 'sum');
			inamts = $("#send_jqgrids").getCol('inMoeny', false, 'sum');
			outqtys = $("#send_jqgrids").getCol('sendQty', false, 'sum');
			outamts = $("#send_jqgrids").getCol('sendMoeny', false, 'sum');
			othqtys = $("#send_jqgrids").getCol('othQty', false, 'sum');
			othamts = $("#send_jqgrids").getCol('othMoeny', false, 'sum');
			if(isNaN(qtys)) {
				qtys = 0;
			}
			if(isNaN(amts)) {
				amts = 0;
			}
			amts = precision(amts,2)
			if(isNaN(inqtys)) {
				inqtys = 0;
			}
			if(isNaN(inamts)) {
				inamts = 0;
			}
			inamts = precision(inamts,2)
			if(isNaN(outqtys)) {
				outqtys = 0;
			}
			if(isNaN(outamts)) {
				outamts = 0;
			}
			outamts = precision(outamts,2)
			if(isNaN(othqtys)) {
				othqtys = 0;
			}
			if(isNaN(othamts)) {
				othamts = 0;
			}
			othamts = precision(othamts,2)
			$("#send_jqgrids").footerData('set', { 
				"custId": "本页合计",
				qty: qtys,
				amt: amts,
				inQty: inqtys,
				inMoeny: inamts,
				sendQty: outqtys,
				sendMoeny: outamts,
				othQty: othqtys,
				othMoeny: othamts,
			}          );    
		},
	});
	jQuery("#send_jqgrids").jqGrid('setGroupHeaders', {
		useColSpanStyle: false,
		groupHeaders: [{
				startColumnName: 'qty',
				numberOfColumns: 2,
				titleText: '<em>期初</em>'
			},
			{
				startColumnName: 'inQty',
				numberOfColumns: 2,
				titleText: '<em>收入</em>'
			},
			{
				startColumnName: 'sendQty',
				numberOfColumns: 2,
				titleText: '<em>发出</em>'
			},
			{
				startColumnName: 'othQty',
				numberOfColumns: 2,
				titleText: '<em>结存</em>'
			}
		]
	});
}

function loadLocalData(page) {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var poolType = $("#poolType").val();
	var formSDt = $(".formSDt").val();
	var formEDt = $(".formEDt").val();
	var bookOkSDt = $(".bookOkSDt").val();
	var bookOkEDt = $(".bookOkEDt").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var invtyEncd = $("#invtyEncd").val();
	var whsEncd = $("#whsEncd").val();
	var batNum = $("#batNum").val();
	if(poolType == null || poolType == "") {
		alert("请选择汇总方式")
	} else {
		var myData = {};
		var data2 = {
			"reqHead": reqHead,
			"reqBody": {
				'poolType': poolType,
				'formSDt': formSDt,
				"invtyClsEncd": invtyClsEncd,
				'formEDt': formEDt,
				'bookOkSDt': bookOkSDt,
				'bookOkEDt': bookOkEDt,
				'invtyEncd': invtyEncd,
				'whsEncd': whsEncd,
				'batNum': batNum,
				"pageNo": page,
				"pageSize": rowNum
			}
		};
		var postD2 = JSON.stringify(data2);
		$.ajax({
			type: "post",
			url: url + "/mis/account/invty/sendProducts/pool",
			async: true,
			data: postD2,
			dataType: 'json',
			contentType: 'application/json; charset=utf-8',
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
				var list = data.respBody.list;
				$("#send_jqgrids").jqGrid('clearGridData')
				var mydata = {}
				mydata.rows = list;
				mydata.page = page;
				mydata.records = data.respBody.count;
				mydata.total = data.respBody.pages;
				$("#send_jqgrids").jqGrid("setGridParam", {
					data: mydata.rows,
					localReader: {
						root: function(object) {
							return mydata.rows
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
	}
}

//查询按钮
$(function() {
	$(".find").click(function() {
		loadLocalData(page)
	})
})

//导出
$(document).on('click', '.exportExcel', function() {
	var poolType = $("#poolType").val();
	var formSDt = $(".formSDt").val();
	var formEDt = $(".formEDt").val();
	var bookOkSDt = $(".bookOkSDt").val();
	var bookOkEDt = $(".bookOkEDt").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var invtyEncd = $("#invtyEncd").val();
	var whsEncd = $("#whsEncd").val();
	var batNum = $("#batNum").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			'poolType': poolType,
			'formSDt': formSDt,
			"invtyClsEncd": invtyClsEncd,
			'formEDt': formEDt,
			'bookOkSDt': bookOkSDt,
			'bookOkEDt': bookOkEDt,
			'invtyEncd': invtyEncd,
			'whsEncd': whsEncd,
			'batNum': batNum,
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/account/invty/sendProducts/poolExport',
		type: 'post',
		data: Data,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		beforeSend: function() {
			$("#mengban").css("display", "block");
			$("#loader").css("display", "block");
		},
		complete: function() {
			$("#mengban").css("display", "none");
			$("#loader").css("display", "none");
		},
		success: function(data) {
			var list = data.respBody.list;
			var execlName = '发出商品汇总表'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
})