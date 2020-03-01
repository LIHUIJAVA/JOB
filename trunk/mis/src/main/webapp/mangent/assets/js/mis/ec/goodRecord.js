var count;
var pages;
var page = 1;
var rowNum;
var cli = 0;
$(function() {
	//点击表格图标显示店铺列表
	$(document).on('click', '.storeId1_biaoge', function() {
		window.open("../../Components/baseDoc/storeRecordList.html", 'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	$(document).on('click', '.storeId_biaoge', function() {
		if($("select[name='ecId']").val() != null) {
			window.open("../../Components/baseDoc/storeRecordList.html", 'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
		} else {
			alert("请选择电商平台")
		}
	});
	$(".trues").attr("disabled", false)
	$(".falses").attr("disabled", false)
})

//刚开始时可点击的按钮
$(function() {
	$(".saveOrder").addClass("gray") //参照
	$(".gray").attr("disabled", true)
	
	$(".download").removeClass("gray")
	$("#mengban").hide()
	$(".down").hide()
})
var myData = {};
//页面初始化
$(function() {
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
			//初始化表格
	allHeight()
	jQuery("#good_jqgrids").jqGrid({
		height: height,
		autoScroll: true,
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		url: '../../assets/js/json/order.json',
		shrinkToFit: false,
		colNames: ['商品id', '商品编码', '店铺编码', '店铺名称', '电商平台编码', '电商平台名称', '商品名称', '商品sku', '平台商品编码',
			'平台商品名称', '规格型号', '最低售价', '安全库存', 'sku属性', '线上状态', '是否二销', '备注'
		], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'id',
				align: "center",
				editable: true,
				sortable: false,
				hidden: true
			},
			{
				name: 'goodId',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'storeId',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'storeName',
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: 'ecId',
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: 'ecName',
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: 'goodName',
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: 'goodSku',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'ecGoodId',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'ecGoodName',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'goodMode',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'upsetPrice',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'safeInv',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'skuProp',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'onlineStatus',
				align: "center",
				editable: true,
				sortable: false,
				edittype: 'select',
				formatter: 'select',
				editoptions: {
					value: "0:请选择; 1:在售; 2:下架; 3:待售"
				},
			},
			{
				name: 'isSecSale',
				align: "center",
				editable: true,
				sortable: false,
				edittype: 'select',
				formatter: 'select',
				editoptions: {
					value: "2:请选择; 0:否; 1:是"
				},
			},
			{
				name: 'memo',
				align: "center",
				editable: true,
				sortable: false,
			}

		],
		rowNum: 500, //一页显示多少条
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条			
		autowidth: true,
		loadonce: true,
		multiboxonly: true,
		cellsubmit: "clientArray",
		rownumbers: true,
		pager: '#good_jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'storeId', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#good_jqgrids").getGridParam('records'); //获取返回的记录数
			page = $("#good_jqgrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#good_jqgrids").getGridParam('rowNum');

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
			search4();
		},
		ondblClickRow: function(rowid) {
			if(mType==1){
				mType=1
			}else{
				mType = 2;
			}
			var gr = $("#good_jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
//					var rowDatas = $("#jqgrids").jqGrid('getRowData', gr); //获取行数据
			$('#good_jqgrids').editRow(gr, true);
			grr(gr)
			$('button').addClass("gray");
			$(".saveOrder").removeClass("gray");
			$(".save").removeClass("gray");
			$(".cancel").removeClass("gray");
			$(".find").removeClass("gray");
			$(".findList").removeClass("gray");
			$(".true").removeClass("gray");
			$(".false").removeClass("gray");
			$(".upOrder").removeClass("gray");
			$(".sure1").removeClass("gray");
			$(".false1").removeClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true)
			$("#mengban").hide();
			$(".sure1").attr('disabled', false)
			$(".false1").attr('disabled', false)
			$(".addOrder").attr('disabled', true)
			$('.addOrder').addClass("gray");
		},
		multiselect: true, //复选框
		caption: "商品档案", //表格的标题名字	

	});
	search4()

})


//function getJQAllData() {
//	//拿到grid对象
//	var obj = $("#good_jqgrids");
//	//获取grid表中所有的rowid值
//	var rowIds = obj.getDataIDs();
//	//初始化一个数组arrayData容器，用来存放rowData
//	var arrayData = new Array();
//	if(rowIds.length > 0) {
//		for(var i = 0; i < rowIds.length; i++) {
//			arrayData.push(obj.getRowData(rowIds[i]));
//		}
//	}
//	return arrayData;
//}


function IsCheckValue(goodId, storeId, ecGoodId, ecGoodName, onlineStatus, isSecSale) {
	if(goodId == '') {
		alert("商品编码不能为空")
		return false;
	} else if(storeId == '') {
		alert("店铺编码不能为空")
		return false;
	}  else if(ecGoodId == '') {
		alert("平台商品编码不能为空")
		return false;
	}  else if(ecGoodName == '') {
		alert("平台商品名称不能为空")
		return false;
	}  else if(onlineStatus == 0) {
		alert("线上状态未选择")
		return false;
	} else if(isSecSale == 2) {
		alert("是否二销未选择")
		return false;
	}
	return true;
}

//打开部门档案后点击确定取消
$(function() {
	//确定
	$(".save").click(function() {

		var ids = $("#good_jqgrids").jqGrid('getGridParam', 'selrow');
		//	部门
		//	获得行号
		var gr = $("#store_jqGrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDatas = $("#store_jqGrids").jqGrid('getRowData', gr);
		if(mType == 1) {
			$("#" + newrowid + "_storeId").val(rowDatas.storeId)
			$("#" + newrowid + "_ecId").val(rowDatas.ecId)
			
		} else if(mType == 2) {
			$("#" + ids + "_storeId").val(rowDatas.storeId)
			$("#" + ids + "_ecId").val(rowDatas.ecId)
			
		}

		$(".list_box").css("opacity", 0);
		$("#purchaseOrder").show();
	})
	//	取消
	$(".cancel").click(function() {
		//		$("#whsDocList").css("opacity", 0);
		//		$("#insertList").css("opacity", 0);
		$(".list_box").css("opacity", 0);
		$("#purchaseOrder").show();
	})

	//	存货
	//确定
	$(".true").click(function() {

		var ids = $("#good_jqgrids").jqGrid('getGridParam', 'selrow');
		//	用户
		//	获得行号
		var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowData = $("#jqgrids").jqGrid('getRowData', gr);
		if(mType == 1) {
			$("#" + newrowid + "_goodId").val(rowData.invtyEncd)
//			$("#" + newrowid + "_goodName").val(rowData.invtyNm)
			$("#good_jqgrids").setRowData(newrowid, {
				goodName: rowData.invtyNm
			})
			
		}else if(mType == 2) {
			$("#" + ids + "_goodId").val(rowData.invtyEncd)
//			$("#" + ids + "_goodName").val(rowData.invtyNm)
			$("#good_jqgrids").setRowData(ids, {
				goodName: rowData.invtyNm
			})
		}
		$(".list_box").css("opacity", 0);
		$("#purchaseOrder").show();
		$("#wwrap").hide();
	})
	//	取消
	$(".false").click(function() {
		$("#purchaseOrder").show()
		$(".list_box").show()
		$("#wwrap").hide()
		$("#wwrap").css("opacity", 0)
		//调拨单
		//获得行号
		var rowid = $("#good_jqgrids").jqGrid('getGridParam', 'selrow');

		$("#" + rowid + "_batNum").val("")
	})
	
	//确定
	$(".sure1").click(function() {
		//	店铺档案
		var ids = $("#good_jqgrids").jqGrid('getGridParam', 'selrow');
		//	获得行号
		var gr = $("#List_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDatas = $("#List_jqgrids").jqGrid('getRowData', gr);
//		if(cli == 1) {
//			$("input[name='storeId2']").val(rowDatas.storeId)
//			$("input[name='storeNm2']").val(rowDatas.storeName)
//		} else 
		if(cli == 2) {
			if(mType == 1) {
				$("#" + newrowid + "_storeId").val(rowDatas.storeId)
				$("#good_jqgrids").setRowData(newrowid, {
					ecId: rowDatas.ecId,
					ecName: rowDatas.ecName,
					storeName: rowDatas.storeName
				})
				
			}else if(mType == 2) {
				$("#" + ids + "_storeId").val(rowDatas.storeId)
				$("#good_jqgrids").setRowData(ids, {
					ecId: rowDatas.ecId,
					ecName: rowDatas.ecName,
					storeName: rowDatas.storeName
				})
			}
			
		}
		$(".formSave_box").css("opacity", 0);
		$(".list_box").hide();
		$(".formSave_box").hide();
		$("#purchaseOrder").show();
		$("#wwrap").hide();
	})
//	取消
	$(".false1").click(function() {
		$(".formSave_box").css("opacity", 0);
		$(".list_box").hide();
		$(".formSave_box").hide();
		$("#purchaseOrder").show();
		$("#wwrap").hide();
		$("input[name='storeId']").val('')
		var gr = $("#List_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDatas = $("#List_jqgrids").jqGrid('getRowData', gr);
//		if(cli == 1) {
//			$("input[name='storeId2']").val('')
//		} else 
		if(cli == 2) {
			$("#" + newrowid + "_storeId").val('')
		}
	})
})

var mType = 0;
var isclick = true;
$(function() {
	$(".saveOrder").click(function() {
		if(isclick) {
			isclick = false;
			if(mType == 1) {
				$(".addOrder").css("background-color", 'black')
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

function grr(gr) {
	$("#" + gr + "_goodId").on("dblclick", function() {
		
		$("#purchaseOrder").hide()
		$(".formSave_box").hide()
		$(".list_box").hide()
		$("#wwrap").show()
		$("#wwrap").css("opacity", 1)
	})
	$("#" + gr + "_storeId").on("dblclick", function() {
		cli = 2
		$(".sure1").removeClass("gray") //查询
		$(".sure1").attr("disabled",false) //查询
		$(".false1").removeClass("gray") //查询
		$(".false1").attr("disabled",false) //查询
		$("#wwrap").hide()
		$("#purchaseOrder").hide()
		$(".list_box").show()
		$(".list_box").css("opacity", 1)
	})
}


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
		//拿到Gride中所有主键id的值
		var gr = $("#good_jqgrids").jqGrid('getDataIDs');

		// 获得当前最大行号（数据编码）
		var rowid = Math.max.apply(Math, gr);
		// 获得新添加行的行号（数据编码）
		window.newrowid = rowid + 1;
		var dataRow = {
			"goodId": "",
			"storeId": "",
			"ecId": '',
			"goodName": '',
			"goodSku": '',
			"ecGoodId": '',
			"ecGoodName": '',
			"goodMode": '',
			"upsetPrice": '',
			"safeInv": '',
			"skuProp": '',
			"onlineStatus": '',
			"isSecSale": '',
			"memo": '',

		};
		$("#good_jqgrids").setColProp('storeId', {
			editable: true
		}); //设置editable属性由true改为false

		//将新添加的行插入到第一列
		$("#good_jqgrids").jqGrid("addRowData", newrowid, dataRow, "first");
		
		//设置grid单元格可编辑
		$('#good_jqgrids').jqGrid('editRow', newrowid, true);
		$("#good_jqgrids").jqGrid('setSelection',newrowid);
		$("#" + newrowid + "_storeId").attr("readonly", "readonly");
		$("#" + newrowid + "_goodId").attr("readonly", "readonly");
		
		$("#" + newrowid + "_upsetPrice").attr("type","number")
		$("#" + newrowid + "_safeInv").attr("type","number")
		grr(newrowid)
	})

})

//添加新数据
function addNewData() {
	//获得行号
	var gr = $("#good_jqgrids").jqGrid('getGridParam', 'selrow');
	
	//获得行数据
	var rowDatas = $("#good_jqgrids").jqGrid('getRowData', gr);
	var ecId = rowDatas.ecId;
	var goodName = rowDatas.goodName;
	var id = $("#" + newrowid + '_id').val();
	var goodId = $("#" + newrowid + '_goodId').val();
	var storeId = $("#" + newrowid + '_storeId').val();
	var goodSku = $("#" + newrowid + '_goodSku').val();
	var ecGoodId = $("#" + newrowid + '_ecGoodId').val();
	var ecGoodName = $("#" + newrowid + '_ecGoodName').val();
	var goodMode = $("#" + newrowid + '_goodMode').val();
	var upsetPrice = $("#" + newrowid + '_upsetPrice').val();
	var safeInv = $("#" + newrowid + '_safeInv').val();
	var skuProp = $("#" + newrowid + '_skuProp').val();
	var onlineStatus = $("#" + newrowid + '_onlineStatus').val();
	var isSecSale = $("#" + newrowid + '_isSecSale').val();
	var memo = $("#" + newrowid + '_memo').val();
	if(IsCheckValue(goodId, storeId, ecGoodId, ecGoodName, onlineStatus, isSecSale) == true) {
		
		var save = {
			"reqHead": reqhead,
			"reqBody": {
				"id":id,
				"goodId": goodId,
				"storeId": storeId,
				"ecId": ecId,
				"goodName": goodName,
				"goodSku": goodSku,
				"ecGoodId": ecGoodId,
				"ecGoodName": ecGoodName,
				"goodMode": goodMode,
				"upsetPrice": upsetPrice,
				"safeInv": safeInv,
				"skuProp": skuProp,
				"onlineStatus": onlineStatus,
				"isSecSale": isSecSale,
				"memo": memo
			}
		}
		var saveJson = JSON.stringify(save);
		$.ajax({
			type: "post",
			url: url3 + "/mis/ec/goodRecord/add",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(msgAdd) {
				alert(msgAdd.respHead.message)
				$("#searchAll").trigger('click');
				$('#good_jqgrids').css("visibility", "true");
	
				$('button').addClass("gray");
				$('.addOrder').removeClass("gray") //增加
				$(".addWhs").removeClass("gray") //确定
				$(".cancel").removeClass("gray") //取消
				$(".find").removeClass("gray") //查询
				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true);
				$(".importExcel").removeClass("gray") //查询
				$(".exportExcel").removeClass("gray") //查询
				$(".importExcel").attr('disabled', false);
				$(".exportExcel").attr('disabled', false);
				
				$(".del").removeClass("gray") //查询
				$("#finds").removeClass("gray") //查询
				$(".del").attr('disabled', false);
				$("#finds").attr('disabled', false);
				
				$(".print").removeClass("gray") //查询
				$(".download").removeClass("gray") //查询
				$(".print").attr('disabled', false);
				$(".download").attr('disabled', false);
				search4()
			},
			error: function(err) {
				console.log("失败")
			}
		});
	}
}

//修改保存按钮
function addEditData() {
	var ids = $("#good_jqgrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowDatas = $("#good_jqgrids").jqGrid('getRowData', ids);
	var ecId = rowDatas.ecId;
	var goodName = rowDatas.goodName;
	var id = $("#" + ids + '_id').val();
	var goodId = $("#" + ids + '_goodId').val();
	var storeId = $("#" + ids + '_storeId').val();
	var goodSku = $("#" + ids + '_goodSku').val();
	var ecGoodId = $("#" + ids + '_ecGoodId').val();
	var ecGoodName = $("#" + ids + '_ecGoodName').val();
	var goodMode = $("#" + ids + '_goodMode').val();
	var upsetPrice = $("#" + ids + '_upsetPrice').val();
	var safeInv = $("#" + ids + '_safeInv').val();
	var skuProp = $("#" + ids + '_skuProp').val();
	var onlineStatus = $("#" + ids + '_onlineStatus').val();
	var isSecSale = $("#" + ids + '_isSecSale').val();
	var memo = $("#" + ids + '_memo').val();
	if(IsCheckValue(goodId, storeId, ecGoodId, ecGoodName, onlineStatus, isSecSale) == true) {
		
		var save = {
			"reqHead": reqhead,
			"reqBody": {
				"id":id,
				"goodId": goodId,
				"storeId": storeId,
				"ecId": ecId,
				"goodName": goodName,
				"goodSku": goodSku,
				"ecGoodId": ecGoodId,
				"ecGoodName": ecGoodName,
				"goodMode": goodMode,
				"upsetPrice": upsetPrice,
				"safeInv": safeInv,
				"skuProp": skuProp,
				"onlineStatus": onlineStatus,
				"isSecSale": isSecSale,
				"memo": memo
			}
		}
		var saveJson = JSON.stringify(save);
		$.ajax({
			type: "post",
			url: url3 + "/mis/ec/goodRecord/edit",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(msgAdd) {
				alert(msgAdd.respHead.message)
				$("#searchAll").trigger('click');
				$('#good_jqgrids').css("visibility", "true");
	
				$('button').addClass("gray");
				$('.addOrder').removeClass("gray") //增加
				$(".addWhs").removeClass("gray") //确定
				$(".cancel").removeClass("gray") //取消
				$(".find").removeClass("gray") //查询
	
				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true);
				$(".importExcel").removeClass("gray") //查询
				$(".exportExcel").removeClass("gray") //查询
				$(".importExcel").attr('disabled', false);
				$(".exportExcel").attr('disabled', false);
				
				$(".del").removeClass("gray") //查询
				$("#finds").removeClass("gray") //查询
				$(".del").attr('disabled', false);
				$("#finds").attr('disabled', false);
				
				$(".print").removeClass("gray") //查询
				$(".download").removeClass("gray") //查询
				$(".print").attr('disabled', false);
				$(".download").attr('disabled', false);
	
				search4()
			},
			error: function(err) {
				console.log("失败")
			}
		});
	}
}

//查询按钮
$(document).on('click', '#finds', function() {
	search4()
})

//条件查询
function search4() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var goodId = $("input[name='goodId']").val();
	var storeId = $("input[name='storeId2']").val();
	
	var goodSku = $("input[name='goodSku']").val();
	var goodName = $("input[name='goodName']").val();
	var ecGoodId = $("input[name='ecGoodId']").val();
	var ecGoodName = $("input[name='ecGoodName']").val();
	var data2 = {
		reqHead,
		"reqBody": {
			"storeId": storeId,
			"goodId": goodId,
			"goodSku": goodSku,
			"goodName": goodName,
			"ecGoodId": ecGoodId,
			"ecGoodName": ecGoodName,
			"pageNo": page,
			"pageSize": rowNum
		}

	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url3 + "/mis/ec/goodRecord/queryList",
		data: postD2,
		dataType: 'json',
		async: true,
		//开始加载动画  添加到ajax里面
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");

		},
		success: function(data) {
			var mydata = {};
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#good_jqgrids").jqGrid("clearGridData");
			$("#good_jqgrids").jqGrid("setGridParam", {
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

//删除行
$(function() {
	$(".del").click(function() {
		var grs = $("#good_jqgrids").jqGrid('getGridParam', 'selarrrow'); //获取行id
		var gr = []
		for(var i =0;i<grs.length;i++) {
			var rowDatas = $("#good_jqgrids").jqGrid('getRowData', grs[i]); //获取行数据    	
			gr.push(rowDatas.id)
		}
		var num = gr.toString()
		var deleteAjax = {
			reqHead,
			"reqBody": {
				"id": num,
			}
		};
		var deleteData = JSON.stringify(deleteAjax)
		if(grs == null) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url3 + "/mis/ec/goodRecord/delete",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(remover) {
					alert(remover.respHead.message);
					search4()
					$("#searchAll").trigger('click')
					$('#good_jqgrids').css("visibility", "true");
				},
				error: function() {
					console.log("删除失败")

				}
			});

		}
	})
})



$(function() {
	$('.download').click(function() {
		$("input[name='storeId2']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='storeId1']").attr('id', 'storeId'); // 给弹框外重复的去除id
		$("input[name='storeNm2']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='storeNm1']").attr('id', 'storeName'); // 给弹框外重复的去除id
		$(".trues").removeClass("gray") //查询
		$(".falses").removeClass("gray") //查询
		$(".down").css("opacity", 1)
		$(".down").show()
		$("#mengban").show()
//		$(".zhehzao").hide()
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

	$(".trues").click(function() {
		var storeId = $("input[name='storeId1']").val()
		if(storeId != '') {
			var storeId1 = $("input[name='storeId1']").val()

			var download = {
				"reqHead": reqhead,
				"reqBody": {
					'storeId': storeId1
				}
			};
			var downloadData = JSON.stringify(download)
			$.ajax({
				type: "post",
				url: url3 + "/mis/ec/goodRecord/download",
				async: true,
				data: downloadData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(msgAdd) {
					alert(msgAdd.respHead.message)
					$("#mengban").hide()
					$(".down").css("opacity", 0)
					$(".down").hide()
					$("input[name='storeId1']").val('')
					search4()
					$("input[name='storeId2']").attr('id', 'storeId'); // 给弹框中重复的加id
					$("input[name='storeId1']").attr('id', ''); // 给弹框外重复的去除id
					$("input[name='storeNm2']").attr('id', 'storeName'); // 给弹框中重复的加id
					$("input[name='storeNm1']").attr('id', ''); // 给弹框外重复的去除id
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
		$("input[name='storeId1']").val('')
		$("input[name='storeNm1']").val('')
		$("input[name='storeId2']").attr('id', 'storeId'); // 给弹框中重复的加id
		$("input[name='storeId1']").attr('id', ''); // 给弹框外重复的去除id
		$("input[name='storeNm2']").attr('id', 'storeName'); // 给弹框中重复的加id
		$("input[name='storeNm1']").attr('id', ''); // 给弹框外重复的去除id

	})
})



//导入
$(function() {
	$(".importExcel").click(function() {
    	var loginName = localStorage.loginAccNum
		var files = $("#FileUpload").val()
		var fileObj = document.getElementById("FileUpload").files[0];
		var formFile = new FormData();
		formFile.append("action", "UploadVMKImagePath");
       	formFile.append("accNum", loginName);  
		formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
		var data = formFile;
		if(files != "") {
			$.ajax({
				type: 'post',
				url: url + "/mis/ec/goodRecord/uploadGoodRecord",
				data: data,
				dataType: "json",
				cache: false, //上传文件无需缓存
				processData: false, //用于对data参数进行序列化处理 这里必须false
				contentType: false, //必须
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
			});
		} else {
			alert("请选择文件")
		}
	});
});


var arr=[];
var obj={}
//导出
$(document).on('click', '.exportExcel', function() {
	var goodId = $("input[name='goodId']").val();
	var storeId = $("input[name='storeId2']").val();
	
	var goodSku = $("input[name='goodSku']").val();
	var goodName = $("input[name='goodName']").val();
	var ecGoodId = $("input[name='ecGoodId']").val();
	var ecGoodName = $("input[name='ecGoodName']").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"storeId": storeId,
			"goodId": goodId,
			"goodSku": goodSku,
			"goodName": goodName,
			"ecGoodId": ecGoodId,
			"ecGoodName": ecGoodName,
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url3 + "/mis/ec/goodRecord/exportList",
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
			var list = data.respBody.list;
			var execlName = '商品档案'
			ExportData(list, execlName)
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
	
})