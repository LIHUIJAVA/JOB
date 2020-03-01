//页面初始化
$(function() {
	pageInit3();
	search2()
});
var count;
var pages;
var page = 1;
var rowNum;

function pageInit3() {
//	allHeight()
	jQuery("#order_jqgrids").jqGrid({
		datatype: "local", //请求数据返回的类型。可选json,xml,txt
		colNames: ['部门编码', '部门名称', '联系方式', '地址', '备注'], //jqGrid的列显示名字
		colModel: [{
				name: "deptId",
				align: "center",
				editable: false,
			},
			{
				name: "deptName",
				align: "center",
				editable: false,
			},
			{
				name: "tel",
				align: "center",
				editable: false,
			},
			{
				name: 'addr',
				align: "center",
				editable: false,
			},
			{
				name: 'memo',
				align: "center",
				editable: false,
			}
		],
		height: 320,
		autowidth: true,
		viewrecords: true,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 100,
		rowList: [100, 300, 500],
		pager: '#order_jqGridPager', //表格页脚的占位符(一般是div)的id
		multiselect: true, //复选框
		caption: "部门档案", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#order_jqgrids").getGridParam('records'); //获取返回的记录数
			page = $("#order_jqgrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#order_jqgrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			search2()
		},
	}).closest(".ui-jqgrid-bdiv").css({
		'overflow-y': 'scroll'
	});
}

//条件查询
$(function() {
	$('.find').click(function() {
		search2()
	})
})

function search2() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var deptId = $("input[class='deptId']").val();
	var deptName = $("input[class='deptName']").val();
	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"deptId": deptId,
			"deptName": deptName,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		url: url + "/mis/purc/DeptDoc/selectDeptDocList",
		async: true,
		data: postD2,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		success: function(data) {
			var mydata = {};
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#order_jqgrids").jqGrid("clearGridData");
			$("#order_jqgrids").jqGrid("setGridParam", {
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
			alert("条件查询失败")
		}
	});
}

$(document).on('click', '.dept_sure', function() {
	//获得行号
	var ids = $("#order_jqgrids").jqGrid('getGridParam', 'selarrrow');
	if(ids.length == 0) {
		alert("请选择单据")
	} else {
		var encd = [];
		var nm = [];
		for(i = 0; i < ids.length; i++) {
			var rowDatas = $("#order_jqgrids").jqGrid('getRowData', ids[i]); //获取行数据
			nm[i] = rowDatas.deptName;
			encd[i] = rowDatas.deptId;
		}
		var encds = encd.join(',');
		var nms = nm.join(',')
		window.parent.opener.document.getElementById("deptId").value = encds;
		window.parent.opener.document.getElementById("deptName").value = nms;
		window.close()
	}
})
$(document).on('click', '.dept_cancel', function() {
	window.parent.opener.document.getElementById("deptId").value = "";
	window.parent.opener.document.getElementById("deptName").value = "";
	window.close()
})