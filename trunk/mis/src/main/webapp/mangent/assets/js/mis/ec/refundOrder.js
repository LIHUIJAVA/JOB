var mType = 0;

$(function() {
	$(".f_date").val(BillDate())
})
//刚开始时可点击的按钮
$(function() {
	$('button').addClass("gray");
	$(".refer").removeClass("gray") //参照
	$('.addOrder1').removeClass("gray") //增加
	$('button').attr('disabled', false);
	$(".gray").attr('disabled', true)
	$(".falses").removeClass("gray")
	$(".falses").attr('disabled', false);
	$(".trues").removeClass("gray")
	$(".trues").attr('disabled', false);
	$(".cancels").removeClass("gray")
	$(".cancels").attr('disabled', false);
	$(".sure").removeClass("gray")
	$(".sure").attr('disabled', false);
})

// 点击增加按钮，执行的操作
$(function() {
	$('.addOrder1').click(function() {
		mType = 1
		$(".down").css("opacity", 1)
		$(".down").show()
		$(".saveOrder").removeClass("gray")
		$(".addOrder1").addClass("gray")
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
		$("#return_jqgrids").jqGrid('clearGridData');
	    $("#return_jqgrids").jqGrid('setGridParam', {
	       url: '../../assets/js/json/order.json',
	       datatype: "json",
	    }).trigger("reloadGrid")
	    $("input").val("");
	    $(".f_date").val(BillDate())
	});
})

//点击弹框确定按钮，执行的操作
$(function() {
	$(".trues").click(function() {

		var ecOrderId = $("input[name='ecOrderId1']").val()
		localStorage.setItem("refundOderId", ecOrderId)
		if(ecOrderId != '') {
			$("#purs_list").show()
			$("#purs_list").css("opacity", 1)
			$(".down").css("opacity", 0)
			$(".down").hide()
			$("#purchaseOrder").css("opacity", 0)
			$("#purchaseOrder").hide()
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"ecOrderId": ecOrderId,
					"orderId": '',
				}
			};
			var postData = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/ec/refundOrder/refundReference',
				type: 'post',
				data: postData,
				dataType: 'json',
				async: true,
				contentType: 'application/json;charset=utf-8',
				error: function() {
					console.log("获取数据错误");
				}, //错误执行方法
				success: function(data) {
					$("input[name='ecOrderId1']").val('')
					var list = data.respBody.list
					for(var i =0;i<list.length;i++) {
						if(list[i].isAudit == 1) {
							list[i].isAudit = "是"
						} else if(list[i].isAudit == 0) {
							list[i].isAudit = "否"
						}
					}
					var mes = data.respHead.isSuccess
					//				//设置页面数据展示
//					for(var i = 0; i < list.length; i++) {
//						$("#mu_jqGrids").setRowData(i + 1, {
//							orderId: list[i].orderId,
//							storeId: list[i].storeId,
//							storeName: list[i].storeName,
//							isAudit: list[i].isAudit,
//							goodNum: list[i].goodNum,
//							payMoney: list[i].payMoney,
//							buyerId: list[i].buyerId,
//							ecOrderId: list[i].ecOrderId
//						});
//					}
					$("#mu_jqGrids").jqGrid('clearGridData');
					$("#mu_jqGrids").jqGrid('setGridParam', {
						datatype: 'local',
						data: list, //newData是符合格式要求的重新加载的数据
					}).trigger("reloadGrid")
				}
			})
		} else {
			alert("请输入订单号")
		}
	})
	$(".falses").click(function() {
		$(".saveOrder").addClass("gray")
		$(".addOrder1").removeClass("gray")
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
		$(".down").css("opacity", 0)
		$(".down").hide()
		$("input[name='ecOrderId1']").val('')
	})
})

//点击参照页面确定按钮
$(function() {
	$(".sure").click(function() {
		var id = $("#zi_jqGrids").jqGrid('getGridParam', 'selarrrow');
		if(id.length == 0) {
			alert("未选择子表")
		} else {
			$("#purs_list").hide()
			$("#purs_list").css("opacity", 0)
			$(".down").css("opacity", 0)
			$(".down").hide()
			$("#purchaseOrder").css("opacity", 1)
			$("#purchaseOrder").show()
			$(".aaaa").removeClass("gray");
			$(".saveOrder").removeClass("gray")
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true)
			var num = []
			var gr = $("#mu_jqGrids").jqGrid('getGridParam', 'selrow');
			//获得行数据
			var rowData = $("#mu_jqGrids").jqGrid('getRowData', gr);

			//获得行数据
			//		var rowDatas = $("#zi_jqGrids").jqGrid('getRowData', id);
			for(var i = 0; i < id.length; i++) {
				var rowDatas = $("#zi_jqGrids").jqGrid('getRowData', id[i]);
				num.push(rowDatas)
			}
			var myDate = new Date();
			var formatDate = function() {
				var y = myDate.getFullYear();
				var m = myDate.getMonth() + 1;
				m = m < 10 ? '0' + m : m;
				var d = myDate.getDate();
				d = d < 10 ? ('0' + d) : d;
				var h = myDate.getHours();
				var f = myDate.getMinutes();
				var s = myDate.getSeconds();
				return y + '-' + m + '-' + d + ' ' + h + ":" + f + ":" + s;
			};
			var ecOrderId = $("input[name='ecOrderId1']").val()

			$("select[name='isRefund']").val(1);
			$("input[name='treDate']").val(formatDate);
			$("input[name='applyDate']").val(formatDate);
			$("input[name='ecOrderId']").val(ecOrderId);
			$("input[name='orderId']").val(rowData.orderId);
			$("input[name='ecOrderId']").val(rowData.ecOrderId);
			$("input[name='storeId']").val(rowData.storeId);
			$("input[name='storeName']").val(rowData.storeName);
			$("input[name='buyerId']").val(rowData.buyerId);
			console.log(num)
			for(var i = 0; i < num.length; i++) {
				if(num[i].isGift == "否") {
					num[i].isGift = 0
				} else {
					num[i].isGift = 1
				}
				$("#return_jqgrids").setRowData(i + 1, {
					goodId: num[i].invId,
					goodName: num[i].invtyName,
					canRefNum: num[i].canRefNum,
					canRefMoney: num[i].canRefMoney,
					batchNo: num[i].batchNo,
					isGift: num[i].isGift,
					prdcDt: num[i].prdcDt,
					invldtnDt: num[i].invldtnDt,
					refMoney: num[i].canRefMoney,
					refNum: num[i].canRefNum,
				});
//				$("#return_jqgrids").jqGrid('clearGridData');
//				$("#return_jqgrids").jqGrid('setGridParam', {
//					datatype: 'local',
//					data: num, //newData是符合格式要求的重新加载的数据
//				}).trigger("reloadGrid")
			}
			$("#return_jqgrids").setColProp("refNum", {
				editable: true
			});
			$("#return_jqgrids").setColProp("refMoney", {
				editable: true
			});
			$("#return_jqgrids").setColProp("refWhs", {
				editable: true
			})
			$("#return_jqgrids").setColProp("memo", {
				editable: true
			});
			var list = getJQAllData();
			var canRefNum = 0;
			var canRefMoney = 0;
			var refNum = 0;
			var refMoney = 0;
			for(var i = 0; i < list.length; i++) {
				canRefNum += parseFloat(list[i].canRefNum * 1);
				canRefMoney += parseFloat(list[i].canRefMoney * 1);
				refNum += parseFloat(list[i].refNum * 1);
				refMoney += parseFloat(list[i].refMoney * 1);
			};
			if(isNaN(canRefNum)) {
				canRefNum = 0
			}
			if(isNaN(canRefMoney)) {
				canRefMoney = 0
			}
			if(isNaN(refNum)) {
				refNum = 0
			}
			if(isNaN(refMoney)) {
				refMoney = 0
			}
			$("input[name='allRefMoney']").val(refMoney)
			$("input[name='allRefNum']").val(refNum)
			canRefNum = canRefNum.toFixed(prec)
			canRefMoney = precision(canRefMoney,2)
			refMoney = precision(refMoney,2)
			refNum = refNum.toFixed(prec)
			$("#return_jqgrids").footerData('set', {
				"goodId":"本页合计",
				"canRefNum": canRefNum,
				"canRefMoney": canRefMoney,
				"refNum": refNum,
				"refMoney": refMoney
			});
			localStorage.removeItem("refundOderId")
		}

	})
	$(".cancels").click(function() {
		$("#purs_list").hide()
		$("#purs_list").css("opacity", 0)
		$(".down").css("opacity", 0)
		$(".down").hide()
		$("#purchaseOrder").css("opacity", 1)
		$("#purchaseOrder").show()
		$(".saveOrder").addClass("gray");
		$(".addOrder1").removeClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
		localStorage.removeItem("refundOderId")
	})
})

////点击表格图标显示供应商列表
//$(function() {
//	$(document).on('click', '.biaoge', function() {
//		var va = window.open("../../components/baseDoc/provrList.html", 'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
//	});
//});
////点击表格图标显示业务员列表
//$(function() {
//	$(document).on('click', '.user', function() {
//		window.open("../../components/baseDoc/userList.html", 'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
//	});
//})

//初始化表格
$(function() {
	allHeight()
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	$("#return_jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json",
		colNames: ['退款单编码', '商品编码', '商品名称', '商品sku', '可退货数量',
			'可退货金额', '退货数量', '退货金额', '批次', '退货仓库', '生产日期', '失效日期', '是否赠品', '备注'

		], //jqGrid的列显示名字
		colModel: [{
				name: 'refId',
				editable: false,
				align: 'center',
				sortable: false,
				hidden: true
			},
			{
				name: 'goodId',
				editable: false,
				align: 'center',
				/*hidden: true,*/
				sortable: false
			},
			{
				name: 'goodName',
				editable: false,
				align: 'center',
				sortable: false

			},
			{
				name: 'goodSku',
				editable: false,
				align: 'center',
				sortable: false,
				hidden: true
			},
			{
				name: 'canRefNum',
				editable: false,
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
				name: 'refNum',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'refMoney',
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
				name: 'refWhs',
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
				name: 'invldtnDt',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'isGift',
				editable: false,
				align: 'center',
				sortable: false,
				edittype: 'select',
				formatter: 'select',
				editoptions: {
					value: "2:请选择;0:否;1:是"
				},
			},
			{
				name: 'memo',
				editable: false,
				align: 'center',
				sortable: false
			},
		],
		loadonce: false,
		rownumbers: true,
		autowidth: true,
		autoScroll: true,
		shrinkToFit: false,
		multiselect: true, //复选框 
		caption: '电商退款单',
		altclass: true,
		viewrecords: true,
		height: height,
		// 隐藏翻页和输入页码
		pgbuttons: false,
		pginput:false,
		forceFit: true,
		sortable: false,
		pager:"return_jqgridPager",
		footerrow: true,
		cellEdit: true,
		cellsubmit: "clientArray",

		//在编辑状态时
		afterEditCell: function(rowid, cellname, val, iRow, iCol) {
			$(".saveOrder").addClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
			if(cellname == "refWhs") {
				$("#" + rowid + "_refWhs").attr("readonly", "readonly");
				$("#" + rowid + "_refWhs").bind("dblclick", function() {
					$("#whsDocList").show();
					$("#whsDocList").css("opacity", 1);
					$("#purs_list").css("opacity", 0);
					$("#purs_list").hide();
					$("#purchaseOrder").hide();
					$(".addWhs").removeClass("gray") //确定可用
					$(".cancel").removeClass("gray") //查询可用
				});
			}
		},
		//离开编辑状态
		afterRestoreCell: function(rowid, cellname, val, iRow, iCol) {
			$(".saveOrder").removeClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true)

		},
		//回车保存
		afterSaveCell: function(rowid, cellname, val, iRow, iCol) {
			$(".saveOrder").removeClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
			
			var listData = getJQAllData();
			for(var i = 0; i < listData.length; i++) {
				if(listData[i].refMoney * 1 > listData[i].canRefMoney * 1) {
					alert("退货金额大于可退货金额")
					break;
				} else {
					if(listData[i].refNum * 1 > listData[i].canRefNum * 1) {
						alert("退货数量大于可退货数量")
						break;
					}
				}	
			}
			if((cellname == "refWhs") ||
				(cellname == "refNum") ||
				(cellname == "refMoney")) {
//				SetNums(rowid, cellname);
				var list = getJQAllData();
				var refNum = 0;
				var refMoney = 0;
				for(var i = 0; i < list.length; i++) {
					refNum += parseFloat(list[i].refNum * 1);
					refMoney += parseFloat(list[i].refMoney * 1);
				};
				if(isNaN(refNum)) {
					refNum = 0
				}
				if(isNaN(refMoney)) {
					refMoney = 0
				}
				if(cellname == "refNum") {
					$("input[name='allRefNum']").val(refNum)
				}
				if(cellname == "refMoney") {
					$("input[name='allRefMoney']").val(refMoney)
				}
				if(cellname == "refWhs") {
					$("input[name='allRefMoney']").val(refMoney)
					$("input[name='allRefNum']").val(refNum)
				}
				refNum = refNum.toFixed(prec)
				refMoney = refMoney.toFixed(prec)
				$("#return_jqgrids").footerData('set', {
					"refId":"本页合计",
					"refNum": refNum,
					"refMoney": refMoney,
				});
			}
		},

	})
	$("#return_jqgrids").jqGrid('navGrid', '#return_jqgridPager', {
		edit: false,
		add: false,
		del: false,
		search: false,
		refresh: false,
	}).navButtonAdd('#return_jqgridPager', {
		caption: "",
		buttonicon: "ui-icon-trash",
		onClickButton: function() {
			var grid = "return_jqgrids"
			//删除一行操作
			removeRows1(grid);
		},
		position: "first"
	}).navButtonAdd('#return_jqgridPager', {
		caption: "",
		buttonicon: "ui-icon-newwin",
		onClickButton: function() {
			//复制一行操作
			copyRows1();
		},
		position: "last"
	}).navButtonAdd('#return_jqgridPager', {
		caption: "",
		buttonicon: "ui-icon-plus",
		onClickButton: function() {
			//新增一行操作
			addRows1();
		},
		position: "last"
	})
})
//新增表格行
function addRows1() {
	mType = 1;
	var gr = $("#return_jqgrids").jqGrid('getDataIDs');
	if(gr.length == 0) {
		var rowid = 0;
	} else if(gr.length != 0) {
		var rowid = Math.max.apply(Math, gr);
	}
	window.newrowid = rowid + 1;
	var dataRow = {};
	$("#return_jqgrids").jqGrid("addRowData", newrowid, dataRow, "last");
}
//删除表格行
function removeRows1() {
	var gr = $("#return_jqgrids").jqGrid('getGridParam', 'selarrrow');
	if(gr.length == 0) {
		alert("请选择要删除的行");
		return;
	} else {
		var length = gr.length;
		for(var i = 0; i < length + 1; i++) {
			$("#return_jqgrids").jqGrid("delRowData", gr[0]);
		}
	}
}
//复制表格行
function copyRows1() {
	var ids = $("#return_jqgrids").jqGrid('getGridParam', 'selarrrow');
	var dataRow = $("#return_jqgrids").jqGrid('getRowData', ids);
	if(ids.length == 0) {
		alert("请选择要复制的行");
		return;
	} else if(ids.length > 1) {
		alert("每次只能复制一行");
		return;
	} else {
		var gr = $("#return_jqgrids").jqGrid('getDataIDs');
		// 选中行实际表示的位置
		var rowid = Math.max.apply(Math, gr);
		// 新插入行的位置
		var newrowid = rowid + 1;
		// 插入一行
		$("#return_jqgrids").jqGrid("addRowData", newrowid, dataRow, "last");
	}
}
$(function() {
	//确定
	$(".addWhs").click(function() {
		//到货单
		//获得行号
		var rowid = $("#return_jqgrids").jqGrid('getGridParam', 'selrow');

		//	仓库档案
		//	获得行号
		var gr = $("#whs_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDatas = $("#whs_jqgrids").jqGrid('getRowData', gr);

		$("#" + rowid + "_refWhs").val(rowDatas.whsEncd)
		//		$("#" + rowid + "_invtyEncd").val(rowData.invtyEncd)
		//		$("#jqgrids").setRowData(rowid, {
		//			whsNm: rowDatas.whsNm
		//		})

		$("#whsDocList").css("opacity", 0);
		$("#insertList").css("opacity", 0);
		$("#purchaseOrder").show();
	})
	//	取消
	$(".cancel").click(function() {
		$("#whsDocList").css("opacity", 0);
		$("#whsDocList").hide();
		$("#purchaseOrder").show();
		//到货单
		//获得行号
		var rowid = $("#return_jqgrids").jqGrid('getGridParam', 'selrow');

		$("#" + rowid + "_refWhs").val("");
	})
})

// 点击保存，传送数据给后台
var isclick = true;
$(function() {
	$(".saveOrder").click(function() {
		if(isclick) {
			isclick = false;
			if(mType == 1) {
				SaveNewData();
			}
			if(mType == 2) {
				SaveModifyData();
			}
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	})
})
// 点击修改按钮，执行的操作
$(function() {
	$('.editOrder').click(function() {
		mType = 2;
		$('button').addClass('gray')
		$(".saveOrder").removeClass("gray");
		$('.upOrder').removeClass("gray");
		$('.editOrder').addClass("gray"); //点击修改后 修改不能用
		$('.toExamine').addClass("gray"); //点击修改后 修改不能用
		$('.noTo').addClass("gray"); //点击修改后 修改不能用
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true);
		/*-----*/
		$("#gbox_jqGrids").hide();
		$("#gbox_jqgrids").show();
		$("#return_jqgrids").setColProp("refNum", {
			editable: true
		});
		$("#return_jqgrids").setColProp("refMoney", {
			editable: true
		});
		$("#return_jqgrids").setColProp("refWhs", {
			editable: true
		})
		$("#return_jqgrids").setColProp("memo", {
			editable: true
		});
		
	});
})

function getJQAllData() {
	//拿到grid对象
	var obj = $("#return_jqgrids");
	//获取grid表中所有的rowid值
	var rowIds = obj.getDataIDs();
	//初始化一个数组arrayData容器，用来存放rowData
	var arrayData = new Array();
	if(rowIds.length > 0) {
		for(var i = 0; i < rowIds.length; i++) {
			if(obj.getRowData(rowIds[i]).batchNo == "") {
				continue;
			} else {

				arrayData.push(obj.getRowData(rowIds[i]));
			}
		}
	}
	return arrayData;
}

function IsCheckValue(listData) {
//	if(listData.length == 0) {
//		alert("未添加子表！")
//		return false;
//	}else 
	if(listData.length != 0) {
		for(var i = 0; i < listData.length; i++) {
			if(listData[i].refWhs == "") {
				alert("退货仓库不能为空")
				return false;
			}else if(listData[i].refNum == '') {
				alert("退货数量不能为空")
				return false;
			}else if(listData[i].refMoney == '') {
				alert("退货金额不能为空")
				return false;
			}else if(listData[i].refNum == 0 && listData[i].refMoney == 0) {
				alert("退货数量、退货金额不能同时为0")
				return false;
			}
		}
	}
	return true;
}

//保存修改后的数据
function SaveModifyData() {
	var listData = getJQAllData();
	var refId = $("input[name='refId']").val();
	var orderId = $("input[name='orderId']").val();
	var ecOrderId = $("input[name='ecOrderId']").val();
	var storeId = $("input[name='storeId']").val();
	var storeName = $("input[name='storeName']").val();
	var ecRefId = $("input[name='ecRefId']").val();
	var applyDate = $("input[name='applyDate']").val();
	var buyerId = $("input[name='buyerId']").val();
	var isRefund = $("select[name='isRefund']").val();
	var allRefNum = $("input[name='allRefNum']").val();
	var allRefMoney = $("input[name='allRefMoney']").val();
	var refReason = $("input[name='refReason']").val();
	var refExplain = $("input[name='refExplain']").val();
	//	var refStatus = $("select[name='refStatus']").val();
	var treDate = $("input[name='treDate']").val();
	var memo = $("input[name='memo']").val();
	var expressCode = $("input[name='expressCode']").val();
	//判断页面是否有值为空
	if(IsCheckValue(listData) == true) {
		for(var i = 0; i < listData.length; i++) {
			if(listData[i].refMoney * 1 > listData[i].canRefMoney * 1) {
				alert("退货金额不可大于可退货金额")
				break;
			} else {
				if(listData[i].refNum * 1 > listData[i].canRefNum * 1) {
					alert("退货数量不可大于可退货数量")
					break;
				} else {
					$(".addOrder1").addClass("gray");
					$(".editOrder").removeClass("gray");
					$(".delOrder").removeClass("gray");
					$(".toExamine").removeClass("gray");
					$(".search").removeClass("gray");
					$('button').attr('disabled', false);
					$(".gray").attr('disabled', true)
					var num = []
					var num1 = []
					var sum = 0;
					var sum1 = 0;
					for(var i = 0; i < listData.length; i++) {
						num.push(listData[i].refMoney * 1)
						num1.push(listData[i].refNum * 1)
					}

					for(var j = 0; j < num.length; j++) {
						sum += num[j]
					}
					for(var k = 0; k < num1.length; k++) {
						sum1 += num1[k]
					}
					$("input[name='allRefMoney']").val(sum)
					$("input[name='allRefNum']").val(sum1)

					var allRefNum = $("input[name='allRefNum']").val();
					if(allRefNum > 0) {
						$("select[name='isRefund']").val(1);
					} else {
						$("select[name='isRefund']").val(0);
					}
					var savedata = {
						"reqHead": reqhead,
						"reqBody": {
							'orderId': orderId,
							'refId': refId,
							'ecOrderId': ecOrderId,
							'storeId': storeId,
							'storeName': storeName,
							'isAudit': 0,
							'ecRefId': ecRefId,
							'applyDate': applyDate,
							'buyerId': buyerId,
							'isRefund': isRefund,
							'expressCode': expressCode,
							'allRefNum': allRefNum,
							'allRefMoney': allRefMoney,
							'refReason': refReason,
							'refExplain': refExplain,
							'refStatus': 0,
							'treDate': treDate,
							'memo': memo,
							'list': listData
						}
					};
					var saveData = JSON.stringify(savedata);
					$.ajax({
						url: url + '/mis/ec/refundOrder/edit',
						type: 'post',
						data: saveData,
						dataType: 'json',
						async: true,
						contentType: 'application/json;charset=utf-8',
						success: function(data) {
							$('button').addClass("gray")
							$('.saveOrder').addClass("gray");
							$(".upOrder").removeClass("gray");
							$(".delOrder").removeClass("gray");
							$(".toExamine").removeClass("gray");
							$(".editOrder").removeClass("gray");
							$(".addWhs").removeClass("gray")
							$(".cancel").removeClass("gray")
							$(".find").removeClass("gray")
							$('button').attr('disabled', false);
							$(".gray").attr('disabled', true)
							$("#return_jqgrids").setColProp("refNum", {
								editable: false
							});
							$("#return_jqgrids").setColProp("refMoney", {
								editable: false
							});
							$("#return_jqgrids").setColProp("refWhs", {
								editable: false
							})
							$("#return_jqgrids").setColProp("memo", {
								editable: false
							});
							alert(data.respHead.message)
						},
						error: function() {
							console.log(error);
						} //错误执行方法
					})
				}
			}
		}

	}
}

//保存
function SaveNewData() {

	var listData = getJQAllData();
	var refId = $("input[name='refId']").val();
	var orderId = $("input[name='orderId']").val();
	var ecOrderId = $("input[name='ecOrderId']").val();
	var storeId = $("input[name='storeId']").val();
	var storeName = $("input[name='storeName']").val();
	var ecRefId = $("input[name='ecRefId']").val();
	var applyDate = $("input[name='applyDate']").val();
	var buyerId = $("input[name='buyerId']").val();
	var isRefund = $("select[name='isRefund']").val();
	var allRefNum = $("input[name='allRefNum']").val();
	var expressCode = $("input[name='expressCode']").val();
	var allRefMoney = $("input[name='allRefMoney']").val();
	var refReason = $("input[name='refReason']").val();
	var refExplain = $("input[name='refExplain']").val();
	//	var refStatus = $("select[name='refStatus']").val();
	var treDate = $("input[name='treDate']").val();
	var memo = $("input[name='memo']").val();

	//判断页面是否有值为空
	if(IsCheckValue(listData) == true) {
		for(var i = 0; i < listData.length; i++) {
			if(listData[i].refMoney * 1 > listData[i].canRefMoney * 1) {
				alert("退货金额不可大于可退货金额")
				break;
			} else {
				if(listData[i].refNum * 1 > listData[i].canRefNum * 1) {
					alert("退货数量不可大于可退货数量")
					break;
				} else {
					$(".saveOrder").removeClass("gray")
					$(".saveOrder").attr('disabled', false);
					var num = []
					var num1 = []
					var sum = 0;
					var sum1 = 0;
					for(var i = 0; i < listData.length; i++) {
						num.push(listData[i].refMoney * 1)
						num1.push(listData[i].refNum * 1)
					}

					for(var j = 0; j < num.length; j++) {
						sum += num[j]
					}
					for(var k = 0; k < num1.length; k++) {
						sum1 += num1[k]
					}
					$("input[name='allRefMoney']").val(sum)
					$("input[name='allRefNum']").val(sum1)

					var allRefNum = $("input[name='allRefNum']").val();
					if(allRefNum > 0) {
						$("select[name='isRefund']").val(1);
					} else {
						$("select[name='isRefund']").val(0);
					}
					
					var savedata = {
						"reqHead": reqhead,
						"reqBody": {
							'orderId': orderId,
							'refId': refId,
							'ecOrderId': ecOrderId,
							'storeId': storeId,
							'isAudit': 0,
							'storeName': storeName,
							'ecRefId': ecRefId,
							'expressCode': expressCode,
							'applyDate': applyDate,
							'buyerId': buyerId,
							'isRefund': isRefund,
							'allRefNum': allRefNum,
							'allRefMoney': allRefMoney,
							'refReason': refReason,
							'refExplain': refExplain,
							'refStatus': 0,
							'treDate': treDate,
							'memo': memo,
							'list': listData
						}
					};
					var saveData = JSON.stringify(savedata);
					$.ajax({
						url: url + '/mis/ec/refundOrder/add',
						type: 'post',
						data: saveData,
						dataType: 'json',
						async: true,
						contentType: 'application/json;charset=utf-8',
						success: function(data) {
							$('button').addClass("gray")
							$('.saveOrder').addClass("gray");
							$(".toExamine").removeClass("gray");
							$(".delOrder").removeClass("gray");
							$(".upOrder").removeClass("gray");
							$(".editOrder").removeClass("gray");
							$(".addWhs").removeClass("gray")
							$(".cancel").removeClass("gray")
							$(".find").removeClass("gray")
							$('button').attr('disabled', false);
							$(".gray").attr('disabled', true)
							alert(data.respHead.message)
							var refId = data.respBody.refId;
							var downTime = data.respBody.downTime;
							var operator = data.respBody.operator;
							var operatorTime = data.respBody.operatorTime;
							$("input[name='refId']").val(refId);
							$("input[name='downTime']").val(downTime);
							$("input[name='operator']").val(operator);
							$("input[name='operatorTime']").val(operatorTime);
							$("#return_jqgrids").setColProp("refNum", {
								editable: false
							});
							$("#return_jqgrids").setColProp("refMoney", {
								editable: false
							});
							$("#return_jqgrids").setColProp("refWhs", {
								editable: false
							})
							$("#return_jqgrids").setColProp("memo", {
								editable: false
							});
						},
						error: function() {
							console.log(error);
						} //错误执行方法
					})
				}
			}
		}
	}
}

//查询详细信息
$(function() {

	var afterUrl = window.location.search.substring(1);
	var b = [];
	b = afterUrl;
	if(b == 1) {
		chaxun()
	}
})

function chaxun() {
	$("#gbox_jqgrids").hide();
	$(".editOrder").removeClass("gray");
	$('button').attr('disabled', false);
	$(".gray").attr('disabled', true)

	var refId = localStorage.refId;
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"refId": refId
		}
	};
	var saveData = JSON.stringify(savedata);
	var myDate = {};
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/ec/refundOrder/query',
		async: true,
		data: saveData,
		dataType: 'json',
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");
		},
		//结束加载动画
		complete: function() {
			$(".zhezhao").css("display", "none");
			$("#loader").css("display", "none");
		},
		success: function(data) {
			var list = data.respBody;
			if(list.isAudit == 0) {
				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true)
				$('.toExamine').attr('disabled', false);
				$('.delOrder').attr('disabled', false);
				$(".delOrder").removeClass("gray");
				$(".upOrder").removeClass("gray");
				$(".editOrder").removeClass("gray");
				$(".addOrder1").addClass("gray");
				$(".noTo").addClass("gray");
				$(".toExamine").removeClass("gray");
			} else if(list.isAudit == 1) {
				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true)
				$(".upOrder").addClass("gray");
				$(".addOrder1").addClass("gray");
				$(".editOrder").addClass("gray");
				$(".toExamine").addClass("gray");
				$(".noTo").removeClass("gray");
				$(".noTo").attr('disabled', false)
			}
			$("input[name='ecOrderId']").val(list.ecOrderId);
			$("input[name='refId']").val(list.refId);
			$("input[name='orderId']").val(list.orderId);
			$("input[name='storeId']").val(list.storeId);
			$("input[name='storeName']").val(list.storeName);
			$("input[name='ecRefId']").val(list.ecRefId);
			$("input[name='applyDate']").val(list.applyDate);
			$("input[name='downTime']").val(list.downTime);
			$("input[name='buyerId']").val(list.buyerId);
			$("select[name='isRefNum']").val(list.isRefNum);
			$("input[name='allRefNum']").val(list.allRefNum);
			$("input[name='allRefMoney']").val(list.allRefMoney);
			$("input[name='refReason']").val(list.refReason);
			$("input[name='refExplain']").val(list.refExplain);
			$("select[name='refStatus']").val(list.refStatus);
			$("input[name='downTime']").val(list.downTime);
			$("input[name='treDate']").val(list.treDate);
			$("input[name='operator']").val(list.operator);
			$("input[name='isAudit']").val(list.isAudit);
			$("select[name='isRefund']").val(list.isRefund);
			$("input[name='memo']").val(list.memo);
			$("input[name='operatorId']").val(list.memo);
			$("input[name='operatorTime']").val(list.memo);
			$("input[name='auditHint']").val(list.memo);
			/*表格渲染*/
			var list1 = data.respBody.list;
			$("#return_jqgrids").jqGrid('clearGridData');
			$("#return_jqgrids").jqGrid('setGridParam', {
				datatype: 'local',
				data: list1, //newData是符合格式要求的重新加载的数据
			}).trigger("reloadGrid")
			var list = getJQAllData();
			var canRefNum = 0;
			var canRefMoney = 0;
			var refNum = 0;
			var refMoney = 0;
			for(var i = 0; i < list.length; i++) {
				canRefNum += parseFloat(list[i].canRefNum * 1);
				canRefMoney += parseFloat(list[i].canRefMoney * 1);
				refNum += parseFloat(list[i].refNum * 1);
				refMoney += parseFloat(list[i].refMoney * 1);
			};
			if(isNaN(canRefNum)) {
				canRefNum = 0
			}
			if(isNaN(canRefMoney)) {
				canRefMoney = 0
			}
			if(isNaN(refNum)) {
				refNum = 0
			}
			if(isNaN(refMoney)) {
				refMoney = 0
			}
			canRefNum = canRefNum.toFixed(prec)
			canRefMoney = canRefMoney.toFixed(prec)
			refNum = refNum.toFixed(prec)
			refMoney = refMoney.toFixed(prec)
			$("#return_jqgrids").footerData('set', {
				"refId":"本页合计",
				"canRefNum": canRefNum,
				"canRefMoney": canRefMoney,
				"refNum": refNum,
				"refMoney": refMoney
			});
		},
		error: function(err) {
			console.log(err)
		}
	})
}

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
	// 点击删除按钮，执行的操作
	$('.delOrder').click(function() {
		var refId = $("input[name='refId']").val()
		mType = 0;
		var deleteAjax = {
			"reqHead": reqhead,
			"reqBody": {
				"refId": refId
			}
		}
		var deleteData = JSON.stringify(deleteAjax);
		if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + '/mis/ec/refundOrder/delete',
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(remover) {
					alert(remover.respHead.message)
					window.location.reload()
				},
				error: function() {
					console.log("删除失败")
				}
			});
		}
	});
})
function ntChk_s() {
	var refId = $("input[name='refId']").val()
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			'refId': refId
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
			if(data.respHead.isSuccess == true){
				$(".addOrder1").addClass("gray")
				$(".editOrder").addClass("gray")
				$(".saveOrder").addClass("gray")
				$(".delOrder").addClass("gray")
				$(".toExamine").addClass("gray")
				$(".toExamine").attr('disabled', true);
				$(".addOrder1").removeClass("gray")
				$(".noTo").removeClass("gray")
				$(".upOrder").addClass("gray")
				$(".noTo").attr('disabled', false);
				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true)
			}

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
function ntChk_q() {
	var refId = $("input[name='refId']").val()
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			'refId': refId
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
			if(data.respHead.isSuccess == true) {
				$(".noTo").addClass("gray")
				$(".upOrder").removeClass("gray")
				$(".noTo").attr('disabled', true);
				$(".upOrder").attr('disabled', false);
				$(".addOrder1").removeClass("gray")
				$(".delOrder").removeClass("gray")
				$(".editOrder").removeClass("gray")
				$(".toExamine").removeClass("gray")
				$(".toExamine").attr('disabled', false);
				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true)
			}
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
