var count;
var pages;
var page = 1;
var rowNum;

$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	//点击表格图标显示存货分类列表
	$(document).on('click', '.momKitNm', function() {
		window.open("../../Components/baseDoc/outIntoWhs.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})

//右列表格的操作
$(function() {
	pageInit();
})

//页面初始化
function pageInit() {
	allHeight()
	jQuery("#jqgrids").jqGrid({
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		url: '../../assets/js/json/order.json',
		colNames: ['编码', '名称', '编码', '名称','重量', '箱规', '批次', '数量', '单价',
			'金额', '数量', '单价', '金额', '数量', '单价', '金额', '数量',
			'单价', '金额', '级别'
		], //jqGrid的列显示名字
		colModel: [{
				name: 'whsEncd',
				align: "center",
				editable: true,
			},
			{
				name: "whsNm",
				align: "center",
				editable: true,
			},
			{
				name: "invtyEncd",
				align: "center",
				editable: true,
			},
			{
				name: "invtyNm",
				align: "center",
				editable: true,
			},
			{
				name: 'weight',
				align: "center",
				editable: true,
				sorttype:"int"
			},
			{
				name: 'bxRule',
				align: "center",
				editable: true,
				sorttype:"int"
			},
			{
				name: 'batNum',
				align: "center",
				editable: true,
				sortable: false,
				hidden: true
			},
			{
				name: 'qty',
				align: "center",
				editable: true,
				sorttype:"int"
			},
			{
				name: "uprc",
				align: "center",
				editable: true,
				sorttype:"int"
			},
			{
				name: "amt",
				align: "center",
				editable: true,
				sorttype:"int"
			},
			{
				name: "inQty", //收入
				align: "center",
				editable: true,
				sorttype:"int"
			},
			{
				name: 'inUnitPrice',
				align: "center",
				editable: true,
				sorttype:"int"
			},
			{
				name: 'inMoeny',
				align: "center",
				editable: true,
				sorttype:"int"
			},
			{
				name: 'sendQty', //发出
				align: "center",
				editable: true,
				sorttype:"int"
			},
			{
				name: 'sendUnitPrice', //主计量单位
				align: "center",
				editable: true,
				sorttype:"int"
			},
			{
				name: 'sendMoeny',
				align: "center",
				editable: true,
				sorttype:"int"
			},
			{
				name: 'othQty', //结存
				align: "center",
				editable: true,
				sorttype:"int"
			},
			{
				name: "othUnitPrice",
				align: "center",
				editable: true,
				sorttype:"int"
			},
			{
				name: "othMoeny",
				align: "center",
				editable: true,
				sorttype:"int"
			},
			{
				name: "level",
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			}
		],
		autowidth: true,
		height: height,
		autoScroll: true,
		shrinkToFit: false,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 9999,
		footerrow: true,
//		rowList: [10, 20, 30], //可供用户选择一页显示多少条
//		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "收发存汇总表", //表格的标题名字
		//cellEdit:true, //单元格是否可编辑
		gridComplete: function() { 
			var qtys = 0;
			var amts = 0;
			var inqtys = 0;
			var inamts = 0;
			var outqtys = 0;
			
			var outamts = 0;
			var othqtys = 0;
			var othamts = 0;
			var ids = $("#jqgrids").getDataIDs();
			for(var i = 0; i < ids.length; i++) {
				var rowData = $("#jqgrids").getRowData(ids[i]);
				if(rowData.level == 1) {
					$('#' + ids[i]).find("td").css("background-color", "#C1E2B3");
				};
				if(rowData.level == 3) {
					qtys += parseFloat(rowData.qty)
					amts += parseFloat(rowData.amt)
					inqtys += parseFloat(rowData.inQty)
					inamts += parseFloat(rowData.inMoeny)
					outqtys += parseFloat(rowData.sendQty)
					outamts += parseFloat(rowData.sendMoeny)
					othqtys += parseFloat(rowData.othQty)
					othamts += parseFloat(rowData.othMoeny)
				};
				if(rowData.level == 2) {
					$('#' + ids[i]).find("td").css("background-color", "#A4E9C1");
				}
			};
			amts = amts.toFixed(2);
			inamts = inamts.toFixed(2);
			outamts = outamts.toFixed(2);
			othamts = othamts.toFixed(2);
			$("#jqgrids").footerData('set', { 
				"whsEncd": "合计",
				qty: qtys,
				amt:amts,
				inQty: inqtys,
				inMoeny:inamts,
				sendQty: outqtys,
				sendMoeny:outamts,
				othQty: othqtys,
				othMoeny:othamts,
				
			}          );    
		},
		ondblClickRow: function(rowid) {
			order(rowid);
		},
	});
	jQuery("#jqgrids").jqGrid('setGroupHeaders', {
		useColSpanStyle: false,
		groupHeaders: [{
				startColumnName: 'whsEncd',
				numberOfColumns: 2,
				titleText: '<em>仓库</em>'
			},
			{
				startColumnName: 'invtyEncd',
				numberOfColumns: 4,
				titleText: '<em>存货</em>'
			}, {
				startColumnName: 'qty',
				numberOfColumns: 3,
				titleText: '<em>期初</em>'
			},
			{
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
}


function order(rowid) {
	//获得行数据
	//debugger
	var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
	var formSDt = $("input[name='formSDt']").val()
	var formEDt = $("input[name='formEDt']").val()
	//存换从传入的是orderId
	localStorage.setItem("sub_whsEncd", rowDatas.whsEncd);
	localStorage.setItem("sub_invtyEncd", rowDatas.invtyEncd);
	localStorage.setItem("sub_batNum", rowDatas.batNum);
	localStorage.setItem("sub_bookOkSDt", formSDt);
	localStorage.setItem("sub_bookOkEDt", formEDt);
	window.open("../../Components/account/subsidiaryLedger.html?1");

}


//查询按钮
$(function() {
	$(".find").click(function() {
		loadLocalData()
	})
})
function loadLocalData() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var poolType = $("#poolType").val();
	var formSDt = $(".formSDt").val();
	var formEDt = $(".formEDt").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var invtyEncd = $("#invtyEncd").val();
	var whsEncd = $("#whsEncd").val();
	var batNum = $("#batNum").val();
	if(poolType == "1") {
		jQuery("#jqgrids").setGridParam().hideCol("batNum").trigger("reloadGrid");
	} else if(poolType == "2") {
		jQuery("#jqgrids").setGridParam().showCol("batNum").trigger("reloadGrid");
	}
	if(poolType == null || poolType == "") {
		alert("请选择汇总方式")
	} else {
		var myData = {};
		var data2 = {
			"reqHead": reqHead,
			"reqBody": {
				'poolType': poolType,
				'formSDt': formSDt,
				"invtyClsEncd": invtyClsEncd,
				'formEDt': formEDt,
				'invtyEncd': invtyEncd,
				'whsEncd': whsEncd,
				'batNum': batNum,
				"pageNo": 1,
				"pageSize": 9999
			}
		};
		var postD2 = JSON.stringify(data2);
		$.ajax({
			type: "post",
			url: url + "/mis/account/invty/sendAndReceiveInvtyCls/pool",
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
				var list = data.respBody.list;
				if(list != null) {
					for(var i = 0; i < list.length; i++) {
						$.extend(list[i], list[i].invtyDetailsList[0])
						delete list[i].invtyDetailsList
					}
					$("#jqgrids").jqGrid('clearGridData');
					$("#jqgrids").jqGrid('setGridParam', {
						datatype: 'local',
						data: list, //newData是符合格式要求的重新加载的数据
						page: 1 //哪一页的值
					}).trigger("reloadGrid")
				} else if(list == null) {
					alert("数据为空")
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
	var poolType = $("#poolType").val();
	var formSDt = $(".formSDt").val();
	var formEDt = $(".formEDt").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var invtyEncd = $("#invtyEncd").val();
	var whsEncd = $("#whsEncd").val();
	var batNum = $("#batNum").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			'poolType': poolType,
			'formSDt': formSDt,
			"invtyClsEncd": invtyClsEncd,
			'formEDt': formEDt,
			'invtyEncd': invtyEncd,
			'whsEncd': whsEncd,
			'batNum': batNum,
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/account/invty/sendAndReceiveInvtyCls/poolExport',
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
			var execlName = '收发存汇总表'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
})
