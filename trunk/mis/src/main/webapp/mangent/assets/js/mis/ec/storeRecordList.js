$(function() {
	pageInit()
})
//页面初始化
function pageInit() {
	allHeight()
	
	//初始化表格
	jQuery("#List_jqgrids").jqGrid({
		url: '../../assets/js/json/order.json', //组件创建完成之后请求数据的url

		height: height,
		autoScroll:true,
		shrinkToFit:false,
		datatype: "json", //请求数据返回的类型。可选json,xml,txt


		colNames: ['店铺编码', '店铺名称', '电商平台编码', '电商平台名称', '免审策略编码', '免审策略名称', '销售类型', '佣金扣点编码', '佣金扣点名称', '发货模式', '安全库存', '结算方式', '负责部门', '负责人', '买家会员号', '支付宝账号', '联系手机', '联系电话', '联系人', '邮箱地址', '业务类型', '默认退货仓', '备注'], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'storeId',
				align: "center",
				index: 'invdate',
				editable: false,
			},
			{
				name: 'storeName',
				align: "center",
				index: 'id',
				editable: false,
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
				id: 'ecName',
				index: 'id',
				editable: false,
			},
			{
				name: 'noAuditId',
				align: "center",
				index: 'invdate',
				editable: false,
			},
			{
				name: 'noAuditName',
				align: "center",
				index: 'id',
				editable: false,
			},

			{
				name: 'salesType',
				align: "center",
				index: 'id',
				editable: false,
			},
			{
				name: 'brokId',
				align: "center",
				index: 'id',
				editable: false,
			},
			{
				name: 'brokName',
				align: "center",
				index: 'id',
				editable: false,
			},
			{
				name: 'deliverMode',
				align: "center",
				index: 'id',
				editable: false,
			},
			{
				name: 'safeInv',
				align: "center",
				index: 'id',
				editable: false,
			},
			{
				name: 'clearingForm',
				align: "center",
				index: 'id',
				editable: false,
			},
			{
				name: 'respDep',
				align: "center",
				index: 'id',
				editable: false,
			},
			{
				name: 'respPerson',
				align: "center",
				index: 'id',
				editable: false,
			},
			{
				name: 'sellerId',
				align: "center",
				index: 'id',
				editable: false,
			},
			{
				name: 'alipayNo',
				align: "center",
				index: 'id',
				editable: false,
			},
			{
				name: 'mobile',
				align: "center",
				index: 'id',
				editable: false,
			},
			{
				name: 'phone',
				align: "center",
				index: 'id',
				editable: false,
			},
			{
				name: 'linkman',
				align: "center",
				index: 'id',
				editable: false,
			},
			{
				name: 'email',
				align: "center",
				index: 'id',
				editable: false,
			},
			{
				name: 'business',
				align: "center",
				index: 'id',
				editable: false,
			},
			{
				name: 'defaultRefWhs',
				align: "center",
				index: 'id',
				editable: false,
			},
			{
				name: 'memo',
				align: "center",
				index: 'invdate',
				editable: false,
			},

		],
		autowidth: true,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 10,
		scrollrows:true,
		rowList: [10, 20, 30], //可供用户选择一页显示多少条
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		multiboxonly: true,
		caption: "店铺档案列表", //表格的标题名字
		pager: '#List_jqGridPager', //表格页脚的占位符(一般是div)的id
	});

}

//查询按钮
$(document).on('click', '.findList', function() {
	if($("input[name='ecId']").val() == '') {
		var ecId = localStorage.pro
	} else if($("input[name='ecId']").val() != '') {
		var ecId = $("input[name='ecId']").val();
	}
	var storeId = $("input[name='storeId']").val();
	var brokId = $("input[name='brokId']").val();
	var respDep = $("input[name='respDep']").val();
	var respPerson = $("input[name='respPerson']").val();
	var business = $("input[name='business']").val();
	var defaultRefWhs = $("input[name='defaultRefWhs']").val();
	var storeName = $("input[name='storeName']").val();
	var startDate = $("input[name='startDate']").val();
	var endDate = $("input[name='endDate']").val();
	var data3 = {
		reqHead,
		"reqBody": {
			"storeId": storeId,
			"brokId": brokId,
			"ecId": ecId,
			"respDep": respDep,
			"respPerson": respPerson,
			"business": business,
			"defaultRefWhs": defaultRefWhs,
			"storeName": storeName,
			"startDate": startDate,
			"endDate": endDate,
			"pageNo": 1,
			"pageSize": 500
		}
	};
	var postD3 = JSON.stringify(data3);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/ec/storeRecord/queryList',
		async: true,
		data: postD3,
		dataType: 'json',
		success: function(data) {
			window.localStorage.removeItem("pro");
			var rowNum = $("#_input").val()
			var list = data.respBody.list;
			myDate = list;
			$("#List_jqgrids").jqGrid('clearGridData');
			$("#List_jqgrids").jqGrid('setGridParam', {
				rowNum:rowNum,
				datatype: 'local',
				data: myDate, //newData是符合格式要求的重新加载的数据
				page: 1 //哪一页的值
			}).trigger("reloadGrid")
		},
		error: function(error) {
			console.log(error)
		}
	});
})