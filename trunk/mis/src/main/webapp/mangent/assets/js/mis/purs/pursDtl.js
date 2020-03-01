$(function() {
	//加载动画html
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
})

var count;
var pages;
var page = 1;
var rowNum;
//表格初始化
$(function() {
	localStorage.removeItem("index")
	localStorage.removeItem("sortorder")
	allHeight()
	$("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['单据日期', '发票编号', '存货编码', '存货名称', '存货代码', '规格型号', '主计量单位', '编码', '数量', '含税单价', '价税合计',
			'无税单价', '无税金额', '税率', '箱规', '箱数'
		], //jqGrid的列显示名字
		colModel: [{
				name: 'bllgDt',
				editable: true,
				align: 'center',
				sortable: true
			},
			{
				name: 'pursInvNum',
				editable: true,
				align: 'center',
				sortable: false

			},
			{
				name: 'invtyEncd',
				editable: true,
				align: 'center',
				sortable: false

			},
			{
				name: 'invtyNm',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'invtyCd',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'spcModel',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'measrCorpNm',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'measrCorpId',
				editable: false,
				align: 'center',
				hidden: true,
				sortable: false
			},
			{
				name: 'qty',
				editable: true,
				align: 'center',
				sortable: true
			},
			{
				name: 'cntnTaxUprc',
				editable: false,
				align: 'center',
				sortable: true
			},
			{
				name: 'prcTaxSum',
				editable: false,
				align: 'center',
				sortable: true
			},
			{
				name: 'noTaxUprc',
				editable: true,
				align: 'center',
				sortable: true
			},
			{
				name: 'noTaxAmt',
				editable: false,
				align: 'center',
				sortable: true
			},
			{
				name: 'taxRate',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'bxRule',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'bxQty',
				editable: true,
				align: 'center',
				sortable: true
			},
		],
		sortable: true,
		loadonce: true,
		autowidth: true,
		height: height,
		autoScroll: true,
		shrinkToFit: false,
		rownumbers: true,
		forceFit: true,
		footerrow: true,
		rowNum: 500,
		rowList: [500, 1000, 3000, 5000],
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "采购明细表", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#jqGrids").getGridParam('records'); //获取返回的记录数
			page = $("#jqGrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#jqGrids").getGridParam('rowNum');

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
			var index = localStorage.getItem("index")
			var sortorder = localStorage.getItem("sortorder")
			if(index == null) {
				var index = ''
			}
			if(sortorder == null) {
				var sortorder = 'desc'
			}
			search(index, sortorder)   
		},
		gridComplete: function() { 
			var qty = 0;
			var prc_tax_sum = 0;
			var no_tax_amt = 0;
			var ids = $("#jqGrids").getDataIDs();
			var rowData = $("#jqGrids").getRowData(ids[i]);
			for(var i = 0; i < ids.length; i++) {
				qty += parseFloat($("#jqGrids").getCell(ids[i], "qty"));
				prc_tax_sum += parseFloat($("#jqGrids").getCell(ids[i], "prcTaxSum"));
				no_tax_amt += parseFloat($("#jqGrids").getCell(ids[i], "noTaxAmt"));
			}; 
			if(isNaN(qty)) {
				qty = 0
			}
			if(isNaN(prc_tax_sum)) {
				prc_tax_sum = 0
			}
			if(isNaN(no_tax_amt)) {
				no_tax_amt = 0
			}
			prc_tax_sum =precision(prc_tax_sum,2)
			no_tax_amt = precision(no_tax_amt,2)
			$("#jqGrids").footerData('set', { 
				"bllgDt": "本页合计",
				"qty": qty,
				"prcTaxSum": prc_tax_sum,
				"noTaxAmt": no_tax_amt,

			}          );    
		},
		onSortCol: function(index, colindex, sortorder) {
			localStorage.setItem("index",index)
			localStorage.setItem("sortorder",sortorder)
			window.index = index;
			window.sortorder = sortorder;
			search(index, sortorder)
		}
	});
})

//查询按钮
$(function() {
	$(".search").click(function() {
		var index = ''
		var sortorder = 'desc'
		search(index, sortorder)
	})
})

function search(index, sortorder) {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var invtyEncd = $("#invtyEncd").val();
	var invtyCd = $(".invtyCd").val();
	var provrId = $("#provrId").val();
	var pursOrdrDt1 = $(".pursOrdrDt1").val();
	var pursOrdrDt2 = $(".pursOrdrDt2").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var deptId = $("#deptId").val();
	var accNum = $("#user").val();
	var provrOrdrNum = $(".supplierNumber").val();

	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"isNtChk": 1,
			"invtyEncd": invtyEncd,
			"bllgDt1": pursOrdrDt1,
			"bllgDt2": pursOrdrDt2,
			"invtyClsEncd": invtyClsEncd,
			"deptId": deptId,
			"accNum": accNum,
			"invtyCd": invtyCd,
			"provrOrdrNum": provrOrdrNum,
			"provrId": provrId,
			"sort":index,
			"sortOrder":sortorder,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/purc/PursOrdr/queryPursOrdrByInvtyEncd',
		async: true,
		data: saveData,
		dataType: 'json',
		//开始加载动画  添加到ajax里面
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");
		},
		success: function(data) {
			var list = data.respBody.list[0];
			if(list) {
				var mydata = {};
				mydata.rows = data.respBody.list;
				mydata.page = page;
				mydata.records = data.respBody.count;
				mydata.total = data.respBody.pages;
				$("#jqGrids").jqGrid("clearGridData");
				$("#jqGrids").jqGrid("setGridParam", {
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
			}else{
				alert("数据为空")
			}
		},
		//结束加载动画
		complete: function() {
			$(".zhezhao").css("display", "none");
			$("#loader").css("display", "none");
		},
		error: function() {
			alert("error")
		}
	});
}


//导出
$(document).on('click', '.exportExcel', function() {
	var invtyEncd = $("#invtyEncd").val();
	var invtyCd = $(".invtyCd").val();
	var provrId = $("#provrId").val();
	var pursOrdrDt1 = $(".pursOrdrDt1").val();
	var pursOrdrDt2 = $(".pursOrdrDt2").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var deptId = $("#deptId").val();
	var accNum = $("#user").val();
	var provrOrdrNum = $(".supplierNumber").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"isNtChk": 1,
			"invtyEncd": invtyEncd,
			"bllgDt1": pursOrdrDt1,
			"bllgDt2": pursOrdrDt2,
			"invtyClsEncd": invtyClsEncd,
			"deptId": deptId,
			"accNum": accNum,
			"invtyCd": invtyCd,
			"provrOrdrNum": provrOrdrNum,
			"provrId": provrId,
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/purc/PursOrdr/queryPursOrdrByInvtyEncdPrint',
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
			var execlName = '采购明细'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
})
