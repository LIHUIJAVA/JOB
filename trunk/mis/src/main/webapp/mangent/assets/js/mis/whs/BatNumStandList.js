var count;
var pages;
var page = 1;
var rowNum;
//点击表格图标显示仓库列表
$(function() {
//	仓库
	$(document).on('click', '.whsEncd_biaoge', function() {
		window.open("../../Components/baseDoc/whsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
//	存货编码
	$(document).on('click', '.invtyEncd_biaoge', function() {
		window.open("../../Components/baseDoc/invtyList.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
//	批号
	$(document).on('click', '.batNum_biaoge', function() {
		window.open("../../Components/baseDoc/batNum.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
//	存货分类
	$(document).on('click', '.invtyClsEncd_biaoge', function() {
		window.open("../../Components/baseDoc/invtyTree.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})
$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	pageInit();
	pageInitALL();
});

//查询按钮
$(document).on('click', '#find', function() {
	search()
})

//条件查询
function search() {
	var invtyCls = $("input[name='invtyCls']").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var whsEncd = $("input[name='whsEncd']").val();
	var batNum = $("input[name='batNum']").val();
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
		var data2 = {
			"reqHead": reqhead,
			"reqBody": {
				"invtyClsEncd": invtyCls,
				"invtyEncd": invtyEncd,
				"whsEncd": whsEncd,
				"batNum": batNum,
				"pageNo": page,
				"pageSize": rowNum
			}
		};
		var postD2 = JSON.stringify(data2);
		$.ajax({
			type: "post",
			contentType: 'application/json; charset=utf-8',
			url: url + '/mis/whs/invty_tab/queryInvtyTabList',
			async: true,
			data: postD2,
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
				$("#batn_jqGrids").jqGrid("clearGridData");
				$("#batn_jqGrids").jqGrid("setGridParam", {
					data: mydata.rows,
					jsonReader: {
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
				alert("查询失败")
			}
		});
//	}

}

//条件详细查询
function searchAll(index, sortorder) {
	
	var ids = $("#batn_jqGrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowData = $("#batn_jqGrids").jqGrid('getRowData', ids);
	var formDt1 = $("input[name='formDt1']").val();
	var formDt2 = $("input[name='formDt2']").val();
	var whsEncd = $("input[name='whsEncd']").val();
//	var chkTm1 = $("input[name='chkTm1']").val();
//	var chkTm2 = $("input[name='chkTm2']").val();
	var invtyEncd = rowData.invtyEncd
	var batNum = rowData.batNum
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": invtyEncd,
			"batNum": batNum,
			"formDt1": formDt1,
			"formDt2": formDt2,
			"whsEncd": whsEncd,
			"sort":index,
			"sortOrder":sortorder,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/whs/invty_tab/selectBatNumStandList',
		async: true,
		data: postD2,
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
			$("#bat_jqgrids").jqGrid("clearGridData");
			$("#bat_jqgrids").jqGrid("setGridParam", {
				data: mydata.rows,
				jsonReader: {
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
			alert("查询失败")
		}
	});

}


function pageInit() {
//	allHeight()
	jQuery("#batn_jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['存货名称', '存货编码', '批次'], //jqGrid的列显示名字
		colModel: [{
				name: 'invtyNm',
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
				name: 'batNum',
				align: "center",
				editable: true,
				sortable: false
			}
		],
		autowidth: true,
		rownumbers: true,
		autoScroll:true,
		shrinkToFit:false,
		rownumWidth: 15,  //序列号列宽度
		height:200,
		loadonce: false,
		forceFit: true,
		rowNum: 500, //一页显示多少条
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条	
		pager: '#batn_jqgridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "库存批次查询", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#batn_jqGrids").getGridParam('records'); //获取返回的记录数
			page = $("#batn_jqGrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#batn_jqGrids").getGridParam('rowNum'); //获取显示配置记录数量

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
		onSelectRow: function() {
			searchAll();
		},
	})
//	search()
}
function pageInitALL() {
	localStorage.removeItem("index")
	localStorage.removeItem("sortorder")
//	allHeight()
	jQuery("#bat_jqgrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['单据日期', '审核日期', '单据号', '仓库名称', '单据类型', '收入数量', '发出数量', '结存数量'], //jqGrid的列显示名字
		colModel: [{
				name: 'formDt',
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
		rownumbers: true,
		autoScroll:true,
		shrinkToFit:false,
		height:200,
		loadonce: false,
		forceFit: true,
		rowNum: 500, //一页显示多少条
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条	
		pager: '#bat_jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "批次库存台账查询", //表格的标题名字
		footerrow: true,
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#bat_jqgrids").getGridParam('records'); //获取返回的记录数
			page = $("#bat_jqgrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#bat_jqgrids").getGridParam('rowNum'); //获取显示配置记录数量

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
		gridComplete: function() { 
			var intoQty = $("#bat_jqgrids").getCol('intoQty', false, 'sum');
			var outQty = $("#bat_jqgrids").getCol('outQty', false, 'sum');
			var balance = $("#bat_jqgrids").getCol('balance', false, 'sum');
			
			$("#bat_jqgrids").footerData('set', { 
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
//	search()
}
function order(rowid) {
	//获得行号
	var gr = $("#bat_jqgrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowDatas = $("#bat_jqgrids").jqGrid('getRowData', gr);
	var formType = rowDatas.formType
	if(formType == '采购入库') {
		localStorage.setItem("intoWhsSnglId", rowDatas.formNum);
		window.open("../../Components/purs/intoWhs.html?1");
	} else if(formType == '销售出库') {
		localStorage.setItem("outWhsId", rowDatas.formNum);
		window.open("../../Components/sell/sellOutWhs.html?1");
	}else {
		localStorage.setItem("formNum", rowDatas.formNum);
		window.open("../../Components/whs/otherOutIntoWhs.html?1");
	}
}
var arr=[];
var obj={}
//导出
$(document).on('click', '.exportExcel', function() {
	var ids = $("#batn_jqGrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowData = $("#batn_jqGrids").jqGrid('getRowData', ids);
	var invtyEncd = rowData.invtyEncd
	var batNum = rowData.batNum
	if(invtyEncd == undefined) {
		alert("未选择单据")
	}else {
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				"invtyEncd": invtyEncd,
				"batNum": batNum,
			}
		};
		var Data = JSON.stringify(data);
		$.ajax({
			url: url3 + "/mis/whs/invty_tab/selectBatNumStandList",
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
				var execlName = '批次台账'
				ExportData(list, execlName)
			},
			error: function() {
				alert("导出失败")
			}
		})
		
	}
	
})
