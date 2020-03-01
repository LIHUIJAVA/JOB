var count;
var pages;
var page = 1;
var rowNum;

$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	pageInit();
//	存货分类
	$(document).on('click', '.invtyClsEncd_biaoge', function() {
		window.open("../../Components/baseDoc/invtyTree.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
});

function pageInit() {
	allHeight()
	jQuery("#jqGrids").jqGrid({
		url: '../../assets/js/json/order.json', //组件创建完成之后请求数据的url
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['拣货单号', '拣货时间', '拣货人', '是否完成拣货', '完成拣货时间', '合并单中的拣货单号'], //jqGrid的列显示名字
		colModel: [{
				name: "pickSnglNum",
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: "pickSnglTm",
				align: "center",
				editable: false,
				sorttype:'date',
			},
			{
				name: 'pickPers',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'isFinshPick',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'finshPickTm',
				align: "center",
				editable: false,
				sorttype:'date',
			},
			{
				name: 'pickNum',
				align: "center",
				editable: false,
				sortable: false
			}
		],
		height:height,
		rownumWidth:20,
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
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		//		multiselect: true, //复选框
		//		multiboxonly: true,
		caption: "拣货单列表", //表格的标题名字
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
			search()
		},
		ondblClickRow: function(rowid) {
			order(rowid);
		}
	})
}

function order(rowid) {
	//获得行数据
	var rowDatas = $("#jqGrids").jqGrid('getRowData', rowid);
	localStorage.setItem("pickSnglNum", rowDatas.pickSnglNum);
	window.open("../../Components/whs/pickingDtl.html?1");
}

$(document).on('click', '#find', function() {
	search()
})

function search() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var pickSnglNum = $(".pickSnglNum").val();
	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"pickSnglNum": pickSnglNum,
			"pageSize": rowNum,
			"pageNo": page,
		}
	};
	var saveData = JSON.stringify(data2);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/whs/pick_sngl/queryAllList',
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
			for(var i = 0; i < list.length; i++) {
				if(list[i].isFinshPick == "0") {
					list[i].isFinshPick = "否"
				} else if(list[i].isFinshPick == "1") {
					list[i].isFinshPick = "是"
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
		error: function() {
			alert("查询失败")
		}
	});
}

$(function() {
	$("#finish").click(function() {
		//获得选中行的行号
		var gr = $('#jqGrids').jqGrid('getGridParam', 'selrow');
		if(gr == null) {
			alert("请选择单号")
		} else {
			var rowDatas = $("#jqGrids").jqGrid('getRowData', gr);
			var data = {
				"reqHead": reqhead,
				"reqBody": {
//					"operatorId": operatorId,
					"pickSnglNum": rowDatas.pickSnglNum
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/whs/pick_sngl/updateTabPC',
				type: 'post',
				data: Data,
				dataType: 'json',
				async: true,
				contentType: 'application/json;charset=utf-8',
				success: function(data) {
					alert(data.respHead.message);
					if(data.respHead.isSuccess == true) {
						search();
					}
				},
				error: function() {
					console.log(error)
				}
			})
		}
	})
})

//删除
$(function() {
	$(".delOrder").click(function() {
		//获得选中行的行号
		var gr = $('#jqGrids').jqGrid('getGridParam', 'selrow');
		//获取选择行的provrId
		var rowData = $("#jqGrids").jqGrid('getRowData', gr);
		var pickSnglNum = rowData.pickSnglNum
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				"pickSnglId": pickSnglNum
			}
		};
		var Data = JSON.stringify(data);
		$.ajax({
			url: url + '/mis/whs/pick_sngl/deletePickSngl',
			type: 'post',
			data: Data,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			success: function(data) {
				alert(data.respHead.message);
				if(data.respHead.isSuccess == true) {
					search();
				}
			},
			error: function() {
				alert("删除失败")
			}
		})
	})
})