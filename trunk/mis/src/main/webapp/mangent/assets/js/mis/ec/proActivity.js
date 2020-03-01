$(function() {
	//点击表格图标显示存货列表
	$(document).on('click', '.storeId_biaoge', function() {
		window.open("../../Components/baseDoc/storeRecordList.html", 'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})

$(function() {
	$(".l_date").val(BillDate())
})
var mType = 0;

//刚开始时可点击的按钮
$(function() {
	$('button').addClass("gray");
	$(".canceles").removeClass("gray") //参照
	$(".addWhses").removeClass("gray") //参照
	$(".find").removeClass("gray") //参照
	$(".refer").removeClass("gray") //参照
	$('.addOrder').removeClass("gray") //增加
	$('button').attr('disabled', false);
	$(".gray").attr('disabled', true)
	$("#mengban").show()
})

// 点击增加按钮，执行的操作
$(function() {
	$('.addOrder').click(function() {
		$('button').addClass("gray");
		$(".saveOrder").removeClass("gray");
		$(".upOrder").removeClass("gray");
		$(".canceles").removeClass("gray") //参照
		$(".addWhses").removeClass("gray") //参照
		$(".find").removeClass("gray") //参照
		//		$('.addOrder').addClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)

		mType = 1;

		$("#proAc_jqgrids").jqGrid('setGridParam', {}).trigger("reloadGrid")
		$("#mengban").hide();
//		$(".jz").hide();

		$("#proAc_jqgrids").jqGrid('clearGridData');
	    $("#proAc_jqgrids").jqGrid('setGridParam', {
	       url: '../../assets/js/json/order.json',
	       datatype: "json",
	    }).trigger("reloadGrid")
	    $(".inp").val("");
		$("input[name=createDate]").val(BillDate());
		$("input[name='proActId']").removeAttr("readonly");

		//		获取制单人
		$("input[name=creator]").val(localStorage.loginName);
	});

})

//初始化表格
$(function() {
	allHeight()
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	$("#proAc_jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json",
		colNames: ['序号','促销方案编码', '促销方案名称', '全部商品', '商品范围', '赠品数量', '已赠数量', '备注'

		], //jqGrid的列显示名字
		colModel: [{
				name: 'no',
				editable: true,
				align: 'center',
				sortable: false,
				hidden:true
			},
			{
				name: 'proPlanId',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'proPlanName',
				editable: false,
				align: 'center',
				/*hidden: true,*/
				sortable: false
			},
			{
				name: 'allGoods',
				editable: true,
				align: 'center',
				sortable: false,
				edittype: 'select',
				formatter:'select',
				editoptions: {value: "0:否;1:是" },

			},
			{
				name: 'goodsRange',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'giftNum',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'hasGiftNum',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'memo',
				editable: true,
				align: 'center',
				sortable: false
			}
		],
		loadonce: false,
		rownumbers: true,
		autowidth: true,
		multiselect: true, //复选框 
		caption: '促销活动',
		altclass: true,
		viewrecords: true,
		height: height,
		autoScroll: true,
		shrinkToFit: false,
		pager: '#proAc_jqgridPager', //表格页脚的占位符(一般是div)的id
		forceFit: true,
		sortable: false,
		cellEdit: true,
		cellsubmit: "clientArray",
		//在编辑状态时
		afterEditCell: function(rowid, cellname, val, iRow, iCol) {
			//获取此行的仓库编号和存货编号
			var id_num = $("#proAc_jqgrids").jqGrid('getGridParam', 'selrow');
			//获得行数据
			var rowDatas = $("#proAc_jqgrids").jqGrid('getRowData', id_num);
			$(".saveOrder").addClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
			if(cellname == "proPlanId") {
				$("#" + rowid + "_proPlanId").attr("readonly", "readonly");
				$("#" + rowid + "_proPlanId").bind("dblclick", function() {
//					显示存货列表
//					window.open("../../Components/ec/proPlanList.html", 'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
					$("#proPlanList").show();
					$("#proPlanList").css("opacity", 1);
					$("#purchaseOrder").hide();
					$(".addWhs").removeClass("gray") //确定可用
					$(".search").removeClass("gray") //查询可用
					$(".search").removeAttr("disabled"); //查询可用
				});
			}
			if(cellname == "allGoods") {
				$("#" + rowid + "_allGoods").on("change",function() {
					var name = $("#" + rowid + "_allGoods").val()
					if(name == 1) {
						$("#proAc_jqgrids").setColProp("goodsRange",{editable:false});
						$("#proAc_jqgrids").jqGrid("setCell",rowid,"goodsRange",'&nbsp;');
					} else if (name == 0) {
						$("#proAc_jqgrids").setColProp("goodsRange",{editable:true});
					}
				})
			}
			if(cellname == "goodsRange") {
				$("#" + rowid + "_goodsRange").attr("readonly", "readonly");
				$("#" + rowid + "_goodsRange").bind("dblclick", function() {
//					显示存货列表
//					window.open("../../Components/ec/proPlanList.html", 'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
					$("#insertList").show();
					$("#proPlanList").hide();
					$("#insertList").css("opacity", 1);
					$("#purchaseOrder").hide();
					$(".addWhs").removeClass("gray") //确定可用
					$(".search").removeClass("gray") //查询可用
					$(".search").removeAttr("disabled"); //查询可用
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
		}

	})
	$("#proAc_jqgrids").jqGrid('navGrid', '#proAc_jqgridPager', {
		edit: false,
		add: false,
		del: false,
		search: false,
		refresh: false,
	}).navButtonAdd('#proAc_jqgridPager', {
		caption: "",
		buttonicon: "ui-icon-trash",
		onClickButton: function() {
			var grid = "proAc_jqgrids"
			//删除一行操作
			removeRows1(grid);
		},
		position: "first"
	}).navButtonAdd('#proAc_jqgridPager', {
		caption: "",
		buttonicon: "ui-icon-newwin",
		onClickButton: function() {
			//复制一行操作
			copyRows1();
		},
		position: "last"
	}).navButtonAdd('#proAc_jqgridPager', {
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
	var gr = $("#proAc_jqgrids").jqGrid('getDataIDs');
	if(gr.length == 0) {
		var rowid = 0;
	} else if(gr.length != 0) {
		var rowid = Math.max.apply(Math, gr);
	}
	window.newrowid = rowid + 1;
	var dataRow = {};
	$("#proAc_jqgrids").jqGrid("addRowData", newrowid, dataRow, "last");
}
//删除表格行
function removeRows1() {
	var gr = $("#proAc_jqgrids").jqGrid('getGridParam', 'selarrrow');
	if(gr.length == 0) {
		alert("请选择要删除的行");
		return;
	} else {
		var length = gr.length;
		for(var i = 0; i < length + 1; i++) {
			$("#proAc_jqgrids").jqGrid("delRowData", gr[0]);
		}
	}
}
//复制表格行
function copyRows1() {
	var ids = $("#proAc_jqgrids").jqGrid('getGridParam', 'selarrrow');
	var dataRow = $("#proAc_jqgrids").jqGrid('getRowData', ids);
	if(ids.length == 0) {
		alert("请选择要复制的行");
		return;
	} else if(ids.length > 1) {
		alert("每次只能复制一行");
		return;
	} else {
		var gr = $("#proAc_jqgrids").jqGrid('getDataIDs');
		// 选中行实际表示的位置
		var rowid = Math.max.apply(Math, gr);
		// 新插入行的位置
		var newrowid = rowid + 1;
		// 插入一行
		$("#proAc_jqgrids").jqGrid("addRowData", newrowid, dataRow, "last");
	}
}
$(function() {
	//确定
	$(".addWhs").click(function() {
		//到货单
		//获得行号
		var rowid = $("#proAc_jqgrids").jqGrid('getGridParam', 'selrow');
		//	仓库档案
		//	获得行号
		var gr = $("#jqGrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDatas = $("#jqGrids").jqGrid('getRowData', gr);
		var userName = localStorage.userName
		$("#" + rowid + "_proPlanId").val(rowDatas.proPlanId)
		$("#proAc_jqgrids").setRowData(rowid, {
			proPlanName: rowDatas.proPlanName,
		})

		$("#proPlanList").css("opacity", 0);
		$("#purchaseOrder").show();
	})
	//	取消
	$(".cancel").click(function() {
		$("#proPlanList").css("opacity", 0);
		$("#purchaseOrder").show();
		//到货单
		//获得行号
		var rowid = $("#proAc_jqgrids").jqGrid('getGridParam', 'selrow');
	})
	
	//确定
	$(".addWhses").click(function() {
		//到货单
		//获得行号
		var rowid = $("#proAc_jqgrids").jqGrid('getGridParam', 'selrow');
		//	仓库档案
		//	获得行号
		var gr = $("#insert_jqgrids").jqGrid('getGridParam', 'selarrrow');
		var num = []
		for(var i =0;i<gr.length;i++) {
			//获得行数据
			var rowDatas = $("#insert_jqgrids").jqGrid('getRowData', gr[i]);
			num.push(rowDatas.invtyEncd)
		}
		$("#" + rowid + "_goodsRange").val(num.toString())

		$("#proPlanList").css("opacity", 0);
		$("#insertList").css("opacity", 0);
		$("#proPlanList").hide();
		$("#insertList").hide();
		$("#purchaseOrder").show();
	})
	//	取消
	$(".canceles").click(function() {
		$("#proPlanList").css("opacity", 0);
		$("#insertList").css("opacity", 0);
		$("#proPlanList").hide();
		$("#insertList").hide();
		$("#purchaseOrder").show();
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
		$("#mengban").hide();
		$('button').addClass('gray')
		$(".saveOrder").removeClass("gray");
		$(".canceles").removeClass("gray") //参照
		$(".addWhses").removeClass("gray") //参照
		$(".find").removeClass("gray") //参照
		$('.upOrder').removeClass("gray");
		$('.editOrder').addClass("gray"); //点击修改后 修改不能用
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true);
		/*-----*/
		$("#gbox_proAc_jqgrids").hide();
		$("#gbox_proAc_jqgrids").show();
//		放弃
		$(".upOrder").click(function() {
			$("#mengban").show()
//			window.location.reload()
			jQuery("#proAc_jqgrids").trigger("reloadGrid");
			$("input").val('');
			$('button').addClass("gray");
			$(".refer").removeClass("gray") //参照
			$('.addOrder').removeClass("gray") //增加
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true)
		})
	});
})


/*获取表格数据*/
function getJQAllData() {
	//拿到grid对象
	var obj = $("#proAc_jqgrids");
	//获取grid表中所有的rowid值
	var rowIds = obj.getDataIDs();
	//初始化一个数组arrayData容器，用来存放rowData
	var arrayData = new Array();
	if(rowIds.length > 0) {
		for(var i = 0; i < rowIds.length; i++) {
			if(obj.getRowData(rowIds[i]).proPlanId == "") {
				continue;
			} else {

				arrayData.push(obj.getRowData(rowIds[i]));
			}
		}
	}
	return arrayData;

}


/*判断条件*/
function IsCheckValue(proActId, proActName, endDate, limitPro, takeStore, priority, listData) {
	if(proActId == '') {
		alert("促销活动编码不能为空")
		return false;
	} else if(proActName == '') {
		alert("促销活动名称不能为空")
		return false;
	} else if(endDate == '') {
		alert("日期不能为空")
		return false;
	} else if(limitPro == null) {
		alert("未选择限量促销")
		return false;
	} else if(takeStore == '') {
		alert("参与店铺不能为空")
		return false;
	}  else if(priority == '') {
		alert("优先级不能为空")
		return false;
	} else if(listData.length == 0) {
		alert("促销方案编码不能为空")
		return false;
	}  else if(listData.length != 0) {
		for(var i = 0; i < listData.length; i++) {
			if(listData[i].allGoods == "") {
				alert("全部不能为空")
				return false;
			} else if(listData[i].allGoods == "0" && listData[i].goodsRange == "") {
				alert("赠品范围不能为空")
				return false;
			} else if(listData[i].giftNum == "") {
				alert("赠品数量不能为空")
				return false;
			} else if(listData[i].hasGiftNum == "") {
				alert("已赠数量不能为空")
				return false;
			} else if(listData[i].giftNum < listData[i].hasGiftNum) {
				alert("已赠数量不能大于赠品数量")
				return false;
			}
		}
	}
	return true;
}

//保存修改后的数据
function SaveModifyData() {
	var listData = getJQAllData();
	var proActId = $("input[name='proActId']").val();
	var proActName = $("input[name='proActName']").val();
	var creator = $("input[name='creator']").val();
	var createDate = $("input[name='createDate']").val();
	var startDate = $("input[name='startDate1']").val();
	var endDate = $("input[name='endDate1']").val();
	var limitPro = $("select[name='limitPro']").val();
	var takeStore = $("input[name='takeStore1']").val();
	var priority = $("input[name='priority']").val();
	var memo = $("input[name='memo']").val();

	//判断页面是否有值为空
	if(IsCheckValue(proActId, proActName, endDate, limitPro, takeStore, priority, listData) == true){
		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				'proActId': proActId,
				'proActName': proActName,
				'startDate': startDate,
				'endDate': endDate,
				'limitPro': limitPro,
				'creator': creator,
				'createDate': createDate,
				'proActName': proActName,
				'takeStore': takeStore,
				'priority': priority,
				'memo': memo,
				'list': listData
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			url: url + '/mis/ec/proActivity/edit',
			type: 'post',
			data: saveData,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			success: function(data) {
				console.log(data)
				$("#mengban").show();
				$(".editOrder").removeClass("gray");
				$(".del").removeClass("gray");
				$(".toExamine").removeClass("gray");
				$(".addOrder").removeClass("gray");
				$(".saveOrder").addClass("gray");
				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true)
				alert(data.respHead.message)
			},
			error: function() {
				console.log(error);
			} //错误执行方法
		})
	}
}

//保存
function SaveNewData() {
	var listData = getJQAllData();
	var proActId = $("input[name='proActId']").val();
	var creator = $("input[name='creator']").val();
	var createDate = $("input[name='createDate']").val();
	var proActId = $("input[name='proActId']").val();
	var proActName = $("input[name='proActName']").val();
	var startDate = $("input[name='startDate1']").val();
	var endDate = $("input[name='endDate1']").val();
	var limitPro = $("select[name='limitPro']").val();
	var takeStore = $("input[name='takeStore1']").val();
	var priority = $("input[name='priority']").val();
	var memo = $("input[name='memo']").val();

	//判断页面是否有值为空
	if(IsCheckValue(proActId, proActName, endDate, limitPro, takeStore, priority, listData) == true){
		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				'proActId': proActId,
				'creator': creator,
				'createDate': createDate,
				'proActName': proActName,
				'startDate': startDate,
				'endDate': endDate,
				'limitPro': limitPro,
				'takeStore': takeStore,
				'priority': priority,
				'memo': memo,
				'list': listData
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			url: url + '/mis/ec/proActivity/add',
			type: 'post',
			data: saveData,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			success: function(data) {
				alert(data.respHead.message)
				if(data.respHead.isSuccess == false) {
					$("input[name='proActId']").removeAttr("readonly")
				}else if(data.respHead.isSuccess == true) {
					$("#mengban").show();
					$(".editOrder").removeClass("gray");
					$(".del").removeClass("gray");
					$(".toExamine").removeClass("gray");
					$(".addOrder").removeClass("gray");
					$(".saveOrder").addClass("gray");
					$('button').attr('disabled', false);
					$(".gray").attr('disabled', true)
					$("input[name='proActId']").attr("readonly","readonly")
				}
			},
			error: function() {
				console.log(error);
			} //错误执行方法
		})
	} 
}

// 点击删除按钮，执行的操作
$(function() {
	$('.del').click(function() {
		var proActId = $("input[name='proActId']").val();
		var deleteAjax = {
			"reqHead": reqhead,
			"reqBody": {
				"proActId": proActId
			}
		}
		var deleteData = JSON.stringify(deleteAjax);
		if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + '/mis/ec/proActivity/delete',
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				beforeSend: function() {
					$("#mengban").css("display", "block");
					$("#loader").css("display", "block");
				},
				complete: function() {
					$("#mengban").css("display", "none");
					$("#loader").css("display", "none");
				},
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
function ntChk_s() {
	var proActId = $("input[name='proActId']").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"list": [{
				"proActId": proActId
			}]
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/ec/proActivity/updateAuditResult',
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
		//结束加载动画
		complete: function() {
			$(".zhezhao").css("display", "none");
			$("#loader").css("display", "none");
		},
		success: function(data) {
			var isSuccess = data.respHead.isSuccess
			if(isSuccess == true) {
				var myDate = new Date();
				var formatDate = function() {
					var y = myDate.getFullYear();
					var m = myDate.getMonth() + 1;
					m = m < 10 ? '0' + m : m;
					var d = myDate.getDate();
					d = d < 10 ? ('0' + d) : d;
					return y + '-' + m + '-' + d;
				};
				$("input[name='auditor']").val(localStorage.loginName)
				$("input[name='auditDate']").val(formatDate)
				$(".editOrder").addClass("gray");
				$(".saveOrder").addClass("gray");
				$(".toExamine").addClass("gray");
				$(".upOrder").addClass("gray");
				$(".del").addClass("gray");
				$(".addOrder").removeClass("gray");
				$(".noTo").removeClass("gray");
				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true)
				$("#mengban").show();
			}
			alert(data.respHead.message)
		},
		error: function() {
			console.log(error)
		}
	})
}
function ntChk_q() {
	var proActId = $("input[name='proActId']").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"list": [{
				"proActId": proActId
			}]
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/ec/proActivity/updateAuditResultNo',
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
		//结束加载动画
		complete: function() {
			$(".zhezhao").css("display", "none");
			$("#loader").css("display", "none");
		},
		success: function(data) {
			$(".addOrder").removeClass("gray");
			$(".saveOrder").addClass("gray");
			$(".del").removeClass("gray");
			$(".noTo").addClass("gray");
			$(".editOrder").removeClass("gray");
			$(".upOrder").removeClass("gray");
			$(".toExamine").removeClass("gray");
			$('button').attr('disabled', false);
			$('.editOrder').attr('disabled', false);
			$(".gray").attr('disabled', true)
			$("#mengban").show();
			alert(data.respHead.message)
		},
		error: function() {
			console.log(error)
		}
	})
}

//查询详细信息
$(function() {

	var afterUrl = window.location.search.substring(1);
	var a = [];
	a = afterUrl;
	if(a == 2 || a == 1) {
		chaxun()
	}
})

function chaxun() {
	$("#mengban").show();
	$("input[name='proActId']").attr("readonly", "readonly")
	var proActId = localStorage.proActId;
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"proActId": proActId,
			"pageNo": 1,
			"pageSize": 500
		}
	};
	var saveData = JSON.stringify(savedata);
	var myDate = {};
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/ec/proActivity/query',
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
			var list1 = data.respBody
			if(list1.auditResult == 0) {
				$(".toExamine").removeClass("gray")
				$(".editOrder").removeClass("gray")
				$(".del").removeClass("gray")
				$(".upOrder").removeClass("gray")
				$(".addOrder").addClass("gray")
				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true)
				
			} else if(list1.auditResult == 1) {
				$(".noTo").removeClass("gray")
				$(".upOrder").removeClass("gray")
				$(".editOrder").addClass("gray")
				$(".addOrder").addClass("gray")
				$(".upOrder").addClass("gray")
				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true)
			}
			$("input[name='proActId']").val(list1.proActId);
			$("input[name='proActName']").val(list1.proActName);
			$("input[name='startDate']").val(list1.startDate);
			$("input[name='endDate1']").val(list1.endDate);
			$("select[name='limitPro']").val(list1.limitPro);
			$("input[name='takeStore1']").val(list1.takeStore);
			$("input[name='priority']").val(list1.priority);
			$("input[name='creator']").val(list1.creator);
			$("input[name='createDate']").val(list1.createDate);
			$("input[name='auditor']").val(list1.auditor);
			$("input[name='auditDate']").val(list1.auditDate);
			$("select[name='auditResult']").val(list1.auditResult);
			$("input[name='memo']").val(list1.memo);

			var list = data.respBody.list;
			$("#proAc_jqgrids").jqGrid('clearGridData');
			$("#proAc_jqgrids").jqGrid('setGridParam', {
				datatype: 'local',
				data: list, //newData是符合格式要求的重新加载的数据
			}).trigger("reloadGrid")
		}
	})
}