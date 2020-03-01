$(function() {
	$('button').addClass("gray");
	$('.addOrder').removeClass("gray")
	$(".addWhs").removeClass("gray")
	$(".cancel").removeClass("gray")
	$(".find").removeClass("gray")
	$('button').attr('disabled', false);
	$(".gray").attr('disabled', true);

	var afterUrl = window.location.search.substring(1);
	var b = [];
	b = afterUrl;
	if(b == 1) {
		var isNtChk = localStorage.isNtChk;
		if(isNtChk == '是') {
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


//点击表格图标显示仓库列表
$(function() {
	$(document).on('click', '.whsNm', function() {
		window.open("../../Components/baseDoc/whsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})

//初始化表格
$(function() {
	allHeight()
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	$("#jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json",
		colNames: ['编码','存货编码', '存货名称' , '货位编码', '货位名称', '规格型号', '计量单位', '条形码', '批次', '生产日期', '保质期', '失效日期',
			'批次','损益数量', '未税单价', '含税单价', '税率', '未税盈亏金额', '含税盈亏金额','账面税额','盘点税额','盈亏税额','损益税额'
		], //jqGrid的列显示名字
		colModel: [{
				name: 'ordrNum',
				editable: false,
				align: 'center',
				sortable: false,
				hidden:true
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
				name: 'gdsBitEncd',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'gdsBitNm',
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
				name: 'crspdBarCd',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'batNum',
				editable: false,
				align: 'center',
				hidden: true,
				sortable: false
			},
			{
				name: 'prdcDt',
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
				name: 'invldtnDt',
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
				name: 'prftLossQtys', //损益数量
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'unTaxUprc', //未税单价
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'cntnTaxUprc', //含税单价
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
				name: 'unTaxPrftLossAmt', //未税盈亏金额
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'cntnTaxPrftLossAmt', //含税盈亏金额
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'bookTaxAmt', 
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'checkTaxAmt',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'prftLossTaxAmt',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'prftLossTaxAmts',
				editable: false,
				align: 'center',
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
		caption: '盘点损益表',
		altclass: true,
		pgbuttons: false,
		pginput:false,
//		viewrecords: true,
		pager: '#jqgridPager',
		forceFit: true,
		sortorder: "desc",
		cellEdit: true,
		cellsubmit: "clientArray",
		//在编辑状态时
		afterEditCell: function(rowid, cellname, val, iRow, iCol) {
			$(".saveOrder").addClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
			if(cellname == "prftLossQtys"){
				$("input[name='prftLossQtys']").attr("onkeyup","if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
				$("input[name='prftLossQtys']").attr("onafterpaste","if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
			}
			if(cellname == "unTaxUprc"){
				$("input[name='unTaxUprc']").attr("onkeyup","if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
				$("input[name='unTaxUprc']").attr("onafterpaste","if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
			}
			if(cellname == "cntnTaxUprc"){
				$("input[name='cntnTaxUprc']").attr("onkeyup","if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
				$("input[name='cntnTaxUprc']").attr("onafterpaste","if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
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
			//设置数量
			if((cellname == "prftLossQtys") ||
				(cellname == "unTaxUprc") ||
				(cellname == "cntnTaxUprc") ||
				(cellname == "taxRate") ||
				(cellname == "unTaxPrftLossAmt") ||
				(cellname == "cntnTaxPrftLossAmt")) {
				//设置变量
				SetNums(rowid, cellname);
			}
			if(cellname == "prftLossQtys"){
				if(val < 0) {
					alert("数量不能小于0")
				}
			}
			if(cellname == "unTaxUprc"){
				if(val < 0) {
					alert("未税单价不能小于0")
				}
			}
			if(cellname == "cntnTaxUprc"){
				if(val < 0) {
					alert("含税单价不能小于0")
				}
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
			var grid = "jqgrids"
			//删除一行操作
			removeRows(grid);
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

//数值转换
function toDecimal(x) {
	var f = parseFloat(x);
	var s = f.toFixed(jingdu)
	return s;
}

//设置数量和金额
function SetNums(rowid, cellname) {
	var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
	var prftLossQtys = parseFloat(rowDatas.prftLossQtys); //损益数量
	var cntnTaxUprc = parseFloat(rowDatas.cntnTaxUprc); //含税单价
	var unTaxUprc = parseFloat(rowDatas.unTaxUprc); //未税单价
	var taxRate = parseFloat(rowDatas.taxRate) * 0.01; //税率

	if(!isNaN(prftLossQtys)) { //损益数量
		prftLossQtys = prftLossQtys;
	} else {
		prftLossQtys = ""
	}

	var unTaxPrftLossAmt = toDecimal(unTaxUprc * prftLossQtys); //未税盈亏金额
	cntnTaxUprc = toDecimal(unTaxUprc * (taxRate + 1)); //含税单价
	var cntnTaxPrftLossAmt = toDecimal(unTaxPrftLossAmt * (taxRate + 1)); //含税盈亏金额

	if(!isNaN(unTaxUprc)) { //未税单价
		unTaxUprc = unTaxUprc;
	} else {
		unTaxUprc = ""
	}
	if(!isNaN(unTaxPrftLossAmt)) { //损益数量
		unTaxPrftLossAmt = unTaxPrftLossAmt;
	} else {
		unTaxPrftLossAmt = ""
	}
	if(!isNaN(cntnTaxUprc)) { //含税单价
		cntnTaxUprc = cntnTaxUprc;
	} else {
		cntnTaxUprc = ""
	}
	if(!isNaN(cntnTaxPrftLossAmt)) { //含税盈亏金额
		cntnTaxPrftLossAmt = cntnTaxPrftLossAmt;
	} else {
		cntnTaxPrftLossAmt = ""
	}

	$("#jqgrids").setRowData(rowid, {
		prftLossQtys: prftLossQtys,
		unTaxUprc: unTaxUprc,
		unTaxPrftLossAmt: unTaxPrftLossAmt,
		cntnTaxUprc: cntnTaxUprc,
		cntnTaxPrftLossAmt: cntnTaxPrftLossAmt,
	});
}

// 点击保存，传送数据给后台
$(function() {
	$(".saveOrder").click(function() {
		SaveModifyData();
	})
})
var isclick = true;
$(function() {
	$(".saveOrder").click(function() {
		if(isclick) {
			isclick = false;
			SaveModifyData();
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	})
})
// 点击修改按钮，执行的操作
$(function() {
	$('.editOrder').click(function() {
//		$("#mengban").hide();
		$('button').addClass("gray")
		$('.saveOrder').removeClass("gray");
		$('.upOrder').removeClass("gray");
		$('.editOrder').addClass("gray"); //点击修改后 修改不能用
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
		$("#jqgrids").setColProp("prftLossQtys", {
			editable: true
		});
		$("#jqgrids").setColProp("unTaxUprc", {
			editable: true
		});
		$("#jqgrids").setColProp("cntnTaxUprc", {
			editable: true
		})
		$("#jqgrids").setColProp("taxRate", {
			editable: true
		});
	});
})
var checkFormNum;
//点完修改后执行的url
function SaveModifyData() {
	var listData = getJQAllData();

	checkFormNum = $("input[name='checkFormNum']").val();
	var checkDt = $("input[name='checkDt']").val();
	var whsEncd = localStorage.whsEncd
	var bookDt = $("input[name='bookDt']").val();
	var memo = $("input[name='memo']").val();

	if(IsCheckValue(checkDt, bookDt, listData) == true) {
		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				'checkFormNum': checkFormNum,
				'checkDt': checkDt,
				'bookDt': bookDt,
				'whsEncd': whsEncd,
				'memo': memo,
				'list': listData
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			url: url + '/mis/whs/check_sngl_loss/updateCheckSnglLoss',
			type: 'post',
			data: saveData,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			success: function(data) {
				alert(data.respHead.message)
				if(data.respHead.isSuccess == true) {
					$("#jqgrids").setColProp("prftLossQtys", {
						editable: false
					});
					$("#jqgrids").setColProp("unTaxUprc", {
						editable: false
					});
					$("#jqgrids").setColProp("cntnTaxUprc", {
						editable: false
					})
					$("#jqgrids").setColProp("taxRate", {
						editable: false
					});
					$('button').removeClass("gray")
					$('.saveOrder').addClass("gray");
					$('.upOrder').addClass("gray");
					$('.noTo').addClass("gray");
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

$(function() {
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

var chk1 = "/mis/whs/check_sngl_loss/updateCSnglLossChk";
var chk2 = "/mis/whs/check_sngl_loss/updateCSnglLossNoChk";

//是否审核		
function ntChk(x, chk) {
	checkFormNum = $("input[name='checkFormNum']").val()
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"list": [{
				"formNum": checkFormNum,
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
			alert(data.respHead.message);
			if(data.respHead.isSuccess == true) {
				if(x == 0) {
					$("button").removeClass("gray");
					$(".upOrder").addClass("gray");
					$(".noTo").addClass("gray");
					$(".saveOrder").addClass("gray");
					$('button').attr('disabled', false);
					$(".gray").attr('disabled', true)
				} else if(x == 1) {
					$("button").addClass("gray");
					$(".addOrder").removeClass("gray");
					$(".noTo").removeClass("gray");
					$(".search").removeClass("gray");
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
		checkFormNum = $("input[name='checkFormNum']").val()
		var deleteAjax = {
			"reqHead": reqhead,
			"reqBody": {
				"checkFormNum": checkFormNum
			}
		}
		var deleteData = JSON.stringify(deleteAjax);
		if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + '/mis/whs/check_sngl_loss/deleteAllCheckSnglLoss',
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
})

function IsCheckValue(checkDt, bookDt, listData) {
	if(checkDt == "") {
		alert("损益日期不能为空")
		return false;
	} else if(bookDt == "") {
		alert("账面日期不能为空")
		return false;
	} else if(listData.length == 0) {
		alert("未添加子表！")
		return false;
	} else if(listData.length != 0) {
		for(var i = 0; i < listData.length; i++) {
			if(listData[i].checkQty == "") {
				alert("损益数量不能为空")
				return false;
			} else if(listData[i].adjIntoWhsQty == "") {
				alert("调整入库数量不能为空")
				return false;
			} else if(listData[i].adjOutWhsQty == "") {
				alert("调整出库数量不能为空")
				return false;
			}
		}
	}
	return true;
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
	checkFormNum = localStorage.checkFormNum;
	var isNtChk = localStorage.isNtChk;
	if(isNtChk == 0) {
		$(".editOrder").removeClass("gray");
		$(".toExamine").removeClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
	} else if(isNtChk == 1) {
		$(".noTo").removeClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
	}
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"checkFormNum": checkFormNum,
			"pageNo": 1,
			"pageSize": 500
		}
	};
	var saveData = JSON.stringify(savedata);
	var myDate = {};
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/whs/check_sngl_loss/query',
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
			var list1 = data.respBody;
			$("input[name='checkFormNum']").val(list1.checkFormNum);
			$("input[name='whsNm']").val(list1.whsNm);
			$("input[name='bookDt']").val(list1.bookDt);
			$("input[name='whsEncd']").val(list1.whsEncd);
			$("input[name='checkDt']").val(list1.checkDt);
			$("#srcFormNum").val(list1.srcFormNum);
			$("input[name='memo']").val(list1.memo);
			var list = data.respBody.list;
			$("#jqgrids").jqGrid('clearGridData');
			$("#jqgrids").jqGrid('setGridParam', {
				datatype: 'local',
				data: list, //newData是符合格式要求的重新加载的数据
			}).trigger("reloadGrid")
		},
		error: function() {
			alert("error")
		}
	})
}