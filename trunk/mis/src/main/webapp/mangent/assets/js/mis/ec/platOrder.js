var count;
var pages;
var page = 1;
var rowNum;

$(function() {
	$("#mengban").hide()
	$("#mengban1").hide()
	$(".batch").hide()
	//点击表格图标显示店铺档案列表
	$(document).on('click', '.storeId_biaoge', function() {
		$(".box").hide()
		$(".formSave_box").css("opacity", 1)
		$(".oneSerch_List").hide()
		/*------点击下载按钮加载动态下拉电商平台-------*/
		var savedata = {
			'reqHead': reqhead,
			'reqBody': {
				'ecId': "",
				'pageNo': 1,
				'pageSize': 500
			},
		};
		var saveData = JSON.stringify(savedata)
		$.ajax({
			type: 'post',
			contentType: 'application/json; charset=utf-8',
			url: url + '/mis/ec/ecPlatform/queryList',
			async: true,
			data: saveData,
			dataType: 'json',
			success: function(data) {
				list = data.respBody.list;
				var option_html = '';
				option_html += '<option value="0" disabled selected>' + "请选择" + "</option>"
				for(i = 0; i < list.length; i++) {
					option_html += '<option value="' + list[i].ecId + '"' + 'id="ab">' + list[i].ecName + "</option>"
				}
				window.pro = $(".ecId").first().children("option").val()
				$(".ecId").html(option_html);
				$(".ecId").change(function(e) {
					window.val = this.value;
					pro = this.value
					window.localStorage.setItem("pro",pro);
				})
				
			},
			error: function() {
				console.log(error)
			}
		})
	});
	$(document).on('click', '.searchOne', function() {
		oneSerch()
	});
	$(document).on('click', '.save', function() {
		$(".box").show()
		$(".oneSerch_List").css("opacity", 0)
		$(".oneSerch_List").hide()
//		$(".oneSerch_List").hide()
	});
	$(document).on('click', '.storeId1_biaoge', function() {
		if($("select[name='ecId']").val() != null) {
			window.open("../../Components/baseDoc/storeRecordList.html", 'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
		} else {
			alert("请选择电商平台")
		}
	});
//	$(document).on('click', '.goodId_biaoge', function() {
//		window.open("../../Components/baseDoc/goodList.html", 'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
//	});
	$(document).on('click', '.deliverWhs_biaoge', function() {
		window.open("../../Components/baseDoc/whsList.html", 'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	$(document).on('click', '.expressCom_biaoge', function() {
		window.open("../../Components/whs/express_cropList.html", 'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})

//表格初始化
$(function() {
//	allHeight()
	//加载动画html 添加到初始的时候
	$(".jz").append("<div id='mengban1' class='zhezhao'></div>");
	$(".jz").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	var rowNum = $("#_input").val()
	$("#plat_jqgrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['订单编码','电商订单号','店铺编码','店铺名称','交易时间','付款时间','旗标','是否客审','审核提示',
					'商品数量','商品金额','实付金额','买家留言','卖家备注','买家会员号','收货人姓名','收货人手机号',
					'是否开票','发票抬头','卖家备注旗帜','是否关闭','是否发货','卖家调整金额',
					'系统优惠金额','订单状态','退货状态','是否含赠品','表头备注','发货时间',
					'关闭时间','审核时间','省','市','区','镇','收货人详细地址','是否自发货','快递公司编码','快递公司名称',
					'快递单号','重量','下载时间'
		],
		colModel: [{
				name: 'orderId',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'ecOrderId',
				editable: false,
				align: 'center',
//				hidden:true
			},
			{
				name: 'storeId',
				editable: true,
				align: 'center',
				sortable: false,
				hidden:true
			},
			{
				name: 'storeName',
				editable: false,
				align: 'center'
			},
			{
				name: 'tradeDt',
				editable: true,
				align: 'center',
			},
			{
				name: 'payTime',
				editable: true,
				align: 'center',
			},
			{
				name: 'waif',
				editable: false,
				align: 'center',
				hidden:true
			},
			{
				name: 'isAudit',
				editable: false,
				align: 'center'
			},
			{
				name: 'auditHint',
				editable: false,
				align: 'center'
			},
			{
				name: 'goodNum',
				editable: true,
				align: 'center'
			},
			{
				name: 'goodMoney', //卖家备注
				editable: false,
				align: 'center'
			},
			{
				name: 'payMoney',
				editable: false,
				align: 'center'
			},
			{
				name: 'buyerNote',
				editable: false,
				align: 'center'
			},
			{
				name: 'sellerNote',
				editable: false,
				align: 'center'
			},
			{
				name: 'buyerId',
				editable: false,
				align: 'center'
			},
			{
				name: 'recName', //是否开票
				editable: false,
				align: 'center'
			},
			{
				name: 'recMobile',
				editable: false,
				align: 'center'
			},
			{
				name: 'isInvoice',
				editable: false,
				align: 'center'
			},
			{
				name: 'invoiceTiel',
				editable: false,
				align: 'center'
			},
			{
				name: 'onteFlag',
				editable: false,
				align: 'center'
			},
			{
				name: 'isClose',
				editable: false,
				align: 'center'
			},
			{
				name: 'isShip',
				editable: false,
				align: 'center'
			},
			{
				name: 'adjustMoney',
				editable: false,
				align: 'center'
			},
			{
				name: 'discountMoney',
				editable: false,
				align: 'center'
			},
			{
				name: 'orderStatus',
				editable: false,
				align: 'center',
				hidden:true
			},
			{
				name: 'returnStatus',
				editable: false,
				align: 'center'
			},
			{
				name: 'hasGift',
				editable: false,
				align: 'center'
			},
			{
				name: 'memo',
				editable: false,
				align: 'center'
			},
			{
				name: 'shipTime',
				editable: false,
				align: 'center'
			},
			{
				name: 'closeTime',
				editable: false,
				align: 'center'
			},
			{
				name: 'auditTime',
				editable: false,
				align: 'center'
			},
			{
				name: 'province',
				editable: false,
				align: 'center'
			},
			{
				name: 'city',
				editable: false,
				align: 'center'
			},
			{
				name: 'county',
				editable: false,
				align: 'center'
			},
			{
				name: 'town',
				editable: false,
				align: 'center'
			},
			{
				name: 'recAddress',
				editable: false,
				align: 'center'
			},
			{
				name: 'deliverSelf',
				editable: false,
				align: 'center'
			},
			{
				name: 'expressCode',
				editable: false,
				align: 'center'
			},
			{
				name: 'expressName',
				editable: false,
				align: 'center'
			},
			{
				name: 'expressNo',
				editable: false,
				align: 'center'
			},
			{
				name: 'weight',
				editable: false,
				align: 'center'
			},
			{
				name: 'downloadTime',
				editable: false,
				align: 'center'
			}
		],
		autowidth: true,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		sortable:true,
		scrollrows: true,
		autoScroll: true,
		rownumWidth: 50,  //序列号列宽度
		height: 200,
		shrinkToFit: false,
		rowNum: 500, //一页显示多少条
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条	
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
//		multiboxonly: true,
		caption: "全部订单", //表格的标题名字
		pager: '#jqgridPager', //表格页脚的占位符(一般是div)的id
//		ondblClickRow: function(rowid) {
//			order(rowid);
//		},
		onSelectRow: function() {
			searchAll();
		},
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#plat_jqgrids").getGridParam('records'); //获取返回的记录数
			page = $("#plat_jqgrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#plat_jqgrids").getGridParam('rowNum'); //获取显示配置记录数量
			
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
		footerrow: true,
		gridComplete: function() { 
			var goodNum = $("#plat_jqgrids").getCol('goodNum', false, 'sum');
			var goodMoney = $("#plat_jqgrids").getCol('goodMoney', false, 'sum');
			var payMoney = $("#plat_jqgrids").getCol('payMoney', false, 'sum');
			var adjustMoney = $("#plat_jqgrids").getCol('adjustMoney', false, 'sum');
			var discountMoney = $("#plat_jqgrids").getCol('discountMoney', false, 'sum');
			
			$("#plat_jqgrids").footerData('set', { 
				"orderId": "本页合计",
				goodNum: goodNum.toFixed(prec),
				goodMoney : precision(goodMoney,2),
				payMoney : precision(payMoney,2),
				adjustMoney : precision(adjustMoney,2),
				discountMoney : precision(discountMoney,2)

			}          );    
		},
	})
	
	
	//初始化表格
	jQuery("#platList_jqgrids1").jqGrid({
		autoScroll:true,
		shrinkToFit:false,
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		url: '../../assets/js/json/order.json',
		colNames: ['存货编码','存货名称','数量','店铺商品编码','购买数量','商品金额','实付金额','商品名称','平台sku',
					'订单号','批次','快递公司编码','活动编码','系统优惠金额','卖家调整金额',
					'备注','平台标价','实付单价','发货仓编码','结算单价','平台订单号',
					'母件编码','母件名称','可退数量','可退金额','是否赠品','生产日期','失效日期'
		], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'invId',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'invtyName',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'invNum',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'goodId',
				align: "center",
				index: 'invdate',
				editable: false,
				sortable: false
			},
			{
				name: 'goodNum',
				align: "center",
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'goodMoney',
				align: "center",
				index: 'invdate',
				editable: false,
				sortable: false
			},
			{
				name: 'payMoney',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'goodName',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'goodSku',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'orderId',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false,
				hidden:true
			},
			{
				name: 'batchNo',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'expressCom',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'proActId',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'discountMoney',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'adjustMoney',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'memo',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'goodPrice',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'payPrice',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'deliverWhs',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'sellerPrice',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'ecOrderId',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'ptoCode',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'ptoName',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'canRefNum',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'canRefMoney',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'isGift',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'prdcDt',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'invldtnDt',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			}
		],
		autowidth: true,
		rownumbers: true,
		loadonce: true,
		sortable:true,
		forceFit: true,
		rowNum: 10,
		scrollrows: true,
		autoScroll: true,
		height: '100%',
		shrinkToFit: false,
		rowList: [10, 20, 30], //可供用户选择一页显示多少条
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		multiboxonly: true,
		rownumWidth: 25,  //序列号列宽度
		caption: "表体信息", //表格的标题名字
		pager: '#platList_jqGridPager1', //表格页脚的占位符(一般是div)的id
	})
	
	
	//初始化表格
	jQuery("#platList_jqgrids3").jqGrid({
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		url: '../../assets/js/json/order.json',
		colNames: ['主键Id','平台id','店铺id','平台订单号','优惠商品sku','优惠类型编码',
					'优惠类型','优惠金额'
		], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'id',
				align: "center",
				index: 'invdate',
				editable: false,
				hidden:true,
				sortable: false
			},
			{
				name: 'platId',
				align: "center",
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'storeId',
				align: "center",
				index: 'invdate',
				editable: false,
				sortable: false
			},
			{
				name: 'orderId',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'skuId',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'couponCode',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				hidden:true,
				sortable: false
			},
			{
				name: 'couponType',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'couponPrice',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			}
		],
		autowidth: true,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		sortable:true,
		rowNum: 10,
		scrollrows: true,
		autoScroll: true,
		height: '100%',
		shrinkToFit: false,
		rowList: [10, 20, 30], //可供用户选择一页显示多少条
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		multiboxonly: true,
		rownumWidth: 20,  //序列号列宽度
		caption: "促销信息", //表格的标题名字
		pager: '#platList_jqGridPager3', //表格页脚的占位符(一般是div)的id
	})
	
	//初始化表格
	jQuery("#platList_jqgrids4").jqGrid({
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		url: '../../assets/js/json/order.json',
		colNames: ['主键Id','操作人编码','操作人','操作时间','操作名称','平台订单号',
					'操作内容','备注'
		], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'logId',
				align: "center",
				index: 'invdate',
				editable: false,
				hidden:true,
				sortable: false
			},
			{
				name: 'operatId',
				align: "center",
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'operatName',
				align: "center",
				index: 'invdate',
				editable: false,
				sortable: false
			},
			{
				name: 'operatTime',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'typeName',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'operatOrder',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				hidden:true,
				sortable: false
			},
			{
				name: 'operatContent',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			},
			{
				name: 'memo',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false
			}
		],
		autowidth: true,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 10,
		scrollrows: true,
		autoScroll: true,
		sortable:true,
		height: '100%',
		shrinkToFit: false,
		rowList: [10, 20, 30], //可供用户选择一页显示多少条
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		multiboxonly: true,
		rownumWidth: 20,  //序列号列宽度
		caption: "操作日志", //表格的标题名字
		pager: '#platList_jqGridPager4', //表格页脚的占位符(一般是div)的id
	})

})

$(document).on('click', "li[data-id='tabContent3']", function() {
	var gr = $("#plat_jqgrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowDatas = $("#plat_jqgrids").jqGrid('getRowData', gr);
	window.ecOrderId = rowDatas.ecOrderId;
	if(ecOrderId == undefined) {
		alert("请先选择单据")
	} else {
		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				"ecOrderId": ecOrderId
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			type: "post",
			contentType: 'application/json; charset=utf-8',
			url: url + '/mis/ec/couponDetail/couponDetailList',
			async: true,
			data: saveData,
			dataType: 'json',
			success: function(data) {
				var list = data.respBody.list;
				myDate = list;
				$("#platList_jqgrids3").jqGrid('clearGridData');
				$("#platList_jqgrids3").jqGrid('setGridParam', {
					datatype: 'local',
					data: myDate, //newData是符合格式要求的重新加载的数据
					page: 1 //哪一页的值
				}).trigger("reloadGrid")
			},
			error: function() {
				console.log(error)
			}
		});
	}
})

$(document).on('click', "li[data-id='tabContent4']", function() {
	var gr = $("#plat_jqgrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowDatas = $("#plat_jqgrids").jqGrid('getRowData', gr);
	window.ecOrderId = rowDatas.ecOrderId;
	if(ecOrderId == undefined) {
		alert("请先选择单据")
	} else {
		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				"ecOrderId": ecOrderId
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			type: "post",
			contentType: 'application/json; charset=utf-8',
			url: url + '/mis/ec/logRecord/logRecordList',
			async: true,
			data: saveData,
			dataType: 'json',
			success: function(data) {
				var list = data.respBody.list;
				myDate = list;
				$("#platList_jqgrids4").jqGrid('clearGridData');
				$("#platList_jqgrids4").jqGrid('setGridParam', {
					datatype: 'local',
					data: myDate, //newData是符合格式要求的重新加载的数据
					page: 1 //哪一页的值
				}).trigger("reloadGrid")
			},
			error: function() {
				console.log(error)
			}
		});
	}
})


function searchAll() {
	var gr = $("#plat_jqgrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowDatas = $("#plat_jqgrids").jqGrid('getRowData', gr);
	window.orderId = rowDatas.orderId;
	if(orderId == undefined) {
		$("#platList_jqgrids1").jqGrid("clearGridData");
		$("#platList_jqgrids3").jqGrid("clearGridData");
		$("#platList_jqgrids4").jqGrid("clearGridData");
		$("input[name='orderId2']").val('');
		$("input[name='storeId2']").val('');
		$("input[name='storeName2']").val('');
		$("input[name='payTime2']").val('');
		$("input[name='waif2']").val('');
		$("select[name='isAudit2']").val('');
		$("input[name='auditHint2']").val('');
		$("input[name='goodNum2']").val('');
		$("input[name='goodMoney2']").val('');
		$("input[name='payMoney2']").val('');
		$("input[name='buyerNote2']").val('');
		$("input[name='sellerNote2']").val('');
		$("input[name='recAddress2']").val('');
		$("input[name='buyerId2']").val('');
		$("input[name='recName2']").val('');
		$("input[name='recMobile2']").val('');
		$("input[name='ecOrderId2']").val('');
		$("input[name='recName2']").val('');
		$("input[name='ecOrderId2']").val('');
		$("select[name='isInvoice2']").val('');
		$("input[name='invoiceTitle2']").val('');
		$("input[name='noteFlag2']").val('');
		$("select[name='isShip2']").val('');
		$("select[name='isClose2']").val('');
		$("input[name='adjustMoney2']").val('');
		$("input[name='discountMoney2']").val('');
		$("input[name='adjustStatus2']").val('');
		$("input[name='tradeDt2']").val('');
		$("input[name='bizTypId2']").val('');
		$("input[name='sellTypId2']").val('');
		$("input[name='recvSendCateId2']").val('');
		$("input[name='orderStatus2']").val('');
		$("input[name='returnStatus2']").val('');
		$("select[name='hasGift2']").val('');
		$("input[name='memo2']").val('');
		$("input[name='orderSellerPrice2']").val('');
		$("input[name='province2']").val('');
		$("input[name='provinceId2']").val('');
		$("input[name='city2']").val('');
		$("input[name='cityId2']").val('');
		$("input[name='county2']").val('');
		$("input[name='countyId2']").val('');
		$("input[name='town2']").val('');
		$("input[name='townId2']").val('');
		$("input[name='freightPrice2']").val('');
		$("input[name='deliverWhs2']").val('');
		$("input[name='deliveryType2']").val('');
		$("select[name='deliverSelf2']").val('');
		$("input[name='shipTime2']").val('');
		$("input[name='closeTime2']").val('');
		$("input[name='auditTime2']").val('');
		
		$("input[name='expressCom2']").val('');
		$("input[name='expressCode2']").val('');
		$("input[name='expressTemplate2']").val('');
		
		$("input[name='weight2']").val('');
		$("input[name='downloadTime2']").val('');
	} else {
		var myData = {};
		var Data = {
			"reqHead": reqhead,
			"reqBody": {
				"orderId": orderId
			}
		};
		var saveData = JSON.stringify(Data);
		$.ajax({
			type: "post",
			url: url + "/mis/ec/platOrder/query",
			async: true,
			data: saveData,
			dataType: 'json',
			contentType: 'application/json;charset=utf-8',
			success: function(data) {
				var fData = data.respBody
				$("input[name='orderId2']").val(fData.orderId);
				$("input[name='storeId2']").val(fData.storeId);
				$("input[name='storeName2']").val(fData.storeName);
				$("input[name='payTime2']").val(fData.payTime);
				$("input[name='waif2']").val(fData.waif);
				$("select[name='isAudit2']").val(fData.isAudit);
				$("input[name='auditHint2']").val(fData.auditHint);
				$("input[name='goodNum2']").val(fData.goodNum);
				$("input[name='goodMoney2']").val(fData.goodMoney);
				$("input[name='payMoney2']").val(fData.payMoney);
				$("input[name='buyerNote2']").val(fData.buyerNote);
				$("input[name='sellerNote2']").val(fData.sellerNote);
				$("input[name='recAddress2']").val(fData.recAddress);
				$("input[name='buyerId2']").val(fData.buyerId);
				$("input[name='recName2']").val(fData.recName);
				$("input[name='recMobile2']").val(fData.recMobile);
				$("input[name='ecOrderId2']").val(fData.ecOrderId);
				$("input[name='recName2']").val(fData.recName);
				$("input[name='ecOrderId2']").val(fData.ecOrderId);
				$("select[name='isInvoice2']").val(fData.isInvoice);
				$("input[name='invoiceTitle2']").val(fData.invoiceTitle);
				$("input[name='noteFlag2']").val(fData.noteFlag);
				$("select[name='isShip2']").val(fData.isShip);
				$("select[name='isClose2']").val(fData.isClose);
				$("input[name='adjustMoney2']").val(fData.adjustMoney);
				$("input[name='discountMoney2']").val(fData.discountMoney);
				$("input[name='adjustStatus2']").val(fData.adjustStatus);
				$("input[name='tradeDt2']").val(fData.tradeDt);
				$("input[name='bizTypId2']").val(fData.bizTypId);
				$("input[name='sellTypId2']").val(fData.sellTypId);
				$("input[name='recvSendCateId2']").val(fData.recvSendCateId);
				$("input[name='orderStatus2']").val(fData.orderStatus);
				$("input[name='returnStatus2']").val(fData.returnStatus);
				$("select[name='hasGift2']").val(fData.hasGift);
				$("input[name='memo2']").val(fData.memo);
				$("input[name='orderSellerPrice2']").val(fData.orderSellerPrice);
				$("input[name='province2']").val(fData.province);
				$("input[name='provinceId2']").val(fData.provinceId);
				$("input[name='city2']").val(fData.city);
				$("input[name='cityId2']").val(fData.cityId);
				$("input[name='county2']").val(fData.county);
				$("input[name='countyId2']").val(fData.countyId);
				$("input[name='town2']").val(fData.town);
				$("input[name='townId2']").val(fData.townId);
				$("input[name='freightPrice2']").val(fData.freightPrice);
				$("input[name='deliverWhs2']").val(fData.deliverWhs);
				$("input[name='deliveryType2']").val(fData.deliveryType);
				$("select[name='deliverSelf2']").val(fData.deliverSelf);
				$("input[name='shipTime2']").val(fData.shipTime);
				$("input[name='closeTime2']").val(fData.closeTime);
				$("input[name='auditTime2']").val(fData.auditTime);
				
				$("input[name='expressCom2']").val(fData.expressNo);
				$("input[name='expressCode2']").val(fData.expressCode);
				$("input[name='expressTemplate2']").val(fData.expressTemplate);
				
				$("input[name='weight2']").val(fData.weight);
				$("input[name='downloadTime2']").val(fData.downloadTime);
				var list = data.respBody.list;
				myDate = list;
				for(var j =0 ; j<list.length;j++) {
					if(list[j].isGift == 0) {
						list[j].isGift = '否'
					}
					if(list[j].isGift == 1) {
						list[j].isGift = '是'
					}
				}
				$("#platList_jqgrids1").jqGrid('clearGridData');
				$("#platList_jqgrids1").jqGrid('setGridParam', {
					//				rowNum:rowNum,
					datatype: 'local',
					data: myDate, //newData是符合格式要求的重新加载的数据
					page: 1 //哪一页的值
				}).trigger("reloadGrid")
				
			},
			error: function(error) {
				alert(error)
			}
		});
	}
}

$(document).on('click', '.search', function() {
	$(".noTo").removeClass("gray")
	$(".noTo").attr('disabled', false);
	search()
})

//查询按钮
function search() {
	var myDate = {};

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
	var memo = $("input[name='memo']").val();
	var expressCom = $("input[name='expressCom']").val();
	var isShip = $("select[name='isShip']").val();
	var isClose = $("select[name='isClose']").val();
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	

	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"orderId": orderId,
			"storeId": storeId,
			"invtyEncd": invtyEncd,
			"isAudit": isAudit,
			"buyerId": buyerId,
			"memo": memo,
			"recName": recName,
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
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/ec/platOrder/queryList',
		async: true,
		data: saveData,
		dataType: 'json',
		//开始加载动画  添加到ajax里面
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");

		},
		success: function(data) {
			var rowNum = $("#_input").val()
			var list = data.respBody.list;
			for(var i = 0; i < list.length; i++) {
				if(list[i].returnStatus == 0) {
					list[i].returnStatus = "未发生退货"
				} else if(list[i].returnStatus == 1) {
					list[i].returnStatus = "发生退货"
				}

				if(list[i].deliverSelf == 0) {
					list[i].deliverSelf = "否"
				} else if(list[i].deliverSelf == 1) {
					list[i].deliverSelf = "是"
				}

				if(list[i].isAudit == 0) {
					list[i].isAudit = "否"
				} else if(list[i].isAudit == 1) {
					list[i].isAudit = "是"
				}

				if(list[i].isInvoice == 0) {
					list[i].isInvoice = "否"
				} else if(list[i].isInvoice == 1) {
					list[i].isInvoice = "是"
				}

				if(list[i].isClose == 0) {
					list[i].isClose = "否"
				} else if(list[i].isClose == 1) {
					list[i].isClose = "是"
				}

				if(list[i].isShip == 0) {
					list[i].isShip = "否"
				} else if(list[i].isShip == 1) {
					list[i].isShip = "是"
				}

				if(list[i].hasGift == 0) {
					list[i].hasGift = "否"
				} else if(list[i].hasGift == 1) {
					list[i].hasGift = "是"
				}
			}
			var mydata = {};
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#plat_jqgrids").jqGrid("clearGridData");
			$("#plat_jqgrids").jqGrid("setGridParam", {
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

$(function() {
	//确定
	$(".sure").click(function() {
		//到货单
		//获得行号
		var rowid = $("#plat_jqgrids").jqGrid('getGridParam', 'selrow');

		//	店铺档案
		//	获得行号
		var gr = $("#List_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDatas = $("#List_jqgrids").jqGrid('getRowData', gr);

		$("input[name='storeId1']").val(rowDatas.storeId)
		$("input[name='storeNm1']").val(rowDatas.storeName)

		$(".formSave_box").css("opacity", 0);
		$(".box").show();
		$(".oneSerch_List").show()
	})
//	取消
	$(".false").click(function() {
		$(".formSave_box").css("opacity", 0);
		$(".box").show();
		$(".oneSerch_List").show()
		//到货单
		//获得行号
//		var rowid = $("#plat_jqgrids").jqGrid('getGridParam', 'selrow');
//		
//		$("#" + rowid + "_whsNm").val("");
//		$("#" + rowid + "_whsEncd").val("");
//		$("#" + rowid + "_invtyEncd").val("")
	})
})

var isclick = true;
$(function() {
	//弃审
	$(".noTo").click(function() {
		if(isclick) {
			isclick = false;
			ntChk();
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	})
})
function ntChk() {
	var sum = []
	//获取此行的仓库编码和存货编码
	var ids = $("#plat_jqgrids").jqGrid('getGridParam', 'selarrrow');
	for(var i = 0;i < ids.length;i++) {
		//获得行数据
		var rowDatas = $("#plat_jqgrids").jqGrid('getRowData', ids[i]);
		sum.push(rowDatas.orderId)
	}
	var orderId = sum.toString()
	if(ids.length == 0) {
		alert("请选择单据!")
	} else {
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				'orderId':orderId
			}
		};
		var Data = JSON.stringify(data);
		$.ajax({
			url: url + '/mis/ec/associatedSearch/orderAbandonAuditByOrderId',
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
			success: function(data) {
				alert(data.respHead.message)
				search()
				
			},
			//结束加载动画
			complete: function() {
				$(".zhezhao").css("display", "none");
				$("#loader").css("display", "none");
			},
			error: function() {
				console.log(error)
			}
		})
	}
}
$(function() {
	//订单打开
	$(".open_d").click(function() {
		var sum = []
		//获取此行的仓库编码和存货编码
		var ids = $("#plat_jqgrids").jqGrid('getGridParam', 'selarrrow');
		for(var i = 0;i < ids.length;i++) {
			//获得行数据
			var rowDatas = $("#plat_jqgrids").jqGrid('getRowData', ids[i]);
			sum.push(rowDatas.orderId)
		}
		var orderId = sum.toString()
		if(ids.length == 0) {
			alert("请选择单据!")
		} else {
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					'orderIds':orderId
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/ec/platOrder/openOrder',
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
				success: function(data) {
					alert(data.respHead.message)
					search()
					
				},
				//结束加载动画
				complete: function() {
					$(".zhezhao").css("display", "none");
					$("#loader").css("display", "none");
				},
				error: function() {
					console.log(error)
				}
			})
		}
		
		
	})
	
	$(".delOrder").click(function() {
		//获得选中行的行号
		var ids = $("#plat_jqgrids").jqGrid('getGridParam', 'selarrrow');
		//获取选择行的provrId
		var rowDatas = [];
		for(var i = 0; i < ids.length; i++) {
//			var ids = $("#plat_jqgrids").jqGrid('getGridParam', 'selrow');
				//获得行数据
			var rowData = $("#plat_jqgrids").jqGrid('getRowData', ids[i]);
			var orderId = rowData.ecOrderId
			//建一个数组，把选中行的id添加到这个数组中去。
			rowDatas.push(orderId)
		}
		if(rowDatas.length == 0) {
			alert("请选择单据!")
		} else if(confirm("确定删除？")) {
			var orderId = rowDatas.toString();
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"orderId": orderId,
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/ec/platOrder/delete',
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
					$(".noTo").removeClass("gray")
					$(".noTo").attr('disabled', false);
					alert(data.respHead.message)
					search()
				},
				error: function() {
					console.log(error)
				}
			})
		}
	})
})

function oneSerch() {
	//获得选中行的行号
	var ids = $("#plat_jqgrids").jqGrid('getGridParam', 'selrow');
	var rowData = $("#plat_jqgrids").jqGrid('getRowData', ids);
	var orderId = rowData.orderId
	if(!ids) {
		alert("请选择单据!")
		return;
	} else {
		$(".box").hide()
		$(".oneSerch_List").css("opacity", 1)
		$(".oneSerch_List").show()
		var savedata = {
			'reqHead': reqhead,
			'reqBody': {
				'orderId': orderId,
			},
		};
		var saveData = JSON.stringify(savedata)
		$.ajax({
			type: "post",
			contentType: 'application/json; charset=utf-8',
			url: url + '/mis/ec/platOrder/unionQuery',
			async: true,
			data: saveData,
			dataType: 'json',
			//开始加载动画  添加到ajax里面
			beforeSend: function() {
				$(".zhezhao").css("display", "block");
				$("#loader").css("display", "block");
	
			},
			success: function(data) {
				var rowNum = $("#_input").val()
				var list = data.respBody.list;
				for(var i = 0; i < list.length;i++) {
					if(list[i].audit == 0) {
						list[i].audit = "否"
					} else if(list[i].audit == 1) {
						list[i].audit = "是"
					}
				}
				myDate = list;
				$("#oneSerch_jqgrids").jqGrid('clearGridData');
				$("#oneSerch_jqgrids").jqGrid('setGridParam', {
					rowNum:rowNum,
					datatype: 'local',
					data: myDate, //newData是符合格式要求的重新加载的数据
					page: 1 //哪一页的值
				}).trigger("reloadGrid")
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
				"ecOrderIds": num,
				"isAudit": ''
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			type: "post",
			contentType: 'application/json; charset=utf-8',
			url: url + '/mis/ec/platOrder/batchSelect',
			async: true,
			data: saveData,
			dataType: 'json',
			//开始加载动画  添加到ajax里面
			beforeSend: function() {
				$(".zhezhao").css("display", "block");
				$("#loader").css("display", "block");
	
			},
			success: function(data) {
				var rowNum = $("#_input").val()
				var list = data.respBody.list;
				for(var i = 0; i < list.length; i++) {
					if(list[i].returnStatus == 0) {
						list[i].returnStatus = "未发生退货"
					} else if(list[i].returnStatus == 1) {
						list[i].returnStatus = "发生退货"
					}
	
					if(list[i].deliverSelf == 0) {
						list[i].deliverSelf = "否"
					} else if(list[i].deliverSelf == 1) {
						list[i].deliverSelf = "是"
					}
	
					if(list[i].isAudit == 0) {
						list[i].isAudit = "否"
					} else if(list[i].isAudit == 1) {
						list[i].isAudit = "是"
					}
	
					if(list[i].isInvoice == 0) {
						list[i].isInvoice = "否"
					} else if(list[i].isInvoice == 1) {
						list[i].isInvoice = "是"
					}
	
					if(list[i].isClose == 0) {
						list[i].isClose = "否"
					} else if(list[i].isClose == 1) {
						list[i].isClose = "是"
					}
	
					if(list[i].isShip == 0) {
						list[i].isShip = "否"
					} else if(list[i].isShip == 1) {
						list[i].isShip = "是"
					}
	
					if(list[i].hasGift == 0) {
						list[i].hasGift = "否"
					} else if(list[i].hasGift == 1) {
						list[i].hasGift = "是"
					}
				}
				var mydata = {};
				mydata.rows = data.respBody.list;
				mydata.page = page;
				mydata.records = data.respBody.count;
				mydata.total = data.respBody.pages;
				$("#plat_jqgrids").jqGrid("clearGridData");
				$("#plat_jqgrids").jqGrid("setGridParam", {
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



$(function() {
	pageInit()
})
//页面初始化
function pageInit() {

	//初始化表格
	jQuery("#oneSerch_jqgrids").jqGrid({
		autoScroll:true,
		shrinkToFit:false,
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		url: '../../assets/js/json/order.json',
		colNames: ['单据类型', '单据名称', '对应单据号', '是否审核'], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'type',
				align: "center",
				index: 'invdate',
				editable: false,
//				hidden:true
			},
			{
				name: 'orderName',
				align: "center",
				index: 'id',
				editable: false,
			},
			{
				name: 'orderId',
				align: "center",
				index: 'invdate',
				editable: false,
			},
			{
				name: 'audit',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
			}
		],
		autowidth: true,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 10,
		scrollrows: true,
		autoScroll: true,
		height: '100%',
		shrinkToFit: false,
		rowList: [10, 20, 30], //可供用户选择一页显示多少条
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		multiboxonly: true,
		rownumWidth: 20,  //序列号列宽度
		caption: "联查表", //表格的标题名字
		pager: '#oneSerch_jqgridPager', //表格页脚的占位符(一般是div)的id
		ondblClickRow: function() {
			order1();
		},
	})
}


function order1() {
	//获得行号
	var gr = $("#oneSerch_jqgrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	//debugger
	var rowData = $("#oneSerch_jqgrids").jqGrid('getRowData', gr);
	var type = rowData.type
	if(type == 1) {
//		alert("销售单")
		//存换从传入的是orderId
		localStorage.setItem("orderId", rowData.orderId);
		window.open("../../Components/sell/sellSngl.html?1");
	} else if(type == 2) {
//		alert("销售出库单")
		localStorage.setItem("outWhsId", rowData.orderId);
		window.open("../../Components/sell/sellOutWhs.html?1");
	} else if(type == 3) {
//		alert("电商退款单")
		localStorage.setItem("refId", rowData.orderId);
		window.open("../../Components/ec/refundOrder.html?1");
	}
//	else if(type == 4) {
////		alert("电商退款单")
//		localStorage.setItem("refId", rowData.orderId);
//		window.open("../../Components/ec/Print_wuLiuSearch.html?1");
//	}

}


//导出
$(document).on('click', '.exportExcel', function() {
	var orderId = $("input[name='orderId1']").val();
	var storeId = $("input[name='storeId1']").val();
	var isAudit = $("select[name='isAudit1']").val();
	var buyerId = $("input[name='buyerId']").val();
	var recName = $("input[name='recName']").val();
	var ecOrderId = $("input[name='ecOrderId']").val();
	var startDate = $("input[name='startDate']").val();
	var endDate = $("input[name='endDate']").val();
	var deliverWhs = $("input[name='deliverWhs']").val();
	var expressCom = $("input[name='expressCom']").val();
	var isShip = $("select[name='isShip']").val();
	var isClose = $("select[name='isClose']").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"orderId": orderId,
			"storeId": storeId,
			"isAudit": isAudit,
			"buyerId": buyerId,
			"recName": recName,
			"ecOrderId": ecOrderId,
			"startDate": startDate,
			"endDate": endDate,
			"deliverWhs":deliverWhs,
			"expressCom":expressCom,
			"invtyEncd": invtyEncd,
			"isShip": isShip,
			"isClose": isClose,
			"pageNo": 1,
			"pageSize": 500
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		url: url + '/mis/ec/platOrder/exportPlatOrder',
		type: 'post',
		data: saveData,
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
			console.log(list)
			var execlName = '全部订单'
			ExportData(list, execlName)
		},
		error: function() {
			console.log(error)
		}
	})
	
})