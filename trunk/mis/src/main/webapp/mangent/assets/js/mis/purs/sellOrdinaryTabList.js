var count;
var pages;
var page = 1;
var rowNum;

//表格初始化-销售发票列表
$(function() {
	allHeight();
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	$("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['销售发票号', '开票日期', '发票类型','业务类型','销售类型','业务员', '部门','客户', '是否审核','审核人','审核时间','表头备注','序号', '销售单号(参照主表)', '销售单号(参照子表)','委托代销结算单号(参照主表)','委托代销结算单号(参照子表)', '退货单号(参照主表)', '退货单号(参照子表)', '仓库名称',
			'仓库编码', '存货编码', '存货名称', '项目编码', '规格型号', '主计量单位', '主计量单位编码', '数量', '含税单价', '价税合计',
			'无税单价', '无税金额', '税率', '税额', '箱规', '箱数', '批次', '国际批次', '表体备注', '是否退货' 
		],
		colModel: [
			{
				name: 'sellInvNum',
				editable: true,
				align: 'center'

			},
			{
				name: 'bllgDt',
				editable: true,
				align: 'center'

			},
			{
				name: 'invTyp',
				editable: false,
				align: 'center',
			},
			{
				name: 'bizTypNm',
				editable: false,
				align: 'center'
			},
			{
				name: 'sellTypNm',
				editable: true,
				align: 'center'
			},
			{
				name: 'userName',
				editable: true,
				align: 'center'
			},
			{
				name: 'deptName',
				editable: true,
				align: 'center'
			},
			{
				name: 'custNm',
				editable: true,
				align: 'center'
			},
			{
				name: 'isNtChk',
				editable: true,
				align: 'center'
			},
			{
				name: 'chkr',
				editable: false,
				align: 'center'
			},
			{
				name: 'chkTm',
				editable: false,
				align: 'center'
			},
			{
				name: 'memo',
				editable: true,
				align: 'center'
			},{
				name: 'ordrNum',
				editable: true,
				sortable: false,
//				hidden: true,
			},
			{
				name: 'sellSnglSubId',
				editable: true,
				sortable: false,
//				hidden: true,
			},
			{
				name: 'sellSnglNums',
				editable: true,
				sortable: false,
//				hidden: true,
			},
			{
				name: 'vouchSubNums',
				editable: true,
				sortable: false,
//				hidden: true,
			},
			{
				name: 'vouchNums',
				editable: true,
				sortable: false,
//				hidden: true,
			},
			{
				name: 'rtnGoodsSubId',
				editable: true,
				sortable: false,
//				hidden: true,
			},
			{
				name: 'rtnGoodsIds',
				editable: true,
				sortable: false,
//				hidden: true,
			},
			{
				name: 'whsNm',
				editable: false,
				sortable: false,
			},
			{
				name: 'whsEncd',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'invtyEncd',
				editable: true,
				align: 'center',
				sortable: false

			},
			{
				name: 'invtyNm',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'projEncd',
				editable: true,
				align: 'center',
				sortable: false

			},
//			{
//				name: 'projNm',
//				editable: false,
//				align: 'center',
//				sortable: false
//			},
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
				name: 'qty',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'cntnTaxUprc',
				editable: true,
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
				name: 'batNum',
				editable: true,
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
				name: 'memo',
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
		],
		autowidth: true,
		height:height,
		// 列拖拽
		sortable:true,
		autoScroll:true,
		shrinkToFit:false,
		rownumbers: true,
		cellEdit: false,
		loadonce: false,
		forceFit: true,
		rowNum: 500, // 每页多少行，用于分页
		rowList: [500, 1000, 3000,5000], //可供用户选择一页显示多少条
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
//		multikey:"altKey",
//		multiboxonly: true,
		caption: "销售发票列表", //表格的标题名字
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
			search();
		},
		//双击弹出采购订单
		ondblClickRow: function(rowid) {
			order(rowid);
		},
		footerrow: true,
		gridComplete: function() { 
			var qty = 0;
			var prcTaxSum = 0;
			var noTaxAmt = 0;
			var taxAmt = 0;
			var bxQty = 0;
			var ids = $("#jqGrids").getDataIDs();
			for(var i = 0; i < ids.length; i++) {
				qty += parseFloat($("#jqGrids").getCell(ids[i],"qty"));
				prcTaxSum += parseFloat($("#jqGrids").getCell(ids[i],"prcTaxSum"));
				noTaxAmt += parseFloat($("#jqGrids").getCell(ids[i],"noTaxAmt"));
				taxAmt += parseFloat($("#jqGrids").getCell(ids[i],"taxAmt"));
				bxQty += parseFloat($("#jqGrids").getCell(ids[i],"bxQty"));
			};
			if(isNaN(qty)) {
				qty = 0
			}
			if(isNaN(prcTaxSum)) {
				prcTaxSum = 0
			}
			if(isNaN(noTaxAmt)) {
				noTaxAmt = 0
			}
			if(isNaN(taxAmt)) {
				taxAmt = 0
			}
			if(isNaN(bxQty)) {
				bxQty = 0
			}
			qty = qty.toFixed(prec);
			prcTaxSum = precision(prcTaxSum,2)
			noTaxAmt = precision(noTaxAmt,2)
			taxAmt = precision(taxAmt,2)
			bxQty = bxQty.toFixed(prec);
			$("#jqGrids").footerData('set', { 
				"sellInvNum": "本页合计",
				qty: qty,
				prcTaxSum:prcTaxSum,
				noTaxAmt:noTaxAmt,
				taxAmt: taxAmt,
				bxQty: bxQty,
				
			}          );    
		},
	})

})

//查询按钮
$(document).on('click', '.search', function() {
	search()
})
function search () {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var sellInvNum = $("input[name='sellInvNum']").val();
//	var custNm = $("input[name='custNm']").val();
	var custId = $("input[name='custId']").val();
//	var custId = localStorage.custId;
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var startBllgDt = $("input[name='startBllgDt']").val();
	var endBllgDt = $("input[name='endBllgDt']").val();
	var isNtChk = $("select[name='isNtChk']").val();
	var memo = $("input[name='memo']").val();
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"sellInvNum": sellInvNum,
			"invtyClsEncd": invtyClsEncd,
//			"custNm": custNm,
			"custId": custId,
			"memo": memo,
//			"bizTypNm": bizTypNm,
			"startBllgDt": startBllgDt,
			"endBllgDt": endBllgDt,
			"isNtChk":isNtChk,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/account/SellComnInv/selectSellComnInvList',
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
			for(var i = 0;i<list.length;i++){
				if(list[i].isNtChk==0){
					list[i].isNtChk="否";
				}else if(list[i].isNtChk==1){
					list[i].isNtChk="是";
				}
			}
			var arr=[];
			//执行深度克隆
			for(var i = 0; i < list.length; i++) {
				if(list[i].sellComnInvSubList != null) {
					var entrs = list[i].sellComnInvSubList
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
			console.log(arr)
			var mydata = {};
			mydata.rows = arr;
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
			alert("error")
		}
	});

}

//function order() {
//	//获得行号
//	var gr = $("#jqGrids").jqGrid('getGridParam', 'selrow');
//	//获得行数据
//	var rowDatas = $("#jqGrids").jqGrid('getRowData', gr);
//	localStorage.setItem("sellInvNum", rowDatas.sellInvNum);
//	localStorage.setItem("isNtChk", rowDatas.isNtChk);
//	window.open("../../Components/purs/sellOrdinaryTab.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');
//
//}
function order(rowid) {
	var rowData = $("#jqGrids").jqGrid('getRowData', rowid);
	var invTyp = rowData.invTyp;
	if(invTyp == '销售普通发票') {
//		alert("销售普通")
		//存换从传入的是orderId
		localStorage.setItem("sellInvNum", rowData.sellInvNum);
		window.open("../../Components/purs/sellOrdinaryTab.html?1");
	} else if(invTyp == '销售专用发票') {
//		alert("销售出库单")
		localStorage.setItem("sellInvNum", rowData.sellInvNum);
		window.open("../../Components/purs/sellInvMasTab.html?1");
	} else {
		alert("发票类型有误")
	}

}
function ntChk(x) {
	//获得选中行的行号
	var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
	//对象数组
	var rowData = [];
	for(var i = 0; i < ids.length; i++) {
		var obj = {}; //对象
		//选中行的id
		//把选中行的i添加到数据对象中
		obj.sellInvNum = $("#jqGrids").getCell(ids[i], "sellInvNum").toString();
		obj.isNtChk = x;
		//建一个数组，把单个对象添加到数组中
		rowData[i] = obj;
	}
	// 数组对象去重
	let hash = {}; 
	rowData = rowData.reduce((preVal, curVal) => {
		hash[curVal.sellInvNum] ? '' : hash[curVal.sellInvNum] = true && preVal.push(curVal); 
		return preVal 
	}, [])

	if(rowData.length == 0) {
		alert("请选择单据!")
	} else {
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				"sellComnInvList": rowData
			}
		};
		var Data = JSON.stringify(data);
		$.ajax({
			url: url + '/mis/account/SellComnInv/updateSellComnInvIsNtChk',
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
				alert("error")
			}
		})
	}

}

//审核与弃审
$(function() {
	//审核
	$(".toExamine").click(function() {
		ntChk(1)
	})
	//弃审
	$(".noTo").click(function() {
		ntChk(0)
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
			var data = $("#jqGrids").getCell(ids[i], "sellInvNum");
			//建一个数组，把选中行的id添加到这个数组中去。
			rowData[i] = data;
		}
		if(rowData.length == 0) {
			alert("请选择单据!")
		} else if(confirm("确定删除？")) {
			var sellInvNum = rowData.toString();
			var data = {
				"reqHead": {
					accNum:'1'
				},
				"reqBody": {
					"sellInvNum": sellInvNum
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/account/SellComnInv/deleteSellComnInvList',
				type: 'post',
				data: Data,
				dataType: 'json',
				async: true,
				contentType: 'application/json;charset=utf-8',
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
	$(".movement").click(function() {
		//获得选中行的行号
		var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
		//获取选择行的provrId
		var rowData = [];
		for(var i = 0; i < ids.length; i++) {
			var gr = $("#jqGrids").getGridParam('selrow');
			//选中行的id
			var data = $("#jqGrids").getCell(ids[i], "sellInvNum");
			//建一个数组，把选中行的id添加到这个数组中去。
			rowData[i] = data;
		}
		if(rowData.length == 0) {
			alert("请选择单据!")
		} else if(confirm("确定推送？")) {
			var sellInvNum = rowData.toString();
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"ids": sellInvNum
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/account/SellComnInv/pushToU8',
				type: 'post',
				data: Data,
				dataType: 'json',
				async: true,
				contentType: 'application/json;charset=utf-8',
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


//导入
$(document).ready(function(){
    $('.dropdown-menu li').click(function(){
        var rs=$(this).text();
        $('#dropdownMenu2').text(rs);
    });
    $('.dropdown-menu .li1').click(function(){
//  	alert("U8导入")
    	var files = $("#FileUpload").val()
    	var fileObj = document.getElementById("FileUpload").files[0];
        var formFile = new FormData();
       	formFile.append("action", "UploadVMKImagePath");  
       	formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
        var data = formFile;
        if(files != "") {
        	$.ajax({
	            type: 'post',
	            url: url + "/mis/account/SellComnInv/uploadSellComnInvFileU8",
	            data:data,
	          	dataType: "json",
	           	cache: false,//上传文件无需缓存
	           	processData: false,//用于对data参数进行序列化处理 这里必须false
	           	contentType: false, //必须
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
    $(".dropdown-menu .li2").click(function () {
    	var files = $("#FileUpload").val()
    	var fileObj = document.getElementById("FileUpload").files[0];
        var formFile = new FormData();
       	formFile.append("action", "UploadVMKImagePath");  
       	formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
        var data = formFile;
        if(files != "") {
        	$.ajax({
	            type: 'post',
	            url: url + "/mis/account/SellComnInv/uploadSellComnInvFile",
	            data:data,
	          	dataType: "json",
	           	cache: false,//上传文件无需缓存
	           	processData: false,//用于对data参数进行序列化处理 这里必须false
	           	contentType: false, //必须
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
	           	}
	        });
        } else {
        	alert("请选择文件")
        }   
    });
});

//导出
$(document).on('click', '.exportExcel', function() {
	var sellInvNum = $("input[name='sellInvNum']").val();
	var custId = $("input[name='custId']").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var startBllgDt = $("input[name='startBllgDt']").val();
	var endBllgDt = $("input[name='endBllgDt']").val();
	var isNtChk = $("select[name='isNtChk']").val();
	
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"sellInvNum": sellInvNum,
			"invtyClsEncd": invtyClsEncd,
			"custId": custId,
			"startBllgDt": startBllgDt,
			"endBllgDt": endBllgDt,
			"isNtChk":isNtChk,
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/account/SellComnInv/printSellComnInvList',
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
			var list = data.respBody.list;
			var execlName = '销售发票列表'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
	
})
