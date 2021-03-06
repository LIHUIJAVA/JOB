
var pages;
var page = 1;
var rowNum;
var tableSums = {};
$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
})
$(document).keydown(function(event) {
	if(event.keyCode == 13) {
		search();
	}
});
//表格初始化-委托代销调整单列表
$(function() {
	localStorage.removeItem("index")
	localStorage.removeItem("sortorder")
	allHeight();
	$("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['发货单号', '发货日期', '业务类型', '业务员', '部门', '客户简称', '发货地址', '表头备注', '表体备注', '是否审核', '是否退货',
		'仓库名称', '仓库编码', '存货编码', '存货名称', '规格型号', '主计量单位', '主计量单位编码', '数量', '含税单价', '价税合计',
			'无税单价', '无税金额', '税率', '税额', '箱规', '箱数', '对应条形码', '批次', '生产日期', '失效日期', '保质期', '国际批次',],
		colModel: [{
				name: 'delvSnglId',
				editable: true,
				align: 'center',
			},
			{
				name: 'delvSnglDt',
				editable: true,
				align: 'center',
				sorttype:'date' // 时间排序

			},
			{
				name: 'bizTypNm',
				editable: false,
				align: 'center',
			},
			{
				name: 'userName',
				editable: false,
				align: 'center'
			},
			{
				name: 'deptName',
				editable: false,
				align: 'center'
			},
			{
				name: 'custNm',
				editable: false,
				align: 'center',
				hidden: true
			},
			{
				name: 'recvrAddr',
				editable: false,
				align: 'center',
			},
			{
				name: 'memo',
				editable: true,
				align: 'center'
			},
			{
				name: 'memos',
				editable: true,
				align: 'center'
			},
			{
				name: 'isNtChk',
				editable: true,
				align: 'center'
			},
			{
				name: 'isNtRtnGood',
				editable: true,
				align: 'center'
			},
			{
				name: 'whsNm',
				editable: false,
				sortable: false,
			},
			{
				name: 'whsEncd',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'invtyEncd',
				editable: true,
				align: 'center',
				sortable: false

			},
			{
				name: 'invtyNm',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'spcModel',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'measrCorpNm',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'measrCorpId',
				editable: false,
				align: 'center',
				hidden: true,
				sortable: false
			},
			{
				name: 'qty',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'cntnTaxUprc',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'prcTaxSum',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'noTaxUprc',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'noTaxAmt',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'taxRate',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'taxAmt',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'bxRule',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'bxQty',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'crspdBarCd',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'batNum',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'prdcDt', //生产日期
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'invldtnDt', //失效日期
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'baoZhiQi',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'intlBat', //国际批次
				editable: true,
				align: 'center',
				sortable: false
			},
		],
		autowidth: true,
		height: height,
		autoScroll: true,
		sortable:true,// 列拖拽
		shrinkToFit: false,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 500,
		rowList: [500, 1000, 3000,5000], //可供用户选择一页显示多少条
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
//		multiboxonly: true,
		caption: "委托代销发货单列表", //表格的标题名字
		footerrow: true,
		gridComplete: function() {
			$("#jqGrids").footerData('set', { 
				"delvSnglId": "总计",
				"qty": tableSums.qty,
				"bxQty": tableSums.bxQty,
				"prcTaxSum": tableSums.prcTaxSum,
				"noTaxAmt": tableSums.noTaxAmt,
				"taxAmt": tableSums.taxAmt
			}          );
		},
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
			var index = localStorage.getItem("index")
			var sortorder = localStorage.getItem("sortorder")
			if(index == null) {
				var index = ''
			}
			if(sortorder == null) {
				var sortorder = 'desc'
			}
			search(index, sortorder);
		},
		//双击弹出采购订单
		ondblClickRow: function(rowid) {
			order(rowid);
		},
		onSortCol: function(index, colindex, sortorder) {
			switch(index) {
				case "delvSnglId":
					index = "ead.delv_sngl_id"
					break;
				case "delvSnglDt":
					index = "ead.delv_sngl_dt"
					break;
				case "qty":
					index = "eads.qty"
					break;
				case "cntnTaxUprc":
					index = "eads.cntn_tax_uprc"
					break;
				case "prcTaxSum":
					index = "eads.prc_tax_sum"
					break;
				case "noTaxUprc":
					index = "eads.no_tax_uprc"
					break;
				case "noTaxAmt":
					index = "eads.no_tax_amt"
					break;
				case "taxAmt":
					index = "eads.tax_amt"
					break;
				case "bxQty":
					index = "eads.bx_qty"
					break;
				case "prdcDt":
					index = "eads.prdc_dt"
					break;
				case "invldtnDt":
					index = "eads.invldtn_dt"
					break;
			}
			localStorage.setItem("index",index)
			localStorage.setItem("sortorder",sortorder)
			window.index = index;
			window.sortorder = sortorder;
			search(index, sortorder)
		}
	})

})
//$(function() {
//	$("#last_jqGridPager").after('<input id="_input" type="number" value="10"/>')
//})
var isNtRtnGoods = "";

//查询按钮
$(document).on('click', '.search', function() {
	var index = ''
	var sortorder = 'desc'
	search(index, sortorder)
})

function search(index, sortorder) {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)

	var delvSnglId = $(".delvSnglId").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var delvSnglDt1 = $(".delvSnglDt1").val();
	var delvSnglDt2 = $(".delvSnglDt2").val();
	var invtyEncd = $("#invtyEncd").val();
	var custId = $("#custId").val();
	var whsEncd = $("#whsEncd").val();
	var isNtChk = $("#isNtChk").val();
	var memo = $("#memo").val();

	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"delvSnglId": delvSnglId,
			"delvSnglDt1": delvSnglDt1,
			"invtyClsEncd": invtyClsEncd,
			"delvSnglDt2": delvSnglDt2,
			"custId": custId,
			"invtyEncd": invtyEncd,
			"whsEncd": whsEncd, 
			"isNtChk": isNtChk,
			"sort":index,
			"memo":memo,
			"sortOrder":sortorder,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/purc/EntrsAgnDelv/queryEntrsAgnDelvListOrderBy',
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
			tableSums = data.respBody.tableSums;
			var list = data.respBody.list;
			for(var i = 0; i < list.length; i++) {
				if(list[i].isNtChk == 0) {
					list[i].isNtChk = "否"
				} else if(list[i].isNtChk == 1) {
					list[i].isNtChk = "是"
				}
			}
			for(var i = 0; i < list.length; i++) {
				if(list[i].isNtRtnGood == 0) {
					list[i].isNtRtnGood = "发货"
				} else if(list[i].isNtRtnGood == 1) {
					list[i].isNtRtnGood = "退货"
				}
			}
			var mydata = {};
			mydata.rows = list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#jqGrids").jqGrid("clearGridData");
			$("#jqGrids").jqGrid("setGridParam", {
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
			alert("eror")
		}
	});

}

function order(rowid) {
	//获得行数据
	var rowDatas = $("#jqGrids").jqGrid('getRowData', rowid);
	isNtRtnGood = rowDatas.isNtRtnGood;
	localStorage.setItem("isNtChk", rowDatas.isNtChk);
	localStorage.setItem("delvSnglId", rowDatas.delvSnglId);
	if(isNtRtnGood == "发货") {
		window.open("../../Components/sell/entrsAgnDelv.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');
	} else if(isNtRtnGood == "退货") {
		window.open("../../Components/sell/entrsAgnReturn.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');
	}
}

function ntChk(x) {
	//获得选中行的行号
	var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
	//获取选择行的provrId
	//对象数组
	var rowData = [];
	for(var i = 0; i < ids.length; i++) {
		var isNtRtnGood = $("#jqGrids").getCell(ids[i], "isNtRtnGood")
		if(isNtRtnGood == "退货") {
			isNtRtnGood = 1;
		} else if(isNtRtnGood == "发货") {
			isNtRtnGood = 0;
		}
	}
	for(var i = 0; i < ids.length; i++) {
		var obj = {}; //对象
		//选中行的id
		//把选中行的i添加到数据对象中
		obj.delvSnglId = $("#jqGrids").getCell(ids[i], "delvSnglId").toString();
		obj.isNtChk = x;
		obj.isNtRtnGood = isNtRtnGood;
		//建一个数组，把单个对象添加到数组中
		rowData[i] = obj;
	}
	var result = [];
	var obj = {};
	for(var i = 0; i < rowData.length; i++) {
		if(!obj[rowData[i].delvSnglId]) {
			result.push(rowData[i]);
			obj[rowData[i].delvSnglId] = true;
		}
	}
	if(result.length == 0) {
		alert("请选择单据!")
	} else {
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				"list": result
			}
		};
		var Data = JSON.stringify(data);
		$.ajax({
			url: url + '/mis/purc/EntrsAgnDelv/updateEntrsAgnDelvIsNtChkList',
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
				alert("审核失败")
			}
		})
	}
}

//审核与弃审
$(function() {
	var isclick = true;
	//审核
	$(".toExamine").click(function() {
		if(isclick) {
			isclick = false;
			ntChk(1);
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	});
	//弃审
	$(".noTo").click(function() {
		if(isclick) {
			isclick = false;
			ntChk(0);
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
			var data = $("#jqGrids").getCell(ids[i], "delvSnglId");
			//建一个数组，把选中行的id添加到这个数组中去。
			rowData[i] = data;
		}
		var result = [];
		var obj = {};
		for(var i = 0; i < rowData.length; i++) {
			if(!obj[rowData[i]]) {
				result.push(rowData[i]);
				obj[rowData[i]] = true;
			}
		}
		if(result.length == 0) {
			alert("请选择单据!")
		} else if(confirm("确定删除？")) {
			var delvSnglId = result.toString();
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"delvSnglId": delvSnglId
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/purc/EntrsAgnDelv/deleteEntrsAgnDelv',
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
				url: url + "/mis/purc/EntrsAgnDelv/uploadEntrsAgnDelvFile",
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

//导出
$(document).on('click', '.exportExcel', function() {
	var delvSnglId = $(".delvSnglId").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var delvSnglDt1 = $(".delvSnglDt1").val();
	var delvSnglDt2 = $(".delvSnglDt2").val();
	var invtyEncd = $("#invtyEncd").val();
	var custId = $("#custId").val();
	var whsEncd = $("#whsEncd").val();
	var isNtChk = $("#isNtChk").val();
	var memo  = $("#memo").val();
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"delvSnglId": delvSnglId,
			"delvSnglDt1": delvSnglDt1,
			"invtyClsEncd": invtyClsEncd,
			"delvSnglDt2": delvSnglDt2,
			"custId": custId,
			"invtyEncd": invtyEncd,
			"whsEncd": whsEncd, 
			"isNtChk": isNtChk,
			"memo":memo
		}
	};
	var Data = JSON.stringify(savedata);
	$.ajax({
		url: url + '/mis/purc/EntrsAgnDelv/printingEntrsAgnDelvList',
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
			var execlName = '委托代销发货单列表'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
})