$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div> ");
	$("#mengban").addClass("zhezhao");
})
var count;
var pages;
var page = 1;
var rowNum;

//表格初始化
$(function() {
	jQuery("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['单据号', '单据日期', '仓库编码', '供应商', '收发类别',  '存货编码', '存货名称',
		'规格型号', '计量单位', '被调整单据号', '金额', '记账人', '箱规', '批次', '出入库','表头备注','表体备注'], //jqGrid的列显示名字
		colModel: [{
				name: "formNum",
				align: "center",
				editable: true,
			},
			{
				name: "formTm",
				align: "center",
				editable: true,
			},
			{
				name: "whsEncd", //科目名称
				align: "center",
				editable: true,
			},
			{
				name: 'provrNm',
				align: "center",
				editable: true,
			},
			{
				name: 'recvSendCateNm',
				align: "center",
				editable: true,
			},
			{
				name: "invtyEncd",
				align: "center",
				editable: true,
			},
			{
				name: "invtyNm",
				align: "center",
				editable: true,
			},
			{
				name: "spcModel", //科目名称
				align: "center",
				editable: true,
			},
			{
				name: 'measrCorpNm',
				align: "center",
				editable: true,
			},
			{
				name: 'isNtCashSubj',
				align: "center",
				editable: true,
			},
			{
				name: "subjTyp",
				align: "center",
				editable: true,
			},
			{
				name: 'bookEntryPers',
				align: "center",
				editable: true,
			},
			{
				name: 'box',
				align: "center",
				editable: true,
			},
			{
				name: 'batNum',
				align: "center",
				editable: true,
			},
			{
				name: 'outIntoWhsInd',
				align: "center",
				editable: true,
			},
			{
				name: "memo",
				align: "center",
				editable: true,
			},
			{
				name: "memos",
				align: "center",
				editable: true,
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
		rowNum: 500,
		rowList: [500, 1000, 3000, 5000],
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		multiselect: true, //复选框
//		multiboxonly: true,
		caption: "出入库调整单列表", //表格的标题名字
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
			search()
		},
		ondblClickRow: function(rowid) {
			order(rowid);
		},
		footerrow: true,
		gridComplete: function() { 
			var subjTyp = 0;
			var ids = $("#jqGrids").getDataIDs();
			for(var i = 0; i < ids.length; i++) {
				subjTyp += parseFloat($("#jqGrids").getCell(ids[i],"subjTyp"));
			};
			if(isNaN(subjTyp)) {
				subjTyp = 0
			}
			subjTyp = precision(subjTyp,2)
			$("#jqGrids").footerData('set', { 
				"formNum": "本页合计",
				subjTyp: subjTyp
				
			}          );    
		},
	})
})

function order(rowid) {
	//获得行数据
	var rowDatas = $("#jqGrids").jqGrid('getRowData', rowid);
	localStorage.setItem("formNum", rowDatas.formNum);
	if(rowDatas.outIntoWhsInd=="出库"){
		window.open("../../Components/account/outWhsAdjSngl.html?1");
	}else if(rowDatas.outIntoWhsInd=="入库"){
		window.open("../../Components/account/intoWhsAdjSngl.html?1");
	}
}

$(function() {
	$(".find").click(function() {
		search()
	})
})

function search() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var formNum = $('.formNum').val();
	var whsEncd = $("#whsEncd").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var provrId = $("#provrId").val();
	var formTm1 = $(".formTm1").val();
	var formTm2 = $(".formTm2").val();
	var invtyEncd = $("#invtyEncd").val();
	var custId = $("#custId").val();
	var memo = $("#memo").val();
	
	var showData = {
		"reqHead": reqhead,
		"reqBody": {
			"formNum":formNum,
			"whsEncd":whsEncd,
			"provrId":provrId,
			"formTm1":formTm1,
			"invtyClsEncd": invtyClsEncd,
			"formTm2":formTm2,
			"custId":custId,
			"invtyEncd":invtyEncd,
			"memo":memo,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var postData = JSON.stringify(showData);
	$.ajax({
		type: "post",
		url: url + "/mis/account/outIntoWhsAdjSngl/selectOutIntoWhsAdjSngl", //列表
		async: true,
		data: postData,
		dataType: 'json',
		contentType: 'application/json',
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
			var arr = [];
			for(var i = 0; i < list.length; i++) {
				if(list[i].outIntoList != null) {
					var entrs = list[i].outIntoList
					for(var j = 0; j < entrs.length; j++) {
						(function(j) {
							var newObj = cloneObj(data.respBody.list);
							for(var k in entrs[j]) {
								newObj[i][k] = entrs[j][k]
							}
							arr.push(newObj[i])
						})(j)
					}
				}
			}
			for(var i = 0; i < arr.length; i++) {
				delete arr[i].outIntoList
				if(arr[i].outIntoWhsInd == 1) {
					arr[i].outIntoWhsInd = "入库"
				} else if(arr[i].outIntoWhsInd == 0) {
					arr[i].outIntoWhsInd = "出库"
				}
			}
			var mydata = {};
			mydata.rows = arr;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#jqGrids").jqGrid("clearGridData");
			$("#jqGrids").jqGrid("setGridParam", {
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
			alert("展示失败")
		}
	})

}

//删除
$(function() {
	$(".delOrder").click(function() {
		var ids = $("#jqGrids").jqGrid('getGridParam', 'selarrrow'); //获取选中=行id
		var rowData = []
		for(i = 0; i < ids.length; i++) {
			var gr = $("#jqGrids").getGridParam('selrow');
			//选中行的id
			var jstime = $("#jqGrids").getCell(ids[i], "formNum");
			rowData[i] = jstime
		}
		var rowDatas = rowData.toString();
		var deleteAjax = {
			"reqHead": reqhead,
			"reqBody": {
				"formNum": rowDatas
			}
		}
		var deleteData = JSON.stringify(deleteAjax)
		if(ids.length == 0) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + "/mis/account/outIntoWhsAdjSngl/deleteOutIntoWhsAdjSngl",
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
				url: url + "/mis/account/outIntoWhsAdjSngl/uploadFileAddDb",
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
	var formNum = $('.formNum').val();
	var whsEncd = $("#whsEncd").val();
	var provrId = $("#provrId").val();
	var formTm1 = $(".formTm1").val();
	var formTm2 = $(".formTm2").val();
	var invtyEncd = $("#invtyEncd").val();
	var custId = $("custId").val();
	var memo = $("memo").val();
	var showData = {
		"reqHead": reqhead,
		"reqBody": {
			"formNum":formNum,
			"whsEncd":whsEncd,
			"provrId":provrId,
			"formTm1":formTm1,
			"formTm2":formTm2,
			"custId":custId,
			"invtyEncd":invtyEncd,
			"memo":memo
		}
	};
	var Data = JSON.stringify(showData);
	$.ajax({
		url: url + '/mis/account/outIntoWhsAdjSngl/selectOutIntoWhsAdjSnglPrint',
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
			var execlName = '出入库调整单列表'
			ExportData(list, execlName)
		},
		error: function() {
			alert("error")
		}
	})
})


function ntChk(x) {
	//获得选中行的行号
	var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
	//对象数组
	var rowData = [];
	for(var i = 0; i < ids.length; i++) {
		var obj = {}; //对象
		//选中行的id
		//把选中行的i添加到数据对象中
		obj.formNum = $("#jqGrids").getCell(ids[i], "formNum").toString();
		//建一个数组，把单个对象添加到数组中
		rowData[i] = obj;
	}
	var result = [];
	var obj = {};
	for(var i = 0; i < rowData.length; i++) {
		if(!obj[rowData[i].formNum]) {
			result.push(rowData[i]);
			obj[rowData[i].formNum] = true;
		}
	}
	if(result.length == 0) {
		alert("请选择单据!")
	} else {
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				"adjFormNum": result
			}
		};
		var Data = JSON.stringify(data);
		$.ajax({
			url: url +x,
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
			//结束加载动画
			complete: function() {
				$(".zhezhao").css("display", "none");
				$("#loader").css("display", "none");
			},
			success: function(data) {
				alert(data.respHead.message)
				if(data.respHead.isSuccess == true) {
					search()
				}
			},
			error: function(error) {
				alert(error.responseText)
			}
		})
	}

}

$(function() {
	//记账
	$(".account").click(function() {
		var x = "/mis/account/form/outIntoAdj/formBook"
		ntChk(x)
	})

	//恢复
	$(".recover").click(function() {
		var x = "/mis/account/form/outIntoAdj/backFormBook"
		ntChk(x)
	})
})
