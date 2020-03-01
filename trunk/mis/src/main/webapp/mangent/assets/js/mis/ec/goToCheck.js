$(function() {
	pageInit();
	$("#mengban").hide()
//	autoWidthJqgrid();
	$(document).on('click', '.biao_storeId', function() {
		window.open("../../Components/baseDoc/storeRecordList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
});

var count;
var pages;
var page = 1;
var rowNum;

//初始化表格
function pageInit() {
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	allHeight()
	jQuery("#jqgrids").jqGrid({
		url: '../../assets/js/json/order.json', //组件创建完成之后请求数据的url
		ajaxGridOptions: {
			contentType: 'application/json; charset=utf-8'
		},
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['店铺编码','店铺名称','平台订单号','订单金额','退款金额','对账单金额','差异'], //jqGrid的列显示名字
		colModel: [{
			name: "storeId",
			align: "center",
			editable: true,
			sortable: false,
//			hidden:true,
		},{
			name: "storeName",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "ecOrderId",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "ordrMoneySum",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "refundMoneySum",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "accountMoneySum",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "difference",
			align: "center",
			editable: true,
			sortable: false,
		}],
		autowidth: true,
		viewrecords: true,
		rownumbers: true,
		loadonce: true,
		collapsed: true,
		height:height,
		autoScroll: true,
		shrinkToFit: false,
		forceFit: true,
		cellEdit: false,
		cellsubmit: "clientArray",
		multiselect: true, //复选框 
		rownumWidth: 30,  //序列号列宽度
		rowNum: 500, //一页显示多少条
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条	
		pager: '#jqGrids', //表格页脚的占位符(一般是div)的id
//		multiboxonly: true,
		//		multiselect: true, //复选框
		caption: "对账单", //表格的标题名字
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
			search()
		},
		footerrow: true,
		gridComplete: function() { 
			var ordrMoneySum = 0;
			var refundMoneySum = 0;
			var accountMoneySum = 0;
			var ids = $("#jqgrids").getDataIDs();
			for(var i = 0; i < ids.length; i++) {
				ordrMoneySum += parseFloat($("#jqgrids").getCell(ids[i],"ordrMoneySum"));
				refundMoneySum += parseFloat($("#jqgrids").getCell(ids[i],"refundMoneySum"));
				accountMoneySum += parseFloat($("#jqgrids").getCell(ids[i],"accountMoneySum"));
			};
			if(isNaN(ordrMoneySum)) {
				ordrMoneySum = 0
			}
			if(isNaN(refundMoneySum)) {
				refundMoneySum = 0
			}
			if(isNaN(accountMoneySum)) {
				accountMoneySum = 0
			}
			ordrMoneySum = ordrMoneySum.toFixed(prec);
			refundMoneySum = refundMoneySum.toFixed(prec);
			accountMoneySum = accountMoneySum.toFixed(prec);
			$("#jqgrids").footerData('set', { 
				"storeId": "本页合计",
				ordrMoneySum: ordrMoneySum,
				refundMoneySum:refundMoneySum,
				accountMoneySum: accountMoneySum
				
			}          );    
		},

	})
}

//function order() {
//	//获得行号
//	var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow');
//	//获得行数据
//	var rowDatas = $("#jqgrids").jqGrid('getRowData', gr);
//	localStorage.setItem("id", rowDatas.id);
//	window.open("../../Components/ec/auditStrategy.html?1");
//
//}

////查询
$(document).on('click', '.search', function() {
	search()
})

function search(){
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var storeId = $("input[name='storeId']").val();
	var startDate = $("input[name='startDate']").val();
	var endDate = $("input[name='endDate']").val();
	if(storeId == '') {
		alert("请选择店铺")
	} else {
		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				"storeId": storeId,
				"startDate": startDate,
				"endDate": endDate,
				"pageSize": rowNum,
				"pageNo": page
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			type: "post",
			contentType: 'application/json; charset=utf-8',
			url: url + '/mis/ec/ecAcccount/goToCheck',
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
				myDate = list;
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
				console.log(error)
			}
		});
	}
}


$(function() {
	//勾兑
	$(".blend").click(function() {
		var startDate = $("input[name='startDate']").val();
		var endDate = $("input[name='endDate']").val();
		//获得选中行的行号
		var ids = $('#jqgrids').jqGrid('getGridParam', 'selarrrow');
		var num = []
		for(var i =0; i<ids.length; i++) {
			var rowData = $("#jqgrids").jqGrid('getRowData', ids[i]);
			num.push(rowData.ecOrderId)
		}
		if(ids.length == 0) {
			alert("请选择单据!")
		} else {
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"startDate": startDate,
					"endDate": endDate,
					"ecOrderIds": num.toString(),
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/ec/ecAcccount/check',
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
					alert(error)
				}
			})
		}
	})
})


//导入
$(function() {
	$(".importExcel").click(function() {
		var files = $("#FileUpload").val()
		var accNum = localStorage.getItem("loginAccNum")
		var fileObj = document.getElementById("FileUpload").files[0];
		var formFile = new FormData();
		formFile.append("action", "UploadVMKImagePath");
		formFile.append("accNum", accNum);
		formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
		var data = formFile;
		if(files != "") {
			$.ajax({
				type: 'post',
				url: url + "/mis/ec/ecAcccount/importAccount",
				data: data,
				dataType: "json",
				cache: false, //上传文件无需缓存
				processData: false, //用于对data参数进行序列化处理 这里必须false
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