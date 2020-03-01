var count;
var pages;
var page = 1;
var rowNum;

localStorage.setItem("outInEncd", 0)
$(function() {
	$(document).on('blur', '#tranInWhsEncd', function() {
		var tranInWhsEncd = $("#tranInWhsEncd").val();
		dev({
			doc1: $("#tranInWhsEncd"),
			doc2: $("#tranInWhsNm"),
			showData: {
				"whsEncd": tranInWhsEncd
			},
			afunction: function(data) {
				if(tranInWhsEncd == ''){
					$("#tranInWhsNm").val('')
				} else {
					if(data.respHead.isSuccess == false) {
						alert("不存在此线下仓库请确认！")
					} else {
						var tranInWhsNm = data.respBody.list[0].whsNm;
						$("#tranInWhsNm").val(tranInWhsNm)					
					}
				}
			},
			url: url + "/mis/whs/whs_doc/selectWhsDocList"
		})
	});
})
//点击转入表格图标显示业务员列表
$(function() {
	$(document).on('click', '.storeId1_biaoge', function() {
		localStorage.setItem("outInEncd", 2)
		window.open("../../Components/baseDoc/whsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})

//页面初始化
$(function() {
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	pageInit();
});

function pageInit() {
	allHeight()
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"pageNo": 1,
			"pageSize": 500
		}
	};
	var postData = JSON.stringify(data);

	jQuery("#jqgrids").jqGrid({
		url: url + '/mis/ec/platWhsMapp/selectList',
		mtype: 'post',
		height: height,
		ajaxGridOptions: {
			contentType: 'application/json; charset=utf-8'
		},
		postData: postData,
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['序号', '平台编码', '线上仓库编码', '线上仓库名称', '线下仓库编码','线下仓库名称',], //jqGrid的列显示名字
		colModel: [{
				name: "id",
				align: "center",
				editable: false,
				sortable: false,
				hidden:true
			},
			{
				name: "type",
				align: "center",
				editable: true,
				sortable: false,
				edittype: 'select',
				editoptions: {
					value: getEcNum()
				}
			},
			{
				name: "online",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'onlineName',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'offline',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'offlineName',
				align: "center",
				editable: false,
				sortable: false
			}
		],
		jsonReader: {
			root: "respBody.list",
			repeatitems: true,
			records: "respBody.count", // json中代表数据行总数的数据		            
			total: "respBody.pages", // json中代表页码总数的数据
		},
		autowidth: true,
		viewrecords: true,
		rownumbers: true,
		sortable:true,
		loadonce: true,
		forceFit: true,
		rowNum: 500, //一页显示多少条
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条	
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id

		multiselect: true, //复选框
		multiboxonly: true,
		caption: "平台仓映射列表", //表格的标题名字
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
			search(page)
		},
		ondblClickRow: function() {
			if(mType == 2) {
				mType = 2
			} else {
				mType = 1;
				if($("#jqgrids").find(".editable").length > 0){ //存在编辑状态的行
					alert("存在未保存的数据，请操作完成后进行编辑！");
					return;
				}
				$(".addOrder").addClass("gray");
				$(".delOrder").addClass("gray");
				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true);
				var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
				var rowDatas = $("#jqgrids").jqGrid('getRowData', gr); //获取行数据
				$('#jqgrids').editRow(gr, true);
				$("#" + gr + "_deptId").attr("readonly", "readonly");
				$("#" + gr + "_offline").attr("readonly", "readonly");
			}
			grr(gr)
		}
	}).closest(".ui-jqgrid-bdiv").css({
		'overflow-y': 'scroll'
	});
}


//新增
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
			deptId: "",
			deptName: "",
			tel: '',
			addr: '',
			memo: ''
		};

		//将新添加的行插入到第一列
		$("#jqgrids").jqGrid("addRowData", newrowid, dataRow, "first");
		//设置grid单元格可编辑
		$('#jqgrids').jqGrid('editRow', newrowid, true);
		$('.addOrder').addClass("gray")
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true);
		// 点击新增行默认选中此行
		$("#jqgrids").jqGrid('setSelection',newrowid);
		grr(newrowid)
		$("#" + newrowid + "_offline").attr("readonly", "readonly");
	})
})


function grr(gr) {
	$("#" + gr + "_offline").on("dblclick", function() {
		$("#wwrap").css("opacity", 1);
		$("#wwrap").show();
		$("#purchaseOrder").hide()
	})
}


//打开部门档案后点击确定取消
$(function() {
	//确定
	$(".true").click(function() {
		var rowid;
		//	仓库档案
		//	获得行号
		var gr = $("#whs_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDatas = $("#whs_jqgrids").jqGrid('getRowData', gr);
		var offline = rowDatas.whsEncd
		$("input[name='offline']").val(offline)
		if(mType == 2) {
			rowid = newrowid
		} else if(mType == 1) {
			//获得行号
			rowid = $("#jqgrids").jqGrid('getGridParam', 'selrow');
		}
		$("#jqgrids").setRowData(rowid, {
			offlineName: rowDatas.whsNm
		})

		$("#wwrap").css("opacity", 0);
		$("#wwrap").hide();
		$("#purchaseOrder").show()
	})
	//	取消
	$(".false").click(function() {
		$("#wwrap").css("opacity", 0);
		$("#wwrap").hide();
		$("#purchaseOrder").show()
		//到货单
		//获得行号
//		var rowid = $("#f_jqgrids").jqGrid('getGridParam', 'selrow');
	})
	
})

function IsCheckValue(type,online,onlineName,offline) {
	if(type == "0") {
		alert("未选择平台编码")
		return false;
	} else if(online == '') {
		alert("线上仓库编码不能为空")
		return false;
	} else if(onlineName == '') {
		alert("线上仓库名称不能为空")
		return false;
	} else if(offline == '') {
		alert("线下仓库编码不能为空")
		return false;
	}
	return true;
}
function addEditData() {
	//获取此行的仓库编码和存货编码
	var id_num = $("#jqgrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowDatas = $("#jqgrids").jqGrid('getRowData', id_num);
	var id = rowDatas.id;
//	var type = $("#" + id_num + "_type").val();
	var type=$("#" + id_num + "_type option:selected").val();
	var online = $("#" + id_num + "_online").val();
	var onlineName = $("#" + id_num + "_onlineName").val();
	var offline = $("#" + id_num + "_offline").val();
	var offlineName = rowDatas.offlineName;
	if(IsCheckValue(type,online,onlineName,offline) == true) {
		var save = {
			"reqHead": reqhead,
			"reqBody": {
				"id": id,
				"type": type,
				"online": online,
				"onlineName": onlineName,
				"offline": offline,
				"offlineName": offlineName
			}
		}
		var saveJson = JSON.stringify(save);
		$.ajax({
			type: "post",
			url: url + "/mis/ec/platWhsMapp/update",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(editMsg) {
				alert(editMsg.respHead.message)
				$(".addOrder").removeClass("gray");
				$(".delOrder").removeClass("gray");
				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true);
				search()
			},
			error: function() {
				console.log("更新失败")
			}
		});
	}
}

var mType = 0;
var isclick = true;
$(function() {
	$(".saveOrder").click(function() {
		if(isclick) {
			isclick = false;
			if(mType == 2) {
				addNewData();
			}
			if(mType == 1) {
				addEditData();
			}
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	})
})
//添加新数据
var newrowid;

function addNewData() {
	//获取此行的仓库编码和存货编码
	var id_num = $("#jqgrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowDatas = $("#jqgrids").jqGrid('getRowData', id_num);
	var id = $("#" + newrowid + "_id").val();
	var type = $("#" + newrowid + "_type").val();
	var type=$("#" + newrowid + "_type option:selected").val();
	var online = $("#" + newrowid + "_online").val();
	var onlineName = $("#" + newrowid + "_onlineName").val();
	var offline = $("#" + newrowid + "_offline").val();
	var offlineName = rowDatas.offlineName;
	if(IsCheckValue(type,online,onlineName,offline) == true) {
		var save = {
			"reqHead": reqhead,
			"reqBody": {
				"id": id,
				"type": type,
				"online": online,
				"onlineName": onlineName,
				"offline": offline,
				"offlineName": offlineName,
				"pageNo": 1,
				"pageSize": 500
			}
		}
		var saveJson = JSON.stringify(save);
		$.ajax({
			type: "post",
			url: url + "/mis/ec/platWhsMapp/insert",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(data) {
				alert(data.respHead.message)
				mType = 1
				search()
	
			},
			error: function() {
				console.log("增加失败")
			}
		});
	}
}

//删除行
$(function() {
	$(".delOrder").click(function() {
		//获取此行的仓库编码和存货编码
		var id_num = $("#jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDatas = $("#jqgrids").jqGrid('getRowData', id_num);
		var id = rowDatas.id
		var deleteAjax = {
			"reqHead": reqhead,
			"reqBody": {
				"id": id
			}
		}
		var deleteData = JSON.stringify(deleteAjax)
		if(id == null) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + "/mis/ec/platWhsMapp/delete",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(remover) {
					alert(remover.respHead.message);
					search()
					
					$('.addOrder').removeClass("gray")
					$('button').attr('disabled', false);
					$(".gray").attr('disabled', false);
				},
				error: function() {
					console.log("删除失败")
				}
			});

		}
	})
})

//条件查询
$(function() {
	$('.find').click(function() {
		search()
	})
})

function search() {
	var online = $("input[name='online1']").val();
	var offline = $("input[name='offline1']").val();
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"online": online,
			"offline": offline,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		url: url + "/mis/ec/platWhsMapp/selectList",
		async: true,
		data: postD2,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
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
			alert("条件查询失败")
		}
	});
	
//	
//	
//	$('#jqgrids').jqGrid('setGridParam', {
//		url: url + "/mis/ec/platWhsMapp/selectList",
//		mtype: "post",
//		datatype: "json", //请求数据返回的类型。可选json,xml,txt
//		postData: postD2,
//		rowNum: 10,
//		ajaxGridOptions: {
//			contentType: 'application/json; charset=utf-8'
//		}
//	}).trigger('reloadGrid')

}



//电商平台名称下拉框
function getEcNum() {
	var ecNum = "";
	var data2 = {
		reqHead,
		"reqBody": {
			"pageNo": 1,
			"pageSize": 500

		}
	}
	var postD2 = JSON.stringify(data2);
	$.ajax({
		url: url3 + "/mis/ec/ecPlatform/queryList",
		type: "post",
		async: false,
		dataType: 'json', //请求数据返回的类型。可选json,xml,txt
		data: postD2,
		contentType: 'application/json',
		success: function(e) {
			var result = e.respBody.list;
			ecNum += "0" + ":" + "请选择" + ";";
			for(var i = 0; i < result.length; i++) {
				if(i != result.length - 1) {
					ecNum += result[i].ecId + ":" + result[i].ecName + ";";
					let aaa=result[i].ecId;
					let bbb=result[i].ecName
				} else {
					ecNum += result[i].ecId + ":" + result[i].ecName;
					let aaa=result[i].ecId;
					let bbb=result[i].ecName
				}
			}


		}
	});
	return ecNum; //必须有此返回值	
	
}


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
				url: url + "/mis/purc/DeptDoc/uploadDeptDocFile",
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
	var online = $("input[name='online1']").val();
	var offline = $("input[name='offline1']").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"online": online,
			"offline": offline,
			"pageNo": 1,
			"pageSize": 500
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + "/mis/ec/platWhsMapp/selectList",
		type: 'post',
		data: Data,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		success: function(data) {
			var obj = {}
			var list = data.respBody.list;
			obj=list;
			daochu(obj)
		},
		error: function() {
			console.log(error)
		}
	})
	
})

function daochu(JSONData) {
    var str = '序号,平台类型,线上仓库编码,线下仓库编码,线上仓库名称,线下仓库名称\n';

    for(let i=0;i<JSONData.length;i++){
        var result ='';
        if (JSONData[i].orderStatusc=='0'){
            result="是";
        } else {
            result="否";
        }
//      str += (i+1).toString()+','+JSONData[i].orderId+'\t'+','+formateOrderTime(JSONData[i].orderTime)+'\t'+','+JSONData[i].p1+'\t'+','+JSONData[i].userName+'\t'+','+JSONData[i].recMobile+'\t'+','+JSONData[i].productName+'\t'+','+result+'\t'+',\n'
		for(let item in JSONData[i]) {
			str += `${JSONData[i][item] + '\t'},`;
	    	if(JSONData[i][item]==null){
				JSONData[i][item]="";
			}
		}
		str += '\n';
    }
    var blob = new Blob([str], {type: "text/plain;charset=utf-8"});
    //解决中文乱码问题
    blob =  new Blob([String.fromCharCode(0xFEFF), blob], {type: blob.type});
    object_url = window.URL.createObjectURL(blob);
    var link = document.createElement("a");
    link.href = object_url;
    link.download =  "平台仓库映射.csv";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}