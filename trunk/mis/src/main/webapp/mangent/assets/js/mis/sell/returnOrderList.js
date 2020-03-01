//退货单列表
var pages;
var page = 1;
var rowNum;
var tableSums = {};
$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	$("#box").hide()
})
//表格初始化
$(function() {
	localStorage.removeItem("index")
	localStorage.removeItem("sortorder")
	allHeight();
	$("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['退货日期','退货单号','仓库名称', '客户编码', '客户名称', '存货编码', '存货代码','存货名称', '对应条形码',
		'含税单价', '数量','价税合计', '箱规', '箱数','生产日期', '保质期','失效日期', '批次','客户订单号','表头备注','表体备注', '项目名称', 
		
		'销售类型', '业务类型', '业务员', '部门',  '发货地址', '销售单号', '是否审核','仓库编码',  '主计量单位', '主计量单位编码',
		'是否退货', '规格型号','无税单价', '无税金额', '税率', '税额', '国际批次','快递单号'
		],
		colModel: [{
				name: 'rtnGoodsDt',
				editable: true,
				align: 'center',
				sorttype: 'date' // 时间排序
			},
			{
				name: 'rtnGoodsId',
				editable: true,
				align: 'center',
			},
			{
				name: 'whsNm',
				editable: false,
				align: 'center',
				sortable: false,
			},
			{
				name: 'custId',
				editable: false,
				align: 'center',
			},
			{
				name: 'custNm',
				editable: false,
				align: 'center',
			},
			{
				name: 'invtyEncd',
				editable: true,
				align: 'center',
				sortable: false

			},
			{
				name: 'invtyCd',
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
				name: 'crspdBarCd',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'cntnTaxUprc',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'qty',
				editable: true,
				align: 'center',
				sortable: true,
				editrules: {
					number: true
				}
			},
			{
				name: 'prcTaxSum',
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
				sortable: false
			},
			{
				name: 'prdcDt', //生产日期
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'baoZhiQiDt',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'invldtnDt', //失效日期
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'batNum',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'custOrdrNum',
				editable: false,
				align: 'center',
			},
			{
				name: 'memo',
				editable: true,
				align: 'center'
			},
			{
				name: 'memos',
				editable: true,
				align: 'center'
			},
			{
				name: 'projNm',
				editable: true,
				align: 'center'
			},
			{
				name: 'sellTypNm',
				editable: true,
				align: 'center'

			},
			{
				name: 'bizTypNm',
				editable: false,
				align: 'center',
			},
			{
				name: 'userName',
				editable: false,
				align: 'center'
			},
			{
				name: 'deptName',
				editable: false,
				align: 'center'
			},
			{
				name: 'recvrAddr',
				editable: true,
				align: 'center'
			},
			{
				name: 'sellOrdrId',
				editable: true,
				align: 'center'
			},
			{
				name: 'isNtChk',
				editable: true,
				align: 'center'
			},
			{
				name: 'whsEncd',
				editable: true,
				align: 'center',
				sortable: false,
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
				name: 'isNtRtnGoods',
				editable: true,
				align: 'center',
				hidden: true,
				sortable: false
			},
			{
				name: 'spcModel',
				editable: false,
				align: 'center',
				sortable: false
			},

			{
				name: 'noTaxUprc',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'noTaxAmt',
				editable: false,
				align: 'center',
				sortable: false
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
				sortable: false
			},
			{
				name: 'intlBat', //国际批次
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'expressNum', 
				editable: true,
				align: 'center',
				sortable: false
			},
		],
		sortable: true,
		autowidth: true,
		height: height,
		loadonce: true,
		autoScroll: true,
		sortable: true, // 列拖拽
		shrinkToFit: false,
		rownumbers: true,
		forceFit: true,
		rowNum: 500,
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		//		multiboxonly: true,
		caption: "退货单列表", //表格的标题名字
		footerrow: true,
		gridComplete: function() {
			$("#jqGrids").footerData('set', { 
				"rtnGoodsDt": "总计",
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
			search(index, sortorder);
		},
		//双击弹出采购订单
		ondblClickRow: function(rowid) {
			order(rowid);
		},
		onSortCol: function(index, colindex, sortorder) {
			switch(index) {
				case "rtnGoodsId":
					index = "rg.rtn_goods_id"
					break;
				case "rtnGoodsDt":
					index = "rg.rtn_goods_dt"
					break;
				case "qty":
					index = "rgs.qty"
					break;
				case "cntnTaxUprc":
					index = "rgs.cntn_tax_uprc"
					break;
				case "prcTaxSum":
					index = "rgs.prc_tax_sum"
					break;
				case "noTaxUprc":
					index = "rgs.no_tax_uprc"
					break;
				case "noTaxAmt":
					index = "rgs.no_tax_amt"
					break;
				case "taxAmt":
					index = "rgs.tax_amt"
					break;
				case "bxQty":
					index = "rgs.bx_qty"
					break;
				case "prdcDt":
					index = "rgs.prdc_dt"
					break;
				case "invldtnDt":
					index = "rgs.invldtn_dt"
					break;
			}
			localStorage.setItem("index",index)
			localStorage.setItem("sortorder",sortorder)
			window.index = index;
			window.sortorder = sortorder;
			search(index, sortorder)
		}
	})

})

//查询按钮
$(document).on('click', '.search', function() {
	var rtnGoodsDt1 = $("input[name='rtnGoodsDt1']").val()
	$("input[name='rtnGoodsDt3']").val(rtnGoodsDt1)
	var rtnGoodsDt2 = $("input[name='rtnGoodsDt2']").val()
	$("input[name='rtnGoodsDt4']").val(rtnGoodsDt2)
	var index = ''
	var sortorder = 'desc'
	search(index, sortorder)
})

//查询按钮
$(document).on('click', '.sure', function() {
	$("#big_wrap").hide()
	$("#box").hide()
	var rtnGoodsDt1 = $("input[name='rtnGoodsDt3']").val()
	$("input[name='rtnGoodsDt1']").val(rtnGoodsDt1)
	var rtnGoodsDt2 = $("input[name='rtnGoodsDt4']").val()
	$("input[name='rtnGoodsDt2']").val(rtnGoodsDt2)
	var index = ''
	var sortorder = 'desc'
	search(index, sortorder)
	
	$("input[name='invtyEncd']").attr('id', 'invtyEncd');
	$("input[name='invtyEncd1']").attr('id', '');
	$("input[name='invtyNm']").attr('id', 'invtyNm');
	$("input[name='invtyNm1']").attr('id', '');
	
	$("input[name='invtyClsEncd']").attr('id', 'invtyCls');
	$("input[name='invtyClsEncd1']").attr('id', '');
	$("input[name='invtyClsNm']").attr('id', 'invtyClsNm');
	$("input[name='invtyClsNm1']").attr('id', '');
	
	$("input[name='custId']").attr('id', 'custId');
	$("input[name='custId1']").attr('id', '');
	$("input[name='custNm']").attr('id', 'custNm');
	$("input[name='custNm1']").attr('id', '');
})

$(function() {
	$(".more").click(function() {
		$("#big_wrap").show()
		$("#box").show()
		$("input[name='invtyEncd1']").attr('id', 'invtyEncd');
		$("input[name='invtyEncd']").attr('id', '');
		$("input[name='invtyNm1']").attr('id', 'invtyNm');
		$("input[name='invtyNm']").attr('id', '');
		var invtyEncd = $("input[name='invtyEncd']").val()
		var invtyEncd1 = $("input[name='invtyEncd1']").val(invtyEncd)
		var invtyNm = $("input[name='invtyNm']").val()
		var invtyNm1 = $("input[name='invtyNm1']").val(invtyNm)

		$("input[name='invtyClsEncd1']").attr('id', 'invtyCls');
		$("input[name='invtyClsEncd']").attr('id', '');
		var invtyClsEncd = $("input[name='invtyClsEncd']").val()
		var invtyClsEncd1 = $("input[name='invtyClsEncd1']").val(invtyClsEncd)
		
		$("input[name='invtyClsNm1']").attr('id', 'invtyClsNm');
		$("input[name='invtyClsNm']").attr('id', '');
		var invtyClsNm = $("input[name='invtyClsNm']").val()
		var invtyClsNm1 = $("input[name='invtyClsNm1']").val(invtyClsNm)

		$("input[name='custId1']").attr('id', 'custId');
		$("input[name='custId']").attr('id', '');
		var custId = $("input[name='custId']").val()
		var custId1 = $("input[name='custId1']").val(custId)

		$("input[name='custNm1']").attr('id', 'custNm');
		$("input[name='custNm']").attr('id', '');
		var custNm = $("input[name='custNm']").val()
		var custNm1 = $("input[name='custNm1']").val(custNm)

		$("input[name='rtnGoodsId1']").attr('id', 'rtnGoodsId');
		$("input[name='rtnGoodsId']").attr('id', '');
		var rtnGoodsId = $("input[name='rtnGoodsId']").val()
		var rtnGoodsId1 = $("input[name='rtnGoodsId1']").val(rtnGoodsId)

		$("select[name='isNtChk1']").attr('id', 'isNtChk');
		$("select[name='isNtChk']").attr('id', '');
		var isNtChk = $("select[name='isNtChk']").val()
		var isNtChk1 = $("select[name='isNtChk1']").val(isNtChk)

		var rtnGoodsDt1 = $("input[name='rtnGoodsDt1']").val()
		$("input[name='rtnGoodsDt3']").val(rtnGoodsDt1)
		
		var rtnGoodsDt2 = $("input[name='rtnGoodsDt2']").val()
		$("input[name='rtnGoodsDt4']").val(rtnGoodsDt2)

		$("input[name='rtnGoodsDt3']").attr('class', 'rtnGoodsDt1 date date1');
		$("input[name='rtnGoodsDt1']").attr('class', 'date date1');

		$("input[name='rtnGoodsDt4']").attr('class', 'rtnGoodsDt2 date date2');
		$("input[name='rtnGoodsDt2']").attr('class', 'date date2');
	})
	$(".cancel").click(function(e) {
		$("#big_wrap").hide()
		$("#box").hide()
		back();
		$("#box input").val("")
		$("#box select").val("")
	});
})

function back() {
	$("input[name='invtyEncd1']").attr('id', '');
	$("input[name='invtyEncd']").attr('id', 'invtyEncd');
	$("input[name='invtyNm1']").attr('id', '');
	$("input[name='invtyNm']").attr('id', 'invtyNm');

	$("input[name='invtyClsEncd1']").attr('id', '');
	$("input[name='invtyClsEncd']").attr('id', 'invtyCls');
	
	$("input[name='invtyClsNm1']").attr('id', '');
	$("input[name='invtyClsNm']").attr('id', 'invtyClsNm');

	$("input[name='custId1']").attr('id', '');
	$("input[name='custId']").attr('id', 'custId');
	$("input[name='custNm1']").attr('id', '');
	$("input[name='custNm']").attr('id', 'custNm');

	$("input[name='rtnGoodsId1']").attr('id', '');
	$("input[name='rtnGoodsId']").attr('di', 'rtnGoodsId1');

	$("select[name='isNtChk1']").attr('id', '');
	$("select[name='isNtChk']").attr('id', 'isNtChk');

	$("input[name='rtnGoodsDt3']").attr('calendar', '');
	$("input[name='rtnGoodsDt1']").attr('calendar', 'YYYY-MM-DD');

	$("input[name='rtnGoodsDt4']").attr('calendar', '');
	$("input[name='rtnGoodsDt2']").attr('calendar', 'YYYY-MM-DD');

	$("input[name='rtnGoodsDt3']").attr('class', 'date date1');
	$("input[name='rtnGoodsDt1']").attr('class', 'rtnGoodsDt1 date date1');

	$("input[name='rtnGoodsDt4']").attr('class', 'date date2');
	$("input[name='rtnGoodsDt2']").attr('class', 'rtnGoodsDt2 date date2');
}

function search(index, sortorder) {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var invtyEncd = $("#invtyEncd").val();
	var rtnGoodsId = $("#rtnGoodsId").val();
	var custId = $("#custId").val();
	var rtnGoodsDt1 = $(".rtnGoodsDt1").val();
	var invtyClsEncd = $("#invtyCls").val();
	var rtnGoodsDt2 = $(".rtnGoodsDt2").val();
	var isNtChk = $("#isNtChk").val();

	var deptId = $("input[name='deptId']").val();
	var txId = $("input[name='txId']").val();
	var whsEncd = $("input[name='whsEncd']").val();
	var custOrdrNum = $("input[name='custOrdrNum']").val();
	var batNum = $("input[name='batNum']").val();
	var intlBat = $("input[name='intlBat']").val();
	var invtyCd = $("input[name='invtyCd']").val();
	var bizTypId = $("select[name='bizTypId'] option:selected").val();
	var sellTypId = $("select[name='sellTypId'] option:selected").val();
	var isNtBllg = $("select[name='isNtBllg'] option:selected").val();
	var accNum = $("#user").val();
	var memo = $("#memo").val();
	var sellOrdrId = $("input[name='sellOrdrId']").val();
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": invtyEncd,
			"rtnGoodsId": rtnGoodsId,
			"custId": custId,
			"rtnGoodsDt1": rtnGoodsDt1,
			"invtyClsEncd": invtyClsEncd,
			"rtnGoodsDt2": rtnGoodsDt2,
			"isNtChk": isNtChk,

			"deptId": deptId,
			"txId": txId,
			"whsEncd": whsEncd,
			"custOrdrNum": custOrdrNum,
			"batNum": batNum,
			"invtyCd": invtyCd,
			"bizTypId": bizTypId,
			"sellTypId": sellTypId,
			"isNtBllg": isNtBllg,
			"accNum": accNum,
			"intlBat": intlBat,
			"sellOrdrId":sellOrdrId,
			"memo":memo,
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
		url: url + '/mis/purc/RtnGoods/queryRtnGoodsListOrderBy',
		async: true,
		data: saveData,
		dataType: 'json',
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
			tableSums = data.respBody.tableSums;
			var list = data.respBody.list;
			for(var i = 0; i < list.length; i++) {
				if(list[i].isNtChk == 0) {
					list[i].isNtChk = "否"
				} else if(list[i].isNtChk == 1) {
					list[i].isNtChk = "是"
				}
			}
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
		error: function() {
			alert("查询失败")
		}
	});
}

function order(rowid) {
	//获得行数据
	var rowDatas = $("#jqGrids").jqGrid('getRowData', rowid);
	localStorage.setItem("rtnGoodsId", rowDatas.rtnGoodsId);
	localStorage.setItem("isNtChk", rowDatas.isNtChk);
	window.open("../../Components/sell/returnOrder.html?1", 'height=700, width=1000, top=200, left=300,location=no, status=no');
}

function ntChk(x) {

	//获得选中行的行号
	var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
	//获取选择行的provrId
	//对象数组
	var rowData = [];
	for(var i = 0; i < ids.length; i++) {
		var obj = {}; //对象
		//选中行的id
		//把选中行的i添加到数据对象中
		obj.rtnGoodsId = $("#jqGrids").getCell(ids[i], "rtnGoodsId").toString();
		obj.isNtChk = x;
		rowData[i] = obj;
	}
	var result = [];
	var obj = {};
	for(var i = 0; i < rowData.length; i++) {
		if(!obj[rowData[i].rtnGoodsId]) {
			result.push(rowData[i]);
			obj[rowData[i].rtnGoodsId] = true;
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
			url: url + '/mis/purc/RtnGoods/updateRtnGoodsIsNtChkList',
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
			error: function(error) {
				alert("审核失败")
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
			var gr = $("#jqGrids").getGridParam('selrow');
			//选中行的id
			var data = $("#jqGrids").getCell(ids[i], "rtnGoodsId");
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
		if(rowData.length == 0) {
			alert("请选择单据!")
		} else if(confirm("确定删除？")) {
			var rtnGoodsId = result.toString();
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"rtnGoodsId": rtnGoodsId,

				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/purc/RtnGoods/deleteRtnGoodsList',
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
			beforeSend: function() {
				$("#mengban").css("display", "block");
				$("#loader").css("display", "block");
			},
			complete: function() {
				$("#mengban").css("display", "none");
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
		var x = "/mis/purc/RtnGoods/uploadRtnGoodsFile"
		importExcel(x)
	});
	$(".dropdown-menu .li1").click(function() {
		var x = "/mis/purc/RtnGoods/uploadRtnGoodsFileU8"
		importExcel(x)
	});
});

//导出
var arr = [];
var obj = {}

$(document).on('click', '.exportExcel', function() {
	var invtyEncd = $("#invtyEncd").val();
	var rtnGoodsId = $("#rtnGoodsId").val();
	var custId = $("#custId").val();
	var rtnGoodsDt1 = $(".rtnGoodsDt1").val();
	var invtyClsEncd = $("#invtyCls").val();
	var rtnGoodsDt2 = $(".rtnGoodsDt2").val();
	var isNtChk = $("#isNtChk").val();

	var deptId = $("input[name='deptId']").val();
	var txId = $("input[name='txId']").val();
	var whsEncd = $("input[name='whsEncd']").val();
	var custOrdrNum = $("input[name='custOrdrNum']").val();
	var batNum = $("input[name='batNum']").val();
	var intlBat = $("input[name='intlBat']").val();
	var invtyCd = $("input[name='invtyCd']").val();
	var bizTypId = $("select[name='bizTypId'] option:selected").val();
	var sellTypId = $("select[name='sellTypId'] option:selected").val();
	var isNtBllg = $("select[name='isNtBllg'] option:selected").val();
	var accNum = $("#user").val();
	var memo = $("#memo").val();
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": invtyEncd,
			"rtnGoodsId": rtnGoodsId,
			"custId": custId,
			"rtnGoodsDt1": rtnGoodsDt1,
			"invtyClsEncd": invtyClsEncd,
			"rtnGoodsDt2": rtnGoodsDt2,
			"isNtChk": isNtChk,

			"deptId": deptId,
			"txId": txId,
			"whsEncd": whsEncd,
			"custOrdrNum": custOrdrNum,
			"batNum": batNum,
			"invtyCd": invtyCd,
			"bizTypId": bizTypId,
			"sellTypId": sellTypId,
			"isNtBllg": isNtBllg,
			"accNum": accNum,
			"intlBat": intlBat,
			"memo":memo
		}
	};
	var Data = JSON.stringify(savedata);
	$.ajax({
		url: url + '/mis/purc/RtnGoods/printingRtnGoodsList',
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
			var execlName = '退货单列表'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
})
