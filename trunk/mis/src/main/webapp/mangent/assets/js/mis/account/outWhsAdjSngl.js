
var mType = 0;

$(function() {
	$('button').addClass("gray");
	$('.addOrder').removeClass("gray");
	$('button').attr('disabled', false);
	$(".gray").attr('disabled', true);
})

// 点击增加按钮，执行的操作
$(function() {
	$('.addOrder').click(function() {
		$(".saveOrder").removeClass("gray");
		$(".upOrder").removeClass("gray"); //新增后放弃能用
		$('.addOrder').addClass("gray");
		$('.exportExcel').addClass("gray");
		$('.editOrder').addClass("gray");
		$('.delOrder').addClass("gray");
		$('.remember').addClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
		mType = 1;

		$("#jqgrids").jqGrid('setGridParam', {}).trigger("reloadGrid")
		$("#mengban").hide();
		$("#gbox_jqGrids").hide();
		$("#gbox_jqgrids").show();

		$(".inputText").val("");
		$("input[name='orderNumber']").val("")
		var time = BillDate();
		$("input[name=formTm]").val(time);
	});
})
$(function(){
	$(".upOrder").click(function() {
		window.location.reload()
	})
})
//打开存货档案后点击确定取消
$(function() {
	//确定
	$(".addWhs").click(function() {
		//到货单
		//获得行号
		var rowid = $("#jqgrids").jqGrid('getGridParam', 'selrow');

		//	仓库档案
		//	获得行号
		var gr = $("#whs_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDatas = $("#whs_jqgrids").jqGrid('getRowData', gr);

		//	存货档案
		//	获得行号
		var ids = $("#insert_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowData = $("#insert_jqgrids").jqGrid('getRowData', ids);

		$("#" + rowid + "_whsEncd").val(rowDatas.whsEncd)
		$("#" + rowid + "_invtyEncd").val(rowData.invtyEncd)
		$("#jqgrids").setRowData(rowid, {
			whsNm: rowDatas.whsNm
		})
		$("#whsDocList").css("opacity", 0);
		$("#insertList").css("opacity", 0);
		$("#purchaseOrder").show();
	})
	//	取消
	$(".cancel").click(function() {
		$("#whsDocList").css("opacity", 0);
		$("#insertList").css("opacity", 0);
		$("#purs_list").css("opacity", 0);
		$("#purchaseOrder").show();
		//到货单
		//获得行号
		var rowid = $("#jqgrids").jqGrid('getGridParam', 'selrow');

		$("#" + rowid + "_whsNm").val("");
		$("#" + rowid + "_whsEncd").val("");
		$("#" + rowid + "_invtyEncd").val("")
	})
})
function getAllData() {
	//拿到grid对象
	var obj = $("#jqgrids");
	//获取grid表中所有的rowid值
	var rowIds = obj.getDataIDs();
	//初始化一个数组arrayData容器，用来存放rowData
	var arrayData = new Array();
	if(rowIds.length > 0) {
		for(var i = 0; i < rowIds.length; i++) {
//			obj.getRowData(rowIds[i]).isNtRtnGoods = 0;
			if(obj.getRowData(rowIds[i]).whsEncd == "") {
				continue;
			} else {
				//rowData=obj.getRowData(rowid);//这里rowid=rowIds[i];
				arrayData.push(obj.getRowData(rowIds[i]));
			}
		}
	}
	return arrayData;
}
//初始化表格
$(function() {
	allHeight()
	$("#jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json",
		colNames: ['仓库编码', '仓库名称','存货编码', '存货名称', '规格型号', '计量单位','金额','箱规', '批次'], //jqGrid的列显示名字
		colModel: [{
				name: "whsEncd",
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: "whsNm",
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: "invtyEncd",
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: "invtyNm",
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: "spcModel", //科目名称
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'measrCorpNm',
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: "amt",
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'bxRule',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'batNum',
				align: "center",
				editable: true,
				sortable: false,
			},
		],
		rowNum: 99999999,
		loadonce: false,
		rownumbers: true,
		autowidth: true,
		multiselect: true, //复选框 
		caption: '出库调整单',
		pager: "#jqgridPager",
		altclass: true,
		viewrecords: true,
		height: height,
		footerrow: true,
		autoScroll: true,
		shrinkToFit: false,
		forceFit: true,
		sortorder: "desc",
		cellEdit: true,
		cellsubmit: "clientArray",

		afterEditCell: function(rowid, cellname, val, iRow, iCol) {
			$(".saveOrder").addClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
			if(cellname == "whsEncd") {
				$("#" + rowid + "_whsEncd").bind("dblclick", function() {
					$("#whsDocList").show();
					$("#whsDocList").css("opacity", 1);
					$("#purs_list").hide();
					$("#insertList").hide();
					$("#purchaseOrder").hide();
					$(".addWhs").removeClass("gray") //确定可用
					$(".find").removeClass("gray") //查询可用
				});
			}
			if(cellname == "invtyEncd") {
				//获得行数据
				var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
				if(rowDatas.whsEncd == "") {
					alert("请先输入仓库档案");
					$("#" + rowid + "_invtyEncd").attr('disabled', 'disabled');
				} else {
					$("#" + rowid + "_invtyEncd").bind("dblclick", function() {
						$("#insertList").show();
						$("#insertList").css("opacity", 1);
						$("#whsDocList").hide();
						$("#purchaseOrder").hide();
						$("#purs_list").hide();
						$(".cancel").removeClass("gray")
						$(".addWhs").removeClass("gray") //确定可用
						$(".find").removeClass("gray") //查询可用
						$('button').attr('disabled', false);
						$(".gray").attr('disabled', true)
					});
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
			if(cellname == "whsEncd") {
				GetWhsInfo(rowid, val);
			}
			//获取商品信息
			if(cellname == "invtyEncd") {
				GetGoodsInfo(rowid, val);
			}
			if((cellname == "amt")) {
				var list = getAllData();
				var amt = 0;
				for(var i = 0; i < list.length; i++) {
					amt += parseFloat(list[i].amt);
				};
				if(isNaN(amt)) {
					amt = 0
				}
				amt = precision(amt,2)
				$("#jqgrids").footerData('set', {
					"whsEncd": "本页合计",
					"amt": amt
				});
			}
			var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
			if(rowDatas.invtyEncd == "") {
				$("#jqgrids").setRowData(rowid, {
					invtyNm: "",
					spcModel: "",
					bxRule: "",
					measrCorpNm: "",
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

//根据存货编码查询存货详细信息
function GetGoodsInfo(rowid, goods) {
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": goods
		}
	};
	var postData = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/purc/InvtyDoc/selectInvtyDocByInvtyDocEncd',
		type: 'post',
		data: postData,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		error: function() {
			alert("获取数据错误");
		}, //错误执行方法
		success: function(data) {
			var list = data.respBody

			//设置页面数据展示
			$("#jqgrids").setRowData(rowid, {
				invtyNm: list.invtyNm,
				spcModel: list.spcModel,
				measrCorpNm: list.measrCorpNm
			});
		}
	})
}

// 点击保存，传送数据给后台
$(function() {
	$(".saveOrder").click(function() {
		if(mType == 1) {
			SaveNewData();
		}
		if(mType == 2) {
			SaveModifyData();
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
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
	});
})

function IsCheckValue(custId,recvSendCateId, listData) {
	var count = getInvtyData();
	var custId1 = custId.split(",");
	if(custId == "") {
		alert("客户不能为空")
		return false;
	} else if(custId1.length>1) {
		alert("客户不能选多个")
		return false;
	} else if(recvSendCateId == "") {
		alert("收发类别编码不能为空")
		return false;
	} else if(listData.length<count.length){
		alert("明细中缺少仓库");
		return false;
	} else if(listData.length != 0) {
		for(var i = 0; i < listData.length; i++) { 
			if(listData[i].invtyEncd == "") {
				alert("存货不能为空")
				return false;
			}else if(listData[i].amt == "") {
				alert("金额不能为空")
				return false;
			} else if(listData[i].amt <0) {
				alert("金额不能小于0")
				return false;
			}
		}
	}
	return true;
}

//点完修改后执行的url
function SaveModifyData() {
	var listData = getJQAllData();
	formNum = $("input[name='formNum']").val();//单据号
	var formTm = $("input[name='formTm']").val(); 
	var accNum = $("#user").val();
	var custId = $("#custId").val();
	var recvSendCateId = $("#recvSendCateId").val();
	var whsEncd = $("#whsEncd").val();
	var memo = $("input[name='memo']").val();
	var loginName = localStorage.loginName

	if(IsCheckValue(custId,recvSendCateId, listData) == true) {

		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				'formNum': formNum,
				'formTm': formTm,
				'custId': custId,
				'recvSendCateId': recvSendCateId,
				'setupPers': loginName,
				"outIntoWhsInd":0,
				'outIntoList': listData,
				'memo': memo
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			url: url + '/mis/account/outIntoWhsAdjSngl/updateOutIntoWhsAdjSngl',
			type: 'post',
			data: saveData,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
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
				alert("error");
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
			if(obj.getRowData(rowIds[i]).whsEncd == "") {
				continue;
			} else {
				arrayData.push(obj.getRowData(rowIds[i]));
			}
		}
	}
	return arrayData;
}
var formNum;
//第一次保存
function SaveNewData() {
	var listData = getJQAllData();
	var formTm = $("input[name='formTm']").val(); //单据号
	var accNum = $("#user").val();
	var custId = $("#custId").val();
	var recvSendCateId = $("#recvSendCateId").val();
	var whsEncd = $("#whsEncd").val();
	var memo = $("input[name='memo']").val();
	var loginName = localStorage.loginName
	
	//判断页面是否有值为空
	if(IsCheckValue(custId,recvSendCateId, listData) == true) {

		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				'formNum': "",
				'formTm': formTm,
				'custId': custId,
				'recvSendCateId': recvSendCateId,
				'setupPers': loginName,
				'outIntoList': listData,
				'bookEntryPers':"",
				"outIntoWhsInd":0,
				'memo': memo
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			url: url + '/mis/account/outIntoWhsAdjSngl/insertOutIntoWhsAdjSngl',
			type: 'post',
			data: saveData,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			success: function(data) {
				alert(data.respHead.message)
				formNum = data.respBody.formNum;
				$("input[name='formNum']").val(data.respBody.formNum); //订单编码
				if(data.respHead.isSuccess == true) {
					$('button').removeClass("gray");
					$('.saveOrder').addClass("gray");
					$('.upOrder').addClass("gray");
					$('.noTo').addClass("gray");
					$('button').attr('disabled', false);
					$(".gray").attr('disabled', true)
					$("#mengban").show();
				}
			},
			error: function() {
				alert("error");
			} //错误执行方法
		})
	}
}

$(function() {
	//是否审核		
	function ntChk(x) {
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				"list": [{
					"formNum": formNum,
					"isNtChk": x,
					"chkr": loginName
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
				alert("error")
			}
		})
	}
	//审核
	$(".toExamine").click(function() {
		ntChk(1)
	});
	//弃审
	$(".noTo").click(function() {
		ntChk(0)
	})
})

// 点击删除按钮，执行的操作
$(function() {
	$('.delOrder').click(function() {
		var deleteAjax = {
			"reqHead": reqhead,
			"reqBody": {
				"formNum": formNum
			}
		}
		var deleteData = JSON.stringify(deleteAjax);
		if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + '/mis/account/outIntoWhsAdjSngl/deleteOutIntoWhsAdjSngl',
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
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
	formNum = localStorage.formNum;
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"formNum": formNum,
			"pageNo": 1,
			"pageSize": 500
		}
	};
	var saveData = JSON.stringify(savedata);
	var myDate = {};
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/account/outIntoWhsAdjSngl/selectOutIntoWhsAdjSnglById',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			var list1 = data.respBody;
			$("input[name='formNum']").val(list1.formNum);
			$("input[name='formTm']").val(list1.formTm); //单据号
			$("#recvSendCateId").val(list1.recvSendCateId);
			$("#recvSendCateNm").val(list1.recvSendCateNm);
			$("#custId").val(list1.custId);
			$("#custNm").val(list1.custNm);
			$("input[name='memo']").val(list1.memo);

			var list = data.respBody.outIntoList;

			for(var i = 0; i < list.length; i++) {
				$("#jqgrids").setRowData(i + 1, {
					invtyEncd: list[i].invtyEncd,
					invtyNm: list[i].invtyNm,
					whsEncd: list[i].whsEncd,
					whsNm: list[i].whsNm,
					spcModel: list[i].spcModel,
					measrCorpNm: list[i].measrCorpNm,
					bxRule: list[i].bxRule,
					amt: list[i].amt,
					batNum:list[i].batNum,
					memo: list[i].memo
				});
			}

		},
		error: function() {
			alert("error")
		}
	})
}