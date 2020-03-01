var count;
var pages;
var page = 1;
var rowNum;
$(function() {
	//	仓库
	$(document).on('click', '.whsEncd_biaoge', function() {
		window.open("../../Components/baseDoc/whsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	//	存货编码
	$(document).on('click', '.invtyEncd_biaoge', function() {
		window.open("../../Components/baseDoc/invtyList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	//	批号
	$(document).on('click', '.batNum_biaoge', function() {
		window.open("../../Components/baseDoc/batNum.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})
//表格初始化
$(function() {
	localStorage.removeItem("index")
	localStorage.removeItem("sortorder")
	allHeight()
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	$("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['仓库编码', '仓库名称', '批号', '生产日期', '保质期', '失效日期',
					'存货编码','存货名称','存货代码','规格型号','主计量单位','结存数量'
		],
		colModel: [{
				name: 'whsEncd',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'whsNm',
				editable: true,
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
				name: 'prdcDt',
				editable: false,
				align: 'center',
			},
			{
				name: 'baoZhiQi',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'invldtnDt',
				editable: false,
				align: 'center'
			},
			{
				name: 'invtyEncd',
				editable: false,
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
				name: 'invtyCd',
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
				name: 'nowStok',
				editable: false,
				align: 'center'
			}
		],
		autowidth: true,
		height:height,
		rowNum: 500, //一页显示多少条
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条	
		autoScroll:true,
		shrinkToFit:false,
		rownumbers: true,
		sortable:true,
		loadonce: false,
		forceFit: true,
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		multiboxonly: true,
		caption: "失效日期维护", //表格的标题名字
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
		onSortCol: function(index, colindex, sortorder) {
			localStorage.setItem("index",index)
			localStorage.setItem("sortorder",sortorder)
			window.index = index;
			window.sortorder = sortorder;
			search(index, sortorder, newPage)   
		}
	})
})
//查询按钮
$(document).on('click', '.search', function() {
	var initial = 1;
	window.newPage = initial;
	var index = '';
	var sortorder = '';
	search(index, sortorder,initial)
})
//查询按钮
function search(index, sortorder,page) {
	var whsEncd = $("input[name='whsEncd']").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var batNum = $("input[name='batNum']").val();
	var invldtnDt = $("input[name='invldtnDt']").val();
	var prdc = $("input[name='prdc']").val();
	var invldtn = $("input[name='invldtn']").val();
	var isBalance = $("select[name='isBalance']").val();
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)

	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"whsEncd": whsEncd,
			"invtyEncd": invtyEncd,
			"batNum": batNum,
			"invldtnDt": invldtnDt,
			"prdc": prdc,
			"isBalance": isBalance,
			"invldtn": invldtn,
			"sort":index,
			"sortOrder":sortorder,
			"pageNo": page,
			"pageSize": rowNum
			
		}
	};
	var saveData = JSON.stringify(savedata)
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/whs/invty_tab/selectInvldtnDtList',
//		url: url + '/mis/ec/proActivity/queryList',
		async: true,
		data: saveData,
		dataType: 'json',
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
			console.log(error)
		}
	});
}


var arr=[];
var obj={}
//导出
$(document).on('click', '.exportExcel', function() {
	
	var whsEncd = $("input[name='whsEncd']").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var batNum = $("input[name='batNum']").val();
	var invldtnDt = $("input[name='invldtnDt']").val();
	var prdc = $("input[name='prdc']").val();
	var invldtn = $("input[name='invldtn']").val();
	var isBalance = $("select[name='isBalance']").val();
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"whsEncd": whsEncd,
			"invtyEncd": invtyEncd,
			"batNum": batNum,
			"invldtnDt": invldtnDt,
			"prdc": prdc,
			"isBalance": isBalance,
			"invldtn": invldtn
			
		}
	};
	var Data = JSON.stringify(savedata);
	$.ajax({
		url: url3 + "/mis/whs/invty_tab/selectInvldtnDtList",
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
			var execlName = '失效日期维护'
			ExportData(list, execlName)
		},
		error: function() {
			console.log(error)
		}
	})
	
})