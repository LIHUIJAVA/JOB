var count;
var pages;
var page = 1;
var rowNum;
$(function() {
	$(document).on('click', '.templateId_biaoge', function() {
		window.open("../../Components/ec/Print_mubanList.html", 'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
		
	});
})

//刚开始时可点击的按钮
$(function() {
	$(".saveOrder").addClass("gray") //参照
	$(".gray").attr("disabled", true)
	
	$(".download").removeClass("gray")
	$("#mengban").hide()
	$(".down").hide()
})


$(function() {
	$(document).on('click', '.clear', function() {
		window.location.reload()
	})
	$(document).on('click', '.break', function() {
		window.location.reload()
	})
})



var myData = {};
//页面初始化
$(function() {
	allHeight()
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	//初始化表格
	jQuery("#print_jqgrids").jqGrid({
		height: height,
		autoScroll: true,
		datatype: "local", //请求数据返回的类型。可选json,xml,txt
		shrinkToFit: false,
		colNames: ['序号','模板编码', '标签编码', '标签名称', '类型标签', '条码类型', '字体大小',
			'X坐标', 'Y坐标', 'Z坐标', '条码高度', '条码宽度', '转向角度', '是否固定值', '动态数据字段', '打印的内容', '备注'
		], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'idx',
				align: "center",
				editable: false,
				sortable: false,
				hidden:true
			},
			{
				name: 'templateId',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'labelId',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'labelName',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'labelTypeValue',
				align: "center",
				editable: true,
				sortable: false,
				edittype: 'select',
				formatter: 'select',
				editoptions: {
					value: "0:请选择;1:Barcode;2:Image;3:LineHorizontal;4:LineVertical;5:Text"
				},
			},
			{
				name: 'barcodeTypeValue',
				align: "center",
				editable: true,
				sortable: false,
				edittype: 'select',
				formatter: 'select',
				editoptions: {
					value: "0:请选择;1:一维码;2:二维码"
				},
			},
			{
				name: 'labelSize',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'positionX',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'positionY',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'positionZ',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'barcodeHigh',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'barcodeWide',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'rotation',
				align: "center",
				editable: true,
				sortable: false,
				edittype: 'select',
				formatter: 'select',
				editoptions: {
					value: "3:请选择;0:不旋转;1:180度;2:270度"
				},
			},
			{
				name: 'isfixed',
				align: "center",
				editable: true,
				sortable: false,
				edittype: 'select',
				formatter: 'select',
				editoptions: {
					value: "2:请选择;0:非固定值;1:固定值"
				},
			},
			{
				name: 'printfieldname',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'printcontent',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'remark',
				align: "center",
				editable: true,
				sortable: false,
			}

		],
		rowList: [10, 20, 30], //可供用户选择一页显示多少条			
		autowidth: true,
		sortable:true,
		loadonce: true,
		multiboxonly: true,
		cellsubmit: "clientArray",
		rownumbers: true,
		pager: '#print_jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'storeId', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		ondblClickRow: function(rowid) {
			var gr = $("#print_jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
			if(mType==1){
				mType=1
			}else{
				mType = 2;
//				$('#print_jqgrids').editRow(gr, true);
//				$("#" + gr + "_templateId").attr("readonly", "readonly");
//				$("#" + gr + "_labelId").attr("readonly", "readonly");
				$("#print_jqgrids").setColProp("templateId",{editable:false});
				$("#print_jqgrids").setColProp("labelId",{editable:false});
				$("#print_jqgrids").editRow(gr,true);
			}
			grr(gr)
			$(".saveOrder").removeClass("gray");
			$(".saveOrder").attr('disabled', false)
		},
		multiselect: true, //复选框
		caption: "商品档案", //表格的标题名字	
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#print_jqgrids").getGridParam('records'); //获取返回的记录数
			page = $("#print_jqgrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#print_jqgrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			search6()
		},

	});
	search6()
})

//function IsCheckValue(goodId, storeId, ecGoodId, ecGoodName, onlineStatus, isSecSale) {
//	if(goodId == '') {
//		alert("商品编码不能为空")
//		return false;
//	} else if(storeId == '') {
//		alert("店铺编码不能为空")
//		return false;
//	}  else if(ecGoodId == '') {
//		alert("平台商品编码不能为空")
//		return false;
//	}  else if(ecGoodName == '') {
//		alert("平台商品名称不能为空")
//		return false;
//	}  else if(onlineStatus == 0) {
//		alert("线上状态未选择")
//		return false;
//	}   else if(isSecSale == 2) {
//		alert("是否二销未选择")
//		return false;
//	} 
//	return true;
//}


$(function() {
	//确定
	$(".true").click(function() {

		var ids = $("#print_jqgrids").jqGrid('getGridParam', 'selrow');
	
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
		$("#muban").hide()
		$("#purchaseOrder").show();
	})
	//	取消
	$(".false").click(function() {
		$("#muban").css("opacity", 0);
		$("#muban").hide()
		$("#purchaseOrder").show();
	})

})

var mType = 0;
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

function grr(gr) {
	$("#" + gr + "_templateId").on("dblclick", function() {
		
		$("#inquiry").removeClass("gray")
		$("#inquiry").attr("disabled", false)
		$("#purchaseOrder").hide()
		$("#muban").show()
		$("#muban").css("opacity", 1)
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
		var gr = $("#print_jqgrids").jqGrid('getDataIDs');
		console.log(gr)
		// 获得当前最大行号（数据编码）
		var rowid = Math.max.apply(Math, gr);
		// 获得新添加行的行号（数据编码）
		window.newrowid = rowid + 1;
		console.log(newrowid)
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
		$("#print_jqgrids").setColProp('storeId', {
			editable: true
		}); //设置editable属性由true改为false

		//将新添加的行插入到第一列
		$("#print_jqgrids").jqGrid("addRowData", newrowid, dataRow, "first");
		
		//设置grid单元格可编辑
		$('#print_jqgrids').jqGrid('editRow', newrowid, true);
		$("#print_jqgrids").jqGrid('setSelection',newrowid);
		grr(newrowid)
		$("#" + newrowid + "_templateId").attr("readonly", "readonly");
	})

})
//添加新数据
function addNewData() {
	var labelTypeValue = $("#" + newrowid + "_labelTypeValue option:selected").val();
	var barcodeTypeValue=$("#" + newrowid + "_barcodeTypeValue option:selected").val();
	var rotation=$("#" + newrowid + "_rotation option:selected").val();
	var isfixed=$("#" + newrowid + "_isfixed option:selected").val();
	console.log(labelTypeValue,barcodeTypeValue,rotation,isfixed)
	var templateId = $("#" + newrowid + "_templateId").val()
	var labelId = $("#" + newrowid + "_labelId").val()
	var labelName = $("#" + newrowid + "_labelName").val()
	var labelSize = $("#" + newrowid + "_labelSize").val()
	var positionX = $("#" + newrowid + "_positionX").val()
	var positionY = $("#" + newrowid + "_positionY").val()
	var positionZ = $("#" + newrowid + "_positionZ").val()
	var barcodeHigh = $("#" + newrowid + "_barcodeHigh").val()
	var barcodeWide = $("#" + newrowid + "_barcodeWide").val()
	var printfieldname = $("#" + newrowid + "_printfieldname").val()
	var printcontent = $("#" + newrowid + "_printcontent").val()
	var remark = $("#" + newrowid + "_remark").val()
//	if(IsCheckValue(goodId, storeId, ecGoodId, ecGoodName, onlineStatus, isSecSale) == true) {
		
		var save = {
			"reqHead": reqhead,
			"reqBody": {
				"labelTypeValue": labelTypeValue,
				"barcodeTypeValue": barcodeTypeValue,
				"rotation": rotation,
				"isfixed": isfixed,
				"templateId": templateId,
				"labelId": labelId,
				"labelName": labelName,
				"labelSize": labelSize,
				"positionX": positionX,
				"positionY": positionY,
				"positionZ": positionZ,
				"barcodeHigh": barcodeHigh,
				"barcodeWide": barcodeWide,
				"printfieldname": printfieldname,
				"printcontent": printcontent,
				"remark": remark
			}
		}
		var saveJson = JSON.stringify(save);
		console.log(saveJson)
		$.ajax({
			type: "post",
			url: url3 + "/mis/whs/label/insertLabelSteup",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(msgAdd) {
				alert(msgAdd.respHead.message)
				mType = 2
//				$("#searchAll").trigger('click');
//				$('#print_jqgrids').css("visibility", "true");
//	
				$('.saveOrder').addClass("gray");
				$('.saveOrder').attr('disabled', true);
				$('.addOrder').removeClass("gray")
//				$(".updite").removeClass("gray")
				$(".break").removeClass("gray")
				$(".clear").removeClass("gray")
				$(".chaxun").removeClass("gray")
				$(".del").removeClass("gray")
				$('.addOrder').attr('disabled', false);
//				$('.updite').attr('disabled', false);
				$('.break').attr('disabled', false);
				$('.clear').attr('disabled', false);
				$(".chaxun").attr('disabled', false);
				$(".del").attr('disabled', false);
//				
//				$(".del").removeClass("gray") //查询
//				$("#finds").removeClass("gray") //查询
//				$(".del").attr('disabled', false);
//				$("#finds").attr('disabled', false);
//				
//				$(".print").removeClass("gray") //查询
//				$(".download").removeClass("gray") //查询
//				$(".print").attr('disabled', false);
//				$(".download").attr('disabled', false);
				search6()
			},
			error: function(err) {
				console.log("失败")
			}
		});
//	}
}

//修改保存按钮
function addEditData() {
	var ids = $("#print_jqgrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowDatas = $("#print_jqgrids").jqGrid('getRowData', ids);
	var idx = rowDatas.idx
	console.log(idx)
	var labelTypeValue = $("#" + ids + "_labelTypeValue option:selected").val();
	var barcodeTypeValue=$("#" + ids + "_barcodeTypeValue option:selected").val();
	var rotation=$("#" + ids + "_rotation option:selected").val();
	var isfixed=$("#" + ids + "_isfixed option:selected").val();
	var templateId = rowDatas.templateId
	var labelId = rowDatas.labelId
	var labelName = $("#" + ids + "_labelName").val()
	var labelSize = $("#" + ids + "_labelSize").val()
	var positionX = $("#" + ids + "_positionX").val()
	var positionY = $("#" + ids + "_positionY").val()
	var positionZ = $("#" + ids + "_positionZ").val()
	var barcodeHigh = $("#" + ids + "_barcodeHigh").val()
	var barcodeWide = $("#" + ids + "_barcodeWide").val()
	var printfieldname = $("#" + ids + "_printfieldname").val()
	var printcontent = $("#" + ids + "_printcontent").val()
	var remark = $("#" + ids + "_remark").val()
//	if(IsCheckValue(goodId, storeId, ecGoodId, ecGoodName, onlineStatus, isSecSale) == true) {
		
		var save = {
			"reqHead": reqhead,
			"reqBody": {
				'idx':idx,
				"labelTypeValue": labelTypeValue,
				"barcodeTypeValue": barcodeTypeValue,
				"rotation": rotation,
				"isfixed": isfixed,
				"templateId": templateId,
				"labelId": labelId,
				"labelName": labelName,
				"labelSize": labelSize,
				"positionX": positionX,
				"positionY": positionY,
				"positionZ": positionZ,
				"barcodeHigh": barcodeHigh,
				"barcodeWide": barcodeWide,
				"printfieldname": printfieldname,
				"printcontent": printcontent,
				"remark": remark
			}
		}
		var saveJson = JSON.stringify(save);
		console.log(saveJson)
		$.ajax({
			type: "post",
			url: url3 + "/mis/whs/label/updateLabelSteup",
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
				$(".chaxun").removeClass("gray")
				$(".del").removeClass("gray")
				$('addOrder').attr('disabled', false);
//				$('updite').attr('disabled', false);
				$('break').attr('disabled', false);
				$('clear').attr('disabled', false);
				$(".chaxun").attr('disabled', false);
				$(".del").attr('disabled', false);
				search6()
			},
			error: function(err) {
				console.log("失败")
			}
		});
//	}
}
//
//查询按钮
$(document).on('click', '#chaxun', function() {
	search6()
})

//条件查询
function search6() {
	var idx = $("input[name='idx']").val();
	var templateId = $("input[name='templateId1']").val();
	
	var labelId = $("input[name='labelId']").val();
	var labelTypeValue = $("select[name='labelTypeValue']").val();
	var barcodeTypeValue = $("select[name='barcodeTypeValue']").val();
	var isFixed = $("select[name='isFixed']").val();
	var printcontent = $("input[name='printcontent']").val();
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var data2 = {
		reqHead,
		"reqBody": {
			"idx": idx,
			"templateId": templateId,
			"labelId": labelId,
			"labelTypeValue": labelTypeValue,
			"barcodeTypeValue": barcodeTypeValue,
			"isFixed": isFixed,
			"printcontent": printcontent,
			"pageNo": page,
			"pageSize": rowNum
		}

	};
	var postD2 = JSON.stringify(data2);
	console.log(postD2)
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url3 + "/mis/whs/label/queryListLabelSteup",
		data: postD2,
		dataType: 'json',
		async: true,
		//开始加载动画  添加到ajax里面
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");

		},
		success: function(data) {
			console.log(data)
			var mydata = {};
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#print_jqgrids").jqGrid("clearGridData");
			$("#print_jqgrids").jqGrid("setGridParam", {
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

////删除行
$(function() {
	$(".del").click(function() {
		var num = []
		var gr = $("#print_jqgrids").jqGrid('getGridParam', 'selarrrow'); //获取行id
		for(var i = 0;i < gr.length;i++) {
			var rowDatas = $("#print_jqgrids").jqGrid('getRowData', gr[i]); //获取行数据    	
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
				url: url3 + "/mis/whs/label/deleteLabelSteup",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(remover) {
					alert(remover.respHead.message);
					search6()
				},
				error: function() {
					console.log("删除失败")

				}
			});

		}
	})
})
