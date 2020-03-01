var count;
var pages;
var page = 1;
var rowNum;
var tableSums = {};
$(function() {
	//加载动画html
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$("#mengban").addClass("zhezhao");
})
//$(document).keydown(function(event) {
//	if(event.keyCode == 13) {
//		search();
//	}
//});

$(function() {
	// 点击更多给弹筐内重复的赋值
	$(".more").click(function() {
		$("#big_wrap").show();
		$("#box").show();
		var invtyEncd = $("input[name='invtyEncd']").val();
		$("input[name='invtyEncd1']").val(invtyEncd);
		var invtyNm = $("input[name='invtyNm']").val();
		$("input[name='invtyNm1']").val(invtyNm);

		var intoWhsSnglId = $("input[name='intoWhsSnglId']").val();
		$("input[name='intoWhsSnglId1']").val(intoWhsSnglId);

		var provrId = $("input[name='provrId']").val();
		$("input[name='provrId1']").val(provrId);
		var provrNm = $("input[name='provrNm']").val();
		$("input[name='provrNm1']").val(provrNm);

		var invtyClsEncd = $("input[name='invtyClsEncd']").val();
		$("input[name='invtyClsEncd1']").val(invtyClsEncd);

		var isNtChk = $("select[name='isNtChk']").val();
		$("select[name='isNtChk1']").val(isNtChk);

		var intoWhsSnglDt1 = $("input[name='intoWhsSnglDt1']").val();
		$("input[name='intoWhsSnglDt3']").val(intoWhsSnglDt1);

		var intoWhsSnglDt2 = $("input[name='intoWhsSnglDt2']").val();
		$("input[name='intoWhsSnglDt4']").val(intoWhsSnglDt2);

		$("input[name='invtyEncd1']").attr('id', 'invtyEncd'); // 给弹框中重复的加id
		$("input[name='invtyEncd']").attr('id', ''); // 给弹框外重复的去除id
		$("input[name='invtyNm1']").attr('id', 'invtyNm'); // 给弹框中重复的加id
		$("input[name='invtyNm']").attr('id', ''); // 给弹框外重复的去除id

		$("input[name='provrId1']").attr('id', 'provrId'); // 给弹框中重复的加id
		$("input[name='provrId']").attr('id', ''); // 给弹框外重复的去除id
		$("input[name='provrNm1']").attr('id', 'provrNm'); // 给弹框中重复的加id
		$("input[name='provrNm']").attr('id', ''); // 给弹框外重复的去除id

		$("select[name='isNtChk1']").attr('id', 'isNtChk'); // 给弹框中重复的加id
		$("select[name='isNtChk']").attr('id', ''); // 给弹框外重复的去除id

		$("input[name='invtyClsEncd1']").attr('id', 'invtyCls'); // 给弹框中重复的加id
		$("input[name='invtyClsEncd']").attr('id', ''); // 给弹框外重复的去除id
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

		$("select[name='isNtChk']").attr('id', 'isNtChk1'); // 给弹框中重复的加id
		$("select[name='isNtChk']").attr('id', '');
		$("#box input").val("")
		$("#box select").val("")
		$("#formSave select").val("")
	})
	$(".sure_more").click(function() {
		$("#big_wrap").hide();
		$("#box").hide();

		var invtyEncd = $("input[name='invtyEncd1']").val();
		$("input[name='invtyEncd']").val(invtyEncd);
		var invtyNm = $("input[name='invtyNm1']").val();
		$("input[name='invtyNm']").val(invtyNm);

		var intoWhsSnglId = $("input[name='intoWhsSnglId1']").val();
		$("input[name='intoWhsSnglId']").val(intoWhsSnglId);

		var provrId = $("input[name='provrId1']").val();
		$("input[name='provrId']").val(provrId);
		var provrNm = $("input[name='provrNm1']").val();
		$("input[name='provrNm']").val(provrNm);

		var invtyClsEncd = $("input[name='invtyClsEncd1']").val();
		$("input[name='invtyClsEncd']").val(invtyClsEncd);

		var isNtChk = $("select[name='isNtChk1']").val();
		$("select[name='isNtChk']").val(isNtChk);

		var intoWhsSnglDt1 = $("input[name='intoWhsSnglDt3']").val();
		$("input[name='intoWhsSnglDt1']").val(intoWhsSnglDt1);

		var intoWhsSnglDt2 = $("input[name='intoWhsSnglDt4']").val();
		$("input[name='intoWhsSnglDt2']").val(intoWhsSnglDt2);
		$("input[name='invtyEncd']").attr('id', 'invtyEncd'); // 给弹框中重复的加id
		$("input[name='invtyEncd1']").attr('id', ''); // 给弹框外重复的去除id
		$("input[name='invtyNm']").attr('id', 'invtyNm'); // 给弹框中重复的加id
		$("input[name='invtyNm1']").attr('id', ''); // 给弹框外重复的去除id

		$("input[name='provrId']").attr('id', 'provrId'); // 给弹框中重复的加id
		$("input[name='provrId1']").attr('id', ''); // 给弹框外重复的去除id
		$("input[name='provrNm']").attr('id', 'provrNm'); // 给弹框中重复的加id
		$("input[name='provrNm1']").attr('id', ''); // 给弹框外重复的去除id

		$("select[name='isNtChk']").attr('id', 'isNtChk'); // 给弹框中重复的加id
		$("select[name='isNtChk1']").attr('id', ''); // 给弹框外重复的去除id

		$("input[name='invtyClsEncd']").attr('id', 'invtyCls'); // 给弹框中重复的加id
		$("input[name='invtyClsEncd1']").attr('id', ''); // 给弹框外重复的去除id
		search();
	})
})

//表格初始化
$(function() {
	localStorage.removeItem("index")
	localStorage.removeItem("sortorder")
	allHeight()
	var rowNum = $("#_input").val();
	$("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['入库单号', '入库日期', '采购类型', '业务员', '供应商', '部门',
			'供应商订单号','表头备注', '表体备注', '订单号', '到货单号', '是否退货', '是否审核', '仓库编码',
			'仓库名称', '存货编码', '存货名称', '规格型号', '主计量单位', '编码', '数量', '含税单价', '价税合计',
			'无税单价', '无税金额', '税率', '税额', '箱规', '箱数', '对应条形码', '批次', '生产日期', '失效日期', '保质期', '国际批次',
		],
		colModel: [{
				name: 'intoWhsSnglId',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'intoWhsDt',
				editable: true,
				align: 'center',
				sortable: true

			},
			{
				name: 'pursTypNm',
				editable: true,
				align: 'center',
				sortable: false

			},
			{
				name: 'userName',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'provrNm',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'deptName',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'provrOrdrNum',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'memo',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'memos',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'pursOrdrId',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'toGdsSnglId',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'isNtRtnGood',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'isNtChk',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'whsEncd',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'whsNm',
				editable: false,
				align: 'center',
				sortable: false,
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
				editable: true,
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
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'taxAmt',
				editable: false,
				align: 'center',
				sortable: true
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
				name: 'crspdBarCd',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'batNum',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'prdcDt', //生产日期
				editable: true,
				align: 'center',
				sortable: true
			},
			{
				name: 'invldtnDt', //失效日期
				editable: false,
				align: 'center',
				sortable: true
			},
			{
				name: 'baoZhiQi',
				editable: false,
				align: 'center',
				sortable: true
			},
			{
				name: 'intlBat', //国际批次
				editable: true,
				align: 'center',
				sortable: false
			},
		],
		sortable: true,
		autowidth: true,
		height: height,
		autoScroll: true,
		shrinkToFit: false,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 500,
		rowList: [500, 1000, 3000, 5000],
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "asc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		//		multiboxonly: true,
		caption: "采购入库(退货)单列表", //表格的标题名字
		footerrow: true,
		userDataOnFooter: true,
		gridComplete: function() {
			$("#jqGrids").footerData('set', { 
				"intoWhsSnglId": "总计",
				"qty": tableSums.qty,
				"bxQty": tableSums.bxQty,
				"prcTaxSum": tableSums.prcTaxSum,
				"noTaxAmt": tableSums.noTaxAmt,
				"taxAmt": tableSums.taxAmt

			}          ); 
		},
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
		//双击弹出采购订单
		ondblClickRow: function(rowid) {
			order(rowid);
		},
		onSortCol: function(index, colindex, sortorder) {
			switch(index) {
				case "intoWhsDt":
					index = "iw.into_whs_dt"
					break;
				case "qty":
					index = "iws.qty"
					break;
				case "cntnTaxUprc":
					index = "iws.cntn_tax_uprc"
					break;
				case "prcTaxSum":
					index = "iws.prc_tax_sum"
					break;
				case "noTaxUprc":
					index = "iws.no_tax_uprc"
					break;
				case "noTaxAmt":
					index = "iws.no_tax_amt"
					break;
				case "taxAmt":
					index = "iws.tax_amt"
					break;
				case "bxQty":
					index = "iws.bx_qty"
					break;
				case "prdcDt":
					index = "iws.prdc_dt"
					break;
				case "invldtnDt":
					index = "iws.invldtn_dt"
					break;
				case "baoZhiQiDt":
					index = "iws.bao_zhi_qi"
					break;
			}
			localStorage.setItem("index", index)
			localStorage.setItem("sortorder", sortorder)
			window.index = index;
			window.sortorder = sortorder;
			search(index, sortorder)
		}
	})
})

//查询按钮
$(document).on('click', '.search', function() {
	var index = ''
	var sortorder = 'desc'
	search(index, sortorder)
})

function search(index, sortorder) {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var myDate = {};

	var invtyEncd = $("#invtyEncd").val();
	var intoWhsSnglId = $(".intoWhsSnglId").val();
	var provrId = $("#provrId").val();
	var intoWhsDt1 = $(".intoWhsSnglDt1").val();
	var intoWhsDt2 = $(".intoWhsSnglDt2").val();
	var isNtChk = $("#isNtChk").val();
	var invtyClsEncd = $(".invtyClsEncd").val();

	var isNtRtnGood = $(".isNtRtnGood").val();
	var deptId = $(".deptId").val();
	var accNum = $("#user").val();
	var provrOrdrNum = $(".provrOrdrNum").val();
	var pursOrdrId = $("#pursOrdrId").val();
	var batNum = $("#batNum").val();
	var intlBat = $(".intlBat").val();
	var isNtBllg = $(".isNtBllg").val();
	var toGdsSngId = $(".toGdsSngId").val();
	var isNtBookEntry = $(".isNtBookEntry").val();
	var invtyCd = $(".invtyCd").val();
	var whsEncd = $("#whsEncd").val();
	var memo = $("#memo").val();

	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": invtyEncd,
			"intoWhsSnglId": intoWhsSnglId,
			"provrId": provrId,
			"intoWhsDt1": intoWhsDt1,
			"intoWhsDt2": intoWhsDt2,
			"invtyClsEncd": invtyClsEncd,
			"isNtChk": isNtChk,
			"isNtRtnGood": isNtRtnGood,
			"deptId": deptId,
			"accNum": accNum,
			"provrOrdrNum": provrOrdrNum,
			"pursOrdrId": pursOrdrId,
			"batNum": batNum,
			"intlBat": intlBat,
			"isNtBllg": isNtBllg,
			"toGdsSnglId": toGdsSngId,
			"isNtBookEntry": isNtBookEntry,
			"invtyCd": invtyCd,
			"whsEncd":whsEncd,
			"memo":memo,
			"sort": index,
			"sortOrder": sortorder,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/purc/IntoWhs/queryIntoWhsListOrderBy',
		async: true,
		data: saveData,
		dataType: 'json',
		//开始加载动画
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
			tableSums = data.respBody.tableSums;
			var list = data.respBody.list;
			for(var i = 0; i < list.length; i++) {
				if(list[i].isNtChk == 0) {
					list[i].isNtChk = "否"
				} else if(list[i].isNtChk == 1) {
					list[i].isNtChk = "是"
				}
			}
			for(var i = 0; i < list.length; i++) {
				if(list[i].isNtRtnGood == 1) {
					list[i].isNtRtnGood = "退货"
				} else if(list[i].isNtRtnGood == 0) {
					list[i].isNtRtnGood = "入库"
				}
			}
			myDate = list;
			var mydata = {};
			mydata.rows = myDate;
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
		error: function() {
			alert("查询失败")
		}
	});
}

function order(rowid) {
	//获得行数据
	var rowDatas = $("#jqGrids").jqGrid('getRowData', rowid);
	localStorage.setItem("intoWhsSnglId", rowDatas.intoWhsSnglId);
	localStorage.setItem("isNtChk", rowDatas.isNtChk);
	if(rowDatas.isNtRtnGood == "退货") {
		window.open("../../Components/purs/returnWhs.html?1", 'height=700, width=1000, top=200, left=300,location=no, status=no');
	} else if(rowDatas.isNtRtnGood == "入库") {
		window.open("../../Components/purs/intoWhs.html?1", 'height=700, width=1000, top=200, left=300,location=no, status=no');
	}

}

function ntChk(x) {
	//获得选中行的行号
	var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
	//对象数组
	var rowData = [];
	for(var i = 0; i < ids.length; i++) {
		var isNtRtnGood = $("#jqGrids").getCell(ids[i], "isNtRtnGood")
		if(isNtRtnGood == "退货") {
			isNtRtnGood = 1;
		} else if(isNtRtnGood == "入库") {
			isNtRtnGood = 0;
		}
	}
	for(var i = 0; i < ids.length; i++) {
		var obj = {}; //对象
		//选中行的id
		//把选中行的i添加到数据对象中
		obj.intoWhsSnglId = $("#jqGrids").getCell(ids[i], "intoWhsSnglId").toString();
		obj.isNtChk = x;
		obj.isNtRtnGood = isNtRtnGood;
		//建一个数组，把单个对象添加到数组中
		rowData[i] = obj;
	}
	var result = [];
	var obj = {};
	for(var i = 0; i < rowData.length; i++) {
		if(!obj[rowData[i].intoWhsSnglId]) {
			result.push(rowData[i]);
			obj[rowData[i].intoWhsSnglId] = true;
		}
	}
	if(result.length == 0) {
		alert("请选择单据!")
	} else {
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				"list": result
			}
		};
		var Data = JSON.stringify(data);
		$.ajax({
			url: url + '/mis/purc/IntoWhs/updateIntoWhsIsNtChk',
			type: 'post',
			data: Data,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
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
				alert(data.respHead.message)
				if(data.respHead.isSuccess == true) {
					search()
				}
			},
			error: function(error) {
				alert(error.responseText)
			}
		})
	}

}

//审核与弃审
$(function() {
	var isclick = true;
	//审核
	$(".toExamine").click(function() {
		if(isclick) {
			isclick = false;
			ntChk(1);
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	});
	//弃审
	$(".noTo").click(function() {
		if(isclick) {
			isclick = false;
			ntChk(0);
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	})
})

//删除
$(function() {
	$(".delOrder").click(function() {
		//获得选中行的行号
		var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
		//获取选择行的provrId
		var rowData = [];
		for(var i = 0; i < ids.length; i++) {
			var data = $("#jqGrids").getCell(ids[i], "intoWhsSnglId");
			//建一个数组，把选中行的id添加到这个数组中去。
			rowData[i] = data;
		}
		var result = [];
		var obj = {};
		for(var i = 0; i < rowData.length; i++) {
			if(!obj[rowData[i]]) {
				result.push(rowData[i]);
				obj[rowData[i]] = true;
			}
		}
		if(result.length == 0) {
			alert("请选择单据!")
		} else if(confirm("确定删除？")) {
			var intoWhsSnglId = result.toString();
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"intoWhsSnglId": intoWhsSnglId,
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/purc/IntoWhs/deleteIntoWhsList',
				type: 'post',
				data: Data,
				dataType: 'json',
				async: true,
				contentType: 'application/json;charset=utf-8',
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
					alert(data.respHead.message)
					if(data.respHead.isSuccess == true) {
						search()
					}
				},
				error: function() {
					alert("删除失败")
				}
			})
		}
	})
})

//自动分配货位
$(function() {
	$(".gdsBitAllotted").click(function() {
		//获得选中行的行号
		var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
		//获取选择行的provrId
		var rowData = [];
		for(var i = 0; i < ids.length; i++) {
			var data = $("#jqGrids").getCell(ids[i], "intoWhsSnglId");
			//建一个数组，把选中行的id添加到这个数组中去。
			rowData[i] = data;
		}
		var result = [];
		var obj = {};
		for(var i = 0; i < rowData.length; i++) {
			if(!obj[rowData[i]]) {
				result.push(rowData[i]);
				obj[rowData[i]] = true;
			}
		}
		if(result.length == 0) {
			alert("请选择单据!")
		} else if(confirm("确定自动分配？")) {
			var intoWhsSnglId = result.toString();
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"formNum": intoWhsSnglId,
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/whs/out_into_whs/allInvtyGdsBitList',
				type: 'post',
				data: Data,
				dataType: 'json',
				async: true,
				contentType: 'application/json;charset=utf-8',
				success: function(data) {
					alert(data.respHead.message)
//					if(data.respHead.isSuccess == true) {
//						search()
//					}
				},
				error: function() {
					alert("自动分配货位失败")
				}
			})
		}
	})
})

//$(function() {
//	$("#last_jqGridPager").after('<input id="_input" type="text" value="500"/>')
//})

//导入
function importExcel(x) {
	var files = $("#FileUpload").val()
	var fileObj = document.getElementById("FileUpload").files[0];
	var formFile = new FormData();
	formFile.append("action", "UploadVMKImagePath");
	formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
	var data = formFile;
	if(files != "") {
		$.ajax({
			type: 'post',
			url: url + x,
			data: data,
			dataType: "json",
			cache: false, //上传文件无需缓存
			processData: false, //用于对data参数进行序列化处理 这里必须false
			contentType: false, //必须
			success: function(data) {
				alert(data.respHead.message)
			},
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
		});
	} else {
		alert("请选择文件")
	}
}

//导入
$(function() {
	$(".dropdown-menu .li2").click(function() {
		var x = "/mis/purc/IntoWhs/uploadIntoWhsFile"
		importExcel(x)
	});
	$(".dropdown-menu .li1").click(function() {
		var x = "/mis/purc/IntoWhs/uploadIntoWhsFileU8"
		importExcel(x)
	});
});

//导出
$(document).on('click', '.exportExcel', function() {
	var invtyEncd = $("#invtyEncd").val();
	var intoWhsSnglId = $(".intoWhsSnglId").val();
	var provrId = $("#provrId").val();
	var intoWhsDt1 = $(".intoWhsSnglDt1").val();
	var intoWhsDt2 = $(".intoWhsSnglDt2").val();
	var isNtChk = $("#isNtChk").val();
	var invtyClsEncd = $(".invtyClsEncd").val();

	var isNtRtnGood = $(".isNtRtnGood").val();
	var deptId = $(".deptId").val();
	var accNum = $("#user").val();
	var provrOrdrNum = $(".provrOrdrNum").val();
	var pursOrdrId = $("#pursOrdrId").val();
	var batNum = $("#batNum").val();
	var intlBat = $(".intlBat").val();
	var isNtBllg = $(".isNtBllg").val();
	var toGdsSngId = $(".toGdsSngId").val();
	var isNtBookEntry = $(".isNtBookEntry").val();
	var invtyCd = $(".invtyCd").val();
	var whsEncd = $("#whsEncd").val();
	var memo = $("#memo").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": invtyEncd,
			"intoWhsSnglId": intoWhsSnglId,
			"provrId": provrId,
			"intoWhsDt1": intoWhsDt1,
			"intoWhsDt2": intoWhsDt2,
			"invtyClsEncd": invtyClsEncd,
			"isNtChk": isNtChk,
			"isNtRtnGood": isNtRtnGood,
			"deptId": deptId,
			"accNum": accNum,
			"provrOrdrNum": provrOrdrNum,
			"pursOrdrId": pursOrdrId,
			"batNum": batNum,
			"intlBat": intlBat,
			"isNtBllg": isNtBllg,
			"toGdsSngId": toGdsSngId,
			"isNtBookEntry": isNtBookEntry,
			"invtyCd": invtyCd,
			"whsEncd":whsEncd,
			"memo":memo
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/purc/IntoWhs/printingIntoWhsList',
		type: 'post',
		data: Data,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		//开始加载动画
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
			var execlName = '入库单列表'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
})
