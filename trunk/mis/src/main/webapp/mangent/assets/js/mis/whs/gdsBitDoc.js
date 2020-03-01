$(function() {
	//点击表格图标显示仓库列表
	$(document).on('click', '.biao_whsEncd', function() {
		window.open("../../Components/baseDoc/whsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	//点击表格图标显示存货分类列表
	$(document).on('click', '.biao_regnEncd', function() {
		window.open("../../Components/whs/regnList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})

var count;
var pages;
var page = 1;
var rowNum;

$(function() {
	//页面加载完成之后执行
	$('.saveOne').addClass("gray") //增加
	$('.saveOne').attr('disabled', true);
	$('.upOne').addClass("gray") //更新
	$('.upOne').attr('disabled', true);
});

//查询按钮
$(document).on('click', '#finds', function() {
	search3()
})

function search3() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var gdsBitEncd = $("input[name='gdsBitEncd']").val()
	var Data = {
		'reqHead': reqhead,
		"reqBody": {
			"gdsBitEncd": gdsBitEncd,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var changeData = JSON.stringify(Data)
	$.ajax({
		type: "post",
		url: url + "/mis/whs/gds_bit/queryList",
		async: true,
		data: changeData,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		//开始加载动画  添加到ajax里面
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");

		},
		success: function(data) {
			var mydata = {};
			mydata.rows = data.respBody.list;
			var list = mydata.rows
			for(var i =0;i<list.length;i++) {
				if(list[i].gdsBitTyp == 1) {
					list[i].gdsBitTyp = '货架'
				} else if(list[i].gdsBitTyp == 2) {
					list[i].gdsBitTyp = '地堆'
				}
			}
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
		//结束加载动画
		complete: function() {
			$(".zhezhao").css("display", "none");
			$("#loader").css("display", "none");
		},
		error: function() {
			alert("条件查询失败")
		}
	});
}

//增行   保存
$(function() {
	$(".addOne").click(function() {
		$('.upOne').addClass("gray") //更新
		$('.upOne').attr('disabled', true);
		$('.addOne').addClass("gray") //增加
		$('.upOne').addClass("gray") //增加
		$('.addOne').attr('disabled', true);
		$('.saveOne').removeClass("gray") //保存
		$('.saveOne').attr('disabled', false);
		var selectedId = $("#jqGrids").jqGrid("getGridParam", "selrow");
		var ids = $("#jqGrids").jqGrid('getDataIDs');

		// 获得当前最大行号（数据编码）
		var rowid = Math.max.apply(Math, ids);
		// 获得新添加行的行号（数据编码）
		newrowid = rowid + 1;

		//获得行数据

		var dataRow = {
			"gdsBitEncd": "",
			"regnEncd": "",
			"gdsBitNm": "",
			"gdsBitTyp": "",
			"gdsBitCd": "",
			"gdsBitQty": "",
			"memo": "",
			"gdsBitCdEncd": "",
			"setupPers": "",
			"mdfr": ""
		};
		$("#jqGrids").setColProp('storeId', {
			editable: true
		}); //设置editable属性由true改为false
		//将新添加的行插入到第一列
		$("#jqGrids").jqGrid("addRowData", newrowid, dataRow, "first");
		//设置grid单元格可编辑
//		$('#jqGrids').jqGrid('editRow', newrowid, false);
		$("#jqGrids").editRow(newrowid,true);
		//		双击区域跳转区域档案
		$("#" + newrowid + "_regnEncd").on("dblclick", function() {
			$(".box").css("opacity", 1)
			$(".box").show()
			$(".gdsBit_box").hide()
			$("#whsDocList").hide()
		})
//		$(document).keyup(function(event) {
//			if(event.keyCode == 13) {
////				$(".saveOne").trigger("click");
//				console.log(1)
//			}
//		});
		$("#" + newrowid + "_regnEncd").attr("readonly", "readonly");
		// 只能输入数字
		$("#" + newrowid + "_gdsBitQty").attr("type", "number");
//		$("#" + newrowid + "_gdsBitQty").attr("onafterpaste", "if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}");
		// 不能输入中文
		$("#" + newrowid + "_gdsBitCd").attr("onkeyup", "value=value.replace(/[\u4e00-\u9fa5]/ig,'')");
	})
})

//打开店铺档案后点击确定取消
$(function() {
	//确定
	$(".truew").click(function() {
		//获得行号
		var rowid = $("#jqGrids").jqGrid('getGridParam', 'selrow');

		//	获得行号
		var gr = $("#r_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDatas = $("#r_jqgrids").jqGrid('getRowData', gr);
		if(gr == null) {
			alert("未选择")
		} else {
			$("input[name='regnEncd']").val(rowDatas.regnEncd)
			$("#jqGrids").setRowData(newrowid, {
				regnNm: rowDatas.regnNm,
				whsEncd: rowDatas.whsEncd,
				whsNm: rowDatas.whsNm,
			})
//			$("input[name='regnNm']").val(rowDatas.regnNm)
//			$("input[name='whsEncd']").val(rowDatas.whsEncd)
//			$("input[name='whsNm']").val(rowDatas.whsNm)

			$(".box").css("opacity", 0);
			$(".box").hide();
			$(".gdsBit_box").show();
		}
	})
	//	取消
	$(".falsew").click(function() {
		$(".box").css("opacity", 0);
		$(".box").hide();
		$(".gdsBit_box").show();
		$("input[name='regnEncd']").val('')
	})
})

//删除行
$(function() {
	$(".delOrders").click(function() {
		var rowIds = $("#jqGrids").jqGrid('getGridParam', 'selrow');
		var allData = $("#jqGrids").jqGrid('getRowData', rowIds);
		var gdsBitEncd = allData.gdsBitEncd
		var whsEncd = allData.whsEncd
		var regnEncd = allData.regnEncd
		var data = {
			reqHead,
			"reqBody": {
				"gdsBitEncd": gdsBitEncd,
				"whsEncd": whsEncd,
				"regnEncd": regnEncd,
			}
		};
		var deleteData = JSON.stringify(data)
		if(rowIds == null) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + "/mis/whs/gds_bit/deleteGdsBitList",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(data) {
					alert(data.respHead.message);
					search3()
					$('.addOne').removeClass("gray")
					$('button').attr('disabled', false);
					$(".gray").attr('disabled', false);
				},
				error: function() {
					alert("删除失败")
				}
			});
		}
	})
})

//货位档案下拉框
function getEcNum() {
	var ecNum = "";
	var data2 = {
		reqHead,
		"reqBody": {
			"pageNo": 1,
			"pageSize": 500

		}
	}
	var postD2 = JSON.stringify(data2);
	$.ajax({
		url: url + "/mis/whs/gds_bit/selectgTypList",
		type: "post",
		async: false,
		dataType: 'json', //请求数据返回的类型。可选json,xml,txt
		data: postD2,
		contentType: 'application/json',
		success: function(e) {
			var result = e.respBody.list;
			ecNum += "0" + ":" + "请选择" + ";";
			for(var i = 0; i < result.length; i++) {
//				if(result.length == 0) {
//					ecNum += "9" + ":" + "无货位类型" + ";";
//				} else {
					if(i != result.length - 1) {
						ecNum += result[i].gdsBitTypEncd + ":" + result[i].gdsBitTypNm + ";";
						let aaa = result[i].gdsBitTypEncd;
						let bbb = result[i].gdsBitTypNm
					} else {
						ecNum += result[i].gdsBitTypEncd + ":" + result[i].gdsBitTypNm;
						let aaa = result[i].gdsBitTypEncd;
						let bbb = result[i].gdsBitTypNm
					}
//				}
			}
		}
	});
	return ecNum; //必须有此返回值	
}

//页面初始化
$(function() {
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	allHeight()
	jQuery("#jqGrids").jqGrid({
		datatype: "local", //请求数据返回的类型。可选json,xml,txt
		shrinkToFit: false,
		colNames: ['仓库编码', '仓库名称','区域编码', '区域名称','货位编码', '货位名称', '货位类型', '货位代码', '货位存放量',
			'创建人', '修改人', '备注'
		],
		colModel: [{
				name: 'whsEncd',
				editable: false,
				align: 'center',
				sortable: false,
				hidden:true
			},
			{
				name: 'whsNm',
				editable: false,
				align: 'center',
				sortable: false,
				hidden:true
			},
			{
				name: 'regnEncd',
				editable: true,
				align: 'center',
				sortable: false,
				hidden:true
			},
			{
				name: 'regnNm',
				editable: false,
				align: 'center',
				sortable: false,
				hidden:true
			},
			{
				name: 'gdsBitEncd',
				editable: true,
				align: 'center',
			},
			{
				name: 'gdsBitNm',
				editable: true,
				align: 'center',
			},
			{
				name: 'gdsBitTyp', //货位类型下拉框
				editable: true,
				align: 'center',
				id: 'gdsBitTyp',
				index: 'id',
				edittype: 'select',
				editoptions: {
					value: getEcNum()
				}
			},
			{
				name: 'gdsBitCd',
				editable: true,
				align: 'center',
			},
			{
				name: 'gdsBitQty',
				editable: true,
				align: 'center',
			},
			{
				name: 'setupPers',
				editable: false,
				align: 'center',
			},
			{
				name: 'mdfr',
				editable: false,
				align: 'center',
			},
			{
				name: 'memo',
				editable: true,
				align: 'center',
			}
		],
		autowidth: true,
		rownumbers: true,
		loadonce: true,
		height: height,
		forceFit: true,
		rowNum: 500,
		scrollrows: true,
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
//		editurl:"clientArray", // 行提交
		viewrecords: true,
		multiselect: true, //复选框
		multiboxonly: true,
		pager: '#jqgridPager', //表格页脚的占位符(一般是div)的id
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
			search3()
		},
		ondblClickRow: function() {
			var gr = $("#jqGrids").jqGrid('getGridParam', 'selrow'); //获取行id
////			var rowDatas = $("#jqGrids").jqGrid('getRowData', gr); //获取行数据
////			jQuery('#jqGrids').editRow(gr, true);
//			
			$("#jqGrids").setColProp("whsEncd",{editable:false});
			$("#jqGrids").setColProp("regnEncd",{editable:false});
			$("#jqGrids").setColProp("whsNm",{editable:false});
			$("#jqGrids").setColProp("regnNm",{editable:false});
			$("#jqGrids").setColProp("gdsBitEncd",{editable:false});
			$("#jqGrids").editRow(gr,true);
			//获得行号
			var gr = $("#jqGrids").jqGrid('getGridParam', 'selrow');
			
			$("#" + gr + "_regnEncd").attr("readonly", "readonly");
			$("#" + gr + "_whsEncd").attr("readonly", "readonly");
			$("#" + gr + "_gdsBitEncd").attr("readonly", "readonly");
			$('.upOne').removeClass("gray") //更新
			$('.upOne').attr('disabled', false);
		},
		gridComplete: function() {                  
			$("#f_jqGrids").hideCol("ecId");  
			//					$("#f_jqGrids").hideCol("noAuditId");   
			$("#f_jqGrids").hideCol("brokId");            
		},
		caption: "货位档案", //表格的标题名字
	});
	search3()
})

function IsCheckValue(gdsBitEncd,gdsBitNm,gdsBitTyp, gdsBitQty, gdsBitCdEncd) {
	if(gdsBitEncd == '') {
		alert("货位编码不能为空")
		return false;
	} else if(gdsBitNm == '') {
		alert("货位名称不能为空")
		return false;
	} else if(gdsBitTyp == 0) {
		alert("货位类型未选择")
		return false;
	} else if(gdsBitQty == '') {
		alert("货位存放量不能为空")
		return false;
	} else if(gdsBitCdEncd == '') {
		alert("货位编码不能为空")
		return false;
	}
	return true;
}
// 点击保存，传送数据给后台
var isclick = true;
$(function() {
	$(".saveOne").click(function() {
		if(isclick) {
			isclick = false;
			SaveNewData();
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	})
	$(".upOne").click(function() {
		if(isclick) {
			isclick = false;
			SaveModifyData();
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	})
})

function SaveNewData() {
	var selectedId = $("#jqGrids").jqGrid("getGridParam", "selrow");
	var ids = $("#jqGrids").jqGrid('getDataIDs');
	//获得行数据
	var rowDatas = $("#jqGrids").jqGrid('getRowData', selectedId);

	// 获得当前最大行号（数据编码）
	var rowid = Math.max.apply(Math, ids);
	var gdsBitTyp = $("#" + rowid + "_gdsBitTyp option:selected").val();
	var gdsBitTypName = $("#" + rowid + "_gdsBitTyp option:selected").text();

	var gdsBitEncd = $("#" + rowid + "_gdsBitEncd").val();
	var regnEncd = $("#" + rowid + "_regnEncd").val();
	var gdsBitNm = $("#" + rowid + "_gdsBitNm").val();

	var gdsBitCd = $("input[name='gdsBitCd']").val();
	var gdsBitQty = $("input[name='gdsBitQty']").val();
	var whsEncd = rowDatas.whsEncd;
	var memo = $("input[name='memo']").val();
	var gdsBitCdEncd = $("input[name='gdsBitCdEncd']").val();
	//		var setupPers = .val();  // 登录人暂定
	var mdfr = $("input[name='mdfr']").val();
	if(IsCheckValue(gdsBitEncd,gdsBitNm,gdsBitTyp, gdsBitQty, gdsBitCdEncd) == true) {
		var save = {
			reqHead,
			"reqBody": {
				"gdsBitEncd": gdsBitEncd,
				"regnEncd": regnEncd,
				"gdsBitNm": gdsBitNm,
				"gdsBitTyp": gdsBitTyp,
				"whsEncd": whsEncd,
				"gdsBitCd": gdsBitCd,
				"gdsBitQty": gdsBitQty,
				"memo": memo,
				"gdsBitCdEncd": gdsBitCdEncd,
				"mdfr": mdfr
			}
		}
		var saveJson = JSON.stringify(save);
		$.ajax({
			type: "post",
			url: url + "/mis/whs/gds_bit/insertGdsBit",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(data) {
				console.log(data)
				alert(data.respHead.message)
				$('.addOne').removeClass("gray") //增加
				$('.addOne').attr('disabled', false);
				$('.upOne').addClass("gray") //更新
				$('.upOne').attr('disabled', true);
				$('.saveOne').addClass("gray") //更新
				$('.saveOne').attr('disabled', true);
				search3()
			},
			error: function(err) {
				alert("失败")
			}
		});
	}
}

function SaveModifyData() {
	//获得行号
	var gr = $("#jqGrids").jqGrid('getGridParam', 'selrow');

	//获得行数据
	var rowDatas = $("#jqGrids").jqGrid('getRowData', gr);

	var gdsBitTyp = $("#" + gr + "_gdsBitTyp option:selected").val();
	var gdsBitTypName = $("#" + gr + "_gdsBitTyp option:selected").text();

	var regnEncd = rowDatas.regnEncd
	var regnNm = rowDatas.regnNm
	var whsEncd = rowDatas.whsEncd
	var whsNm = rowDatas.whsNm
	var gdsBitEncd = rowDatas.gdsBitEncd
	var gdsBitNm = $("#" + gr + "_gdsBitNm").val();

	var gdsBitCd = $("input[name='gdsBitCd']").val();
	var gdsBitQty = $("input[name='gdsBitQty']").val();
	var memo = $("input[name='memo']").val();
	var gdsBitCdEncd = $("input[name='gdsBitCdEncd']").val();
	var setupPers = rowDatas.setupPers;
	if(IsCheckValue(gdsBitEncd,gdsBitNm,gdsBitTyp, gdsBitQty, gdsBitCdEncd) == true) {
		var save = {
			reqHead,
			"reqBody": {
				"gdsBitEncd": gdsBitEncd,
				"regnEncd": regnEncd,
				"setupPers": setupPers,
				"whsEncd": whsEncd,
				"gdsBitNm": gdsBitNm,
				"gdsBitTyp": gdsBitTyp,
				"gdsBitCd": gdsBitCd,
				"gdsBitQty": gdsBitQty,
				"memo": memo,
				"gdsBitCdEncd": gdsBitCdEncd,
			}
		}
		var saveJson = JSON.stringify(save);
		$.ajax({
			type: "post",
			url: url + "/mis/whs/gds_bit/updateGdsBit",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(data) {
				$('.upOne').addClass("gray") //更新
				$('.upOne').attr('disabled', true);
				alert(data.respHead.message);
				search3()
			},
			error: function() {
				alert("更新失败")
			}
		});
	}
}

//导入
$(function() {
	$(".importExcel").click(function() {
		var files = $("#FileUpload").val()
		var fileObj = document.getElementById("FileUpload").files[0];
		var formFile = new FormData();
		formFile.append("action", "UploadVMKImagePath");
		formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
		var data = formFile;
		if(files != "") {
			$.ajax({
				type: 'post',
				url: url + "/mis/whs/gds_bit/uploadGdsBitFile",
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
	});
});


var arr=[];
var obj={}
//导出
$(document).on('click', '.exportExcels', function() {
	var gdsBitEncd = $("input[name='gdsBitEncd']").val()
	var regnEncd = $("input[name='regnEncd1']").val()
	var whsEncd = $("input[name='whsEncd1']").val()
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"gdsBitEncd": gdsBitEncd,
			"regnEncd": regnEncd,
			"whsEncd": whsEncd,
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + "/mis/whs/gds_bit/queryListDaYin",
		type: 'post',
		data: Data,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		success: function(data) {
			var list = data.respBody.list;
			var execlName = '货位档案'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
	
})