//对方科目
var mType = 0;
var add = 0;
var newId = '';
var addId, addName;

var count;
var pages;
var page = 1;
var rowNum;

$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$("#mengban").addClass("zhezhao");
	initPage()
	loadLocalData1()
})
/*表格初始化*/
function initPage() {
	//	allHeight();
	$("#jqgrids").jqGrid({
		datatype: 'local',
		colNames: ['序号', '收发类别编码', '收发类别名称', '存货分类编码', '存货分类名称', '对方科目编码', '对方科目名称',
			'暂估科目编码', '暂估科目名称', '备注'
		], //jqGrid的列显示名字
		colModel: [{
				name: 'ordrNum',
				hidden: true,
				editable: true,
				align: 'center',
				sortable: true,
			},
			{
				name: 'recvSendCateId', //收发类别编码
				editable: true,
				align: 'center',
				sortable: true
			},
			{
				name: 'recvSendCateNm', //收发类别名称
				editable: true,
				align: 'center',
				sortable: true
			},
			{
				name: 'invtyBigClsEncd', //存货分类编码
				editable: true,
				align: 'center',
				sortable: true
			},
			{
				name: 'invtyBigClsNm', //存货分类名称
				editable: true,
				align: 'center',
				sortable: true
			},
			{
				name: 'cntPtySubjId', //对方科目编码
				editable: true,
				align: 'center',
				sortable: true
			},
			{
				name: 'cntPtySubjNm', //对放科目名称
				editable: true,
				align: 'center',
				sortable: true
			},
			{
				name: 'teesSubjEncd', //暂估科目编码
				editable: true,
				align: 'center',
				sortable: true
			},
			{
				name: 'teesSubjNm', //暂估科目名称
				editable: true,
				align: 'center',
				sortable: true
			},
			{
				name: 'memo',
				editable: true,
				align: 'center',
				sortable: true
			},
		],
		rownumbers: true,
		loadonce: true,
		autowidth: true,
		height: 400,
		autoScroll: true,
		shrinkToFit: false,
		forceFit: true,
		rowNum: 500,
		rowList: [500, 1000, 3000, 5000],
		pager: '#jqgridPager', //表格页脚的占位符(一般是div)的id
		multiselect: true, //复选框
		caption: "对方科目", //表格的标题名字	
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
			loadLocalData1();
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

function loadLocalData1() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTab',
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
			}).trigger("reloadGrid")
		},
		error: function() {
			alert("error")
		}
	})
}

function grr(gr) {
	$("#" + gr + "_recvSendCateNm").attr("readonly", "readonly");
	$("#" + gr + "_invtyBigClsNm").attr("readonly", "readonly");
	$("#" + gr + "_cntPtySubjNm").attr("readonly", "readonly");
	$("#" + gr + "_teesSubjNm").attr("readonly", "readonly");
	$("#" + gr + "_recvSendCateId").attr("readonly", "readonly");
	$("#" + gr + "_invtyBigClsEncd").attr("readonly", "readonly");
	$("#" + gr + "_cntPtySubjId").attr("readonly", "readonly");
	$("#" + gr + "_teesSubjEncd").attr("readonly", "readonly");

	$("#" + gr + "_recvSendCateId").on("dblclick", function() {
		add = 1;
		$(".recv_List").show();
		$(".account_List").hide();
		$("#box").show();
		$("#big_wrap").show()

		$(".click_span").click(function() {
			addId = $(this).children().first().text();
			addName = $(this).children().first().next().next().text();
		})

		$('#recv_tree>ul>li>div span').parent().next().show()
		$('#recv_tree>ul>li>div span').eq(0).removeClass("open").addClass("close");
	})
	$("#" + gr + "_invtyBigClsEncd").on("dblclick", function() {
		add = 2;
		$(".invty_List").show();
		$(".account_List").hide();
		$("#box").show()
		$("#big_wrap").show()
		$(".click_span").click(function() {
			addId = $(this).children().first().text();
			addName = $(this).children().first().next().next().text();
		})

		$('#tree>ul>li>div').next().show()
		$('#tree>ul>li>div span').eq(0).addClass("close").removeClass("open");
	})
	$("#" + gr + "_cntPtySubjId").on("dblclick", function() {
		add = 3;
		$(".account_List").css("opacity", 1);
		$(".account_List").show();
		$("#purchaseOrder").hide();
	})
	$("#" + gr + "_teesSubjEncd").on("dblclick", function() {
		add = 4;
		$(".account_List").css("opacity", 1);
		$(".account_List").show();
		$("#purchaseOrder").hide();
	})
}

$(function() {
	$(".sure").click(function() {
		$("#purchaseOrder").show();
		$(".recv_List").hide();
		$(".invty_List").hide();
		$(".account_List").hide();
		$("#box").hide()
		$("#big_wrap").hide()
		var gr;
		if(mType == 1) {
			gr = newId
		} else {
			gr = $("#jqgrids").jqGrid('getGridParam', 'selrow');
		}
		if(add == 1) {
			$("#" + gr + "_recvSendCateId").val(addId);
			$("#" + gr + "_recvSendCateNm").val(addName);
		} else if(add == 2) {
			$("#" + gr + "_invtyBigClsEncd").val(addId);
			$("#" + gr + "_invtyBigClsNm").val(addName);
		} else if(add == 3) {
			var idd = $("#jqGrids").jqGrid('getGridParam', 'selrow');
			var rowData = $("#jqGrids").jqGrid('getRowData', idd);
			addId = rowData.subjId;
			addName = rowData.subjNm;
			$("#" + gr + "_cntPtySubjId").val(addId);
			$("#" + gr + "_cntPtySubjNm").val(addName);
		} else if(add == 4) {
			var idd = $("#jqGrids").jqGrid('getGridParam', 'selrow');
			var rowData = $("#jqGrids").jqGrid('getRowData', idd);
			addId = rowData.subjId;
			addName = rowData.subjNm
			$("#" + gr + "_teesSubjEncd").val(addId);
			$("#" + gr + "_teesSubjNm").val(addName);
		}
	})
	$(".cancel").click(function() {
		$("#purchaseOrder").show();
		$(".recv_List").hide();
		$(".invty_List").hide();
		$(".account_List").hide();
		$("#box").hide()
		$("#big_wrap").hide()
	})
})

// 点击保存选择
$(function() {
	$(".saveOrder").click(function() {
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
		newId = newrowid;
		var dataRow = {
			invtyBigClsEncd: '',
			recvSendCateId: '', //收发类别编码
			cntPtySubjId: '', //对方科目编码
			teesSubjEncd: '', //暂估科目编码
			memo: '',
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
	var ordrNum = $("#" + newId + '_ordrNum').val();
	var invtyBigClsEncd = $("#" + newId + '_invtyBigClsEncd').val();
	var recvSendCateId = $("#" + newId + '_recvSendCateId').val();
	var cntPtySubjId = $("#" + newId + '_cntPtySubjId').val();
	var teesSubjEncd = $("#" + newId + '_teesSubjEncd').val();
	var memo = $("#" + newId + '_memo').val();
	$("button").removeClass("gray");
	$('button').attr('disabled', false);
	$(".gray").attr('disabled', true)
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			'ordrNum': ordrNum,
			'invtyBigClsEncd': invtyBigClsEncd,
			'recvSendCateId': recvSendCateId,
			'cntPtySubjId': cntPtySubjId,
			'teesSubjEncd': teesSubjEncd,
			'memo': memo,
		}

	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		url: url + '/mis/account/invtyCntPtySubjSetTab/insertInvtyCntPtySubjSetTab',
		type: 'post',
		data: saveData,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		success: function(data) {
			alert(data.respHead.message)
			if(data.respHead.isSuccess == true) {
				loadLocalData1();
			}
		},
		error: function() {
			alert("error");
		} //错误执行方法
	})

}

//修改后的保存
function SaveModifyData() {
	var rowDatas = [];
	var gr = $("#jqgrids").jqGrid('getGridParam', 'selarrrow');
	for(var i = 0; i < gr.length; i++) {
		var obj = {};
		obj.ordrNum = parseInt($("#" + gr[i] + '_ordrNum').val());
		obj.invtyBigClsEncd = $("#" + gr[i] + '_invtyBigClsEncd').val()
		obj.recvSendCateId = $("#" + gr[i] + '_recvSendCateId').val();
		obj.cntPtySubjId = $("#" + gr[i] + '_cntPtySubjId').val();
		obj.teesSubjEncd = $("#" + gr[i] + '_teesSubjEncd').val();
		obj.memo = $("#" + gr[i] + '_memo').val();
		rowDatas[i] = obj;
	}

	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			'list': rowDatas
		}
	};

	var saveData = JSON.stringify(savedata);
	$.ajax({
		url: url + '/mis/account/invtyCntPtySubjSetTab/updateInvtyCntPtySubjSetTab',
		type: 'post',
		data: saveData,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		success: function(data) {
			alert(data.respHead.message)
			if(data.respHead.isSuccess == true) {
				loadLocalData1();
			}
		},
		error: function(data) {
			alert(error)
		} //错误执行方法
	})

}

/*点击删除行执行的操作*/
$(function() {
	$(".inv_delOrder").click(function() {
		var ids = $("#jqgrids").jqGrid('getGridParam', 'selarrrow'); //获取选中=行id
		var rowData = []
		for(i = 0; i < ids.length; i++) {
			//选中行的id
			var jstime = $("#jqgrids").getCell(ids[i], "ordrNum");
			rowData[i] = jstime
		}
		var rowDatas = rowData.toString();
		var deldata = {
			'reqHead': reqhead,
			"reqBody": {
				"ordrNum": rowDatas
			}
		};
		var delData = JSON.stringify(deldata)

		if(ids.length == 0) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + "/mis/account/invtyCntPtySubjSetTab/deleteInvtyCntPtySubjSetTabList",
				async: true,
				data: delData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(data) {
					alert(data.respHead.message)
					if(data.respHead.isSuccess == true) {
						loadLocalData1();
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
				url: url + "/mis/account/invtyCntPtySubjSetTab/uploadFileAddDb",
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
		url: url + '/mis/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTabPrint',
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
			var execlName = '对方科目'
			ExportData(list, execlName)
		},
		error: function() {
			alert("error")
		}
	})

})
