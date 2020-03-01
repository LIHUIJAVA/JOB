$(function() {
	pageInit();
})

//页面初始化

function pageInit() {
//	allHeight()
	pageNo = 1;
	rowNum = 10;

	//创建jqGrid组件	
	var data3 = {
		"reqHead": reqhead,
		"reqBody": {
			"pageNo": 1,
			"pageSize": 500
		}
	};
	var postD3 = JSON.stringify(data3);

	//初始化表格
	jQuery("#jqgrids").jqGrid({
		url: url3 + "/mis/ec/ecPlatform/queryList", //组件创建完成之后请求数据的url
		mtype: "post",
//		height: height * 0.6,
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		postData: postD3,
		ajaxGridOptions: {
			contentType: 'application/json; charset=utf-8'
		},
		colNames: [ '平台编码', '平台名称', '备注'], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			
			{
				name: 'ecId',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'ecName',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'memo',
				align: "center",
				editable: true,
				sortable: false,
			}

		],
		jsonReader: {
			root: "respBody.list",
			repeatitems: true,
			records: "respBody.count", // json中代表数据行总数的数据		            
			total: "respBody.pages", // json中代表页码总数的数据
		},
		autowidth:true,
		height:'100%',
		multiboxonly: true,
		autoScroll:true,
		shrinkToFit:false,
		rowNum: 10, //一页显示多少条
		viewrecords: true,
		rownumWidth: 20,  //序列号列宽度
		rownumbers: true,
		loadonce: true,
		forceFit: true, //调整列宽度不会改变表格的宽度
		rowNum: 10,
		rowList: [10, 20, 30], //可供用户选择一页显示多少条
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id

		multiselect: true, //复选框
		caption: "商品档案", //表格的标题名字	

		ondblClickRow: function() {
			mType = 2;
			var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
			var rowDatas = $("#jqgrids").jqGrid('getRowData', gr); //获取行数据
			$('#jqgrids').editRow(gr, true);
			$("#" + gr + "_ecId").attr("readonly", "readonly");
			$("#jqgrids").jqGrid('setCell', gr, 'ecId', '', 'not-editable-cell');
		}

	});
}


var mType = 0;
$(function() {
	$(".saveOrder").click(function() {
		if(mType == 1) {
			$(".addOrder").css("background-color", 'black')
			addNewData(); //添加新数据
			//			window.location.reload()
		} else if(mType == 2) {
			addEditData(); //编辑按钮		
		}
	})
})
//增行   保存
var newrowid;
$(function() {
	$(".addOrder").click(function() {
		mType = 1
		var selectedId = $("#jqgrids").jqGrid("getGridParam", "selrow");
		//拿到Gride中所有主键id的值
		var gr = $("#jqgrids").jqGrid('getDataIDs');
		//获得当前最大行号（数据编码）
		var rowid = Math.max.apply(Math, gr);
		//获得新添加行的行号（数据编码）
		newrowid = rowid + 1;
		var dataRow = {
			ecId: "",
			ecName: "",	
			memo: '',
		};

		//将新添加的行插入到第一列
		$("#jqgrids").jqGrid("addRowData", newrowid, dataRow, "first");
		//设置grid单元格可编辑
		$('#jqgrids').jqGrid('editRow', newrowid, true);
		$(".addOrder").css("background-color", 'gray')

	})
})



function addNewData() {
	var ecId = $("#" + newrowid + '_ecId').val();
	var ecName = $("#" + newrowid + "_ecName").val();
	var memo = $("#" + newrowid + "_memo").val();
	var save = {
		"reqHead": reqhead,
		"reqBody": {
			"ecId": ecId,
			"ecName": ecName,	
			"memo": memo
		}
	}
	var saveJson = JSON.stringify(save);
	$.ajax({
		type: "post",
		url: url3 + "/mis/ec/ecPlatform/add",
		async: true,
		data: saveJson,
		dataType: 'json',
		contentType: 'application/json',
		success: function(msgAdd) {
			alert(msgAdd.respHead.message)
			window.location.reload();
			//			$("#searchAll").trigger('click');
			$('#jqgrids').css("visibility", "true");
		},
		error: function(err) {
			console.log("增加失败")
		}
	});
}

//点击更新按钮
function addEditData() {
	//获取选中行id
	var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow');
//	var id = $("#" + gr + '_id').val();
	var ecId = $("#" + gr + '_ecId').val();
	var ecName = $("#" + gr + '_ecName').val();
	var memo = $("#" + gr + "_memo").val();
	var edit = {
		"reqHead": reqhead,
		"reqBody": {
			"ecId": ecId,
			"ecName": ecName,
			"memo": memo
		}
	}
	editJson = JSON.stringify(edit);
	$.ajax({
		type: "post",
		url: url3 + "/mis/ec/ecPlatform/edit",
		async: true,
		data: editJson,
		dataType: 'json',
		contentType: 'application/json',
		success: function(editMsg) {
			alert(editMsg.respHead.message);
			window.location.reload();
		},
		error: function(editMsg) {
			console.log("更新失败")
			console.log(editMsg)
		}
	});
}

//删除行
$(function(){
	$(".delOrder").click(function() {
		var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
		var rowDatas = $("#jqgrids").jqGrid('getRowData', gr); //获取行数据    	

		var deleteAjax = {
			reqHead,
			"reqBody": {
				"ecId": rowDatas.ecId,
			}
		};
		var deleteData = JSON.stringify(deleteAjax)
		if(gr == null) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url3 + "/mis/ec/ecPlatform/delete",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(remover) {
					alert("删除成功");
					window.location.reload();
					$("#searchAll").trigger('click')
					$('#jqgrids').css("visibility", "true");
				},
				error: function() {
					console.log("删除失败")

				}
			});

		}
	})
})

$(function(){
		//条件查询
	$('#find').click(function() {
		var ecId = $("input[name='ecId1']").val();
		
		var data2 = {
			reqHead,
			"reqBody": {
				"ecId": ecId,
				"pageNo": 1,
				"pageSize": 500
			}

		};
		var postD2 = JSON.stringify(data2);
		if (ecId=='') {
			window.location.reload()
		} else{
			$('#jqgrids').jqGrid('setGridParam', {
			url: url3 + "/mis/ec/ecPlatform/queryList",
			mtype: "post",
			datatype: "json", //请求数据返回的类型。可选json,xml,txt
			postData: postD2,
			rowNum: rowNum,
			ajaxGridOptions: {
				contentType: 'application/json; charset=utf-8'
			}
		}).trigger('reloadGrid')
		}
		
	})
})
