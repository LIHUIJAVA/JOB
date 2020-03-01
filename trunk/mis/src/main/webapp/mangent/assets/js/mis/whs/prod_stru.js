var mType = 0;
var arr = 0;
var chk1 = "/mis/whs/prod_stru/updatePStruChk";
var chk2 = "/mis/whs/prod_stru/updatePStruNoChk";

//刚开始时可点击的按钮
$(function() {
	$('button').addClass("gray");
	//	$(".refer").removeClass("gray") //参照
	$('.addOrder').removeClass("gray") //增加
	$('button').attr('disabled', false);
	$(".gray").attr('disabled', true)
	//1.存货
	$(document).on('keypress', '#momEncd', function(event) {
		if(event.keyCode == '13') {
			$('#momEncd').blur();
		}
	})

	$(document).on('blur', '#momEncd', function() {
		var momEncd = $("#momEncd").val();
		dev({
			doc1: $("#momEncd"),
			doc2: $("#momNm"),
			showData: {
				"invtyEncd": momEncd
			},
			afunction: function(data) {
				$("#momNm").val(data.respBody.invtyNm)
				$("#momSpc").val(data.respBody.spcModel)
				$("#measrCorp").val(data.respBody.measrCorpNm)
			},
			url: url + "/mis/purc/InvtyDoc/selectInvtyDocByInvtyDocEncd"
		})
	});
})

// 点击增加按钮，执行的操作
$(function() {
	$('.addOrder').click(function() {
		localStorage.removeItem('invtyEncd');

		$('button').addClass("gray");
		$(".saveOrder").removeClass("gray");
		$(".upOrder").removeClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)

		mType = 1;

		$("#jqgrids").jqGrid('setGridParam', {}).trigger("reloadGrid")
		$("#mengban").hide();
		$("#gbox_jqGrids").hide();
		$("#gbox_jqgrids").show();

		$("#jqgrids").jqGrid('clearGridData');
	    $("#jqgrids").jqGrid('setGridParam', {
	       url: '../../assets/js/json/order.json',
	       datatype: "json",
	    }).trigger("reloadGrid")
	    $("input").val("");
	    $(".encds").val("");
	    $(".names").val("");

	});
})

//点击表格图标显示存货列表--母配套件
$(function() {
	$(document).on('click', '.biaoge', function() {
		arr = 1;
		$("#insertList").show();
		$("#insertList").css("opacity", 1);
		$("#purchaseOrder").hide();
		$('#insertList>div>div>div button').attr('disabled', false).removeClass('gray');
	})
})

//打开仓库/存货档案后点击确定取消
$(function() {
	//确定
	$(".addWhs").click(function() {

		//本单
		//获得行号
		var rowid = $("#jqgrids").jqGrid('getGridParam', 'selrow');
		//	获得存货行号
		var ids = $("#insert_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowData = $("#insert_jqgrids").jqGrid('getRowData', ids);
		$("#whsDocList").css("opacity", 0);
		$("#insertList").css("opacity", 0);
		$("#purchaseOrder").show();
		var a = rowData.invtyEncd;
		$("#" + rowid + "_subEncd").val(a)
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				"invtyEncd": a
			}
		};
		var postData = JSON.stringify(data);
		$.ajax({
			url: url + '/mis/purc/InvtyDoc/selectInvtyDocByInvtyDocEncd',
			type: 'post',
			data: postData,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8', //错误执行方法
			success: function(data) {
				var list = data.respBody
				if(arr == 1) {
					$('#momSpc').val(list.spcModel);
					$("#momEncd").val(list.invtyEncd);
					$("#momNm").val(list.invtyNm);
					$("#measrCorp").val(list.measrCorpNm);
				} else if(arr == 2) {
					//设置页面数据展示
					$("#jqgrids").setRowData(rowid, {
						subNm: list.invtyNm,
						subSpc: list.spcModel,
						measrCorpNm: list.measrCorpNm,
						//					measrCorpId: list.measrCorpId,
						bxRule: list.bxRule
					});
				}
	
			},
			error: function() {
				console.log("获取数据错误");
			},
		})

	})
	//	取消
	$(".cancel").click(function() {
		$("#whsDocList").css("opacity", 0);
		$("#insertList").css("opacity", 0);
		$("#purchaseOrder").show();
		//本单
		//获得行号
		var rowid = $("#insert_jqgrids").jqGrid('getGridParam', 'selrow');

		$("#" + rowid + "_whsNm").val("");
		$("#" + rowid + "_whsEncd").val("");
		$("#" + rowid + "_invtyEncd").val("")
	})

})

//初始化表格
$(function() {
	allHeight()
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	$("#jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json",
		colNames: ['子件编码', '子件名称', '子件规格', '计量单位编码', '计量单位', '箱规', '子件数量', '备注'], //jqGrid的列显示名字
		colModel: [{
				name: 'subEncd',
				align: "center",
				index: 'invdate',
				editable: true,
			},
			{
				name: 'subNm',
				align: "center",
				index: 'invdate',
				editable: false
			},
			{
				name: 'subSpc',
				align: "center",
				index: 'invdate',
				editable: false
			},
			{
				name: 'measrCorp',
				align: "center",
				index: 'invdate',
				editable: false,
				hidden: true
			},
			{
				name: 'measrCorpNm',
				editable: false,
				align: 'center',

			},
			{
				name: 'bxRule',
				align: "center",
				index: 'invdate',
				editable: false
			},
			{
				name: 'subQty',
				align: "center",
				index: 'invdate',
				editable: true,
				editrules: {
					number: true
				}

			},
			{
				name: 'memo',
				align: "center",
				index: 'invdate',
				editable: true
			}
		],
		loadonce: false,
		autowidth: true,
		multiselect: true, //复选框 
		caption: '子件明细',
		altclass: true,
		viewrecords: true,
		rowNum: 100, //一页显示多少条
		height: height,
		autoScroll: true,
		pager:"#prod_jqgridPager",
		shrinkToFit: false,
		forceFit: true,
		sortable: false,
		cellEdit: true,
		rownumWidth: 20, //序列号列宽度
		rownumbers: true,
		cellsubmit: "clientArray",

		//在编辑状态时
		afterEditCell: function(rowid, cellname, val, iRow, iCol) {
			$(".saveOrder").addClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
			if(cellname == "subEncd") {
				arr = 2;
//				$("#" + rowid + "_" + cellname).bind('keyup', function() {
//					findGrid(rowid, cellname, val);
//				})
				$("#" + rowid + "_subEncd").bind("dblclick", function() {
					arr = 2;
					$("#insert_jqgrids").show();
					$("#insertList").css("opacity", 1);
					$("#purchaseOrder").hide();
					$("#purs_list").hide();
					$('.find').attr('disabled', false);
					$('.find').removeClass('gray');
					$('.addWhs').attr('disabled', false);
					$('.addWhs').removeClass('gray');
					$('.cancel').attr('disabled', false);
					$('.cancel').removeClass('gray');
				});
			}

		},
		//离开编辑状态
		afterRestoreCell: function(rowid, cellname, val, iRow, iCol) {
			$(".saveOrder").removeClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true)

		},
		//回车保存
		afterSaveCell: function(rowid, cellname, val, iRow, iCol) {
			$(".saveOrder").removeClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
			//获取商品信息
			if(cellname == "subEncd") {
				GetGoodsInfo1(rowid, val);
			}
		}
	});
	//		鼠标指向变色
	$("table").delegate(".jqgrow", "mouseover", function() {
		$(this).css("background-color", "rgb(252, 248, 227)");
	});
	$("table").delegate(".jqgrow", "mouseout", function() {
		$(this).css("background-color", "");
	});
	$("#jqgrids").jqGrid('navGrid', '#prod_jqgridPager', {
		edit: false,
        add: false,
        del: false,
        search: false,
        refresh:false,
	}).navButtonAdd('#prod_jqgridPager', {
		caption: "",
		buttonicon: "ui-icon-trash",
		onClickButton: function() {
			//删除一行操作
			removeRows();
		},
		position: "first"
	})
});
//根据存货编码查询存货详细信息
function GetGoodsInfo1(rowid, goods) {
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": goods
		}
	};
	if(goods) {
		var postData = JSON.stringify(data);
		$.ajax({
			url: url + '/mis/purc/InvtyDoc/printingInvtyDocList',
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
				if(list.length == 0) {
					$("#jqgrids").setRowData(rowid, {
						subNm: "",
						subSpc: "",
						measrCorpNm: "",
						bxRule: "",
					});
				}
				$("#" + rowid + "_invtyEncd").val(list[0].invtyEncd);
				if(list.length == 1) {
					$("#jqgrids").setRowData(rowid, {
						subNm: list[0].invtyNm,
						subSpc: list[0].spcModel,
						measrCorpNm: list[0].measrCorpNm,
						bxRule: list[0].bxRule,
					});
				} else {
					for(var i = 1; i < list.length; i++) {
						$("#jqgrids").setRowData(rowid, {
							subNm: list[0].invtyNm,
							subSpc: list[0].spcModel,
							measrCorpNm: list[0].measrCorpNm,
							bxRule: list[0].bxRule,
						});
						//设置页面数据展示
						$("#jqgrids").setRowData(++rowid, {
							subNm: list[i].invtyNm,
							subSpc: list[i].spcModel,
							measrCorpNm: list[i].measrCorpNm,
							bxRule: list[i].bxRule,
						});
					}
				}
			}
		})
	}
}
function removeRows(){
	var gr = $("#jqgrids").jqGrid('getGridParam', 'selarrrow');
	if(gr.length==0) {
		alert("请选择要删除的行");
		return;
	} else {
		for (var i = 0; i < gr.length+1; i++) {
			$("#jqgrids").jqGrid("delRowData", gr[0]);
		}
	}
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
		$('button').addClass('gray')
		$(".saveOrder").removeClass("gray");
		$('.upOrder').removeClass("gray");
		$('.editOrder').addClass("gray"); //点击修改后 修改不能用
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true);
		/*-----*/
		$("#gbox_jqGrids").hide();
		$("#gbox_jqgrids").show();
		//		var afterUrl = window.location.search.substring(1);
		//		var b = [];
		//		b = afterUrl;
		//		if(b == 1) {
		//			var intoWhsSnglId = localStorage.intoWhsSnglId;
		//			getIntoWhsSnglId(intoWhsSnglId);
		//		}
	});
})

function getJQAllData() {
	//拿到grid对象
	var obj = $("#jqgrids");
	//获取grid表中所有的rowid值
	var rowIds = obj.getDataIDs();
	//初始化一个数组arrayData容器，用来存放rowData
	var arrayData = new Array();
	if(rowIds.length > 0) {
		for(var i = 0; i < rowIds.length; i++) {
			if(obj.getRowData(rowIds[i]).subEncd == "") {
				continue;
			} else {
				arrayData.push(obj.getRowData(rowIds[i]));
			}
		}
	}
	return arrayData;
}

function IsCheckValue(momEncd, listData) {
	if(momEncd == '') {
		alert("母件编码不能为空")
		return false;
	} else if(listData.length == 0) {
		alert("子件编码不能为空")
		return false;
	} else if(listData.length != 0) {
		for(var i = 0; i < listData.length; i++) {
			if(listData[i].subQty == "") {
				alert("子件数量不能为空")
				return false;
			}
		}
	}
	return true;
}

//保存修改后的数据
function SaveModifyData() {
	var listData = getJQAllData();
	//	alert(6)
	var momEncd = $("input[name='momEncd']").val();
	var momNm = $("input[name='momNm']").val();
	var momSpc = $("input[name='momSpc']").val();
	//	var measrCorpId = $("input[name='measrCorpId']").val();
	//	var measrCorp = $("input[name='measrCorp']").val();
	var edComnt = $("input[name='edComnt']").val();
	var memo = $("input[name='memo']").val();
	var ordrNum = $("input[name='ordrNum']").val();

	//判断页面是否有值为空
	if(IsCheckValue(momEncd, listData) == true) {

		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				// 主表
				'momEncd': momEncd,
				'edComnt': edComnt,
				'momNm': momNm,
				'momSpc': momSpc,
				//				'measrCorp': measrCorpId,
				'memo': memo,
				'ordrNum': ordrNum,
				'list': listData // 子表
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			url: url + '/mis/whs/prod_stru/updateProdStru',
			type: 'post',
			data: saveData,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			success: function(data) {
				alert(data.respHead.message)
				if(data.respHead.isSuccess == true) {
					$(".addOrder").removeClass("gray");
					$(".editOrder").removeClass("gray");
					$(".delOrder").removeClass("gray");
					$(".toExamine").removeClass("gray");
					$(".search").removeClass("gray");
					$('button').attr('disabled', false);
					$(".gray").attr('disabled', true);
					$("#mengban").show();
				}
			},
			error: function() {
				console.log('错误');
			} //错误执行方法
		})

	}
}

$(function() {
	// 点击删除按钮，执行的操作
	$('.delOrder').click(function() {
		var ordrNum = $("input[name='ordrNum']").val();
		var deleteAjax = {
			"reqHead": reqhead,
			"reqBody": {
				'ordrNum': ordrNum
			}
		}
		var deleteData = JSON.stringify(deleteAjax);
		if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + '/mis/whs/prod_stru/deleteAllProdStru',
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(remover) {
					//							console.log(remover)
					alert(remover.respHead.message)
					//				alert('删除成功')
					window.location.reload()
				},
				error: function() {
					console.log("删除失败")
				}
			});
		}
	});
})

//保存
function SaveNewData() {
	var listData = getJQAllData();
	//	alert(6)
	var momEncd = $("input[name='momEncd']").val();
	var momNm = $("input[name='momNm']").val();
	var momSpc = $("input[name='momSpc']").val();
	//	var measrCorpId = $("input[name='measrCorpId']").val();
	//	var measrCorp = $("input[name='measrCorp']").val();
	var edComnt = $("input[name='edComnt']").val();
	var memo = $("input[name='memo']").val();
	//	var ordrNum = $("input[name='ordrNum']").val();

	//判断页面是否有值为空
	if(IsCheckValue(momEncd, listData) == true) {
		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				// 主表
				'momEncd': momEncd,
				'edComnt': edComnt,
				'momNm': momNm,
				'momSpc': momSpc,
				//				'measrCorp': measrCorpId,
				'memo': memo,
				//				'ordrNum':ordrNum,
				'list': listData // 子表
			}
		};
		var saveData = JSON.stringify(savedata);
		console.log(saveData)
		$.ajax({
			url: url + '/mis/whs/prod_stru/insertProdStru',
			type: 'post',
			data: saveData,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			success: function(data) {
				alert(data.respHead.message)
				var ordrNum = data.respBody.ordrNum;
				$("input[name='ordrNum']").val(data.respBody.ordrNum);
				if(data.respHead.isSuccess == true) {
					$('button').removeClass("gray");
					$('.addOrder').removeClass("gray");
					$('.saveOrder').addClass("gray");
					$('.upOrder').addClass("gray");
					$('.noTo').addClass("gray");
					$('button').attr('disabled', false);
					$(".gray").attr('disabled', true)
					$("#mengban").show();
				}

			},
			error: function() {
				alert('保存失败');
			} //错误执行方法
		})
	}
}
$(function() {
	//审核
	$(".toExamine").click(function() {
		$("button").addClass("gray");
		$(".addOrder").removeClass("gray");
		$(".noTo").removeClass("gray");
		$(".search").removeClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
		ntChk(1, chk1)
	});
	//弃审
	$(".noTo").click(function() {
		$("button").removeClass("gray");
		$(".upOrder").addClass("gray");
		$(".noTo").addClass("gray");
		$(".saveOrder").addClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
		ntChk(0, chk2)
	})
})

//				是否审核		
function ntChk(x, chk) {
	var ordrNum = $("input[name='ordrNum']").val()
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"list": [{
				'ordrNum': ordrNum,
				"isNtChk": x
			}]
		}
	};
	var Data = JSON.stringify(data);
	console.log(Data)
	$.ajax({
		url: url + chk,
		type: 'post',
		data: Data,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		success: function(data) {
			console.log(data)
			alert(data.respHead.message)
		},
		error: function() {
			console.log(error)
		}
	})
}

//查询产品结构详细信息
$(function() {

	var afterUrl = window.location.search.substring(1);
	var b = [];
	b = afterUrl;
	var a = 1
	if(a == b) {
		chaxun()
	}
})
function chaxun() {
	$("#mengban").show();
//		$("#gbox_jqgrids").hide();
	$(".addOrder").addClass("gray");
	$(".editOrder").removeClass("gray");
	$('button').attr('disabled', false);
	$(".gray").attr('disabled', true)

	var ordrNum = localStorage.ordrNum;
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"ordrNum": ordrNum,
			"pageNo": 1,
			"pageSize": 500
		}
	};
	var saveData = JSON.stringify(savedata);
	var myDate = {};
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/whs/prod_stru/query',
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
			var list = data.respBody.list;
			var data = data.respBody
			$("input[name='momEncd']").val(data.momEncd);
			$("input[name='momNm']").val(data.momNm);
			//				$("input[name='intoWhsSnglId']").val(list.intoWhsSnglId);
			$("input[name='momSpc']").val(data.momSpc);
			$("input[name='measrCorp']").val(data.measrCorpNm);
			$("input[name='edComnt']").val(data.edComnt);
			$("input[name='memo']").val(data.memo);
			$("input[name='ordrNum']").val(data.ordrNum);
			for(var i = 0; i < list.length; i++) {
				$("#jqgrids").setRowData(i + 1, {
					subEncd: list[i].subEncd,
					subNm: list[i].subNm,
					subSpc: list[i].subSpc,
					measrCorp: list[i].measrCorp,
					measrCorpNm: list[i].measrCorpNm,
					bxRule: list[i].bxRule,
					subQty: list[i].subQty,
					memo: list[i].memo
				});
			}
			if(data.isNtChk == 0) {
				$(".toExamine").removeClass("gray");
				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true)
			}else if(data.isNtChk == 1) {
				$(".editOrder").addClass("gray");
				$(".noTo").removeClass("gray");
				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true)
			}
		},
		error:function() {
			console.log(error)
		}
	})
}
