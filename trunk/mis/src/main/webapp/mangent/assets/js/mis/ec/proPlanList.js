var count;
var pages;
var page = 1;
var rowNum;
//表格初始化
$(function() {
	allHeight();
	//加载动画html 添加到初始的时候
	$(".jz").append("<div id='mengban1' class='zhezhao'></div>");
	$(".jz").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	$("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['促销方案编码', '促销方案名称', '促销方式', '促销条件', '赠品倍增', '表头备注', ],
		colModel: [{
				name: 'proPlanId',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'proPlanName',
				editable: true,
				align: 'center',
				sortable: false

			},
			{
				name: 'proWay',
				editable: true,
				align: 'center',
				sortable: false

			},
			{
				name: 'proCriteria',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'giftMul',
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
		],
		autowidth: true,
		height: height,
		autoScroll: true,
		rowNum: 500, //一页显示多少条
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条	
		shrinkToFit: false,
		rownumbers: true,
		loadonce: false,
		sortable:true,
		forceFit: true,
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
//		multiboxonly: true,
		caption: "促销方案列表", //表格的标题名字
		ondblClickRow: function(rowid) {
			order(rowid);
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
			search4()
		},
	})
})


//查询按钮
$(document).on('click', '.search', function() {
	search4()
})


function search4() {
	//查询按钮
//	$(document).on('click', '.search', function() {
		var myDate = {};

		var proPlanId = $(".proPlanId").val();
		var proPlanName = $(".proPlanName").val();
		var proCriteria = $(".proCriteria").val();
		var intoWhsSnglDt1 = $(".intoWhsSnglDt1").val();
		var intoWhsSnglDt2 = $(".intoWhsSnglDt2").val();
		var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
		rowNum = parseInt(rowNum1)

		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				"proPlanId": proPlanId,
				"proPlanName": proPlanName,
				/*	"provrId": provrId,
					"intoWhsDt1": intoWhsDt1,
					"intoWhsDt2": intoWhsDt2,*/
				"pageNo": page,
				"pageSize": rowNum
			}
		};
		var saveData = JSON.stringify(savedata)
		$.ajax({
			type: "post",
			contentType: 'application/json; charset=utf-8',
			url: url + '/mis/ec/proPlan/queryList',
			async: true,
			data: saveData,
			dataType: 'json',
			//开始加载动画  添加到ajax里面
			beforeSend: function() {
				$(".zhezhao").css("display", "block");
				$("#loader").css("display", "block");
	
			},
			success: function(data) {
				var mydata = {};
				mydata.rows = data.respBody.list;
				var listData = mydata.rows
				for(var i = 0;i<listData.length;i++) {
					if(listData[i].proWay == 0) {
						listData[i].proWay = "赠品"
					}
					if(listData[i].proCriteria == 1) {
						listData[i].proCriteria = "买一送一"
					}
					if(listData[i].proCriteria == 2) {
						listData[i].proCriteria = "买n送n"
					}
					if(listData[i].proCriteria == 3) {
						listData[i].proCriteria = "满额送"
					}
					if(listData[i].proCriteria == 4) {
						listData[i].proCriteria = "前xx名赠xx"
					}
					if(listData[i].proCriteria == 5) {
						listData[i].proCriteria = "前xx小时赠xx"
					}
					if(listData[i].proCriteria == 6) {
						listData[i].proCriteria = "前xx名满xxx赠xxx"
					}
					if(listData[i].proCriteria == 7) {
						listData[i].proCriteria = "前xx小时满xx赠xx"
					}
					if(listData[i].giftMul == 0) {
						listData[i].giftMul = "否"
					}
					if(listData[i].giftMul == 1) {
						listData[i].giftMul = "是"
					}
				}
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
			//结束加载动画
			complete: function() {
				$(".zhezhao").css("display", "none");
				$("#loader").css("display", "none");
			},
			error: function() {
				console.log(error)
			}
		});
//	})
}

function order(rowid) {
	//获得行数据
	var rowDatas = $("#jqGrids").jqGrid('getRowData', rowid);

	localStorage.setItem("proPlanId1", rowDatas.proPlanId);
	window.open("../../Components/ec/proPlan.html?1");

}


//删除
$(function() {
	$(".delOrder").click(function() {
		//获得选中行的行号
		var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
		var arr = []
		for(var i = 0; i < ids.length; i++) {
			var rowData = $("#jqGrids").jqGrid('getRowData', ids[i]);
			var proPlanId = rowData.proPlanId
			arr.push(proPlanId)
		}
		if(ids.length == 0) {
			alert("请选择单据!")
		} else if(confirm("确定删除？")) {
			var num = arr.toString();
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"proPlanId": num
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/ec/proPlan/delete',
				type: 'post',
				data: Data,
				dataType: 'json',
				async: true,
				contentType: 'application/json;charset=utf-8',
				success: function(data) {
					alert(data.respHead.message)
					search4()
				},
				error: function() {
					console.log(error)
				}
			})
		}
	})
})