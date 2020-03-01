//初始化表格
$(function() {
//	allHeight()
	$("#mu_jqGrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json",
		colNames: ['订单编号', '店铺编号', '店铺名称', '是否客审','商品数量', '实付金额', '买家会员号', '电商订单号'], //jqGrid的列显示名字
		colModel: [{
				name: 'orderId',
				editable: false,
				align: 'center',
				sortable: false,
			},
			{
				name: 'storeId',
				editable: false,
				align: 'center',
				/*hidden: true,*/
				sortable: false
			},
			{
				name: 'storeName',
				editable: false,
				align: 'center',
				sortable: false

			},
			{
				name: 'isAudit',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'goodNum',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'payMoney',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'buyerId',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'ecOrderId',
				editable: false,
				align: 'center',
				sortable: false
			}
		],
		autowidth: true,
		height:200,
		autoScroll:true,
		shrinkToFit:false,
		rownumbers: true,
		loadonce: false,
		forceFit: true,
		pager: '#mu_jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		multiboxonly:true,
		caption: "母表", //表格的标题名字
		onSelectRow: function() {
			searchAll();
		}
	})
})

function searchAll() {
	var gr = $("#mu_jqGrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowDatas = $("#mu_jqGrids").jqGrid('getRowData', gr);
	var orderId = rowDatas.orderId;
	var ecOrderId = localStorage.getItem("refundOderId")
	var myData = {};
	var Data = {
		"reqHead": reqhead,
		"reqBody": {
			"ecOrderId": ecOrderId,
			"orderId": orderId,
		}
	};
	var saveData = JSON.stringify(Data);
	console.log(saveData)
	$.ajax({
		type: "post",
		url: url + '/mis/ec/refundOrder/refundReference',
		async: true,
		data: saveData,
		dataType: 'json',
		contentType: 'application/json;charset=utf-8',
		success: function(data) {
			console.log(data)
			var list = data.respBody.list;
			var mes = data.respHead.isSuccess
			for(var i =0;i<list.length;i++) {
				var listAll = data.respBody.list[i].platOrdersList;
				for(var j = 0;j<listAll.length;j++) {
					if(listAll[j].isGift == 0) {
						listAll[j].isGift = "否"
					} else {
						listAll[j].isGift = "是"
					}
				}
			}
//			myDate = list;
			if(mes == true) {
				$("#zi_jqGrids").jqGrid('clearGridData');
				$("#zi_jqGrids").jqGrid('setGridParam', {
					//				rowNum:rowNum,
					datatype: 'local',
					data: listAll, //newData是符合格式要求的重新加载的数据
					page: 1 //哪一页的值
				}).trigger("reloadGrid")
			} else {
				alert(data.respHead.message)
			}
		},
		error: function() {
			alert("错误")
		}
	});
}


//初始化表格
$(function() {
//	allHeight()
	$("#zi_jqGrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json",
		colNames: ['存货编码', '存货名称','可退数量','可退金额',
		'批号','生产日期','失效日期','是否赠品','店铺商品编码','平台sku'
		], //jqGrid的列显示名字
		colModel: [
			{
				name: 'invId',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'invtyName',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'canRefNum',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'canRefMoney',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'batchNo',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'prdcDt',
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
			{
				name: 'isGift',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'goodId',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'goodSku',
				editable: false,
				align: 'center',
				sortable: false
			}
		],
		autowidth: true,
		height:200,
		autoScroll:true,
		shrinkToFit:false,
		rownumbers: true,
		loadonce: false,
		forceFit: true,
		pager: '#zi_jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		caption: "子表", //表格的标题名字
	})
})