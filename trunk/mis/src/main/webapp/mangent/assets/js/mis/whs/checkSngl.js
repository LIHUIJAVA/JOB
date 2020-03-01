var mType = 0;
$(function() {
	$('button').addClass("gray");
	$('.addOrder').removeClass("gray");
	$(".addWhs").removeClass("gray");
	$(".cancel").removeClass("gray");
	$(".find").removeClass("gray");
	$(".saveOrder").addClass("gray");
	$('button').attr('disabled', false);
	$(".gray").attr('disabled', true);

	var afterUrl = window.location.search.substring(1);
	var b = [];
	b = afterUrl;
	if(b == 1) {
		var isNtChk = localStorage.isNtChk;
		if(isNtChk == "是") {
			$(".noTo").removeClass("gray");
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
//批次
$(document).on('click', '.biao_batNum1', function() {
	var invtyEncd = $("input[name='invtyEncd']").val()
	var whsEncd = $("input[name='whsEncd']").val()
	localStorage.setItem("invtyEncd",invtyEncd)
	localStorage.setItem("whsEncd",whsEncd)
	window.open("../../Components/baseDoc/batNum.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
})
// 点击增加按钮，执行的操作
$(function() {
	$('.addOrder').click(function() {
		localStorage.removeItem('whsEncd');
		localStorage.removeItem('invtyClsEncd');
		localStorage.removeItem('invtyEncd');
		$("button").addClass("gray")
		$(".chackWhs").removeClass("gray");
		$(".upOrder").removeClass("gray"); //新增后放弃能用
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
		mType = 1;
		$("#jqgrids").setColProp("checkQty", {
			editable: true
		});
		$("#jqgrids").setColProp("adjIntoWhsQty", {
			editable: true
		});
		$("#jqgrids").setColProp("adjOutWhsQty", {
			editable: true
		})
		$("#jqgrids").setColProp("reasn", {
			editable: true
		});
		$("#jqgrids").jqGrid('setGridParam', {}).trigger("reloadGrid")
//		$("#mengban").hide();
		$("#gbox_jqGrids").hide();
		$("#gbox_jqgrids").show();

		
		$("#jqgrids").jqGrid('clearGridData');
	    $("#jqgrids").jqGrid('setGridParam', {
	       url: '../../assets/js/json/order.json',
	       datatype: "json",
	    }).trigger("reloadGrid")
	    $("input").val("");
		$("input[name=checkDt]").val(BillDate());
		$("input[name=bookDt]").val(BillDate());
	});
})

$(function() {
	//点击表格图标显示仓库列表
	$(document).on('click', '.whsNm', function() {
		window.open("../../Components/baseDoc/whsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	//点击表格图标显示存货列表
	$(document).on('click', '.invtyClsEncd', function() {
		window.open("../../Components/baseDoc/invtyTree.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	//点击表格图标显示存货列表
	$(document).on('click', '.invtyEncd_biaoge', function() {
		window.open("../../Components/baseDoc/invtyList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
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
		colNames: ['存货编码', '存货名称', '货位编码', '货位名称', '规格型号', '计量单位', '条形码', '批次', '生产日期', '保质期', '失效日期',
			'账面数量', '盘点数量*', '调整入库数量*', '调整出库数量*', '盈亏数量', '账面调节数量', '盈亏比例(%)', '原因'
		], //jqGrid的列显示名字
		colModel: [{
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
				name: 'barCd',
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
				name: 'bookQty', //账面数量
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'checkQty', //盘点数量
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'adjIntoWhsQty', //调整入库数量
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'adjOutWhsQty', //调整出库数量
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'prftLossQty', //盈亏数量
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'bookAdjustQty', //账面调节数量
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'prftLossRatio', //盈亏比例
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'reasn',
				editable: false,
				align: 'center',
				sortable: false
			},
		],
		loadonce: false,
		rownumbers: true,
		autowidth: true,
		height: height,
		autoScroll: true,
		shrinkToFit: false,
		caption: '盘点单',
		rowNum: 999999999,
		altclass: true,
		pgbuttons: false,
		pginput: false,
		//		viewrecords: true,
		pager: '#jqgridPager',
		multiselect: true, //复选框 
		forceFit: true,
		sortorder: "desc",
		cellEdit: true,
		footerrow: true,
		cellsubmit: "clientArray",
		gridComplete: function() {
			$("#jqgrids").footerData('set', {
				"invtyEncd": "本页合计",
			});
		},
		//在编辑状态时
		afterEditCell: function(rowid, cellname, val, iRow, iCol) {
			$(".saveOrder").addClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
			if((cellname == "checkQty") ||
			   (cellname == "adjIntoWhsQty") ||
			   (cellname == "adjOutWhsQty") ){
				$("input[name='checkQty']").attr("onkeyup","if(this.value.length==1){this.value=this.value.replace(/[^0-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
				$("input[name='checkQty']").attr("onafterpaste","if(this.value.length==1){this.value=this.value.replace(/[^0-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
				$("input[name='adjIntoWhsQty']").attr("onkeyup","if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
				$("input[name='adjIntoWhsQty']").attr("onafterpaste","if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
				$("input[name='adjOutWhsQty']").attr("onkeyup","if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
				$("input[name='adjOutWhsQty']").attr("onafterpaste","if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
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
			if((cellname == "checkQty") || (cellname == "adjIntoWhsQty") || (cellname == "adjOutWhsQty")) {
				//设置变量
				SetNums2(rowid, cellname);
				sumQty();
			}
			if(cellname == "checkQty" || cellname == "adjIntoWhsQty" || cellname == "adjOutWhsQty") {
				if(val < 0) {
					alert("数量不能小于0")
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

//增行   保存
//$(function() {
function fn() {
//	$(".addOne").click(function() {
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
			"invtyEncd": "",
			"invtyNm": "",
			"gdsBitEncd": '',
			"gdsBitNm": '',
			"spcModel": '',
			"measrCorpNm": '',
			"barCd": '',
			"batNum": '',
			"prdcDt": '',
			"baoZhiQi": '',
			"invldtnDt": '',
			"bookQty": '',
			"checkQty": '',
			"adjIntoWhsQty": '',
			"adjOutWhsQty": '',
			"prftLossQty": '',
			"bookAdjustQty": '',
			"prftLossRatio": '',
			"reasn": '',
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
			if(obj.getRowData(rowIds[i]).invtyEncd == "") {
				arrayData.push(obj.getRowData(rowIds[i]));
			}
		}
	}
	return arrayData;
}
//点击盘库按钮后
$(function() {
	$(".chackWhs").click(function() {
		var whsEncd = $("input[name='whsEncd']").val()
		if(whsEncd == undefined||whsEncd=="") {
			alert("请输入仓库编码")
		} else {
			$(".down").show()
			$(".down").css("opacity",1)
			$(".true").removeClass("gray");
			$(".falses").removeClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
		}
	})
	$(".falses").click(function() {
		$(".down").hide()
		$(".down").css("opacity",0)
	})
	$(".true").click(function() {
		var myDate = {};
		var invtyEncd = $("input[name='invtyEncd']").val()
		var batNum = $("input[name='batNum']").val()
		var invtyClsEncd = $("input[name='invtyCls']").val()
		var qty = $("#qty").val();
		var whsEncd = $("input[name='whsEncd']").val()
		if(qty == null) {
			alert("请确定账面为零是否盘点")
		} else {
			$(".saveOrder").removeClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
			var savedata = {
				"reqHead": reqhead,
				"reqBody": {
					"whsEncd": whsEncd,
					"invtyClsEncd": invtyClsEncd,
					"invtyEncd": invtyEncd,
					"batNum": batNum,
					"Qty": qty,
					"pageNo": 1,
					"pageSize": 999999999
				}
			};
			var saveData = JSON.stringify(savedata);
			$.ajax({
				type: "post",
				contentType: 'application/json; charset=utf-8',
				url: url + '/mis/whs/check_sngl/selectCheckSnglList',
				async: true,
				data: saveData,
				dataType: 'json',
				success: function(data) {
					alert(data.respHead.message)
					$(".down").hide()
					$(".down").css("opacity",0)
					var list = data.respBody.list;
					myDate = list;
					$("#jqgrids").jqGrid('clearGridData');
					$("#jqgrids").jqGrid('setGridParam', {
						datatype: 'local',
						data: list, //newData是符合格式要求的重新加载的数据
					}).trigger("reloadGrid")
					var numList = getJQAllData1()
					if(numList.length == 0) {
						fn()
					}
				},
				error: function() {
					alert("盘库失败")
				}
			});
		}
	})
})

function sumQty() {
	var list = getJQAllData(); //此函数在有参照功能的单据中新增
	var bookQty = 0;
	var checkQty = 0;
	var adjIntoWhsQty = 0;
	var adjOutWhsQty = 0;
	var prftLossQty = 0;
	var bookAdjustQty = 0;
	for(var i = 0; i < list.length; i++) {
		bookQty += parseFloat(list[i].bookQty);
		checkQty += parseFloat(list[i].checkQty);
		adjIntoWhsQty += parseFloat(list[i].adjIntoWhsQty);
		adjOutWhsQty += parseFloat(list[i].adjOutWhsQty);
		prftLossQty += parseFloat(list[i].prftLossQty);
		bookAdjustQty += parseFloat(list[i].bookAdjustQty);
	};
	if(isNaN(bookQty)) {
		bookQty = 0
	}
	if(isNaN(checkQty)) {
		checkQty = 0
	}
	if(isNaN(adjIntoWhsQty)) {
		adjIntoWhsQty = 0
	}
	if(isNaN(adjOutWhsQty)) {
		adjOutWhsQty = 0
	}
	if(isNaN(prftLossQty)) {
		prftLossQty = 0
	}
	if(isNaN(bookAdjustQty)) {
		bookAdjustQty = 0
	}
	bookQty = bookQty.toFixed(prec)
	checkQty = checkQty.toFixed(prec)
	adjIntoWhsQty = adjIntoWhsQty.toFixed(prec)
	adjOutWhsQty = adjOutWhsQty.toFixed(prec)
	prftLossQty = prftLossQty.toFixed(prec)
	bookAdjustQty = bookAdjustQty.toFixed(prec)
	$("#jqgrids").footerData('set', {
		"bookQty": bookQty,
		"checkQty": checkQty,
		"adjIntoWhsQty": adjIntoWhsQty,
		"adjOutWhsQty": adjOutWhsQty,
		"prftLossQty": prftLossQty,
		"bookAdjustQty": bookAdjustQty,
	});
}
//数值转换
function toDecimal(x) {
	var f = parseFloat(x);
	var s = f.toFixed(jingdu)
	return s;
}

//设置数量和金额
function SetNums2(rowid, cellname) {
	var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
	var bookQty = parseFloat(rowDatas.bookQty); //账面数
	var checkQty = parseFloat(rowDatas.checkQty); //盘点数
	var adjIntoWhsQty = parseFloat(rowDatas.adjIntoWhsQty); //入库数
	var adjOutWhsQty = parseFloat(rowDatas.adjOutWhsQty); //出库数

	if(!isNaN(checkQty)) { //盘点数
		checkQty = checkQty;
	} else {
		checkQty = ""
	}

	if(!isNaN(adjIntoWhsQty)) { //入库数
		adjIntoWhsQty = adjIntoWhsQty;
	} else {
		adjIntoWhsQty = 0
	}

	if(!isNaN(adjOutWhsQty)) { //出库数
		adjOutWhsQty = adjOutWhsQty;
	} else {
		adjOutWhsQty = 0
	}

	var prftLossQty, bookAdjustQty, prftLossRatio;
	bookAdjustQty = toDecimal(bookQty + adjIntoWhsQty - adjOutWhsQty); //账面调节数量
	if(isNaN(bookAdjustQty)) {
		bookAdjustQty = ""
	}
	prftLossQty = toDecimal(checkQty - bookAdjustQty); //盈亏数量
	if(isNaN(prftLossQty)) {
		prftLossQty = ""
	}
	prftLossRatio = toDecimal(prftLossQty / bookAdjustQty * 100);
	if(isNaN(prftLossRatio)) {
		prftLossRatio = 0
	} else if(prftLossRatio == Infinity) {
		prftLossRatio = 0
	}
	$("#jqgrids").setRowData(rowid, {
		checkQty: checkQty,
		adjIntoWhsQty: adjIntoWhsQty,
		adjOutWhsQty: adjOutWhsQty,
		prftLossQty: prftLossQty,
		bookAdjustQty: bookAdjustQty,
		prftLossRatio: prftLossRatio,
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

var checkFormNum;
// 点击修改按钮，执行的操作
$(function() {
	$('.editOrder').click(function() {
		$("#jqgrids").setColProp("checkQty", {
			editable: true
		});
		$("#jqgrids").setColProp("adjIntoWhsQty", {
			editable: true
		});
		$("#jqgrids").setColProp("adjOutWhsQty", {
			editable: true
		})
		$("#jqgrids").setColProp("reasn", {
			editable: true
		});
		mType = 2;
//		$("#mengban").hide();
		$('button').addClass("gray")
		$('.saveOrder').removeClass("gray");
		$('.upOrder').removeClass("gray");
		$('.editOrder').addClass("gray"); //点击修改后 修改不能用
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
	});
})

//点完修改后执行的url
function SaveModifyData() {
	var listData = getJQAllData();

	checkFormNum = $("input[name='checkFormNum']").val();
	var checkDt = $("input[name='checkDt']").val();
	var whsEncd = $("input[name='whsEncd']").val();
	//	var whsEncd = localStorage.whsEncd
	var bookDt = $("input[name='bookDt']").val();
	var memo = $("input[name='memo']").val();

	if(IsCheckValue(checkDt, bookDt) == true) {

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
			url: url + '/mis/whs/check_sngl/updateCheckSngl',
			type: 'post',
			data: saveData,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			success: function(data) {
				alert(data.respHead.message)
				if(data.respHead.isSuccess == true) {
					$("#jqgrids").setColProp("checkQty", {
						editable: false
					});
					$("#jqgrids").setColProp("adjIntoWhsQty", {
						editable: false
					});
					$("#jqgrids").setColProp("adjOutWhsQty", {
						editable: false
					})
					$("#jqgrids").setColProp("reasn", {
						editable: false
					});
					$('button').removeClass("gray")
					$('.saveOrder').removeClass("gray");
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

//第一次保存
function SaveNewData() {

	var listData = getJQAllData();
	var checkDt = $("input[name='checkDt']").val();
	var whsEncd = $("input[name='whsEncd']").val();
	//	var whsEncd = localStorage.whsEncd
	var bookDt = $("input[name='bookDt']").val();
	var memo = $("input[name='memo']").val();

	//判断页面是否有值为空
	if(IsCheckValue(checkDt, bookDt) == true) {

//		$("#mengban").show();
		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				'checkDt': checkDt,
				'bookDt': bookDt,
				'whsEncd': whsEncd,
				'memo': memo,
				'list': listData
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			url: url + '/mis/whs/check_sngl/insertCheckSngl',
			type: 'post',
			data: saveData,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			success: function(data) {
				alert(data.respHead.message)
				checkFormNum = data.respBody.pursOrdrId;
				$("input[name='checkFormNum']").val(data.respBody.checkFormNum); //订单编码
				if(data.respHead.isSuccess == true) {
					$("#jqgrids").setColProp("checkQty", {
						editable: false
					});
					$("#jqgrids").setColProp("adjIntoWhsQty", {
						editable: false
					});
					$("#jqgrids").setColProp("adjOutWhsQty", {
						editable: false
					})
					$("#jqgrids").setColProp("reasn", {
						editable: false
					});
					$('button').removeClass("gray");
					$('.saveOrder').removeClass("gray");
					$('.upOrder').addClass("gray");
					$('.noTo').addClass("gray");
					$("input").attr("readonly", "readonly");
					$("img").css("pointer-events", "none")
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

var chk1 = "/mis/whs/check_sngl/updateCSnglChk";
var chk2 = "/mis/whs/check_sngl/updateCSnglNoChk";

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
					$(".addOrder").removeClass("gray");
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
				url: url + '/mis/whs/check_sngl/deleteAllCheckSngl',
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

function IsCheckValue(checkDt, bookDt) {
	if(checkDt == "") {
		alert("盘点日期不能为空")
	} else if(bookDt == "") {
		alert("账面日期不能为空")
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
//	$("#mengban").show();
	checkFormNum = localStorage.checkFormNum;
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"checkFormNum": checkFormNum,
			"pageNo": 1,
			"pageSize": 500
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/whs/check_sngl/query',
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
			if(list1.isNtChk == 0) {
				$(".delOrder").removeClass("gray");
				$(".editOrder").removeClass("gray");
				$(".addOrder").addClass("gray");
				$(".addOrder").attr('disabled', true)
				$(".noTo").addClass("gray");
				$(".toExamine").removeClass("gray");
				$(".toExamine").attr('disabled', false)
				$(".editOrder").attr('disabled', false)
				$(".delOrder").attr('disabled', false)
				$(".noTo").attr('disabled', true)
				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true)
			} else if(list1.isNtChk == 1) {
				$(".editOrder").addClass("gray");
				$(".delOrder").addClass("gray");
				$(".editOrder").attr('disabled', true)
				$(".addOrder").addClass("gray");
				$(".addOrder").attr('disabled', true)
				$(".toExamine").addClass("gray");
				$(".noTo").removeClass("gray");
				$(".noTo").attr('disabled', false)
				$(".toExamine").attr('disabled', true)
				$(".delOrder").attr('disabled', true)
				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true)
			}
			$("input[name='checkFormNum']").val(list1.checkFormNum);
			$("input[name='whsEncd']").val(list1.whsEncd);
			$("input[name='whsNm']").val(list1.whsNm);
			$("input[name='bookDt']").val(list1.bookDt);
			$("input[name='checkDt']").val(list1.checkDt);
			$("input[name='memo']").val(list1.memo);
			var list = data.respBody.list;
			$("#jqgrids").jqGrid('clearGridData');
			$("#jqgrids").jqGrid('setGridParam', {
				datatype: 'local',
				data: list, //newData是符合格式要求的重新加载的数据
			}).trigger("reloadGrid")
			sumQty();
		},
		error: function() {
			alert("error")
		}
	})
}