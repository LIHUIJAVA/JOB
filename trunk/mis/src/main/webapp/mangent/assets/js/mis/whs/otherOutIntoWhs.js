var mType = 0;

$(function() {
	getData();
	$('button').addClass("gray");
	$('.addOrder').removeClass("gray")
	$('.batNum_search').removeClass("gray")
	$('.print1').removeClass("gray")
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
			$('.gds_sure').removeClass("gray");
			$('.gds_cancel').removeClass("gray")
			$(".gds_delete").removeClass("gray");
			$(".gdsBitAllotted").removeClass("gray");
			$('.toExamine').removeClass("gray");
			$('.delOrder').removeClass("gray");
			$('.editOrder').removeClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
		}
	} else if(b == 2) {
		$(".gdsBitAllotted").addClass("gray")
		$('button').addClass("gray")
		$(".gray").attr('disabled', true)
	}
})

// 点击增加按钮，执行的操作
$(function() {
	$('.addOrder').click(function() {
		$("#jqgrids").setColProp("invtyEncd", {
			editable: true
		});
		$("#jqgrids").setColProp("qty", {
			editable: true
		})
		$("#jqgrids").setColProp("prdcDt", {
			editable: true
		});
		$("#jqgrids").setColProp("memos", {
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
		$("#jqgrids").setColProp("batNum", {
			editable: true
		});
		$("#jqgrids").setColProp("projEncd", {
			editable: true
		});
		
		localStorage.removeItem('whsEncd');

		$(".saveOrder").removeClass("gray");
		$(".upOrder").removeClass("gray"); //新增后放弃能用
		$('.addOrder').addClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
		mType = 1;

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
	    makeCode_new()
	    $("#qrcode").hide()
//	    $("#qrcode").removeAttr("title");
//	    $("#qrcode canvas").removeAttr("style");
//	    $("#qrcode img").removeAttr("src");
//	    $("#qrcode img").attr("style","display: none;");
		$("#outIntoWhsTypId").val("");
		$("input[name=purType]").val("普通采购");
		$("input[name=formDt]").val(BillDate());
	});
})
//点击表格图标显示仓库列表
$(function() {
	$(document).on('click', '.whsNm', function() {
		window.open("../../Components/baseDoc/whsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})

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
		//到货单
		//获得行号
		var rowid = $("#jqgrids").jqGrid('getGridParam', 'selrow');

		//	存货档案
		//	获得行号
		var ids = $("#insert_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowData = $("#insert_jqgrids").jqGrid('getRowData', ids);

		$("#" + rowid + "_invtyEncd").val(rowData.invtyEncd)
		var baoZhiQi = rowData.baoZhiQi;
		$("#jqgrids").setRowData(rowid, {
			baoZhiQi: baoZhiQi
		})
		var outIntoWhsTyp = $("#outIntoWhsTypId").val();
		$("#insertList").css("opacity", 0);
		$("#purchaseOrder").show();
	})
	//	取消
	$(".cancel").click(function() {
		$("#insertList").css("opacity", 0);
		$("#purchaseOrder").show();
		//到货单
		//获得行号
		var rowid = $("#jqgrids").jqGrid('getGridParam', 'selrow');

		$("#" + rowid + "_invtyEncd").val("")
	})

	//确定
	$(".true_b").click(function() {
		//调拨单
		//获得行号
		var rowid = $("#jqgrids").jqGrid('getGridParam', 'selrow');

		//	存货档案
		//	获得行号
		var ids = $("#batNum_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowData = $("#batNum_jqgrids").jqGrid('getRowData', ids);

		$("#jqgrids input[name='batNum']").val(rowData.batNum)
		var prdoc = rowData.prdcDt;
		var baoZhiQi = rowData.baoZhiQi;
		var unTaxUprc = rowData.unTaxUprc;
		var pr = prdoc.split(" ")
		var p;
		for(var i = 0; i < pr.length; i++) {
			p = pr[0].toString()
		}
		$("#jqgrids").setRowData(rowid, {
			prdcDt: p,
			unTaxUprc: unTaxUprc,
			baoZhiQi: baoZhiQi
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
			"ordrNum": "",
			"invtyEncd": "",
			"invtyNm": '',
			"spcModel": '',
			"measrCorpNm": '',
			"measrCorpId": '',
			"qty": '',
			"cntnTaxUprc": '',
			"cntnTaxAmt": '',
			"unTaxUprc": '',
			"unTaxAmt": '',
			"taxRate": '',
			"taxAmt": '',
			"batNum": '',
			"bxRule": '',
			"bxQty": '',
			"baoZhiQi": '',
			"prdcDt": '',
			"invldtnDt": '',
			"memos": '',
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
//初始化表格
$(function() {
	allHeight()
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	$("#jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json",
		colNames: ['备注' ,'序号', '存货编码*', '存货名称', '规格型号', '主计量单位', '计量单位编码', '批次*', '无税单价', '数量*', '含税单价', '价税合计',
			'无税金额', '项目编码', '项目名称', '税率', '税额', '箱规', '箱数', '保质期', '生产日期', '失效日期','是否保质期管理'
		], //jqGrid的列显示名字
		colModel: [{
				name: 'memos',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'ordrNum',
				editable: false,
				align: 'center',
				sortable: false,
				hidden: true
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
				name: 'batNum', //批次
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'unTaxUprc',
				editable: false,
				align: 'center',
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
				name: 'cntnTaxAmt', //含税金额
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'unTaxAmt',
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
				name: 'baoZhiQi',
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
				name: 'invldtnDt', //失效日期
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'isQuaGuaPer',
				editable: false,
				align: 'center',
				sortable: false,
//				hidden:true
			}
		],
		rowNum: 99999999,
		loadonce: false,
		rownumbers: true,
		autoScroll: true,
		shrinkToFit: false,
		autowidth: true,
		multiselect: true, //复选框 
		multiboxonly: true,
		caption: '其他出入库',
		altclass: true,
		pgbuttons: false,
		pginput: false,
		//		viewrecords: true,
		height: height,
		forceFit: true,
		pager: "#jqgridPager",
		sortorder: "desc",
		cellEdit: true,
		cellsubmit: "clientArray",
		footerrow: true,
		gridComplete: function() {
			$("#jqgrids").footerData('set', {
				"invtyEncd": "本页合计",
			});
		},
		afterEditCell: function(rowid, cellname, val, iRow, iCol) {
			$(".saveOrder").addClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true)
			var outIntoWhsTyp = $("#outIntoWhsTypId").val();
			console.log(outIntoWhsTyp)
			if(cellname == "invtyEncd") {
				if(outIntoWhsTyp == null) {
					alert("请选择出入库类型")
					$("#" + rowid + "_invtyEncd").attr("readonly", "readonly");
					return;
				}else {
					$("#" + rowid + "_" + cellname).bind('keyup', function() {
						findGrid(rowid, cellname, val);
					})
					$("#" + rowid + "_" + cellname).bind("dblclick", function() {
						$("#insertList").css("opacity", 1);
						$("#insertList").show();
						$("#purchaseOrder").hide();
						$("#batNum_list").hide();
						$("#ProjClsList").hide();
						$(".addWhs").removeClass("gray") //确定可用
						$(".find").removeClass("gray") //查询可用
						$(".cancel").removeClass("gray") //取消可用
						$('button').attr('disabled', false); //可点击状态
					});
				}
			}

			if(cellname == "prdcDt") {
				$("input[name='prdcDt']").attr("calendar","YYYY-MM-DD")
			}
			if(cellname == "cntnTaxUprc") {//含税单价
				$("input[name='cntnTaxUprc']").attr("onkeyup","if(this.value.length==1){this.value=this.value.replace(/[^0-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
				$("input[name='cntnTaxUprc']").attr("onafterpaste","if(this.value.length==1){this.value=this.value.replace(/[^0-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
			}
			if(cellname == "cntnTaxAmt") {//价税合计
				$("input[name='cntnTaxAmt']").attr("onkeyup","if(this.value.length==1){this.value=this.value.replace(/[^0-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
				$("input[name='cntnTaxAmt']").attr("onafterpaste","if(this.value.length==1){this.value=this.value.replace(/[^0-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
			}
			if(cellname == "unTaxAmt") {//无税金额
				$("input[name='unTaxAmt']").attr("onkeyup","if(this.value.length==1){this.value=this.value.replace(/[^0-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
				$("input[name='unTaxAmt']").attr("onafterpaste","if(this.value.length==1){this.value=this.value.replace(/[^0-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
			}
			if((cellname == "unTaxUprc") ||(cellname == "bxQty")) {
				$("input[name='unTaxUprc']").attr("onkeyup","if(this.value.length==1){this.value=this.value.replace(/[^0-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
				$("input[name='unTaxUprc']").attr("onafterpaste","if(this.value.length==1){this.value=this.value.replace(/[^0-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
				$("input[name='bxQty']").attr("onkeyup","if(this.value.length==1){this.value=this.value.replace(/[^0-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
				$("input[name='bxQty']").attr("onafterpaste","if(this.value.length==1){this.value=this.value.replace(/[^0-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
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
			if(cellname == "qty") {
				var outIntoWhsTyp = $("#outIntoWhsTypId").val();
				var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
				var unTaxUprc = rowDatas.unTaxUprc
				if(outIntoWhsTyp == 12 && unTaxUprc == '') {
					alert("请填入无税单价")
					console.log(rowid)
					$("#" + rowid + "_qty").attr("readonly", "readonly");
//					return;
				}else if(outIntoWhsTyp == 11) {
					var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
					if(rowDatas.batNum == "") {
						alert("未选择批次");
						$("#" + rowid + "_qty").attr('disabled', 'disabled');
					}
				}
				$("input[name='qty']").attr("onkeyup","if(this.value.length==1){this.value=this.value.replace(/[^0-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
				$("input[name='qty']").attr("onafterpaste","if(this.value.length==1){this.value=this.value.replace(/[^0-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
			}
			if(cellname == "batNum") {
				var outIntoWhsTyp = $("#outIntoWhsTypId").val();
				console.log(outIntoWhsTyp)
				if(outIntoWhsTyp == 11) {
					$("#jqgrids input[name='batNum']").attr("readonly", "readonly");
					//双击批次时
					$("#jqgrids input[name='batNum']").bind("dblclick", function() {
						$(".true_b").removeClass("gray");
						$(".cancel").removeClass("gray");
						$(".search").removeClass("gray");
						$('button').attr('disabled', false);
						$(".gray").attr('disabled', true)
						//获得行数据
						var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
						var invtyEncd = rowDatas.invtyEncd;
						var whsEncd = $("#whsEncd").val();
						if(invtyEncd == undefined || invtyEncd == null || whsEncd == "") {
							alert("请填入仓库和存货")
						} else {
							window.localStorage.setItem("invtyEncd", invtyEncd);
							window.localStorage.setItem("whsEncd", whsEncd);
							//存货列表显示
							$("#insertList").css("opacity", 0);
							$("#insertList").hide();
							$("#ProjClsList").hide();
							//调拨单隐藏
							$("#purchaseOrder").hide();
							$("#whsDocList").hide();
							$("#batNum_list").css("opacity", 1);
							$("#batNum_list").css("display", "block");
							searcherBatNum();
						}
					});
				}else if(outIntoWhsTyp == 12) {
					var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
					var invtyEncd = rowDatas.invtyEncd;
					var whsEncd = $("#whsEncd").val();
					if(invtyEncd == undefined || invtyEncd == null || whsEncd == "") {
						alert("请填入仓库和存货")
						$("#" + rowid + "_batNum").attr("readonly", "readonly");
						return
					}
				}
			}
		},
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
				whs_GetGoodsInfo(rowid, val);
			}
			if(cellname == "projEncd") {
				GetProInfo1(rowid, val);
			}
			if(cellname == "batNum") {
				var outIntoWhsTyp = $("#outIntoWhsTypId").val();
				if(outIntoWhsTyp == 11) {
					setProductDateBat(rowid)
				}else if(outIntoWhsTyp == 12) {
					var ids = $('#jqgrids').jqGrid('getGridParam', 'selrow');
					var rowDatas = $("#jqgrids").jqGrid('getRowData', ids);
					//计算生产日期
					var prdcDt = rowDatas.prdcDt //生产日期
					//保质期
					var baoZhiQiDt = rowDatas.baoZhiQi
					var invldtnDt = getNewDay1(prdcDt, baoZhiQiDt);
					if(invldtnDt == "NaN-NaN-NaN") {
						invldtnDt = "";
					} else {
						invldtnDt = invldtnDt
					};
					$("#jqgrids").setRowData(rowid, {
						invldtnDt: invldtnDt,
						baoZhiQi: baoZhiQiDt
					});
				}
			}
			
			//生产日期
			if(cellname == "prdcDt" ) {
				var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
				console.log(rowDatas)
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
			if(cellname == "qty") {
				if(val < 0) {
					alert("数量不能小于0")
				}
			}
			if(cellname == "unTaxUprc") {
				if(val < 0) {
					alert("无税单价不能小于0")
				}
			}
			if(cellname == "bxQty") {
				if(val < 0) {
					alert("箱数不能小于0")
				}
			}
			
			if(cellname == "cntnTaxUprc") {//含税单价
				SetNums_Uprc(rowid, cellname)
				var numList = getJQAllData1()
				if(numList.length == 0) {
					fn()
				}
				sumAmt();
			}
			if(cellname == "cntnTaxAmt") {//价税合计
				SetNums_Amt(rowid, cellname)
				var numList = getJQAllData1()
				if(numList.length == 0) {
					fn()
				}
				sumAmt();
			}
			if(cellname == "unTaxAmt") {//无税金额
				SetNums_unTaxAmt(rowid, cellname)
				var numList = getJQAllData1()
				if(numList.length == 0) {
					fn()
				}
				sumAmt();
			}
			if(cellname == "unTaxUprc") {//无税单价
				SetNums_unTaxUprc(rowid, cellname)
				var numList = getJQAllData1()
				if(numList.length == 0) {
					fn()
				}
				sumAmt();
			}
			if(cellname == "taxRate") {//税率
				SetNums_taxRate(rowid, cellname)
				var numList = getJQAllData1()
				if(numList.length == 0) {
					fn()
				}
				sumAmt();
			}
//			if(cellname == "bxQty") {//箱数
////				var qty = parseFloat(rowDatas.qty);//数量
//				var bxRule = parseFloat(rowDatas.bxRule);//箱规
//			//	var bxQty = parseFloat(rowDatas.bxQty);//箱数
//			//	if(cellname == "cannibQty") {
////				var	bxQty = parseFloat(qty / bxRule); //箱数
////				} else if(cellname == "bxQty") {
//				var	qty = parseFloat(bxQty * bxRule);
////				}
//				if(!isNaN(bxQty)) {
//					bxQty = parseFloat(bxQty);
//				} //数量
//				else {
//					bxQty = "";
//				}
//				$("#jqgrids").setRowData(rowid, {
//					qty: qty,
//					isNtRtnGoods: 1
//				});
//			}
			if(cellname == "qty") {//数量
				SetNums_rQty(rowid, cellname)
				var numList = getJQAllData1()
				if(numList.length == 0) {
					fn()
				}
				sumAmt();
				var outIntoWhsTyp = $("#outIntoWhsTypId").val();
				var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
				var cntnTaxUprc = rowDatas.cntnTaxUprc
				var cntnTaxAmt = rowDatas.cntnTaxAmt
				var unTaxAmt = rowDatas.unTaxAmt
				var qty = rowDatas.qty
				console.log(cntnTaxUprc,cntnTaxAmt,unTaxAmt)
				if(cntnTaxUprc != '' && cntnTaxAmt != '' && unTaxAmt != '' && qty != '' && outIntoWhsTyp == 12) {
					$("#jqgrids").setColProp("cntnTaxUprc", {
						editable: true
					});
					$("#jqgrids").setColProp("cntnTaxAmt", {
						editable: true
					});
					$("#jqgrids").setColProp("unTaxAmt", {
						editable: true
					});
				}else {
					$("#jqgrids").setColProp("cntnTaxUprc", {
						editable: false
					});
					$("#jqgrids").setColProp("cntnTaxAmt", {
						editable: false
					});
					$("#jqgrids").setColProp("unTaxAmt", {
						editable: false
					});
					$("#jqgrids").setColProp("unTaxUprc", {
						editable: false
					});
					$("#jqgrids").setColProp("taxRate", {
						editable: false
					});
					SetNums_cQty(rowid, cellname)
					var numList = getJQAllData1()
					if(numList.length == 0) {
						fn()
					}
					sumAmt();
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
function sumAmt() {
	var list = getJQAllData(); //此函数在有参照功能的单据中新增
	var qty = 0;
	var unTaxAmt = 0;
	var cntnTaxAmt = 0;
	var prcTaxSum = 0;
	for(var i = 0; i < list.length; i++) {
		qty += parseFloat(list[i].qty);
		unTaxAmt += parseFloat(list[i].unTaxAmt);
		cntnTaxAmt += parseFloat(list[i].cntnTaxAmt);
		prcTaxSum += parseFloat(list[i].prcTaxSum);
	};
	if(isNaN(qty)) {
		qty = 0
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
	qty = qty.toFixed(prec)
	unTaxAmt = precision(unTaxAmt,2)
	cntnTaxAmt = precision(cntnTaxAmt,2)
	prcTaxSum = precision(prcTaxSum,2)
	$("#jqgrids").footerData('set', {
		"qty": qty,
		"unTaxAmt": unTaxAmt,
		"cntnTaxAmt": cntnTaxAmt,
		"prcTaxSum": prcTaxSum,
	});
}


//数值转换
function toDecimal(x) {
	var f = parseFloat(x);
	var s = f.toFixed(jingdu)
	return s;
}

//设置数量和金额----含税单价
function SetNums_Uprc(rowid, cellname) {
	var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
	var cntnTaxUprc = parseFloat(rowDatas.cntnTaxUprc);//含税单价
	var qty = parseFloat(rowDatas.qty);//数量
	if(isNaN(qty)) {
		qty = 0
	}
	var taxRate = parseFloat(rowDatas.taxRate);//税率
	
	var cntnTaxAmt = toDecimal(cntnTaxUprc * qty)//价税合计
	if(isNaN(cntnTaxAmt)) {
		cntnTaxAmt = ""
	}
	var unTaxAmt = toDecimal(cntnTaxAmt / (taxRate * 0.01 + 1))//无税金额
	if(isNaN(unTaxAmt)) {
		unTaxAmt = ""
	}
	var unTaxUprc = toDecimal(unTaxAmt / qty)//无税单价
	if(isNaN(unTaxUprc)) {
		unTaxUprc = ""
	}
	var taxAmt = toDecimal(cntnTaxAmt * 1 - unTaxAmt * 1)//税额
	if(isNaN(taxAmt)) {
		taxAmt = ""
	}
	$("#jqgrids").setRowData(rowid, {
		cntnTaxAmt: cntnTaxAmt,
		unTaxAmt: unTaxAmt,
		unTaxUprc: unTaxUprc,
		taxAmt: taxAmt,
		isNtRtnGoods: 1
	});

}
//设置数量和金额----价税合计
function SetNums_Amt(rowid, cellname) {
	var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
	var cntnTaxAmt = parseFloat(rowDatas.cntnTaxAmt);//价税合计
	var qty = parseFloat(rowDatas.qty);//数量
	var taxRate = parseFloat(rowDatas.taxRate);//税率

	var cntnTaxUprc = toDecimal(cntnTaxAmt / qty)//含税单价
	if(isNaN(cntnTaxUprc)) {
		cntnTaxUprc = ""
	}
	var unTaxAmt = toDecimal(cntnTaxAmt / (taxRate * 0.01 + 1))//无税金额
	if(isNaN(unTaxAmt)) {
		unTaxAmt = ""
	}
	var unTaxUprc = toDecimal(unTaxAmt / qty)//无税单价
	if(isNaN(unTaxUprc)) {
		unTaxUprc = ""
	}
	var taxAmt = toDecimal(cntnTaxAmt * 1 - unTaxAmt * 1) //税额
	if(isNaN(taxAmt)) {
		taxAmt = ""
	}
	$("#jqgrids").setRowData(rowid, {
		cntnTaxUprc: cntnTaxUprc,
		unTaxAmt: unTaxAmt,
		taxAmt: taxAmt,
		unTaxUprc: unTaxUprc,
		isNtRtnGoods: 1
	});

}
//设置数量和金额----无税单价
function SetNums_unTaxUprc(rowid, cellname) {
	var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
	var unTaxUprc = parseFloat(rowDatas.unTaxUprc);//无税单价
	var qty = parseFloat(rowDatas.qty);//数量
	var taxRate = parseFloat(rowDatas.taxRate);//税率

	var cntnTaxUprc = toDecimal(unTaxUprc * (taxRate * 0.01 + 1))//含税单价
	if(isNaN(cntnTaxUprc)) {
		cntnTaxUprc = ""
	}
	var cntnTaxAmt = toDecimal(cntnTaxUprc * qty)//价税合计
	if(isNaN(cntnTaxAmt)) {
		cntnTaxAmt = ""
	}
	var taxAmt = toDecimal(cntnTaxAmt / (taxRate * 0.01 + 1) * taxRate * 0.01) //税额
	if(isNaN(taxAmt)) {
		taxAmt = ""
	}
	var unTaxAmt = toDecimal(cntnTaxAmt * 1 - taxAmt * 1)//无税金额
	if(isNaN(unTaxAmt)) {
		unTaxAmt = ""
	}
	$("#jqgrids").setRowData(rowid, {
		cntnTaxUprc: cntnTaxUprc,
		cntnTaxAmt: cntnTaxAmt,
		taxAmt: taxAmt,
		unTaxAmt: unTaxAmt,
		isNtRtnGoods: 1
	});

}
//设置数量和金额----数量（入）
function SetNums_rQty(rowid, cellname) {
	var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
	var unTaxUprc = parseFloat(rowDatas.unTaxUprc);//无税单价
	var taxRate = parseFloat(rowDatas.taxRate);//税率
	var qty = parseFloat(rowDatas.qty);//数量
	var bxRule = parseFloat(rowDatas.bxRule);//箱规
//	var bxQty = parseFloat(rowDatas.bxQty);//箱数
//	if(cellname == "cannibQty") {
	var	bxQty = parseFloat(qty / bxRule); //箱数
//	} else if(cellname == "bxQty") {
//		qty = parseFloat(bxQty * bxRule);
//	}
	if(!isNaN(bxQty)) {
		bxQty = parseFloat(bxQty);
	} //数量
	else {
		bxQty = "";
	}
	var unTaxAmt = toDecimal(unTaxUprc * qty)//无税金额
	if(isNaN(unTaxUprc)) {
		unTaxUprc = ""
	}
	var taxAmt = toDecimal(unTaxAmt * taxRate * 0.01) //税额
	if(isNaN(taxAmt)) {
		taxAmt = ""
	}
	var cntnTaxAmt = toDecimal(unTaxAmt * taxRate * 0.01)//价税合计
	if(isNaN(unTaxAmt)) {
		unTaxAmt = ""
	}
	var cntnTaxUprc = toDecimal(cntnTaxAmt / qty)//含税单价
	if(isNaN(cntnTaxUprc)) {
		cntnTaxUprc = ""
	}
	$("#jqgrids").setRowData(rowid, {
		unTaxAmt: unTaxAmt,
		taxAmt: taxAmt,
		bxQty: bxQty,
//		qty: qty,
		cntnTaxAmt: cntnTaxAmt,
		cntnTaxUprc: cntnTaxUprc,
		isNtRtnGoods: 1
	});

}
//设置数量和金额----数量（出）
function SetNums_cQty(rowid, cellname) {
	var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
	var unTaxUprc = parseFloat(rowDatas.unTaxUprc);//无税单价
	var qty = parseFloat(rowDatas.qty);//数量
	var taxRate = parseFloat(rowDatas.taxRate);//税率
	var bxRule = parseFloat(rowDatas.bxRule);//箱规

	var	bxQty = parseFloat(qty / bxRule); //箱数
	if(!isNaN(bxQty)) {
		bxQty = parseFloat(bxQty);
	} //数量
	else {
		bxQty = "";
	}
	var unTaxAmt = toDecimal(unTaxUprc * qty)//无税金额
	if(isNaN(unTaxUprc)) {
		unTaxUprc = ""
	}
	var taxAmt = toDecimal(unTaxAmt * taxRate * 0.01) //税额
	if(isNaN(taxAmt)) {
		taxAmt = ""
	}
	var cntnTaxAmt = toDecimal(unTaxAmt * 1 + taxAmt * 1)//价税合计
	if(isNaN(unTaxAmt)) {
		unTaxAmt = ""
	}
	var cntnTaxUprc = toDecimal(cntnTaxAmt / qty)//含税单价
	if(isNaN(cntnTaxUprc)) {
		cntnTaxUprc = ""
	}
	$("#jqgrids").setRowData(rowid, {
		unTaxAmt: unTaxAmt,
		taxAmt: taxAmt,
		cntnTaxAmt: cntnTaxAmt,
		cntnTaxUprc: cntnTaxUprc,
		isNtRtnGoods: 1
	});

}
//设置数量和金额----税率
function SetNums_taxRate(rowid, cellname) {
	var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
	var unTaxAmt = parseFloat(rowDatas.unTaxAmt);//无税金额
	var qty = parseFloat(rowDatas.qty);//数量
	var taxRate = parseFloat(rowDatas.taxRate);//税率

	var taxAmt = toDecimal(unTaxAmt * taxRate * 0.01) //税额
	if(isNaN(taxAmt)) {
		taxAmt = ""
	}
	var cntnTaxAmt = toDecimal(unTaxAmt * 1 + taxAmt * 1)//价税合计
	if(isNaN(unTaxAmt)) {
		unTaxAmt = ""
	}
	var cntnTaxUprc = toDecimal(cntnTaxAmt / qty)//含税单价
	if(isNaN(cntnTaxUprc)) {
		cntnTaxUprc = ""
	}
	$("#jqgrids").setRowData(rowid, {
		taxAmt: taxAmt,
		cntnTaxAmt: cntnTaxAmt,
		cntnTaxUprc: cntnTaxUprc,
		isNtRtnGoods: 1
	});

}
//设置数量和金额----无税金额
function SetNums_unTaxAmt(rowid, cellname) {
	var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
	var unTaxAmt = parseFloat(rowDatas.unTaxAmt);//无税金额
	var qty = parseFloat(rowDatas.qty);//数量
	if(isNaN(qty)) {
		qty = 0
	}
	var taxRate = parseFloat(rowDatas.taxRate);//税率
	
	var taxAmt = toDecimal(unTaxAmt * taxRate * 0.01)//税额
	if(isNaN(taxAmt)) {
		taxAmt = ""
	}
	var cntnTaxAmt = toDecimal(unTaxAmt * 1 + taxAmt * 1)//价税合计
	if(isNaN(unTaxAmt)) {
		unTaxAmt = ""
	}
	var cntnTaxUprc = toDecimal(cntnTaxAmt / qty)//含税单价
	if(isNaN(cntnTaxUprc)) {
		cntnTaxUprc = ""
	}
	var unTaxUprc = toDecimal(unTaxAmt / qty)//无税单价
	if(isNaN(unTaxUprc)) {
		unTaxUprc = ""
	}
	
	$("#jqgrids").setRowData(rowid, {
		taxAmt: taxAmt,
		cntnTaxAmt: cntnTaxAmt,
		cntnTaxUprc: cntnTaxUprc,
		unTaxUprc: unTaxUprc,
		isNtRtnGoods: 1
	});

}

//设置日期相关
function setProductDateBat(rowid) {
	var ids = $('#jqgrids').jqGrid('getGridParam', 'selrow');
	var rowDatas = $("#jqgrids").jqGrid('getRowData', ids);
	//计算生产日期
	var prdcDt = rowDatas.prdcDt //生产日期
	//保质期
	var baoZhiQiDt = rowDatas.baoZhiQi
	var invldtnDt = getNewDay1(prdcDt, baoZhiQiDt);
	if(invldtnDt == "NaN-NaN-NaN") {
		invldtnDt = "";
	} else {
		invldtnDt = invldtnDt
	};
	$("#jqgrids").setRowData(rowid, {
		invldtnDt: invldtnDt,
		baoZhiQi: baoZhiQiDt
	});
}

//失效日期
function getNewDay1(dateTemp, days) {
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
function makeCode() {		
	var elText = $("input[name='formNum']").val()  // 获取内容外部元素
	// 判断有无内容
	if (!elText) {
		console.log("无")
		return;
	}
	// 生成
	qrcode.makeCode(elText);
}
function makeCode_new() {
	qrcode.makeCode('');
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
		$("#jqgrids").setColProp("invtyEncd", {
			editable: true
		});
		$("#jqgrids").setColProp("qty", {
			editable: true
		})
		$("#jqgrids").setColProp("prdcDt", {
			editable: true
		});
		$("#jqgrids").setColProp("memos", {
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
		$("#jqgrids").setColProp("batNum", {
			editable: true
		});
		$("#jqgrids").setColProp("projEncd", {
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

		$("#gbox_jqGrids").hide();
		$("#gbox_jqgrids").show();
	});
})

//点完修改后执行的url
function SaveModifyData() {
	var listData = getJQAllData();

	var formDt = $("input[name='formDt']").val();
	var whsEncd = $("input[name='whsEncd']").val();
	//	var whsEncd = localStorage.whsEncd;
	formNum = $("input[name='formNum']").val();
	var recvSendCateId = $("input[name='recvSendCateId']").val();
	var outIntoWhsTypId = $("select[name='outIntoWhsTypId']").val();
	var memo = $("input[name='memo']").val();

	if(IsCheckValue(whsEncd, outIntoWhsTypId, listData) == true) {

		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				'formNum': formNum,
				'outIntoWhsTypId': outIntoWhsTypId,
				'formDt': formDt,
				'whsEncd': whsEncd,
				'recvSendCateId': recvSendCateId,
				'memo': memo,
				'list': listData
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			url: url + '/mis/whs/out_into_whs/updateOthOutIntoWhs',
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
					$("#jqgrids").setColProp("qty", {
						editable: false
					})
					$("#jqgrids").setColProp("prdcDt", {
						editable: false
					});
					$("#jqgrids").setColProp("memos", {
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
					$("#jqgrids").setColProp("batNum", {
						editable: false
					});
					$("#jqgrids").setColProp("projEncd", {
						editable: false
					});
					$('button').removeClass("gray")
					$('.addOrder').removeClass("gray");
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
var formNum;
//第一次保存
function SaveNewData() {
	var listData = getJQAllData();
	var formDt = $("input[name='formDt']").val();
	var whsEncd = $("input[name='whsEncd']").val();
	//	var whsEncd = localStorage.whsEncd;
	var recvSendCateId = $("input[name='recvSendCateId']").val();
	var outIntoWhsTypId = $("select[name='outIntoWhsTypId']").val();
	formNum = $("input[name='formNum']").val();
	var memo = $("input[name='memo']").val();

	//判断页面是否有值为空
	if(IsCheckValue(whsEncd, outIntoWhsTypId, listData) == true) {

		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				'formNum': "",
				'formDt': formDt,
				'whsEncd': whsEncd,
				'recvSendCateId': recvSendCateId,
				'outIntoWhsTypId': outIntoWhsTypId,
				'memo': memo,
				'list': listData
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			url: url + '/mis/whs/out_into_whs/insertOthOutIntoWhs',
			type: 'post',
			data: saveData,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			success: function(data) {
				alert(data.respHead.message)
				formNum = data.respBody.formNum;
				localStorage.setItem("formNum", formNum)
				$("input[name='formNum']").val(data.respBody.formNum); //订单编码
				if(data.respHead.isSuccess == true) {
					$("#jqgrids").setColProp("invtyEncd", {
						editable: false
					});
					$("#jqgrids").setColProp("qty", {
						editable: false
					})
					$("#jqgrids").setColProp("prdcDt", {
						editable: false
					});
					$("#jqgrids").setColProp("memos", {
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
					$("#jqgrids").setColProp("batNum", {
						editable: false
					});
					$("#jqgrids").setColProp("projEncd", {
						editable: false
					});
					$('button').removeClass("gray");
					$('.addOrder').removeClass("gray");
					$('.saveOrder').addClass("gray");
					$('.upOrder').addClass("gray");
					$('.noTo').addClass("gray");
					$('button').attr('disabled', false);
					$(".gray").attr('disabled', true)
//					chaxun()
				}
				$("#qrcode").show()
				makeCode();
			},
			error: function() {
				console.log(error);
			} //错误执行方法
		})
	}
}


function IsCheckValue(whsEncd, outIntoWhsTypId, listData) {
	if(whsEncd == "") {
		alert("仓库不能为空")
		return false;
	} else if(outIntoWhsTypId == null) {
		alert("出入库类型不能为空")
		return false;
	} else if(listData.length == 0) {
		alert("存货不能为空")
		return false;
	} else if(listData.length != 0) {
		for(var i = 0; i < listData.length; i++) {
			if(listData[i].qty == "") {
				alert("数量不能为空")
				return false;
			} else if(listData[i].batNum == "") {
				alert("批次不能为空")
				return false;
			} else if(listData[i].qty < 0) {
				alert("数量不能小于0")
				return false;
			} else if(listData[i].unTaxUprc < 0) {
				alert("无税单价不能小于0")
				return false;
			} else if(listData[i].bxQty < 0) {
				alert("箱数不能小于0")
				return false;
			} else if(listData[i].projNm == "") {
				alert("项目编码不能为空")
				return false;
			}
		}
	}
	return true;
}

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
				if(x == 0) {
					$("button").removeClass("gray");
					$(".upOrder").addClass("gray");
					$(".noTo").addClass("gray");
					$(".addOrder").removeClass("gray");
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
			alert("error")
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
				url: url + '/mis/whs/out_into_whs/deleteAllOthOutIntoWhs',
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

	//审核
	$(".toExamine").click(function() {
		if(isclick) {
			isclick = false;
			var chk1 = '/mis/whs/out_into_whs/updateOutInWhsChk';
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
			var chk2 = '/mis/whs/out_into_whs/updateOutInWhsNoChk';
			ntChk(0, chk2);
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	})

})

$(function() {
	$('.print1').click(function() {
		var formNum = $("input[name='formNum']").val()
		var outIntoWhsTypId = $("select[name='outIntoWhsTypId']").val()
		if(formNum == '') {
			alert("不存在的单据")
		} else {
			if(outIntoWhsTypId == 1 || 
			outIntoWhsTypId == 3 ||
			outIntoWhsTypId == 9 ||
			outIntoWhsTypId == 8 ||
			outIntoWhsTypId == 12) {
				localStorage.setItem("formNum",formNum)
				window.open("../../Components/whs/print_otherIn.html?1");
				
			} else if(outIntoWhsTypId == 2 || 
			outIntoWhsTypId == 4 ||
			outIntoWhsTypId == 6 ||
			outIntoWhsTypId == 7 ||
			outIntoWhsTypId == 10 ||
			outIntoWhsTypId == 11) {
				localStorage.setItem("formNum",formNum)
				window.open("../../Components/whs/print_otherOut.html?1");
			}
		}
	})
})


//查询详细信息
$(function() {

	var afterUrl = window.location.search.substring(1);
	var a = [];
	a = afterUrl;
	if(a == 2 || a == 1) {
		chaxun()
	}
})

function chaxun() {
	if(localStorage.formNum) {
		formNum = localStorage.formNum;
	} else if(localStorage.orderId) {
		formNum = localStorage.orderId;
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
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/whs/out_into_whs/query',
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
			var list1 = data.respBody;
			var list = data.respBody.list;
			if(list1.isNtChk == 1) {
				$('.noTo').removeClass("gray")
				$('.toExamine').addClass("gray")
				$('.editOrder').removeClass("gray")
				$('.addOrder').addClass("gray")
				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true);
			}else if(list1.isNtChk == 0) {
				$('.toExamine').removeClass("gray")
				$('.noTo').addClass("gray")
				$('.editOrder').removeClass("gray")
				$('.addOrder').addClass("gray")
				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true);
			}
			if(list1.outIntoWhsTypId == 12) {
				
			}
			$("input[name='formDt']").val(list1.formDt);
			$("input[name='formNum']").val(list1.formNum);
			$("input[name='whsNm']").val(list1.whsNm);
			$("input[name='recvSendCateNm']").val(list1.recvSendCateNm);
			$("input[name='recvSendCateId']").val(list1.recvSendCateId);
			$("input[name='whsEncd']").val(list1.whsEncd);
			$("input[name='recvSendCateNm']").val(list1.recvSendCateNm);
			$("input[name='outWhsEncd']").val(list1.outWhsEncd);
			$("input[name='inWhsEncd']").val(list1.inWhsEncd);
			$("input[name='outWhsNm']").val(list1.outWhsNm);
			$("input[name='inWhsNm']").val(list1.inWhsNm);
			$("#outIntoWhsTypId").val(list1.outIntoWhsTypId);
			$("#srcFormNum").val(list1.srcFormNum);
			$("input[name='memo']").val(list1.memo);

			$("#jqgrids").jqGrid('clearGridData');
			$("#jqgrids").jqGrid('setGridParam', {
				datatype: 'local',
				data: list, //newData是符合格式要求的重新加载的数据
			}).trigger("reloadGrid")
			makeCode();
			$("#qrcode").show()
			sumAmt()
			$("#jqgrids").setColProp("cntnTaxUprc", {
				editable: true
			});
			$("#jqgrids").setColProp("cntnTaxAmt", {
				editable: true
			});
			$("#jqgrids").setColProp("unTaxAmt", {
				editable: true
			});
		},
		error: function() {
			alert("error")
		}
	})
}
