
var count;
var pages;
var page = 1;
var rowNum;
$(function() {
	allHeight()
	jQuery("#store_jqGrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['店铺编码', '店铺名称', '电商平台编码'], //jqGrid的列显示名字
		colModel: [{
				name: 'storeId',
				align: "center",
				index: 'invdate',
				editable: false,
			},
			{
				name: 'storeName',
				align: "center",
				index: 'id',
				editable: true,
			},
			{
				name: 'ecId',
				align: "center",
				index: 'invdate',
				editable: false,
			}
		],
		autowidth: true,
		height:height,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 100,
		rowList: [100, 300, 500],
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		pager:"store_jqGridPager",
		caption: "店铺档案", //表格的标题名字
		multiselect: true, //复选框
		multiboxonly: true,
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#store_jqGrids").getGridParam('records'); //获取返回的记录数
			page = $("#store_jqGrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#store_jqGrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			search()
		},
	});
})


$(function() {
	/*------点击下载按钮加载动态下拉电商平台-------*/
	var savedata = {
		'reqHead': reqhead,
		'reqBody': {
			'ecId': "",
			'pageNo': 1,
			'pageSize': 500
		},
	};
	var saveData = JSON.stringify(savedata)
	$.ajax({
		type: 'post',
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/ec/ecPlatform/queryList',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			list = data.respBody.list;
			var option_html = '';
			option_html += '<option value="" selected>' + "请选择" + "</option>"
			for(i = 0; i < list.length; i++) {
				option_html += '<option value="' + list[i].ecId + '"' + 'id="ab">' + list[i].ecName + "</option>"
			}
			window.pro = $(".ecName").first().children("option").val()
			$(".ecName").html(option_html);
			$(".ecName").change(function(e) {
				window.val = this.value;
				pro = this.value
				$("input[name='ecId']").val(pro)
			})
			
		},
		error: function() {
			alert("error")
		}
	})
})


$(function() {
	$('#find').click(function() {
		search()
	})
})

//条件查询
function search() {
	var myDate = {};
	if($("input[name='ecId']").val() == '') {
//		alert("取本地ecId")
		var ecId = localStorage.pro
	} else if($("input[name='ecId']").val() != '') {
//		alert("取当前ecId")
		var ecId = $("input[name='ecId']").val();	
	}
	var storeId = $(".storeId").val();
	var storeName = $(".storeName").val();
	var rowNum1 = $("#gbox_store_jqGrids td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var Data = {
		"reqHead":reqhead,
		"reqBody": {
			'ecId':ecId,
			"storeId": storeId,
			"storeName": storeName,
			"pageNo": page,
			"pageSize": rowNum
		}
	}
	var changeData = JSON.stringify(Data)
	$.ajax({
		type: "post",
		url: url + "/mis/ec/storeRecord/queryList",
		async: true,
		data: changeData,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		success: function(data) {
			window.localStorage.removeItem("pro");
			var mydata = {};
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#store_jqGrids").jqGrid("clearGridData");
			$("#store_jqGrids").jqGrid("setGridParam", {
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
	$("#jqGrids").jqGrid('clearGridData')

}


$(document).on('click', '.click_span', function() {
	//点击改变颜色
	$(".addColor").removeClass("addColor");
	$(this).addClass("addColor");
})

$(document).on('click', '.sure', function() {
//	window.localStorage.removeItem("invtyEncd")
	//获得行号
	var ids = $("#store_jqGrids").jqGrid('getGridParam', 'selarrrow');
	if(ids.length == 0) {
		alert("请选择单据")
	} else  {
		var encd = [];
		var nm = [];
		for(i = 0; i < ids.length; i++) {
			var rowDatas = $("#store_jqGrids").jqGrid('getRowData', ids[i]); //获取行数据
			nm[i] = rowDatas.storeName;
			encd[i] = rowDatas.storeId;
		}
		var encds = encd.join(',');
		var nms = nm.join(',')
		window.parent.opener.document.getElementById("storeId").value = encds;
		window.parent.opener.document.getElementById("storeName").value = nms;
		localStorage.setItem("storeId", encds);
		localStorage.setItem("storeName", nms);
		window.close()
	}
})
$(document).on('click', '.false', function() {
	window.parent.opener.document.getElementById("storeName").value = "";
	window.parent.opener.document.getElementById("storeId").value = "";
	window.close()
	localStorage.clear();
})



