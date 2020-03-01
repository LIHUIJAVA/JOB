//委托代销发货单
var mType = 0;
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
	$('button').addClass("gray");
	$('.addOrder').removeClass("gray")
	$(".addWhs").removeClass("gray")
	$(".cancel").removeClass("gray")
	$(".find").removeClass("gray")
	$(".pro_sure").removeClass("gray")
	$(".pro_cancel").removeClass("gray")
	$(".search_pro").removeClass("gray")
	$(".sure1").removeClass("gray")
	$('button').attr('disabled', false);
	$(".gray").attr('disabled', true);

	var afterUrl = window.location.search.substring(1);
	var b = [];
	b = afterUrl;
	var isNtChk = localStorage.isNtChk;
	if(b == 1) {
		if(isNtChk == "是") {
			$(".noTo").removeClass("gray")
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
		} else if(isNtChk == "否") {
			$('.toExamine').removeClass("gray");
			$('.delOrder').removeClass("gray");
			$('.editOrder').removeClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
		}
	}
})

// 点击增加按钮，执行的操作
$(function() {
	$('.addOrder').click(function() {
		$(".delOrder").addClass("gray");
		$(".editOrder").addClass("gray");
		$(".toExamine").addClass("gray");
		$(".noTo").addClass("gray");
		$(".saveOrder").removeClass("gray");
		$(".upOrder").removeClass("gray");
		$('.addOrder').addClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)

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

		$("#purchaseOrder #formSave input").val("");
		$("#purchaseOrder #formSave select").val("");
		$("input[name=sellType]").val("委托代销");
		$("#jqgrids").jqGrid('clearGridData');
		$("#jqgrids").jqGrid('setGridParam', {
			url: '../../assets/js/json/order.json',
			datatype: "json",
		}).trigger("reloadGrid")
		var time = BillDate();
		//入库日期
		$("input[name=delvSnglDt]").val(time);
	});
})

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
	$(".sure1").click(function() {
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

		$("#whsDocList").css("opacity", 0);
		$("#insertList").css("opacity", 0);
		$("#purchaseOrder").show();
	})
	//	取消
	$(".cancel").click(function() {
		$("#whsDocList").css("opacity", 0);
		$("#insertList").css("opacity", 0);
		$("#purchaseOrder").show();
	})
	//确定
	$(".true1").click(function() {
		//调拨单
		//获得行号
		var rowid = $("#jqgrids").jqGrid('getGridParam', 'selrow');

		//	存货档案
		//	获得行号
		var ids = $("#batNum_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowData = $("#batNum_jqgrids").jqGrid('getRowData', ids);
		var prdoc = rowData.prdcDt;
		var invldtnDt = rowData.invldtnDt;
		var inv = invldtnDt.split(" ")
		var pr = prdoc.split(" ")
		var p,invl;
		for(var i = 0; i < pr.length; i++) {
			p = pr[0].toString()
		}
		for(var i = 0; i < inv.length; i++) {
			invl = inv[0].toString()
		}
		$("#jqgrids").setRowData(rowid, {
			prdcDt: p,
			invldtnDt:invl
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
	allHeight()
	$("#jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json",
		colNames: ['仓库编码', '仓库名称', '存货编码', '存货名称', '规格型号', '主计量单位', '主计量单位编码', '数量', '含税单价', '价税合计',
			'无税单价', '无税金额', '税率', '税额', '箱规', '箱数', '对应条形码', '批次', '生产日期', '失效日期', '保质期',
			'项目编码', '项目名称', '国际批次', '表体备注', '是否退货','是否保质期管理'
		], //jqGrid的列显示名字
		colModel: [{
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
				name: 'isQuaGuaPer',
				editable: false,
				align: 'center',
				sortable: false,
				hidden:true
			}
		],
		loadonce: false,
		rownumbers: true,
		autowidth: true,
		height: height,
		autoScroll: true,
		shrinkToFit: false,
		multiselect: true, //复选框 
		caption: '委托代销发货单',
		altclass: true,
		pgbuttons: false,
		pginput: false,
		pager: "#jqgridPager",
		//		viewrecords: true,
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
			$("#" + rowid + "_batNum").attr("readonly", "readonly");
			if(cellname == "whsEncd") {
				$("#" + rowid + "_whsEncd").bind("dblclick", function() {
					$("#whsDocList").show();
					$("#whsDocList").css("opacity", 1);
					$("#insertList").hide();
					$("#purchaseOrder").hide();
					$('#batNum_list').hide()
					$("#ProjClsList").hide();
				});
			}
			if(cellname == "prdcDt") {
				$("input[name='prdcDt']").attr("calendar","YYYY-MM-DD")
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
				$("#" + rowid + "_invtyEncd").bind("dblclick", function() {
					$(".find").removeClass("gray");
					$(".addWhs").removeClass("gray");
					$(".cancel").removeClass("gray");
					$('button').attr('disabled', false);
					$(".gray").attr('disabled', true)
					$("#insertList").show();
					$("#insertList").css("opacity", 1);
					$("#whsDocList").hide();
					$("#purchaseOrder").hide();
					$('#batNum_list').hide()
					$("#ProjClsList").hide();
				});
			}
			if(cellname == "projEncd") {
				$("#" + rowid + "_projEncd").bind("dblclick", function() {
					$('#tree1>ul>li>div span').parent().next().css("display", "block")
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
					$(".true1").removeClass("gray");
					$(".cancel").removeClass("gray");
					$(".search").removeClass("gray");
					$(".batNum_search").removeClass("gray");
					$('button').attr('disabled', false);
					$(".gray").attr('disabled', true)
					$("#ProjClsList").hide();
					//获得行数据
					var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
					var invtyEncd = rowDatas.invtyEncd;
					var whsEncd = rowDatas.whsEncd;
					window.localStorage.setItem("invtyEncd", invtyEncd);
					window.localStorage.setItem("whsEncd", whsEncd);
					//存货列表显示
					$("#insertList").css("opacity", 0);
					$("#insertList").hide();
					$("#purchaseOrder").hide();
					$("#whsDocList").hide();
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
			if(cellname == "projEncd") {
				GetProInfo1(rowid, val);
			}
			if(cellname == "whsEncd") {
				GetWhsInfo(rowid, val);
			}
			//获取商品信息
			if(cellname == "invtyEncd") {
				sell_GetGoodsInfo(rowid, val);
				$("#jqgrids").setRowData(rowid, {
					isNtRtnGoods: 0,
				});
			}
			//生产日期
			if(cellname == "prdcDt") {
				var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
				var baoZhiQi = rowDatas.baoZhiQi; 
				var isQuaGuaPer = rowDatas.isQuaGuaPer;
				if(isQuaGuaPer == 0) {
					alert("该存货非保质期管理，不需要填写效期")
					$("#jqgrids").setRowData(rowid, {
						prdcDt: "",
						invldtnDt:"",
					});
				}else{
					setProductDate(rowid, val,baoZhiQi)
				}
			}
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
		buttonicon: "ui-icon-plus",
		onClickButton: function() {
			//新增一行操作
			addRows();
		},
		position: "last"
	})
})


function GetProInfo1(rowid, val) {
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
	var list = getJQAllData();
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
		$('button').addClass("gray")
		$('.saveOrder').removeClass("gray");
		$('.upOrder').removeClass("gray");
		$('.editOrder').addClass("gray"); //点击修改后 修改不能用
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true);
	});
})

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

function IsCheckValue(custId, bizTypId, accNum, listData) {
	var count = getInvtyData();
	if(custId == null || custId == "") {
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
	} else if(listData.length<count.length){
		alert("明细中缺少仓库");
		return false;
	} else if(listData.length != 0) {
		for(var i = 0; i < listData.length; i++) {
			if(listData[i].invtyEncd == "") {
				alert("存货不能为空")
				return false;
			} else if(listData[i].prdcDt == "") {
				alert("生产日期不能为空")
				return false;
			} else if(listData[i].batNum == "") {
				alert("批次不能为空")
				return false;
			} else if(listData[i].qty == "") {
				alert("数量不能为空")
				return false;
			}  else if(listData[i].qty <0) {
				alert("数量不能小于0")
				return false;
			}  
		}
	}
	return true;
}
var delvSnglId;
//保存修改后的数据
function SaveModifyData() {
	var listData = getJQAllData();

	//获得行号
	var gr = $("#jqGrid").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowDatas = $("#jqGrid").jqGrid('getRowData', gr);

	delvSnglId = $("input[name='delvSnglId']").val();
	var delvSnglDt = $("input[name='delvSnglDt']").val();
	var pursOrdrId = $("input[name='pursOrdrId']").val();
	var toGdsSnglId = $("input[name='toGdsSnglId']").val();
	var bizTypId = $("select[name='bizType']").val();
	var custId = $("#custId").val();
	var userName = $("#userName").val();
	var tel = localStorage.tel;
	var contcr = localStorage.contcr;
	var recvAddr = $("#addr").val();
	if(recvAddr != "") {
		var addr = recvAddr;
	} else if(localStorage.addr) {
		var addr = localStorage.addr;
	}
	var mdfr = localStorage.loginAccNum;
	var deptId = $("#deptId").val();
	var custOrdrNum = $("input[name='custOrdrNum']").val();
	var outIntoWhsTypId = $("input[name='outIntoWhsTypId']").val();
	var accNum = $("#user").val();
	var memo = $("input[name='memo']").val();

	if(IsCheckValue(custId, bizTypId, accNum, listData) == true) {

		var savedata = {
			"reqHead": reqHead,
			"reqBody": {
				"isNtRtnGood": 0,
				'sellTypId': "2",
				'delvSnglId': delvSnglId,
				'delvSnglDt': delvSnglDt,
				'pursOrdrId': pursOrdrId,
				'toGdsSnglId': toGdsSnglId,
				'outIntoWhsTypId': outIntoWhsTypId,
				'custId': custId,
				'deptId': deptId,
				'userName': userName,
				'custOrdrNum': custOrdrNum,
				'accNum': accNum,
				"bizTypId": bizTypId,
				'recvrTel': tel,
				'recvrAddr': addr,
				'recvr': contcr,
				'mdfr': mdfr,
				'memo': memo,
				'list': listData
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			url: url + '/mis/purc/EntrsAgnDelv/editEntrsAgnDelv',
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
					$(".editOrder").removeClass("gray");
					$(".delOrder").removeClass("gray");
					$(".toExamine").removeClass("gray");
					$(".search").removeClass("gray");
					$(".saveOrder").addClass("gray")
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
	var listData = getJQAllData();
	var delvSnglDt = $("input[name='delvSnglDt']").val(); //销售日期
	var custId = $("#custId").val();
	var userName = $("#userName").val();
	var sellTypId = $("input[name='sellType']").val();
	var bizTypId = $("select[name='bizType']").val();
	var accNum = $("#user").val();
	var tel = localStorage.tel;
	var contcr = localStorage.contcr;
	var recvAddr = $("#addr").val();
	if(recvAddr != "") {
		var addr = recvAddr;
	} else if(localStorage.addr) {
		var addr = localStorage.addr;
	}
	var setupPers = localStorage.loginAccNum;
	var deptId = $("#deptId").val();
	var txId = $("select[name='txId']").val();
	var memo = $("input[name='memo']").val();
	var custOrdrNum = $("input[name=custOrdrNum]").val();

	//判断页面是否有值为空
	if(IsCheckValue(custId, bizTypId, accNum, listData) == true) {
		var savedata = {
			"reqHead": reqHead,
			"reqBody": {
				"isNtRtnGood": 0,
				'sellTypId': "2",
				'delvSnglId': "",
				'delvSnglDt': delvSnglDt,
				'custId': custId,
				'deptId': deptId,
				'userName': userName,
				"bizTypId": bizTypId,
				'accNum': accNum,
				'txId': txId,
				'recvrTel': tel,
				'recvrAddr': addr,
				'recvr': contcr,
				'setupPers': setupPers,
				'memo': memo,
				'custOrdrNum': custOrdrNum,
				'formTypEncd': "023",
				'list': listData
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			url: url + '/mis/purc/EntrsAgnDelv/addEntrsAgnDelv',
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
				delvSnglId = data.respBody.delvSnglId;
				$("input[name='delvSnglId']").val(data.respBody.delvSnglId); //订单编码
				if(data.respHead.isSuccess == true) {
					$(".addOrder").removeClass("gray");
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
				}

			},
			error: function(error) {
				console.log(error);
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
				"delvSnglId": delvSnglId
			}
		}
		var deleteData = JSON.stringify(deleteAjax);
		if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + '/mis/purc/EntrsAgnDelv/deleteEntrsAgnDelv',
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
				"isNtRtnGood": 0,
				"delvSnglId": delvSnglId,
				"isNtChk": x,
			}]
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/purc/EntrsAgnDelv/updateEntrsAgnDelvIsNtChkList',
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
	delvSnglId = localStorage.delvSnglId;
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"delvSnglId": delvSnglId,
		}
	};
	var saveData = JSON.stringify(savedata);
	var myDate = {};
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/purc/EntrsAgnDelv/queryEntrsAgnDelv',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			var list = data.respBody;
			$("input[name='custOrdrNum']").val(list.custOrdrNum);
			$("input[name='deptName']").val(list.deptName);
			$("input[name='sellType']").val(list.sellTypNm);
			$("input[name='delvSnglDt']").val(list.delvSnglDt);
			$("input[name='delvSnglId']").val(list.delvSnglId);
			$("input[name='accNum']").val(list.accNum);
			$("input[name='custId']").val(list.custId);
			$("#userName").val(list.userName)
			$("#deptId").val(list.deptId);
			$("#custNm").val(list.custNm);
			$("#bizType").val(list.bizTypId);

			$("input[name='addr']").val(list.recvrAddr);
			$("input[name='txId']").val(list.txId);
			$("input[name='memo']").val(list.memo);

			var arr = eval(data); //数组
			var list1 = arr.respBody.entrsAgnDelvSub;
			$("#jqgrids").jqGrid('clearGridData');
			$("#jqgrids").jqGrid('setGridParam', {
				datatype: 'local',
				data: list1, //newData是符合格式要求的重新加载的数据
			}).trigger("reloadGrid")
			sumAdd()
		}
	})
}