var count;
var pages;
var page = 1;
var rowNum;
$(function() {
	$(".saveOrder").attr("disabled", true)
	$(".saveOrder").addClass("gray")
	$(".update").attr("disabled", true)
	$(".update").addClass("gray")
})
$(function() {
	//页面加载完成之后执行	
	var pageNo = 1;
	var rowNum = 10;
	pageInit7();
	//点击右边条数修改显示行数
	$(".ui-pg-selbox.ui-widget-content.ui-corner-all").click(function() {
		pageNo = $("#jqgrids").jqGrid("getGridParam", "page");
		rowNum = $("select[class='ui-pg-selbox ui-widget-content ui-corner-all']")[0].value >> 0;
		var data3 = {
			reqHead,
			"reqBody": {
				"pageSize": rowNum,
				"pageNo": pageNo
			}
		};
		var postD3 = JSON.stringify(data3);
		jQuery("#jqgrids").jqGrid({
			url: url3 + "/mis/ec/brokerage/queryList", //组件创建完成之后请求数据的url
			mtype: "post",
			datatype: "json", //请求数据返回的类型。可选json,xml,txt
			postData: postD3,
			ajaxGridOptions: {
				contentType: 'application/json; charset=utf-8'
			},
			colNames: ['佣金扣点编码', '佣金扣点名称', '备注'], //jqGrid的列显示名字
			colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
				{
					name: 'brokId',
					align: "center",
					index: 'invdate',
					editable: true,
				},
				{
					name: 'brokName',
					align: "center",
					index: 'id',
					editable: true,
				},
				{
					name: 'memo',
					align: "center",
					index: 'invdate',
					editable: true,
				},

			],
			rowList: [10, 20, 30], //可供用户选择一页显示多少条
			autowidth: true,
			pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
			sortname: 'brokId', //初始化的时候排序的字段
			sortorder: "desc", //排序方式,可选desc,asc
			viewrecords: true,
			rowNum: rowNum, //一页显示多少条
			pageNo: pageNo,
			jsonReader: {
				root: "respBody.list", // json中代表实际模型数据的入口
				records: "respBody.count", // json中代表数据行总数的数据		            
				total: "respBody.pages", // json中代表页码总数的数据
				repeatitems: true,
			},
			onPaging: function(pgButton) {
				pageNo = $("#jqgrids").jqGrid("getGridParam", "page");
				rowNum = $("select[class='ui-pg-selbox ui-widget-content ui-corner-all']")[0].value >> 0;
				if(pgButton === 'prev') {
					pageNo -= 1;
				} else if(pgButton === 'next') {
					pageNo += 1;

				} else if(pgButton === 'records') {
					pageNo = 1;
				}
			}
		});
	})
})

$(document).on('click', '#find', function() {
	search()
})

function search() {
	var expressEncd = $("input[name='expressEncd1']").val();
	var expressNm = $("input[name='expressNm1']").val();
	var expressTyp = $("input[name='expressTyp1']").val();
	var setupPers = $("input[name='setupPers1']").val();
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)

	var data2 = {
		reqHead,
		"reqBody": {
			"expressEncd": expressEncd,
			"expressNm": expressNm,
			"expressTyp": expressTyp,
			"setupPers": setupPers,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		url: url3 + "/mis/whs/express_crop/queryList",
		contentType: 'application/json; charset=utf-8',
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
};
//增行   保存
$(function() {
	$(".addOrder").click(function() {
		$(".saveOrder").attr("disabled", false)
		$(".saveOrder").removeClass("gray")
		var newrowid;
		var selectedId = $("#jqgrids").jqGrid("getGridParam", "selrow");
		var ids = jQuery("#jqgrids").jqGrid('getDataIDs');

		//获得当前最大行号（数据编码）
		var rowid = Math.max.apply(Math, ids);
		//获得新添加行的行号（数据编码）
		newrowid = rowid + 1;
		var dataRow = {
			brokId: "",
			brokName: '',
			memo: ''

		};
		$("#jqgrids").setColProp('expressEncd', {
			editable: true
		}); //设置editable属性由true改为false

		//将新添加的行插入到第一列
		$("#jqgrids").jqGrid("addRowData", newrowid, dataRow, "first");
		//设置grid单元格可编辑
		$('#jqgrids').jqGrid('editRow', newrowid, false);
		$("#" + newrowid + "_deliverPhone").attr("maxlength", '11');
		$("#" + newrowid + "_deliverMobile").attr("maxlength", '11');
		//			$("#" + newrowid + "_deliverPhone").attr("type", 'number')
		//	$(document).keyup(function(event){
		//			  if(event.keyCode ==13){
		//			    $(".saveOrder").trigger("click");
		//			  }
		//		});
	})

})

function IsCheckValue(expressEncd, expressNm, deliver, deliverPhone, companyCode, province, city, country, detailedAddress) {
	if(expressEncd == '') {
		alert("快递公司编码不能为空")
		return false;
	} else if(expressNm == '') {
		alert("快递公司名称不能为空")
		return false;
	} else if(deliver == '') {
		alert("发货人姓名不能为空")
		return false;
	} else if(deliverPhone == '') {
		alert("发货人手机号不能为空")
		return false;
	} else if(deliverPhone.length < 11) {
		alert("发货人手机号格式错误")
		return false;
	} else if(companyCode == 0) {
		alert("快递公司代码未选择")
		return false;
	} else if(province == '') {
		alert("发货地省不能为空")
		return false;
	} else if(city == '') {
		alert("发货地市不能为空")
		return false;
	} else if(country == '') {
		alert("发货地区不能为空")
		return false;
	} else if(detailedAddress == '') {
		alert("发货人详细信息不能为空")
		return false;
	}
	return true;
}

// 点击保存，传送数据给后台
var isclick = true;
$(function() {
	$(".saveOrder").click(function() {
		if(isclick) {
			isclick = false;
			SaveNewData();
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	})
	$(".update").click(function() {
		if(isclick) {
			isclick = false;
			SaveModifyData();
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	})
})
// 保存
function SaveNewData() {
	var expressEncd = $("input[name='expressEncd']").val();
	var expressNm = $("input[name='expressNm']").val();
	var expressTyp = $("input[name='expressTyp']").val();
	var isNtSprtGdsToPay = $("select[name='isNtSprtGdsToPay']").val();
	var gdsToPayServCost = $("input[name='gdsToPayServCost']").val();
	var isNtStpUse = $("select[name='isNtStpUse']").val();
	var firstWet = $("input[name='firstWet']").val();
	var firstWetStrPrice = $("input[name='firstWetStrPrice']").val();
	var memo = $("input[name='memo']").val();
	var deliver = $("input[name='deliver']").val();
	var deliverPhone = $("input[name='deliverPhone']").val();
	var companyCode = $("select[name='companyCode'] option:selected").val();
	var deliverMobile = $("input[name='deliverMobile']").val();
	var province = $("input[name='province']").val();
	var city = $("input[name='city']").val();
	var country = $("input[name='country']").val();
	var detailedAddress = $("input[name='detailedAddress']").val();
	if(IsCheckValue(expressEncd, expressNm, deliver, deliverPhone, companyCode, province, city, country, detailedAddress) == true) {
		var save = {
			reqHead,
			"reqBody": {
				"expressEncd": expressEncd,
				"expressNm": expressNm,
				"expressTyp": expressTyp,
				"isNtSprtGdsToPay": isNtSprtGdsToPay,
				"gdsToPayServCost": gdsToPayServCost,
				"isNtStpUse": isNtStpUse,
				"firstWet": firstWet,
				"firstWetStrPrice": firstWetStrPrice,
				"memo": memo,
				"deliver": deliver,
				"deliverPhone": deliverPhone,
				"companyCode": companyCode,
				"deliverMobile": deliverMobile,
				"province": province,
				"city": city,
				"country": country,
				"detailedAddress": detailedAddress,

			}
		}
		var saveJson = JSON.stringify(save);
		$.ajax({
			type: "post",
			url: url3 + "/mis/whs/express_crop/insertExpressCorp",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(msgAdd) {
				alert(msgAdd.respHead.message)
				search()
				$("#searchAll").trigger('click');
				$('#jqgrids').css("visibility", "true");
				$(".saveOrder").attr("disabled", true)
				$(".saveOrder").addClass("gray")
			},
			error: function(err) {
				alert("失败")
			}
		});
	}
}
// 更新
function SaveModifyData() {
	var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
	var rowDatas = $("#jqgrids").jqGrid('getRowData', gr); //获取行数据
	var expressEncd = rowDatas.expressEncd
	var expressNm = $("input[name='expressNm']").val();
	var expressTyp = $("input[name='expressTyp']").val();
	var isNtSprtGdsToPay = $("select[name='isNtSprtGdsToPay']").val();
	var gdsToPayServCost = $("input[name='gdsToPayServCost']").val();
	var isNtStpUse = $("select[name='isNtStpUse']").val();
	var firstWet = $("input[name='firstWet']").val();
	var firstWetStrPrice = $("input[name='firstWetStrPrice']").val();
	var printCnt = $("input[name='printCnt']").val();
	var memo = $("input[name='memo']").val();
	var mdfr = $("input[name='mdfr']").val();
	var deliver = $("input[name='deliver']").val();
	var deliverPhone = $("input[name='deliverPhone']").val();
	var companyCode = $("select[name='companyCode'] option:selected").val();
	var deliverMobile = $("input[name='deliverMobile']").val();
	var province = $("input[name='province']").val();
	var city = $("input[name='city']").val();
	var country = $("input[name='country']").val();
	var detailedAddress = $("input[name='detailedAddress']").val();
	var setupPers = rowDatas.setupPers;

	var edit = {
		reqHead,
		"reqBody": {
			"expressEncd": expressEncd,
			"expressNm": expressNm,
			"expressTyp": expressTyp,
			"isNtSprtGdsToPay": isNtSprtGdsToPay,
			"gdsToPayServCost": gdsToPayServCost,
			"isNtStpUse": isNtStpUse,
			"firstWet": firstWet,
			"firstWetStrPrice": firstWetStrPrice,
			"printCnt": printCnt,
			"setupPers": setupPers,
			"memo": memo,
			"deliver": deliver,
			"deliverPhone": deliverPhone,
			"companyCode": companyCode,
			"deliverMobile": deliverMobile,
			"province": province,
			"city": city,
			"country": country,
			"detailedAddress": detailedAddress,
		}
	}
	editJson = JSON.stringify(edit);
	$.ajax({
		type: "post",
		url: url3 + "/mis/whs/express_crop/updateExpressCorp",
		async: true,
		data: editJson,
		dataType: 'json',
		contentType: 'application/json',

		success: function(editMsg) {
			alert(editMsg.respHead.message);
			search()
			$(".update").attr("disabled", true)
			$(".update").addClass("gray")
		},
		error: function() {
			console.log("更新失败")
		}
	});
}

//删除行
$(function() {
	$(".delOrder").click(function() {
		var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
		var rowDatas = $("#jqgrids").jqGrid('getRowData', gr); //获取行数据    	

		var deleteAjax = {
			reqHead,
			"reqBody": {
				"expressEncd": rowDatas.expressEncd,
			}
		};
		var deleteData = JSON.stringify(deleteAjax)
		if(gr == null) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url3 + "/mis/whs/express_crop/deleteExpressCorp",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(remover) {
					alert(remover.respHead.message);
					window.location.reload();
					$("#searchAll").trigger('click')
					$('#jqgrids').css("visibility", "true");
				},
				error: function() {
					console.log("删除失败")

				}
			});

		}
	})
})

//快递公司代码下拉框
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
		url: url3 + "/mis/whs/express_crop/queryExpressCodeAndNameList",
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
					ecNum += result[i].expressCode + ":" + result[i].expressName + ";";
					let aaa = result[i].expressCode;
					let bbb = result[i].expressName
				} else {
					ecNum += result[i].expressCode + ":" + result[i].expressName;
					let aaa = result[i].expressCode;
					let bbb = result[i].expressName
				}
				//				}
			}
		}
	});
	return ecNum; //必须有此返回值	
}

function pageInit7() {
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	allHeight()
	//初始化表格
	jQuery("#jqgrids").jqGrid({
		height: 330,
		datatype: "local", //请求数据返回的类型。可选json,xml,txt
		ajaxGridOptions: {
			contentType: 'application/json; charset=utf-8'
		},
		colNames: ['快递公司编码', '快递公司名称', '快递类型', '是否支持到货付款', '货到付款服务费', '是否停用', '首重', '首重起价', '发货人姓名', '发货人手机', '发货人座机', '快递公司代码', '发货地省', '发货地市', '发货地区', '发货人详细地址', '备注', '创建人', '修改人'],
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'expressEncd',
				align: "center",
				index: 'invdate',
				editable: true,
			},
			{
				name: 'expressNm',
				align: "center",
				index: 'id',
				editable: true,
			},
			{
				name: 'expressTyp',
				align: "center",
				index: 'invdate',
				editable: true,
				hidden:true
			},
			{
				name: 'isNtSprtGdsToPay',
				align: "center",
				index: 'invdate',
				editable: true,
				edittype: 'select',
				formatter: 'select',
				editoptions: {
					value: "0:否;1:是"
				},
				hidden:true
			},
			{
				name: 'gdsToPayServCost',
				align: "center",
				index: 'invdate',
				editable: true,
				hidden:true
			},
			{
				name: 'isNtStpUse',
				align: "center",
				index: 'invdate',
				editable: true,
				edittype: 'select',
				formatter: 'select',
				editoptions: {
					value: "0:否;1:是"
				},
			},
			{
				name: 'firstWet',
				align: "center",
				index: 'invdate',
				editable: true,
			},
			{
				name: 'firstWetStrPrice',
				align: "center",
				index: 'invdate',
				editable: true,
			},

			{
				name: 'deliver',
				align: "center",
				index: 'invdate',
				editable: true,
			},
			{
				name: 'deliverPhone',
				align: "center",
				index: 'invdate',
				editable: true,
			},
			{
				name: 'deliverMobile',
				align: "center",
				index: 'invdate',
				editable: true,
			},
			{
				name: 'companyCode',
				align: "center",
				index: 'invdate',
				editable: true,
				edittype: 'select',
				editoptions: {
					value: getEcNum()
				}
			},
			{
				name: 'province',
				align: "center",
				index: 'invdate',
				editable: true,
			},
			{
				name: 'city',
				align: "center",
				index: 'invdate',
				editable: true,
			},
			{
				name: 'country',
				align: "center",
				index: 'invdate',
				editable: true,
			},
			{
				name: 'detailedAddress',
				align: "center",
				index: 'invdate',
				editable: true,
			},

			{
				name: 'memo',
				align: "center",
				index: 'invdate',
				editable: true,
			},
			{
				name: 'setupPers',
				align: "center",
				index: 'invdate',
				editable: false,
			},
			{
				name: 'mdfr',
				align: "center",
				index: 'invdate',
				editable: false,
			},

		],
		gridComplete: function() {   
			//					$("#jqgrids").hideCol("setupPers");         
			           },
		rowNum: 100, //一页显示多少条
		rowList: [100, 200, 300], //可供用户选择一页显示多少条			
		autowidth: true,
		rownumbers: true,
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'expressEncd', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		height: height,
		autoScroll: true,
		shrinkToFit: false,
		jsonReader: {
			records: "respBody.count", // json中代表数据行总数的数据	
			root: "respBody.list", // json中代表实际模型数据的入口
			total: "respBody.pages", // json中代表页码总数的数据
			repeatitems: true

		},

		caption: "快递公司列表查询", //表格的标题名字	
		multiselect: true, //复选框
		multiboxonly: true,
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
		ondblClickRow: function() {

			$(".update").attr("disabled", false)
			$(".update").removeClass("gray")
			var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
			var rowDatas = $("#jqgrids").jqGrid('getRowData', gr); //获取行数据
			$("#" + gr + "_deliverPhone").attr("maxlength", '11')
			$("#" + gr + "_deliverMobile").attr("maxlength", '11')
			$("#jqgrids").setColProp("expressEncd", {
				editable: false
			});
			$("#jqgrids").editRow(gr, true);
		}
	});
	search()
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
				url: url + "/mis/whs/express_crop/uploadExpressCorpFile",
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

var arr = [];
var obj = {}
//导出
$(document).on('click', '.exportExcel', function() {
	var expressEncd = $("input[name='expressEncd1']").val();
	var expressNm = $("input[name='expressNm1']").val();
	var expressTyp = $("input[name='expressTyp1']").val();
	var setupPers = $("input[name='setupPers1']").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"expressEncd": expressEncd,
			"expressNm": expressNm,
			"expressTyp": expressTyp,
			"setupPers": setupPers,
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url3 + "/mis/whs/express_crop/queryListDaYin",
		type: 'post',
		data: Data,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		success: function(data) {
			var list = data.respBody.list;
			var execlName = '快递公司档案'
			ExportData(list, execlName)
		},
		error: function() {
			console.log(error)
		}
	})

})