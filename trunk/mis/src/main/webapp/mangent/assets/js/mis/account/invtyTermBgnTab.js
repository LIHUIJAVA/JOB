//库存期初表
var count;
var pages;
var page = 1;
var rowNum;

//表格初始化
$(function() {
	jQuery("#jqGrids").jqGrid({
		datatype: "local",
		colNames: ['存货名称', '仓库名称', '规格型号', '主计量单位',
			'编码', '数量', '含税单价', '价税合计', '无税单价', '无税金额', '批次', '箱规', '生产日期', '保质期', '失效日期'
		], //jqGrid的列显示名字
		colModel: [{
				name: 'invtyNm',
				editable: true,
				align: 'center',
				sortable: false

			},
			{
				name: 'whsNm',
				editable: true,
				align: 'center',
				sortable: false

			},
			{
				name: 'spcModel', //规格型号
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'measrCorpNm', //主计量单位
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'measrCorpId', //单位编码
				editable: false,
				align: 'center',
				hidden: true,
				sortable: false
			},
			{
				name: 'qty', //数量
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'cntnTaxUprc', //含税单价
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'prcTaxSum', //价税合计
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'uprc', //无税单价
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'amt', //无税金额
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'batNum', //批次
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
				name: 'prdcDt',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'quaGuarPer', //保质期
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'invldtnDt',
				editable: true,
				align: 'center',
				sortable: false
			},
		],
		autowidth: true,
		rownumbers: true,
		autowidth: true,
		height: 320,
		autoScroll: true,
		shrinkToFit: false,
		loadonce: false,
		forceFit: true,
		rowNum: 10,
		rowList: [10, 20, 30], //可供用户选择一页显示多少条
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "库存期初", //表格的标题名字
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
			seacher()
		},
	})
	seacher()
})

//查询按钮
$(function() {
	$(".search").click(function() {
		seacher()
	})
})

function seacher() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/account/invtyTermBgnTab/selectInvtyTermBgnTab',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			var mydata = {}
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#jqGrids").jqGrid('clearGridData')
			$("#jqGrids").jqGrid("setGridParam", {
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
		},
		error: function() {
			alert("error")
		}
	});
}
//导入--url不对
$(function() {
	$(".importExcel").click(function() {
		var files = $("#FileUpload").val()
		var fileObj = document.getElementById("FileUpload").files[0];
		var formFile = new FormData();
		formFile.append("action", "UploadVMKImagePath");
		formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
		var data = formFile;
		if(files != "") {
			$.ajax({
				type: 'post',
				url: url + "/mis/purc/CustDoc/uploadCustDocFile",
				data: data,
				dataType: "json",
				cache: false, //上传文件无需缓存
				processData: false, //用于对data参数进行序列化处理 这里必须false
				contentType: false, //必须
				success: function(data) {
					alert(data.respHead.message)
					window.location.reload()
				}
			});
		} else {
			alert("请选择文件")
		}
	});
});