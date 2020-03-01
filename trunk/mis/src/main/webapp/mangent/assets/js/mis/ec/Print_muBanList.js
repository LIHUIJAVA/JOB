var count;
var pages;
var page = 1;
var rowNum;
//刚开始时可点击的按钮
$(function() {
	$(".saveOrders").addClass("gray") //参照
	$(".gray").attr("disabled", true)
	
	$(".download").removeClass("gray")
	$("#mengban").hide()
	$(".down").hide()
})
var myData = {};
//页面初始化
$(function() {
	allHeight()
	//初始化表格
	jQuery("#print_muBanList_jqgrids").jqGrid({
		height: height,
		autoScroll: true,
		datatype: "local", //请求数据返回的类型。可选json,xml,txt
		shrinkToFit: false,
		colNames: ['模板编码', '模板名称', '操作员', '操作员姓名', '备注', '编码', '操作日期'
		], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'templateId',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'templateName',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'opid',
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: 'opname',
				align: "center",
				editable: false,
				sortable: false,
				hidden:true
			},
			{
				name: 'reamrk',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'idx',
				align: "center",
				editable: false,
				sortable: false,
				hidden:true
			},
			{
				name: 'opdate',
				align: "center",
				editable: false,
				sortable: false,
			}
		],
//				rowNum: 10, //一页显示多少条
		rowList: [10, 20, 30], //可供用户选择一页显示多少条			
		autowidth: true,
		loadonce: true,
		multiboxonly: true,
		cellsubmit: "clientArray",
		rownumbers: true,
		pager: '#print_muBanList_jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'storeId', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		rownumWidth: 20,  //序列号列宽度
		multiselect: true, //复选框
		caption: "模板", //表格的标题名字	

	});
	search()
})


//条件查询
$(function() {
	$('#inquiry').click(function() {
		search()
	})
})

////查询按钮
function search() {
	var templateId = $("input[name='templateId2']").val();
	var templateName = $("input[name='templateName2']").val();
	var opid = $("input[name='opid2']").val();
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var data2 = {
		reqHead,
		"reqBody": {
			"templateId": templateId,
			"templateName": templateName,
			"opid": opid,
			"pageNo": page,
			"pageSize": rowNum
		}

	};
	var postD2 = JSON.stringify(data2);
	console.log(postD2)
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url3 + "/mis/whs/label/queryListLabelTemplates",
		data: postD2,
		dataType: 'json',
		async: true,
		success: function(data) {
			console.log(data)
			var mydata = {};
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#print_muBanList_jqgrids").jqGrid("clearGridData");
			$("#print_muBanList_jqgrids").jqGrid("setGridParam", {
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
			console.log(error)
		}
	});
}



$(document).on('click', '.yess', function() {
	//获得行号
	var gr = $("#print_muBanList_jqgrids").jqGrid('getGridParam', 'selrow');
	if(gr == null) {
		alert("未选择")
	} else  {
		window.localStorage.removeItem("whsEncd")
		//获得行数据
		var rowDatas = $("#print_muBanList_jqgrids").jqGrid('getRowData', gr);
		window.parent.opener.document.getElementById("templateId").value= rowDatas.templateId;
		localStorage.setItem("templateId",rowDatas.templateId)
		window.parent.opener.document.getElementById("templateName").value= rowDatas.templateName;
		localStorage.setItem("templateName",rowDatas.templateName)
		window.close()
	}
})

$(document).on('click', '.nos', function() {
	localStorage.clear();
	window.parent.opener.document.getElementById("templateName").value= "";
	window.parent.opener.document.getElementById("templateId").value= "";
	window.close()
})