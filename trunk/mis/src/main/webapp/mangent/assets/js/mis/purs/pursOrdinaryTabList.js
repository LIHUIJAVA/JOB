var count;
var pages;
var page = 1;
var rowNum;
//表格初始化-采购普通发票列表
$(function() {
	allHeight()
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	$("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['采购发票号', '开票日期', '发票类型','供方银行名称',
					'供应商名称','部门名称','对应入库单号','表头备注','是否审核','审核人','审核时间',
					'入库单号','序号','仓库编码', '存货编码', '存货名称', '项目编码', '规格型号', '主计量单位', '编码', '数量', '含税单价', '价税合计',
					'无税单价', '无税金额', '税率', '税额', '箱规', '箱数', '对应条形码', '批次', '表体备注', '是否退货'
			],
		colModel: [{
				name: 'pursInvNum',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'bllgDt',
				editable: true,
				align: 'center'

			},
			{
				name: 'invTyp',
				editable: true,
				align: 'center'

			},
			{
				name: 'provrBankNm',
				editable: true,
				align: 'center',
				hidden:true
			},
			{
				name: 'provrNm',
				editable: false,
				align: 'center'
			},
			{
				name: 'deptName',
				editable: false,
				align: 'center'
			},
			{
				name: 'intoWhsSnglId',
				editable: false,
				align: 'center',
			},
			{
				name: 'memo',
				editable: false,
				align: 'center'
			},
			{
				name: 'isNtChk',
				editable: false,
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
				name: 'crspdIntoWhsSnglNum',
				editable: false,
				align: 'center',
				sortable: false,
			},
			{
				name: 'intoWhsSnglSubtabId',
				editable: false,
				align: 'center',
				sortable: false,
//				hidden:true
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
		autoScroll:true,
		// 列拖拽
		sortable:true,
		shrinkToFit:false,
		rownumbers: true,
		loadonce: false,
		forceFit: true,
		rowNum:500,
		rowList:[500,1000,3000,5000],
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		multiboxonly: true,
		caption: "采购发票列表", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#jqGrids").getGridParam('records'); //获取返回的记录数
			page = $("#jqGrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#jqGrids").getGridParam('rowNum');
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
			var noTaxAmt = 0;
			var taxAmt = 0;
			var prcTaxSum = 0;
			var bxQty = 0;
			var ids = $("#jqGrids").getDataIDs();
			for(var i = 0; i < ids.length; i++) {
				qty += parseFloat($("#jqGrids").getCell(ids[i],"qty"));
				noTaxAmt += parseFloat($("#jqGrids").getCell(ids[i],"noTaxAmt"));
				taxAmt += parseFloat($("#jqGrids").getCell(ids[i],"taxAmt"));
				prcTaxSum += parseFloat($("#jqGrids").getCell(ids[i],"prcTaxSum"));
				bxQty += parseFloat($("#jqGrids").getCell(ids[i],"bxQty"));
			};
			if(isNaN(qty)) {
				qty = 0
			}
			if(isNaN(noTaxAmt)) {
				noTaxAmt = 0
			}
			if(isNaN(taxAmt)) {
				taxAmt = 0
			}
			if(isNaN(prcTaxSum)) {
				prcTaxSum = 0
			}
			if(isNaN(bxQty)) {
				bxQty = 0
			}
			qty = qty.toFixed(2);
			noTaxAmt = precision(noTaxAmt,2)
			taxAmt = precision(taxAmt,2)
			prcTaxSum = precision(prcTaxSum,2)
			bxQty = bxQty.toFixed(2);
			$("#jqGrids").footerData('set', { 
				"pursInvNum": "本页合计",
				qty: qty,
				noTaxAmt:noTaxAmt,
				taxAmt: taxAmt,
				prcTaxSum:prcTaxSum,
				bxQty: bxQty,
				
			}          );    
		},
	})
})
 
//查询按钮
$(document).on('click', '.search', function() {
	search()
})

function search(){
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var pursInvNum = $(".pursInvNum").val();
	var provrId = $("#provrId").val();
	var startBllgDt = $(".startBllgDt").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var endBllgDt = $(".endBllgDt").val();
	var memo = $("input[name='memo']").val();
	var isNtChk = $("#isNtChk").val();
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"startBllgDt": startBllgDt,
			"endBllgDt": endBllgDt,
			"invtyClsEncd": invtyClsEncd,
			"pursInvNum": pursInvNum,
			"provrId": provrId,
			"memo": memo,
			"isNtChk":isNtChk,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/account/PursComnInv/queryPursComnInvList',
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
					list[i].isNtChk="否"
				}else if(list[i].isNtChk==1){
					list[i].isNtChk="是"
				}
			}
			var arr=[];
			//执行深度克隆
			for(var i = 0; i < list.length; i++) {
				if(list[i].pursList != null) {
					var entrs = list[i].pursList
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
		}
	});

}

function order(rowid) {
	//获得行数据
	var rowDatas = $("#jqGrids").jqGrid('getRowData', rowid);
	var invTyp = rowDatas.invTyp
	localStorage.setItem("pursInvNum", rowDatas.pursInvNum);
	if(invTyp == '采购普通发票') {
		//存换从传入的是orderId
		localStorage.setItem("pursInvNum", rowDatas.pursInvNum);
		window.open("../../Components/purs/pursOrdinaryTab.html?1");
	} else if(invTyp == '采购专用发票') {
		localStorage.setItem("pursInvNum", rowDatas.pursInvNum);
		window.open("../../Components/purs/pursInvMasTab.html?1");
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
		obj.pursInvNum = $("#jqGrids").getCell(ids[i], "pursInvNum").toString();
		obj.isNtChk = x;
		//建一个数组，把单个对象添加到数组中
		rowData[i] = obj;
	}
	let hash = {}; 
	rowData = rowData.reduce((preVal, curVal) => {
		hash[curVal.pursInvNum] ? '' : hash[curVal.pursInvNum] = true && preVal.push(curVal); 
		return preVal 
	}, [])
	if(rowData.length == 0) {
		alert("请选择单据!")
	} else {
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				"pursComnInvList": rowData
			}
		};
		var Data = JSON.stringify(data);
		$.ajax({
			url: url + '/mis/account/PursComnInv/updatePursComnInvIsNtChk',
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
			error: function(error) {
				alert(error.responseText)
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
			var data = $("#jqGrids").getCell(ids[i], "pursInvNum");
			//建一个数组，把选中行的id添加到这个数组中去。
			rowData[i] = data;
		}
		if(rowData.length == 0) {
			alert("请选择单据!")
		} else if(confirm("确定删除？")) {
			var pursInvNum = rowData.toString();
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"pursInvNum": pursInvNum
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/account/PursComnInv/deletePursComnInvList',
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
			var data = $("#jqGrids").getCell(ids[i], "pursInvNum");
			//建一个数组，把选中行的id添加到这个数组中去。
			rowData[i] = data;
		}
		if(rowData.length == 0) {
			alert("请选择单据!")
		} else if(confirm("确定推送？")) {
			var pursInvNum = rowData.toString();
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"ids": pursInvNum
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/account/PursComnInv/pushToU8',
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

//$(function() {
//	$("#last_jqGridPager").after('<input id="_input" type="text" value="500"/>')
//})

//导入
$(document).ready(function(){
    $('.dropdown-menu li').click(function(){
        var rs=$(this).text();
        $('#dropdownMenu2').text(rs);
    });
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
	            url: url + "/mis/account/PursComnInv/uploadPursComnInvFileU8",
	            data:data,
	          	dataType: "json",
	           	cache: false,//上传文件无需缓存
	           	processData: false,//用于对data参数进行序列化处理 这里必须false
	           	contentType: false, //必须
	           	success: function(data) {
	           		alert(data.respHead.message)
	           	}
	        });
        } else {
        	alert("请选择文件")
        }
    });
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
	            url: url + "/mis/account/PursComnInv/uploadPursComnInvFile",
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
});

//导出
$(document).on('click', '.exportExcel', function() {
	var pursInvNum = $(".pursInvNum").val();
	var provrId = $("#provrId").val();
	var startBllgDt = $(".startBllgDt").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var endBllgDt = $(".endBllgDt").val();
	var isNtChk = $("#isNtChk").val();

	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"startBllgDt": startBllgDt,
			"endBllgDt": endBllgDt,
			"invtyClsEncd": invtyClsEncd,
			"pursInvNum": pursInvNum,
			"provrId": provrId,
			"isNtChk":isNtChk,
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/account/PursComnInv/printPursComnInvList',
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
			var execlName = '采购发票列表'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
	
})

