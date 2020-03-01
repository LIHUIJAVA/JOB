//应付科目
var mType = 0;
var newId = '';
var add = 0;
var addId, addName

var count;
var pages;
var page = 1;
var rowNum = 10;

$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$("#mengban").addClass("zhezhao");
	pageInit();
	loadLocalData(page)
})
/*表格初始化*/
function pageInit() {
//	allHeight()
	$("#jqgrids").jqGrid({
		datatype: "local",
		colNames: ['序号', '供应商分类编码', '供应商分类名称', '应付科目编码', '应付科目名称', '预付科目编码', '预付科目名称'], //jqGrid的列显示名字
		colModel: [{
				name: 'incrsId',
				editable: true,
				hidden: false,
				align: 'center',
				sortable: true,
				hidden: true
			},
			{
				name: 'provrClsEncd',
				editable: true,
				align: 'center',
				sortable: true,
			},
			{
				name: 'provrClsNm',
				editable: true,
				align: 'center',
				sortable: true
			},
			{
				name: 'payblSubjEncd',
				editable: true,
				align: 'center',
				sortable: true
			},
			{
				name: 'payblSubjNm',
				editable: true,
				align: 'center',
				sortable: true
			},
			{
				name: 'prepySubjEncd',
				editable: true,
				align: 'center',
				sortable: true

			},
			{
				name: 'prepySubjNm',
				editable: true,
				align: 'center',
				sortable: true
			},
		],
		viewrecords: true,
		rownumbers: true,
		loadonce: true,
		height: 400,
		forceFit: true,
		rowNum: 500,
		rowList: [500, 1000, 3000, 5000],
		pager: '#jqgridPager', //表格页脚的占位符(一般是div)的id
		multiselect: true, //复选框
		multiboxonly: true,
		caption: "应付科目", //表格的标题名字	
		viewrecords: true, //观察记录
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
		ondblClickRow: function(rowid) {
			if(mType == 1) {
				mType = 1
			} else {
				mType = 2;
				$('#jqgrids').editRow(rowid, true);
				grr(rowid)
			}
		},
	})
}

function loadLocalData(page) {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	$("#box").hide();
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var myData = {};
	var saveData = JSON.stringify(savedata)
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/account/invtyPayblSubj/selectInvtyPayblSubj',
		async: true,
		data: saveData,
		dataType: 'json',
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
		error: function() {
			alert("error")
		}
	})
}

function grr(rowid) {
	$("#" + rowid + "_provrClsNm").attr("readonly", "readonly");
	$("#" + rowid + "_payblSubjNm").attr("readonly", "readonly");
	$("#" + rowid + "_prepySubjNm").attr("readonly", "readonly");
	$("#" + rowid + "_provrClsEncd").attr("readonly", "readonly");
	$("#" + rowid + "_payblSubjEncd").attr("readonly", "readonly");
	$("#" + rowid + "_prepySubjEncd").attr("readonly", "readonly");

	/*打开弹框*/
	$("#" + rowid + "_provrClsEncd").bind("dblclick", function() {
		add = 1;
		$("#box").show();
		$("#big_wrap").show();
		$(".click_span").click(function() {
			$(".account_List").hide();
			addId = $(this).children().first().text();
			addName = $(this).children().first().next().next().text();
		})
	});
	$("#" + rowid + "_payblSubjEncd").bind("dblclick", function() {
		add = 2;
		$(".account_List").css("opacity", 1);
		$(".account_List").show();
		$("#purchaseOrder").hide();
	});
	$("#" + rowid + "_prepySubjEncd").bind("dblclick", function() {
		add = 3;
		$(".account_List").css("opacity", 1);
		$(".account_List").show();
		$("#purchaseOrder").hide();
	});
}

$(function() {
	$(".sure").click(function() {
		$("#purchaseOrder").show();
		$(".account_List").hide();
		$("#box").hide();
		$("#big_wrap").hide();
		var gr;
		if(mType == 1) {
			gr = newId
		} else {
			gr = $("#jqgrids").jqGrid('getGridParam', 'selrow');
		}
		if(add == 1) {
			$("#" + gr + "_provrClsEncd").val(addId);
			$("#" + gr + "_provrClsNm").val(addName);
		} else if(add == 2) {
			var idd = $("#jqGrids").jqGrid('getGridParam', 'selrow');
			var rowData = $("#jqGrids").jqGrid('getRowData', idd);
			addId = rowData.subjId;
			addName = rowData.subjNm;
			$("#" + gr + "_payblSubjEncd").val(addId);
			$("#" + gr + "_payblSubjNm").val(addName);
		} else if(add == 3) {
			var idd = $("#jqGrids").jqGrid('getGridParam', 'selrow');
			var rowData = $("#jqGrids").jqGrid('getRowData', idd);
			addId = rowData.subjId;
			addName = rowData.subjNm;
			$("#" + gr + "_prepySubjEncd").val(addId);
			$("#" + gr + "_prepySubjNm").val(addName);
		}
	})
	$(".cancel").click(function() {
		$("#purchaseOrder").show();
		$(".account_List").hide();
		$("#box").hide();
		$("#big_wrap").hide();
	})
})

// 点击保存，传送数据给后台
$(function() {
	$(".paybl_saveOrder").click(function() {
		$(".addOrder").removeClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
		if(mType == 1) {
			SaveNewData();
		} else if(mType == 2) {
			SaveModifyData();
		}
	})
})

// 点击增行按钮，执行的操作
$(function() {
	$('.addOrder').click(function() {
		mType = 1
		$(".addOrder").addClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
		//拿到Gride中所有主键id的值
		var gr = $("#jqgrids").jqGrid('getDataIDs');
		//获得当前最大行号（数据编码）
		var rowid = Math.max.apply(Math, gr);
		//获得新添加行的行号（数据编码）
		newrowid = rowid + 1;
		newId = newrowid
		var dataRow = {
			provrClsEncd: '',
			payblSubjEncd: '',
			prepySubjEncd: '',
			provrClsNm: '',
			payblSubjNm: '',
			prepySubjNm: '',
		};

		//将新添加的行插入到第一列
		$("#jqgrids").jqGrid("addRowData", newrowid, dataRow, "first");
		//设置grid单元格可编辑
		$('#jqgrids').jqGrid('editRow', newrowid, true);

		grr(newrowid)
	});
})

//保存
function SaveNewData() {
	var gr = $("#jqgrids").jqGrid('getDataIDs');
	//获得当前最大行号（数据编码）
	var rowid = Math.max.apply(Math, gr);
	//获得新添加行的行号（数据编码）
	newrowid = rowid + 1;
	var provrClsEncd = $("#" + newId + '_provrClsEncd').val();
	var payblSubjEncd = $("#" + newId + '_payblSubjEncd').val();
	var prepySubjEncd = $("#" + newId + '_prepySubjEncd').val();
	var provrClsNm = $("#" + newId + '_provrClsNm').val();
	var payblSubjNm = $("#" + newId + '_payblSubjNm').val();
	var prepySubjNm = $("#" + newId + '_prepySubjNm').val();

	$("button").removeClass("gray");
	$('button').attr('disabled', false);
	$(".gray").attr('disabled', true)
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			'provrClsEncd': provrClsEncd,
			'payblSubjEncd': payblSubjEncd,
			'prepySubjEncd': prepySubjEncd,
			'provrClsNm': provrClsNm,
			'payblSubjNm': payblSubjNm,
			'prepySubjNm': prepySubjNm,
		}

	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		url: url + '/mis/account/invtyPayblSubj/insertInvtyPayblSubj',
		type: 'post',
		data: saveData,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		success: function(data) {
			alert(data.respHead.message)
			if(data.respHead.isSuccess == true) {
				loadLocalData(page);
			}
		},
		error: function(data) {
			//alert(data.respHead.message)
			alert("error")
		} //错误执行方法
	})
}

//修改后的保存
function SaveModifyData() {
	var rowDatas = [];
	var gr = $("#jqgrids").jqGrid('getGridParam', 'selarrrow');
	for(var i = 0; i < gr.length; i++) {
		var obj = {};
		obj.incrsId = $("#" + gr[i] + '_incrsId').val()
		obj.provrClsEncd = $("#" + gr[i] + '_provrClsEncd').val()
		obj.payblSubjEncd = $("#" + gr[i] + '_payblSubjEncd').val()
		obj.prepySubjEncd = $("#" + gr[i] + '_prepySubjEncd').val();
		obj.provrClsNm = $("#" + gr[i] + '_provrClsNm').val();
		obj.payblSubjNm = $("#" + gr[i] + '_payblSubjNm').val();
		obj.prepySubjNm = $("#" + gr[i] + '_prepySubjNm').val();
		rowDatas[i] = obj;
	}
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"list": rowDatas,
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		url: url + '/mis/account/invtyPayblSubj/updateInvtyPayblSubj',
		type: 'post',
		data: saveData,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		success: function(data) {
			alert(data.respHead.message)
			if(data.respHead.isSuccess == true) {
				loadLocalData(page);
			}
		},
		error: function(data) {
			alert(error)
		}
	})

}

/*点击删除行执行的操作*/
$(function() {
	$(".delOrder_a").click(function() {
		var rowDatas = [];
		var gr = $("#jqgrids").jqGrid('getGridParam', 'selarrrow');
		for(i = 0; i < gr.length; i++) {
			//选中行的数据
			var jstime = $("#jqgrids").getCell(gr[i], "incrsId");
			rowDatas[i] = jstime
		}
		var rowData = rowDatas.toString();
		if(rowData.length == 0) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			var deldata = {
				"reqHead": reqhead,
				"reqBody": {
					"incrsId": rowData
				}
			};
			var delData = JSON.stringify(deldata)
			$.ajax({
				type: "post",
				url: url + "/mis/account/invtyPayblSubj/deleteInvtyPayblSubjList",
				async: true,
				data: delData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(data) {
					alert(data.respHead.message)
					if(data.respHead.isSuccess == true) {
						loadLocalData(page);
					}
				},
				error: function() {
					alert("删除失败")
				}
			})
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
				url: url + "/mis/account/invtyPayblSubj/uploadFileAddDb",
				data: data,
				dataType: "json",
				cache: false, //上传文件无需缓存
				processData: false, //用于对data参数进行序列化处理 这里必须false
				contentType: false, //必须
				success: function(data) {
					alert(data.respHead.message)
					if(data.respHead.isSuccess == true) {
						window.location.reload();
					}
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
	var data = {
		"reqHead": reqhead,
		"reqBody": {}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/account/invtyPayblSubj/selectInvtyPayblSubjPrint',
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
			var execlName = '应付科目'
			ExportData(list, execlName)
		},
		error: function() {
			alert("error")
		}
	})
})
