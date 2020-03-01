var count;
var pages;
var page = 1;
var rowNum;

$(function() {
	//页面加载完成之后执行
	pageInit1();
})

function pageInit1() {
	allHeight()
	jQuery("#whs_jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['仓库编码', '仓库名称', '部门名称', '仓库地址', '电话', '负责人','总仓'], //jqGrid的列显示名字
		colModel: [{
				name: 'whsEncd',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: "whsNm",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: "deptName",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: "whsAddr",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'tel',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'princ',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'realWhs',
				align: "center",
				editable: true,
				sortable: false
			}
		],
		autowidth: true,
		rownumbers: true,
		loadonce: false,
		height: height,
		forceFit: true,
		rowNum:100,
		rowList: [100, 300, 500],
		pager: '#whs_jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "仓库档案", //表格的标题名字
		multiselect: true, //复选框
		multiboxonly: true,
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#whs_jqgrids").getGridParam('records'); //获取返回的记录数
			page = $("#whs_jqgrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#whs_jqgrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			whs_search()
		},
	})
}

//条件查询
$(document).on('click', '.whs_find', function() {
	whs_search()
})

function whs_search() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var whsEncd = $(".whsEncd").val();
	var whsNm = $(".whsNm").val();
	var deptEncd = $(".deptEncd").val();
	var Data = {
		"reqHead": reqhead,
		"reqBody": {
			"whsEncd": whsEncd,
			"whsNm": whsNm,
			"deptEncd": deptEncd,
			"pageNo": page,
			"pageSize": rowNum
		}
	}
	var changeData = JSON.stringify(Data)
	$.ajax({
		type: "post",
		url: url + "/mis/whs/whs_doc/queryList",
		async: true,
		data: changeData,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		success: function(data) {
			if(data.respHead.isSuccess==false){
				alert(data.respHead.message)
			}
			var mydata = {};
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#whs_jqgrids").jqGrid("clearGridData");
			$("#whs_jqgrids").jqGrid("setGridParam", {
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
			alert("条件查询失败")
		}
	});
}

$(document).on('click', '.true', function() {
	//获得行号
	var ids = $("#whs_jqgrids").jqGrid('getGridParam', 'selarrrow');
	if(ids.length == 0) {
		alert("请选择单据")
	} else {
		var encd = [];
		var nm = [];
		var realWhs = [];
		for(i = 0; i < ids.length; i++) {
			var rowDatas = $("#whs_jqgrids").jqGrid('getRowData', ids[i]); //获取行数据
			nm[i] = rowDatas.whsNm;
			encd[i] = rowDatas.whsEncd;
			realWhs[i] = rowDatas.realWhs;
		}
		var encds = encd.join(',');
		var nms = nm.join(',')
		var real = realWhs.join(',')
		window.localStorage.removeItem("whsEncd")
		var outInEncd = localStorage.outInEncd;
		var realWhss = localStorage.realWhss;
		if(outInEncd == 1) {
			window.parent.opener.document.getElementById("tranOutWhsEncd").value = encds;
			localStorage.setItem("tranOutWhsEncd", rowDatas.whsEncd)
			window.parent.opener.document.getElementById("tranOutWhsNm").value = nms;
			localStorage.setItem("tranOutWhsNm", rowDatas.whsNm)
		} else if(outInEncd == 2) {
			window.parent.opener.document.getElementById("tranInWhsEncd").value = encds;
			localStorage.setItem("tranInWhsEncd", rowDatas.whsEncd)
			window.parent.opener.document.getElementById("tranInWhsNm").value = nms;
			localStorage.setItem("tranInWhsNm", rowDatas.whsNm)
		} else if(outInEncd == 0) {
			window.parent.opener.document.getElementById("whsNm").value = nms;
			window.parent.opener.document.getElementById("whsEncd").value = encds;
			localStorage.setItem("whsEncd", rowDatas.whsEncd)
			if(realWhss == 0) {
				window.parent.opener.document.getElementById("realWhs").value = real;
				
			}
		}
		window.close()
		localStorage.setItem("outInEncd", 0)
		localStorage.removeItem("realWhss")
	}
})

$(document).on('click', '.false', function() {
	localStorage.setItem("outInEncd", 0)
//		localStorage.clear();
	//	window.parent.opener.document.getElementById("whsNm").value= "";
	//	window.parent.opener.document.getElementById("whsEncd").value= "";
	window.close()
})