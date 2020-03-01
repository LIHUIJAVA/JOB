var count;
var pages;
var page = 1;
var rowNum;
var lastId;
$(function() {
	//点击表格图标显示店铺列表
	$(document).on('click', '.biao_invtyEncd', function() {
		window.open("../../Components/baseDoc/invtyList.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	$(document).on('click', '.biao_whsEncd', function() {
		window.open("../../Components/baseDoc/whsList.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
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
/*------点击下载按钮加载动态下拉电商平台-------*/
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
			option_html += '<option value="" selected>' + "请选择" + "</option>"
			for(i = 0; i < list.length; i++) {
				option_html += '<option value="' + list[i].ecId + '"' + 'id="ab">' + list[i].ecName + "</option>"
			}
			window.pro = $(".platEncd").first().children("option").val()
			$(".platEncd").html(option_html);
			$(".platEncd").change(function(e) {
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
			ecNum += "" + ":" + "请选择" + ";";
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


$(function() {
	pageInit7();
})

//页面初始化
var myData = {};
function pageInit7() {
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
		url: url3 + "/mis/ec/platWhsSpecial/selectList",
		async: true,
		data: postD2,
		dataType: 'json',
		contentType: 'application/json',
		success: function(data) {
			var data = data.respBody.list;
			myData=data
	//初始化表格
			allHeight()
			jQuery("#platWhsSpecial_jqgrids").jqGrid({
				mtype: "post",
				datatype: "local", //请求数据返回的类型。可选json,xml,txt
				data: data,
				ajaxGridOptions: {
					contentType: 'application/json; charset=utf-8'
				},
				colNames: ['序号' ,'平台编码', '平台名称', '存货编码', '存货名称','仓库编码','仓库名称'
				], //jqGrid的列显示名字
				colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
					{
						name: 'stId',
						align: "center",
						editable: false,
						sortable: false,
						hidden: true
					},
					{
						name: 'platCode',
						align: "center",
						editable: false,
						sortable: false,
					},
					{
						name: 'ecName',
						align: "center",
						editable: true,
						sortable: false,
						edittype: 'select',
						editoptions: {
							value: getEcNum()
						}
					},
					{
						name: 'invtyCode',
						align: "center",
						editable: true,
						sortable: false,
					},
					{
						name: 'invtyNm',
						align: "center",
						editable: false,
						sortable: false,
					},
					{
						name: 'whsCode',
						align: "center",
						editable: true,
						sortable: false,
					},
					{
						name: 'whsNm',
						align: "center",
						editable: false,
						sortable: false,
					}
				],
				jsonReader: {
					root: "respBody.list",
					repeatitems: true,
					records: "respBody.count", // json中代表数据行总数的数据		            
					total: "respBody.pages", // json中代表页码总数的数据
				},
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
				pager: '#platWhsSpecial_jqGridPager', //表格页脚的占位符(一般是div)的id
		
				multiselect: true, //复选框
				multiboxonly: true,
				caption: "特殊存货", //表格的标题名字	
				onPaging: function(pageBtn) { //翻页实现 
					var records = $("#platWhsSpecial_jqgrids").getGridParam('records'); //获取返回的记录数
					page = $("#platWhsSpecial_jqgrids").getGridParam('page'); //获取返回的当前页
					var rowNum1 = $("#platWhsSpecial_jqgrids").getGridParam('rowNum');
		
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
					search3();
				},
		
				ondblClickRow: function(rowid){
					$(".saveOrder").removeClass("gray")
					$(".saveOrder").attr("disabled",false)
					var gr = $("#platWhsSpecial_jqgrids").jqGrid('getGridParam', 'selrow');//获取行id
					if(mType == 1) {
						mType = 1
					} else {
						mType = 2;
						if($("#platWhsSpecial_jqgrids").find(".editable").length > 0){ //存在编辑状态的行
							alert("存在未保存的数据，请操作完成后进行编辑！");
							return;
						}
						jQuery('#platWhsSpecial_jqgrids').editRow(gr, true);
						$("#" + gr + "_invtyCode").attr("readonly", "readonly");
						$("#" + gr + "_whsCode").attr("readonly", "readonly");
						$("#" + gr + "_ecName").on("change",function() {
							var name = $("#" + gr + "_ecName").val()
							$("#platWhsSpecial_jqgrids").setRowData(gr, {
								platCode: name
							})
						})
					}
					grr(gr)
				}
		
			});
		}
	})
}


//查询按钮
$(document).on('click', '#finds', function() {
	search3()
})

//条件查询
function search3() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var invtyEncd = $("input[name='invtyEncd']").val();
	var platEncd = $("select[name='platEncd']").val();
	var whsEncd = $("input[name='whsEncd']").val();
	
	var myDate = {};
	var data2 = {
		reqHead,
		"reqBody": {
			"invtyEncd":invtyEncd,
			"platEncd":platEncd,
			"whsEncd":whsEncd,
			"pageNo": page,
			"pageSize": rowNum
		}

	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url3 + "/mis/ec/platWhsSpecial/selectList",
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
			$("#platWhsSpecial_jqgrids").jqGrid("clearGridData");
			$("#platWhsSpecial_jqgrids").jqGrid("setGridParam", {
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
		$(".saveOrder").removeClass("gray")
		$(".saveOrder").attr("disabled",false)
		mType = 1;
		var selectedId = $("#platWhsSpecial_jqgrids").jqGrid("getGridParam", "selrow");
		var ids = $("#platWhsSpecial_jqgrids").jqGrid('getDataIDs');

		// 获得当前最大行号（数据编码）
		var rowid = Math.max.apply(Math, ids);
		// 获得新添加行的行号（数据编码）
		newrowid = rowid + 1;
		var dataRow = {
			"stId": '',
			"platCode": '',
			"ecName": '',
			"invtyCode": '',
			"invtyNm": '',
			"whsCode": '',
			"whsNm": "", 

		};
//		$("#platWhsSpecial_jqgrids").setColProp('storeId', {
//			editable: true
//		}); //设置editable属性由true改为false

		//将新添加的行插入到第一列
		$("#platWhsSpecial_jqgrids").jqGrid("addRowData", newrowid, dataRow, "first");
		//设置grid单元格可编辑
		$('#platWhsSpecial_jqgrids').jqGrid('editRow', newrowid, true);
		$("#platWhsSpecial_jqgrids").jqGrid('setSelection',newrowid);
		grr(newrowid)
		$(".addOrder").addClass("gray")
		$(".addOrder").attr("disabled",true)
		$("#" + newrowid + "_ecName").on("change",function() {
			var name = $("#" + newrowid + "_ecName").val()
			$("#platWhsSpecial_jqgrids").setRowData(newrowid, {
				platCode: name
			})
		})
		$("#" + newrowid + "_invtyCode").attr("readonly", "readonly");
		$("#" + newrowid + "_whsCode").attr("readonly", "readonly");
	})

})


function grr(gr) {
	$(".save").removeClass("gray");
	
//	仓库
	$("#"+gr+"_invtyCode").on("dblclick", function() {
		$("#purchaseOrder").hide()
		$("#whsDocList").hide()
		$("#insertList").show()
		$("#insertList").css("opacity",1)
	})
	
//	快递公司
	$("#"+gr+"_whsCode").on("dblclick", function() {
		$(".save").attr("disabled",false)
		$("#purchaseOrder").hide()
		$("#insertList").hide()
		$("#whsDocList").show()
		$("#whsDocList").css("opacity",1)
	})
}

//打开仓库/存货档案后点击确定取消
$(function() {
	//确定
	$(".addWhs").click(function() {
		//获得行号
		var rowid = $("#platWhsSpecial_jqgrids").jqGrid('getGridParam', 'selrow');

		//	存货档案
		//	获得行号
		var ids = $("#insert_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowData = $("#insert_jqgrids").jqGrid('getRowData', ids);

		$("#" + rowid + "_invtyCode").val(rowData.invtyEncd)
		$("#platWhsSpecial_jqgrids").setRowData(rowid, {
			invtyNm: rowData.invtyNm
		})

		$("#whsDocList").css("opacity", 0);
		$("#insertList").css("opacity", 0);
		$("#purchaseOrder").show();
	})
	//确定
	$(".addWhss").click(function() {
		//获得行号
		var rowid = $("#platWhsSpecial_jqgrids").jqGrid('getGridParam', 'selrow');

		//	仓库档案
		//	获得行号
		var gr = $("#whs_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDatas = $("#whs_jqgrids").jqGrid('getRowData', gr);

		$("#" + rowid + "_whsCode").val(rowDatas.whsEncd)
		$("#platWhsSpecial_jqgrids").setRowData(rowid, {
			whsNm: rowDatas.whsNm
		})

		$("#whsDocList").css("opacity", 0);
		$("#insertList").css("opacity", 0);
		$("#purchaseOrder").show();
	})
	//	取消
	$(".cancel").click(function() {
		$("#whsDocList").hide();
		$("#whsDocList").css("opacity", 0);
		$("#insertList").hide();
		$("#insertList").css("opacity", 0);
		$("#purchaseOrder").show();
		//到货单
		//获得行号
//		var rowid = $("#jqgrids").jqGrid('getGridParam', 'selrow');
//
//		$("#" + rowid + "_whsNm").val("");
//		$("#" + rowid + "_whsEncd").val("");
//		$("#" + rowid + "_invtyEncd").val("")
	})
})

function IsCheckValue(ecName, invtyCode, whsCode) {
	if(ecName == "") {
		alert("平台未选择")
		return false;
	} else if(invtyCode == "") {
		alert("存货编码不能为空")
		return false;
	} else if(whsCode == "") {
		alert("仓库编码不能为空")
		return false;
	}
	return true;
}

//添加新数据
function addNewData() {
	//获得行号
	var gr = $("#platWhsSpecial_jqgrids").jqGrid('getGridParam', 'selrow');
	
	//获得行数据
	var rowDatas = $("#platWhsSpecial_jqgrids").jqGrid('getRowData', gr);
	
	var platCode = rowDatas.platCode
	
	var ecName = $("select[name='ecName']").val()
	var invtyCode = $("input[name='invtyCode']").val()
	var whsCode = $("input[name='whsCode']").val()
	
	if(IsCheckValue(ecName, invtyCode, whsCode) == true) {
		var save = {
			reqHead,
			"reqBody": {
				"platEncd": platCode,
				"invtyEncd": invtyCode,
				"whsEncd": whsCode,
			}
		}
		var saveJson = JSON.stringify(save);
		$.ajax({
			type: "post",
			url: url3 + "/mis/ec/platWhsSpecial/add",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(msgAdd) {
				$(".addOrder").removeClass("gray")
				$(".saveOrder").addClass("gray")
				$(".addOrder").attr("disabled",false)
				$(".saveOrder").attr("disabled",true)
				mType = 2
				alert(msgAdd.respHead.message)
				$("#platWhsSpecial_jqgrids").jqGrid().trigger("reloadGrid");
				search3()
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
	var gr = $("#platWhsSpecial_jqgrids").jqGrid('getGridParam', 'selrow');
	
	//获得行数据
	var rowDatas = $("#platWhsSpecial_jqgrids").jqGrid('getRowData', gr);
	
	var stId = rowDatas.stId
//	var platCode = rowDatas.platCode
	var platCode = $("select[name='ecName']").val()
	
	var ecName = $("select[name='ecName']").val()
	var invtyCode = $("input[name='invtyCode']").val()
	var whsCode = $("input[name='whsCode']").val()
	
	if(IsCheckValue(ecName, invtyCode, whsCode) == true) {
		var save = {
			reqHead,
			"reqBody": {
				"stId": stId,
				"platEncd": platCode,
				"invtyEncd": invtyCode,
				"whsEncd": whsCode,
			}
		}
		var saveJson = JSON.stringify(save);
		console.log(saveJson)
		$.ajax({
			type: "post",
			url: url3 + "/mis/ec/platWhsSpecial/update",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(editMsg) {
				mType = 0
				alert(editMsg.respHead.message);
				$("#platWhsSpecial_jqgrids").jqGrid().trigger("reloadGrid");
				search3()
				$(".addOrder").removeClass("gray")
				$(".addOrder").attr("disabled", false)
			},
			error: function() {
				console.log("更新失败")
			}
		});
	}
}

//删除行
$(function() {
	$(".delOrder").click(function() {
		var num = []
		var gr = $("#platWhsSpecial_jqgrids").jqGrid('getGridParam', 'selarrrow'); //获取行id
		for(var i = 0;i<gr.length;i++) {
			var rowDatas = $("#platWhsSpecial_jqgrids").jqGrid('getRowData', gr[i]); //获取行数据
			var stId = rowDatas.stId
			num.push(stId)
		}

		var deleteAjax = {
			reqHead,
			"reqBody": {
				"stId": num.toString(),
			}
		};
		var deleteData = JSON.stringify(deleteAjax)
		if(gr.length == 0) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url3 + "/mis/ec/platWhsSpecial/delete",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(remover) {
					alert(remover.respHead.message);
					search3()
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