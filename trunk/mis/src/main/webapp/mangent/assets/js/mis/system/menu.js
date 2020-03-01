var count;
var pages;
var page = 1;
var rowNum;

$(function() {
	pageInit();
	loadLocalData(page)
})

function pageInit() {
//	allHeight()
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	allHeight()
	jQuery("#jqgrids").jqGrid({
		height: height,
		ajaxGridOptions: {
			contentType: 'application/json; charset=utf-8'
		},
		datatype: "local", //请求数据返回的类型。可选json,xml,txt
		colNames: ['菜单编码', '菜单名称', '菜单等级', '菜单图标', '菜单url', '上次菜单编码'], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'id',
				align: "center",
				index: 'id1',
				editable: true,
			},
			{
				name: 'name',
				align: "center",
				index: 'id',
				editable: true,
			},
			{
				name: 'level',
				align: "center",
				index: 'invdate',
				editable: true,
			},
			{
				name: 'ico',
				align: "center",
				index: 'invdate',
				editable: true,
			},
			{
				name: 'url',
				align: "center",
				index: 'invdate',
				editable: true,
			},
			{
				name: 'pid',
				align: "center",
				index: 'invdate',
				editable: true,
			},
		],
		autowidth: true,
		viewrecords: true,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 100,
		rowList: [100, 300, 500],
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		multiselect: true, //复选框
		multiboxonly: true,
		caption: "菜单查询", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#jqgrids").getGridParam('records'); //获取返回的记录数
			page = $("#jqgrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#jqgrids").getGridParam('rowNum'); //获取显示配置记录数量

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
		ondblClickRow: function(rowid, id, cellcontent, e) {
			var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
			var rowDatas = $("#jqgrids").jqGrid('getRowData', gr); //获取行数据

			if(rowDatas.id) {
				mType = 2;
				$('#jqgrids').editRow(gr, true);
				$("#" + gr + "_id").attr("readonly", "readonly");
			}
		}
	}).closest(".ui-jqgrid-bdiv").css({
		'overflow-y': 'scroll'
	});
}

function loadLocalData(page) {
	var rowNum1 = $("td select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var postData = JSON.stringify(data);
	$.ajax({
		type: "post",
		url: url + "/mis/system/menu/queryList", //列表
		async: false,
		data: postData,
		dataType: 'json',
		contentType: 'application/json',
		success: function(data) {
			var mydata = {};
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#jqgrids").jqGrid("clearGridData");
			$("#jqgrids").jqGrid("setGridParam", {
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
	})
}

$(document).on('click', '#find', function() {
	search()
})

//查询按钮
function search() {
	var id = $('input[name="id1"]').val();
	var name = $('input[name="name1"]').val();
	var level = $("input[name='level1']").val();
	var ico = $('input[name="ico1"]').val();
	var url1 = $('input[name="url1"]').val();
	var pid = $('input[name="pid1"]').val();
	var data2 = {
		reqHead,
		"reqBody": {
			"id": id,
			"name": name,
			"level": level,
			"ico": ico,
			"url": url1,
			"pid": pid,
			"pageSize": rowNum,
			"pageNo": page,
		}
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		url: url + "/mis/system/menu/queryList",
		async: true,
		data: postD2,
		dataType: 'json',
		contentType: 'application/json',
		//开始加载动画  添加到ajax里面
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");

		},
		success: function(data) {
			var mydata = {}
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#jqgrids").jqGrid("setGridParam", {
				data: mydata.rows,
				localReader: {
					root: function(object) {
						return mydata.rows
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
			alert("搜索失败")
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

//增行   保存
var newrowid;
$(function() {
	$(".addOrder").click(function() {
		mType = 1;
		var selectedId = $("#jqgrids").jqGrid("getGridParam", "selrow");
		var ids = jQuery("#jqgrids").jqGrid('getDataIDs');

		//获得当前最大行号（数据编码）
		var rowid = Math.max.apply(Math, ids);
		//获得新添加行的行号（数据编码）
		newrowid = rowid + 1;
		var dataRow = {
			id: "",
			name: '',
			level: '',
			ico: '',
			url: '',
			pid: ''

		};

		$("#jqgrids").setColProp('id', {
			editable: true
		}); //设置editable属性由true改为false

		//将新添加的行插入到第一列
		$("#jqgrids").jqGrid("addRowData", newrowid, dataRow, "first");
		//设置grid单元格可编辑
		$('#jqgrids').jqGrid('editRow', newrowid, true);
		// 点击新增行默认选中此行
		$("#jqgrids").jqGrid('setSelection', newrowid);
	})

})

//添加新数据
var newrowid;

function addNewData() {
	var id = $("input[name='id']").val();
	var name = $("input[name='name']").val();
	var level = $("input[name='level']").val();
	var ico = $("input[name='ico']").val();
	var urlv = $("input[name='url']").val();
	var pid = $("input[name='pid']").val();
	var save = {
		"reqHead": reqhead,
		"reqBody": {
			"id": id,
			"name": name,
			"level": level,
			"ico": ico,
			"url": urlv,
			"pid": pid,

		}
	}
	var saveJson = JSON.stringify(save);
	if(id == '' && name == '' && level == '' && ico == '' && url == '' && pid == '') {
		alert('请重新输入内容')
	} else {
		$.ajax({
			type: "post",
			url: url + "/mis/system/menu/add",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(data) {
				alert(data.respHead.message)
				if(data.respHead.isSuccess==true){
					search()
				}
			},
			error: function() {
				alert("增加失败")
			}
		});

	}

}

function addEditData() {
	var gr = $("#jqgrids").jqGrid('getGridParam', 'selarrrow');
	var rowData = [];
	for(var i = 0; i < gr.length; i++) {
		var obj = {};
		obj.id = $("#" + gr[i] + "_id").val().toString();
		obj.name = $("#" + gr[i] + "_name").val().toString();
		obj.level = $("#" + gr[i] + "_level").val().toString();
		obj.ico = $("#" + gr[i] + "_ico").val().toString();
		obj.url = $("#" + gr[i] + "_url").val().toString();
		obj.pid = $("#" + gr[i] + "_pid").val().toString();
		rowData[i] = obj;
	}

	if(rowData.length == 0) {
		alert("未选择，请重新选择!")
	} else {

		var edit = {
			"reqHead": reqhead,
			"reqBody": {
				"list": rowData
			}
		}
		var editJson = JSON.stringify(edit);
		$.ajax({
			type: "post",
			url: url + "/mis/system/menu/editList",
			async: true,
			data: editJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(data) {
				alert(data.respHead.message);
				if(data.respHead.isSuccess==true){
					search()
				}
			},
			error: function() {
				alert("更新失败")
			}
		});

	}

}

//删除行
$(function() {
	$(".delOrder").click(function() {
		var sum = []
		var gr = $("#jqgrids").jqGrid('getGridParam', 'selarrrow'); //获取行id
		for(var i = 0; i < gr.length; i++) {
			var rowDatas = $("#jqgrids").jqGrid('getRowData', gr[i]); //获取行数据   
			sum.push(rowDatas.id)
		}
		var num = sum.toString()
		var deleteAjax = {
			reqHead,
			"reqBody": {
				"id": num,

			}
		};
		var deleteData = JSON.stringify(deleteAjax)
		if(gr.length==0) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + "/mis/system/menu/delete",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(data) {
					alert(data.respHead.message);
					if(data.respHead.isSuccess==true){
						search()
					}
				},
				error: function() {
					alert("删除失败")
				}
			});

		}
	})
})