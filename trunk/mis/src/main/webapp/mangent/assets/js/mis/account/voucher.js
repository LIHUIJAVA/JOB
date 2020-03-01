//表格初始化
$(function() {
	var comnVouchId = localStorage.comnVouchId;
	var showData = {
		"reqHead": reqhead,
		"reqBody": {
			"comnVouchId": comnVouchId
		}
	};
	var postData = JSON.stringify(showData);
	$.ajax({
		type: "post",
		url: url + "/mis/account/vouchTab/voucher/formlist", //列表
		async: true,
		data: postData,
		dataType: 'json',
		contentType: 'application/json',
		success: function(data) {
			var list = data.respBody.list;
			jQuery("#jqGrids").jqGrid({
				data: list,
				datatype: "local", //请求数据返回的类型。可选json,xml,txt
				colNames: ['单据号', '单据类型id', '单据类型', '存货编码', '存货名称', '存货分类编码', '存货分类名称', '批次', '借方科目编号', '借方科目类型',
					'借方科目名称', '借方金额', '借方数量', '贷方科目编号', '贷方科目类型', '贷方科目名称', '贷方金额', '贷方数量'
				], //jqGrid的列显示名字
				colModel: [{
						name: "formNum",
						align: "center",
						editable: true,
						sortable: false,
					},
					{
						name: "formTypEncd",
						align: "center",
						editable: true,
						sortable: false,
					},
					{
						name: "formTypName",
						align: "center",
						editable: true,
						sortable: false,
					},
					{
						name: "invtyEncd",
						align: "center",
						editable: true,
						sortable: false
					},
					{
						name: 'invtyNm',
						align: "center",
						editable: true,
						sortable: false,
					},
					{
						name: "invtyClsEncd",
						align: "center",
						editable: true,
						sortable: false,
						hidden: true
					},
					{
						name: "invtyClsNm",
						align: "center",
						editable: true,
						sortable: false,
						hidden: true
					},
					{
						name: "batNum",
						align: "center",
						editable: true,
						sortable: false,
						hidden: true
					},

					{
						name: "subDebitId",
						align: "center",
						editable: true,
						sortable: false,
					},
					{
						name: "subDebitType",
						align: "center",
						editable: true,
						sortable: false,
					},
					{
						name: "subDebitNm",
						align: "center",
						editable: true,
						sortable: false
					},
					{
						name: 'subDebitMoney',
						align: "center",
						editable: true,
						sortable: false,
					},
					{
						name: "subDebitNum",
						align: "center",
						editable: true,
						sortable: false,
					},
					{
						name: "subCreditId",
						align: "center",
						editable: true,
						sortable: false
					},
					{
						name: 'subCreditType',
						align: "center",
						editable: true,
						sortable: false,
					},
					{
						name: "subCreditNm",
						align: "center",
						editable: true,
						sortable: false,
					},
					{
						name: "subCreditMoney",
						align: "center",
						editable: true,
						sortable: false,
					},
					{
						name: "subCreditNum",
						align: "center",
						editable: true,
						sortable: false,
					},
				],
				height:550,
				autowidth: true,
				autoScroll: true,
				shrinkToFit: false,
				viewrecords: true,
				rownumbers: true,
				loadonce: true,
				forceFit: true,
				rowNum: 10,
				rowList: [10, 20, 30], //可供用户选择一页显示多少条
				pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
				caption: "凭证单据联查", //表格的标题名字
				ondblClickRow: function(rowid) {
					order(rowid)
				},
				footerrow: true,
				gridComplete: function() { 
					var qtys = 0;
					var amts = 0;
					var qty = 0;
					var amt = 0;
					var ids = $("#jqGrids").getDataIDs();
					for(var i = 0; i < ids.length; i++) {
						qtys += parseFloat($("#jqGrids").getCell(ids[i], "subCreditMoney"));
						amts += parseFloat($("#jqGrids").getCell(ids[i], "subDebitMoney"));
						qty += parseFloat($("#jqGrids").getCell(ids[i], "subDebitNum"));
						amt += parseFloat($("#jqGrids").getCell(ids[i], "subCreditNum"));
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
					$("#jqGrids").footerData('set', { 
						"formNum": "合计",
						subCreditMoney: qtys,
						subDebitMoney: amts,
						subDebitNum:qty,
						subCreditNum:amt
					}          );  
				}
			})
		},
		error: function() {
			alert("展示失败")
		}
	})
})
function order(rowid){
	//获得行数据
	//debugger
	var rowData = $("#jqGrids").jqGrid('getRowData', rowid);
	localStorage.setItem("formNum", rowData.formNum);
	localStorage.setItem("formTypEncd", rowData.formTypEncd);
	window.open("../../Components/account/detilForm.html?1");
}