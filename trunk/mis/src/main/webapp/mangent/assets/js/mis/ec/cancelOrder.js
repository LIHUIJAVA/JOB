$(function() {
	
		$("#mengban").hide()
		$(".down").hide()
})

$(function() {
	$(document).on('click', '.storeId1_biaoge', function() {
		$(".box").hide()
		$(".formSave_box").css("opacity", 1)
	});
	$(document).on('click', '.storeId_biaoge', function() {
		if($("select[name='ecId']").val() != null) {
			window.open("../../Components/baseDoc/storeRecordList.html", 'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
		} else {
			alert("请选择电商平台")
		}
	});
})
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
	pageInit7();
})

//页面初始化

function pageInit7() {
//	allHeight()
	pageNo = 1;
	rowNum = 10;
	
	var startTime = $("input[name='startTime']").val();
	var endTime = $("input[name='endTime']").val();
	//创建jqGrid组件	
	var data3 = {
		"reqHead": reqhead,
		"reqBody": {
			"endTime":endTime,
			"startTime": startTime,
			"pageNo": 1,
			"pageSize": 500
		}
	};
	var postD3 = JSON.stringify(data3);
	//初始化表格
	jQuery("#jqgrids").jqGrid({
		url: url3 + "/mis/ec/cancelOrder/queryList", //组件创建完成之后请求数据的url
		mtype: "post",
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		postData: postD3,
		ajaxGridOptions: {
			contentType: 'application/json; charset=utf-8'
		},
		colNames: ['退款单编码', '客户账号', '客户姓名', '审核时间', '申请时间', '退款金额', '审核状态', '审核人', '订单号', '审核备注',
			'退款原因', '退款来源', '本地库审核状态', '电商平台编码', '店铺编码'
		], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'id',
				align: "center",
				index: 'orderId',
				editable: false,
			},
			{
				name: 'buyerId',
				align: "center",
				index: 'buyerId',
				editable: true,
			},
			{
				name: 'buyerName',
				align: "center",
				index: 'recName',
				editable: true,
			},
			{
				name: 'checkTime',
				align: "center",
				index: 'recMobile',
				editable: true,
			},
			{
				name: 'applyTime',
				align: "center",
				index: 'ecOrderId',
				editable: true,
			},
			{
				name: 'applyRefundSum',
				align: "center",
				index: 'isInvoice',
				editable: true,
			},
			{
				name: 'status',
				align: "center",
				index: 'onteFlag',
				editable: true,
			},
			{
				name: 'checkUserName',
				align: "center",
				index: 'isClose',
				editable: true,
			},
			{
				name: 'orderId',
				align: "center",
				index: 'isShip',
				editable: true,
			},
			{
				name: 'checkRemark',
				align: "center",
				index: 'orderStatus',
				editable: true,
			},
			{
				name: 'reason',
				align: "center",
				index: 'returnStatus',
				editable: true,
			},
			{
				name: 'systemId',
				align: "center",
				index: 'hasGift',
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
			}

		],
		jsonReader: {
			root: "respBody.list",
			repeatitems: true,
			records: "respBody.count", // json中代表数据行总数的数据		            
			total: "respBody.pages", // json中代表页码总数的数据
		},
		autowidth:true,
		height:350,
		autoScroll:true,
		shrinkToFit:false,
		viewrecords: true,
		rownumbers: true,
		loadonce: true,
		forceFit: true, //调整列宽度不会改变表格的宽度
		rowNum: 10,
		rowList: [10, 20, 30], //可供用户选择一页显示多少条
		pager: '#jqgridPager', //表格页脚的占位符(一般是div)的id

		multiselect: true, //复选框
		caption: "订单取消列表查询", //表格的标题名字	

		ondblClickRow: function() {
			mType = 2;
			var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
			var rowDatas = $("#jqgrids").jqGrid('getRowData', gr); //获取行数据
			$('#jqgrids').editRow(gr, true);
			$("#" + gr + "_deptId").attr("readonly", "readonly");
		}

	});
}

$(document).on('click', '#finds', function() {
	search()
})

//查询按钮
function search() {
	
	var myDate = {};
	var storeId = $("input[name='storeId1']").val();
	var orderId = $('input[name="orderId"]').val();
	var isAudit = $('select[name="isAudit"]').val();
	var startTime = $('input[name="startTime"]').val();
	var endTime = $('input[name="endTime"]').val();
	var data2 = {
		reqHead,
		"reqBody": {
			"storeId": storeId,
			"orderId": orderId,
			"endTime":endTime,
			"startTime": startTime,
			"isAudit": isAudit,
			"pageNo": 1,
			"pageSize": 500
		}
	};
	var saveData = JSON.stringify(data2);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url3 + "/mis/ec/cancelOrder/queryList",
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			var list = data.respBody.list;
			for(var i = 0;i<list.length;i++){
				if(list[i].isAudit==0){
					list[i].isAudit="否"
				}else if(list[i].isAudit==1){
					list[i].isAudit="是"
				}
			}
			myDate = list;
			var rowNum = $("#_input").val()
			$("#jqgrids").jqGrid({
				data: myDate,
				datatype: "local",
			});
			$("#jqgrids").jqGrid('clearGridData');
			$("#jqgrids").jqGrid('setGridParam', {
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
}

$(function() {

	//审核
	$('.Audit').click(function() {
		var gr = $("#jqgrids").jqGrid('getGridParam', 'selarrrow');
		var num = []
		for(var i = 0;i<gr.length;i++) {
			var rowDatas = $("#jqgrids").jqGrid('getRowData', gr[i]);
			var id = rowDatas.id
			num.push(id)
		}
		var ids = num.toString()
		//获得行数据
		var data = {
			reqhead,
			"reqBody": {
				"id": ids
			}
		};
		var postData = JSON.stringify(data);
		$.ajax({
			type: "post",
			url: url3 + "/mis/ec/cancelOrder/audit",
			async: true,
			data: postData,
			dataType: 'json',
			contentType: 'application/json',
			success: function(msgAdd) {
				alert(msgAdd.respHead.message)
				search()
			},
			error: function(err) {
				console.log("失败")
			}
		});

	})
})

//$(function() {
//	$("#last_jqgridPager").after('<input id="_input" type="text" value="10"/>')
//})

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
//				alert("无订单号按照店铺编码下载")
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
				url: url3 + "/mis/ec/cancelOrder/download",
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
					search()
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
