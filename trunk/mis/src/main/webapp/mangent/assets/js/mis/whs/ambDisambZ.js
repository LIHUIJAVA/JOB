
//组装、拆卸
var mType = 0; //保存类型
var kit = 0; //字母件
var isList = 0; //仓库还是存货
var isclick = true;

var grid = "";
if($("#gbox_jqGrids").hide()) {
	grid = "zi_jqgrids";
} else if($("#gbox_jqgrids").hide()) {
	grid = "jqGrids";
}
$(function() {
	getData();
	//1.母件
	$(document).on('keypress', '#momKitEncd', function(event) {
		if(event.keyCode == '13') {
			$('#momKitEncd').blur();
		}
	})

	$(document).on('blur', '#momKitEncd', function() {
		var momEncd = $("input[name='momKitEncd']").val()
		if(momEncd == '') {
			return
		}else {
			amb(momEncd)			
		}
	});
})
$(function() {
	$('button').addClass("gray");
	$('.addOrder').removeClass("gray")
	$(".addWhs").removeClass("gray")
	$(".batNum_search").removeClass("gray")
	$(".cancel").removeClass("gray")
	$(".find").removeClass("gray")
	$('button').attr('disabled', false);
	$(".gray").attr('disabled', true);

	var afterUrl = window.location.search.substring(1);
	var b = [];
	b = afterUrl;
	if(b == 1) {
		var isNtChk = localStorage.isNtChk;
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
		localStorage.removeItem('invtyEncd');

		$(".saveOrder").removeClass("gray");
		$(".upOrder").removeClass("gray");
		$('.addOrder').addClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)

		mType = 1;
		$("#mu_jqgrids").setColProp("whsEncd", {
			editable: true
		});
		$("#mu_jqgrids").setColProp("mbatNum", {
			editable: true
		});
		$("#mu_jqgrids").setColProp("momQty", {
			editable: true
		})
		$("#mu_jqgrids").setColProp("bxQty", {
			editable: true
		});
		$("#mu_jqgrids").setColProp("mprdcDt", {
			editable: true
		});
		$("#zi_jqgrids").setColProp("whsEncd", {
			editable: true
		});
		$("#zi_jqgrids").setColProp("subKitEncd", {
			editable: true
		});
		$("#zi_jqgrids").setColProp("sbatNum", {
			editable: true
		});
		$("#zi_jqgrids").setColProp("sprdcDt", {
			editable: true
		});
		$("#zi_jqgrids").setColProp("taxRate", {
			editable: true
		});
		$("input[name='momKitEncd']").removeAttr("readonly")
		$("input[name='memo']").removeAttr("readonly")
		$("input[name='feeComnt']").removeAttr("readonly")
		$("input[name='fee']").removeAttr("readonly")
		$("#" + grid).jqGrid('setGridParam', {}).trigger("reloadGrid")
//		$("#mengban").hide();
		$("#gbox_jqGrids").hide();
		$("#gbox_jqgrids").show();

		$("#mu_jqgrids").jqGrid('clearGridData');
	    $("#mu_jqgrids").jqGrid('setGridParam', {
			url: '../../assets/js/json/ambDis.json',
	        datatype: "json",
	    }).trigger("reloadGrid")
		$("#zi_jqgrids").jqGrid('clearGridData');
	    $("#zi_jqgrids").jqGrid('setGridParam', {
	        url: '../../assets/js/json/order.json',
	        datatype: "json",
	    }).trigger("reloadGrid")
	    $(".inputText").val("");
		//入库日期
		$("input[name=formDt]").val(BillDate());

	});
});


//点击表格图标显示存货列表--母配套件
$(function() {
	$(document).on('click', '.momKitNm', function() {
		var formDt = $("input[name='formDt']").val()
		if(formDt == '') {
			return;
		}else {
			$(".yes").removeClass("gray")
			$(".no").removeClass("gray")
			$(".search_amb").removeClass("gray")
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
			kit = 1;
			isList = 2; //存货
			$("#prod").show();
			$("#prod").css("opacity", 1);
			$("#insertList").hide();
			$("#insertList").css("opacity", 0);
			$("#whsDocList").hide();
			$("#purchaseOrder").hide();
			$("#batNum_list").hide();
		}
	});
})

//打开仓库/存货档案后点击确定取消
$(function() {
	//确定
	$(".yes").click(function() {

		//本单
		//获得行号
		var rowid = $("#" + grid).jqGrid('getGridParam', 'selrow');

		//	仓库档案
		//	获得行号
		var gr = $("#p_jqGrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDatas = $("#p_jqGrids").jqGrid('getRowData', gr);
		var momEncd = rowDatas.momEncd
		if(momEncd == undefined) {
			alert("未选择")
			return;
		}else {
			amb(momEncd)			
		}
		

		$("#whsDocList").css("opacity", 0);
		$("#insertList").css("opacity", 0);
		$("#purchaseOrder").show();
	})
	//	取消
	$(".no").click(function() {
		$("#whsDocList").css("opacity", 0);
		$("#insertList").css("opacity", 0);
		$("#purchaseOrder").show();
		//本单
		//获得行号
		var rowid = $("#" + grid).jqGrid('getGridParam', 'selrow');

		$("#" + rowid + "_whsNm").val("");
		$("#" + rowid + "_whsEncd").val("");
		$("#" + rowid + "_invtyEncd").val("")
	})
	
	//确定
	$(".addWhs").click(function() {

		//本单
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

		if(kit == 1) { //母配套件
			var invtyEncd;
			if(isList == 1) { //仓库
				$("#1_whsEncd").val(rowDatas.whsEncd);
				$("#mu_jqgrids").setRowData(1, {
					whsNm: rowDatas.whsNm
				})
				var whsEncd = rowDatas.whsEncd;
				localStorage.setItem("whsEncd", whsEncd);
			} else if(isList == 2) { //存货
				invtyEncd = rowData.invtyEncd;
				$("#momKitNm").val(rowData.invtyNm)
				$("#momKitEncd").val(rowData.invtyEncd)
				getAmbDis(invtyEncd);
				localStorage.setItem("invtyEncd", invtyEncd);
			}

		} else if(kit == 2) { //子配套件
			if(isList == 1) { //仓库
				$("#" + rowid + "_whsEncd").val(rowDatas.whsEncd);
				$("#zi_jqgrids").setRowData(rowid, {
					whsNm: rowDatas.whsNm
				})
				var whsEncd = rowDatas.whsEncd;
				localStorage.setItem("whsEncd", whsEncd);
			} else if(isList == 2) { //存货
				$("#" + rowid + "_subKitEncd").val(rowData.invtyEncd);
				var invtyEncd = rowData.invtyEncd;
				localStorage.setItem("invtyEncd", invtyEncd);
			}
		}

		$("#whsDocList").css("opacity", 0);
		$("#insertList").css("opacity", 0);
		$("#purchaseOrder").show();
	})
	//	取消
	$(".cancel").click(function() {
		$("#whsDocList").css("opacity", 0);
		$("#insertList").css("opacity", 0);
		$("#purchaseOrder").show();
		//本单
		//获得行号
		var rowid = $("#" + grid).jqGrid('getGridParam', 'selrow');

		$("#" + rowid + "_whsNm").val("");
		$("#" + rowid + "_whsEncd").val("");
		$("#" + rowid + "_invtyEncd").val("")
	})

	//确定
	$(".sure").click(function() {

		//	存货档案
		//	获得行号
		var ids = $("#batNum_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowData = $("#batNum_jqgrids").jqGrid('getRowData', ids);

		var rowid = $("#zi_jqgrids").jqGrid('getGridParam', 'selrow');
		$("#" + rowid + "_sbatNum").val(rowData.batNum);

		var prdoc = rowData.prdcDt;
		var unTaxUprc = rowData.unTaxUprc;
		var invldtnDt = rowData.invldtnDt;
		var pr = prdoc.split(" ")
		var p;
		for(var i = 0; i < pr.length; i++) {
			p = pr[0].toString()
		}
		$("#zi_jqgrids").setRowData(rowid, {
			sprdcDt: p,
			sunTaxUprc: unTaxUprc,
			sinvldtnDt: invldtnDt
		})

		$("#batNum_list").css("opacity", 0);
		$("#purchaseOrder").show();
		localStorage.removeItem("invtyEncd");
  		localStorage.removeItem("whsEncd");
	})
	//	取消
	$(".false2").click(function() {
		$("#batNum_list").css("opacity", 0);
		$("#purchaseOrder").show();
		//调拨单
		//获得行号
		var rowid = $("#zi_jqgrids").jqGrid('getGridParam', 'selrow');

	})
})
function amb(momEncd) {
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"momEncd": momEncd,
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/whs/prod_stru/queryAmbDisambSngl',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			if(data.respHead.isSuccess == true){
				var list = data.respBody.list;
				for(var i =0;i<list.length;i++) {
					$("input[name='momKitEncd']").val(data.respBody.list[0].momEncd)						
					$("input[name='momKitNm']").val(data.respBody.list[0].momNm)
					$("#mu_jqgrids").setRowData(1, {
						momKitEncd: data.respBody.list[0].momEncd,
						invtyNm: data.respBody.list[0].momNm,
						spcModel: data.respBody.list[0].momSpc,
						measrCorpNm: data.respBody.list[0].measrCorpNm,
						baoZhiQi: data.respBody.list[0].mbaoZhiQiDt,
						taxRate: data.respBody.list[0].moptaxRate,
						bxRule: data.respBody.list[0].mbxRule
					})
					var struSubList = data.respBody.list[0].struSubList;
					for(var j=0;j<struSubList.length;j++) {
//							var rowid = $("#zi_jqgrids").jqGrid('getGridParam', 'selrow');
						$("#zi_jqgrids").setRowData(j+1, {
							subKitEncd: struSubList[j].subEncd,
							invtyNm: struSubList[j].subNm,
							spcModel: struSubList[j].subSpc,
							momSubRatio: struSubList[j].subQty,
							measrCorpNm: struSubList[j].smeasrCorpNm,
							bxRule: struSubList[j].bxRule,
							baoZhiQi: struSubList[j].sbaoZhiQiDt,
							taxRate: struSubList[j].soptaxRate
						})
					}
				}
			}else if(data.respHead.isSuccess == false) {
				alert("该母配套件不存在")
			}
			
		},
		error: function() {
			alert('查询失败')
		}
	});
}
//初始化母配套件表格
$(function() {
	$("#mu_jqgrids").jqGrid({
		url: '../../assets/js/json/ambDis.json',
		datatype: "json",
		colNames: ['仓库编码', '仓库名称', '存货编码', '存货名称', '规格型号', '主计量单位', '批次', '数量', '箱数', '生产日期', '保质期', '失效日期',
			'税率', '含税单价', '含税金额', '无税单价', '无税金额', '税额', '箱规'
		], //jqGrid的列显示名字
		colModel: [{
				name: 'whsEncd', //仓库编码
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'whsNm', //仓库名称
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'momKitEncd', //存货编码
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'invtyNm', //存货名称
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'spcModel', //规格型号
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'measrCorpNm', //主计量单位
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'mbatNum', //批次
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'momQty', //母件数量
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'bxQty', //箱数
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'mprdcDt', //生产日期
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'baoZhiQi', //保质期
				editable: false,
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
				name: 'taxRate', //税率
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'mcntnTaxUprc', //含税单价
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'mcntnTaxAmt', //含税金额
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'munTaxUprc', //无税单价
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'munTaxAmt', //无税金额
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'mtaxAmt', //无税金额
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'bxRule', //箱规
				editable: false,
				align: 'center',
				sortable: false
			}
		],
		rowNum: 99999999,
		loadonce: false,
		rownumbers: true,
		autowidth: true,
		multiselect: true, //复选框 
		caption: '母配套件',
		altclass: true,
		viewrecords: true,
		height: 80,
		autoScroll: true,
		shrinkToFit: false,
		forceFit: true,
		sortable: false,
		cellEdit: true,
		cellsubmit: "clientArray",
		//在编辑状态时
		afterEditCell: function(rowid, cellname, val, iRow, iCol) {
			var rowData = $("#mu_jqgrids").jqGrid('getRowData', rowid);
			localStorage.setItem("whsEncd", rowData.whsEncd);
			localStorage.setItem("invtyEncd", rowData.momKitEncd)
			$(".saveOrder").addClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
//			$("#" + rowid + "_whsEncd").attr("readonly", "readonly");
			if(cellname == "whsEncd") {
				$("#" + rowid + "_whsEncd").bind("dblclick", function() {
					kit = 1;
					isList = 1;
					$("#whsDocList").show();
					$("#prod").hide();
					$("#whsDocList").css("opacity", 1);
					$("#insertList").hide();
					$("#batNum_list").hide();
					$("#purchaseOrder").hide();
				});
			}
			if(cellname == "mprdcDt") {
				$("input[name='mprdcDt']").attr("calendar","YYYY-MM-DD")
			}
			if(cellname == "momQty") {
				var list = getJQAllData6()
				for(var i = 0;i <list.length; i++) {
					if(list[i].sbatNum == '') {
						alert("请先填写子配套件批次")
						$("#" + rowid + "_momQty").attr("readonly", "readonly");
						return;
					}
				}
			}
			if((cellname == "momQty") ||
			   (cellname == "bxQty")) {
				$("input[name='momQty']").attr("onkeyup","if(this.value.length==1){this.value=this.value.replace(/[^0-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
				$("input[name='momQty']").attr("onafterpaste","if(this.value.length==1){this.value=this.value.replace(/[^0-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
				$("input[name='bxQty']").attr("onkeyup","if(this.value.length==1){this.value=this.value.replace(/[^0-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
				$("input[name='bxQty']").attr("onafterpaste","if(this.value.length==1){this.value=this.value.replace(/[^0-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
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
			//生产日期
			if(cellname == "mprdcDt" || cellname == "mbatNum") {
				setProductDate1(rowid, val)
			}
			if(cellname == "whsEncd") {
				GetWhsInfo1(rowid, val);
			}
			if((cellname == "bxQty") ||
				(cellname == "momQty")){
//				var rowDatas = $("#mu_jqgrids").jqGrid('getRowData', rowid);
//				var munTaxAmt = parseFloat(rowDatas.munTaxAmt) //无税金额
//				console.log(munTaxAmt)
//				if(munTaxAmt != '') {
//					alert(1)
					//设置变量
					SetNums1(rowid, cellname, val)
					sumAmt()
//				}
			}
			if(cellname == "momQty") {
				if(val < 0) {
					alert("数量不能小于0")
				}
			}
			if(cellname == "bxQty") {
				if(val < 0) {
					alert("箱数不能小于0")
				}
			}
		}

	})
})

//设置日期相关
function setProductDate1(rowid, invldtnDt) {
	var rowDatas = $("#mu_jqgrids").jqGrid('getRowData', rowid);
	//计算生产日期
	var prdcDt = rowDatas.mprdcDt //生产日期
	//保质期
	var baoZhiQi = rowDatas.baoZhiQi
	var invldtnDt = getNewDay(prdcDt, baoZhiQi);
	if(invldtnDt == "NaN-NaN-NaN") {
		invldtnDt = "";
	} else {
		invldtnDt = invldtnDt
	};
	$("#mu_jqgrids").setRowData(rowid, {
		invldtnDt: invldtnDt,
	});
}
function getJQAllData6() {
	//拿到grid对象
	var obj = $("#zi_jqgrids");
	//获取grid表中所有的rowid值
	var rowIds = obj.getDataIDs();
	//初始化一个数组arrayData容器，用来存放rowData
	var arrayData = new Array();
	if(rowIds.length > 0) {
		for(var i = 0; i < rowIds.length; i++) {
			if(obj.getRowData(rowIds[i]).subKitEncd == "") {
				continue;
			}else
			if(obj.getRowData(rowIds[i]).subKitEncd != "") {
				arrayData.push(obj.getRowData(rowIds[i]));
			}
		}
	}
	return arrayData;
}
function getJQAllData7() {
	//拿到grid对象
	var obj = $("#zi_jqgrids");
	//获取grid表中所有的rowid值
	var rowIds = obj.getDataIDs();
	//初始化一个数组arrayData容器，用来存放rowData
	var arrayData = new Array();
	if(rowIds.length > 0) {
		for(var i = 0; i < rowIds.length; i++) {
			if(obj.getRowData(rowIds[i]).subKitEncd == "") {
				continue;
			}else
			if(obj.getRowData(rowIds[i]).subKitEncd != "") {
				arrayData.push(obj.getRowData(rowIds[i]).sunTaxUprc);
			}
		}
	}
	return arrayData;
}
//设置数量和金额
function SetNums1(rowid, cellname, val) {
	var rowDatas = $("#mu_jqgrids").jqGrid('getRowData', rowid);
	var rowData = $("#zi_jqgrids").jqGrid('getRowData', rowid);
	
	var momQty = parseFloat(rowDatas.momQty);//母件数量
	var taxRate = parseFloat(rowData.taxRate);//母件数量
	
//	子件
	var listZ = getJQAllData6()
	for(var i = 0;i<listZ.length;i++) {
		var bxRule = listZ[i].bxRule;
		//无税金额  无税单价 含税单价 含税金额 箱数  子件数量
		var sAmt, scUprc, scAmt	, sbxQty, sbQty;
		
		var momSubRatio = listZ[i].momSubRatio //母子比例
		sbQty = parseFloat(momSubRatio * momQty); //子件数量
		if(isNaN(sbQty)) {
			sbQty = ""
		}
		sbxQty = parseFloat(sbQty / bxRule); //箱数
		if(isNaN(sbxQty)) {
			sbxQty = ""
		}
		$("#zi_jqgrids").setRowData(i + 1, {
			subQty: sbQty,
			sbxQty: sbxQty,
		});
		var sunTaxUprc = parseFloat(listZ[i].sunTaxUprc) //无税单价
		sAmt = toDecimal(sunTaxUprc * sbQty); //子件无税金额
		if(isNaN(sAmt)) {
			sAmt = ""
		}
		
		staxAmtNum = toDecimal(sAmt * taxRate * 0.01); //税额
		if(isNaN(staxAmtNum)) {
			staxAmtNum = ""
		}
	
		scAmt = toDecimal(sAmt * 1 + staxAmtNum * 1);//子件含税金额
		if(isNaN(scAmt)) {
			scAmt = ""
		}
		scUprc = toDecimal(scAmt / sbQty);//子件含税单价
		if(isNaN(scUprc)) {
			scUprc = ""
		}
		$("#zi_jqgrids").setRowData(i + 1, {
			scntnTaxAmt: scAmt,  //含税金额
			staxAmt: staxAmtNum,  //税额
			scntnTaxUprc: scUprc, //含税单价
			sunTaxAmt: sAmt,  //无税金额
			isNtRtnGoods: 1
		});
	}
	
	var list = getJQAllData4()
	var listNum = 0
	for(var k = 0;k<list.length;k++) {
		listNum += list[k]
	}
	console.log(listNum)
	var rowid = $("#mu_jqgrids").jqGrid('getGridParam', 'selrow');
	$("#mu_jqgrids").setRowData(rowid, {
		munTaxAmt: listNum
	})
	var rowDatas = $("#mu_jqgrids").jqGrid('getRowData', rowid);
	var munTaxAmt = parseFloat(rowDatas.munTaxAmt) //无税金额
	
//	母件
	var munTaxUprc = parseFloat(rowDatas.munTaxUprc) //无税单价
	var momQty = parseFloat(rowDatas.momQty) //数量
	var mcntnTaxUprc = parseFloat(rowDatas.mcntnTaxUprc) //含税单价
	var taxRate = parseFloat(rowDatas.taxRate) //税率
	var mcntnTaxAmt = parseFloat(rowDatas.mcntnTaxAmt) //含税金额
	var bxRule = parseFloat(rowDatas.bxRule) //箱规
	var bxQty = parseFloat(rowDatas.bxQty) //箱数
	
	bxQty = toDecimal(momQty / bxRule);  // 箱数
	
	if(!isNaN(bxQty)) {
		bxQty = parseFloat(bxQty);
	} //数量
	else {
		bxQty = "";
	}
	if(!isNaN(momQty)) {
		momQty = momQty
	} //盒数量
	else {
		momQty = ""
	}
	if(!isNaN(taxRate)) {
		taxRate = taxRate
	} //税率
	else {
		taxRate = ""
	}
	
//	   无税单价/含税单价/含税金额
	var mUprc, mcUprc, mAmt;
	taxAmtNum = toDecimal(munTaxAmt * taxRate * 0.01); //税额
	if(isNaN(taxAmtNum)) {
		taxAmtNum = ""
	}
	mUprc = toDecimal(munTaxAmt / momQty);  // 无税单价
	if(isNaN(mUprc)) {
		mUprc = ""
	}
	mAmt = toDecimal(munTaxAmt * 1 + taxAmtNum * 1);  // 含税金额
	if(isNaN(mAmt)) {
		mAmt = ""
	}
	mcUprc = toDecimal(mAmt / momQty);  // 含税单价
	if(isNaN(mcUprc)) {
		mcUprc = ""
	}
	console.log(mUprc, mcUprc, mAmt)
	$("#mu_jqgrids").setRowData(rowid, {
		mtaxAmt: taxAmtNum, //税额
		munTaxUprc: mUprc, //无税单价
		mcntnTaxAmt: mAmt,  //含税金额
		mcntnTaxUprc: mcUprc,  //含税单价
		bxQty: bxQty,  //箱数
		isNtRtnGoods: 1
	});
	
}


//获取本地保存的存货编码，获取存货数据，放进母套件表格中
function getAmbDis(invtyEncd) {
	var Data = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": invtyEncd
		}
	}
	var changeData = JSON.stringify(Data)
	$.ajax({
		type: "post",
		url: url + "/mis/purc/InvtyDoc/selectInvtyDocByInvtyDocEncd",
		async: true,
		data: changeData,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		success: function(data) {
			var list = data.respBody;
			$("#mu_jqgrids").setRowData(1, {
				momKitEncd: list.invtyEncd,
				invtyNm: list.invtyNm,
				spcModel: list.spcModel,
				bxRule: list.bxRule,
				measrCorpNm: list.measrCorpNm,
//				munTaxUprc: list.refCost,
				taxRate: list.iptaxRate,
				measrCorpId: list.measrCorpId,
				baoZhiQi: list.baoZhiQiDt,
				prcTaxSum: list.prcTaxSum,
			});

		},
		error: function() {
			alert("error")
		}
	})
}


function allHeight1() {
	$(window).resize(function() {
		$("#zi_jqgrids").setGridHeight($(window).height() - $('.purchaseTit ').outerHeight(true) - $('.order-title').outerHeight(true) - 119 - $("#gbox_mu_jqgrids").height());
		
	});
	var height1 = $('.purchaseTit').outerHeight(true);
	var height2 = $('.order-title').outerHeight(true);
	if(acc == 1) {
		height2 = height2
	} else if(acc == 2) {
		height2 = 0
	}
	var height3 = $(window).height()  // 浏览器当前窗口可视区域高度
	var height4 = $("#gbox_mu_jqgrids").height()  // 浏览器当前窗口可视区域高度
	window.height = height3 - height1 - height2 - 119 - height4;
}
//增行   保存
function fn() {
//	$(".addOne").click(function() {
		//拿到Gride中所有主键id的值
		var gr = $("#zi_jqgrids").jqGrid('getDataIDs');
		if(gr.length == 0) {
			var rowid = 0;
		} else if(gr.length != 0) {
			// 获得当前最大行号（数据编码）
			var rowid = Math.max.apply(Math, gr);
		}
		// 获得新添加行的行号（数据编码）
		window.newrowid = rowid + 1;
		var dataRow = {
			"whsEncd": "",
			"whsNm": "",
			"subKitEncd": '',
			"invtyNm": '',
			"spcModel": '',
			"measrCorpNm": '',
			"subQty": '',
			"sbxQty": '',
			"momSubRatio": '',
			"sbatNum": '',
			"sprdcDt": '',
			"baoZhiQi": '',
			"sinvldtnDt": '',
			"taxRate": '',
			"scntnTaxUprc": '',
			"scntnTaxAmt": '',
			"sunTaxUprc": '',
			"sunTaxAmt": '',
			"bxRule": '',
		};

		//将新添加的行插入到第一列
		$("#zi_jqgrids").jqGrid("addRowData", newrowid, dataRow, "last");

}

//初始化子配套件表格
$(function() {
	allHeight1()
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	$("#zi_jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json",
		colNames: ['仓库编码', '仓库名称', '存货编码*', '存货名称', '规格型号',
			'主计量单位', '批次*', '数量*', '箱数', '母子比例', '生产日期', '保质期', '失效日期',
			'税率', '含税单价', '含税金额', '无税单价', '无税金额','税额', '箱规'
		], //jqGrid的列显示名字
		colModel: [{
				name: 'whsEncd', //仓库编码
				editable: false,
				align: 'center',
				sortable: false
			}, {
				name: 'whsNm', //仓库名称
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'subKitEncd', //存货编码
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'invtyNm', //存货名称
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'spcModel', //规格型号
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'measrCorpNm', //主计量单位
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'sbatNum', //批次
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'subQty', //数量
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'sbxQty', //箱数
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'momSubRatio', //母子比例
				editable: false,
				align: 'center',
				editrules: {
					required: true,
					number: true
				},
				sortable: false
			},
			{
				name: 'sprdcDt', //生产日期
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'baoZhiQi', //保质期
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'sinvldtnDt', //失效日期
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'taxRate', //税率
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'scntnTaxUprc', //含税单价
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'scntnTaxAmt', //含税金额
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'sunTaxUprc', //无税单价
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'sunTaxAmt', //无税金额
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'staxAmt', //税额
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'bxRule', //箱规
				editable: false,
				align: 'center',
				sortable: false
			}
		],
		loadonce: false,
		rownumbers: true,
		autowidth: true,
		multiselect: true, //复选框 
		caption: '子配套件',
		altclass: true,
		pgbuttons: false,
		pginput: false,
		//		viewrecords: true,
		pager: '#zi_jqGridPager', //表格页脚的占位符(一般是div)的id
		height: height,
		autoScroll: true,
		shrinkToFit: false,
		forceFit: true,
		sortable: false,
		cellEdit: true,
		cellsubmit: "clientArray",
		footerrow: true,
		gridComplete: function() {
			$("#zi_jqgrids").footerData('set', {
				"whsEncd": "本页合计",
			});
		},
		//在编辑状态时
		afterEditCell: function(rowid, cellname, val, iRow, iCol) {
			$(".saveOrder").addClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
//			$("#" + rowid + "_whsEncd").attr("readonly", "readonly");
			$("#" + rowid + "_sbatNum").attr("readonly", "readonly");
			if(cellname == "whsEncd") {
				$("#" + rowid + "_whsEncd").bind("dblclick", function() {
					kit = 2;
					isList = 1;
					$("#whsDocList").show();
					$("#whsDocList").css("opacity", 1);
					$("#insertList").hide();
					$("#prod").hide();
					$("#purchaseOrder").hide();
				});
			}
			if(cellname == "subKitEncd") {
				//获得行数据
				var rowDatas = $("#zi_jqgrids").jqGrid('getRowData', rowid);
				if(rowDatas.whsEncd == "") {
					alert("请先输入仓库档案");
					$("#" + rowid + "_subKitEncd").attr('disabled', 'disabled');
				} else {
					$("#" + rowid + "_" + cellname).bind('keyup', function() {
						findGrid(rowid, cellname, val);
					})
					$("#" + rowid + "_subKitEncd").bind("dblclick", function() {
						kit = 2;
						isList = 2;
						$("#insertList").show();
						$("#insertList").css("opacity", 1);
						$("#whsDocList").hide();
						$("#prod").hide();
						$("#batNum_list").hide();
						$("#purchaseOrder").hide();
					});
				}
			}
			if(cellname == "sbatNum") {
				var rowDatas = $("#zi_jqgrids").jqGrid('getRowData', rowid);
				localStorage.setItem("whsEncd", rowDatas.whsEncd);
				localStorage.setItem("invtyEncd", rowDatas.subKitEncd)
				isMom = 0;
				kit = 2;
				//双击批次时
				if(rowDatas.whsEncd == "") {
					alert("请输入仓库档案")
				} else {
					$("#" + rowid + "_" + cellname).bind("dblclick", function() {
						$(".sure").removeClass("gray");
						$(".false2").removeClass("gray");
						$(".search").removeClass("gray");
						$('button').attr('disabled', false);
						$(".gray").attr('disabled', true)

						//存货列表显示
						$("#insertList").css("opacity", 0);
						$("#insertList").hide();
						//调拨单隐藏
						$("#purchaseOrder").hide();
						$("#prod").hide();
						$("#whsDocList").hide();
						$("#batNum_list").css("opacity", 1);
						$("#batNum_list").css("display", "block");
						searcherBatNum();
					});
				}

			}
			if(cellname == "sprdcDt") {
				$("input[name='sprdcDt']").attr("calendar","YYYY-MM-DD")
			}
			if(cellname == "momSubRatio") {
				var rowDatas = $("#zi_jqgrids").jqGrid('getRowData', rowid);
				var rowData = $("#mu_jqgrids").jqGrid('getRowData', rowid);
				var momQty = rowData.momQty
				if(momQty == "") {
					alert("未填入母件数量！");
					$("#" + rowid + "_momSubRatio").attr('disabled', 'disabled');
				}
				if(rowDatas.sbatNum == "") {
					alert("未选择子件批次");
					$("#" + rowid + "_momSubRatio").attr('disabled', 'disabled');
				}
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
			if(cellname == "subKitEncd") {
				whs_GetGoodsInfo(rowid, val);
			}
			if(cellname == "whsEncd") {
				var rowData = $("#" + grid).jqGrid('getRowData', rowid);
				var whsEncd = rowData.whsEncd
			}
			if(cellname == "whsEncd") {
				GetWhsInfo2(rowid, val);
			}
			if(cellname == "sprdcDt") {
				var rowDatas = $("#" + grid).jqGrid('getRowData', rowid);
				//计算生产日期
				var prdcDt = rowDatas.sprdcDt //生产日期
				//保质期
				var baoZhiQi = rowDatas.baoZhiQi
				console.log(baoZhiQi)
				var sinvldtnDt = getNewDay(prdcDt, baoZhiQi);
				if(sinvldtnDt == "NaN-NaN-NaN") {
					sinvldtnDt = "";
				} else {
					sinvldtnDt = sinvldtnDt
				};
				console.log(sinvldtnDt)
				$("#" + grid).setRowData(rowid, {
					sinvldtnDt: sinvldtnDt,
					//		baoZhiQiDt: baoZhiQi
				});
			}
			
			if(cellname == "sbatNum") {
				setProductDate(rowid, val)
			}
		}
	})

	$("#zi_jqgrids").jqGrid('navGrid', '#zi_jqGridPager', {
		edit: false,
		add: false,
		del: false,
		search: false,
		refresh: false,
	}).navButtonAdd('#zi_jqGridPager', {
		caption: "",
		buttonicon: "ui-icon-trash",
		onClickButton: function() {
			var grid = "zi_jqgrids"
			//删除一行操作
			removeRows1(grid);
		},
		position: "first"
	}).navButtonAdd('#zi_jqGridPager', {
		caption: "",
		buttonicon: "ui-icon-newwin",
		onClickButton: function() {
			//复制一行操作
			copyRows1();
		},
		position: "last"
	}).navButtonAdd('#zi_jqGridPager', {
		caption: "",
		buttonicon: "ui-icon-plus",
		onClickButton: function() {
			//新增一行操作
			addRows1();
		},
		position: "last"
	})
})
//新增表格行
function addRows1() {
	mType = 1;
	var gr = $("#zi_jqgrids").jqGrid('getDataIDs');
	if(gr.length == 0) {
		var rowid = 0;
	} else if(gr.length != 0) {
		var rowid = Math.max.apply(Math, gr);
	}
	window.newrowid = rowid + 1;
	var dataRow = {};
	$("#zi_jqgrids").jqGrid("addRowData", newrowid, dataRow, "last");
}
//删除表格行
function removeRows1() {
	var gr = $("#zi_jqgrids").jqGrid('getGridParam', 'selarrrow');
	if(gr.length == 0) {
		alert("请选择要删除的行");
		return;
	} else {
		var length = gr.length;
		for(var i = 0; i < length + 1; i++) {
			$("#zi_jqgrids").jqGrid("delRowData", gr[0]);
		}
	}
}
//复制表格行
function copyRows1() {
	var ids = $("#zi_jqgrids").jqGrid('getGridParam', 'selarrrow');
	var dataRow = $("#zi_jqgrids").jqGrid('getRowData', ids);
	if(ids.length == 0) {
		alert("请选择要复制的行");
		return;
	} else if(ids.length > 1) {
		alert("每次只能复制一行");
		return;
	} else {
		var gr = $("#zi_jqgrids").jqGrid('getDataIDs');
		// 选中行实际表示的位置
		var rowid = Math.max.apply(Math, gr);
		// 新插入行的位置
		var newrowid = rowid + 1;
		// 插入一行
		$("#zi_jqgrids").jqGrid("addRowData", newrowid, dataRow, "last");
	}
}
function getJQAllData4() {
	//拿到grid对象
	var obj = $("#zi_jqgrids");
	//获取grid表中所有的rowid值
	var rowIds = obj.getDataIDs();
	//初始化一个数组arrayData容器，用来存放rowData
	var arrayData = new Array();
	if(rowIds.length > 0) {
		for(var i = 0; i < rowIds.length; i++) {
			if(obj.getRowData(rowIds[i]).sunTaxAmt == "") {
				continue;
			}else
			if(obj.getRowData(rowIds[i]).sunTaxAmt != "") {
				arrayData.push(obj.getRowData(rowIds[i]).sunTaxAmt * 1);
			}
		}
	}
	return arrayData;
}
function sumAmt() {
	var list = getJQAllData(); //此函数在有参照功能的单据中新增
	var subQty = 0;
	var sunTaxAmt = 0;
	var scntnTaxAmt = 0;
	var prcTaxSum = 0;
	for(var i = 0; i < list.length; i++) {
		subQty += parseFloat(list[i].subQty);
		sunTaxAmt += parseFloat(list[i].sunTaxAmt);
		scntnTaxAmt += parseFloat(list[i].scntnTaxAmt);
		prcTaxSum += parseFloat(list[i].prcTaxSum);
	};
	if(isNaN(subQty)) {
		subQty = 0
	}
	if(isNaN(sunTaxAmt)) {
		sunTaxAmt = 0
	}
	if(isNaN(scntnTaxAmt)) {
		scntnTaxAmt = 0
	}
	if(isNaN(prcTaxSum)) {
		prcTaxSum = 0
	}
	subQty = subQty.toFixed(prec)
	sunTaxAmt = precision(sunTaxAmt,2)
	scntnTaxAmt = precision(scntnTaxAmt,2)
	prcTaxSum = precision(prcTaxSum,2)
	$("#zi_jqgrids").footerData('set', {
		"subQty": subQty,
		"sunTaxAmt": sunTaxAmt,
		"scntnTaxAmt": scntnTaxAmt,
		"prcTaxSum": prcTaxSum,
	});
}

function GetWhsInfo1(rowid, val) {
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			'whsEncd': val,
		}
	};
	var postData = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/whs/whs_doc/selectWhsDocList',
		type: 'post',
		data: postData,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		error: function() {
			console.log(error);
		}, //错误执行方法
		success: function(data) {
			var list = data.respBody.list[0];
			//设置页面数据展示
			$("#mu_jqgrids").setRowData(rowid, {
				whsNm: list.whsNm,
			});
		}
	})
}
function GetWhsInfo2(rowid, val) {
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			'whsEncd': val,
		}
	};
	var postData = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/whs/whs_doc/selectWhsDocList',
		type: 'post',
		data: postData,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		error: function() {
			console.log(error);
		}, //错误执行方法
		success: function(data) {
			var list = data.respBody.list[0];
			//设置页面数据展示
			$("#zi_jqgrids").setRowData(rowid, {
				whsNm: list.whsNm,
			});
		}
	})
}
//设置日期相关
function setProductDate(rowid, sinvldtnDt) {
	var rowDatas = $("#" + grid).jqGrid('getRowData', rowid);
	//计算生产日期
	var prdcDt = rowDatas.sprdcDt //生产日期
	//保质期
	var baoZhiQi = rowDatas.baoZhiQi
	console.log(baoZhiQi)
	var sinvldtnDt = getNewDay(prdcDt, baoZhiQi);
	if(sinvldtnDt == "NaN-NaN-NaN") {
		sinvldtnDt = "";
	} else {
		sinvldtnDt = sinvldtnDt
	};
	console.log(sinvldtnDt)
	$("#" + grid).setRowData(rowid, {
		sinvldtnDt: sinvldtnDt,
		//		baoZhiQiDt: baoZhiQi
	});
}

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

//数值转换
function toDecimal(x) {
	var f = parseFloat(x);
	var s = f.toFixed(jingdu)
	return s;
}

//根据存货编码查询存货详细信息
function whs_GetGoodsInfo(rowid, goods) {
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
			$("#" + grid).setRowData(rowid, {
				invtyNm: list.invtyNm,
				spcModel: list.spcModel,
				bxRule: list.bxRule,
				measrCorpNm: list.measrCorpNm,
//				sunTaxUprc: list.refCost,
				taxRate: list.iptaxRate,
				measrCorpId: list.measrCorpId,
				baoZhiQi: list.baoZhiQiDt,
				prcTaxSum: list.prcTaxSum,
			});
		}
	})
}

// 点击保存，传送数据给后台
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
		$("#mu_jqgrids").setColProp("whsEncd", {
			editable: true
		});
		$("#mu_jqgrids").setColProp("mbatNum", {
			editable: true
		});
		$("#mu_jqgrids").setColProp("momQty", {
			editable: true
		})
		$("#mu_jqgrids").setColProp("bxQty", {
			editable: true
		});
		$("#mu_jqgrids").setColProp("mprdcDt", {
			editable: true
		});
		$("#zi_jqgrids").setColProp("whsEncd", {
			editable: true
		});
		$("#zi_jqgrids").setColProp("subKitEncd", {
			editable: true
		});
		$("#zi_jqgrids").setColProp("sbatNum", {
			editable: true
		});
		$("#zi_jqgrids").setColProp("sprdcDt", {
			editable: true
		});
		$("#zi_jqgrids").setColProp("taxRate", {
			editable: true
		});
		$("input[name='momKitEncd']").removeAttr("readonly")
		$("input[name='memo']").removeAttr("readonly")
		$("input[name='feeComnt']").removeAttr("readonly")
		$("input[name='fee']").removeAttr("readonly")
		mType = 2;
//		$("#mengban").hide();
		$('button').addClass("gray")
		$('.saveOrder').removeClass("gray");
		$('.upOrder').removeClass("gray");
		$('.editOrder').addClass("gray"); //点击修改后 修改不能用

		$(".addWhs").removeClass("gray")
		$(".cancel").removeClass("gray")
		$(".find").removeClass("gray")
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true);

		$("#gbox_jqGrids").hide();
		$("#gbox_jqgrids").show();
	});
})

//拿到grid对象
function getJQAllData() {
	var obj = $("#zi_jqgrids");
	//获取grid表中所有的rowid值
	var rowIds = obj.getDataIDs();
	//初始化一个数组arrayData容器，用来存放rowData
	var arrayData = new Array();
	if(rowIds.length > 0) {
		for(var i = 0; i < rowIds.length; i++) {
			if(obj.getRowData(rowIds[i]).subKitEncd == "") {
				continue;
			} else {
				arrayData.push(obj.getRowData(rowIds[i]));
			}
		}
	}
	return arrayData;
}

function IsCheckValue(momKitEncd,momQty, listData) {
	var rowDatas = $("#mu_jqgrids").jqGrid('getRowData', 1);
	var mbatNum = rowDatas.mbatNum;
	var whsEncd = rowDatas.whsEncd;
	console.log(listData)
	if(momKitEncd == '') {
		alert("母配套件编码不能为空")
		return false;
	} else if(whsEncd == '') {
		alert("母件仓库不能为空")
		return false;
	} else if(mbatNum == '') {
		alert("母件批次不能为空")
		return false;
	} else if(momQty == '') {
		alert("母件数量不能为空")
		return false;
	} else if(momQty < 0) {
		alert("母件数量不能小于0")
		return false;
	}else if(listData.length == 0) {
		alert("子件填写有误！")
		return false;
	} else if(listData.length != 0) {
		for(var i = 0; i < listData.length; i++) {
			if(listData[i].whsEncd == "") {
				alert("子件仓库不能为空")
				return false;
			} else if(listData[i].sbatNum == "") {
				alert("子件批次不能为空")
				return false;
			} else if((listData[i].whsEncd.indexOf("<input") != -1) ||
				(listData[i].subKitEncd.indexOf("<input") != -1) ||
				(listData[i].sbatNum.indexOf("<input") != -1) ||
				(listData[i].sprdcDt.indexOf("<input") != -1) ||
				(listData[i].taxRate.indexOf("<input") != -1)) {
				alert("子配套件中存在未保存项，请检查回车保存后操作")
				return false;
			}
		}
	}
	return true;
}
//拿到grid对象
function getJQAllData_mu() {
	var obj = $("#mu_jqgrids");
	//获取grid表中所有的rowid值
	var rowIds = obj.getDataIDs();
	//初始化一个数组arrayData容器，用来存放rowData
	var arrayData = new Array();
	if(rowIds.length > 0) {
		for(var i = 0; i < rowIds.length; i++) {
			if(obj.getRowData(rowIds[i]).subKitEncd == "") {
				continue;
			} else {
				arrayData.push(obj.getRowData(rowIds[i]));
			}
		}
	}
	return arrayData;
}
function IsCheckValue_mu(listData_mu) {
	if(listData_mu.length != 0) {
		for(var i = 0; i < listData_mu.length; i++) {
			if((listData_mu[i].whsEncd.indexOf("<input") != -1) ||
				(listData_mu[i].mbatNum.indexOf("<input") != -1) ||
				(listData_mu[i].momQty.indexOf("<input") != -1) ||
				(listData_mu[i].bxQty.indexOf("<input") != -1) ||
				(listData_mu[i].mprdcDt.indexOf("<input") != -1)) {
				alert("母配套件中存在未保存项，请检查回车保存后操作")
				return false;
			}
		}
	}
	return true;
}
var formNum;
//保存修改后的数据
function SaveModifyData() {
	var listData = getJQAllData();
	var listData_mu = getJQAllData_mu();
	formNum = $('#formNm').val();
	var formDt = $("input[name='formDt']").val();
	var formTyp = $("input[name='formTyp']").val();
	var fee = $("input[name='fee']").val();
	var feeComnt = $("input[name='feeComnt']").val();
	var memo = $("input[name='memo']").val();
	var rowDatas = $("#mu_jqgrids").jqGrid('getRowData', 1);
	var momKitEncd = rowDatas.momKitEncd;
	var momQty = rowDatas.momQty;
	//判断页面是否有值为空
	if(IsCheckValue(momKitEncd, momQty, listData) == true) {
		if(IsCheckValue_mu(listData_mu) == true) {
			var savedata = {
				"reqHead": reqhead,
			};
			var save = {
				"reqBody": {
					'formNum': formNum,
					'formDt': formDt,
					'formTyp': formTyp,
					'fee': fee,
					'feeComnt': feeComnt,
					'typ': '套件',
					'memo': memo,
					'list': listData
				}
			}
			var save1 = save.reqBody;
			var save1 = $.extend(true, save1, rowDatas); //深拷贝
			var o1 = $.extend(true, save, savedata);
			var saveData = JSON.stringify(o1);
	
			$.ajax({
				url: url + '/mis/whs/amb_disamb_sngl/updateASngl',
				type: 'post',
				data: saveData,
				dataType: 'json',
				async: true,
				contentType: 'application/json;charset=utf-8',
				success: function(data) {
					alert(data.respHead.message)
					if(data.respHead.isSuccess == true) {
						$("#mu_jqgrids").setColProp("whsEncd", {
							editable: false
						});
						$("#mu_jqgrids").setColProp("mbatNum", {
							editable: false
						});
						$("#mu_jqgrids").setColProp("momQty", {
							editable: false
						})
						$("#mu_jqgrids").setColProp("bxQty", {
							editable: false
						});
						$("#mu_jqgrids").setColProp("mprdcDt", {
							editable: false
						});
						$("#zi_jqgrids").setColProp("whsEncd", {
							editable: false
						});
						$("#zi_jqgrids").setColProp("subKitEncd", {
							editable: false
						});
						$("#zi_jqgrids").setColProp("sbatNum", {
							editable: false
						});
						$("#zi_jqgrids").setColProp("sprdcDt", {
							editable: false
						});
						$("#zi_jqgrids").setColProp("taxRate", {
							editable: false
						});
						$("input[name='momKitEncd']").attr("readonly","readonly")
						$("input[name='memo']").attr("readonly","readonly")
						$("input[name='feeComnt']").attr("readonly","readonly")
						$("input[name='fee']").attr("readonly","readonly")
						$(".addOrder").addClass("gray");
						$(".editOrder").removeClass("gray");
						$(".delOrder").removeClass("gray");
						$(".toExamine").removeClass("gray");
						$(".search").removeClass("gray");
						$(".saveOrder").addClass("gray");
						$('button').attr('disabled', false);
						$(".gray").attr('disabled', true)
	//					$("#mengban").show();
					}
				},
				error: function() {
					console.log(error);
				} //错误执行方法
			})
		}
	}
}

//保存刚开始的数据
function SaveNewData() {
	var listData = getJQAllData();
	var listData_mu = getJQAllData_mu();
	var formDt = $("input[name='formDt']").val();
	var formTyp = $("input[name='formTyp']").val();
	var fee = $("input[name='fee']").val();
	var feeComnt = $("input[name='feeComnt']").val();
	var memo = $("input[name='memo']").val();
	var rowDatas = $("#mu_jqgrids").jqGrid('getRowData', 1);
	var momKitEncd = rowDatas.momKitEncd;
	var mbatNum = rowDatas.mbatNum;
	var momQty = rowDatas.momQty;
	//判断页面是否有值为空
	if(IsCheckValue(momKitEncd, momQty, listData) == true) {
		if(IsCheckValue_mu(listData_mu) == true) {
			var savedata = {
				"reqHead": reqhead,
			}
			var save = {
				"reqBody": {
					'formNum': '',
					'formDt': formDt,
					'formTyp': formTyp,
					'fee': fee,
					'feeComnt': feeComnt,
					'memo': memo,
					'typ': '套件',
					'list': listData
				}
			}
			var save1 = save.reqBody;
			var save1 = $.extend(true, save1, rowDatas); //深拷贝
			var o1 = $.extend(true, save, savedata);
			var saveData = JSON.stringify(o1);
			console.log(saveData)
			$.ajax({
				url: url + '/mis/whs/amb_disamb_sngl/insertASngl',
				type: 'post',
				data: saveData,
				dataType: 'json',
				async: true,
				contentType: 'application/json;charset=utf-8',
				success: function(data) {
					alert(data.respHead.message)
					formNum = data.respBody.formNum;
					$("#formNm").val(formNum); //订单编码
	
					if(data.respHead.isSuccess == true) {
						$("#mu_jqgrids").setColProp("whsEncd", {
							editable: false
						});
						$("#mu_jqgrids").setColProp("mbatNum", {
							editable: false
						});
						$("#mu_jqgrids").setColProp("momQty", {
							editable: false
						})
						$("#mu_jqgrids").setColProp("bxQty", {
							editable: false
						});
						$("#mu_jqgrids").setColProp("mprdcDt", {
							editable: false
						});
						$("#zi_jqgrids").setColProp("whsEncd", {
							editable: false
						});
						$("#zi_jqgrids").setColProp("subKitEncd", {
							editable: false
						});
						$("#zi_jqgrids").setColProp("sbatNum", {
							editable: false
						});
						$("#zi_jqgrids").setColProp("sprdcDt", {
							editable: false
						});
						$("#zi_jqgrids").setColProp("taxRate", {
							editable: false
						});
						$("input[name='momKitEncd']").attr("readonly","readonly")
						$("input[name='memo']").attr("readonly","readonly")
						$("input[name='feeComnt']").attr("readonly","readonly")
						$("input[name='fee']").attr("readonly","readonly")
						$(".addOrder").removeClass("gray");
						$(".editOrder").removeClass("gray");
						$(".delOrder").removeClass("gray");
						$(".toExamine").removeClass("gray");
						$(".search").removeClass("gray");
						$(".saveOrder").addClass("gray");
						$('button').attr('disabled', false);
						$(".gray").attr('disabled', true)
	//					$("#mengban").show();
					}
				},
				error: function() {
					console.log(error);
				} //错误执行方法
			})
		}
	}
}
//是否审核		
function ntChk(x, chk) {
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"list": [{
				"formNum": formNum,
				"isNtChk": x,
			}]
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + chk,
		type: 'post',
		data: Data,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
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
					$(".addOrder").removeClass("gray");
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
	// 点击删除按钮，执行的操作
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
				url: url + '/mis/whs/amb_disamb_sngl/deleteAllAmbDisambSngl',
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
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
	var chk1 = "/mis/whs/amb_disamb_sngl/updateASnglChk";
	var chk2 = "/mis/whs/amb_disamb_sngl/updateASnglNoChk";
	//审核
	$(".toExamine").click(function() {
		if(isclick) {
			isclick = false;
			ntChk(1, chk1);
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	});
	//弃审
	$(".noTo").click(function() {
		if(isclick) {
			isclick = false;
			ntChk(0, chk2);
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
		url: url + '/mis/whs/amb_disamb_sngl/query',
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
			var list = data.respBody;
			if(list.isNtChk == 0) {
				$("button").removeClass("gray");
				$(".upOrder").addClass("gray");
				$(".noTo").addClass("gray");
				$(".saveOrder").addClass("gray");
				$(".addOrder").addClass("gray");
				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true)
			} else if(list.isNtChk == 1) {
				$("button").addClass("gray");
				$(".addOrder").addClass("gray");
				$(".noTo").removeClass("gray");
				$(".search").removeClass("gray");
				$(".refer").removeClass("gray");

				$(".addWhs").removeClass("gray") //确定
				$(".cancel").removeClass("gray") //取消
				$(".find").removeClass("gray") //查询

				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true)
			}
			$("#formNm").val(list.formNum);
			$("input[name='formDt']").val(list.formDt);
			$("input[name='momKitNm']").val(list.invtyNm);
			$("input[name='momKitEncd']").val(list.momKitEncd);
			$("select").val(list.formTyp);
			$("input[name='fee']").val(list.fee);
			$("input[name='feeComnt']").val(list.feeComnt);
			$("input[name='memo']").val(list.memo);
			$("#mu_jqgrids").setRowData(1, {
				momKitEncd: list.momKitEncd,
				whsEncd: list.whsEncd,
				whsNm: list.whsNm,
				invtyNm: list.invtyNm,
				spcModel: list.spcModel,
				bxQty: list.bxQty,
				bxRule: list.bxRule,
				measrCorpNm: list.measrCorpNm,
				munTaxUprc: list.munTaxUprc,
				taxRate: list.taxRate,
				measrCorpId: list.measrCorpId,
				baoZhiQi: list.baoZhiQi,
				prcTaxSum: list.prcTaxSum,
				invldtnDt: list.invldtnDt,
				mbatNum: list.mbatNum,
				momQty: list.momQty,
				mprdcDt: list.mprdcDt,
				munTaxAmt: list.munTaxAmt, //无税金额
				mcntnTaxAmt: list.mcntnTaxAmt, //含税金额
				mcntnTaxUprc: list.mcntnTaxUprc, //含税单价
			})

			var ambSnglubList = data.respBody.list;
			$("#zi_jqgrids").jqGrid('clearGridData');
			$("#zi_jqgrids").jqGrid('setGridParam', {
				datatype: 'local',
				data: ambSnglubList, //newData是符合格式要求的重新加载的数据
			}).trigger("reloadGrid")
			sumAmt();
		}
	})

}