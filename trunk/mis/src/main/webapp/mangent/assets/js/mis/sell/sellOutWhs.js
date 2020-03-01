var mType = 0;
//销售出库

$(function() {
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	getData();
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
//开始可以点击的按钮
$(function() {
	$('button').addClass("gray");
	$(".refer").removeClass("gray") //参照
	$('.addOrder').removeClass("gray");
	$(".cancel").removeClass("gray")
	$(".find").removeClass("gray")
	$(".print1").addClass("gray")
	$('button').attr('disabled', false);
	$(".gray").attr('disabled', true);

	var afterUrl = window.location.search.substring(1);
	var a = [];
	a = afterUrl;
	if(a == 1) {
		var isNtChk = localStorage.isNtChk;
		if(isNtChk == "是") {
			$(".noTo").removeClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
		} else if(isNtChk == "否") {
			$('.toExamine').removeClass("gray");
			$('.delOrder').removeClass("gray");
			$('.editOrder').removeClass("gray");
			$(".gdsBitAllotted").removeClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
		}
	} else if(a == 2) {
		$('button').addClass("gray")
		$(".gray").attr('disabled', true)
	}
})

// 点击增加按钮，执行的操作
$(function() {
	$('.addOrder').click(function() {
		localStorage.removeItem("invtyEncd");
		localStorage.removeItem("whsEncd");
		$("#mengban").hide();
		$(".saveOrder").removeClass("gray");
		$('.addOrder').addClass("gray");
		$(".upOrder").removeClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)

		mType = 1;

		$("#jqgrids").jqGrid('setGridParam', {}).trigger("reloadGrid")
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
		$("#jqgrids").setColProp("prdcDt", {
			editable: true
		});
		$("#jqgrids").setColProp("batNum", {
			editable: true
		});
		$("#jqgrids").setColProp("projEncd", {
			editable: true
		});
		$("#jqgrids").setColProp("intlBat", {
			editable: true
		});
		$("#gbox_jqGrids").hide();
		$("#gbox_jqgrids").show();

		$(".inputText").val("");
		$("input[name=sellType]").val("普通销售");

		var time = BillDate();
		//出库日期
		$("input[name=outWhsDt]").val(time);
	});
})

//点击参照按钮，执行的操作
$(function() {
	$(".refer").click(function() {
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
		$("#jqgrids").setColProp("prdcDt", {
			editable: true
		});
		$("#jqgrids").setColProp("batNum", {
			editable: true
		});
		$("#jqgrids").setColProp("projEncd", {
			editable: true
		});
		$("#jqgrids").setColProp("intlBat", {
			editable: true
		});

		$("#whsDocList").hide();
		$("#insertList").hide();
		$("#purchaseOrder").hide();
		$('#invtyTree').hide();
		$("#purs_list").show();
		$("#purs_list").css("opacity", 1)

		$(".sure").click(function() {
			var gr = $("#jqGrids_list").jqGrid('getGridParam', 'selrow');
			//获得行数据
			var rowDatas = $("#jqGrids_list").jqGrid('getRowData', gr);
			var sellSnglId = rowDatas.sellSnglId
			if(sellSnglId == undefined) {
				alert("请选择单据")
			} else {
				$("#purs_list").css("opacity", 0);
				$("#purchaseOrder").show();
				searchsellSnglId(sellSnglId)
			}
		})
		$(".refer_cancel").click(function() {
			window.location.reload();
		})
		//		放弃
		$(".upOrder").click(function() {
			window.location.reload();
		})
	})
})
//参照采购订单
function searchsellSnglId(sellSnglId) {
	$("#jqgrids").trigger("reloadGrid");
	$("#gbox_jqgrids").show();
	$("#gbox_jqGrids").hide()
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"sellSnglId": sellSnglId,
			"pageNo": 1,
			"pageSize": 99999999
		}
	};
	var saveData = JSON.stringify(savedata);
	var myDate = {};
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/purc/SellSngl/querySellSngl',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			$("#jqgrids").setColProp("batNum", {
				editable: true
			});
			var list = data.respBody.sellSnglSub;
			localStorage.setItem("custId", data.respBody.custId);

			$("#purchaseOrder #formSave input").val("");
			$("input[name='sellType']").val(data.respBody.sellTypNm);
			$("#user").val(data.respBody.accNum);
			$("#userName").val(data.respBody.userName);
			$("input[name='custId']").val(data.respBody.custId);
			$("input[name='custNm']").val(data.respBody.custNm);
			$("input[name='txId']").val(data.respBody.txId);
			$("input[name='deptName']").val(data.respBody.deptName);
			$("input[name='deptId']").val(data.respBody.deptId);
			$("#bizType").val(data.respBody.bizTypId);
			$("input[name='sellOrdrInd']").val(data.respBody.sellSnglId); //销售订单
			$("input[name='memo']").val(data.respBody.memo);
			var time = BillDate();
			//出库日期
			$("input[name=outWhsDt]").val(time);
			//到货日期
			$("input[name=toGdsSnglDt]").val(time);

			for(var i = 0; i < list.length; i++) {
				list[i].isNtRtnGoods = 0;
				$("#jqgrids").setRowData(i + 1, {
					whsNm: list[i].whsNm,
					whsEncd: list[i].whsEncd,
					invtyEncd: list[i].invtyEncd,
					invtyNm: list[i].invtyNm,
					spcModel: list[i].spcModel,
					measrCorpId: list[i].measrCorpId,
					measrCorpNm: list[i].measrCorpNm,
					qty: list[i].qty,
					projEncd: list[i].projEncd,
					projNm: list[i].projNm,
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
					prdcDt: list[i].prdcDt,
					invldtnDt: list[i].invldtnDt,
					batNum: list[i].batNum,
					intlBat: list[i].intlBat,
					memo: list[i].memo,
					isNtRtnGoods: list[i].isNtRtnGoods
				});
			}
			sumAdd()
		}
	})
}

function sumAdd() {
	var list = getJqData(); //此函数在有参照功能的单据中新增
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

var invtyClsEncd;
var invtyClsNm;
//条件查询供应商档案
$(document).on('click', '.click_span', function() {
	//点击改变颜色
	$(".addColor").removeClass("addColor");
	$(this).addClass("addColor");
	invtyClsEncd = $(this).children().first().text().toString();
	invtyClsNm = $(this).children().next().next().text().toString();
})
//打开仓库/存货档案后点击确定取消
$(function() {
	$(".sure2").click(function() {
		//到货单
		//获得行号
		var rowid = $("#jqgrids").jqGrid('getGridParam', 'selrow');
		$("#" + rowid + "_projEncd").val(invtyClsEncd)
		$("#jqgrids").setRowData(rowid, {
			projNm: invtyClsNm
		})
		$("#invtyTree").hide();
		$("#invtyTree").css("opacity", 0);
		$("#purchaseOrder").show();
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
		$("#invtyTree").css("opacity", 0);
		$("#purchaseOrder").show();
	})

	//	批次
	//确定
	$(".sure1").click(function() {
		//调拨单
		//获得行号
		var rowid = $("#jqgrids").jqGrid('getGridParam', 'selrow');

		//	存货档案
		//	获得行号
		var ids = $("#batNum_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowData = $("#batNum_jqgrids").jqGrid('getRowData', ids);
		var prdoc = rowData.prdcDt;
		var pr = prdoc.split(" ")
		var p;
		for(var i = 0; i < pr.length; i++) {
			p = pr[0].toString()
		}
		$("#jqgrids").setRowData(rowid, {
			prdcDt: p
		})
		$("#" + rowid + "_batNum").val(rowData.batNum)
		$("#batNum_list").css("opacity", 0);
		$("#purchaseOrder").show();
		localStorage.removeItem("invtyEncd");
		localStorage.removeItem("whsEncd");
	})
	//	取消
	$(".cancel").click(function() {
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
	$("#jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json",
		colNames: ['序号', '仓库名称', '仓库编码', '存货编码', '存货名称', '规格型号', '主计量单位', '主计量单位编码', '数量', '含税单价', '价税合计',
			'无税单价', '无税金额', '税率', '税额', '箱规', '箱数', '对应条形码', '批次', '生产日期', '失效日期', 
			'保质期', '国际批次', '表体备注', '是否退货', '项目编码', '项目名称'], //jqGrid的列显示名字
		colModel: [{
				name: 'ordrNum',
				editable: false,
				align: 'center',
				sortable: false,
				hidden: true
			},
			{
				name: 'whsNm',
				editable: false,
				align: 'center',
				sortable: false,
			},
			{
				name: 'whsEncd',
				editable: true,
				align: 'center',
				sortable: false,
				//				hidden: true
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
				name: 'measrCorpId',
				editable: false,
				align: 'center',
				hidden: true,
				sortable: false
			},
			{
				name: 'qty',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'cntnTaxUprc',
				editable: true,
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
				name: 'batNum',
				editable: false,
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
				name: 'intlBat', //国际批次
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
				name: 'isNtRtnGoods',
				editable: true,
				align: 'center',
				hidden: true,
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
			}
		],
		rowNum: 99999999,
		loadonce: false,
		rownumbers: true,
		autowidth: true,
		height: 400,
		autoScroll: true,
		shrinkToFit: false,
		multiselect: true, //复选框 
		caption: '销售出库单',
		pager: "#jqgridPager",
		altclass: true,
		pgbuttons: false,
		pginput: false,
		//		viewrecords: true,
		forceFit: true,
		sortable: false,
		cellEdit: true,
		cellsubmit: "clientArray",
		footerrow: true,
		gridComplete: function() { 
			$("#jqgrids").footerData('set', { 
				"whsNm": "本页合计",
			});
		},

		//在编辑状态时
		afterEditCell: function(rowid, cellname, val, iRow, iCol) {
			$("#findGrid").hide();
			$(".saveOrder").addClass("gray");
			$(".sure2").removeClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
			$("#" + rowid + "_batNum").attr("readonly", "readonly");
			if(cellname == "whsEncd") {
				$("#" + rowid + "_whsEncd").bind("dblclick", function() {
					$("#whsDocList").show();
					$("#whsDocList").css("opacity", 1);
					$("#insertList").hide();
					$("#purchaseOrder").hide();
					$(".addWhs").removeClass("gray"); //确定可用
					$(".find").removeClass("gray"); //查询可用
					$(".cancel").removeClass("gray");
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
				$("#" + rowid + "_" + cellname).bind('keyup', function() {
					findGrid(rowid, cellname, val);
				})
				//获得行数据
				var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
				if(rowDatas.whsNm == "") {
					alert("请先输入仓库档案");
					$("#" + rowid + "_invtyEncd").attr('disabled', 'disabled');
				} else {
					$("#" + rowid + "_invtyEncd").bind("dblclick", function() {
						$("#insertList").show();
						$("#insertList").css("opacity", 1);

						$("#batNum_list").css("display", "none");
						$("#whsDocList").hide();
						$("#invtyTree").hide();
						$("#purchaseOrder").hide();
						$("#purs_list").hide();
						$(".addWhs").removeClass("gray") //确定可用
						$(".find").removeClass("gray") //查询可用
						$(".cancel").removeClass("gray")
						$('button').attr('disabled', false);
						$(".gray").attr('disabled', true)
					});
				}
			}
			if(cellname == "projEncd") {
				$("#" + rowid + "_projEncd").bind("dblclick", function() {
					$('#tree1>ul>li>div span').parent().next().css("display", "block")
					$("#batNum_list").hide();
					$("#insertList").hide();
					$("#purs_list").hide();
					$("#purchaseOrder").hide();
					$("#whsDocList").hide();
					$("#invtyTree").show();
					$("#invtyTree").css("opacity", 1)
				});
			}
			if(cellname == "batNum") {

				//获得行数据
				var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
				var invtyEncd = rowDatas.invtyEncd;
				var whsEncd = rowDatas.whsEncd;
				window.localStorage.setItem("invtyEncd", invtyEncd);
				window.localStorage.setItem("whsEncd", whsEncd);
				//双击批次时
				$("#" + rowid + "_" + cellname).bind("dblclick", function() {
					$(".sure1").removeClass("gray");
					$('.sure1').attr('disabled', false);
					$(".batNum_search").removeClass("gray");
					$(".true").removeClass("gray");
					$(".false").removeClass("gray");

					$(".search").removeClass("gray");
					$('button').attr('disabled', false);
					$(".gray").attr('disabled', true)
					$("#insertList").css("display", "none");
					$("#whsDocList").css("display", "none");

					$("#purs_list").css("display", "none");

					$("#purchaseOrder").hide();
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
				GetProInfo(rowid, val);
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
				setProductDate(rowid, val,baoZhiQi)
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
				sumAdd();
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
			//删除一行操作
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

function getJqData() {
	//拿到grid对象
	var obj = $("#jqgrids");
	//获取grid表中所有的rowid值
	var rowIds = obj.getDataIDs();
	//初始化一个数组arrayData容器，用来存放rowData
	var arrayData = new Array();
	if(rowIds.length > 0) {
		for(var i = 0; i < rowIds.length; i++) {
			obj.getRowData(rowIds[i]).isNtRtnGoods = 0;
			if(obj.getRowData(rowIds[i]).whsEncd == "") {
				continue;
			} else {
				arrayData.push(obj.getRowData(rowIds[i]));
			}
		}
	}
	return arrayData;
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

$(function() {
	$('.print1').click(function() {
		var outWhsId = $("input[name='formNum']").val()
		if(outWhsId == '') {
			alert("不存在的单据")
		} else {
			localStorage.setItem("outWhsId", outWhsId)
			window.open("../../Components/sell/print_sellOut.html?1");
		}
	})
})
// 点击修改按钮，执行的操作
$(function() {
	$('.editOrder').click(function() {
		$("#mengban").hide();
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
		$("#jqgrids").setColProp("prdcDt", {
			editable: true
		});
		$("#jqgrids").setColProp("batNum", {
			editable: true
		});
		$("#jqgrids").setColProp("projEncd", {
			editable: true
		});
		$("#jqgrids").setColProp("intlBat", {
			editable: true
		});
		mType = 2;
		$('button').addClass("gray")
		$('.saveOrder').removeClass("gray");
		$('.upOrder').removeClass("gray");
		$('.editOrder').addClass("gray"); //点击修改后 修改不能用
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true);
	});
})

function IsCheckValue(custId, bizTypId, accNum, listData) {
	var count = $("#jqgrids").getGridParam("reccount");
	if(custId == "") {
		alert("客户不能为空")
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
	} else if(listData.length<count){
		alert("明细中缺少仓库");
		return false;
	}  else if(listData.length != 0) {
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
			} else if(listData[i].qty < 0) {
				alert("数量不能小于0")
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
var outWhsId;
//保存刚开始的数据
function SaveNewData() {
	var listData = getJqData();
	outWhsId = $("input[name='formNum']").val(); //销售id
	var outWhsDt = $("input[name='outWhsDt']").val(); //销售日期
	var custId = $("#custId").val();
	var userName = $("#userName").val();
	var sellTypId = $("input[name='sellType']").val();
	var bizTypId = $("select[name='bizType']").val();
	var sellOrdrInd = $("input[name='sellOrdrInd']").val();
	var accNum = $("#user").val();
	var deptId = $("#deptId").val();
	var deptNm = $("#deptNm").val();
	var txId = $("select[name='txId']").val();
	var invtyCls = $("select[name='invtyCls']").val();
	var custOrdrNum = $("input[name='custOrdrNum']").val();
	var memo = $("input[name='memo']").val();
	//判断页面是否有值为空
	if(IsCheckValue(custId, bizTypId, accNum, listData) == true) {
		var savedata = {
			"reqHead": reqHead,
			"reqBody": {
				'sellTypId': "1",
				'outWhsId': "",
				'outWhsDt': outWhsDt,
				'custId': custId,
				'deptId': deptId,
				'userName': userName,
				"bizTypId": bizTypId,
				'sellOrdrInd': sellOrdrInd,
				'accNum': accNum,
				'txId': txId,
				"invtyCls": invtyCls,
				'memo': memo,
				'formTypEncd': "009",
				'custOrdrNum': custOrdrNum,
				'isNtRtnGood': 0,
				'list': listData
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			url: url + '/mis/purc/SellOutWhs/addSellOutWhs',
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
				outWhsId = data.respBody.outWhsId;
				localStorage.setItem("outWhsId",outWhsId)
				$("input[name='formNum']").val(data.respBody.outWhsId); //订单编码
				if(data.respHead.isSuccess == true) {
					$('.gds_sure').removeClass("gray");
					$('.gds_cancel').removeClass("gray")
					$(".gds_delete").removeClass("gray");

					$(".addOrder").removeClass("gray");
					$(".editOrder").removeClass("gray");
					$(".delOrder").removeClass("gray");
					$(".toExamine").removeClass("gray");
					$(".search").removeClass("gray");
					$(".saveOrder").addClass("gray");
					$(".upOrder").removeClass("gray");

					$('button').attr('disabled', false);
					$(".gray").attr('disabled', true);
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
					chaxun()
				}
			},
			error: function() {
				console.log(error);
			} //错误执行方法
		})
	}
}

//保存修改后的数据
function SaveModifyData() {
	var listData = getJqData();

	//获得行号
	var gr = $("#jqGrid").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowDatas = $("#jqGrid").jqGrid('getRowData', gr);

	var bizTypId = $("select[name='bizType']").val();

	outWhsId = $("input[name='formNum']").val();
	var outWhsDt = $("input[name='outWhsDt']").val();
	var toGdsSnglId = $("input[name='toGdsSnglId']").val();
	var custId = $("#custId").val();
	var userName = $("#userName").val();
	var deptId = $("#deptId").val();
	var deptNm = $("#deptNm").val();
	var custPurcId = $("input[name='custPurcId']").val();
	var sellOrdrInd = $("input[name='sellOrdrInd']").val();
	var outIntoWhsTypId = $("input[name='outIntoWhsTypId']").val();
	var accNum = $("#user").val();
	var invtyCls = $("select[name='invtyCls']").val();
	var custOrdrNum = $("input[name='custOrdrNum']").val();
	var memo = $("input[name='memo']").val();

	//判断页面是否有值为空
	if(IsCheckValue(custId, bizTypId, accNum, listData) == true) {
		var savedata = {
			"reqHead": reqHead,
			"reqBody": {
				'sellTypId': "1",
				'outWhsId': outWhsId,
				'bizTypId': bizTypId,
				'outWhsDt': outWhsDt,
				'toGdsSnglId': toGdsSnglId,
				'outIntoWhsTypId': outIntoWhsTypId,
				'custId': custId,
				'deptId': deptId,
				'userName': userName,
				'custPurcId': custPurcId,
				'sellOrdrInd': sellOrdrInd,
				'accNum': accNum,
				'invtyCls': invtyCls,
				'custOrdrNum': custOrdrNum,
				'memo': memo,
				'list': listData
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			url: url + '/mis/purc/SellOutWhs/editSellOutWhs',
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
					$(".addOrder").removeClass("gray");
					$(".refer").removeClass("gray");
					$(".editOrder").removeClass("gray");
					$(".delOrder").removeClass("gray");
					$(".toExamine").removeClass("gray");
					$(".search").removeClass("gray");
					$(".saveOrder").addClass("gray");
					$(".upOrder").removeClass("gray");
					$('button').attr('disabled', false);
					$(".gray").attr('disabled', true)
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
				}
			},
			error: function() {
				console.log(error);
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
				"isNtRtnGood": 0,
				"outWhsId": outWhsId,
				"isNtChk": x,
			}]
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/purc/SellOutWhs/updateSellOutWhsIsNtChk',
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
				"outWhsId": outWhsId
			}
		}
		var deleteData = JSON.stringify(deleteAjax);
		if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + '/mis/purc/SellOutWhs/deleteSellOutWhsList',
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
	if(a == 1 || a == 2) {
		chaxun()
	}
})

function chaxun() {
	$("#addButton").hide()
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
	if(localStorage.outWhsId) {
		outWhsId = localStorage.outWhsId;
	} else if(localStorage.orderId) {
		outWhsId = localStorage.orderId;
	}

	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"outWhsId": outWhsId,
		}
	};
	var saveData = JSON.stringify(savedata);
	console.log(saveData)
	var myDate = {};
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/purc/SellOutWhs/querySellOutWhs',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			console.log(data)
			$(".print1").removeClass("gray")
			//			$(".print1").addClass("gray")
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
			var list1 = data.respBody;
			$("input[name='sellType']").val(list1.sellTypNm);
			$("input[name='deptName']").val(list1.deptName);
			$("input[name='outWhsDt']").val(list1.outWhsDt);
			$("input[name='formNum']").val(list1.outWhsId);
			$("input[name='accNum']").val(list1.accNum);
			$("#userName").val(list1.userName);
			$("input[name='provr']").val(list1.provrNm);
			$("input[name='custId']").val(list1.custId);
			$("input[name='custNm']").val(list1.custNm);
			$("input[name='custOrdrNum']").val(list1.custOrdrNum);
			$("input[name='sellOrdrInd']").val(list1.sellOrdrInd);
			$("#deptId").val(list1.deptId)
			$("#bizType").val(list1.bizTypId);

			$("input[name='addr']").val(list1.recvrAddr);
			$("input[name='txId']").val(list1.txId);
			$("input[name='memo']").val(list1.memo);
			var list = data.respBody.sellOutWhsSub;
			$("#jqgrids").jqGrid('clearGridData');
			$("#jqgrids").jqGrid('setGridParam', {
				datatype: 'local',
				data: list, //newData是符合格式要求的重新加载的数据
			}).trigger("reloadGrid")
			sumAdd()
			makeCode()
		}
	})
}