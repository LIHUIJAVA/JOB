var pages;
var page = 1;
var rowNum;
var tableSums = {};
$(function() {
	//加载动画html
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
		colNames: ['销售日期', '销售单号', '仓库名称', '客户编码', '客户名称', '存货编码', '存货代码', '存货名称',
			'对应条形码', '含税单价', '数量', '价税合计','税率', '箱规', '箱数', '生产日期', '批次', '失效日期', '保质期',
			"客户订单号", '表头备注','表体备注', '项目名称', "收件人", "收件人电话", "收件人地址",
			'销售类型', '业务类型', '业务员', '部门', '发货地址', '是否审核', '仓库编码', '主计量单位', '规格型号',
			'项目编码', '国际批次', '无税单价', '无税金额',  '税额', '是否退货', '主计量单位编码',
		],
		colModel: [{
				name: 'sellSnglDt',
				editable: true,
				align: 'center',
				sorttype: 'date' // 时间排序
			},
			{
				name: 'sellSnglId',
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
				hidden: true
			},
			{
				name: 'custNm',
				editable: false,
				align: 'center',
				hidden: true
			},
			{
				name: 'invtyEncd',
				editable: true,
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
			}, {
				name: 'cntnTaxUprc',
				editable: false,
				align: 'center',
				sortable: true
			},
			{
				name: 'qty',
				editable: true,
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
				name: 'taxRate',
				editable: true,
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
				name: 'prdcDt', //生产日期
				editable: true,
				align: 'center',
				sortable: true
			},
			{
				name: 'batNum',
				editable: true,
				align: 'center',
				sortable: false
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
				sortable: false
			},
			{
				name: 'custOrdrNum',
				editable: true,
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
				name: 'projNm',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'recvr',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'recvrTel',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'recvrAddr',
				editable: false,
				align: 'center',
				sortable: false
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
				editable: false,
				align: 'center',
			},
			{
				name: 'isNtChk',
				editable: true,
				align: 'center'
			},
			{
				name: 'whsEncd',
				editable: true,
				sortable: false,
				align: 'center',
			},
			{
				name: 'measrCorpNm',
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
				name: 'projEncd',
				editable: true,
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
				name: 'taxAmt',
				editable: false,
				align: 'center',
				sortable: true
			},
			{
				name: 'isNtRtnGoods',
				editable: true,
				align: 'center',
				hidden: true,
				sortable: false
			},
			{
				name: 'measrCorpId',
				editable: false,
				align: 'center',
				hidden: true,
				sortable: false
			},
		],
		autowidth: true,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		height: height,
		sortable: true, // 列拖拽
		autoScroll: true,
		shrinkToFit: false,
		rowNum: 500,
		rowList: [500, 1000, 3000, 5000],
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		//		multiboxonly: true,
		caption: "销售单列表", //表格的标题名字
		footerrow: true,
		gridComplete: function() {
			$("#jqGrids").footerData('set', { 
				"sellSnglDt": "总计",
				"qty": tableSums.qty,
				"prcTaxSum": tableSums.prcTaxSum,
				"noTaxAmt": tableSums.noTaxAmt,
				"taxAmt": tableSums.taxAmt,
				"bxQty": tableSums.bxQty
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
			sellList_search(index, sortorder)
		},
		//双击弹出采购订单
		ondblClickRow: function(rowid) {
			order(rowid);
		},
		onSortCol: function(index, colindex, sortorder) {
			switch(index) {
				case "sellSnglId":
					index = "ss.sell_sngl_id"
					break;
				case "sellSnglDt":
					index = "ss.sell_sngl_dt"
					break;
				case "qty":
					index = "sss.qty"
					break;
				case "cntnTaxUprc":
					index = "sss.cntn_tax_uprc"
					break;
				case "prcTaxSum":
					index = "sss.prc_tax_sum"
					break;
				case "noTaxUprc":
					index = "sss.no_tax_uprc"
					break;
				case "noTaxAmt":
					index = "sss.no_tax_amt"
					break;
				case "taxAmt":
					index = "sss.tax_amt"
					break;
				case "bxQty":
					index = "sss.bx_qty"
					break;
				case "prdcDt":
					index = "sss.prdc_dt"
					break;
				case "invldtnDt":
					index = "sss.invldtn_dt"
					break;
			}
			localStorage.setItem("index",index)
			localStorage.setItem("sortorder",sortorder)
			window.index = index;
			window.sortorder = sortorder;
			sellList_search(index, sortorder)
		}
	})

})

//查询按钮
$(document).on('click', '.search', function() {
	var sellSnglDt1 = $("input[name='sellSnglDt1']").val()
	$("input[name='sellSnglDt3']").val(sellSnglDt1)
	var sellSnglDt2 = $("input[name='sellSnglDt2']").val()
	$("input[name='sellSnglDt4']").val(sellSnglDt2)
	var index = ''
	var sortorder = 'desc'
	sellList_search(index, sortorder)
})

//查询按钮
$(document).on('click', '.sure', function() {
	var sellSnglDt1 = $("input[name='sellSnglDt3']").val()
	$("input[name='sellSnglDt1']").val(sellSnglDt1)
	var sellSnglDt2 = $("input[name='sellSnglDt4']").val()
	$("input[name='sellSnglDt2']").val(sellSnglDt2)
	
	var ecOrderId = $("input[name='ecOrderId1']").val()
	$("input[name='ecOrderId']").val(ecOrderId)
	
	$("#big_wrap").hide()
	$("#box").hide()
	var index = ''
	var sortorder = 'desc'
	sellList_search(index, sortorder)
	
	$("input[name='invtyEncd']").attr('id', 'invtyEncd');
	$("input[name='invtyEncd1']").attr('id', '');
	$("input[name='invtyNm']").attr('id', 'invtyNm');
	$("input[name='invtyNm1']").attr('id', '');
	
	$("input[name='whsEncd']").attr('id', 'whsEncd');
	$("input[name='whsEncd1']").attr('id', '');
	$("input[name='whsNm']").attr('id', 'whsNm');
	$("input[name='whsNm1']").attr('id', '');
	
	$("input[name='invtyClsEncd']").attr('id', 'invtyCls');
	$("input[name='invtyClsEncd1']").attr('id', '');
	
	$("input[name='invtyClsNm']").attr('id', 'invtyClsNm');
	$("input[name='invtyClsNm1']").attr('id', '');
	
	$("input[name='custId']").attr('id', 'custId');
	$("input[name='custId1']").attr('id', '');
	
	$("input[name='custNm']").attr('id', 'custNm');
	$("input[name='custNm1']").attr('id', '');
	
	$("input[name='ecOrderId']").attr('id', 'ecOrderId');
	$("input[name='ecOrderId1']").attr('id', '');
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

		$("input[name='whsEncd1']").attr('id', 'whsEncd');
		$("input[name='whsEncd']").attr('id', '');
		$("input[name='whsNm1']").attr('id', 'whsNm');
		$("input[name='whsNm']").attr('id', '');
		
		var whsEncd = $("input[name='whsEncd']").val()
		$("input[name='whsEncd1']").val(whsEncd)
		var whsNm = $("input[name='whsNm']").val()
		$("input[name='whsNm1']").val(whsNm)

		$("input[name='invtyClsEncd1']").attr('id', 'invtyCls');
		$("input[name='invtyClsEncd']").attr('id', '');
		var invtyClsEncd = $("input[name='invtyClsEncd']").val()
		$("input[name='invtyClsEncd1']").val(invtyClsEncd)
		
		$("input[name='invtyClsNm1']").attr('id', 'invtyClsNm');
		$("input[name='invtyClsNm']").attr('id', '');
		var invtyClsNm = $("input[name='invtyClsNm']").val()
		$("input[name='invtyClsNm1']").val(invtyClsNm)

		$("input[name='custId1']").attr('id', 'custId');
		$("input[name='custId']").attr('id', '');
		var custId = $("input[name='custId']").val()
		$("input[name='custId1']").val(custId)

		$("input[name='custNm1']").attr('id', 'custNm');
		$("input[name='custNm']").attr('id', '');
		var custNm = $("input[name='custNm']").val()
		$("input[name='custNm1']").val(custNm)
		
		$("input[name='ecOrderId1']").attr('id', 'ecOrderId');
		$("input[name='ecOrderId']").attr('id', '');
		var ecOrderId = $("input[name='ecOrderId']").val()
		$("input[name='ecOrderId1']").val(ecOrderId)

		$("input[name='sellSnglId1']").attr('class', 'sellSnglId');
		$("input[name='sellSnglId']").attr('class', '');
		var sellSnglId = $("input[name='sellSnglId']").val()
		$("input[name='sellSnglId1']").val(sellSnglId)

		$("select[name='isNtChk1']").attr('id', 'isNtChk');
		$("select[name='isNtChk']").attr('id', '');
		var isNtChk = $("select[name='isNtChk']").val()
		$("select[name='isNtChk1']").val(isNtChk)

		var sellSnglDt1 = $("input[name='sellSnglDt1']").val()
		$("input[name='sellSnglDt3']").val(sellSnglDt1)

		var sellSnglDt2 = $("input[name='sellSnglDt2']").val()
		$("input[name='sellSnglDt4']").val(sellSnglDt2)

		$("input[name='sellSnglDt3']").attr('class', 'sellSnglDt1 date date1');
		$("input[name='sellSnglDt1']").attr('class', 'date date1');

		$("input[name='sellSnglDt4']").attr('class', 'sellSnglDt2 date date2');
		$("input[name='sellSnglDt2']").attr('class', 'date date2');
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

	$("input[name='whsEncd1']").attr('id', '');
	$("input[name='whsEncd']").attr('id', 'whsEncd');
	$("input[name='whsNm1']").attr('id', '');
	$("input[name='whsNm']").attr('id', 'whsNm');

	$("input[name='invtyClsEncd1']").attr('id', '');
	$("input[name='invtyClsEncd']").attr('id', 'invtyCls');
	
	$("input[name='invtyClsNm1']").attr('id', '');
	$("input[name='invtyClsNm']").attr('id', 'invtyClsNm');

	$("input[name='custId1']").attr('id', '');
	$("input[name='custId']").attr('id', 'custId');
	$("input[name='custNm1']").attr('id', '');
	$("input[name='custNm']").attr('id', 'custNm');

	$("input[name='sellSnglId1']").attr('class', '');
	$("input[name='sellSnglId']").attr('class', 'sellSnglId');
	
	$("input[name='ecOrderId1']").attr('id', '');
	$("input[name='ecOrderId']").attr('id', 'ecOrderId');

	$("select[name='isNtChk1']").attr('id', '');
	$("select[name='isNtChk']").attr('id', 'isNtChk');

	$("input[name='sellSnglDt3']").attr('calendar', '');
	$("input[name='sellSnglDt1']").attr('calendar', 'YYYY-MM-DD');

	$("input[name='sellSnglDt4']").attr('calendar', '');
	$("input[name='sellSnglDt2']").attr('calendar', 'YYYY-MM-DD');

	$("input[name='sellSnglDt3']").attr('class', 'date date1');
	$("input[name='sellSnglDt1']").attr('class', 'sellSnglDt1 date date1');

	$("input[name='sellSnglDt4']").attr('class', 'date date2');
	$("input[name='sellSnglDt2']").attr('class', 'sellSnglDt2 date date2');
}

function sellList_search(index, sortorder) {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var sellSnglId = $(".sellSnglId").val();
	var custId = $("#custId").val();
	var invtyEncd = $("#invtyEncd").val();
	var sellSnglDt1 = $(".sellSnglDt1").val();
	var sellSnglDt2 = $(".sellSnglDt2").val();
	var invtyClsEncd = $("#invtyCls").val();
	var isNtChk = $("#isNtChk").val();
	var whsEncd = $("#whsEncd").val()

	var deptId = $("input[name='deptId']").val();
	var txId = $("input[name='txId']").val();
	var ecOrderId = $("#ecOrderId").val();
	var custOrdrNum = $("input[name='custOrdrNum']").val();
	var batNum = $("input[name='batNum']").val();
	var intlBat = $("input[name='intlBat']").val();
	var bizTypId = $("select[name='bizTypId'] option:selected").val();
	var sellTypId = $("select[name='sellTypId'] option:selected").val();
	var isNtBllg = $("select[name='isNtBllg'] option:selected").val();
	var invtyCd = $(".invtyCd").val();
	var accNum = $("#user").val();
	var memo = $("#memo").val();
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": invtyEncd,
			"sellSnglId": sellSnglId,
			"invtyClsEncd": invtyClsEncd,
			"custId": custId,
			"sellSnglDt1": sellSnglDt1,
			"sellSnglDt2": sellSnglDt2,
			"isNtChk": isNtChk,
			"whsEncd": whsEncd,

			"deptId": deptId,
			"txId": txId,
			"ecOrderId": ecOrderId,
			"custOrdrNum": custOrdrNum,
			"batNum": batNum,
			"intlBat": intlBat,
			"bizTypId": bizTypId,
			"sellTypId": sellTypId,
			"isNtBllg": isNtBllg,
			"accNum": accNum,
			'invtyCd': invtyCd,
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
		url: url + '/mis/purc/SellSngl/querySellSnglListOrderBy',
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
			alert("error")
		}
	});
}

function order(rowid) {
	//获得行数据
	var rowDatas = $("#jqGrids").jqGrid('getRowData', rowid);
	localStorage.setItem("isNtChk", rowDatas.isNtChk);
	localStorage.setItem("sellSnglId", rowDatas.sellSnglId);
	window.open("../../Components/sell/sellSngl.html?1");
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
		obj.sellSnglId = $("#jqGrids").getCell(ids[i], "sellSnglId").toString();
		obj.isNtChk = x;
		//建一个数组，把单个对象添加到数组中
		rowData[i] = obj;
	}
	var result = [];
	var obj = {};
	for(var i = 0; i < rowData.length; i++) {
		if(!obj[rowData[i].sellSnglId]) {
			result.push(rowData[i]);
			obj[rowData[i].sellSnglId] = true;
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
			url: url + '/mis/purc/SellSngl/updateSellSnglIsNtChk',
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
				alert(data.respHead.message)
				if(data.respHead.isSuccess == true) {
					sellList_search()
				}
			},
			error: function() {
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
			var data = $("#jqGrids").getCell(ids[i], "sellSnglId");
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
			var sellSnglId = result.toString();
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"sellSnglId": sellSnglId
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/purc/SellSngl/deleteSellSnglList',
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
						sellList_search()
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
		var x = "/mis/purc/SellSngl/uploadSellSnglFile"
		importExcel(x)
	});
	$(".dropdown-menu .li1").click(function() {
		var x = "/mis/purc/SellSngl/uploadSellSnglFileU8"
		importExcel(x)
	});
});

//导出
$(document).on('click', '.exportExcel', function() {
	var sellSnglId = $(".sellSnglId").val();
	var custId = $("#custId").val();
	var invtyEncd = $("#invtyEncd").val();
	var sellSnglDt1 = $(".sellSnglDt1").val();
	var sellSnglDt2 = $(".sellSnglDt2").val();
	var invtyClsEncd = $("#invtyCls").val();
	var isNtChk = $("#isNtChk").val();
	var whsEncd = $("#whsEncd").val()

	var deptId = $("input[name='deptId']").val();
	var txId = $("input[name='txId']").val();
	var ecOrderId = $("input[name='ecOrderId']").val();
	var custOrdrNum = $("input[name='custOrdrNum']").val();
	var batNum = $("input[name='batNum']").val();
	var intlBat = $("input[name='intlBat']").val();
	var bizTypId = $("select[name='bizTypId'] option:selected").val();
	var sellTypId = $("select[name='sellTypId'] option:selected").val();
	var isNtBllg = $("select[name='isNtBllg'] option:selected").val();
	var invtyCd = $(".invtyCd").val();
	var accNum = $("#user").val();
	var memo = $("#memo").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": invtyEncd,
			"sellSnglId": sellSnglId,
			"invtyClsEncd": invtyClsEncd,
			"custId": custId,
			"sellSnglDt1": sellSnglDt1,
			"sellSnglDt2": sellSnglDt2,
			"isNtChk": isNtChk,
			"whsEncd": whsEncd,

			"deptId": deptId,
			"txId": txId,
			"ecOrderId": ecOrderId,
			"custOrdrNum": custOrdrNum,
			"batNum": batNum,
			"intlBat": intlBat,
			"bizTypId": bizTypId,
			"sellTypId": sellTypId,
			"isNtBllg": isNtBllg,
			"accNum": accNum,
			'invtyCd': invtyCd,
			"memo":memo
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/purc/SellSngl/printingSellSnglList',
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
			var execlName = '销售单列表'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})

})