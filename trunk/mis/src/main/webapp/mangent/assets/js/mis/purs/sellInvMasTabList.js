var count;
var pages;
var page = 1;
var rowNum;
//表格初始化-销售专用发票列表
$(function() {
	allHeight();
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	$("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['销售发票号', '开票日期', '发票类型','业务类型','销售类型','业务员', '部门','客户', '是否审核','备注', 
		],
		colModel: [{
				name: 'sellInvNum',
				editable: true,
				align: 'center'

			},
			{
				name: 'bllgDt',
				editable: true,
				align: 'center'

			},
			{
				name: 'invTyp',
				editable: false,
				align: 'center',
			},
			{
				name: 'bizTypNm',
				editable: false,
				align: 'center'
			},
			{
				name: 'sellTypNm',
				editable: true,
				align: 'center'
			},
			{
				name: 'bizMemNm',
				editable: true,
				align: 'center'
			},
			{
				name: 'deptNm',
				editable: true,
				align: 'center'
			},
			{
				name: 'custNm',
				editable: true,
				align: 'center'
			},
			{
				name: 'isNtChk',
				editable: true,
				align: 'center'
			},
			{
				name: 'memo',
				editable: true,
				align: 'center'
			},
		],
		autowidth: true,
		height:height,
		autoScroll:true,
		shrinkToFit:false,
		rownumbers: true,
		loadonce: false,
		forceFit: true,
		rowList: [500,1000,3000,5000], //可供用户选择一页显示多少条
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
//		multiboxonly: true,
		rowNum: 500, // 每页多少行，用于分页
		caption: "销售专用发票列表", //表格的标题名字
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
			search();
		},
		//双击弹出采购订单
		ondblClickRow: function(rowid) {
			order(rowid);
		},
	})

})

//查询按钮
$(document).on('click', '.search', function() {
	search()
})
function search () {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var sellInvNum = $("input[name='sellInvNum']").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var custNm = $("input[name='custNm']").val();
	var custId = $("#custId").val();
	var startBllgDt = $("input[name='startBllgDt']").val();
	var endBllgDt = $("input[name='endBllgDt']").val();
	var isNtChk = $("select[name='isNtChk']").val();
	
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"sellInvNum": sellInvNum,
			"custNm": custNm,
			"invtyClsEncd": invtyClsEncd,
			"custId": custId,
//			"bizTypNm": bizTypNm,
			"startBllgDt": startBllgDt,
			"endBllgDt": endBllgDt,
			"isNtChk":isNtChk,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/account/sellInvMasTab/selectSellInvMasTab',
		async: true,
		data: saveData,
		dataType: 'json',
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
				if(list[i].isNtChk==0){
					list[i].isNtChk="否";
				}else if(list[i].isNtChk==1){
					list[i].isNtChk="是";
				}
			}
			var arr=[];
			//执行深度克隆
			for(var i = 0; i < list.length; i++) {
				if(list[i].sellSubList != null) {
					var entrs = list[i].sellSubList
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
			var mydata = {};
			mydata.rows = arr;
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
		error: function() {
			alert("error")
		}
	});

}

function order(rowid) {
	//获得行数据
	var rowDatas = $("#jqGrids").jqGrid('getRowData', rowid);
	localStorage.setItem("sellInvNum", rowDatas.sellInvNum);
	localStorage.setItem("isNtChk", rowDatas.isNtChk);
	window.open("../../Components/purs/sellInvMasTab.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');

}

function ntChk(x) {
	//获得选中行的行号
	var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
	//获取选择行的provrId
	var obj = {}; //对象
	//对象数组
	var rowData = [];
	for(var i = 0; i < ids.length; i++) {
		//选中行的id
		//把选中行的i添加到数据对象中
		obj.sellInvNum = $("#jqGrids").getCell(ids[i], "sellInvNum");
		obj.isNtChk = x;
		rowData[i] = obj
	}
	if(rowData.length == 0) {
		alert("请选择单据!")
	} else {
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				"sellInvMasTabList": rowData
			}
		};
		var Data = JSON.stringify(data);
		$.ajax({
			url: url + '/mis/account/sellInvMasTab/updateSellInvMasTabIsNtChk',
			type: 'post',
			data: Data,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
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
				if(data.respHead.isSuccess==true){
					search()
				}
			},
			error: function() {
				alert("error")
			}
		})
	}

}

//审核与弃审
$(function() {
	//审核
	$(".toExamine").click(function() {
		ntChk(1)
	})
	//弃审
	$(".noTo").click(function() {
		ntChk(0)
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
			var data = $("#jqGrids").getCell(ids[i], "sellInvNum");
			//建一个数组，把选中行的id添加到这个数组中去。
			rowData[i] = data;
		}
		if(rowData.length == 0) {
			alert("请选择单据!")
		} else {
			var sellInvNum = rowData.toString();
			var data = {
				"reqHead": {
					accNum:'1'
				},
				"reqBody": {
					"sellInvNum": sellInvNum
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/account/sellInvMasTab/deleteSellInvMasTabList',
				type: 'post',
				data: Data,
				dataType: 'json',
				async: true,
				contentType: 'application/json;charset=utf-8',
				success: function(data) {
					alert(data.respHead.message)
					if(data.respHead.isSuccess==true){
						search()
					}
				},
				error: function() {
					alert("error")
				}
			})
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
	            url: url + "/mis/account/sellInvMasTab/uploadSellInvMasTabFile",
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
	var sellInvNum = $("input[name='sellInvNum']").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var custNm = $("input[name='custNm']").val();
	var custId = $("#custId").val();
	var startBllgDt = $("input[name='startBllgDt']").val();
	var endBllgDt = $("input[name='endBllgDt']").val();
	var isNtChk = $("select[name='isNtChk']").val();
	
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"sellInvNum": sellInvNum,
			"custNm": custNm,
			"invtyClsEncd": invtyClsEncd,
			"custId": custId,
			"startBllgDt": startBllgDt,
			"endBllgDt": endBllgDt,
			"isNtChk":isNtChk,
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/account/sellInvMasTab/printingSellInvMasTabList',
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
			var execlName = '销售专用发票列表'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
	
})