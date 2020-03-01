var count;
var pages;
var page = 1;
var rowNum;
var mType = 0;
//刚开始时可点击的按钮
$(function() {
	$(".saveOrder").addClass("gray") //参照
	$(".del").addClass("gray") //参照
	$(".editOrder").addClass("gray") //参照
	$(".gray").attr("disabled", true)
})

var myData = {};
//页面初始化
$(function() {
	//初始化表格
	allHeight()
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	jQuery("#present_jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		height: height,
		autoScroll: true,
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		shrinkToFit: false,
		colNames: ['序号', '赠品编码','赠品名称'], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'id',
				align: "center",
				editable: true,
				sortable: false,
				hidden: true
			},
			{
				name: 'presentEncd',
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: 'presentNm',
				align: "center",
				editable: false,
				sortable: false,
			}
		],
		rowList: [10, 20, 30], //可供用户选择一页显示多少条			
		autowidth: true,
		loadonce: true,
		cellEdit: true,
		//		multiboxonly: true,
		rownumWidth: 8, //序列号列宽度
		cellsubmit: "clientArray",
		multiselectWidth: 10, //复选框列宽度
		rownumbers: true,
		pager: '#present_jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'storeId', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		caption: "赠品范围", //表格的标题名字	
		afterEditCell: function(rowid, cellname, val, iRow, iCol) {
			$(".saveOrder").addClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true)
			if(cellname == "presentEncd") {
//				$("#" + rowid + "_" + cellname).bind('keyup', function() {
//					findGrid(rowid, cellname, val);
//				})
				$("#" + rowid + "_presentEncd").bind("dblclick", function() {
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
			if(cellname == "presentEncd") {
				GetGoodsInfo_pre(rowid, val);
			}
		}
	});
	$("#present_jqgrids").jqGrid('navGrid', '#present_jqGridPager', {
		edit: false,
		add: false,
		del: false,
		search: false,
		refresh: false,
	}).navButtonAdd('#present_jqGridPager', {
		caption: "",
		buttonicon: "ui-icon-trash",
		onClickButton: function() {
			var grid = "present_jqgrids"
			//删除一行操作
			removeRows1(grid);
		},
		position: "first"
	}).navButtonAdd('#present_jqGridPager', {
		caption: "",
		buttonicon: "ui-icon-newwin",
		onClickButton: function() {
			//复制一行操作
			copyRows1();
		},
		position: "last"
	}).navButtonAdd('#present_jqGridPager', {
		caption: "",
		buttonicon: "ui-icon-plus",
		onClickButton: function() {
			//新增一行操作
			addRows1();
		},
		position: "last"
	})
})

//根据存货编码查询存货详细信息
function GetGoodsInfo_pre(rowid, goods) {
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": goods
		}
	};
	if(goods) {
		var postData = JSON.stringify(data);
		$.ajax({
			url: url + '/mis/purc/InvtyDoc/printingInvtyDocList',
			type: 'post',
			data: postData,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			error: function() {
				alert("获取数据错误");
			}, //错误执行方法
			success: function(data) {
				var list = data.respBody.list;
				if(list.length == 0) {
					alert("无此存货,请重新输入")
					$("#present_jqgrids").setRowData(rowid, {
						presentEncd: "",
						presentNm: "",
					});
					return;
				}
				$("#" + rowid + "_invtyEncd").val(list[0].invtyEncd);
				if(list.length == 1) {
					$("#present_jqgrids").setRowData(rowid, {
						presentNm: list[0].invtyNm,
					});
				} else {
					for(var i = 1; i < list.length; i++) {
						$("#present_jqgrids").setRowData(rowid, {
							presentNm: list[0].invtyNm,
						});
						//设置页面数据展示
						$("#present_jqgrids").setRowData(++rowid, {
							presentEncd: list[i].invtyEncd,
							presentNm: list[i].invtyNm,
						});
					}
				}
			}
		})
	}
}

//新增表格行
function addRows1() {
	mType = 1;
	var gr = $("#present_jqgrids").jqGrid('getDataIDs');
	if(gr.length == 0) {
		var rowid = 0;
	} else if(gr.length != 0) {
		var rowid = Math.max.apply(Math, gr);
	}
	window.newrowid = rowid + 1;
	var dataRow = {};
	$("#present_jqgrids").jqGrid("addRowData", newrowid, dataRow, "last");
}
//删除表格行
function removeRows1() {
	var gr = $("#present_jqgrids").jqGrid('getGridParam', 'selarrrow');
	if(gr.length == 0) {
		alert("请选择要删除的行");
		return;
	} else {
		var length = gr.length;
		for(var i = 0; i < length + 1; i++) {
			$("#present_jqgrids").jqGrid("delRowData", gr[0]);
		}
	}
}
//复制表格行
function copyRows1() {
	var ids = $("#present_jqgrids").jqGrid('getGridParam', 'selarrrow');
	var dataRow = $("#present_jqgrids").jqGrid('getRowData', ids);
	if(ids.length == 0) {
		alert("请选择要复制的行");
		return;
	} else if(ids.length > 1) {
		alert("每次只能复制一行");
		return;
	} else {
		var gr = $("#present_jqgrids").jqGrid('getDataIDs');
		// 选中行实际表示的位置
		var rowid = Math.max.apply(Math, gr);
		// 新插入行的位置
		var newrowid = rowid + 1;
		// 插入一行
		$("#present_jqgrids").jqGrid("addRowData", newrowid, dataRow, "last");
	}
}


$(function() {
	$(".addOLine").click(function() {
		mType = 1
		$(".addOLine").addClass("gray");
		$(".saveOrder").removeClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true);
		$("#present_jqgrids").setColProp("presentEncd", {
			editable: true
		});
		$("#present_jqgrids").jqGrid('clearGridData');
	    $("#present_jqgrids").jqGrid('setGridParam', {
	       url: '../../assets/js/json/order.json',
	       datatype: "json",
	    }).trigger("reloadGrid")
	    $("input").val("");
	})
	$(".editOrder").click(function() {
		mType = 2
		$(".addOLine").addClass("gray");
		$(".saveOrder").removeClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true);
		//		$("input[name='presentRangeEncd']").attr("readonly","readonly")
		window.presentRangeEncd1 = $("input[name='presentRangeEncd']").val()
		$("#present_jqgrids").setColProp("presentEncd", {
			editable: true
		});
	})
})

function IsCheckValue(presentRangeEncd, presentRangeName, listData) {
	var numb = []
	if(presentRangeEncd == '') {
		alert("赠品范围编码不能为空")
		return false;
	} else if(presentRangeName == '') {
		alert("赠品范围名称不能为空")
		return false;
	} else if(listData.length == 0) {
		alert("赠品编码不能为空")
		return false;
	} else if(listData.length != 0) {
		for(var i = 0; i < listData.length; i++) {
			numb.push(listData[i].presentEncd)
		}
		let nary = numb.slice().sort();
		for(let k = 0; k < nary.length; k++) {
			if(nary[k] == nary[k + 1]) {
				alert("赠品编码重复内容：" + nary[k]);
				return false;
			}
		}
	}
	return true;
}


$(function() {
	$(".clear").click(function() {
		window.location.reload()
	})
})


$(function() {
	//确定
	$(".true").click(function() {

		var ids = $("#present_jqgrids").jqGrid('getGridParam', 'selrow');

		//	获得行号
		var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDatas = $("#jqgrids").jqGrid('getRowData', gr);
		$("#" + ids + "_presentEncd").val(rowDatas.invtyEncd)
		$("#present_jqgrids").setRowData(ids, {
			presentNm: rowDatas.invtyNm
		})
		$("#wwrap").hide()
		$("#purchaseOrder").show()
		$("#wwrap").css("opacity", 0)
	})
	//	取消
	$(".false").click(function() {
		$("#wwrap").hide()
		$("#purchaseOrder").show()
		$("#wwrap").css("opacity", 0)
	})

})

//	第一个
function getJQAllData() {
	//拿到grid对象
	var obj = $("#present_jqgrids");
	//获取grid表中所有的rowid值
	var rowIds = obj.getDataIDs();
	//初始化一个数组arrayData容器，用来存放rowData
	var arrayData = new Array();
	if(rowIds.length > 0) {
		for(var i = 0; i < rowIds.length; i++) {
			if(obj.getRowData(rowIds[i]).presentEncd == "") {
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
	var listData = getJQAllData()
	var presentRangeEncd = $("input[name='presentRangeEncd']").val()
	var presentRangeName = $("input[name='presentRangeName']").val()
	if(IsCheckValue(presentRangeEncd, presentRangeName, listData) == true) {
		var save = {
			"reqHead": reqhead,
			"reqBody": {
				"presentRangeEncd": presentRangeEncd,
				"presentRangeName": presentRangeName,
				"list": listData
			}
		}
		var saveJson = JSON.stringify(save);
		$.ajax({
			type: "post",
			url: url3 + "/mis/ec/presentRangeList/insertPresentRangeList",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(data) {
				alert(data.respHead.message)
				//				$('button').attr('disabled', false);
				//				$(".gray").attr('disabled', true);
				$(".del").removeClass("gray");
				$(".del").attr('disabled', false);
				$(".editOrder").removeClass("gray");
				$(".editOrder").attr('disabled', false);
				$(".addOLine").removeClass("gray");
				$(".addOLine").attr('disabled', false);
				$(".saveOrder").addClass("gray");
				$(".saveOrder").attr('disabled', true);
				$("#present_jqgrids").setColProp("presentEncd", {
					editable: false
				});
			},
			error: function(err) {
				console.log("失败")
			}
		});
	}
}

//修改保存按钮
function addEditData() {
	var listData = getJQAllData()
	var presentRangeEncd = $("input[name='presentRangeEncd']").val()
	var presentRangeName = $("input[name='presentRangeName']").val()
	if(presentRangeEncd === presentRangeEncd1) {
		if(IsCheckValue(presentRangeEncd, presentRangeName, listData) == true) {

			var save = {
				"reqHead": reqhead,
				"reqBody": {
					"presentRangeEncd": presentRangeEncd,
					"presentRangeName": presentRangeName,
					"list": listData
				}
			}
			var saveJson = JSON.stringify(save);
			$.ajax({
				type: "post",
				url: url3 + "/mis/ec/presentRangeList/updatePresentRangeList",
				async: true,
				data: saveJson,
				dataType: 'json',
				contentType: 'application/json',
				success: function(data) {
					alert(data.respHead.message)
					$(".del").removeClass("gray");
					$(".del").attr('disabled', false);
					$(".editOrder").removeClass("gray");
					$(".editOrder").attr('disabled', false);
					$(".addOLine").removeClass("gray");
					$(".addOLine").attr('disabled', false);
					$(".saveOrder").addClass("gray");
					$(".saveOrder").attr('disabled', true);
					$("#present_jqgrids").setColProp("presentEncd", {
						editable: false
					});
				},
				error: function(err) {
					console.log("失败")
				}
			});
		}
	} else {
		alert("赠品范围编码不可修改")
	}
}

$(function() {
	// 点击删除按钮，执行的操作
	$('.del').click(function() {
		var presentRangeEncd = $("input[name='presentRangeEncd']").val()
		mType = 0;
		var deleteAjax = {
			"reqHead": reqhead,
			"reqBody": {
				"presentRangeEncd": presentRangeEncd
			}
		}
		var deleteData = JSON.stringify(deleteAjax);
		if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + '/mis/ec/presentRangeList/deletePresentRangeList',
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
	if(a == 2||a == 1) {
		chaxun()
	}
})

function chaxun() {
	$("input[name='presentRangeEncd']").attr("readonly","readonly")
	presentRangeEncd = localStorage.presentRangeEncd;
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"presentRangeEncd": presentRangeEncd,
			"pageNo": 1,
			"pageSize": 500
		}
	};
	var saveData = JSON.stringify(savedata);
	var myDate = {};
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/ec/presentRangeList/selectPresentRangeListById',
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
			$(".addOLine").attr('disabled', true);
			$(".editOrder").attr('disabled', false);
			var list1 = data.respBody
			$("input[name='presentRangeEncd']").val(list1.presentRangeEncd);
			$("input[name='presentRangeName']").val(list1.presentRangeName);

			var list = data.respBody.list;
			$("#present_jqgrids").jqGrid('clearGridData');
			$("#present_jqgrids").jqGrid('setGridParam', {
				datatype: 'local',
				data: list, //newData是符合格式要求的重新加载的数据
			}).trigger("reloadGrid")
		}
	})
}
