var mType = 0

$(function() {
	pageInit1();
	pageInit2();
	pageInit3();
//	$("#t_jqgrids").setColProp("buyerNote",{editable:false});
//	$("#m_jqgrids").setColProp("sellerNote",{editable:false});
//	$("#b_jqgrids").setColProp("includeSku",{editable:false});
	//	autoWidthJqgrid();
	$("table").delegate(".jqgrow", "mouseover", function() {
		$(this).css("background-color", "rgb(252, 248, 227)");
	});
	$("table").delegate(".jqgrow", "mouseout", function() {
		$(this).css("background-color", "");
	});
});
//刚开始时可点击的按钮
$(function() {
	$('button').addClass("gray");
	//	$(".refer").removeClass("gray") //参照
	$('.addOrder').removeClass("gray") //增加
	$('button').attr('disabled', false);
	$(".gray").attr('disabled', true)
})
//放弃按钮点击前提示请勿删除!!!!!
$(function(){
	$(".upOrder").click(function(){
		if(confirm("确定重置?")){
			window.location.reload()
		}
	})
})
$(function() {

	$(".inlineOrder").on("click", function() {
//		var selectedId = $("#t_jqgrids").jqGrid("getGridParam", "selrow");
//		var ids = jQuery("#t_jqgrids").jqGrid('getDataIDs');
//		
//		$("#" + ids + "_buyerNote").attr("maxlength", '2')
//		$("#t_jqgrids").addRowData(ids + 1, {}, 'first');
		
		
		
		//拿到Gride中所有主键id的值
		var gr = $("#t_jqgrids").jqGrid('getDataIDs');
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
		$("#t_jqgrids").setColProp('storeId', {
			editable: true
		}); //设置editable属性由true改为false

		//将新添加的行插入到第一列
		$("#t_jqgrids").jqGrid("addRowData", newrowid, dataRow, "last");
		
		//设置grid单元格可编辑
//		$('#print_jqgrids').jqGrid('editRow', newrowid, true);
//		$("#t_jqgrids").jqGrid('setSelection',newrowid);
//		grr(newrowid)
//		$("#" + newrowid + "_templateId").attr("readonly", "readonly");
		
		
		
	})
	$(".inlineOrder1").on("click", function() {
		//		$("#t_jqgrids").addRowData('1', 'first');
		var ids = jQuery("#m_jqgrids").jqGrid('getDataIDs');
//		$("#" + ids + "_sellerNote").attr("maxlength", '2')
		$("#m_jqgrids").addRowData(ids + 1, {}, 'last');
		//		$("#b_jqgrids").addRowData('1', 'first');
	})
	$(".inlineOrder2").on("click", function() {
		//		$("#t_jqgrids").addRowData('1', 'first');
		//		$("#m_jqgrids").addRowData('1', 'first');
		var ids = jQuery("#b_jqgrids").jqGrid('getDataIDs');
//		$("#" + ids + "_includeSku").attr("maxlength", '2')
		$("#b_jqgrids").addRowData(ids + 1, {}, 'last');
	})
})

$(function() {
	$(".delOrder").click(function() {
		//	获得第一个行号
		var gr = $("#t_jqgrids").jqGrid('getGridParam', 'selarrrow');
		var len = gr.length;
		for(var i =0;i<len;i++) {
			if(gr.length == 0) {
				alert("请选择要删除的行");
				return;
			} else {
				$("#t_jqgrids").jqGrid("delRowData", gr[0]);
			}
		}
		//获得第一个行数据
		//		var rowDatas = $("#t_jqgrids").jqGrid('getRowData', gr);
		
	})

	$(".delOrder1").click(function() {
		//	获得第一个行号
		var gr = $("#m_jqgrids").jqGrid('getGridParam', 'selarrrow');
		var len = gr.length;
		for(var i =0;i<len;i++) {
			if(gr.length == 0) {
				alert("请选择要删除的行");
				return;
			} else {
				$("#m_jqgrids").jqGrid("delRowData", gr[0]);
			}
		}
	})

	$(".delOrder2").click(function() {
		//	获得第一个行号
		var gr = $("#b_jqgrids").jqGrid('getGridParam', 'selarrrow');
		var len = gr.length;
		for(var i =0;i<len;i++) {
			if(gr.length == 0) {
				alert("请选择要删除的行");
				return;
			} else {
				$("#b_jqgrids").jqGrid("delRowData", gr[0]);
			}
		}
	})
})


//限制
$(function() {
	//	点击复选框的限制
	$('#buyerNoteNull').click(function() {
		if($(this).prop("checked")) {
			$(this).val(1);
			$("#t_jqgrids").setColProp("buyerNote",{editable:false});
//			$("#t_jqgrids").setColProp("buyerNote",{editable:false});
			$("#buyerNote").attr("disabled", "true");
			$("#buyerNote").next().css("color", "#CCCCCC");
			$(".inlineOrder").attr('disabled', 'true');
		} else {
			$(this).val(0);
			$("#buyerNote").removeAttr("disabled");
			$("#buyerNote").next().css("color", "");
			$(".inlineOrder").attr('disabled', 'false');
		}
	});
	$('#sellerNoteNull').click(function() {
		if($(this).prop("checked")) {
			$("#m_jqgrids").setColProp("sellerNote",{editable:false});
			$(this).val(1);
//			$("#m_jqgrids").setColProp("sellerNote",{editable:false});
			$("#sellerNote").attr("disabled", "true");
			$("#sellerNote").next().css("color", "#CCCCCC");
			$(".inlineOrder1").attr('disabled', 'true');
		} else {
			$(this).val(0);
			$("#sellerNote").removeAttr("disabled");
			$("#sellerNote").next().css("color", "");
			$(".inlineOrder1").attr('disabled', 'false');
		}
	});

	$('#buyerNote').click(function() {
		if($(this).prop("checked")) {
			$("#t_jqgrids").setColProp("buyerNote",{editable:true});
			$(this).val(1);
			$("#vaType").val(1)
//			$("#t_jqgrids").setColProp("buyerNote",{editable:true});
			$("#buyerNoteNull").attr("disabled", "true");
			$("#buyerNoteNull").next().css("color", "#CCCCCC");

			$(".inlineOrder").removeClass("gray");
			$('.inlineOrder').attr('disabled', false);
			$(".delOrder").removeClass("gray");
			$('.delOrder').attr('disabled', false);
		} else {
			$(this).val(0);
			$("#vaType").val('')
			$("#buyerNoteNull").removeAttr("disabled");
			$("#buyerNoteNull").next().css("color", "");

			$(".inlineOrder").addClass("gray");
			$('.inlineOrder').attr('disabled', true);
			$(".delOrder").addClass("gray");
			$('.delOrder').attr('disabled', true);
		}
	});

	$('#sellerNote').click(function() {
		if($(this).prop("checked")) {
			$(this).val(1);
			$("#vaType").val(0)
			$("#m_jqgrids").setColProp("sellerNote",{editable:true});
			$("#sellerNoteNull").attr("disabled", "true");
			$("#sellerNoteNull").next().css("color", "#CCCCCC");

			$(".inlineOrder1").removeClass("gray");
			$('.inlineOrder1').attr('disabled', false);
			$(".delOrder1").removeClass("gray");
			$('.delOrder1').attr('disabled', false);
		} else {
			$(this).val(0);

			$("#vaType").val('')
			$("#sellerNoteNull").removeAttr("disabled");
			$("#sellerNoteNull").next().css("color", "");

			$(".inlineOrder1").addClass("gray");
			$('.inlineOrder1').attr('disabled', true);
			$(".delOrder1").addClass("gray");
			$('.delOrder1').attr('disabled', true);
		}
	});

	$('#includeSku').click(function() {
		if($(this).prop("checked")) {
			$(this).val(1);
			$("#vaType").val(2)
			$("#b_jqgrids").setColProp("includeSku",{editable:true});
			$(".inlineOrder2").removeClass("gray");
			$('.inlineOrder2').attr('disabled', false);
			$(".delOrder2").removeClass("gray");
			$('.delOrder2').attr('disabled', false);
		} else {
			$(this).val(0);
			$("#vaType").val('')
			$("#b_jqgrids").setColProp("includeSku",{editable:false});
			$(".inlineOrder2").addClass("gray");
			$('.inlineOrder2').attr('disabled', true);
			$(".delOrder2").addClass("gray");
			$('.delOrder2').attr('disabled', true);
		}
	});

	//三条件限制
	$('#q').click(function() {
		if($(this).prop("checked")) {
			$(this).val(0);
			$(".inp2 input").attr("disabled", true);
			$(".inp2 input").next().css("color", "#CCCCCC");
			
			$(".inlineOrder").addClass("gray");
			$('.inlineOrder').attr('disabled', true);
			$(".delOrder").addClass("gray");
			$('.delOrder').attr('disabled', true);
			
			$(".inlineOrder1").addClass("gray");
			$('.inlineOrder1').attr('disabled', true);
			$(".delOrder1").addClass("gray");
			$('.delOrder1').attr('disabled', true);
			
			$(".inlineOrder2").addClass("gray");
			$('.inlineOrder2').attr('disabled', true);
			$(".delOrder2").addClass("gray");
			$('.delOrder2').attr('disabled', true);
			
			$("#buyerNoteNull").prop("checked",false);
			$("#buyerNote").prop("checked",false);
			$("#sellerNoteNull").prop("checked",false);
			$("#sellerNote").prop("checked",false);
			$("#includeSku").prop("checked",false);
			
			$("#t_jqgrids").jqGrid('clearGridData');// 清空数据
			$("#t_jqgrids").jqGrid('setGridParam',{
				url: '../../assets/js/json/order.json',
			    datatype:"json",
			    mtype:"post",
			    page:1,// 初始化页码
//			    postData:{"param1":"value1",...} //新的查询参数
			});
			$("#m_jqgrids").jqGrid('clearGridData');// 清空数据
			$("#m_jqgrids").jqGrid('setGridParam',{
				url: '../../assets/js/json/order.json',
			    datatype:"json",
			    mtype:"post",
			    page:1,// 初始化页码
			});
			$("#b_jqgrids").jqGrid('clearGridData');// 清空数据
			$("#b_jqgrids").jqGrid('setGridParam',{
				url: '../../assets/js/json/order.json',
			    datatype:"json",
			    mtype:"post",
			    page:1,// 初始化页码
			});
		}
	});
	$('#w').click(function() {
		if($(this).prop("checked")) {
			$(this).val(1);
			$(".inp2 input").removeAttr("disabled");
			$(".inp2 input").next().css("color", "");
		}
	});
	$('#e').click(function() {
		if($(this).prop("checked")) {
			$(this).val(2);
			$(".inp2 input").attr("disabled", "true");
			$(".inp2 input").next().css("color", "#CCCCCC");
			
			$(".inlineOrder").addClass("gray");
			$('.inlineOrder').attr('disabled', true);
			$(".delOrder").addClass("gray");
			$('.delOrder').attr('disabled', true);
			
			$(".inlineOrder1").addClass("gray");
			$('.inlineOrder1').attr('disabled', true);
			$(".delOrder1").addClass("gray");
			$('.delOrder1').attr('disabled', true);
			
			$(".inlineOrder2").addClass("gray");
			$('.inlineOrder2').attr('disabled', true);
			$(".delOrder2").addClass("gray");
			$('.delOrder2').attr('disabled', true);
			
			$("#buyerNoteNull").prop("checked",false);
			$("#buyerNote").prop("checked",false);
			$("#sellerNoteNull").prop("checked",false);
			$("#sellerNote").prop("checked",false);
			$("#includeSku").prop("checked",false);
			
			$("#t_jqgrids").jqGrid('clearGridData');// 清空数据
			$("#t_jqgrids").jqGrid('setGridParam',{
				url: '../../assets/js/json/order.json',
			    datatype:"json",
			    mtype:"post",
			    page:1,// 初始化页码
//			    postData:{"param1":"value1",...} //新的查询参数
			});
			$("#m_jqgrids").jqGrid('clearGridData');// 清空数据
			$("#m_jqgrids").jqGrid('setGridParam',{
				url: '../../assets/js/json/order.json',
			    datatype:"json",
			    mtype:"post",
			    page:1,// 初始化页码
			});
			$("#b_jqgrids").jqGrid('clearGridData');// 清空数据
			$("#b_jqgrids").jqGrid('setGridParam',{
				url: '../../assets/js/json/order.json',
			    datatype:"json",
			    mtype:"post",
			    page:1,// 初始化页码
			});
		}
	});

})
//初始化表格
function pageInit1() {
	jQuery("#t_jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		ajaxGridOptions: {
			contentType: 'application/json; charset=utf-8'
		},
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['买家包含字段'], //jqGrid的列显示名字
		colModel: [{
			name: "buyerNote",
			align: "center",
			editable: true,
			sortable: false,
			editoptions : {maxlength : "50"}
		}],
		autowidth: true,
		viewrecords: true,
		rownumbers: true,
		loadonce: true,
		collapsed: true,
		height: 100,
		autoScroll: true,
		shrinkToFit: false,
		forceFit: true,
		cellEdit: true,
		sortable: false,
		cellsubmit: "clientArray",
		rowNum: 100,
//		rowList: [10, 20, 30], //可供用户选择一页显示多少条
		pager: '#t_jqGrids', //表格页脚的占位符(一般是div)的id
		multiboxonly: true,
		multiselect: true, //复选框
		caption: "买家留言包含字段", //表格的标题名字
		rownumWidth: 8, // 设置序列号列的宽度
		//在编辑状态时
		afterEditCell: function(rowid, cellname, val, iRow, iCol) {
			$(".saveOrder").addClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);

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
}

function pageInit2() {

	jQuery("#m_jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		ajaxGridOptions: {
			contentType: 'application/json; charset=utf-8'
		},
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['卖家包含字段'], //jqGrid的列显示名字
		colModel: [{
			name: "sellerNote",
			align: "center",
			editable: true,
			sortable: false,
			editoptions : {maxlength : "50"}
		}],
		autowidth: true,
		viewrecords: true,
		rownumbers: true,
		loadonce: true,
		height: 100,
		autoScroll: true,
		shrinkToFit: false,
		forceFit: true,
		collapsed: true,
		cellEdit: true,
		sortable: false,
		cellsubmit: "clientArray",
		rowNum: 100,
//		rowList: [10, 20, 30], //可供用户选择一页显示多少条
		pager: '#m_jqGrids', //表格页脚的占位符(一般是div)的id
		multiboxonly: true,
		rownumWidth: 8, // 设置序列号列的宽度
		multiselect: true, //复选框
		caption: "卖家留言包含字段", //表格的标题名字
		//在编辑状态时
		afterEditCell: function(rowid, cellname, val, iRow, iCol) {
			$(".saveOrder").addClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);

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
}

function pageInit3() {

	jQuery("#b_jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		ajaxGridOptions: {
			contentType: 'application/json; charset=utf-8'
		},
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['sku'], //jqGrid的列显示名字
		colModel: [{
			name: "includeSku",
			align: "center",
			editable: true,
			sortable: false,
			editoptions : {maxlength : "50"}
		}],
		autowidth: true,
		viewrecords: true,
		rownumbers: true,
		loadonce: true,
		autoScroll: true,
		shrinkToFit: false,
		height: 100,
		collapsed: true,
		forceFit: true,
		cellEdit: true,
		sortable: false,
		cellsubmit: "clientArray",
		rowNum: 100,
		rownumWidth: 8, // 设置序列号列的宽度
//		rowList: [10, 20, 30], //可供用户选择一页显示多少条
		pager: '#b_jqGrids', //表格页脚的占位符(一般是div)的id
		multiboxonly: true,
		multiselect: true, //复选框
		caption: "sku", //表格的标题名字
		//在编辑状态时
		afterEditCell: function(rowid, cellname, val, iRow, iCol) {
			$(".saveOrder").addClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);

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
}

//自适应宽度
$(function() {
	$(window).resize(function() {
		$("#t_jqgrids").setGridWidth($(window).width());
	});
});

$(function() {
	$(window).resize(function() {
		$("#m_jqgrids").setGridWidth($(window).width());
	});
});

$(function() {
	$(window).resize(function() {
		$("#b_jqgrids").setGridWidth($(window).width());
	});
});

arr = new Array();
$(function() {
	$(".inp2").on("click", "input", function() {
		var sum = $('#xianzhi input[name="vaType"]').val() * 1
		arr.push(sum)
	})
	return arr;
})


function IsCheckValue1(name,num,buyerNoteNull,buyerNote,sellerNoteNull,sellerNote,includeSku,listData1) {
	if(name == "") {
		alert("免审策略名称不能为空")
		return false;
	} else if(num == undefined) {
		alert("业务免审规则未选择")
		return false;
	} else if(num == 1 && buyerNoteNull == undefined && buyerNote == undefined && sellerNoteNull == undefined && sellerNote == undefined && includeSku == '') {
		alert("条件未选择，有且至少有一个")
		return false;
	} else if(listData1.length == 0 && buyerNote == "1") {
		alert("未填写买家留言")
		return false;
	} else if(listData1.length != 0) {
		for(var i = 0; i < listData1.length; i++) {
			var buy1 = listData1[i].buyerNote.indexOf("<input",0)
			if(buy1 != -1) {
				alert("买家留言中存在未回车提交到表格项")
				return false;
			} 
		}
	} 
	return true;
}
function IsCheckValue4(name,num,buyerNoteNull,buyerNote,sellerNoteNull,sellerNote,includeSku,listData1) {
	if(name == "") {
		alert("免审策略名称不能为空")
		return false;
	} else if(num == undefined) {
		alert("业务免审规则未选择")
		return false;
	} else if(num == 1 && buyerNoteNull == undefined && buyerNote == undefined && sellerNoteNull == undefined && sellerNote == undefined && includeSku == '0') {
		alert("条件未选择，有且至少有一个")
		return false;
	} else if(listData1.length == 0 && buyerNote == "1") {
		alert("未填写买家留言")
		return false;
	} else if(listData1.length != 0) {
		for(var i = 0; i < listData1.length; i++) {
			var buy1 = listData1[i].buyerNote.indexOf("<input",0)
			if(buy1 != -1) {
				alert("买家留言中存在未回车提交到表格项")
				return false;
			} 
		}
	} 
	return true;
}

function IsCheckValue2(listData2,sellerNote) {
	console.log(listData2.length)
	if(listData2.length == 0 && sellerNote == "1") {
		alert("未填写卖家留言")
		return false;
	}else if(listData2.length != 0) {
		for(var i = 0; i < listData2.length; i++) {
			var buy2 = listData2[i].sellerNote.indexOf("<input",0)
			if(buy2 != -1) {
				alert("卖家留言中存在未回车提交到表格项")
				return false;
			} 
		}
	} 
	return true;
}
function IsCheckValue3(listData3,includeSku) {
	if(listData3.length == 0 && includeSku == 1) {
		alert("未填写sku")
		return false;
	} else if(listData3.length != 0) {
		for(var i = 0; i < listData3.length; i++) {
			var buy3 = listData3[i].includeSku.indexOf("<input",0)
			if(buy3 != -1) {
				alert("sku中存在未回车提交到表格项")
				return false;
			} 
		}
	}
	return true;
}
//	第一个
function getJQAllData1() {
	//拿到grid对象
	var obj1 = $("#t_jqgrids");
	//获取grid表中所有的rowid值
	var rowIds1 = obj1.getDataIDs();
	//初始化一个数组arrayData容器，用来存放rowData
	var arrayData1 = new Array();
	if(rowIds1.length > 0) {
		for(var i = 0; i < rowIds1.length; i++) {
			if(obj1.getRowData(rowIds1[i]).buyerNote == "") {
				continue;
			} else {
				arrayData1.push(obj1.getRowData(rowIds1[i]));
			}
		}
	}
	return arrayData1;
}

//	第二个
function getJQAllData2() {
	//拿到grid对象
	var obj2 = $("#m_jqgrids");
	//获取grid表中所有的rowid值
	var rowIds2 = obj2.getDataIDs();
	//初始化一个数组arrayData容器，用来存放rowData
	var arrayData2 = new Array();
	if(rowIds2.length > 0) {
		for(var i = 0; i < rowIds2.length; i++) {
			if(obj2.getRowData(rowIds2[i]).sellerNote == "") {
				continue;
			} else {
				arrayData2.push(obj2.getRowData(rowIds2[i]));
			}
		}
	}
	return arrayData2;
}

//	第三个
function getJQAllData3() {
	//拿到grid对象
	var obj3 = $("#b_jqgrids");
	//获取grid表中所有的rowid值
	var rowIds3 = obj3.getDataIDs();
	//初始化一个数组arrayData容器，用来存放rowData
	var arrayData3 = new Array();
	if(rowIds3.length > 0) {
		for(var i = 0; i < rowIds3.length; i++) {
			if(obj3.getRowData(rowIds3[i]).includeSku == "") {
				continue;
			} else {
				arrayData3.push(obj3.getRowData(rowIds3[i]));
			}
		}
	}
	return arrayData3;
}

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
// 点击增加按钮，执行的操作
$(function() {
	
	$('.addOrder').click(function() {
		mType = 1;
		$("#mengban").hide();
		$('button').addClass("gray");
		$(".saveOrder").removeClass("gray");
		$(".upOrder").removeClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)

		$('.inlineOrder').attr('disabled', true);
		$('.inlineOrder1').attr('disabled', true);
		$('.inlineOrder2').attr('disabled', true);

		$('.delOrder').attr('disabled', true);

		$('.delOrder1').attr('disabled', true);

		$('.delOrder2').attr('disabled', true);
		
		var buyerNoteNull = $("input[name='buyerNoteNull']:checked").val()
		var sellerNoteNull = $("input[name='sellerNoteNull']:checked").val();
		var buyerNote = $("input[name='buyerNote']:checked").val();
		var sellerNote = $("input[name='sellerNote']:checked").val();
		var includeSku = $("input[name='includeSku']:checked").val();
		if(buyerNoteNull == undefined  && buyerNote == undefined ) {
			$("#t_jqgrids").setColProp("buyerNote",{editable:false});
		}
		if(sellerNoteNull == undefined  && sellerNote == undefined ) {
			$("#m_jqgrids").setColProp("sellerNote",{editable:false});
		}
		if(includeSku == undefined) {
			$("#b_jqgrids").setColProp("includeSku",{editable:false});
		}
	})
})

//增加保存
function SaveNewData() {
	var num = $('input[name="radio"]:checked').val()
	var obj = [];
	var listData1 = getJQAllData1();
	var listData2 = getJQAllData2();
	var listData3 = getJQAllData3();
	var name = $("input[name='name']").val();
	var buyerNoteNull = $("input[name='buyerNoteNull']:checked").val()
	var sellerNoteNull = $("input[name='sellerNoteNull']:checked").val();
	var buyerNote = $("input[name='buyerNote']:checked").val();
	var sellerNote = $("input[name='sellerNote']:checked").val();
	var momEncd = $("input[name='momEncd']").val();
	var includeSku = $("input[name='includeSku']").val();
	var vaType = $("input[name='vaType']").val();
	console.log(listData1)
	console.log(listData2)
	console.log(listData3)
	var new_arr=[];
	for(var i=0;i<arr.length;i++) {  
	   var items=arr[i];  
	   //判断元素是否存在于new_arr中，如果不存在则插入到new_ar中
	   if($.inArray(items,new_arr)==-1) {  
		new_arr.push(items);  
	     }  
	}
	
	//	第1个
	var d = {
		'1': 'buyerNote'
	}
	for(var j = 0; j < listData1.length; j++) {
		new_arr.forEach(item => {
			const data1 = {
				vaType: item,
				va: listData1[j][d[item]],
			}
			if(data1.va != undefined & data1.va != "") {
				obj.push(data1)
			}
		})
	}
	//	第2个
	var c = {
		'0': 'sellerNote',
	}
	for(var j = 0; j < listData2.length; j++) {
		new_arr.forEach(item => {
			const data2 = {
				vaType: item,
				va: listData2[j][c[item]],
			}
			if(data2.va != undefined & data2.va != "") {
				obj.push(data2)
			}
		})
	}
//
	//	第3个
	var f = {
		'2': 'includeSku'
	}
	for(var j = 0; j < listData3.length; j++) {
		new_arr.forEach(item => {
			const data3 = {
				vaType: item,
				va: listData3[j][f[item]],
			}
			if(data3.va != undefined & data3.va != "") {
				obj.push(data3)
			}
		})
	}
	console.log(buyerNoteNull,buyerNote,sellerNoteNull,sellerNote,includeSku)
	//判断页面是否有值为空
	if(IsCheckValue1(name,num,buyerNoteNull,buyerNote,sellerNoteNull,sellerNote,includeSku,listData1) == true) {
		if(IsCheckValue2(listData2,sellerNote) == true) {
			if(IsCheckValue3(listData3,includeSku) == true) {
				var savedata = {
					"reqHead": reqhead,
					"reqBody": {
						'auditWay': num,
						'name': name,
						'buyerNoteNull': buyerNoteNull,
						'sellerNoteNull': sellerNoteNull,
						'buyerNote': buyerNote,
						'sellerNote': sellerNote,
						'includeSku': includeSku,
						//				'chkTm': chkTm,
						'list': obj
					}
				};
				var saveData = JSON.stringify(savedata);
				console.log(saveData)
				$.ajax({
					url: url + '/mis/ec/auditStrategy/addStrategy',
					type: 'post',
					data: saveData,
					dataType: 'json',
					async: true,
					contentType: 'application/json;charset=utf-8',
					success: function(data) {
						$("#mengban").show()
						alert(data.respHead.message)
						var addId = data.respBody.id
						$("input[name='id']").val(addId);
						//					fun()
					},
					error: function() {
						console.log(error);
					} //错误执行方法
				})
			}
		}
	}
}

// 点击修改按钮，执行的操作
$(function() {
	$('.editOrder').click(function() {
		mType = 2;
		$("#mengban").hide()
		if($(".nu input[name='buyerNote']").val() == 1) {
			$("#buyerNote").removeAttr("disabled");
			$("#buyerNote").next().css("color", "");

//			$(".inlineOrder").removeClass("gray");
//			$('.inlineOrder').attr('disabled', false);
//			$(".delOrder").removeClass("gray");
//			$('.delOrder').attr('disabled', false);
		}
		if($(".nu input[name='buyerNoteNull']").val() == 1) {
			$("#buyerNoteNull").removeAttr("disabled");
			$("#buyerNoteNull").next().css("color", "");

			$(".inlineOrder").addClass("gray");
			$('.inlineOrder').attr('disabled', true);
			$(".delOrder").addClass("gray");
			$('.delOrder').attr('disabled', true);
		}
		
		
		if($(".nu1 input[name='sellerNote']").val() == 1) {
			$("#sellerNote").removeAttr("disabled");
			$("#sellerNote").next().css("color", "");

//			$(".inlineOrder1").removeClass("gray");
//			$('.inlineOrder1').attr('disabled', false);
//			$(".delOrder1").removeClass("gray");
//			$('.delOrder1').attr('disabled', false);
		}
		if($(".nu1 input[name='sellerNoteNull']").val() == 1) {
			$("#sellerNoteNull").removeAttr("disabled");
			$("#sellerNoteNull").next().css("color", "");

			$(".inlineOrder1").addClass("gray");
			$('.inlineOrder1').attr('disabled', true);
			$(".delOrder1").addClass("gray");
			$('.delOrder1').attr('disabled', true);
		}
		
		
		if($(".nu2 input[name='includeSku']").val() == 1) {
			$("#includeSku").removeAttr("disabled");
			$("#includeSku").next().css("color", "");
			
			$(".inlineOrder2").removeClass("gray");
			$('.inlineOrder2').attr('disabled', false);
			$(".delOrder2").removeClass("gray");
			$('.delOrder2').attr('disabled', false);
		}
		if($(".nu2 input[name='includeSku']").val() == '') {
			$("#includeSku").attr('disabled', false);;
			$("#includeSku").next().css("color", "");
			
			$(".inlineOrder2").addClass("gray");
			$('.inlineOrder2').attr('disabled', true);
			$(".delOrder2").addClass("gray");
			$('.delOrder2').attr('disabled', true);
		}
		$("#mengban").hide();
//		$('button').addClass('gray')
		$(".saveOrder").removeClass("gray");
		$('.upOrder').removeClass("gray");
		$('.editOrder').addClass("gray"); //点击修改后 修改不能用
//		$('button').attr('disabled', false);
		$(".editOrder").attr('disabled', true);
		$(".saveOrder").attr('disabled', false);
		$(".upOrder").attr('disabled', false);
	});
})

//保存修改后的数据
function SaveModifyData() {
	var num = $('input[name="radio"]:checked').val()
	var obj = [];
	var listData1 = getJQAllData1();
	var listData2 = getJQAllData2();
	var listData3 = getJQAllData3();
	var name = $("input[name='name']").val();
	if(localStorage.getItem('id') != null) {
		var id = localStorage.id;
	}else {
		var id = $("input[name='id']").val();		
	}
	var buyerNoteNull = $("input[name='buyerNoteNull']:checked").val();
	var sellerNoteNull = $("input[name='sellerNoteNull:checked']").val();
	//	var provrId = localStorage.provrId;
	//	var userName = localStorage.userName;
	var buyerNote = $("input[name='buyerNote']:checked").val();
	var sellerNote = $("input[name='sellerNote']:checked").val();
	var momEncd = $("input[name='momEncd']").val();
	//	var accNum = localStorage.accNum;
	var includeSku = $("input[name='includeSku']").val();
	var vaType = $("input[name='vaType']").val();
	
	var new_arr=[];
	for(var i=0;i<arr.length;i++) {  
	   var items=arr[i];  
	   //判断元素是否存在于new_arr中，如果不存在则插入到new_ar中
	   if($.inArray(items,new_arr)==-1) {  
		new_arr.push(items);  
	     }  
	}
	
	//	第1个
	var d = {
		'1': 'buyerNote'
	}
	for(var j = 0; j < listData1.length; j++) {
		new_arr.forEach(item => {
			const data1 = {
				vaType: item,
				va: listData1[j][d[item]],
			}
			if(data1.va != undefined & data1.va != "") {
				obj.push(data1)
			}
		})
	}
	//	第2个
	var c = {
		'0': 'sellerNote',
	}
	for(var j = 0; j < listData2.length; j++) {
		new_arr.forEach(item => {
			const data2 = {
				vaType: item,
				va: listData2[j][c[item]],
			}
			if(data2.va != undefined & data2.va != "") {
				obj.push(data2)
			}
		})
	}
//
	//	第3个
	var f = {
		'2': 'includeSku'
	}
	for(var j = 0; j < listData3.length; j++) {
		new_arr.forEach(item => {
			const data3 = {
				vaType: item,
				va: listData3[j][f[item]],
			}
			if(data3.va != undefined & data3.va != "") {
				obj.push(data3)
			}
		})
	}
	console.log(buyerNoteNull,buyerNote,sellerNoteNull,sellerNote,includeSku)
	//判断页面是否有值为空
	if(IsCheckValue4(name,num,buyerNoteNull,buyerNote,sellerNoteNull,sellerNote,includeSku,listData1) == true) {
		if(IsCheckValue2(listData2,sellerNote) == true) {
			if(IsCheckValue3(listData3,includeSku) == true) {
				var savedata = {
					"reqHead": reqhead,
					"reqBody": {
						'id': id,
						'auditWay': num,
						'name': name,
						'buyerNoteNull': buyerNoteNull,
						'sellerNoteNull': sellerNoteNull,
						'buyerNote': buyerNote,
						'sellerNote': sellerNote,
						'includeSku': includeSku,
						'list': obj
					}
				};
				var saveData = JSON.stringify(savedata);
				console.log(saveData)
				$.ajax({
					url: url + '/mis/ec/auditStrategy/update',
					type: 'post',
					data: saveData,
					dataType: 'json',
					async: true,
					contentType: 'application/json;charset=utf-8',
					success: function(data) {
						$("#mengban").show()
						alert(data.respHead.message)
						$('.editOrder').removeClass("gray"); //点击修改后 修改不能用
						$('button').attr('disabled', false);
						$(".gray").attr('disabled', true)
					},
					error: function() {
						console.log('错误');
					} //错误执行方法
				})
			}
		}
	}
}


//查询详细信息
$(function() {
	var afterUrl = window.location.search.substring(1);
	var b = [];
	b = afterUrl;
	var a = 1
	if(a == b) {
		chaxun()
	}
})


function chaxun() {
	$("#mengban").show()
	var id = localStorage.id;
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"id": id
		}
	};
	var saveData = JSON.stringify(savedata);
	var myDate = {};
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/ec/auditStrategy/findById',
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
			console.log(data)
			$("input[name='name']").attr("readonly","readonly" )
			$(".addOrder").addClass("gray");
			$('.addOrder').attr('disabled', true);
			$(".editOrder").removeClass("gray");
			$('.editOrder').attr('disabled', false);
			var list = data.respBody.strategyGoodsList;
			$("input[name='name']").val(data.respBody.name);
			$("input[name='radio'][value='"+data.respBody.auditWay+"']").attr("checked",'checked');
			$(".nu input[name='buyerNoteNull']").val(data.respBody.buyerNoteNull);
			$(".nu input[name='buyerNote']").val(data.respBody.buyerNote);
			$(".nu1 input[name='sellerNoteNull']").val(data.respBody.sellerNoteNull);
			$(".nu1 input[name='sellerNote']").val(data.respBody.sellerNote);
			$(".nu2 input[name='includeSku']").val(data.respBody.includeSku);
			
			window.num = $('input[name="radio"]:checked').val()
			
			window.arr = []
			if($(".nu input[name='buyerNoteNull']").val() == 1) {
				$(".nu input[name='buyerNoteNull']").attr('checked','checked')
			}
			if($(".nu input[name='buyerNote']").val() == 1) {
				$(".nu input[name='buyerNote']").attr('checked','checked')
				$("#vaType").val(0)
				arr.push(parseInt($('#xianzhi input[name="vaType"]').val()))
				
				$("#buyerNoteNull").attr("disabled", "true");
				$("#buyerNoteNull").next().css("color", "#CCCCCC");
	
				$(".inlineOrder").removeClass("gray");
				$('.inlineOrder').attr('disabled', false);
				$(".delOrder").removeClass("gray");
				$('.delOrder').attr('disabled', false);
			}
			
			if($(".nu1 input[name='sellerNoteNull']").val() == 1) {
				$(".nu1 input[name='sellerNoteNull']").attr('checked','checked')
			}
			if($(".nu1 input[name='sellerNote']").val() == 1) {
				$(".nu1 input[name='sellerNote']").attr('checked','checked')
				$("#sellerNoteNull").attr("disabled", "true");
				$("#sellerNoteNull").next().css("color", "#CCCCCC");
				$("#vaType").val(1)
				arr.push(parseInt($('#xianzhi input[name="vaType"]').val()))
	
				$(".inlineOrder1").removeClass("gray");
				$('.inlineOrder1').attr('disabled', false);
				$(".delOrder1").removeClass("gray");
				$('.delOrder1').attr('disabled', false);
			}
			
			if($(".nu2 input[name='includeSku']").val() == 1) {
				$(".nu2 input[name='includeSku']").attr('checked','checked')
				$("#vaType").val(2)
				arr.push(parseInt($('#xianzhi input[name="vaType"]').val()))
				$(".inlineOrder2").removeClass("gray");
				$('.inlineOrder2').attr('disabled', false);
				$(".delOrder2").removeClass("gray");
				$('.delOrder2').attr('disabled', false);
			}
			
			var num3 = []
			for(var i = 0; i < list.length; i++) {
				if(list[i].vaType != 0) {
					continue;
				}
				num3.push(list[i])
				for(var l = 0; l < num3.length; l++) {
					$("#m_jqgrids").setRowData(l + 1, {
						sellerNote: num3[l].va
					})
				}
			}
			
			var num1 = []
			for(var j = 0; j < list.length; j++) {
				if(list[j].vaType != 1) {
					continue;
				}
				num1.push(list[j])
				for(var i = 0; i < num1.length; i++) {
					$("#t_jqgrids").setRowData(i + 1, {
						buyerNote: num1[i].va
					})
				}
			}
			
			var num2 = []
			for(var k = 0; k < list.length; k++) {
				if(list[k].vaType != 2) {
					continue;
				}
				num2.push(list[k])
				for(var i = 0; i < num2.length; i++) {
					$("#b_jqgrids").setRowData(i + 1, {
						includeSku: num2[i].va
					})
				}
			}
		}
	})
}