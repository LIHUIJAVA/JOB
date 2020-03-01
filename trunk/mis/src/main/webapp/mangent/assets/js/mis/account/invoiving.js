//进销存统计表
var count;
var pages;
var page = 1;
var rowNum;

$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");

	//点击表格图标显示存货列表
	$(document).on('click', '.biao_invtyEncd', function() {
		window.open("../../Components/baseDoc/invtyList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	//点击表格图标显示仓库列表
	$(document).on('click', '.biao_whsEncd', function() {
		window.open("../../Components/baseDoc/whsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	//点击表格图标显示批次列表
	$(document).on('click', '.biao_batNum', function() {
		window.open("../../Components/baseDoc/batNum.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	//点击表格图标显示存货分类列表
	$(document).on('click', '.momKitNm', function() {
		window.open("../../Components/baseDoc/outIntoWhs.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	//	存货分类
	$(document).on('click', '.invtyClsEncd_biaoge', function() {
		window.open("../../Components/baseDoc/invtyTree.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})

//右列表格的操作
$(function() {
	pageInit();
})

//页面初始化
function pageInit() {
	allHeight()
	jQuery("#jqgrids").jqGrid({
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		url: '../../assets/js/json/order.json',
		colNames: ['存货编码', '期初数量', '期初金额', '采购数量', '采购金额', '暂估数量', '暂估金额', '其他入库数量', '其他入库金额', '其他出库数量', '其他出库金额',
			'销售数量', '销售收入', '销售金额','无税均价', '含税均价', '出库数量', '销售成本', '期末数量', '期末金额','毛利', '毛利率'
		], //jqGrid的列显示名字
		colModel: [{
				name: 'invtyEncd',
				align: "center",
				editable: true,
			},
			{
				name: "qty", //期初数量
				align: "center",
				editable: true,
				sorttype: "integer"
			},
			{
				name: "amt",
				align: "center",
				editable: true,
				sorttype: "integer"
			},
			{
				name: "purcQty", //采购数量
				align: "center",
				editable: true,
				sorttype: "integer"
			},
			{
				name: 'purcAmt',
				align: "center",
				editable: true,
				sorttype: "integer"
			},
			{
				name: 'purcTempQty', //暂估数量
				align: "center",
				editable: true,
				sorttype: "integer"
			},
			{
				name: 'purcTempAmt',
				align: "center",
				editable: true,
				sorttype: "integer"
			},
			{
				name: 'othInQty', //其他入库数量
				align: "center",
				editable: true,
				sorttype: "integer"
			},
			{
				name: "othInAmt",
				align: "center",
				editable: true,
				sorttype: "integer"
			},
			{
				name: 'othOutQty', //其他出库数量
				align: "center",
				editable: true,
				sorttype: "integer"
			},
			{
				name: "othOutAmt",
				align: "center",
				editable: true,
				sorttype: "integer"
			},
			{
				name: "sellQty", //销售数量
				align: "center",
				editable: true,
				sorttype: "integer"
			},
			{
				name: "sellInAmt", //销售收入
				align: "center",
				editable: true,
				sorttype: "integer"
			},
			{
				name: "sellAmt", //销售金额
				align: "center",
				editable: true,
				sorttype: "integer"
			},
			{
				name: 'noTaxUprc', //无税均价
				align: "center",
				editable: true,
				sorttype: "integer"
			},
			{
				name: 'cntnTaxUprc',
				align: "center",
				editable: true,
				sorttype: "integer"
			},
			{
				name: "outQty", //出库数量
				align: "center",
				editable: true,
				sorttype: "integer"
			},
			{
				name: 'sellCost', //销售成本
				align: "center",
				editable: true,
				sorttype: "integer"
			},
			{
				name: "finalQty", //期末数量
				align: "center",
				editable: true,
				sorttype: "integer"
			},
			{
				name: "finalAmt",
				align: "center",
				editable: true,
				sorttype: "integer"
			},
			{
				name: 'gross', //毛利
				align: "center",
				editable: true,
				sorttype: "integer"
			},
			{
				name: 'grossRate', //毛利率
				align: "center",
				editable: true,
				sortable: false,
			},

		],
		autowidth: true,
		autoScroll: true,
		shrinkToFit: false,
		height: height,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 500,
		footerrow: true,
		rowList: [500, 1000, 3000,5000], //可供用户选择一页显示多少条
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "进销存统计表", //表格的标题名字
		//cellEdit:true, //单元格是否可编辑
		gridComplete: function() { 
			var outQty = 0;
			var qty = 0;
			var amt = 0;
			var purcQty = 0;
			var purcAmt = 0;
			var purcTempQty = 0;
			var purcTempAmt = 0;
			var othInQty = 0;
			var othInAmt = 0;
			var othOutQty = 0;
			var othOutAmt = 0;
			var sellQty = 0;
			var sellAmt = 0;
			var finalQty = 0;
			var finalAmt = 0;
			var sellCost = 0;
			var gross = 0;
			var sellInAmt = 0;
			var ids = $("#jqgrids").getDataIDs();
			for(var i = 0; i < ids.length; i++) {
				outQty += parseFloat($("#jqgrids").getCell(ids[i], "outQty"));
				qty += parseFloat($("#jqgrids").getCell(ids[i], "qty"));
				amt += parseFloat($("#jqgrids").getCell(ids[i], "amt"));
				purcQty += parseFloat($("#jqgrids").getCell(ids[i], "purcQty"));
				purcAmt += parseFloat($("#jqgrids").getCell(ids[i], "purcAmt"));
				purcTempQty += parseFloat($("#jqgrids").getCell(ids[i], "purcTempQty"));
				purcTempAmt += parseFloat($("#jqgrids").getCell(ids[i], "purcTempAmt"));
				othInQty += parseFloat($("#jqgrids").getCell(ids[i], "othInQty"));
				othInAmt += parseFloat($("#jqgrids").getCell(ids[i], "othInAmt"));
				othOutQty += parseFloat($("#jqgrids").getCell(ids[i], "othOutQty"));
				othOutAmt += parseFloat($("#jqgrids").getCell(ids[i], "othOutAmt"));
				sellQty += parseFloat($("#jqgrids").getCell(ids[i], "sellQty"));
				sellAmt += parseFloat($("#jqgrids").getCell(ids[i], "sellAmt"));
				finalQty += parseFloat($("#jqgrids").getCell(ids[i], "finalQty"));
				finalAmt += parseFloat($("#jqgrids").getCell(ids[i], "finalAmt"));
				sellCost += parseFloat($("#jqgrids").getCell(ids[i], "sellCost"));
				sellInAmt += parseFloat($("#jqgrids").getCell(ids[i], "sellInAmt"));//销售收入
			}; 
			if(isNaN(sellCost)) {
				sellCost = 0;
			}
			sellCost = precision(sellCost,2)
			if(isNaN(qty)) {
				qty = 0;
			}
			if(isNaN(amt)) {
				amt = 0;
			}
			amt = precision(amt,2)
			if(isNaN(purcQty)) {
				purcQty = 0;
			}
			if(isNaN(purcAmt)) {
				purcAmt = 0;
			}
			purcAmt = precision(purcAmt,2)
			if(isNaN(purcTempQty)) {
				purcTempQty = 0;
			}
			if(isNaN(purcTempAmt)) {
				purcTempAmt = 0;
			}
			purcTempAmt = precision(purcTempAmt,2); //暂估
			if(isNaN(othInQty)) {
				othInQty = 0;
			}
			if(isNaN(othInAmt)) {
				othInAmt = 0;
			}
			othInAmt = precision(othInAmt,2)
			if(isNaN(othOutQty)) {
				othOutQty = 0;
			}
			if(isNaN(othOutAmt)) {
				othOutAmt = 0;
			}
			othOutAmt =precision(othOutAmt,2)
			if(isNaN(sellQty)) {
				sellQty = 0;
			}
			if(isNaN(sellAmt)) {
				sellAmt = 0;
			}
			sellAmt = precision(sellAmt,2)
			if(isNaN(finalQty)) {
				finalQty = 0;
			}
			if(isNaN(finalAmt)) {
				finalAmt = 0;
			}
			if(isNaN(sellInAmt)) {
				sellInAmt = 0;
			}
			
			var noTaxUprc = parseFloat(sellInAmt/sellQty);
			var cntnTaxUprc = parseFloat(sellAmt/sellQty);
			gross = parseFloat(sellInAmt-sellCost);
			var grossRate = parseFloat(gross/sellInAmt).toFixed(4)
			if(isNaN(gross)) {
				gross = 0;
			}
			gross = precision(gross,2)
			if(isNaN(noTaxUprc)) {
				noTaxUprc = 0;
			}
			if(isNaN(cntnTaxUprc)) {
				cntnTaxUprc = 0;
			}
			if(isNaN(grossRate)) {
				grossRate = 0;
			}
			if(isNaN(finalAmt)) {
				finalAmt = 0;
			}
			if(isNaN(outQty)) {
				outQty = 0;
			}
			noTaxUprc = precision(noTaxUprc,2)
			cntnTaxUprc = precision(cntnTaxUprc,2)
			finalAmt = precision(finalAmt,2)
			grossRate = (grossRate*100).toFixed(2)+"%";
			$("#jqgrids").footerData('set', { 
				"invtyEncd": "本页合计",
				"qty": qty,
				"amt": amt,
				"purcQty": purcQty,
				"purcAmt": purcAmt,
				"purcTempQty": purcTempQty,
				"purcTempAmt": purcTempAmt, //暂估
				"othInQty": othInQty,
				"othInAmt": othInAmt,
				"othOutQty": othOutQty,
				"othOutAmt": othOutAmt,
				"sellQty": sellQty,
				"sellAmt": sellAmt,
				"finalQty": finalQty,
				"finalAmt": finalAmt,
				"sellCost": sellCost,
				"gross": gross,
				"sellInAmt":sellInAmt,
				"noTaxUprc":noTaxUprc,
				"cntnTaxUprc":cntnTaxUprc,
				"grossRate":grossRate,
				"outQty":outQty
			}          );    
		},
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
			loadLocalData(page)
		},
	});
}

function loadLocalData(page) {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var formSDt = $(".formSDt").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var formEDt = $(".formEDt").val();
	var invtyEncd = $("#invtyEncd").val();
	var myData = {};
	var data2 = {
		"reqHead": reqHead,
		"reqBody": {
			'formSDt': formSDt,
			'formEDt': formEDt,
			"invtyClsEncd": invtyClsEncd,
			'invtyEncd': invtyEncd,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		url: url + "/mis/account/invty/invoiving/pool",
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
			if(data.respHead.isSuccess==false){
				alert(data.respHead.message)
			}
			var list = data.respBody.list;
			if(list) {
				for(var i = 0; i < list.length; i++) {
					$.extend(list[i], list[i].invtyDetailsList[0])
					delete list[i].invtyDetailsList
				}
			}
			$("#jqgrids").jqGrid('clearGridData')
			var mydata = {}
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#jqgrids").jqGrid("setGridParam", {
				data: mydata.rows,
				jsonReader: {
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

//查询按钮
$(function() {
	$(".find").click(function() {
		loadLocalData(page)
	})
})

//导出
$(document).on('click', '.exportExcel', function() {
	var formSDt = $(".formSDt").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var formEDt = $(".formEDt").val();
	var invtyEncd = $("#invtyEncd").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			'formSDt': formSDt,
			'formEDt': formEDt,
			"invtyClsEncd": invtyClsEncd,
			'invtyEncd': invtyEncd,
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/account/invty/invoiving/poolExport',
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
			var execlName = '进销存统计表'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
})