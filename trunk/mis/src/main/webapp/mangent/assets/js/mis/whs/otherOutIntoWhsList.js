$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	//点击表格图标显示仓库列表
	$(document).on('click', '.biao_whsEncd', function() {
		window.open("../../Components/baseDoc/whsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	//	存货分类
	$(document).on('click', '.invtyClsEncd_biaoge', function() {
		window.open("../../Components/baseDoc/invtyTree.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})

var count;
var pages;
var rowNum;
//表格初始化
$(function() {
	localStorage.removeItem("index")
	localStorage.removeItem("sortorder")
	allHeight()
	$("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['单据号', '单据日期', '来源单据号', '仓库编码', '仓库名称', '出入库类型', '出库状态', '入库状态', '表头备注', '是否向WMS上传',
			'是否审核', '是否记账', '是否完成', '是否关闭', '打印次数', '创建人', '创建时间', '修改人', '修改时间', '审核人',
			'审核时间', '记账人', '记账时间', '操作人', '操作人编码', '是否生成凭证','制凭证人','制凭证时间','出入库类型编码', '出入库类型名称', '单据类型编码', '单据类型名称',
			'序号', '存货编码', '数量', '税率', '税额', '含税单价', '未税单价', '记账单价', '含税金额', '未税金额', '国际批号', '批号',
			'生产日期', '保质期', '失效日期', '存货名称', '规格型号', '存货代码', '箱规', '箱数', '计量单位名称', '对应条形码', '存货分类编码',
			'存货分类名称', '计量单位编码','调拨单出库仓库编码	','调拨单入库仓库编码','调拨单出库仓库名称','调拨单入库仓库名称','表体备注'
		],
		colModel: [{
				name: 'formNum',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'formDt',
				editable: true,
				align: 'center',
			},
			{
				name: 'srcFormNum',
				editable: true,
				align: 'center',
				sortable: false

			},
			{
				name: 'whsEncd',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'whsNm',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'outIntoWhsTyp',
				editable: false,
				align: 'center',
				sortable: false,
				hidden:true
			},
			{
				name: 'outStatus',
				editable: false,
				align: 'center',
				sortable: false,
				hidden:true
			},
			{
				name: 'inStatus',
				editable: false,
				align: 'center',
				sortable: false,
				hidden:true
			},
			{
				name: 'memo',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'isNtWms',
				editable: true,
				align: 'center',
				sortable: false,
				hidden:true
			},
			{
				name: 'isNtChk',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'isNtBookEntry',
				editable: true,
				align: 'center',
				sortable: false,
				hidden:true
			},
			{
				name: 'isNtCmplt',
				editable: true,
				align: 'center',
				sortable: false,
				hidden:true
			},
			{
				name: 'isNtClos',
				editable: true,
				align: 'center',
				sortable: false,
				hidden:true
			},
			{
				name: 'printCnt',
				editable: true,
				align: 'center',
				sortable: false,
				hidden:true
			},
			{
				name: 'setupPers',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'setupTm',
				editable: true,
				align: 'center',
			},
			{
				name: 'mdfr',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'modiTm',
				editable: true,
				align: 'center',
			},
			{
				name: 'chkr',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'chkTm',
				editable: true,
				align: 'center',
			},
			{
				name: 'bookEntryPers',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'bookEntryTm',
				editable: true,
				align: 'center',
			},
			{
				name: 'operator',
				editable: true,
				align: 'center',
				sortable: false,
				hidden:true
			},
			{
				name: 'operatorId',
				editable: true,
				align: 'center',
				sortable: false,
				hidden:true
			},
			{
				name: 'isNtMakeVouch',
				editable: true,
				align: 'center',
				sortable: false,
//				hidden:true
			},
			{
				name: 'makVouchPers',
				editable: true,
				align: 'center',
				sortable: false,
//				hidden:true
			},
			{
				name: 'makVouchTm',
				editable: true,
				align: 'center',
				sortable: false,
//				hidden:true
			},
			{
				name: 'outIntoWhsTypId',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'outIntoWhsTypNm',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'formTypEncd',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'formTypName',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'ordrNum',
				editable: true,
				align: 'center',
				sortable: false,
				hidden:true
			},
			{
				name: 'invtyEncd',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'qty',
				editable: true,
				align: 'center',
			},
			{
				name: 'taxRate',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'taxAmt',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'cntnTaxUprc',
				editable: true,
				align: 'center',
			},
			{
				name: 'unTaxUprc',
				editable: true,
				align: 'center',
			},
			{
				name: 'bookEntryUprc',
				editable: true,
				align: 'center',
			},
			{
				name: 'cntnTaxAmt',
				editable: true,
				align: 'center',
			},
			{
				name: 'unTaxAmt',
				editable: true,
				align: 'center',
			},
			{
				name: 'intlBat',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'batNum',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'prdcDt',
				editable: true,
				align: 'center',
			},
			{
				name: 'baoZhiQi',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'invldtnDt',
				editable: true,
				align: 'center',
			},
			{
				name: 'invtyNm',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'spcModel',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'invtyCd',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'bxRule',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'bxQty',
				editable: true,
				align: 'center',
			},
			{
				name: 'measrCorpNm',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'crspdBarCd',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'invtyClsEncd',
				editable: true,
				align: 'center',
				sortable: false,
				hidden:true
			},
			{
				name: 'invtyClsNm',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'measrCorpId',
				editable: true,
				align: 'center',
				sortable: false,
				hidden:true
			},
			{
				name: 'outWhsEncd',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'inWhsEncd',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'outWhsNm',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'inWhsNm',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'memos',
				editable: true,
				align: 'center',
				sortable: false,
			}
		],
		autowidth: true,
		height: height,
		autoScroll: true,
		shrinkToFit: false,
		rownumbers: true,
		loadonce: true,
		sortable:true,
		forceFit: true,
		rowNum: 500,
		rowList: [500, 1000, 3000, 5000],
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
//		multiboxonly: true,
		caption: "其他出入库列表", //表格的标题名字
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
			var index = localStorage.getItem("index")
			var sortorder = localStorage.getItem("sortorder")
			if(index == null) {
				var index = ''
			}
			if(sortorder == null) {
				var sortorder = ''
			}
			window.newPage = page;
			search(index, sortorder, page)
		},
		//双击弹出采购订单
		ondblClickRow: function(rowid) {
			order(rowid);
		},
		footerrow: true,
		gridComplete: function() { 
			var qty = $("#jqGrids").getCol('qty', false, 'sum');
			var cntnTaxAmt = $("#jqGrids").getCol('cntnTaxAmt', false, 'sum');
			var unTaxAmt = $("#jqGrids").getCol('unTaxAmt', false, 'sum');
			
			$("#jqGrids").footerData('set', { 
				"formNum": "本页合计",
				qty: qty.toFixed(prec),
				cntnTaxAmt : precision(cntnTaxAmt,2),
				unTaxAmt : precision(unTaxAmt,2),

			}          );    
		},
		onSortCol: function(index, colindex, sortorder) {
			switch(index) {
				case "formDt":
					index = "oi.form_dt"
					break;
				case "setupTm":
					index = "oi.setup_tm"
					break;
				case "modiTm":
					index = "oi.modi_tm"
					break;
				case "chkTm":
					index = "oi.chk_tm"
					break;
				case "bookEntryTm":
					index = "oi.book_entry_tm"
					break;
				case "qty":
					index = "oiTab.qty"
					break;
				case "cntnTaxUprc":
					index = "oiTab.cntn_tax_uprc"
					break;
				case "unTaxUprc":
					index = "oiTab.un_tax_uprc"
					break;
				case "bookEntryUprc":
					index = "oiTab.book_entry_uprc"
					break;
				case "cntnTaxAmt":
					index = "oiTab.cntn_tax_amt"
					break;
				case "unTaxAmt":
					index = "oiTab.un_tax_amt"
					break;
				case "prdcDt":
					index = "oiTab.prdc_dt"
					break;
				case "invldtnDt":
					index = "oiTab.invldtn_dt"
					break;
				case "bxQty":
					index = "oiTab.bx_qty"
					break;
			}
			localStorage.setItem("index",index)
			localStorage.setItem("sortorder",sortorder)
			window.index = index;
			window.sortorder = sortorder;
			search(index, sortorder, newPage)      
		}
	})
})

//查询按钮
$(document).on('click', '.search', function() {
	var initial = 1;
	window.newPage = initial;
	var index = '';
	var sortorder = '';
	search(index, sortorder,initial)
})

function search(index, sortorder, page){
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var whsEncd = $("input[name='whsEncd']").val();
	var formNum = $("input[name='formNum']").val();
	var srcFormNum = $("input[name='srcFormNum']").val();
	var formDt1 = $("input[name='formDt1']").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var formDt2 = $("input[name='formDt2']").val();
	var outIntoWhsTyp = $("select[name='outIntoWhsTyp']").val();
	var isNtChk = $("select[name='isNtChk']").val();
	var memo = $("input[name='memo']").val();
	var memos = $("input[name='memos']").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var batNum = $("input[name='batNum']").val();
	var setupPers = $("input[name='userName']").val()

	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"formNum": formNum,
			"srcFormNum": srcFormNum,
			"formDt1": formDt1,
			"formDt2": formDt2,
			"memo": memo,
			"memos": memos,
			"invtyClsEncd": invtyClsEncd,
			"invtyEncd": invtyEncd,
			"batNum": batNum,
			"outIntoWhsTypId": outIntoWhsTyp,
			"whsEncd": whsEncd,
			'isNtChk': isNtChk,
			"setupPers": setupPers,
			"sort":index,
			"sortOrder":sortorder,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/whs/out_into_whs/queryList',
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
			if(list) {
				for(var i = 0; i < list.length; i++) {
					if(list[i].isNtChk == 0) {
						list[i].isNtChk = "否"
					} else if(list[i].isNtChk == 1) {
						list[i].isNtChk = "是"
					}
					if(list[i].isNtWms == 0) {
						list[i].isNtWms = "否"
					} else if(list[i].isNtWms == 1) {
						list[i].isNtWms = "是"
					}
					if(list[i].isNtBookEntry == 0) {
						list[i].isNtBookEntry = "否"
					} else if(list[i].isNtBookEntry == 1) {
						list[i].isNtBookEntry = "是"
					}
					if(list[i].isNtCmplt == 0) {
						list[i].isNtCmplt = "否"
					} else if(list[i].isNtCmplt == 1) {
						list[i].isNtCmplt = "是"
					}
					if(list[i].isNtClos == 0) {
						list[i].isNtClos = "否"
					} else if(list[i].isNtClos == 1) {
						list[i].isNtClos = "是"
					}
					if(list[i].isNtMakeVouch == 0) {
						list[i].isNtMakeVouch = "否"
					} else if(list[i].isNtMakeVouch == 1) {
						list[i].isNtMakeVouch = "是"
					}
				}
			}
			var mydata = {};
			mydata.rows = list;
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
			alert("查询失败")
		}
	});
}
//整单联查
$(document).on('click', '.joint_search', function() {
	var gr = $("#jqGrids").jqGrid('getGridParam', 'selarrrow');
	if(gr.length == 0) {
		alert("请选择单据")
		return;
	} else if(gr.length > 1){
		alert("请选择单条数据")
		return;
	} else {
		var rowid = $("#jqGrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDatas = $("#jqGrids").jqGrid('getRowData', rowid);
		var formNum = rowDatas.formNum;
		var formTypEncd = rowDatas.formTypEncd;
		window.open("../../Components/system/jointSearch.html?formNum=" + formNum + "&formTypEncd=" + formTypEncd, 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	}
})

function order(rowid) {
	//获得行数据
	var rowDatas = $("#jqGrids").jqGrid('getRowData', rowid);
	localStorage.setItem("formNum", rowDatas.formNum);
	localStorage.setItem("isNtChk", rowDatas.isNtChk);
	window.open("../../Components/whs/otherOutIntoWhs.html?1");
}

var chk1 = '/mis/whs/out_into_whs/updateOutInWhsChk';
var chk2 = '/mis/whs/out_into_whs/updateOutInWhsNoChk';

function ntChk(x, chk) {
	//获得选中行的行号
	var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
	//获取选择行的provrId
	//对象数组
	var rowData = [];
	for(var i = 0; i < ids.length; i++) {
		var obj = {}; //对象
		//选中行的id
		//把选中行的i添加到数据对象中
		obj.formNum = $("#jqGrids").getCell(ids[i], "formNum").toString();
		obj.isNtChk = x;
		//建一个数组，把单个对象添加到数组中
		rowData[i] = obj;
	}
	if(rowData.length == 0) {
		alert("请选择单据!")
	} else {
		var formNum = rowData.toString();
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				"list": rowData
			}
		};
		var Data = JSON.stringify(data);
		$.ajax({
			url: url + chk,
			type: 'post',
			data: Data,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
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
				alert(data.respHead.message);
				if(data.respHead.isSuccess==true){
					var initial = 1;
					window.newPage = initial;
					var index = '';
					var sortorder = '';
					search(index, sortorder,initial)
				}
			},
			error: function() {
				alert("审核失败")
			}
		})
	}

}
var isclick = true;
//审核与弃审
$(function() {
	//审核
	$(".toExamine").click(function() {
		if(isclick) {
			isclick = false;
			ntChk(1, chk1);
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	});
	//弃审
	$(".noTo").click(function() {
		if(isclick) {
			isclick = false;
			ntChk(0, chk2);
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	})
})

//删除 自动分配货位
$(function() {
	$(".delOrder").click(function() {
		//获得选中行的行号
		var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
		//获取选择行的provrId
		var rowData = [];
		for(var i = 0; i < ids.length; i++) {
			var data = $("#jqGrids").getCell(ids[i], "formNum");
			//建一个数组，把选中行的id添加到这个数组中去。
			rowData[i] = data;
		}
		if(rowData.length == 0) {
			alert("请选择单据!")
		} else if(confirm("确定删除？")) {
			var formNum = rowData.toString();
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"formNum": formNum,
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/whs/out_into_whs/deleteAllOthOutIntoWhs',
				type: 'post',
				data: Data,
				dataType: 'json',
				async: true,
				contentType: 'application/json;charset=utf-8',
				success: function(data) {
					alert(data.respHead.message);
					if(data.respHead.isSuccess==true){
						var initial = 1;
						window.newPage = initial;
						var index = '';
						var sortorder = '';
						search(index, sortorder,initial)
					}
				},
				error: function() {
					alert("删除失败")
				}
			})
		}
	})
	$(".voluntarily").click(function() {
		//获得选中行的行号
		var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
		//获取选择行的provrId
		var rowData = [];
		var rowDatas = [];
		for(var i = 0; i < ids.length; i++) {
			var data = $("#jqGrids").getCell(ids[i], "formNum");
			var datas = $("#jqGrids").getCell(ids[i], "isNtChk");
			//建一个数组，把选中行的id添加到这个数组中去。
			rowData[i] = data;
			rowDatas[i] = datas;
		}
		console.log(rowDatas)
		var have = $.inArray("是", rowDatas)
		console.log(have)
		if(rowData.length == 0) {
			alert("请选择单据!")
		} else if(have == 0) {
			alert("存在审核单据,请选择未审单据")
			return;
		} else if(confirm("确定分配？")) {
			var formNum = rowData.toString();
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"formNum": formNum,
				}
			};
			var Data = JSON.stringify(data);
			console.log(Data)
			$.ajax({
				url: url + '/mis/whs/out_into_whs/allInvtyGdsBitList',
				type: 'post',
				data: Data,
				dataType: 'json',
				async: true,
				contentType: 'application/json;charset=utf-8',
				success: function(data) {
					alert(data.respHead.message);
					if(data.respHead.isSuccess==true){
						var initial = 1;
						window.newPage = initial;
						var index = '';
						var sortorder = '';
						search(index, sortorder,initial)
					}
				},
				error: function() {
					alert("删除失败")
				}
			})
		}
	})
})

// 导入
$(document).ready(function(){
    $('.dropdown-menu li').click(function(){
        var rs=$(this).text();
        $('#dropdownMenu2').text(rs);
    });
    //  	alert("U8导入")
    $('.dropdown-menu .li1').click(function(){
		var files = $("#FileUpload").val()
		var fileObj = document.getElementById("FileUpload").files[0];
		var formFile = new FormData();
		formFile.append("action", "UploadVMKImagePath");
		formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
		var data = formFile;
		if(files != "") {
			$.ajax({
				type: 'post',
				url: url + "/mis/whs/out_into_whs/uploadFileAddDbU8",
				data: data,
				dataType: "json",
				cache: false, //上传文件无需缓存
				processData: false, //用于对data参数进行序列化处理 这里必须false
				contentType: false, //必须
				success: function(data) {
					alert(data.respHead.message)
				},
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
			});
		} else {
			alert("请选择文件")
		}
	});
    //  	alert("新系统导入")
    $('.dropdown-menu .li2').click(function(){
		var files = $("#FileUpload").val()
		var fileObj = document.getElementById("FileUpload").files[0];
		var formFile = new FormData();
		formFile.append("action", "UploadVMKImagePath");
		formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
		var data = formFile;
		if(files != "") {
			$.ajax({
				type: 'post',
				url: url + "/mis/whs/out_into_whs/uploadFileAddDb",
				data: data,
				dataType: "json",
				cache: false, //上传文件无需缓存
				processData: false, //用于对data参数进行序列化处理 这里必须false
				contentType: false, //必须
				success: function(data) {
					alert(data.respHead.message)
				},
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
			});
		} else {
			alert("请选择文件")
		}
	});
});
var arr = [];
var obj = {}
//导出
$(document).on('click', '.exportExcel', function() {
	var whsEncd = $("input[name='whsEncd']").val();
	var formNum = $("input[name='formNum']").val();
	var formDt1 = $("input[name='formDt1']").val();
	var formDt2 = $("input[name='formDt2']").val();
	var outIntoWhsTyp = $("select[name='outIntoWhsTyp']").val();
	var isNtChk = $("select[name='isNtChk']").val();
	var srcFormNum = $("input[name='srcFormNum']").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var batNum = $("input[name='batNum']").val();
	
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"formNum": formNum,
			"formDt1": formDt1,
			"formDt2": formDt2,
			"outIntoWhsTypId": outIntoWhsTyp,
			"whsEncd": whsEncd,
			"srcFormNum": srcFormNum,
			"invtyClsEncd": invtyClsEncd,
			"invtyEncd": invtyEncd,
			"batNum": batNum,
			'isNtChk': isNtChk,
		}
	};
	var Data = JSON.stringify(savedata);
	$.ajax({
		url: url + '/mis/whs/out_into_whs/queryListDaYin',
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
			var execlName = '其他出入库'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})

})