var count;
var pages;
var page = 1;
var rowNum;
var tableSums = {};
$(function() {
	//加载动画html
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$("#mengban").addClass("zhezhao");
})

$(function() {
	// 点击更多给弹筐内重复的赋值
	$(".more").click(function() {
		$("#big_wrap").show();
		$("#box").show();
		var invtyEncd = $("input[name='invtyEncd']").val();
		$("input[name='invtyEncd1']").val(invtyEncd);
		var invtyNm = $("input[name='invtyNm']").val();
		$("input[name='invtyNm1']").val(invtyNm);

		var toGdsSnglId = $("input[name='toGdsSnglId']").val();
		$("input[name='toGdsSnglId1']").val(toGdsSnglId);

		var provrId = $("input[name='provrId']").val();
		$("input[name='provrId1']").val(provrId);
		var provrNm = $("input[name='provrNm']").val();
		$("input[name='provrNm1']").val(provrNm);

		var invtyClsEncd = $("input[name='invtyClsEncd']").val();
		$("input[name='invtyClsEncd1']").val(invtyClsEncd);

		var isNtChk = $("select[name='isNtChk']").val();
		$("select[name='isNtChk1']").val(isNtChk);

		var toGdsSnglDt1 = $("input[name='toGdsSnglDt1']").val();
		$("input[name='toGdsSnglDt3']").val(toGdsSnglDt1);

		var toGdsSnglDt2 = $("input[name='toGdsSnglDt2']").val();
		$("input[name='toGdsSnglDt4']").val(toGdsSnglDt2);

		$("input[name='invtyEncd1']").attr('id', 'invtyEncd'); // 给弹框中重复的加id
		$("input[name='invtyEncd']").attr('id', ''); // 给弹框外重复的去除id
		$("input[name='invtyNm1']").attr('id', 'invtyNm'); // 给弹框中重复的加id
		$("input[name='invtyNm']").attr('id', ''); // 给弹框外重复的去除id

		$("input[name='provrId1']").attr('id', 'provrId'); // 给弹框中重复的加id
		$("input[name='provrId']").attr('id', ''); // 给弹框外重复的去除id
		$("input[name='provrNm1']").attr('id', 'provrNm'); // 给弹框中重复的加id
		$("input[name='provrNm']").attr('id', ''); // 给弹框外重复的去除id

		$("select[name='isNtChk1']").attr('id', 'isNtChk'); // 给弹框中重复的加id
		$("select[name='isNtChk']").attr('id', ''); // 给弹框外重复的去除id

		$("input[name='invtyClsEncd1']").attr('id', 'invtyCls'); // 给弹框中重复的加id
		$("input[name='invtyClsEncd']").attr('id', ''); // 给弹框外重复的去除id
	})
	$(".cancel_more").click(function() {
		$("#big_wrap").hide();
		$("#box").hide();
		$("input[name='invtyEncd1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='invtyEncd']").attr('id', 'invtyEncd'); // 给弹框外重复的去除id
		$("input[name='invtyNm1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='invtyNm']").attr('id', 'invtyNm'); // 给弹框外重复的去除id

		$("input[name='provrId1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='provrId']").attr('id', 'provrId'); // 给弹框外重复的去除id
		$("input[name='provrNm1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='provrNm']").attr('id', 'provrNm'); // 给弹框外重复的去除id

		$("input[name='invtyClsEncd1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='invtyClsEncd']").attr('id', 'invtyCls'); // 给弹框外重复的去除id

		$("select[name='isNtChk']").attr('id', 'isNtChk'); // 给弹框中重复的加id
		$("select[name='isNtChk1']").attr('id', '');
		$("#box input").val("")
		$("#box select").val("")
		$("#formSave select").val("")
	})
	$(".sure_more").click(function() {
		$("#big_wrap").hide();
		$("#box").hide();

		var invtyEncd = $("input[name='invtyEncd1']").val();
		$("input[name='invtyEncd']").val(invtyEncd);
		var invtyNm = $("input[name='invtyNm1']").val();
		$("input[name='invtyNm']").val(invtyNm);

		var toGdsSnglId = $("input[name='toGdsSnglId1']").val();
		$("input[name='toGdsSnglId']").val(toGdsSnglId);

		var provrId = $("input[name='provrId1']").val();
		$("input[name='provrId']").val(provrId);
		var provrNm = $("input[name='provrNm1']").val();
		$("input[name='provrNm']").val(provrNm);

		var invtyClsEncd = $("input[name='invtyClsEncd1']").val();
		$("input[name='invtyClsEncd']").val(invtyClsEncd);

		var isNtChk = $("select[name='isNtChk1']").val();
		$("select[name='isNtChk']").val(isNtChk);

		var intoWhsSnglDt1 = $("input[name='toGdsSnglDt3']").val();
		$("input[name='toGdsSnglDt1']").val(intoWhsSnglDt1);

		var intoWhsSnglDt2 = $("input[name='toGdsSnglDt4']").val();
		$("input[name='toGdsSnglDt2']").val(intoWhsSnglDt2);

		$("input[name='invtyEncd1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='invtyEncd']").attr('id', 'invtyEncd'); // 给弹框外重复的去除id
		$("input[name='invtyNm1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='invtyNm']").attr('id', 'invtyNm'); // 给弹框外重复的去除id

		$("input[name='provrId1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='provrId']").attr('id', 'provrId'); // 给弹框外重复的去除id
		$("input[name='provrNm1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='provrNm']").attr('id', 'provrNm'); // 给弹框外重复的去除id

		$("input[name='invtyClsEncd1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='invtyClsEncd']").attr('id', 'invtyCls'); // 给弹框外重复的去除id

		$("select[name='isNtChk']").attr('id', 'isNtChk'); // 给弹框中重复的加id
		$("select[name='isNtChk1']").attr('id', '');
		search();
	})
})

//表格初始化
$(function() {
	localStorage.removeItem("index")
	localStorage.removeItem("sortorder")
	allHeight();
	//	$(".frozen-bdiv .ui-jqgrid-bdiv").height()-16px
	$("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['到货编码', '到货日期', '采购类型', '业务员', '供应商', '部门', '供应商订单号', '表头备注','表体备注', '订单号', '是否拒收', '是否审核',
			'仓库编码', '仓库名称', '存货编码', '存货名称', '规格型号', '主计量单位', '编码', '数量', '含税单价', '价税合计',
			'无税单价', '无税金额', '税率', '税额', '箱规', '箱数', '对应条形码', '批次', '生产日期', '失效日期', '保质期', '国际批次', '是否打开'
		],
		colModel: [{
				name: 'toGdsSnglId',
				editable: false,
				align: 'center',
				frozen: true,
				sortable: true
			},
			{
				name: 'toGdsSnglDt',
				editable: false,
				align: 'center',
				frozen: true,
				sortable: true
			},
			{
				name: 'pursTypNm',
				editable: false,
				align: 'center',
				sortable: false

			},
			{
				name: 'userName',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'provrNm',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'deptName',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'provrOrdrNum',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'memo',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'memos',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'pursOrdrId',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'isNtRtnGood',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'isNtChk',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'whsEncd',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'whsNm',
				editable: false,
				align: 'center',
				sortable: false,
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
				sortable: true
			},
			{
				name: 'cntnTaxUprc',
				editable: false,
				align: 'center',
				sortable: true
			},
			{
				name: 'prcTaxSum',
				editable: true,
				align: 'center',
				sortable: true
			},
			{
				name: 'noTaxUprc',
				editable: false,
				align: 'center',
				sortable: true
			},
			{
				name: 'noTaxAmt',
				editable: false,
				align: 'center',
				sortable: true
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
				sortable: true
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
				sortable: true
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
				name: 'prdcDt', //生产日期
				editable: true,
				align: 'center',
				sortable: true
			},
			{
				name: 'invldtnDt', //失效日期
				editable: false,
				align: 'center',
				sortable: true
			},
			{
				name: 'baoZhiQi',
				editable: false,
				align: 'center',
				sortable: true
			},
			{
				name: 'intlBat', //国际批次
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'dealStat',
				editable: false,
				align: 'center',
				sortable: true
			},
		],
		//		autowidth: true,
		height: height,
		autowidth: true,
		autoScroll: true,
		shrinkToFit: false,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 500,
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		sortable: true,
		multiselect: true, //复选框
		//		multiboxonly: true,
		caption: "到货单列表", //表格的标题名字
		footerrow: true,
		gridComplete: function() {
			$("#jqGrids").footerData('set', { 
				"toGdsSnglId": "总计",
				"qty": tableSums.qty,
				"bxQty": tableSums.bxQty,
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
			search(index, sortorder)  
		},
		//双击弹出采购订单
		ondblClickRow: function(rowid) {
			order(rowid);
		},
		onSortCol: function(index, colindex, sortorder) {
			switch(index) {
				case "toGdsSnglId":
					index = "tgs.to_gds_sngl_id"
					break;
				case "toGdsSnglDt":
					index = "tgs.to_gds_sngl_dt"
					break;
				case "qty":
					index = "tgss.qty"
					break;
				case "cntnTaxUprc":
					index = "tgss.cntn_tax_uprc"
					break;
				case "prcTaxSum":
					index = "tgss.prc_tax_sum"
					break;
				case "noTaxUprc":
					index = "tgss.no_tax_uprc"
					break;
				case "noTaxAmt":
					index = "tgss.no_tax_amt"
					break;
				case "taxAmt":
					index = "tgss.tax_amt"
					break;
				case "bxQty":
					index = "tgss.bx_qty"
					break;
				case "prdcDt":
					index = "tgss.prdc_dt"
					break;
				case "invldtnDt":
					index = "tgss.invldtn_dt"
					break;
				case "baoZhiQiDt":
					index = "tgss.bao_zhi_qi"
					break;
			}
			localStorage.setItem("index", index)
			localStorage.setItem("sortorder", sortorder)
			window.index = index;
			window.sortorder = sortorder;
			search(index, sortorder)      
		}
	});
})

//查询按钮
$(document).on('click', '.search', function() {
	var index = ''
	var sortorder = 'desc'
	search(index, sortorder)
})

function search(index, sortorder) {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var invtyEncd = $("#invtyEncd").val();
	var toGdsSnglId = $(".toGdsSnglId").val();
	var provrId = $("#provrId").val();
	var toGdsSnglDt1 = $(".toGdsSnglDt1").val();
	var toGdsSnglDt2 = $(".toGdsSnglDt2").val();
	var isNtChk = $("#isNtChk").val();
	var isNtRtnGood = $("#isNtRtnGood").val();
	var deptId = $("#deptId").val();
	var accNum = $("#user").val();
	var provrOrderNum = $(".provrOrdrNum").val();
	var pursOrdrId = $(".pursOrdrId").val();
	var batNum = $("#batNum").val();
	var intlBat = $(".intlBat").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var whsEncd = $("#whsEncd").val();
	var invtyCd = $(".invtyCd").val();
	var memo = $(".memo").val();
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": invtyEncd,
			"invtyClsEncd": invtyClsEncd,
			"toGdsSnglId": toGdsSnglId,
			"provrId": provrId,
			"toGdsSnglDt1": toGdsSnglDt1,
			"toGdsSnglDt2": toGdsSnglDt2,
			"isNtChk": isNtChk,
			"isNtRtnGood": isNtRtnGood,
			"deptId": deptId,
			"accNum": accNum,
			"provrOrdrNum": provrOrderNum,
			"pursOrdrId": pursOrdrId,
			"batNum": batNum,
			"intlBat": intlBat,
			"invtyCd": invtyCd,
			"whsEncd": whsEncd,
			"memo":memo,
			"sort": index,
			"sortOrder": sortorder,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/purc/ToGdsSngl/queryToGdsSnglListOrderBy',
		async: true,
		data: saveData,
		dataType: 'json',
		//开始加载动画
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
					list[i].isNtRtnGood = "拒收"
				} else if(list[i].isNtRtnGood == 0) {
					list[i].isNtRtnGood = "到货"
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
	localStorage.setItem("toGdsSnglId", rowDatas.toGdsSnglId);
	localStorage.setItem("isNtChk", rowDatas.isNtChk);
	if(rowDatas.isNtRtnGood == "拒收") {
		window.open("../../Components/purs/returnToGdsSngl.html?1", 'height=700, width=1000, top=200, left=300,location=no, status=no');
	} else if(rowDatas.isNtRtnGood == "到货") {
		window.open("../../Components/purs/toGdsSngl.html?1", 'height=700, width=1000, top=200, left=300,location=no, status=no');
	}
}

function ntChk(x) {
	//获得选中行的行号
	var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
	//获取选择行的provrId
	//对象数组
	var rowData = [];
	for(var i = 0; i < ids.length; i++) {
		var isNtRtnGood = $("#jqGrids").getCell(ids[i], "isNtRtnGood")
		if(isNtRtnGood == "拒收") {
			isNtRtnGood = 1;
		} else if(isNtRtnGood == "到货") {
			isNtRtnGood = 0;
		}
	}
	for(var i = 0; i < ids.length; i++) {
		var obj = {}; //对象
		//选中行的id
		//把选中行的i添加到数据对象中
		obj.toGdsSnglId = $("#jqGrids").getCell(ids[i], "toGdsSnglId");
		obj.isNtChk = x;
		obj.isNtRtnGood = isNtRtnGood;
		rowData[i] = obj;
	}
	var result = [];
	var obj = {};
	for(var i = 0; i < rowData.length; i++) {
		if(!obj[rowData[i].toGdsSnglId]) {
			result.push(rowData[i]);
			obj[rowData[i].toGdsSnglId] = true;
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
			url: url + '/mis/purc/ToGdsSngl/updateToGdsSnglIsNtChk',
			type: 'post',
			data: Data,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			//开始加载动画
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
				if(data.respHead.isSuccess == true) {
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
			var data = $("#jqGrids").getCell(ids[i], "toGdsSnglId");
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
			var toGdsSnglId = result.toString();
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"toGdsSnglId": toGdsSnglId,
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/purc/ToGdsSngl/deleteToGdsSnglList',
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
					if(data.respHead.isSuccess == true) {
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

$(function() {
	$(".Close").click(function() {
		var x = '/mis/purc/ToGdsSngl/updateToGdsSnglDealStatOK';
		var comfirm = "关闭"
		closeOrOpen(x, comfirm)
	})
	$(".Open").click(function() {
		var x = '/mis/purc/ToGdsSngl/updateToGdsSnglDealStatNO';
		var comfirm = "打开"
		closeOrOpen(x, comfirm)
	})
})

function closeOrOpen(x, comfirm) {
	//获得选中行的行号
	var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
	//获取选择行的provrId
	var rowData = [];
	for(var i = 0; i < ids.length; i++) {
		var gr = $("#jqGrids").getGridParam('selrow');
		//选中行的id
		var data = $("#jqGrids").getCell(ids[i], "toGdsSnglId");
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
	} else if(confirm("确定" + comfirm + "此单据？")) {
		var toGdsSnglId = result.toString();
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				"toGdsSnglId": toGdsSnglId,
			}
		};
		var Data = JSON.stringify(data);
		$.ajax({
			url: url + x,
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
				if(data.respHead.isSuccess == true) {
					search()
				}
			},
			error: function() {
				alert(comfirm + "失败")
			}
		})
	}
}

//导入
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
		var x = "/mis/purc/ToGdsSngl/uploadToGdsSnglFile"
		importExcel(x)
	});
	$(".dropdown-menu .li1").click(function() {
		var x = "/mis/purc/ToGdsSngl/uploadToGdsSnglFileU8"
		importExcel(x)
	});
});

//导出
$(document).on('click', '.exportExcel', function() {
	var invtyEncd = $("#invtyEncd").val();
	var toGdsSnglId = $(".toGdsSnglId").val();
	var provrId = $("#provrId").val();
	var toGdsSnglDt1 = $(".toGdsSnglDt1").val();
	var toGdsSnglDt2 = $(".toGdsSnglDt2").val();
	var isNtChk = $("#isNtChk").val();
	var isNtRtnGood = $("#isNtRtnGood").val();
	var deptId = $("#deptId").val();
	var accNum = $("#user").val();
	var provrOrderNum = $(".provrOrdrNum").val();
	var pursOrdrId = $(".pursOrdrId").val();
	var batNum = $("#batNum").val();
	var intlBat = $(".intlBat").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var whsEncd = $("#whsEncd").val();
	var invtyCd = $(".invtyCd").val();
	var memo = $("#memo").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": invtyEncd,
			"invtyClsEncd": invtyClsEncd,
			"toGdsSnglId": toGdsSnglId,
			"provrId": provrId,
			"toGdsSnglDt1": toGdsSnglDt1,
			"toGdsSnglDt2": toGdsSnglDt2,
			"isNtChk": isNtChk,
			"isNtRtnGood": isNtRtnGood,
			"deptId": deptId,
			"accNum": accNum,
			"provrOrderNum": provrOrderNum,
			"pursOrdrId": pursOrdrId,
			"batNum": batNum,
			"intlBat": intlBat,
			"invtyCd": invtyCd,
			"whsEncd": whsEncd,
			"memo":memo
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/purc/ToGdsSngl/printingToGdsSnglList',
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
			var execlName = '到货单列表'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
})