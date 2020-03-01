$(function() {
	pageInit();
});
//初始化表格
function pageInit() {
	var data3 = {
		"reqHead": reqhead,
		"reqBody": {
			"pageNo": 1,
			"pageSize": 99999999
		}
	};
	var postD3 = JSON.stringify(data3);

	jQuery("#jqgrids").jqGrid({
		url: url + "/mis/purc/MeasrCorpDoc/selectMeasrCorpDocList",
		mtype: 'post',
		ajaxGridOptions: {
			contentType: 'application/json; charset=utf-8'
		},
		postData: postD3,
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['计量单位编码', '计量单位名称', '备注'], //jqGrid的列显示名字
		colModel: [{
				name: "measrCorpId",
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: "measrCorpNm",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: "memo",
				align: "center",
				editable: true,
				sortable: false
			}
		],
		autowidth:true,
		height:320,
		autoScroll:true,
		shrinkToFit:false,
		rownumbers: true,
		loadonce: true,
		rownumWidth: 15,  //序列号列宽度
		forceFit: true,
		rowNum: 100, //一页显示多少条
		rowList: [100, 300, 500],
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		multiboxonly: true,
		jsonReader: {
			records: "respBody.count", // json中代表数据行总数的数据	
			root: "respBody.list", // json中代表实际模型数据的入口
			total: "respBody.pages", // json中代表页码总数的数据
			repeatitems: true

		},
		caption: "计量单位档案", //表格的标题名字
		ondblClickRow: function() {
				mType = 1;
				var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
				var rowDatas = $("#jqgrids").jqGrid('getRowData', gr); //获取行数据
				$('#jqgrids').editRow(gr, true);
				$("#" + gr + "_measrCorpId").attr("readonly", "readonly");
		},
	});
}

function IsCheckValue(measrCorpId, measrCorpNm) {
	if(measrCorpId == '') {
		alert("计量单位编码不能为空")
		return false;
	} else if(measrCorpNm == '') {
		alert("计量单位名称不能为空")
		return false;
	}
	return true;
}
function addEditData() {
	var measrCorpId = $("input[name='measrCorpId']").val();
	var measrCorpNm = $("input[name='measrCorpNm']").val();
	if(IsCheckValue(measrCorpId, measrCorpNm) == true) {
		mType = 1
		var gr = $("#jqgrids").jqGrid('getGridParam', 'selarrrow');
	
		var rowData = [];
		for(var i = 0; i < gr.length; i++) {
			var obj = {};
			obj.measrCorpId = $("#" + gr[i] + "_measrCorpId").val().toString();
			obj.measrCorpNm = $("#" + gr[i] + "_measrCorpNm").val().toString();
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
				url: url + "/mis/purc/MeasrCorpDoc/updateMeasrCorpDocByMeasrCorpId",
				async: true,
				data: editJson,
				dataType: 'json',
				contentType: 'application/json',
				success: function(editMsg) {
					alert(editMsg.respHead.message)
					window.location.reload()
				},
				error: function() {
					alert("更新失败")
				}
			});
		}
	}
}

var mType = 0;
var isclick = true;
$(function() {
	$(".saveOrder").click(function() {
		if(isclick) {
			isclick = false;
			if(mType == 1) {
				addEditData();
			}
			if(mType == 2) {
				$(".addOrder").css("background-color", 'black').attr("disabled", false)
				addNewData();
			}
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	})
})
var newrowid;

function addNewData() {
	var measrCorpId = $("#" + newrowid + "_measrCorpId").val();
	var measrCorpNm = $("#" + newrowid + "_measrCorpNm").val();
	var memo = $("#" + newrowid + "_memo").val();
	if(IsCheckValue(measrCorpId, measrCorpNm) == true) {
		var save = {
			"reqHead": reqhead,
			"reqBody": {
				"measrCorpId": measrCorpId,
				"measrCorpNm": measrCorpNm,
				"memo": memo
			}
		}
		var saveJson = JSON.stringify(save);
		$.ajax({
			type: "post",
			url: url + "/mis/purc/MeasrCorpDoc/insertMeasrCorpDoc", //注意路径
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(data) {
				alert(data.respHead.message)
				window.location.reload()
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
		var ids = jQuery("#jqgrids").jqGrid('getDataIDs');
		//获得当前最大行号（数据编码）
		var rowid = Math.max.apply(Math, ids);
		//获得新添加行的行号（数据编码）
		newrowid = rowid + 1;
		var dataRow = {
			measrCorpId: "",
			measrCorpNm: "",
			memo: ""
		};
		//将新添加的行插入到第一列
		$("#jqgrids").jqGrid("addRowData", newrowid, dataRow, "first");
		//设置grid单元格可编辑
		$('#jqgrids').jqGrid('editRow', newrowid, true);
		$(".addOrder").css("background-color", 'gray').attr("disabled", true)

	})

})

//删除行
$(function() {
	$(".delOrder").click(function() {
		var ids = $("#jqgrids").jqGrid('getGridParam', 'selarrrow'); //获取行id
		var rowData = [];
		for(i = 0; i < ids.length; i++) {
			//选中行的id
			var jstime = $("#jqgrids").getCell(ids[i], "measrCorpId");
			rowData[i] = jstime
		}
		var rowDatas = rowData.toString();
		var deleteAjax = {
			"reqHead": reqhead,
			"reqBody": {
				"measrCorpId": rowDatas
			}
		}
		var deleteData = JSON.stringify(deleteAjax)
		if(ids.length == 0) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + "/mis/purc/MeasrCorpDoc/deleteMeasrCorpDocList",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(remover) {
					window.location.reload()
				},
				error: function() {
					alert("删除失败")
				}
			});

		}
	})
})



//导入
$(function () {
    $(".importExcel").click(function () {
    	var files = $("#FileUpload").val()
    	var fileObj = document.getElementById("FileUpload").files[0];
        var formFile = new FormData();
       	formFile.append("action", "UploadVMKImagePath");  
       	formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
        var data = formFile;
        if(files != "") {
        	$.ajax({
	            type: 'post',
	            url: url + "/mis/purc/MeasrCorpDoc/uploadMeasrCorpDocFile",
	            data:data,
	          	dataType: "json",
	           	cache: false,//上传文件无需缓存
	           	processData: false,//用于对data参数进行序列化处理 这里必须false
	           	contentType: false, //必须
	           	success: function(data) {
	           		alert(data.respHead.message)
	           		window.location.reload()
	           	}
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
		url: url + '/mis/purc/MeasrCorpDoc/printingMeasrCorpDocList',
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
			alert("error")
		}
	})

})

function daochu(JSONData) {
    var str = '计量单位编码,计量单位名称,备注\n';

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
    link.download =  "计量单位档案.csv";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}