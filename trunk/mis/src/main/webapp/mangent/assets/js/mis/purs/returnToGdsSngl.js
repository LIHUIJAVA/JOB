//到货拒收单
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
/*刚开始可点*/
$(function() {
	$('button').addClass("gray");
	$('.addOrder').removeClass("gray")
	$(".refer").removeClass("gray") //参照
	$('button').attr('disabled', false);
	$(".gray").attr('disabled', true);
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
			if(obj.getRowData(rowIds[i]).whsEncd == "") {
				arrayData.push(obj.getRowData(rowIds[i]));
			}
		}
	}
	return arrayData;
}

var souceId = "";
//点击参照按钮，执行的操作
$(function() {
	$(".refer").click(function() {
		toGds = 1;
		$("#jqgrids").trigger("reloadGrid");
		mType = 1;
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
		$("#jqgrids").setColProp("batNum", {
			editable: true
		});
		$("#jqgrids").setColProp("intlBat", {
			editable: true
		});
		$("#jqgrids").setColProp("prdcDt", {
			editable: true
		});
		$('button').addClass("gray");
		$('.addOrder').removeClass("gray") //增加
		$(".sure").removeClass("gray") //确定
		$(".cancel").removeClass("gray") //取消
		$(".searcher_return").removeClass("gray") //查询

		$(".saveOrder").removeClass("gray");
		$(".upOrder").removeClass("gray");
		$('.refer').removeClass("gray")
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)

		$("#whsDocList").hide();
		$("#insertList").hide();
		$("#purchaseOrder").hide();
		$("#purs_list").show();
		$("#purs_list").css("opacity", 1)
	})
	$(".sure").click(function() {
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
				delete encd[i].toOrdrNum;
				var keyMap = {
					returnQty: 'qty',
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
			searchtoGdsSnglId()
		}
	})
	$(".refer_cancel").click(function() {
		window.location.reload();
	})
})

var toFormTypEncd;
//参照到货单
function searchtoGdsSnglId() {
	$("#gbox_jqgrids").show();
	$("#gbox_jqGrids").hide();
	var grs = $("#jqGrids_list").jqGrid('getGridParam', 'selrow');
	var rowDatas = $("#jqGrids_list").jqGrid('getRowData', grs);

	var ids = $("#jqGrids_list").jqGrid('getGridParam', 'selarrrow');
	var toGdsSngl = [];
	for(i = 0; i < ids.length; i++) {
		var rowDatas = $("#jqGrids_list").jqGrid('getRowData', ids[i]); //获取行数据
		toGdsSngl[i] = rowDatas.toGdsSnglId;
	}
	var toGdsSnglId = toGdsSngl.join(',');

	$("#deteil_list").trigger("reloadGrid");
	$("#purchaseOrder #formSave input").val("");
	$("input[name='purType']").val(rowDatas.pursTypNm);
	$("input[name='chkTm']").val(rowDatas.chkTm);
	$("input[name='memo']").val(rowDatas.memo);
	$("#provrNm").val(rowDatas.provrNm);
	$("#provrId").val(rowDatas.provrId);
	toFormTypEncd = rowDatas.formTypEncd;
	$("input[name='toGdsSnglId1']").val(toGdsSnglId);
	$("input[name='deptName']").val(rowDatas.deptName);
	$("#deptId").val(rowDatas.deptId);
	$("input[name='provrOrdrNum']").val(rowDatas.provrOrdrNum);
	$("#user").val(rowDatas.accNum);
	$("#userName").val(rowDatas.userName);
	$(".memo").val(rowDatas.memo);
	souceId = toGdsSnglId;
	var time = BillDate();
	//到货日期
	$("input[name=toGdsSnglDt]").val(time);
}

function sumAdd() {
	var list = getAllData();
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

// 点击增加按钮，执行的操作
$(function() {
	$('.addOrder').click(function() {

		$(".saveOrder").removeClass("gray");
		$(".upOrder").removeClass("gray");
		$('.addOrder').addClass("gray");
		$(".refer").removeClass("gray") //参照
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)

		mType = 1;

		$("#jqgrids").jqGrid('setGridParam', {}).trigger("reloadGrid")
		$("#gbox_jqGrids").hide();
		$("#gbox_jqgrids").show();

		$(".inputText").val("");
		$("input[name=purType]").val("普通采购");
		var time = BillDate();
		//到货日期
		$("input[name=toGdsSnglDt]").val(time);
	});
})

//打开仓库/存货档案后点击确定取消
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
	})

	//	批次
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
	allHeight()
	$("#jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json",
		colNames: ['序号', '仓库编码*', '仓库名称', '存货编码*', '存货名称', '规格型号', '主计量单位', '编码', '数量*', '含税单价', '价税合计',
			'无税单价', '无税金额', '税率', '税额', '箱规', '箱数', '对应条形码', '批次*', '生产日期*', '失效日期', '保质期', '国际批次', '表体备注', '是否拒收'
		], //jqGrid的列显示名字
		colModel: [{
				name: 'toOrdrNum',
				editable: false,
				align: 'center',
				sortable: false,
				hidden: true
			},
			{
				name: 'whsEncd',
				editable: false,
				align: 'center',
				sortable: false,
			},
			{
				name: 'whsNm',
				editable: false,
				align: 'center',
				sortable: false
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
		],
		rowNum: 99999999,
		loadonce: false,
		rownumbers: true,
		autowidth: true,
		height: height,
		autoScroll: true,
		shrinkToFit: false,
		multiselect: true, //复选框 
		caption: '到货拒收单',
		altclass: true,
		pager: "#jqgridPager",
		pgbuttons: false,
		pginput: false,
		//		viewrecords: true,
		forceFit: true,
		sortable: false,
		cellEdit: true,
		footerrow: true,
		cellsubmit: "clientArray",
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
					$("#purchaseOrder").hide();
					$(".addWhs ").removeClass('gray');
					$(".find").removeClass('gray');
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
				if(rowDatas.whsNm == "") {
					alert("请先输入仓库档案");
					$("#" + rowid + "_invtyEncd").attr('disabled', 'disabled');
				} else {
					$("#" + rowid + "_" + cellname).bind('keyup', function() {
						findGrid(rowid, cellname, val);
					})
					$("#" + rowid + "_invtyEncd").bind("dblclick", function() {
						$("#insertList").show();
						$("#insertList").css("opacity", 1);
						$('#purs_list').hide();
						$("#whsDocList").hide();
						$("#purchaseOrder").hide();
						$(".cancel").removeClass("gray")
						$(".addWhs").removeClass("gray") //确定可用
						$(".find").removeClass("gray") //查询可用
						$('button').attr('disabled', false);
						$(".gray").attr('disabled', true);
						$("#batNum_list").css("display", "none");
					});
				}
			}

			if(cellname == "batNum") {
				//双击批次时
				$("#" + rowid + "_" + cellname).bind("dblclick", function() {
					//获得行数据
					var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
					var whsEncd = rowDatas.whsEncd
					var invtyEncd = rowDatas.invtyEncd
					window.localStorage.setItem("whsEncd", whsEncd);
					window.localStorage.setItem("invtyEncd", invtyEncd);

					$(".bat_true").removeClass("gray");
					$(".bat_false").removeClass("gray");
					$(".batNum_search").removeClass("gray");
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
			$(".saveOrder").removeClass("gray")
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
			$("#findGrid").hide();
			if(cellname == "whsEncd") {
				GetWhsInfo(rowid, val);
			}
			//获取商品信息
			if(cellname == "invtyEncd") {
				GetGoodsInfo(rowid, val);
			}
			if(cellname == "prdcDt" || cellname == "batNum") {
				var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
				var baoZhiQi = rowDatas.baoZhiQi;
				setProductDate(rowid, val,baoZhiQi)
			}
			if((cellname == "bxQty") ||
				(cellname == "qty") ||
				(cellname == "cntnTaxUprc") ||
				(cellname == "prcTaxSum") ||
				(cellname == "noTaxUprc") ||
				(cellname == "noTaxAmt") ||
				(cellname == "taxRate")) {
				//设置变量
				purs_SetNums(rowid, cellname);
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
			//删除一行操作
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
		$("#jqgrids").setColProp("batNum", {
			editable: true
		});
		$("#jqgrids").setColProp("intlBat", {
			editable: true
		});
		$("#jqgrids").setColProp("prdcDt", {
			editable: true
		});
		mType = 2;
		$('button').addClass('gray')
		$(".saveOrder").removeClass("gray");
		$('.upOrder').removeClass("gray");
		$('.editOrder').addClass("gray"); //点击修改后 修改不能用
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
	});
})

function getAllData() {
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
var toGdsSnglDt;
//保存修改后的数据
function SaveModifyData() {
	var listData = getJQAllData();

	var toGdsSnglDt = $("input[name='toGdsSnglDt']").val();
	var provrId = $("#provrId").val();
	var userName = $("#userName").val();
	var deptId = $("#deptId").val();
	var provrOrdrNum = $("input[name='provrOrdrNum']").val();
	toGdsSnglId = $("input[name='toGdsSnglId']").val();
	var accNum = $("#user").val();
	var pursOrdrId = souceId;
	var memo = $("input[name='remarks']").val();

	//判断页面是否有值为空
	if(IsCheckValue(provrId, accNum, listData) == true) {

		var savedata = {
			"reqHead": reqHead,
			"reqBody": {
				'toGdsSnglId': toGdsSnglId,
				'toGdsSnglDt': toGdsSnglDt,
				'pursTypId': "1",
				'provrId': provrId,
				'userName': userName,
				'deptId': deptId,
				'provrOrdrNum': provrOrdrNum,
				'accNum': accNum,
				'memo': memo,
				"pursOrdrId": pursOrdrId,
				"isNtRtnGood": 1,
				'list': listData
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			url: url + '/mis/purc/ToGdsSngl/editToGdsSngl',
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
					$(".addOrder").addClass("gray");
					$(".refer").addClass("gray");
					$(".editOrder").removeClass("gray");
					$(".delOrder").removeClass("gray");
					$(".toExamine").removeClass("gray");
					$(".search").removeClass("gray");
					$(".saveOrder").addClass("gray");
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
					$("#jqgrids").setColProp("batNum", {
						editable: false
					});
					$("#jqgrids").setColProp("intlBat", {
						editable: false
					});
					$("#jqgrids").setColProp("prdcDt", {
						editable: false
					});
				}
			},
			error: function() {
				alert("获取数据错误");
			} //错误执行方法
		})

	}
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

function IsCheckValue(provrId, accNum, listData) {
	var count = $("#jqgrids").getGridParam("reccount");
	var provrId1 = provrId.split(",");
	if(provrId == "" || provrId == undefined) {
		alert("供应商不能为空")
		return false;
	} else if(provrId1.length > 1) {
		alert("供应商不能选多个")
		return false;
	} else if(accNum == "") {
		alert("业务员不能为空")
		return false;
	} else if(listData.length == 0) {
		alert("仓库不能为空")
		return false;
	} else if(listData.length < count) {
		alert("明细中缺少仓库");
		return false;
	} else if(listData.length != 0) {
		for(var i = 0; i < listData.length; i++) {
			if(listData[i].invtyEncd == "") {
				alert("存货不能为空")
				return false;
			} else if(listData[i].qty == "") {
				alert("数量不能为空")
				return false;
			} else if(listData[i].qty > 0) {
				alert("数量不能大于0")
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
//第一次保存
function SaveNewData() {
	var listData = getJQAllData();

	toGdsSnglDt = $("input[name='toGdsSnglDt']").val();
	var provrId = $("#provrId").val();
	var userName = $("#userName").val();
	var deptId = $("#deptId").val();
	var provrOrdrNum = $("input[name='provrOrdrNum']").val();
	var accNum = $("#user").val();
	var memo = $("input[name='remarks']").val();
	var pursOrdrId = souceId;

	//判断页面是否有值为空
	if(IsCheckValue(provrId, accNum, listData) == true) {

		var savedata = {
			"reqHead": reqHead,
			"reqBody": {
				'toGdsSnglId': "",
				'toGdsSnglDt': toGdsSnglDt,
				'pursTypId': "1",
				'provrId': provrId,
				'userName': userName,
				'deptId': deptId,
				"toFormTypEncd": toFormTypEncd,
				'provrOrdrNum': provrOrdrNum,
				'accNum': accNum,
				'formTypEncd': "003",
				"memo": memo,
				"pursOrdrId": pursOrdrId,
				"isNtRtnGood": 1,
				'list': listData
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			url: url + '/mis/purc/ToGdsSngl/addToGdsSngl',
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
				toGdsSnglId = data.respBody.toGdsSnglId;
				$("input[name='toGdsSnglId']").val(data.respBody.toGdsSnglId); //订单编码
				if(data.respHead.isSuccess == true) {
					$(".addOrder").removeClass("gray");
					$(".refer").removeClass("gray") //参照
					$(".editOrder").removeClass("gray");
					$(".delOrder").removeClass("gray");
					$(".toExamine").removeClass("gray");
					$(".search").removeClass("gray");
					$(".saveOrder").addClass("gray");
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
					$("#jqgrids").setColProp("batNum", {
						editable: false
					});
					$("#jqgrids").setColProp("intlBat", {
						editable: false
					});
					$("#jqgrids").setColProp("prdcDt", {
						editable: false
					});
				}
			},
			error: function() {
				alert(error);
			} //错误执行方法
		})
	}
}

//删除
$(function() {
	// 点击删除按钮，执行的操作
	$('.delOrder').click(function() {
		mType = 0;
		var deleteAjax = {
			"reqHead": reqhead,
			"reqBody": {
				"toGdsSnglId": toGdsSnglId
			}
		}
		var deleteData = JSON.stringify(deleteAjax);
		if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + '/mis/purc/ToGdsSngl/deleteToGdsSnglList',
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
				"toGdsSnglId": toGdsSnglId,
				"isNtChk": x,
				'isNtRtnGood': 1
			}]
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/purc/ToGdsSngl/updateToGdsSnglIsNtChk',
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
				$("#jqgrids").setColProp("batNum", {
					editable: false
				});
				$("#jqgrids").setColProp("intlBat", {
					editable: false
				});
				$("#jqgrids").setColProp("prdcDt", {
					editable: false
				});
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
	$("#jqgrids").setColProp("batNum", {
		editable: false
	});
	$("#jqgrids").setColProp("intlBat", {
		editable: false
	});
	$("#jqgrids").setColProp("prdcDt", {
		editable: false
	});
	toGdsSnglId = localStorage.toGdsSnglId;
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"toGdsSnglId": toGdsSnglId,
		}
	};
	var saveData = JSON.stringify(savedata);
	var myDate = {};
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/purc/ToGdsSngl/queryToGdsSngl',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			var list1 = data.respBody;
			$("input[name='purType']").val(list1.pursTypNm);
			$("input[name='toGdsSnglDt']").val(list1.toGdsSnglDt);
			$("input[name='toGdsSnglId']").val(list1.toGdsSnglId);
			$("input[name='toGdsSnglId1']").val(list1.pursOrdrId);
			$("#user").val(list1.accNum);
			$("#userName").val(list1.userName);
			$("input[name='provrNm']").val(list1.provrNm);
			$("input[name='deptName']").val(list1.deptName);
			$("input[name='remarks']").val(list1.memo);
			$("input[name='provrOrdrNum']").val(list1.provrOrdrNum);
			$("#deptId").val(list1.deptId);
			$("#provrId").val(list1.provrId);
			var list = data.respBody.toGdsSnglSub;
			$("#jqgrids").jqGrid('clearGridData');
			$("#jqgrids").jqGrid('setGridParam', {
				datatype: 'local',
				data: list, //newData是符合格式要求的重新加载的数据
			}).trigger("reloadGrid")
			sumAdd()
		}
	})
}