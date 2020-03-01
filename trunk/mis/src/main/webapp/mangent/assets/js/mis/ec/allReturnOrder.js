

//表格初始化
$(function() {
	$(".sure").removeClass("gray")
	$('.searcher').removeClass("gray")
	$('.cancel').removeClass("gray")
	$('button').attr('disabled', false);
	$(".gray").attr('disabled', true)
	var rowNum = $("#_input").val()
	$("#jqGrids_list").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['店铺', '订单编码', '付款时间', '商品数量', '商品金额',
			'实付金额', '买家留言', '卖家备注', '快递公司', '收货人详细地址', '买家会员号',
			'收货人姓名', '收货人手机号', '电商订单号', '是否开票', '发票抬头', '卖家备注'
		],
		colModel: [{
				name: 'storeName',
				editable: true,
				align: 'center',
				sortable: false,

			},
			{
				name: 'orderId',
				editable: true,
				align: 'center'

			},
			{
				name: 'payTime',
				editable: false,
				align: 'center',
			},
			{
				name: 'goodNum',
				editable: false,
				align: 'center'
			},
			{
				name: 'goodMoney',
				editable: false,
				align: 'center'
			},
			{
				name: 'payMoney',
				editable: false,
				align: 'center',
			},
			{
				name: 'buyerNote',
				editable: true,
				align: 'center'
			},
			{
				name: 'sellerNote',
				editable: true,
				align: 'center'
			},
			{
				name: 'sellerNote', //快递
				editable: false,
				align: 'center'
			},
			{
				name: 'recAddress',
				editable: false,
				align: 'center'
			},
			{
				name: 'buyerId',
				editable: false,
				align: 'center'
			},
			{
				name: 'recName',
				editable: false,
				align: 'center'
			},
			{
				name: 'recMobile',
				editable: false,
				align: 'center'
			},
			{
				name: 'ecOrderId',
				editable: false,
				align: 'center'
			},
			{
				name: 'isInvoice', //是否开票
				editable: false,
				align: 'center'
			},
			{
				name: 'invoiceTiel',
				editable: false,
				align: 'center'
			},
			{
				name: 'onteFlag',
				editable: false,
				align: 'center'
			},
		],
		autowidth: true,
		rownumbers: true,
		loadonce: false,
		forceFit: true,
		rowNum: rowNum,
		pager: '#jqGridPager_list', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "订单明细", //表格的标题名字
	})
})

//查询按钮
$(document).on('click', '.searcher', function() {
	var myDate = {};

	var invtyEncd = $(".invtyEncd").val();
	var pursOrdrId = $(".pursOrdrId").val();
	var provrId = $(".provrId").val();
	var pursOrdrDt1 = $(".pursOrdrDt1").val();
	var pursOrdrDt2 = $(".pursOrdrDt2").val();

	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": invtyEncd,
			"pursOrdrId": pursOrdrId,
			"provrId": provrId,
			"pursOrdrDt1": pursOrdrDt1,
			"pursOrdrDt2": pursOrdrDt2,
			"isNtChk":1,
			"pageNo": 1,
			"pageSize": 500
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/ec/platOrder/queryList',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			var list = data.respBody.list;
			myDate = list;
			var rowNum = $("#_input").val()
			$("#jqGrids_list").jqGrid({
				data: myDate,
				datatype: "local",
			});
			$("#jqGrids_list").jqGrid('clearGridData');
			$("#jqGrids_list").jqGrid('setGridParam', {
				datatype: 'local',
				rowNum: rowNum,
				data: myDate, //newData是符合格式要求的重新加载的数据
				page: 1 //哪一页的值
			}).trigger("reloadGrid")
		},
		error: function() {
			console.log(error)
		}
	});
})

$(function() {
	$("#last_jqGridPager_list").after('<input id="_input" type="text" value="10"/>')
})