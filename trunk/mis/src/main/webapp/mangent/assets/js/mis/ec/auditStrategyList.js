$(function() {
	pageInit();
//	autoWidthJqgrid();
});

var count;
var pages;
var page = 1;
var rowNum;

//初始化表格
function pageInit() {
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	allHeight()
	jQuery("#jqgrids").jqGrid({
		url: '../../assets/js/json/order.json', //组件创建完成之后请求数据的url
		//		mtype: 'post',
		//		height: '100%',
		ajaxGridOptions: {
			contentType: 'application/json; charset=utf-8'
		},
		//		postData: postData,
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['策略id','策略名称','审核方式','买家留言非空','买家备注包含关键字','卖家留言非空','卖家备注包含关键字','是否包含特殊sku'], //jqGrid的列显示名字
		colModel: [{
			name: "id",
			align: "center",
			editable: true,
			sortable: false,
			hidden:true,
		},{
			name: "name",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "auditWay",
			align: "center",
			editable: true,
			sortable: false,
//			edittype: 'select',
//			editoptions: {
//				value: {0:"否",1:"是"}
//			}
		},{
			name: "buyerNoteNull",
			align: "center",
			editable: true,
			sortable: false,
			edittype: 'select',
			editoptions: {
				value: {0:"否",1:"是"}
			}
		},{
			name: "buyerNote",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "sellerNoteNull",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "sellerNote",
			align: "center",
			editable: true,
			sortable: false,
		},{
			name: "includeSku",
			align: "center",
			editable: true,
			sortable: false,
		}],
		autowidth: true,
		viewrecords: true,
		rownumbers: true,
		loadonce: true,
		collapsed: true,
		height:height,
		autoScroll: true,
		shrinkToFit: false,
		forceFit: true,
		cellEdit: false,
		cellsubmit: "clientArray",
//		multiselect: true, //复选框 
		rownumWidth: 30,  //序列号列宽度
		rowNum: 500, //一页显示多少条
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条	
		pager: '#jqGrids', //表格页脚的占位符(一般是div)的id
//		multiboxonly: true,
		//		multiselect: true, //复选框
		caption: "审核策略", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#jqgrids").getGridParam('records'); //获取返回的记录数
			page = $("#jqgrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#jqgrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			search()
		},
		ondblClickRow: function(rowid) {
			order(rowid);
		},

	})
}

function order(rowid) {
	//获得行数据
	var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
	localStorage.setItem("id", rowDatas.id);
	window.open("../../Components/ec/auditStrategy.html?1");

}

////查询
$(document).on('click', '.search', function() {
	search()
})

function search(){
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
//	alert(1)
//	var myDate = {};
	var name = $("input[name='name']").val();
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"name": name,
			"pageSize": rowNum,
			"pageNo": page
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/ec/auditStrategy/selectList',
		async: true,
		data: saveData,
		dataType: 'json',
		//开始加载动画  添加到ajax里面
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");

		},
		success: function(data) {
			
			var list = data.respBody.list;
			for(var i = 0; i < list.length;i++) {
				if(list[i].auditWay == 0) {
					list[i].auditWay = "免审"
				}else if(list[i].auditWay == 1) {
					list[i].auditWay = "有条件必审"
				}else if(list[i].auditWay == 2) {
					list[i].auditWay = "必审"
				}
				
				if(list[i].buyerNote == 0) {
					list[i].buyerNote = "否"
				} else if(list[i].buyerNote == 1) {
					list[i].buyerNote = "是"
				}
				
				if(list[i].buyerNoteNull == 0) {
					list[i].buyerNoteNull = "否"
				} else if(list[i].buyerNoteNull == 1) {
					list[i].buyerNoteNull = "是"
				}
				
				if(list[i].sellerNoteNull == 0) {
					list[i].sellerNoteNull = "否"
				} else if(list[i].sellerNoteNull == 1) {
					list[i].sellerNoteNull = "是"
				}
				
				if(list[i].sellerNote == 0) {
					list[i].sellerNote = "否"
				} else if(list[i].sellerNote == 1) {
					list[i].sellerNote = "是"
				}
				
				if(list[i].includeSku == 0) {
					list[i].includeSku = "否"
				} else if(list[i].includeSku == 1) {
					list[i].includeSku = "是"
				}
			}
			
			myDate = list;
			var mydata = {};
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#jqgrids").jqGrid("clearGridData");
			$("#jqgrids").jqGrid("setGridParam", {
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
		//结束加载动画
		complete: function() {
			$(".zhezhao").css("display", "none");
			$("#loader").css("display", "none");
		},
		error: function() {
			console.log(error)
		}
	});

}

//删除
$(function() {
	$(".delOrder").click(function() {
		//获得选中行的行号
		var ids = $('#jqgrids').jqGrid('getGridParam', 'selrow');
		var rowData = $("#jqgrids").jqGrid('getRowData', ids);
		var id = rowData.id
		if(id == undefined) {
			alert("请选择单据!")
		} else if(confirm("确定删除？")) {
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"id": id
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/ec/auditStrategy/delete',
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