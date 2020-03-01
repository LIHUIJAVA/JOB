$(function() {
	$("#mengban").hide()
	$(".down").hide()
})

$(function() {
	$(document).on('click', '.storeId1_biaoge', function() {
		window.open("../../Components/baseDoc/storeRecordList.html", 'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	$(document).on('click', '.storeId_biaoge', function() {
		$("#purchaseOrder").hide()
		$(".box").hide()
		$("#wwrap").hide()
		$(".formSave_box").show()
		$(".formSave_box").css("opacity", 1)
		$("#wwrap").css("opacity", 0)
		$('.sure').removeClass("gray") //增加
		$('.false').removeClass("gray") //增加
		$('#find').removeClass("gray") //增加
	});
})


$(function() {
	//审核
	$('.Audit').click(function() {
		var num = []
		var ids = $("#jqgrids").jqGrid('getGridParam', 'selarrrow');
		for(var i = 0; i< ids.length;i++) {
			//获得行数据
			var rowData = $("#jqgrids").jqGrid('getRowData', ids[i]);
			var compensateId = rowData.compensateId
			num.push(compensateId)
		}
		var nums = num.toString()
		var data = {
			reqHead,
			"reqBody": {
				"compensateId": nums
			}
		};
		var postData = JSON.stringify(data);
		if(ids.length == '') {
			alert("请选择单据")
		} else {
			$.ajax({
				type: "post",
				url: url3 + "/mis/ec/compensate/audit",
				async: true,
				data: postData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(msgAdd) {
					alert(msgAdd.respHead.message)
					search()
				},
				error: function(err) {
					console.log("审核失败")
				}
			});
		}
	})
})

$(function() {
	//页面加载完成之后执行	
	var pageNo = 1;
	var rowNum = 10;
	pageInit();
	//点击右边条数修改显示行数
	$(".ui-pg-selbox.ui-widget-content.ui-corner-all").click(function() {
		pageNo = $("#jqgrids").jqGrid("getGridParam", "page");
		rowNum = $("select[class='ui-pg-selbox ui-widget-content ui-corner-all']")[0].value >> 0;
		var data3 = {
			reqHead,
			"reqBody": {
				"pageSize": rowNum,
				"pageNo": pageNo
			}
		};
		var postD3 = JSON.stringify(data3);
		jQuery("#jqgrids").jqGrid({
			url: url3 + "/mis/ec/compensate/queryList", //组件创建完成之后请求数据的url
			mtype: "post",
			datatype: "json", //请求数据返回的类型。可选json,xml,txt
			postData: postD3,
			ajaxGridOptions: {
				contentType: 'application/json; charset=utf-8'
			},

			rowList: [10, 20, 30], //可供用户选择一页显示多少条
			autowidth: true,
			pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
			sortname: 'compensateId', //初始化的时候排序的字段
			sortorder: "desc", //排序方式,可选desc,asc
			viewrecords: true,
			rowNum: rowNum, //一页显示多少条
			pageNo: pageNo,
			jsonReader: {
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
	var storeId = $("input[name='storeId']").val();
	var ecOrderId = $('input[name="ecOrderId"]').val();
	var compensateId = $('input[name="compensateId"]').val();
	var startTime = $('input[name="startTime"]').val();
	var endTime = $('input[name="endTime"]').val();
	var checkStatus = $('select[name="checkStatus"]').val();
	var isAudit = $('select[name="isAudit"]').val();
	var data2 = {
		reqHead,
		"reqBody": {
			"storeId": storeId,
			"ecOrderId": ecOrderId,
			"compensateId": compensateId,
			"startTime":startTime,
			"endTime":endTime,
			"checkStatus":checkStatus,
			"isAudit":isAudit,
			"pageNo": 1,
			"pageSize": 9999999
		}
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/ec/compensate/queryList',
		async: true,
		data: postD2,
		dataType: 'json',
		success: function(data) {
			var rowNum = $("#_input").val()
			var list = data.respBody.list;
			for(var i = 0; i < list.length; i++) {
				if(list[i].type == 10) {
					list[i].type = "延迟发货赔付"
				} else if(list[i].type == 20) {
					list[i].type = "商家直赔"
				} else if(list[i].type == 30) {
					list[i].type = "商家直赔"
				} else if(list[i].type == 40) {
					list[i].type = "先行赔付非运费"
				}

				if(list[i].orderType == 21) {
					list[i].orderType = "fbp"
				} else if(list[i].orderType == 22) {
					list[i].orderType = "sop"
				} else if(list[i].orderType == 23) {
					list[i].orderType = "lbp"
				} else if(list[i].orderType == 25) {
					list[i].orderType = "sopl"
				}

				if(list[i].compensateType == 10) {
					list[i].compensateType = "积分"
				} else if(list[i].compensateType == 20) {
					list[i].compensateType = "余额"
				} else if(list[i].compensateType == 30) {
					list[i].compensateType = "优惠券"
				} else if(list[i].compensateType == 40) {
					list[i].compensateType = "京豆"
				}

				if(list[i].checkStatus == 0) {
					list[i].checkStatus = "待审核"
				} else if(list[i].checkStatus == 1) {
					list[i].checkStatus = "审核通过"
				} else if(list[i].checkStatus == 2) {
					list[i].checkStatus = "审核不通过"
				} else if(list[i].checkStatus == 3) {
					list[i].checkStatus = "审核通过"
				} else if(list[i].checkStatus == 12) {
					list[i].checkStatus = "审核通过"
				}

				if(list[i].erpCheckStatus == 11) {
					list[i].erpCheckStatus = "客服审核为京东责任"
				} else if(list[i].erpCheckStatus == 12) {
					list[i].erpCheckStatus = "客服审核为商家责任"
				} else if(list[i].erpCheckStatus == 21) {
					list[i].erpCheckStatus = "运营审核为商家责任"
				} else if(list[i].erpCheckStatus == 22) {
					list[i].erpCheckStatus = "运营审核为商家责任"
				} else if(list[i].erpCheckStatus == 0) {
					list[i].erpCheckStatus = "审核通过"
				}

				if(list[i].canSecondAppeal == 0) {
					list[i].canSecondAppeal = "可以"
				} else if(list[i].canSecondAppeal == 1) {
					list[i].canSecondAppeal = "不可以"
				}
				
				if(list[i].isAudit == 0) {
					list[i].isAudit = "否"
				} else if(list[i].isAudit == 1) {
					list[i].isAudit = "是"
				}
			}
			myDate = list;
			$("#jqgrids").jqGrid('clearGridData');
			$("#jqgrids").jqGrid('setGridParam', {
				rowNum: rowNum,
				datatype: 'local',
				data: myDate, //newData是符合格式要求的重新加载的数据
				page: 1 //哪一页的值
			}).trigger("reloadGrid")
		},
		error: function() {
			console.log(error)
		}
	});
}



function pageInit() {
	allHeight()
	jQuery("#jqgrids").jqGrid({
		url: "../../assets/js/json/order.json", //组件创建完成之后请求数据的url
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['赔付单号', '赔付子单号', '商家编码', '赔付类型', '订单号', '订单类型', '更新日期', 
			'创建日期', '赔付金额类型', '应赔付金额','赔付金额', '赔付原因',
			'商家审核状态', '运营，客服审核状态', '是否可二次申诉',
			'本地库审核状态', '电商平台编码', '店铺编码','下载时间','审核时间','审核人id','审核人','店铺名称'
		], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'compensateId',
				align: "center",
				index: 'orderId',
				editable: false,
			},
			{
				name: 'compensateKeyid',
				align: "center",
				index: 'buyerId',
				editable: true,
			},
			{
				name: 'venderId',
				align: "center",
				index: 'recName',
				editable: true,
			},
			{
				name: 'type',
				align: "center",
				index: 'recMobile',
				editable: true,
			},
			{
				name: 'orderId',
				align: "center",
				index: 'ecOrderId',
				editable: true,
			},
			{
				name: 'orderType',
				align: "center",
				index: 'isInvoice',
				editable: true,
			},
			{
				name: 'modified',
				align: "center",
				index: 'onteFlag',
				editable: true,
			},
			{
				name: 'created',
				align: "center",
				index: 'isClose',
				editable: true,
			},
			{
				name: 'compensateType',
				align: "center",
				index: 'isShip',
				editable: true,
			},
			{
				name: 'shouldPay',
				align: "center",
				index: 'orderStatus',
				editable: true,
			},
			{
				name: 'compensateAmount',
				align: "center",
				index: 'returnStatus',
				editable: true,
			},
			{
				name: 'compensateReason',
				align: "center",
				index: 'hasGift',
				editable: true,
			},
			{
				name: 'checkStatus',
				align: "center",
				index: 'startDate',
				editable: true,
			},
			{
				name: 'erpCheckStatus',
				align: "center",
				index: 'startDate',
				editable: true,
			},
			{
				name: 'canSecondAppeal',
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
				name: 'downloadTime',
				align: "center",
				index: 'endDate',
				editable: true,
			},
			{
				name: 'auditTime',
				align: "center",
				index: 'endDate',
				editable: true,
			},
			{
				name: 'auditUserId',
				align: "center",
				index: 'endDate',
				editable: true,
			},
			{
				name: 'auditUserName',
				align: "center",
				index: 'endDate',
				editable: true,
			},
			{
				name: 'storeName',
				align: "center",
				index: 'endDate',
				editable: true,
			}

		],
		rowNum: 10, //一页显示多少条
		rowList: [10, 20, 30], //可供用户选择一页显示多少条
		autowidth:true,
		multiselect: true, //复选框
//		multiboxonly: true,
		height:height,
		autoScroll:true,
		shrinkToFit:false,
		viewrecords: true, //定义是否要显示总记录数
		rownumbers: true,
		forceFit: true, //调整列宽度不会改变表格的宽度
		loadonce: true, //将loadonce为 true去掉后，翻页栏（下一页，上一页那些）全是灰色的，点击无反应。而且在点击时，在后台也不见触发到任何事件。但点击刷新等按钮时会去后台取数据。
		sortname: 'compensateId', //初始化的时候排序的字段
		pager: '#jqgridPager', //表格页脚的占位符(一般是div)的id
		sortorder: "desc", //排序方式,可选desc,asc
//		jsonReader: {
//			records: "respBody.count", // json中代表数据行总数的数据	
//			root: "respBody.list", // json中代表实际模型数据的入口
//			total: "respBody.pages", // json中代表页码总数的数据
//			repeatitems: true
//
//		},
		//		multiselect: true,
		caption: "订单赔付列表查询", //表格的标题名字	
		//此事件发生在行双击后发生。
		ondblClickRow: function() {
			mType = 2;
			var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
			var rowDatas = $("#jqgrids").jqGrid('getRowData', gr); //获取行数据
			$('#jqgrids').editRow(gr, true);
			$("#" + gr + "_deptId").attr("readonly", "readonly");
		}

	});
}


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
		var storeId = $("input[name='storeId1']").val()
		if(storeId != '') {
			var storeId1 = $("input[name='storeId1']").val()
			var ecOrderId = $("input[name='ecOrderId1']").val()

			var download = {
				"reqHead": reqhead,
				"reqBody": {
					'storeId': storeId1,
					'ecOrderId': ecOrderId,
				}
			};
			var downloadData = JSON.stringify(download)
			$.ajax({
				type: "post",
				url: url3 + "/mis/ec/compensate/download",
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


//打开部门档案后点击确定取消
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

		$("input[name='storeId']").val(rowDatas.storeId)
//		$("input[name='storeName1']").val(rowDatas.storeName)

		$(".formSave_box").css("opacity", 0);
		$(".box").show();
		$(".formSave_box").hide();
	})
//	取消
	$(".false").click(function() {
		$(".formSave_box").css("opacity", 0);
		$(".box").show();
		$("input[name='storeId']").val('')
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