var count;
var pages;
var page = 1;
var rowNum;
$(function() {
	$(document).on('click', '.templateId_biaoge', function() {
		window.open("../../Components/ec/Print_mubanList.html", 'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
		
	});
	$(document).on('click', '.storeId1_biaoge', function() {
		window.open("../../Components/baseDoc/whsList.html", 'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})


$(function() {
	$(document).on('click', '.clear', function() {
		window.location.reload()
	})
	$(document).on('click', '.break', function() {
		window.location.reload()
	})
})


$(function() {
	/*------加载动态下拉电商平台-------*/
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
			option_html += '<option value="" disabled selected>' + "请选择" + "</option>"
			for(i = 0; i < list.length; i++) {
				option_html += '<option value="' + list[i].ecId + '"' + 'id="ab">' + list[i].ecName + "</option>"
			}
			window.pro = $(".ecOrderId1").first().children("option").val()
			$(".ecOrderId1").html(option_html);
			$(".ecOrderId1").change(function(e) {
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

//刚开始时可点击的按钮
$(function() {
	$(".saveOrder").addClass("gray") //参照
	$(".gray").attr("disabled", true)
	
	$(".download").removeClass("gray")
	$("#mengban").hide()
	$(".down").hide()
})

//电商平台名称下拉框
function getEcNum() {
	var ecNum = "";
	var data2 = {
		reqHead,
		"reqBody": {
			"pageNo": 1,
			"pageSize": 500

		}
	}
	var postD2 = JSON.stringify(data2);
	$.ajax({
		url: url3 + "/mis/ec/ecPlatform/queryList",
		type: "post",
		async: false,
		dataType: 'json', //请求数据返回的类型。可选json,xml,txt
		data: postD2,
		contentType: 'application/json',
		success: function(e) {
			var result = e.respBody.list;
			ecNum += "0" + ":" + "请选择" + ";";
			for(var i = 0; i < result.length; i++) {
				if(i != result.length - 1) {
					ecNum += result[i].ecId + ":" + result[i].ecName + ";";
					let aaa=result[i].ecId;
					let bbb=result[i].ecName
				} else {
					ecNum += result[i].ecId + ":" + result[i].ecName;
					let aaa=result[i].ecId;
					let bbb=result[i].ecName
				}
			}


		}
	});
	return ecNum; //必须有此返回值	
	
}

var myData = {};
//页面初始化
$(function() {
	allHeight()
	//初始化表格
	jQuery("#print_guanLian_jqgrids").jqGrid({
		height: height,
//				data: data,
		autoScroll: true,
		datatype: "local", //请求数据返回的类型。可选json,xml,txt
		shrinkToFit: false,
		colNames: ['序号', '仓库编码', '仓库名称', '平台编码', '平台名称', '模板编码'
		], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'idx',
				align: "center",
				editable: false,
				sortable: false,
				hidden: true
			},
			{
				name: 'warehouseId',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'warehouseName',
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: 'platformId',
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: 'platformName',
				align: "center",
				editable: true,
				sortable: false,
				edittype: 'select',
				editoptions: {
					value: getEcNum()
				}
			},
			{
				name: 'templateId',
				align: "center",
				editable: true,
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
		pager: '#print_guanLian_jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'storeId', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		rownumWidth: 20,  //序列号列宽度
		ondblClickRow: function(rowid) {
			var gr = $("#print_guanLian_jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
			if(mType==1){
				mType=1
			}else{
				mType = 2;
				$('#print_guanLian_jqgrids').editRow(gr, true);
				$("#" + gr + "_warehouseId").attr("readonly", "readonly");
				$("#" + gr + "_platformName").attr("disabled", "disabled");
			}
			console.log(mType)
			grr(gr)
			
			$(".saveOrder").removeClass("gray");
			$(".saveOrder").attr('disabled', false)
		},
		multiselect: true, //复选框
		caption: "商品档案", //表格的标题名字	
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#print_guanLian_jqgrids").getGridParam('records'); //获取返回的记录数
			page = $("#print_guanLian_jqgrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#print_guanLian_jqgrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			loadLocalData(page);
		},

	});
	search5()
})

////查询按钮
$(document).on('click', '.search', function() {
	search5()
})

//条件查询
function search5() {
	var platformId = $("select[name='ecOrderId1'] option:selected").val();
	
	var warehouseId = $("input[name='warehouseId1']").val();
	var templateId = $("input[name='templateId1']").val();
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var data2 = {
		reqHead,
		"reqBody": {
			"warehouseId": warehouseId,
			"platformId": platformId,
			"templateId": templateId,
			"pageNo": page,
			"pageSize": rowNum
		}

	};
	var postD2 = JSON.stringify(data2);
	console.log(postD2)
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url3 + "/mis/whs/label/queryListLabelTemplateRelation",
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
			$("#print_guanLian_jqgrids").jqGrid("clearGridData");
			$("#print_guanLian_jqgrids").jqGrid("setGridParam", {
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
	$(".saveOrder").click(function() {
		if(mType == 1) {
			$(".addOrder").css("background-color", 'black')
			addNewData(); //添加新数据
		} else if(mType == 2) {
			addEditData(); //编辑按钮		
		}
	})
})


function grr(id) {
	console.log(id)
	$("#" + id + "_warehouseId").on("dblclick", function() {
		$("#purchaseOrder").hide()
		$("#whsDocList").show()
		$("#whsDocList").css("opacity", 1)
	})
	$("#" + id + "_templateId").on("dblclick", function() {
		$("#inquiry").removeClass("gray")
		$("#inquiry").attr("disabled",false)
		$(".yes").removeClass("gray")
		$(".yes").attr("disabled",false)
		$(".no").removeClass("gray")
		$(".no").attr("disabled",false)
		$("#whsDocList").hide()
		$("#purchaseOrder").hide()
		$("#muban").show()
		$("#muban").css("opacity", 1)
	})
}
//打开仓库/存货档案后点击确定取消
$(function() {
	//确定
	$(".addWhs").click(function() {
		//到货单
		//获得行号
		var rowid = $("#print_guanLian_jqgrids").jqGrid('getGridParam', 'selrow');

		//	仓库档案
		//	获得行号
		var gr = $("#whs_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDatas = $("#whs_jqgrids").jqGrid('getRowData', gr);

		$("#" + rowid + "_warehouseId").val(rowDatas.whsEncd)
		$("#print_guanLian_jqgrids").setRowData(rowid, {
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
		var rowid = $("#print_guanLian_jqgrids").jqGrid('getGridParam', 'selrow');

//		$("#" + rowid + "_warehouseId").val(rowDatas.whsEncd)
//		$("#print_guanLian_jqgrids").setRowData(rowid, {
//			warehouseName: rowDatas.whsNm
//		})
	})
	//模板确定
	$(".yes").click(function() {

		var ids = $("#print_guanLian_jqgrids").jqGrid('getGridParam', 'selrow');
	
		//	获得行号
		var gr = $("#print_muBanList_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDatas = $("#print_muBanList_jqgrids").jqGrid('getRowData', gr);
		if(mType == 1) {
			$("#" + newrowid + "_templateId").val(rowDatas.templateId)
			
		} else if(mType == 2) {
			$("#" + ids + "_templateId").val(rowDatas.templateId)
			
		}

		$("#muban").css("opacity", 0);
		$("#whsDocList").css("opacity", 0);
		$("#muban").hide()
		$("#whsDocList").hide()
		$("#purchaseOrder").show();
	})
	//	取消
	$(".no").click(function() {
		$("#muban").css("opacity", 0);
		$("#whsDocList").css("opacity", 0);
		$("#muban").hide()
		$("#whsDocList").hide()
		$("#purchaseOrder").show();
	})
})

//增行   保存
$(function() {

	$(".addOrder").click(function() {
		$('button').addClass("gray");
		$(".saveOrder").removeClass("gray");
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
		$(".addOrder").attr('disabled', true)
		$('.addOrder').addClass("gray");
		mType = 1;
					console.log(mType)
		//拿到Gride中所有主键id的值
		var gr = $("#print_guanLian_jqgrids").jqGrid('getDataIDs');

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
		$("#print_guanLian_jqgrids").setColProp('storeId', {
			editable: true
		}); //设置editable属性由true改为false

		//将新添加的行插入到第一列
		$("#print_guanLian_jqgrids").jqGrid("addRowData", newrowid, dataRow, "first");
		
		//设置grid单元格可编辑
		$('#print_guanLian_jqgrids').jqGrid('editRow', newrowid, true);
		$("#print_guanLian_jqgrids").jqGrid('setSelection',newrowid);
		grr(newrowid)
		$("#" + newrowid + "_platformName").on("change",function() {
			var name = $("#" + newrowid + "_platformName").val()
			console.log(name)
			$("#print_guanLian_jqgrids").setRowData(newrowid, {
				platformId: name
			})
		})
	})

})

//判断
function IsCheckValue(warehouseId, platformId, templateId) {
	if(warehouseId == "") {
		alert("仓库编码不能为空")
		return false;
	} else if(platformId == 0) {
		alert("平台未选择")
		return false;
	}  else if(templateId == "") {
		alert("模板编码不能为空")
		return false;
	}
	return true;
}

//添加新数据
function addNewData() {
	console.log(newrowid)
	var ids = $("#print_guanLian_jqgrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowData = $("#print_guanLian_jqgrids").jqGrid('getRowData', ids);
	var platformId = $("#" + newrowid + "_platformName option:selected").val();
	console.log(platformId)
	var warehouseId = $("#" + newrowid + "_warehouseId").val()
	var templateId = $("#" + newrowid + "_templateId").val()
	
	var warehouseName = rowData.warehouseName
	var platformName = $("#" + newrowid + "_platformName option:selected").text();
	
	if(IsCheckValue(warehouseId, platformId, templateId) == true) {
		
		var save = {
			"reqHead": reqhead,
			"reqBody": {
				"warehouseId": warehouseId,
				"platformName": platformName,
				"warehouseName": warehouseName,
				"platformId": platformId,
				"templateId": templateId
			}
		}
		var saveJson = JSON.stringify(save);
		console.log(saveJson)
		$.ajax({
			type: "post",
			url: url3 + "/mis/whs/label/insertlabeLtemplaterelation",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(msgAdd) {
				alert(msgAdd.respHead.message)
				mType = 2
				$('.saveOrder').addClass("gray");
				$('.saveOrder').attr('disabled', true);
				$('.addOrder').removeClass("gray")
//				$(".updite").removeClass("gray")
				$(".break").removeClass("gray")
				$(".clear").removeClass("gray")
				$(".search").removeClass("gray")
				$(".del").removeClass("gray")
				$('.addOrder').attr('disabled', false);
//				$('.updite').attr('disabled', false);
				$('.break').attr('disabled', false);
				$('.clear').attr('disabled', false);
				$(".search").attr('disabled', false);
				$(".del").attr('disabled', false);
				search5()
			},
			error: function(err) {
				console.log("失败")
			}
		});
	}
}

//修改保存按钮
function addEditData() {
	var ids = $("#print_guanLian_jqgrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowDatas = $("#print_guanLian_jqgrids").jqGrid('getRowData', ids);
	var idx = rowDatas.idx
	var platformId = $("#" + ids + "_platformName option:selected").val();
	console.log(platformId)
	var warehouseId = $("#" + ids + "_warehouseId").val()
	var templateId = $("#" + ids + "_templateId").val()
	
	var warehouseName = rowDatas.warehouseName
	var platformName = $("#" + ids + "_platformName option:selected").text();
	if(IsCheckValue(warehouseId, platformId, templateId) == true) {
		
		var save = {
			"reqHead": reqhead,
			"reqBody": {
				'idx':idx,
				"warehouseId": warehouseId,
				"platformName": platformName,
				"warehouseName": warehouseName,
				"platformId": platformId,
				"templateId": templateId
			}
		}
		var saveJson = JSON.stringify(save);
		console.log(saveJson)
		$.ajax({
			type: "post",
			url: url3 + "/mis/whs/label/updateLabelTemplateRelation",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(msgAdd) {
				alert(msgAdd.respHead.message)
				$('.saveOrder').addClass("gray");
				$('.saveOrder').attr('disabled', true);
				$('.addOrder').removeClass("gray")
//				$(".updite").removeClass("gray")
				$(".break").removeClass("gray")
				$(".clear").removeClass("gray")
				$(".search").removeClass("gray")
				$(".del").removeClass("gray")
				$('addOrder').attr('disabled', false);
//				$('updite').attr('disabled', false);
				$('break').attr('disabled', false);
				$('clear').attr('disabled', false);
				$(".search").attr('disabled', false);
				$(".del").attr('disabled', false);
				search5()
			},
			error: function(err) {
				console.log("失败")
			}
		});
	}
}

////删除行
$(function() {
	$(".del").click(function() {
		var num = []
		var gr = $("#print_guanLian_jqgrids").jqGrid('getGridParam', 'selarrrow'); //获取行id
		for(var i = 0;i < gr.length;i++) {
			var rowDatas = $("#print_guanLian_jqgrids").jqGrid('getRowData', gr[i]); //获取行数据    	
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
				url: url3 + "/mis/whs/label/deleteLabelTemplateRelation",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(remover) {
					alert(remover.respHead.message);
					search5()
				},
				error: function() {
					console.log("删除失败")

				}
			});

		}
	})
})

