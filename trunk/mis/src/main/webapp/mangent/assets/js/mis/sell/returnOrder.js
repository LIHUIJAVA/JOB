//退货单
var mType = 0;
var refer = 0;
$(function() {
	getData();
	$("#jqgrids").setColProp("whsEncd", {
		editable: false
	});
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
	$("#jqgrids").setColProp("intlBat", {
		editable: false
	});
	$("#jqgrids").setColProp("prdcDt", {
		editable: false
	});
	$("#jqgrids").setColProp("batNum", {
		editable: false
	});
	$("#jqgrids").setColProp("projEncd", {
		editable: false
	});
	$('button').addClass("gray");
	$('.addOrder').removeClass("gray");
	$(".refer").removeClass("gray") //参照
	$(".addWhs").removeClass("gray") //确定
	$(".print1").addClass("gray")
	$(".cancel").removeClass("gray") //取消
	$('button').attr('disabled', false);
	$(".gray").attr('disabled', true)

	var afterUrl = window.location.search.substring(1);
	var b = [];
	b = afterUrl;
	if(b == 1) {
		var isNtChk = localStorage.isNtChk;
		if(isNtChk == "是") {
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

$(function() {
	$('.print1').click(function() {
		var rtnGoodsId = $("input[name='rtnGoodsId']").val()
		if(rtnGoodsId == '') {
			alert("不存在的单据")
		} else {
			localStorage.setItem("rtnGoodsId", rtnGoodsId)
			window.open("../../Components/sell/print_returnOrder.html?1");
		}
	})
})
// 点击增加按钮，执行的操作
$(function() {
	$('.addOrder').click(function() {
		$("#jqgrids").setColProp("whsEncd", {
			editable: true
		});
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
		$("#jqgrids").setColProp("intlBat", {
			editable: true
		});
		$("#jqgrids").setColProp("prdcDt", {
			editable: true
		});
		$("#jqgrids").setColProp("batNum", {
			editable: true
		});
		$("#jqgrids").setColProp("projEncd", {
			editable: true
		});
		$(".delOrder").addClass("gray");
		$(".editOrder").addClass("gray");
		$(".toExamine").addClass("gray");
		$(".noTo").addClass("gray");
		$(".saveOrder").removeClass("gray");
		$(".upOrder").removeClass("gray");
		//		$(".ctrlc_v").removeClass("gray");
		$(".bat_true").removeClass("gray");
		$(".ctrlc_v").removeClass("gray");
		$('.addOrder').addClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
		$(".bat_true").attr('disabled', false)

		mType = 1;

		$("#jqgrids").jqGrid('setGridParam', {}).trigger("reloadGrid")
		$("#mengban").hide();
		$("#gbox_jqGrids").hide();
		$("#gbox_jqgrids").show();

		$(".inputText").val("");
		$("input[name=sellType]").val("普通销售");

		var time = BillDate();
		//入库日期
		$("input[name=rtnGoodsDt]").val(time);
		$("#jqgrids").jqGrid('clearGridData');
		$("#jqgrids").jqGrid('setGridParam', {
			url: '../../assets/js/json/order.json',
			datatype: "json",
		}).trigger("reloadGrid")
	});
})
$(function() {
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
	$('.custId').bind('keypress', function(event) {
		if(event.keyCode == '13') {
			$('.custId').blur();
		}
	})
	$(".custId").blur(function() {
		var custId = $(".custId").val();
		dev({
			doc1: $(".custId"),
			doc2: $(".custNm"),
			showData: {
				"custId": custId,
			},
			afunction: function(data) {
				var custNm = data.respBody.custDoc[0].custNm;
				var addr = data.respBody.custDoc[0].delvAddr;
				$(".custNm").val(custNm);
				$("#addr").val(addr)

			},
			url: url + "/mis/purc/CustDoc/selectCustDocByCustId"
		})
	})
})

var toFormTypEncd;
//点击参照按钮，执行的操作
$(function() {
	$(".refer").click(function() {
		$("#jqgrids").trigger("reloadGrid");
		mType = 1;
		$('button').addClass("gray");
		$('.addOrder').removeClass("gray") //增加
		$(".sure").removeClass("gray") //确定
		$(".cancel").removeClass("gray") //取消
		$(".searcher").removeClass("gray") //查询

		$(".saveOrder").removeClass("gray");
		$(".upOrder").removeClass("gray");
		$('.refer').removeClass("gray")
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
		$("#mengban").hide();

		$("#whsDocList").hide();
		$("#insertList").hide();
		$("#purchaseOrder").hide();
		$("#purs_list").show();
		$("#purs_list").css("opacity", 1)
	})
	$(".sure").click(function() {
		$("#jqgrids").setColProp("whsEncd", {
			editable: true
		});
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
		$("#jqgrids").setColProp("intlBat", {
			editable: true
		});
		$("#jqgrids").setColProp("prdcDt", {
			editable: true
		});
		$("#jqgrids").setColProp("batNum", {
			editable: true
		});
		$("#jqgrids").setColProp("projEncd", {
			editable: true
		});
		var gr = $("#deteil_list").jqGrid('getGridParam', 'selarrrow');
		//获得行数据
		var encd = [];
		for(var i = 0; i < gr.length; i++) {
			rowDatas = $("#deteil_list").jqGrid('getRowData', gr[i]);
			encd[i] = rowDatas;
		}
		if(encd.length == 0) {
			alert("请选择明细")
		} else {
			$("#purs_list").css("opacity", 0);
			$("#purchaseOrder").show();
			var num = []
			for(var i = 0; i < encd.length; i++) {
				delete encd[i].qty;
				var keyMap = {
					rtnblQty: 'qty',
					baoZhiQiDt: 'baoZhiQi',
					ordrNum: 'toOrdrNum'
				}
				var objs = Object.keys(encd[i]).reduce((newData, key) => {
					let newKey = keyMap[key] || key
					newData[newKey] = encd[i][key]
					return newData
				}, {})
				num.push(objs)
			}
			referSure(num)
			$("#jqgrids").jqGrid('clearGridData');
			$("#jqgrids").jqGrid('setGridParam', {
				datatype: 'local',
				data: num, //newData是符合格式要求的重新加载的数据
			}).trigger("reloadGrid")
			sumAdd()
			searchsellSnglId()
		}
	})
	$(".refer_cancel").click(function() {
		window.location.reload();
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
//参照销售单
function searchsellSnglId() {
	refer = 1;
	$("#gbox_jqgrids").show();
	$("#gbox_jqGrids").hide();
	var grs = $("#jqGrids_list").jqGrid('getGridParam', 'selrow');
	var rowDatas = $("#jqGrids_list").jqGrid('getRowData', grs);
	$("#jqgrids").setColProp("batNum", {
		editable: true
	});
	toFormTypEncd = rowDatas.formTypEncd;
	$("#purchaseOrder #formSave input").val("");
	$('#sellOrdId').val(rowDatas.sellSnglId);
	$("input[name='sellType']").val(rowDatas.sellTypNm);
	$("#user").val(rowDatas.accNum);
	$("#userName").val(rowDatas.userName);
	$("#custId").val(rowDatas.custId);
	$("#custNm").val(rowDatas.custNm);
	$("#bizType").val(rowDatas.bizTypId);
	$("input[name='deptId']").val(rowDatas.deptId);
	$("input[name='deptName']").val(rowDatas.deptName);
	$("input[name='memo']").val(rowDatas.memo);
	$("input[name='addr']").val(rowDatas.recvrAddr)
	$("input[name='supplierNumber']").val(rowDatas.provrOrdrNum);
	$("input[name='custOrdrNum']").val(rowDatas.custOrdrNum);
	var time = BillDate();
	//入库日期
	$("input[name=rtnGoodsDt]").val(time);
	//到货日期
	$("input[name=toGdsSnglDt]").val(time);

}

function GetProInfo2(rowid, val) {
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			'projEncd': val,
			"pageNo": 1,
			"pageSize": 1
		}
	};
	var postData = JSON.stringify(data);
	$.ajax({
		url: url3 + "/mis/ec/projCls/queryList",
		type: 'post',
		data: postData,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		error: function() {
			console.log(error);
		}, //错误执行方法
		success: function(data) {
			var list = data.respBody.list;
			for(var i = 0; i < list.length; i++) {
				var projNm = list[0].projNm
				//设置页面数据展示
				$("#jqgrids").setRowData(rowid, {
					projNm: projNm,
				});
			}
		}

	})
}

function sumAdd() {
	var list = getAllData(); //此函数在有参照功能的单据中新增
	var qty = 0;
	var noTaxAmt = 0;
	var taxAmt = 0;
	var prcTaxSum = 0;
	var bxQty = 0;
	for(var i = 0; i < list.length; i++) {
		qty += parseFloat(list[i].qty);
		noTaxAmt += parseFloat(list[i].noTaxAmt);
		taxAmt += parseFloat(list[i].taxAmt);
		prcTaxSum += parseFloat(list[i].prcTaxSum);
		bxQty += parseFloat(list[i].bxQty);
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
	if(isNaN(bxQty)) {
		bxQty = 0
	}
	qty = qty.toFixed(prec)
	noTaxAmt = precision(noTaxAmt, 2)
	taxAmt = precision(taxAmt, 2)
	prcTaxSum = precision(prcTaxSum, 2)
	bxQty = precision(bxQty, 2)
	$("#jqgrids").footerData('set', {
		"qty": qty,
		"noTaxAmt": noTaxAmt,
		"taxAmt": taxAmt,
		"prcTaxSum": prcTaxSum,
		"bxQty": bxQty,
	});
}
//打开仓库/存货档案后点击确定取消
$(function() {
	$(".pro_sure").click(function() {
		//到货单
		//获得行号
		var gr = $("#insertProjCls_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDatas = $("#insertProjCls_jqgrids").jqGrid('getRowData', gr);
		var projEncd = rowDatas.projEncd
		var projNm = rowDatas.projNm
		var rowid = $("#jqgrids").jqGrid('getGridParam', 'selrow');
		$("#" + rowid + "_projEncd").val(projEncd)
		$("#jqgrids").setRowData(rowid, {
			projNm: projNm
		})
		$("#ProjClsList").hide();
		$("#ProjClsList").css("opacity", 0);
		$("#purchaseOrder").show();

	}) //	取消
	$(".pro_cancel").click(function() {
		$("#whsDocList").css("opacity", 0);
		$("#insertList").css("opacity", 0);
		$("#invtyTree").css("opacity", 0);
		$("#purchaseOrder").show();
		//到货单
		//获得行号
		var rowid = $("#jqgrids").jqGrid('getGridParam', 'selrow');

		$("#" + rowid + "_projEncd").val("");
		$("#jqgrids").setRowData(rowid, {
			projNm: ""
		});
	})
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

		//		动态设置批次可编辑
		$("#jqgrids").setColProp("batNum", {
			editable: true
		});

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
	})

	//确定
	$(".bat_true").click(function() {
		//调拨单
		//获得行号
		var rowid = $("#jqgrids").jqGrid('getGridParam', 'selrow');

		//	存货档案
		//	获得行号
		var ids = $("#batNum_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowData = $("#batNum_jqgrids").jqGrid('getRowData', ids);

		$("#" + rowid + "_batNum").val(rowData.batNum)
		var prdoc = rowData.prdcDt;
		var pr = prdoc.split(" ")
		var p;
		for(var i = 0; i < pr.length; i++) {
			p = pr[0].toString()
		}
		$("#jqgrids").setRowData(rowid, {
			prdcDt: p
		})
		$("#batNum_list").css("opacity", 0);
		$("#purchaseOrder").show();
		localStorage.removeItem("invtyEncd");
		localStorage.removeItem("whsEncd");
	})
	//	取消
	$(".bat_false").click(function() {
		$("#batNum_list").css("opacity", 0);
		$("#purchaseOrder").show();
		//调拨单
		//获得行号
		var rowid = $("#jqgrids").jqGrid('getGridParam', 'selrow');

		$("#" + rowid + "_batNum").val("")
	})
})
//初始化表格
$(function() {
	allHeight();
	$("#jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json",
		colNames: ['序号', '仓库编码', '仓库名称', '存货编码', '存货名称', '主计量单位', '规格型号', '数量',
			'含税单价', '价税合计', '批次', '生产日期', '失效日期', '保质期', '项目编码', '项目名称', '箱规', '箱数',
			'表体备注', '对应条形码', '国际批次', '无税单价', '无税金额', '税率', '税额', '主计量单位编码', '是否退货'
		], //jqGrid的列显示名字
		colModel: [{
				name: 'toOrdrNum',
				editable: true,
				align: 'center',
				sortable: false,
				hidden: true
			},
			{
				name: 'whsEncd',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'whsNm',
				editable: false,
				align: 'center',
				sortable: false,
			},
			{
				name: 'invtyEncd',
				editable: true,
				align: 'center',
				sortable: false

			},
			{
				name: 'invtyNm',
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
				name: 'spcModel',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'qty',
				editable: true,
				align: 'center',
				sortable: false,
				editrules: {
					number: true
				}
			},
			{
				name: 'cntnTaxUprc',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'prcTaxSum',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'batNum',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'prdcDt', //生产日期
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'invldtnDt', //失效日期
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'baoZhiQi',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'projEncd',
				editable: true,
				align: 'center',
				sortable: false

			},
			{
				name: 'projNm',
				editable: false,
				align: 'center',
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
				name: 'memo',
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
				name: 'intlBat', //国际批次
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'noTaxUprc',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'noTaxAmt',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'taxRate',
				editable: true,
				align: 'center',
				sortable: false
			},
			{

				name: 'taxAmt',
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
			{
				name: 'isNtRtnGoods',
				editable: true,
				align: 'center',
				hidden: true,
				sortable: false
			},
		],
		rowNum: 99999999,
		loadonce: false,
		rownumbers: true,
		autowidth: true,
		height: height,
		autoScroll: true,
		shrinkToFit: false,
		multiselect: true, //复选框 
		caption: '退货单',
		pager: "#jqgridPager",
		altclass: true,
		pgbuttons: false,
		pginput: false,
		forceFit: true,
		sortable: false,
		cellEdit: true,
		cellsubmit: "clientArray",
		footerrow: true,
		gridComplete: function() { 
			$("#jqgrids").footerData('set', { 
				"whsEncd": "本页合计",
			});
		},

		//在编辑状态时
		afterEditCell: function(rowid, cellname, val, iRow, iCol) {
			$("#findGrid").hide();
			$(".saveOrder").addClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
			if(cellname == "whsEncd") {
				$("#" + rowid + "_whsEncd").bind("dblclick", function() {
					$("#whsDocList").show();
					$("#whsDocList").css("opacity", 1);
					$("#insertList").hide();
					$("#purs_list").hide();
					$("#ProjClsList").hide();
					$("#purchaseOrder").hide();
					$(".addWhs").removeClass("gray") //确定可用
					$(".find").removeClass("gray") //查询可用
					$('button').attr('disabled', false);
					$(".gray").attr('disabled', true);
				});
			}
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
			if(cellname == "invtyEncd") {
				$("#jqgrids").setColProp("cntnTaxUprc", {
					editable: false
				});
				//获得行数据
				var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
				if(rowDatas.whsEncd == "") {
					alert("请先输入仓库档案");
					$("#" + rowid + "_invtyEncd").attr('disabled', 'disabled');
				} else {
					$("#" + rowid + "_" + cellname).bind('keyup', function() {
						findGrid(rowid, cellname, val);
					})
					$("#" + rowid + "_invtyEncd").bind("dblclick", function() {
						$("#purs_list").hide()
						$("#ProjClsList").hide();
						$(".addWhs").removeClass("gray") //确定可用
						$(".find").removeClass("gray") //查询可用
						$('button').attr('disabled', false);
						$(".gray").attr('disabled', true);
						$("#insertList").show();
						$("#insertList").css("opacity", 1);
						$("#batNum_list").css("display", "none");
						$("#whsDocList").hide();
						$("#purchaseOrder").hide();
					});
				}
			}
			if(cellname == "projEncd") {
				$("#" + rowid + "_projEncd").bind("dblclick", function() {
					$('#tree1>ul>li>div span').parent().next().css("display", "block")
					$(".saveOrder").addClass("gray") //参照
					$(".gray").attr("disabled", true)

					$(".pro_sure").removeClass("gray")
					$(".pro_cancel").removeClass("gray")
					$(".search_pro").removeClass("gray")
					$(".pro_sure").attr("disabled", false)
					$(".pro_cancel").attr("disabled", false)
					$(".search_pro").attr("disabled", false)
					$("#purs_list").hide();
					$("#batNum_list").hide();
					$("#insertList").hide();
					$("#purchaseOrder").hide();
					$("#whsDocList").hide();
					$("#ProjClsList").show();
					$("#ProjClsList").css("opacity", 1)
				});
			}

			if(cellname == "batNum") {
				//双击批次时
				$("#" + rowid + "_" + cellname).bind("dblclick", function() {
					$(".bat_true").removeClass("gray");
					$(".bat_false").removeClass("gray");
					$(".search").removeClass("gray");
					$(".batNum_search").removeClass("gray");
					$('button').attr('disabled', false);
					$(".gray").attr('disabled', true);
					$("#ProjClsList").hide();
					//获得行数据
					var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
					var invtyEncd = rowDatas.invtyEncd;
					var whsEncd = rowDatas.whsEncd;
					window.localStorage.setItem("invtyEncd", invtyEncd);
					window.localStorage.setItem("whsEncd", whsEncd);

					$("#insertList").css("display", "none");
					$("#whsDocList").css("display", "none");
					$("#purs_list").css("display", "none");

					//调拨单隐藏
					$("#purchaseOrder").hide();
					//					$("#whsDocList").hide();
					$("#batNum_list").css("opacity", 1);
					$("#batNum_list").css("display", "block");
					searcherBatNum();
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
			$("#findGrid").hide();
			if(cellname == "projEncd") {
				GetProInfo2(rowid, val);
			}
			if(cellname == "whsEncd") {
				GetWhsInfo(rowid, val);
			}
			//获取商品信息
			if(cellname == "invtyEncd") {
				sell_GetGoodsInfo(rowid, val);
			}
			//生产日期
			if(cellname == "prdcDt" || cellname == "batNum") {
				var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
				var baoZhiQi = rowDatas.baoZhiQi;
				setProductDate(rowid, val, baoZhiQi)
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
					baoZhiQi: "",
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

function getAllData() {
	//拿到grid对象
	var obj = $("#jqgrids");
	//获取grid表中所有的rowid值
	var rowIds = obj.getDataIDs();
	//初始化一个数组arrayData容器，用来存放rowData
	var arrayData = new Array();
	if(rowIds.length > 0) {
		for(var i = 0; i < rowIds.length; i++) {
			obj.getRowData(rowIds[i]).isNtRtnGoods = 0;
			if(obj.getRowData(rowIds[i]).invtyEncd == "") {
				continue;
			} else {
				//rowData=obj.getRowData(rowid);//这里rowid=rowIds[i];
				arrayData.push(obj.getRowData(rowIds[i]));
			}
		}
	}
	return arrayData;
}

//根据存货编码查询存货详细信息
function sell_GetGoodsInfo(rowid, goods) {
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
						invtyNm: "",
						spcModel: "",
						bxRule: "",
						measrCorpNm: "",
						crspdBarCd: "",
						cntnTaxUprc: '',
						taxRate: '',
						measrCorpId: "",
						baoZhiQi: "",
						prcTaxSum: "",
					});
				}
				$("#" + rowid + "_invtyEncd").val(list[0].invtyEncd);
				if(list.length == 1) {
					$("#jqgrids").setRowData(rowid, {
						invtyNm: list[0].invtyNm,
						spcModel: list[0].spcModel,
						bxRule: list[0].bxRule,
						measrCorpNm: list[0].measrCorpNm,
						crspdBarCd: list[0].crspdBarCd,
						cntnTaxUprc: list[0].refSellPrc,
						taxRate: list[0].iptaxRate,
						measrCorpId: list[0].measrCorpId,
						baoZhiQi: list[0].baoZhiQiDt,
						prcTaxSum: list[0].prcTaxSum,
					});
				} else {
					for(var i = 1; i < list.length; i++) {
						$("#jqgrids").setRowData(rowid, {
							invtyNm: list[0].invtyNm,
							spcModel: list[0].spcModel,
							bxRule: list[0].bxRule,
							measrCorpNm: list[0].measrCorpNm,
							crspdBarCd: list[0].crspdBarCd,
							cntnTaxUprc: list[0].refSellPrc,
							taxRate: list[0].iptaxRate,
							measrCorpId: list[0].measrCorpId,
							baoZhiQi: list[0].baoZhiQiDt,
							prcTaxSum: list[0].prcTaxSum,
						});
						//设置页面数据展示
						$("#jqgrids").setRowData(++rowid, {
							invtyEncd: list[i].invtyEncd,
							invtyNm: list[i].invtyNm,
							spcModel: list[i].spcModel,
							bxRule: list[i].bxRule,
							measrCorpNm: list[i].measrCorpNm,
							crspdBarCd: list[i].crspdBarCd,
							cntnTaxUprc: list[i].refSellPrc,
							taxRate: list[i].iptaxRate,
							measrCorpId: list[i].measrCorpId,
							baoZhiQi: list[i].baoZhiQiDt,
							prcTaxSum: list[i].prcTaxSum,
						});
					}
				}
			}
		})
	}
}

var rtnGoodsId;

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
		$("#jqgrids").setColProp("whsEncd", {
			editable: true
		});
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
		$("#jqgrids").setColProp("intlBat", {
			editable: true
		});
		$("#jqgrids").setColProp("prdcDt", {
			editable: true
		});
		$("#jqgrids").setColProp("batNum", {
			editable: true
		});
		$("#jqgrids").setColProp("projEncd", {
			editable: true
		});
		mType = 2;
		$("#mengban").hide();
		$('button').addClass("gray")
		$('.saveOrder').removeClass("gray");
		$('.upOrder').removeClass("gray");
		$('.editOrder').addClass("gray"); //点击修改后 修改不能用
		$('.cancel').removeClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)

		$("#gbox_jqGrids").hide();
		$("#gbox_jqgrids").show();

		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)

		var afterUrl = window.location.search.substring(1);
		var b = [];
		b = afterUrl;
		if(b == 1) {
			rtnGoodsId = localStorage.rtnGoodsId;
			getRtnGoodsId(rtnGoodsId);
		}
	});
})

function getRtnGoodsId(rtnGoodsId) {
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"rtnGoodsId": rtnGoodsId,
			"pageNo": 1,
			"pageSize": 99999999
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/purc/RtnGoods/queryRtnGoodsList',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			var list = data.respBody.list[0].rtnGoodsSub;
			for(var i = 0; i < list.length; i++) {
				$("#jqgrids").setRowData(i + 1, {
					invtyEncd: list[i].invtyEncd,
					invtyNm: list[i].invtyNm,
					spcModel: list[i].spcModel,
					measrCorpNm: list[i].measrCorpNm,
					qty: list[i].qty,
					cntnTaxUprc: list[i].cntnTaxUprc,
					prcTaxSum: list[i].prcTaxSum,
					noTaxUprc: list[i].noTaxUprc,
					noTaxAmt: list[i].noTaxAmt,
					taxRate: list[i].taxRate,
					taxAmt: list[i].taxAmt,
					bxRule: list[i].bxRule,
					bxQty: list[i].bxQty,
					crspdBarCd: list[i].crspdBarCd,
					baoZhiQi: list[i].baoZhiQi,
					isNtRtnGoods: list[i].isNtRtnGoods,
					whsNm: list[i].whsNm,
					whsEncd: list[i].whsEncd,
					batNum: list[i].batNum,
					prdcDt: list[i].prdcDt,
					invldtnDt: list[i].invldtnDt,
					intlBat: list[i].intlbat,
					memo: list[i].memo
				});
			}

		},
		error: function(error) {
			alert(error)
		}
	})
}

//拿到grid对象
function getJQAllData() {

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

function IsCheckValue(custId, accNum, bizTypId, listData) {
	var count = getInvtyData();
	if(custId == "") {
		alert("客户简称不能为空")
		return false;
	} else if(accNum == "") {
		alert("业务员不能为空")
		return false;
	} else if(bizTypId == null) {
		alert("业务类型不能为空")
		return false;
	} else if(listData.length == 0) {
		alert("仓库不能为空")
		return false;
	} else if(listData.length < count.length) {
		alert("明细中缺少仓库");
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
			} else if(listData[i].qty > 0) {
				alert("数量不能大于0")
				return false;
			} else if(listData[i].whsNm == "") {
				alert("仓库不能为空")
				return false;
			} else if(listData[i].noTaxUprc == "") {
				alert("无税单价不能为空")
				return false;
			} else if(listData[i].taxRate == "") {
				alert("税率不能为空")
				return false;
			} else if(listData[i].prcTaxSum > 0) {
				alert("价税合计不能大于0")
				return false;
			} else if(listData[i].noTaxAmt > 0) {
				alert("无税金额不能大于0")
				return false;
			} else if(listData[i].projEncd == "") {
				alert("项目编码不能为空")
				return false;
			}
		}
	}
	return true;
}

//保存修改后的数据
function SaveModifyData() {
	var listData = getJQAllData();

	//获得行号
	var gr = $("#jqGrid").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowDatas = $("#jqGrid").jqGrid('getRowData', gr);

	rtnGoodsId = $("input[name='rtnGoodsId']").val();
	var rtnGoodsDt = $("input[name='rtnGoodsDt']").val();
	var custId = $("#custId").val();
	var accNum = $("#user").val();
	var userName = $("#userName").val();
	var tel = localStorage.tel;
	var contcr = localStorage.contcr;
	var addr = localStorage.addr;
	var deptId = $("#deptId").val();
	var bizTypId = $("#bizType").val();
	var memo = $("input[name='memo']").val();
	var sellOrdId = $('#sellOrdId').val()
	var custOrdrNum = $("input[name='custOrdrNum']").val();
	var expressNum = $("input[name='expressNum']").val();
	//	var sellSnglId = $("input[name='sellSnglId']").val();
	//	var toGdsSnglId = $("input[name='toGdsSnglId']").val();	
	//	var outIntoWhsTypId = $("input[name='outIntoWhsTypId']").val();

	//判断页面是否有值为空
	if(IsCheckValue(custId, accNum, bizTypId, listData) == true) {
		var savedata = {
			"reqHead": reqHead,
			"reqBody": {
				'sellTypId': "1",
				"sellOrdrId": sellOrdId,
				'rtnGoodsId': rtnGoodsId,
				'rtnGoodsDt': rtnGoodsDt,
				'custId': custId,
				'deptId': deptId,
				'userName': userName,
				'accNum': accNum,
				'recvrTel': tel,
				'recvrAddr': addr,
				'recvr': contcr,
				'bizTypId': bizTypId,
				'custOrdrNum': custOrdrNum,
				'expressNum': expressNum,
				'memo': memo,
				'list': listData
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			url: url + '/mis/purc/RtnGoods/editRtnGoods',
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
				alert(data.respHead.message);
				if(data.respHead.isSuccess == true) {
					$(".addOrder").removeClass("gray");
					$(".refer").removeClass("gray");
					$(".editOrder").removeClass("gray");
					$(".delOrder").removeClass("gray");
					$(".toExamine").removeClass("gray");
					$(".search").removeClass("gray");
					$(".saveOrder").addClass("gray");
					$('button').attr('disabled', false);
					$(".gray").attr('disabled', true)
				}
			},
			error: function() {
				console.log(error);
			} //错误执行方法
		})

	}
}

//保存刚开始的数据
function SaveNewData() {
	//	submit()
	var listData = getJQAllData();
	var sellOrdId = $('#sellOrdId').val()
	var rtnGoodsDt = $("input[name='rtnGoodsDt']").val();
	var custId = $("#custId").val();
	var custNm = $("#custNm").val();
	var accNum = $("#user").val();
	var userName = $("#userName").val();
	var tel = localStorage.tel;
	var contcr = localStorage.contcr;
	var addr = localStorage.addr;
	var deptId = $("#deptId").val();
	var sellTypId = $("input[name='sellType']").val();
	var bizTypId = $("#bizType").val();
	var custOrdrNum = $("input[name='custOrdrNum']").val();
	var memo = $("input[name='memo']").val();
	var expressNum = $("input[name='expressNum']").val();
	//判断页面是否有值为空
	if(IsCheckValue(custId, accNum, bizTypId, listData) == true) {
		var savedata = {
			"reqHead": reqHead,
			"reqBody": {
				'sellTypId': "1",
				"sellOrdrId": sellOrdId,
				'rtnGoodsId': rtnGoodsId,
				'rtnGoodsDt': rtnGoodsDt,
				'custId': custId,
				'deptId': deptId,
				'userName': userName,
				'accNum': accNum,
				'recvrTel': tel,
				'recvrAddr': addr,
				'recvr': contcr,
				'toFormTypEncd': toFormTypEncd,
				'bizTypId': bizTypId,
				'formTypEncd': "008",
				'custOrdrNum': custOrdrNum,
				'expressNum': expressNum,
				'memo': memo,
				//				'txId':txId,
				'list': listData
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			url: url + '/mis/purc/RtnGoods/addRtnGoods',
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
				rtnGoodsId = data.respBody.rtnGoodsId;
				$("input[name='rtnGoodsId']").val(data.respBody.rtnGoodsId); //订单编码
				if(data.respHead.isSuccess == true) {
					$("#jqgrids").setColProp("whsEncd", {
						editable: false
					});
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
					$("#jqgrids").setColProp("intlBat", {
						editable: false
					});
					$("#jqgrids").setColProp("prdcDt", {
						editable: false
					});
					$("#jqgrids").setColProp("batNum", {
						editable: false
					});
					$("#jqgrids").setColProp("projEncd", {
						editable: false
					});
					$(".addOrder").removeClass("gray");
					$(".refer").removeClass("gray");
					$(".editOrder").removeClass("gray");
					$(".delOrder").removeClass("gray");
					$(".toExamine").removeClass("gray");
					$(".search").removeClass("gray");
					$(".saveOrder").addClass("gray");
					$('button').attr('disabled', false);
					$(".gray").attr('disabled', true);
				}
			},
			error: function(data) {
				console.log(data)
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
				"rtnGoodsId": rtnGoodsId,
				"isNtChk": x,
			}]
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/purc/RtnGoods/updateRtnGoodsIsNtChkList',
		type: 'post',
		data: Data,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader1").css("display", "block");
		},
		//结束加载动画
		complete: function() {
			$(".zhezhao").css("display", "none");
			$("#loader1").css("display", "none");
		},
		success: function(data) {
			alert(data.respHead.message)
			if(x == 1 && data.respHead.isSuccess == true) {
				$("button").addClass("gray");
				$(".addOrder").removeClass("gray");
				$(".noTo").removeClass("gray");
				$(".search").removeClass("gray");
				$(".refer").removeClass("gray");
				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true)
			} else if(x == 0 && data.respHead.isSuccess == true) {
				$("button").removeClass("gray");
				$(".upOrder").addClass("gray");
				$(".noTo").addClass("gray");
				$(".saveOrder").addClass("gray");
				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true)
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

$(function() {
	// 点击删除按钮，执行的操作
	$('.delOrder').click(function() {
		var deleteAjax = {
			"reqHead": reqhead,
			"reqBody": {
				"rtnGoodsId": rtnGoodsId
			}
		}
		var deleteData = JSON.stringify(deleteAjax);
		if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + '/mis/purc/RtnGoods/deleteRtnGoodsList',
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
	var b = [];
	b = afterUrl;
	var a = 1
	if(a == b) {
		chaxun()
	}
})

function chaxun() {
	$("#jqgrids").setColProp("whsEncd", {
		editable: false
	});
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
	$("#jqgrids").setColProp("intlBat", {
		editable: false
	});
	$("#jqgrids").setColProp("prdcDt", {
		editable: false
	});
	$("#jqgrids").setColProp("batNum", {
		editable: false
	});
	$("#jqgrids").setColProp("projEncd", {
		editable: false
	});
	$("#jqgrids").setColProp("intlBat", {
		editable: false
	});
	$("#addButton").hide()
	rtnGoodsId = localStorage.rtnGoodsId;
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"rtnGoodsId": rtnGoodsId,
		}
	};
	var saveData = JSON.stringify(savedata);
	var myDate = {};
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/purc/RtnGoods/queryRtnGoods',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			$(".print1").removeClass("gray")
			$(".print1").attr('disabled', false);
			var list1 = data.respBody;
			$("input[name='sellType']").val(list1.sellTypNm);
			$("#deptId").val(list1.deptId);
			$("#deptName").val(list1.deptName);
			$("input[name='rtnGoodsDt']").val(list1.rtnGoodsDt);
			$("input[name='rtnGoodsId']").val(list1.rtnGoodsId);
			$("input[name='accNum']").val(list1.accNum);
			$("#userName").val(list1.userName);
			$("input[name='custId']").val(list1.custId);
			$("input[name='custNm']").val(list1.custNm);
			$(".custOrdrNum").val(list1.custOrdrNum)
			var a = list1.bizTypId;
			$("#bizType").val(a);
			$("input[name='addr']").val(list1.recvrAddr);
			$("input[name='memo']").val(list1.memo);
			$("#sellOrdId").val(list1.sellOrdId);
			$("input[name='expressNum']").val(list1.expressNum)

			var arr = eval(data); //数组
			var list = arr.respBody.rtnGoodsSub;
			$("#jqgrids").jqGrid('clearGridData');
			$("#jqgrids").jqGrid('setGridParam', {
				datatype: 'local',
				data: list, //newData是符合格式要求的重新加载的数据
			}).trigger("reloadGrid")
			sumAdd()
		}
	})

}

$(function() {
	$('.ctrlc_v').click(function() {
		$(".ctrlc_v").addClass("gray");
		$(".ctrlYes").removeClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
		$("#jqgrids").setColProp("whsNm", {
			editable: true
		});
		$("#jqgrids").setColProp("invtyNm", {
			editable: true
		});
		$("#jqgrids").setColProp("measrCorpNm", {
			editable: true
		});
		$("#jqgrids").setColProp("cntnTaxUprc", {
			editable: true
		});
		$("#jqgrids").setColProp("prcTaxSum", {
			editable: true
		});
		$("#jqgrids").setColProp("batNum", {
			editable: true
		});
		$("#jqgrids").setColProp("invldtnDt", {
			editable: true
		});
		$("#jqgrids").setColProp("baoZhiQi", {
			editable: true
		});
		$("#jqgrids").setColProp("bxRule", {
			editable: true
		});
		$("#jqgrids").setColProp("spcModel", {
			editable: true
		});
		$("#jqgrids").setColProp("noTaxAmt", {
			editable: true
		});
		$("#jqgrids").setColProp("taxAmt", {
			editable: true
		});
		$("#jqgrids").setColProp("crspdBarCd", {
			editable: true
		});
		$("#jqgrids").setColProp("projNm", {
			editable: true
		});
		var obj = $("#jqgrids");
		//获取grid表中所有的rowid值
		var rowIds = obj.getDataIDs();
		for(var i = 0; i < rowIds.length; i++) {
			$('#jqgrids').jqGrid('editRow', rowIds[i], true);

		}
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
		var val = $("#" + rowids[i] + "_whsEncd").val()
		var projEncd = $("#" + rowids[i] + "_projEncd").val()
		jQuery("#jqgrids").saveRow(rowids[i], false, 'clientArray');
		var rowid = rowids[i]
		if(val == '') {
			continue;
		} else if(val != '') {
			GetWhsInfo(rowid, val);
		}
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
		if(projEncd == '') {
			continue;
		} else if(projEncd != '') {
			GetProInfo2(rowid, projEncd)
		}
	}
	$("#jqgrids").setColProp("whsNm", {
		editable: false
	});
	$("#jqgrids").setColProp("invtyNm", {
		editable: false
	});
	$("#jqgrids").setColProp("measrCorpNm", {
		editable: false
	});
	$("#jqgrids").setColProp("cntnTaxUprc", {
		editable: false
	});
	$("#jqgrids").setColProp("prcTaxSum", {
		editable: false
	});
	//	$("#jqgrids").setColProp("batNum",{editable:false});
	$("#jqgrids").setColProp("invldtnDt", {
		editable: false
	});
	$("#jqgrids").setColProp("baoZhiQi", {
		editable: false
	});
	$("#jqgrids").setColProp("bxRule", {
		editable: false
	});
	$("#jqgrids").setColProp("spcModel", {
		editable: false
	});
	$("#jqgrids").setColProp("noTaxAmt", {
		editable: false
	});
	$("#jqgrids").setColProp("taxAmt", {
		editable: false
	});
	$("#jqgrids").setColProp("crspdBarCd", {
		editable: false
	});
	$("#jqgrids").setColProp("projNm", {
		editable: false
	});
}
$(function() {
	$('#jqgrids').pasteFromTable();
})