var count;
var pages;
var page = 1;
var rowNum;
$(function() {
	
		$("#mengban").hide()
		$(".down").hide()
})
$(function() {
	//点击表格图标显示存货列表
	$(document).on('click', '.storeI_dbiaoge', function() {
	$(".box").hide()
		$(".formSave_box").css("opacity", 1)
	});
	$(document).on('click', '.storeId1_biaoge', function() {
		if($("select[name='ecId']").val() != null) {
			window.open("../../Components/baseDoc/storeRecordList.html", 'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
		} else {
			alert("请选择电商平台")
		}
	});
})


$(function() {
	var serviceId;
	$(window).resize(function() {
		$("#jqgrids").setGridWidth($(window).width());
	});
	//	$(document).click(function(){
	//		serviceId =$('input:checked').parent().parent().children()[2].innerHTML;
	//
	//	})

	//审核

	$('.Audit').click(function() {
		var num = []
		var ids = $("#jqgrids").jqGrid('getGridParam', 'selarrrow');
		for(var i = 0;i<ids.length;i++) {
			var rowData = $("#jqgrids").jqGrid('getRowData', ids[i]);
			var serviceId = rowData.serviceId
			num.push(serviceId)
		}
		var sum = num.toString()
		//获得行数据
		var data = {
			'reqHead':reqhead,
			"reqBody": {
				"serviceId": sum
			}
		};
		var postData = JSON.stringify(data);
		if(ids.length == '') {
			alert("请选择单据")
		} else {
			$.ajax({
				type: "post",
				url: url3 + "/mis/ec/aftermarket/audit",
				async: true,
				data: postData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(msgAdd) {
					alert(msgAdd.respHead.message)
				},
				error: function(err) {
					console.log("失败")
				}
			});
		}
	})
})

	var pageNo = 1;
	var rowNum = 10;
//var height = $(window).height() * 0.6
$(function() {
	//页面加载完成之后执行	
	pageInit1();
	//点击右边条数修改显示行数
	$(".ui-pg-selbox.ui-widget-content.ui-corner-all").click(function() {
		pageNo = $("#jqgrids").jqGrid("getGridParam", "page");
		rowNum = $("select[class='ui-pg-selbox ui-widget-content ui-corner-all']")[0].value >> 0;
		var data3 = {
			reqhead,
			"reqBody": {
				"pageSize": rowNum,
				"pageNo": pageNo
			}
		};
		var postD3 = JSON.stringify(data3);
		jQuery("#jqgrids").jqGrid({
			url: url3 + "/mis/ec/aftermarket/queryList", //组件创建完成之后请求数据的url
			mtype: "post",
			datatype: "json", //请求数据返回的类型。可选json,xml,txt
			postData: postD3,
			ajaxGridOptions: {
				contentType: 'application/json; charset=utf-8'
			},

			rowList: [10, 20, 30], //可供用户选择一页显示多少条
			autowidth: true,
			pager: '#jqgridPager', //表格页脚的占位符(一般是div)的id
			sortname: 'applyId', //初始化的时候排序的字段
			sortorder: "desc", //排序方式,可选desc,asc
			viewrecords: true,
			rowNum: rowNum, //一页显示多少条
			pageNo: pageNo,
			localReader: {
				root: "respBody.list", // json中代表实际模型数据的入口
				records: "respBody.count", // json中代表数据行总数的数据		            
				total: "respBody.pages", // json中代表页码总数的数据
				repeatitems: true,
			},
			onPaging: function(pgButton) {
				pageNo = $("#jqgrids").jqGrid("getGridParam", "page");
				rowNum = $("select[class='ui-pg-selbox ui-widget-content ui-corner-all']")[0].value >> 0;
				if(pgButton === 'prev') {
					pageNo -= 1;
				} else if(pgButton === 'next') {
					pageNo += 1;

				} else if(pgButton === 'records') {
					pageNo = 1;
				}
			}
		});
	})


});


$(document).on('click', '#finds', function() {
	search()
})

//查询按钮
function search() {
	var myDate = {};
	var storeId = $("input[name='storeId1']").val();
	var orderId = $("input[name='orderId']").val();
	var serviceId = $("input[name='serviceId']").val();
	var startDate =$('input[name="startDate1"]').val();
	var endDate =$('input[name="endDate1"]').val();
	var isAudit =$('select[name="isAudit"]').val();
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var data2 = {
		reqHead,
		"reqBody": {
			"storeId": storeId,
			"isAudit": isAudit,
			"startTime": startDate,
			"endTime": endDate,
			"orderId":orderId,
			"serviceId":serviceId,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(data2);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url3 + "/mis/ec/aftermarket/queryList",
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			var mydata = {};
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#jqgrids").jqGrid("clearGridData");
			$("#jqgrids").jqGrid("setGridParam", {
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

function pageInit1() {
	//初始化表格
	jQuery("#jqgrids").jqGrid({
		url: "../../assets/js/json/order.json", //组件创建完成之后请求数据的url
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['服务单号', '申请单号', '申请时间', '客户期望', '客户期望名称', '服务单状态', '服务单状态名称', '客户账号'
		, '客户名称', '用户级别',
			'用户电话', '取件地址', '订单号', '订单类型', '订单类型名称', '实付金额', 'sku编码', '商品类型', '商品类型名称'
			, '商品名称', '审核人账号', '审核人名称',
			'审核时间', '审核结果', '审核结果名称', '处理人账号', '处理人', '处理时间', '处理结果', '处理结果名称'
			, '本地库审核状态', '平台编码', '店铺编码','审核提示'
		], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'serviceId',
				align: "center",
				index: 'orderId',
				editable: false,
			},
			{
				name: 'applyId',
				align: "center",
				index: 'buyerId',
				editable: true,
			},
			{
				name: 'applyTime',
				align: "center",
				index: 'recName',
				editable: true,
			},
			{
				name: 'customerExpect',
				align: "center",
				index: 'recMobile',
				editable: true,
			},
			{
				name: 'customerExpectName',
				align: "center",
				index: 'ecOrderId',
				editable: true,
			},
			{
				name: 'serviceStatus',
				align: "center",
				index: 'isInvoice',
				editable: true,
			},
			{
				name: 'serviceStatusName',
				align: "center",
				index: 'onteFlag',
				editable: true,
			},
			{
				name: 'customerPin',
				align: "center",
				index: 'isClose',
				editable: true,
			},
			{
				name: 'customerName',
				align: "center",
				index: 'isShip',
				editable: true,
			},
			{
				name: 'customerGrade',
				align: "center",
				index: 'orderStatus',
				editable: true,
			},
			{
				name: 'customerTel',
				align: "center",
				index: 'returnStatus',
				editable: true,
			},
			{
				name: 'pickwareAddress',
				align: "center",
				index: 'hasGift',
				editable: true,
			},
			{
				name: 'orderId',
				align: "center",
				index: 'startDate',
				editable: true,
			},
			{
				name: 'orderType',
				align: "center",
				index: 'startDate',
				editable: true,
			},
			{
				name: 'orderTypeName',
				align: "center",
				index: 'startDate',
				editable: true,
			},
			{
				name: 'actualPayPrice',
				align: "center",
				index: 'startDate',
				editable: true,
			},
			{
				name: 'skuId',
				align: "center",
				index: 'startDate',
				editable: true,
			},
			{
				name: 'wareType',
				align: "center",
				index: 'startDate',
				editable: true,
			},
			{
				name: 'wareTypeName',
				align: "center",
				index: 'startDate',
				editable: true,
			},
			{
				name: 'wareName',
				align: "center",
				index: 'startDate',
				editable: true,
			},
			{
				name: 'approvePin',
				align: "center",
				index: 'startDate',
				editable: true,
			},
			{
				name: 'approveName',
				align: "center",
				index: 'startDate',
				editable: true,
			},
			{
				name: 'approveTime',
				align: "center",
				index: 'startDate',
				editable: true,
			},
			{
				name: 'approveResult',
				align: "center",
				index: 'startDate',
				editable: true,
			},
			{
				name: 'approveResultName',
				align: "center",
				index: 'startDate',
				editable: true,
			},
			{
				name: 'processPin',
				align: "center",
				index: 'startDate',
				editable: true,
			},
			{
				name: 'processName',
				align: "center",
				index: 'startDate',
				editable: true,
			},
			{
				name: 'processTime',
				align: "center",
				index: 'startDate',
				editable: true,
			},
			{
				name: 'processResult',
				align: "center",
				index: 'startDate',
				editable: true,
			},
			{
				name: 'processResultName',
				align: "center",
				index: 'startDate',
				editable: true,
			},
			{
				name: 'isAudit',
				align: "center",
				index: 'startDate',
				editable: true,
			},
			{
				name: 'ecId',
				align: "center",
				index: 'endDate',
				editable: true,
			},
			{
				name: 'storeId',
				align: "center",
				index: 'endDate',
				editable: true,
			},
			{
				name: 'auditHint',
				align: "center",
				index: 'endDate',
				editable: true,
			}

		],
		rowNum: 10, //一页显示多少条
		rowList: [10, 20, 30], //可供用户选择一页显示多少条			
		autowidth: true,
		rownumbers: true,
		height:'100%',
		autoScroll:true,
		shrinkToFit:false,
		pager: '#jqgridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'applyId', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true,
		caption: "自主售后列表查询", //表格的标题名字	
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#jqgrids").getGridParam('records'); //获取返回的记录数
			page = $("#jqgrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#jqgrids").getGridParam('rowNum'); //获取显示配置记录数量

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

	});

}



$(function() {
	//确定
	$(".sure").click(function() {
		//到货单
		//获得行号
		var rowid = $("#jqgrids").jqGrid('getGridParam', 'selrow');

		//	店铺档案
		//	获得行号
		var gr = $("#List_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDatas = $("#List_jqgrids").jqGrid('getRowData', gr);

		$("input[name='storeId1']").val(rowDatas.storeId)
//		$("input[name='storeName1']").val(rowDatas.storeName)

		$(".formSave_box").css("opacity", 0);
		$(".box").show();

	})
//	取消
	$(".false").click(function() {
		$(".formSave_box").css("opacity", 0);
		$(".box").show();
		//到货单
		//获得行号
//		var rowid = $("#jqgrids").jqGrid('getGridParam', 'selrow');
//		
//		$("input[name='orderId1']").val("");
//		$("input[name='storeId1']").val("");
//		$("input[name='orderId1']").val("");
//		$("input[name='orderId1']").val("");
//		$("input[name='orderId1']").val("");
//		$("#" + rowid + "_whsEncd").val("");
//		$("#" + rowid + "_invtyEncd").val("")
	})
})


$(function() {
	$('.download').click(function() {
		$(".down").css("opacity", 1)
		$(".down").show()
		$("#mengban").show()
/*------点击下载按钮加载动态下拉电商平台-------*/
		var savedata = {
			'reqHead': reqhead,
			'reqBody': {
				'ecId': "",
				'pageNo': 1,
				'pageSize': 500
			},
		};
		var saveData = JSON.stringify(savedata)
		$.ajax({
			type: 'post',
			contentType: 'application/json; charset=utf-8',
			url: url + '/mis/ec/ecPlatform/queryList',
			async: true,
			data: saveData,
			dataType: 'json',
			success: function(data) {
				list = data.respBody.list;
				var option_html = '';
				option_html += '<option value="0" disabled selected>' + "请选择" + "</option>"
				for(i = 0; i < list.length; i++) {
					option_html += '<option value="' + list[i].ecId + '"' + 'id="ab">' + list[i].ecName + "</option>"
//					var proCriteria = list[i].proConditionEncd;
				}
				window.pro = $(".ecId").first().children("option").val()
				$(".ecId").html(option_html);
				$(".ecId").change(function(e) {
					window.val = this.value;
					pro = this.value
					window.localStorage.setItem("pro",pro);
				})
				
			},
			error: function() {
				console.log(error)
			}
		})

	})

	$(".true").click(function() {
		var storeId = $("input[name='storeId']").val()
		if(storeId != '') {
			var storeId1 = $("input[name='storeId']").val()
			var ecOrderId = $("input[name='ecOrderId']").val()

			var download = {
				"reqHead": reqhead,
				"reqBody": {
					'storeId': storeId1,
					'ecOrderId': ecOrderId
				}
			};
			var downloadData = JSON.stringify(download)
			$.ajax({
				type: "post",
				url: url3 + "/mis/ec/aftermarket/download",
				async: true,
				data: downloadData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(msgAdd) {
					alert(msgAdd.respHead.message)
					//				window.location.reload();
					$("#mengban").hide()
					$(".down").css("opacity", 0)
					$(".down").hide()
					$("input[name='storeId']").val('')
					$("input[name='ecOrderId']").val('')
				},
				error: function() {
					console.log("下载失败")

				}
			});
		}
		else if(storeId == '') {
			alert("请选择店铺")
		}
	})


	$(".falses").click(function() {
		$("#mengban").hide()
		$(".down").css("opacity", 0)
		$(".down").hide()

	})
})
