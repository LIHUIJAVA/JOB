
var count;
var pages;
var page = 1;
var rowNum;
//表格初始化
$(function() {
//	allHeight();
	//加载动画html 添加到初始的时候
//	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
//	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
//	$("#mengban1").addClass("zhezhao");
	$("#p_jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['编码', '母件编码', '母件名称', '母件规格', '备注', '是否审核', '创建人', '创建时间', '修改人', '修改时间', '审核人', '审核时间'],
		colModel: [{
				name: 'ordrNum',
				editable: true,
				align: 'center',
				hidden: true
			},
			{
				name: 'momEncd',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'momNm',
				editable: true,
				align: 'center'

			},
			{
				name: 'momSpc',
				editable: true,
				align: 'center'

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
			{
				name: 'setupPers',
				editable: true,
				align: 'center'

			},
			{
				name: 'setupTm',
				editable: true,
				align: 'center'

			},
			{
				name: 'mdfr',
				editable: true,
				align: 'center'

			},
			{
				name: 'modiTm',
				editable: true,
				align: 'center'

			},
			{
				name: 'chkr',
				editable: true,
				align: 'center'

			},
			{
				name: 'chkTm',
				editable: true,
				align: 'center'

			}
		],
		autowidth: true,
		rownumbers: true,
		loadonce: false,
		height:400,
		rowNum: 500, //一页显示多少条
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条	
		forceFit: true,
		pager: '#pro_jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
//		multiboxonly: true,
		autoScroll:true,
		rownumWidth: 30,  //序列号列宽度
		shrinkToFit:false,
		caption: "产品结构列表", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#p_jqGrids").getGridParam('records'); //获取返回的记录数
			page = $("#p_jqGrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#p_jqGrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			search_amb()
		}
	})
})

//查询全部
function search_amb () {
	var rowNum1 = $("#gbox_p_jqGrids td[dir='ltr'] select").val() //获取显示配置记录数量
	rowNum = parseInt(rowNum1)
	var myDate = {};

	var momEncd = $("input[name='momEncd']").val();
//	var ordrNum = $(".ordrNum").val();
	var isNtChk = $("select[name='isNtChk']").val();

	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"momEncd": momEncd,
//			'ordrNum':ordrNum,
			'isNtChk':isNtChk,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/whs/prod_stru/queryAmbDisambSngl',
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
			for(var i = 0;i<list.length;i++){
				if(list[i].isNtChk==0){
					list[i].isNtChk="否"
				}else if(list[i].isNtChk==1){
					list[i].isNtChk="是"
				}
			}
			myDate = list;
			var mydata = {};
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#p_jqGrids").jqGrid("clearGridData");
			$("#p_jqGrids").jqGrid("setGridParam", {
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
			alert('查询失败')
		}
	});
}


//查询按钮
$(document).on('click', '.search_amb', function() {
	search_amb();
})
