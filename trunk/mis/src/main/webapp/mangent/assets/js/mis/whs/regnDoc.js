var count;
var pages;
var page = 1;
var rowNum;
$(document).on('click', '#find', function() {
	search()
})

//查询按钮
function search() {
	var regnEncd = $("#regnEncd").val();
	var regnNm = $("#regnNm").val();

	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var myData = {};
	var Data = {
		"reqHead": reqhead,
		"reqBody": {
			"regnEncd": regnEncd,
			"regnNm": regnNm,
			"pageNo": page,
			"pageSize": rowNum
		}
	}
	var changeData = JSON.stringify(Data)
	$.ajax({
		type: "post",
		url: url + "/mis/whs/regn/queryList",
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
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#r_jqgrids").jqGrid("clearGridData");
			$("#r_jqgrids").jqGrid("setGridParam", {
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
	$("#r_jqgrids").jqGrid('clearGridData')

}
function IsCheckValue(regnEncd, regnNm) {
	if(regnEncd == '') {
		alert("区域名称不能为空")
		return false;
	} else if(regnNm == '') {
		alert("区域编码不能为空")
		return false;
	}
	return true;
}

//增行   保存
$(function() {
	$(".addOrder").click(function() {
		var newrowid;
		var selectedId = $("#r_jqgrids").jqGrid("getGridParam", "selrow");
		var ids = jQuery("#r_jqgrids").jqGrid('getDataIDs');

		//获得当前最大行号（数据编码）
		var rowid = Math.max.apply(Math, ids);
		//获得新添加行的行号（数据编码）
		newrowid = rowid + 1;
		var dataRow = {
			regnEncd: "",
			regnNm: "",
			siteQty: "",
			longs: "",
			wide: "",
			setupPers: "",
			mdfr: "",
			memo: ""
		};

		$("#r_jqgrids").setColProp('regnEncd', {
			editable: true
		}); //设置editable属性由true改为false
		//将新添加的行插入到第一列
		$("#r_jqgrids").jqGrid("addRowData", newrowid, dataRow, "first");
		//设置grid单元格可编辑
		$('#r_jqgrids').jqGrid('editRow', newrowid, false);
	})
})
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
	var regnEncd = $("input[name='regnEncd']").val();
	var regnNm = $("input[name='regnNm']").val();
	var longs = $("input[name='longs']").val();
	var wide = $("input[name='wide']").val();
	var siteQty = $("input[name='siteQty']").val();
	var memo = $("input[name='memo']").val();
	if(IsCheckValue(regnEncd, regnNm) == true) {
		var save = {
			reqHead,
			"reqBody": {
				"regnEncd": regnEncd,
				"regnNm": regnNm,
				"longs": longs,
				"wide": wide,
				"siteQty": siteQty,
				"memo": memo
			}
		}
		var saveJson = JSON.stringify(save);
		$.ajax({
			type: "post",
			url: url + "/mis/whs/regn/insertRegn",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(msgAdd) {
				alert(msgAdd.respHead.message)
				$("#r_jqgrids").trigger("reloadGrid"); 
				$("#searchAll").trigger('click');
				$('#jqgrids').css("visibility", "true");
				search()
			},
			error: function(err) {
				alert("失败")
			}
		});
	}
}
//删除行
$(function() {
	$(".delOrder").click(function() {
		var rowIds = $("#r_jqgrids").jqGrid('getGridParam', 'selrow'); 
		var rowData = $("#r_jqgrids").jqGrid('getRowData', rowIds);
        var regnEncd = rowData.regnEncd;
		var data = {
			reqHead,
			"reqBody": {
				"regnEncd": regnEncd,
			}
		};
		var deleteData = JSON.stringify(data)
		
		if(rowIds == null) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + "/mis/whs/regn/deleteRegnList",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(data) {
					alert(data.respHead.message);
					search()
				},
				error: function() {
					alert("删除失败")
				}
			});
		
		}
	})
})
var myData = {};
$(function() {
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	var Data = {
		"reqHead": reqhead,
		"reqBody": {
			"regnEncd": '',
			"regnNm": '',
			"pageNo": 1,
			"pageSize": 500
		}
	}
	var changeData = JSON.stringify(Data)
	$.ajax({
		type: "post",
		url: url + "/mis/whs/regn/queryList",
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
			var list = data.respBody.list;
			allHeight()
			//初始化右侧表格
			$("#r_jqgrids").jqGrid({
				datatype: "local", //请求数据返回的类型。可选json,xml,txt
				data: list,
				colNames: ['区域编码', '区域名称', '地堆数量', '长', '宽', '创建人',
					'修改人', '备注'
				],
				colModel: [
					{
						name: 'regnEncd',
						editable: true,
						align: 'center',
						sortable: false
					},
					{
						name: 'regnNm',
						editable: true,
						align: 'center',
					},
					{
						name: 'siteQty',
						editable: true,
						align: 'center',
					},
					{
						name: 'longs',
						editable: true,
						align: 'center',
					},
					{
						name: 'wide',
						editable: true,
						align: 'center',
					},
					{
						name: 'setupPers',
						editable: false,
						align: 'center',
		//				hidden: true,
					},
					{
						name: 'mdfr',
						editable: false,
						align: 'center',
		//				hidden: true,
					},
					{
						name: 'memo',
						editable: true,
						align: 'center',
					}
				],
				rowNum: 100,
				rowList: [100, 200, 300], //可供用户选择一页显示多少条
				autowidth: true,
				rownumbers: true,
				loadonce: false,
				height: height,
				autoScroll:true,
				multiselect: true, //复选框
				multiboxonly: true,
				shrinkToFit:false,
				forceFit: true,	
				cellsubmit: "clientArray",
				sortname: 'id', //初始化的时候排序的字段
				sortorder: "desc", //排序方式,可选desc,asc
				viewrecords: true,
				pager: '#r_jqGridPager', //表格页脚的占位符(一般是div)的id
				caption: "区域详情", //表格的标题名字,
				onPaging: function(pageBtn) { //翻页实现 
					var records = $("#r_jqgrids").getGridParam('records'); //获取返回的记录数
					page = $("#r_jqgrids").getGridParam('page'); //获取返回的当前页
					var rowNum1 = $("#r_jqgrids").getGridParam('rowNum');
		
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
					search();
				},
				ondblClickRow: function(){
					var gr = $("#r_jqgrids").jqGrid('getGridParam', 'selrow');//获取行id
					var rowDatas = $("#r_jqgrids").jqGrid('getRowData', gr);//获取行数据
					jQuery('#r_jqgrids').editRow(gr, true);
					$("#" + gr + "_regnEncd").attr("readonly", "readonly");
				}
			})
		},
		//结束加载动画
		complete: function() {
			$(".zhezhao").css("display", "none");
			$("#loader").css("display", "none");
		},
	})
	$("#r_jqgrids").setColProp("regnNm", { editable: true });//设置列可编辑
    $("#r_jqgrids").setColProp("siteQty", { editable: true });//设置列可编辑
	$("#r_jqgrids").setColProp("longs", { editable: true });//设置列可编辑
    $("#r_jqgrids").setColProp("wide", { editable: true });//设置列可编辑
	$("#r_jqgrids").setColProp("memo", { editable: true });//设置列可编辑

})

//更新
function SaveModifyData() {
	var regnEncd = $("input[name='regnEncd']").val();
	var regnNm = $("input[name='regnNm']").val();
	var longs = $("input[name='longs']").val();
	var wide = $("input[name='wide']").val();
	var siteQty = $("input[name='siteQty']").val();
	var memo = $("input[name='memo']").val();
	if(IsCheckValue(regnEncd, regnNm) == true) {
	
	   	var edit = {
	   		reqHead,
			"reqBody":{
				"regnEncd": regnEncd,
				"regnNm": regnNm,
				"longs": longs,
				"wide": wide,
				"siteQty": siteQty,
				"memo": memo,
			}
		}
   	    editJson = JSON.stringify(edit);
    	$.ajax({
    		type:"post",
			url: url + "/mis/whs/regn/updateRegn",
			async:true,
			data:editJson,
			dataType:'json',
			contentType: 'application/json',
			success:function(editMsg){
				alert(editMsg.respHead.message);
		    	search()
			},
			error:function(){
				alert("更新失败")
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
	            url: url + "/mis/whs/regn/uploadRegnFile",
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
	var regnEncd = $("#regnEncd").val();
	var regnNm = $("#regnNm").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"regnEncd": regnEncd,
			"regnNm": regnNm,
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url3 + "/mis/whs/regn/queryListDaYin",
		type: 'post',
		data: Data,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		success: function(data) {
			var list = data.respBody.list;
			var execlName = '区域档案'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
	
})
