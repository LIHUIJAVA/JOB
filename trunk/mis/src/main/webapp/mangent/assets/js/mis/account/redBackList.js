$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
})

var page = 1;
var rowNum;

//表格初始化
$(function() {
	jQuery("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['供应商', '入库类别', '业务类别', '入库类型', '单据日期', '单据号', '制单人', '记账日期', '记账人', '凭证号', '凭证摘要', '仓库', '存货编码', '存货代码', '存货名称', '规格型号', '计量单位', '数量', '单价', '金额'], //jqGrid的列显示名字
		colModel: [{
				name: "provrNm",
				align: "center",
				editable: true,
			},
			{
				name: "recvSendCateNm", //入库类别
				align: "center",
				editable: true,
			},
			{
				name: 'bizTypNm',//业务类别
				align: "center",
				editable: true,
			},
			{
				name: 'outIntoWhsTypNm',
				align: "center",
				editable: true,
			},
			{
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
				name: "spcModel", 
				align: "center",
				editable: true,
			},
			{
				name: 'bookOkDt',
				align: "center",
				editable: true,
			},
			{
				name: 'bookOkAcc',
				align: "center",
				editable: true,
			},
			{
				name: "makeVouvhId",
				align: "center",
				editable: true,
			},
			{
				name: 'makeVouchAbst',
				align: "center",
				editable: true,
			},
			{
				name: 'whsEncd',
				align: "center",
				editable: true,
			},
			{
				name: 'invtyEncd',
				align: "center",
				editable: true,
			},
			{
				name: 'invtyCd',
				align: "center",
				editable: true,
			},
			{
				name: "invtyClsNm",
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
				name: 'qty',
				align: "center",
				editable: true,
			},
			{
				name: 'noTaxUprc',
				align: "center",
				editable: true,
			},
			{
				name: "noTaxAmt",
				align: "center",
				editable: true,
			}
		],
		height: 380,
		autowidth: true,
		autoScroll: true,
		shrinkToFit: false,
		viewrecords: true,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 500,
		rowList: [500, 1000, 3000, 5000],
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		multiselect: true, //复选框
		multiboxonly: true,
		caption: "红字回冲单列表", //表格的标题名字
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
		}
	})
})

$(function() {
	$(".search").click(function() {
		search()
	})
})

function search() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	
	var formNumStart = $('.formNumStart').val();
	var formNumEnd = $('.formNumEnd').val();
	var formSDt = $('.formSDt').val();
	var formEDt = $('.formEDt').val();
	var provrId = $("#provrId").val();
	var bookOkSDt = $(".bookOkSDt").val();
	var bookOkEDt = $(".bookOkEDt").val();
	var makeVouchIdStart = $("#makeVouchIdStart").val();
	var makeVouchIdEnd = $("#makeVouchIdEnd").val();

	var showData = {
		"reqHead": reqhead,
		"reqBody": {
			"formSDt": formSDt,
			"formEDt": formEDt,
			"bookOkSDt": bookOkSDt,
			"bookOkEDt": bookOkEDt,
			"formNumStart": formNumStart,
			"formNumEnd": formNumEnd,
			"makeVouchIdStart": makeVouchIdStart,
			"makeVouchIdEnd": makeVouchIdEnd,
			"provrId": provrId,
			"isRedBack": 0,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var postData = JSON.stringify(showData);
	$.ajax({
		type: "post",
		url: url + "/mis/account/form/backFlush/list", //列表
		async: true,
		data: postData,
		dataType: 'json',
		contentType: 'application/json',
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
				if(list[i].formBackFlushSub != null) {
					var entrs = list[i].formBackFlushSub
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
		error: function() {
			alert("展示失败")
		}
	})

}
