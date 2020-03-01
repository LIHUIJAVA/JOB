//销售出库参照的销售单
var count;
var pages;
var page = 1;
var rowNum;

//表格初始化
$(function() {
	$("#purs_list .purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$("#purs_list .purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div>");
	$("#mengban1").addClass("zhezhao");
	allHeight();
	$("#jqGrids_list").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['销售单号', '销售日期', '销售类型', '业务类型id','业务类型', '业务员', '部门', '客户id','客户简称', '发货地址', '备注', '是否审核'],
		colModel: [{
				name: 'sellSnglId',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'sellSnglDt',
				editable: true,
				align: 'center'

			},
			{
				name: 'sellTypNm',
				editable: true,
				align: 'center'

			},
			{
				name: 'bizTypId',
				editable: true,
				align: 'center'

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
				name: 'custId',
				editable: false,
				align: 'center'
			},
			{
				name: 'custNm',
				editable: false,
				align: 'center'
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
				name: 'isNtChk',
				editable: true,
				align: 'center'
			},
		],
		autowidth: true,
		rownumbers: true,
		loadonce: false,
		forceFit: true,
		height:height,
		autoScroll:true,
		shrinkToFit:false,
		rowNum:500,
		rowList: [500,1000, 3000, 5000], //可供用户选择一页显示多少条
		pager: '#jqGridPager_list', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		multiboxonly: true,
		caption: "销售单列表", //表格的标题名字
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
			sellOut_search();
		},
	})
	$("#jqGrids_list_cb").hide()
})

//查询按钮
$(document).on('click', '.searcher', function() {
	sellOut_search()
})
function sellOut_search () {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var sellSnglId = $(".sellSnglId").val();
	var custId = $("#custId1").val();
	var invtyEncd = $(".invtyEncd").val();
	var sellSnglDt1 = $(".sellSnglDt1").val();
	var sellSnglDt2 = $(".sellSnglDt2").val();
	var isNtChk = $("#isNtChk").val();
	var whsEncd = $("#whsEncd").val()
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": invtyEncd,
			"sellSnglId": sellSnglId,
			"custId": custId,
			"sellSnglDt1": sellSnglDt1,
			"sellSnglDt2": sellSnglDt2,
			"isNtChk":1,
			"whsEncd":whsEncd,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/purc/SellSngl/querySellSnglList',
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
					list[i].isNtChk="否"
				}else if(list[i].isNtChk==1){
					list[i].isNtChk="是"
				}
			}
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
