var count;
var pages;
var page = 1;
var rowNum;
$(function() {
	//点击表格图标显示模板列表
//	$(document).on('click', '.storeId1_biaoge', function() {
//		cli = 1
//		$("#wwrap").hide()
//		$("#purchaseOrder").hide()
//		$(".list_box").show()
//		$(".list_box").css("opacity",1)
//	});、
})


$(function() {
	$(document).on('click', '.clears', function() {
		window.location.reload()
	})
	$(document).on('click', '.breaks', function() {
		window.location.reload()
	})
})



//刚开始时可点击的按钮
$(function() {
	$(".saveOrders").addClass("gray") //参照
	$(".gray").attr("disabled", true)
	
	$(".download").removeClass("gray")
	$("#mengban").hide()
	$(".down").hide()
})
var myData = {};
//页面初始化
$(function() {
	allHeight()
	//初始化表格
	jQuery("#print_muBan_jqgrids").jqGrid({
		height: height,
//				data: data,
		autoScroll: true,
		datatype: "local", //请求数据返回的类型。可选json,xml,txt
		shrinkToFit: false,
		colNames: ['模板编码', '模板名称', '操作员', '操作员姓名', '备注', '编码', '操作日期'
		], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'templateId',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'templateName',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'opid',
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: 'opname',
				align: "center",
				editable: false,
				sortable: false,
				hidden:true
			},
			{
				name: 'reamrk',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'idx',
				align: "center",
				editable: false,
				sortable: false,
				hidden:true
			},
			{
				name: 'opdate',
				align: "center",
				editable: false,
				sortable: false,
			}
		],
//				rowNum: 10, //一页显示多少条
		rowList: [10, 20, 30], //可供用户选择一页显示多少条			
		autowidth: true,
		loadonce: true,
		multiboxonly: true,
		cellsubmit: "clientArray",
		rownumbers: true,
		pager: '#print_muBan_jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'storeId', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		rownumWidth: 20,  //序列号列宽度
		ondblClickRow: function(rowid) {
			if(mType==1){
				mType=1
			}else{
				mType = 2;
			}
			var gr = $("#print_muBan_jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
			$('#print_muBan_jqgrids').editRow(gr, true);
			console.log(mType)
			$("#" + gr + "_templateId").attr("readonly", "readonly");
			
			$(".saveOrders").removeClass("gray");
			$(".saveOrders").attr('disabled', false)
		},
		multiselect: true, //复选框
		caption: "商品档案", //表格的标题名字	
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#print_muBan_jqgrids").getGridParam('records'); //获取返回的记录数
			page = $("#print_muBan_jqgrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#print_muBan_jqgrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			search(page)
		},

	});
	search()
})

////查询按钮
$(document).on('click', '.search', function() {
	search()
})

//条件查询
function search() {
	var templateId = $("input[name='templateId1']").val();
	var templateName = $("input[name='templateName1']").val();
	var opid = $("input[name='opid1']").val();
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var data2 = {
		reqHead,
		"reqBody": {
			"templateId": templateId,
			"templateName": templateName,
			"opid": opid,
			"pageNo": page,
			"pageSize": rowNum
		}

	};
	var postD2 = JSON.stringify(data2);
	console.log(postD2)
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url3 + "/mis/whs/label/queryListLabelTemplates",
		data: postD2,
		dataType: 'json',
		async: true,
		success: function(data) {
			console.log(data)
			var mydata = {};
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#print_muBan_jqgrids").jqGrid("clearGridData");
			$("#print_muBan_jqgrids").jqGrid("setGridParam", {
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

var mType = 0;
$(function() {
	$(".saveOrders").click(function() {
		if(mType == 1) {
			addNewData(); //添加新数据
		} else if(mType == 2) {
			addEditData(); //编辑按钮		
		}
	})
})



//打开仓库/存货档案后点击确定取消
$(function() {
	//确定
	$(".addWhs").click(function() {
		//到货单
		//获得行号
		var rowid = $("#print_muBan_jqgrids").jqGrid('getGridParam', 'selrow');

		//	仓库档案
		//	获得行号
		var gr = $("#whs_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDatas = $("#whs_jqgrids").jqGrid('getRowData', gr);

		$("#" + rowid + "_warehouseId").val(rowDatas.whsEncd)
		$("#print_muBan_jqgrids").setRowData(rowid, {
			warehouseName: rowDatas.whsNm
		})

		$("#whsDocList").css("opacity", 0);
		$("#whsDocList").hide();
		$("#purchaseOrder").show();
	})
	//	取消
	$(".cancel").click(function() {
		$("#whsDocList").css("opacity", 0);
		$("#whsDocList").hide();
		$("#purchaseOrder").show();
		//到货单
		//获得行号
		var rowid = $("#print_muBan_jqgrids").jqGrid('getGridParam', 'selrow');

//		$("#" + rowid + "_warehouseId").val(rowDatas.whsEncd)
//		$("#print_muBan_jqgrids").setRowData(rowid, {
//			warehouseName: rowDatas.whsNm
//		})
	})
})

//增行   保存
$(function() {

	$(".addOrders").click(function() {
		$('button').addClass("gray");
		$(".saveOrders").removeClass("gray");
		$(".save").removeClass("gray");
		$(".cancel").removeClass("gray");
		$(".find").removeClass("gray");
		$(".findList").removeClass("gray");
		$(".true").removeClass("gray");
		$(".false").removeClass("gray");
		$(".upOrder").removeClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
		$("#mengban").hide();
		$(".addOrders").attr('disabled', true)
		$('.addOrders').addClass("gray");
		mType = 1;
					console.log(mType)
		//拿到Gride中所有主键id的值
		var gr = $("#print_muBan_jqgrids").jqGrid('getDataIDs');

		// 获得当前最大行号（数据编码）
		var rowid = Math.max.apply(Math, gr);
		// 获得新添加行的行号（数据编码）
		window.newrowid = rowid + 1;
		var dataRow = {
			"warehouseId": "",
			"warehouseName": "",
			"platformId": '',
			"platformName": '',
			"templateId": ''

		};
		$("#print_muBan_jqgrids").setColProp('storeId', {
			editable: true
		}); //设置editable属性由true改为false

		//将新添加的行插入到第一列
		$("#print_muBan_jqgrids").jqGrid("addRowData", newrowid, dataRow, "first");
		
		//设置grid单元格可编辑
		$('#print_muBan_jqgrids').jqGrid('editRow', newrowid, true);
		$("#print_muBan_jqgrids").jqGrid('setSelection',newrowid);
		var loginName = localStorage.loginAccNum
		$("#print_muBan_jqgrids").setRowData(newrowid, {
			opid: loginName
		})
	})

})

//判断
function IsCheckValue(templateId, templateName) {
	if(templateId == "") {
		alert("模板编码不能为空")
		return false;
	} else if(templateName == "") {
		alert("模板名称不能为空")
		return false;
	}
	return true;
}

//添加新数据
function addNewData() {
	console.log(newrowid)
	var ids = $("#print_muBan_jqgrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowData = $("#print_muBan_jqgrids").jqGrid('getRowData', ids);
	
	var templateName = $("#" + newrowid + "_templateName").val()
	var templateId = $("#" + newrowid + "_templateId").val()
	var reamrk = $("#" + newrowid + "_reamrk").val()
	var opid = rowData.opid
	
	if(IsCheckValue(templateId, templateName) == true) {
		
		var save = {
			"reqHead": reqhead,
			"reqBody": {
				"templateName": templateName,
				"templateId": templateId,
				"opid": opid,
				"reamrk": reamrk,
			}
		}
		var saveJson = JSON.stringify(save);
		console.log(saveJson)
		$.ajax({
			type: "post",
			url: url3 + "/mis/whs/label/insertLabelTemplates",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(msgAdd) {
				alert(msgAdd.respHead.message)
				$('.saveOrders').addClass("gray");
				$('.saveOrders').attr('disabled', true);
				$('.addOrders').removeClass("gray")
				$(".updites").removeClass("gray")
				$(".breaks").removeClass("gray")
				$(".clears").removeClass("gray")
				$(".search").removeClass("gray")
				$(".dels").removeClass("gray")
				$('addOrders').attr('disabled', false);
				$('updites').attr('disabled', false);
				$('breaks').attr('disabled', false);
				$('clears').attr('disabled', false);
				$(".search").attr('disabled', false);
				$(".dels").attr('disabled', false);
				search()
			},
			error: function(err) {
				console.log("失败")
			}
		});
	}
}

//修改保存按钮
function addEditData() {
	var gr = $("#print_muBan_jqgrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowData = $("#print_muBan_jqgrids").jqGrid('getRowData', gr);
	
	var templateName = $("#" + gr + "_templateName").val()
	var templateId = $("#" + gr + "_templateId").val()
	var reamrk = $("#" + gr + "_reamrk").val()
	var opid = rowData.opid
	var idx = rowData.idx
	
	if(IsCheckValue(templateId, templateName) == true) {
		
		var save = {
			"reqHead": reqhead,
			"reqBody": {
				'idx':idx,
				"templateName": templateName,
				"templateId": templateId,
				"opid": opid,
				"reamrk": reamrk,
			}
		}
		var saveJson = JSON.stringify(save);
		console.log(saveJson)
		$.ajax({
			type: "post",
			url: url3 + "/mis/whs/label/updateLabelTemplates",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(msgAdd) {
				alert(msgAdd.respHead.message)
				$('.saveOrders').addClass("gray");
				$('.saveOrders').attr('disabled', true);
				$('.addOrders').removeClass("gray")
				$(".updites").removeClass("gray")
				$(".breaks").removeClass("gray")
				$(".clears").removeClass("gray")
				$(".search").removeClass("gray")
				$(".dels").removeClass("gray")
				$('addOrders').attr('disabled', false);
				$('updites').attr('disabled', false);
				$('breaks').attr('disabled', false);
				$('clears').attr('disabled', false);
				$(".search").attr('disabled', false);
				$(".dels").attr('disabled', false);
				search()
			},
			error: function(err) {
				console.log("失败")
			}
		});
	}
}

////删除行
$(function() {
	$(".dels").click(function() {
		var num = []
		var gr = $("#print_muBan_jqgrids").jqGrid('getGridParam', 'selarrrow'); //获取行id
		for(var i = 0;i < gr.length;i++) {
			var rowDatas = $("#print_muBan_jqgrids").jqGrid('getRowData', gr[i]); //获取行数据    	
			var idx = rowDatas.idx
			num.push(idx)
		}
		var deleteAjax = {
			reqHead,
			"reqBody": {
				"idx": num.toString(),
			}
		};
		var deleteData = JSON.stringify(deleteAjax)
		console.log(deleteData)
		if(gr == null) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url3 + "/mis/whs/label/deleteLabelTemplates",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(remover) {
					alert(remover.respHead.message);
					search()
				},
				error: function() {
					console.log("删除失败")

				}
			});

		}
	})
})

