//流水账
var page = 1;
var rowNum;

$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing</div></div > ");
	$("#mengban").addClass("zhezhao");
})

//右列表格的操作
$(function() {
	pageInit();
})

//页面初始化
function pageInit() {
	allHeight()
	jQuery("#jqgrids").jqGrid({
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		url: '../../assets/js/json/order.json',
		colNames: ['单据日期', '单据号', '记账日期', '记账人', '收发类别编码', '收发类别名称', '业务类型', '仓库名称',
			'客户名称', '供应商名称', '经手人', '存货名称', '规格型号', '主计量单位', '收入数量', '收入单价',
			'收入金额', '发出数量', '发出单价', '发出金额',
		], //jqGrid的列显示名字
		colModel: [{
				name: 'formDt',
				align: "center",
				editable: true,
				sorttype: 'date'
			},
			{
				name: "formNum",
				align: "center",
				editable: true,
				sorttype: 'integer'
			},
			{
				name: "bookOkDt",
				align: "center",
				editable: true,
				sorttype: 'date'
			},
			{
				name: "bookOkAcc",
				align: "center",
				editable: true,
				sorttype: 'integer'
			},
			{
				name: 'recvSendCateId',
				align: "center",
				editable: true,
				sorttype: 'integer'
			},
			{
				name: 'recvSendCateNm',
				align: "center",
				editable: true,
			}, {
				name: 'bizTypNm',
				align: "center",
				editable: true,
			},
			{
				name: "whsNm",
				align: "center",
				editable: true,
			},
			{
				name: "custNm",
				align: "center",
				editable: true,
			},
			{
				name: "porvrNm",
				align: "center",
				editable: true,
			},
			{
				name: 'userName',
				align: "center",
				editable: true,
			},
			{
				name: 'invtyNm',
				align: "center",
				editable: true,
			},
			{
				name: 'spcModel', //规格型号
				align: "center",
				editable: true,
			},
			{
				name: 'measrCorpNm', //主计量单位
				align: "center",
				editable: true,
			},
			{
				name: 'inQty', //收入数量
				align: "center",
				editable: true,
				sorttype: 'integer'
			},
			{
				name: 'inUprc',
				align: "center",
				editable: true,
				sorttype: 'integer'
			},
			{
				name: "inAmt",
				align: "center",
				editable: true,
				sorttype: 'integer'
			},
			{
				name: "sendQty",
				align: "center",
				editable: true,
				sorttype: 'integer'
			},
			{
				name: "sendUprc",
				align: "center",
				editable: true,
				sorttype: 'integer'
			},
			{
				name: 'sendAmt',
				align: "center",
				editable: true,
				sorttype: 'integer'
			}
		],
		sortable: true,
		autowidth: true,
		autoScroll: true,
		shrinkToFit: false,
		height: height,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 500,
		rowList: [500, 1000, 3000, 5000],
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		footerrow: true,
		caption: "流水账", //表格的标题名字
		//cellEdit:true, //单元格是否可编辑
		gridComplete: function() { 
			var inqtys = 0;
			var inamts = 0;
			var outqtys = 0;
			var outamts = 0;
			var inqtys = $("#jqgrids").getCol('inQty', false, 'sum');
			var inamts = $("#jqgrids").getCol('inAmt', false, 'sum');
			var outqtys = $("#jqgrids").getCol('sendQty', false, 'sum');
			var outamts = $("#jqgrids").getCol('sendAmt', false, 'sum');
			if(isNaN(inqtys)) {
				inqtys = 0;
			}
			if(isNaN(inamts)) {
				inamts = 0;
			}
			if(isNaN(outqtys)) {
				outqtys = 0;
			}
			if(isNaN(outamts)) {
				outamts = 0;
			}
			inamts = precision(inamts,2)
			outamts = precision(outamts,2)
			$("#jqgrids").footerData('set', { 
				"formDt": "本页合计",
				"inQty": inqtys,
				"inAmt": inamts,
				"sendQty": outqtys,
				"sendAmt": outamts,

			}          );    
		},
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#jqgrids").getGridParam('records'); //获取返回的记录数
			page = $("#jqgrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#jqgrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			loadLocalData(page)
		},
	});
	//	jQuery("#jqgrids").jqGrid('setGroupHeaders', {
	//		useColSpanStyle: false,
	//		groupHeaders: [
	//			{
	//				startColumnName: 'inQty',
	//				numberOfColumns: 3,
	//				titleText: '<em>收入</em>'
	//			},
	//			{
	//				startColumnName: 'sendQty',
	//				numberOfColumns: 3,
	//				titleText: '<em>发出</em>'
	//			}
	//		]
	//	});
}

function loadLocalData(page) {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var isNtBookOk = $("#isNtBookOk").val();
	var formSDt = $(".formSDt").val();
	var formEDt = $(".formEDt").val();
	var invtyEncd = $("#invtyEncd").val();
	var formNum = $(".formNum").val();
	var invtyCls = $("#invtyCls").val();
	var formType = $("#formCode").val();
	var whsEncd = $("#whsEncd").val();
	if(isNtBookOk == null || isNtBookOk == "") {
		alert("请选择包含单据")
	} else {
		var myData = {};
		var data2 = {
			"reqHead": reqHead,
			"reqBody": {
				'isNtBookOk': isNtBookOk,
				'formSDt': formSDt,
				'formEDt': formEDt,
				'invtyEncd': invtyEncd,
				'formNum': formNum,
				'invtyCls': invtyCls,
				'whsEncd': whsEncd,
				'formType': formType,
				"pageNo": page,
				"pageSize": rowNum
			}
		};
		var postD2 = JSON.stringify(data2);
		$.ajax({
			type: "post",
			url: url + "/mis/account/invty/stream/list",
			async: true,
			data: postD2,
			dataType: 'json',
			contentType: 'application/json; charset=utf-8',
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
				if(list == null) {
					alert("查询无数据");
				} else {
					var arr = [];
					for(var i = 0; i < list.length; i++) {
						if(list[i].bookEntrySub != null) {
							var entrs = list[i].bookEntrySub
							for(var j = 0; j < entrs.length; j++) {
								(function(j) {
									var newObj = cloneObj(data.respBody.list);
									for(var k in entrs[j]) {
										newObj[i][k] = entrs[j][k]
									}
									arr.push(newObj[i])
								})(j)
							}
						}
					}
					$("#jqgrids").jqGrid('clearGridData')
					var mydata = {}
					mydata.rows = arr;
					mydata.page = page;
					mydata.records = data.respBody.count;
					mydata.total = data.respBody.pages;
					$("#jqgrids").jqGrid("setGridParam", {
						data: mydata.rows,
						localReader: {
							root: function(object) {
								return mydata.rows
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
				};
			},
			error: function() {
				alert("条件查询失败")
			}
		});
	}
}

//查询按钮
$(function() {
	$(".find").click(function() {
		loadLocalData(page)
	})
})

//导出
$(document).on('click', '.exportExcel', function() {
	var isNtBookOk = $("#isNtBookOk").val();
	var formSDt = $(".formSDt").val();
	var formEDt = $(".formEDt").val();
	var invtyEncd = $("#invtyEncd").val();
	var formNum = $(".formNum").val();
	var invtyCls = $("#invtyCls").val();
	var formType = $("#formCode").val();
	var whsEncd = $("#whsEncd").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			'isNtBookOk': isNtBookOk,
			'formSDt': formSDt,
			'formEDt': formEDt,
			'invtyEncd': invtyEncd,
			'formNum': formNum,
			'invtyCls': invtyCls,
			'whsEncd': whsEncd,
			'formType': formType,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/account/invty/stream/listExport',
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
			var execlName = '流水账'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
})