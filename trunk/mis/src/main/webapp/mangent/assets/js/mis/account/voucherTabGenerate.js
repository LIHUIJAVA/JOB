//凭证类别下拉选择框
$(function() {
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"pageNo": 1,
			"pageSize": 9999999
		}
	};
	var postData = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/account/vouchCateDoc/selectVouchCateDoc',
		type: 'post',
		data: postData,
		dataType: 'json',
		async: false,
		contentType: 'application/json;charset=utf-8',
		success: function(data) {
			var list = data.respBody.list;

			var valtnMode = $("#voucherId")
			for(var i = 0; i < list.length; i++) {
				addEle(valtnMode, list[i].vouchCateNm, list[i].vouchCateWor);
			}

			function addEle(ele, value, val) {
				var optionStr = "";
				optionStr = '<option value=' + val + '>' + value + "</option>";
				ele.append(optionStr);
			}

			function removeEle(ele) {
				ele.find("option").remove();
				var optionStar = "<option value=" + "请选择" + ">" + "请选择" + "</option>";
				ele.append(optionStar);
			}

		},
		error: function() {
			alert("error");
		}, //错误执行方法
	})
})


var mType = 0;

//表格初始化
$(function() {
	$('#formCode').fSelect();
	jQuery("#jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['单据类型id', '单据类型', '业务类型', '单据号', '科目类型(借)', '科目类型(贷)', '科目编码(借)',
			'科目编码(贷)', '科目名称(借)', '科目名称(贷)', '借方金额', '贷方金额', '借方数量', '贷方数量', '科目方向(借)', '科目方向(贷)',
			'存货编码', '存货名称', '存货代码', '规格型号','客户','客户名称','供应商','供应商名称','部门','部门名称','项目编码','项目名称'
		], //jqGrid的列显示名字
		colModel: [{
				name: "formTypEncd",
				align: "center",
				editable: false,
				sortable: false,
				hidden: true
			},
			{
				name: "formTypName",
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: "bizTypNm",
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: "formNum",
				align: "center",
				sortable: false,
			},
			{
				name: "subDebitType", //科目类型
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: "subCreditType", //科目类型
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: "subDebitId", //科目编码
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: "subCreditId", //
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: "subDebitNm", //科目名称
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: "subCreditNm", //科目名称
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: "subDebitMoney", //借方金额
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: "subCreditMoney",
				align: "center",
				sortable: false,
			},
			{
				name: "subDebitNum", //jie方数量
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: 'subCreditNum',
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: "subDebitPath", //借 科目方向
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: "subCreditPath", //dai 科目方向
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: "invtyEncd",
				align: "center",
				sortable: false,
			},
			{
				name: 'invtyNm',
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: "invtyCd",
				align: "center",
				sortable: false,
			},
			{
				name: 'spcModel',
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: 'custId',
				align: "center",
				editable: true,
				hidden:true,
				sortable: false
			},
			{
				name: 'custNm',
				align: "center",
				editable: true,
				hidden:true,
				sortable: false
			},
			{
				name: 'provrId',
				align: "center",
				editable: true,
				hidden:true,
				sortable: false
			},
			{
				name: 'provrNm',
				align: "center",
				hidden:true,
				editable: true,
				sortable: false
			},
			{
				name: 'deptId',
				align: "center",
				editable: true,
				hidden:true,
				sortable: false
			},
			{
				name: 'deptName',
				align: "center",
				editable: true,
				hidden:true,
				sortable: false
			},
			{
				name: 'projEncd',
				align: "center",
				editable: true,
				hidden:true,
				sortable: false
			},
			{
				name: 'projNm',
				align: "center",
				editable: true,
				hidden:true,
				sortable: false
			},
		],
		autowidth: true,
		height: 400,
		autoScroll: true,
		shrinkToFit: false,
		viewrecords: true,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 99999,
		cellsubmit: "clientArray",
		cellEdit: true,
		//		rowList: [10, 20, 30], //可供用户选择一页显示多少条
//		pager: '#jqgridPager', //表格页脚的占位符(一般是div)的id
		caption: "生成凭证", //表格的标题名字
		footerrow: true,
		gridComplete: function() { 
			var qtys = 0;
			var amts = 0;
			var qty = 0;
			var amt = 0;
			var ids = $("#jqgrids").getDataIDs();
			for(var i = 0; i < ids.length; i++) {
				qtys += parseFloat($("#jqgrids").getCell(ids[i], "subCreditMoney"));
				amts += parseFloat($("#jqgrids").getCell(ids[i], "subDebitMoney"));
				qty += parseFloat($("#jqgrids").getCell(ids[i], "subDebitNum"));
				amt += parseFloat($("#jqgrids").getCell(ids[i], "subCreditNum"));
			};   
			if(isNaN(qtys)) {
				qtys = ""
			}
			if(isNaN(amts)) {
				amts = ""
			} 
			if(isNaN(qty)) {
				qty = ""
			}
			if(isNaN(amt)) {
				amt = ""
			}
			$("#jqgrids").footerData('set', { 
				"formTypName": "合计",
				subCreditMoney: qtys,
				subDebitMoney: amts,
				subDebitNum: qty,
				subCreditNum: amt,
			}          );   
			strC = qtys;
			strD = amts
		},
		//在编辑状态时
		afterEditCell: function(rowid, cellname, val, iRow, iCol) {
			$(".makeOrder").addClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
			if(cellname == "subCreditId") {
				$("#" + rowid + "_subCreditId").bind("dblclick", function() {
					mType = 1;
					grr()
				});
			}
			if(cellname == "subDebitId") {
				$("#" + rowid + "_subDebitId").bind("dblclick", function() {
					mType = 2;
					grr()
				});
			}
		},
		//离开编辑状态
		afterRestoreCell: function(rowid, cellname, val, iRow, iCol) {
			$(".makeOrder").removeClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true)

		},
		//回车保存
		afterSaveCell: function(rowid, cellname, val, iRow, iCol) {
			$(".makeOrder").removeClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true);
			if(cellname == "subDebitId" || cellname == "subCreditId") {
				getName(rowid, cellname, val)
			}
		}
	})
	jQuery("#jqgrids").jqGrid('setGroupHeaders', {
		useColSpanStyle: false,
		groupHeaders: [{
				startColumnName: 'subDebitType',
				numberOfColumns: 2,
				titleText: '<em>科目类型</em>'
			},
			{
				startColumnName: 'subDebitId',
				numberOfColumns: 2,
				titleText: '<em>科目编码</em>'
			},
			{
				startColumnName: 'subDebitNm',
				numberOfColumns: 2,
				titleText: '<em>科目名称</em>'
			}
		]
	});
})

function getName(rowid, cellname, val) {
	var showData = {
		"reqHead": reqhead,
		"reqBody": {
			"subjId": val,
			"pageNo": 1,
			"pageSize": 1
		}
	};
	var postD2 = JSON.stringify(showData);
	$.ajax({
		type: "post",
		url: url + "/mis/account/acctItmDoc/queryAcctItmDocList",
		async: true,
		data: postD2,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		success: function(data) {
			var list = data.respBody.list[0];
			if(data.respBody.count == 0) {
				alert("无此科目");
				if(cellname == "subDebitId") {
					$("#jqgrids").setRowData(rowid, {
						subDebitId: "",
						subDebitNm: ""
					})
				} else if(cellname == "subCreditId") {
					$("#jqgrids").setRowData(rowid, {
						subCreditId: "",
						subCreditNm: ""
					})
				}
			} else {
				if(cellname == "subDebitId") {
					$("#jqgrids").setRowData(rowid, {
						subDebitNm: list.subjNm
					})
				} else if(cellname == "subCreditId") {
					$("#jqgrids").setRowData(rowid, {
						subCreditNm: list.subjNm
					})
				}
			}
		},
		error: function() {
			alert("error");
		}
	})

}

//选择未生成凭证的单子
$(function() {
	$(".selectOrder").click(function() {
		$("#mengban").hide();
		$("#purchaseOrder").hide();
		$("#order").css("opacity", 1);
		$("#order").show();
	})
	$("#yes").click(function() {
		$("#jqgrids").setColProp("subCreditType", {
			editable: true
		});
		$("#jqgrids").setColProp("subDebitType", {
			editable: true
		})
		$("#jqgrids").setColProp("subCreditId", {
			editable: true
		});
		$("#jqgrids").setColProp("subDebitId", {
			editable: true
		});
		$("#purchaseOrder").show();
		$("#order").css("opacity", 0);
		$("#order").hide();
		var ids = $("#order_jqGrids").jqGrid('getGridParam', 'selarrrow'); //获取行id
		var rowData = [];
		var formTypEncd = [];
		for(i = 0; i < ids.length; i++) {
			//选中行的id
			rowData[i] = $("#order_jqGrids").getCell(ids[i], "formNum");
			formTypEncd[i] = $("#order_jqGrids").getCell(ids[i], "formTypEncd");
		}
		getFormNum(rowData, formTypEncd);

	})
	$("#no").click(function() {
		$("#purchaseOrder").show();
		$("#order").css("opacity", 0);
		$("#order").hide();
	})

})
var moneyC;
var moneyD;
var strC = 0;
var strD = 0;

function getFormNum(rowData, formTypEncd) {
	var rowDatas = rowData.toString();
	var formTypEncds = formTypEncd.toString();
	var whsEncd = $("#whsEncd").val();
	var invtyEncds = $("#invtyEncd").val();
	var bookOkSDt = $("#bookOkSDt").val();
	var bookOkEDt = $("#bookOkEDt").val();
	var formSDt = $("#formSDt").val();
	var formEDt = $("#formEDt").val();
	var custId = $("#custId").val();
	var provrId = $("#provrId").val();
	var recvSendCateId = $("#recvSendCateId").val()
	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"isSelect": "",
			"formNum": rowDatas,
			"whsEncd": whsEncd,
			"invtyEncds": invtyEncds,
			"formType": formTypEncds,
			"bookOkSDt": bookOkSDt,
			"bookOkEDt": bookOkEDt,
			"formSDt": formSDt,
			"formEDt": formEDt,
			"custId":custId,
			"provrId":provrId,
			"recvSendCateId":recvSendCateId,
			"pageSize": 999999,
			"pageNo": 1
		}
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		url: url + "/mis/account/form/novoucher/list",
		async: true,
		data: postD2,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
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

			var b = [];
			var c = [];
			for(var i in arr) {
				b.push(arr[i].subjectCredit);
				$.extend(arr[i], b[i]);
			}
			for(var i in arr) {
				c.push(arr[i].subjectDebit);
				$.extend(arr[i], c[i]);
			}
			for(var i = 0; i < arr.length; i++) {
				delete arr[i].bookEntrySub;
			}
			myData = arr;
			$("#jqgrids").jqGrid('clearGridData');
			$("#jqgrids").jqGrid('setGridParam', {
				datatype: 'local',
				data: myData, //newData是符合格式要求的重新加载的数据
				page: 1 //哪一页的值
			}).trigger("reloadGrid")
		},
		error: function() {
			alert("条件查询失败")
		}
	});

}

function grr() {
	$(".account_List").css("opacity", 1);
	$(".account_List").show();
	$("#purchaseOrder").hide();
}

$(function() {
	$(".sure").click(function() {
		$("#purchaseOrder").show();
		$(".account_List").hide();
		var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow');

		var ids = $("#jqGrids").jqGrid('getGridParam', 'selrow');
		var rowDatas = $("#jqGrids").jqGrid('getRowData', ids);
		var records = $("#jqgrids").jqGrid('getGridParam', 'records');
		if(mType == 1) {
			$("#" + gr + "_subCreditId").val(rowDatas.subjId);
			$("#jqgrids").setRowData(gr, {
				subCreditNm: rowDatas.subjNm,
			})
		} else if(mType == 2) {
			$("#" + gr + "_subDebitId").val(rowDatas.subjId);
			$("#jqgrids").setRowData(gr, {
				subDebitNm: rowDatas.subjNm
			})
		}

		//		if(mType == 1) {
		//			$("#" + gr + "_subCreditId").val(rowDatas.subjId);
		//			for(var i = 0;i<records;i++) {
		//				var id = i+1;
		//				if(gr==id){
		//					$("#jqgrids").setRowData(i+1, {
		//						subCreditNm: rowDatas.subjNm,
		//					})
		//				}else{
		//					$("#jqgrids").setRowData(i+1, {
		//						subCreditNm: rowDatas.subjNm,
		//						subCreditId:rowDatas.subjId
		//					})
		//				}
		//			}
		//		} else if(mType == 2) {
		//			$("#" + gr + "_subDebitId").val(rowDatas.subjId);
		//			for(var i = 0;i<records;i++) {
		//				var id = i+1;
		//				if(gr==id){
		//					$("#jqgrids").setRowData(i+1, {
		//						subDebitNm: rowDatas.subjNm
		//					})
		//				}else{
		//					$("#jqgrids").setRowData(i+1, {
		//						subDebitId:rowDatas.subjId,
		//						subDebitNm: rowDatas.subjNm
		//					})
		//				}
		//			}
		//			
		//		}
	})
	$(".cancel").click(function() {
		$("#purchaseOrder").show();
		$(".account_List").hide();
	})
})

$(function() {
	$(".makeOrder").click(function() {
		var vouchCateWor = $("select").val();
		var comnVouchComnt = $("#comnVouchComnt").val();
		if(strC == 0 && strD == 0) {
			alert("不符合制单标准")
		} else if(vouchCateWor == 0) {
			alert("请选择凭证类别")
		} else if(strC != 0 && strC == strD) {
			//拿到grid对象
			var obj = $("#jqgrids");
			//获取grid表中所有的rowid值
			var rowIds = obj.getDataIDs();
			//初始化一个数组arrayData容器，用来存放rowData
			var arrayData = [];
			if(rowIds.length > 0) {
				for(var i = 0; i < rowIds.length; i++) {
					//rowData=obj.getRowData(rowid);//这里rowid=rowIds[i];
					arrayData.push(obj.getRowData(rowIds[i]));
				}
			}
			var deleteAjax = {
				"reqHead": reqhead,
				"reqBody": {
					"vouchCateWor": vouchCateWor,
					"comnVouchComnt":comnVouchComnt,
					"list": arrayData
				}
			}
			var deleteData = JSON.stringify(deleteAjax)
			$.ajax({
				type: "post",
				url: url + "/mis/account/vouchTab/voucher/generate",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(data) {
					alert(data.respHead.message)
				},
				error: function() {
					alert("制单失败")
				}
			});
		} else {
			alert("请检查是否制单有误")
		}
	})
})
