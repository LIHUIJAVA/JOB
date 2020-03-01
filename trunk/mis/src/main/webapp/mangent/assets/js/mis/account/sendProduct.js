//发出商品明细账
$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
})
$(function() {
	allHeight()
	jQuery("#duct_jqGrids").jqGrid({
		url: '../../assets/js/json/order.json', //组件创建完成之后请求数据的url
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['记账日期', '单据号','凭证编码', '凭证摘要',
			'单据类型编码', '单据类型', '数量', '单价', '金额', '数量',
			'单价', '金额', '数量', '单价', '金额','序号'
		], //jqGrid的列显示名字
		colModel: [{
				name: "bookOkDt",
				align: "center",
				editable: true,
			},
			{
				name: "formNum",
				align: "center",
				editable: true,
			},
			{
				name: "makeVouchId",
				align: "center",
				editable: true,
			},
			{
				name: 'makeVouchAbst',
				align: "center",
				editable: true,
			},
			{
				name: 'recvSendCateId',
				align: "center",
				editable: true,
			},
			{
				name: 'recvSendCateNm',
				align: "center",
				editable: true,
			},
			{
				name: 'inQty',
				align: "center",
				editable: true,
			},
			{
				name: 'inUnitPrice',
				align: "center",
				editable: true,
			},
			{
				name: 'inMoeny',
				align: "center",
				editable: true,
			},
			{
				name: 'sendQty',
				align: "center",
				editable: true,
			},
			{
				name: 'sendUnitPrice',
				align: "center",
				editable: true,
			},
			{
				name: 'sendMoeny',
				align: "center",
				editable: true,
			},
			{
				name: 'othQty',
				align: "center",
				editable: true,
			},
			{
				name: 'othUnitPrice',
				align: "center",
				editable: true,
			},
			{
				name: 'othMoeny',
				align: "center",
				editable: true,
			},
			{
				name: 'detailId',
				align: "center",
				editable: true,
				hidden:true
			}
		],
		autowidth: true,
		viewrecords: true,
		autoScroll: true,
		shrinkToFit: false,
		rownumbers: true,
		height:height - 30,
		loadonce: true,
		forceFit: true,
		rowNum: 500,
		footerrow: true,
		rowList: [500, 1000, 3000, 5000],
		pager: '#jqgridPager', //表格页脚的占位符(一般是div)的id
//		multiboxonly: true,
		multiselect: true, //复选框
		caption: "发出商品明细账", //表格的标题名字
		//双击弹出采购订单
		ondblClickRow: function(rowid) {
			order(rowid);
		},
		gridComplete: function() { 
			var qtys = 0;
			var amts = 0;
			var inqtys = 0;
			var inamts = 0;
			var outqtys = 0;
			var outamts = 0;
			var othqtys = 0;
			var othamts = 0;
			var ids = $("#duct_jqGrids").getDataIDs();
			for(var i = 0; i < ids.length; i++) {
				var rowData = $("#duct_jqGrids").getRowData(ids[i]);
				if(rowData.detailId == -1) {
					qtys += parseFloat($("#duct_jqGrids").getCell(ids[i], "qty"));
					amts += parseFloat($("#duct_jqGrids").getCell(ids[i], "amt"));
					inqtys += parseFloat($("#duct_jqGrids").getCell(ids[i], "inQty"));
					inamts += parseFloat($("#duct_jqGrids").getCell(ids[i], "inMoeny"));
					outqtys += parseFloat($("#duct_jqGrids").getCell(ids[i], "sendQty"));
					outamts += parseFloat($("#duct_jqGrids").getCell(ids[i], "sendMoeny"));
					othqtys += parseFloat($("#duct_jqGrids").getCell(ids[i], "othQty"));
					othamts += parseFloat($("#duct_jqGrids").getCell(ids[i], "othMoeny"));
				}
			};   
			if(isNaN(qtys)) {
				qtys = 0
			}
			if(isNaN(amts)) {
				amts = 0
			}
			amts = precision(amts,2)
			if(isNaN(inqtys)) {
				inqtys = 0
			}
			if(isNaN(inamts)) {
				inamts = 0
			}
			inamts = precision(inamts,2)
			if(isNaN(outqtys)) {
				outqtys = 0
			}

			if(isNaN(outamts)) {
				outamts = 0
			}
			outamts = precision(outamts,2)
			if(isNaN(othqtys)) {
				othqtys = 0
			}
			if(isNaN(othamts)) {
				othamts = 0
			}
			othamts = precision(othamts,2)
			$("#duct_jqGrids").footerData('set', { 
				"bookOkDt": "本页合计",
				qty: qtys,
				amt: amts,
				inQty: inqtys,
				inMoeny: inamts,
				sendQty: outqtys,
				sendMoeny: outamts,
				othQty: othqtys,
				othMoeny: othamts,

			}          );    
		},
	});
	jQuery("#duct_jqGrids").jqGrid('setGroupHeaders', {
		useColSpanStyle: false,
		groupHeaders: [{
				startColumnName: 'inQty',
				numberOfColumns: 3,
				titleText: '<em>收入</em>'
			},
			{
				startColumnName: 'sendQty',
				numberOfColumns: 3,
				titleText: '<em>发出</em>'
			},
			{
				startColumnName: 'othQty',
				numberOfColumns: 3,
				titleText: '<em>结存</em>'
			}
		]
	});
})

function order(rowid) {
	//获得行数据
	var rowDatas = $("#duct_jqGrids").jqGrid('getRowData', rowid);
	localStorage.setItem("outWhsId", rowDatas.formNum);
	window.open("../../Components/sell/sellOutWhs.html?2",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
}

//条件查询
$(function() {
	$('#find').click(function() {
		search()
	})
})

function search() {
	var whsEncd = $("#whsEncd").val();
	var invtyEncd = $("#invtyEncd").val();
	var invtyClsEncd = $("#invtyCls").val();
	var batNum = $("#batNum").val();
	var bookOkSDt = $("#bookOkSDt").val();
	var bookOkEDt = $("#bookOkEDt").val();
	if(whsEncd == "") {
		alert("请输入仓库编码")
	} else if(invtyEncd == "") {
		alert("请输入存货编码")
	} else if(batNum == "") {
		alert("请输入批次")
	} else if(bookOkSDt == "" || bookOkEDt == "") {
		alert("请输入日期")
	} else {
		var data2 = {
			"reqHead": reqhead,
			"reqBody": {
				"whsEncd": whsEncd,
				"invtyEncd": invtyEncd,
				"invtyClsEncd": invtyClsEncd,
				"batNum": batNum,
				"bookOkSDt": bookOkSDt,
				"bookOkEDt": bookOkEDt
			}
		};
		var postD2 = JSON.stringify(data2);
		$.ajax({
			type: "post",
			url: url + "/mis/account/invty/sendProduct/list",
			async: true,
			data: postD2,
			dataType: 'json',
			contentType: 'application/json; charset=utf-8',
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
				var spcModel = data.respBody.spcModel;
				var invtyClsEncd = data.respBody.invtyClsEncd;
				var invtyClsNm = data.respBody.invtyClsNm;
				var measrCorpNm = data.respBody.measrCorpNm;
				if(spcModel != null) {
					$("#spcModel").val(data.respBody.spcModel);
				}
				if(invtyClsEncd != "") {
					$("#invtyCls").val(data.respBody.invtyClsEncd);
					$("#invtyClsNm").val(data.respBody.invtyClsNm)
				}
				if(measrCorpNm != null) {
					$("#measrCorpNm").val(data.respBody.measrCorpNm);
				}
				var list = data.respBody.invtyDetailsList;
				if(list) {
					$("#duct_jqGrids").jqGrid('clearGridData');
					$("#duct_jqGrids").jqGrid('setGridParam', {
						datatype: 'local',
						data: list, //newData是符合格式要求的重新加载的数据
						page: 1 //哪一页的值
					}).trigger("reloadGrid")
				} else {
					alert("查询数据为空")
				}
			},
			error: function() {
				alert("条件查询失败")
			}
		});
	}
}

//导出
$(document).on('click', '.exportExcel', function() {
	var whsEncd = $("#whsEncd").val();
	var invtyEncd = $("#invtyEncd").val();
	var invtyClsEncd = $("#invtyCls").val();
	var batNum = $("#batNum").val();
	var bookOkSDt = $("#bookOkSDt").val();
	var bookOkEDt = $("#bookOkEDt").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"whsEncd": whsEncd,
			"invtyEncd": invtyEncd,
			"invtyClsEncd": invtyClsEncd,
			"batNum": batNum,
			"bookOkSDt": bookOkSDt,
			"bookOkEDt": bookOkEDt
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/account/invty/sendProduct/listExport',
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
			var execlName = '发出商品明细账'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
})
