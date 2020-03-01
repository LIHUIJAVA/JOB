//凭证列表
var page = 1;
var rowNum;

//凭证类别下拉选择框
$(function() {
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"pageNo": 1,
			"pageSize": 9999999
		}
	};
	var postData = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/account/vouchCateDoc/selectVouchCateDoc',
		type: 'post',
		data: postData,
		dataType: 'json',
		async: false,
		contentType: 'application/json;charset=utf-8',
		success: function(data) {
			var list = data.respBody.list;

			var valtnMode = $("#comnVouchTyp")
			for(var i = 0; i < list.length; i++) {
				addEle(valtnMode, list[i].vouchCateNm, list[i].vouchCateWor);
			}

			function addEle(ele, value, val) {
				var optionStr = "";
				optionStr = '<option value=' + val + '>' + value + "</option>";
				ele.append(optionStr);
			}

			function removeEle(ele) {
				ele.find("option").remove();
				var optionStar = "<option value=" + "请选择" + ">" + "请选择" + "</option>";
				ele.append(optionStar);
			}

		},
		error: function() {
			alert("error");
		}, //错误执行方法
	})
})
$(function() {
	pageInit();
})

//表格初始化
function pageInit() {
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div> ");
	$("#mengban1").addClass("zhezhao");
	jQuery("#jqGrids").jqGrid({
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		url: '../../assets/js/json/order.json',
		colNames: ['凭证日期', '凭证类型', '凭证编码','编码', '凭证摘要', '制单人',
			'审核人', '记账人','是否导出'
		], //jqGrid的列显示名字
		colModel: [{
				name: "ctime",
				align: "center",
				sortrable: true,
				sorttype: "date"
			},
			{
				name: "vouchCateWor",
				align: "center",
			},
			{
				name: "tabNum",
				align: "center",
			},
			{
				name: "comnVouchId",
				align: "center",
				hidden:true
			},
			{
				name: 'comnVouchComnt',
				align: "center",
			},
			{
				name: "userName",
				align: "center",
			},
			{
				name: 'chkr',
				align: "center",
			},
			{
				name: 'bookOkAcc',
				align: "center",
			},
			{
				name: 'imported',
				align: "center",
			}
		],
		height: 380,
		autowidth: true,
		autoScroll: true,
		shrinkToFit: false,
		viewrecords: true,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 50000,
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		multiselect: true, //复选框
//		multiboxonly: true,
		caption: "凭证列表", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#jqGrids").getGridParam('records'); //获取返回的记录数
			page = $("#jqGrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#jqGrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			loadLocalData();
		},
		ondblClickRow: function(rowid) {
			order(rowid)
		},
	})
}
$(function() {
	$(".search").click(function() {
		loadLocalData(page)
	})
})

function loadLocalData() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var tabNum = $("#tabNum").val();
	var vouchCateWor = $("select").val();
	var userName = $("#user").val();
	var start = $(".start").val();
	var end = $(".end").val();
	var imported = $("#imported").val();
	var showData = {
		"reqHead": reqhead,
		"reqBody": {
			"tabNum": tabNum,
			"vouchCateWor": vouchCateWor,
			"userName": userName,
			"start": start,
			"end": end,
			"imported":imported
		}
	};
	var postData = JSON.stringify(showData);
	$.ajax({
		type: "post",
		url: url + "/mis/account/vouchTab/selectVouchTab", //列表
		async: true,
		data: postData,
		dataType: 'json',
		contentType: 'application/json',
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
		success: function(data) {
			var list = data.respBody.list;
			for(var i = 0;i<list.length;i++){
				if(list[i].imported==1){
					list[i].imported="是"
				}else if(list[i].imported==0){
					list[i].imported="否"
				}
			}
			$("#jqGrids").jqGrid("clearGridData");
			$("#jqGrids").jqGrid("setGridParam", {
				datatype: 'local',
				data: list, //newData是符合格式要求的重新加载的数据
				page: 1 //哪一页的值
			}).trigger("reloadGrid");
		},
		error: function() {
			alert("展示失败")
		}
	})
}

function order(rowid) {
	//获得行数据
	var rowDatas = $("#jqGrids").jqGrid('getRowData', rowid);
	if(rowDatas.comnVouchId){
		localStorage.setItem("comnVouchId", rowDatas.comnVouchId);
		window.open("../../Components/account/voucher.html");
	}
}

//删除行
$(function() {
	$(".delOrder").click(function() {
		var ids = $("#jqGrids").jqGrid('getGridParam', 'selarrrow'); //获取选中=行id
		var rowData = []
		for(i = 0; i < ids.length; i++) {
			var gr = $("#jqGrids").getGridParam('selrow');
			//选中行的id
			var jstime = $("#jqGrids").getCell(ids[i], "comnVouchId");
			rowData[i] = jstime
		}
		var rowDatas = rowData.toString();
		var deleteAjax = {
			"reqHead": reqhead,
			"reqBody": {
				"comnVouchId": rowDatas
			}
		}
		var deleteData = JSON.stringify(deleteAjax)
		if(ids.length == 0) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + "/mis/account/vouchTab/voucher/del",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(data) {
					alert(data.respHead.message);
					if(data.respHead.isSuccess==true){
						loadLocalData()
					}
				},
				error: function(data) {
					alert("删除失败")
				}
			});

		}
	})
})

//导入
$(function() {
	$(".importExcel").click(function() {
		var files = $("#FileUpload").val()
		var fileObj = document.getElementById("FileUpload").files[0];
		var formFile = new FormData();
		formFile.append("action", "UploadVMKImagePath");
		formFile.append("file", fileObj);
		var deleteAjax = {
			"reqHead": reqhead,
			"reqBody": {
				formFile
			}
		}
		var data = JSON.stringify(deleteAjax)
		if(files != "") {
			$.ajax({
				type: 'post',
				url: url + "/mis/account/vouchTab/voucher/import",
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
	var tabNum = $("#tabNum").val();
	var vouchCateWor = $("select").val();
	var userName = $("#user").val();
	var start = $(".start").val();
	var end = $(".end").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"tabNum": tabNum,
			"vouchCateWor": vouchCateWor,
			"userName": userName,
			"start": start,
			"end": end,
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/account/vouchTab/voucher/export',
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
			var list = data.respBody.list;
			var execlName = '凭证列表'
			ExportData(list, execlName)
		},
		error: function() {
			alert("error")
		}
	})

})
