//表格初始化
$(function() {
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	var showData = {
		"reqHead": reqhead,
		"reqBody": {
			"pageNo": 1,
			"pageSize": 9999999
		}
	};
	var postData = JSON.stringify(showData);
	$.ajax({
		type: "post",
		url: url + "/mis/account/vouchCateDoc/selectVouchCateDoc", //列表
		async: true,
		data: postData,
		dataType: 'json',
		contentType: 'application/json',
		//开始加载动画  添加到ajax里面
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");

		},
		success: function(data) {
			var data = data.respBody.list;

			jQuery("#jqgrids").jqGrid({
				//				height: height * 0.6,
				data: data,
				datatype: "local", //请求数据返回的类型。可选json,xml,txt
				colNames: ['凭证类别字', '凭证类别名称', '限制类型', '限制科目'], //jqGrid的列显示名字
				colModel: [{
						name: "vouchCateWor",
						align: "center",
						editable: true,
						sortable: false,
					},
					{
						name: "vouchCateNm",
						align: "center",
						editable: true,
						sortable: false,
					},
					{
						name: "lmtMode",
						align: "center",
						editable: true,
						sortable: false,
						edittype: 'select',
						editoptions: {
							value: {
								"无限制": "无限制",
								"借方必有": "借方必有",
								"贷方必有": "贷方必有",
								"凭证必有": "凭证必有",
								"凭证必无": "凭证必无",
								"借方必无": "借方必无",
								"贷方必无": "贷方必无"
							}
						}
					},
					{
						name: 'subjNm',
						align: "center",
						editable: true,
						sortable: false,
					},
				],
				autowidth: true,
				height: 320,
				autoScroll: true,
				shrinkToFit: false,
				viewrecords: true,
				rownumbers: true,
				loadonce: true,
				rownumWidth: 20, //序列号列宽度
				forceFit: true,
				rowNum: 100,
				rowList: [100, 300, 500],
				pager: '#jqgridPager', //表格页脚的占位符(一般是div)的id
				multiselect: true, //复选框
				multiboxonly: true,
				caption: "凭证类别", //表格的标题名字
				ondblClickRow: function() {
					mType = 2;
					var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
					var rowDatas = $("#jqgrids").jqGrid('getRowData', gr); //获取行数据
					$('#jqgrids').editRow(gr, true);
					$("#" + gr + "_vouchCateWor").attr("readonly", "readonly")
					$("#" + gr + "_subjNm").on("dblclick", function() {
						grr();
					})
				}
			})

			for(var i = 0; i < data.length; i++) {
				var arr = [];
				$.each(data[i].lmtSubjList, function(j, p) {
					arr.push(p.lmtSubjId);
				});
				$("#jqgrids").setRowData(i + 1, {
					subjNm: arr
				})
			}

		},
		//结束加载动画
		complete: function() {
			$(".zhezhao").css("display", "none");
			$("#loader").css("display", "none");
		},
		error: function() {
			alert("更新失败")
		}
	})
})

function grr() {
	var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow');
	if(gr == null) {
		alert("请选择行")
	} else {
		$(".account_List").css("opacity", 1);
		$(".account_List").show();
		$("#purchaseOrder").hide();
	}
}

$(function() {
	$(".sure").click(function() {
		$("#purchaseOrder").show();
		$(".account_List").hide();
		var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow');

		var ids = $("#jqGrids").jqGrid('getGridParam', 'selarrrow');

		var rowData = [];
		for(var i = 0; i < ids.length; i++) {
			//选中行的id
			var jstime = $("#jqGrids").getCell(ids[i], "subjId");
			//建一个数组，把选中行的id添加到这个数组中去。
			rowData[i] = jstime;
		}
		var rowDatas = rowData.toString();
		$("#" + gr + "_subjNm").val(rowDatas);
	})
	$(".cancel").click(function() {
		$("#purchaseOrder").show();
		$(".account_List").hide();
	})
})

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

//声明新行号
var newrowid;

//点击增加按钮
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
			vouchCateWor: "",
			vouchCateNm: '',
			lmtMode: '',
			subjNm: '',
		};

		//将新添加的行插入到第一列
		$("#jqgrids").jqGrid("addRowData", newrowid, dataRow, "first");
		//设置grid单元格可编辑
		$('#jqgrids').jqGrid('editRow', newrowid, true);
		$(".addOrder").css("background-color", 'gray')
		$("#" + newrowid + "_subjNm").on("dblclick", function() {
			grr();
		})
		$("#jqgrids").jqGrid('setSelection', newrowid);
	})
})

function addNewData() {
	var vouchCateWor = $("#" + newrowid + "_vouchCateWor").val();
	var vouchCateNm = $("#" + newrowid + "_vouchCateNm").val();
	var lmtMode = $("#" + newrowid + "_lmtMode").val();
	var lmtSubjId = $("#" + newrowid + "_subjNm").val();
	var lmtList = [{
		lmtSubjId: lmtSubjId
	}]
	var save = {
		"reqHead": reqhead,
		"reqBody": {
			'vouchCateWor': vouchCateWor,
			'vouchCateNm': vouchCateNm,
			'lmtMode': lmtMode,
			'lmtSubjList': lmtList
		}
	}
	var saveJson = JSON.stringify(save);
	$.ajax({
		type: "post",
		url: url + "/mis/account/vouchCateDoc/insertVouchCateDoc",
		async: true,
		data: saveJson,
		dataType: 'json',
		contentType: 'application/json',
		success: function(data) {
			alert(data.respHead.message)
			if(data.respHead.isSuccess == true) {
				window.location.reload()
			}
		},
		error: function() {
			alert("增加失败")
		}
	});
}

function addEditData() {
	var gr = $("#jqgrids").jqGrid('getGridParam', 'selarrrow');
	var rowData = [];
	for(var i = 0; i < gr.length; i++) {
		var obj = {};
		var lmtSubjId = $("#" + gr[i] + "_subjNm").val()
		obj.vouchCateWor = $("#" + gr[i] + "_vouchCateWor").val().toString();
		obj.vouchCateNm = $("#" + gr[i] + "_vouchCateNm").val().toString();
		obj.lmtMode = $("#" + gr[i] + "_lmtMode").val().toString();
		obj.lmtSubjList = [{
			lmtSubjId: lmtSubjId
		}]
		rowData[i] = obj;
	}

	if(gr.length == 0) {
		alert("请选择单据!")
	} else {
		var edit = {
			"reqHead": reqhead,
			"reqBody": {
				'list': rowData
			}
		}
		var editJson = JSON.stringify(edit);
		$.ajax({
			type: "post",
			url: url + "/mis/account/vouchCateDoc/updateVouchCateDoc",
			async: true,
			data: editJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(editMsg) {
				alert(editMsg.respHead.message)
				if(editMsg.respHead.isSuccess == true) {
					window.location.reload()
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
		var ids = $("#jqgrids").jqGrid('getGridParam', 'selarrrow'); //获取选中=行id
		var rowData = []
		for(i = 0; i < ids.length; i++) {
			var gr = $("#jqgrids").getGridParam('selrow');
			//选中行的id
			var jstime = $("#jqgrids").getCell(ids[i], "vouchCateWor");
			rowData[i] = jstime
		}
		var rowDatas = rowData.toString();
		var deleteAjax = {
			"reqHead": reqhead,
			"reqBody": {
				"vouchCateWor": rowDatas
			}
		}
		var deleteData = JSON.stringify(deleteAjax)
		if(ids.length == 0) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + "/mis/account/vouchCateDoc/deleteVouchCateDoc",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(data) {
					alert(data.respHead.message)
					if(data.respHead.isSuccess == true) {
						window.location.reload()
					}
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
				url: url + "/mis/account/vouchCateDoc/uploadFileAddDb",
				data: data,
				dataType: "json",
				cache: false, //上传文件无需缓存
				processData: false, //用于对data参数进行序列化处理 这里必须false
				contentType: false, //必须
				success: function(data) {
					alert(data.respHead.message)
					if(data.respHead.isSuccess == true) {
						window.location.reload()
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
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {}
	}
	var Data = JSON.stringify(savedata);
	$.ajax({
		url: url + '/mis/account/vouchCateDoc/selectVouchCateDocPrint',
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
			for(var i = 0; i < obj.length; i++) {
				delete obj[i].lmtSubjList;
			}
			daochu(obj)
		},
		error: function() {
			alert("error")
		}
	})

})

function daochu(JSONData) {
    var str = '序号,凭证类别字,凭证类别名称,凭证类别排序号,限制方式,备注\n';

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
    link.download =  "凭证类别档案.csv";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}