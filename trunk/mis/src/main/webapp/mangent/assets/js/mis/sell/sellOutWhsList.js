//销售出库列表
var pages;
var page = 1;
var rowNum;
var tableSums = {};
$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	$("#box").hide()
})

//表格初始化
$(function() {
	localStorage.removeItem("index")
	localStorage.removeItem("sortorder")
	allHeight();
	$("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['出库单号', '销售单号','存货名称','数量',  '客户简称', '出库日期', '销售类型', '业务类型', '业务员', '部门', '客户编码','表头备注', '表体备注', '是否退货', '是否审核',
		'仓库名称', '仓库编码', '存货编码', '规格型号', '主计量单位', '主计量单位编码',  '含税单价', '价税合计',
			'无税单价', '无税金额', '税率', '税额', '箱规', '箱数', '对应条形码', '批次','生产日期', '失效日期', '保质期', '国际批次','是否退货','项目编码', '项目名称'
		],
		colModel: [{
				name: 'outWhsId',
				editable: true,
				align: 'center',
			},
			{
				name: 'sellOrdrInd',
				editable: true,
				align: 'center'
			},
			{
				name: 'invtyNm',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'qty',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'custNm',
				editable: false,
				align: 'center',
			},
			{
				name: 'outWhsDt',
				editable: true,
				align: 'center',
				sorttype:'date' // 时间排序

			},
			{
				name: 'sellTypNm',
				editable: true,
				align: 'center'

			},
			{
				name: 'bizTypNm',
				editable: false,
				align: 'center',
			},
			{
				name: 'userName',
				editable: false,
				align: 'center'
			},
			{
				name: 'deptName',
				editable: false,
				align: 'center'
			},
			{
				name: 'custId',
				editable: false,
				align: 'center',
			},
			{
				name: 'memo',
				editable: true,
				align: 'center'
			},
			{
				name: 'memos',
				editable: true,
				align: 'center'
			},
			{
				name: 'isNtRtnGood',
				editable: true,
				align: 'center'
			},
			{
				name: 'isNtChk',
				editable: true,
				align: 'center'
			},
			{
				name: 'whsNm',
				editable: false,
				align: 'center',
				sortable: false,
			},
			{
				name: 'whsEncd',
				editable: true,
				align: 'center',
				sortable: false,
				//				hidden: true
			},
			{
				name: 'invtyEncd',
				editable: true,
				align: 'center',
				sortable: false

			},
			{
				name: 'spcModel',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'measrCorpNm',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'measrCorpId',
				editable: false,
				align: 'center',
				hidden: true,
				sortable: false
			},
			{
				name: 'cntnTaxUprc',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'prcTaxSum',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'noTaxUprc',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'noTaxAmt',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'taxRate',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'taxAmt',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'bxRule',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'bxQty',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'crspdBarCd',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'batNum',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'prdcDt', //生产日期
				editable: true,
				align: 'center',
				editoptions: {
					dataInit: function(element) {
						$(element).datepicker({
							dateFormat: 'yy-mm-dd'
						})
					}
				},
				sortable: false
			},
			{
				name: 'invldtnDt', //失效日期
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'baoZhiQi',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'intlBat', //国际批次
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'isNtRtnGoods',
				editable: true,
				align: 'center',
				hidden: true,
				sortable: false
			},
			{
				name: 'projEncd',
				editable: true,
				align: 'center',
				sortable: false

			},
			{
				name: 'projNm',
				editable: false,
				align: 'center',
				sortable: false
			}
		],
		sortable:true,
		autowidth: true,
		height: height,
		autoScroll: true,
		sortable:true,// 列拖拽
		shrinkToFit: false,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 500,
		rowList: [500, 1000, 3000,5000],
		pager: '#jqgridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
//		multiboxonly: true,
		caption: "销售出库单列表", //表格的标题名字
		footerrow: true,
		gridComplete: function() {
			$("#jqGrids").footerData('set', { 
				"outWhsId": "总计",
				"qty": tableSums.qty,
				"prcTaxSum": tableSums.prcTaxSum,
				"noTaxAmt": tableSums.noTaxAmt,
				"taxAmt": tableSums.taxAmt
			}          );
		},
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
				var sortorder = 'desc'
			}
			search(index, sortorder);
		},
		//双击弹出采购订单
		ondblClickRow: function(rowid) {
			order(rowid);
		},
		onSortCol: function(index, colindex, sortorder) {
			switch(index) {
				case "outWhsId":
					index = "sow.out_whs_id"
					break;
				case "outWhsDt":
					index = "sow.out_whs_dt"
					break;
				case "qty":
					index = "sows.qty"
					break;
				case "cntnTaxUprc":
					index = "sows.cntn_tax_uprc"
					break;
				case "prcTaxSum":
					index = "sows.prc_tax_sum"
					break;
				case "noTaxUprc":
					index = "sows.no_tax_uprc"
					break;
				case "noTaxAmt":
					index = "sows.no_tax_amt"
					break;
				case "taxAmt":
					index = "sows.tax_amt"
					break;
				case "bxQty":
					index = "sows.bx_qty"
					break;
				case "prdcDt":
					index = "sows.prdc_dt"
					break;
				case "invldtnDt":
					index = "sows.invldtn_dt"
					break;
			}
			localStorage.setItem("index",index)
			localStorage.setItem("sortorder",sortorder)
			window.index = index;
			window.sortorder = sortorder;
			search(index, sortorder)
		}
	})

})

//查询按钮
$(document).on('click', '.search', function() {
	var outWhsDt1 = $("input[name='outWhsDt1']").val()
    $("input[name='outWhsDt3']").val(outWhsDt1)
    
    var outWhsDt2 = $("input[name='outWhsDt2']").val()
    $("input[name='outWhsDt4']").val(outWhsDt2)
    
    $("input[name='sellOrdrInd']").attr('class','sellOrdrInd');
    $("input[name='sellOrdrInd1']").attr('class','');
    
    $("input[name='custOrdrNum']").attr('class','custOrdrNum');
    $("input[name='custOrdrNum1']").attr('class','');
    
	var index = ''
	var sortorder = 'desc'
	search(index, sortorder)
})
//查询按钮
$(document).on('click', '.sure', function() {
	$("#big_wrap").hide()
	$("#box").hide()
	var outWhsDt1 = $("input[name='outWhsDt3']").val()
    $("input[name='outWhsDt1']").val(outWhsDt1)
    
    var outWhsDt2 = $("input[name='outWhsDt4']").val()
    $("input[name='outWhsDt2']").val(outWhsDt2)
	var index = ''
	var sortorder = 'desc'
	search(index, sortorder)
	
	$("input[name='invtyEncd']").attr('id', 'invtyEncd');
	$("input[name='invtyEncd1']").attr('id', '');
	$("input[name='invtyNm']").attr('id', 'invtyNm');
	$("input[name='invtyNm1']").attr('id', '');
	
	$("input[name='invtyClsEncd']").attr('id', 'invtyCls');
	$("input[name='invtyClsEncd1']").attr('id', '');
	$("input[name='invtyClsNm']").attr('id', 'invtyClsNm');
	$("input[name='invtyClsNm1']").attr('id', '');
	
	$("input[name='custId']").attr('id', 'custId');
	$("input[name='custId1']").attr('id', '');
	$("input[name='custNm']").attr('id', 'custNm');
	$("input[name='custNm1']").attr('id', '');
	var sellOrdrInd = $("input[name='sellOrdrInd1']").val()
    $("input[name='sellOrdrInd']").val(sellOrdrInd)
    
    var custOrdrNum = $("input[name='custOrdrNum1']").val()
    $("input[name='custOrdrNum']").val(custOrdrNum)
})

$(function() {
	$(".more").click(function() {
		$("#big_wrap").show()
		$("#box").show()
        $("input[name='invtyEncd1']").attr('id','invtyEncd');
        $("input[name='invtyEncd']").attr('id','');
        $("input[name='invtyNm1']").attr('id','invtyNm');
        $("input[name='invtyNm']").attr('id','');
		var invtyEncd = $("input[name='invtyEncd']").val()
		$("input[name='invtyEncd1']").val(invtyEncd)
		var invtyNm = $("input[name='invtyNm']").val()
		$("input[name='invtyNm1']").val(invtyNm)
        
		$("input[name='invtyClsEncd1']").attr('id','invtyCls');
        $("input[name='invtyClsEncd']").attr('id','');
        var invtyClsEncd = $("input[name='invtyClsEncd']").val()
        $("input[name='invtyClsEncd1']").val(invtyClsEncd)
        
        $("input[name='invtyClsNm1']").attr('id','invtyClsNm');
        $("input[name='invtyClsNm']").attr('id','');
        var invtyClsNm = $("input[name='invtyClsNm']").val()
        $("input[name='invtyClsNm1']").val(invtyClsNm)
        
		$("input[name='custId1']").attr('id','custId');
        $("input[name='custId']").attr('id','');
        var custId = $("input[name='custId']").val();
        $("input[name='custId1']").val(custId);
        
        $("input[name='custNm1']").attr('id','custNm');
        $("input[name='custNm']").attr('id','');
        var custNm = $("input[name='custNm']").val();
        $("input[name='custNm1']").val(custNm);
        
		$("input[name='outWhsId1']").attr('class','outWhsId');
        $("input[name='outWhsId']").attr('class','');
        var outWhsId = $("input[name='outWhsId']").val()
        $("input[name='outWhsId1']").val(outWhsId)
        
        $("input[name='sellOrdrInd1']").attr('class','sellOrdrInd');
        $("input[name='sellOrdrInd']").attr('class','');
        var sellOrdrInd = $("input[name='sellOrdrInd']").val()
        $("input[name='sellOrdrInd1']").val(sellOrdrInd)
        
        $("input[name='custOrdrNum1']").attr('class','custOrdrNum');
        $("input[name='custOrdrNum']").attr('class','');
        var custOrdrNum = $("input[name='custOrdrNum']").val()
        $("input[name='custOrdrNum1']").val(custOrdrNum)
        
		$("select[name='isNtChk1']").attr('id','isNtChk');
        $("select[name='isNtChk']").attr('id','');
        var isNtChk = $("select[name='isNtChk']").val()
        $("select[name='isNtChk1']").val(isNtChk)
        
        var outWhsDt1 = $("input[name='outWhsDt1']").val()
        $("input[name='outWhsDt3']").val(outWhsDt1)
        
        var outWhsDt2 = $("input[name='outWhsDt2']").val()
        $("input[name='outWhsDt4']").val(outWhsDt2)
        
		$("input[name='outWhsDt3']").attr('class','outWhsDt1 date date1');
        $("input[name='outWhsDt1']").attr('class','date date1');
        
		$("input[name='outWhsDt4']").attr('class','outWhsDt2 date date2');
        $("input[name='outWhsDt2']").attr('class','date date2');
	})
	$(".cancel").click(function(e) {
		$("#big_wrap").hide()
		$("#box").hide()
       	back();
       	$("#box input").val("")
		$("#box select").val("")
   	});
})

function back(){
	$("input[name='invtyEncd1']").attr('id','');
    $("input[name='invtyEncd']").attr('id','invtyEncd');
    $("input[name='invtyNm1']").attr('id','');
    $("input[name='invtyNm']").attr('id','invtyNm');
    
	$("input[name='invtyClsEncd1']").attr('id','');
    $("input[name='invtyClsEncd']").attr('id','invtyCls');
    
    $("input[name='invtyClsNm1']").attr('id','');
    $("input[name='invtyClsNm']").attr('id','invtyClsNm');
    
	$("input[name='custId1']").attr('id','');
    $("input[name='custId']").attr('id','custId');
    
    $("input[name='custNm1']").attr('id','');
    $("input[name='custNm']").attr('id','custNm');
    
	$("input[name='outWhsId1']").attr('class','');
    $("input[name='outWhsId']").attr('class','outWhsId');
    
    $("input[name='sellOrdrInd1']").attr('class','');
    $("input[name='sellOrdrInd']").attr('class','sellOrdrInd');
    
    $("input[name='custOrdrNum1']").attr('class','');
    $("input[name='custOrdrNum']").attr('class','custOrdrNum');
    
	$("select[name='isNtChk1']").attr('id','');
    $("select[name='isNtChk']").attr('id','isNtChk');
    
	$("input[name='outWhsDt3']").attr('calendar','');
    $("input[name='outWhsDt1']").attr('calendar','YYYY-MM-DD');
    
	$("input[name='outWhsDt4']").attr('calendar','');
    $("input[name='outWhsDt2']").attr('calendar','YYYY-MM-DD');
    
	$("input[name='outWhsDt3']").attr('class','date date1');
    $("input[name='outWhsDt1']").attr('class','outWhsDt1 date date1');
    
	$("input[name='outWhsDt4']").attr('class','date date2');
    $("input[name='outWhsDt2']").attr('class','outWhsDt2 date date2');
}

function search(index, sortorder) {
//	var rowNum1 = $("#jqGrids").getGridParam('rowNum'); //获取显示配置记录数量
//	rowNum = parseInt(rowNum1)
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var invtyEncd = $("#invtyEncd").val();
	var outWhsId = $(".outWhsId").val();
	var custId = $("#custId").val();
	var outWhsDt1 = $(".outWhsDt1").val();
	var outWhsDt2 = $(".outWhsDt2").val();
	var isNtChk = $("#isNtChk").val();
	var invtyClsEncd = $("#invtyCls").val();
	
	var bizTypId = $("select[name='bizTypId'] option:selected").val();
	var sellTypId = $("select[name='sellTypId'] option:selected").val();
//	var isNtChk = $("select[name='isNtChk'] option:selected").val();
	var isNtBookEntry = $("select[name='isNtBookEntry'] option:selected").val();
	var isNtRtnGood = $("select[name='isNtRtnGood'] option:selected").val();
	var accNum = $("#user").val()
	var deptId = $("input[name='deptId']").val();
	var custOrdrNum = $(".custOrdrNum").val();
	var sellOrdrInd = $(".sellOrdrInd").val();
	var batNum = $("input[name='batNum']").val();
	var intlBat = $("input[name='intlBat']").val();
	var invtyCd = $("input[name='invtyCd']").val();
	var expressNum = $("input[name='expressNum']").val();
	var whsEncd = $("#whsEncd").val();
	var memo = $("#memo").val();
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": invtyEncd,
			"outWhsId": outWhsId,
			"custId": custId,
			"outWhsDt1": outWhsDt1,
			"outWhsDt2": outWhsDt2,
			"invtyClsEncd": invtyClsEncd,
			"isNtChk": isNtChk,
			
			"bizTypId": bizTypId,
			"sellTypId": sellTypId,
			"isNtBookEntry": isNtBookEntry,
			"isNtRtnGood": isNtRtnGood,
			"accNum": accNum,
			"deptId": deptId,
			"sellOrdrInd":sellOrdrInd,
			"custOrdrNum": custOrdrNum,
			"batNum": batNum,
			"intlBat": intlBat,
			"invtyCd": invtyCd,
			"whsEncd":whsEncd,
			"memo":memo,
			"expressNum":expressNum,
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
		url: url + '/mis/purc/SellOutWhs/querySellOutWhsListOrderBy',
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
			if(data.respHead.isSuccess==false){
				alert(data.respHead.message)
			}
			tableSums = data.respBody.tableSums;
			var list = data.respBody.list;
			for(var i = 0; i < list.length; i++) {
				if(list[i].isNtChk == 0) {
					list[i].isNtChk = "否"
				} else if(list[i].isNtChk == 1) {
					list[i].isNtChk = "是"
				}
			}
			for(var i = 0; i < list.length; i++) {
				if(list[i].isNtRtnGood == 1) {
					list[i].isNtRtnGood = "退货"
				} else if(list[i].isNtRtnGood == 0) {
					list[i].isNtRtnGood = "出库"
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

function order(rowid) {
	//获得行数据
	var rowDatas = $("#jqGrids").jqGrid('getRowData', rowid);
	localStorage.setItem("outWhsId", rowDatas.outWhsId);
	localStorage.setItem("isNtChk", rowDatas.isNtChk);
	if(rowDatas.isNtRtnGood == "出库") {
		window.open("../../Components/sell/sellOutWhs.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');
	} else if(rowDatas.isNtRtnGood == "退货") {
		window.open("../../Components/sell/returnSellOutWhs.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');
	}
}

function ntChk(x) {
	//获得选中行的行号
	var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
	//获取选择行的provrId
	//对象数组
	var rowData = [];
	for(var i = 0; i < ids.length; i++) {
		var isNtRtnGood = $("#jqGrids").getCell(ids[i], "isNtRtnGood");
		if(isNtRtnGood == "退货") {
			isNtRtnGood = 1;
		} else if(isNtRtnGood == "出库") {
			isNtRtnGood = 0;
		};
		var obj = {}; //对象
		//选中行的id
		//把选中行的i添加到数据对象中
		obj.outWhsId = $("#jqGrids").getCell(ids[i], "outWhsId").toString();
		obj.isNtChk = x;
		obj.isNtRtnGood = isNtRtnGood;
		//建一个数组，把单个对象添加到数组中
		rowData[i] = obj;
	}
	var result = [];
	var obj = {};
	for(var i = 0; i < rowData.length; i++) {
		if(!obj[rowData[i].outWhsId]) {
			result.push(rowData[i]);
			obj[rowData[i].outWhsId] = true;
		}
	}
	if(result.length == 0) {
		alert("请选择单据!")
	} else {
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				"list": result
			}
		};
		var Data = JSON.stringify(data);
		$.ajax({
			url: url + '/mis/purc/SellOutWhs/updateSellOutWhsIsNtChk',
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
				alert(data.respHead.message)
				if(data.respHead.isSuccess==true){
					search()
				}
			},
			error: function() {
				alert("审核失败")
			}
		})
	}

}

//审核与弃审
$(function() {
	var isclick = true;
	//审核
	$(".toExamine").click(function() {
		if(isclick) {
			isclick = false;
			ntChk(1);
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	});
	//弃审
	$(".noTo").click(function() {
		if(isclick) {
			isclick = false;
			ntChk(0);
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	})
})

//删除
$(function() {
	$(".delOrder").click(function() {
		//获得选中行的行号
		var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
		//获取选择行的provrId
		var rowData = [];
		for(var i = 0; i < ids.length; i++) {
			var gr = $("#jqGrids").getGridParam('selrow');
			//选中行的id
			var data = $("#jqGrids").getCell(ids[i], "outWhsId");
			//建一个数组，把选中行的id添加到这个数组中去。
			rowData[i] = data;
		}
		var result = [];
		var obj = {};
		for(var i = 0; i < rowData.length; i++) {
			if(!obj[rowData[i]]) {
				result.push(rowData[i]);
				obj[rowData[i]] = true;
			}
		}
		if(result.length == 0) {
			alert("请选择单据!")
		} else if(confirm("确定删除？")) {
			var rowDatas = result.toString();
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"outWhsId": rowDatas,
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/purc/SellOutWhs/deleteSellOutWhsList',
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
					alert(data.respHead.message)
					if(data.respHead.isSuccess==true){
						search()
					}
				},
				error: function() {
					alert("删除失败")
				}
			})
		}
	})
})

//自动分配货位
$(function() {
	$(".gdsBitAllotted").click(function() {
		//获得选中行的行号
		var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
		//获取选择行的provrId
		var rowData = [];
		for(var i = 0; i < ids.length; i++) {
			var gr = $("#jqGrids").getGridParam('selrow');
			//选中行的id
			var data = $("#jqGrids").getCell(ids[i], "outWhsId");
			//建一个数组，把选中行的id添加到这个数组中去。
			rowData[i] = data;
		}
		var result = [];
		var obj = {};
		for(var i = 0; i < rowData.length; i++) {
			if(!obj[rowData[i]]) {
				result.push(rowData[i]);
				obj[rowData[i]] = true;
			}
		}
		if(result.length == 0) {
			alert("请选择单据!")
		} else if(confirm("确定自动分配货位？")) {
			var rowDatas = result.toString();
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"formNum": rowDatas,
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/whs/out_into_whs/allInvtyGdsBitList',
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
					alert(data.respHead.message)
//					if(data.respHead.isSuccess==true){
//						search()
//					}
				},
				error: function() {
					alert("自动分配失败")
				}
			})
		}
	})
})

function importExcel(x) {
	var files = $("#FileUpload").val()
	var fileObj = document.getElementById("FileUpload").files[0];
	var formFile = new FormData();
	formFile.append("action", "UploadVMKImagePath");
	formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
	var data = formFile;
	if(files != "") {
		$.ajax({
			type: 'post',
			url: url + x,
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
}

//导入
$(function() {
	$(".dropdown-menu .li2").click(function() {
		var x = "/mis/purc/SellOutWhs/uploadSellOutWhsFile"
		importExcel(x)
	});
	$(".dropdown-menu .li1").click(function() {
		var x = "/mis/purc/SellOutWhs/uploadSellOutWhsFileU8"
		importExcel(x)
	});
});

//导出
var arr = [];
var obj = {}

$(document).on('click', '.exportExcel', function() {
	var invtyEncd = $("#invtyEncd").val();
	var outWhsId = $(".outWhsId").val();
	var custId = $("#custId").val();
	var outWhsDt1 = $(".outWhsDt1").val();
	var outWhsDt2 = $(".outWhsDt2").val();
	var isNtChk = $("#isNtChk").val();
	var invtyClsEncd = $("#invtyCls").val();
	
	var bizTypId = $("select[name='bizTypId'] option:selected").val();
	var sellTypId = $("select[name='sellTypId'] option:selected").val();
//	var isNtChk = $("select[name='isNtChk'] option:selected").val();
	var isNtBookEntry = $("select[name='isNtBookEntry'] option:selected").val();
	var isNtRtnGood = $("select[name='isNtRtnGood'] option:selected").val();
	var accNum = $("#user").val()
	var deptId = $("input[name='deptId']").val();
	var custOrdrNum = $("input[name='custOrdrNum']").val();
	var sellOrdrInd = $("input[name='sellOrdrInd']").val();
	var batNum = $("input[name='batNum']").val();
	var intlBat = $("input[name='intlBat']").val();
	var invtyCd = $("input[name='invtyCd']").val();
	var expressNum = $("input[name='expressNum']").val();
	
	var whsEncd = $("#whsEncd").val();
	var memo = $("#memo").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": invtyEncd,
			"outWhsId": outWhsId,
			"custId": custId,
			"outWhsDt1": outWhsDt1,
			"outWhsDt2": outWhsDt2,
			"invtyClsEncd": invtyClsEncd,
			"isNtChk": isNtChk,
			
			"bizTypId": bizTypId,
			"sellTypId": sellTypId,
			"isNtBookEntry": isNtBookEntry,
			"isNtRtnGood": isNtRtnGood,
			"accNum": accNum,
			"deptId": deptId,
			"custOrdrNum": custOrdrNum,
			"sellOrdrInd": sellOrdrInd,
			"batNum": batNum,
			"intlBat": intlBat,
			"invtyCd": invtyCd,
			"whsEncd":whsEncd,
			"memo":memo,
			"expressNum":expressNum
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/purc/SellOutWhs/printingSellOutWhsList',
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
			var execlName = '销售出库单列表'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})

})
