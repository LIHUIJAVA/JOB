var count;
var pages;
var page = 1;
var rowNum;
var mType = 0;
//刚开始时可点击的按钮
$(function() {
	$(".saveOrder").addClass("gray")
	$(".dels").addClass("gray")
	$(".editOrder").addClass("gray")
	$(".sure").removeClass("gray")
	$(".cannle").removeClass("gray")
	$(".search").removeClass("gray")
	$(".gray").attr("disabled", true)
	$("#mengban").show()
})


var myData = {};
//页面初始化
$(function() {
	//初始化表格
	allHeight()
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	$("#pro_jqGrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json",
		colNames: ['序号', '金额', '数量', '积分倍数', '赠品数量', '赠品范围', '主表编码', '备注'], //jqGrid的列显示名字
		colModel: [{
				name: 'no',
				index: 'id',
				editable: true,
				align: 'center',
				sortable: false,
				hidden: true
			}, {
				name: 'money',
				index: 'id',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'number',
				editable: true,
				index: 'id',
				align: 'center',
				sortable: false
			},
			{
				name: 'integralMul',
				index: 'id',
				editable: true,
				align: 'center',
				sortable: false

			},
			{
				name: 'giftNum',
				editable: true,
				index: 'id',
				align: 'center',
				sortable: false
			},
			{
				name: 'giftRange',
				editable: true,
				align: 'center',
				sortable: false,
				index: 'id',
			},
			{
				name: 'proPlanId',
				editable: true,
				align: 'center',
				sortable: false,
				index: 'id',
				hidden:true
			},
			{
				name: 'memo',
				editable: true,
				index: 'id',
				align: 'center',
				sortable: false
			},
		],		
		autowidth: true,
		loadonce: true,
		cellEdit: true,
		// 隐藏翻页和输入页码
		pgbuttons: false,
		pginput:false,
		rowNum:9999999,
		height:height,
		//		multiboxonly: true,
		//		rownumWidth: 4, //序列号列宽度
		cellsubmit: "clientArray",
		//		multiselectWidth: 10, //复选框列宽度
		rownumbers: true,
		pager: '#pro_jqgridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'storeId', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		caption: "促销方案", //表格的标题名字	
		afterEditCell: function(rowid, cellname, val, iRow, iCol) {
			$(".saveOrder").addClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true)
			if(cellname == "giftRange") {
				$("#" + rowid + "_giftRange").attr("readonly", "readonly");
				$("#" + rowid + "_giftRange").bind("dblclick", function() {
					$("#wwrap").show()
					$("#purchaseOrder").hide()
					$("#wwrap").css("opacity", 1)
				})
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
	});
	$("#pro_jqGrids").jqGrid('navGrid', '#pro_jqgridPager', {
		edit: false,
		add: false,
		del: false,
		search: false,
		refresh: false,
	}).navButtonAdd('#pro_jqgridPager', {
		caption: "",
		buttonicon: "ui-icon-trash",
		onClickButton: function() {
			var grid = "pro_jqGrids"
			//删除一行操作
			removeRows1(grid);
		},
		position: "first"
	}).navButtonAdd('#pro_jqgridPager', {
		caption: "",
		buttonicon: "ui-icon-newwin",
		onClickButton: function() {
			//复制一行操作
			copyRows1();
		},
		position: "last"
	}).navButtonAdd('#pro_jqgridPager', {
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
	var gr = $("#pro_jqGrids").jqGrid('getDataIDs');
	if(gr.length == 0) {
		var rowid = 0;
	} else if(gr.length != 0) {
		var rowid = Math.max.apply(Math, gr);
	}
	window.newrowid = rowid + 1;
	var dataRow = {};
	$("#pro_jqGrids").jqGrid("addRowData", newrowid, dataRow, "last");
}
//删除表格行
function removeRows1() {
	var gr = $("#pro_jqGrids").jqGrid('getGridParam', 'selarrrow');
	if(gr.length == 0) {
		alert("请选择要删除的行");
		return;
	} else {
		var length = gr.length;
		for(var i = 0; i < length + 1; i++) {
			$("#pro_jqGrids").jqGrid("delRowData", gr[0]);
		}
	}
}
//复制表格行
function copyRows1() {
	var ids = $("#pro_jqGrids").jqGrid('getGridParam', 'selarrrow');
	var dataRow = $("#pro_jqGrids").jqGrid('getRowData', ids);
	if(ids.length == 0) {
		alert("请选择要复制的行");
		return;
	} else if(ids.length > 1) {
		alert("每次只能复制一行");
		return;
	} else {
		var gr = $("#pro_jqGrids").jqGrid('getDataIDs');
		// 选中行实际表示的位置
		var rowid = Math.max.apply(Math, gr);
		// 新插入行的位置
		var newrowid = rowid + 1;
		// 插入一行
		$("#pro_jqGrids").jqGrid("addRowData", newrowid, dataRow, "last");
	}
}
$(function() {
	/*------点击增加按钮加载动态下拉促销方案-------*/
	var savedata = {
		'reqHead': reqhead,
		'reqBody': {
			'pageNo': 1,
			'pageSize': 500
		},
	};
	var saveData = JSON.stringify(savedata)
	$.ajax({
		type: 'post',
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/ec/proCondition/selectProCondition',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			list = data.respBody.list;
			var option_html = '';
			option_html += '<option value="0" disabled selected>' + "" + "</option>"
			for(i = 0; i < list.length; i++) {
				option_html += '<option value="' + list[i].proConditionEncd + '"' + 'id="ab">' + list[i].proConditionName + "</option>"
			}
			window.pro = $(".proCriteria").first().children("option").val()
			$(".proCriteria").html(option_html);
			$(".proCriteria").change(function(e) {
				window.val = this.value;
				pro = this.value
				window.localStorage.setItem("pro", pro);
			})

		},
		error: function() {
			console.log(error)
		}
	})
})

$(function() {
	// 点击增加按钮，执行的操作
	$('.addOLine').click(function() {
		$("#mengban").hide()
		$(".addOLine").addClass("gray");
		$(".saveOrder").removeClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true);
		mType = 1
		$("#pro_jqGrids").jqGrid('clearGridData');
	    $("#pro_jqGrids").jqGrid('setGridParam', {
	       url: '../../assets/js/json/order.json',
	       datatype: "json",
	    }).trigger("reloadGrid")
	    $(".inp").val("");
	    $("select[name='proCriteria'] option[value='0']").prop("selected", true);
	    $("select[name='giftMul'] option[value='0']").prop("selected", true);
	});
	$(".editOrder").click(function() {
		$("#mengban").hide()
		$(".saveOrder").removeClass("gray");
		$(".editOrder").addClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true);
		mType = 2
		window.proPlanId1 = $("input[name='proPlanId']").val()
	})
	
})

function IsCheckValue(proPlanId, proPlanName, proCriteria, listData) {
	if(proPlanName == '') {
		alert("促销方案名称不能为空")
		return false;
	} else if(proPlanId == '') {
		alert("促销方案编码不能为空")
		return false;
	} else if(proCriteria == 0) {
		alert("促销条件不能为空")
		return false;
	} else if(listData.length == 0) {
		alert("未添加子表！")
		return false;
	} else if(listData.length != 0) {
		for(var i = 0; i < listData.length; i++) {
			if(listData[i].money == "") {
				alert("金额不能为空")
				return false;
			} else if(listData[i].qty == "") {
				alert("数量不能为空")
				return false;
			} else if(listData[i].integralMul == "") {
				alert("积分倍数不能为空")
				return false;
			} else if(listData[i].giftNum == "") {
				alert("赠品数量不能为空")
				return false;
			} else if(listData[i].giftRange == "") {
				alert("赠品范围不能为空")
				return false;
			}
		}
	}
	return true;
}

$(function() {
	//确定
	$(".sure").click(function() {

		var ids = $("#pro_jqGrids").jqGrid('getGridParam', 'selrow');

		//	获得行号
		var gr = $("#present_list_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDatas = $("#present_list_jqgrids").jqGrid('getRowData', gr);
		$("#" + ids + "_giftRange").val(rowDatas.presentRangeEncd)

		$("#wwrap").hide()
		$("#purchaseOrder").show()
		$("#wwrap").css("opacity", 0)
	})
	//	取消
	$(".cannle").click(function() {
		$("#wwrap").hide()
		$("#purchaseOrder").show()
		$("#wwrap").css("opacity", 0)
	})

})

function getJQAllData() {
	//拿到grid对象
	var obj = $("#pro_jqGrids");
	//获取grid表中所有的rowid值
	var rowIds = obj.getDataIDs();
	//初始化一个数组arrayData容器，用来存放rowData
	var arrayData = new Array();
	if(rowIds.length > 0) {
		for(var i = 0; i < rowIds.length; i++) {
			if(obj.getRowData(rowIds[i]).money == "") {
				continue;
			} else {
				arrayData.push(obj.getRowData(rowIds[i]));
			}
		}
	}
	return arrayData;
}

// 点击保存，传送数据给后台
var isclick = true;
$(function() {
	$(".saveOrder").click(function() {
		if(isclick) {
			isclick = false;
			if(mType == 1) {
				addNewData();
			}
			if(mType == 2) {
				addEditData();
			}
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	})
})
//添加新数据
function addNewData() {
	var listData = getJQAllData();
	var proPlanId = $("input[name='proPlanId']").val();
	var proPlanName = $("input[name='proPlanName']").val();
	var proCriteria = $("select[name='proCriteria'] option:selected").val();
	var proWay = $("select[name='proWay'] option:selected").val();
	var giftMul = $("select[name='giftMul'] option:selected").val();
	var memo = $("input[name='memo']").val();

	//判断页面是否有值为空
	if(IsCheckValue(proPlanId, proPlanName, proCriteria, listData) == true) {
		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				'proPlanId': proPlanId,
				'proPlanName': proPlanName,
				'proWay': proWay,
				'proCriteria': proCriteria,
				'giftMul': giftMul,
				'memo': memo,
				'list': listData
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			url: url + '/mis/ec/proPlan/add',
			type: 'post',
			data: saveData,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			success: function(data) {
				$("#mengban").show()
				$(".saveOrder").addClass("gray");
				$(".dels").removeClass("gray");
				$(".addOLine").removeClass("gray");
				$(".editOrder").removeClass("gray");
				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true);
				alert(data.respHead.message)
			},
			error: function() {
				console.log(error);
			} //错误执行方法
		})
	} else {
		$('button').addClass("gray");
		$(".saveOrder").removeClass("gray");
		$(".upOrder").removeClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
	}
}

//修改保存按钮
function addEditData() {
	var listData = getJQAllData();
	var proPlanId = $("input[name='proPlanId']").val();
	var proPlanName = $("input[name='proPlanName']").val();
	var proCriteria = $("select[name='proCriteria'] option:selected").val();
	var proWay = $("select[name='proWay'] option:selected").val();
	var giftMul = $("select[name='giftMul'] option:selected").val();
	var memo = $("input[name='memo']").val();
	if(proPlanId === proPlanId1) {
		if(IsCheckValue(proPlanId, proPlanName, proCriteria, listData) == true) {

			var save = {
				"reqHead": reqhead,
				"reqBody": {
					'proPlanId': proPlanId,
					'proPlanName': proPlanName,
					'proWay': proWay,
					'proCriteria': proCriteria,
					'giftMul': giftMul,
					'memo': memo,
					'list': listData
				}
			}
			var saveJson = JSON.stringify(save);
			$.ajax({
				type: "post",
				url: url3 + "/mis/ec/proPlan/edit",
				async: true,
				data: saveJson,
				dataType: 'json',
				contentType: 'application/json',
				success: function(data) {
					$(".dels").removeClass("gray");
					$(".saveOrder").addClass("gray");
					$(".addOLine").removeClass("gray");
					$(".editOrder").removeClass("gray");
					$('button').attr('disabled', false);
					$(".gray").attr('disabled', true);
					$("#mengban").show()
					alert(data.respHead.message)
				},
				error: function(err) {
					console.log("失败")
				}
			});
		}
	} else {
		alert("促销方案编码不可修改")
	}

}

$(function() {
	// 点击删除按钮，执行的操作
	$('.dels').click(function() {
		var proPlanId = $("input[name='proPlanId']").val()
		mType = 0;
		var deleteAjax = {
			"reqHead": reqhead,
			"reqBody": {
				"proPlanId": proPlanId
			}
		}
		var deleteData = JSON.stringify(deleteAjax);
		if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + '/mis/ec/proPlan/delete',
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(remover) {
					alert(remover.respHead.message)
					window.location.reload()
				},
				error: function() {
					alert("删除失败")
				}
			});
		}
	});
})

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
	$("input[name='proPlanId']").attr("readonly", "readonly")
	var proPlanId = localStorage.proPlanId1;
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"proPlanId": proPlanId,
			"pageNo": 1,
			"pageSize": 500
		}
	};
	var saveData = JSON.stringify(savedata);
	var myDate = {};
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/ec/proPlan/query',
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
			$(".addOLine").addClass("gray")
			$(".editOrder").removeClass("gray")
			$(".dels").removeClass("gray")
			$(".addOLine").attr('disabled', true);
			$(".editOrder").attr('disabled', false);
			$(".dels").attr('disabled', false);
			var list1 = data.respBody
			$("input[name='proPlanId']").val(list1.proPlanId);
			$("input[name='proPlanName']").val(list1.proPlanName);
			$("input[name='memo']").val(list1.memo);
			$("select[name='proWay']").val(list1.proWay);
			$("select[name='proCriteria']").val(list1.proCriteria);
			$("select[name='giftMul']").val(list1.giftMul);

			var list = data.respBody.list;
			$("#pro_jqGrids").jqGrid('clearGridData');
			$("#pro_jqGrids").jqGrid('setGridParam', {
				datatype: 'local',
				data: list, //newData是符合格式要求的重新加载的数据
			}).trigger("reloadGrid")
		}
	})
}