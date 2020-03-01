
$(function() {
	$("#jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json",
		colNames: ['仓库编码', '仓库名称', '存货编码', '存货名称', '主计量单位',
			'规格型号', '数量', '含税单价', '价税合计', '批次', '生产日期',
			'失效日期', '保质期', '项目编码', '项目名称', '箱规', '箱数', '备注', '对应条形码',
			'国际批次', '无税单价', '无税金额', '税率', '税额', '是否退货', '主计量单位编码',
			//			,'存货代码'
		], //jqGrid的列显示名字
		colModel: [{
				name: 'whsEncd',
				editable: true,
				sortable: false,
				align: 'center',
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
				name: 'measrCorpNm',
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
				name: 'baoZhiQiDt',
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
			{
				name: 'projNm',
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
				name: 'memo',
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
				name: 'intlBat', //国际批次
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'noTaxUprc',
				editable: false,
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
				name: 'isNtRtnGoods',
				editable: true,
				align: 'center',
				hidden: true,
				sortable: false
			},
			{
				name: 'measrCorpId',
				editable: false,
				align: 'center',
				hidden: true,
				sortable: false
			},
		],
		rowNum: 99999999,
		loadonce: false,
		rownumbers: true,
		autowidth: true,
		height: 500,
		autoScroll: true,
		shrinkToFit: false,
		multiselect: true, //复选框 
		caption: '详情页',
		altclass: true,
		pager: "#jqgridPager",
		pgbuttons: false,
		pginput: false,
		forceFit: true,
		sortable: false,
		cellsubmit: "clientArray",
		footerrow: true,
		editurl: "clientArray", // 行提交
		gridComplete: function() { 
			$("#jqgrids").footerData('set', { 
				"whsEncd": "本页合计",
			});
		},
	})

})

//查询详细信息
$(function() {
	var afterUrl = window.location.search.substring(1);
	chaxun()
})

function chaxun() {
	var formNum = localStorage.formNum;
	var formTypEncd = localStorage.formTypEncd;
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"isFindForm":"1",
			"isSelect":"",
			"formNum": formNum,
			"formType": formTypEncd,
			"pageNo": 1,
			"pageSize": 1
		}
	};
	var saveData = JSON.stringify(savedata);
	var myDate = {};
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + "/mis/account/form/voucher/formList",
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			var list1 = data.respBody.list[0];
//			$("input[name='formTypName']").val(list1.formTypName);
			$("input[name='department']").val(list1.deptName);

			$("input[name='formDt']").val(list1.formDt);
			$("input[name='formNum']").val(list1.formNum);
			$("input[name='accNum']").val(list1.accNum);
			$("input[name='provr']").val(list1.provrNm);
			$("input[name='custId']").val(list1.custId);
			$("input[name='custNm']").val(list1.custNm);
			$("#userName").val(list1.userName);
			$(".custOrdrNum").val(list1.custOrdrNum)
			$("#deptId").val(list1.deptId);
			$("#deptName").val(list1.deptName);

			var a = list1.bizTypId
			var b = parseInt(a);
			$("#bizType").get(0).selectedIndex = b

			$("input[name='addr']").val(list1.recvrAddr);
			$("input[name='txId']").val(list1.txId);
			$("input[name='memo']").val(list1.memo);
			var arr = eval(data); //数组
			var list = arr.respBody.list[0].bookEntrySub;
			
			$("#jqgrids").jqGrid('setGridParam', {
				datatype: 'local',
				data: list, //newData是符合格式要求的重新加载的数据
				page: 1 //哪一页的值
			}).trigger("reloadGrid")
			sumAdd()
		}
	})

}
function sumAdd() {
	var list = getJQAllData();
	var qty = 0;
	var noTaxAmt = 0;
	var taxAmt = 0;
	var prcTaxSum = 0;
	for(var i = 0; i < list.length; i++) {
		qty += parseFloat(list[i].qty);
		noTaxAmt += parseFloat(list[i].noTaxAmt);
		taxAmt += parseFloat(list[i].taxAmt);
		prcTaxSum += parseFloat(list[i].prcTaxSum);
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
	qty = qty.toFixed(2)
	noTaxAmt = precision(noTaxAmt,2)
	taxAmt = precision(taxAmt,2)
	prcTaxSum = precision(prcTaxSum,2)
	$("#jqgrids").footerData('set', {
		"qty": qty,
		"noTaxAmt": noTaxAmt,
		"taxAmt": taxAmt,
		"prcTaxSum": prcTaxSum,
	});
}
//拿到grid对象
function getJQAllData() {
	var obj = $("#jqgrids");
	//获取grid表中所有的rowid值
	var rowIds = obj.getDataIDs();
	//初始化一个数组arrayData容器，用来存放rowData
	var arrayData = new Array();
	if(rowIds.length > 0) {
		for(var i = 0; i < rowIds.length; i++) {
			if(obj.getRowData(rowIds[i]).whsEncd == "") {
				continue;
			} else {
				arrayData.push(obj.getRowData(rowIds[i]));
			}
		}
	}
	return arrayData;
}