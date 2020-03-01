
var count;
var pages;
var page = 1;
var rowNum;
var mType = 0;

$(function() {
	pageInit();
	loadLocalData(page)
})

//表格初始化
function pageInit() {
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	jQuery("#jqgrids").jqGrid({
		datatype: "local", //请求数据返回的类型。可选json,xml,txt
		colNames: ['银行编码', '银行名称', '个人账号是否定长', '个人账号长度', '自动带出的个人账号长度', '单位编码', '企业账号是否定长', '企业账号长度', '银行行号'], //jqGrid的列显示名字
		colModel: [{
				name: "bankEncd",
				align: "center",
				editable: true,
				sortable: false,
				editoptions: {   
					dataInit: function(element) { 
						$(element).keydown(function(event) {   
							if(event.keyCode == 13) {
								return false;
							}     
						});      
					}  
				}
			},
			{
				name: "bankNm",
				align: "center",
				editable: true,
				sortable: false,
				editoptions: {   
					dataInit: function(element) { 
						$(element).keydown(function(event) {   
							if(event.keyCode == 13) {
								return false;
							}     
						});      
					}  
				}
			},
			{
				name: "indvAcctIsFixlen", //是否
				align: "center",
				editable: true,
				sortable: false,
				edittype: 'select',
				editoptions: {
					value: {
						0: "否",
						1: "是"
					},
					dataInit: function(element) { 
						$(element).keydown(function(event) {   
							if(event.keyCode == 13) {
								return false;
							}     
						});      
					} 
				},
			},
			{
				name: 'indvAcctNumLen',
				align: "center",
				editable: true,
				sortable: false,
				editoptions: {   
					dataInit: function(element) { 
						$(element).keydown(function(event) {   
							if(event.keyCode == 13) {
								return false;
							}     
						});      
					}  
				}
			},
			{
				name: 'autoOutIndvNumLen',
				align: "center",
				editable: true,
				sortable: false,
				editoptions: {   
					dataInit: function(element) { 
						$(element).keydown(function(event) {   
							if(event.keyCode == 13) {
								return false;
							}     
						});      
					}  
				}
			},
			{
				name: "corpEncd",
				align: "center",
				editable: true,
				sortable: false,
				editoptions: {   
					dataInit: function(element) { 
						$(element).keydown(function(event) {   
							if(event.keyCode == 13) {
								return false;
							}     
						});      
					}  
				}
			},
			{
				name: "compAcctIsFixLen", //是否
				align: "center",
				editable: true,
				sortable: false,
				edittype: 'select',
				editoptions: {
					value: {
						0: "否",
						1: "是"
					},
					dataInit: function(element) { 
						$(element).keydown(function(event) {   
							if(event.keyCode == 13) {
								return false;
							}     
						});      
					} 
				},
			},
			{
				name: 'compAcctNumLen',
				align: "center",
				editable: true,
				sortable: false,
				editoptions: {   
					dataInit: function(element) { 
						$(element).keydown(function(event) {   
							if(event.keyCode == 13) {
								return false;
							}     
						});      
					}  
				}
			},
			{
				name: 'bankId',
				align: "center",
				editable: true,
				sortable: false,
				editoptions: {   
					dataInit: function(element) { 
						$(element).keydown(function(event) {   
							if(event.keyCode == 13) {
								return false;
							}     
						});      
					}  
				}
			}
		],
		loadonce: false,
		rownumbers: true,
		autowidth: true,
		height: 320,
		autoScroll: true,
		shrinkToFit: false,
		multiselect: true, //复选框 
		altclass: true,
		rowNum:10,
		viewrecords: true,
		forceFit: true,
		sortorder: "desc",
		rowList: [10, 20, 30], //可供用户选择一页显示多少条
		cellsubmit: "clientArray",
		pager: '#jqGridPager',
		caption: "银行档案", //表格的标题名字
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
		ondblClickRow: function() {
			mType = 1;
			var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
			$('#jqgrids').editRow(gr, true);
			var grValue = $("#" + gr + "_deptId").val()
			if(grValue){
				$("#" + gr + "_bankEncd").attr("readonly", "readonly");
			}
		},
	})
}

function loadLocalData() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var showData = {
		"reqHead": reqhead,
		"reqBody": {
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var postData = JSON.stringify(showData);
	$.ajax({
		type: "post",
		url: url + "/mis/account/bankDoc/selectBankDoc", //列表
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
			alert("加载失败")
		}
	})
}

$(function() {
	$(".saveOrder").click(function() {
		if(mType == 1) {
			addEditData(); //编辑按钮
		} else if(mType == 2) {
			$(".addOrder").css("background-color", 'black')
			addNewData(); //添加新数据
		}
	})
})

//声明新行号
var newrowid;

//点击增加按钮
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
			bankEncd: "",
			indvAcctIsFixlen: 0,
			indvAcctNumLen: 0,
			autoOutIndvNumLen: 0,
			compAcctIsFixLen: 0,

			bankNm: '',
			corpEncd: '',
			bankId: 0,
			compAcctNumLen: 0,
		};

		//将新添加的行插入到第一列
		$("#jqgrids").jqGrid("addRowData", newrowid, dataRow, "first");
		//设置grid单元格可编辑
		$('#jqgrids').jqGrid('editRow', newrowid, true);
		$(".addOrder").addClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
	})
})

function addNewData() {
	var bankEncd = $("#" + newrowid + "_bankEncd").val();
	var indvAcctIsFixlen = parseInt($("#" + newrowid + "_indvAcctIsFixlen").val());
	var indvAcctNumLen = parseInt($("#" + newrowid + "_indvAcctNumLen").val());
	var autoOutIndvNumLen = parseInt($("#" + newrowid + "_autoOutIndvNumLen").val());
	var compAcctIsFixLen = parseInt($("#" + newrowid + "_compAcctIsFixLen").val());

	var bankNm = $("#" + newrowid + "_bankNm").val();
	var corpEncd = $("#" + newrowid + "_corpEncd").val();
	var bankId = parseInt($("#" + newrowid + "_bankId").val());
	var compAcctNumLen = parseInt($("#" + newrowid + "_compAcctNumLen").val());
	var save = {
		"reqHead": reqhead,
		"reqBody": {
			'bankEncd': bankEncd,
			'indvAcctIsFixlen': indvAcctIsFixlen,
			'indvAcctNumLen': indvAcctNumLen,
			'autoOutIndvNumLen': autoOutIndvNumLen,
			'compAcctIsFixLen': compAcctIsFixLen,

			'bankNm': bankNm,
			'corpEncd': corpEncd,
			'bankId': bankId,
			'compAcctNumLen': compAcctNumLen
		}
	}
	var saveJson = JSON.stringify(save);
	$.ajax({
		type: "post",
		url: url + "/mis/account/bankDoc/insertBankDoc",
		async: true,
		data: saveJson,
		dataType: 'json',
		contentType: 'application/json',
		success: function(data) {
			alert(data.respHead.message);
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
		obj.bankEncd = $("#" + gr[i] + "_bankEncd").val().toString();
		obj.indvAcctIsFixlen = $("#" + gr[i] + "_indvAcctIsFixlen").val().toString();
		obj.indvAcctNumLen = $("#" + gr[i] + "_indvAcctNumLen").val().toString();
		obj.autoOutIndvNumLen = $("#" + gr[i] + "_autoOutIndvNumLen").val().toString();
		obj.compAcctIsFixLen = $("#" + gr[i] + "_compAcctIsFixLen").val().toString();

		obj.bankNm = $("#" + gr[i] + "_bankNm").val().toString();
		obj.corpEncd = $("#" + gr[i] + "_corpEncd").val().toString();
		obj.bankId = $("#" + gr[i] + "_bankId").val().toString();
		obj.compAcctNumLen = $("#" + gr[i] + "_compAcctNumLen").val().toString();
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
			url: url + "/mis/account/bankDoc/updateBankDoc",
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
		var ids = $("#jqgrids").jqGrid('getGridParam', 'selarrrow'); //获取选中行id
		var rowData = []
		for(i = 0; i < ids.length; i++) {
			var jstime = $("#jqgrids").getCell(ids[i], "bankEncd");
			rowData[i] = jstime
		}
		var rowDatas = rowData.toString();
		var deleteAjax = {
			"reqHead": reqhead,
			"reqBody": {
				"bankEncd": rowDatas
			}
		}
		var deleteData = JSON.stringify(deleteAjax)
		if(ids.length == 0) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + "/mis/account/bankDoc/deleteBankDoc",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(remover) {
					if(remover.respHead.isSuccess == true) {
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
	            url: url + "/mis/account/bankDoc/uploadFileAddDb",
	            data:data,
	          	dataType: "json",
	           	cache: false,//上传文件无需缓存
	           	processData: false,//用于对data参数进行序列化处理 这里必须false
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
	var data = {
		"reqHead": reqhead,
		"reqBody": {}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/account/bankDoc/selectBankDocPrint',
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
			obj = list;
			daochu(obj)
		},
		error: function() {
			alert("导出失败")
		}
	})
})
function daochu(JSONData) {
    var str = '银行编码,银行名称,个人账号是否定长,个人账号长度,自动带出的个人账号长度,单位编码,企业账号是否定长,企业账号长度,银行行号,是否系统预置,时间戳\n';

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
    link.download =  "银行档案.csv";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}