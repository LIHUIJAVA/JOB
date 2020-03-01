var count;
var pages;
var rowNum;
$(function() {
	localStorage.removeItem("index")
	localStorage.removeItem("sortorder")
	allHeight()
	$("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['盘点单号', '盘点日期', '账面日期','仓库编码', '仓库名称', '货位编码', '货位名称', '盘点批号','盘点状态', '表头备注', '是否向WMS上传', "是否审核",
		'是否记账','是否完成','是否关闭','打印次数','创建人','创建时间','修改人','修改时间','审核人','审核时间','记账人','记账时间','操作员',
		'操作员编码','单据类型编码','单据类型名称','序号','存货编码','存货大类编码','条形码','批号','生产日期','保质期','失效日期','账面数量',
		'盘点数量','盈亏数量','账面调节数量','调整入库数量','盈亏比例(%)','调整出库数量','原因','存货名称','规格型号','存货代码','箱规','计量单位名称','计量单位编码'], //jqGrid的列显示名字
		colModel: [{
				name: "checkFormNum",
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: "checkDt",
				align: "center",
				editable: true,
			},
			{
				name: "bookDt",
				align: "center",
				editable: true,
			},
			{
				name: "whsEncd",
				align: "center",
				editable: true,
				sortable: false,
//				hidden: true
			},
			{
				name: "whsNm",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: "gdsBitEncd",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: "gdsBitNm",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'checkBat',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'checkStatus',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			},
			{
				name: 'memo',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'isNtWms',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			},
			{
				name: 'isNtChk',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'isNtBookEntry',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			},
			{
				name: 'isNtCmplt',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			},
			{
				name: 'isNtClos',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			},
			{
				name: 'printCnt',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			},
			{
				name: 'setupPers',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'setupTm',
				align: "center",
				editable: true,
			},
			{
				name: 'mdfr',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'modiTm',
				align: "center",
				editable: true,
			},
			{
				name: 'chkr',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'chkTm',
				align: "center",
				editable: true,
			},
			{
				name: 'bookEntryPers',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			},
			{
				name: 'bookEntryTm',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			},
			{
				name: 'operator',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			},
			{
				name: 'operatorId',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			},
			{
				name: 'formTypEncd',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'formTypName',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'ordrNum',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			},
			{
				name: 'invtyEncd',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'invtyBigClsEncd',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'barCd',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'batNum',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'prdcDt',
				align: "center",
				editable: true,
			},
			{
				name: 'baoZhiQi',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'invldtnDt',
				align: "center",
				editable: true,
			},
			{
				name: 'bookQty',
				align: "center",
				editable: true,
			},
			{
				name: 'checkQty',
				align: "center",
				editable: true,
			},
			{
				name: 'prftLossQty',
				align: "center",
				editable: true,
			},
			{
				name: 'bookAdjustQty',
				align: "center",
				editable: true,
			},
			{
				name: 'adjIntoWhsQty',
				align: "center",
				editable: true,
			},
			{
				name: 'prftLossRatio',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'adjOutWhsQty',
				align: "center",
				editable: true,
				sorttype:'integer',
			},
			{
				name: 'reasn',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'invtyNm',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'spcModel',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'invtyCd',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'bxRule',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'measrCorpNm',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'measrCorpId',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			}
		],
		autowidth: true,
		autoScroll: true,
		shrinkToFit: false,
		sortable:true,
		rownumbers: true,
		height: height,
		loadonce: true,
		forceFit: true,
		rowNum:500,
		rowList: [500, 1000, 3000,5000],
		pager: '#jqgridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
//		multiboxonly: true,
		caption: "盘点单列表", //表格的标题名字
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
				var sortorder = ''
			}
			window.newPage = page;
			search(index, sortorder, page)
		},
		//双击弹出采购订单
		ondblClickRow: function(rowid) {
			order(rowid);
		},
		footerrow: true,
		gridComplete: function() { 
			var bookQty = $("#jqGrids").getCol('bookQty', false, 'sum');
			var checkQty = $("#jqGrids").getCol('checkQty', false, 'sum');
			var prftLossQty = $("#jqGrids").getCol('prftLossQty', false, 'sum');
			var bookAdjustQty = $("#jqGrids").getCol('bookAdjustQty', false, 'sum');
			var adjIntoWhsQty = $("#jqGrids").getCol('adjIntoWhsQty', false, 'sum');
			var adjOutWhsQty = $("#jqGrids").getCol('adjOutWhsQty', false, 'sum');
			
			$("#jqGrids").footerData('set', { 
				"checkFormNum": "本页合计",
				bookQty: bookQty.toFixed(prec),
				checkQty:checkQty.toFixed(prec),
				prftLossQty: prftLossQty.toFixed(prec),
				bookAdjustQty:bookAdjustQty.toFixed(prec),
				adjIntoWhsQty: adjIntoWhsQty.toFixed(prec),
				adjOutWhsQty:adjOutWhsQty.toFixed(prec),
				
			}          );    
		},
		onSortCol: function(index, colindex, sortorder) {
			switch(index) {
				case "checkDt":
					index = "cs.check_dt"
					break;
				case "bookDt":
					index = "cs.book_dt"
					break;
				case "setupTm":
					index = "cs.setup_tm"
					break;
				case "modiTm":
					index = "cs.modi_tm"
					break;
				case "chkTm":
					index = "cs.chk_tm"
					break;
				case "prdcDt":
					index = "csTab.prdc_dt"
					break;
				case "invldtnDt":
					index = "csTab.invldtn_dt"
					break;
				case "bookQty":
					index = "csTab.book_qty"
					break;
				case "checkQty":
					index = "csTab.check_qty"
					break;
				case "prftLossQty":
					index = "csTab.prft_loss_qty"
					break;
				case "bookAdjustQty":
					index = "csTab.book_adjust_qty"
					break;
				case "adjIntoWhsQty":
					index = "csTab.adj_into_whs_qty"
					break;
				case "adjOutWhsQty":
					index = "csTab.adj_out_whs_qty"
					break;
			}
			localStorage.setItem("index",index)
			localStorage.setItem("sortorder",sortorder)
			window.index = index;
			window.sortorder = sortorder;
			search(index, sortorder, newPage)    
		}
	})
})

//整单联查
$(document).on('click', '.joint_search', function() {
	var gr = $("#jqGrids").jqGrid('getGridParam', 'selrow');
	if(gr == null) {
		alert("请选择单据")
	} else {
		//获得行数据
		var rowDatas = $("#jqGrids").jqGrid('getRowData', gr);
		var formNum = rowDatas.checkFormNum;
		var formTypEncd = rowDatas.formTypEncd;
		window.open("../../Components/system/jointSearch.html?formNum=" + formNum + "&formTypEncd=" + formTypEncd, 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	}
})

//点击表格图标显示仓库列表
$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");

	$(document).on('click', '.whsNm', function() {
		window.open("../../Components/baseDoc/whsList.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
//	存货分类
	$(document).on('click', '.invtyClsEncd_biaoge', function() {
		window.open("../../Components/baseDoc/invtyTree.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})

//查询按钮
$(function() {
	$(document).on('click', '#find', function() {
		var initial = 1;
		window.newPage = initial;
		var index = '';
		var sortorder = '';
		search(index, sortorder,initial)
	})
})

function search(index, sortorder, page){
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var checkFormNum = $(".checkFormNum").val();
	var checkDt1 = $(".checkDt1").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var checkDt2 = $(".checkDt2").val();
	var whsEncd = $("input[name='whsEncd']").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var memo = $("input[name='memo']").val();
	var batNum = $("input[name='batNum']").val();
	var isNtChk = $("#isNtChk").val();
	var setupPers = $("input[name='userName']").val()

	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"checkFormNum": checkFormNum,
			"checkDt1": checkDt1,
			"invtyClsEncd": invtyClsEncd,
			"checkDt2": checkDt2,
			"whsEncd": whsEncd,
			"invtyEncd": invtyEncd,
			"batNum": batNum,
			"memo": memo,
			"setupPers": setupPers,
			'isNtChk':isNtChk,
			"sort":index,
			"sortOrder":sortorder,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/whs/check_sngl/queryList',
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
			for(var i = 0; i < list.length; i++) {
				if(list[i].isNtChk == 0) {
					list[i].isNtChk = "否"
				} else if(list[i].isNtChk == 1) {
					list[i].isNtChk = "是"
				}
				if(list[i].isNtWms == 0) {
					list[i].isNtWms = "否"
				} else if(list[i].isNtWms == 1) {
					list[i].isNtWms = "是"
				}
				if(list[i].isNtBookEntry == 0) {
					list[i].isNtBookEntry = "否"
				} else if(list[i].isNtBookEntry == 1) {
					list[i].isNtBookEntry = "是"
				}
				if(list[i].isNtCmplt == 0) {
					list[i].isNtCmplt = "否"
				} else if(list[i].isNtCmplt == 1) {
					list[i].isNtCmplt = "是"
				}
				if(list[i].isNtClos == 0) {
					list[i].isNtClos = "否"
				} else if(list[i].isNtClos == 1) {
					list[i].isNtClos = "是"
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
			alert("查询失败")
		}
	});

}

function order(rowid) {
	//获得行数据
	var rowDatas = $("#jqGrids").jqGrid('getRowData', rowid);
	localStorage.setItem("checkFormNum", rowDatas.checkFormNum);
//	localStorage.setItem("isNtChk", rowDatas.isNtChk);
	window.open("../../Components/whs/checkSngl.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');

}

var chk1 = "/mis/whs/check_sngl/updateCSnglChk";
var chk2 = "/mis/whs/check_sngl/updateCSnglNoChk";

function ntChk(x, chk) {
	//获得选中行的行号
	var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
	//获取选择行的provrId
	//对象数组
	var rowData = [];
	for(var i = 0; i < ids.length; i++) {
		var obj = {}; //对象
		//选中行的id
		//把选中行的i添加到数据对象中
		obj.formNum = $("#jqGrids").getCell(ids[i], "checkFormNum").toString();
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
				alert(data.respHead.message);
				if(data.respHead.isSuccess==true){
					var initial = 1;
					window.newPage = initial;
					var index = '';
					var sortorder = '';
					search(index, sortorder,initial)
				}
			},
			error: function() {
				alert("审核失败")
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
			var data = $("#jqGrids").getCell(ids[i], "checkFormNum");
			//建一个数组，把选中行的id添加到这个数组中去。
			rowData[i] = data;
		}
		if(rowData.length == 0) {
			alert("请选择单据!")
		} else if(confirm("确定删除？")) {
			var checkFormNum = rowData.toString();
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"checkFormNum": checkFormNum
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/whs/check_sngl/deleteAllCheckSngl',
				type: 'post',
				data: Data,
				dataType: 'json',
				async: true,
				contentType: 'application/json;charset=utf-8',
				success: function(data) {
					alert(data.respHead.message);
					if(data.respHead.isSuccess==true){
						var initial = 1;
						window.newPage = initial;
						var index = '';
						var sortorder = '';
						search(index, sortorder,initial)
					}
				},
				error: function() {
					alert("删除失败")
				}
			})
		}
	})
})
// 导入
$(document).ready(function(){
    $('.dropdown-menu li').click(function(){
        var rs=$(this).text();
        $('#dropdownMenu2').text(rs);
    });
    //  	alert("U8导入")
    $('.dropdown-menu .li1').click(function(){
		var files = $("#FileUpload").val()
		var fileObj = document.getElementById("FileUpload").files[0];
		var formFile = new FormData();
		formFile.append("action", "UploadVMKImagePath");
		formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
		var data = formFile;
		if(files != "") {
			$.ajax({
				type: 'post',
				url: url + "/mis/whs/check_sngl/uploadFileAddDbU8",
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
    //  	alert("新系统导入")
    $('.dropdown-menu .li2').click(function(){
		var files = $("#FileUpload").val()
		var fileObj = document.getElementById("FileUpload").files[0];
		var formFile = new FormData();
		formFile.append("action", "UploadVMKImagePath");
		formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
		var data = formFile;
		if(files != "") {
			$.ajax({
				type: 'post',
				url: url + "/mis/whs/check_sngl/uploadFileAddDb",
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

var arr=[];
var obj={}
//导出
$(document).on('click', '.exportExcel', function() {
	var checkFormNum = $(".checkFormNum").val();
	var checkDt1 = $(".checkDt1").val();
	var checkDt2 = $(".checkDt2").val();
	var whsEncd = $(".whsNm").val();
	var isNtChk = $("#isNtChk").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"checkFormNum": checkFormNum,
			"checkDt1": checkDt1,
			"checkDt2": checkDt2,
			"whsEncd": whsEncd,
			"invtyClsEncd": invtyClsEncd,
			'isNtChk':isNtChk
		}
	};
	var Data = JSON.stringify(savedata);
	$.ajax({
		url: url + '/mis/whs/check_sngl/queryListDaYin',
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
			var execlName = '盘点单列表'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
	
})