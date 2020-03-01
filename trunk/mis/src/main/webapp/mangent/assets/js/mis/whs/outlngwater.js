$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	//点击表格图标显示仓库列表
	$(document).on('click', '.whs', function() {
		window.open("../../Components/baseDoc/whsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	//点击表格图标显示存货列表
	$(document).on('click', '.inv', function() {
		window.open("../../Components/baseDoc/invtyList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	//点击表格图标显示存货列表
	$(document).on('click', '.batNum_biaoge', function() {
		window.open("../../Components/baseDoc/batNum.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
//	存货分类
	$(document).on('click', '.invtyClsEncd_biaoge', function() {
		window.open("../../Components/baseDoc/invtyTree.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})

$(function() {
	pageInit();
});
var count;
var pages;
var rowNum;
function pageInit() {
	localStorage.removeItem("index")
	localStorage.removeItem("sortorder")
	allHeight()
	jQuery("#jqGrids").jqGrid({
		url: '../../assets/js/json/order.json', //组件创建完成之后请求数据的url
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['单据日期', '出入库类型', '单据号', '仓库名称', '业务类型', '客户','批次', '备注', '创建人', '审核人', '审核日期', '存货编码', '存货名称', '规格型号', '主计量单位', '入库数量', '入库单价', '出库数量', '出库单价'], //jqGrid的列显示名字
		colModel: [{
				name: "formDt",
				align: "center",
				editable: true,
			},
			{
				name: "outIntoWhsTypNm",
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: "formNum",
				align: "center",
				editable: false,
				sortable: false
			},

			{
				name: 'whsNm',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'recvSendCateNm',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'custNm',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'batNum',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'memo',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'setupPers',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'chkr',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'chkTm',
				align: "center",
				editable: false,
			},
			{
				name: 'invtyEncd',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'invtyNm',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'spcModel',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'measrCorpNm',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'intoQty',
				align: "center",
				editable: false,
			},
			{
				name: 'intoNoTaxUprc',
				align: "center",
				editable: false,
			},
			{
				name: 'outQty',
				align: "center",
				editable: false,
			},
			{
				name: 'outNoTaxUprc',
				align: "center",
				editable: false,
			}
		],
		loadonce: true,
		autowidth: true,
		height:height,
		shrinkToFit:false,
		viewrecords: true,
		rownumbers: true,
		sortable:true,
		autoScroll:true,
		forceFit: true,
		rowNum: 500,
		rowList: [500, 1000, 3000,5000],
		pager: '#jqgridPager', //表格页脚的占位符(一般是div)的id
		multiselect: true, //复选框
//		multiboxonly: true,
		caption: "出入库流水", //表格的标题名字
		cellEdit: false, //双击表格必须加
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		footerrow: true,
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
			out(index, sortorder, page)
		},
		gridComplete: function() { 
			var intoQty = $("#jqGrids").getCol('intoQty', false, 'sum');
			var outQty = $("#jqGrids").getCol('outQty', false, 'sum');
			intoQty = intoQty.toFixed(prec)
			outQty = outQty.toFixed(prec)
			$("#jqGrids").footerData('set', { 
				"formDt": "本页合计",
				intoQty: intoQty,
				outQty: outQty,
				
			}          );    
		},
		//双击弹出采购订单
		ondblClickRow: function(rowid) {
			order(rowid);
		},
		onSortCol: function(index, colindex, sortorder) {
			localStorage.setItem("index",index)
			localStorage.setItem("sortorder",sortorder)
			window.index = index;
			window.sortorder = sortorder;
			out(index, sortorder, newPage)
		}
	})
}


function order(rowid) {
	//获得行数据
	var rowDatas = $("#jqGrids").jqGrid('getRowData', rowid);
	var outIntoWhsTypNm = rowDatas.outIntoWhsTypNm
	var chkr = rowDatas.chkr;
	if(chkr){
		localStorage.setItem("isNtChk", "1");
	}else{
		localStorage.setItem("isNtChk", "0");
	}
	if(outIntoWhsTypNm == '采购入库') {
		localStorage.setItem("intoWhsSnglId", rowDatas.formNum);
		window.open("../../Components/purs/intoWhs.html?1");
	} else if(outIntoWhsTypNm == '销售出库') {
		localStorage.setItem("outWhsId", rowDatas.formNum);
		window.open("../../Components/sell/sellOutWhs.html?1");
	}else {
		localStorage.setItem("formNum", rowDatas.formNum);
		window.open("../../Components/whs/otherOutIntoWhs.html?1");
	}
}


$(function() {
	$('.momKitNm').click(function() {

		$('#purchaseOrder').hide();
		$("#insertList").hide();
		$('#whsDocList').hide();
		$("#outIntoWhsTypNmList").show();
		$("#outIntoWhsTypNmList").css("opacity", 1);
		pageInit2();
	})

	//确定
	$(".addWhs2").click(function() {
		//获得行数据 
		var ids = $("#in_jqgrids").jqGrid('getGridParam', 'selarrrow');
		var rowData = [];
		var min = [];
		for(i = 0; i < ids.length; i++) {
			var rowDatas = $("#in_jqgrids").jqGrid('getRowData', ids[i]); //获取行数据
			var dat = rowDatas.nm
			min[i] = dat
			//选中行的id
			var jstime = $("#in_jqgrids").getCell(ids[i], "id");
			rowData[i] = jstime
		}
		var rowIds = rowData.toString();
		var mins = min.join(',')
		$("input[name='outIntoWhsTypNm']").val(mins)
		$("input[name='outIntoWhsTypId']").val(rowIds)
		$("#purchaseOrder").show();
		$("#purchaseOrder").css("opacity", 1);
		$("#outIntoWhsTypNmList").hide();

	})
	//	取消
	$(".cancel2").click(function() {
		$("#purchaseOrder").show();
		$("#purchaseOrder").css("opacity", 1);
		$("#outIntoWhsTypNmList").hide();
		$("input[name='outIntoWhsTypNm1']").val('')
	})
})
$(function() {
	//2.出入类型
	$(document).on('keypress', '#outIntoWhsTypId', function(event) {
		if(event.keyCode == '13') {
			$('#outIntoWhsTypId').blur();
		}
	})

	$(document).on('blur', '#outIntoWhsTypId', function() {
		var outIntoWhsTypId = $("#outIntoWhsTypId").val();
		if(outIntoWhsTypId != '') {
			dev({
				doc1: $("#outIntoWhsTypId"),
				doc2: $("#outIntoWhsTypNm"),
				showData: {
					"outIntoWhsTypId": outIntoWhsTypId
				},
				afunction: function(data) {
					var sum = []
					var list = data.respBody.list;
					for(var i = 0; i<list.length;i++) {
						var Nm = list[i].nm
						sum.push(Nm)
					}
					var outIntoWhsTypNm = sum.toString()
					$("#outIntoWhsTypNm").val(outIntoWhsTypNm)
					if(data.respHead.isSuccess == false && outIntoWhsTypId != "") {
						alert("此仓库不存在")
						$("#outIntoWhsTypNm").val("");
						$("#outIntoWhsTypId").val("");
					}
				},
				url: url + '/mis/whs/invty_tab/outIntoWhsTyp'
			})
		}else if(outIntoWhsTypId == '') {
			$("#outIntoWhsTypNm").val("");
		}
	});
})
//出入库类型
function pageInit2() {
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"pageNo": 1,
			"pageSize": 500
		}
	};
	var postData = JSON.stringify(data);
	jQuery("#in_jqgrids").jqGrid({
		url: url + '/mis/whs/invty_tab/outIntoWhsTyp', //组件创建完成之后请求数据的url
		mtype: 'post',
		height: 400,
		ajaxGridOptions: {
			contentType: 'application/json; charset=utf-8'
		},
		postData: postData,
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['编码', '出入库类型', '单号', '来源单据号'], //jqGrid的列显示名字
		colModel: [{
				name: "id",
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: "nm",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: "fn",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: "sfn",
				align: "center",
				editable: true,
				sortable: false
			}
		],
		jsonReader: {
			root: "respBody.list",
			repeatitems: true,
			records: "respBody.count", // json中代表数据行总数的数据		            
			total: "respBody.pages", // json中代表页码总数的数据
		},
		autowidth: true,
		viewrecords: true,
		autoScroll: true,
		rownumWidth: 20, //序列号列宽度
		shrinkToFit: false,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		//		rowNum: 10,
		rowList: [10, 20, 30], //可供用户选择一页显示多少条
		//		pager: '#in_jqGridPager', //表格页脚的占位符(一般是div)的id

		multiselect: true, //复选框
		caption: "出入库类型", //表格的标题名字
		//		cellEdit:true, //双击表格必须加

	})

}

//条件查询
$(function() {
	$('.chaxun').click(function() {
		var initial = 1;
		window.newPage = initial;
		var index = '';
		var sortorder = '';
		out(index, sortorder,initial)
	})
})

function out(index, sortorder,page) {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var formDt1 = $("input[name='formDt1']").val();
	var formDt2 = $("input[name='formDt2']").val();
	var chkTm1 = $("input[name='chkTm1']").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var chkTm2 = $("input[name='chkTm2']").val();
	var formNum = $("input[name='formNum1']").val();
	var invtyNm = $("input[name='invtyNm1']").val();
	var invtyEncd = $("input[name='invtyId1']").val();
	var whsNm = $("input[name='whsNm1']").val();
	var whsEncd = $("input[name='whsEcnd1']").val();
	var batNum = $("input[name='batNum1']").val();
	var outIntoWhsTypId1 = $("input[name='outIntoWhsTypId']").val();
	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"formDt1": formDt1,
			"formDt2": formDt2,
			"chkTm1": chkTm1,
			"invtyClsEncd": invtyClsEncd,
			"chkTm2": chkTm2,
			"formNum": formNum,
			"invtyEncd": invtyEncd,
			"whsEncd": whsEncd,
			"outIntoWhsTypId": outIntoWhsTypId1,
			"batNum": batNum,
			"sort":index,
			"sortOrder":sortorder,
			"pageNo": page,
			"pageSize": rowNum,
		}
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/whs/invty_tab/outIngWaterList',
		async: true,
		data: postD2,
		dataType: 'json',
		//开始加载动画  添加到ajax里面
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");
		},
		success: function(data) {
			var list = data.respBody.list;
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
		//结束加载动画
		complete: function() {
			$(".zhezhao").css("display", "none");
			$("#loader").css("display", "none");
		},
		error: function() {
			console.log(error)
		}
	});

}

function pageInit3() {
	var myData = {};
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"pageNo": 1,
			"pageSize": 500
		}
	};
	var postData = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/whs/whs_doc/queryList',
		type: 'post',
		data: postData,
		dataType: 'json',
		async: false,
		contentType: 'application/json;charset=utf-8',
		error: function() {
			console.log(error);
		}, //错误执行方法
		success: function(data) {
			var arr = eval(data); //数组
			var list = arr.respBody.list;
			myData = list;

		}
	})
	jQuery("#whs_jqgrids").jqGrid({
		data: myData,
		datatype: "local", //请求数据返回的类型。可选json,xml,txt
		colNames: ['仓库编码', '仓库名称', '部门名称', '仓库地址', '电话', '负责人'], //jqGrid的列显示名字
		colModel: [{
				name: 'whsEncd',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: "whsNm",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: "deptName",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: "whsAddr",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'tel',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'princ',
				align: "center",
				editable: true,
				sortable: false
			}
		],
		autowidth: true,
		rownumbers: true,
		loadonce: false,
		height: 400,
		autoScroll: true,
		rownumWidth: 25, //序列号列宽度
		shrinkToFit: false,
		forceFit: true,
		rowList: [10, 20, 30], //可供用户选择一页显示多少条
		pager: '#whs_jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "仓库档案", //表格的标题名字
	})
}


var arr=[];
var obj={}
//导出
$(document).on('click', '.exportExcel', function() {
	var formDt1 = $("input[name='formDt1']").val();
	var formDt2 = $("input[name='formDt2']").val();
	var chkTm1 = $("input[name='chkTm1']").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var chkTm2 = $("input[name='chkTm2']").val();
	var formNum = $("input[name='formNum1']").val();
	var invtyNm = $("input[name='invtyNm1']").val();
	var invtyEncd = $("input[name='invtyId1']").val();
	var whsNm = $("input[name='whsNm1']").val();
	var whsEncd = $("input[name='whsEcnd1']").val();
	var batNum = $("input[name='batNum1']").val();
	var outIntoWhsTypId1 = $("input[name='outIntoWhsTypId']").val();
	
	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"formDt1": formDt1,
			"formDt2": formDt2,
			"chkTm1": chkTm1,
			"invtyClsEncd": invtyClsEncd,
			"chkTm2": chkTm2,
			"formNum": formNum,
			"invtyEncd": invtyEncd,
			"whsEncd": whsEncd,
			"outIntoWhsTypId": outIntoWhsTypId1,
			"batNum": batNum,
		}
	};
	var Data = JSON.stringify(data2);
	console.log(Data)
	$.ajax({
		url: url3 + "/mis/whs/invty_tab/outIngWaterList",
		type: 'post',
		data: Data,
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
			var execlName = '出入库流水'
			ExportData(list, execlName)
		},
		error: function() {
			console.log(error)
		}
	})
	
})