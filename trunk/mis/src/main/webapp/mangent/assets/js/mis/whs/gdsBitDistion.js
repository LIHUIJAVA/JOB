$(function() {
	//点击表格图标显示仓库列表
	$(document).on('click', '.biao_whsEncd', function() {
		window.open("../../Components/baseDoc/whsList.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	$(document).on('click', '.biao_invtyEncd', function() {
		window.open("../../Components/baseDoc/invtyList.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	$(document).on('click', '.biao_batNum', function() {
		window.open("../../Components/baseDoc/batNum.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	//货位
	$(document).on('click', '.biao_gdsBitEncd', function() {
		localStorage.setItem("gds",0)
		window.open("../../Components/whs/gdsBitList.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	//货位1
	$(document).on('click', '.biao1_gdsBitEncd', function() {
		localStorage.setItem("gds",1)
		window.open("../../Components/whs/gdsBitList.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	//货位2
	$(document).on('click', '.biao2_gdsBitEncd', function() {
		localStorage.setItem("gds",2)
		window.open("../../Components/whs/gdsBitList.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})
var count;
var pages;
var page = 1;
var rowNum;
//表格初始化
$(function() {
	allHeight()
	//加载动画html 添加到初始的时候
	$(".jz").append("<div id='mengban1' class='zhezhao'></div>");
	$(".jz").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	$("#invtyWhs_jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['序号', '原始货位', '移入货位', '存货编码', '存货名称', '仓库编码', '仓库名称', '批号', '数量', '操作人', '操作人编码', '是否移位完成', '规格型号', '生产日期'],
		colModel: [{
				name: 'ordrNum',
				editable: false,
				align: 'center',
				hidden:true
			},
			{
				name: 'oalBit',
				editable: false,
				align: 'center'
			},
			{
				name: 'targetBit',
				editable: false,
				align: 'center'
			},
			{
				name: 'invtyEncd',
				editable: false,
				align: 'center'
			},
			{
				name: 'invtyNm',
				editable: true,
				align: 'center'
			},
			{
				name: 'whsEncd',
				editable: true,
				align: 'center'

			},
			{
				name: 'whsNm',
				editable: false,
				align: 'center'
			},
			{
				name: 'batNum',
				editable: false,
				align: 'center'
			},
			{
				name: 'oalBitNum',
				editable: true,
				align: 'center',
			},
			{
				name: 'operator',
				editable: true,
				align: 'center',
			},
			{
				name: 'operatorId',
				editable: false,
				align: 'center'
			},
			{
				name: 'isOporFish',
				editable: false,
				align: 'center'
			},
			{
				name: 'spcModel',
				editable: true,
				align: 'center',
			},
			{
				name: 'prdcDt',
				editable: true,
				align: 'center',
			}
		],
		autowidth: true,
		height: height,
		autoScroll: true,
		shrinkToFit: false,
		rownumbers: true,
		loadonce: false,
		forceFit: true,
		rowNum: 500,
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条
		pager: '#invtyWhs_jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
//		multiboxonly: true,
		caption: "移位列表", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#invtyWhs_jqGrids").getGridParam('records'); //获取返回的记录数
			page = $("#invtyWhs_jqGrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#invtyWhs_jqGrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			search1()
		}
	})
})

//查询按钮
$(document).on('click', '.search1', function() {
	search1()
})

function search1() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var gdsBitEncd = $("input[name='gdsBitEncd']").val();
	var gdsBitEncd1 = $("input[name='gdsBitEncd1']").val();
	var whsEncd = $("input[name='whsEncd']").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var batNum = $("input[name='batNum']").val();
	var isOporFish = $("select[name='isOporFish']").val();

	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"oalBit": gdsBitEncd,
			"targetBit": gdsBitEncd1,
			"whsEncd": whsEncd,
			"invtyEncd": invtyEncd,
			"batNum": batNum,
			"isOporFish": isOporFish,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/whs/gds_bit_distion/selectMTabLists',
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
			var list = data.respBody.list;
			var mydata = {};
			for(var i =0;i<list.length;i++) {
				if(list[i].isOporFish == 0) {
					list[i].isOporFish = '否'
				}else if(list[i].isOporFish == 1) {
					list[i].isOporFish = '是'
				}
			}
			mydata.rows = list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#invtyWhs_jqGrids").jqGrid("clearGridData");
			$("#invtyWhs_jqGrids").jqGrid("setGridParam", {
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
			alert("查询失败")
		}
	});
}

$(function() {
	$('.delete').click(function() {
		var gr = $("#invtyWhs_jqGrids").jqGrid('getGridParam', 'selarrrow'); //获取行id
		var num = []
		for(var i =0;i<gr.length;i++) {
			var rowDatas = $("#invtyWhs_jqGrids").jqGrid('getRowData', gr[i]); //获取行数据
			num.push(rowDatas.ordrNum)
		}
		if(num.length == 0) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			var ordrNum = num.toString()
			var deleteAjax = {
				reqHead,
				"reqBody": {
					"ordrNum": ordrNum
				}
			};
			var deleteData = JSON.stringify(deleteAjax)
			$.ajax({
				type: "post",
				url: url + "/mis/whs/gds_bit_distion/deleteMovbit",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(remover) {
					alert(remover.respHead.message);
					search1()
				},
				error: function() {
					console.log("删除失败")

				}
			});
		}
	})
	$('.move1').click(function() {
		$("#wraps").show()
		$("#wraps").css("opacity",1)
		$("#purchaseOrder").hide()
	})
	$('.back').click(function() {
		$("#wraps").hide()
		$("#wraps").css("opacity",0)
		$("#purchaseOrder").show()
		search1()
	})
	$('.move').click(function() {
		var gr = $("#jqGrids").jqGrid('getGridParam', 'selrow'); //获取行id
		var rowDatas = $("#jqGrids").jqGrid('getRowData', gr); //获取行数据
		var whsEncd = rowDatas.whs_encd;
		if(whsEncd == undefined || whsEncd == "") {
			alert("请选择正确单据")
		} else {
			localStorage.setItem("whs_encd", rowDatas.whs_encd)
			$("#big_wrap").show();
			$("#box").show();
		}
	})
	$(".sure").click(function() {
		var gdsBitEncd2 = $('#gdsBitEncd2').val();
		var qty2 = $(".qty").val()
		var gr = $("#jqGrids").jqGrid('getGridParam', 'selrow'); //获取行id
		var rowDatas = $("#jqGrids").jqGrid('getRowData', gr); //获取行数据
		var whsEncd = rowDatas.whs_encd;
		var gdsBitEncd1 = rowDatas.gds_bit_encd;
		var invtyEncd = rowDatas.invty_encd;
		var batNum = rowDatas.bat_num;
		var regnEncd = rowDatas.regn_encd;
		var TargetRegnEncd = $("input[name='regnEncd']").val();
		//获得选中行的行号
		var ids = $('#jqGrids').jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDatas = $("#jqGrids").jqGrid('getRowData', ids);
		var prdcDt = rowDatas.prdc_dt;
		var qty1 = rowDatas.qty * 1;
		var qty3 = qty2 * 1
		var gdsBitEncd = $("input[name='gdsBitEncd2']").val()
		if(gdsBitEncd2 == "" || qty2 == "" || gdsBitEncd == "") {
			alert('请输入条件')
		} else if(qty3 > qty1) {
			alert("移位数量超出原有数量")
		} else {
			$("#big_wrap").hide();
			$("#box").hide();

			var savedata = {
				"reqHead": reqhead,
				"reqBody": {
					"list": [{
						"oalBit": gdsBitEncd1,
						"targetBit": gdsBitEncd,
						"regnEncd": regnEncd,
						"TargetRegnEncd": TargetRegnEncd,
						"whsEncd": whsEncd,
						"invtyEncd": invtyEncd,
						"prdcDt": prdcDt,
						"batNum": batNum,
						"qty": qty2,
					}]
				}
			}
			var saveData = JSON.stringify(savedata);
			$.ajax({
				type: "post",
				contentType: 'application/json; charset=utf-8',
				url: url + '/mis/whs/gds_bit_distion/insertMovbitPC',
				async: true,
				data: saveData,
				dataType: 'json',
				success: function(data) {
					alert(data.respHead.message)
					$("#big_wrap").hide();
					$("#box").hide();
				},
				error: function() {
					console.log(error)
				}
			});
		}
	})
	$(".cancel").click(function() {
		$("#big_wrap").hide();
		$("#box").hide();
	})
	
	$(".move2").click(function() {
		var gr = $("#invtyWhs_jqGrids").jqGrid('getGridParam', 'selarrrow'); //获取行id
		var num = []
		var num1 = []
		for(var i =0;i<gr.length;i++) {
			var rowDatas = $("#invtyWhs_jqGrids").jqGrid('getRowData', gr[i]); //获取行数据
			num.push(rowDatas.ordrNum)
			num1.push(rowDatas.isOporFish)
	}
		var sum = $.inArray("是", num1)
		var ordrNum = num.toString();
		if(gr.length == 0) {
			alert('请选择单据！')
		} else if(sum != -1){
			alert('所选单据存在以完成移位单据！')
		} else {
			var savedata = {
				"reqHead": reqhead,
				"reqBody": {
					"ordrNum": ordrNum,
				}
			}
			var saveData = JSON.stringify(savedata);
			$.ajax({
				type: "post",
				contentType: 'application/json; charset=utf-8',
				url: url + '/mis/whs/gds_bit_distion/updateMBitListPC',
				async: true,
				data: saveData,
				dataType: 'json',
				success: function(data) {
					alert(data.respHead.message)
					search1()
				},
				error: function() {
					console.log(error)
				}
			});
		}
	})
})