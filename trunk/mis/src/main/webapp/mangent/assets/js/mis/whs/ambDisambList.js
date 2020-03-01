//组装、拆卸列表
var count;
var pages;
var rowNum;
//表格初始化
$(function() {
	localStorage.removeItem("index")
	localStorage.removeItem("sortorder")
	allHeight();
	$("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: [
		'母配套件编码','单据号', '单据日期', '单据类型(组装、拆卸)', '费用(总的)', '费用说明', '表头备注', '类型(套件、散件)', '仓库编码', '母件数量',
		'税率', '主表税额', '含税单价', '未税单价', '含税金额', '未税金额', '母配套件批次', '生产日期', '保质期', '失效日期',
		'是否向WMS上传', '是否审核', '是否记账', '是否完成', '是否关闭', '打印次数', '创建人', '创建时间', '修改人',
		'修改时间', '审核人', '审核时间', '记账人', '记账时间', '存货名称', '规格型号', '存货代码', '表头箱规',
		'表头箱数', '保质期天数', '计量单位名称', '仓库名称', '对应条形码', '单据类型编码', '单据类型名称', '序号', '子配套件编码',
		'仓库编码', '子件数量', '母子比例', '税率', '子表税额', '含税单价', '未税单价', '含税金额', '未税金额', '子配套件批次',
		'生产日期', '保质期', '失效日期', '存货名称', '规格型号', '存货代码', '表体箱规', '表体箱数', '保质期天数',
		'计量单位名称', '仓库名称', '对应条形码'
		],
		colModel: [{
				name: 'momKitEncd',
				editable: true,
				align: 'center',
				sortable: false
			},{
				name: 'formNum',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'formDt',
				editable: true,
				align: 'center',
			},
			{
				name: 'formTyp', //
				editable: false,
				align: 'center',
				sortable: false,
			},
			{
				name: 'fee',
				editable: false,
				align: 'center'
			},
			{
				name: 'feeComnt',
				editable: false,
				sortable: false,
				align: 'center'
			},
			{
				name: 'memo',
				editable: false,
				sortable: false,
				align: 'center'
			},
			{
				name: 'typ',
				editable: false,
				sortable: false,
				align: 'center'
			},
			{
				name: 'whsEncd',
				editable: false,
				sortable: false,
				align: 'center'
			},
			{
				name: 'momQty',
				editable: true,
				align: 'center',
			},
			{
				name: 'taxRate',
				editable: true,
				sortable: false,
				align: 'center',
			},
			{
				name: 'mtaxAmt',
				editable: true,
				sortable: false,
				align: 'center',
			},
			{
				name: 'mcntnTaxUprc',
				editable: true,
				align: 'center',
			},
			{
				name: 'munTaxUprc',
				editable: true,
				align: 'center',
			},
			{
				name: 'mcntnTaxAmt',
				editable: true,
				align: 'center',
			},
			{
				name: 'munTaxAmt',
				editable: true,
				align: 'center',
			},
			{
				name: 'mbatNum',
				editable: true,
				sortable: false,
				align: 'center',
			},
			{
				name: 'mprdcDt',
				editable: true,
				align: 'center',
			},
			{
				name: 'baoZhiQi',
				editable: true,
				sortable: false,
				align: 'center',
			},
			{
				name: 'invldtnDt',
				editable: true,
				align: 'center',
			},
			{
				name: 'isNtWms',
				sortable: false,
				editable: true,
				align: 'center',
				hidden:true
			},
			{
				name: 'isNtChk',
				editable: true,
				sortable: false,
				align: 'center',
			},
			{
				name: 'isNtBookEntry',
				editable: true,
				sortable: false,
				align: 'center',
				hidden:true
			},
			{
				name: 'isNtCmplt',
				editable: true,
				sortable: false,
				align: 'center',
				hidden:true
			},
			{
				name: 'isNtClos',
				editable: true,
				sortable: false,
				align: 'center',
				hidden:true
			},
			{
				name: 'printCnt',
				editable: true,
				align: 'center',
			},
			{
				name: 'setupPers',
				editable: true,
				sortable: false,
				align: 'center',
			},
			{
				name: 'setupTm',
				editable: true,
				align: 'center',
			},
			{
				name: 'mdfr',
				editable: true,
				sortable: false,
				align: 'center',
			},
			{
				name: 'modiTm',
				editable: true,
				align: 'center',
			},
			{
				name: 'chkr',
				editable: true,
				sortable: false,
				align: 'center',
			},
			{
				name: 'chkTm',
				editable: true,
				align: 'center',
			},
			{
				name: 'bookEntryPers',
				editable: true,
				sortable: false,
				align: 'center',
				hidden:true
			},
			{
				name: 'bookEntryTm',
				editable: true,
				align: 'center',
				hidden:true
			},
			{
				name: 'invtyNm',
				editable: true,
				sortable: false,
				align: 'center',
			},
			{
				name: 'spcModel',
				sortable: false,
				editable: true,
				align: 'center',
			},
			{
				name: 'invtyCd',
				sortable: false,
				editable: true,
				align: 'center',
			},
			{
				name: 'bxRule',
				editable: true,
				sortable: false,
				align: 'center',
			},
			{
				name: 'bxQty',
				editable: true,
				align: 'center',
			},
			{
				name: 'baoZhiQiDt',
				editable: true,
				align: 'center',
				sortable: false,
				hidden:true
			},
			{
				name: 'measrCorpNm',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'whsNm',
				editable: true,
				sortable: false,
				align: 'center',
			},
			{
				name: 'crspdBarCd',
				editable: true,
				sortable: false,
				align: 'center',
			},
			{
				name: 'formTypEncd',
				editable: true,
				sortable: false,
				align: 'center',
			},
			{
				name: 'formTypName',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'ordrNum',
				editable: true,
				sortable: false,
				align: 'center',
				hidden:true
			},
			{
				name: 'subKitEncd',
				editable: true,
				sortable: false,
				align: 'center',
			},
			{
				name: 'swhsEncd',
				editable: true,
				sortable: false,
				align: 'center',
			},
			{
				name: 'subQty',
				editable: true,
				align: 'center',
			},
			{
				name: 'momSubRatio',
				editable: true,
				sortable: false,
				align: 'center',
			},
			{
				name: 'staxRate',
				editable: true,
				sortable: false,
				align: 'center',
			},
			{
				name: 'staxAmt',
				editable: true,
				sortable: false,
				align: 'center',
			},
			{
				name: 'scntnTaxUprc',
				editable: true,
				align: 'center',
			},
			{
				name: 'sunTaxUprc',
				editable: true,
				align: 'center',
			},
			{
				name: 'scntnTaxAmt',
				editable: true,
				align: 'center',
			},
			{
				name: 'sunTaxAmt',
				editable: true,
				align: 'center',
			},
			{
				name: 'sbatNum',
				editable: true,
				sortable: false,
				align: 'center',
			},
			{
				name: 'sprdcDt',
				editable: true,
				align: 'center',
			},
			{
				name: 'sbaoZhiQi',
				editable: true,
				sortable: false,
				align: 'center',
			},
			{
				name: 'sinvldtnDt',
				editable: true,
				align: 'center',
			},
			{
				name: 'sinvtyNm',
				sortable: false,
				editable: true,
				align: 'center',
			},
			{
				name: 'sspcModel',
				editable: true,
				sortable: false,
				align: 'center',
			},
			{
				name: 'sinvtyCd',
				sortable: false,
				editable: true,
				align: 'center',
			},
			{
				name: 'sbxRule',
				editable: true,
				sortable: false,
				align: 'center',
			},
			{
				name: 'sbxQty',
				editable: true,
				align: 'center',
			},
			{
				name: 'sbaoZhiQiDt',
				editable: true,
				align: 'center',
				sortable: false,
				hidden:true
			},
			{
				name: 'smeasrCorpNm',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'swhsNm',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'scrspdBarCd',
				editable: true,
				align: 'center',
				sortable: false
			},
		],
		autowidth: true,
		height:height,
		autoScroll:true,
		sortable:true,
		shrinkToFit:false,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 500, //一页显示多少条
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条	
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
//		multiboxonly: true,
		caption: "组装拆卸单列表", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#jqGrids").getGridParam('records'); //获取返回的记录数
			page = $("#jqGrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#jqGrids").getGridParam('rowNum'); //获取显示配置记录数量

			rowNum = parseInt(rowNum1)
			var total = Math.ceil(records / rowNum); //$("#jqGrid").getGridParam('total');//获取总页数
			if(pageBtn === "next" && page < total) {
				page = parseInt(page) + 1;
			}
			if(pageBtn === "prev" && page > 1) {
				page = parseInt(page) - 1;
			}
			if(pageBtn === "last") {
				page = total;
			}
			if(pageBtn === "first") {
				page = 1;
			}
			var index = localStorage.getItem("index")
			var sortorder = localStorage.getItem("sortorder")
			if(index == null) {
				var index = ''
			}
			if(sortorder == null) {
				var sortorder = ''
			}
			window.newPage = page;
			search(index, sortorder, page)
		},
		ondblClickRow: function(rowid) {
			order(rowid);
		},
		footerrow: true,
		gridComplete: function() { 
			var fee = $("#jqGrids").getCol('fee', false, 'sum');
			var momQty = $("#jqGrids").getCol('momQty', false, 'sum');
			var mcntnTaxAmt = $("#jqGrids").getCol('mcntnTaxAmt', false, 'sum');
			var munTaxAmt = $("#jqGrids").getCol('munTaxAmt', false, 'sum');
			var bxQty = $("#jqGrids").getCol('bxQty', false, 'sum');
			var subQty = $("#jqGrids").getCol('subQty', false, 'sum');
			var scntnTaxAmt = $("#jqGrids").getCol('scntnTaxAmt', false, 'sum');
			var sunTaxAmt = $("#jqGrids").getCol('sunTaxAmt', false, 'sum');
			var sbxQty = $("#jqGrids").getCol('sbxQty', false, 'sum');
		
			$("#jqGrids").footerData('set', { 
				"momKitEncd": "本页合计",
				fee : precision(fee,2),
				mcntnTaxAmt : precision(mcntnTaxAmt,2),
				munTaxAmt : precision(munTaxAmt,2),
				scntnTaxAmt : precision(scntnTaxAmt,2),
				sunTaxAmt :precision(sunTaxAmt,2),
				momQty: momQty.toFixed(prec),
				bxQty:bxQty.toFixed(prec),
				subQty: subQty.toFixed(prec),
				sbxQty: sbxQty.toFixed(prec),
				
			}          );    
		},
		onSortCol: function(index, colindex, sortorder) {
			switch(index) {
				case "formDt":
					index = "amb_disamb_sngls.form_dt"
					break;
				case "fee":
					index = "amb_disamb_sngls.fee"
					break;
				case "momQty":
					index = "amb_disamb_sngls.mom_qty"
					break;
				case "mcntnTaxUprc":
					index = "amb_disamb_sngls.mcntn_tax_uprc"
					break;
				case "munTaxUprc":
					index = "amb_disamb_sngls.mun_tax_uprc"
					break;
				case "mcntnTaxAmt":
					index = "amb_disamb_sngls.mcntn_tax_amt"
					break;
				case "munTaxAmt":
					index = "amb_disamb_sngls.mun_tax_amt"
					break;
				case "mprdcDt":
					index = "amb_disamb_sngls.mprdc_dt"
					break;
				case "invldtnDt":
					index = "amb_disamb_sngls.invldtn_dt"
					break;
				case "printCnt":
					index = "amb_disamb_sngls.print_cnt"
					break;
				case "setupTm":
					index = "amb_disamb_sngls.setup_tm"
					break;
				case "modiTm":
					index = "amb_disamb_sngls.modi_tm"
					break;
				case "chkTm":
					index = "amb_disamb_sngls.chk_tm"
					break;
				case "bxQty":
					index = "amb_disamb_sngls.bx_qty"
					break;
				case "subQty":
					index = "amb_disamb_sngl_sub_tab.sub_qty"
					break;
				case "scntnTaxUprc":
					index = "amb_disamb_sngl_sub_tab.scntn_tax_uprc"
					break;
				case "sunTaxUprc":
					index = "amb_disamb_sngl_sub_tab.sun_tax_uprc"
					break;
				case "scntnTaxAmt":
					index = "amb_disamb_sngl_sub_tab.scntn_tax_amt"
					break;
				case "sunTaxAmt":
					index = "amb_disamb_sngl_sub_tab.sun_tax_amt"
					break;
				case "sprdcDt":
					index = "amb_disamb_sngl_sub_tab.sprdc_dt"
					break;
				case "sinvldtnDt":
					index = "amb_disamb_sngl_sub_tab.sinvldtn_dt"
					break;
				case "sbxQty":
					index = "amb_disamb_sngl_sub_tab.sbx_qty"
					break;
			}
			localStorage.setItem("index",index)
			localStorage.setItem("sortorder",sortorder)
			window.index = index;
			window.sortorder = sortorder;
			search(index, sortorder, newPage)   
		}
	})
})

//整单联查
$(document).on('click', '.joint_search', function() {
	var gr = $("#jqGrids").jqGrid('getGridParam', 'selrow');
	if(gr == null) {
		alert("请选择单据")
	} else {
		//获得行数据
		var rowDatas = $("#jqGrids").jqGrid('getRowData', gr);
		var formNum = rowDatas.formNum;
		var formTypEncd = rowDatas.formTypEncd;
		window.open("../../Components/system/jointSearch.html?formNum=" + formNum + "&formTypEncd=" + formTypEncd, 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	}
})

//点击表格图标显示仓库列表
$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	$(document).on('click', '.whsNm', function() {
//		a = 1;
		localStorage.setItem("outInEncd", 0);
		window.open("../../Components/baseDoc/whsList.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
//	存货分类
	$(document).on('click', '.invtyClsEncd_biaoge', function() {
		window.open("../../Components/baseDoc/invtyTree.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	//批次
	$(document).on('click', '.biao_sbatNum', function() {
		window.open("../../Components/baseDoc/sbatNum.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	})
})
//查询按钮
$(document).on('click', '.search', function() {
	var initial = 1;
	window.newPage = initial;
	var index = '';
	var sortorder = '';
	search(index, sortorder,initial)
})

function search(index, sortorder, page){
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var formNum = $(".formNum").val();
	var formTyp = $(".formTyp").val();
	var formDt1 = $(".cannibDt1").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var sbatNum = $("input[name='sbatNum']").val();
	var formDt2 = $(".cannibDt2").val();
	var isNtChk = $("#isNtChk").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var batNum = $("input[name='batNum']").val();
	var memo = $("input[name='memo']").val();
	var whsEncd = $("input[name='whsEncd']").val()
	var setupPers = $("input[name='userName']").val()
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"formNum": formNum,
			"whsEncd": whsEncd,
			"formTyp": formTyp,
			"memo": memo,
			"formDt1": formDt1,
			"sbatNum": sbatNum,
			"invtyClsEncd": invtyClsEncd,
			"formDt2": formDt2,
			'isNtChk':isNtChk,
			"invtyEncd": invtyEncd,
			"batNum": batNum,
			"setupPers": setupPers,
			"sort":index,
			"sortOrder":sortorder,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/whs/amb_disamb_sngl/queryList',
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
			var list = data.respBody.list;
			for(var i = 0;i<list.length;i++){
				if(list[i].isNtChk == 0) {
					list[i].isNtChk = "否"
				} else if(list[i].isNtChk == 1) {
					list[i].isNtChk = "是"
				}
				if(list[i].isNtWms == 0) {
					list[i].isNtWms = "否"
				} else if(list[i].isNtWms == 1) {
					list[i].isNtWms = "是"
				}
				if(list[i].isNtBookEntry == 0) {
					list[i].isNtBookEntry = "否"
				} else if(list[i].isNtBookEntry == 1) {
					list[i].isNtBookEntry = "是"
				}
				if(list[i].isNtCmplt == 0) {
					list[i].isNtCmplt = "否"
				} else if(list[i].isNtCmplt == 1) {
					list[i].isNtCmplt = "是"
				}
				if(list[i].isNtClos == 0) {
					list[i].isNtClos = "否"
				} else if(list[i].isNtClos == 1) {
					list[i].isNtClos = "是"
				}	
			}
			var mydata = {};
			mydata.rows = list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#jqGrids").jqGrid("clearGridData");
			$("#jqGrids").jqGrid("setGridParam", {
				data: mydata.rows,
				localReader: {
					root: function(object) {
						return mydata.rows;
					},
					page: function(object) {
						return mydata.page;
					},
					total: function(object) {
						return mydata.total;
					},
					records: function(object) {
						return mydata.records;
					},
					repeatitems: false
				}
			}).trigger("reloadGrid");
		},
		error: function() {
			alert("查询失败")
		}
	});

}

function order(rowid) {
	//获得行数据
	var rowDatas = $("#jqGrids").jqGrid('getRowData', rowid);
	var formTyp = rowDatas.formTyp
	console.log(rowDatas.formTyp)
	if(formTyp == "拆卸") {
		localStorage.setItem("formNum", rowDatas.formNum);
		localStorage.setItem("isNtChk", rowDatas.isNtChk);
		window.open("../../Components/whs/ambDisambC.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');
	}else if(formTyp == "组装") {
		localStorage.setItem("formNum", rowDatas.formNum);
		localStorage.setItem("isNtChk", rowDatas.isNtChk);
		window.open("../../Components/whs/ambDisambZ.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');
	}
	
}


var chk1 = '/mis/whs/amb_disamb_sngl/updateASnglChk';
var chk2 = '/mis/whs/amb_disamb_sngl/updateASnglNoChk';

function ntChk(x,chk) {
	//获得选中行的行号
	var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
	//获取选择行的provrId
	//对象数组
	var rowData = [];
	for(var i = 0; i < ids.length; i++) {
		var obj = {}; //对象
		//选中行的id
		//把选中行的i添加到数据对象中
		obj.formNum = $("#jqGrids").getCell(ids[i], "formNum").toString();
		obj.isNtChk = x;
		//建一个数组，把单个对象添加到数组中
		rowData[i] = obj;
	}
	if(rowData.length == 0) {
		alert("请选择单据!")
	} else {
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				"list": rowData
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
				if(data.respHead.isSuccess==true){
					var initial = 1;
					window.newPage = initial;
					var index = '';
					var sortorder = '';
					search(index, sortorder,initial)
				}
			},
			error: function() {
				alert("审核失败")
			}
		})
	}

}
var isclick = true;
//审核与弃审
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

//删除
$(function() {
	$(".delOrder").click(function() {
		//获得选中行的行号
		var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
		//获取选择行的provrId
		var rowData = [];
		for(var i = 0; i < ids.length; i++) {
			var data = $("#jqGrids").getCell(ids[i], "formNum");
			//建一个数组，把选中行的id添加到这个数组中去。
			rowData[i] = data;
		}

		if(rowData.length == 0) {
			alert("请选择单据!")
		} else if(confirm("确定删除？")) {
			var formNum = rowData.toString();
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"formNum": formNum,
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/whs/amb_disamb_sngl/deleteAllAmbDisambSngl',
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
					if(data.respHead.isSuccess==true){
						var initial = 1;
						window.newPage = initial;
						var index = '';
						var sortorder = '';
						search(index, sortorder,initial)
					}
				},
				error: function() {
					alert("删除失败")
				}
			})
		}
	})
})
// 导入
$(document).ready(function(){
    $('.dropdown-menu li').click(function(){
        var rs=$(this).text();
        $('#dropdownMenu2').text(rs);
    });
    //  	alert("U8导入")
    $('.dropdown-menu .li1').click(function(){
    	var files = $("#FileUpload").val()
    	var fileObj = document.getElementById("FileUpload").files[0];
        var formFile = new FormData();
       	formFile.append("action", "UploadVMKImagePath");  
       	formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
        var data = formFile;
        if(files != "") {
        	$.ajax({
	            type: 'post',
	            url: url + "/mis/whs/amb_disamb_sngl/uploadFileAddDbU8",
	            data:data,
	          	dataType: "json",
	           	cache: false,//上传文件无需缓存
	           	processData: false,//用于对data参数进行序列化处理 这里必须false
	           	contentType: false, //必须
	           	success: function(data) {
	           		alert(data.respHead.message)
	           	},
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
	        });
        } else {
        	alert("请选择文件")
        }   
    });
    //  	alert("新系统导入")
    $('.dropdown-menu .li2').click(function(){
    	var files = $("#FileUpload").val()
    	var fileObj = document.getElementById("FileUpload").files[0];
        var formFile = new FormData();
       	formFile.append("action", "UploadVMKImagePath");  
       	formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
        var data = formFile;
        if(files != "") {
        	$.ajax({
	            type: 'post',
	            url: url + "/mis/whs/amb_disamb_sngl/uploadFileAddDb",
	            data:data,
	          	dataType: "json",
	           	cache: false,//上传文件无需缓存
	           	processData: false,//用于对data参数进行序列化处理 这里必须false
	           	contentType: false, //必须
	           	success: function(data) {
	           		alert(data.respHead.message)
	           	}
	        });
        } else {
        	alert("请选择文件")
        }   
    });
});

var arr=[];
var obj={}
//导出
$(document).on('click', '.exportExcel', function() {
	var formNum = $(".formNum").val();
	var formTyp = $(".formTyp").val();
	var formDt1 = $(".cannibDt1").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var sbatNum = $("input[name='sbatNum']").val();
	var formDt2 = $(".cannibDt2").val();
	var isNtChk = $("#isNtChk").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var batNum = $("input[name='batNum']").val();
	var whsEncd = $("input[name='whsEncd']").val()
	var setupPers = $("input[name='userName']").val()
	
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"formNum": formNum,
			"whsEncd": whsEncd,
			"formTyp": formTyp,
			"formDt1": formDt1,
			"sbatNum": sbatNum,
			"invtyClsEncd": invtyClsEncd,
			"formDt2": formDt2,
			'isNtChk':isNtChk,
			"invtyEncd": invtyEncd,
			"batNum": batNum,
			"setupPers": setupPers,
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		url: url + '/mis/whs/amb_disamb_sngl/queryListDaYin',
		type: 'post',
		data: saveData,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		beforeSend: function() {
			$("#mengban").css("display", "block");
			$("#loader").css("display", "block");
		},
		complete: function() {
			$("#mengban").css("display", "none");
			$("#loader").css("display", "none");
		},
		success: function(data) {
			var list = data.respBody.list;
			var execlName = '组装拆卸单'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
	
})