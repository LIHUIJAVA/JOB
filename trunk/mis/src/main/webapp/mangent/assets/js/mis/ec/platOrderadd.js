$(function() {

	$(document).on('click', '.storeIds_biaoge', function() {
		window.open("../../Components/baseDoc/storeRecordList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});

	$(document).on('click', '.deliverWhs_biaoge', function() {
		window.open("../../Components/baseDoc/whsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	$(document).on('click', '.expressCode_biaoge', function() {
		window.open("../../Components/ec/whsPlatExpressMappList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});

	$(".saveOnes").attr('disabled', true);
	$('.saveOnes').addClass("gray");
	$(".aaaa").attr('disabled', true);
	$('.aaaa').addClass("gray");
	//3.快递公司
	$(document).on('keypress', '#ckys_kd', function(event) {
		if(event.keyCode == '13') {
			$('#ckys_kd').blur();
		}
	})

	$(document).on('blur', '#ckys_kd', function() {
		var ckys_kd = $("#ckys_kd").val();
		console.log(ckys_kd)
		dev({
			doc1: $("#ckys_kd"),
			doc2: $("#expressName"),
			showData: {
				"expressEncd": ckys_kd
			},
			afunction: function(data) {
				if(ckys_kd == '') {
					$("#expressName").val('')
				}else {
					if(data.respHead.isSuccess == false) {
						alert("不存在此快递公司请确认！")
					} else {
						var expressName = data.respBody.expressNm;
						$("#expressName").val(expressName)
					}
				}
			},
			url: url + "/mis/whs/express_crop/selectExpressCorp",
		})
	});
})



$(function() {
	//页面加载完成之后执行	
	var pageNo = 1;
	var rowNum = 10;
	pageInits();
	//点击右边条数修改显示行数
	$(".ui-pg-selbox.ui-widget-content.ui-corner-all").click(function() {
		pageNo = $("#add_jqGrids").jqGrid("getGridParam", "page");
		rowNum = $("select[class='ui-pg-selbox ui-widget-content ui-corner-all']")[0].value >> 0;
		var data3 = {
			reqHead,
			"reqBody": {
				"pageSize": rowNum,
				"pageNo": pageNo
			}
		};
		var postD3 = JSON.stringify(data3);
		jQuery("#add_jqGrids").jqGrid({
			url: url3 + "/mis/ec/PlatOrder/queryList", //组件创建完成之后请求数据的url
			mtype: "post",
			datatype: "json", //请求数据返回的类型。可选json,xml,txt
			postData: postD3,
			ajaxGridOptions: {
				contentType: 'application/json; charset=utf-8'
			},

			rowList: [10, 20, 30], //可供用户选择一页显示多少条
			//			autowidth:true,
			pager: '#add_jqGridPager', //表格页脚的占位符(一般是div)的id
			sortname: 'orderId', //初始化的时候排序的字段
			sortorder: "desc", //排序方式,可选desc,asc
			viewrecords: true,
			rowNum: rowNum, //一页显示多少条
			pageNo: pageNo,
			jsonReader: {
				root: "respBody.list", // json中代表实际模型数据的入口
				records: "respBody.count", // json中代表数据行总数的数据		            
				total: "respBody.pages", // json中代表页码总数的数据
				repeatitems: true,
			},
			onPaging: function(pgButton) {
				pageNo = $("#add_jqGrids").jqGrid("getGridParam", "page");
				rowNum = $("select[class='ui-pg-selbox ui-widget-content ui-corner-all']")[0].value >> 0;
				if(pgButton === 'prev') {
					pageNo -= 1;
				} else if(pgButton === 'next') {
					pageNo += 1;

				} else if(pgButton === 'records') {
					pageNo = 1;
				}
			}
		});
	})
});

//增行   保存
$(function() {

	$(".addOne").click(function() {
		$(".saveOnes").attr('disabled', false);
		$('.saveOnes').removeClass("gray");
		$("#mengban").hide();
		$(".aaaa").attr('disabled', false)
		$('.aaaa').removeClass("gray");

		mType = 1;
		//拿到Gride中所有主键id的值
		var gr = $("#add_jqGrids").jqGrid('getDataIDs');
		if(gr.length == 0) {
			var rowid = 0;
		} else if(gr.length != 0) {
			// 获得当前最大行号（数据编码）
			var rowid = Math.max.apply(Math, gr);
		}
		// 获得新添加行的行号（数据编码）
		window.newrowid = rowid + 1;
		var dataRow = {
			"ecgoodid": "",
			"goodNum": "",
			"goodMoney": '',
			"payMoney": '',
			"goodSku": '',
			"discountMoney": '',
			"adjustMoney": '',
			"goodPrice": '',
			"payPrice": '',
			"sellerPrice": '',
			"invId": '',
			"invNum": '',
			"ptoCode": '',
			"ptoName": '',
			"canRefNum": '',
			"canRefMoney": '',
			"isGift": '',
			"memo": '',
		};
		//		$("#add_jqGrids").setColProp('storeId', {
		//			editable: true
		//		}); //设置editable属性由true改为false

		//将新添加的行插入到第一列
		$("#add_jqGrids").jqGrid("addRowData", newrowid, dataRow, "last");

		//设置grid单元格可编辑
//		$('#add_jqGrids').jqGrid('editRow', newrowid, true);
//		$("#add_jqGrids").jqGrid('setSelection', newrowid);

		$("#add_jqGrids").setColProp("goodName",{editable:true});
		$("#add_jqGrids").setColProp("goodSku",{editable:true});
		$("#add_jqGrids").setColProp("#" + newrowid + "_invId",{editable:true});
		$("#" + newrowid + "_goodName").attr("maxlength", '100')
//		$("#" + newrowid + "_goodId").on("dblclick", function() {
//			//			alert('店铺商品档案')
//			$("#good").css('opacity', 1)
//			$("#good").show()
//			$("#purchaseOrder").hide()
//			$("#" + newrowid + "_goodId").attr("readonly", "readonly");
//			$("#" + newrowid + "_goodName").attr("readonly", "readonly");
//			$("#" + newrowid + "_goodSku").attr("readonly", "readonly");
//			$("#" + newrowid + "_invId").attr("readonly", "readonly");
//		})
		
	})

})

function getJQAllData() {
	//拿到grid对象
	var obj = $("#add_jqGrids");
	//获取grid表中所有的rowid值
	var rowIds = obj.getDataIDs();
	//初始化一个数组arrayData容器，用来存放rowData
	var arrayData = new Array();
	if(rowIds.length > 0) {
		for(var i = 0; i < rowIds.length; i++) {
			arrayData.push(obj.getRowData(rowIds[i]));
		}
	}
	return arrayData;
}

function IsCheckValue(storeId, storeName, payTime, goodNum1, recAddress, recName, recMobile, ecOrderId, tradeDt, province, city, county, deliverWhs, canMatchActive, deliverSelf, listData) {
	if(storeId == '') {
		alert("店铺编号不能为空")
		return false;
	} else if(storeName == '') {
		alert("店铺名称不能为空")
		return false;
	} else if(payTime == '') {
		alert("付款时间不能为空")
		return false;
	} else if(goodNum1 == '') {
		alert("商品数量不能为空")
		return false;
	} else if(recAddress == '') {
		alert("收货人详细地址不能为空")
		return false;
	} else if(recName == '') {
		alert("收货人姓名不能为空")
		return false;
	} else if(recMobile == '') {
		alert("收货人手机号不能为空")
		return false;
	}  else if(recMobile.length < 11) {
		alert("收货人手机号格式错误")
		return false;
	} else if(ecOrderId == '') {
		alert("电商订单号不能为空")
		return false;
	} else if(tradeDt == '') {
		alert("交易时间不能为空")
		return false;
	} else if(province == '') {
		alert("收货地址省不能为空")
		return false;
	} else if(city == '') {
		alert("收货地址市不能为空")
		return false;
	} else if(county == '') {
		alert("收货地址区不能为空")
		return false;
	} else if(deliverWhs == '') {
		alert("发货仓库不能为空")
		return false;
	} else if(deliverSelf == '') {
		alert("是否自发货未选择")
		return false;
	} else if(canMatchActive == '') {
		alert("是否匹配活动未选择")
		return false;
	} else if(listData.length == 0) {
		alert("未添加子表")
		return false;
	} else if(listData.length != 0) {
		for(var i = 0; i < listData.length; i++) {
			if(listData[i].goodName == "") {
				alert("商品名称不能为空")
				return false;
			} else if(listData[i].goodNum == "") {
				alert("商品数量不能为空")
				return false;
			} else if(listData[i].goodMoney == "") {
				alert("商品金额不能为空")
				return false;
			} else if(listData[i].payMoney == "") {
				alert("实付金额不能为空")
				return false;
			} else if(listData[i].goodPrice == "") {
				alert("商品单价不能为空")
				return false;
			} else if(listData[i].payPrice == "") {
				alert("实付单价不能为空")
				return false;
			} else if(listData[i].invId != '' && listData[i].invNum == '') {
				alert("存货数量不能为空")
				return false;
			} else if(listData[i].ptoCode != '' && listData[i].ptoName == '') {
				alert("母件名称不能为空")
				return false;
			} else if(listData[i].ptoName != '' && listData[i].ptoCode == '') {
				alert("母件编码不能为空")
				return false;
			} else if(listData[i].goodId == '' && listData[i].invId == '') {
				alert("平台商品编码与存货编码不能同时为空")
				return false;
			} else if(listData[i].isGift == 2) {
				alert("是否赠品未选择")
				return false;
			}
		}
	}
	return true;
}

//保存
$(function() {
	$(".saveOnes").click(function() {
		var listData = getJQAllData()
		var storeId = $('input[name="storeId1"]').val();
		var storeName = $('input[name="storeNm1"]').val();
		var payTime = $('input[name="payTime1"]').val();
		var waif = $('input[name="waif1"]').val();
		var auditHint = $('input[name="auditHint1"]').val();
		var goodNum1 = $('input[name="goodNum1"]').val();
		var goodMoney1 = $('input[name="goodMoney1"]').val();
		var payMoney1 = $('input[name="payMoney1"]').val();
		var buyerNote = $('input[name="buyerNote1"]').val();
		var sellerNote = $('input[name="sellerNote1"]').val();
		var recAddress = $('input[name="recAddress1"]').val();
		var buyerId = $('input[name="buyerId1"]').val();
		var recName = $('input[name="recName1"]').val();
		var recMobile = $('input[name="recMobile1"]').val();
		var ecOrderId = $('input[name="ecOrderId1"]').val();
		var isInvoice = 0;
		var invoiceTiel = $('input[name="invoiceTiel1"]').val();
		var noteFlag = 0;
		var isClose = 0;
		var isShip = 0;
		var adjustMoney1 = $('input[name="adjustMoney1"]').val();
		
		if(adjustMoney1 == '') {
			var adjustMoney1 = 0
		}

		var discountMoney1 = $('input[name="discountMoney1"]').val();
		if(discountMoney1 == '') {
			var discountMoney1 = 0
		}
		var adjustStatus = 0;
		var tradeDt = $('input[name="tradeDt1"]').val();
		var bizTypId = 2;
		var sellTypId = 1;
		var recvSendCateId = 6;
		var orderStatus = 0;
		var returnStatus = 0;
		var memo = $('input[name="memo1"]').val();

		var orderSellerPrice = $('input[name="orderSellerPrice1"]').val();
		var province = $('input[name="province1"]').val();
		var city = $('input[name="city1"]').val();
		var county = $('input[name="county1"]').val();

		var freightPrice = $('input[name="freightPrice1"]').val();
		if(freightPrice == '') {
			var freightPrice = 0
		}
		

		var deliverWhs = $('input[name="deliverWhs1"]').val();

		var deliveryType = 0;
		var deliverSelf = $('select[name="deliverSelf1"] option:selected').val();
		var canMatchActive = $('select[name="canMatchActive1"] option:selected').val();
		var expressCode = $('input[name="expressCode1"]').val();
		var expressNo = $('input[name="expressNo1"]').val();

		var weight = $('input[name="weight1"]').val();
		if(weight == '') {
			var weight = 0
		}

		var expressTemplate = $('input[name="expressTemplate1"]').val();

		for(var i = 0;i < listData.length;i++) {
			listData[i].sellerPrice = listData[i].payPrice
			listData[i].canRefMoney = listData[i].payMoney
			if(listData[i].isGift == 1) {
				$('select[name="hasGift1"] option:selected').val(1)
			} else {
				$('select[name="hasGift1"] option:selected').val(0)
			}
			if(listData[i].invNum != '') {
				listData[i].canRefNum = listData[i].invNum
			}
		}
		var hasGift = $('select[name="hasGift1"] option:selected').val();
		if(IsCheckValue(storeId, storeName, payTime, goodNum1, recAddress, recName, recMobile, ecOrderId, tradeDt, province, city, county, deliverWhs, canMatchActive, deliverSelf,listData) == true) {
			var save = {
				"reqHead": reqhead,
				"reqBody":{
					"storeId":storeId,
					"storeName":storeName,
					"payTime":payTime,
					"waif":waif,
					"isAudit":0,
					"auditHint":auditHint,
					"goodNum":goodNum1,
					"goodMoney":goodMoney1,
					"payMoney":payMoney1,
					"buyerNote":buyerNote,
					"sellerNote":sellerNote,
					"recAddress":recAddress,								
					"buyerId":buyerId,
					"recName":recName,
					"recMobile":recMobile,
					"ecOrderId":ecOrderId,
					
					
					"isInvoice":0,
					"invoiceTitle":invoiceTiel,
					"noteFlag":0,
					"isClose":0,
					"isShip":0,
					"adjustMoney":adjustMoney1,
					"discountMoney":discountMoney1,
					
					"adjustStatus":0,
					"tradeDt":tradeDt,
					"bizTypId":2,
					"sellTypId":1,
					"recvSendCateId":6,
					
					"orderStatus":0,
					"returnStatus":0,
					"hasGift":hasGift,
					"memo":memo,
					"orderSellerPrice":payMoney1,
					"province":province,
					"city":city,
					"county":county,
					"freightPrice":freightPrice,
					"deliverWhs":deliverWhs,
					"canMatchActive":canMatchActive,
					"deliveryType":0,
					"deliverSelf":deliverSelf,
					"expressCode":expressCode,
					"expressNo":expressNo,
					"weight":weight,
					"expressTemplate":expressTemplate,
					"list":listData
				}
			}
			var saveJson = JSON.stringify(save);
			$.ajax({
				type:"post",
				url:url3+"/mis/ec/platOrder/add",
				async:true,
				data:saveJson,
				dataType:'json',
				contentType: 'application/json',
				beforeSend: function() {
					$("#mengban1").css("display", "block");
					$("#loader").css("display", "block");
				},
				complete: function() {
					$("#mengban1").css("display", "none");
					$("#loader").css("display", "none");
				},
				success:function(msgAdd){
					alert(msgAdd.respHead.message)
					$("mengban").show()
					$(".saveOnes").attr('disabled', true);
					$('.saveOnes').addClass("gray");
					$(".aaaa").attr('disabled', true);
					$('.aaaa').addClass("gray");
				},
				error:function(err){
					console.log("失败")
				}
			});
		}
	})
})

$(document).on('click', '.again', function() {
	window.location.reload();
})

function pageInits() {
	allHeight()
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	//初始化表格
	jQuery("#add_jqGrids").jqGrid({
//		url: '../../assets/js/json/order.json',
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['平台商品编号', '商品名称 *', '商品数量 *', '商品金额 *', '实付金额 *', '商品sku',
			'系统优惠金额', '卖家调整金额', '商品单价 *', '实付单价 *', '结算单价 *', '存货编码', '存货数量', '母件编码', '母件名称',
			'可退数量', '可退金额', '是否赠品 *', '表体备注'
		], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'goodId',
				align: "center",
				index: 'orderId',
				editable: true,
				sortable: false
			},
			{
				name: 'goodName',
				align: "center",
				index: 'orderId',
				editable: true,
				sortable: false
			},
			{
				name: 'goodNum',
				align: "center",
				index: 'storeId',
				editable: true,
				sortable: false
			},
			{
				name: 'goodMoney',
				align: "center",
				index: 'storeName',
				editable: false,
				sortable: false
			},
			{
				name: 'payMoney',
				align: "center",
				index: 'isAudit',
				editable: true,
				sortable: false
			},
			{
				name: 'goodSku',
				align: "center",
				index: 'buyerId',
				editable: true,
				sortable: false
			},
			{
				name: 'discountMoney',
				align: "center",
				index: 'onteFlag',
				editable: false,
				sortable: false
			},
			{
				name: 'adjustMoney',
				align: "center",
				index: 'isClose',
				editable: true,
				sortable: false
			},
			{
				name: 'goodPrice',
				align: "center",
				index: 'isClose',
				editable: true,
				sortable: false
			},
			{
				name: 'payPrice',
				align: "center",
				index: 'isClose',
				editable: true,
				sortable: false
			},
			{
				name: 'sellerPrice',
				align: "center",
				index: 'isShip',
				editable: true,
				hidden: true,
				sortable: false
			},
			{
				name: 'invId',
				align: "center",
				index: 'isShip',
				editable: true,
				sortable: false
			},
			{
				name: 'invNum',
				align: "center",
				index: 'isShip',
				editable: true,
				sortable: false
			},
			{
				name: 'ptoCode',
				align: "center",
				index: 'isShip',
				editable: true,
				sortable: false
			},
			{
				name: 'ptoName',
				align: "center",
				index: 'isShip',
				editable: true,
				sortable: false
			},
			{
				name: 'canRefNum',
				align: "center",
				index: 'isShip',
				editable: true,
				hidden: true,
				sortable: false
			},
			{
				name: 'canRefMoney',
				align: "center",
				index: 'isShip',
				editable: true,
				hidden: true,
				sortable: false
			},
			{
				name: 'isGift',
				align: "center",
				index: 'isShip',
				editable: false,
				edittype: 'select',
				formatter: 'select',
				editoptions: {
					value: "2:请选择;0:否;1:是"
				},
				sortable: false
			},
			{
				name: 'memo',
				align: "center",
				index: 'isShip',
				editable: true,
				sortable: false
			}

		],
		rowNum: 100, //一页显示多少条
		rowList: [100, 200, 300], //可供用户选择一页显示多少条			
		autowidth: true,
		height:height,
		autoScroll: true,
		shrinkToFit: false,
		cellEdit: true,
		cellsubmit: "clientArray",
		rownumbers: true,
		editurl: "clientArray", // 行提交
		pager: '#add_jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'orderId', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框 
		multiboxonly: true,
		caption: "订单新增", //表格的标题名字	
		footerrow: true,
		//在编辑状态时
		afterEditCell: function(rowid, cellname, val, iRow, iCol) {
			$(".saveOnes").addClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
			$("#" + rowid + "_whsEncd").attr("readonly", "readonly");
			if(cellname == "goodId") {
				$('input[name="goodId"]').bind("dblclick", function() {
					$("#good").css('opacity', 1)
					$("#good").show()
					$("#purchaseOrder").hide()
				});
			}
			if((cellname == "goodNum") ||
				(cellname == "goodMoney") ||
				(cellname == "payMoney") ||
				(cellname == "discountMoney") ||
				(cellname == "adjustMoney") ||
				(cellname == "goodPrice") ||
				(cellname == "payPrice") ||
				(cellname == "invNum")) {
					$("#" +  rowid + "_goodNum").attr("type","number")
					$("#" +  rowid + "_goodMoney").attr("type","number")
					$("#" +  rowid + "_payMoney").attr("type","number")
					$("#" +  rowid + "_discountMoney").attr("type","number")
					$("#" +  rowid + "_adjustMoney").attr("type","number")
					$("#" +  rowid + "_goodPrice").attr("type","number")
					$("#" +  rowid + "_payPrice").attr("type","number")
					$("#" +  rowid + "_invNum").attr("type","number")
				}
			
		},
		//离开编辑状态
		afterRestoreCell: function(rowid, cellname, val, iRow, iCol) {
			$(".saveOnes").removeClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true)
		},
		//回车保存
		afterSaveCell: function(rowid, cellname, val, iRow, iCol) {
			$(".saveOnes").removeClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
			
			if((cellname == "goodPrice") ||
				(cellname == "goodNum") ||
//				(cellname == "payPrice") ||
				(cellname == "payMoney") ||
				(cellname == "discountMoney") ||
				(cellname == "adjustMoney") ||
				(cellname == "invNum") ||
				(cellname == "isGift")) {
				SetNums1(rowid, cellname);
				var list = getJQAllData();
				var num = []
				var num1 = []
				var num2 = []
				var gifts = []
				var goodNum = 0;
				var goodMoney = 0;
				var payMoney = 0;
				var discountMoney = 0;
				var adjustMoney = 0;
				var invNum = 0;
				var gift = 0;
				var isGift = [];
				for(var i = 0; i < list.length; i++) {
					goodNum += parseFloat(list[i].goodNum * 1);
					goodMoney += parseFloat(list[i].goodMoney * 1);
					payMoney += parseFloat(list[i].payMoney * 1);
					discountMoney += parseFloat(list[i].discountMoney * 1);
					adjustMoney += parseFloat(list[i].adjustMoney * 1);
					invNum += parseFloat(list[i].invNum * 1)
					gift = list[i].isGift * 1
					isGift.push(gift)
				};
				
				
				if(isNaN(goodNum)) {
					goodNum = 0
				}
				if(isNaN(goodMoney)) {
					goodMoney = 0
				}
				if(isNaN(payMoney)) {
					payMoney = 0
				}
				if(isNaN(discountMoney)) {
					discountMoney = 0
				}
				if(isNaN(adjustMoney)) {
					adjustMoney = 0
				}
				if(isNaN(invNum)) {
					invNum = 0
				}
				
				if(isGift.includes(1) == true) {
					$("select[name='hasGift1']").val(1)
				} else {
					$("select[name='hasGift1']").val(0)
				}
				if(cellname == "goodPrice") {
					$("input[name='goodMoney1']").val(goodMoney)
				}
				if(cellname == "payMoney") {
					$("input[name='payMoney1']").val(payMoney)
				}
				if(cellname == "goodNum") {
					$("input[name='goodNum1']").val(goodNum)
				}
				goodNum = goodNum.toFixed(prec)
				goodMoney = precision(goodMoney,2)
				payMoney = precision(payMoney,2)
				discountMoney = precision(discountMoney,2)
				adjustMoney = precision(adjustMoney,2)
				invNum = invNum.toFixed(prec)
				$("#add_jqGrids").footerData('set', {
					"goodId":"本页合计",
					"goodNum": goodNum,
					"goodMoney": goodMoney,
					"payMoney": payMoney,
					"discountMoney": discountMoney,
					"adjustMoney": adjustMoney,
					"invNum": invNum,
				});
			}
		}
	})
	$(function() {
		$(window).resize(function() {
			$("#add_jqGrids").setGridWidth($(window).width());
		});
	});
	$("#add_jqGrids").jqGrid('navGrid', '#add_jqGridPager', {
		edit: false,
		add: false,
		del: false,
		search: false,
		refresh: false,
	}).navButtonAdd('#add_jqGridPager', {
		caption: "",
		buttonicon: "ui-icon-trash",
		onClickButton: function() {
			var grid = "add_jqGrids"
			//删除一行操作
			removeRows1(grid);
		},
		position: "first"
	}).navButtonAdd('#add_jqGridPager', {
		caption: "",
		buttonicon: "ui-icon-newwin",
		onClickButton: function() {
			//复制一行操作
			copyRows1();
		},
		position: "last"
	}).navButtonAdd('#add_jqGridPager', {
		caption: "",
		buttonicon: "ui-icon-plus",
		onClickButton: function() {
			//新增一行操作
			addRows1();
		},
		position: "last"
	})
}
//新增表格行
function addRows1() {
	mType = 1;
	var gr = $("#add_jqGrids").jqGrid('getDataIDs');
	if(gr.length == 0) {
		var rowid = 0;
	} else if(gr.length != 0) {
		var rowid = Math.max.apply(Math, gr);
	}
	window.newrowid = rowid + 1;
	var dataRow = {};
	$("#add_jqGrids").jqGrid("addRowData", newrowid, dataRow, "last");
}
//删除表格行
function removeRows1() {
	var gr = $("#add_jqGrids").jqGrid('getGridParam', 'selarrrow');
	if(gr.length == 0) {
		alert("请选择要删除的行");
		return;
	} else {
		var length = gr.length;
		for(var i = 0; i < length + 1; i++) {
			$("#add_jqGrids").jqGrid("delRowData", gr[0]);
		}
	}
}
//复制表格行
function copyRows1() {
	var ids = $("#add_jqGrids").jqGrid('getGridParam', 'selarrrow');
	var dataRow = $("#add_jqGrids").jqGrid('getRowData', ids);
	if(ids.length == 0) {
		alert("请选择要复制的行");
		return;
	} else if(ids.length > 1) {
		alert("每次只能复制一行");
		return;
	} else {
		var gr = $("#add_jqGrids").jqGrid('getDataIDs');
		// 选中行实际表示的位置
		var rowid = Math.max.apply(Math, gr);
		// 新插入行的位置
		var newrowid = rowid + 1;
		// 插入一行
		$("#add_jqGrids").jqGrid("addRowData", newrowid, dataRow, "last");
	}
}

//设置数量和金额
function SetNums1(rowid, cellname) {
	var rowDatas = $("#add_jqGrids").jqGrid('getRowData', rowid);
	var goodNum = parseFloat(rowDatas.goodNum); //商品数量
	var goodPrice = parseFloat(rowDatas.goodPrice); //商品单价
	var goodMoney = parseFloat(rowDatas.goodMoney); //商品金额
	var payMoney = parseFloat(rowDatas.payMoney); //商品金额
	var discountMoney = parseFloat(rowDatas.discountMoney); //商品金额
//	var payPrice = parseFloat(rowDatas.payPrice); //商品金额
	var isGift = parseFloat(rowDatas.isGift); //商品金额
//	if(!isNaN(goodPrice)) {
//		goodPrice = 0
//	}
	if(cellname == "payMoney") {
		if(payMoney != 0) {
			isGift = 0
		} else {
			isGift = 1
		}
	}
	if(cellname == "goodPrice") {
		goodMoney = parseFloat(goodNum * goodPrice);
		discountMoney = parseFloat(goodMoney - payMoney);
	}
	if(cellname == "payMoney") {
		discountMoney = parseFloat(goodMoney - payMoney);
	}
	if(isNaN(goodMoney)) {
		goodMoney = ''	
	}
	if(isNaN(discountMoney)) {
		discountMoney = ''	
	}
	$("#add_jqGrids").setRowData(rowid, {
		goodMoney: goodMoney,
		discountMoney: discountMoney,
		isGift: isGift,
	});
}


$(function() {
	//	店铺商品
	//确定
	$(".define").click(function() {
		//		订单
		//获得行号
		var rowid = $("#add_jqGrids").jqGrid('getGridParam', 'selrow');

		//	仓库档案
		//	获得行号
		var ids = $("#good_jqgrids_a").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowData = $("#good_jqgrids_a").jqGrid('getRowData', ids);
		console.log(rowData.ecGoodId)
		//		商品编码
		$("#" + rowid + "_goodId").val(rowData.ecGoodId)

		$("#add_jqGrids").setRowData(rowid, {
			goodSku: rowData.goodSku,
			invId: rowData.goodId,
			goodName: rowData.goodName,
		})
		
		$("#add_jqGrids").setColProp("goodSku",{editable:false});
		$("#add_jqGrids").setColProp("invId",{editable:false});
		$("#add_jqGrids").setColProp("goodName",{editable:false});

		$("#good").css("opacity", 0);
		$("#good").hide();
		$("#purchaseOrder").show();
	})
	//	取消
	$(".abolish").click(function() {
		//		订单
		//获得行号
		var rowid = $("#add_jqGrids").jqGrid('getGridParam', 'selrow');

		//	仓库档案
		//	获得行号
		var ids = $("#good_jqgrids_a").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowData = $("#good_jqgrids_a").jqGrid('getRowData', ids);
		$("#good").css("opacity", 0);
		$("#good").hide();
		$("#purchaseOrder").show();
//		$("#" + rowid + "_goodId").val('')
//
//		$("#add_jqGrids").setRowData(rowid, {
//			goodSku: '',
//			invId: '',
//			goodName: '',
//		})
		$("#add_jqGrids").setColProp("goodSku",{editable:true});
		$("#add_jqGrids").setColProp("invId",{editable:true});
		$("#add_jqGrids").setColProp("goodName",{editable:true});
	})

	$(".delOrder").click(function() {
		//	获得第一个行号
		var gr = $("#add_jqGrids").jqGrid('getGridParam', 'selrow');
		//获得第一个行数据
		//		var rowDatas = $("#t_jqgrids").jqGrid('getRowData', gr);
		if(!gr) {
			alert("请选择要删除的行");
			return;
		} else {
			if(confirm('确实要删除该内容吗?')) {

				$("#add_jqGrids").jqGrid("delRowData", gr);
			}
		}
	})
})