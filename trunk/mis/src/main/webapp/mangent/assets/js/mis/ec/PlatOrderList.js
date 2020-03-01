var count;
var pages;
var page = 1;
var rowNum;
//点击表格图标显示仓库列表
$(function() {
	$(".batch").hide()
	//	仓库
	$(document).on('click', '.whsEncd_biaoge', function() {
		window.open("../../Components/baseDoc/whsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	//	存货编码
	$(document).on('click', '.invtyEncd_biaoge', function() {
		window.open("../../Components/baseDoc/invtyList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	//	批号
	$(document).on('click', '.batNum_biaoge', function() {
		window.open("../../Components/baseDoc/batNum.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
//	//	存货分类
//	$(document).on('click', '.invtyClsEncd_biaoge', function() {
//		window.open("../../Components/baseDoc/invtyTree.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
//	});
//	//	部门
//	$(document).on('click', '.depId_biaoge', function() {
//		window.open("../../Components/baseDoc/deptDocList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
//	});
//	//	客户
//	$(document).on('click', '.custId_biaoge', function() {
//		window.open("../../Components/baseDoc/custDocList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
//	});
//	//	业务员
//	$(document).on('click', '.accNum_biaoge', function() {
//		window.open("../../Components/baseDoc/userList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
//	});
	$(document).on('click', '.deliverWhs_biaoge', function() {
		window.open("../../Components/baseDoc/whsList.html", 'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	$(document).on('click', '.expressCom_biaoge', function() {
		window.open("../../Components/whs/express_cropList.html", 'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	$(document).on('click', '.storeId_biaoge', function() {
		window.open("../../Components/baseDoc/storeRecordList.html", 'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})
$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	pageInit();
});

//查询按钮
$(document).on('click', '#find', function() {
	search()
})

//条件查询
function search() {
	var orderId = $("input[name='orderId1']").val();
	var storeId = $("input[name='storeId1']").val();
	var isAudit = $("select[name='isAudit1']").val();
	var buyerId = $("input[name='buyerId']").val();
	var recName = $("input[name='recName']").val();
	var ecOrderId = $("input[name='ecOrderId']").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var startDate = $("input[name='startDate']").val();
	var endDate = $("input[name='endDate']").val();
	var memo = $("input[name='memo']").val();
	var deliverWhs = $("input[name='deliverWhs']").val();
	var expressCom = $("input[name='expressCom']").val();
	var isShip = $("select[name='isShip']").val();
	var isClose = $("select[name='isClose']").val();
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"orderId": orderId,
			"storeId": storeId,
			"invtyEncd": invtyEncd,
			"isAudit": isAudit,
			"buyerId": buyerId,
			"recName": recName,
			"memo": memo,
			"ecOrderId": ecOrderId,
			"startDate": startDate,
			"endDate": endDate,
			"deliverWhs":deliverWhs,
			"expressCom":expressCom,
			"isShip": isShip,
			"isClose": isClose,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/ec/platOrder/orderssssList',
		async: true,
		data: postD2,
		dataType: 'json',
		//开始加载动画  添加到ajax里面
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");

		},
		success: function(data) {
			var list = data.respBody.list;
			for(var i = 0; i < list.length; i++) {
				if(list[i].isGift == 0) {
					list[i].isGift = "否"
				} else if(list[i].isGift == 1) {
					list[i].isGift = "是"
				}
			}
			var mydata = {};
			mydata.rows = list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#sales_jqGrids").jqGrid("clearGridData");
			$("#sales_jqGrids").jqGrid("setGridParam", {
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
			console.log(error)
		}
	});

}

function pageInit() {
	allHeight()
	jQuery("#sales_jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['平台订单号','订单编号','店铺编号','店铺名称','交易时间','付款时间','商品编号',
		'商品sku','平台商品名称','商品单价','商品数量','商品金额','系统优惠金额','卖家调整金额','实付金额','存货编码','存货名称',
		'存货数量','母件编码','母件名称','批号','生产日期','失效日期','发货仓库编码','发货仓库名称','快递公司编码',
		'快递公司名称','快递单号','可退数量','可退金额','是否赠品','收货人姓名','收货人手机号','省','市','区','镇','详细地址','备注'], //jqGrid的列显示名字
		colModel: [{
				name: 'ecOrderId',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'orderId',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'storeId',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'storeName',
				align: "center",
				editable: true,
				sorttype:'date',
			},
			{
				name: 'tradeDt',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'payTime',
				align: "center",
				editable: true,
				sorttype:'date',
			},
			{
				name: 'goodId',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'goodSku',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'goodName',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'goodPrice',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'goodNum',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'goodMoney',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'discountMoney',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'adjustMoney',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'payMoney',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'invId',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'invName',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'invNum',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'ptoCode',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'ptoName',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'batchNo',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'prdcDt',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'invldtnDt',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'deliverWhs',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'deliverWhsName',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'expressCode',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'expressName',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'expressNo',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'canRefNum',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'canRefMoney',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'isGift',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'recName',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'recMobile',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'province',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'city',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'county',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'town',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'address',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'memo',
				align: "center",
				editable: true,
				sorttype:'integer',
			}
		],
		sortable: true,
		autowidth: true,
		rownumbers: true,
		autoScroll: true,
		sortable: true, // 列拖拽
		shrinkToFit: false,
		height: height,
		loadonce: true,
		forceFit: true,
		rowNum: 500,
		rowList: [500, 1000, 3000, 5000],
		pager: '#sales_jqgridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "订单列表", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#sales_jqGrids").getGridParam('records'); //获取返回的记录数
			page = $("#sales_jqGrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#sales_jqGrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			search()
		},
		ondblClickRow: function() {
			order1();
		},
		footerrow: true,
		gridComplete: function() {
			var goodNum = $("#sales_jqGrids").getCol('goodNum', false, 'sum');
			var goodMoney = $("#sales_jqGrids").getCol('goodMoney', false, 'sum');
			var discountMoney = $("#sales_jqGrids").getCol('discountMoney', false, 'sum');
			var adjustMoney = $("#sales_jqGrids").getCol('adjustMoney', false, 'sum');
			var payMoney = $("#sales_jqGrids").getCol('payMoney', false, 'sum');
			var invNum = $("#sales_jqGrids").getCol('invNum', false, 'sum');
			var canRefNum = $("#sales_jqGrids").getCol('canRefNum', false, 'sum');
			var canRefMoney = $("#sales_jqGrids").getCol('canRefMoney', false, 'sum');
			$("#sales_jqGrids").footerData('set', { 
				"ecOrderId": "本页合计",
				goodNum: goodNum.toFixed(prec),
				canRefNum: canRefNum.toFixed(prec),
				invNum: invNum.toFixed(prec),
				goodMoney : precision(goodMoney,2),
				payMoney : precision(payMoney,2),
				adjustMoney : precision(adjustMoney,2),
				discountMoney : precision(discountMoney,2),
				canRefMoney : precision(canRefMoney,2)
			}          );
			
		},
	})
	//	search()
}
function order1() {
	//获得行号
	var gr = $("#sales_jqGrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	//debugger
	var rowData = $("#sales_jqGrids").jqGrid('getRowData', gr);
	localStorage.setItem("orderId", rowData.orderId);
	window.open("../../Components/ec/platOrderDet.html?1");
	

}
// 批量查询
$(function() {
	$(".batchSelect").click(function() {
		$(".batch").show()
		$(".batch").css("opacity",1)
	})
	$(".queding").click(function() {
		var ecOrderIds = $("textarea[name='ecOrderIds']").val();
		var num = ecOrderIds.replace(/\n/g,',');
		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				"ecOrderIds": num
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			type: "post",
			contentType: 'application/json; charset=utf-8',
			url: url + '/mis/ec/platOrder/batchList',
			async: true,
			data: saveData,
			dataType: 'json',
			//开始加载动画  添加到ajax里面
			beforeSend: function() {
				$(".zhezhao").css("display", "block");
				$("#loader").css("display", "block");
	
			},
			success: function(data) {
				$(".toExamine").removeClass("gray")
				$(".toExamine").attr("disabled",false)
				var list = data.respBody.list;
				for(var i = 0; i < list.length; i++) {
					if(list[i].isGift == 0) {
						list[i].isGift = "否"
					} else if(list[i].isGift == 1) {
						list[i].isGift = "是"
					}
				}
				var mydata = {};
				mydata.rows = data.respBody.list;
				mydata.page = page;
				mydata.records = data.respBody.count;
				mydata.total = data.respBody.pages;
				$("#sales_jqGrids").jqGrid("clearGridData");
				$("#sales_jqGrids").jqGrid("setGridParam", {
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
				$(".batch").hide()
				$(".batch").css("opacity",0)
				$("textarea[name='ecOrderIds']").val('')
			},
			//结束加载动画
			complete: function() {
				$(".zhezhao").css("display", "none");
				$("#loader").css("display", "none");
			},
			error: function() {
				console.log(error)
			}
		});
	})
	$(".quxiao").click(function() {
		$(".batch").hide()
		$(".batch").css("opacity",0)
		$("textarea[name='ecOrderIds']").val('')
	})
})

//导出
$(document).on('click', '.exportExcel', function() {
	
	var orderId = $("input[name='orderId1']").val();
	var storeId = $("input[name='storeId1']").val();
	var isAudit = $("select[name='isAudit1']").val();
	var buyerId = $("input[name='buyerId']").val();
	var recName = $("input[name='recName']").val();
	var ecOrderId = $("input[name='ecOrderId']").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var startDate = $("input[name='startDate']").val();
	var endDate = $("input[name='endDate']").val();
	var deliverWhs = $("input[name='deliverWhs']").val();
	var expressCom = $("input[name='expressCom']").val();
	var isShip = $("select[name='isShip']").val();
	var isClose = $("select[name='isClose']").val();
	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"orderId": orderId,
			"storeId": storeId,
			"invtyEncd": invtyEncd,
			"isAudit": isAudit,
			"buyerId": buyerId,
			"recName": recName,
			"ecOrderId": ecOrderId,
			"startDate": startDate,
			"endDate": endDate,
			"deliverWhs":deliverWhs,
			"expressCom":expressCom,
			"isShip": isShip,
			"isClose": isClose
		}
	};
	var Data = JSON.stringify(data2);
	$.ajax({
		url: url3 + "/mis/ec/platOrder/exportOrderssssList",
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
			var execlName = '订单列表'
			ExportData(list, execlName)
		},
		error: function() {
			console.log(error)
		}
	})
	
})