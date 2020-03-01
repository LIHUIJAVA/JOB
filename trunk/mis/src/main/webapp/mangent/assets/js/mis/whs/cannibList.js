localStorage.setItem("outInEncd", 0)

//点击转出仓库表格图标显示仓库列表
$(function() {
	$(document).on('click', '.tranOutWhsEncd1', function() {
		localStorage.setItem("outInEncd", 1)
		window.open("../../Components/baseDoc/whsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
});
//点击转入表格图标显示业务员列表
$(function() {
	$(document).on('click', '.tranInWhsEncd1', function() {
		localStorage.setItem("outInEncd", 2)
		window.open("../../Components/baseDoc/whsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})
var count;
var pages;
var page = 1;
var rowNum;
//表格初始化
$(function() {
	localStorage.removeItem("tranInWhsEncd");
	localStorage.removeItem("tranOutWhsEncd");
	$("#jqGrids_list").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['单据号', '调拨日期', '转入仓库', '转出仓库', '创建人', '审核人','是否审核'],
		colModel: [{
				name: 'formNum',
				editable: true,
				align: 'center'
			},
			{
				name: 'cannibDt',
				editable: true,
				align: 'center'

			},
			{
				name: 'tranInWhsEncd',
				editable: false,
				align: 'center',
			},
			{
				name: 'tranOutWhsEncd',
				editable: false,
				align: 'center'
			},
			{
				name: 'setupPers',
				editable: false,
				align: 'center'
			},
			{
				name: 'chkr',
				editable: false,
				align: 'center'
			},
			{
				name: 'isNtChk',
				editable: true,
				align: 'center',
			},
		],
		autowidth: true,
		height:400,
		autoScroll:true,
		shrinkToFit:false,
		rownumbers: true,
		loadonce: false,
		forceFit: true,
		rowNum: 500,
		rowList: [500, 1000, 3000,5000],
		pager: '#jqGridPager_list', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
//		multiselect: true, //复选框
//		multiboxonly: true,
		caption: "调拨单列表", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#jqGrids_list").getGridParam('records'); //获取返回的记录数
			page = $("#jqGrids_list").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#jqGrids_list").getGridParam('rowNum'); //获取显示配置记录数量

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
			cann_search()
		},
		//双击弹出采购订单
//		ondblClickRow: function() {
//			order();
//		},
	})
})

//查询按钮
$(document).on('click', '.cann_search', function() {
	cann_search()
})

function cann_search(){
	var rowNum1 = $("#gbox_jqGrids_list td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var formNum = $("input[name='formNum1']").val();
	var cannibDt1 = $(".cannibDt1").val();
	var cannibDt2 = $(".cannibDt2").val();

	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"formNum": formNum,
			"formDt1": cannibDt1,
			"formDt2": cannibDt2,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/whs/cannib_sngl/queryList',
		async: true,
		data: saveData,
		dataType: 'json',
		//开始加载动画  添加到ajax里面
//		beforeSend: function() {
//			$(".zhezhao").css("display", "block");
//			$("#loader").css("display", "block");
//
//		},
//		//结束加载动画
//		complete: function() {
//			$(".zhezhao").css("display", "none");
//			$("#loader").css("display", "none");
//		},
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
			mydata.rows = list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#jqGrids_list").jqGrid("clearGridData");
			$("#jqGrids_list").jqGrid("setGridParam", {
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
			alert("查询失败")
		}
	});

}



