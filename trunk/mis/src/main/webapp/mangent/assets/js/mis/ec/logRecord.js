var count;
var pages;
var page = 1;
var rowNum;
$(function() {
	//页面加载完成之后执行	
	var pageNo = 1;
	var rowNum = 10;
	pageInit();
	//点击右边条数修改显示行数
	$(".ui-pg-selbox.ui-widget-content.ui-corner-all").click(function() {
		pageNo = $("#jqgrids").jqGrid("getGridParam", "page");
		rowNum = $("select[class='ui-pg-selbox ui-widget-content ui-corner-all']")[0].value >> 0;
		var data3 = {
			reqHead,
			"reqBody": {
				"pageSize": rowNum,
				"pageNo": pageNo
			}
		};
		var postD3 = JSON.stringify(data3);
		jQuery("#jqgrids").jqGrid({
			url: url3 + "/mis/ec/storeRecord/queryList", //组件创建完成之后请求数据的url
			mtype: "post",
			datatype: "json", //请求数据返回的类型。可选json,xml,txt
			postData: postD3,
			ajaxGridOptions: {
				contentType: 'application/json; charset=utf-8'
			},
			rowList: [10, 20, 30], //可供用户选择一页显示多少条
			autowidth: true,
			pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
			sortname: 'ecId', //初始化的时候排序的字段
			sortorder: "desc", //排序方式,可选desc,asc
			viewrecords: true,
			rowNum: rowNum, //一页显示多少条
			pageNo: pageNo,
			jsonReader: {
				root: "respBody.list", // json中代表实际模型数据的入口
				records: "respBody.count", // json中代表数据行总数的数据		            
				total: "respBody.pages", // json中代表页码总数的数据
				repeatitems: true,
			},
			onPaging: function(pgButton) {
				pageNo = $("#jqgrids").jqGrid("getGridParam", "page");
				rowNum = $("select[class='ui-pg-selbox ui-widget-content ui-corner-all']")[0].value >> 0;
				if(pgButton === 'prev') {
					pageNo -= 1;
				} else if(pgButton === 'next') {
					pageNo += 1;

				} else if(pgButton === 'records') {
					pageNo = 1;
				}
			}
		});
	})
});

//查询按钮
$(document).on('click', '#find', function() {
	search()
})
function search() {
	var operatId = $("input[name='operatId1']").val();
	var operatType = $("select[name='operatType1']").val();
	var operatOrder = $("input[name='operatOrder1']").val();
	var startDate = $("input[name='startDate1']").val();
	var endDate = $("input[name='endDate1']").val();
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)

	var data2 = {
		reqHead,
		"reqBody": {

			"operatId": operatId,
			"operatType": operatType,
			"operatOrder": operatOrder,
			"startDate":startDate,
			"endDate":endDate,
			"pageNo": page,
			"pageSize": rowNum

		}
	}
	var postD2 = JSON.stringify(data2);
	console.log(postD2)
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url3 + "/mis/ec/logRecord/queryList",
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
			var list = mydata.rows;
			for(var i =0;i<list.length;i++) {
				if(list[i].operatType == 1) {
					list[i].operatType = "新增"
				} else if(list[i].operatType == 2) {
					list[i].operatType = "审核"
				} else if(list[i].operatType == 3) {
					list[i].operatType = "弃审"
				} else if(list[i].operatType == 4) {
					list[i].operatType = "拆单"
				} else if(list[i].operatType == 5) {
					list[i].operatType = "修改"
				} else if(list[i].operatType == 6) {
					list[i].operatType = "合并"
				} else if(list[i].operatType == 7) {
					list[i].operatType = "关闭"
				} else if(list[i].operatType == 8) {
					list[i].operatType = "发货"
				} else if(list[i].operatType == 9) {
					list[i].operatType = "手工下载"
				} else if(list[i].operatType == 10) {
					list[i].operatType = "自动下载"
				} else if(list[i].operatType == 11) {
					list[i].operatType = "自动匹配"
				} else if(list[i].operatType == 12) {
					list[i].operatType = "退款下载"
				} else if(list[i].operatType == 13) {
					list[i].operatType = "删除"
				} else if(list[i].operatType == 14) {
					list[i].operatType = "取电子面单"
				} else if(list[i].operatType == 15) {
					list[i].operatType = "打印快递单"
				} else if(list[i].operatType == 16) {
					list[i].operatType = "导入订单"
				} else if(list[i].operatType == 17) {
					list[i].operatType = "取消面单"
				} else if(list[i].operatType == 18) {
					list[i].operatType = "强制发货"
				} else if(list[i].operatType == 19) {
					list[i].operatType = "取消发货"
				} else if(list[i].operatType == 20) {
					list[i].operatType = "物流表删除"
				}
			}
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#jqgrids").jqGrid("clearGridData");
			$("#jqgrids").jqGrid("setGridParam", {
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
			console.log(error)
		}
	});
}

$(function() {
	var savedata = {
		'reqHead': reqhead,
		'reqBody': {},
	};
	var saveData = JSON.stringify(savedata)
	$.ajax({
		type: 'post',
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/ec/logRecord/logTypeList',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			list = data.respBody.list;
			var option_html = '';
			option_html += '<option value="" selected>' + "请选择" + "</option>"
			for(i = 0; i < list.length; i++) {
				option_html += '<option value="' + list[i].id + '"' + 'id="ab">' + list[i].typeName + "</option>"
			}
			window.pro = $("#operatType").first().children("option").val()
			$("#operatType").html(option_html);
			$("#operatType").change(function(e) {
				window.val = this.value;
				pro = this.value
				window.localStorage.setItem("pro",pro);
			})
			
		},
		error: function() {
			console.log(error)
		}
	})
})



function pageInit() {
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	//初始化表格
	allHeight()
	jQuery("#jqgrids").jqGrid({
		url: '../../assets/js/json/order.json', //组件创建完成之后请求数据的url
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['日志编码', '操作员编码', '操作员名称', '操作时间', '操作类型', '操作订单编码', '操作内容', '备注'], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'logId',
				align: "center",
				index: 'invdate',
				editable: false,
				sortable: false
			},
			{
				name: 'operatId',
				align: "center",
				index: 'invdate',
				editable: true,
				sortable: false
			},
			{
				name: 'operatName',
				align: "center",
				index: 'id',
				editable: true,
				sortable: false
			},
			{
				name: 'operatTime',
				align: "center",
				index: 'id',
				editable: true,
				sortable: false
			},
			{
				name: 'operatType',
				align: "center",
				index: 'invdate',
				editable: true,
				sortable: false
			},
			{
				name: 'operatOrder',
				align: "center",
				index: 'invdate',
				editable: true,
				sortable: false
			},
			{
				name: 'operatContent',
				align: "center",
				index: 'invdate',
				editable: true,
				sortable: false
			},
			{
				name: 'memo',
				align: "center",
				index: 'invdate',
				editable: true,
				sortable: false
			},

		],
		rowNum: 500, //一页显示多少条
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条	
		autowidth: true,
		height:height,
		sortable:true,
		autoScroll:true,
		shrinkToFit:false,
		rownumbers: true,
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'goodId', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,

		caption: "电商平台列表查询", //表格的标题名字	
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#jqgrids").getGridParam('records'); //获取返回的记录数
			page = $("#jqgrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#jqgrids").getGridParam('rowNum'); //获取显示配置记录数量

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
	
	$(function() {
		$(window).resize(function() {
			$("#jqgrids").setGridWidth($(window).width());
		});
	});
//	search()
}

var arr=[];
var obj={}
//导出
$(document).on('click', '.exportExcel', function() {
	var operatId = $("input[name='operatId1']").val();
	var operatType = $("select[name='operatType1']").val();
	var operatOrder = $("input[name='operatOrder1']").val();
	var startDate = $("input[name='startDate1']").val();
	var endDate = $("input[name='endDate1']").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"operatId": operatId,
			"operatType": operatType,
			"operatOrder": operatOrder,
			"startDate":startDate,
			"endDate":endDate,
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url3 + "/mis/ec/logRecord/exportList",
		type: 'post',
		data: Data,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		beforeSend: function() {
			$("#mengban").css("display", "block");
			$("#loader").css("display", "block");
		},
		complete: function() {
			$("#mengban").css("display", "none");
			$("#loader").css("display", "none");
		},
		success: function(data) {
			var list = data.respBody.list;
			var execlName = '电商处理日志'
			ExportData(list, execlName)
		},
		error: function() {
			console.log(error)
		}
	})
	
})