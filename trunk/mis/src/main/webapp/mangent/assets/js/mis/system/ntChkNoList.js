var count;
var pages;
var rowNum;

$(function() {
	pageInit();
})

//表格初始化
function pageInit() {
	localStorage.removeItem("index")
	localStorage.removeItem("sortorder")
	allHeight()
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	jQuery("#jqgrids").jqGrid({
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		url: '../../assets/js/json/order.json',
		colNames: ['单据号', '单据类型编码', '单据类型名称', '单据日期', '创建人', '创建时间'], //jqGrid的列显示名字
		colModel: [{
				name: "formNum",
				align: "center",
				sortable: false
			},
			{
				name: "formTypEncd",
				align: "center",
				sortable: false,
			},
			{
				name: "formTypName",
				align: "center",
				sortable: false,
			},
			{
				name: 'formDt',
				align: "center",
			},
			{
				name: 'setupPers',
				align: "center",
				sortable: false,
			},
			{
				name: "setupTm",
				align: "center",
			}
		],
		loadonce: false,
		rownumbers: true,
		autowidth: true,
		height: height,
		rownumWidth:20,
		sortable: true,
		autoScroll: true,
		shrinkToFit: false,
		multiselect: true, //复选框 
//		multiboxonly: true,
		altclass: true,
		rowNum: 500,
		viewrecords: true,
		forceFit: true,
		sortorder: "desc",
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条
		pager: '#jqGridPager',
		caption: "未审核单据", //表格的标题名字
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
			var index = localStorage.getItem("index")
			var sortorder = localStorage.getItem("sortorder")
			if(index == null) {
				var index = ''
			}
			if(sortorder == null) {
				var sortorder = ''
			}
			window.newPage = page;
			loadLocalData(index, sortorder, page)
		},
		ondblClickRow: function(rowid) {
			order(rowid);
		},
		onSortCol: function(index, colindex, sortorder) {
			localStorage.setItem("index",index)
			localStorage.setItem("sortorder",sortorder)
			window.index = index;
			window.sortorder = sortorder;
			loadLocalData(index, sortorder, newPage)
		}
	})
}

function order(rowid) {
	//获得行数据
	var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
	localStorage.setItem("isNtChk", "否");
	if(rowDatas.formTypEncd == "001") { //采购订单
		localStorage.setItem("pursOrdrId", rowDatas.formNum);
		localStorage.setItem("ntChkNo", "1");
		window.open("../../Components/purs/purchaseOrder.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');
	} else if(rowDatas.formTypEncd == "003") { //到货拒收单
		localStorage.setItem("toGdsSnglId", rowDatas.formNum);
		window.open("../../Components/purs/returnToGdsSngl.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');
	} else if(rowDatas.formTypEncd == "002") { //到货单
		localStorage.setItem("toGdsSnglId", rowDatas.formNum);
		window.open("../../Components/purs/toGdsSngl.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');
	} else if(rowDatas.formTypEncd == "004") { //采购入库
		localStorage.setItem("intoWhsSnglId", rowDatas.formNum);
		window.open("../../Components/purs/intoWhs.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');
	} else if(rowDatas.formTypEncd == "006") { //采购退货单
		localStorage.setItem("intoWhsSnglId", rowDatas.formNum);
		window.open("../../Components/purs/returnWhs.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');
	} else if(rowDatas.formTypEncd == "007") { //销售单
		localStorage.setItem("sellSnglId", rowDatas.formNum);
		window.open("../../Components/sell/sellSngl.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');
	} else if(rowDatas.formTypEncd == "008") { //退货单
		localStorage.setItem("rtnGoodsId", rowDatas.formNum);
		window.open("../../Components/sell/returnOrder.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');
	} else if(rowDatas.formTypEncd == "009") { //销售出库单
		localStorage.setItem("outWhsId", rowDatas.formNum);
		window.open("../../Components/sell/sellOutWhs.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');
	} else if(rowDatas.formTypEncd == "010") { //红字销售出库单
		localStorage.setItem("outWhsId", rowDatas.formNum);
		window.open("../../Components/sell/returnSellOutWhs.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');
	} else if(rowDatas.formTypEncd == "023") { //委托代销发货单
		localStorage.setItem("delvSnglId", rowDatas.formNum);
		window.open("../../Components/sell/entrsAgnDelv.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');
	} else if(rowDatas.formTypEncd == "024") { //委托代销退货单
		localStorage.setItem("delvSnglId", rowDatas.formNum);
		window.open("../../Components/sell/entrsAgnReturn.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');
	} else if(rowDatas.formTypEncd == "011") { //调拨单
		localStorage.setItem("formNum", rowDatas.formNum);
		window.open("../../Components/whs/cannibSngl.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');
	} else if(rowDatas.formTypEncd == "012") { //组装
		localStorage.setItem("formNum", rowDatas.formNum);
		window.open("../../Components/whs/ambDisambZ.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');
	} else if(rowDatas.formTypEncd == "019") { //采购专用发票
		localStorage.setItem("pursInvNum", rowDatas.formNum);
		window.open("../../Components/purs/pursInvMasTab.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');
	} else if(rowDatas.formTypEncd == "020") { //采购普通发票
		localStorage.setItem("pursInvNum", rowDatas.formNum);
		window.open("../../Components/purs/pursOrdinaryTab.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');
	} else if(rowDatas.formTypEncd == "021") { //销售专用发票
		localStorage.setItem("sellInvNum", rowDatas.formNum);
		window.open("../../Components/purs/sellInvMasTab.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');
	} else if(rowDatas.formTypEncd == "022") { //销售普通发票
		localStorage.setItem("sellInvNum", rowDatas.formNum);
		window.open("../../Components/purs/sellOrdinaryTab.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');
	} else if(rowDatas.formTypEncd == "013") { //拆卸
		localStorage.setItem("formNum", rowDatas.formNum);
		window.open("../../Components/whs/ambDisambC.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');
	} else if(rowDatas.formTypEncd == "028") { //盘点单
		localStorage.setItem("checkFormNum", rowDatas.formNum);
		window.open("../../Components/whs/checkSngl.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');
	} else if(rowDatas.formTypEncd == "029") { //盘点损益单
		localStorage.setItem("checkFormNum", rowDatas.formNum);
		localStorage.setItem("isNtChk", 0);
		window.open("../../Components/whs/checkSnglLoss.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');
	} else if(rowDatas.formTypEncd == "014" || rowDatas.formTypEncd == "015") { //其他出入库
		localStorage.setItem("formNum", rowDatas.formNum);
		window.open("../../Components/whs/otherOutIntoWhs.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');
	}
}

$(function() {
	$("#find").click(function() {
		var initial = 1;
		window.newPage = initial;
		var index = '';
		var sortorder = '';
		loadLocalData(index, sortorder,initial)
	})
})

function loadLocalData(index, sortorder,page) {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var formNum = $(".formNum").val();
	var formTypeEncd = $(".formTypeEncd").val();
	var setPers = $("#userName").val();
	var formDt1 = $(".formDt1").val();
	var formDt2 = $(".formDt2").val();
	var showData = {
		"reqHead": reqhead,
		"reqBody": {
			"formNum": formNum,
			"formTypEncd": formTypeEncd,
			"setupPers": setPers,
			"formDt1": formDt1,
			"formDt2": formDt2,
			"sort":index,
			"sortOrder":sortorder,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var postData = JSON.stringify(showData);
	$.ajax({
		type: "post",
		url: url + "/mis/whs/invty_tab/selectNtChkNoList", //列表
		async: true,
		data: postData,
		dataType: 'json',
		contentType: 'application/json',
		//开始加载动画  添加到ajax里面
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
			var mydata = {};
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
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
		error: function() {
			alert("加载失败")
		}
	})
}
var arr=[];
var obj={}
//导出
$(document).on('click', '.exportExcel', function() {
	var formNum = $(".formNum").val();
	var formTypeEncd = $(".formTypeEncd").val();
//	var formTypeNm = $(".formTypeNm").val();
	var formDt1 = $(".formDt1").val();
	var formDt2 = $(".formDt2").val();
	var setPers = $("#user").val();
	
	var showData = {
		"reqHead": reqhead,
		"reqBody": {
			"formNum": formNum,
			"formTypEncd": formTypeEncd,
//			"formTypeNm": formTypeNm,
			"setPers": setPers,
			"formDt1": formDt1,
			"formDt2": formDt2
		}
	};
	var Data = JSON.stringify(showData);
	$.ajax({
		url: url + "/mis/whs/invty_tab/selectNtChkNoList",
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
			var execlName = '未审核单据'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
	
})