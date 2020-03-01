var count;
var pages;
var page = 1;
var rowNum;
//本月第一天
function getCurrentMonthFirst1() {
	var date = new Date();
	date.setDate(1);
	var month = parseInt(date.getMonth() + 1);
	var day = date.getDate();
	if(month < 10) {
		month = '0' + month
	}
	if(day < 10) {
		day = '0' + day
	}
	
	var firstDates = date.getFullYear() + '-' + month + '-' + day +  ' 00:00:00';
	return firstDates
}
$(function() {
	window.dates = getCurrentMonthFirst1();
	$(".f_date").val(dates)
	$(".l_date").val(new Date().format("yyyy-MM-dd hh:mm:ss"))
})


$(function() {
	$(document).on('click', '.storeId_biaoge', function() {
		$(".box").hide()
		$(".formSave_box").css("opacity", 1)
		$("#purchaseOrder").hide()
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
	});
	//点击表格图标显示存货列表
//	$(document).on('click', '.storeId_biaoge', function() {
//		window.open("../../Components/baseDoc/storeRecordList.html", 'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
//	});
	$("#mengban").hide()
	$(document).on('click', '.storeId1_biaoge', function() {
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
		var rowid = $("#jqGrids").jqGrid('getGridParam', 'selrow');

		//	店铺档案
		//	获得行号
		var gr = $("#List_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDatas = $("#List_jqgrids").jqGrid('getRowData', gr);

		$("input[name='storeId']").val(rowDatas.storeId)
		$("input[name='storeNm1']").val(rowDatas.storeName)

		$(".formSave_box").css("opacity", 0);
		$("#purchaseOrder").show()
	})
//	取消
	$(".false").click(function() {
		$(".formSave_box").css("opacity", 0);
		$("#purchaseOrder").show()
		//到货单
		//获得行号
//		var rowid = $("#jqgrids").jqGrid('getGridParam', 'selrow');
//		
//		$("#" + rowid + "_whsNm").val("");
//		$("#" + rowid + "_whsEncd").val("");
//		$("#" + rowid + "_invtyEncd").val("")
	})
})


//表格初始化
$(function() {
	allHeight()
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	var rowNum = $("#_input").val()
	$("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['退款单编码', '订单编码', '电商平台编码', '电商订单号', '寄回快递单号', '店铺编码', '店铺名称',
			'电商退款单号', '审核提示', '申请日期', '买家会员号', '是否退货','整单退货数量','整单退款金额','退款原因',
			'退款说明','退款状态','下载时间','处理日期','操作员','是否审核','表头备注'
		],
		colModel: [{
				name: 'refId',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'orderId',
				editable: true,
				align: 'center',
				sortable: false

			},
			{
				name: 'ecId',
				editable: true,
				align: 'center',
				hidden:true,
				sortable: false
			},
			{
				name: 'ecOrderId',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'expressCode',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'storeId',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'storeName',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'ecRefId',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'auditHint',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'applyDate',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'buyerId',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'isRefund',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'allRefNum',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'allRefMoney',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'refReason',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'refExplain',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'refStatus',
				editable: false,
				align: 'center',
				hidden:true,
				sortable: false
			},
			{
				name: 'downTime',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'treDate',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'operator',
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
				name: 'memo',
				editable: false,
				align: 'center',
				sortable: false
			},
		],
		autowidth: true,
		height:300,
		autoScroll:true,
		sortable:true,
		shrinkToFit:false,
		rownumbers: true,
		loadonce: false,
		rowNum: 500, //一页显示多少条
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条	
		forceFit: true,
		pager: '#r_jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
//		multiboxonly:true,
		caption: "退款单列表", //表格的标题名字
		//双击弹出电商退货单
		ondblClickRow: function(rowid) {
			order(rowid);
		},
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
			search()
		},
		footerrow: true,
		gridComplete: function() { 
			var allRefNum = $("#jqGrids").getCol('allRefNum', false, 'sum');
			var allRefMoney = $("#jqGrids").getCol('allRefMoney', false, 'sum');
			
			$("#jqGrids").footerData('set', { 
				"refId": "本页合计",
				allRefNum: allRefNum.toFixed(prec),
				allRefMoney : precision(allRefMoney,2)

			}          );    
		},
	})
})


$(document).on('click', '.search', function() {
	search()
})

//查询按钮
function search() {
	var myDate = {};
	var refId = $(".refId").val();
	var orderId = $(".orderId").val();
	var storeId = $("input[name='storeId']").val();
	var ecOrderId = $("input[name='ecOrderId1']").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var expressCode = $("input[name='expressCode']").val();
	var isAudit = $("select[name='isAudit']").val();
	var ecRefId = $(".ecRefId").val();
	var applyDateStart = $(".applyDateStart").val();
	var applyDateEnd = $(".applyDateEnd").val();
	var buyerId = $(".buyerId").val();
	var isRefoud = $("select[name='isRefoud']").val();
	var refStatus = $(".refStatus").val();
	var treDateStart = $(".treDateStart").val();
	var treDateEnd = $(".treDateEnd").val();
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)

	var savedata = {
		"reqHead":reqhead,
		"reqBody": {
			"refId": refId,
			"ecOrderId": ecOrderId,
			"orderId": orderId,
			"invtyEncd": invtyEncd,
			"expressCode": expressCode,
			"storeId": storeId,
			"ecRefId": ecRefId,
			"isAudit": isAudit,
			"applyDateStart": applyDateStart,
			"applyDateEnd": applyDateEnd,
			"buyerId": buyerId,
			"isRefund": isRefoud,
			"refStatus": refStatus,
			"treDateStart": treDateStart,
			"treDateEnd": treDateEnd,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/ec/refundOrder/queryList',
		async: true,
		data: saveData,
		dataType: 'json',
		//开始加载动画  添加到ajax里面
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");

		},
		success: function(data) {
			console.log(data)
			var mydata = {};
			mydata.rows = data.respBody.list;
			var listData = mydata.rows
			for(var i =0;i<listData.length;i++) {
				if(listData[i].isRefund == 0) {
					listData[i].isRefund = '否'
				}else if(listData[i].isRefund == 1) {
					listData[i].isRefund = '是'
				}
				if(listData[i].isAudit == 0) {
					listData[i].isAudit = '否'
				}else if(listData[i].isAudit == 1) {
					listData[i].isAudit = '是'
				}
			}
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#jqGrids").jqGrid("clearGridData");
			$("#jqGrids").jqGrid("setGridParam", {
				data: mydata.rows,
				jsonReader: {
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
		//结束加载动画
		complete: function() {
			$(".zhezhao").css("display", "none");
			$("#loader").css("display", "none");
		},
		error: function() {
			console.log(error)
		}
	});
}


/*双击跳转*/
function order(rowid) {
	//获得行数据
	var rowDatas = $("#jqGrids").jqGrid('getRowData', rowid);
	localStorage.setItem("refId", rowDatas.refId);
	window.open("../../Components/ec/refundOrder.html?1");
}

function IsCheckValue(storeId, startDate, endDate) {
	if(storeId == "") {
		alert("店铺编码不能为空")
		return false;
	} else if(startDate == "") {
		alert("交易时间不能为空")
		return false;
	} else if(endDate == "") {
		alert("交易时间不能为空")
		return false;
	}
	return true;
}

//下载
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
		var ecOrderId = $("input[name='ecOrderId']").val()
		var storeId = $("input[name='storeId1']").val()
		var startDate = $("input[name='startDate1']").val()
		var endDate = $("input[name='endDate1']").val()
		if(IsCheckValue(storeId, startDate, endDate) == true) {
			var download = {
				"reqHead": reqhead,
				"reqBody": {
					'ecOrderId': ecOrderId,
					'startDate': startDate,
					'endDate': endDate,
					'storeId': storeId,
				}
			};
			var downloadData = JSON.stringify(download)
			$.ajax({
				type: "post",
				url: url3 + "/mis/ec/refundOrder/download",
				async: true,
				data: downloadData,
				dataType: 'json',
				contentType: 'application/json',
				//开始加载动画  添加到ajax里面
				beforeSend: function() {
					$(".zhezhao").css("display", "block");
					$("#loader").css("display", "block");
		
				},
				success: function(msgAdd) {
				
					alert(msgAdd.respHead.message)
					//				window.location.reload();
					$("#mengban").hide()
					$(".down").css("opacity", 0)
					$(".down").hide()
					$("input[name='ecOrderId']").val('')
					$("input[name='storeName']").val('')
					$("input[name='storeId']").val('')
					$("input[name='startDate1']").val(dates)
					$("input[name='endDate1']").val(new Date().format("yyyy-MM-dd hh:mm:ss"))
				},
				//结束加载动画
				complete: function() {
					$(".zhezhao").css("display", "none");
					$("#loader").css("display", "none");
				},
				error: function() {
					console.log("下载失败")

				}
			});
		}
	})


	$(".falses").click(function() {
		$("#mengban").hide()
		$(".down").css("opacity", 0)
		$(".down").hide()
		$("input[name='ecOrderId']").val('')
		$("input[name='storeName1']").val('')
		$("input[name='storeId1']").val('')
		$("input[name='startDate1']").val(dates)
		$("input[name='endDate1']").val(new Date().format("yyyy-MM-dd hh:mm:ss"))
	})
})

var isclick = true;
//审核与弃审
$(function() {
	//审核
	$(".toExamine").click(function() {
		if(isclick) {
			isclick = false;
			ntChk_s();
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	});
	//弃审
	$(".noTo").click(function() {
		if(isclick) {
			isclick = false;
			ntChk_q();
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	})
})

// 审核
function ntChk_s() {
	var sum = []
	//获取此行的仓库编码和存货编码
	var ids = $("#jqGrids").jqGrid('getGridParam', 'selarrrow');
	for(var i = 0;i < ids.length;i++) {
		//获得行数据
		var rowDatas = $("#jqGrids").jqGrid('getRowData', ids[i]);
		sum.push(rowDatas.refId)
	}
	if(sum.length == 0) {
		alert("请选择单据!")
		return;
	} else {
		var refId = sum.toString()
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				'refId':refId
			}
		};
		var Data = JSON.stringify(data);
		$.ajax({
			url: url + '/mis/ec/refundOrder/audit',
			type: 'post',
			data: Data,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			//开始加载动画  添加到ajax里面
			beforeSend: function() {
				$(".zhezhao").css("display", "block");
				$("#loader").css("display", "block");

			},
			success: function(data) {
				alert(data.respHead.message)
			},
			//结束加载动画
			complete: function() {
				$(".zhezhao").css("display", "none");
				$("#loader").css("display", "none");
			},
			error: function() {
				console.log(error)
			}
		})
	}
}
// 弃审
function ntChk_q() {
	var sum = []
	//获取此行的仓库编码和存货编码
	var ids = $("#jqGrids").jqGrid('getGridParam', 'selarrrow');
	for(var i = 0;i < ids.length;i++) {
		//获得行数据
		var rowDatas = $("#jqGrids").jqGrid('getRowData', ids[i]);
		sum.push(rowDatas.refId)
	}
	if(sum.length == 0) {
		alert("请选择单据!")
		return;
	} else {
		var refId = sum.toString()
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				'refId':refId
			}
		};
		var Data = JSON.stringify(data);
		$.ajax({
			url: url + '/mis/ec/refundOrder/noAudit',
			type: 'post',
			data: Data,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			//开始加载动画  添加到ajax里面
			beforeSend: function() {
				$(".zhezhao").css("display", "block");
				$("#loader").css("display", "block");

			},
			success: function(data) {
				alert(data.respHead.message)
			},
			//结束加载动画
			complete: function() {
				$(".zhezhao").css("display", "none");
				$("#loader").css("display", "none");
			},
			error: function() {
				console.log(error)
			}
		})
	}
}
//删除
$(function() {
	$(".delOrder").click(function() {
		var sum = []
		//获取此行的仓库编码和存货编码
		var ids = $("#jqGrids").jqGrid('getGridParam', 'selarrrow');
		for(var i = 0;i < ids.length;i++) {
			//获得行数据
			var rowDatas = $("#jqGrids").jqGrid('getRowData', ids[i]);
			sum.push(rowDatas.refId)
		}
		var refId = sum.toString()
		if(ids.length == 0) {
			alert("请选择单据!")
		} else if(confirm("确定删除？")) {
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"refId":refId
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/ec/refundOrder/delete',
				type: 'post',
				data: Data,
				dataType: 'json',
				async: true,
				contentType: 'application/json;charset=utf-8',
				beforeSend: function() {
					$("#mengban").css("display", "block");
					$("#loader").css("display", "block");
				},
				complete: function() {
					$("#mengban").css("display", "none");
					$("#loader").css("display", "none");
				},
				success: function(data) {
					alert(data.respHead.message)
					search()
				},
				error: function() {
					console.log(error)
				}
			})
		}
	})
})

$(function() {
	$("#last_jqGridPager").after('<input id="_input" type="text" value="10"/>')
})

$(function() {
	pageInit_l()
	$(document).on('click', '.searchOne', function() {
		oneSerch()
	});
	$(document).on('click', '.save', function() {
		$("#purchaseOrder").show()
		$(".down").hide()
		$(".formSave_box").hide()
		$(".oneSerch_List").css("opacity", 0)
		$(".oneSerch_List").hide()
	});
})

function oneSerch() {
	//获得选中行的行号
	var ids = $("#jqGrids").jqGrid('getGridParam', 'selrow');
	var rowData = $("#jqGrids").jqGrid('getRowData', ids);
	var refId = rowData.refId
	if(!ids) {
		alert("请选择单据!")
		return;
	} else {
		$("#purchaseOrder").hide()
		$(".down").hide()
		$(".formSave_box").hide()
		$(".oneSerch_List").css("opacity", 1)
		$(".oneSerch_List").show()
		var savedata = {
			'reqHead': reqhead,
			'reqBody': {
				'refId': refId,
			},
		};
		var saveData = JSON.stringify(savedata)
		console.log(saveData)
		$.ajax({
			type: "post",
			contentType: 'application/json; charset=utf-8',
			url: url + '/mis/ec/refundOrder/unionQuery',
			async: true,
			data: saveData,
			dataType: 'json',
			beforeSend: function() {
				$("#mengban").css("display", "block");
				$("#loader").css("display", "block");
			},
			complete: function() {
				$("#mengban").css("display", "none");
				$("#loader").css("display", "none");
			},
			success: function(data) {
				console.log(data)
//				var rowNum = $("#_input").val()
				var list = data.respBody.list;
				for(var i = 0; i < list.length;i++) {
					if(list[i].type == 1) {
						list[i].type = "退货单"
					} else if(list[i].type == 2) {
						list[i].type = "销售出库单"
					}
					if(list[i].audit == 0) {
						list[i].audit = "待审核"
					} else if(list[i].audit == 1) {
						list[i].audit = "已审核"
					}
				}
				myDate = list;
				$("#oneSerch_jqgrids").jqGrid('clearGridData');
				$("#oneSerch_jqgrids").jqGrid('setGridParam', {
//					rowNum:rowNum,
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
}

//页面初始化
function pageInit_l() {

	//初始化表格
	jQuery("#oneSerch_jqgrids").jqGrid({
		autoScroll:true,
		shrinkToFit:false,
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		url: '../../assets/js/json/order.json',
		colNames: ['单据类型', '单据名称', '对应单据号', '是否审核'], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'type',
				align: "center",
				index: 'invdate',
				editable: false,
//				hidden:true
			},
			{
				name: 'orderName',
				align: "center",
				index: 'id',
				editable: false,
			},
			{
				name: 'orderId',
				align: "center",
				index: 'invdate',
				editable: false,
			},
			{
				name: 'audit',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: false,
			}
		],
		autowidth: true,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 10,
		scrollrows: true,
		autoScroll: true,
		height: '100%',
		shrinkToFit: false,
		rowList: [10, 20, 30], //可供用户选择一页显示多少条
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		multiboxonly: true,
		rownumWidth: 20,  //序列号列宽度
		caption: "联查表", //表格的标题名字
		pager: '#oneSerch_jqgridPager', //表格页脚的占位符(一般是div)的id
		ondblClickRow: function() {
			order1();
		},
	})
}

function order1() {
	//获得行号
	var gr = $("#oneSerch_jqgrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	//debugger
	var rowData = $("#oneSerch_jqgrids").jqGrid('getRowData', gr);
	var type = rowData.type
	if(type == '销售出库单') {
//		alert("销售出库单")
		localStorage.setItem("outWhsId", rowData.orderId);
		window.open("../../Components/sell/sellOutWhs.html?1");
	} else if(type == '退货单') {
//		alert("退货单")
		localStorage.setItem("rtnGoodsId", rowData.orderId);
		window.open("../../Components/sell/returnOrder.html?1");
	}

}

//导出
$(document).on('click', '.exportExcel', function() {
	var refId = $(".refId").val();
	var orderId = $(".orderId").val();
	var storeId = $("#storeId").val();
	var ecRefId = $(".ecRefId").val();
	var applyDateStart = $(".applyDateStart").val();
	var applyDateEnd = $(".applyDateEnd").val();
	var buyerId = $(".buyerId").val();
	var isRefNum = $(".isRefNum").val();
	var isAudit = $(".isAudit").val();
	var refStatus = $(".refStatus").val();
	var treDateStart = $(".treDateStart").val();
	var treDateEnd = $(".treDateEnd").val();
	var ecOrderId = $("input[name='ecOrderId1']").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var isRefoud = $("select[name='isRefoud']").val();

	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"refId": refId,
			"orderId": orderId,
			"storeId": storeId,
			"ecRefId": ecRefId,
			"isAudit": isAudit,
			"ecOrderId": ecOrderId,
			"invtyEncd": invtyEncd,
			"isRefoud": isRefoud,
			"applyDateStart": applyDateStart,
			"applyDateEnd": applyDateEnd,
			"buyerId": buyerId,
			"isRefNum": isRefNum,
			"refStatus": refStatus,
			"treDateStart": treDateStart,
			"treDateEnd": treDateEnd,
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		url: url + '/mis/ec/refundOrder/exportList',
		type: 'post',
		data: saveData,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		beforeSend: function() {
			$("#mengban").css("display", "block");
			$("#loader").css("display", "block");
		},
		complete: function() {
			$("#mengban").css("display", "none");
			$("#loader").css("display", "none");
		},
		success: function(data) {
			var list = data.respBody.list;
			var execlName = '退款单'
			ExportData(list, execlName)
		},
		error: function() {
			console.log(error)
		}
	})
	
})