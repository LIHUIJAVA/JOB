var count;
var pages;
var rowNum;
//表格初始化-采购普通发票列表
$(function() {
//	allHeight()
	//加载动画html 添加到初始的时候
	$(".purchaseTits").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTits").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	$("#canzhao_jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['单据号', '单据日期', '单据类型','客户订单号','业务类型名称', '业务类型ID','销售类型名称','销售类型ID','业务员名称', '业务员ID', '部门名称', '部门ID','客户名称','客户ID', '是否审核','备注', 
		],
		colModel: [{
				name: 'sellInvNum',
				editable: true,
				align: 'center',
				sortable: false

			},
			{
				name: 'bllgDt',
				editable: true,
				align: 'center',
				sortable: false

			},
			{
				name: 'formTypEncd',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'custOrdrNum',
				editable: false,
				align: 'center',
//				sortable: false
			},
			{
				name: 'bizTypNm',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'bizTypId',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'sellTypNm',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'sellTypId',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'userName',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'accNum',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'deptName',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'deptId',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'custNm',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'custId',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'isNtChk',
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
		],
		autowidth: true,
		height:200,
		autoScroll:true,
		shrinkToFit:false,
		rownumbers: true,
		rowNum: 500,
		rowList: [500, 1000, 3000, 5000],
		loadonce: false,
		forceFit: true,
		pager: '#canzhao_jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
//		multiboxonly: true,
		caption: "销售单列表(主表)", //表格的标题名字
		onSelectRow: function() {
			searchAll();
		},
		onPaging: function(pageBtn) {
			var records = $("#canzhao_jqGrids").getGridParam('records'); //获取返回的记录数
			page = $("#canzhao_jqGrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#canzhao_jqGrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			sear(index, sortorder, page)
		},
		// 全选触发事件
		onSelectAll:function(rowids,statue){
			var gr = $("#canzhao_jqGrids").jqGrid('getGridParam', 'selarrrow');
			var num = [];
				
			var length = gr.length;
			for(var i = 0; i < length; i++) {
				var obj = {}
				// 获取所选航的数据
				var rowDatas = $("#canzhao_jqGrids").jqGrid('getRowData', gr[i]);//获取行数据
				obj.sellInvNum = rowDatas.sellInvNum
				obj.formTypEncd = rowDatas.formTypEncd
				num.push(obj)
			}
			if(num.length == 0) {
				$("#canzhao_jqGrids_b").jqGrid("clearGridData");
			} else {
				var myData = {};
				var Data = {
					"reqHead": reqhead,
					"reqBody": {
						"lists": num
					}
				};
				var saveData = JSON.stringify(Data);
				$.ajax({
					type: "post",
					url: url + "/mis/account/SellComnInv/selectSellComnInvBySellRtnEntList",
					async: true,
					data: saveData,
					dataType: 'json',
					contentType: 'application/json;charset=utf-8',
					success: function(data) {
						var fData = data.respBody
						var list = data.respBody.list;
						myDate = list;
						$("#canzhao_jqGrids_b").jqGrid('clearGridData');
						$("#canzhao_jqGrids_b").jqGrid('setGridParam', {
							//				rowNum:rowNum,
							datatype: 'local',
							data: myDate, //newData是符合格式要求的重新加载的数据
							page: 1 //哪一页的值
						}).trigger("reloadGrid")
						
					},
					error: function() {
						alert(error)
					}
				});
			}
       },
       onSortCol: function(index, colindex, sortorder) {
			switch(index) {
				case "custOrdrNum":
					index = "a.cust_ordr_num"
					break;
			}
			localStorage.setItem("index",index)
			localStorage.setItem("sortorder",sortorder)
			window.index = index;
			window.sortorder = sortorder;
			var formTypEncd = localStorage.getItem("formTypEncd")
			sear(index, sortorder,formTypEncd)
		}
	})
	allHeight()
	$("#canzhao_jqGrids_b").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['序号','单据号','仓库编码','仓库名称', '存货编码', '存货名称', '主计量单位',
		'规格型号','数量','含税单价', '价税合计', '批次', '生产日期',
		'失效日期', '保质期','项目编码', '项目名称','箱规', '箱数', '备注', '对应条形码', 
		'国际批次', '无税单价', '无税金额', '税率', '税额', '是否退货','主计量单位编码',
//			,'存货代码'
		], //jqGrid的列显示名字
		colModel: [{
				name: 'ordrNum',
				editable: true,
				align: 'center',
//				hidden:true

			},
			{
				name: 'sellInvNum',
				editable: true,
				align: 'center'

			},
			{
				name: 'whsEncd',
				editable: true,
				sortable: false,
				align: 'center',
			},
			{
				name: 'whsNm',
				editable: false,
				align: 'center',
				sortable: false,
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
				name: 'measrCorpNm',
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
				name: 'qty',
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
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'batNum',
				editable: true,
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
				name: 'baoZhiQiDt',
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
				name: 'memo',
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
				name: 'intlBat', //国际批次
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
				name: 'isNtRtnGoods',
				editable: true,
				align: 'center',
				hidden: true,
				sortable: false
			},
			{
				name: 'measrCorpId',
				editable: false,
				align: 'center',
				hidden: true,
				sortable: false
			},
		],
		autowidth: true,
//		height:height - 170,
		height:220,
		autoScroll:true,
		shrinkToFit:false,
		rownumbers: true,
		loadonce: false,
		rowNum: 99999999,
		forceFit: true,
//		pager: '#canzhao_jqGridPager_b', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
//		multiboxonly: true,
		caption: "销售单列表(子表)", //表格的标题名字
		footerrow: true,
        gridComplete: function() { 
			var qty = $("#canzhao_jqGrids_b").getCol('qty', false, 'sum');
			var prcTaxSum = $("#canzhao_jqGrids_b").getCol('prcTaxSum', false, 'sum');
			var bxQty = $("#canzhao_jqGrids_b").getCol('bxQty', false, 'sum');
			var noTaxAmt = $("#canzhao_jqGrids_b").getCol('noTaxAmt', false, 'sum');
			var taxAmt = $("#canzhao_jqGrids_b").getCol('taxAmt', false, 'sum');
			qty = qty.toFixed(2);
			prcTaxSum = precision(prcTaxSum,2)
			noTaxAmt = precision(noTaxAmt,2)
			taxAmt = precision(taxAmt,2)
			bxQty = bxQty.toFixed(2)
			$("#canzhao_jqGrids_b").footerData('set', { 
				"whsEncd": "本页合计",
				"qty": qty,
				"prcTaxSum": prcTaxSum,
				"bxQty": bxQty,
				"noTaxAmt": noTaxAmt,
				"taxAmt": taxAmt,

			}          );    
		},
	})
})
function searchAll() {
	var gr = $("#canzhao_jqGrids").jqGrid('getGridParam', 'selarrrow');
	var num = [];
		
	var length = gr.length;
	for(var i = 0; i < length; i++) {
		var obj = {}
		// 获取所选航的数据
		var rowDatas = $("#canzhao_jqGrids").jqGrid('getRowData', gr[i]);//获取行数据
		obj.sellInvNum = rowDatas.sellInvNum
		obj.formTypEncd = rowDatas.formTypEncd
		num.push(obj)
	}
	if(num.length == 0) {
		$("#canzhao_jqGrids_b").jqGrid("clearGridData");
	} else {
		var myData = {};
		var Data = {
			"reqHead": reqhead,
			"reqBody": {
				"lists": num
			}
		};
		var saveData = JSON.stringify(Data);
		$.ajax({
			type: "post",
			url: url + "/mis/account/SellComnInv/selectSellComnInvBySellRtnEntList",
			async: true,
			data: saveData,
			dataType: 'json',
			contentType: 'application/json;charset=utf-8',
			success: function(data) {
				var fData = data.respBody
				var list = data.respBody.list;
				myDate = list;
				$("#canzhao_jqGrids_b").jqGrid('clearGridData');
				$("#canzhao_jqGrids_b").jqGrid('setGridParam', {
					//				rowNum:rowNum,
					datatype: 'local',
					data: myDate, //newData是符合格式要求的重新加载的数据
					page: 1 //哪一页的值
				}).trigger("reloadGrid")
				
			},
			error: function() {
				alert(error)
			}
		});
	}
}

$(function() {
	$(".yes").click(function() {
		var index = '';
		var sortorder = '';
		var formTypEncd = $("select[name='formTypEncd']").val()
		localStorage.setItem("formTypEncd",formTypEncd)
		sear(index, sortorder,formTypEncd)
	})
})

function sear(index, sortorder,formTypEncd) {
	var rowNum1 = $("#canzhao_jqGridPager td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var myDate = {};
//	var formTypEncd = $("select[name='formTypEncd']").val()
	var formDt1 = $("input[name='formDt1']").val()
	var formDt2 = $("input[name='formDt2']").val()
	var accNum = $("input[name='accNum1']").val()
	var custId = $("input[name='custId1']").val()
	var custOrdrNum = $("input[name='custOrdrNum1']").val()
	if(formTypEncd != null) {
		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				'formTypEncd': formTypEncd,
				'formDt1': formDt1,
				'formDt2': formDt2,
				'accNum': accNum,
				'custId': custId,
				'custOrdrNum': custOrdrNum,
				"isNtChk": '1',
				"isNtBllg": '0', // 是否开票
				"sort":index,
				"sortOrder":sortorder,
				"pageNo": page,
				"pageSize": rowNum
			}
		};
		var saveData = JSON.stringify(savedata);
		var myDate = {};
		console.log(saveData)
		$.ajax({
			type: "post",
			contentType: 'application/json; charset=utf-8',
			url: url + '/mis/account/SellComnInv/selectSellReturnEntrs',
			async: true,
			data: saveData,
			dataType: 'json',
			//开始加载动画  添加到ajax里面
			beforeSend: function() {
				$(".zhezhao").css("display", "block");
				$("#loader").css("display", "block");

			},
			success: function(data) {
				if(data.respHead.isSuccess == false) {
					alert(data.respHead.message)
				} else if(data.respHead.isSuccess == true) {
					$("#mengban").hide();
					$("select[name='formTypEncd']").val('')
	
					$("#tk").hide()
					$("#big_wrap").hide()
					$("#tk").css("opacity", 0)
					var mydata = {};
					var list = data.respBody.list;
					for(var i = 0; i < list.length; i++) {
						if(list[i].isNtChk == 0) {
							list[i].isNtChk = "否"
						} else if(list[i].isNtChk == 1) {
							list[i].isNtChk = "是"
						}
					}
					myDate = list;
					mydata.rows = myDate;
					mydata.page = page;
					mydata.records = data.respBody.count;
					mydata.total = data.respBody.pages;
					$("#canzhao_jqGrids").jqGrid("clearGridData");
					$("#canzhao_jqGrids").jqGrid("setGridParam", {
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
				}
			},
			//结束加载动画
			complete: function() {
				$(".zhezhao").css("display", "none");
				$("#loader").css("display", "none");
			},
		})
	} else if(formTypEncd == null) {
		alert("请选择单据类型")
	}
	
}