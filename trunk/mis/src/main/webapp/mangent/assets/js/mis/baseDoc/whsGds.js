var count;
var pages;
var page = 1;
var rowNum;
var lastId;
$(function() {
	//点击表格图标显示店铺列表
	$(document).on('click', '.biao_invtyEncd', function() {
		window.open("../../Components/whs/gdsBitList.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	$(document).on('click', '.biao_whsEncd1', function() {
		localStorage.setItem("realWhss",0)
		window.open("../../Components/baseDoc/insertRealWhsList.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
		//区域
	$(document).on('click', '.biao_regnEncd', function() {
		window.open("../../Components/whs/regnList.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})

//刚开始时可点击的按钮
$(function() {
	$(".saveOrder").addClass("gray") //参照
	$(".gray").attr("disabled", true)
	
	$(".download").removeClass("gray")
	$("#mengban").hide()
	$(".down").hide()
})

$(function() {
	pageInit_Gds();
})

//页面初始化
var myData = {};
function pageInit_Gds() {
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	//初始化表格
	allHeight()
	jQuery("#whsGds_jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json",
		colNames: ['id' ,'总仓', '货位编码', '货位名称','区域编码','区域名称'], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'id',
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: 'realWhs',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'gdsBitEncd',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'gdsBitNm',
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: 'regnEncd',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'regnNm',
				align: "center",
				editable: false,
				sortable: false,
			}
		],
		autowidth:true,
		height:height,
		autoScroll:true,
		shrinkToFit:false,
		viewrecords: true,
		sortable:true,
		rownumbers: true,
		loadonce: true,
		editurl:"clientArray", // 行提交
		forceFit: true, //调整列宽度不会改变表格的宽度
		rowNum: 500, //一页显示多少条
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条	
		pager: '#whsGds_jqGridPager', //表格页脚的占位符(一般是div)的id

		multiselect: true, //复选框
		multiboxonly: true,
		caption: "仓库货位对应关系", //表格的标题名字	
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#whsGds_jqgrids").getGridParam('records'); //获取返回的记录数
			page = $("#whsGds_jqgrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#whsGds_jqgrids").getGridParam('rowNum');

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
			search_Gds();
		}
	});
	search_Gds()
}


//查询按钮
$(document).on('click', '#finds', function() {
	search_Gds()
})

//条件查询
function search_Gds() {
	var gdsBitEncd = $("input[name='gdsBitEncd1']").val();
	var realWhs = $("input[name='realWhs1']").val();
	var regnEncd = $("input[name='regnEncd1']").val();
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	
	var myDate = {};
	var data2 = {
		reqHead,
		"reqBody": {
			"gdsBitEncd":gdsBitEncd,
			"realWhs":realWhs,
			"regnEncd":regnEncd,
			"pageNo": page,
			"pageSize": rowNum
		}

	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url3 + "/mis/whs/whs_doc/selectWhsGds",
		data: postD2,
		dataType: 'json',
		async: true,
		//开始加载动画  添加到ajax里面
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");

		},
		success: function(data) {
			var mydata = {};
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#whsGds_jqgrids").jqGrid("clearGridData");
			$("#whsGds_jqgrids").jqGrid("setGridParam", {
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

var mType = 0;
$(function() {
	$(".saveOrder").click(function() {
		if(mType == 1) {
			$(".addOrder").css("background-color", 'black')
			addNewData(); //添加新数据
		}
	})
})
var newrowid;
//增行   保存
$(function() {
	
	$(".addOrder").click(function() {
		$(".saveOrder").removeClass("gray")
		$(".saveOrder").attr("disabled",false)
		mType = 1;
		var selectedId = $("#whsGds_jqgrids").jqGrid("getGridParam", "selrow");
		var ids = $("#whsGds_jqgrids").jqGrid('getDataIDs');

		// 获得当前最大行号（数据编码）
		var rowid = Math.max.apply(Math, ids);
		// 获得新添加行的行号（数据编码）
		newrowid = rowid + 1;
		var dataRow = {
			"realWhs": '',
			"gdsBitEncd": '',
			"gdsBitNm": '',
			"regnEncd": '',
			"regnNm": ''
		};
//		$("#whsGds_jqgrids").setColProp('storeId', {
//			editable: true
//		}); //设置editable属性由true改为false

		//将新添加的行插入到第一列
		$("#whsGds_jqgrids").jqGrid("addRowData", newrowid, dataRow, "first");
		//设置grid单元格可编辑
		$('#whsGds_jqgrids').jqGrid('editRow', newrowid, true);
		$("#whsGds_jqgrids").jqGrid('setSelection',newrowid);
		grr(newrowid)
		$(".addOrder").addClass("gray")
		$(".addOrder").attr("disabled",true)
		$("#" + newrowid + "_realWhs").attr("readonly", "readonly");
		$("#" + newrowid + "_gdsBitEncd").attr("readonly", "readonly");
		$("#" + newrowid + "_regnEncd").attr("readonly", "readonly");
	})

})


function grr(gr) {
	$(".save").removeClass("gray");
	
//	仓库
	$("#"+gr+"_gdsBitEncd").on("dblclick", function() {
		$("#purchaseOrder").hide()
		$("#whsDocList").hide()
		$("#regn").hide()
		$("#insertList").show()
		$("#insertList").css("opacity",1)
	})
	
//	货位档案
	$("#"+gr+"_realWhs").on("dblclick", function() {
		$(".save").attr("disabled",false)
		$("#purchaseOrder").hide()
		$("#insertList").hide()
		$("#regn").hide()
		$("#whsDocList").show()
		$("#whsDocList").css("opacity",1)
	})
	
//	区域档案
	$("#"+gr+"_regnEncd").on("dblclick", function() {
		$(".save").attr("disabled",false)
		$("#purchaseOrder").hide()
		$("#insertList").hide()
		$("#whsDocList").hide()
		$("#regn").show()
		$("#regn").css("opacity",1)
	})
}

//打开仓库/存货档案后点击确定取消
$(function() {
	//确定
	$(".true_gb").click(function() {
		//获得行号
		var rowid = $("#whsGds_jqgrids").jqGrid('getGridParam', 'selrow');

		//	存货档案
		//	获得行号
		var ids = $("#jqGrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowData = $("#jqGrids").jqGrid('getRowData', ids);

		$("#" + rowid + "_gdsBitEncd").val(rowData.gdsBitEncd)
		$("#whsGds_jqgrids").setRowData(rowid, {
			gdsBitNm: rowData.gdsBitNm
		})

		$("#whsDocList").css("opacity", 0);
		$("#regn").css("opacity", 0);
		$("#insertList").css("opacity", 0);
		$("#purchaseOrder").show();
	})
	//确定
	$(".yes").click(function() {
		//获得行号
		var rowid = $("#whsGds_jqgrids").jqGrid('getGridParam', 'selrow');

		//	仓库档案
		//	获得行号
		var gr = $("#zc_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDatas = $("#zc_jqgrids").jqGrid('getRowData', gr);
		$("#" + rowid + "_realWhs").val(rowDatas.realWhs)

		$("#regn").css("opacity", 0);
		$("#whsDocList").css("opacity", 0);
		$("#insertList").css("opacity", 0);
		$("#purchaseOrder").show();
	})
	//确定
	$("#true").click(function() {
		//获得行号
		var rowid = $("#whsGds_jqgrids").jqGrid('getGridParam', 'selrow');

		//	仓库档案
		//	获得行号
		var gr = $("#r_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDatas = $("#r_jqgrids").jqGrid('getRowData', gr);
		$("#" + rowid + "_regnEncd").val(rowDatas.regnEncd)
		$("#whsGds_jqgrids").setRowData(rowid, {
			regnNm: rowDatas.regnNm
		})

		$("#regn").css("opacity", 0);
		$("#whsDocList").css("opacity", 0);
		$("#insertList").css("opacity", 0);
		$("#purchaseOrder").show();
	})
	//	取消
	$(".false_gb").click(function() {
		$("#whsDocList").hide();
		$("#whsDocList").css("opacity", 0);
		$("#insertList").hide();
		$("#insertList").css("opacity", 0);
		$("#purchaseOrder").show();
	})
	$("#false").click(function() {
		$("#whsDocList").hide();
		$("#whsDocList").css("opacity", 0);
		$("#insertList").hide();
		$("#insertList").css("opacity", 0);
		$("#purchaseOrder").show();
	})
	$(".no").click(function() {
		$("#whsDocList").hide();
		$("#whsDocList").css("opacity", 0);
		$("#insertList").hide();
		$("#insertList").css("opacity", 0);
		$("#purchaseOrder").show();
	})
})

function IsCheckValue(realWhs, gdsBitEncd) {
	if(realWhs == "") {
		alert("总仓不能为空")
		return false;
	} else if(gdsBitEncd == "") {
		alert("货位编码不能为空")
		return false;
	} 
	return true;
}

//添加新数据
function addNewData() {
	var gdsBitEncd = $("input[name='gdsBitEncd']").val()
	var realWhs = $("input[name='realWhs']").val()
	var regnEncd = $("input[name='regnEncd']").val()
	console.log(gdsBitEncd)
	if(IsCheckValue(realWhs, gdsBitEncd) == true) {
		var save = {
			reqHead,
			"reqBody": {
				"gdsBitEncd": gdsBitEncd,
				"realWhs": realWhs,
				"regnEncd": regnEncd
			}
		}
		var saveJson = JSON.stringify(save);
		$.ajax({
			type: "post",
			url: url3 + "/mis/whs/whs_doc/insertWhsGds",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(msgAdd) {
				alert(msgAdd.respHead.message)
				if(data.respHead.isSuccess == true) {
					$(".addOrder").removeClass("gray")
					$(".addOrder").attr("disabled",false)
					search_Gds()
				}
			},
			error: function(err) {
				console.log("失败")
			}
		});
	}
}


//删除行
$(function() {
	$(".delOrder").click(function() {
		var gr = $("#whsGds_jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
		var rowDatas = $("#whsGds_jqgrids").jqGrid('getRowData', gr); //获取行数据
		var gdsBitEncd = rowDatas.gdsBitEncd
		var realWhs = rowDatas.realWhs
		var id = rowDatas.id
		var deleteAjax = {
			reqHead,
			"reqBody": {
				"gdsBitEncd": gdsBitEncd,
				"realWhs": realWhs,
				"id": id
			}
		};
		var deleteData = JSON.stringify(deleteAjax)
		console.log(deleteData)
		if(gr.length == 0) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url3 + "/mis/whs/whs_doc/deleteWhsGds",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(data) {
					alert(data.respHead.message);
					if(data.respHead.isSuccess == true) {
						search3()
					}
				},
				error: function() {
					console.log("删除失败")

				}
			});

		}
	})
})