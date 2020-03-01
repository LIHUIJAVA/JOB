$(function() {
	//点击表格图标显示仓库列表
	$(document).on('click', '.biao_whsEncd', function() {
		window.open("../../Components/baseDoc/whsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	$(document).on('click', '.invtyNm_biaoge', function() {
		window.open("../../Components/baseDoc/invtyList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	$(document).on('click', '.batNum_biaoge', function() {
		window.open("../../Components/baseDoc/batNum.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
//	存货分类
	$(document).on('click', '.invtyClsEncd_biaoge', function() {
		window.open("../../Components/baseDoc/invtyTree.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})
var count;
var pages;
var rowNum;

$(function() {
	pageInit();
});

function pageInit() {
	localStorage.removeItem("index")
	localStorage.removeItem("sortorder")
	allHeight();
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	jQuery("#bzq_jqGrids").jqGrid({
		url: '../../assets/js/json/order.json', //组件创建完成之后请求数据的url
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['仓库编码', '仓库名称', '存货编码', '存货名称', '规格型号', '主计量单位', '结存数量', '含税单价', '未税单价', '含税金额', '未税金额', '批次', '生产日期', '保质期(天)', '失效日期', '剩余天数', '状态', '箱规'], //jqGrid的列显示名字
		colModel: [{
				name: "whs_encd",
				align: "center",
				editable: true,
				sortable: false,
			},{
				name: "whs_nm",
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: "invty_encd",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: "invty_nm",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'spc_model',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'measr_corp_nm',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'now_stok',
				align: "center",
				editable: true,
			},
			{
				name: 'cntn_tax_uprc',
				align: "center",
				editable: true,
			},
			{
				name: 'un_tax_uprc',
				align: "center",
				editable: true,
			},
			{
				name: 'cntn_tax_amt',
				align: "center",
				editable: true,
			},
			{
				name: 'un_tax_amt',
				align: "center",
				editable: true,
			},
			{
				name: 'bat_num',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'prdcdt',
				align: "center",
				editable: true,
			},
			{
				name: 'bao_zhi_qi',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'invldtndt',
				align: "center",
				editable: true,
			},
			{
				name: 'overdueDays',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'stat',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'bxRule',
				align: "center",
				editable: true,
				sortable: false
			}
		],
		autowidth: true,
		viewrecords: true,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		sortable:true,
		autoScroll: true,
		shrinkToFit: false,
		height: height,
		rowList: [500, 1000, 3000,5000],
		rowNum: 500,
		pager: '#jqgridPager', //表格页脚的占位符(一般是div)的id
		multiselect: true, //复选框
		caption: "保质期预警管理", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#bzq_jqGrids").getGridParam('records'); //获取返回的记录数
			page = $("#bzq_jqGrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#bzq_jqGrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			bao_search(index, sortorder, page)
		},
		footerrow: true,
		gridComplete: function() { 
			var now_stok = $("#bzq_jqGrids").getCol('now_stok', false, 'sum');
			var cntn_tax_amt = $("#bzq_jqGrids").getCol('cntn_tax_amt', false, 'sum');
			var un_tax_amt = $("#bzq_jqGrids").getCol('un_tax_amt', false, 'sum');
			
			$("#bzq_jqGrids").footerData('set', { 
				"whs_encd": "本页合计",
				now_stok: now_stok.toFixed(prec),
				cntn_tax_amt : precision(cntn_tax_amt,2),
				un_tax_amt : precision(un_tax_amt,2),
				
			}          );    
		},
		onSortCol: function(index, colindex, sortorder) {
			localStorage.setItem("index",index)
			localStorage.setItem("sortorder",sortorder)
			window.index = index;
			window.sortorder = sortorder;
			bao_search(index, sortorder, newPage)      
		}
	})
}


//条件查询
$(function() {
	$('#find').click(function() {
		var initial = 1;
		window.newPage = initial;
		var index = '';
		var sortorder = '';
		bao_search(index, sortorder,initial)
	})
})

function bao_search(index, sortorder, page){
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var whsEncd = $("#whsEncd").val();
	var invldtnDt = $("input[name='invldtnDt1']").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var invtyEncd = $("input[name='invtyEncd1']").val();
	var batNum = $("input[name='batNum1']").val();

	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"whsEncd": whsEncd,
			"invldtnDt": invldtnDt,
			"invtyEncd": invtyEncd,
			"invtyClsEncd": invtyClsEncd,
			"batNum": batNum,
			"sort":index,
			"sortOrder":sortorder,
			"pageNo": page,
			"pageSize": rowNum,
		}
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/whs/invty_tab/queryBaoZhiQiList',
		async: true,
		data: postD2,
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
			$("#bzq_jqGrids").jqGrid("clearGridData");
			$("#bzq_jqGrids").jqGrid("setGridParam", {
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
			alert("查询失败")
		}
	});

}
var arr=[];
var obj={}
//导出
$(document).on('click', '.exportExcel', function() {
	var whsEncd = $("#whsEncd").val();
	var invldtnDt = $("input[name='invldtnDt1']").val();
	var invtyEncd = $("input[name='invtyEncd1']").val();
	var batNum = $("input[name='batNum1']").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"whsEncd": whsEncd,
			"invldtnDt": invldtnDt,
			"invtyEncd": invtyEncd,
			"invtyClsEncd": invtyClsEncd,
			"batNum": batNum,
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url3 + "/mis/whs/invty_tab/queryBaoZhiQiList",
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
			var execlName = '保质期预警'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
	
})