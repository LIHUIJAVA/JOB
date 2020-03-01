//点击表格图标显示仓库列表
$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	$(document).on('click', '.whsNm', function() {
		window.open("../../Components/baseDoc/whsList.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
//	存货分类
	$(document).on('click', '.invtyClsEncd_biaoge', function() {
		window.open("../../Components/baseDoc/invtyTree.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})
var count;
var pages;
var page = 1;
var rowNum;
$(function() {
	allHeight()
	pageInit();
});

function pageInit() {
	localStorage.removeItem("whsEncd")
	jQuery("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['单据号', '仓库名称', '存货编码', '存货名称', '规格型号', '主计量单位', '条形码', '批次', '生产日期', '保质期', '失效日期', '拣货日期', '货位', '销售类型', '业务类型', '数量', '制作人'], //jqGrid的列显示名字
		colModel: [{
				name: "sellSnglId",
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: "whsNm",
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: "invtyEncd",
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'invtyNm',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'spcModel',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'measrCorpNm',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'barCd',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'batNum',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'prdcDt',
				align: "center",
				editable: false,
				sorttype:'date',
			},
			{
				name: 'baoZhiQi',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'invldtnDt',
				align: "center",
				editable: false,
				sorttype:'date',
			},
			{
				name: 'pickSnglTm',
				align: "center",
				editable: false,
				sorttype:'date',
			},
			{
				name: 'gdsBitNm',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'sellTyp',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'bizTyp',
				align: "center",
				editable: false,
				sortable: false
			},
			{
				name: 'qty',
				align: "center",
				editable: false,
				sorttype:'integer',
			},
			{
				name: 'pickPers',
				align: "center",
				editable: false,
				sortable: false
			}
		],
		autowidth: true,
		viewrecords: true,
		rownumbers: true,
		loadonce: true,
		sortable:true,
		autoScroll: true,
		shrinkToFit: false,
		forceFit: true,
		rowNum: 500, //一页显示多少条
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条	
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		height: height,
		multiselect: true, //复选框
		caption: "生成拣货", //表格的标题名字
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
			search()
		},
		footerrow: true,
		gridComplete: function() { 
			var qty = $("#jqGrids").getCol('qty', false, 'sum');
			
			$("#jqGrids").footerData('set', { 
				"sellSnglId": "本页合计",
				qty: qty.toFixed(prec),
				
			}          );    
		},
	})
}


$(document).on('click', '#find', function() {
	var whsEncd = $("input[name='whsEncd']").val()
	if(whsEncd == '') {
		alert("请选择仓库")
	} else {
		search()
	}
})

function search() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var whsEncd = $("input[name='whsEncd']").val()
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"whsEncd": whsEncd,
			"invtyClsEncd": invtyClsEncd,
			"pageSize": rowNum,
			"pageNo":page,
		}
	};
	var saveData = JSON.stringify(data2);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/whs/pick_sngl/selectSellById',
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
			var mydata = {};
			mydata.rows = list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#jqGrids").jqGrid("clearGridData");
			$("#jqGrids").jqGrid("setGridParam", {
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

$(function() {
	$(".merge").click(function() {
		var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
		var rowData = [];
		for(var i = 0; i < ids.length; i++) {
			var gr = $("#jqGrids").getGridParam('selrow');
			//选中行的id
			var data = $("#jqGrids").getCell(ids[i], "sellSnglId");
			//建一个数组，把选中行的id添加到这个数组中去。
			rowData[i] = data;
		}
		var rowDatas = rowData.toString()
		var data2 = {
			"reqHead": reqhead,
			"reqBody": {
				"sellSnglId": rowDatas,
			}
		};
		var saveData = JSON.stringify(data2);
		$.ajax({
			type: "post",
			contentType: 'application/json; charset=utf-8',
			url: url + '/mis/whs/pick_sngl/insertSellPick',
			async: true,
			data: saveData,
			dataType: 'json',
			success: function(data) {
				alert(data.respHead.message)
				if(data.respHead.isSuccess == true) {
					search()
				}
			},
			error: function() {
				console.log(error)
			}
		})
	})
})

//查询详细信息
$(function() {
	var afterUrl = window.location.search.substring(1);
	var b = [];
	b = afterUrl;
	var a = 1
	if(a == b) {
		chaxun()
	}
})

function chaxun() {
	var pickSnglNum = localStorage.pickSnglNum
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"pickSnglNum": pickSnglNum,
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/whs/pick_sngl/selectPSubTabById',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			var arr = eval(data); //数组
			var list1 = arr.respBody.list;
			var arr1 = [];
			var length = list1.length;
			for(var i = 0; i < length; i++) {
				arr1.push(list1[i].sellSnglSub);
			}
			var list = [];

			function fun(arr) {
				for(var i = 0; i < arr.length; i++) {
					if(Array.isArray(arr[i])) {
						fun(arr[i]);
					} else {
						list.push(arr[i]);
					}
				}
			}
			fun(arr1);
			$("#jqGrids").jqGrid('clearGridData');
			$("#jqGrids").jqGrid('setGridParam', {
				datatype: 'local',
				rowNum:10,
				data: list1, //newData是符合格式要求的重新加载的数据
				page: 1 //哪一页的值
			}).trigger("reloadGrid")
		}
	})

}