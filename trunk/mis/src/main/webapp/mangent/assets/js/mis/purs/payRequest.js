var mType = 0;

//刚开始时可点击的按钮
$(function() {
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	$('button').addClass("gray");
	$(".refer").removeClass("gray") //参照
	$(".addWhs").removeClass("gray") //确定
	$(".cancel").removeClass("gray") //取消
	$(".find").removeClass("gray") //查询

	var afterUrl = window.location.search.substring(1);
	var b = [];
	b = afterUrl;
	if(b == 1) {
		var isNtChk = localStorage.isNtChk;
		if(isNtChk == "是") {
			$(".noTo").removeClass("gray")
		} else if(isNtChk == "否") {
			$('.toExamine').removeClass("gray");
			$('.delOrder').removeClass("gray");
			$('.editOrder').removeClass("gray");
		}
	}
	$('button').attr('disabled', false);
	$(".gray").attr('disabled', true)
})

//点击参照按钮，执行的操作
$(function() {
	$(".refer").click(function() {
		mType = 1;
		$("#jqgrids").trigger("reloadGrid");
		$('button').addClass("gray");
		$(".sure_refer").removeClass("gray") //确定
		$(".cancel").removeClass("gray") //取消
		$(".searcher").removeClass("gray") //查询

		$(".saveOrder").removeClass("gray");
		$(".upOrder").removeClass("gray");
		$('.refer').removeClass("gray")
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
		$("#mengban").hide();

		$("#purchaseOrder").hide();
		$("#purs_list").show();
		$("#purs_list").css("opacity", 1)
	})
	$(".refer_cancel").click(function() {
		window.location.reload();
	})
})

function isTrue(encd){
	for(var i = 0; i < encd.length; i++) {
		if(encd[i].applPayAmt==""){
			alert("请输入采购明细中第"+(i+1)+"行的本次申请金额");
			return false
		}else {
			return true;
		}
	}
}
$(function() {
	$(".sure_refer").click(function() {
		var gr = $("#deteil_list").jqGrid('getGridParam', 'selarrrow');
		//获得行数据
		var encd = [];
		for(var i = 0; i < gr.length; i++) {
			rowDatas = $("#deteil_list").jqGrid('getRowData', gr[i]);
			encd[i] = rowDatas;
		}
		if(encd.length == 0) {
			alert("请选择明细")
		} else if(isTrue(encd)==true){
			$("#purs_list").css("opacity", 0);
			$("#purchaseOrder").show();
			for(var i = 0; i < encd.length; i++) {
				$("#jqgrids").setRowData(i + 1, {
					srcFormNum: encd[i].pursOrdrId,
					qty: encd[i].qty,
					amt: encd[i].applPayAmt,
					orgnlSnglCurrApplAmt: encd[i].applPayAmt,
					expctPayDt: encd[i].planPayDt,
					formOrdrNum: encd[i].ordrNum
				});
			}
			searchPursOrdrId()
			pay_sumAdd()
		}
	})
})

//参照采购订单
function searchPursOrdrId() {
	var grs = $("#jqGrids_list").jqGrid('getGridParam', 'selrow');
	var rowDatas = $("#jqGrids_list").jqGrid('getRowData', grs);
	$("#mengban").hide();
	$("#deteil_list").trigger("reloadGrid");
	$("#gbox_jqgrids").show();
	$("#gbox_jqGrids").hide();

	$("#formSave input").val("");
	$("input[name='formTypEncd']").val("申请付款单");
	$("#user").val(rowDatas.accNum);
	$("#userName").val(rowDatas.userName);
	$("input[name='provrId']").val(rowDatas.provrId);
	$("input[name='deptId']").val(rowDatas.deptId);
	$("input[name='provrNm']").val(rowDatas.provrNm);
	$("input[name='deptName']").val(rowDatas.deptName);
	$("input[name='remarks']").val(rowDatas.memo);
	$("input[name='supplierNumber']").val(rowDatas.provrOrdrNum);

	var time = BillDate();
	//到货日期
	$("input[name=payApplDt]").val(time);

}

//点击表格图标显示业务员列表
$(function() {
	$(document).on('click', '.user', function() {
		window.open("../../Components/baseDoc/userList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})
//初始化表格
$(function() {
	allHeight()
	$("#jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json",
		colNames: ['来源子表序号', '来源单据号', '数量', '源单本次申请金额', '金额', '实际付款日期', '预计付款日期', '行关闭人'], //jqGrid的列显示名字
		colModel: [{
				name: 'formOrdrNum',
				editable: false,
				align: 'center',
				sortable: false,
				hidden: true,
			},
			{
				name: 'srcFormNum',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'qty',
				editable: false,
				align: 'center',
				sortable: false,
				hidden: true
			},
			{
				name: 'orgnlSnglCurrApplAmt',
				editable: false,
				align: 'center',
				sortable: false

			},
			{
				name: 'amt',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'actlPayTm',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'expctPayDt',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'lineClosPers',
				editable: false,
				align: 'center',
				hidden: true,
				sortable: false
			},
		],
		rowNum: 99999999,
		loadonce: false,
		rownumbers: true,
		rownumWidth: 20, //序列号列宽度
		autowidth: true,
		multiselect: true, //复选框 
		caption: '付款申请单',
		pager: "#jqgridPager",
		altclass: true,
		pgbuttons: false,
		pginput: false,
		//		viewrecords: true,
		height: height,
		autoScroll: true,
		shrinkToFit: false,
		forceFit: true,
		sortable: false,
		cellEdit: true,
		footerrow: true,
		cellsubmit: "clientArray",
		afterSaveCell: function(rowid, cellname, val, iRow, iCol) {
			$(".saveOrder").removeClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
			if((cellname == "amt")) {
				pay_sumAdd();
			}
		},
		gridComplete: function() { 
			$("#jqgrids").footerData('set', { 
				"srcFormNum": "本页合计",
			});
		},
	})
	$("#jqgrids").jqGrid('navGrid', '#jqgridPager', {
		edit: false,
		add: false,
		del: false,
		search: false,
		refresh: false,
	}).navButtonAdd('#jqgridPager', {
		caption: "",
		buttonicon: "ui-icon-trash",
		onClickButton: function() {
			//删除一行操作
			removeRows();
		},
		position: "first"
	}).navButtonAdd('#jqgridPager', {
		caption: "",
		buttonicon: "ui-icon-newwin",
		onClickButton: function() {
			//复制一行操作
			copyRows();
		},
		position: "last"
	}).navButtonAdd('#jqgridPager', {
		caption: "",
		buttonicon: "ui-icon-plus",
		onClickButton: function() {
			//新增一行操作
			addRows();
		},
		position: "last"
	})
})

function pay_sumAdd() {
	var list = getAllData();
	var qty = 0;
	var orgnlSnglCurrApplAmt = 0;
	var amt = 0;
	for(var i = 0; i < list.length; i++) {
		qty += parseFloat(list[i].qty);
		orgnlSnglCurrApplAmt += parseFloat(list[i].orgnlSnglCurrApplAmt);
		amt += parseFloat(list[i].amt);
	}; 
	if(isNaN(qty)) {
		qty = 0
	}
	if(isNaN(orgnlSnglCurrApplAmt)) {
		orgnlSnglCurrApplAmt = 0
	}
	if(isNaN(amt)) {
		amt = 0
	}
	qty = qty.toFixed(prec)
	orgnlSnglCurrApplAmt = precision(orgnlSnglCurrApplAmt,2)
	amt = precision(amt,2)
	$("#jqgrids").footerData('set', { 
		"qty": qty,
		"orgnlSnglCurrApplAmt": orgnlSnglCurrApplAmt,
		"amt": amt,
	});
}

function removeRows() {
	var gr = $("#jqgrids").jqGrid('getGridParam', 'selarrrow');
	if(gr.length == 0) {
		alert("请选择要删除的行");
		return;
	} else {
		for(var i = 0; i < gr.length + 1; i++) {
			$("#jqgrids").jqGrid("delRowData", gr[0]);
		}
	}
}
// 点击保存，传送数据给后台
var isclick = true;
$(function() {
	$(".saveOrder").click(function() {
		if(isclick) {
			isclick = false;
			if(mType == 1) {
				SaveNewData();
			}
			if(mType == 2) {
				SaveModifyData();
			}
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	})
})

// 点击修改按钮，执行的操作
$(function() {
	$('.editOrder').click(function() {
		mType = 2;
		$("#mengban").hide();
		$('button').addClass("gray")
		$('.saveOrder').removeClass("gray");
		$('.upOrder').removeClass("gray");
		$('.editOrder').addClass("gray"); //点击修改后 修改不能用
		$(".addWhs").removeClass("gray")
		$(".cancel").removeClass("gray")
		$(".find").removeClass("gray")
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
	});
})

//失效日期
function getNewDay(dateTemp, days) {
	var dateTemp = dateTemp.split("-");
	var nDate = new Date(dateTemp[1] + '-' + dateTemp[2] + '-' + dateTemp[0]); //转换为MM-DD-YYYY格式  
	var millSeconds = Math.abs(nDate) + (days * 24 * 60 * 60 * 1000);
	var rDate = new Date(millSeconds);
	var year = rDate.getFullYear();
	var month = rDate.getMonth() + 1;
	if(month < 10) month = "0" + month;
	var date = rDate.getDate();
	if(date < 10) date = "0" + date;
	return(year + "-" + month + "-" + date);
}

//修改到货单信息
function SaveModifyData() {
	var listData = getAllData();
	payApplId = $("input[name='payApplId']").val();
	var payApplDt = $("input[name='payApplDt']").val();
	var provrId = $("#provrId").val();
	var deptId = $("#deptId").val();
	var userName = $("#userName").val();
	var stlSubj = $(".stlSubj").val();
	var stlMode = $(".stlMode").val();
	var prepyMoneyBal = $(".prepyMoneyBal").val();
	var acctPyblBal = $(".acctPyblBal").val();
	var provrOrdrNum = $("input[name='supplierNumber']").val();
	var accNum = $("#user").val();
	var memo = $("input[name='remarks']").val();

	//判断页面是否有值为空
	if(IsCheckValue(provrId, accNum, listData) == true) {
		var savedata = {
			"reqHead": reqHead,
			"reqBody": {
				'payApplId': payApplId,
				'payApplDt': payApplDt,
				'provrId': provrId,
				'deptId': deptId,
				"stlSubj": stlSubj,
				"stlMode": stlMode,
				"prepyMoneyBal": prepyMoneyBal,
				"acctPyblBal": acctPyblBal,
				'userName': userName,
				'provrOrdrNum': provrOrdrNum,
				'accNum': accNum,
				'memo': memo,
				'list': listData
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			url: url + '/mis/purc/PayApplForm/editPayApplForm',
			type: 'post',
			data: saveData,
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
					$(".editOrder").removeClass("gray");
					$(".delOrder").removeClass("gray");
					$(".toExamine").removeClass("gray");
					$(".saveOrder").addClass("gray");
					$(".search").removeClass("gray");
					$('button').attr('disabled', false);
					$(".gray").attr('disabled', true)
					$("#mengban").show();
				}
			},
			error: function() {
				alert("获取数据错误");
			} //错误执行方法
		})

	}
}

function getAllData() {
	var obj = $("#jqgrids");
	var rowIds = obj.getDataIDs();
	var arrayData = new Array();
	if(rowIds.length > 0) {
		for(var i = 0; i < rowIds.length; i++) {
			if(obj.getRowData(rowIds[i]).qty == "") {
				continue;
			} else {
				arrayData.push(obj.getRowData(rowIds[i]));
			}
		}
	}
	return arrayData;
}

function IsCheckValue(provrId, accNum, listData) {
	var provrId1 = provrId.split(",");
	if(provrId == "" || provrId == undefined) {
		alert("供应商不能为空")
		return false;
	} else if(provrId1.length>1) {
		alert("供应商不能选多个")
		return false;
	} else if(accNum == '') {
		alert("业务员不能为空")
		return false;
	} else if(listData.length == 0) {
		alert("仓库不能为空")
		return false;
	} else if(listData.length != 0) {
		for(var i = 0; i < listData.length; i++) {
			if(listData[i].invtyEncd == "") {
				alert("存货不能为空")
				return false;
			} else if(listData[i].batNum == "") {
				alert("批次不能为空")
				return false;
			} else if(listData[i].qty == "") {
				alert("数量不能为空")
				return false;
			} else if(listData[i].batNum == "") {
				alert("批次不能为空")
				return false;
			} else if(listData[i].prdcDt == "") {
				alert("生产日期不能为空")
				return false;
			} else if(listData[i].noTaxUprc == "") {
				alert("无税单价不能为空")
				return false;
			} else if(listData[i].taxRate == "") {
				alert("税率不能为空")
				return false;
			}
		}
	}
	return true;
}

var payApplId;

//保存
function SaveNewData() {
	var listData = getAllData();
	var payApplDt = $("input[name='payApplDt']").val();
	var provrId = $("#provrId").val();
	var deptId = $("#deptId").val();
	var userName = $("#userName").val();
	var provrOrdrNum = $("input[name='supplierNumber']").val();
	var stlSubj = $(".stlSubj").val();
	var stlMode = $(".stlMode").val();
	var prepyMoneyBal = $(".prepyMoneyBal").val();
	var acctPyblBal = $(".acctPyblBal").val();
	var accNum = $("#user").val();
	var memo = $("input[name='remarks']").val();
	//判断页面是否有值为空
	if(IsCheckValue(provrId, accNum, listData) == true) {
		var savedata = {
			"reqHead": reqHead,
			"reqBody": {
				'payApplId': "",
				'payApplDt': payApplDt,
				'provrId': provrId,
				"deptId": deptId,
				'userName': userName,
				"stlSubj": stlSubj,
				"stlMode": stlMode,
				"prepyMoneyBal": prepyMoneyBal,
				"acctPyblBal": acctPyblBal,
				'provrOrdrNum': provrOrdrNum,
				'accNum': accNum,
				"memo": memo,
				'formTypEncd': "032",
				'list': listData
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			url: url + '/mis/purc/PayApplForm/addPayApplForm',
			type: 'post',
			data: saveData,
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
				payApplId = data.respBody.payApplId;
				$("input[name='payApplId']").val(data.respBody.payApplId); //订单编码
				if(data.respHead.isSuccess == true) {
					$(".editOrder").removeClass("gray");
					$(".delOrder").removeClass("gray");
					$(".toExamine").removeClass("gray");
					$(".search").removeClass("gray");
					$(".saveOrder").addClass("gray");
					$(".upOrder").addClass("gray");
					$('button').attr('disabled', false);
					$(".gray").attr('disabled', true)
					$("#mengban").show();
				}
			},
			error: function(error) {
				alert("error");
			} //错误执行方法
		})
	}
}
$(function() {
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
$(function() {
	// 点击删除按钮，执行的操作
	$('.delOrder').click(function() {
		mType = 0;
		var deleteAjax = {
			"reqHead": reqhead,
			"reqBody": {
				"payApplId": payApplId
			}
		}
		var deleteData = JSON.stringify(deleteAjax);
		if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + '/mis/purc/PayApplForm/deleteEntrsAgnAdj',
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				beforeSend: function() {
					$(".zhezhao").css("display", "block");
					$("#loader").css("display", "block");
				},
				//结束加载动画
				complete: function() {
					$(".zhezhao").css("display", "none");
					$("#loader").css("display", "none");
				},
				success: function(remover) {
					alert(remover.respHead.message)
					window.location.reload()
				},
				error: function() {
					alert("删除失败")
				}
			});
		}
	});
})
//是否审核
function ntChk(x) {
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"list": [{
				"payApplId": payApplId,
				"isNtChk": x,
			}]
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/purc/PayApplForm/updatePayApplFormIsNtChk',
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
				if(x == 1) {
					$("button").addClass("gray");
					$(".noTo").removeClass("gray");
					$(".search").removeClass("gray");
					$(".refer").removeClass("gray");

					$(".addWhs").removeClass("gray") //确定
					$(".cancel").removeClass("gray") //取消
					$(".find").removeClass("gray") //查询

					$('button').attr('disabled', false);
					$(".gray").attr('disabled', true)
				} else if(x == 0) {
					$("button").removeClass("gray");
					$(".upOrder").addClass("gray");
					$(".noTo").addClass("gray");
					$(".saveOrder").addClass("gray");
					$('button').attr('disabled', false);
					$(".gray").attr('disabled', true)
				}
			}
		},
		error: function() {
			alert("审核失败")
		}
	})
}

//查询详细信息
$(function() {
	var afterUrl = window.location.search.substring(1);
	var b = [];
	b = afterUrl;
	if(b == 1) {
		chaxun()
	}
})

function chaxun() {
	$("#jqgrids").setColProp("invtyEncd", {
		editable: false
	})
	$("#mengban").hide();
	payApplId = localStorage.payApplId;
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"payApplId": payApplId,
		}
	};
	var saveData = JSON.stringify(savedata);
	var myDate = {};
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/purc/PayApplForm/queryPayApplForm',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			var list1 = data.respBody;
			$("input[name='formTypEncd']").val(list1.formTypName);
			$("input[name='payApplDt']").val(list1.payApplDt);
			$("input[name='payApplId']").val(list1.payApplId);
			$("#user").val(list1.accNum);
			$("#userName").val(list1.userName);
			$(".stlSubj").val(list1.stlSubj);
			$(".stlMode").val(list1.stlMode);
			$(".prepyMoneyBal").val(list1.prepyMoneyBal);
			$(".acctPyblBal").val(list1.acctPyblBal);
			$("input[name='provrNm']").val(list1.provrNm);
			$("input[name='deptName']").val(list1.deptName);
			$("input[name='provrId']").val(list1.provrId);
			$("input[name='deptId']").val(list1.deptId);
			$("input[name='remarks']").val(list1.memo);
			$("input[name='supplierNumber']").val(list1.provrOrdrNum);
			var list = data.respBody.list;
			$("#jqgrids").jqGrid('clearGridData');
			$("#jqgrids").jqGrid('setGridParam', {
				datatype: 'local',
				data: list, //newData是符合格式要求的重新加载的数据
			}).trigger("reloadGrid")
//			for(var i = 0; i < list.length; i++) {
//				$("#jqgrids").setRowData(i + 1, {
//					srcFormNum: list[i].pursOrdrId,
//					qty: list[i].qty,
//					amt: list[i].amt,
//					orgnlSnglCurrApplAmt: list[i].orgnlSnglCurrApplAmt,
//					expctPayDt: list[i].expctPayDt,
//					srcFormNum: list[i].srcFormNum,
//					actlPayTm: list[i].actlPayTm,
//					formOrdrNum: list[i].formOrdrNum
//				});
//			}
			pay_sumAdd()
		}
	})
}