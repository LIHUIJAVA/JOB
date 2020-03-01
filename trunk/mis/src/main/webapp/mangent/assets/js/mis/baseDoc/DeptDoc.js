var count;
var pages;
var page = 1;
var rowNum;
//页面初始化
$(function() {
	pageInit();
	search()
});

function pageInit() {
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	allHeight()
	jQuery("#jqgrids").jqGrid({
		datatype: "local", //请求数据返回的类型。可选json,xml,txt
		colNames: ['部门编码', '部门名称', '联系方式', '地址', '备注'], //jqGrid的列显示名字
		colModel: [{
				name: "deptId",
				align: "center",
				editable: true,
			},
			{
				name: "deptName",
				align: "center",
				editable: true,
			},
			{
				name: "tel",
				align: "center",
				editable: true,
			},
			{
				name: 'addr',
				align: "center",
				editable: true,
			},
			{
				name: 'memo',
				align: "center",
				editable: true,
			}
		],
		height: height - 10,
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
		caption: "部门档案", //表格的标题名字
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
			mType = 1;
			var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
			$('#jqgrids').editRow(gr, true);
			var grValue = $("#" + gr + "_deptId").val()
			if(grValue) {
				$("#" + gr + "_deptId").attr("readonly", "readonly");
			}
		}
	}).closest(".ui-jqgrid-bdiv").css({
		'overflow-y': 'scroll'
	});
}

function addEditData() {
	var gr = $("#jqgrids").jqGrid('getGridParam', 'selarrrow');
	var rowData = [];
	for(var i = 0; i < gr.length; i++) {
		var obj = {};
		obj.deptId = $("#" + gr[i] + "_deptId").val().toString();
		obj.deptName = $("#" + gr[i] + "_deptName").val().toString();
		obj.tel = $("#" + gr[i] + "_tel").val().toString();
		obj.addr = $("#" + gr[i] + "_addr").val().toString();
		obj.memo = $("#" + gr[i] + "_memo").val().toString();
		rowData[i] = obj;
	}
	if(rowData.length == 0) {
		alert("请选择单据!")
	} else {
		var edit = {
			"reqHead": reqhead,
			"reqBody": {
				"list": rowData
			}
		};

		var editJson = JSON.stringify(edit);
		$.ajax({
			type: "post",
			url: url + "/mis/purc/DeptDoc/updateDeptDocByDeptEncd",
			async: true,
			data: editJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(editMsg) {
				alert(editMsg.respHead.message)
				search()
			},
			error: function() {
				alert("更新失败")
			}
		});
	}
}

var mType = 0;
var save = 0;
$(function() {
	$(".saveOrder").click(function() {
		save = 1;
		if(mType == 1) {
			addEditData(); //编辑按钮
		} else if(mType == 2) {
			addNewData(); //添加新数据
		}
		$(".addOrder").removeClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
	})
})

//添加新数据
var newrowid;

function addNewData() {
	var deptId = $("#" + newrowid + "_deptId").val();
	var deptName = $("#" + newrowid + "_deptName").val();
	var tel = $("#" + newrowid + "_tel").val();
	var addr = $("#" + newrowid + "_addr").val();
	var memo = $("#" + newrowid + "_memo").val();
	var save = {
		"reqHead": reqhead,
		"reqBody": {
			"deptId": deptId,
			"deptName": deptName,
			"tel": tel,
			"addr": addr,
			"memo": memo
		}
	}
	var saveJson = JSON.stringify(save);
	if(deptId == '' && tel == '' && addr == '' && memo == '') {
		alert('请重新输入内容')
	} else {
		$.ajax({
			type: "post",
			url: url + "/mis/purc/DeptDoc/insertDeptDoc",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(data) {
				alert(data.respHead.message)
				search()
			},
			error: function() {
				alert("增加失败")
			}
		});

	}

}

//新增部门档案
$(function() {
	$(".addOrder").click(function() {
		mType = 2;
		var selectedId = $("#jqgrids").jqGrid("getGridParam", "selrow");
		var gr = $("#jqgrids").jqGrid('getDataIDs');
		//获得当前最大行号（数据编码）
		var rowid = Math.max.apply(Math, gr);
		//获得新添加行的行号（数据编码）
		newrowid = rowid + 1;
		var dataRow = {
			deptId: "",
			deptName: "",
			tel: '',
			addr: '',
			memo: ''
		};

		//将新添加的行插入到第一列
		$("#jqgrids").jqGrid("addRowData", newrowid, dataRow, "first");
		//设置grid单元格可编辑
		$('#jqgrids').jqGrid('editRow', newrowid, true);

		$(".addOrder").addClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true);
		// 点击新增行默认选中此行
		$("#jqgrids").jqGrid('setSelection', newrowid);
	})
})

//删除行
$(function() {
	$(".delOrder").click(function() {
		var ids = $("#jqgrids").jqGrid('getGridParam', 'selarrrow'); //获取选中=行id
		var rowData = [];

		for(i = 0; i < ids.length; i++) {

			//选中行的id
			var jstime = $("#jqgrids").getCell(ids[i], "deptId");
			rowData[i] = jstime
		}
		var rowDatas = rowData.toString();
		var deleteAjax = {
			"reqHead": reqhead,
			"reqBody": {
				"deptId": rowDatas
			}
		}
		var deleteData = JSON.stringify(deleteAjax)
		if(ids.length == 0) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + "/mis/purc/DeptDoc/deleteDeptDocList",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(remover) {
					search()
				},
				error: function() {
					alert("删除失败")
				}
			});

		}
	})
})

//条件查询
$(function() {
	$('.find').click(function() {
		search()
	})
})

function search() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var deptId = $("input[class='deptId']").val();
	var deptName = $("input[class='deptName']").val();
	var myData = {}
	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"deptId": deptId,
			"deptName": deptName,
			"pageNo": page,
			"pageSize": rowNum,
		}
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		url: url + "/mis/purc/DeptDoc/selectDeptDocList",
		async: true,
		data: postD2,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
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
			alert("条件查询失败")
		}
	});
}
//导入
$(function() {
	$(".importExcel").click(function() {
		var files = $("#FileUpload").val()
		var fileObj = document.getElementById("FileUpload").files[0];
		var formFile = new FormData();
		formFile.append("action", "UploadVMKImagePath");
		formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
		var data = formFile;
		if(files != "") {
			$.ajax({
				type: 'post',
				url: url + "/mis/purc/DeptDoc/uploadDeptDocFile",
				data: data,
				dataType: "json",
				cache: false, //上传文件无需缓存
				processData: false, //用于对data参数进行序列化处理 这里必须false
				contentType: false, //必须
				success: function(data) {
					alert(data.respHead.message)
					window.location.reload()
				},
				//开始加载动画  添加到ajax里面
				beforeSend: function() {
					$(".zhezhao").css("display", "block");
					$("#loader").css("display", "block");
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

//导出
$(document).on('click', '.exportExcel', function() {
	var deptId = $("input[class='deptId']").val();
	var deptName = $("input[class='deptName']").val();
	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"deptId": deptId,
			"deptName": deptName,
		}
	}
	var Data = JSON.stringify(data2);
	$.ajax({
		url: url + '/mis/purc/DeptDoc/printingDeptDocList',
		type: 'post',
		data: Data,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		beforeSend: function() {
			$("#mengban").css("display", "block");
			$("#loader").css("display", "block");
		},
		complete: function() {
			$("#mengban").css("display", "none");
			$("#loader").css("display", "none");
		},
		success: function(data) {
			var arr = [];
			var obj = {}
			var list = data.respBody.list;
			obj = list;
			daochu(obj)
		},
		error: function() {
			alert("导出失败")
		}
	})

})

function daochu(JSONData) {
    var str = '部门编码,部门名称,电话,地址,备注\n';

    for(let i=0;i<JSONData.length;i++){
        var result ='';
        if (JSONData[i].orderStatusc=='0'){
            result="是";
        } else {
            result="否";
        }
		for(let item in JSONData[i]) {
			if(JSONData[i][item]==null){
				JSONData[i][item]="";
			}
			str += `${JSONData[i][item] + '\t'},`;
		}
		str += '\n';
    }
    var blob = new Blob([str], {type: "text/plain;charset=utf-8"});
    //解决中文乱码问题
    blob =  new Blob([String.fromCharCode(0xFEFF), blob], {type: blob.type});
    object_url = window.URL.createObjectURL(blob);
    var link = document.createElement("a");
    link.href = object_url;
    link.download =  "部门档案.csv";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}