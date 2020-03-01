//存货科目
var mType=0;
var newId = '';
var add = 0;
var addId, addName

var count;
var pages;
var page = 1;
var rowNum;

$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$("#mengban").addClass("zhezhao");
	pageInit();
	loadLocalData(page)
})
/*表格初始化*/
function pageInit() {
//	allHeight();
	$("#jqgrids").jqGrid({
		datatype: "local",
		colNames: ["ordrNum", '存货分类编码', '存货分类名称', '存货科目编码', '存货科目名称', '委托代销科目编码', '委托代销科目名称', '备注'], //jqGrid的列显示名字
		colModel: [{
				name: 'ordrNum',
				editable: true,
				align: 'center',
				sortable: true,
				hidden: true
			},
			{
				name: 'invtyBigClsEncd',
				editable: true,
				align: 'center',
				sortable: true,
			},
			{
				name: 'invtyClsNm',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'invtySubjId',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'invtySubjNm',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'entrsAgnSubjId',
				editable: true,
				align: 'center',
				sortable: false

			},
			{
				name: 'entrsAgnSubjNm',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'memo',
				editable: true,
				align: 'center',
				sortable: false
			},
		],
		viewrecords: true,
		rownumbers: true,
		autowidth: true,
		height: 400,
		autoScroll: true,
		shrinkToFit: false,
		loadonce: true,
		forceFit: true,
		rowNum: 500, //一页写多少条
		rowList: [500, 1000, 3000, 5000],
		pager: '#jqgridPager', //表格页脚的占位符(一般是div)的id
		multiselect: true, //复选框
		multiboxonly: true,
		cellsubmit: "clientArray",
		caption: "存货科目", //表格的标题名字	
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

function loadLocalData() {
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
		url: url + '/mis/account/invtySubjSetTab/selectInvtySubjSetTab',
		async: true,
		data: saveData,
		dataType: 'json',
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
		error: function() {
			alert("error")
		}
	})
}

function grr(rowid) {
	$("#" + rowid + "_invtyClsNm").attr("readonly", "readonly");
	$("#" + rowid + "_invtySubjNm").attr("readonly", "readonly");
	$("#" + rowid + "_entrsAgnSubjNm").attr("readonly", "readonly");
	$("#" + rowid + "_invtyBigClsEncd").attr("readonly", "readonly");
	$("#" + rowid + "_invtySubjId").attr("readonly", "readonly");
	$("#" + rowid + "_entrsAgnSubjId").attr("readonly", "readonly");

	/*打开弹框*/
	$("#" + rowid + "_invtyBigClsEncd").bind("dblclick", function() {
		add = 1;
		$("#box").show();
		$("#big_wrap").show();
		$(".click_span").click(function() {
			$(".account_List").hide();
			addId = $(this).children().first().text();
			addName = $(this).children().first().next().next().text();
		})
	});
	$("#" + rowid + "_invtySubjId").bind("dblclick", function() {
		add = 2;
		$(".account_List").css("opacity", 1);
		$(".account_List").show();
		$("#purchaseOrder").hide();
	});
	$("#" + rowid + "_entrsAgnSubjId").bind("dblclick", function() {
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
			$("#" + gr + "_invtyBigClsEncd").val(addId);
			$("#" + gr + "_invtyClsNm").val(addName);
		} else if(add == 2) {
			var idd = $("#jqGrids").jqGrid('getGridParam', 'selrow');
			var rowData = $("#jqGrids").jqGrid('getRowData', idd);
			addId = rowData.subjId;
			addName = rowData.subjNm;
			$("#" + gr + "_invtySubjId").val(addId);
			$("#" + gr + "_invtySubjNm").val(addName);
		} else if(add == 3) {
			var idd = $("#jqGrids").jqGrid('getGridParam', 'selrow');
			var rowData = $("#jqGrids").jqGrid('getRowData', idd);
			addId = rowData.subjId;
			addName = rowData.subjNm;
			$("#" + gr + "_entrsAgnSubjId").val(addId);
			$("#" + gr + "_entrsAgnSubjNm").val(addName);
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
	$(".saveOrder").click(function() {
		$(".addOrder").removeClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
		if(mType == 1) {
			SaveNewData();
			mType = 0
		} else if(mType == 2) {
			SaveModifyData();
			mType = 0
		}
	})
})

// 点击增行按钮，执行的操作
$(function() {
	$('.addOrder').click(function() {
		mType = 1;
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
			invtyBigClsEncd: '',
			invtyClsNm: '',
			invtySubjId: '',
			invtySubjNm: '',
			entrsAgnSubjId: '',
			entrsAgnSubjNm: '',
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
	var invtyBigClsEncd = $("#" + newId + '_invtyBigClsEncd').val();
	var invtySubjId = $("#" + newId + '_invtySubjId').val();
	var entrsAgnSubjId = $("#" + newId + '_entrsAgnSubjId').val();
	var invtyClsNm = $("#" + newId + '_invtyClsNm').val();
	var invtySubjNm = $("#" + newId + '_invtySubjNm').val();
	var entrsAgnSubjNm = $("#" + newId + '_entrsAgnSubjNm').val();

	$("button").removeClass("gray");
	$('button').attr('disabled', false);
	$(".gray").attr('disabled', true)
	//	$("#box").show();
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			'invtyBigClsEncd': invtyBigClsEncd,
			'invtySubjId': invtySubjId,
			'entrsAgnSubjId': entrsAgnSubjId,
			'invtyClsNm': invtyClsNm,
			'invtySubjNm': invtySubjNm,
			'entrsAgnSubjNm': entrsAgnSubjNm,
		}

	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		url: url + '/mis/account/invtySubjSetTab/insertInvtySubjSetTab ',
		type: 'post',
		data: saveData,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		success: function(data) {
			alert(data.respHead.message)
			if(data.respHead.isSuccess == true) {
				loadLocalData1(page);
			}
		},
		error: function(data) {
			alert("error")
		} //错误执行方法
	})
	$("#jqgrids").trigger("reloadGrid");
}

//修改后的保存
function SaveModifyData() {
	var rowDatas = [];
	var gr = $("#jqgrids").jqGrid('getGridParam', 'selarrrow');
	for(var i = 0; i < gr.length; i++) {
		var obj = {};
		obj.ordrNum = $("#" + gr[i] + '_ordrNum').val();
		obj.invtyBigClsEncd = $("#" + gr[i] + '_invtyBigClsEncd').val()
		obj.invtySubjId = $("#" + gr[i] + '_invtySubjId').val()
		obj.entrsAgnSubjId = $("#" + gr[i] + '_entrsAgnSubjId').val();
		obj.invtyClsNm = $("#" + gr[i] + '_invtyClsNm').val();
		obj.invtySubjNm = $("#" + gr[i] + '_invtySubjNm').val();
		obj.entrsAgnSubjNm = $("#" + gr[i] + '_entrsAgnSubjNm').val();
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
		url: url + '/mis/account/invtySubjSetTab/updateInvtySubjSetTab',
		type: 'post',
		data: saveData,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		success: function(data) {
			alert(data.respHead.message)
			if(data.respHead.isSuccess == true) {
				loadLocalData1(page);
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
			var jstime = $("#jqgrids").getCell(gr[i], "ordrNum");
			rowDatas[i] = jstime
		}
		var rowData = rowDatas.toString();

		if(rowData.length == 0) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			var deldata = {
				"reqHead": reqhead,
				"reqBody": {
					"ordrNum": rowData
				}
			};
			var delData = JSON.stringify(deldata)
			$.ajax({
				type: "post",
				url: url + "/mis/account/invtySubjSetTab/deleteInvtySubjSetTabList",
				async: true,
				data: delData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(data) {
					alert(data.respHead.message);
					if(data.respHead.isSuccess == true) {
						window.location.reload();
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
				url: url + "/mis/account/invtySubjSetTab/uploadFileAddDb",
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
		url: url + '/mis/account/invtySubjSetTab/selectInvtySubjSetTabPrint',
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
			var execlName = '存货科目'
			ExportData(list, execlName)
		},
		error: function() {
			alert("error")
		}
	})
})