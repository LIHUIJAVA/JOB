//表格初始化-采购结算单列表
$(function() {
	allHeight()
	$("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['结算单号', '业务类型编码', '采购类型编码', '结算日期', '部门编码', '表头税率',
			'结算人', '备注'],
		colModel: [{
				name: 'stlSnglNum',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'bizTypEncd',
				editable: true,
				align: 'center'

			},
			{
				name: 'pursTypEncd',
				editable: true,
				align: 'center'

			},
			{
				name: 'sltDt',
				editable: false,
				align: 'center',
			},
			{
				name: 'deptEncd',
				editable: false,
				align: 'center'
			},
			{
				name: 'tabHeadTaxRate',
				editable: false,
				align: 'center'
			},
			{
				name: 'stlPers',
				editable: false,
				align: 'center',
			},
			{
				name: 'memo',
				editable: true,
				align: 'center'
			},
		],
		autowidth: true,
		height:height,
		autoScroll:true,
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
		caption: "采购结算单列表", //表格的标题名字
		//双击弹出采购订单
		ondblClickRow: function(rowid) {
			order(rowid);
		},
	})
})


//查询按钮
$(document).on('click', '.search', function() {
	search()
})
function search () {
	
	var myDate = {};

//	var invtyEncd = $(".invtyEncd").val();
//	var intoWhsSnglId = $(".intoWhsSnglId").val();
//	var provrId = $(".provrId").val();
//	var intoWhsDt1 = $(".intoWhsSnglDt1").val();
//	var intoWhsDt2 = $(".intoWhsSnglDt2").val();
var isNtChk = $("#isNtChk").val();

	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
//			"invtyEncd": invtyEncd,
//			"intoWhsSnglId": intoWhsSnglId,
//			"provrId": provrId,
//			"intoWhsDt1": intoWhsDt1,
//			"intoWhsDt2": intoWhsDt2,
			"isNtChk":isNtChk,
			"pageNo": 1,
			"pageSize": 999999999
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/account/pursStlSnglMasTab/selectPursStlSnglMasTab',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			var rowNum = $("#_input").val()
			var list = data.respBody.list;
			myDate = list;
			$("#jqGrids").jqGrid('clearGridData');
			$("#jqGrids").jqGrid('setGridParam', {
				rowNum:rowNum,
				datatype: 'local',
				data: myDate, //newData是符合格式要求的重新加载的数据
				page: 1 //哪一页的值
			}).trigger("reloadGrid")
		},
		error: function() {
		}
	});

}

function order(rowid) {
	//获得行数据
	var rowDatas = $("#jqGrids").jqGrid('getRowData', rowid);
	localStorage.setItem("stlSnglNum", rowDatas.stlSnglNum);
	window.open("../../Components/purs/pursStlSnglMasTab.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');

}

//function ntChk(x) {
//	//获得选中行的行号
//	var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
//	//对象数组
//	var rowData = [];
//	for(var i = 0; i < ids.length; i++) {
//		var obj = {}; //对象
//		//选中行的id
//		//把选中行的i添加到数据对象中
//		obj.stlSnglNum = $("#jqGrids").getCell(ids[i], "stlSnglNum").toString();
//		obj.isNtChk = x;
//		obj.chkr = loginName;
//		//建一个数组，把单个对象添加到数组中
//		rowData[i] = obj;
//	}
//	if(rowData.length == 0) {
//		alert("请选择单据!")
//	} else {
//		var data = {
//			"reqHead": reqhead,
//			"reqBody": {
//				"list": rowData
//			}
//		};
//		var Data = JSON.stringify(data);
//		$.ajax({
//			url: url + '/mis/purc/IntoWhs/updateIntoWhsIsNtChk',
//			type: 'post',
//			data: Data,
//			dataType: 'json',
//			async: true,
//			contentType: 'application/json;charset=utf-8',
//			success: function(data) {
//				alert(data.respHead.message)
//			},
//			error: function(error) {
//				alert(error.responseText)
//			}
//		})
//	}
//
//}
//
////审核与弃审
//$(function() {
//	//审核
//	$(".toExamine").click(function() {
//		ntChk(1)
//	})
//
//	//弃审
//	$(".noTo").click(function() {
//		ntChk(0)
//	})
//})

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
			var data = $("#stlSnglNum").getCell(ids[i], "stlSnglNum");
			//建一个数组，把选中行的id添加到这个数组中去。
			rowData[i] = data;
		}
		if(rowData.length == 0) {
			alert("请选择单据!")
		} else {
			var intoWhsSnglId = rowData.toString();
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"list": [{
						"stlSnglNum": stlSnglNum,
						"isNtChk": 0
					}]
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/account/pursStlSnglMasTab/deletePursStlSnglMasTab',
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

$(function() {
	$("#last_jqGridPager").after('<input id="_input" type="text" value="10"/>')
})