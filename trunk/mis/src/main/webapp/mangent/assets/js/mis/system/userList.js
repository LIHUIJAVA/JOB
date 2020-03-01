//页面初始化
$(function() {
	pageInit7();
});
var count;
var pages;
var page = 1;
var rowNum;

function pageInit7() {
	allHeight()
	jQuery("#user_jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['用户号', '用户名', '所属部门编码', '所属部门名称', '角色编码',
			'角色名称', '手机号码', '企业邮箱', '用户创建日期'
		], //jqGrid的列显示名字
		colModel: [{
				name: 'accNum',
				index: 'id',
				editable: true,
				align: 'center',
				width: 100
			},
			{
				name: 'userName',
				index: 'invdate',
				editable: true,
				align: 'center',
				width: 100
			},
			{
				name: 'depId',
				index: 'invdate',
				align: 'center',
				width: 100
			},
			{
				name: 'depName',
				index: 'invdate',
				editable: true,
				edittype: "select",
				align: 'center',
				width: 100
			},
			{
				name: 'roleId',
				index: 'invdate',
				align: 'center',
				width: 100
			},
			{
				name: 'roleName',
				index: 'invdate',
				align: 'center',
				width: 100,
				editable: true,
			},
			{
				name: 'phoneNo',
				index: 'invdate',
				editable: true,
				align: 'center',
				width: 100
			},
			{
				name: 'email',
				index: 'invdate',
				editable: true,
				align: 'center',
				width: 100
			},
			{
				name: 'createDate',
				index: 'invdate',
				editable: true,
				width: 100,
				align: 'center',
				editable: false
			}
		],
		autowidth: true,
		viewrecords: true,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		height:height,
		rowNum: 100,
		rowList: [100, 300, 500],
		pager: '#user_jqGridPager', //表格页脚的占位符(一般是div)的id
		caption: "用户档案", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#user_jqgrids").getGridParam('records'); //获取返回的记录数
			page = $("#user_jqgrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#user_jqgrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			loadLocalData()
		},
	});
}

//条件查询
$(function() {
	$('#find').click(function() {
		loadLocalData()
	})
})

function loadLocalData() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var accNum = $("input[name='accNum']").val();
	var userName = $("input[name='userName']").val();
	var roleId = $("input[name='roleId1']").val();
	var roleName = $("input[name='roleName']").val();
	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"accNum": accNum,
			"userName": userName,
			"roleId": roleId,
			"roleName": roleName,
			"pageNo": page,
			"pageSize": rowNum,
		}
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + "/mis/system/misUser/queryList",
		data: postD2,
		dataType: 'json',
		async: true,
		success: function(data) {
			var mydata = {};
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#user_jqgrids").jqGrid("clearGridData");
			$("#user_jqgrids").jqGrid("setGridParam", {
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
			alert("查询失败")
		}
	});

}

$(document).on('click', '.true', function() {
	//获得行号
	var gr = $("#user_jqgrids").jqGrid('getGridParam', 'selrow');
	if(gr == null) {
		alert("未选择")
	} else {
		//获得行数据
		var rowDatas = $("#user_jqgrids").jqGrid('getRowData', gr);

		window.parent.opener.document.getElementById("user").value = rowDatas.accNum;
		window.parent.opener.document.getElementById("userName").value = rowDatas.userName;
		var deptId = window.parent.opener.document.getElementById("deptId")
		if(deptId&&deptId.size>0){
			window.parent.opener.document.getElementById("deptId").value = rowDatas.depId;
			window.parent.opener.document.getElementById("deptName").value = rowDatas.depName;
		}
		
		//业务员编码
		localStorage.setItem("userName", rowDatas.userName);
		window.close()
	}
})
$(document).on('click', '.false', function() {
	window.close();
	if(rowDatas.userName) {
		rowDatas.userName = rowDatas.userName;
	} else {
		window.parent.opener.document.getElementById("user").value = '';
		window.parent.opener.document.getElementById("userName").value = '';
	}
})