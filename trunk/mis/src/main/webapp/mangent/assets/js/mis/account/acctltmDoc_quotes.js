var page = 1;
var rowNum;
//表格初始化
$(function() {
	jQuery("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['级别', '科目编码', '科目名称', '是否银行科目', '是否现金科目', '科目类型',
			'是否银行帐', '是否日记账', '余额方向'
		], //jqGrid的列显示名字
		colModel: [{
				name: "encdLvlSub",
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: "subjId",
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: "subjNm", //科目名称
				align: "left",
				editable: true,
				sortable: false
			},
			{
				name: 'isNtBankSubj',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'isNtCashSubj',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: "subjTyp",
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'isNtBkat',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'isNtDayBookEntry',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'balDrct',
				align: "center",
				editable: true,
				sortable: false,
			}
		],
		height: 320,
		autowidth: true,
		autoScroll: true,
		shrinkToFit: false,
		viewrecords: true,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 500,
		multiselect: true, //复选框
		multiboxonly: true,
		rowList: [500, 1000, 3000], //可供用户选择一页显示多少条
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		cellsubmit: 'clientArray',
		caption: "会计科目", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#jqGrids").getGridParam('records'); //获取返回的记录数
			page = $("#jqGrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#insertClsList td select").val()
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
			find();
		},
	})
})

$(function(){
	$(".search").click(function(){
		find()
	})
})

function find() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var subjId = $(".subjId").val();
	var showData = {
		"reqHead": reqhead,
		"reqBody": {
			"subjId":subjId,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var postData = JSON.stringify(showData);
	$.ajax({
		type: "post",
		url: url + "/mis/account/acctItmDoc/queryAcctItmDocList", //列表
		async: true,
		data: postData,
		dataType: 'json',
		contentType: 'application/json',
		success: function(data) {
			var data1 = data.respBody.list;
			for(var i = 0; i < data1.length; i++) {
				if(data1[i].isNtBankSubj == 0) {
					data1[i].isNtBankSubj = "否"
				} else if(data1[i].isNtBankSubj == 1) {
					data1[i].isNtBankSubj = "是"
				}
				if(data1[i].isNtCashSubj == 0) {
					data1[i].isNtCashSubj = "否"
				} else if(data1[i].isNtCashSubj == 1) {
					data1[i].isNtCashSubj = "是"
				}
				if(data1[i].isNtBkat == 0) {
					data1[i].isNtBkat = "否"
				} else if(data1[i].isNtBkat == 1) {
					data1[i].isNtBkat = "是"
				}
				if(data1[i].isNtDayBookEntry == 0) {
					data1[i].isNtDayBookEntry = "否"
				} else if(data1[i].isNtDayBookEntry == 1) {
					data1[i].isNtDayBookEntry = "是"
				}
				if(data1[i].balDrct == 0) {
					data1[i].balDrct = "借"
				} else if(data1[i].balDrct == 1) {
					data1[i].balDrct = "贷"
				}
			}
			var myData = [];
			funa(data1);
			//第一级A
			function funa(arr) {
				for(var i = 0; i < arr.length; i++) {
					if((arr[i].children != null) && (arr[i].children.length > 0)) {
						myData.push(arr[i]); //添加Grid
					} else {
						myData.push(arr[i]); //添加Grid
					}
				}
			}
			var mydata = {};
			mydata.rows = myData;
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
			alert("加载失败")
		}
	})
}