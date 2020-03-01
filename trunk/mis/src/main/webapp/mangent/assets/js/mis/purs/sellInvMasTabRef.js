///*销售发票参照*/

//销售出库单列表表格初始化
$(function() {
	$("#b_jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['出库单号', '出库日期', '销售类型', '业务类型id', '业务类型', '业务员', '部门id', '部门', '客户名称', '客户id', '销售单号', '备注', '是否审核'],
		colModel: [{
				name: 'outWhsId',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'outWhsDt',
				editable: true,
				align: 'center'

			},
			{
				name: 'sellTypNm',
				editable: true,
				align: 'center'

			},
			{
				name: 'bizTypId',
				editable: false,
				align: 'center',
			},
			{
				name: 'bizTypNm',
				editable: false,
				align: 'center',
			},
			{
				name: 'userName',
				editable: false,
				align: 'center'
			},
			{
				name: 'deptId',
				editable: false,
				align: 'center'
			},
			{
				name: 'deptName',
				editable: false,
				align: 'center'
			},
			{
				name: 'custNm',
				editable: false,
				align: 'center',
			},
			{
				name: 'custId',
				editable: false,
				align: 'center',
			},
			{
				name: 'sellOrdrInd',
				editable: true,
				align: 'center'
			},
			{
				name: 'memo',
				editable: true,
				align: 'center'
			},
			{
				name: 'isNtChk',
				editable: true,
				align: 'center'
			},
		],
		autowidth: true,
		height:300,
		autoScroll:true,
		shrinkToFit:false,
		rownumbers: true,
		loadonce: false,
		forceFit: true,
		rowList: [10, 20, 30], //可供用户选择一页显示多少条
		pager: '#jqgridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		multiboxonly: true,
		caption: "销售出库单", //表格的标题名字
		//双击弹出采购订单
//		ondblClickRow: function() {
//			order();
//		},
	})

})

//查询按钮
$(document).on('click', '.searcher', function() {
	search()
})

function search() {
	var myDate = {};

	var invtyEncd = $(".invtyEncd").val();
	var outWhsId = $(".outWhsId").val();
	var custId = $(".custId").val();
	var outWhsDt1 = $(".outWhsDt1").val();
	var outWhsDt2 = $(".outWhsDt2").val();

	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": invtyEncd,
			"outWhsId": outWhsId,
			"custId": custId,
			"outWhsDt1": outWhsDt1,
			"outWhsDt2": outWhsDt2,
			"pageNo": 1,
			"pageSize":200
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/purc/SellOutWhs/querySellOutWhsList',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			var list = data.respBody.list;
			myDate = list;
			var rowNum = $("#_input").val()
			$("#b_jqGrids").jqGrid('clearGridData');
			$("#b_jqGrids").jqGrid('setGridParam', {
				datatype: 'local',
//				rowNum: rowNum,
				data: myDate, //newData是符合格式要求的重新加载的数据
				page: 1 //哪一页的值
			}).trigger("reloadGrid")
		},
		error: function() {
			alert("查询失败")
		}
	});

}
//$(function() {
//	$("#last_jqGridPager_list").after('<input id="_input" type="text" value="10"/>')
//})