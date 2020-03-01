//刚开始时可点击的按钮
$(function() {
	$('button').addClass("gray");
	$(".saveOrder").removeClass("gray")
	$('.de').removeClass("gray") //增加
	$(".addWhs").removeClass("gray") //确定
	$(".cancel").removeClass("gray") //取消
//	$(".find").removeClass("gray") //查询
	$("#findg").removeClass("gray") //查询
	$("#findes").removeClass("gray") //查询
	$(".addOrder").removeClass("gray")
	$(".print").removeClass("gray")
	$(".exportExcel").removeClass("gray")
	$(".importExcel").removeClass("gray")
	$(".saveOrder").attr("disabled", false) //
})


$(function() {
	//页面加载完成之后执行	
	var pageNo = 1;
	var rowNum = 10;
});

$(function() {
	//点击表格图标显示存货列表
	$(document).on('click', '.biao_whsId', function() {
		window.open("../../Components/baseDoc/whsList.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	$(document).on('click', '.biao_expressId', function() {
		window.open("../../Components/whs/express_cropList.html", 'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})

$(function() {
	var savedata = {
		'reqHead': reqhead,
		'reqBody': {
			'ecId': "",
			'pageNo': 1,
			'pageSize': 500
		},
	};
	var saveData = JSON.stringify(savedata)
	$.ajax({
		type: 'post',
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/ec/ecPlatform/queryList',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			list = data.respBody.list;
			var option_html = '';
			option_html += '<option value="" selected>' + "" + "</option>"
			for(i = 0; i < list.length; i++) {
				option_html += '<option value="' + list[i].ecId + '"' + 'id="ab">' + list[i].ecName + "</option>"
			}
			window.pro = $(".ecId").first().children("option").val()
			$(".ecId").html(option_html);
			$(".ecId").change(function(e) {
				window.val = this.value;
				pro = this.value
				window.localStorage.setItem("pro",pro);
			})
		},
		error: function() {
			console.log(error)
		}
	})
})


//电商平台名称下拉框
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
		url: url3 + "/mis/ec/ecPlatform/queryList",
		type: "post",
		async: false,
		dataType: 'json', //请求数据返回的类型。可选json,xml,txt
		data: postD2,
		contentType: 'application/json',
		success: function(e) {
			var result = e.respBody.list;
			ecNum += "0" + ":" + "请选择" + ";";
			for(var i = 0; i < result.length; i++) {
				if(i != result.length - 1) {
					ecNum += result[i].ecId + ":" + result[i].ecName + ";";
					let aaa=result[i].ecId;
					let bbb=result[i].ecName
				} else {
					ecNum += result[i].ecId + ":" + result[i].ecName;
					let aaa=result[i].ecId;
					let bbb=result[i].ecName
				}
			}


		}
	});
	return ecNum; //必须有此返回值	
	
}

var myData = {};
//页面初始化
$(function() {
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	var data2 = {
		reqHead,
		"reqBody": {
			"pageNo": 1,
			"pageSize": 500
		}
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		url: url3 + "/mis/ec/whsPlatExpressMapp/selectList",
		async: true,
		data: postD2,
		dataType: 'json',
		contentType: 'application/json',
		success: function(data) {
			var data = data.respBody.list;
			myData=data
			allHeight()
	//初始化表格
			jQuery("#f_jqgrids").jqGrid({
				height: height,
				autoScroll:true,
				data: data,
				datatype: "local", //请求数据返回的类型。可选json,xml,txt
				shrinkToFit:false,
				colNames: ['主键id', '仓库编码', '仓库名称', '平台id',
				'平台名称', '快递公司编码', '快递公司名称', '最小重量', '最大重量','打印模板编码','打印模板名称'], //jqGrid的列显示名字
				colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
					{
						name: 'id',
						align: "center",
						index: 'invdate',
						editable: false,
						hidden:true,
						sortable: false
					},
					{
						name: 'whsId',
						align: "center",
						index: 'id',
						editable: true,
						sortable: false
					},
					{
						name: 'whsName',
						align: "center",
						index: 'invdate',
						editable: false,
						sortable: false
					},
					{
						name: 'platId',
						align: "center",
						id: 'ecName',
						index: 'id',
						editable: false,
						hidden:true,
						sortable: false
					},
					{
						name: 'platName',
						align: "center",
						index: 'invdate',
						editable: true,
						edittype: 'select',
						editoptions: {
							value: getEcNum()
						},
						sortable: false
					},
					{
						name: 'expressId',
						align: "center",
						index: 'id',
						editable: true,
						sortable: false
					},
					{
						name: 'expressName',
						align: "center",
						index: 'id',
						editable: false,
						sortable: false
					},
					{
						name: 'weightMin',
						align: "center",
//						index: 'id',
						editable: true,
						sorttype:'integer',
					},
					{
						name: 'weightMax',
						align: "center",
//						index: 'id',
						editable: true,
						sorttype:'integer',
					},
					{
						name: 'templateId',
						align: "center",
						index: 'id',
						editable: true,
					},
					{
						name: 'templateName',
						align: "center",
						index: 'id',
						editable: false,
						sortable: false
					}
				],
				autowidth: true,
				rownumbers: true,
				loadonce: true,
				forceFit: true,
				sortable:true,
				scrollrows:true,
				rowNum: 500, //一页显示多少条
				rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条	
				sortname: 'id', //初始化的时候排序的字段
				pager : '#f_jqGridPager',//表格页脚的占位符(一般是div)的id
				sortorder: "desc", //排序方式,可选desc,asc
				viewrecords: true,
				multiselect: true, //复选框
				multiboxonly: true,
				caption: "仓库平台快递公司映射", //表格的标题名字	
				ondblClickRow: function(){
					var gr = $("#f_jqgrids").jqGrid('getGridParam', 'selrow');//获取行id
					if(mType == 1) {
						mType = 1
					} else {
						mType = 2;
						$(".addOrder").addClass("gray");
						$(".de").addClass("gray");
						$(".saveOrder").removeClass("gray");
						$('button').attr('disabled', false);
						$(".gray").attr('disabled', true);
						if($("#f_jqgrids").find(".editable").length > 0){ //存在编辑状态的行
							alert("存在未保存的数据，请操作完成后进行编辑！");
							return;
						}
						jQuery('#f_jqgrids').editRow(gr, true);
						var rowDatas = $("#f_jqgrids").jqGrid('getRowData', gr);//获取行数据
						$("#" + gr + "_whsId").attr("readonly", "readonly");
						$("#" + gr + "_expressId").attr("readonly", "readonly");
						$("#" + gr + "_templateId").attr("readonly", "readonly");
					}
//					$('.saveOrder')[0].disabled=true;
					grr(gr)
				}
		
			});
		}
	})

})



function grr(gr) {
	$(".save").removeClass("gray");
	
//	仓库
	$("#"+gr+"_whsId").on("dblclick", function() {
		$("#purchaseOrder").hide()
		$(".box").hide()
		$("#whsDocList").show()
		$("#whsDocList").css("opacity",1)
	})
	
//	快递公司
	$("#"+gr+"_expressId").on("dblclick", function() {
		$(".save").attr("disabled",false)
		$("#purchaseOrder").hide()
		$("#whsDocList").hide()
		$(".box").show()
		$(".box").css("opacity",1)
	})
//	模板
	$("#"+gr+"_templateId").on("dblclick", function() {
		$(".yes").removeClass("gray")
		$(".no").removeClass("gray")
		$("#inquiry").removeClass("gray")
		$(".yes").attr("disabled",false)
		$(".no").attr("disabled",false)
		$("#inquiry").attr("disabled",false)
		$("#purchaseOrder").hide()
		$("#whsDocList").hide()
		$(".box").hide()
		$("#muban").show()
		$("#muban").css("opacity",1)
	})
}


function IsCheckValue(whsId, platName, expressId,templateId,weightMin,weightMax) {
	if(whsId == "") {
		alert("仓库id不能为空")
		return false;
	} else if(platName == "请选择") {
		alert("平台名称未选择")
		return false;
	} else if(expressId == "") {
		alert("快递公司id不能为空")
		return false;
	} else if(templateId == "") {
		alert("模板编码不能为空")
		return false;
	} else if(weightMin * 1 > weightMax * 1) {
		alert("最小重量不能高于最大重量")
		return false;
	}
	return true;
}


//打开部门档案后点击确定取消
$(function() {
//	快递公司
	$(".save").click(function() {
		var rowid;
		//	获得行号
		var gr = $("#ex_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDatas = $("#ex_jqgrids").jqGrid('getRowData', gr);
		$("input[name='expressId']").val(rowDatas.expressEncd)
		if(mType == 1) {
			rowid = newrowid
		} else if(mType == 2) {
			//获得行号
			rowid = $("#f_jqgrids").jqGrid('getGridParam', 'selrow');
		}
		$("#f_jqgrids").setRowData(rowid, {
			expressName: rowDatas.expressNm
		})

		$("#purchaseOrder").show()
		$("#whsDocList").hide()
		$(".box").hide()
		$(".box").css("opacity",0)
	})
	//	取消
	$(".cancel").click(function() {
		$("#purchaseOrder").show()
		$("#whsDocList").hide()
		$(".box").hide()
		$(".box").css("opacity",0)
		//到货单
		//获得行号
		var rowid = $("#f_jqgrids").jqGrid('getGridParam', 'selrow');
	})
	
	
//	仓库档案
	//确定
	$(".addWhs").click(function() {
		//获得行号
		var rowid;

		//	用户
		//	获得行号
		var ids = $("#whs_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowData = $("#whs_jqgrids").jqGrid('getRowData', ids);
		$("input[name='whsId']").val(rowData.whsEncd)
		if(mType == 1) {
			rowid = newrowid
		} else if(mType == 2) {
			//获得行号
			rowid = $("#f_jqgrids").jqGrid('getGridParam', 'selrow');
		}
		$("#f_jqgrids").setRowData(rowid, {
			whsName: rowData.whsNm
		})

		$("#purchaseOrder").show()
		$(".box").hide()
		$("#whsDocList").hide()
		$("#whsDocList").css("opacity",0)
	})
	//	取消
	$(".cancel").click(function() {
		$("#purchaseOrder").show()
		$(".box").hide()
		$("#whsDocList").hide()
		$("#whsDocList").css("opacity",0)
		//获得行号
		var rowid = $("#f_jqgrids").jqGrid('getGridParam', 'selrow');
	})
	//模板确定
	$(".yes").click(function() {

		var ids = $("#f_jqgrids").jqGrid('getGridParam', 'selrow');
	
		//	获得行号
		var gr = $("#print_muBanList_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDatas = $("#print_muBanList_jqgrids").jqGrid('getRowData', gr);
		if(mType == 1) {
			$("#" + newrowid + "_templateId").val(rowDatas.templateId)
			rowid = newrowid
		} else if(mType == 2) {
			$("#" + ids + "_templateId").val(rowDatas.templateId)
			rowid = ids
		}
		$("#f_jqgrids").setRowData(rowid, {
			templateName: rowDatas.templateName
		})
		
		$("#muban").css("opacity", 0);
		$("#muban").hide()
		$(".box").css("opacity", 0);
		$(".box").hide()
		$("#whsDocList").css("opacity", 0);
		$("#whsDocList").hide()
		$("#purchaseOrder").show();
	})
	//	取消
	$(".no").click(function() {
		$("#muban").css("opacity", 0);
		$("#muban").hide()
		$(".box").css("opacity", 0);
		$(".box").hide()
		$("#whsDocList").css("opacity", 0);
		$("#whsDocList").hide()
		$("#purchaseOrder").show();
	})
})



var mType = 0;
var isclick = true;
$(function() {
	$(".saveOrder").click(function() {
		if(isclick) {
			isclick = false;
			if(mType == 1) {
				$(".addOrder").css("background-color", 'black')
				addNewData();
			}
			if(mType == 2) {
				addEditData();
			}
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	})
})
var newrowid;
//增行   保存
$(function() {
	
	$(".addOrder").click(function() {
		mType = 1;
		var selectedId = $("#f_jqgrids").jqGrid("getGridParam", "selrow");
		var ids = $("#f_jqgrids").jqGrid('getDataIDs');

		// 获得当前最大行号（数据编码）
		var rowid = Math.max.apply(Math, ids);
		// 获得新添加行的行号（数据编码）
		newrowid = rowid + 1;
		var dataRow = {
			"whsId": '',
			"whsName": '',
			"expressId": '',
			"weightMax": '',
			"weightMin": '',
			"platId": '',
			"templateId": "", //当前页数

		};
		$("#f_jqgrids").setColProp('storeId', {
			editable: true
		}); //设置editable属性由true改为false

		//将新添加的行插入到第一列
		$("#f_jqgrids").jqGrid("addRowData", newrowid, dataRow, "first");
		//设置grid单元格可编辑
		$('#f_jqgrids').jqGrid('editRow', newrowid, true);
		$("#f_jqgrids").jqGrid('setSelection',newrowid);
		grr(newrowid)
		$(".addOrder").addClass("gray")
		$(".addOrder").attr("disabled",true)
		$("#" + newrowid + "_platName").on("change",function() {
			var name = $("#" + newrowid + "_platName").val()
			$("#f_jqgrids").setRowData(newrowid, {
				platId: name
			})
		})
		$("#" + newrowid + "_whsId").attr("readonly", "readonly");
		$("#" + newrowid + "_expressId").attr("readonly", "readonly");
		$("#" + newrowid + "_templateId").attr("readonly", "readonly");
	})

})
//添加新数据
function addNewData() {
	//获得行号
	var gr = $("#f_jqgrids").jqGrid('getGridParam', 'selrow');
	
	//获得行数据
	var rowDatas = $("#f_jqgrids").jqGrid('getRowData', gr);
	
	var platId = $("#" + gr + "_platName option:selected").val();
	var platName=$("#" + gr + "_platName option:selected").text();
	var whsId = $("input[name='whsId']").val()
	var expressId = $("input[name='expressId']").val()
	var weightMin = $("input[name='weightMin']").val()
	var weightMax = $("input[name='weightMax']").val()
	var templateId = $("input[name='templateId']").val()
	console.log(weightMin,weightMax)
	if(IsCheckValue(whsId, platName, expressId,templateId,weightMin,weightMax) == true) {
		var save = {
			reqHead,
			"reqBody": {
				"platId": platId,
				"whsId": whsId,
				"expressId": expressId,
				"weightMin": weightMin,
				"weightMax": weightMax,
				"templateId": templateId,
			}
		}
		var saveJson = JSON.stringify(save);
		$.ajax({
			type: "post",
			url: url3 + "/mis/ec/whsPlatExpressMapp/insert",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(msgAdd) {
				mType = 0
				alert(msgAdd.respHead.message)
				$("#f_jqgrids").jqGrid().trigger("reloadGrid");
//				$(".saveOrder").addClass("gray")
				$(".addOrder").removeClass("gray")
				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true);
				
				search4()
			},
			error: function(err) {
				console.log("失败")
			}
		});
	}
}


//修改保存按钮
function addEditData() {
	//获得行号
	var gr = $("#f_jqgrids").jqGrid('getGridParam', 'selrow');
	
	//获得行数据
	var rowDatas = $("#f_jqgrids").jqGrid('getRowData', gr);
	
	var id = rowDatas.id
	var platId = $("select[name='platName']").val();
	var platName=$("select[name='platName']").text();
//	var expressName = rowDatas.expressName
	var whsId = $("input[name='whsId']").val()
	var expressId = $("input[name='expressId']").val()
	var weightMin = $("input[name='weightMin']").val()
	var weightMax = $("input[name='weightMax']").val()
	var templateId = $("input[name='templateId']").val()
	if(IsCheckValue(whsId, platName, expressId,templateId,weightMin,weightMax) == true) {
		var save = {
			reqHead,
			"reqBody": {
				"id": id,
				"platId": platId,
				"whsId": whsId,
				"expressId": expressId,
				"weightMin": weightMin,
				"weightMax": weightMax,
				"templateId": templateId,
			}
		}
		var saveJson = JSON.stringify(save);
		console.log(saveJson)
		$.ajax({
			type: "post",
			url: url3 + "/mis/ec/whsPlatExpressMapp/update",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(editMsg) {
				mType = 0
				alert(editMsg.respHead.message);
				$("#f_jqgrids").jqGrid().trigger("reloadGrid");
				search4()
				$(".addOrder").removeClass("gray")
				$(".addOrder").attr("disabled", false)
				$(".de").removeClass("gray")
				$(".de").attr("disabled", false)
			},
			error: function() {
				console.log("更新失败")
			}
		});
	}
}





//查询按钮
$(document).on('click', '#findes', function() {
	search4()
})


//条件查询
function search4() {
	var whsId = $("input[name='whsId1']").val();
	var whsName = $("input[name='whsName1']").val();
	var expressId = $("input[name='expressId1']").val();
	var expressName = $("input[name='expressName1']").val();
	var platName = $("select[name='ecId1'] option:selected").text();
	var platId = $("select[name='ecId1']").val();

	var data2 = {
		reqHead,
		"reqBody": {
			"whsId": whsId,
			"whsName": whsName,
			"expressId": expressId,
			"expressName": expressName,
			"platName": platName,
			"platId": platId,
			"pageNo": 1,
			"pageSize": 500

		}

	};
	var postD2 = JSON.stringify(data2);
	console.log(postD2)
	$.ajax({
		type: "post",
		url: url3 + "/mis/ec/whsPlatExpressMapp/selectList",
		async: true,
		data: postD2,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		//开始加载动画  添加到ajax里面
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");

		},
		success: function(data) {
			var rowNum = $("#_input").val()
			var list = data.respBody.list;
			myDate = list;

			$("#f_jqgrids").jqGrid('clearGridData');
			$("#f_jqgrids").jqGrid('setGridParam', {
				rowNum: rowNum,
				datatype: 'local',
				data: myDate, //newData是符合格式要求的重新加载的数据
				page: 1 //哪一页的值
			}).trigger("reloadGrid")
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
//删除行
$(function() {
	$(".de").click(function() {
		var gr = $("#f_jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
		var rowDatas = $("#f_jqgrids").jqGrid('getRowData', gr); //获取行数据    	
		var id = rowDatas.id

		var deleteAjax = {
			reqHead,
			"reqBody": {
				"id": id,
			}
		};
		var deleteData = JSON.stringify(deleteAjax)
		if(gr == null) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url3 + "/mis/ec/whsPlatExpressMapp/delete",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(remover) {
					alert(remover.respHead.message);
					
					$("#f_jqgrids").jqGrid().trigger("reloadGrid");
					search4()
					$("#searchAll").trigger('click')
					$('#f_jqgrids').css("visibility", "true");
					
					$('.addOrder').removeClass("gray")
					$('button').attr('disabled', false);
					$(".gray").attr('disabled', false);
				},
				error: function() {
					console.log("删除失败")

				}
			});

		}
	})
})
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
	            url: url + "/mis/purc/IntoWhs/uploadIntoWhsFile",
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
	var whsId = $("input[name='whsId1']").val();
	var whsName = $("input[name='whsName1']").val();
	var expressId = $("input[name='expressId1']").val();
	var expressName = $("input[name='expressName1']").val();
	var platName = $("select[name='ecId1'] option:selected").text();
	var platId = $("select[name='ecId1']").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"whsId": whsId,
			"whsName": whsName,
			"expressId": expressId,
			"expressName": expressName,
			"platName": platName,
			"platId": platId,
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url3 + "/mis/ec/whsPlatExpressMapp/selectList",
		type: 'post',
		data: Data,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		success: function(data) {
			var list = data.respBody.list;
			obj=list;
			daochu(obj)
		},
		error: function() {
			console.log(error)
		}
	})
	
})

function daochu(JSONData) {
    var str = '序号,仓库编码,平台编码,快递公司编码,仓库名称,平台名称,快递公司名称,发货最小重量,发货最大重量,打印模板编码,打印模板名称\n';

    for(let i=0;i<JSONData.length;i++){
        var result ='';
        if (JSONData[i].orderStatusc=='0'){
            result="是";
        } else {
            result="否";
        }
//      str += (i+1).toString()+','+JSONData[i].orderId+'\t'+','+formateOrderTime(JSONData[i].orderTime)+'\t'+','+JSONData[i].p1+'\t'+','+JSONData[i].userName+'\t'+','+JSONData[i].recMobile+'\t'+','+JSONData[i].productName+'\t'+','+result+'\t'+',\n'
		for(let item in JSONData[i]) {
			str += `${JSONData[i][item] + '\t'},`;
	    	if(JSONData[i][item]==null){
				JSONData[i][item]="";
			}
		}
		str += '\n';
    }
    var blob = new Blob([str], {type: "text/plain;charset=utf-8"});
    //解决中文乱码问题
    blob =  new Blob([String.fromCharCode(0xFEFF), blob], {type: blob.type});
    object_url = window.URL.createObjectURL(blob);
    var link = document.createElement("a");
    link.href = object_url;
    link.download =  "仓库平台快递公司映射.csv";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}