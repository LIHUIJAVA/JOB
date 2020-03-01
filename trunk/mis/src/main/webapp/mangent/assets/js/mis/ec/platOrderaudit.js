var count;
var pages;
var page = 1;
var rowNum;

$(function() {
	//1.仓库
	$(document).on('keypress', '#whsEncd', function(event) {
		if(event.keyCode == '13') {
			$('#whsEncd').blur();
		}
	})

	$(document).on('blur', '#whsEncd', function() {
		var whsEncd = $("#whsEncd").val();
		dev({
			doc1: $("#whsEncd"),
			doc2: $("#whsNm"),
			showData: {
				"whsEncd": whsEncd
			},
			afunction: function(data) {
				if(whsEncd == '') {
					$("#whsNm").val('')
				}else {
					if(data.respHead.isSuccess == false) {
						alert("不存在此发货仓库请确认！")
					} else {
						var whsNm = data.respBody.list[0].whsNm;
						$("#whsNm").val(whsNm)
					}
				}
				
			},
			url: url + "/mis/whs/whs_doc/selectWhsDocList"
		})
	});
	//1.仓库
	$(document).on('keypress', '#tranOutWhsEncd', function(event) {
		if(event.keyCode == '13') {
			$('#tranOutWhsEncd').blur();
		}
	})

	$(document).on('blur', '#tranOutWhsEncd', function() {
		var tranOutWhsEncd = $("#tranOutWhsEncd").val();
		dev({
			doc1: $("#tranOutWhsEncd"),
			doc2: $("#tranOutWhsNm"),
			showData: {
				"whsEncd": tranOutWhsEncd
			},
			afunction: function(data) {
				if(tranOutWhsEncd == '') {
					$("#tranOutWhsNm").val('')
				}else {
					if(data.respHead.isSuccess == false) {
						alert("不存在此转出仓库请确认！")
					} else {
						var tranOutWhsNm = data.respBody.list[0].whsNm;
						$("#tranOutWhsNm").val(tranOutWhsNm)
					}					
				}
			},
			url: url + "/mis/whs/whs_doc/selectWhsDocList"
		})
	});
	//2.店铺
	$(document).on('keypress', '#storeId1', function(event) {
		if(event.keyCode == '13') {
			$('#storeId1').blur();
		}
	})

	$(document).on('blur', '#storeId1', function() {
		var storeId1 = $("#storeId1").val();
		dev({
			doc1: $("#storeId1"),
			doc2: $("#storeNm1"),
			showData: {
				"storeId": storeId1
			},
			afunction: function(data) {
				if(storeId1 == '') {
					$("#storeNm1").val('')
				} else {
					if(data.respHead.isSuccess == false) {
						alert("不存在此店铺请确认！")
					} else {
						var storeNm1 = data.respBody.storeName;
						$("#storeNm1").val(storeNm1)
					}
				}
			},
			url: url + "/mis/ec/storeRecord/query"
		})
	});
	//2.1.店铺
	$(document).on('keypress', '#storeId3', function(event) {
		if(event.keyCode == '13') {
			$('#storeId3').blur();
		}
	})

	$(document).on('blur', '#storeId3', function() {
		var storeId3 = $("#storeId3").val();
		dev({
			doc1: $("#storeId3"),
			doc2: $("#storeName"),
			showData: {
				"storeId": storeId3
			},
			afunction: function(data) {
				if(storeId3 == '') {
					$("#storeName").val('')
				}else {
					if(data.respHead.isSuccess == false) {
						alert("不存在此店铺请确认！")
					} else {
						var storeName = data.respBody.storeName;
						$("#storeName").val(storeName)
					}
				}
			},
			url: url + "/mis/ec/storeRecord/query"
		})
	});
})

//刚开始时可点击的按钮
$(function() {
	$(".adit").hide()
})
$(function() {
	$(".upDate").click(function() {
		$(".adit").show()
		$(".adit").css("opacity",1)
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
		
		
	})
	
	//批量修改快递公司
	$(".queding1").click(function() {
		var sum = []
		//获取此行的仓库编码和存货编码
		var ids = $("#platAudit_jqgrids").jqGrid('getGridParam', 'selarrrow');
		for(var i = 0;i < ids.length;i++) {
			//获得行数据
			var rowDatas = $("#platAudit_jqgrids").jqGrid('getRowData', ids[i]);
			sum.push(rowDatas.orderId)
		}
		var orderIds = sum.toString()
		var expressCode = $("select[name='expressEncd2'] option:selected").val()
		if(ids.length == 0) {
			alert("未选择单据!!")
		} else if(expressCode == 0) {
			alert("请选择快递公司")
		} else {
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"orderIds":orderIds,
					"expressCode":expressCode
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/ec/platOrder/batchEditExpress',
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
					$(".adit").hide()
					$(".adit").css("opacity",0)
					$("select[name='expressEncd2']").val(0)
					search_a()
				},
				error: function() {
					console.log(error)
				}
			})
		}
	})
	$(".quxiao1").click(function() {
		$(".adit").hide()
		$(".adit").css("opacity",0)
		$("select[name='expressEncd2']").val(0)
	})
	
	
	//批量修改发货仓库
	$(".queding2").click(function() {
		var sum = []
		//获取此行的仓库编码和存货编码
		var ids = $("#platAudit_jqgrids").jqGrid('getGridParam', 'selarrrow');
		for(var i = 0;i < ids.length;i++) {
			//获得行数据
			var rowDatas = $("#platAudit_jqgrids").jqGrid('getRowData', ids[i]);
			sum.push(rowDatas.orderId)
		}
		var orderIds = sum.toString()
		var whsEncd1 = $("input[name='whsEncd1']").val()
		if(ids.length == 0) {
			alert("未选择单据!!")
		} else if(whsEncd1 == 0) {
			alert("请选择仓库")
		} else {
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"orderIds":orderIds,
					"whsEncd":whsEncd1
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/ec/platOrder/batchEditWhs',
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
					$(".adit").hide()
					$(".adit").css("opacity",0)
					$("select[name='expressEncd2']").val(0)
					search_a()
				},
				error: function() {
					console.log(error)
				}
			})
		}
	})
	$(".quxiao2").click(function() {
		$(".adit").hide()
		$(".adit").css("opacity",0)
		$("select[name='expressEncd2']").val(0)
	})
})

function getDateStr(AddDayCount) {
    var dd = new Date();
    dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
    var y = dd.getFullYear();
    var m = dd.getMonth()+1;//获取当前月份的日期
    var d = dd.getDate();
    return y+'-'+(m<10?'0'+m:m)+'-'+d + ' 00:00:00';
}


$(function() {
	window.dates = getDateStr(-2);
	$(".f_date").val(dates)
	$(".l_date").val(new Date().format("yyyy-MM-dd hh:mm:ss"))
})


$(function() {
	$("#mengban").hide()
	$(".batch").hide()
//	$(".splitOrder").addClass("gray")
//	$(".splitOrder").attr("disabled", true)
	//点击表格图标显示店铺档案列表
	$(document).on('click', '.storeId_biaoge', function() {
		$(".box").hide()
		$(".formSave_box").css("opacity", 1)
		$(".oneSerch_List").hide()
		/*------加载动态下拉电商平台-------*/
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
		$(".box").hide()
		$(".oneSerch_List").css("opacity", 1)
		$(".oneSerch_List").show()
//		$(".oneSerch_List").hide()
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
	$(document).on('click', '.whsEncd1_biaoge', function() {
		localStorage.setItem("outInEncd",1)
		window.open("../../Components/baseDoc/whsList.html", 'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	$(document).on('click', '.expressCom_biaoge', function() {
		window.open("../../Components/whs/express_cropList.html", 'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})

//表格初始化
$(function() {
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
//	allHeight()
	var checkBox=[];
    var cPage=0;
	var rowNum = $("#_input").val()
	$("#platAudit_jqgrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['订单编码','电商订单号','店铺编码','店铺名称','交易时间','付款时间','旗标','是否客审','审核提示',
					'商品数量','商品金额','实付金额','买家留言','卖家备注','买家会员号','收货人姓名','收货人手机号',
					'是否开票','发票抬头','卖家备注旗帜','是否关闭','是否发货','卖家调整金额',
					'系统优惠金额','订单状态','退货状态','是否含赠品','备注','发货时间',
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
				sorttype:'date'
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
//		sortable:true,
		forceFit: true,
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
		ondblClickRow: function(rowid) {
			order(rowid);
		},
		onSelectRow: function() {
			searchAll();
//			$(".splitOrder").removeClass("gray")
//			$(".splitOrder").attr("disabled", false)
		},
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#platAudit_jqgrids").getGridParam('records'); //获取返回的记录数
			page = $("#platAudit_jqgrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#platAudit_jqgrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			search_a()
		},
		footerrow: true,
		gridComplete: function() { 
			var goodNum = $("#platAudit_jqgrids").getCol('goodNum', false, 'sum');
			var goodMoney = $("#platAudit_jqgrids").getCol('goodMoney', false, 'sum');
			var payMoney = $("#platAudit_jqgrids").getCol('payMoney', false, 'sum');
			var adjustMoney = $("#platAudit_jqgrids").getCol('adjustMoney', false, 'sum');
			var discountMoney = $("#platAudit_jqgrids").getCol('discountMoney', false, 'sum');
			$("#platAudit_jqgrids").footerData('set', { 
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
				hidden:true,
				sortable: false
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
		forceFit: true,
		rowNum: 10,
		scrollrows: true,
//		sortable:false,
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
		rownumWidth: 20,  //序列号列宽度
		caption: "操作日志", //表格的标题名字
		pager: '#platList_jqGridPager4', //表格页脚的占位符(一般是div)的id
	})
	
	
	//初始化表格
	jQuery("#split_jqgrids").jqGrid({
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		url: '../../assets/js/json/order.json',
		colNames: ['序号','存货编码','商品名称','数量','拆分数量'
		], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'no',
				align: "center",
				index: 'invdate',
				editable: false
			},
			{
				name: 'invId',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
			},
			{
				name: 'goodName',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
			},
			{
				name: 'invNum',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
			},
			{
				name: 'splitNum',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: true,
				
			}
		],
		autowidth: true,
		sortable:true,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 10,
		cellEdit:true,
		cellsubmit: "clientArray",
		scrollrows: true,
		autoScroll: true,
		height: 300,
		shrinkToFit: false,
		rowList: [10, 20, 30], //可供用户选择一页显示多少条
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		multiboxonly: true,
		rownumWidth: 20,  //序列号列宽度
		caption: "操作日志", //表格的标题名字
		pager: '#split_jqGridPager', //表格页脚的占位符(一般是div)的id
		afterEditCell: function(rowid, cellname, val, iRow, iCol) {
			if(cellname == "splitNum") {
				$("#" + rowid + "_splitNum").attr("type", 'number')
			}
		},
//		离开编辑状态
		afterRestoreCell: function(rowid, cellname, val, iRow, iCol) {
			$(".sures").removeClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true)
			if(cellname == "splitNum") {
				test();
			}
		},
		//回车保存
		afterSaveCell: function(rowid, cellname, val, iRow, iCol) {
			$(".sures").removeClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
		}
	})

})

function test() { 
	//获得行号
	var gr = $("#split_jqgrids").jqGrid('getGridParam', 'selrow');
	
	//获得行数据
	var rowDatas = $("#split_jqgrids").jqGrid('getRowData', gr);
	var splitNum = rowDatas.splitNum
　　if(splitNum == undefined || splitNum == '') {　　　　　　
		alert('请输入拆分数量');　　　　　　
		return false;　　　　
	}
	if(!(/(^[1-9]\d*$)/.test(splitNum))) {
		alert('输入的不是正整数或未回车提交单元格');
		return false;
	} else {
		var gr = $("#platAudit_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDatas = $("#platAudit_jqgrids").jqGrid('getRowData', gr);
	
		var orderId = rowDatas.orderId;
		var grs = $("#split_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDataes = $("#split_jqgrids").jqGrid('getRowData', grs);
	
		var invNum1 = rowDataes.invNum
		var splitNum1 = rowDataes.splitNum;

		var num = []
		var sum = []
		var grs = $("#split_jqgrids").jqGrid('getGridParam', 'selarrrow');
		for(var i = 0; i < grs.length; i++) {
			//获得行数据
			var rowData = $("#split_jqgrids").jqGrid('getRowData', grs[i]);
			num.push(rowData.no)
			sum.push(rowData.splitNum)
		}
		var myData = {};
		
		if(grs.length == 0) {
			alert("请选择要拆分的单据")
		} else if(parseInt(splitNum1) > parseInt(invNum1)) {
			alert("拆单失败，拆单数量大于或等于订单内商品数量")
		} else {
			var Data = {
				"reqHead": reqhead,
				"reqBody": {
					"orderId": orderId,
					"platOrdersIds": num.toString(),
					"splitNum": sum.toString(),
				}
			};
			var saveData = JSON.stringify(Data);
			$.ajax({
				type: "post",
				url: url + "/mis/ec/platOrder/splitOrder",
				async: true,
				data: saveData,
				dataType: 'json',
				contentType: 'application/json;charset=utf-8',
				success: function(data) {
					console.log(data)
					alert(data.respHead.message)
				},
				error: function(error) {
					console.log(error)
				}
			});
		}
	}
}
//	手动拆单页面
$(document).on('click', '.splitOrder', function() {
	var gr = $("#platAudit_jqgrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowDatas = $("#platAudit_jqgrids").jqGrid('getRowData', gr);
	var myData = {};
	if(gr != null) {
		$(".box").hide()
		$(".down").hide()
		$(".oneSerch_List").hide()
		$(".formSave_box").hide()
		$(".oneSerch_List").css("opacity", 0)
		$(".hand_split").css("opacity", 1)
		$(".hand_split").show()
		window.orderId = rowDatas.orderId;
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
				myDate = list;
				$("#split_jqgrids").jqGrid('clearGridData');
				$("#split_jqgrids").jqGrid('setGridParam', {
					//				rowNum:rowNum,
					datatype: 'local',
					data: myDate, //newData是符合格式要求的重新加载的数据
					page: 1 //哪一页的值
				}).trigger("reloadGrid")
				
			},
			error: function() {
				console.log(error)
			}
		});
	} else {
		alert("请选择单据")
	}
	
});
var num_order;
//	商品调整页面
$(document).on('click', '.selectecGooId1', function() {
	var gr = $("#platAudit_jqgrids").jqGrid('getGridParam', 'selarrrow');
	var num = []
	for(var i = 0; i < gr.length; i++) {
		//获得行数据
		var rowDatas = $("#platAudit_jqgrids").jqGrid('getRowData', gr[i]);
		var orderId = rowDatas.orderId
		num.push(orderId)
	}
	num_order = num.toString()
	if(num.length != 0) {
		$(".box").hide()
		$(".down").hide()
		$(".oneSerch_List").hide()
		$(".formSave_box").hide()
		$(".oneSerch_List").css("opacity", 0)
		$(".hand_split").css("opacity", 0)
		$(".hand_split").hide()
		$(".selectecGooId").show()
		$(".selectecGooId").css("opacity", 1)
		
		var Data = {
			"reqHead": reqhead,
			"reqBody": {
				"orderId": num.toString()
//				"orderId": "ec111201906100000002,ec111201906100000003,ec111201906100000004,ec111201906100000005,ec111201906100000006"
			}
		};
		var saveData = JSON.stringify(Data);
		console.log(saveData)
		$.ajax({
			type: "post",
			url: url + "/mis/ec/platOrder/selectecGooId",
			async: true,
			data: saveData,
			dataType: 'json',
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
				console.log(data)
				var list = data.respBody.list;
				myDate = list;
				$("#selectecGooId_jqgrids").jqGrid('clearGridData');
				$("#selectecGooId_jqgrids").jqGrid('setGridParam', {
					datatype: 'local',
					data: myDate, //newData是符合格式要求的重新加载的数据
					page: 1 //哪一页的值
				}).trigger("reloadGrid")
				
			},
			error: function() {
				console.log(error)
			}
		});
	} else {
		alert("请选择单据")
	}
	
});
function getJQAllData7() {
	//拿到grid对象
	var obj = $("#selectecGooId_jqgrids");
	//获取grid表中所有的rowid值
	var rowIds = obj.getDataIDs();
	//初始化一个数组arrayData容器，用来存放rowData
	var arrayData = new Array();
	if(rowIds.length > 0) {
		for(var i = 0; i < rowIds.length; i++) {
			if(obj.getRowData(rowIds[i]).invIdLast == "") {
				continue;
			} else if(obj.getRowData(rowIds[i]).invIdLast != "") {
				arrayData.push(obj.getRowData(rowIds[i]));
			}
		}
	}
	return arrayData;
}

$(document).on('click', ".sures_s", function() {
	var list2 = getJQAllData7()
	if(list2.length > 1) {
		alert("只能修改一条商品")
	} else {
		for(var i =0;i<list2.length;i++) {
			var invId = list2[0].invId
			var invIdLast = list2[0].invIdLast
			var multiple = list2[0].multiple
		}
		var inv = invIdLast.indexOf("<input",0)
		if(inv != -1 || multiple == '') {
			alert('未回车提交单元格');
			return false;
		}else {
			var savedata = {
				"reqHead": reqhead,
				"reqBody": {
					"invId": invId,
					"invIdLast": invIdLast,
					"multiple": multiple,
					"orderIds":num_order
				}
			};
			var saveData = JSON.stringify(savedata);
			$.ajax({
				type: "post",
				contentType: 'application/json; charset=utf-8',
				url: url + '/mis/ec/platOrder/updateecGooId',
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
					console.log(data)
					alert(data.respHead.message)
					if(data.respHead.isSuccess == true) {
						$(".down").hide()
						$(".oneSerch_List").hide()
						$(".formSave_box").hide()
						$(".oneSerch_List").css("opacity", 0)
						$(".hand_split").css("opacity", 0)
						$(".hand_split").hide()
						$(".selectecGooId").hide()
						$(".selectecGooId").css("opacity", 0)
						$(".box").show()
						$(".box").css("opacity", 1)
					}
				},
				error: function() {
					console.log(error)
				}
			});
		}
	}
	
})

$(document).on('click', ".over_s", function() {
	$(".box").show()
	$(".down").hide()
	$(".oneSerch_List").hide()
	$(".formSave_box").hide()
	$(".oneSerch_List").css("opacity", 0)
	$(".hand_split").css("opacity", 0)
	$(".hand_split").hide()
	$(".selectecGooId").hide()
	$(".selectecGooId").css("opacity", 0)
	
})
$(document).on('click', ".over", function() {
	$(".box").show()
	$(".down").hide()
	$(".oneSerch_List").hide()
	$(".formSave_box").hide()
	$(".oneSerch_List").css("opacity", 0)
	$(".hand_split").css("opacity", 0)
	$(".hand_split").hide()
})

$(document).on('click', ".sures", function() {
	test()
})

$(document).on('click', "li[data-id='tabContent3']", function() {
	var gr = $("#platAudit_jqgrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowDatas = $("#platAudit_jqgrids").jqGrid('getRowData', gr);
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
	var gr = $("#platAudit_jqgrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowDatas = $("#platAudit_jqgrids").jqGrid('getRowData', gr);
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
	var gr = $("#platAudit_jqgrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowDatas = $("#platAudit_jqgrids").jqGrid('getRowData', gr);
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
				$("input[name='expressCom2']").val(fData.expressCom);
				$("input[name='expressCode2']").val(fData.expressCode);
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
			error: function() {
				console.log(error)
			}
		});
	}
}

$(document).on('click', '.search', function() {
	search_a()
})

//查询按钮
function search_a() {
	var myDate = {};

	var orderId = $("input[name='orderId1']").val();
	var storeId = $("input[name='storeId1']").val();
//	var isAudit = $("select[name='isAudit1']").val();
	var buyerId = $("input[name='buyerId']").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var recName = $("input[name='recName']").val();
	var memo = $("input[name='memo']").val();
	var ecOrderId = $("input[name='ecOrderId']").val();
	var startDate = $("input[name='startDate']").val();
	var endDate = $("input[name='endDate']").val();
	var deliverWhs = $("input[name='deliverWhs']").val();
	var expressCom = $("input[name='expressCom']").val();
	var isShip = $("select[name='isShip']").val();
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	

	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"orderId": orderId,
			"storeId": storeId,
			"isAudit": "0",
			"buyerId": buyerId,
			"recName": recName,
			"memo": memo,
			"invtyEncd": invtyEncd,
			"ecOrderId": ecOrderId,
			"startDate": startDate,
			"endDate": endDate,
			"deliverWhs":deliverWhs,
			"expressCom":expressCom,
			"isShip": isShip,
			"isClose": "0",
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
		//结束加载动画
		complete: function() {
			$(".zhezhao").css("display", "none");
			$("#loader").css("display", "none");
		},
		success: function(data) {
			$(".toExamine").removeClass("gray")
			$(".toExamine").attr("disabled",false)
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
			$("#platAudit_jqgrids").jqGrid("clearGridData");
			$("#platAudit_jqgrids").jqGrid("setGridParam", {
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
			console.log(error)
		}
	});
}
//
$(function() {
	$(document).on('click', '.autoMatch', function() {
		var num = []
		//获得行号
		var gr = $("#platAudit_jqgrids").jqGrid('getGridParam', 'selarrrow');
		for(var i = 0;i <gr.length;i++) {
			var rowDatas = $("#platAudit_jqgrids").jqGrid('getRowData', gr[i]);
			var orderId = rowDatas.orderId
			num.push(orderId)
		}
		var nums = num.toString()
		if(gr.length != 0) {
			var savedata = {
				"reqHead": reqhead,
				"reqBody": {
					"orderIds": nums
				}
			};
			var saveData = JSON.stringify(savedata);
			$.ajax({
				type: "post",
				contentType: 'application/json; charset=utf-8',
				url: url + '/mis/ec/platOrder/autoMatch',
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
})
//
function order(rowid) {
	//获得行数据
	//debugger
	var rowDatas = $("#platAudit_jqgrids").jqGrid('getRowData', rowid);
	//存换从传入的是orderId
	localStorage.setItem("orderId", rowDatas.orderId);
	window.open("../../Components/ec/platOrderDet.html?1");

}

$(function() {
	//确定
	$(".sure").click(function() {
		//到货单
		//获得行号
		var rowid = $("#platAudit_jqgrids").jqGrid('getGridParam', 'selrow');

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
//		var rowid = $("#platAudit_jqgrids").jqGrid('getGridParam', 'selrow');
//		
//		$("#" + rowid + "_whsNm").val("");
//		$("#" + rowid + "_whsEncd").val("");
//		$("#" + rowid + "_invtyEncd").val("")
	})
})

//下载
$(function() {
	$('.download').click(function() {
		$(".down").css("opacity", 1)
		$(".down").show()
		$("#mengban").show()
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
	})

	$(".true").click(function() {
		$("#mengban").attr("z-index", "4");
		var storeId = $("input[name='storeId3']").val()
		if(storeId != '') {
			var orderId1 = $("input[name='orderId3']").val()
			if(orderId1 == '') {
//				alert("无订单号按照店铺编码下载")
				var storeId1 = $("input[name='storeId3']").val()
				var startDate = $("input[name='startDate1']").val()
				var endDate = $("input[name='endDate1']").val()

				var download = {
					"reqHead": reqhead,
					"reqBody": {
						'storeId': storeId1,
						'startDate': startDate,
						'endDate': endDate,
					}
				};
				var downloadData = JSON.stringify(download)
				$.ajax({
					type: "post",
					url: url3 + "/mis/ec/platOrder/download",
					async: true,
					data: downloadData,
					dataType: 'json',
					contentType: 'application/json',
					//开始加载动画  添加到ajax里面
					beforeSend: function() {
						$(".zhezhao").css("display", "block");
						$("#loader").css("display", "block");
			
					},
					success: function(msgAdd) {
						$("#mengban").attr("z-index");
						
						alert(msgAdd.respHead.message)
						//				window.location.reload();
						$("#mengban").hide()
						$(".down").css("opacity", 0)
						$(".down").hide()
						$("input[name='storeId3']").val('')
						$("input[name='startDate1']").val('')
						$("input[name='endDate1']").val('')
					},
					//结束加载动画
					complete: function() {
						$(".zhezhao").css("display", "none");
						$("#loader").css("display", "none");
					},
					error: function() {
						console.log("下载失败")

					}
				});
			} else if(orderId1 != '') {
//				alert("订单号非空按照订单号下载")
				var orderId1 = $("input[name='orderId3']").val()
				var storeId = $("input[name='storeId3']").val()
				var download = {
					"reqHead": reqhead,
					"reqBody": {
						'storeId':storeId,
						'orderId': orderId1,
					}
				};
				var downloadData = JSON.stringify(download)
				$.ajax({
					type: "post",
					url: url3 + "/mis/ec/platOrder/downloadByOrderId",
					async: true,
					data: downloadData,
					dataType: 'json',
					contentType: 'application/json',
					success: function(msgAdd) {
						alert(msgAdd.respHead.message)
						//				window.location.reload();
						$("#mengban").hide()
						$(".down").css("opacity", 0)
						$(".down").hide()
						$("input[name='orderId3']").val('')
						$("input[name='storeId3']").val('')
						$("input[name='startDate1']").val(dates)
						$("input[name='endDate1']").val(new Date().format("yyyy-MM-dd hh:mm:ss"))
					},
					error: function() {
						console.log("下载失败")

					}
				});
			}
		}
		else if(storeId == '') {
			alert("请选择店铺")
		}
	})


	$(".falses").click(function() {
		$("#mengban").hide()
		$(".down").css("opacity", 0)
		$(".down").hide()
		$("input[name='orderId1']").val('')
		$("input[name='storeId']").val('')
		$("input[name='startDate1']").val(dates)
		$("input[name='endDate1']").val(new Date().format("yyyy-MM-dd hh:mm:ss"))
	})
})

var isclick = true;
$(function() {
	//审核
	$(".toExamine").click(function() {
		if(isclick) {
			isclick = false;
			ntChk();
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	});
})
function ntChk() {
	var sum = []
	//获取此行的仓库编码和存货编码
	var ids = $("#platAudit_jqgrids").jqGrid('getGridParam', 'selarrrow');
	for(var i = 0;i < ids.length;i++) {
		//获得行数据
		var rowDatas = $("#platAudit_jqgrids").jqGrid('getRowData', ids[i]);
		sum.push(rowDatas.orderId)
	}
	var orderId = sum.toString()
	if(sum.length == 0) {
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
			url: url + '/mis/ec/associatedSearch/orderAuditByOrderId',
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
				$(".toExamine").addClass("gray")
				$(".toExamine").attr('disabled', true);
				search_a()
				
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
	//订单关闭
	$(".up_d").click(function() {
		var sum = []
		//获取此行的仓库编码和存货编码
		var ids = $("#platAudit_jqgrids").jqGrid('getGridParam', 'selarrrow');
		for(var i = 0;i < ids.length;i++) {
			//获得行数据
			var rowDatas = $("#platAudit_jqgrids").jqGrid('getRowData', ids[i]);
			sum.push(rowDatas.orderId)
		}
		var orderId = sum.toString()
		if(sum.length == 0) {
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
				url: url + '/mis/ec/platOrder/closeOrder',
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
					search_a()
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
		var ids = $("#platAudit_jqgrids").jqGrid('getGridParam', 'selarrrow');
		//获取选择行的provrId
		var rowDatas = [];
		for(var i = 0; i < ids.length; i++) {
//			var ids = $("#platAudit_jqgrids").jqGrid('getGridParam', 'selrow');
				//获得行数据
			var rowData = $("#platAudit_jqgrids").jqGrid('getRowData', ids[i]);
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
					alert(data.respHead.message)
					search_a()
				},
				error: function() {
					console.log(error)
				}
			})
		}
	})
})
$(function() {
	//确定
	$(".addWhs").click(function() {

		//	获得行号
		var rowid = $("#selectecGooId_jqgrids").jqGrid('getGridParam', 'selrow');
		//	获得行数据
//		var rowDatas = $("#selectecGooId_jqgrids").jqGrid('getRowData', gr);

		//	存货档案
		//	获得行号
		var ids = $("#insert_jqgrids").jqGrid('getGridParam', 'selrow');
		//	获得行数据
		var rowData = $("#insert_jqgrids").jqGrid('getRowData', ids);

		$("#" + rowid + "_invIdLast").val(rowData.invtyEncd)
//		$("#selectecGooId_jqgrids").setRowData(rowid, {
//			whsNm: rowDatas.whsNm
//		})

		//		动态设置批次可编辑
		$("#jqgrids").setColProp("batNum", {
			editable: true
		});

		$("#insertList").hide();
		$("#insertList").css("opacity", 0);
		$(".selectecGooId").show();
	})
	//	取消
	$(".cancel").click(function() {
		$(".box").hide()
		$(".down").hide()
		$("#insertList").hide()
		$(".oneSerch_List").hide()
		$(".formSave_box").hide()
		$(".oneSerch_List").css("opacity", 0)
		$(".hand_split").css("opacity", 0)
		$(".hand_split").hide()
		$(".selectecGooId").show()
		$(".selectecGooId").css("opacity", 1)
		//	获得行号
		var rowid = $("#selectecGooId_jqgrids").jqGrid('getGridParam', 'selrow');
		//到货单
		//获得行号
		$("#" + rowid + "_invIdLast").val('')
	})
})
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
				"isAudit": "0",
				"isClose":"0"
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
				$(".toExamine").removeClass("gray")
				$(".toExamine").attr("disabled",false)
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
				$("#platAudit_jqgrids").jqGrid("clearGridData");
				$("#platAudit_jqgrids").jqGrid("setGridParam", {
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

//导入
$(function () {
    $(".importExcel").click(function () {
    	var loginName = localStorage.loginAccNum
    	var files = $("#FileUpload").val()
    	var fileObj = document.getElementById("FileUpload").files[0];
        var formFile = new FormData();
       	formFile.append("action", "UploadVMKImagePath");
       	formFile.append("accNum", loginName);  
       	formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
        var data = formFile;
        if(files != "") {
        	$.ajax({
	            type: 'post',
	            url: url + "/mis/ec/platOrder/importPlatOrder",
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