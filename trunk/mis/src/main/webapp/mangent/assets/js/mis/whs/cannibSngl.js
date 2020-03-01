//调拨单
var mType = 0;
$(function() {
	getData();
	//1.仓库
	$(document).on('keypress', '#tranOutWhsEncd', function(event) {
		if(event.keyCode == '13') {
			$('#tranOutWhsEncd').blur();
		}
	})

	$(document).on('blur', '#tranOutWhsEncd', function() {
		var tranOutWhsEncd = $("#tranOutWhsEncd").val();
		if(tranOutWhsEncd != '') {
			dev({
				doc1: $("#tranOutWhsEncd"),
				doc2: $("#tranOutWhsNm"),
				showData: {
					"whsEncd": tranOutWhsEncd
				},
				afunction: function(data) {
					if(data.respHead.isSuccess == false) {
						alert("不存在此转出仓库请确认！")
						$("#tranOutWhsNm").val("");
						$("#tranOutWhsEncd").val("");
					} else if(data.respHead.isSuccess == true) {
						var tranOutWhsNm = data.respBody.list[0].whsNm;
						$("#tranOutWhsNm").val(tranOutWhsNm)
					}
				},
				url: url + "/mis/whs/whs_doc/selectWhsDocList"
			})
			
		}else if(tranOutWhsEncd == '') {
			$("#tranOutWhsNm").val('')
		}
	});
	$(document).on('keypress', '#tranInWhsEncd', function(event) {
		if(event.keyCode == '13') {
			$('#tranInWhsEncd').blur();
		}
	})
	$(document).on('blur', '#tranInWhsEncd', function() {
		var tranInWhsEncd = $("#tranInWhsEncd").val();
		if(tranInWhsEncd != '') {
			dev({
				doc1: $("#tranInWhsEncd"),
				doc2: $("#tranInWhsNm"),
				showData: {
					"whsEncd": tranInWhsEncd
				},
				afunction: function(data) {
					if(data.respHead.isSuccess == false) {
						alert("不存在此转入仓库请确认！")
						$("#tranInWhsNm").val("");
						$("#tranInWhsEncd").val("");
					} else if(data.respHead.isSuccess == true) {
						var tranInWhsNm = data.respBody.list[0].whsNm;
						$("#tranInWhsNm").val(tranInWhsNm)
					}
				},
				url: url + "/mis/whs/whs_doc/selectWhsDocList"
			})
			
		}else if(tranInWhsEncd == '') {
			$("#tranInWhsNm").val('')
		}
	});
})
$(function() {
	$('button').addClass("gray");
	$(".refer").removeClass("gray") //参照
	$(".addOne").removeClass("gray")
	$('.addOrder').removeClass("gray")
	$('.search4').removeClass("gray")
	$('.print1').removeClass("gray")
	$('.batNum_search').removeClass("gray")
	$('.batNum_search').attr('disabled', false);
	$('button').attr('disabled', false);
	$(".gray").attr('disabled', true);
//	$("#mengban").show();
	var afterUrl = window.location.search.substring(1);
	var b = [];
	b = afterUrl;
	if(b == 1) {
		var isNtChk = localStorage.isNtChk;
		if(isNtChk == "是") {
			$("button").addClass("gray")
			$(".noTo").removeClass("gray")
			$('.editOrder').addClass("gray");
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
		localStorage.removeItem('tranOutWhsEncd');
		localStorage.removeItem('tranInWhsEncd');
//		$("#mengban").hide();
		$('button').addClass("gray");
		$(".saveOrder").removeClass("gray");
		$(".refer").removeClass("gray");
		$(".ctrlc_v").removeClass("gray");
		$(".addOne").removeClass("gray")
		$(".upOrder").removeClass("gray"); //新增后放弃能用
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
		mType = 1;
		$("#jqgrids").setColProp("invtyEncd", {
			editable: true
		});
		$("#jqgrids").setColProp("batNum", {
			editable: true
		});
		$("#jqgrids").setColProp("cannibQty", {
			editable: true
		})
		$("#jqgrids").setColProp("prdcDt", {
			editable: true
		});
		$("#jqgrids").setColProp("taxRate", {
			editable: true
		});
		$("#jqgrids").setColProp("bxQty", {
			editable: true
		});
		$("#jqgrids").setColProp("unTaxUprc", {
			editable: true
		});
		$("#jqgrids").setColProp("projEncd", {
			editable: true
		});
		$("#jqgrids").jqGrid('clearGridData');
	    $("#jqgrids").jqGrid('setGridParam', {
	       url: '../../assets/js/json/order.json',
	       datatype: "json",
	    }).trigger("reloadGrid")
	    $("input").val("");
		$("input[name=cannibDt]").val(BillDate());

	});
})

//点击参照按钮，执行的操作
$(function() {
	$(".refer").click(function() {
		mType = 1;
//		$("#mengban").hide();
		$('button').addClass("gray");
		$('.addOrder').removeClass("gray") //增加
		$(".cann_sure").removeClass("gray") //确定
		$(".addOne").removeClass("gray")
		$(".cancel").removeClass("gray") //取消
		$(".cann_search").removeClass("gray") //查询

		$(".saveOrder").removeClass("gray");
		$(".upOrder").removeClass("gray");
		$('.refer').removeClass("gray")
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)

		$("#whsDocList").hide();
		$("#insertList").hide();
		$("#purchaseOrder").hide();
		$("#ProjClsList").hide();
		$("#batNum_list").hide();
		$("#purs_list").show();
		$("#purs_list").css("opacity", 1)

		$(".cann_sure").click(function() {
			$("#jqgrids").trigger("reloadGrid");
			var gr = $("#jqGrids_list").jqGrid('getGridParam', 'selrow');
			//获得行数据
			var rowDatas = $("#jqGrids_list").jqGrid('getRowData', gr);
			var formNum = rowDatas.formNum
			if(formNum == undefined) {
				alert("请选择单据")
			} else {
				$("#purs_list").css("opacity", 0);
				$("#purchaseOrder").show();
				searchFormNum(formNum)
			}
		})
		$(".refer_cancel").click(function() {
			window.location.reload();
		})
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
			"spcModel": '',
			"measrCorpNm": '',
			"cannibQty": '',
			"batNum": '',
			"prdcDt": '',
			"baoZhiQi": '',
			"invldtnDt": '',
			"cntnTaxUprc": '',
			"cntnTaxAmt": '',
			"unTaxUprc": '',
			"unTaxAmt": '',
			"bxRule": '',
			"taxRate": '',
			"bxQty": '',
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


function searchFormNum(formNum) {
	$("#jqgrids").trigger("reloadGrid");
	$("#gbox_jqgrids").show();
	$("#gbox_jqGrids").hide()
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"formNum": formNum,
			"pageNo": 1,
			"pageSize": 999999999
		}
	};
	var saveData = JSON.stringify(savedata);
	var myDate = {};
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/whs/cannib_sngl/query',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			var list = data.respBody.list;
			$("#purchaseOrder #formSave input").val("");
			$("#tranOutWhsEncd").val(data.respBody.tranOutWhsEncd);
			$("#tranOutWhsNm").val(data.respBody.tranOutWhsEncdName);
			$("#tranInWhsEncd").val(data.respBody.tranInWhsEncd);
			$("#tranInWhsNm").val(data.respBody.tranInWhsEncdName);

			var time = new Date().format("yyyy-MM-dd hh:mm:ss");
			$("input[name='cannibDt']").val(time);
			//到货日期
			$("input[name=toGdsSnglDt]").val(time);
			
			$("#jqgrids").jqGrid('clearGridData');
			$("#jqgrids").jqGrid('setGridParam', {
				datatype: 'local',
				data: list, //newData是符合格式要求的重新加载的数据
			}).trigger("reloadGrid")
			var numList = getJQAllData1()
			if(numList.length == 0) {
				fn()
			}
			sumAmt()
			$("#jqgrids").setColProp("invtyEncd", {
				editable: true
			});
			$("#jqgrids").setColProp("cannibQty", {
				editable: true
			});
			$("#jqgrids").setColProp("batNum", {
				editable: true
			});
			$("#jqgrids").setColProp("prdcDt", {
				editable: true
			});
			$("#jqgrids").setColProp("unTaxUprc", {
				editable: true
			});
			$("#jqgrids").setColProp("taxRate", {
				editable: true
			});
			$("#jqgrids").setColProp("bxQty", {
				editable: true
			});
			$("#jqgrids").setColProp("projEncd", {
				editable: true
			});
		}
	})

}

function sumAmt() {
	var list = getJQAllData(); //此函数在有参照功能的单据中新增
	var cannibQty = 0;
	var unTaxAmt = 0;
	var cntnTaxAmt = 0;
	var prcTaxSum = 0;
	for(var i = 0; i < list.length; i++) {
		cannibQty += parseFloat(list[i].cannibQty);
		unTaxAmt += parseFloat(list[i].unTaxAmt);
		cntnTaxAmt += parseFloat(list[i].cntnTaxAmt);
		prcTaxSum += parseFloat(list[i].prcTaxSum);
	};
	if(isNaN(cannibQty)) {
		cannibQty = 0
	}
	if(isNaN(unTaxAmt)) {
		unTaxAmt = 0
	}
	if(isNaN(cntnTaxAmt)) {
		cntnTaxAmt = 0
	}
	if(isNaN(prcTaxSum)) {
		prcTaxSum = 0
	}
	cannibQty = cannibQty.toFixed(prec)
	unTaxAmt = precision(unTaxAmt,2)
	cntnTaxAmt = precision(cntnTaxAmt,2)
	prcTaxSum = precision(prcTaxSum,2)
	$("#jqgrids").footerData('set', {
		"cannibQty": cannibQty,
		"unTaxAmt": unTaxAmt,
		"cntnTaxAmt": cntnTaxAmt,
		"prcTaxSum": prcTaxSum,
	});
}
localStorage.setItem("outInEncd", 0)

//点击转出仓库表格图标显示仓库列表
$(function() {
	$(document).on('click', '.tranOutWhsEncd1', function() {
		localStorage.setItem("outInEncd", 1)
		window.open("../../Components/baseDoc/whsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
});
//点击转入表格图标显示业务员列表
$(function() {
	$(document).on('click', '.tranInWhsEncd1', function() {
		localStorage.setItem("outInEncd", 2)
		window.open("../../Components/baseDoc/whsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})

var invtyEncd_cannib;

//打开存货档案后点击确定取消
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

	})//	取消
	$(".pro_cancel").click(function() {
		$("#whsDocList").css("opacity", 0);
		$("#insertList").css("opacity", 0);
		$("#invtyTree").css("opacity", 0);
		$("#purchaseOrder").show();
		//到货单
		//获得行号
//		var rowid = $("#jqgrids").jqGrid('getGridParam', 'selrow');

//		$("#" + rowid + "_projEncd").val("");
//		$("#jqgrids").setRowData(rowid, {
//			projNm: ""
//		});
	})
	//确定
	$(".addWhs").click(function() {
		//调拨单
		//获得行号
		var rowid = $("#jqgrids").jqGrid('getGridParam', 'selrow');

		//	存货档案
		//	获得行号
		var ids = $("#insert_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowData = $("#insert_jqgrids").jqGrid('getRowData', ids);

		$("#" + rowid + "_invtyEncd").val(rowData.invtyEncd)

		//		动态设置批次可编辑
		$("#jqgrids").setColProp("batNum", {
			editable: true
		});

		$("#insertList").css("opacity", 0);
		$("#purchaseOrder").show();
	})
	//	取消
	$(".cancel").click(function() {
		$("#insertList").css("opacity", 0);
		$("#purchaseOrder").show();
		//调拨单
		//获得行号
		var rowid = $("#jqgrids").jqGrid('getGridParam', 'selrow');

		$("#" + rowid + "_invtyEncd").val("")
	})

	//确定
	$(".sure").click(function() {
		//调拨单
		//获得行号
		var rowid = $("#jqgrids").jqGrid('getGridParam', 'selrow');

		//	存货档案
		//	获得行号
		var ids = $("#batNum_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDatas = $("#batNum_jqgrids").jqGrid('getRowData', ids);
		$("#" + rowid + "_batNum").val(rowDatas.batNum)

		var prdoc = rowDatas.prdcDt;
//		var baoZhiQi = rowDatas.baoZhiQi;
		var invldtnDt = rowDatas.invldtnDt;
		var unTaxUprc = rowDatas.unTaxUprc;
		var pr = prdoc.split(" ")
		var p;
		for(var i = 0; i < pr.length; i++) {
			p = pr[0].toString()
		}
		$("#jqgrids").setRowData(rowid, {
			prdcDt: p,
			unTaxUprc: unTaxUprc
		})

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
	allHeight();
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	$("#jqgrids").jqGrid({
		//引入空白表格
		url: '../../assets/js/json/order.json',
		datatype: "json",
		colNames: ['生产日期','存货编码*', '存货名称', '规格型号', '主计量单位', '批次*', '调拨数量*', '生产日期', '保质期', '失效日期', '含税单价', '含税金额',
			'无税单价', '无税金额', '项目编码', '项目名称','箱规', '税率*','税额', '箱数'
		], //jqGrid的列显示名字
		colModel: [{
				name: 'cannibDt1', //存货编码
				editable: true,
				align: 'center',
				sortable: false,
				hidden:true
			},
			{
				name: 'invtyEncd', //存货编码
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
				name: 'batNum', //批次
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'cannibQty', //数量
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'prdcDt', //生产日期
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
				name: 'cntnTaxUprc', //含税单价
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'cntnTaxAmt', //含税金额
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'unTaxUprc', //无税单价
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'unTaxAmt', //无税金额
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'projEncd', //项目编码
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'projNm', //项目名称
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'bxRule', //箱规
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
				name: 'taxAmt', //税额
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'bxQty', //箱数
				editable: false,
				align: 'center',
				sortable: false
			}
		],
		rowNum: 999999999,
		loadonce: false,
		rownumbers: true,
		autowidth: true,
		height: height,
		autoScroll: true,
		shrinkToFit: false,
		multiselect: true, //复选框 
		caption: '调拨单',
		altclass: true,
		pgbuttons: false,
		pginput: false,
		//		viewrecords: true,
		forceFit: true,
		sortorder: "desc",
		cellEdit: true, //单个表格是否可编辑·1
		cellsubmit: "clientArray",
		footerrow: true,
		gridComplete: function() {
			$("#jqgrids").footerData('set', {
				"invtyEncd": "本页合计",
			});
		},
		pager: "#ca_jqgridPager",
		//单个表格在编辑的情况下
		afterEditCell: function(rowid, cellname, val, iRow, iCol) {
			//保存按钮不能用
			$(".saveOrder").addClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
			$("#" + rowid + "_batNum").attr("readonly", "readonly");
			if(cellname == "invtyEncd") {
				$("#" + rowid + "_" + cellname).bind('keyup', function() {
					findGrid(rowid, cellname, val);
				})
				//双击存货编码时
				$("#" + rowid + "_" + cellname).bind("dblclick", function() {
					$(".zhezhao").hide()
					$(".find").removeClass("gray");
					$(".addWhs").removeClass("gray");
					$(".cancel").removeClass("gray");
					$('button').attr('disabled', false);
					$(".gray").attr('disabled', true)

					//存货列表显示
					$("#insertList").css("opacity", 1);
					$("#insertList").show();
					//调拨单隐藏
					$("#purchaseOrder").hide();
					$("#ProjClsList").hide();
					$("#batNum_list").hide();
					$("#purs_list").hide();
				});
			}
			if(cellname == "projEncd") {
				$(".pro_sure").removeClass("gray");
				$(".pro_cancel").removeClass("gray");
				$(".search_pro").removeClass("gray");
				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true);
				$("#" + rowid + "_projEncd").bind("dblclick", function() {
					$('#tree1>ul>li>div span').parent().next().css("display", "block")
					$("#insertList").hide();
					$("#batNum_list").hide();
					$("#purchaseOrder").hide();
					$("#purs_list").hide();
					$("#ProjClsList").show();
					$("#ProjClsList").css("opacity", 1)
				});
			}
			if(cellname == "prdcDt") {
				$("input[name='prdcDt']").attr("calendar","YYYY-MM-DD")
			}
			if(cellname == "bxQty") {
				$("input[name='bxQty']").attr("onkeyup","if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
				$("input[name='bxQty']").attr("onafterpaste","if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
			}
			if(cellname == "unTaxUprc") {
				$("input[name='unTaxUprc']").attr("onkeyup","if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
				$("input[name='unTaxUprc']").attr("onafterpaste","if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
			}
			if(cellname == "cannibQty") {
				$("input[name='cannibQty']").attr("onkeyup","if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
				$("input[name='cannibQty']").attr("onafterpaste","if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
				var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
				if(rowDatas.batNum == "") {
					alert("未选择批次");
					$("#" + rowid + "_cannibQty").attr('disabled', 'disabled');
				}
			}
			if(cellname == "batNum") {
				var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
				console.log(rowDatas.invtyEncd)
				var invtyEncd = rowDatas.invtyEncd
				localStorage.setItem("invtyEncd",invtyEncd)
				if(rowDatas.invtyEncd == "") {
					alert("请先输入存货档案");
					$("#" + rowid + "_batNum").attr('disabled', 'disabled');
					return;
				} else {
					var tranOutWhsEncd = $("input[name='tranOutWhsEncd']").val()
					if(tranOutWhsEncd == '') {
						alert("请填写转出仓库！");
						$("#" + rowid + "_batNum").attr('disabled', 'disabled');
						return;
					}else {
						$("#" + rowid + "_" + cellname).bind("dblclick", function() {
							$(".zhezhao").hide()
							$(".sure").removeClass("gray");
							$(".cancel").removeClass("gray");
		//					$(".search").removeClass("gray");
							$('button').attr('disabled', false);
							$(".gray").attr('disabled', true)
							var whsEncd_b = $("input[name='tranOutWhsEncd']").val();
							localStorage.setItem("whsEncd",whsEncd_b)
							$('.batNum_search').removeClass("gray")
							$('.batNum_search').attr('disabled', false);
							//存货列表显示
							$("#insertList").css("opacity", 0);
							$("#insertList").hide();
							//调拨单隐藏
							$("#purchaseOrder").hide();
							$("#ProjClsList").hide();
							$("#batNum_list").css("opacity", 1);
							$("#batNum_list").css("display", "block");
							var whsEncd = $("#tranOutWhsEncd").val()
							searcherBatNum(whsEncd, invtyEncd)
						});
					}
				}
			}
		},
		//单个表格不编辑的情况下
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
			//获取商品信息
			if(cellname == "invtyEncd") {
				GetGoodsInfo(rowid, val);
				//获得出库编码
				var tranOutWhsEncd = localStorage.tranOutWhsEncd;
				//获得行号
				var ids = $("#jqgrids").jqGrid('getGridParam', 'selrow');
				//获得行数据
				var rowData = $("#jqgrids").jqGrid('getRowData', ids);
				var invtyEncd = rowData.invtyEncd;
				window.localStorage.setItem("invtyEncd", invtyEncd);
				//获取现存量可用量
				getQty(rowid, invtyEncd, tranOutWhsEncd, batNum);
			}
			if(cellname == "projEncd") {
				GetProInfo1(rowid, val);
			}
			//设置失效日期
			if(cellname == "prdcDt" || cellname == "batNum") {
				var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
			    var baoZhiQi = rowDatas.baoZhiQi;
			    var val = rowDatas.prdcDt;
			    setProductDate(rowid, val, baoZhiQi)
			}
			if(cellname == "bxQty" || cellname == "cannibQty") {
				if(val < 0) {
					alert("数量不能小于0")
				}
			}
			//获得批次后
			if(cellname == "batNum") {
				var tranOutWhsEncd = localStorage.tranOutWhsEncd;

				//获得行号
				var ids = $("#jqgrids").jqGrid('getGridParam', 'selrow');
				//获得行数据
				var rowData = $("#jqgrids").jqGrid('getRowData', ids);
				//获得本行存货编码
				var invtyEncd = rowData.invtyEncd;
				//获得本行批次
				var batNum = rowData.batNum;
				//获取现存量可用量
				getQty(rowid, invtyEncd, tranOutWhsEncd, batNum)

			}
			//设置数量
			if((cellname == "bxQty") ||
				(cellname == "cannibQty") ||
				(cellname == "taxRate")) {
				//设置变量
				SetNums1(rowid, cellname);
				var numList = getJQAllData1()
				if(numList.length == 0) {
					fn()
				}
				sumAmt()
			}
		}
	})
	$("#jqgrids").jqGrid('navGrid', '#ca_jqgridPager', {
		edit: false,
		add: false,
		del: false,
		search: false,
		refresh: false,
	}).navButtonAdd('#ca_jqgridPager', {
		caption: "",
		buttonicon: "ui-icon-trash",
		onClickButton: function() {
			var grid = "jqgrids"
			//删除一行操作
			removeRows(grid);
		},
		position: "first"
	}).navButtonAdd('#ca_jqgridPager', {
		caption: "",
		buttonicon: "ui-icon-newwin",
		onClickButton: function() {
			//复制一行操作
			copyRows();
		},
		position: "last"
	}).navButtonAdd('#ca_jqgridPager', {
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
			console.log(error);
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
//根据存货编码，出库编码，批次获得可用量库存量
function getQty(rowid, invtyEncd, tranOutWhsEncd, batNum) {
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			'invtyEncd': invtyEncd,
			'tranOutWhsEncd': tranOutWhsEncd,
			'batNum': batNum
		}
	};
	var postData = JSON.stringify(savedata);
	$.ajax({
		url: url + '/mis/whs/cannib_sngl/selectInvty',
		type: 'post',
		data: postData,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		error: function() {
			alert("获取数据错误");
		}, //错误执行方法
		success: function(data) {
			var list = data.respBody.list[0]
			//设置页面数据展示现存量，可用量
			$("#jqgrids").setRowData(rowid, {
				nowStok: list.nowStok,
				avalQty: list.avalQty
			});
		}
	})
}

//数值转换，后保留几位小数---jingdu为参数
function toDecimal(x) {
	var f = parseFloat(x);
	var s = f.toFixed(jingdu)
	return s;
}

//设置数量和金额
function SetNums1(rowid, cellname) {
	var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
	var bxRule = parseFloat(rowDatas.bxRule);
	var qty = parseFloat(rowDatas.cannibQty);
	var bxQty = parseFloat(rowDatas.bxQty);
	var taxRate = parseFloat(rowDatas.taxRate);
	if(cellname == "cannibQty") {
		bxQty = parseFloat(qty / bxRule); //箱数
	} else if(cellname == "bxQty") {
		qty = parseFloat(bxQty * bxRule);
	}
	if(!isNaN(bxQty)) {
		bxQty = parseFloat(bxQty);
	} //数量
	else {
		bxQty = "";
	}
	if(!isNaN(qty)) {
		qty = qty
	} //盒数量
	else {
		qty = ""
	}
	if(!isNaN(taxRate)) {
		taxRate = taxRate
	} //税率
	else {
		taxRate = ""
	}
	
	var cost, noTax, taxAmtNum, prcTax, cntnTax
	cost = parseFloat(rowDatas.unTaxUprc); //无税单价

	noTax = toDecimal(cost * qty); //无税金额
	if(isNaN(noTax)) {
		noTax = ""
	}
	taxAmtNum = toDecimal(noTax * taxRate * 0.01); //税额
	if(isNaN(taxAmtNum)) {
		taxAmtNum = ""
	}
	prcTax = toDecimal(noTax * 1 + taxAmtNum * 1); //价税合计
	if(isNaN(prcTax)) {
		prcTax = ""
	}
	cntnTax = toDecimal(prcTax / qty); //含税单价
	if(isNaN(cntnTax)) {
		cntnTax = ""
	}
	$("#jqgrids").setRowData(rowid, {
		taxRate: taxRate,
		bxQty: bxQty,
		cannibQty: qty,
		unTaxAmt: noTax,
		taxAmt: taxAmtNum,
		cntnTaxAmt: prcTax,
		cntnTaxUprc: cntnTax,
		isNtRtnGoods: 1
	});

}

// 点击修改按钮，执行的操作
$(function() {
	$('.editOrder').click(function() {
		$("#jqgrids").setColProp("invtyEncd", {
			editable: true
		});
		$("#jqgrids").setColProp("cannibQty", {
			editable: true
		});
		$("#jqgrids").setColProp("batNum", {
			editable: true
		});
		$("#jqgrids").setColProp("prdcDt", {
			editable: true
		});
		$("#jqgrids").setColProp("unTaxUprc", {
			editable: true
		});
		$("#jqgrids").setColProp("taxRate", {
			editable: true
		});
		$("#jqgrids").setColProp("bxQty", {
			editable: true
		});
		$("#jqgrids").setColProp("projEncd", {
			editable: true
		});
		mType = 2;
		$('button').addClass("gray")
		$('.saveOrder').removeClass("gray");
		$('.upOrder').removeClass("gray");
		$('.editOrder').addClass("gray"); //点击修改后 修改不能用
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)

		$("#gbox_jqGrids").hide();
		$("#gbox_jqgrids").show();
	});
})

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
	var cannibDt = $("input[name='cannibDt']").val();
	var tranOutWhsEncd = $("input[name='tranOutWhsEncd']").val();
	var tranInWhsEncd = $("input[name='tranInWhsEncd']").val();
	//	var tranInWhsEncd = $("#tranInWhsEncd").val();
	var memo = $("input[name='memo']").val();

	//判断页面是否有值为空
	if(IsCheckValue(tranOutWhsEncd, tranInWhsEncd, listData) == true) {

		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				'formNum': "",
				'formDt': cannibDt,
				'tranOutWhsEncd': tranOutWhsEncd,
				'tranInWhsEncd': tranInWhsEncd,
				'list': listData,
				'memo': memo
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			url: url + '/mis/whs/cannib_sngl/insertCSngl',
			type: 'post',
			data: saveData,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			success: function(data) {
				alert(data.respHead.message)
				formNum = data.respBody.formNum;
				$("input[name='formNum']").val(data.respBody.formNum); //订单编码
				if(data.respHead.isSuccess == true) {
					$("#jqgrids").setColProp("invtyEncd", {
						editable: false
					});
					$("#jqgrids").setColProp("batNum", {
						editable: false
					});
					$("#jqgrids").setColProp("cannibQty", {
						editable: false
					})
					$("#jqgrids").setColProp("prdcDt", {
						editable: false
					});
					$("#jqgrids").setColProp("taxRate", {
						editable: false
					});
					$("#jqgrids").setColProp("bxQty", {
						editable: false
					});
					$("#jqgrids").setColProp("unTaxUprc", {
						editable: false
					});
					$("#jqgrids").setColProp("projEncd", {
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
var formNum;

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
				url: url + '/mis/whs/cannib_sngl/deleteAllCSngl',
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
	var chk1 = '/mis/whs/cannib_sngl/updateCSnglChk';
	var chk2 = '/mis/whs/cannib_sngl/updateCSnglNoChk';
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

function IsCheckValue(tranOutWhsEncd, tranInWhsEncd, listData) {
	if(tranOutWhsEncd == '') {
		alert("转出仓库不能为空")
		return false;
	} else if(tranInWhsEncd == '') {
		alert("转入仓库不能为空")
		return false;
	} else if(listData.length == 0) {
		alert("存货不能为空")
		return false;
	} else if(listData.length != 0) {
		for(var i = 0; i < listData.length; i++) {
			if(listData[i].cannibQty == "") {
				alert("数量不能为空")
				return false;
			} else if(listData[i].batNum == "") {
				alert("批次不能为空")
				return false;
			} else if(listData[i].cannibQty < 0) {
				alert("数量不能小于0")
				return false;
			}  else if(listData[i].bxQty < 0) {
				alert("箱数不能小于0")
				return false;
			} else if(listData[i].taxRate == "") {
				alert("税率不能为空")
				return false;
			}
		}
	}
	return true;
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
					$('button').attr('disabled', false);
					$(".gray").attr('disabled', true)
				} else if(x == 0) {
					$("button").removeClass("gray");
					$(".upOrder").addClass("gray");
					$(".noTo").addClass("gray");
					$(".saveOrder").removeClass("gray");
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

//点完修改后执行的url
function SaveModifyData() {
	var listData = getJQAllData();

	formNum = $("input[name='formNum']").val();
	var cannibDt = $("input[name='cannibDt']").val();
	var tranOutWhsEncd = $("input[name='tranOutWhsEncd']").val();
	var tranInWhsEncd = $("input[name='tranInWhsEncd']").val();
	var memo = $("input[name='memo']").val();

	if(IsCheckValue(tranOutWhsEncd, tranInWhsEncd, listData) == true) {
		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				'formNum': formNum,
				'formDt': cannibDt,
				'tranOutWhsEncd': tranOutWhsEncd,
				'tranInWhsEncd': tranInWhsEncd,
				'memo': memo,
				'list': listData
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			url: url + '/mis/whs/cannib_sngl/updateCSngl',
			type: 'post',
			data: saveData,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			success: function(data) {
				alert(data.respHead.message)
				if(data.respHead.isSuccess == true) {
					$("#jqgrids").setColProp("invtyEncd", {
						editable: false
					});
					$("#jqgrids").setColProp("batNum", {
						editable: false
					});
					$("#jqgrids").setColProp("cannibQty", {
						editable: false
					})
					$("#jqgrids").setColProp("prdcDt", {
						editable: false
					});
					$("#jqgrids").setColProp("taxRate", {
						editable: false
					});
					$("#jqgrids").setColProp("bxQty", {
						editable: false
					});
					$("#jqgrids").setColProp("unTaxUprc", {
						editable: false
					});
					$("#jqgrids").setColProp("projEncd", {
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

$(function() {
	$('.print1').click(function() {
		var formNum = $("input[name='formNum']").val();
		if(formNum == '') {
			alert("不存在的单据")
		} else {
			localStorage.setItem("formNum",formNum)
			window.open("../../Components/whs/print_cannib.html?1");
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
	$("#jqgrids").setColProp("invtyEncd", {
		editable: false
	});
	$("#jqgrids").setColProp("cannibQty", {
		editable: false
	});
	$("#jqgrids").setColProp("batNum", {
		editable: false
	});
	$("#jqgrids").setColProp("prdcDt", {
		editable: false
	});
	$("#jqgrids").setColProp("unTaxUprc", {
		editable: false
	});
	$("#jqgrids").setColProp("taxRate", {
		editable: false
	});
	$("#jqgrids").setColProp("bxQty", {
		editable: false
	});
	$("#jqgrids").setColProp("projEncd", {
		editable: false
	});
//	$("#mengban").hide();
	formNum = localStorage.formNum;
	var isNtChk = localStorage.isNtChk;
	if(isNtChk == 0) {
		$(".toExamine").removeClass("gray");
		$(".print1").removeClass("gray");
		$('.addOrder').addClass("gray")
		$('.refer').addClass("gray")
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
	} else if(isNtChk == 1) {
		$(".noTo").removeClass("gray");
		$(".print1").removeClass("gray");
		$('.addOrder').addClass("gray")
		$('.refer').addClass("gray")
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
	}
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"formNum": formNum,
			"pageNo": 1,
			"pageSize": 99999999
		}
	};
	var saveData = JSON.stringify(savedata);
	var myData = {};
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
//      contentType:"text/html;charset=utf8",
		url: url + '/mis/whs/cannib_sngl/query',
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
			console.log(data)
			$(".print1").removeClass("gray");
			$('.addOrder').addClass("gray")
			$('.refer').addClass("gray")
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true)
			var list1 = data.respBody;
			$("input[name='cannibDt']").val(list1.cannibDt);
			$("input[name='formNum']").val(list1.formNum);
			$("#tranOutWhsEncd").val(list1.tranOutWhsEncd);
			$("#tranOutWhsNm").val(list1.tranOutWhsEncdName);
			$("#tranInWhsEncd").val(list1.tranInWhsEncd);
			$("#tranInWhsNm").val(list1.tranInWhsEncdName);
			$("input[name='memo']").val(list1.memo);

			var list = data.respBody.list;
			$("#jqgrids").jqGrid('clearGridData');
			$("#jqgrids").jqGrid('setGridParam', {
				datatype: 'local',
				data: list, //newData是符合格式要求的重新加载的数据
			}).trigger("reloadGrid")
			sumAmt()
		},
		error: function() {
			alert("error")
		}
	})
}
$(function() {
	$('.ctrlc_v').click(function() {
		$(".ctrlc_v").addClass("gray");
		$(".ctrlYes").removeClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
		$("#jqgrids").setColProp("invtyNm",{editable:true});
		$("#jqgrids").setColProp("spcModel",{editable:true});
		$("#jqgrids").setColProp("measrCorpNm",{editable:true});
		$("#jqgrids").setColProp("baoZhiQi",{editable:true});
		$("#jqgrids").setColProp("invldtnDt",{editable:true});
		$("#jqgrids").setColProp("cntnTaxUprc",{editable:true});
		$("#jqgrids").setColProp("cntnTaxAmt",{editable:true});
		$("#jqgrids").setColProp("unTaxUprc",{editable:true});
		$("#jqgrids").setColProp("unTaxAmt",{editable:true});
		$("#jqgrids").setColProp("bxRule",{editable:true});
		$("#jqgrids").setColProp("projNm",{editable:true});
		var obj = $("#jqgrids");
		//获取grid表中所有的rowid值
		var rowIds = obj.getDataIDs();
		for(var i =0;i<rowIds.length;i++) {
			$('#jqgrids').jqGrid('editRow', rowIds[i], true);
			
		}
	})
	$('.ctrlYes').click(function() {
		$(".ctrlc_v").removeClass("gray");
		$(".ctrlYes").addClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
		submit()
		sumAmt()
	})
})
function submit() {
	var obj = $("#jqgrids");
	//获取grid表中所有的rowid值
	var rowids = obj.getDataIDs();
	for(var i =0;i<rowids.length;i++) {
		var goods = $("#" + rowids[i] + "_invtyEncd").val()
		var cannibQty = $("#" + rowids[i] + "_cannibQty").val()
		var projEncd = $("#" + rowids[i] + "_projEncd").val()
		jQuery("#jqgrids").saveRow(rowids[i], false, 'clientArray');
		var rowid = rowids[i]
		if(goods == '') {
			continue;
		} else if(goods != '') {
			GetGoodsInfo_can(rowid, goods)
		}
		if(cannibQty == '') {
			continue;
		} else if(cannibQty != '') {
			SetNums1(rowid, 'cannibQty');
		}
		if(projEncd == '') {
			continue;
		} else if(projEncd != '') {
			GetProInfo1(rowid, projEncd)
		}
	}
	$("#jqgrids").setColProp("invtyNm",{editable:false});
	$("#jqgrids").setColProp("spcModel",{editable:false});
	$("#jqgrids").setColProp("measrCorpNm",{editable:false});
	$("#jqgrids").setColProp("baoZhiQi",{editable:false});
	$("#jqgrids").setColProp("invldtnDt",{editable:false});
	$("#jqgrids").setColProp("cntnTaxUprc",{editable:false});
	$("#jqgrids").setColProp("cntnTaxAmt",{editable:false});
	$("#jqgrids").setColProp("unTaxUprc",{editable:false});
	$("#jqgrids").setColProp("unTaxAmt",{editable:false});
	$("#jqgrids").setColProp("bxRule",{editable:false});
	$("#jqgrids").setColProp("projNm",{editable:false});
}
$(function () {
	$('#jqgrids').pasteFromTable();
})