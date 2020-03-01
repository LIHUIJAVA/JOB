var isSuccess;
var serialNum;
//默认可以点击的按钮
$(function() {
	$('.gds_sure').removeClass("gray");
	$('.gds_cancel').removeClass("gray")
	$(".gds_delete").removeClass("gray");
	$('button').attr('disabled', false);
	$(".gray").attr('disabled', true);
})
//点击页面上的货位分配
$(function() {
	$(".gdsBitAllotted").click(function() {
		var orderNum = $('input[name="formNum"]').val(); //单据号
		var ids = $("#jqgrids").jqGrid('getGridParam', 'selarrrow');
		var rowData = $("#jqgrids").jqGrid('getRowData', ids);
		serialNum = rowData.ordrNum;
		if(orderNum == "") {
			alert("请先保存数据")
		} else if(ids == null || ids.length == 0) {
			alert("请选择要分配货位的单据")
			$("#mengban").hide();
		} else if(ids.length > 1) {
			alert("每次只能分配一条单据")
			$("#mengban").hide();
		} else {
			$("#mengban").hide();
			$("#box").show();
			$("#box").css("opacity", 1);
			$("#box").css("top", "20%")
			var ids = $("#jqgrids").jqGrid('getGridParam', 'selrow');
			var rowData = $("#jqgrids").jqGrid('getRowData', ids);
			serialNum = rowData.ordrNum;
			var formNum = $('input[name="formNum"]').val(); //单据号
			var whsEncd = $("input[name='whsEncd']").val();
			if(whsEncd == "" || whsEncd == undefined) {
				var whsEncd = rowData.whsEncd
			}
			$(".gds_orderNum").val(formNum);
			$(".gsd_invtyEncd").val(rowData.invtyEncd);
			$(".gsd_whsEncd").val(whsEncd);
			$(".gsd_batNum").val(rowData.batNum);
			$(".serialNum").val(rowData.ordrNum);
			$(".gsd_prdcDt").val(rowData.prdcDt);
			localStorage.setItem("whs_encd", whsEncd);
			initPage();
			findGds()
		}
	})
})
//初始化表格
function findGds() {
	$("#gds_jqgrids").trigger("reloadGrid");
	var orderNum = $('input[name="formNum"]').val(); //单据号
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"orderNum": orderNum, //单据号
			"serialNum": serialNum
		}
	};
	var postData = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/whs/out_into_whs/queryInvtyGdsBitList',
		type: 'post',
		data: postData,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		error: function() {
			alert("获取数据错误");
		}, //错误执行方法
		success: function(data) {
			var list = data.respBody.list;
			isSuccess = data.respHead.isSuccess;
			for(var i = 0; i < list.length; i++) {
				$("#gds_jqgrids").setRowData(i + 1, {
					regnEncd: list[i].regnEncd,
					gdsBitEncd: list[i].gdsBitEncd,
					qty: list[i].qty,
					id: list[i].id,
				})
			}
		}
	})
}

function initPage() {
	$("#gds_jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['序号', '区域编码', '货位编码', '数量'], //jqGrid的列显示名字
		colModel: [{
				name: "id",
				align: "center",
				editable: true,
				sortable: false,
				hidden: true
			},
			{
				name: "regnEncd", //区域编码
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'gdsBitEncd', //货位编码
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'qty',
				align: "center",
				editable: true,
				sortable: false
			},
		],
		autowidth: true,
		height: '100%',
		autoScroll: true,
		shrinkToFit: false,
		rowNum: 5, //一页显示多少条
		rownumbers: true,
		loadonce: false,
		forceFit: true,
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "分配货位", //表格的标题名字
		cellEdit: true, //单元格是否可编辑
		cellsubmit: "clientArray",
		multiselect: true, //复选框 

		afterEditCell: function(rowid, cellname, val) {
			$(".gds_sure").addClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true)
			if(cellname == "gdsBitEncd") { //货位编码
				$("#" + rowid + "_" + cellname).bind("dblclick", function() {
					localStorage.setItem("gds", 0)
					window.open("../../Components/whs/gdsBitList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
					gdsBit()
				});
				$("#" + rowid + "_" + cellname).keydown(function() {
					CloseGdsBit()
				})
				CloseRegn();
			}
			if(cellname == "qty") {
				CloseRegn();
			}
		},
		//离开编辑状态
		afterRestoreCell: function(rowid, cellname, val, iRow, iCol) {
			$(".gds_sure").removeClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true)
		},
		//回车保存
		afterSaveCell: function(rowid, cellname, val) {
			$(".gds_sure").removeClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true)
			if(cellname == "regnEncd") {
				CloseRegn();
			}
			if(cellname == "gdsBitEncd") {
				CloseGdsBit();
			}
		}
	});
}

var beginRegn;
var beginGdsBit;

function regn() {
	var ids = $("#gds_jqgrids").jqGrid('getGridParam', 'selrow');
	beginRegn = setInterval(function() {
		$("#" + ids + "_regnEncd").val(localStorage.regnEncd)
	}, 500)
}

function CloseRegn() {
	clearInterval(beginRegn)
}

function gdsBit() {
	var ids = $("#gds_jqgrids").jqGrid('getGridParam', 'selrow');
	beginGdsBit = setInterval(function() {
		//每间隔500MS，执行
		$("#" + ids + "_gdsBitEncd").val(localStorage.gdsBitEncd)
		if(localStorage.regnEncd == undefined) {
			localStorage.regnEncd = ""
		}
		$("#gds_jqgrids").setRowData(ids, {
			regnEncd: localStorage.regnEncd,
		})
	}, 500)
}

function CloseGdsBit() {
	clearInterval(beginGdsBit)
}
//删除
$(function() {
	$(".gds_delete").click(function() {
		var ids = $("#gds_jqgrids").jqGrid('getGridParam', 'selarrrow')
		var rowData = [];
		for(var i = 0; i < ids.length; i++) {
			var id = $("#gds_jqgrids").getCell(ids[i], "id");
			rowData[i] = id;
		}
		var rowDatas = rowData.toString();
		var orderNum = $('.gds_orderNum').val(); //单据号
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				"num": orderNum,
				"id": rowDatas
			}
		};
		var postData = JSON.stringify(data);
		if(ids.length == 0) {
			alert("请选择要删除的行")
		} else {
			$.ajax({
				url: url + '/mis/whs/out_into_whs/deleteInvtyGdsBitList',
				type: 'post',
				data: postData,
				dataType: 'json',
				async: true,
				contentType: 'application/json;charset=utf-8',
				success: function(data) {
					alert(data.respHead.message)
					findGds()
				},
				error: function() {
					alert("删除失败");
				}
			})
		}
	})
})

//取消
$(function() {
	$(".gds_cancel").click(function() {
		$("#box").css("opacity", 0);
		$("#box").css("top", "100%");
		CloseGdsBit();
	})
})

//新增
$(function() {
	$(".gds_sure").click(function() {
		if(isSuccess == false) {
			addNewData()
		} else if(isSuccess == true) {
			addEditData()
		}
	})
})

function getAllData() {
	var obj = $("#gds_jqgrids");
	//获取grid表中所有的rowid值
	var rowIds = obj.getDataIDs();
	//初始化一个数组arrayData容器，用来存放rowData
	var arrayData = new Array();
	if(rowIds.length > 0) {
		for(var i = 0; i < rowIds.length; i++) {
			if(obj.getRowData(rowIds[i]).gdsBitEncd == "") {
				continue;
			} else {
				arrayData.push(obj.getRowData(rowIds[i]));
			}
		}
	}
	return arrayData;
}

function addNewData() {
	var ids = $("#gds_jqgrids").jqGrid('getGridParam', 'selarrrow');
	var orderNum = $('.gds_orderNum').val(); //单据号
	var whsEncd = $(".gsd_whsEncd").val();
	var serialNum = $(".serialNum").val();
	var invtyEncd = $(".gsd_invtyEncd").val();
	var batNum = $(".gsd_batNum").val();
	var prdcDt = $(".gsd_prdcDt").val()
	var rowData = getAllData();
	if(rowData.length == 0) {
		alert("请选择单据")
	} else {
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				'orderNum': orderNum,
				'whsEncd': whsEncd,
				'serialNum': serialNum,
				'invtyEncd': invtyEncd,
				'batNum': batNum,
				'prdcDt': prdcDt,
				"list": rowData,
			}
		};
		var postData = JSON.stringify(data);
		$.ajax({
			url: url + '/mis/whs/out_into_whs/insertInvtyGdsBitList',
			type: 'post',
			data: postData,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			success: function(data) {
				alert(data.respHead.message)
				findGds()
				$("#box").css("opacity", 0);
				$("#box").css("top", "100%")
			},
			error: function() {
				alert("新增失败");
			}
		})
	}
}

function addEditData() {
	var ids = $("#gds_jqgrids").jqGrid('getGridParam', 'selarrrow');
	var orderNum = $('.gds_orderNum').val(); //单据号
	var whsEncd = $(".gsd_whsEncd").val();
	var serialNum = $(".serialNum").val();
	var invtyEncd = $(".gsd_invtyEncd").val();
	var batNum = $(".gsd_batNum").val();
	var prdcDt = $(".gsd_prdcDt").val()
	var rowData = getAllData();
	if(rowData.length == 0) {
		alert("请选择单据")
	} else {
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				'orderNum': orderNum,
				'whsEncd': whsEncd,
				'serialNum': serialNum,
				'invtyEncd': invtyEncd,
				'batNum': batNum,
				'prdcDt': prdcDt,
				"list": rowData,
			}
		};
		var postData = JSON.stringify(data);
		$.ajax({
			url: url + '/mis/whs/out_into_whs/uploadInvtyGdsBitList',
			type: 'post',
			data: postData,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			success: function(data) {
				alert(data.respHead.message)
				findGds()
				$("#box").css("opacity", 0);
				$("#box").css("top", "100%")
			},
			error: function() {
				alert("修改失败");
			}
		})
	}
}