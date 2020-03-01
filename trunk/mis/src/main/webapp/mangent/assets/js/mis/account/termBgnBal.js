$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	//点击表格图标显示存货列表
	$(document).on('click', '.biao_invtyEncd', function() {
		window.open("../../Components/baseDoc/invtyList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})
var count;
var pages;
var page = 1;
var rowNum;
$(function() {
	pageInit();
})
//表格初始化
function pageInit() {
	allHeight();
	$("#term_jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['序号', '是否记账', '仓库编码', '仓库名称', '存货编码', '存货名称', '规格型号', '计量单位',
			'数量', '单价', '金额', '批次', '生产日期', '保质期', '失效日期', '存货科目编码', '存货科目名称'
		],
		colModel: [{
				name: 'ordrNum',
				editable: true,
				align: 'center',
				hidden: true
			},
			{
				name: 'isNtBookEntry',
				editable: true,
				align: 'center',
			},
			{
				name: 'whsEncd',
				editable: true,
				align: 'center',
			},
			{
				name: 'whsNm',
				editable: false,
				align: 'center'
			},
			{
				name: 'invtyEncd',
				editable: true,
				align: 'center'
			},
			{
				name: 'invtyNm',
				editable: false,
				align: 'center',
			},
			{
				name: 'spcModel',
				editable: false,
				align: 'center'
			},
			{
				name: 'measrCorpNm',
				editable: false,
				align: 'center'
			},
			{
				name: 'qty',
				editable: true,
				align: 'center',
				sorttype:"integer"
			},
			{
				name: 'noTaxUprc',
				editable: true,
				align: 'center',
				sorttype:"integer"
			},
			{
				name: 'noTaxAmt',
				editable: true,
				align: 'center',
				sorttype:"integer"
			},
			{
				name: 'batNum',
				editable: true,
				align: 'center'
			},
			{
				name: 'prdcDt',
				editable: false,
				align: 'center',
				sorttype:"date"
			},
			{
				name: 'baoZhiQi',
				editable: false,
				align: 'center',
				sorttype:"date"
			},
			{
				name: 'invldtnDt',
				editable: false,
				align: 'center',
				sorttype:"date"
			},
			{
				name: 'invtySubjId',
				editable: false,
				align: 'center'
			},
			{
				name: 'SubjNm',
				editable: false,
				align: 'center'
			}
		],
		sortable:true,
		autowidth: true,
		height: height,
		autoScroll: true,
		shrinkToFit: false,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 500,
		pager: '#jqgridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		multiboxonly: true,
		rowList: [500, 1000, 3000,5000], //可供用户选择一页显示多少条
		footerrow: true,
		caption: "期初余额", //表格的标题名字
		gridComplete: function() {                   
			var qty = $("#term_jqGrids").getCol('qty', false, 'sum');  
			var noTaxAmt = $("#term_jqGrids").getCol('noTaxAmt', false, 'sum');    
			noTaxAmt = precision(noTaxAmt,2) 
			$("#term_jqGrids").footerData('set', { "isNtBookEntry":"本页合计",
				qty: qty,
				noTaxAmt:noTaxAmt
			}          );        
		},
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#term_jqGrids").getGridParam('records'); //获取返回的记录数
			page = $("#term_jqGrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#term_jqGrids").getGridParam('rowNum'); //获取显示配置记录数量

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
	});
}

//查询按钮
$(document).on('click', '.search', function() {
	search();
})

function search() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var invtyEncd = $("#invtyEncd").val();

	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": invtyEncd,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/account/TermBgnBal/queryTermBgnBalList',
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
			var mydata = {};
			var list = data.respBody.list;
			for(var i = 0; i < list.length; i++) {
				if(list[i].isNtBookEntry == 0) {
					list[i].isNtBookEntry = "否"
				} else if(list[i].isNtBookEntry == 1) {
					list[i].isNtBookEntry = "是"
				}
			}
			mydata.rows = list
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#term_jqGrids").jqGrid("clearGridData");
			$("#term_jqGrids").jqGrid("setGridParam", {
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
	});
}

//记账
$(function() {
	$(".account").click(function() {
		var data = {
			"reqHead": reqhead,
			"reqBody": {}
		};
		var Data = JSON.stringify(data);
		$.ajax({
			url: url + '/mis/account/form/final/termBgnBook',
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
				if(data.respHead.isSuccess == true) {
					search();
				}
			},
			error: function() {
				alert("error")
			}
		})
	})
})
//恢复记账
$(function() {
	$(".resump").click(function() {
		var data = {
			"reqHead": reqhead,
			"reqBody": {}
		};
		var Data = JSON.stringify(data);
		$.ajax({
			url: url + '/mis/account/form/final/termBgnBackBook',
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
				if(data.respHead.isSuccess == true) {
					search();
				}
			},
			error: function() {
				alert("error")
			}
		})
	})
})

//删除
$(function() {
	$(".delOrder").click(function() {
		//获得选中行的行号
		var ids = $('#term_jqGrids').jqGrid('getGridParam', 'selarrrow');
		//获取选择行的provrId
		var rowData = [];
		for(var i = 0; i < ids.length; i++) {
			var gr = $("#term_jqGrids").getGridParam('selrow');
			//选中行的id
			var data = $("#term_jqGrids").getCell(ids[i], "ordrNum");
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
					"ordrNum": ordrNum,
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/account/TermBgnBal/deleteTermBgnBalList',
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
						search();
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
				url: url + "/mis/account/TermBgnBal/uploadTermBgnBalFile",
				data: data,
				dataType: "json",
				cache: false, //上传文件无需缓存
				processData: false, //用于对data参数进行序列化处理 这里必须false
				contentType: false, //必须
				success: function(data) {
					alert(data.respHead.message);
					if(data.respHead.isSuccess == true) {
						search();
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
		url: url + '/mis/account/TermBgnBal/queryTermBgnBalListPrint',
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
			var execlName = '期初余额'
			ExportData(list, execlName)
		},
		error: function() {
			alert("error")
		}
	})
})
