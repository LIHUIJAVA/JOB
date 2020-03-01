var count;
var pages;
var page = 1;
var rowNum;
var tableSums = {};
$(function() {
	//加载动画html 添加到初始的时候
	$("#purchaseOrder .purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$("#purchaseOrder .purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div> ");
	$("#mengban").addClass("zhezhao");
})

$(function() {
	// 点击更多给弹筐内重复的赋值
	$(".more").click(function() {
		$("#big_wrap").show();
		$("#box").show();
		var invtyEncd = $("input[name='invtyEncd']").val();
		$("input[name='invtyEncd1']").val(invtyEncd);
		var invtyNm = $("input[name='invtyNm']").val();
		$("input[name='invtyNm1']").val(invtyNm);

		var invtyClsEncd = $("input[name='invtyClsEncd']").val();
		$("input[name='invtyClsEncd1']").val(invtyClsEncd);

		var whsEncd = $("input[name='whsEncd']").val();
		$("input[name='whsEncd1']").val(whsEncd);
		var whsNm = $("input[name='whsNm']").val();
		$("input[name='whsNm1']").val(whsNm);

		var intWhsDt1 = $("input[name='intWhsDt1']").val();
		$("input[name='intWhsDt3']").val(intWhsDt1);

		var intWhsDt2 = $("input[name='intWhsDt2']").val();
		$("input[name='intWhsDt4']").val(intWhsDt2);

		$("input[name='invtyEncd1']").attr('id', 'invtyEncd'); // 给弹框中重复的加id
		$("input[name='invtyEncd']").attr('id', ''); // 给弹框外重复的去除id
		$("input[name='invtyNm1']").attr('id', 'invtyNm'); // 给弹框中重复的加id
		$("input[name='invtyNm']").attr('id', ''); // 给弹框外重复的去除id

		$("input[name='invtyClsEncd1']").attr('id', 'invtyCls'); // 给弹框中重复的加id
		$("input[name='invtyClsEncd']").attr('id', ''); // 给弹框外重复的去除id

		$("input[name='whsEncd1']").attr('id', 'whsEncd');
		$("input[name='whsEncd']").attr('id', '');
		$("input[name='whsNm1']").attr('id', 'whsNm');
		$("input[name='whsNm']").attr('id', '');
	})
	$(".cancel_more").click(function() {
		$("#big_wrap").hide();
		$("#box").hide();
		$("input[name='invtyEncd1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='invtyEncd']").attr('id', 'invtyEncd'); // 给弹框外重复的去除id
		$("input[name='invtyNm1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='invtyNm']").attr('id', 'invtyNm'); // 给弹框外重复的去除id

		$("input[name='invtyClsEncd1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='invtyClsEncd']").attr('id', 'invtyCls'); // 给弹框外重复的去除id

		$("input[name='whsEncd1']").attr('id', '');
		$("input[name='whsEncd']").attr('id', 'whsEncd');
		$("input[name='whsNm1']").attr('id', '');
		$("input[name='whsNm']").attr('id', 'whsNm');

		$("#box input").val("");
	})
	$(".sure_more").click(function() {
		$("#big_wrap").hide();
		$("#box").hide();

		var invtyEncd = $("input[name='invtyEncd1']").val();
		$("input[name='invtyEncd']").val(invtyEncd);
		var invtyNm = $("input[name='invtyNm1']").val();
		$("input[name='invtyNm']").val(invtyNm);

		var invtyClsEncd = $("input[name='invtyClsEncd1']").val();
		$("input[name='invtyClsEncd']").val(invtyClsEncd);

		var intWhsDt1 = $("input[name='intWhsDt3']").val();
		$("input[name='intWhsDt1']").val(intWhsDt1);

		var intWhsDt2 = $("input[name='intWhsDt4']").val();
		$("input[name='intWhsDt2']").val(intWhsDt2);

		var whsEncd = $("input[name='whsEncd1']").val();
		$("input[name='whsEncd']").val(whsEncd);
		var whsNm = $("input[name='whsNm1']").val();
		$("input[name='whsNm']").val(whsNm);
		
		$("input[name='invtyEncd1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='invtyEncd']").attr('id', 'invtyEncd'); // 给弹框外重复的去除id
		$("input[name='invtyNm1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='invtyNm']").attr('id', 'invtyNm'); // 给弹框外重复的去除id

		$("input[name='invtyClsEncd1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='invtyClsEncd']").attr('id', 'invtyCls'); // 给弹框外重复的去除id

		$("input[name='whsEncd1']").attr('id', '');
		$("input[name='whsEncd']").attr('id', 'whsEncd');
		$("input[name='whsNm1']").attr('id', '');
		$("input[name='whsNm']").attr('id', 'whsNm');

		search();
		$("#box input").val("");
	})
})

//表格初始化
$(function() {
	localStorage.removeItem("index")
	localStorage.removeItem("sortorder")
	allHeight();
	$("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['单据日期', '仓库编码', '仓库名称', '存货编码', '存货名称', '存货代码', '规格型号', '主计量单位',
			'编码', '数量', '含税单价', '价税合计', '无税单价', '无税金额', '税率', '箱规',
			'箱数', '批次', '生产日期', '保质期', '失效日期'
		], //jqGrid的列显示名字
		colModel: [{
				name: 'intoWhsDt',
				editable: true,
				align: 'center',
				sorttype: 'date'
			},
			{
				name: 'whsEncd',
				editable: true,
				align: 'center',
			},
			{
				name: 'whsNm',
				editable: true,
				align: 'center',
			},
			{
				name: 'invtyEncd',
				editable: true,
				align: 'center',
			},
			{
				name: 'invtyNm',
				editable: false,
				align: 'center'
			},
			{
				name: 'invtyCd',
				editable: false,
				align: 'center',
			},
			{
				name: 'spcModel',
				editable: false,
				align: 'center',
			},
			{
				name: 'measrCorpNm',
				editable: false,
				align: 'center',
			},
			{
				name: 'measrCorpId',
				editable: false,
				align: 'center',
				hidden: true,
			},
			{
				name: 'qty',
				editable: true,
				align: 'center',
				sorttype: 'integer'
			},
			{
				name: 'cntnTaxUprc',
				editable: false,
				align: 'center',
				sorttype: 'integer'
			},
			{
				name: 'prcTaxSum',
				editable: false,
				align: 'center',
				sorttype: 'integer'
			},
			{
				name: 'noTaxUprc',
				editable: true,
				align: 'center',
				sorttype: 'integer'
			},
			{
				name: 'noTaxAmt',
				editable: false,
				align: 'center',
				sorttype: 'integer'
			},
			{
				name: 'taxRate',
				editable: false,
				align: 'center',
				sorttype: 'integer'
			},
			{
				name: 'bxRule',
				editable: false,
				align: 'center',
				sorttype: 'integer'
			},
			{
				name: 'bxQty',
				editable: true,
				align: 'center',
				sorttype: 'integer'
			},
			{
				name: 'batNum',
				editable: false,
				align: 'center',
			},
			{
				name: 'prdcDt',
				editable: false,
				align: 'center',
				sorttype: 'date'
			},
			{
				name: 'baoZhiQi',
				editable: true,
				align: 'center',
				sorttype: 'integer'
			},
			{
				name: 'invldtnDt',
				editable: true,
				align: 'center',
				sorttype: 'date'
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
		rowList: [500, 1000, 3000, 5000],
		rowNum: 500,
		footerrow: true,
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "入库明细表", //表格的标题名字
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
			$("#jqGrids").footerData('set', { 
				"intoWhsDt": "总计",
				"qty":tableSums. qty,
				"bxQty":tableSums.bxQty,
				"prcTaxSum": tableSums.prcTaxSum,
				"noTaxAmt": tableSums.noTaxAmt,
				"taxAmt":tableSums.taxAmt
			}          );    
		},
		onSortCol: function(index, colindex, sortorder) {
			localStorage.setItem("index",index)
			localStorage.setItem("sortorder",sortorder)
			window.index = index;
			window.sortorder = sortorder;
			search(index, sortorder)
		}
	})
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
	var myData = {};
	var invtyEncd = $("#invtyEncd").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var whsEncd = $("#whsEncd").val();
	var intWhsDt1 = $(".intWhsDt1").val();
	var intWhsDt2 = $(".intWhsDt2").val();
	var provrId = $("#provrId").val();

	var deptId = $(".deptId").val();
	var accNum = $("#user").val();
	var provrOrdrNum = $(".provrOrdrNum").val();
	var pursOrdrId = $("#pursOrdrId").val();
	var batNum = $("#batNum").val();
	var intlBat = $(".intlBat").val();
	var toGdsSngId = $(".toGdsSngId").val();
	var invtyCd = $(".invtyCd").val();

	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"isNtChk": 1,
			"invtyEncd": invtyEncd,
			"whsEncd": whsEncd,
			"invtyClsEncd": invtyClsEncd,
			"intoWhsDt1": intWhsDt1,
			"intoWhsDt2": intWhsDt2,
			"provrId": provrId,
			"deptId": deptId,
			"accNum": accNum,
			"provrOrdrNum": provrOrdrNum,
			"pursOrdrId": pursOrdrId,
			"batNum": batNum,
			"intlBat": intlBat,
			"toGdsSngId": toGdsSngId,
			"invtyCd": invtyCd,
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
		url: url + '/mis/purc/IntoWhs/queryIntoWhsByInvtyEncd',
		async: true,
		data: saveData,
		dataType: 'json',
		//开始加载动画  添加到ajax里面
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");

		},
		success: function(data) {
			tableSums = data.respBody.tableSums;
			var list = data.respBody.list;
			var mydata = {};
			mydata.rows = list;
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
		},
		//结束加载动画
		complete: function() {
			$(".zhezhao").css("display", "none");
			$("#loader").css("display", "none");
		},
		error: function() {
			alert("查询失败")
		}
	});

}

//导出
$(document).on('click', '.exportExcel', function() {
	var invtyEncd = $("#invtyEncd").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var whsEncd = $("#whsEncd").val();
	var intWhsDt1 = $(".intWhsDt1").val();
	var intWhsDt2 = $(".intWhsDt2").val();
	var provrId = $("#provrId").val();

	var deptId = $(".deptId").val();
	var accNum = $("#user").val();
	var provrOrdrNum = $(".provrOrdrNum").val();
	var pursOrdrId = $("#pursOrdrId").val();
	var batNum = $("#batNum").val();
	var intlBat = $(".intlBat").val();
	var toGdsSngId = $(".toGdsSngId").val();
	var invtyCd = $(".invtyCd").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"isNtChk": 1,
			"invtyEncd": invtyEncd,
			"whsEncd": whsEncd,
			"invtyClsEncd": invtyClsEncd,
			"intoWhsDt1": intWhsDt1,
			"intoWhsDt2": intWhsDt2,
			"provrId": provrId,
			"deptId": deptId,
			"accNum": accNum,
			"provrOrdrNum": provrOrdrNum,
			"pursOrdrId": pursOrdrId,
			"batNum": batNum,
			"intlBat": intlBat,
			"toGdsSngId": toGdsSngId,
			"invtyCd": invtyCd,
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/purc/IntoWhs/queryIntoWhsByInvtyEncdPrint',
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
			var execlName = '入库明细表'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
})
