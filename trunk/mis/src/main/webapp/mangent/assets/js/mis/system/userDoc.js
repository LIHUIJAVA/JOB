//角色
function getTypes() {
	var data1 = {
		reqhead,
		"reqBody": {
			"email": '',
		}
	};
	var postD1 = JSON.stringify(data1);
	var str = "";
	$.ajax({
		type: "post",
		async: false,
		dataType: 'json',
		data: postD1,
		url: url + "/mis/system/role/queryAll", //角色
		success: function(data) {
			if(data != null) {
				var length = data.respBody.list.length;
				str += "0" + ":" + "请选择" + ";";
				for(var i = 0; i < length; i++) {
					if(i != length - 1) {
						str += data.respBody.list[i].id + ":" + data.respBody.list[i].name + ";";
					} else {
						str += data.respBody.list[i].id + ":" + data.respBody.list[i].name; // 这里是option里面的 value:label
					}
				}
			}
		},
		error: function() {
			alert('角色获取失败')
		}
	});
	return str;
}

//部门
function getType() {
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
//页面初始化
$(function() {
	pageInit();
});

function pageInit() {
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	allHeight()
	jQuery("#jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		height:height,
		colNames: ['用户名', '用户号', '所属部门编码', '所属部门名称', '角色编码', '角色名称', '手机号码', '企业邮箱', '用户创建日期'], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....

			{
				name: 'userName',
				editable: true,
				width: 100,
				align: 'center'
			},
			{
				name: 'accNum',
				editable: true,
				width: 100,
				sortable:true,
				sorttype:"int",
				align: 'center'
			},
			{
				name: 'depId',
				width: 100,
				align: 'center',
				hidden: true
			},
			{
				name: 'depName',
				editable: true,
				edittype: "select",
				editoptions: {
					value: getType()
				},
				width: 100,
				align: 'center'
			},
			{
				name: 'roleId',
				width: 100,
				align: 'center',
				hidden: true
			},
			{
				name: 'roleName',
				width: 100,
				editable: true,
				edittype: "select",
				editoptions: {
					value: getTypes()
				},
				align: 'center'
			},
			{
				name: 'phoneNo',
				editable: true,
				width: 100,
				align: 'center'
			},
			{
				name: 'email',
				editable: true,
				width: 100,
				align: 'center'
			},
			{
				name: 'createDate',
				editable: true,
				width: 100,
				editable: false,
				align: 'center'
			}
		],
		sortable:true,
		sortorder :"asc",
		multiselect: true, //复选框
		multiboxonly: true,
		viewrecords: true,
		rowNum: 500,
		rowList: [500, 1000, 2000], //可供用户选择一页显示多少条
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		caption: "用户档案", //表格的标题名字
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
			var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
			var rowDatas = $("#jqgrids").jqGrid('getRowData', gr); //获取行数据
			$('#jqgrids').editRow(gr, true);
			$("#" + gr + "_accNum").attr("readonly", "readonly");
			$("#whs").addClass("gray");
			$(".addOrder").addClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
		}
	});
}

function addEditData() {
	var gr = $("#jqgrids").jqGrid('getGridParam', 'selarrrow');
	var list = [];
	for(var i = 0; i < gr.length; i++) {
		var obj = {}
		obj.accNum = $("#" + gr[i] + "_accNum").val();
		obj.userName = $("#" + gr[i] + "_userName").val();
		obj.depId = $("#" + gr[i] + "_depName option:selected").val();
		obj.depName = $("#" + gr[i] + "_depName option:selected").text();
		obj.roleId = $("#" + gr[i] + "_roleName option:selected").val();
		obj.roleName = $("#" + gr[i] + "_roleName option:selected").text();
		obj.phoneNo = $("#" + gr[i] + "_phoneNo").val();
		obj.email = $("#" + gr[i] + "_email").val();
		list[i] = obj;
	}
	if(gr.length == 0) {
		alert("请选择单据")
	} else {
		var edit = {
			"reqHead": reqhead,
			"reqBody": {
				"list": list,
			}
		};

		var editJson = JSON.stringify(edit);
		$.ajax({
			type: "post",
			url: url + "/mis/system/misUser/updateBatch",
			async: true,
			data: editJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(editMsg) {
				alert(editMsg.respHead.message)
				search();
			},
			error: function() {
				alert("编辑失败")
			}
		});
	}
}

var mType = 0;
$(function() {
	$(".saveOrder").click(function() {
		$("#whs").removeClass("gray");
		$(".addOrder").removeClass("gray");
		$(".gray").attr('disabled', true);
		$('button').attr('disabled', false);
		if(mType == 1) {
			addNewData()
		} else if(mType == 2) {
			$(".addOrder").css("background-color", 'black')
			addEditData(); //编辑按钮
		}
	})
})

//添加新数据
var newrowid;

function addNewData() {
	//	var ids = $("#jqgrids").jqGrid('getGridParam', 'selrow');
	var accNum = $("#" + newrowid + "_accNum").val();
	var userName = $("#" + newrowid + "_userName").val();
	var depId = $("#" + newrowid + "_depName option:selected").val();
	var depName = $("#" + newrowid + "_depName option:selected").text();
	var roleId = $("#" + newrowid + "_roleName option:selected").val();
	var roleName = $("#" + newrowid + "_roleName option:selected").text();
	var phoneNo = $("#" + newrowid + "_phoneNo").val();
	var email = $("#" + newrowid + "_email").val();
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var day = date.getDate();
	var createDate = year + "-" + month + "-" + day;
	var save = {
		"reqHead": reqhead,
		"reqBody": {
			"accNum": accNum,
			"userName": userName,
			"depId": depId,
			"depName": depName,
			"roleId": roleId,
			"roleName": roleName,
			"phoneNo": phoneNo,
			"email": email,
			"createDate": createDate
		}
	}
	var saveJson = JSON.stringify(save);
	if(accNum == '' && roleId == '' && depId == '') {
		alert('请重新输入内容')
	} else {
		$.ajax({
			type: "post",
			url: url + "/mis/system/misUser/add",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(data) {
				alert(data.respHead.message)
				search()
			},
			error: function() {
				alert("新增失败")
			}
		});

	}
}

//新增部门档案
$(function() {
	$(".addOrder").click(function() {
		mType = 1;
		var selectedId = $("#jqgrids").jqGrid("getGridParam", "selrow");
		var gr = $("#jqgrids").jqGrid('getDataIDs');
		//获得当前最大行号（数据编码）
		var rowid = Math.max.apply(Math, gr);
		//获得新添加行的行号（数据编码）
		newrowid = rowid + 1;
		var dataRow = {
			"accNum": "",
			"userName": "",
			"depId": "",
			"depName": "",
			"roleId": "",
			"roleName": "",
			"phoneNo": "",
			"email": "",
			"createDate": ""
		};
		//将新添加的行插入到第一列
		$("#jqgrids").jqGrid("addRowData", newrowid, dataRow, "first");
		//设置grid单元格可编辑
		$('#jqgrids').jqGrid('editRow', newrowid, true);
		$(".addOrder").addClass("gray");
		$("#whs").addClass("gray");
		$(".gray").attr('disabled', true);
		$('button').attr('disabled', false);
	})
})

//删除行
$(function() {
	$(".delOrder").click(function() {
		var ids = $("#jqgrids").jqGrid('getGridParam', 'selarrrow'); //获取行id
		var rowData = [];
		for(i = 0; i < ids.length; i++) {
			//选中行的id
			var jstime = $("#jqgrids").getCell(ids[i], "accNum");
			rowData[i] = jstime
		}
		var rowDatas = rowData.toString();
		var deleteAjax = {
			"reqHead": reqhead,
			"reqBody": {
				"accNum": rowDatas
			}
		}
		var deleteData = JSON.stringify(deleteAjax)
		if(ids.length == 0) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + "/mis/system/misUser/delete",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(data) {
					alert(data.respHead.message)
					search();
				},
				error: function() {
					alert("error")
				}
			});

		}
	})
})

//条件查询
$(function() {
	$('#find').click(function() {
		search()
	})
})

function search() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var accNum = $(".userId").val();
	var userName = $(".userName").val();
	var deptId = $("#deptId").val();
	var roleId = $(".roleId").val()
	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"accNum": accNum,
			"userName": userName,
			"deptId": deptId,
			"roleId":roleId,
			"pageNo": page,
			"pageSize": rowNum,
		}
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		url: url + "/mis/system/misUser/queryList",
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
			var list = data.respBody.list;
			$("#jqgrids").jqGrid("clearGridData");
			$("#jqgrids").jqGrid("setGridParam", {
				datatype: 'local',
				data: list, //newData是符合格式要求的重新加载的数据
				page: 1 //哪一页的值
			}).trigger("reloadGrid");
		},
		//结束加载动画
		complete: function() {
			$(".zhezhao").css("display", "none");
			$("#loader").css("display", "none");
		},
		error: function() {
			alert("error")
		}
	});
}
var jstime;
$(function(){
	$("#whs").click(function(){
		var gr = $("#jqgrids").getGridParam('selrow');
		//选中行的id
		jstime = $("#jqgrids").getCell(gr, "accNum");
		var userName = $("#jqgrids").getCell(gr, "userName");
		if(jstime){
			$("#user").val(jstime);
			$("#userName").val(userName);
			$("#userSearch").hide();
			$("#whsGrid").show();
			initGrid()
			search2()
		}else{
			alert("请选择用户")
		}
	})
	$(".return").click(function(){
		$("#userSearch").show();
		$("#whsGrid").hide();
	})
})

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
				url: url + "/mis/system/misUser/uploadFileAddDb",
				data: data,
				dataType: "json",
				cache: false, //上传文件无需缓存
				processData: false, //用于对data参数进行序列化处理 这里必须false
				contentType: false, //必须
				success: function(data) {
					alert(data.respHead.message)
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
	var accNum = $(".userId").val();
	var userName = $(".userName").val();
	var deptId = $("#deptId").val();
	var roleId = $(".roleId").val()

	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"accNum": accNum,
			"userName": userName,
			"deptId": deptId,
			"roleId":roleId,
		}
	}
	var Data = JSON.stringify(savedata);
	$.ajax({
		url: url + '/mis/system/misUser/queryListExport',
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
	var str = '账号,密码,账套,用户名称,部门编号,部门名称,角色编号,联系电话,企业邮箱,用户创建日期,仓库编号,仓库名称,\n';

	for(let i = 0; i < JSONData.length; i++) {
		var result = '';
		for(let item in JSONData[i]) {
			if(JSONData[i][item] == null) {
				JSONData[i][item] = "";
			}
			str += `${JSONData[i][item] + '\t'},`;
		}
		str += '\n';
	}
	var blob = new Blob([str], {
		type: "text/plain;charset=utf-8"
	});
	//解决中文乱码问题
	blob = new Blob([String.fromCharCode(0xFEFF), blob], {
		type: blob.type
	});
	object_url = window.URL.createObjectURL(blob);
	var link = document.createElement("a");
	link.href = object_url;
	link.download = "用户档案.csv";
	document.body.appendChild(link);
	link.click();
	document.body.removeChild(link);
}