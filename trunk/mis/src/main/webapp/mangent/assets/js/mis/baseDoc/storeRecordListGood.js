
$(function() {
	jQuery("#List_jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['店铺编码', '店铺名称', '电商平台编码','电商平台名称'], //jqGrid的列显示名字
		colModel: [{
				name: 'storeId',
				align: "center",
				index: 'invdate',
				editable: false,
			},
			{
				name: 'storeName',
				align: "center",
				index: 'id',
				editable: true,
			},
			{
				name: 'ecId',
				align: "center",
				index: 'invdate',
				editable: false,
			},
			{
				name: 'ecName',
				align: "center",
				index: 'invdate',
				editable: false,
			}
		],
		autowidth: true,
		rownumbers: true,
		loadonce: true,
		height:300,
		forceFit: true,
		rowNum: 10,
		rowList: [100, 300, 500],
		pager: '#List_jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "店铺档案", //表格的标题名字
	});

})


//条件查询
$(document).on('click', '.findList', function() {
	var myDate = {};
	var storeId = $(".storeId").val();
	var storeName = $(".storeName").val();
	var ecId = $("input[name='ecId1']").val();
	var Data = {
		"reqHead":reqhead,
		"reqBody": {
			'ecId':ecId,
			"storeId": storeId,
			"storeName": storeName,
			"pageNo": 1,
			"pageSize": 100
		}
	}
	var changeData = JSON.stringify(Data)
	$.ajax({
		type: "post",
		url: url3 + "/mis/ec/storeRecord/queryList",
		async: true,
		data: changeData,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		success: function(data) {
			window.localStorage.removeItem("pro");
			var arr = eval(data); //数组
			var list = arr.respBody.list;
			myDate = list;
			jQuery("#List_jqgrids").jqGrid({
				data: myDate,
				datatype: "local", //请求数据返回的类型。可选json,xml,txt
			});

			$("#List_jqgrids").jqGrid('setGridParam', {
				datatype: 'local',
				data: myDate, //newData是符合格式要求的重新加载的数据
				page: 1 //哪一页的值
			}).trigger("reloadGrid")

		},
		error: function() {
			alert("条件查询失败")
		}
	});
	$("#List_jqgrids").jqGrid('clearGridData')

})
