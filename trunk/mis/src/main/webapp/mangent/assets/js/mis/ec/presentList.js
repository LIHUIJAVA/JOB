var count;
var pages;
var page = 1;
var rowNum;
var mType = 0;
var myData = {};
//页面初始化
$(function() {
	allHeight()
	//加载动画html 添加到初始的时候
	$(".zz").append("<div id='mengban1' class='zhezhao'></div>");
	$(".zz").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	//初始化表格
	jQuery("#present_list_jqgrids").jqGrid({
		height: height,
		autoScroll: true,
		datatype: "local", //请求数据返回的类型。可选json,xml,txt
		shrinkToFit: false,
		colNames: ['序号','赠品范围编码', '赠品范围名称'
		], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'id',
				align: "center",
				editable: false,
				sortable: false,
				hidden:true
			},
			{
				name: 'presentRangeEncd',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'presentRangeName',
				align: "center",
				editable: true,
				sortable: false,
			}
		],
		rowNum: 500, //一页显示多少条
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条				
		autowidth: true,
		sortable:true,
		loadonce: true,
		multiselect: true, //复选框
		multiboxonly: true,
		rownumWidth: 8,  //序列号列宽度
		cellsubmit: "clientArray",
//		multiselectWidth:8, //复选框列宽度
		rownumbers: true,
		pager: '#present_list_jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'storeId', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		ondblClickRow: function(rowid) {
			order(rowid);
		},
//		multiselect: true, //复选框
		caption: "赠品范围列表", //表格的标题名字	
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#present_list_jqgrids").getGridParam('records'); //获取返回的记录数
			page = $("#present_list_jqgrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#present_list_jqgrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			search6()
		},

	});
	search6()
})
function order(rowid) {
	//获得行数据
	var rowDatas = $("#present_list_jqgrids").jqGrid('getRowData', rowid);
	localStorage.setItem("presentRangeEncd", rowDatas.presentRangeEncd);
	window.open("../../Components/ec/present.html?1");
}

//查询按钮
$(document).on('click', '.search', function() {
	search6()
})

//条件查询
function search6() {
	var presentRangeEncd = $("input[name='presentRangeEncd']").val();
	var presentRangeName = $("input[name='presentRangeName']").val();
	
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var data2 = {
		reqHead,
		"reqBody": {
			"presentRangeEncd": presentRangeEncd,
			"presentRangeName": presentRangeName,
			"pageNo": page,
			"pageSize": rowNum
		}

	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url3 + "/mis/ec/presentRangeList/selectPresentRangeList",
		data: postD2,
		dataType: 'json',
		async: true,
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
			$("#present_list_jqgrids").jqGrid("clearGridData");
			$("#present_list_jqgrids").jqGrid("setGridParam", {
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

////删除行
$(function() {
	$(".del").click(function() {
		var num = []
		var gr = $("#present_list_jqgrids").jqGrid('getGridParam', 'selarrrow'); //获取行id
		for(var i = 0;i < gr.length;i++) {
			var rowDatas = $("#present_list_jqgrids").jqGrid('getRowData', gr[i]); //获取行数据    	
			var presentRangeEncd = rowDatas.presentRangeEncd
			num.push(presentRangeEncd)
		}
		var deleteAjax = {
			reqHead,
			"reqBody": {
				"presentRangeEncd": num.toString(),
			}
		};
		var deleteData = JSON.stringify(deleteAjax)
		console.log(deleteData)
		if(gr == null) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url3 + "/mis/ec/presentRangeList/deletePresentRangeList",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(remover) {
					alert(remover.respHead.message);
					search6()
				},
				error: function() {
					console.log(error)

				}
			});

		}
	})
})
