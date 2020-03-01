var count;
var pages;
var page = 1;
var rowNum;
//表格初始化
$(function() {
	allHeight();
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	var rowNum = $("#_input").val()
	$("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['促销活动编码', '促销活动名称', '起始日期', '结束日期', '单独执行', '限量促销',
					'参与店铺','制单人','制单日期','审核人','审核日期','审核结果','表头备注'
		],
		colModel: [{
				name: 'proActId',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'proActName',
				editable: true,
				align: 'center',
				sortable: false

			},
			{
				name: 'startDate',
				editable: true,
				align: 'center',
				sortable: false

			},
			{
				name: 'endDate',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'aloneExecute',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'limitPro',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'takeStore',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'creator',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'createDate',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'auditor',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'auditDate',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'auditResult',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'memo',
				editable: false,
				align: 'center',
				sortable: false
			}
		],
		autowidth: true,
		height: height,
		autoScroll: true,
		rowNum: 500, //一页显示多少条
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条	
		shrinkToFit: false,
		rownumbers: true,
		loadonce: false,
		sortable:true,
		forceFit: true,
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
//		multiboxonly: true,
		caption: "促销活动列表", //表格的标题名字
		ondblClickRow: function(rowid) {
			order(rowid);
		},
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
			search4()
		},
	})
})


//查询按钮
$(document).on('click', '.search', function() {
	search4()
})


function search4() {
	//查询按钮
//	$(document).on('click', '.search', function() {
		var myDate = {};

		var proActId = $(".proActId").val();
		var proActName = $(".proActName").val();
		var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
		rowNum = parseInt(rowNum1)

		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				"proActId": proActId,
				"proActName": proActName,
				"pageNo": page,
				"pageSize": rowNum
			}
		};
		var saveData = JSON.stringify(savedata)
		$.ajax({
			type: "post",
			contentType: 'application/json; charset=utf-8',
			url: url + '/mis/ec/proActivity/queryList',
			async: true,
			data: saveData,
			dataType: 'json',
			//开始加载动画  添加到ajax里面
			beforeSend: function() {
				$("#mengban1").css("display", "block");
				$("#loader").css("display", "block");
			},
			success: function(data) {
				var mydata = {};
				mydata.rows = data.respBody.list;
				
				mydata.page = page;
				mydata.records = data.respBody.count;
				mydata.total = data.respBody.pages;
				$("#jqGrids").jqGrid("clearGridData");
				$("#jqGrids").jqGrid("setGridParam", {
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
				$("#mengban1").css("display", "none");
				$("#loader").css("display", "none");
			},
			error: function() {
				console.log(error)
			}
		});
//	})
}

function order(rowid) {
	//获得行数据
	var rowDatas = $("#jqGrids").jqGrid('getRowData', rowid);
	localStorage.setItem("auditResult", rowDatas.auditResult);
	localStorage.setItem("proActId", rowDatas.proActId);
	window.open("../../Components/ec/proActivity.html?1");

}
var isclick = true;
$(function() {
	//审核
	$(".toExamine").click(function() {
		if(isclick) {
			isclick = false;
			ntChk_s();
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	});
	//弃审
	$(".noTo").click(function() {
		if(isclick) {
			isclick = false;
			ntChk_q();
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	})
})
// 审核
function ntChk_s() {
	var num = []
	//获得行号
	var gr = $("#jqGrids").jqGrid('getGridParam', 'selarrrow');
	for(var i = 0;i<gr.length;i++) {
		var obj = {}
		//获得行数据
		var rowDatas = $("#jqGrids").jqGrid('getRowData', gr[i]);
		obj.proActId = rowDatas.proActId
		obj.auditor = rowDatas.auditor
		num.push(obj)
	}
	var list = num
	if(list.length == 0) {
		alert("未选择单据")
	}else {
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				"list":list
			}
		};
		var Data = JSON.stringify(data);
		$.ajax({
			url: url + '/mis/ec/proActivity/updateAuditResult',
			type: 'post',
			data: Data,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			//开始加载动画  添加到ajax里面
			beforeSend: function() {
				$("#mengban1").css("display", "block");
				$("#loader").css("display", "block");
			},
			//结束加载动画
			complete: function() {
				$("#mengban1").css("display", "none");
				$("#loader").css("display", "none");
			},
			success: function(data) {
				alert(data.respHead.message)
			},
			error: function() {
				console.log(error)
			}
		})
	}
}
// 弃审
function ntChk_q() {
	var num = []
	//获得行号
	var gr = $("#jqGrids").jqGrid('getGridParam', 'selarrrow');
	for(var i = 0;i<gr.length;i++) {
		var obj = {}
		//获得行数据
		var rowDatas = $("#jqGrids").jqGrid('getRowData', gr[i]);
		obj.proActId = rowDatas.proActId
		obj.auditor = rowDatas.auditor
		num.push(obj)
	}
	var list = num
	if(list.length == 0) {
		alert("未选择单据")
	}else {
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				"list":list
			}
		};
		var Data = JSON.stringify(data);
		$.ajax({
			url: url + '/mis/ec/proActivity/updateAuditResultNo',
			type: 'post',
			data: Data,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			//开始加载动画  添加到ajax里面
			beforeSend: function() {
				$("#mengban1").css("display", "block");
				$("#loader").css("display", "block");
			},
			//结束加载动画
			complete: function() {
				$("#mengban1").css("display", "none");
				$("#loader").css("display", "none");
			},
			success: function(data) {
				alert(data.respHead.message)
			},
			error: function() {
				console.log(error)
			}
		})
	}
}

//删除
$(function() {
	$(".delted").click(function() {
		//获得选中行的行号
		var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
		var arr = []
		for(var i = 0; i < ids.length; i++) {
			var rowData = $("#jqGrids").jqGrid('getRowData', ids[i]);
			var proActId = rowData.proActId
			arr.push(proActId)
		}
		if(ids.length == 0) {
			alert("请选择单据!")
		} else if(confirm("确定删除？")) {
			var num = arr.toString();
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"proActId": num
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/ec/proActivity/delete',
				type: 'post',
				data: Data,
				dataType: 'json',
				async: true,
				contentType: 'application/json;charset=utf-8',
				success: function(data) {
					alert(data.respHead.message)
					search()
				},
				error: function() {
					console.log(error)
				}
			})
		}
	})
})