var count;
var pages;
var page = 1;
var rowNum;
var isclick = true;
$(function() {
	//页面加载完成之后执行	
	var pageNo = 1;
	var rowNum = 10;
	pageInit();
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
			url: url3 + "/mis/whs/gd_flow_crop/queryList", //组件创建完成之后请求数据的url
			mtype: "post",
			datatype: "json", //请求数据返回的类型。可选json,xml,txt
			postData: postD3,
			ajaxGridOptions: {
				contentType: 'application/json; charset=utf-8'
			},

			rowList: [10, 20, 30], //可供用户选择一页显示多少条
			autowidth: true,
			pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
			sortname: 'gdFlowEncd', //初始化的时候排序的字段
			sortorder: "desc", //排序方式,可选desc,asc
			viewrecords: true,
			rowNum: rowNum, //一页显示多少条
			pageNo: pageNo,
			//			lastpage:1,
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
//查询按钮
$(document).on('click', '#find', function() {
	search6()
})

//条件查询
function search6() {
	var gdFlowEncd = $("input[name='gdFlowEncd1']").val();
	var gdFlowNm = $("input[name='gdFlowNm1']").val();
	var traffMode = $("input[name='traffMode1']").val();
	var traffVehic = $("input[name='traffVehic1']").val();
	var printCnt = $("input[name='printCnt1']").val();
	var memo = $("input[name='memo1']").val();
	var setupPers = $("input[name='setupPers1']").val();
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)

	var data2 = {
		reqHead,
		"reqBody": {
			"gdFlowEncd": gdFlowEncd,
			"gdFlowNm": gdFlowNm,
			"traffMode": traffMode,
			"traffVehic": traffVehic,
			"printCnt": printCnt,
			"memo": memo,
			"setupPers": setupPers,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url3 + "/mis/whs/gd_flow_crop/queryList",
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
}

function IsCheckValue(gdFlowEncd,gdFlowNm) {
	if(gdFlowEncd == '') {
		alert("物流公司编码不能为空")
		return false;
	} else if(gdsBitNm == '') {
		alert("物流公司名称不能为空")
		return false;
	}
	return true;
}

//增行   保存
$(function() {
	$(".addOrder").click(function() {
		var newrowid;
		var selectedId = $("#jqgrids").jqGrid("getGridParam", "selrow");
		var ids = jQuery("#jqgrids").jqGrid('getDataIDs');

		//获得当前最大行号（数据编码）
		var rowid = Math.max.apply(Math, ids);
		//获得新添加行的行号（数据编码）
		newrowid = rowid + 1;
		var dataRow = {
			gdFlowEncd: "",

		};
		//*********************************************

		$("#jqgrids").setColProp('gdFlowEncd', {
			editable: true
		}); //设置editable属性由true改为false
		//*********************************************
		//将新添加的行插入到第一列
		$("#jqgrids").jqGrid("addRowData", newrowid, dataRow, "first");
		//设置grid单元格可编辑
		$('#jqgrids').jqGrid('editRow', newrowid, false);
	})
	$(".saveOrder").click(function() {
		if(isclick) {
			isclick = false;
			SaveNewData();
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	})
})
function SaveNewData() {
	var gdFlowEncd = $("input[name='gdFlowEncd']").val();
	var gdFlowNm = $("input[name='gdFlowNm']").val();
	var traffMode = $("input[name='traffMode']").val();
	var traffVehic = $("input[name='traffVehic']").val();
	var printCnt = $("input[name='printCnt']").val();
	var memo = $("input[name='memo']").val();
	if(IsCheckValue(gdFlowEncd,gdFlowNm) == true) {
		var save = {
			reqHead,
			"reqBody": {
				"gdFlowEncd": gdFlowEncd,
				"gdFlowNm": gdFlowNm,
				"traffMode": traffMode,
				"traffVehic": traffVehic,
				"printCnt": printCnt,
				"memo": memo,
			}
		}
		var saveJson = JSON.stringify(save);
		$.ajax({
			type: "post",
			url: url3 + "/mis/whs/gd_flow_crop/insertGdFlowCorp",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(msgAdd) {
				alert(msgAdd.respHead.message)
				window.location.reload();
				$("#searchAll").trigger('click');
				$('#jqgrids').css("visibility", "true");
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
		var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
		var rowDatas = $("#jqgrids").jqGrid('getRowData', gr); //获取行数据    	

		var deleteAjax = {
			reqHead,
			"reqBody": {
				"gdFlowEncd": rowDatas.gdFlowEncd,
			}
		};
		var deleteData = JSON.stringify(deleteAjax)
		if(gr == null) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url3 + "/mis/whs/gd_flow_crop/deleteGdFlowCorp",
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


function pageInit() {
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	allHeight()
	//初始化表格
	jQuery("#jqgrids").jqGrid({
		datatype: "local", //请求数据返回的类型。可选json,xml,txt
		colNames: ['物流公司编码', '物流公司名称', '运输方式', '运输车辆', '备注', '创建人', '修改人'],
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'gdFlowEncd',
				align: "center",
				index: 'invdate',
				editable: true,
			},
			{
				name: 'gdFlowNm',
				align: "center",
				index: 'id',
				editable: true,
			},
			{
				name: 'traffMode',
				align: "center",
				index: 'invdate',
				editable: true,
			},
			{
				name: 'traffVehic',
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
		rowNum: 10, //一页显示多少条
		rowList: [10, 20, 30], //可供用户选择一页显示多少条			
		autowidth: true,
		height: height,
		autoScroll: true,
		shrinkToFit: false,
		multiselect: true, //复选框
		multiboxonly: true,
		rownumbers: true,
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'gdFlowEncd', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		jsonReader: {
			records: "respBody.count", // json中代表数据行总数的数据	
			root: "respBody.list", // json中代表实际模型数据的入口
			total: "respBody.pages", // json中代表页码总数的数据
			repeatitems: true

		},

		caption: "物流公司列表查询", //表格的标题名字	
		
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
			search6()
		},
		ondblClickRow: function() {
			$('.saveOrder')[0].disabled = true;
			$('.saveOrder').addClass("gray");
			var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
			var rowDatas = $("#jqgrids").jqGrid('getRowData', gr); //获取行数据
			$("#jqgrids").setColProp("gdFlowEncd",{editable:false});
			$("#jqgrids").editRow(gr,true);
			//点击更新按钮
			$(".update").click(function() {
				if(isclick) {
					isclick = false;
					SaveModifyData();
					setTimeout(function() {
						isclick = true;
					}, 1000);
				}
			})

		}

	});
	search6()
	$(function() {
		$(window).resize(function() {
			$("#jqgrids").setGridWidth($(window).width());
		});
	});
}
function SaveModifyData() {
	var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
	var rowDatas = $("#jqgrids").jqGrid('getRowData', gr); //获取行数据
	var gdFlowEncd = rowDatas.gdFlowEncd;
	var gdFlowNm = $("input[name='gdFlowNm']").val();
	var traffMode = $("input[name='traffMode']").val();
	var traffVehic = $("input[name='traffVehic']").val();
	var printCnt = $("input[name='printCnt']").val();
	var memo = $("input[name='memo']").val();
	var setupPers = rowDatas.setupPers;
	if(IsCheckValue(gdFlowEncd,gdFlowNm) == true) {

		var edit = {
			reqHead,
			"reqBody": {
				"gdFlowEncd": gdFlowEncd,
				"gdFlowNm": gdFlowNm,
				"traffMode": traffMode,
				"traffVehic": traffVehic,
				"printCnt": printCnt,
				"memo": memo,
				"setupPers": setupPers,
			}
		}
		editJson = JSON.stringify(edit);

		$.ajax({
			type: "post",
			url: url3 + "/mis/whs/gd_flow_crop/updateGdFlowCorp",
			async: true,
			data: editJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(editMsg) {
				alert(editMsg.respHead.message);
				window.location.reload();
			},
			error: function() {
				console.log("更新失败")
			}
		});
	}
}

//导入
$(function () {
    $(".importExcel").click(function () {
    	var files = $("#FileUpload").val()
    	var fileObj = document.getElementById("FileUpload").files[0];
        var formFile = new FormData();
       	formFile.append("action", "UploadVMKImagePath");  
       	formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
        var data = formFile;
        if(files != "") {
        	$.ajax({
	            type: 'post',
	            url: url + "/mis/whs/gd_flow_crop/uploadGdFlowCropFile",
	            data:data,
	          	dataType: "json",
	           	cache: false,//上传文件无需缓存
	           	processData: false,//用于对data参数进行序列化处理 这里必须false
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
$(document).on('click', '.exportExcel', function() {
	var gdFlowEncd = $("input[name='gdFlowEncd1']").val();
	var gdFlowNm = $("input[name='gdFlowNm1']").val();
	var traffMode = $("input[name='traffMode1']").val();
	var traffVehic = $("input[name='traffVehic1']").val();
	var printCnt = $("input[name='printCnt1']").val();    
	var memo = $("input[name='memo1']").val();
	var setupPers = $("input[name='setupPers1']").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"gdFlowEncd" : gdFlowEncd,
			"gdFlowNm":gdFlowNm,
			"traffMode" : traffMode,
			"traffVehic" : traffVehic,
			"printCnt" : printCnt,
			"memo" : memo,
			"setupPers" : setupPers
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url3 + "/mis/whs/gd_flow_crop/queryListDaYin",
		type: 'post',
		data: Data,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		success: function(data) {
			var list = data.respBody.list;
			var execlName = '物流公司档案'
			ExportData(list, execlName)
		},
		error: function() {
			console.log(error)
		}
	})
	
})