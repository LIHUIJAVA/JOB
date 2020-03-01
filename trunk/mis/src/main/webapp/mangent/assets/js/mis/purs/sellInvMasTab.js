//销售普通发票
var mType = 0;

var grid = "";
if($("#gbox_jqGrids").hide()) {
	grid = "jqgrids";
} else if($("#gbox_jqgrids").hide()) {
	grid = "jqGrids";
}
$(function() {
	$('button').addClass("gray");
	$('.addOrder').removeClass("gray")
	$(".addWhs").removeClass("gray")
	$(".yes").removeClass("gray")
	$(".no").removeClass("gray")
	$(".cancel").removeClass("gray")
	$(".find").removeClass("gray")
	$('.yes').attr('disabled', false);
	$('.no').attr('disabled', false);
	$(".refer").removeClass("gray")
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

// 点击增加按钮，执行的操作
//$(function() {
//	$('.addOrder').click(function() {
//		$(".saveOrder").removeClass("gray");
//		$(".upOrder").removeClass("gray");
//		$('.addOrder').addClass("gray");
//		$('button').attr('disabled', false);
//		$(".gray").attr('disabled', true)
//
//		mType = 1;
//
//		$("#" + grid).jqGrid('setGridParam', {}).trigger("reloadGrid")
//		$("#mengban").hide();
//		$("#gbox_jqGrids").hide();
//		$("#gbox_jqgrids").show();
//
//		$(".inputText").val("");
//		$("input[name=invTyp]").val("销售专用发票");
//
//		var time = BillDate();
//		//入库日期
//		$("input[name=bllgDt]").val(time);
//	});
//});

//点击参照按钮，执行的操作
$(function(outWhsId) {
	$(".tiaojian").click(function() {
		$("#tk").show()
		$("#big_wrap").show()
		$("#tk").css("opacity", 1)
		$("input[name='accNum1']").attr('id', 'user'); // 给弹框中重复的加id
		$("input[name='accNum']").attr('id', ''); // 给弹框外重复的去除id
		$("input[name='userName1']").attr('id', 'userName'); // 给弹框中重复的加id
		$("input[name='userName']").attr('id', ''); // 给弹框外重复的去除id
		
		$("input[name='custId1']").attr('id', 'custId'); // 给弹框中重复的加id
		$("input[name='custId']").attr('id', ''); // 给弹框外重复的去除id
		$("input[name='custNm1']").attr('id', 'custNm'); // 给弹框中重复的加id
		$("input[name='custNm']").attr('id', ''); // 给弹框外重复的去除id
		
		$("input[name='custId1']").attr('class', 'custId encds'); // 给弹框中重复的加id
		$("input[name='custId']").attr('class', 'encds'); // 给弹框外重复的去除id
		$("input[name='custNm1']").attr('class', 'custNm names'); // 给弹框中重复的加id
		$("input[name='custNm']").attr('class', 'names'); // 给弹框外重复的去除id
	})
	$(".refer").click(function() {
		mType = 1;
		$("#whsDocList").hide();
		$("#insertList").hide();
		$("#purchaseOrder").hide();
		$("#ProjClsList").hide();
		$("#purs_list").show();
		$("#purs_list").css("opacity", 1)
		$(".tiaojian").removeClass("gray") //取消
		$('.tiaojian').attr('disabled', false);
		$(".sure").removeClass("gray") //确定
		$(".refer_cancel").removeClass("gray");
		$('.refer_cancel').attr('disabled', false);
		$('.sure').attr('disabled', false);
		$("#tk").show()
		$("#big_wrap").show()
		$("#tk").css("opacity", 1)
		
		$("input[name='accNum1']").attr('id', 'user'); // 给弹框中重复的加id
		$("input[name='accNum']").attr('id', ''); // 给弹框外重复的去除id
		$("input[name='userName1']").attr('id', 'userName'); // 给弹框中重复的加id
		$("input[name='userName']").attr('id', ''); // 给弹框外重复的去除id
		
		$("input[name='custId1']").attr('id', 'custId'); // 给弹框中重复的加id
		$("input[name='custId']").attr('id', ''); // 给弹框外重复的去除id
		$("input[name='custNm1']").attr('id', 'custNm'); // 给弹框中重复的加id
		$("input[name='custNm']").attr('id', ''); // 给弹框外重复的去除id
		
		$("input[name='custId1']").attr('class', 'custId encds'); // 给弹框中重复的加id
		$("input[name='custId']").attr('class', 'encds'); // 给弹框外重复的去除id
		$("input[name='custNm1']").attr('class', 'custNm names'); // 给弹框中重复的加id
		$("input[name='custNm']").attr('class', 'names'); // 给弹框外重复的去除id
	})
	$(".no").click(function() {
		$("#tk").hide()
		$("#big_wrap").hide()
		$("#tk").css("opacity", 0)
		$("select[name='formTypEncd']").val('')
		
		$("input[name='accNum1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='accNum']").attr('id', 'user'); // 给弹框外重复的去除id
		$("input[name='userName1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='userName']").attr('id', 'userName'); // 给弹框外重复的去除id
		
		$("input[name='custId1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='custId']").attr('id', 'custId'); // 给弹框外重复的去除id
		$("input[name='custNm1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='custNm']").attr('id', 'custNm'); // 给弹框外重复的去除id
		
		$("input[name='custId1']").attr('class', ''); // 给弹框中重复的加id
		$("input[name='custId']").attr('class', 'custId encds'); // 给弹框外重复的去除id
		$("input[name='custNm1']").attr('class', ''); // 给弹框中重复的加id
		$("input[name='custNm']").attr('class', 'custNm names'); // 给弹框外重复的去除id
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
	$(".sure").click(function() {
		$("#jqgrids").setColProp("ordrNum", {
			editable: true
		});
		$("#jqgrids").setColProp("sellSnglSubId", {
			editable: true
		});
		$("#jqgrids").setColProp("sellSnglNums", {
			editable: true
		})
		$("#jqgrids").setColProp("whsEncd", {
			editable: true
		});
		$("#jqgrids").setColProp("invtyEncd", {
			editable: true
		});
		$("#jqgrids").setColProp("projEncd", {
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
		$("#jqgrids").setColProp("intlBat", {
			editable: true
		});
		$("#jqgrids").setColProp("memo", {
			editable: true
		});
		$("#jqgrids").setColProp("isNtRtnGoods", {
			editable: true
		});
		var ids = $('#canzhao_jqGrids').jqGrid('getGridParam', 'selarrrow');
		var arrList = [];
		var length = ids.length;
		for(var i = 0; i < length; i++) {
			// 获取所选航的数据
			var str = $("#canzhao_jqGrids").jqGrid('getRowData', ids[i]);
			arrList.push(str)
		}
		
		var gr = $('#canzhao_jqGrids_b').jqGrid('getGridParam', 'selarrrow');
		var num1 = [];
		var length = gr.length;
		for(var i = 0; i < length; i++) {
			// 获取所选航的数据
			var rowDatas = $("#canzhao_jqGrids_b").jqGrid('getRowData', gr[i]);//获取行数据
			num1.push(rowDatas)
		}
		if(num1.length == 0) {
			alert("请选择单据")
		} else {
			for(var i = 0; i < arrList.length; i++) {
				if(arrList[i].isNtChk == "否") {
					arrList[i].isNtChk = 0
				} else if(arrList[i].isNtChk == "是") {
					arrList[i].isNtChk = 1
				}
			}
			searchsellSnglId(arrList,num1)
		}
	})
	$(".refer_cancel").click(function() {
		window.location.reload();
		$("#purs_list").hide();
		$("#purs_list").css("opacity", 0)
		$("#purchaseOrder").show();
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
			"ordrNum": "",
			"sellSnglSubId": "",
			"sellSnglNums": '',
			"whsNm": '',
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
			"batNum": '',
			"intlBat": '',
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
			if(obj.getRowData(rowIds[i]).whsNm == "") {
				arrayData.push(obj.getRowData(rowIds[i]));
			}
		}
	}
	return arrayData;
}
//参照到货单
function searchsellSnglId(arrList,num1) {
//	var kg = true;
//	for(var i = 0; i < arrList.length; i++) {
//		if(arrList[0].bizTypId != arrList[i].bizTypId ||
//			arrList[0].sellTypId != arrList[i].sellTypId ||
//			arrList[0].deptId != arrList[i].deptId ||
//			arrList[0].bizMemId != arrList[i].bizMemId ||
//			arrList[0].custId != arrList[i].custId) {
//			kg = false;
//			break;
//		}
//	}
//	if(kg == false) {
//		alert("所选择的的销售类型、业务类型、部门、业务员、客户存在不同，无法参照，请修改选择的单据")
//	} else if(kg == true) {
		
		$("input[name='accNum1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='accNum']").attr('id', 'user'); // 给弹框外重复的去除id
		$("input[name='userName1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='userName']").attr('id', 'userName'); // 给弹框外重复的去除id
		
		$("input[name='custId1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='custId']").attr('id', 'custId'); // 给弹框外重复的去除id
		$("input[name='custNm1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='custNm']").attr('id', 'custNm'); // 给弹框外重复的去除id
		
		$("input[name='custId1']").attr('class', ''); // 给弹框中重复的加id
		$("input[name='custId']").attr('class', 'custId encds'); // 给弹框外重复的去除id
		$("input[name='custNm1']").attr('class', ''); // 给弹框中重复的加id
		$("input[name='custNm']").attr('class', 'custNm names'); // 给弹框外重复的去除id
		
		var ids = $('#canzhao_jqGrids').jqGrid('getGridParam', 'selrow');
		var str = $("#canzhao_jqGrids").jqGrid('getRowData', ids);
		$("#gbox_jqgrids").show();
		$("#gbox_jqGrids").hide()
		$("#purs_list").css("opacity", 0);
		$("#purchaseOrder").show();	
		$(".addOrder").addClass("gray")
		$(".saveOrder").removeClass("gray")
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true);

		var nums = []
		for(var i = 0; i<arrList.length;i++) {
			$("input[name='custId']").val(arrList[0].custId);
			$("input[name='memo']").val(arrList[0].memo);
			$("input[name='custOrdrNum']").val(arrList[0].custOrdrNum);
			$("input[name='custNm']").val(arrList[0].custNm);
			$("input[name='deptId']").val(arrList[0].deptId);
			$("input[name='deptName']").val(arrList[0].deptName);
			$("#user").val(arrList[0].accNum);
			$("#userName").val(arrList[0].userName);
			$("input[name='invTyp']").val("销售专用发票");
			$("input[name='toFormTypEncd']").val(arrList[0].formTypEncd);
			$("select[name='sellTypId']").val(arrList[0].sellTypId);
			$("#bizType").val(arrList[0].bizTypId);
			//到货日期
			$("input[name=bllgDt]").val(BillDate());
			nums.push(arrList[i].sellInvNum)
			
			$("input[name='sellSnglNum']").val(nums.toString());
			window.form = arrList[i].formTypEncd
		}
		var num = []
		for(var i=0;i<num1.length;i++){
				var keyMap = {
					sellInvNum:'sellSnglNums',ordrNum:'sellSnglSubId'
				}
				var objs = Object.keys(num1[i]).reduce((newData,key) => {
					let newKey = keyMap[key] || key
					newData[newKey] = num1[i][key]
					return newData
				},{})
			num.push(objs)
		}
		referSure(num)
		$("#jqgrids").jqGrid('clearGridData');
		$("#jqgrids").jqGrid('setGridParam', {
			datatype: 'local',
			data: num, //newData是符合格式要求的重新加载的数据
		}).trigger("reloadGrid");
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
		qty = qty.toFixed(prec)
		noTaxAmt = precision(noTaxAmt,2)
		taxAmt = precision(taxAmt,2)
		prcTaxSum = precision(prcTaxSum,2)
		bxQty = bxQty.toFixed(prec)
		$("#jqgrids").footerData('set', {
			"whsNm": "本页合计",
			"qty": qty,
			"noTaxAmt": noTaxAmt,
			"taxAmt": taxAmt,
			"prcTaxSum": prcTaxSum,
			"bxQty": bxQty,
		});

//	}
}

//打开仓库/存货档案后点击确定取消
$(function() {
	//确定
	$(".addWhs").click(function() {
		//到货单
		//获得行号
		var rowid = $("#" + grid).jqGrid('getGridParam', 'selrow');

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
		$("#" + grid).setRowData(rowid, {
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
})

//初始化表格
$(function() {
	allHeight()
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	$("#" + grid).jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json",
		colNames: ['序号', '销售单号(参照主表)', '销售单号(参照子表)', '仓库名称', '仓库编码', '存货编码', '存货名称', '项目编码', '项目名称', '规格型号', '主计量单位', '主计量单位编码', '数量', '含税单价', '价税合计',
			'无税单价', '无税金额', '税率', '税额', '箱规', '箱数', '批次', '国际批次', '表体备注', '是否退货'
		], //jqGrid的列显示名字
		colModel: [{
				name: 'ordrNum',
				editable: false,
				sortable: false,
				hidden: true,
			},
			{
				name: 'sellSnglNums',
				editable: false,
				sortable: false,
//				hidden: true,
			},
			{
				name: 'sellSnglSubId',
				editable: false,
				sortable: false,
//				hidden: true,
			},
			{
				name: 'whsNm',
				editable: false,
				sortable: false,
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
				name: 'batNum',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'intlBat', //国际批次
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
		multiselect: true, //复选框 
		caption: '销售专用发票',
		altclass: true,
		pgbuttons: false,
		pginput: false,
		//		viewrecords: true,
		rowNum: 99999999,
		height: height,
		pager:"#jqgridPager",
		autoScroll: true,
		shrinkToFit: false,
		forceFit: true,
		sortable: false,
		pager: '#jqgridPager',
		footerrow: true,
		cellEdit: true,
		cellsubmit: "clientArray",

		//在编辑状态时
		afterEditCell: function(rowid, cellname, val, iRow, iCol) {
			$(".saveOrder").addClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
			$("#" + rowid + "_whsEncd").attr("readonly", "readonly");
			if(cellname == "whsEncd") {
				$("#" + rowid + "_whsEncd").bind("dblclick", function() {
					$("#whsDocList").show();
					$("#whsDocList").css("opacity", 1);
					$("#insertList").hide();
					$("#purchaseOrder").hide();
					$("#purs_list").hide();
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
						$(".find").removeClass("gray");
						$(".addWhs").removeClass("gray");
						$(".cancel").removeClass("gray");
						$('button').attr('disabled', false);
						$(".gray").attr('disabled', true)
						$("#insertList").show();
						$("#insertList").css("opacity", 1);
						$("#whsDocList").hide();
						$("#purchaseOrder").hide();
						$("#purs_list").hide();
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
					$("#tk").hide();
					$("#purs_list").hide();
					$("#insertList").hide();
					$("#whsDocList").hide();
					$("#purchaseOrder").hide();
					$("#ProjClsList").show();
					$("#ProjClsList").css("opacity", 1)
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
			if(cellname == "invtyEncd") {
				GetGoodsInfo(rowid, val);
			}
			if(cellname == "projEncd") {
				GetProInfo2(rowid, val);
			}
			//生产日期
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
				purs_SetNums(rowid, cellname,val)
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
				noTaxAmt = noTaxAmt.toFixed(8)
				taxAmt = taxAmt.toFixed(8)
				prcTaxSum = prcTaxSum.toFixed(2)
				bxQty = bxQty.toFixed(2)
				$("#jqgrids").footerData('set', {
					"whsNm": "本页合计",
					"qty": qty,
					"noTaxAmt": noTaxAmt,
					"taxAmt": taxAmt,
					"prcTaxSum": prcTaxSum,
					"bxQty": bxQty,
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
			alert("error");
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
		$("#jqgrids").setColProp("ordrNum", {
			editable: true
		});
		$("#jqgrids").setColProp("sellSnglSubId", {
			editable: true
		});
		$("#jqgrids").setColProp("sellSnglNums", {
			editable: true
		})
		$("#jqgrids").setColProp("whsEncd", {
			editable: true
		});
		$("#jqgrids").setColProp("invtyEncd", {
			editable: true
		});
		$("#jqgrids").setColProp("projEncd", {
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
		$("#jqgrids").setColProp("intlBat", {
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
	var obj = $("#" + grid);
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
//判断
function IsCheckValue(custId, sellTypId, accNum, listData) {
	var count = $("#jqgrids").getGridParam("reccount");
	if(custId == "") {
		alert("客户不能为空")
		return false;
	} else if(accNum == "") {
		alert("业务员不能为空")
		return false;
	} else if(sellTypId == 0) {
		alert("销售类型未选择")
		return false;
	} else if(bizType == "") {
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
			} else if(listData[i].qty == "") {
				alert("数量不能为空")
				return false;
			}
		}
	}
	return true;
}

//保存修改后的数据
function SaveModifyData() {
	var listData = getJQAllData();

	var custId = $("input[name='custId']").val();

	var invTyp = $("input[name='invTyp']").val();
	var bllgDt = $("input[name='bllgDt']").val();
	//	var custId = $("input[name='custNm']").val();
	sellInvNum = $("input[name='sellInvNum']").val();
	var custOrdrNum = $("input[name='custOrdrNum']").val();
	var bizTypId = $("select[name='bizType']").val();
	var accNum = $("input[name='accNum']").val();
	var deptId = $("#deptId").val();
	var sellTypId = $("select[name='sellTypId']").val();
	var userName = $("input[name='userName']").val();
	var sellSnglNum = $("input[name='sellSnglNum']").val();
	var vouchNum = $("input[name='vouchNum']").val();
	var rtnGoodsId = $("input[name='rtnGoodsId']").val();
	var toFormTypEncd = $("input[name='toFormTypEncd']").val();
	var memo = $("input[name='memo']").val();

	//判断页面是否有值为空
	if(IsCheckValue(custId, sellTypId, accNum, listData) == true) {
		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				'invTyp': invTyp,
				'bllgDt': bllgDt,
				'sellInvNum': sellInvNum,
				'bizTypId': bizTypId,
				'sellTypId': sellTypId,
				'custOrdrNum': custOrdrNum,
				'accNum': accNum,
				'userName': userName,
				"deptId": deptId,
				"toFormTypEncd": toFormTypEncd,
				'custId': custId,
				'vouchNum': vouchNum,
				'sellSnglNum': sellSnglNum,
				'rtnGoodsId': rtnGoodsId,
				'memo': memo,
				'formTypEncd': "021",
				'sellComnInvSubList': listData
			}
		};
		var saveData = JSON.stringify(savedata);
		console.log(saveData)
		$.ajax({
			url: url + '/mis/account/SellComnInv/updateSellComnInv',
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
					$('button').attr('disabled', false);
					$(".gray").attr('disabled', true)
					$("#mengban").show();
					$("#jqgrids").setColProp("ordrNum", {
						editable: false
					});
					$("#jqgrids").setColProp("sellSnglSubId", {
						editable: false
					});
					$("#jqgrids").setColProp("sellSnglNums", {
						editable: false
					})
					$("#jqgrids").setColProp("whsEncd", {
						editable: false
					});
					$("#jqgrids").setColProp("invtyEncd", {
						editable: false
					});
					$("#jqgrids").setColProp("projEncd", {
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
					$("#jqgrids").setColProp("intlBat", {
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
			error: function(error) {
				alert(error)
			} //错误执行方法
		})

	}
}
var sellInvNum;
//保存刚开始的数据
function SaveNewData() {
	var listData = getJQAllData();
	var custId = $("input[name='custId']").val();
	var invTyp = $("input[name='invTyp']").val();
	var bllgDt = $("input[name='bllgDt']").val();
	var userName = $("input[name='userName']").val();
	sellInvNum = $("input[name='sellInvNum']").val();
	var bizTypId = $("select[name='bizType']").val();
	var accNum = $("input[name='accNum']").val();
	var custOrdrNum = $("input[name='custOrdrNum']").val();
	var deptId = $("#deptId").val();
	var sellTypId = $("select[name='sellTypId']").val();
	//	var subjEncd = $("input[name='subjEncd']").val();
	var vouchNum = $("input[name='vouchNum']").val();
	var toFormTypEncd = $("input[name='toFormTypEncd']").val();
	var sellSnglNum = $("input[name='sellSnglNum']").val();
	var rtnGoodsId = $("input[name='rtnGoodsId']").val();
	var memo = $("input[name='memo']").val();

	//判断页面是否有值为空
	if(IsCheckValue(custId, sellTypId, accNum, listData) == true) {
		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				'invTyp': invTyp,
				'bllgDt': bllgDt,
				'sellInvNum': sellInvNum,
				'custOrdrNum': custOrdrNum,
				'bizTypId': bizTypId,
				'sellTypId': sellTypId,
				'accNum': accNum,
				'userName': userName,
				"deptId": deptId,
				'custId': custId,
				"toFormTypEncd": toFormTypEncd,
				'vouchNum': vouchNum,
				'sellSnglNum': sellSnglNum,
				'rtnGoodsId': rtnGoodsId,
				'memo': memo,
				'formTypEncd': "021",
				'sellComnInvSubList': listData
			}
		};
		var saveData = JSON.stringify(savedata);
		console.log(saveData)
		$.ajax({
			url: url + '/mis/account/SellComnInv/addSellComnInv',
			type: 'post',
			data: saveData,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			success: function(data) {
				alert(data.respHead.message)
				sellInvNum = data.respBody.sellInvNum;
				$("input[name='sellInvNum']").val(data.respBody.sellInvNum); //订单编码
				if(data.respHead.isSuccess == true) {
					$(".addOrder").removeClass("gray");
					$(".saveOrder").addClass("gray");
					$(".editOrder").removeClass("gray");
					$(".delOrder").removeClass("gray");
					$(".toExamine").removeClass("gray");
					$(".search").removeClass("gray");
					$('button').attr('disabled', false);
					$(".gray").attr('disabled', true)
					$("#mengban").show();
					$("#jqgrids").setColProp("ordrNum", {
						editable: false
					});
					$("#jqgrids").setColProp("sellSnglSubId", {
						editable: false
					});
					$("#jqgrids").setColProp("sellSnglNums", {
						editable: false
					})
					$("#jqgrids").setColProp("whsEncd", {
						editable: false
					});
					$("#jqgrids").setColProp("invtyEncd", {
						editable: false
					});
					$("#jqgrids").setColProp("projEncd", {
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
					$("#jqgrids").setColProp("intlBat", {
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
				alert("error");
			} //错误执行方法
		})
	} else {
		$("#mengban").hide()
	}
}

//是否审核		
function ntChk(x) {
	var sellInvNum = $("input[name='sellInvNum']").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"sellComnInvList": [{
				"sellInvNum": sellInvNum,
				"isNtChk": x
			}]
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/account/SellComnInv/updateSellComnInvIsNtChk',
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
			alert("审核失败")
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
		mType = 0;
		var deleteAjax = {
			"reqHead": {
				accNum: '1'
			},
			"reqBody": {
				"sellInvNum": sellInvNum
			}
		}
		var deleteData = JSON.stringify(deleteAjax);
		if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + '/mis/account/SellComnInv/deleteSellComnInvList',
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
	$("#mengban").show();
	$(".editOrder").removeClass("gray");
	$('button').attr('disabled', false);
	$(".gray").attr('disabled', true)
	var sellInvNum = localStorage.sellInvNum;
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"sellInvNum": sellInvNum
		}
	};
	var saveData = JSON.stringify(savedata);
	var myDate = {};
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/account/SellComnInv/selectSellComnInvById',
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
			console.log(data)
			var list = data.respBody;
			if(list.isNtChk == 1) {
				$(".toExamine").addClass("gray");
				$(".editOrder").addClass("gray");
				$(".refer").addClass("gray");
				$(".noTo").removeClass("gray");
				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true)
			}else if(list.isNtChk == 0) {
				$(".noTo").addClass("gray");
				$(".refer").removeClass("gray");
				$(".toExamine").removeClass("gray");
				$(".editOrder").removeClass("gray");
				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true)
			}
			$("select[name='sellTypId']").val(list.sellTypId);
			$("input[name='invTyp']").val(list.invTyp);
			$("input[name='bllgDt']").val(list.bllgDt);
			$("select[name='bizType']").val(list.bizTypId);
			$("input[name='sellInvNum']").val(list.sellInvNum);
			$("input[name='accNum']").val(list.accNum);
			$("input[name='deptId']").val(list.deptId);
			$("input[name='userName']").val(list.userName);
			$("input[name='deptName']").val(list.deptName);
			$("input[name='custId']").val(list.custId);
			$("input[name='custNm']").val(list.custNm);
			$("input[name='vouchNum']").val(list.vouchNum);
			$("input[name='sellSnglNum']").val(list.sellSnglNum);
			$("input[name='rtnGoodsId']").val(list.rtnGoodsId);
			$("input[name='toFormTypEncd']").val(list.toFormTypEncd);
			$("input[name='memo']").val(list.memo);

			var list2 = data.respBody.sellComnInvSubList;
			
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
			noTaxAmt = noTaxAmt.toFixed(8)
			taxAmt = taxAmt.toFixed(8)
			prcTaxSum = prcTaxSum.toFixed(2)
			bxQty = bxQty.toFixed(2)
			$("#jqgrids").footerData('set', {
				"whsNm": "本页合计",
				"qty": qty,
				"noTaxAmt": noTaxAmt,
				"taxAmt": taxAmt,
				"prcTaxSum": prcTaxSum,
				"bxQty": bxQty,
			});
		},
		error: function() {
			alert("查询失败")
		},
	})

}