var chk1 = "/mis/whs/prod_stru/updatePStruChk";
var chk2 = "/mis/whs/prod_stru/updatePStruNoChk";

var count;
var pages;
var page = 1;
var rowNum;
//表格初始化
$(function() {
	allHeight();
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	$("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['编码', '母件编码', '母件名称', '母件规格', '备注', '是否审核', '创建人', '创建时间', '修改人', '修改时间', '审核人', '审核时间'],
		colModel: [{
				name: 'ordrNum',
				editable: true,
				align: 'center',
				hidden: true
			},
			{
				name: 'momEncd',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'momNm',
				editable: true,
				align: 'center'

			},
			{
				name: 'momSpc',
				editable: true,
				align: 'center'

			},
			{
				name: 'memo',
				editable: true,
				align: 'center'

			},
			{
				name: 'isNtChk',
				editable: true,
				align: 'center'

			},
			{
				name: 'setupPers',
				editable: true,
				align: 'center'

			},
			{
				name: 'setupTm',
				editable: true,
				align: 'center'

			},
			{
				name: 'mdfr',
				editable: true,
				align: 'center'

			},
			{
				name: 'modiTm',
				editable: true,
				align: 'center'

			},
			{
				name: 'chkr',
				editable: true,
				align: 'center'

			},
			{
				name: 'chkTm',
				editable: true,
				align: 'center'

			}
		],
		autowidth: true,
		rownumbers: true,
		loadonce: false,
		height:height,
		rowNum: 500,
		rowList: [500, 1000, 3000,5000],
		forceFit: true,
		pager: '#p_jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
//		multiboxonly: true,
		autoScroll:true,
		rownumWidth: 30,  //序列号列宽度
		shrinkToFit:false,
		caption: "产品结构列表", //表格的标题名字
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
		//双击弹出产品结构
		ondblClickRow: function(rowid) {
			order(rowid);
		},
	})
})

//查询全部
function search () {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var myDate = {};

	var momEncd = $("input[name='momEncd']").val();
//	var ordrNum = $(".ordrNum").val();
	var isNtChk = $("select[name='isNtChk']").val();

	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"momEncd": momEncd,
//			'ordrNum':ordrNum,
			'isNtChk':isNtChk,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/whs/prod_stru/queryList',
		async: true,
		data: saveData,
		dataType: 'json',
		//开始加载动画  添加到ajax里面
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");

		},
		success: function(data) {
			var list = data.respBody.list;
			for(var i = 0;i<list.length;i++){
				if(list[i].isNtChk==0){
					list[i].isNtChk="否"
				}else if(list[i].isNtChk==1){
					list[i].isNtChk="是"
				}
			}
			myDate = list;
			var mydata = {};
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#jqGrids").jqGrid("clearGridData");
			$("#jqGrids").jqGrid("setGridParam", {
				data: mydata.rows,
				jsonReader: {
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
			alert('查询失败')
		}
	});
}


//查询按钮
$(document).on('click', '.search', function() {
	search();
})

function order(rowid) {
	//获得行数据
	var rowDatas = $("#jqGrids").jqGrid('getRowData', rowid);
	localStorage.setItem("ordrNum", rowDatas.ordrNum);
	window.open("../../Components/whs/prod_stru.html?1",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
}

function ntChk(x, chk) {
	//获得选中行的行号
	var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
	//对象数组
	var rowData = [];
	for(var i = 0; i < ids.length; i++) {
		var obj = {}; //对象
		//选中行的id
		//把选中行的i添加到数据对象中
		obj.ordrNum = $("#jqGrids").getCell(ids[i], "ordrNum").toString();
		obj.isNtChk = x;
		//建一个数组，把单个对象添加到数组中
		rowData[i] = obj;
	}
	if(rowData.length == 0) {
		alert("请选择单据!")
	} else {
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				"list": rowData
			}
		};
		var Data = JSON.stringify(data);
		$.ajax({
			url: url + chk,
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
			success: function(data) {
				alert(data.respHead.message);
				search();
			},
			//结束加载动画
			complete: function() {
				$(".zhezhao").css("display", "none");
				$("#loader").css("display", "none");
			},
			error: function(error) {
				alert(error.responseText)
			}
		})
	}

}
var isclick = true;
//审核与弃审
$(function() {
	//审核
	$(".toExamine").click(function() {
		if(isclick) {
			isclick = false;
			ntChk(1, chk1);
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	});
	//弃审
	$(".noTo").click(function() {
		if(isclick) {
			isclick = false;
			ntChk(0, chk2);
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	})
})

//删除
$(function() {
	$(".delOrder").click(function() {
		//获得选中行的行号
		var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
		//获取选择行的provrId
		var rowData = [];
		for(var i = 0; i < ids.length; i++) {
			var gr = $("#jqGrids").getGridParam('selrow');
			//选中行的id
			var data = $("#jqGrids").getCell(ids[i], "ordrNum");
			//建一个数组，把选中行的id添加到这个数组中去。
			rowData[i] = data;
		}
		if(rowData.length == 0) {
			alert("请选择单据!")
		} else if(confirm("确定删除？")) {
			var ordrNum = rowData.toString();
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"ordrNum": ordrNum
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/whs/prod_stru/deleteAllProdStru',
				type: 'post',
				data: Data,
				dataType: 'json',
				async: true,
				contentType: 'application/json;charset=utf-8',
				success: function(data) {
					alert(data.respHead.message)
//					window.location.reload()
					search();
				},
				error: function() {
					console.log('删除失败')
				}
			})
		}
	})
})

$(function() {
	$("#last_jqGridPager").after('<input id="_input" type="text" value="10"/>')
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
	            url: url + "/mis/whs/prod_stru/uploadRegnFile",
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


var arr=[];
var obj={}
//导出
$(document).on('click', '.exportExcel', function() {
	var momEncd = $("input[name='momEncd']").val();
//	var ordrNum = $(".ordrNum").val();
	var isNtChk = $("#isNtChk").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"momEncd": momEncd,
//			'ordrNum':ordrNum,
			'isNtChk':isNtChk,
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url3 + "/mis/whs/prod_stru/queryListPrint",
		type: 'post',
		data: Data,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		success: function(data) {
			var list = data.respBody.list;
			var execlName = '产品结构'
			ExportData(list, execlName)
		},
		error: function() {
			console.log(error)
		}
	})
	
})
