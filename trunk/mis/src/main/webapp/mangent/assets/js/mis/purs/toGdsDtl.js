
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

		var provrId = $("input[name='provrId']").val();
		$("input[name='provrId1']").val(provrId);
		var provrNm = $("input[name='provrNm']").val();
		$("input[name='provrNm1']").val(provrNm);

		var invtyClsEncd = $("input[name='invtyClsEncd']").val();
		$("input[name='invtyClsEncd1']").val(invtyClsEncd);

		var whsEncd = $("input[name='whsEncd']").val();
		$("input[name='whsEncd1']").val(whsEncd);
		var whsNm = $("input[name='whsNm']").val();
		$("input[name='whsNm1']").val(whsNm);

		var toGdsSnglDt1 = $("input[name='toGdsSnglDt1']").val();
		$("input[name='toGdsSnglDt3']").val(toGdsSnglDt1);

		var toGdsSnglDt2 = $("input[name='toGdsSnglDt2']").val();
		$("input[name='toGdsSnglDt4']").val(toGdsSnglDt2);

		$("input[name='invtyEncd1']").attr('id', 'invtyEncd'); // 给弹框中重复的加id
		$("input[name='invtyEncd']").attr('id', ''); // 给弹框外重复的去除id
		$("input[name='invtyNm1']").attr('id', 'invtyNm'); // 给弹框中重复的加id
		$("input[name='invtyNm']").attr('id', ''); // 给弹框外重复的去除id

		$("input[name='provrId1']").attr('id', 'provrId'); // 给弹框中重复的加id
		$("input[name='provrId']").attr('id', ''); // 给弹框外重复的去除id
		$("input[name='provrNm1']").attr('id', 'provrNm'); // 给弹框中重复的加id
		$("input[name='provrNm']").attr('id', ''); // 给弹框外重复的去除id

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

		$("input[name='provrId1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='provrId']").attr('id', 'provrId'); // 给弹框外重复的去除id
		$("input[name='provrNm1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='provrNm']").attr('id', 'provrNm'); // 给弹框外重复的去除id

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

		var provrId = $("input[name='provrId1']").val();
		$("input[name='provrId']").val(provrId);
		var provrNm = $("input[name='provrNm1']").val();
		$("input[name='provrNm']").val(provrNm);

		var invtyClsEncd = $("input[name='invtyClsEncd1']").val();
		$("input[name='invtyClsEncd']").val(invtyClsEncd);

		var intoWhsSnglDt1 = $("input[name='toGdsSnglDt3']").val();
		$("input[name='toGdsSnglDt1']").val(intoWhsSnglDt1);

		var intoWhsSnglDt2 = $("input[name='toGdsSnglDt4']").val();
		$("input[name='toGdsSnglDt2']").val(intoWhsSnglDt2);

		var whsEncd = $("input[name='whsEncd1']").val();
		$("input[name='whsEncd']").val(whsEncd);
		var whsNm = $("input[name='whsNm1']").val();
		$("input[name='whsNm']").val(whsNm);
		
		$("input[name='invtyEncd1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='invtyEncd']").attr('id', 'invtyEncd'); // 给弹框外重复的去除id
		$("input[name='invtyNm1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='invtyNm']").attr('id', 'invtyNm'); // 给弹框外重复的去除id

		$("input[name='provrId1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='provrId']").attr('id', 'provrId'); // 给弹框外重复的去除id
		$("input[name='provrNm1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='provrNm']").attr('id', 'provrNm'); // 给弹框外重复的去除id

		$("input[name='invtyClsEncd1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='invtyClsEncd']").attr('id', 'invtyCls'); // 给弹框外重复的去除id

		$("input[name='whsEncd1']").attr('id', '');
		$("input[name='whsEncd']").attr('id', 'whsEncd');
		$("input[name='whsNm1']").attr('id', '');
		$("input[name='whsNm']").attr('id', 'whsNm');

		search();
	})
})

//表格初始化
$(function() {
	localStorage.removeItem("index")
	localStorage.removeItem("sortorder")
	allHeight()
	$("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['单据日期','仓库编码', '仓库名称', '存货编码', '存货名称', '存货代码', '规格型号', '主计量单位',
			'编码', '数量', '含税单价', '价税合计', '无税单价', '无税金额', '税率', '箱规',
			'箱数', '批次', '生产日期', '保质期', '失效日期'
		], //jqGrid的列显示名字
		colModel: [{
				name: 'toGdsSnglDt',
				editable: true,
				align: 'center',
				sortable: true
			},
			{
				name: 'whsEncd',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'whsNm',
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
			{
				name: 'batNum',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'prdcDt',
				editable: false,
				align: 'center',
				sortable: true
			},
			{
				name: 'baoZhiQi',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'invldtnDt',
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
		rowNum: 500,
		rowList: [500, 1000, 3000, 5000],
		footerrow: true,
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "到货明细表", //表格的标题名字
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
			$("#jqGrids").footerData('set', { 
				"toGdsSnglDt": "总计",
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
	var invtyEncd = $("#invtyEncd").val();
	var whsEncd = $("#whsEncd").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var toGdsSnglDt1 = $(".toGdsSnglDt1").val();
	var toGdsSnglDt2 = $(".toGdsSnglDt2").val();
	var provrId = $("#provrId").val();
	var deptId = $("#deptId").val();
	var accNum = $("#user").val();
	var provrOrderNum = $(".provrOrdrNum").val();
	var pursOrdrId = $(".pursOrdrId").val();
	var batNum = $("#batNum").val();
	var intlBat = $(".intlBat").val();
	var invtyCd = $(".invtyCd").val();
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"isNtChk": 1,
			"invtyEncd": invtyEncd,
			"whsEncd": whsEncd,
			"invtyClsEncd": invtyClsEncd,
			"toGdsSnglDt1": toGdsSnglDt1,
			"toGdsSnglDt2": toGdsSnglDt2,
			"provrId": provrId,
			"deptId": deptId,
			"accNum": accNum,
			"provrOrderNum": provrOrderNum,
			"pursOrdrId": pursOrdrId,
			"batNum": batNum,
			"intlBat": intlBat,
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
		url: url + '/mis/purc/ToGdsSngl/queryToGdsSnglByInvtyEncd',
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
//$(function() {
//	$("#last_jqGridPager").after('<input id="_input" type="text" value="500"/>')
//})