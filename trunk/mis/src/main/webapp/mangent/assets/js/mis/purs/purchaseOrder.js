var mType = 0;

$(function() {
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	getData();
	$(document).on('keypress', '#user', function(even) {
		if(event.keyCode == '13') {
			$('#user').blur();
		}
	})
	$(document).on('blur', '#user', function() {
		var user = $("#user").val();
		dev({
			doc1: $("#user"),
			doc2: $("#userName"),
			showData: {
				"accNum": user,
			},
			afunction: function(data) {
				var userName = data.respBody.userName;
				var deptId = data.respBody.depId;
				var deptName = data.respBody.depName;
				$("#userName").val(userName);
				$("#deptId").val(deptId);
				$("#deptName").val(deptName);
			},
			url: url + "/mis/system/misUser/query"
		})
	})
})

$(function() {
	var find_height = $(".purchaseTit").height() + $('.order-title').height() + 150;
	$("#findGrid").css("top", find_height)
	//	$("#mengban").show();
	$('button').addClass("gray");
	$('.addOrder').removeClass("gray")
	$('button').attr('disabled', false);
	$(".gray").attr('disabled', true)
	var afterUrl = window.location.search.substring(1);
	var b = [];
	b = afterUrl;
	if(b == 1) {
		var isNtChk = localStorage.isNtChk;
		if(isNtChk == '是') {
			$(".noTo").removeClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true)
		} else if(isNtChk == "否") {
			$('.toExamine').removeClass("gray");
			$('.delOrder').removeClass("gray");
			$('.editOrder').removeClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true)
		}
	}
})

// 点击增加按钮，执行的操作
$(function() {
	$('.addOrder').click(function() {
		$("#mengban").hide();
		$("#jqgrids").setColProp("invtyEncd", {
			editable: true
		});
		$("#jqgrids").setColProp("qty", {
			editable: true
		})
		$("#jqgrids").setColProp("planToGdsDt", {
			editable: true
		});
		$("#jqgrids").setColProp("memo", {
			editable: true
		});
		$("#jqgrids").setColProp("prcTaxSum", {
			editable: true
		});
		$("#jqgrids").setColProp("taxRate", {
			editable: true
		});
		$("#jqgrids").setColProp("bxQty", {
			editable: true
		});
		$("#jqgrids").setColProp("cntnTaxUprc", {
			editable: true
		});
		$("#jqgrids").setColProp("noTaxAmt", {
			editable: true
		});
		$("#jqgrids").setColProp("noTaxUprc", {
			editable: true
		});
		$(".saveOrder").removeClass("gray");
		$(".ctrlc_v").removeClass("gray");
		$(".upOrder").removeClass("gray"); //新增后放弃能用
		$('.addOrder').addClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
		mType = 1;

		$("#jqgrids").jqGrid('setGridParam', {}).trigger("reloadGrid")
		$("#gbox_jqGrids").hide();
		$("#gbox_jqgrids").show();

		$(".inputText").val("");
		$("input[name='orderNumber']").val("")
		$("input[name=purType]").val("普通采购");
		var time = BillDate();
		$("input[name=orderDate]").val(time);
	});
})

//打开存货档案后点击确定取消
$(function() {
	//确定
	$(".addWhs").click(function() {
		//到货单
		//获得行号
		var rowid = $("#jqgrids").jqGrid('getGridParam', 'selrow');
		//	存货档案
		//	获得行号
		var ids = $("#insert_jqgrids").jqGrid('getGridParam', 'selarrrow');
		var good = [];
		for(var i = 0; i < ids.length; i++) {
			var rowData = $("#insert_jqgrids").jqGrid('getRowData', ids[i]);
			good[i] = rowData.invtyEncd;
		}
		var goods = good.join(",");
		GetGoodsInfo(rowid, goods);

		$("#insertList").css("opacity", 0);
		$("#purchaseOrder").show();
	})
	//	取消
	$(".cancel12").click(function() {
		$("#insertList").css("opacity", 0);
		$("#purchaseOrder").show();
		//到货单
		//获得行号
		//		var rowid = $("#jqgrids").jqGrid('getGridParam', 'selrow');
		//
		//		$("#" + rowid + "_invtyEncd").val("")
	})
})

function getJQAllData1() {
	//拿到grid对象
	var obj = $("#jqgrids");
	//获取grid表中所有的rowid值
	var rowIds = obj.getDataIDs();
	//初始化一个数组arrayData容器，用来存放rowData
	var arrayData = new Array();
	if(rowIds.length > 0) {
		for(var i = 0; i < rowIds.length; i++) {
			if(obj.getRowData(rowIds[i]).invtyEncd == "") {
				arrayData.push(obj.getRowData(rowIds[i]));
			}
		}
	}
	return arrayData;
}
//初始化表格
$(function() {
	allHeight();
	$("#jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json",
		colNames: ['保质期', '表体备注', '存货编码*', '存货名称', '数量*', '含税单价', '价税合计',
			'无税单价', '无税金额', '规格型号', '主计量单位', '税率', '税额', '计划到货时间', '箱规', '箱数',
			'对应条形码', '主计量单位编码'
		], //jqGrid的列显示名字
		colModel: [{
				name: 'baoZhiQiDt',
				editable: false,
				align: 'center',
				hidden: true,
				sortable: false
			}, {
				name: 'memo',
				editable: true,
				align: 'center',
				sortable: false
			}, {
				name: 'invtyEncd',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'invtyNm',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'qty',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'cntnTaxUprc', //含税单价
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'prcTaxSum', //价税合计
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'noTaxUprc', //无税单价
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'noTaxAmt', //无税金额
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'spcModel',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'measrCorpNm',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'taxRate', //税率
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'taxAmt', //税额
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'planToGdsDt',
				editable: true,
				align: 'center',
				editoptions: {
					dataInit: function(element) {
						$(element).datepicker({
							dateFormat: 'yy-mm-dd'
						})
					}
				},
				sortable: false
			},
			{
				name: 'bxRule',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'bxQty',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'crspdBarCd',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'measrCorpId',
				editable: false,
				align: 'center',
				hidden: true,
				sortable: false
			},
		],
		rowNum: 99999999,
		loadonce: false,
		rownumbers: true,
		autowidth: true,
		multiselect: true, //复选框 
		caption: '采购订单',
		altclass: true,
		pgbuttons: false,
		pginput: false,
		//		viewrecords: true,
		height: height,
		autoScroll: true,
		shrinkToFit: false,
		forceFit: true,
		pager: "#jqgridPager",
		sortorder: "desc",
		cellEdit: true,
		footerrow: true,
		cellsubmit: "clientArray",
		gridComplete: function() { 
			$("#jqgrids").footerData('set', { 
				"memo": "本页合计",
			});
		},
		afterEditCell: function(rowid, cellname, val, iRow, iCol) {
			$("#findGrid").hide();
			$(".saveOrder").addClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true)
			if(cellname == "invtyEncd") {
				$("#jqgrids").setColProp("cntnTaxUprc", {
					editable: false
				});
				$("#" + rowid + "_" + cellname).bind('keyup', function() {
					findGrid(rowid, cellname, val);
				})
				$("input[name='invtyEncd']").bind("dblclick", function() {
					$("#insertList").css("opacity", 1);
					$("#purchaseOrder").hide();
					$(".addWhs").removeClass("gray") //确定可用
					$(".find").removeClass("gray") //查询可用
					$(".cancel12").removeClass("gray") //取消可用
					$('button').attr('disabled', false); //可点击状态
				});
			};
			if(cellname == "prdcDt") {
				$("input[name='prdcDt']").attr("calendar", "YYYY-MM-DD")
			}
			var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
			if((cellname == "cntnTaxUprc") ||
				(cellname == "prcTaxSum") ||
				(cellname == "noTaxUprc") ||
				(cellname == "noTaxAmt") ||
				(cellname == "taxRate")) {
				if(rowDatas.qty == "") {
					alert("请先输入数量");
					$("#" + rowid + "_cntnTaxUprc").attr('disabled', 'disabled');
					$("#" + rowid + "_prcTaxSum").attr('disabled', 'disabled');
					$("#" + rowid + "_noTaxUprc").attr('disabled', 'disabled');
					$("#" + rowid + "_noTaxAmt").attr('disabled', 'disabled');
					$("#" + rowid + "_taxRate").attr('disabled', 'disabled');
				}
			}
		},
		afterRestoreCell: function(rowid, cellname, val, iRow, iCol) {
			$(".saveOrder").removeClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true)
		},
		//回车保存
		afterSaveCell: function(rowid, cellname, val, iRow, iCol) {
			$(".saveOrder").removeClass("gray")
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
			$("#findGrid").hide();
			//获取商品信息
			if(cellname == "invtyEncd") {
				if(val) {
					GetGoodsInfo(rowid, val);
				}
			}
			//设置数量
			if((cellname == "bxQty") ||
				(cellname == "qty") ||
				(cellname == "cntnTaxUprc") ||
				(cellname == "prcTaxSum") ||
				(cellname == "noTaxUprc") ||
				(cellname == "noTaxAmt") ||
				(cellname == "taxRate")) {
				//设置变量
				purs_SetNums(rowid, cellname, val);
				var numList = getJQAllData1()
				if(numList.length == 0) {
					addRows()
				}
				sumAdd()
			}
			var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
			if(rowDatas.invtyEncd == "") {
				$("#jqgrids").setRowData(rowid, {
					invtyNm: "",
					spcModel: "",
					bxRule: "",
					measrCorpNm: "",
					crspdBarCd: "",
					noTaxUprc: "",
					taxRate: "",
					measrCorpId: "",
					baoZhiQiDt: "",
					prcTaxSum: "",
				});
			}
		}
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
			//删除一行操作
			copyRows();
		},
		position: "last"
	}).navButtonAdd('#jqgridPager', {
		caption: "",
		buttonicon: "ui-icon-plus",
		onClickButton: function() {
			//删除一行操作
			addRows();
		},
		position: "last"
	})
})

function sumAdd() {
	var list = getJQAllData();
	var qty = 0;
	var noTaxAmt = 0;
	var taxAmt = 0;
	var prcTaxSum = 0;
	for(var i = 0; i < list.length; i++) {
		qty += parseFloat(list[i].qty);
		noTaxAmt += parseFloat(list[i].noTaxAmt);
		taxAmt += parseFloat(list[i].taxAmt);
		prcTaxSum += parseFloat(list[i].prcTaxSum);
	}; 
	if(isNaN(qty)) {
		qty = 0
	}
	if(isNaN(noTaxAmt)) {
		noTaxAmt = 0
	}
	if(isNaN(taxAmt)) {
		taxAmt = 0
	}
	if(isNaN(prcTaxSum)) {
		prcTaxSum = 0
	}
	qty = qty.toFixed(prec)
	noTaxAmt = precision(noTaxAmt, 2)
	taxAmt = precision(taxAmt, 2)
	prcTaxSum = precision(prcTaxSum, 2)
	$("#jqgrids").footerData('set', { 
		"qty": qty,
		"noTaxAmt": noTaxAmt,
		"taxAmt": taxAmt,
		"prcTaxSum": prcTaxSum,
	});
}

// 点击修改按钮，执行的操作
$(function() {
	$('.editOrder').click(function() {
		mType = 2;
		$('button').addClass("gray")
		$('.saveOrder').removeClass("gray");
		$('.upOrder').removeClass("gray");
		$('.ctrlc_v').removeClass("gray");
		$('.editOrder').addClass("gray"); //点击修改后 修改不能用
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true);

		$("#jqgrids").setColProp("invtyEncd", {
			editable: true
		});
		$("#jqgrids").setColProp("qty", {
			editable: true
		})
		$("#jqgrids").setColProp("planToGdsDt", {
			editable: true
		});
		$("#jqgrids").setColProp("memo", {
			editable: true
		});
		$("#jqgrids").setColProp("prcTaxSum", {
			editable: true
		});
		$("#jqgrids").setColProp("taxRate", {
			editable: true
		});
		$("#jqgrids").setColProp("bxQty", {
			editable: true
		});
		$("#jqgrids").setColProp("cntnTaxUprc", {
			editable: true
		});
		$("#jqgrids").setColProp("noTaxAmt", {
			editable: true
		});
		$("#jqgrids").setColProp("noTaxUprc", {
			editable: true
		});
	});
})

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

function IsCheckValue(provrId, accNum, listData) {
	var provrId1 = provrId.split(",");
	if(provrId == "" || provrId == undefined) {
		alert("供应商不能为空")
		return false;
	} else if(provrId1.length > 1) {
		alert("供应商不能选多个")
		return false;
	} else if(accNum == "" || accNum == undefined) {
		alert("业务员不能为空")
		return false;
	} else if(listData.length == 0) {
		alert("存货不能为空")
		return false;
	} else if(listData.length != 0) {
		for(var i = 0; i < listData.length; i++) {
			if(listData[i].qty == "") {
				alert("数量不能为空")
				return false;
			} else if(listData[i].qty < 0) {
				alert("数量不能小于0")
				return false;
			} else if(listData[i].taxRate == "") {
				alert("税率不能为空")
				return false;
			} else if(listData[i].prcTaxSum == "") {
				alert("价税合计不能为空")
				return false;
			}
		}
	}
	return true;
}

//点完修改后执行的url
function SaveModifyData() {
	var listData = getJQAllData();

	var pursOrdrDt = $("input[name='orderDate']").val();
	var userName = $("#userName").val();
	var provrOrdrNum = $("input[name='supplierNumber']").val();
	var pursOrdrId = $("input[name='orderNumber']").val();
	var accNum = $("#user").val();
	var provrId = $("#provrId").val();
	var deptId = $("#deptId").val();
	var memo = $("input[name='remarks']").val();

	if(IsCheckValue(provrId, accNum, listData) == true) {
		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				'pursOrdrId': pursOrdrId,
				'pursOrdrDt': pursOrdrDt,
				'pursTypId': "1",
				'provrId': provrId,
				"deptId": deptId,
				'userName': userName,
				'provrOrdrNum': provrOrdrNum,
				'accNum': accNum,
				'memo': memo,
				'list': listData
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			url: url + '/mis/purc/PursOrdr/editPursOrdr',
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
					$('button').removeClass("gray")
					$('.saveOrder').addClass("gray");
					$('.upOrder').addClass("gray");
					$('.noTo').addClass("gray");
					$('button').attr('disabled', false);
					$(".gray").attr('disabled', true)
				}
			},
			error: function() {
				alert("修改失败");
			} //错误执行方法
		})
	}
}

function getJQAllData() {
	//拿到grid对象
	var obj = $("#jqgrids");
	//获取grid表中所有的rowid值
	var rowIds = obj.getDataIDs();
	//初始化一个数组arrayData容器，用来存放rowData
	var arrayData = new Array();
	if(rowIds.length > 0) {
		for(var i = 0; i < rowIds.length; i++) {
			if(obj.getRowData(rowIds[i]).invtyEncd == "") {
				continue;
			} else {
				arrayData.push(obj.getRowData(rowIds[i]));
			}
		}
	}
	return arrayData;
}
var pursOrdrId;
//第一次保存
function SaveNewData() {
	var listData = getJQAllData();
	var pursOrdrDt = $("input[name='orderDate']").val();
	var userName = $("#userName").val();
	var provrOrdrNum = $("input[name='supplierNumber']").val();
	var accNum = $("#user").val();
	var provrId = $("#provrId").val();
	var deptId = $("#deptId").val();
	var memo = $("input[name='remarks']").val();

	//判断页面是否有值为空
	if(IsCheckValue(provrId, accNum, listData) == true) {
		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				'pursOrdrId': "",
				'pursOrdrDt': pursOrdrDt,
				'pursTypId': "1",
				'provrId': provrId,
				"deptId": deptId,
				'userName': userName,
				'provrOrdrNum': provrOrdrNum,
				'accNum': accNum,
				'list': listData,
				'formTypEncd': "001",
				'memo': memo
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			url: url + '/mis/purc/PursOrdr/addPursOrdr',
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
				pursOrdrId = data.respBody.pursOrdrId;
				$("input[name='orderNumber']").val(data.respBody.pursOrdrId); //订单编码
				if(data.respHead.isSuccess == true) {
					$('button').removeClass("gray");
					$('.saveOrder').addClass("gray");
					$(".ctrlc_v").addClass("gray");
					$(".ctrlYes").addClass("gray");
					$('.upOrder').addClass("gray");
					$('.noTo').addClass("gray");
					$('button').attr('disabled', false);
					$(".gray").attr('disabled', true);
					$("#jqgrids").setColProp("invtyEncd", {
						editable: false
					});
					$("#jqgrids").setColProp("qty", {
						editable: false
					})
					$("#jqgrids").setColProp("planToGdsDt", {
						editable: false
					});
					$("#jqgrids").setColProp("memo", {
						editable: false
					});
					$("#jqgrids").setColProp("prcTaxSum", {
						editable: false
					});
					$("#jqgrids").setColProp("taxRate", {
						editable: false
					});
					$("#jqgrids").setColProp("bxQty", {
						editable: false
					});
					$("#jqgrids").setColProp("cntnTaxUprc", {
						editable: false
					});
					$("#jqgrids").setColProp("noTaxAmt", {
						editable: false
					});
					$("#jqgrids").setColProp("noTaxUprc", {
						editable: false
					});
				}
			},
			error: function() {
				alert("保存失败");
			} //错误执行方法
		})
	}
}

//是否审核		
function ntChk(x) {
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"list": [{
				"pursOrdrId": pursOrdrId,
				"isNtChk": x
			}]
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/purc/PursOrdr/updatePursOrdrIsNtChk',
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
					$(".addOrder").removeClass("gray");
					$(".noTo").removeClass("gray");
					$(".search").removeClass("gray");
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

// 点击删除按钮，执行的操作
$(function() {
	$('.delOrder').click(function() {
		var deleteAjax = {
			"reqHead": reqhead,
			"reqBody": {
				"pursOrdrId": pursOrdrId
			}
		}
		var deleteData = JSON.stringify(deleteAjax);
		if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + '/mis/purc/PursOrdr/deletePursOrdrList',
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
	$("#jqgrids").setColProp("invtyEncd", {
		editable: false
	});
	$("#jqgrids").setColProp("qty", {
		editable: false
	})
	$("#jqgrids").setColProp("planToGdsDt", {
		editable: false
	});
	$("#jqgrids").setColProp("memo", {
		editable: false
	});
	$("#jqgrids").setColProp("prcTaxSum", {
		editable: false
	});
	$("#jqgrids").setColProp("taxRate", {
		editable: false
	});
	$("#jqgrids").setColProp("bxQty", {
		editable: false
	});
	$("#jqgrids").setColProp("cntnTaxUprc", {
		editable: false
	});
	$("#jqgrids").setColProp("noTaxAmt", {
		editable: false
	});
	$("#jqgrids").setColProp("noTaxUprc", {
		editable: false
	});
	pursOrdrId = localStorage.pursOrdrId;
	if(localStorage.ntChkNo == "1") {
		$(".addOrder").hide()
	}
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"pursOrdrId": pursOrdrId,
		}
	};
	var saveData = JSON.stringify(savedata);
	var myDate = {};
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/purc/PursOrdr/queryPursOrdr',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			var list1 = data.respBody;
			$("input[name='purType']").val(list1.pursTypNm);
			$("input[name='orderDate']").val(list1.pursOrdrDt);
			$("input[name='orderNumber']").val(list1.pursOrdrId);
			$("#user").val(list1.accNum);
			$("#userName").val(list1.userName);
			$("input[name='remarks']").val(list1.memo);
			$("input[name='supplierNumber']").val(list1.provrOrdrNum);
			$("#deptName").val(list1.deptName);
			$("#deptId").val(list1.deptId);
			$("#provrId").val(list1.provrId);
			$("#provrNm").val(list1.provrNm);
			var list = data.respBody.pursOrdrSub;

			$("#jqgrids").jqGrid('clearGridData');
			$("#jqgrids").jqGrid('setGridParam', {
				datatype: 'local',
				data: list, //newData是符合格式要求的重新加载的数据
			}).trigger("reloadGrid")
			sumAdd()
		},
		error: function() {
			alert("error")
		}
	})
}
$(function() {
	$('.ctrlc_v').click(function() {
		$(".ctrlc_v").addClass("gray");
		$(".ctrlYes").removeClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
		$("#jqgrids").setColProp("invtyNm", {
			editable: true
		});
		$("#jqgrids").setColProp("spcModel", {
			editable: true
		});
		$("#jqgrids").setColProp("measrCorpNm", {
			editable: true
		});
		$("#jqgrids").setColProp("cntnTaxUprc", {
			editable: true
		});
		$("#jqgrids").setColProp("noTaxAmt", {
			editable: true
		});
		$("#jqgrids").setColProp("taxAmt", {
			editable: true
		});
		$("#jqgrids").setColProp("bxRule", {
			editable: true
		});
		$("#jqgrids").setColProp("crspdBarCd", {
			editable: true
		});
		$("#jqgrids").setColProp("noTaxUprc", {
			editable: true
		});
		$("#jqgrids").setColProp("baoZhiQiDt", {
			editable: true
		});
		var obj = $("#jqgrids");
		//获取grid表中所有的rowid值
		var rowIds = obj.getDataIDs();
		for(var i = 0; i < rowIds.length; i++) {
			$('#jqgrids').jqGrid('editRow', rowIds[i], true);

		}
		//		ctrl_v(e,cellArr,cb)
	})
	$('.ctrlYes').click(function() {
		$(".ctrlc_v").removeClass("gray");
		$(".ctrlYes").addClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
		submit()
		sumAdd()
	})
})

function submit() {
	var obj = $("#jqgrids");
	//获取grid表中所有的rowid值
	var rowids = obj.getDataIDs();
	for(var i = 0; i < rowids.length; i++) {
		var goods = $("#" + rowids[i] + "_invtyEncd").val()
		var qty = $("#" + rowids[i] + "_qty").val()
		jQuery("#jqgrids").saveRow(rowids[i], false, 'clientArray');
		var rowid = rowids[i]
		if(goods == '') {
			continue;
		} else if(goods != '') {
			ctrl_GetGoodsInfo(rowid, goods)
		}
		if(qty == '') {
			continue;
		} else if(qty != '') {
			purs_SetNums(rowid, 'qty');
		}
	}
	$("#jqgrids").setColProp("invtyNm", {
		editable: false
	});
	$("#jqgrids").setColProp("spcModel", {
		editable: false
	});
	$("#jqgrids").setColProp("measrCorpNm", {
		editable: false
	});
	$("#jqgrids").setColProp("cntnTaxUprc", {
		editable: false
	});
	$("#jqgrids").setColProp("noTaxAmt", {
		editable: false
	});
	$("#jqgrids").setColProp("taxAmt", {
		editable: false
	});
	$("#jqgrids").setColProp("bxRule", {
		editable: false
	});
	$("#jqgrids").setColProp("crspdBarCd", {
		editable: false
	});
	$("#jqgrids").setColProp("noTaxUprc", {
		editable: false
	});
	$("#jqgrids").setColProp("baoZhiQiDt", {
		editable: false
	});
}
$(function() {
	$('#jqgrids').pasteFromTable();
})