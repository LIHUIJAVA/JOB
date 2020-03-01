$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");

	//点击表格图标显示存货列表
	$(document).on('click', '.biao_invtyEncd', function() {
		window.open("../../Components/baseDoc/invtyList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	$(document).on('click', '.biao_whsEncd', function() {
		window.open("../../Components/baseDoc/whsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	$(document).on('click', '.momKitNm', function() {
		window.open("../../Components/baseDoc/outIntoWhs.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	//	存货分类
	$(document).on('click', '.invtyClsEncd_biaoge', function() {
		window.open("../../Components/baseDoc/invtyTree.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})
var pages;
var page = 1;
var rowNum;
$(function() {
	allHeight()
	jQuery("#order_jqGrids").jqGrid({
		url: '../../assets/js/json/order.json', //组件创建完成之后请求数据的url
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['单据日期', '单据号', '仓库名称', '单据类型', '收发类别', '存货编码', '存货名称',
			'规格型号', '计量单位', '供应商名称', '数量', '无税金额', '无税单价', '备注'
		], //jqGrid的列显示名字
		colModel: [{
				name: "formDt",
				align: "center",
				editable: true,
			},
			{
				name: "formNum",
				align: "center",
				editable: true,
			},
			{
				name: "whsNm",
				align: "center",
				editable: true,
			},
			{
				name: 'outIntoWhsTypNm',
				align: "center",
				editable: true,
			},
			{
				name: 'recvSendCateNm',
				align: "center",
				editable: true,
			},
			{
				name: 'invtyEncd',
				align: "center",
				editable: true,
			},
			{
				name: 'invtyNm',
				align: "center",
				editable: true,
			},
			{
				name: 'spcModel',
				align: "center",
				editable: true,
			},
			{
				name: 'measrCorpNm',
				align: "center",
				editable: true,
			},
			{
				name: 'provrNm',
				align: "center",
				editable: true,
			},
			{
				name: 'qty',
				align: "center",
				editable: true,
			},
			{
				name: 'noTaxAmt',
				align: "center",
				editable: true,
			},
			{
				name: 'noTaxUprc',
				align: "center",
				editable: true,
			},
			{
				name: 'memo',
				align: "center",
				editable: true,
			}
		],
		sortable:true,
		height: height,
		autowidth: true,
		viewrecords: true,
		autoScroll: true,
		shrinkToFit: false,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum:500,
		rowList: [500,1000,3000], //可供用户选择一页显示多少条
		footerrow: true,
		pager: '#order_jqGridPager', //表格页脚的占位符(一般是div)的id
		multiboxonly: true,
		multiselect: true, //复选框
		caption: "恢复记账", //表格的标题名字
		gridComplete: function() {                   
			var qty = $("#order_jqGrids").getCol('qty', false, 'sum');  
			var noTaxAmt = $("#order_jqGrids").getCol('noTaxAmt', false, 'sum');        
			$("#order_jqGrids").footerData('set', { 
				"formDt": "本页合计",
				qty: qty,
				noTaxAmt: noTaxAmt
			}          );        
		},
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#order_jqGrids").getGridParam('records'); //获取返回的记录数
			page = $("#order_jqGrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#order_jqGrids").getGridParam('rowNum'); //获取显示配置记录数量

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
		}
	})
})

//条件查询
$(function() {
	$('#find').click(function() {
		search()
	})
})

function search() {
	var myData = {};
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var whsEncd = $("input[name='whsEncd']").val();
	var formType = $("input[name='formCode']").val();
	var invtyEncds = $("input[name='invtyEncd']").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var formNum = $("input[name='formNum']").val();
	var formSDt = $("#formSDt").val();
	var formEDt = $("#formEDt").val();
	var bookOkSDt = $("#bookOkSDt").val();
	var bookOkEDt = $("#bookOkEDt").val();
	var loginTime = localStorage.loginTime;
	var data2 = {
		"reqHead": reqHead,
		"reqBody": {
			"whsEncd": whsEncd,
			"invtyEncd": invtyEncds,
			"formType": formType,
			"invtyClsEncd": invtyClsEncd,
			"formNum": formNum,
			"loginTime": loginTime,
			"formSDt":formSDt,
			"formEDt":formEDt,
			"bookOkSDt":bookOkSDt,
			"bookOkEDt":bookOkEDt,
			"pageSize": rowNum,
			"pageNo": page,
		}
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		url: url + "/mis/account/form/backForm/list",
		async: true,
		data: postD2,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
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
			var arr = [];
			for(var i = 0; i < list.length; i++) {
				if(list[i].bookEntrySub != null) {
					var entrs = list[i].bookEntrySub
					for(var j = 0; j < entrs.length; j++) {
						(function(j) {
							var newObj = cloneObj(data.respBody.list);
							for(var k in entrs[j]) {
								newObj[i][k] = entrs[j][k]
							}
							arr.push(newObj[i])
						})(j)
					}
				}
			}
			var mydata = {};
			mydata.rows = arr;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#order_jqGrids").jqGrid("clearGridData");
			$("#order_jqGrids").jqGrid("setGridParam", {
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
	})
}
//恢复
$(function() {
	$(".resump").click(function() {
		var gr = $("#order_jqGrids").jqGrid('getGridParam', 'selarrrow');
		var rowDatas = [];
		for(i = 0; i < gr.length; i++) {
			//选中行的数据
			var formNum = $("#order_jqGrids").getCell(gr[i], "formNum");
			rowDatas[i] = formNum;
		}
		var rowData = rowDatas.toString();
		var data2 = {
			"reqHead": reqhead,
			"reqBody": {
				"formNum": rowData
			}
		};
		var postD2 = JSON.stringify(data2);
		$.ajax({
			type: "post",
			url: url + "/mis/account/form/back/book",
			async: true,
			data: postD2,
			dataType: 'json',
			contentType: 'application/json; charset=utf-8',
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
				alert(data.respHead.message)
				if(data.respHead.isSuccess == true) {
					search()
				}
			},
			error: function(data) {
				alert("error")
			}
		})
	})
})
