//明细账
function IsCheckValue(invtyEncd, batNum, whsEncd, bookOkSDt, bookOkEDt) {
	if(invtyEncd == "") {
		alert("存货编码不能为空")
		return false;
	} else if(batNum == "") {
		alert("批次不能为空")
		return false;
	} else if(whsEncd == "") {
		alert("仓库编码不能为空")
		return false;
	} else if(bookOkSDt == "") {
		alert("记账起始日期不能为空")
		return false;
	} else if(bookOkEDt == "") {
		alert("记账结束日期不能为空")
		return false;
	}
	return true;
}

//表格初始化
$(function() {
	allHeight()
	$("#sub_jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['记账日期', '凭证号', '凭证摘要', '收发类别ID', '收发类别', '数量', '金额', '单价',
			'数量', '金额', '单价', '数量', '金额', '单价', '单据号','合计'
		],
		colModel: [{
				name: 'bookOkDt',
				editable: false,
				align: 'center',
				sorttype: "date"
			},
			{
				name: 'makeVouchId',
				editable: false,
				align: 'center'
			},
			{
				name: 'makeVouchAbst',
				editable: false,
				align: 'center'
			},
			{
				name: 'recvSendCateId',
				editable: false,
				align: 'center'
			},
			{
				name: 'recvSendCateNm',
				editable: false,
				align: 'center'
			},
			{
				name: 'inQty',
				editable: false,
				align: 'center',
				sorttype: "integer"
			},
			{
				name: 'inMoeny',
				editable: false,
				align: 'center',
				sorttype: "integer"
			},
			{
				name: 'inUnitPrice',
				editable: false,
				align: 'center',
				sorttype: "integer"
			},
			{
				name: 'sendQty',
				editable: false,
				align: 'center',
				sorttype: "integer"
			},
			{
				name: 'sendMoeny',
				editable: false,
				align: 'center',
				sorttype: "integer"
			},
			{
				name: 'sendUnitPrice',
				editable: false,
				align: 'center',
				sorttype: "integer"
			},
			{
				name: 'othQty',
				editable: false,
				align: 'center',
				sorttype: "integer"
			},
			{
				name: 'othMoeny',
				editable: false,
				align: 'center',
				sorttype: "integer"
			},
			{
				name: 'othUnitPrice',
				editable: false,
				align: 'center',
				sorttype: "integer"
			},
			{
				name: 'formNum',
				editable: false,
				align: 'center',
				sorttype: "integer"
			},
			{
				name: 'detailId',
				editable: false,
				align: 'center',
				sorttype: "integer",
				hidden:true
			}
		],
		sortable: true,
		autowidth: true,
		autoScroll: true,
		shrinkToFit: false,
		rownumbers: true,
		loadonce: true,
		height: height - 30,
		footerrow: true,
		rowNum: 99999, // 每页多少行，用于分页
		forceFit: true,
		pager: '#jqgridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
//		multiboxonly: true,
		caption: "明细账", //表格的标题名字
		gridComplete: function() {     
			var inqtys = 0;
			var inamts = 0;
			var outqtys = 0;
			var outamts = 0;
			var othqtys = 0;
			var othamts = 0;
			var ids = $("#sub_jqGrids").getDataIDs();
			for(var i = 0; i < ids.length; i++) {
				var rowData = $("#sub_jqGrids").getRowData(ids[i]);
				if(rowData.detailId == -1) {
					inqtys += parseFloat($("#sub_jqGrids").getCell(ids[i], "inQty"));
					inamts += parseFloat($("#sub_jqGrids").getCell(ids[i], "inMoeny"));
					outqtys += parseFloat($("#sub_jqGrids").getCell(ids[i], "sendQty"));
					outamts += parseFloat($("#sub_jqGrids").getCell(ids[i], "sendMoeny"));
					othqtys += parseFloat($("#sub_jqGrids").getCell(ids[i], "othQty"));
					othamts += parseFloat($("#sub_jqGrids").getCell(ids[i], "othMoeny"));
				}
			};   

			if(isNaN(inqtys)) {
				inqtys = 0;
			}
			if(isNaN(inamts)) {
				inamts = 0;
			}
			inamts = precision(inamts,2)
			if(isNaN(outqtys)) {
				outqtys = 0;
			}
			if(isNaN(outamts)) {
				outamts = 0;
			}
			outamts = precision(outamts,2)
			if(isNaN(othqtys)) {
				othqtys = 0;
			}
			if(isNaN(othamts)) {
				othamts = 0;
			}
			othamts = precision(othamts,2)
			$("#sub_jqGrids").footerData('set', { 
				"bookOkDt": "本页合计",
				inQty: inqtys,
				inMoeny: inamts,
				sendQty: outqtys,
				sendMoeny: outamts,
				othQty: othqtys,
				othMoeny: othamts,

			}          );
		},
		ondblClickRow: function(rowid) {
			order(rowid);
		},
	});
	jQuery("#sub_jqGrids").jqGrid('setGroupHeaders', {
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
	//debugger
	var rowData = $("#sub_jqGrids").jqGrid('getRowData', rowid);
	var recvSendCateId = rowData.recvSendCateId;
	if(recvSendCateId == 9) {
		localStorage.setItem("intoWhsSnglId", rowData.formNum);
		window.open("../../Components/purs/intoWhs.html?1", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	} else if(recvSendCateId == 10) {
		localStorage.setItem("outWhsId", rowData.formNum);
		window.open("../../Components/sell/sellOutWhs.html?2", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	} else {
		localStorage.setItem("formNum", rowData.formNum);
		window.open("../../Components/whs/otherOutIntoWhs.html?1", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	}

}

////查询按钮
$(document).on('click', '.addWhs', function() {
	var myDate = {};
	var invtyEncd = $("#invtyEncd").val();
	var invtyClsEncd = $("input[name='invtyCls']").val();
	var whsEncd = $("input[name='whsEncd']").val();
	var batNum = $("input[name='batNum']").val();
	var bookOkSDt = $(".bookOkSDt").val();
	var bookOkEDt = $(".bookOkEDt").val();
	if(IsCheckValue(invtyEncd, batNum, whsEncd, bookOkSDt, bookOkEDt) == true) {
		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				"invtyEncd": invtyEncd,
				"invtyClsEncd": invtyClsEncd,
				"batNum": batNum,
				"bookOkSDt": bookOkSDt,
				"bookOkEDt": bookOkEDt,
				'whsEncd': whsEncd,
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			type: "post",
			contentType: 'application/json; charset=utf-8',
			url: url + '/mis/account/invty/detailed/list',
			async: true,
			data: saveData,
			dataType: 'json',
			success: function(data) {
				$("#mengban").hide()
				$('#findList').hide()
				var list = data.respBody;

				$("input[name='subDebitNm1']").val(list.subNm);
				$("input[name='invtyNm1']").val(list.invtyNm);
				$("input[name='measrCorpNm1']").val(list.measrCorpNm);
				$("input[name='batNum1']").val(list.batNum);
				$("input[name='invtyEncd1']").val(list.invtyEncd);
				$("input[name='spcModel1']").val(list.spcModel);
				var list1 = data.respBody.invtyDetailsList;
				myDate = list1;
				$("#sub_jqGrids").jqGrid('clearGridData');
				$("#sub_jqGrids").jqGrid('setGridParam', {
					datatype: 'local',
					data: myDate, //newData是符合格式要求的重新加载的数据
					page: 1 //哪一页的值
				}).trigger("reloadGrid")
			},
			error: function() {
				alert("error")
			}
		});
	}
})

//条件取消
$(document).on('click', '.cancel', function() {
	$('#findList').css("opacity", 0)
	$('#findList').css("display", 'none')
	window.location.reload()
})

$(document).on('click', '#find', function() {
	$('#findList').show();
	$('#findList').css("opacity", 1)
	$('#findList').css("background", '#fff')
})

//查询详细信息
$(function() {
	var afterUrl = window.location.search.substring(1);
	var a = [];
	a = afterUrl;
	if(a == 1) {
		chaxun()
	}
})

function chaxun() {
	var invtyEncd = localStorage.getItem("sub_invtyEncd");
	var batNum = localStorage.getItem("sub_batNum");
	var whsEncd = localStorage.getItem("sub_whsEncd");
	var bookOkSDt = localStorage.getItem("sub_bookOkSDt");
	var bookOkEDt = localStorage.getItem("sub_bookOkEDt");
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": invtyEncd,
			"batNum": batNum,
			"whsEncd": whsEncd,
			"bookOkSDt": bookOkSDt,
			"bookOkEDt": bookOkEDt,
		}
	};
	var saveData = JSON.stringify(savedata);
	var myDate = {};
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/account/invty/detailed/list',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			var list = data.respBody;
			$("input[name='subDebitNm1']").val(list.subNm);
			$("input[name='invtyNm1']").val(list.invtyNm);
			$("input[name='measrCorpNm1']").val(list.measrCorpNm);
			$("input[name='batNum1']").val(list.batNum);
			$("input[name='invtyEncd1']").val(list.invtyEncd);
			$("input[name='spcModel1']").val(list.spcModel);
			var rowNum = $("#_input").val()
			var list1 = data.respBody.invtyDetailsList;
			myDate = list1;
			$("#sub_jqGrids").jqGrid('clearGridData');
			$("#sub_jqGrids").jqGrid('setGridParam', {
				rowNum: rowNum,
				datatype: 'local',
				data: myDate, //newData是符合格式要求的重新加载的数据
				page: 1 //哪一页的值
			}).trigger("reloadGrid")
		},
		error: function() {
			alert("error")
		}
	})
}

//导出
$(document).on('click', '.exportExcel', function() {
	var invtyEncd = $("#invtyEncd").val();
	var invtyClsEncd = $("input[name='invtyCls']").val();
	var whsEncd = $("input[name='whsEncd']").val();
	var batNum = $("input[name='batNum']").val();
	var bookOkSDt = $(".bookOkSDt").val();
	var bookOkEDt = $(".bookOkEDt").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": invtyEncd,
			"invtyClsEncd": invtyClsEncd,
			"batNum": batNum,
			"bookOkSDt": bookOkSDt,
			"bookOkEDt": bookOkEDt,
			'whsEncd': whsEncd,
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/account/invty/detailed/listExport',
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
			var list = [];
			var list = data.respBody.list;
			var execlName = '明细账'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
})