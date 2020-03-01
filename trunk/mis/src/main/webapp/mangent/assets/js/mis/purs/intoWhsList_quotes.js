var count;
var pages;
var rowNum;

//表格初始化
$(function() {
	//加载动画html 添加到初始的时候
	$(".purchaseTits").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTits").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	$("#jqGrids_list").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['入库单号', '入库日期', '采购类型', '业务员Id', '业务员', '供应商id','供应商','部门id', '部门',
			'供应商订单号', '备注', '订单号', '到货单号','单据类型编码'
		],
		colModel: [{
				name: 'intoWhsSnglId',
				editable: false,
				align: 'center',
				sortable: false,
			},
			{
				name: 'intoWhsDt',
				editable: false,
				align: 'center',
				sortable: false

			},
			{
				name: 'pursTypNm',
				editable: false,
				align: 'center',
				sortable: false

			},
			{
				name: 'accNum',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'userName',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'provrId',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'provrNm',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'deptId',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'deptName',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'provrOrdrNum',
				editable: false,
				align: 'center'
			},
			{
				name: 'memo',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'pursOrdrId',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'toGdsSnglId',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'formTypEncd',
				editable: false,
				align: 'center',
				sortable: false
			}
		],
		autowidth: true,
		rownumbers: true,
		height: 200,
		autoScroll:true,
		shrinkToFit:false,
		loadonce: false,
		forceFit: true,
		rowNum: 500,
		rowList: [500, 1000, 3000, 5000],
		pager: '#jqGridPager_list', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
//		multiboxonly: true,
		caption: "采购入库(退货)单主表", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#jqGrids_list").getGridParam('records'); //获取返回的记录数
			page = $("#jqGrids_list").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#jqGrids_list").getGridParam('rowNum'); //获取显示配置记录数量

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
			into_search(index, sortorder, page);
		},
		// 全选触发事件
		onSelectAll:function(rowids,statue){
			var gr = $("#jqGrids_list").jqGrid('getGridParam', 'selarrrow');
			var num = [];
			for(var i =0;i<gr.length;i++) {
				//获得行数据
				var rowDatas = $("#jqGrids_list").jqGrid('getRowData', gr[i]);
				var intoWhsSnglId = rowDatas.intoWhsSnglId;
				num.push(intoWhsSnglId)
			}
			if(num.length == 0) {
				$("#jqGrids_list_b").jqGrid("clearGridData");
			} else {
				var myData = {};
				var Data = {
					"reqHead": reqhead,
					"reqBody": {
						"intoWhsSnglId": num.toString()
					}
				};
				var saveData = JSON.stringify(Data);
				$.ajax({
					type: "post",
					url: url + "/mis/purc/IntoWhs/queryIntoWhsByIntoWhsIds",
					async: true,
					data: saveData,
					dataType: 'json',
					contentType: 'application/json;charset=utf-8',
					success: function(data) {
						var fData = data.respBody
						var list = data.respBody.list;
						myDate = list;
						$("#jqGrids_list_b").jqGrid('clearGridData');
						$("#jqGrids_list_b").jqGrid('setGridParam', {
							datatype: 'local',
							data: myDate, //newData是符合格式要求的重新加载的数据
							page: 1 //哪一页的值
						}).trigger("reloadGrid")
						
					},
					error: function(error) {
						alert(error)
					}
				});
			}
        },
		onSelectRow: function() {
			searchAll();
		},
		onSortCol: function(index, colindex, sortorder) {
			switch(index) {
				case "provrOrdrNum":
					index = "iw.provr_ordr_num"
					break;
			}
			localStorage.setItem("index",index)
			localStorage.setItem("sortorder",sortorder)
			window.index = index;
			window.sortorder = sortorder;
			into_search(index, sortorder, newPage)      
		}
	})
	$("#jqGrids_list_cb").hide()
})

//查询按钮

$(document).on('click', '.searcher', function() {
	clickSearch=1;
	var initial = 1;
	window.newPage = initial;
	var index = '';
	var sortorder = '';
	into_search(index, sortorder,newPage);
})
function into_search(index, sortorder,newPage) {
	var rowNum1 = $("#jqGridPager_list td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var myDate = {};

	var invtyEncd = $("input[name='invtyEncd2']").val();
	var intoWhsSnglId = $("input[name='intoWhsSnglId2']").val();
	var provrId = $("input[name='provrId2']").val();
	var intoWhsDt1 = $("input[name='intoWhsSnglDt1']").val();
	var intoWhsDt2 = $("input[name='intoWhsSnglDt2']").val();
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": invtyEncd,
			"intoWhsSnglId": intoWhsSnglId,
			"provrId": provrId,
			"intoWhsDt1": intoWhsDt1,
			"intoWhsDt2": intoWhsDt2,
			"isNtChk":1,
			"isNtBlle":0,
			"unBllgQty":1,
			"sort":index,
			"sortOrder":sortorder,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	console.log(saveData)
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/purc/IntoWhs/queryIntoWhsLists',
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
			$("#jqGrids_list").jqGrid("clearGridData");
			$("#jqGrids_list").jqGrid("setGridParam", {
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
			$("#big_wrap").hide()
			$("#boxs").hide()
		},
		//结束加载动画
		complete: function() {
			$(".zhezhao").css("display", "none");
			$("#loader").css("display", "none");
		},
		error: function() {
			alert("error")
		}
	});
}


//表格初始化
$(function() {
	var rowNum = $("#_input").val()
	allHeight()
	$("#jqGrids_list_b").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['序号','入库单号','仓库编码', '仓库名称', '批次', '存货编码', '存货名称', '项目编码', '项目名称', '规格型号', '主计量单位', '编码', '数量', '未开票数量', '含税单价', '价税合计',
			'无税单价', '无税金额', '税率', '税额', '箱规', '箱数', '对应条形码', '生产日期', '失效日期', '保质期', '国际批次', '备注', '是否退货'
		], //jqGrid的列显示名字
		colModel: [{
				name: 'ordrNum',
				editable: false,
				align: 'center',
				sortable: false,
				hidden:true
			},{
				name: 'intoWhsSnglId',
				editable: false,
				align: 'center',
				sortable: false,
			},
			{
				name: 'whsEncd',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'whsNm',
				editable: false,
				align: 'center',
				sortable: false,
			},
			{
				name: 'batNum',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'invtyEncd',
				editable: true,
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
				editable: true,
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
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'unBllgQty',
				editable: true,
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
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'noTaxUprc',
				editable: true,
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
				editable: true,
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
				editable: true,
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
				name: 'prdcDt', //生产日期
				editable: true,
				align: 'center',
			},
			{
				name: 'invldtnDt', //失效日期
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
				name: 'intlBat', //国际批次
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'memo',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'isNtRtnGoods',
				editable: true,
				align: 'center',
				hidden: true,
				sortable: false
			},
		],
		autowidth: true,
		rownumbers: true,
		height: height - 170,
		autoScroll:true,
		shrinkToFit:false,
		loadonce: false,
		rowNum: 99999999,
		forceFit: true,
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		caption: "采购入库(退货)单子表明细", //表格的标题名字
		footerrow: true,
        gridComplete: function() { 
			var qty = $("#jqGrids_list_b").getCol('qty', false, 'sum');
			var prcTaxSum = $("#jqGrids_list_b").getCol('prcTaxSum', false, 'sum');
			var noTaxAmt = $("#jqGrids_list_b").getCol('noTaxAmt', false, 'sum');
			var taxAmt = $("#jqGrids_list_b").getCol('taxAmt', false, 'sum');
			var bxQty = $("#jqGrids_list_b").getCol('bxQty', false, 'sum');
			qty = qty.toFixed(2);
			prcTaxSum = precision(prcTaxSum,2)
			noTaxAmt = precision(noTaxAmt,2)
			taxAmt = precision(taxAmt,2)
			bxQty = bxQty.toFixed(2)
			$("#jqGrids_list_b").footerData('set', { 
				"intoWhsSnglId": "本页合计",
				"qty": qty,
				"prcTaxSum": prcTaxSum,
				"noTaxAmt": noTaxAmt,
				"taxAmt": taxAmt,
				"bxQty": bxQty,

			}          );    
		},
	})
})

function searchAll() {
	var gr = $("#jqGrids_list").jqGrid('getGridParam', 'selarrrow');
	var num = [];
	for(var i =0;i<gr.length;i++) {
		//获得行数据
		var rowDatas = $("#jqGrids_list").jqGrid('getRowData', gr[i]);
		var intoWhsSnglId = rowDatas.intoWhsSnglId;
		num.push(intoWhsSnglId)
	}
	if(num.length == 0) {
		$("#jqGrids_list_b").jqGrid("clearGridData");
	} else {
		var myData = {};
		var Data = {
			"reqHead": reqhead,
			"reqBody": {
				"intoWhsSnglId": num.toString()
			}
		};
		var saveData = JSON.stringify(Data);
		$.ajax({
			type: "post",
			url: url + "/mis/purc/IntoWhs/queryIntoWhsByIntoWhsIds",
			async: true,
			data: saveData,
			dataType: 'json',
			contentType: 'application/json;charset=utf-8',
			success: function(data) {
				var fData = data.respBody
				var list = data.respBody.list;
				myDate = list;
				$("#jqGrids_list_b").jqGrid('clearGridData');
				$("#jqGrids_list_b").jqGrid('setGridParam', {
					//				rowNum:rowNum,
					datatype: 'local',
					data: myDate, //newData是符合格式要求的重新加载的数据
					page: 1 //哪一页的值
				}).trigger("reloadGrid")
				
			},
			error: function(error) {
				alert(error)
			}
		});
	}
}