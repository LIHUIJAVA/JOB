var count;
var pages;
var page = 1;
var rowNum;
$(function() {
	$(document).on('click', '.invtyNm_biaoge', function() {
//		localStorage.setItem("outInEncd", 1)
		window.open("../../Components/baseDoc/invtyList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	$(document).on('click', '.invtyClsNm_biaoge', function() {
//		localStorage.setItem("outInEncd", 1)
		window.open("../../Components/baseDoc/invtyTree.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
});
$(function() {
	pageInit();
	pageInit1();
	$(".addWhes").addClass("gray")
	$(".addWhes").attr("disabled",true)
});

function pageInit() {
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	jQuery("#book_jqGrids").jqGrid({
		url: '../../assets/js/json/order.json', //组件创建完成之后请求数据的url
		height: 200,
		ajaxGridOptions: {
			contentType: 'application/json; charset=utf-8'
		},
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['存货分类编码','存货分类名称', '存货编码', '存货名称', '规格型号', '存货代码', '计量单位', '库存单位', '安全库存', '最高库存', '最低库存', '代管供应商'], //jqGrid的列显示名字
		colModel: [{
				name: "invtyClsEncd",
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: "invtyClsNm",
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: "invtyEncd",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: "invtyNm",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'spcModel',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'invtyCd',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'measrCorpNm',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: '库存单位',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: '安全库存',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: '最高库存',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: '最低库存',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: '代管供应商',
				align: "center",
				editable: true,
				sortable: false
			}
		],
		autowidth: true,
		viewrecords: true,
		rownumbers: true,
		loadonce: true,
		autoScroll: true,
		sortable:true,
		shrinkToFit: false,
		forceFit: true,
		rowNum: 500, //一页显示多少条
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条	
		pager: '#jqgridPager', //表格页脚的占位符(一般是div)的id
		multiboxonly: true,
		multiselect: true, //复选框
		caption: "存货", //表格的标题名字
//		ondblClickRow: function() {
//			order();
//		},
		onSelectRow: function() {
			searchAll();
		},
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#book_jqGrids").getGridParam('records'); //获取返回的记录数
			page = $("#book_jqGrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#book_jqGrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			search()
		},
	})
		//宽度根据窗口大小自适应
	$(function() {
		$(window).resize(function() {
			$("#book_jqGrids").setGridWidth($(window).width());
		});
	});
}

//条件详细查询
function searchAll(index, sortorder) {
	var myDate = {};
	//获得行号
	var gr = $("#book_jqGrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowDatas = $("#book_jqGrids").jqGrid('getRowData', gr);
	var invtyEncd = rowDatas.invtyEncd;
	var formDt1 = $(".formDt1").val();
	var whsEncd = $("input[name='whsEncd']").val();
	var formDt2 = $(".formDt2").val();
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"formDt1": formDt1,
			"formDt2": formDt2,
			'invtyEncd': invtyEncd,
			'whsEncd': whsEncd,
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
		url: url + '/mis/whs/invty_tab/InvtyStandList',
		async: true,
		data: saveData,
		dataType: 'json',
		//开始加载动画  添加到ajax里面
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");

		},
		success: function(data) {
			var mydata = {};
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#whs_jqgrids").jqGrid("clearGridData");
			$("#whs_jqgrids").jqGrid("setGridParam", {
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

function pageInit1() {
	localStorage.removeItem("index")
	localStorage.removeItem("sortorder")
	jQuery("#whs_jqgrids").jqGrid({
		url: '../../assets/js/json/order.json', //组件创建完成之后请求数据的url
		height: 200,
		ajaxGridOptions: {
			contentType: 'application/json; charset=utf-8'
		},
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['单据日期','审核日期', '单据号', '仓库名称', '单据类型', '收入数量', '发出数量', '结存数量'], //jqGrid的列显示名字
		colModel: [{
				name: "formDt",
				align: "center",
				editable: true,
			},
			{
				name: "chkTm",
				align: "center",
				editable: true,
			},
			{
				name: "formNum",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: "whsNm",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'formType',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'intoQty',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'outQty',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'balance',
				align: "center",
				editable: true,
				sortable: false
			}
		],
		autowidth: true,
		viewrecords: true,
		rownumbers: true,
		loadonce: true,
		autoScroll: true,
		sortable:true,
		shrinkToFit: false,
		forceFit: true,
		rowNum: 500, //一页显示多少条
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条	
		pager: '#whs_jqGridPager', //表格页脚的占位符(一般是div)的id
//		multiboxonly: true,
		multiselect: true, //复选框
		caption: "库存台账", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#whs_jqgrids").getGridParam('records'); //获取返回的记录数
			page = $("#whs_jqgrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#whs_jqgrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			searchAll(index, sortorder)
		},
		footerrow: true,
		gridComplete: function() { 
			var intoQty = $("#whs_jqgrids").getCol('intoQty', false, 'sum');
			var outQty = $("#whs_jqgrids").getCol('outQty', false, 'sum');
			var balance = $("#whs_jqgrids").getCol('balance', false, 'sum');
			
			$("#whs_jqgrids").footerData('set', { 
				"formDt": "本页合计",
				intoQty: intoQty.toFixed(prec),
				outQty:outQty.toFixed(prec),
				balance:balance.toFixed(prec),
				
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
			searchAll(index, sortorder)      
		}
	})
	//宽度根据窗口大小自适应
	$(function() {
		$(window).resize(function() {
			$("#whs_jqgrids").setGridWidth($(window).width());
		});
	});
}
function order(rowid) {
	//获得行数据
	var rowDatas = $("#whs_jqgrids").jqGrid('getRowData', rowid);
	var formType = rowDatas.formType
	if(formType == '采购入库') {
		localStorage.setItem("intoWhsSnglId", rowDatas.formNum);
		window.open("../../Components/purs/intoWhs.html?1");
	} else if(formType == '销售出库') {
		localStorage.setItem("outWhsId", rowDatas.formNum);
		window.open("../../Components/sell/sellOutWhs.html?1");
	} else if(formType == '期初') {
		return;
	}else {
		localStorage.setItem("formNum", rowDatas.formNum);
		window.open("../../Components/whs/otherOutIntoWhs.html?1");
	}
}
function getJQAllData() {
	//拿到grid对象
	var obj = $("#whs_jqgrids");
	//获取grid表中所有的rowid值
	var rowIds = obj.getDataIDs();
	//初始化一个数组arrayData容器，用来存放rowData
	var arrayData = new Array();
	if(rowIds.length > 0) {
		for(var i = 0; i < rowIds.length; i++) {
//			if(obj.getRowData(rowIds[i]).whsNm == "") {
//				continue;
//			} else {
				arrayData.push(obj.getRowData(rowIds[i]));
//			}
		}
	}
	return arrayData;
}


//存货查询按钮
$(document).on('click', '#find', function() {
	search()
})

function search() {
	var myDate = {};
	var invtyEncd = $("input[name='invtyEncd']").val();
	var invtyCls = $("input[name='invtyCls']").val();
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			'invtyClsEncd': invtyCls,
			'invtyEncd': invtyEncd,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/purc/InvtyDoc/selectInvtyDocList',
		async: true,
		data: saveData,
		dataType: 'json',
		//开始加载动画  添加到ajax里面
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");

		},
		success: function(data) {
			var list = data.respBody.list;

			var arr1 = [];
			var length = list.length;
			for(var i = 0; i < length; i++) {
				const arr = { ...list[i],
					...list[i].invtyCls
				}
				arr1.push(arr);
			}

			myDate = arr1;
			myDate.rows = data.respBody.list;
			myDate.page = page;
			myDate.records = data.respBody.count;
			myDate.total = data.respBody.pages;
			$("#book_jqGrids").jqGrid("clearGridData");
			$("#book_jqGrids").jqGrid("setGridParam", {
				data: myDate.rows,
				localReader: {
					root: function(object) {
						return myDate.rows;
					},
					page: function(object) {
						return myDate.page;
					},
					total: function(object) {
						return myDate.total;
					},
					records: function(object) {
						return myDate.records;
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


var arr=[];
var obj={}
//导出
$(document).on('click', '.exportExcel', function() {
	//获得行号
	var gr = $("#book_jqGrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowDatas = $("#book_jqGrids").jqGrid('getRowData', gr);
	var invtyEncd = rowDatas.invtyEncd;
	var formDt1 = $(".formDt1").val();
	var formDt2 = $(".formDt2").val();
	if(invtyEncd == undefined) {
		alert("未选择单据")
	} else{
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				"formDt1": formDt1,
				"formDt2": formDt2,
				'invtyEncd': invtyEncd,
			}
		};
		var Data = JSON.stringify(data);
		$.ajax({
			url: url3 + "/mis/whs/invty_tab/InvtyStandList",
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
			var execlName = '库存台账'
			ExportData(list, execlName)
			},
			error: function() {
				console.log(error)
			}
		})
	}
})