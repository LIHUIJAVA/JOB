//采购专用发票
var mType = 0;

//刚开始时可点击的按钮
$(function() {
	$('button').addClass("gray");
	$(".refer").removeClass("gray") //参照
	$('.addOrder').removeClass("gray") //增加
	$(".addWhs").removeClass("gray") //确定
	$(".cancel").removeClass("gray") //取消
	$(".tiaojian").removeClass("gray") //取消
	$(".batNum_search").removeClass("gray")
	$(".find").removeClass("gray") //查询
	$("#boxs").hide()

	var afterUrl = window.location.search.substring(1);
	var b = [];
	b = afterUrl;
	if(b == 1) {
		var isNtChk = localStorage.isNtChk;
		if(isNtChk == 1) {
			$(".noTo").removeClass("gray")
		} else if(isNtChk == 0) {
			$('.toExamine').removeClass("gray");
			$('.delOrder').removeClass("gray");
			$('.editOrder').removeClass("gray");
		}
	}

	$('button').attr('disabled', false);
	$(".gray").attr('disabled', true)
})

// 点击增加按钮，执行的操作
//$(function() {
//	$('.addOrder').click(function() {
//		localStorage.removeItem('isNtChk');
//
//		$('button').addClass("gray");
//		$(".saveOrder").removeClass("gray");
//		$(".upOrder").removeClass("gray");
//		$(".refer").removeClass("gray") //参照
//		$('button').attr('disabled', false);
//		$(".gray").attr('disabled', true)
//
//		mType = 1;
//
//		$("#jqgrids").jqGrid('setGridParam', {}).trigger("reloadGrid")
//		$("#mengban").hide();
//		$("#gbox_jqGrids").hide();
//		$("#gbox_jqgrids").show();
//
//		$(".inputText").val("");
//		$("input[name=purType]").val("普通采购");
//		$("input[name=invTyp]").val("专用发票");
//		var time = BillDate();
//		//入库日期
//		$("input[name=bllgDt]").val(time);
//	});
//})

//点击参照按钮，执行的操作
$(function() {
	$(".tiaojian").click(function() {
		$("#big_wrap").show()
		$("#boxs").show()
	})
	$(".refer").click(function() {
		mType = 1;

		$('button').addClass("gray");
		$('.addOrder').removeClass("gray") //增加
		$(".sure").removeClass("gray") //确定
		$(".cancel").removeClass("gray") //取消
		$(".searcher").removeClass("gray") //查询
		$(".tiaojian").removeClass("gray") //查询

		$(".saveOrder").removeClass("gray");
		$(".upOrder").removeClass("gray");
		$('.refer').removeClass("gray");
		$(".no").removeClass("gray") //取消
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
		$("#mengban").hide();

		$("input[name=invTyp]").val("采购专用发票");
		$("#whsDocList").hide();
		$("#insertList").hide();
		$("#purchaseOrder").hide();
		$("#purs_list").show(); //到货单列表on
		$("#purs_list").css("opacity", 1)
		$("#big_wrap").show()
		$("#boxs").show()
	})
	$(".no").click(function() {
		$("#big_wrap").hide()
		$("#boxs").hide()
	})
	$(".sure").click(function() {
		$("#jqgrids").setColProp("whsEncd", {
			editable: true
		});
		$("#jqgrids").setColProp("invtyEncd", {
			editable: true
		});
		$("#jqgrids").setColProp("projEncd", {
			editable: true
		})
		$("#jqgrids").setColProp("qty", {
			editable: true
		});
		$("#jqgrids").setColProp("cntnTaxUprc", {
			editable: true
		});
		$("#jqgrids").setColProp("prcTaxSum", {
			editable: true
		});
		$("#jqgrids").setColProp("noTaxUprc", {
			editable: true
		});
		$("#jqgrids").setColProp("noTaxAmt", {
			editable: true
		});
		$("#jqgrids").setColProp("taxRate", {
			editable: true
		});
		$("#jqgrids").setColProp("bxQty", {
			editable: true
		});
		$("#jqgrids").setColProp("batNum", {
			editable: true
		});
		$("#jqgrids").setColProp("memo", {
			editable: true
		});
		$("#jqgrids").setColProp("isNtRtnGoods", {
			editable: true
		});
		var gr = $("#jqGrids_list").jqGrid('getGridParam', 'selarrrow');

		var rowData = [];

		for(i = 0; i < gr.length; i++) {
			var rowDatas = $("#jqGrids_list").jqGrid('getRowData', gr[i])
			rowData.push(rowDatas)
		}
		
		var grs = $("#jqGrids_list_b").jqGrid('getGridParam', 'selarrrow');
		var rowData1 = [];
		for(j = 0; j < grs.length; j++) {
			var rowDatas1 = $("#jqGrids_list_b").jqGrid('getRowData', grs[j])
			rowData1.push(rowDatas1)
		}
		
		if(rowData.length == 0) {
			alert("请选择单据")
		} else {
			searchintoWhsSnglId(rowData1,rowData)
		}
	})
	$(".refer_cancel").click(function() {
		window.location.reload();
	})
	
})

//增行   保存
//$(function() {
function fn() {
//	$(".addOne").click(function() {
		mType = 1;
		//拿到Gride中所有主键id的值
		var gr = $("#jqgrids").jqGrid('getDataIDs');
		if(gr.length == 0) {
			var rowid = 0;
		} else if(gr.length != 0) {
			// 获得当前最大行号（数据编码）
			var rowid = Math.max.apply(Math, gr);
		}
		// 获得新添加行的行号（数据编码）
		window.newrowid = rowid + 1;
		var dataRow = {
			"crspdIntoWhsSnglNum": "",
			"intoWhsSnglSubtabId": "",
			"whsEncd": '',
			"invtyEncd": '',
			"invtyNm": '',
			"spcModel": '',
			"measrCorpNm": '',
			"measrCorpId": '',
			"qty": '',
			"cntnTaxUprc": '',
			"prcTaxSum": '',
			"noTaxUprc": '',
			"noTaxAmt": '',
			"taxRate": '',
			"taxAmt": '',
			"bxRule": '',
			"bxQty": '',
			"crspdBarCd": '',
			"batNum": '',
			"memo": '',
			"isNtRtnGoods": '',
		};

		//将新添加的行插入到第一列
		$("#jqgrids").jqGrid("addRowData", newrowid, dataRow, "last");

}

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

//参照采购入库单
function searchintoWhsSnglId(rowData1,rowData) {
	var num = true;
	for(var i = 0; i < rowData.length; i++) {
		if(rowData[0].userName != rowData[i].userName || rowData[0].provrId != rowData[i].provrId || rowData[0].deptId != rowData[i].deptId) {
			num = false;
			break;
		}
	}
	if(num == false) {
		alert("所选择的的业务员、供应商、部门存在不同，无法参照，请修改选择的单据")
	} else if(num == true) {
		$("#gbox_jqgrids").show();
		$("#gbox_jqGrids").hide()
		$("#purs_list").css("opacity", 0);
		$("#purchaseOrder").show();
		var num = []
		for(var i = 0; i<rowData.length;i++) {
			$("input[name=purType]").val("普通采购");
			$("input[name='invTyp']").val("采购专用发票");
			$("input[name='bllgDt']").val(rowData[0].bllgDt);
			$("input[name='pursInvNum']").val(rowData[0].pursInvNum);
//			$("input[name='intoWhsSnglId']").val(rowData[0].intoWhsSnglId);
			$("input[name='pursOrdrId']").val(rowData[0].pursOrdrId);
			$("#user").val(rowData[0].accNum);
			$("#userName").val(rowData[0].userName);
			$("input[name='toFormTypEncd']").val(rowData[0].formTypEncd);
			$("input[name='provrOrdrNum']").val(rowData[0].provrOrdrNum);
			$("input[name='provrNm']").val(rowData[0].provrNm);
			$("input[name='provrId']").val(rowData[0].provrId);
			$("input[name='deptName']").val(rowData[0].deptName);
			$("input[name='deptId']").val(rowData[0].deptId);
			$("input[name='chkDt']").val(rowData[0].chkDt);
			$(".memo").val(rowData[0].memo);

			$("input[name=bllgDt]").val(BillDate());
			num.push(rowData[i].intoWhsSnglId)
		}
		$("input[name='intoWhsSnglId']").val(num.toString());
		var num = []
		for(var i=0;i<rowData1.length;i++){
//			delete encd[i].qty;
			var keyMap = {
				intoWhsSnglId:'crspdIntoWhsSnglNum',ordrNum:'intoWhsSnglSubtabId',
				unBllgQty:'qty',
			}
			var objs = Object.keys(rowData1[i]).reduce((newData,key) => {
				let newKey = keyMap[key] || key
				newData[newKey] = rowData1[i][key]
				return newData
			},{})
			num.push(objs)
		}
		referSure(num)
		$("#jqgrids").jqGrid('clearGridData');
		$("#jqgrids").jqGrid('setGridParam', {
			datatype: 'local',
			data: num, //newData是符合格式要求的重新加载的数据
		}).trigger("reloadGrid")
		var numList = getJQAllData1()
		if(numList.length == 0) {
			fn()
		}
		
		var list = getAllData();
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
		qty = qty.toFixed(2)
		noTaxAmt = precision(noTaxAmt,2)
		taxAmt = precision(taxAmt,2)
		prcTaxSum = precision(prcTaxSum,2)
		bxQty = bxQty.toFixed(2)
		$("#jqgrids").footerData('set', {
			"crspdIntoWhsSnglNum": "本页合计",
			"qty": qty,
			"noTaxAmt": noTaxAmt,
			"taxAmt": taxAmt,
			"prcTaxSum": prcTaxSum,
			"bxQty": bxQty,
		});
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

	})//	取消
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
		colNames: ['入库单号','序号','仓库编码', '存货编码', '存货名称', '项目编码', '项目名称', '规格型号', '主计量单位', '编码', '数量', '含税单价', '价税合计',
			'无税单价', '无税金额', '税率', '税额', '箱规', '箱数', '对应条形码', '批次', '表体备注', '是否退货'
		], //jqGrid的列显示名字
		colModel: [
			{
				name: 'crspdIntoWhsSnglNum',
				editable: false,
				align: 'center',
				sortable: false,
			},
			{
				name: 'intoWhsSnglSubtabId',
				editable: false,
				align: 'center',
				sortable: false,
//				hidden:true
			},
			{
				name: 'whsEncd',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'invtyEncd',
				editable: false,
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
				name: 'projEncd',
				editable: false,
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
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'cntnTaxUprc',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'prcTaxSum',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'noTaxUprc',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'noTaxAmt',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'taxRate',
				editable: false,
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
				editable: false,
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
				name: 'memo',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'isNtRtnGoods',
				editable: false,
				align: 'center',
				hidden: true,
				sortable: false
			},
		],
		loadonce: false,
		rownumbers: true,
		autowidth: true,
		autoScroll: true,
		shrinkToFit: false,
		multiselect: true, //复选框 
		caption: '采购专用发票',
		altclass: true,
		pager:"#jqgridPager",
		pgbuttons: false,
		pginput:false,
		rowNum:9999999,
//		viewrecords: true,
		height: height,
		forceFit: true,
		sortable: false,
		footerrow: true,
		cellEdit: true,
		cellsubmit: "clientArray",

		//在编辑状态时
		afterEditCell: function(rowid, cellname, val, iRow, iCol) {
			$("#findGrid").hide();
			$(".saveOrder").addClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
			$("#" + rowid + "_whsEncd").attr('readonly', 'readyonly')
			if(cellname == "whsEncd") {
				$("#" + rowid + "_whsEncd").bind("dblclick", function() {
					$("#whsDocList").show();
					$("#whsDocList").css("opacity", 1);
					$("#purs_list").hide();
					$("#batNum_list").hide();
					$("#insertList").hide();
					$("#purchaseOrder").hide();
					$(".addWhs").removeClass("gray") //确定可用
					$(".find").removeClass("gray") //查询可用
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
				//获得行数据
				var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
				if(rowDatas.whsNm == "") {
					alert("请先输入仓库档案");
					$("#" + rowid + "_invtyEncd").attr('disabled', 'disabled');
				} else {
					$("#" + rowid + "_invtyEncd").bind("dblclick", function() {
						$("#insertList").show();
						$("#insertList").css("opacity", 1);
						$("#whsDocList").hide();
						$("#batNum_list").hide();
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
					$("#boxs").hide();
					$("#batNum_list").hide();
					$("#purs_list").hide();
					$("#insertList").hide();
					$("#whsDocList").hide();
					$("#purchaseOrder").hide();
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
					$(".batNum_search").removeClass("gray")
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
			//获取商品信息
			if(cellname == "invtyEncd") {
				GetGoodsInfo(rowid, val);

				//获得行号
				var id = $("#jqgrids").jqGrid('getGridParam', 'selrow');
				//获得行数据
				var rowDatas = $("#jqgrids").jqGrid('getRowData', id);

				var invtyEncd = rowDatas.invtyEncd
				window.localStorage.setItem("invtyEncd", invtyEncd);
			}
			if(cellname == "projEncd") {
				GetProInfo2(rowid, val);
			}

			if(cellname == "whsEncd") {
				GetGoodsInfo(rowid, val);

				//获得行号
				var ids = $("#jqgrids").jqGrid('getGridParam', 'selrow');
				//获得行数据
				var rowData = $("#jqgrids").jqGrid('getRowData', ids);

				var whsEncd = rowData.whsEncd
				window.localStorage.setItem("whsEncd", whsEncd);
			}
			if(cellname == "prdcDt") {
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
				purs_SetNums(rowid, cellname, val);
				var list = getAllData();
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
				qty = qty.toFixed(2)
				noTaxAmt = precision(noTaxAmt,2)
				taxAmt = precision(taxAmt,2)
				prcTaxSum = precision(prcTaxSum,2)
				bxQty = bxQty.toFixed(2)
				$("#jqgrids").footerData('set', {
					"crspdIntoWhsSnglNum": "本页合计",
					"qty": qty,
					"noTaxAmt": noTaxAmt,
					"taxAmt": taxAmt,
					"prcTaxSum": prcTaxSum,
					"bxQty": bxQty,
				});
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
function GetProInfo2(rowid, val) {
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			'projEncd': val,
			"pageNo":1,
			"pageSize":1
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
		}, //错误执行方法
		success: function(data) {
			var list = data.respBody.list;
			for(var i = 0;i<list.length;i++) {
				var projNm = list[0].projNm
				//设置页面数据展示
				$("#jqgrids").setRowData(rowid, {
					projNm: projNm,
				});
			}
		}
	
	})
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
		$("#jqgrids").setColProp("projEncd", {
			editable: true
		})
		$("#jqgrids").setColProp("qty", {
			editable: true
		});
		$("#jqgrids").setColProp("cntnTaxUprc", {
			editable: true
		});
		$("#jqgrids").setColProp("prcTaxSum", {
			editable: true
		});
		$("#jqgrids").setColProp("noTaxUprc", {
			editable: true
		});
		$("#jqgrids").setColProp("noTaxAmt", {
			editable: true
		});
		$("#jqgrids").setColProp("taxRate", {
			editable: true
		});
		$("#jqgrids").setColProp("bxQty", {
			editable: true
		});
		$("#jqgrids").setColProp("batNum", {
			editable: true
		});
		$("#jqgrids").setColProp("memo", {
			editable: true
		});
		$("#jqgrids").setColProp("isNtRtnGoods", {
			editable: true
		});
		mType = 2;
		$("#mengban").hide();
		$('button').addClass('gray')
		$(".saveOrder").removeClass("gray");
		$('.upOrder').removeClass("gray");
		$('.editOrder').addClass("gray"); //点击修改后 修改不能用
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true);
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
	var provrId1 = provrId.split(",");
	var count = $("#jqgrids").getGridParam("reccount");
	if(provrId == "" || provrId == undefined) {
		alert("供应商不能为空")
		return false;
	} else if(provrId1.length > 1) {
		alert("供应商不能选多个")
		return false;
	}else if(accNum == "") {
		alert("业务员不能为空")
		return false;
	} else if(listData.length == 0) {
		alert("仓库不能为空")
		return false;
	} else if(listData.length<count){
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
			} else if(listData[i].batNum == "") {
				alert("批次不能为空")
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

//保存修改后的数据
function SaveModifyData() {
	var listData = getJQAllData();
	var invTyp = $("input[name='invTyp']").val();
	var provrOrdrNum = $("input[name='provrOrdrNum']").val();
	var bllgDt = $("input[name='bllgDt']").val();
	var pursInvNum = $("input[name='pursInvNum']").val();
	var provrId = $("#provrId").val();
	var accNum = $("#user").val();
	var deptId = $("#deptId").val();
	var userName = $("#userName").val();
	var toFormTypEncd = $("input[name='toFormTypEncd']").val();
	var provrNm = $("input[name='provrNm']").val();
	var deptName = $("input[name='deptName']").val();
	var memo = $(".memo").val();
	//判断页面是否有值为空
	if(IsCheckValue(provrId, accNum, listData) == true) {

		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				'invTyp': invTyp,
				'formTypEncd':'019',
				'pursInvNum': pursInvNum,
				'provrOrdrNum': provrOrdrNum,
				"toFormTypEncd": toFormTypEncd,
				'provrId': provrId,
				'userName': userName,
				'provrNm': provrNm,
				'pursTypId': '1',
				'deptName': deptName,
				'deptId': deptId,
				'accNum': accNum,
				'memo': memo,
				'list': listData
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			url: url + '/mis/account/PursComnInv/updatePursComnInv',
			type: 'post',
			data: saveData,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			success: function(data) {
				alert(data.respHead.message)
				if(data.respHead.isSuccess == true) {
					$(".addOrder").addClass("gray");
					$(".editOrder").removeClass("gray");
					$(".delOrder").removeClass("gray");
					$(".toExamine").removeClass("gray");
					$(".search").removeClass("gray");
					$(".saveOrder").addClass("gray");
					$('button').attr('disabled', false);
					$(".gray").attr('disabled', true)
					$("#mengban").show();
					$("#jqgrids").setColProp("whsEncd", {
						editable: false
					});
					$("#jqgrids").setColProp("invtyEncd", {
						editable: false
					});
					$("#jqgrids").setColProp("projEncd", {
						editable: false
					})
					$("#jqgrids").setColProp("qty", {
						editable: false
					});
					$("#jqgrids").setColProp("cntnTaxUprc", {
						editable: false
					});
					$("#jqgrids").setColProp("prcTaxSum", {
						editable: false
					});
					$("#jqgrids").setColProp("noTaxUprc", {
						editable: false
					});
					$("#jqgrids").setColProp("noTaxAmt", {
						editable: false
					});
					$("#jqgrids").setColProp("taxRate", {
						editable: false
					});
					$("#jqgrids").setColProp("bxQty", {
						editable: false
					});
					$("#jqgrids").setColProp("batNum", {
						editable: false
					});
					$("#jqgrids").setColProp("memo", {
						editable: false
					});
					$("#jqgrids").setColProp("isNtRtnGoods", {
						editable: false
					});
				}
			},
			error: function() {
			} //错误执行方法
		})
	}
}

//保存
function SaveNewData() {
	var listData = getJQAllData();
	var invTyp = $("input[name='invTyp']").val();
	var bllgDt = $("input[name='bllgDt']").val();
	var provrOrdrNum = $("input[name='provrOrdrNum']").val();
	var pursInvNum = $("input[name='pursInvNum']").val();
	var intoWhsSnglId = $("input[name='intoWhsSnglId']").val();
	var provrId = $("#provrId").val();
	var accNum = $("#user").val();
	var deptId = $("#deptId").val();
	var userName = $("#userName").val();
	var provrNm = $("input[name='provrNm']").val();
	var deptName = $("input[name='deptName']").val();
	var bllgDt = $("input[name='bllgDt']").val();
	var memo = $(".memo").val();
	var toFormTypEncd = $("input[name='toFormTypEncd']").val();
	//判断页面是否有值为空
	if(IsCheckValue(provrId, accNum, listData) == true) {

		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				'invTyp': invTyp,
				'pursInvNum': pursInvNum,
				'intoWhsSnglId': intoWhsSnglId,
				'provrId': provrId,
				'provrOrdrNum': provrOrdrNum,
				'pursTypId': '1',
				'userName': userName,
				'bllgDt': bllgDt,
				"toFormTypEncd": toFormTypEncd,
				'provrNm': provrNm,
				'deptName': deptName,
				'deptId': deptId,
				'accNum': accNum,
				'memo': memo,
				'formTypEncd':"019",
				'list': listData
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			url: url + '/mis/account/PursComnInv/addPursComnInv',
			type: 'post',
			data: saveData,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			success: function(data) {
				alert(data.respHead.message)
				if(data.respHead.isSuccess) {
					$(".addOrder").removeClass("gray");
					$(".editOrder").removeClass("gray");
					$(".delOrder").removeClass("gray");
					$(".toExamine").removeClass("gray");
					$(".search").removeClass("gray");
					$(".saveOrder").addClass("gray");
					$('button').attr('disabled', false);
					$(".gray").attr('disabled', true)
					$("#mengban").show();
					$("input[name='pursInvNum']").val(data.respBody.pursInvNum);
					$("#jqgrids").setColProp("whsEncd", {
						editable: false
					});
					$("#jqgrids").setColProp("invtyEncd", {
						editable: false
					});
					$("#jqgrids").setColProp("projEncd", {
						editable: false
					})
					$("#jqgrids").setColProp("qty", {
						editable: false
					});
					$("#jqgrids").setColProp("cntnTaxUprc", {
						editable: false
					});
					$("#jqgrids").setColProp("prcTaxSum", {
						editable: false
					});
					$("#jqgrids").setColProp("noTaxUprc", {
						editable: false
					});
					$("#jqgrids").setColProp("noTaxAmt", {
						editable: false
					});
					$("#jqgrids").setColProp("taxRate", {
						editable: false
					});
					$("#jqgrids").setColProp("bxQty", {
						editable: false
					});
					$("#jqgrids").setColProp("batNum", {
						editable: false
					});
					$("#jqgrids").setColProp("memo", {
						editable: false
					});
					$("#jqgrids").setColProp("isNtRtnGoods", {
						editable: false
					});
				}
			},
			error: function() {
			} //错误执行方法
		})
	}
}
var pursInvNum;
//是否审核		
function ntChk(x) {
	var pursInvNum = $("input[name='pursInvNum']").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"pursComnInvList": [{
				"pursInvNum": pursInvNum,
				"isNtChk": x
			}]
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/account/PursComnInv/updatePursComnInvIsNtChk',
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
		}
	})
}

$(function() {
	//审核
	$(".toExamine").click(function() {
		ntChk(1)
	});
	//弃审
	$(".noTo").click(function() {
		ntChk(0)
	})

	// 点击删除按钮，执行的操作
	$('.delOrder').click(function() {
		var pursInvNum = $("input[name='pursInvNum']").val();
		var deleteAjax = {
			"reqHead": reqhead,
			"reqBody": {
				"pursInvNum": pursInvNum
			}
		}
		var deleteData = JSON.stringify(deleteAjax);
		if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + '/mis/account/PursComnInv/deletePursComnInvList',
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
	var b = [];
	b = afterUrl;
	var a = 1
	if(a == b) {
		chaxun()
	}
})

function chaxun() {
	$("#mengban").show()
	$(".addOrder").addClass("gray");
	$(".editOrder").removeClass("gray");
	$(".delOrder").removeClass("gray");
	$(".toExamine").removeClass("gray");
	$(".search").removeClass("gray");
	$(".saveOrder").addClass("gray");
	$('button').attr('disabled', false);
	$(".gray").attr('disabled', true)
	pursInvNum = localStorage.pursInvNum;
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"pursInvNum": pursInvNum,
			"pageNo": 1,
			"pageSize": 500
		}
	};
	var saveData = JSON.stringify(savedata);
	var myDate = {};
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/account/PursComnInv/queryPursComnInvByPursInvNum',
		async: true,
		data: saveData,
		dataType: 'json',
		//开始加载动画  添加到ajax里面
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
			var list1 = data.respBody;
			if(list1.isNtChk == 1) {
				$(".toExamine").addClass("gray");
				$(".editOrder").addClass("gray");
				$(".delOrder").addClass("gray");
				$(".refer").addClass("gray");
				$(".noTo").removeClass("gray");
				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true)
			} else {
				$(".noTo").addClass("gray");
				$(".refer").removeClass("gray");
				$(".toExamine").removeClass("gray");
				$(".delOrder").removeClass("gray");
				$(".editOrder").removeClass("gray");
				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true)
			}
			$("input[name='purType']").val(list1.pursTypNm); //发票类型
			$("input[name='intoWhsSnglId']").val(list1.intoWhsSnglId); //发票类型
			$("input[name='invTyp']").val(list1.invTyp); //发票类型
			$("input[name='pursTypNm']").val(list1.pursTypNm); //采购类型
			$("input[name='bllgDt']").val(list1.bllgDt);
			$("input[name='pursInvNum']").val(list1.pursInvNum); //发票编码
			$("input[name='accNum']").val(list1.accNum); //业务员
			$("#userName").val(list1.userName); //业务员
			$("input[name='toFormTypEncd']").val(list1.toFormTypEncd);
			$("input[name='provrNm']").val(list1.provrNm);
			$("input[name='provrId']").val(list1.provrId);
			$("input[name='deptName']").val(list1.deptName);
			$("input[name='deptId']").val(list1.deptId);
			$(".memo").val(list1.memo);

			var list2 = data.respBody.pursList;

			$("#jqgrids").jqGrid('clearGridData');
			$("#jqgrids").jqGrid('setGridParam', {
				datatype: 'local',
				data: list2, //newData是符合格式要求的重新加载的数据
			}).trigger("reloadGrid")
			
			var list = getAllData();
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
			qty = qty.toFixed(2)
			noTaxAmt = precision(noTaxAmt,2)
			taxAmt = precision(taxAmt,2)
			prcTaxSum = precision(prcTaxSum,2)
			bxQty = bxQty.toFixed(2)
			$("#jqgrids").footerData('set', {
				"crspdIntoWhsSnglNum": "本页合计",
				"qty": qty,
				"noTaxAmt": noTaxAmt,
				"taxAmt": taxAmt,
				"prcTaxSum": prcTaxSum,
				"bxQty": bxQty,
			});
		}
	})
}