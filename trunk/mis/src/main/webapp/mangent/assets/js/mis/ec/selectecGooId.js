
$(function() {
	pageInites()
})
//页面初始化
function pageInites() {
	allHeight()
	//初始化表格
	jQuery("#selectecGooId_jqgrids").jqGrid({
		url: '../../assets/js/json/order.json', //组件创建完成之后请求数据的url
		height: height,
		autoScroll:true,
		shrinkToFit:false,
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['原始商品编号', '原始商品名称', '改后商品编号', '倍数'], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'invId',
				align: "center",
				editable: false,
			},
			{
				name: 'invtyNm',
				align: "center",
				editable: false,
			},
			{
				name: 'invIdLast',
				align: "center",
				editable: true,
			},
			{
				name: 'multiple',
				align: "center",
				editable: true,
                formatter:"integer",
                formatoptions: { defaultValue: '1' } ,
			}
		],
		autowidth: true,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		cellEdit: true,
//		editurl: "clientArray", // 行提交
		cellsubmit: "clientArray",
		rowNum: 100,
		scrollrows:true,
		rowList: [100, 200, 300], //可供用户选择一页显示多少条
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		multiboxonly: true,
		caption: "订单商品修改列表", //表格的标题名字
		pager: '#selectecGooId_jqGridPager', //表格页脚的占位符(一般是div)的id
		//在编辑状态时
		afterEditCell: function(rowid, cellname, val, iRow, iCol) {
			if(cellname == "invIdLast") {
				$("#" + rowid + "_invIdLast").attr("readonly", "readonly");
				$("#" + rowid + "_invIdLast").bind("dblclick", function() {
					$(".box").hide()
					$(".down").hide()
					$(".oneSerch_List").hide()
					$(".formSave_box").hide()
					$(".oneSerch_List").css("opacity", 0)
					$(".hand_split").css("opacity", 0)
					$(".hand_split").hide()
					$(".selectecGooId").hide()
					$("#insertList").show();
					$("#insertList").css("opacity", 1);
				});
			}
			if(cellname == "multiple") {
				$("input[name='multiple']").attr("onkeyup","if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
				$("input[name='multiple']").attr("onafterpaste","if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}")
			}
		},
		//离开编辑状态
		afterRestoreCell: function(rowid, cellname, val, iRow, iCol) {
			if(cellname == "multiple") {
				test(iRow)
			}
			$(".saveOrder").removeClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true)
		},
		afterSaveCell: function(rowid, cellname, val, iRow, iCol) {
			if(cellname == "multiple") {
				test1(iRow)
			}
		}
	});
}
function test1(iRow) {
//	获得行数据
	var rowDatas = $("#selectecGooId_jqgrids").jqGrid('getRowData', iRow);
	var multiple = rowDatas.multiple
	if(!(/(^[1-9]\d*$)/.test(multiple))) {
		alert('输入的不是正整数或未回车提交单元格');
		return false;
	}
}