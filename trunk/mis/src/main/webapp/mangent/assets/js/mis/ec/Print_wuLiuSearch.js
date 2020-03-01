var count;
var pages;
var page = 1;
var rowNum;
$(function() {
	//点击表格图标显示店铺列表
	$(document).on('click', '.invtyEncd_biaoge', function() {
		window.open("../../Components/baseDoc/invtyList.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	$(document).on('click', '.deliverWhs_biaoge', function() {
		window.open("../../Components/baseDoc/whsList.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	$(document).on('click', '.expressEncd_biaoge', function() {
		window.open("../../Components/whs/express_cropList.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	$(document).on('click', '.storeId_biaoge', function() {
		window.open("../../Components/baseDoc/storeRecordList.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	$(document).on('click', '.invtyEncds_biaoge', function() {
		window.open("../../Components/baseDoc/invtyList_wl.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	//1.存货
	$(document).on('keypress', '#invtyEncds', function(event) {
		if(event.keyCode == '13') {
			$('#invtyEncds').blur();
		}
	})

	$(document).on('blur', '#invtyEncds', function() {
		var invtyEncds = $("#invtyEncds").val();
		dev({
			doc1: $("#invtyEncds"),
			doc2: $("#invtyNms"),
			showData: {
				"invtyEncd": invtyEncds
			},
			afunction: function(data) {
				var invtyNms = data.respBody.invtyNm;
				$("#invtyNms").val(invtyNms);
				if(data.respHead.isSuccess == false && invtyEncds != "") {
					alert("此存货不存在")
					$("#invtyNms").val("");
					$("#invtyEncds").val("");
				}
			},
			url: url + "/mis/purc/InvtyDoc/selectInvtyDocByInvtyDocEncd"
		})
	});
})

//刚开始时可点击的按钮
$(function() {
	$(".saveOrder").addClass("gray") //参照
	$(".gray").attr("disabled", true)
	
	$(".download").removeClass("gray")
	$("#mengban").hide()
	$(".down").hide()
})
var myData = {};
//页面初始化
$(function() {
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	//初始化表格
	jQuery("#print_wuLiu_jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		height: 300,
		autoScroll: true,
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		shrinkToFit: false,
		colNames: ['物流号', '平台编码','店铺编码','店铺名称','模板编码','快递公司编码', '快递公司名称', '快递单号', '销售单号', '订单编码', '销售出库单号', '电商订单号',
		'创建时间', '整单商品数', '整单金额', '整单实付金额', '买家留言', '卖家备注', '收货人详细地址',
		'收货人姓名', '收货人手机号', '备注', '业务类型编码', '销售类型编码', '发货仓库编码', '仓库名称','收发类别编码',
		'物流公司编码','调整状态', '是否开始拣货', '是否打印', '是否发货', '是否回传平台', '打印时间', '发货时间', '揽件时间',
		'体积', '重量', '运费', '是否自发货', '大头笔编码', '大头笔名称', '集包地编码',
		'集包地名称', '目的地网店编码', '目的地网点名称',
		'二段码', '三段码'
		], //jqGrid的列显示名字
		colModel: [
			{
				name: 'ordrNum',
				align: "center",
				editable: true,
				sortable: false,
//				hidden: true
			}, //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'platId',
				align: "center",
				editable: true,
				sortable: false,
				id:"1"
			},
			{
				name: 'storeId',
				align: "center",
				editable: true,
				sortable: false,
				id:"1"
			},
			{
				name: 'storeName',
				align: "center",
				editable: true,
				sortable: false,
				id:"1"
			},
			{
				name: 'templateId',
				align: "center",
				editable: true,
				sortable: false,
				id:"1"
			},
			{
				name: 'expressEncd',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			},
			{
				name: 'expressName',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'expressCode',
				align: "center",
				editable: true,
				sortable: false,
				id:"2"
			},
			{
				name: 'saleEncd',
				align: "center",
				editable: true,
				sortable: false,
				id:"3"
			},
			{
				name: 'orderId',
				align: "center",
				editable: false,
				sortable: false,
				id:"4"
			},
			{
				name: 'outWhsId',
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: 'ecOrderId',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'createDate',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'goodNum',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'goodMoney',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			},
			{
				name: 'payMoney',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			},
			{
				name: 'buyerNote',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'sellerNote',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'recAddress',
				align: "center",
				editable: true,
				sortable: false,
//						edittype: 'select',
//						formatter: 'select',
//						editoptions: {
//							value: "0:请选择; 1:在售; 2:下架; 3:待售"
//						},
			},
			{
				name: 'recName',
				align: "center",
				editable: true,
				sortable: false,
//						edittype: 'select',
//						formatter: 'select',
//						editoptions: {
//							value: "2:请选择; 0:否; 1:是"
//						},
			},
			{
				name: 'recMobile',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'memo',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'bizTypId',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			},
			{
				name: 'sellTypId',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			},
			{
				name: 'deliverWhs',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			},
			{
				name: 'whsName',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'recvSendCateId',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			},
			{
				name: 'gdFlowEncd',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			},
			{
				name: 'adjustStatus',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			},
			{
				name: 'isPick',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'isPrint',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'isShip',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'isBackPlatform',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'printTime',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'shipDate',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'packageDate',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'volume',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'weight',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'freight',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'deliverSelf',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'bigShotCode',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			},
			{
				name: 'bigShotName',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			},
			{
				name: 'gatherCenterCode',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			},
			{
				name: 'gatherCenterName',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			},
			{
				name: 'branchCode',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			},
			{
				name: 'branchName',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			},
			{
				name: 'secondSectionCode',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			},
			{
				name: 'a',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			}
		],
		rowNum: 100, //一页显示多少条
		rowList: [100, 200, 500, 1000], //可供用户选择一页显示多少条			
		autowidth: true,
		loadonce: true,
		sortable:true,
		multiboxonly: true,
		cellsubmit: "clientArray",
		rownumbers: true,
		pager: '#print_wuLiu_jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'storeId', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		onSelectRow: function() {
			searchAll();
		},
		multiselect: true, //复选框
		caption: "物流单列表", //表格的标题名字	
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#print_wuLiu_jqgrids").getGridParam('records'); //获取返回的记录数
			page = $("#print_wuLiu_jqgrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#print_wuLiu_jqgrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			search3()
		},
		footerrow: true,
		gridComplete: function() { 
			var goodNum = 0;
			var freight = 0;
			var ids = $("#print_wuLiu_jqgrids").getDataIDs();
			for(var i = 0; i < ids.length; i++) {
				goodNum += parseFloat($("#print_wuLiu_jqgrids").getCell(ids[i],"goodNum"));
				freight += parseFloat($("#print_wuLiu_jqgrids").getCell(ids[i],"freight"));
			};
			if(isNaN(goodNum)) {
				goodNum = 0
			}
			if(isNaN(freight)) {
				freight = 0
			}
			goodNum = goodNum.toFixed(prec);
			freight = precision(freight,2);
			$("#print_wuLiu_jqgrids").footerData('set', { 
				"ordrNum": "本页合计",
				goodNum: goodNum,
				freight:freight
				
			}          );    
		},
	});
	
	//初始化表格
	jQuery("#print_jqgrids1").jqGrid({
		autoScroll:true,
		shrinkToFit:false,
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		url: '../../assets/js/json/order.json',
		colNames: ['存货编码','存货名称','数量','商品名称',
					'批次',
					'备注',
					'母件编码','母件名称','是否赠品','生产日期','失效日期'
		], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'invId',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false,
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
				sortable: false,
			},
			{
				name: 'goodName',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false,
			},
			{
				name: 'batchNo',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false,
			},
			{
				name: 'memo',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false,
			},
			{
				name: 'ptoCode',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false,
			},
			{
				name: 'ptoName',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false,
			},
			{
				name: 'isGift',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false,
			},
			{
				name: 'prdcDt',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false,
			},
			{
				name: 'invldtnDt',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false,
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
		pager: '#print_jqGridPager1', //表格页脚的占位符(一般是div)的id
	})
	
	//初始化表格
	jQuery("#print_jqgrids2").jqGrid({
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
				sortable: false,
			},
			{
				name: 'operatId',
				align: "center",
				index: 'id',
				editable: false,
				sortable: false,
			},
			{
				name: 'operatName',
				align: "center",
				index: 'invdate',
				editable: false,
				sortable: false,
			},
			{
				name: 'operatTime',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false,
			},
			{
				name: 'typeName',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false,
			},
			{
				name: 'operatOrder',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				hidden:true,
				sortable: false,
			},
			{
				name: 'operatContent',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false,
			},
			{
				name: 'memo',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false,
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
		pager: '#print_jqGridPager2', //表格页脚的占位符(一般是div)的id
	})
	//	修改快递单号
	//初始化表格
	jQuery("#printAudie_jqgrids").jqGrid({
		autoScroll:true,
		shrinkToFit:false,
		cellEdit: true,
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		url: '../../assets/js/json/order.json',
		colNames: ['物流单号','平台订单号','快递单号'], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'ordrNum',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false,
			},
			{
				name: 'ecOrderId',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
				sortable: false,
			},
			{
				name: 'expressCode',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: true,
				sortable: false,
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
		height: 300,
		shrinkToFit: false,
		rowList: [10, 20, 30], //可供用户选择一页显示多少条
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
//		multiboxonly: true,
		cellsubmit: "clientArray",
		rownumWidth: 15,  //序列号列宽度
		caption: "表体信息", //表格的标题名字
		pager: '#printAudie_jqGridPager', //表格页脚的占位符(一般是div)的id
	})
})

function searchAll() {
	var gr = $("#print_wuLiu_jqgrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowDatas = $("#print_wuLiu_jqgrids").jqGrid('getRowData', gr);
	window.orderId = rowDatas.orderId;
	if(orderId == undefined) {
		$("#print_jqgrids2").jqGrid("clearGridData");
		$("#print_jqgrids1").jqGrid("clearGridData");
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
				$("#print_jqgrids1").jqGrid('clearGridData');
				$("#print_jqgrids1").jqGrid('setGridParam', {
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


$(document).on('click', "li[id='tab2']", function() {
	var gr = $("#print_wuLiu_jqgrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowDatas = $("#print_wuLiu_jqgrids").jqGrid('getRowData', gr);
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
				$("#print_jqgrids2").jqGrid('clearGridData');
				$("#print_jqgrids2").jqGrid('setGridParam', {
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

// 拣货
$(document).on('click', ".pick", function() {
	var gr = $("#print_wuLiu_jqgrids").jqGrid('getGridParam', 'selarrrow');
	var num = []
	for(var i = 0;i < gr.length; i++) {
		//获得行数据
		var rowDatas = $("#print_wuLiu_jqgrids").jqGrid('getRowData', gr[i]);
		num.push(rowDatas.ordrNum)
	}
	if(num.length == 0) {
		alert("请先选择单据")
	} else {
		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				"ordrNums": num.toString(),
				"type":'1'
			}
		};
		var saveData = JSON.stringify(savedata);
		console.log(saveData)
		$.ajax({
			type: "post",
			contentType: 'application/json; charset=utf-8',
			url: url + '/mis/ec/logisticsTab/updatePick',
			async: true,
			data: saveData,
			dataType: 'json',
			success: function(data) {
				console.log(data)
				alert(data.respHead.message)
			},
			error: function() {
				console.log(error)
			}
		});
	}
})
//取消拣货
$(document).on('click', ".notPick", function() {
	var gr = $("#print_wuLiu_jqgrids").jqGrid('getGridParam', 'selarrrow');
	var num = []
	for(var i = 0;i < gr.length; i++) {
		//获得行数据
		var rowDatas = $("#print_wuLiu_jqgrids").jqGrid('getRowData', gr[i]);
		num.push(rowDatas.ordrNum)
	}
	if(num.length == 0) {
		alert("请先选择单据")
	} else {
		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				"ordrNums": num.toString(),
				"type":'2'
			}
		};
		var saveData = JSON.stringify(savedata);
		console.log(saveData)
		$.ajax({
			type: "post",
			contentType: 'application/json; charset=utf-8',
			url: url + '/mis/ec/logisticsTab/updatePick',
			async: true,
			data: saveData,
			dataType: 'json',
			success: function(data) {
				console.log(data)
				alert(data.respHead.message)
			},
			error: function() {
				console.log(error)
			}
		});
	}
})


$(document).on('click', '.search1', function() {
	var num = []
	var arrList = []
	//获取此行的仓库编码和存货编码
	var grs = $("#print_wuLiu_jqgrids").jqGrid('getGridParam', 'selarrrow');
	localStorage.setItem("grs", grs);
	if(grs.length != 0) {
		for(var l =0;l<grs.length;l++) {
			//获得行数据
			var rowDatas = $("#print_wuLiu_jqgrids").jqGrid('getRowData', grs[l]);
			var ordrNum = rowDatas.ordrNum
			num.push(ordrNum)
			arrList.push(rowDatas)
		}
		
		var num1 = true;
		for(var i = 0; i < arrList.length; i++) {
			if(arrList[0].templateId != arrList[i].templateId) {
				num1 = false;
				break;
			}
		}
		if(num1 == false) {
			alert("所选择的快递单模板编码存在不同，请修改选择")
		} else if(num1 == true) {
			var sum = num.toString()
			localStorage.setItem("sum", sum);
			for(var l =0;l<grs.length;l++) {
				//获得行数据
				var rowDatas = $("#print_wuLiu_jqgrids").jqGrid('getRowData', grs[l]);
				if(rowDatas.templateId == "ZTO") {
					window.open("../../Components/ec/print_zto.html",'newwindow');
				} else if(rowDatas.templateId == "YTO") {
					window.open("../../Components/ec/print_yto.html",'newwindow');
//					alert("跳转圆通快递模板页面")
				} else if(rowDatas.templateId == "YUNDA") {
					window.open("../../Components/ec/print_yunda.html",'newwindow');
//					alert("跳转韵达快递模板页面")
				} else if(rowDatas.templateId == "STO") {
					window.open("../../Components/ec/print_sto.html",'newwindow');
//					alert("跳转申通快递模板页面")
				}else if(rowDatas.templateId == "JDWL") {
//					alert("跳转京东快递模板页面")
					window.open("../../Components/ec/print_JD.html",'newwindow');
				} else {
					alert("无此模板")
				}
			}
		}
	} else {
		alert("请选择单据")
	}
})


//查询按钮
$(document).on('click', '#finds', function() {
	search3()
})

//条件查询
function search3() {
	var expressCode = $("input[name='expressCode']").val();
	var saleEncd = $("input[name='saleEncd']").val();
	
	var ecOrderId = $("input[name='ecOrderId']").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var deliverWhs = $("input[name='deliverWhs']").val();
	var expressEncd = $("input[name='expressEncd']").val();
	var recName = $("input[name='recName']").val();
	var recMobile = $("input[name='recMobile']").val();
	var storeId = $("input[name='storeId']").val();
	
	var createDate1 = $("input[name='createDate1']").val();
	var createDate2 = $("input[name='createDate2']").val();
	var printTimeBegin = $("input[name='printTimeBegin']").val();
	var printTimeEnd = $("input[name='printTimeEnd']").val();
	var invtyEncds = $("input[name='invtyEncds']").val();
	var qty = $("input[name='qty']").val();
	var isPrint = $("select[name='isPrint']").val();
	var isShip = $("select[name='isShip']").val();
	var isPick = $("select[name='isPick']").val();
	var expressCodes = $("select[name='expressCodes']").val();
	var isBackPlatform = $("select[name='isBackPlatform']").val();
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var myDate = {};
	var data2 = {
		reqHead,
		"reqBody": {
			"storeId":storeId,
			"expressCode":expressCode,
			"saleEncd":saleEncd,
			"ecOrderId":ecOrderId,
			"invtyEncd":invtyEncd,
			"invtyEncds":invtyEncds,
			"expressCodes":expressCodes,
			"isBackPlatform":isBackPlatform,
			"deliverWhs":deliverWhs,
			"expressEncd":expressEncd,
			"recName":recName,
			"recMobile":recMobile,
			"createDate1":createDate1,
			"createDate2":createDate2,
			"qty":qty,
			"printTimeBegin":printTimeBegin,
			"printTimeEnd":printTimeEnd,
			"isPrint":isPrint,
			"isShip":isShip,
			"isPick":isPick,
			"pageNo": page,
			"pageSize": rowNum
		}

	};
	var postD2 = JSON.stringify(data2);
	console.log(postD2)
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url3 + "/mis/ec/logisticsTab/selectList",
		data: postD2,
		dataType: 'json',
		async: true,
		//开始加载动画  添加到ajax里面
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");

		},
		success: function(data) {
			var mydata = {};
			mydata.rows = data.respBody.list;
			var listData = mydata.rows
			for(var j =0 ; j<listData.length;j++) {
				if(listData[j].isPick == 0) {
					listData[j].isPick = '否'
				}
				if(listData[j].isPick == 1) {
					listData[j].isPick = '是'
				}
				if(listData[j].isPrint == 0) {
					listData[j].isPrint = '否'
				}
				if(listData[j].isPrint == 1) {
					listData[j].isPrint = '是'
				}
				if(listData[j].isShip == 0) {
					listData[j].isShip = '否'
				}
				if(listData[j].isShip == 1) {
					listData[j].isShip = '是'
				}
				if(listData[j].isBackPlatform == 0) {
					listData[j].isBackPlatform = '否'
				}
				if(listData[j].isBackPlatform == 1) {
					listData[j].isBackPlatform = '是'
				}
				if(listData[j].deliverSelf == 0) {
					listData[j].deliverSelf = '否'
				}
				if(listData[j].deliverSelf == 1) {
					listData[j].deliverSelf = '是'
				}
			}
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#print_wuLiu_jqgrids").jqGrid("clearGridData");
			$("#print_wuLiu_jqgrids").jqGrid("setGridParam", {
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

//删除
$(function() {
	$(".delOrder1").click(function() {
		var sum = []
		//获取此行的仓库编码和存货编码
		var ids = $("#print_wuLiu_jqgrids").jqGrid('getGridParam', 'selarrrow');
		for(var i = 0;i < ids.length;i++) {
			//获得行数据
			var rowDatas = $("#print_wuLiu_jqgrids").jqGrid('getRowData', ids[i]);
			sum.push(rowDatas.ordrNum)
		}
		var ordrNum = sum.toString()
		if(ids.length == 0) {
			alert("请选择单据!")
		} else if(confirm("确定删除？")) {
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"ordrNum":ordrNum
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/ec/logisticsTab/delete',
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
					alert(data.respHead.message)
					search3()
				},
				error: function() {
					console.log(error)
				}
			})
		}
	})
//	动态加载快递公司下拉框
	var savedata = {
		'reqHead': reqhead,
		'reqBody': {
			'pageNo': 1,
			'pageSize': 500
		},
	};
	var saveData = JSON.stringify(savedata)
	$.ajax({
		type: 'post',
		contentType: 'application/json; charset=utf-8',
		url:url3+"/mis/whs/express_crop/queryList",
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			list = data.respBody.list;
			var option_html = '';
			option_html += '<option value="0">' + "请选择" + "</option>"
			for(i = 0; i < list.length; i++) {
				option_html += '<option value="' + list[i].expressEncd + '"' + 'id="ab">' + list[i].expressNm + "</option>"
			}
			window.pro = $(".expressEncd").first().children("option").val()
			$(".expressEncd").html(option_html);
			$(".expressEncd").change(function(e) {
				window.val = this.value;
				pro = this.value
				window.localStorage.setItem("pro",pro);
			})
			
		},
		error: function() {
			console.log(error)
		}
	})
	//批量修改快递公司
	$(".upDate").click(function() {
		$(".down").show()
		$(".down").css("opacity",1)
	})
	$(".queding").click(function() {
		var sum = []
		//获取此行的仓库编码和存货编码
		var ids = $("#print_wuLiu_jqgrids").jqGrid('getGridParam', 'selarrrow');
		for(var i = 0;i < ids.length;i++) {
			//获得行数据
			var rowDatas = $("#print_wuLiu_jqgrids").jqGrid('getRowData', ids[i]);
			sum.push(rowDatas.orderId)
		}
		var orderId = sum.toString()
		var expressEncd2 = $("select[name='expressEncd2'] option:selected").val()
		if(ids.length == 0) {
			alert("未选择单据!!")
		} else if(expressEncd2 == 0) {
			alert("请选择快递公司")
		} else {
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"ordrNum":orderId,
					"expressEncd":expressEncd2
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/ec/logisticsTab/batchUpdate',
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
					alert(data.respHead.message)
					$(".down").hide()
					$(".down").css("opacity",0)
					$("select[name='expressEncd2']").val(0)
					search3()
				},
				error: function() {
					console.log(error)
				}
			})
		}
	})
	
	$(".quxiao").click(function() {
		$(".down").hide()
		$(".down").css("opacity",0)
		$("select[name='expressEncd2']").val(0)
	})
	
	
	//批量修改快递单号
	$(".upDateDH").click(function() {
		var gr = $("#print_wuLiu_jqgrids").jqGrid('getGridParam', 'selarrrow');
		//获得行数据
		var list = []
		for(var i =0;i<gr.length;i++) {
			var rowDatas = $("#print_wuLiu_jqgrids").jqGrid('getRowData', gr[i]);
			list.push(rowDatas)
		}
		if(list.length == 0) {
			alert("请选择单据！")
		} else {
			$(".printAudie").show()
			$(".down").hide()
			$("#purchaseOrder").hide()
			$(".printAudie").css("opacity",1)
			for(var j = 0; j < list.length; j++) {
				$("#printAudie_jqgrids").setRowData(j + 1, {
					ordrNum: list[j].ordrNum,
					ecOrderId: list[j].ecOrderId,
					expressCode: list[j].expressCode
				});
			}
		}
	})
	$("#yes").click(function() {
		//拿到grid对象
		var obj = $("#printAudie_jqgrids");
		//获取grid表中所有的rowid值
		var rowIds = obj.getDataIDs();
		var ordrNum='';
		var expressCode='';
		if(rowIds.length > 0) {
			for(var i = 0; i < rowIds.length; i++) {
				if(obj.getRowData(rowIds[i]).ordrNum == "") {
					continue;
				} else {
					ordrNum = ordrNum + obj.getRowData(rowIds[i]).ordrNum+",";
					if(obj.getRowData(rowIds[i]).expressCode.indexOf("input")>0){
						alert("请输入快递单号并回车确认！！！！");
						return;
					}
					expressCode = expressCode + obj.getRowData(rowIds[i]).expressCode+",";
				}
			}
		}
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				'ordrNum':ordrNum,
				'expressCode':expressCode
			}
		};
		var Data = JSON.stringify(data);
		$.ajax({
			url: url + '/mis/ec/logisticsTab/updateExpressCode',
			type: 'post',
			data: Data,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			success: function(data) {
				alert(data.respHead.message)
				$(".printAudie").hide()
				$(".printAudie").css("opacity",0)
				$("#purchaseOrder").show()
				search3()
			},
			error: function() {
				console.log(error)
			}
		})
	})
	
	$(".no").click(function() {
		$(".printAudie").hide()
		$(".printAudie").css("opacity",0)
		$("#purchaseOrder").show()
	})
})

// 取消面单
$(document).on('click', '.other', function() {
	var num = []
	//获取此行的仓库编码和存货编码
	var grs = $("#print_wuLiu_jqgrids").jqGrid('getGridParam', 'selarrrow');
	if(grs.length != 0) {
		for(var l =0;l<grs.length;l++) {
			//获得行数据
			var rowDatas = $("#print_wuLiu_jqgrids").jqGrid('getRowData', grs[l]);
			var ordrNum = rowDatas.ordrNum
			num.push(ordrNum)
		}
		var sum = num.toString()
		var myDate = {};
		var savedata = {
			reqHead,
			"reqBody": {
				"logisticsNum":sum
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			type: "post",
			contentType: 'application/json; charset=utf-8',
			url: url + '/mis/ec/logisticsTab/cancelECExpressOrder',
			async: true,
			data: saveData,
			dataType: 'json',
			//开始加载动画  添加到ajax里面
			beforeSend: function() {
				$(".zhezhao").css("display", "block");
				$("#loader").css("display", "block");
	
			},
			success: function(data) {
				alert(data.respHead.message)
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
	} else {
		alert("请选择单据")
	}
})
// 下单（直营）
$(document).on('click', '.downD', function() {
	var num = []
	//获取此行的仓库编码和存货编码
	var grs = $("#print_wuLiu_jqgrids").jqGrid('getGridParam', 'selarrrow');
	if(grs.length != 0) {
		for(var l =0;l<grs.length;l++) {
			//获得行数据
			var rowDatas = $("#print_wuLiu_jqgrids").jqGrid('getRowData', grs[l]);
			var ordrNum = rowDatas.ordrNum
			num.push(ordrNum)
		}
		var sum = num.toString()
		var myDate = {};
		var savedata = {
			reqHead,
			"reqBody": {
				"logisticsNum":sum
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			type: "post",
			contentType: 'application/json; charset=utf-8',
			url: url + '/mis/ec/logisticsTab/achieveJDWLOrder',
			async: true,
			data: saveData,
			dataType: 'json',
			//开始加载动画  添加到ajax里面
			beforeSend: function() {
				$(".zhezhao").css("display", "block");
				$("#loader").css("display", "block");
	
			},
			success: function(data) {
				alert(data.respHead.message)
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
	} else {
		alert("请选择单据")
	}
})
// 取消拦截（直营）
$(document).on('click', '.cancelDown', function() {
	var num = []
	//获取此行的仓库编码和存货编码
	var grs = $("#print_wuLiu_jqgrids").jqGrid('getGridParam', 'selarrrow');
	if(grs.length != 0) {
		for(var l =0;l<grs.length;l++) {
			//获得行数据
			var rowDatas = $("#print_wuLiu_jqgrids").jqGrid('getRowData', grs[l]);
			var ordrNum = rowDatas.ordrNum
			num.push(ordrNum)
		}
		var sum = num.toString()
		var myDate = {};
		var savedata = {
			reqHead,
			"reqBody": {
				"logisticsNum":sum
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			type: "post",
			contentType: 'application/json; charset=utf-8',
			url: url + '/mis/ec/logisticsTab/cancelExpressOrder',
			async: true,
			data: saveData,
			dataType: 'json',
			//开始加载动画  添加到ajax里面
			beforeSend: function() {
				$(".zhezhao").css("display", "block");
				$("#loader").css("display", "block");
	
			},
			success: function(data) {
				alert(data.respHead.message)
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
	} else {
		alert("请选择单据")
	}
})

// 发货
$(document).on('click', '.delivery', function() {
	var num = []
	//获取此行的仓库编码和存货编码
	var grs = $("#print_wuLiu_jqgrids").jqGrid('getGridParam', 'selarrrow');
	if(grs.length != 0) {
		for(var l =0;l<grs.length;l++) {
			//获得行数据
			var rowDatas = $("#print_wuLiu_jqgrids").jqGrid('getRowData', grs[l]);
			var ordrNum = rowDatas.ordrNum
			num.push(ordrNum)
		}
		var sum = num.toString()
		var myDate = {};
		var savedata = {
			reqHead,
			"reqBody": {
				"logisticsNum":sum
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			type: "post",
			contentType: 'application/json; charset=utf-8',
			url: url + '/mis/ec/logisticsTab/platOrderShip',
			async: true,
			data: saveData,
			dataType: 'json',
			//开始加载动画  添加到ajax里面
			beforeSend: function() {
				$(".zhezhao").css("display", "block");
				$("#loader").css("display", "block");
	
			},
			success: function(data) {
				alert(data.respHead.message)
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
	} else {
		alert("请选择单据")
	}
})
// 取消发货
$(document).on('click', '.cancelShip', function() {
	var num = []
	//获取此行的仓库编码和存货编码
	var grs = $("#print_wuLiu_jqgrids").jqGrid('getGridParam', 'selarrrow');
	if(grs.length != 0) {
		for(var l =0;l<grs.length;l++) {
			//获得行数据
			var rowDatas = $("#print_wuLiu_jqgrids").jqGrid('getRowData', grs[l]);
			var ordrNum = rowDatas.ordrNum
			num.push(ordrNum)
		}
		var sum = num.toString()
		var myDate = {};
		var savedata = {
			reqHead,
			"reqBody": {
				"logisticsNum":sum
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			type: "post",
			contentType: 'application/json; charset=utf-8',
			url: url + '/mis/ec/logisticsTab/cancelShip',
			async: true,
			data: saveData,
			dataType: 'json',
			//开始加载动画  添加到ajax里面
			beforeSend: function() {
				$(".zhezhao").css("display", "block");
				$("#loader").css("display", "block");
	
			},
			success: function(data) {
				alert(data.respHead.message)
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
	} else {
		alert("请选择单据")
	}
})

// 强制发货
$(document).on('click', '.forceShip', function() {
	var num = []
	//获取此行的仓库编码和存货编码
	var grs = $("#print_wuLiu_jqgrids").jqGrid('getGridParam', 'selarrrow');
	if(grs.length != 0) {
		for(var l =0;l<grs.length;l++) {
			//获得行数据
			var rowDatas = $("#print_wuLiu_jqgrids").jqGrid('getRowData', grs[l]);
			var ordrNum = rowDatas.ordrNum
			num.push(ordrNum)
		}
		var sum = num.toString()
		var myDate = {};
		var savedata = {
			reqHead,
			"reqBody": {
				"logisticsNum":sum
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			type: "post",
			contentType: 'application/json; charset=utf-8',
			url: url + '/mis/ec/logisticsTab/forceShip',
			async: true,
			data: saveData,
			dataType: 'json',
			//开始加载动画  添加到ajax里面
			beforeSend: function() {
				$(".zhezhao").css("display", "block");
				$("#loader").css("display", "block");
	
			},
			success: function(data) {
				alert(data.respHead.message)
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
	} else {
		alert("请选择单据")
	}
})

// 取电子面单
$(document).on('click', '.other-git', function() {
	var num = []
	//获取此行的仓库编码和存货编码
	var grs = $("#print_wuLiu_jqgrids").jqGrid('getGridParam', 'selarrrow');
	if(grs.length != 0) {
		for(var l =0;l<grs.length;l++) {
			//获得行数据
			var rowDatas = $("#print_wuLiu_jqgrids").jqGrid('getRowData', grs[l]);
			var ordrNum = rowDatas.ordrNum
			num.push(ordrNum)
		}
		var sum = num.toString()
		var myDate = {};
		var savedata = {
			reqHead,
			"reqBody": {
				"logisticsNum":sum
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			type: "post",
			contentType: 'application/json; charset=utf-8',
			url: url + '/mis/ec/logisticsTab/achieveECExpressOrder',
			async: true,
			data: saveData,
			dataType: 'json',
			//开始加载动画  添加到ajax里面
			beforeSend: function() {
				$(".zhezhao").css("display", "block");
				$("#loader").css("display", "block");
	
			},
			success: function(data) {
				alert(data.respHead.message)
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
	} else {
		alert("请选择单据")
	}
})
//导入
$(function () {
    $(".importExcel").click(function () {
    	var loginName = localStorage.loginAccNum
    	var files = $("#FileUpload").val()
    	if(files.indexOf("xlsx")>0){
    		alert("请选择xls格式文件");
    		return;
    	}
    	var fileObj = document.getElementById("FileUpload").files[0];
        var formFile = new FormData();
       	formFile.append("action", "UploadVMKImagePath");
       	formFile.append("accNum", loginName);  
       	formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
        var data = formFile;
        if(files != "") {
        	$.ajax({
	            type: 'post',
	            url: url + "/mis/ec/logisticsTab/importExpressCode",
	            data:data,
	          	dataType: "json",
	           	cache: false,//上传文件无需缓存
	           	processData: false,//用于对data参数进行序列化处理 这里必须false
	           	contentType: false, //必须//开始加载动画  添加到ajax里面
				beforeSend: function() {
					$(".zhezhao").css("display", "block");
					$("#loader").css("display", "block");
		
				},
	           	success: function(data) {
	           		alert(data.respHead.message)
					$(".down").hide()
					$(".down").css("opacity",0)
	           	},
	           	error:function(error) {
	           		console.log(error)
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
    });
});
var arr=[];
var obj={}
//导出
$(document).on('click', '.exportExcel', function() {
	var expressCode = $("input[name='expressCode']").val();
	var saleEncd = $("input[name='saleEncd']").val();
	
	var ecOrderId = $("input[name='ecOrderId']").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var deliverWhs = $("input[name='deliverWhs']").val();
	var expressEncd = $("input[name='expressEncd']").val();
	var recName = $("input[name='recName']").val();
	var recMobile = $("input[name='recMobile']").val();
	var storeId = $("input[name='storeId']").val();
	
	var createDate1 = $("input[name='createDate1']").val();
	var createDate2 = $("input[name='createDate2']").val();
	var printTimeBegin = $("input[name='printTimeBegin']").val();
	var printTimeEnd = $("input[name='printTimeEnd']").val();
	var qty = $("input[name='qty']").val();
	var isPrint = $("select[name='isPrint']").val();
	var isShip = $("select[name='isShip']").val();
	var isPick = $("select[name='isPick']").val();
	
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"storeId":storeId,
			"expressCode":expressCode,
			"saleEncd":saleEncd,
			"ecOrderId":ecOrderId,
			"invtyEncd":invtyEncd,
			"deliverWhs":deliverWhs,
			"expressEncd":expressEncd,
			"recName":recName,
			"recMobile":recMobile,
			"createDate1":createDate1,
			"createDate2":createDate2,
			"qty":qty,
			"printTimeBegin":printTimeBegin,
			"printTimeEnd":printTimeEnd,
			"isPrint":isPrint,
			"isShip":isShip,
			"isPick":isPick,
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url3 + "/mis/ec/logisticsTab/exportList",
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
			var list = data.respBody.list;
			var execlName = '物流单查询'
			ExportData(list, execlName)
		},
		error: function() {
			console.log(error)
		}
	})
	
})