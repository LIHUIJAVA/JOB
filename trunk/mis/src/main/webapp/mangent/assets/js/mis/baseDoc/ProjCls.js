var count;
var pages;
var page = 1;
var rowNum;
var mType = 0;

//刚开始时可点击的按钮
$(function() {
	$(".saveOrder").addClass("gray") //参照
	$(".gray").attr("disabled", true)
	
	$(".download").removeClass("gray")
//	$("#mengban").hide()
//	$(".down").hide()
})


$(function() {
	$(document).on('click', '.break', function() {
		window.location.reload()
	})
})



var myData = {};
//页面初始化
$(function() {
	allHeight()
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	//初始化表格
	jQuery("#insertProjCls_jqgrids").jqGrid({
		height: height,
		autoScroll: true,
		datatype: "local", //请求数据返回的类型。可选json,xml,txt
//		datatype: "json",
//		url: '../../assets/js/json/order.json',
		shrinkToFit: false,
		colNames: ['序号','项目编码','项目名称', '备注'], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'ordrNum',
				align: "center",
				editable: false,
				sortable: false,
				hidden:true
			},
			{
				name: 'projEncd',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'projNm',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'memo',
				align: "center",
				editable: true,
				sortable: false,
			}
		],
		rowList: [10, 20, 30], //可供用户选择一页显示多少条			
		autowidth: true,
		sortable:true,
		loadonce: true,
//		multiboxonly: true,
		cellsubmit: "clientArray",
		rownumbers: true,
		pager: '#insertProjCls_jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'storeId', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		ondblClickRow: function(rowid) {
			var gr = $("#insertProjCls_jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
			if(mType == 1){
				mType=1;
			}else{
				mType = 2;
				$('button').addClass("gray");
				$(".saveOrder").removeClass("gray");
				$(".upOrder").removeClass("gray");
				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true)
				$("#mengban").hide();
				$(".addOrder").attr('disabled', true)
				$('.addOrder').addClass("gray");
				$("#insertProjCls_jqgrids").setColProp("projEncd",{editable:false});
				$("#insertProjCls_jqgrids").editRow(gr,true);
			}
			grr(gr)
			$(".saveOrder").removeClass("gray");
			$(".saveOrder").attr('disabled', false)
		},
		multiselect: true, //复选框
		caption: "商品档案", //表格的标题名字	
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#insertProjCls_jqgrids").getGridParam('records'); //获取返回的记录数
			page = $("#insertProjCls_jqgrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#insertProjCls_jqgrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			search6()
		},

	});
	search6()
})

function IsCheckValue(projEncd, projNm) {
	if(projEncd == '') {
		alert("项目编码不能为空")
		return false;
	} else if(projNm == '') {
		alert("项目名称不能为空")
		return false;
	}
	return true;
}



$(function() {
	$(".saveOrder").click(function() {
		if(mType == 1) {
			addNewData(); //添加新数据
		} else if(mType == 2) {
			addEditData(); //编辑按钮		
		}
	})
})


function grr(gr) {
	$("#" + gr + "_templateId").on("dblclick", function() {
		
		$("#inquiry").removeClass("gray")
		$("#inquiry").attr("disabled", false)
		$("#purchaseOrder").hide()
		$("#muban").show()
		$("#muban").css("opacity", 1)
	})
}


//增行   保存
$(function() {

	$(".addOrder").click(function() {
		$('button').addClass("gray");
		$(".saveOrder").removeClass("gray");
		$(".save").removeClass("gray");
		$(".cancel").removeClass("gray");
		$(".find").removeClass("gray");
		$(".findList").removeClass("gray");
		$(".true").removeClass("gray");
		$(".false").removeClass("gray");
		$(".upOrder").removeClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)
		$("#mengban").hide();
		$(".addOrder").attr('disabled', true)
		$('.addOrder').addClass("gray");
		mType = 1;
		//拿到Gride中所有主键id的值
		var gr = $("#insertProjCls_jqgrids").jqGrid('getDataIDs');
		// 获得当前最大行号（数据编码）
		var rowid = Math.max.apply(Math, gr);
		// 获得新添加行的行号（数据编码）
		window.newrowid = rowid + 1;
		var dataRow = {
			"ordrNum": "",
			"projEncd": "",
			"projNm": "",
			"memo": ''

		};
		$("#insertProjCls_jqgrids").setColProp('storeId', {
			editable: true
		}); //设置editable属性由true改为false

		//将新添加的行插入到第一列
		$("#insertProjCls_jqgrids").jqGrid("addRowData", newrowid, dataRow, "first");
		
		//设置grid单元格可编辑
		$('#insertProjCls_jqgrids').jqGrid('editRow', newrowid, true);
		$("#insertProjCls_jqgrids").jqGrid('setSelection',newrowid);
		grr(newrowid)
		$("#" + newrowid + "_templateId").attr("readonly", "readonly");
	})

})
//添加新数据
function addNewData() {
	var projEncd = $("#" + newrowid + "_projEncd").val()
	var projNm = $("#" + newrowid + "_projNm").val()
	var memo = $("#" + newrowid + "_memo").val()
	if(IsCheckValue(projEncd, projNm) == true) {
		var save = {
			"reqHead": reqhead,
			"reqBody": {
				"projEncd": projEncd,
				"projNm": projNm,
				"memo": memo
			}
		}
		var saveJson = JSON.stringify(save);
		$.ajax({
			type: "post",
			url: url3 + "/mis/ec/projCls/insertProjCls",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(msgAdd) {
				alert(msgAdd.respHead.message)
				$('button').addClass("gray");
				$(".gray").attr('disabled', true)
				$(".saveOrder").attr('disabled', true)
				$(".addOrder").attr('disabled', false)
				$(".break").attr('disabled', false)
				$(".del").attr('disabled', false)
				$(".search").attr('disabled', false)
				$('.saveOrder').addClass("gray");
				$(".addOrder").removeClass("gray");
				$(".break").removeClass("gray");
				$(".del").removeClass("gray");
				$(".search").removeClass("gray");
				mType = 0;
				search6()
			},
			error: function(err) {
				alert("失败")
			}
		});
	}
}

//修改保存按钮
function addEditData() {
	var ids = $("#insertProjCls_jqgrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowDatas = $("#insertProjCls_jqgrids").jqGrid('getRowData', ids);
	var projEncd = rowDatas.projEncd
	var ordrNum = rowDatas.ordrNum
	var projNm = $("#" + ids + "_projNm").val()
	var memo = $("#" + ids + "_memo").val()
	if(IsCheckValue(projEncd, projNm) == true) {
		var save = {
			"reqHead": reqhead,
			"reqBody": {
				"projEncd": projEncd,
				"ordrNum": ordrNum,
				"projNm": projNm,
				"memo": memo
			}
		}
		var saveJson = JSON.stringify(save);
		$.ajax({
			type: "post",
			url: url3 + "/mis/ec/projCls/updateProjCls",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(msgAdd) {
				alert(msgAdd.respHead.message)
				$('button').addClass("gray");
				$(".gray").attr('disabled', true)
				$(".saveOrder").attr('disabled', true)
				$(".addOrder").attr('disabled', false)
				$(".break").attr('disabled', false)
				$(".del").attr('disabled', false)
				$(".search").attr('disabled', false)
				$('.saveOrder').addClass("gray");
				$(".addOrder").removeClass("gray");
				$(".break").removeClass("gray");
				$(".del").removeClass("gray");
				$(".search").removeClass("gray");
				mType = 0;
				search6()
			},
			error: function(err) {
				alert("失败")
			}
		});
	}
}
//
//查询按钮
$(document).on('click', '.search', function() {
	search6()
})

//条件查询
function search6() {
	var projEncd = $("input[name='projEncd']").val();
	var projNm = $("input[name='projNm']").val();
	
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var data2 = {
		reqHead,
		"reqBody": {
			"projEncd": projEncd,
			"projNm": projNm,
			"pageNo": page,
			"pageSize": rowNum
		}

	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url3 + "/mis/ec/projCls/queryList",
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
			$("#insertProjCls_jqgrids").jqGrid("clearGridData");
			$("#insertProjCls_jqgrids").jqGrid("setGridParam", {
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
			alert("error")
		}
	});

}

////删除行
$(function() {
	$(".del").click(function() {
		var num = []
		var gr = $("#insertProjCls_jqgrids").jqGrid('getGridParam', 'selarrrow'); //获取行id
		for(var i = 0;i < gr.length;i++) {
			var rowDatas = $("#insertProjCls_jqgrids").jqGrid('getRowData', gr[i]); //获取行数据    	
			var ordrNum = rowDatas.ordrNum
			num.push(ordrNum)
		}
		var deleteAjax = {
			reqHead,
			"reqBody": {
				"ordrNum": num.toString(),
			}
		};
		var deleteData = JSON.stringify(deleteAjax)
		if(gr == null) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url3 + "/mis/ec/projCls/delProjCls",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(remover) {
					alert(remover.respHead.message);
					search6()
				},
				error: function() {
					alert("删除失败")

				}
			});
		}
	})
})
