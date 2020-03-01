function gettypes() {
	var data1 = {
		"reqHead": reqhead,
		"reqBody": {
			"pageNo": 1,
			"pageSize": 500
		}
	};
	var postD1 = JSON.stringify(data1);
	var str = "";
	$.ajax({
		type: "post",
		async: false,
		dataType: 'json',
		data: postD1,
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/purc/DeptDoc/selectDeptDocList', //部门
		success: function(data) {
			if(data != null) {
				var length = data.respBody.list.length;
				str += "0" + ":" + "请选择" + ";";
				for(var i = 0; i < length; i++) {
					if(i != length - 1) {
						str += data.respBody.list[i].deptId + ":" + data.respBody.list[i].deptName + ";";
					} else {
						str += data.respBody.list[i].deptId + ":" + data.respBody.list[i].deptName;
					}
				}
			}
		},
		error: function() {
			alert('部门获取失败')
		}
	});
	return str;
}
var count;
var pages;
var page = 1;
var rowNum;
var mType = 0;
$(function() {
	//页面加载完成之后执行
	pageInit();
})

function pageInit() {
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	allHeight()
	jQuery("#jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['角色编码', '角色名称', '角色等级', '所属部门编码', '所属部门名称'], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'id',
				align: "center",
				index: 'invdate',
				editable: false
			},
			{
				name: 'name',
				align: "center",
				index: 'id',
				editable: true
			},
			{
				name: 'level',
				align: "center",
				index: 'invdate',
				editable: true
			},
			{
				name: 'depId',
				align: "center",
				index: 'invdate',
				editable: true,
				hidden: true,
			},
			{
				name: 'depName',
				align: "center",
				index: 'invdate',
				editable: true,
				edittype: "select",
				editoptions: {
					value: gettypes()
				}
			},
		],
		height: height,
		rowNum: 100, //一页显示多少条
		rowList: [100, 300, 500],
		rownumbers: true,
		multiselect: true, //复选框
		multiboxonly: true,
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'accSet', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		rownumWidth: 25,  //序列号列宽度
		caption: "角色列表查询", //表格的标题名字	
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
			search()
		},
		ondblClickRow: function() {
			mType = 2;
			$("#" + gr + "_id").attr("readonly", "readonly");
			var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
			jQuery('#jqgrids').editRow(gr, true);
			$(".addOrder").addClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
		}
	});

}

//增行  
$(function() {
	$(".addOrder").click(function() {
		$("#mengban").hide()
		$(".addOrder").addClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true);
		mType = 1;
		var newrowid;
		var selectedId = $("#jqgrids").jqGrid("getGridParam", "selrow");
		var ids = jQuery("#jqgrids").jqGrid('getDataIDs');

		var rowid = Math.max.apply(Math, ids); //获得当前最大行号（数据编码）
		newrowid = rowid + 1; //获得新添加行的行号（数据编码）
		var dataRow = {
			id: "",
			name: "",
			level: '',
			depId: '',
			depName: '',

		};

		$("#jqgrids").setColProp('id', {
			editable: true
		}); //设置editable属性由true改为false

		//将新添加的行插入到第一列
		$("#jqgrids").jqGrid("addRowData", newrowid, dataRow, "first");
		//设置grid单元格可编辑
		$('#jqgrids').jqGrid('editRow', newrowid, false);
		// 点击新增行默认选中此行
		$("#jqgrids").jqGrid('setSelection',newrowid);

		$(document).keyup(function(event) {
			if(event.keyCode == 13) {
				$(".saveOrder").trigger("click");
			}
		});
	})

})

function editData() {
	var gr = $("#jqgrids").jqGrid('getGridParam', 'selarrrow');
	var list = [];
	for(var i = 0; i < gr.length; i++) {
		var rowDatas = $("#jqgrids").jqGrid('getRowData', gr[i]);
		var obj = {}
		obj.id = rowDatas.id;
		obj.name = $("#" + gr[i] + "_name").val();
		obj.level = $("#" + gr[i] + "_level").val();
		obj.depId = $("#" + gr[i] + "_depName option:selected").val();
		obj.depName = $("#" + gr[i] + "_depName option:selected").text();
		list[i] = obj;
	}
	if(gr.length == 0) {
		alert("请选择单据")
	} else {
		var edit = {
			"reqBody": {
				"list": list,
			}
		}
		var editJson = JSON.stringify(edit);
		$.ajax({
			type: "post",
			url: url + "/mis/system/role/edit",
			async: true,
			data: editJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(editMsg) {
				alert(editMsg.respHead.message);
				search()
			},
			error: function() {
				alert("error")
			}
		});
	}
}

function addData() {
	var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow');
	var id = $("#" + gr + "_id").val();
	var name = $("#" + gr + "_name").val();
	var level = $("#" + gr + "_level").val();
	var depId = $("#" + gr + "_depName option:selected").val();
	var depName = $("#" + gr + "_depName option:selected").text();
	if(gr==undefined) {
		alert("请选择单据")
	} else {
		var save = {
			"reqBody": {
				"id": id,
				"name": name,
				"level": level,
				"depId": depId,
				"depName": depName
			}
		}
		var saveJson = JSON.stringify(save);

		$.ajax({
			type: "post",
			url: url + "/mis/system/role/add",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(msgAdd) {
				alert(msgAdd.respHead.message)
				search()
			},
			error: function(err) {
				alert("增加失败")
			}
		});
	}

}

$(function() {
	$(".saveOrder").click(function() {
		$(".addOrder").removeClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true);
		if(mType == 1) {
			addData()
		} else if(mType == 2) {
			editData(); //编辑按钮
		}
	})
})

$(document).on('click', '#find', function() {
	search()
})

//查询按钮
function search() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var id = $("input[name='id1']").val();
	var name = $("input[name='name1']").val();
	var depId = $("input[name='depId1']").val();
	var data2 = {
		reqHead,
		"reqBody": {
			"id": id,
			"name": name,
			"depId": depId,
			"pageNo": page,
			"pageSize": rowNum,
		}
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		url: url + "/mis/system/role/queryList",
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
			var mydata = {};
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#jqgrids").jqGrid("clearGridData");
			$("#jqgrids").jqGrid("setGridParam", {
				data: mydata.rows,
				jsonReader: {
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
			alert("增加失败")
		}
	});
}

//删除行
$(function() {
	$(".delOrder").click(function() {
		var list = []
		var gr = $("#jqgrids").jqGrid('getGridParam', 'selarrrow'); //获取行id
		for(var i = 0; i < gr.length; i++) {
			var rowDatas = $("#jqgrids").jqGrid('getRowData', gr[i]); //获取行数据    	
			var id = rowDatas.id
			list.push(id)
		}
		var list = list.toString()
		var deleteAjax = {
			reqHead,
			"reqBody": {
				"list": list
			}
		};
		var deleteData = JSON.stringify(deleteAjax)
		if(gr.length == 0) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + "/mis/system/role/delete",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(data) {
					alert(data.respHead.message)
					search()
				},
				error: function() {
					alert("删除失败")

				}
			});
		}
	})
})