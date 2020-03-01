$(function() {
	//点击表格图标显示仓库列表
	$(document).on('click', '.biao_whsEncd', function() {
		window.open("../../Components/baseDoc/whsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	//点击表格图标显示存货分类列表
	$(document).on('click', '.biao_regnEncd', function() {
		window.open("../../Components/whs/regnList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})

var count;
var pages;
var page = 1;
var rowNum;

//查询按钮
$(document).on('click', '.search_gb', function() {
	search7()
})

function search7() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var gdsBitEncd = $("input[name='gdsBitEncd2']").val()
	var gdsBitNm = $("input[name='gdsBitNm2']").val()
	var Data = {
		"reqHead": reqhead,
		"reqBody": {
			"gdsBitEncd": gdsBitEncd,
			"gdsBitNm": gdsBitNm,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var changeData = JSON.stringify(Data)
	$.ajax({
		type: "post",
		url: url + "/mis/whs/gds_bit/queryList",
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
			var mydata = {};
			mydata.rows = data.respBody.list;
			var list = mydata.rows
			for(var i =0;i<list.length;i++) {
				if(list[i].gdsBitTyp == 1) {
					list[i].gdsBitTyp = '货架'
				} else if(list[i].gdsBitTyp == 2) {
					list[i].gdsBitTyp = '地堆'
				}
			}
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
		//结束加载动画
		complete: function() {
			$(".zhezhao").css("display", "none");
			$("#loader").css("display", "none");
		},
		error: function() {
			alert("条件查询失败")
		}
	});
}

//页面初始化
$(function() {
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	allHeight()
	jQuery("#jqGrids").jqGrid({
		datatype: "local", //请求数据返回的类型。可选json,xml,txt
		shrinkToFit: false,
		colNames: ['货位编码', '货位名称'
		],
		colModel: [
			{
				name: 'gdsBitEncd',
				editable: true,
				align: 'center',
			},
			{
				name: 'gdsBitNm',
				editable: true,
				align: 'center',
			}
		],
		autowidth: true,
		rownumbers: true,
		loadonce: true,
		height: height,
		forceFit: true,
		rownumWidth: 10,  //序列号列宽度
		rowNum: 500,
		scrollrows: true,
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		multiboxonly: true,
		pager: '#gds_jqGridPager', //表格页脚的占位符(一般是div)的id
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
			search7()
		},
		caption: "货位档案", //表格的标题名字
	});
	search7()
})
$(function(){
	$(".true").click(function() {
		//获得行号
		var gr = $("#jqGrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDatas = $("#jqGrids").jqGrid('getRowData', gr);
		var gds = localStorage.getItem("gds")
		if(gds == 0) {
			window.parent.opener.document.getElementById("gdsBitEncd").value = rowDatas.gdsBitEncd;
			localStorage.setItem("gdsBitEncd",rowDatas.gdsBitEncd)
		}else if(gds == 1) {
			window.parent.opener.document.getElementById("gdsBitEncd1").value = rowDatas.gdsBitEncd;
			localStorage.setItem("gdsBitEncd",rowDatas.gdsBitEncd)
		}else if(gds == 2) {
			window.parent.opener.document.getElementById("gdsBitEncd2").value = rowDatas.gdsBitEncd;
			localStorage.setItem("gdsBitEncd",rowDatas.gdsBitEncd)
			window.parent.opener.document.getElementById("regnEncd").value = rowDatas.regnEncd;
			localStorage.setItem("regnEncd",rowDatas.regnEncd)
		}
		localStorage.removeItem("gds")
		window.close()
	})
	$(".false").click(function(){
		var gds = localStorage.getItem("gds")
		if(gds == 0) {
			window.parent.opener.document.getElementById("gdsBitEncd").value = "";
			localStorage.setItem("gdsBitEncd","")
		}else if(gds == 1) {
			window.parent.opener.document.getElementById("gdsBitEncd1").value = "";
			localStorage.setItem("gdsBitEncd","")
		}else if(gds == 2) {
			window.parent.opener.document.getElementById("gdsBitEncd2").value = "";
			localStorage.setItem("gdsBitEncd","")
			window.parent.opener.document.getElementById("regnEncd").value = "";
			localStorage.setItem("regnEncd","")
		}
		window.close();
	})
})