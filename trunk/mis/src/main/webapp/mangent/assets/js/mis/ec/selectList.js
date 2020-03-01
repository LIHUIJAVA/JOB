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
		colNames: ['店铺编码','店铺名称','业务单号','电商订单号','单据类型','商户订单号','商品编号','商品名称','费用发生时间','计费时间','结算时间','费用项','金额','收支类型','备注','店铺号','收支方向','账单日期','勾兑结果','勾兑人编码','勾兑人','勾兑时间','是否勾兑项','创建人','创建时间',], //jqGrid的列显示名字
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
			name: "billNo",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "ecOrderId",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "orderType",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "shOrderId",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "goodNo",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "goodName",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "startTime",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "jifeiTime",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "jiesuanTime",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "costType",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "money",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "moneyType",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "memo",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "shopId",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "direction",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "costDate",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "checkResult",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "checkerId",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "checkerName",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "checkTime",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "isCheckType",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "creator",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "createTime",
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
		multiboxonly: true,
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
			var money = 0;
			var ids = $("#jqgrids").getDataIDs();
			for(var i = 0; i < ids.length; i++) {
				money += parseFloat($("#jqgrids").getCell(ids[i],"money"));
			};
			if(isNaN(money)) {
				money = 0
			}
			money = money.toFixed(prec);
			$("#jqgrids").footerData('set', { 
				"storeId": "本页合计",
				money: money
			}          );    
		},

	})
	$("#jqgrids").jqGrid('navGrid', '#jqGrids', {
		edit: false,
		add: false,
		del: false,
		search: false,
		refresh: false,
	}).navButtonAdd('#jqGrids', {
		caption: "",
		buttonicon: "ui-icon-trash",
		onClickButton: function() {
			var grid = "jqgrids"
			//删除一行操作
			removeRows(grid);
		},
		position: "first"
	})
}


////查询
$(document).on('click', '.search', function() {
	search()
})

function search(){
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
//	alert(1)
//	var myDate = {};
	var storeId = $("input[name='storeId']").val();
	var ecOrderId = $("input[name='ecOrderId']").val();
	var checkResult = $("select[name='checkResult']").val();
	var isCheckType = $("select[name='isCheckType']").val();
	var checkerId = $("input[name='accNum']").val();
	var startTime1 = $("input[name='startTime1']").val();
	var startTime2 = $("input[name='startTime2']").val();
	var checkTime1 = $("input[name='checkTime1']").val();
	var checkTime2 = $("input[name='checkTime2']").val();
	var jiesuanTime1 = $("input[name='jiesuanTime1']").val();
	var jiesuanTime2 = $("input[name='jiesuanTime2']").val();
	if(storeId == '') {
		alert("请选择店铺")
	} else {
		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				"storeId": storeId,
				"ecOrderId": ecOrderId,
				"checkResult": checkResult,
				"isCheckType": isCheckType,
				"checkerId": checkerId,
				"startTime1": startTime1,
				"startTime2": startTime2,
				"checkTime1": checkTime1,
				"checkTime2": checkTime2,
				"jiesuanTime1": jiesuanTime1,
				"jiesuanTime2": jiesuanTime2,
				"pageSize": rowNum,
				"pageNo": page
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			type: "post",
			contentType: 'application/json; charset=utf-8',
			url: url + '/mis/ec/ecAcccount/selectList',
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
				for(var i = 0; i < list.length;i++) {
					if(list[i].moneyType == 0) {
						list[i].moneyType = "收"
					}else if(list[i].moneyType == 1) {
						list[i].moneyType = "支"
					}
					
					if(list[i].direction == 0) {
						list[i].direction = "收入"
					} else if(list[i].direction == 1) {
						list[i].direction = "支出"
					}
					
					if(list[i].checkResult == 0) {
						list[i].checkResult = "未勾兑"
					} else if(list[i].checkResult == 1) {
						list[i].checkResult = "已勾兑"
					}
					
					if(list[i].isCheckType == 0) {
						list[i].isCheckType = "否"
					} else if(list[i].isCheckType == 1) {
						list[i].isCheckType = "是"
					}
					
				}
				
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
	//删除
	$(".delOrder").click(function() {
		//获得选中行的行号
		var ids = $('#jqgrids').jqGrid('getGridParam', 'selarrrow');
		var num = []
		for(var i =0; i<ids.length; i++) {
			var rowData = $("#jqgrids").jqGrid('getRowData', ids[i]);
			num.push(rowData.billNo)
		}
		if(ids.length == 0) {
			alert("请选择单据!")
		} else if(confirm("确定删除？")) {
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"billNos": num.toString()
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/ec/ecAcccount/delete',
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
					alert("删除失败")
				}
			})
		}
	})
	// 自动勾兑
	$(".blend").click(function() {
		if(confirm("是否自动勾兑？")) {
			var data = {
				"reqHead": reqhead,
				"reqBody": {
				}
			};
			var Data = JSON.stringify(data);
			console.log(Data)
			$.ajax({
				url: url + '/mis/ec/ecAcccount/autoCheck',
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
					$(".down").hide()
					$(".down").css("opacity",0)
					$("#mengban").hide()
					alert(data.respHead.message)
				},
				error: function() {
					alert(error)
				},
				//结束加载动画
				complete: function() {
					$(".zhezhao").css("display", "none");
					$("#loader").css("display", "none");
				},
			})
		}
	})
})


//导入
$(function() {
	$(".importExcel").click(function() {
		$("#mengban").show()
		$(".down1").show()
		$(".down1").css("opacity",1)
		$(".true1").removeClass("gray");
		$(".falses1").removeClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true);
		
		$("input[name='storeId1']").attr('id','storeId');
        $("input[name='storeId']").attr('id','');
		$("input[name='storeName1']").attr('id','storeName');
        $("input[name='storeName']").attr('id','');
//      var whsEncd1 = $("input[name='whsEncd1']").val()
//      var whsEncd2 = $("input[name='whsEncd2']").val(whsEncd1)
	})
	$(".falses1").click(function() {
		$(".down1").hide()
		$(".down1").css("opacity",0)
		$("#mengban").hide()
		$("input[name='storeId1']").attr('id', '');
		$("input[name='storeId']").attr('id', 'storeId');
		$("input[name='storeName1']").attr('id', '');
		$("input[name='storeName']").attr('id', 'storeName');
		$("input[name='storeId1']").val('')
		$("input[name='storeName1']").val('')
	})
	$(".true1").click(function() {
		var storeId = $("input[name='storeId1']").val()
		var files = $("#FileUpload").val()
		var accNum = localStorage.getItem("loginAccNum")
		var fileObj = document.getElementById("FileUpload").files[0];
		var formFile = new FormData();
		formFile.append("action", "UploadVMKImagePath");
		formFile.append("accNum", accNum);
		formFile.append("storeId", storeId);
		formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
		var data = formFile;
		if(files != "") {
			if(storeId == '') {
				alert("请选择店铺后导入")
			} else {
				$.ajax({
					type: 'post',
					url: url + "/mis/ec/ecAcccount/importAccount",
					data: data,
					dataType: "json",
					cache: false, //上传文件无需缓存
					processData: false, //用于对data参数进行序列化处理 这里必须false
					contentType: false, //必须
					//开始加载动画  添加到ajax里面
					beforeSend: function() {
						$(".zhezhao").css("display", "block");
						$("#loader").css("display", "block");
			
					},
					success: function(data) {
						$(".down1").hide()
						$(".down1").css("opacity",0)
						$("#mengban").hide()
						$("input[name='storeId1']").attr('id', '');
						$("input[name='storeId']").attr('id', 'storeId');
						$("input[name='storeName1']").attr('id', '');
						$("input[name='storeName']").attr('id', 'storeName');
						alert(data.respHead.message)
					},
					error: function() {
						console.log(error)
					},
					//结束加载动画
					complete: function() {
						$(".zhezhao").css("display", "none");
						$("#loader").css("display", "none");
					},
				});
			}
		} else {
			alert("请选择文件")
		}
	});
});