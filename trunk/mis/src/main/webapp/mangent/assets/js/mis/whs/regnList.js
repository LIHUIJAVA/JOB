var count;
var pages;
var page = 1;
var rowNum;
$(function() {
	pageInit();
});

function pageInit() {
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	var Data = {
		"reqHead": reqhead,
		"reqBody": {
			"regnEncd": '',
			"regnNm": '',
			"pageNo": 1,
			"pageSize": 500
		}
	}
	var changeData = JSON.stringify(Data)
	$.ajax({
		type: "post",
		url: url + "/mis/whs/regn/queryList",
		async: true,
		data: changeData,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		//开始加载动画  添加到ajax里面
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");

		},
		success: function(data) {
			var list = data.respBody.list;
			allHeight()
			//初始化右侧表格
			$("#r_jqgrids").jqGrid({
				datatype: "local", //请求数据返回的类型。可选json,xml,txt
				data: list,
				colNames: ['区域编码', '区域名称', '地堆数量', '长', '宽'],
				colModel: [
					{
						name: 'regnEncd',
						editable: false,
						align: 'center',
						sortable: false
					},
					{
						name: 'regnNm',
						editable: true,
						align: 'center',
					},
					{
						name: 'siteQty',
						editable: true,
						align: 'center',
					},
					{
						name: 'longs',
						editable: true,
						align: 'center',
					},
					{
						name: 'wide',
						editable: true,
						align: 'center',
					}
				],
				rowNum: 10,
				rowList: [10, 20, 30], //可供用户选择一页显示多少条
				autowidth: true,
				rownumbers: true,
				loadonce: false,
				height: height,
				autoScroll: true,
				shrinkToFit: false,
				forceFit: true,
				sortname: 'id', //初始化的时候排序的字段
				sortorder: "desc", //排序方式,可选desc,asc
				viewrecords: true,
				pager: '#r_jqGridPager', //表格页脚的占位符(一般是div)的id
				caption: "区域详情", //表格的标题名字,
				onPaging: function(pageBtn) { //翻页实现 
					var records = $("#r_jqgrids").getGridParam('records'); //获取返回的记录数
					page = $("#r_jqgrids").getGridParam('page'); //获取返回的当前页
					var rowNum1 = $("#r_jqgrids").getGridParam('rowNum');
		
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
					search();
				},
			})
		},
		//结束加载动画
		complete: function() {
			$(".zhezhao").css("display", "none");
			$("#loader").css("display", "none");
		},
	})
}

$(document).on('click', '#find', function() {
	search()
})


//查询按钮
function search() {
	var regnEncd = $("input[name='regnEncd1']").val();
	var regnNm = $("input[name='regnNm1']").val();

	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var Data = {
		"reqHead": reqhead,
		"reqBody": {
			"regnEncd": regnEncd,
			"regnNm": regnNm,
			"pageNo": page,
			"pageSize": rowNum
		}
	}
	var changeData = JSON.stringify(Data)
	$.ajax({
		type: "post",
		url: url + "/mis/whs/regn/queryList",
		async: true,
		data: changeData,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		success: function(data) {
			var mydata = {};
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#r_jqgrids").jqGrid("clearGridData");
			$("#r_jqgrids").jqGrid("setGridParam", {
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
			alert("条件查询失败")
		}
	});
}

$(function() {
	$("#true").click(function() {
		//获得行号
		var gr = $("#r_jqgrids").jqGrid('getGridParam', 'selrow');
		if(gr == null) {
			alert("未选择")
		} else {
			//获得行数据
			var rowDatas = $("#r_jqgrids").jqGrid('getRowData', gr);

			if(localStorage.gds == 1) {
				window.parent.opener.document.getElementById("regnEncd1").value = rowDatas.regnEncd;
				localStorage.removeItem("gds")
			} else {
				window.parent.opener.document.getElementById("regnEncd").value = rowDatas.regnEncd;
				localStorage.setItem("regnEncd",rowDatas.regnEncd);
			}
			window.close()
		}
	})
	$("#false").click(function() {
		if(localStorage.gds == 1) {
			window.parent.opener.document.getElementById("regnEncd1").value = "";
		} else {
			window.parent.opener.document.getElementById("regnEncd").value = "";
		}
		localStorage.setItem("regnEncd","");
		window.close()
	})
})