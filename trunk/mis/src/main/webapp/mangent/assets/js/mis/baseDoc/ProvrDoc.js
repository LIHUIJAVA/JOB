//左侧树的渲染
$(function() {
	var data3 = {
		"reqHead": reqhead,
		"reqBody": {}
	};
	var postD3 = JSON.stringify(data3);
	$.ajax({
		type: "post",
		data: postD3,
		url: url + "/mis/purc/ProvrCls/selectProvrCls",
		async: true,
		dataType: 'json',
		contentType: "application/json; charset=utf-8",
		success: function(data) {
			var obj = data.respBody.list;
			var arr1 = Array.from(obj)
			paintingTree(arr1, "tree")

			function paintingTree(arr1, id) {
				if(arr1[0]["pId"] !== undefined) {
					arr1 = removeEmptyFromPaintData(arr1)
				}
				var str = ""
				//渲染树
				function createTree(arr1) {
					if(arr1) {
						var children = arr1;
						str += "<ul>";
						for(var j = 0; j < children.length; j++) {
							str += "<li>"
							if(children[j]["children"]) {
								if(children[j]["open"]) {
									str += "<div open='true'><span class='close'></span><span class='openTrue click_span']><span class='provrClsId'>" + children[j]["provrClsId"] + "</span><span class='pid'>" + ")&nbsp&nbsp" + "</span><span class='provrClsNm'>" + children[j]["provrClsNm"] + "</span><span class='level'>" + children[j]["level"] + "</span></div>";
								} else {
									str += "<div open='false'><span class='open'></span><span class='openTrue click_span'><span class='provrClsId'>" + children[j]["provrClsId"] + "</span><span class='pid'>" + ")&nbsp&nbsp" + "</span><span class='provrClsNm'>" + children[j]["provrClsNm"] + "</span><span class='level'>" + children[j]["level"] + "</span></div>";
								}

							} else {
								str += "<div><span class='leaf'></span><span class='leafName click_span'><span class='provrClsId'>" + children[j]["provrClsId"] + "</span><span class='pid'>" + ")&nbsp&nbsp" + "</span><span class='provrClsNm'>" + children[j]["provrClsNm"] + "</span><span class='level'>" + children[j]["level"] + "</div>";
							}

							createTree(children[j]["children"])
							str += "</li>"
						}
						str += "</ul>";
					}
				}

				createTree(arr1)
				$("#" + id).hide()
				$("#" + id).html(str)
				$("[open=true]").each(function() {
					$(this).next().show()
				})
				$("[open=false]").each(function() {
					$(this).next().hide()
				})
				$(document).on("click", ".close", function() {
					$(this).parent().next().hide()
					$(this).addClass("open").removeClass("close")
				})
				$(document).on("click", ".open", function() {
					$(this).parent().next().show()
					$(this).addClass("close").removeClass("open")
				})
				$('#tree>ul>li>div span').parent().next().show()
				$('#tree>ul>li>div span').eq(0).addClass("close").removeClass("open")
				$("#" + id).show()
			}

			var nm = $(".provrClsNm");
			var id = $(".provrClsId");
			for(var i = 0; i < id.length; i++) {
				//设置option中的内容,建立option节点
				var option = document.createElement("option");
				//转换DOM对象为JQ对象,好用JQ里面提供的方法 给option的value赋值		
				$(option).val(id[i].innerHTML);
				//给option的text赋值,这就是你点开下拉框能够看到的东西
				$(option).text(nm[i].innerHTML);
				select.appendChild(option); //在select中增加option节点
			}
		},
		error: function() {
			alert("获取失败")
		},
	});
})

var mType = 0;
var options;
var search = 0;
var count;
var pages;
var page = 1;
var rowNum;
//右列表格的操作
$(function() {
	//页面加载完成之后执行
	pageInit();
	$(window).resize(function() {　　
		$("#jqgrids").setGridWidth($(window).width() * 0.99);　　
		$("#jqgrids").setGridWidth(document.body.clientWidth * 0.99);
	});
})
//页面初始化
function pageInit() {
	//加载动画html 添加到初始的时候
	$("body").append("<div id='mengban' class='zhezhao'></div>");
	$("body").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	jQuery("#jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['供应商编码', '供应商名称', '供应商简称', '联系方式', '地址', '备注'], //jqGrid的列显示名字
		colModel: [{
				name: 'provrId',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: "provrNm",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: "provrShtNm",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: "tel",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'addr',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'memo',
				align: "center",
				editable: true,
				sortable: false
			}
		],
		height: 400,
		autowidth: true,
		rownumbers: true,
		loadonce: true,
		rowNum: 100, //一页显示多少条
		forceFit: true,
		rowList: [100, 300, 500],
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		multiboxonly: true,
		caption: "供应商档案", //表格的标题名字
		//cellEdit:true, //单元格是否可编辑
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
			if(search == 1) {
				search1()
			} else if(search == 2) {
				search2()
			}
		},
		ondblClickRow: function() {
			editData()
		}
	});
}
var provrClsId;
//条件查询供应商档案
$(document).on('click', '.click_span', function() {
	//点击改变颜色
	$(".addColor").removeClass("addColor");
	$(this).addClass("addColor");

	provrClsId = $(this).children().first().text().toString();
	search = 1;
	search1()
})

//查询按钮
$(function() {
	$(".find").click(function() {
		search = 2;
		$('.click_span').removeClass("addColor")
		search2()
	})
})

function search2() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	$("#edit").hide()
	var provrId1 = $(".provrId1").val();
	var provrNm1 = $(".provrNm1").val();
	var provClsId1 = $(".provClsId1").val();
	var myDate = {};
	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"provrId": provrId1,
			"provrNm": provrNm1,
			"provrClsId": provClsId1,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		url: url + "/mis/purc/ProvrDoc/selectProvrDocList",
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
			var arr = eval(data); //数组
			var list = arr.respBody.list;
			var arr1 = [];
			var length = list.length;
			for(var i = 0; i < length; i++) {
				var provrDocData = list[i].provrDoc;
				arr1.push(list[i].provrDoc);
			}
			var arr2 = [];

			function fun(arr) {
				for(var i = 0; i < arr.length; i++) {
					if(Array.isArray(arr[i])) {
						fun(arr[i]);
					} else {
						arr2.push(arr[i]);
					}
				}
			}
			fun(arr1);
			//			var arr2 = arr1.flat(Infinity)
			var rootData = JSON.stringify(arr2);

			var obj = JSON.parse(rootData)
			myDate = obj;
			var mydata = {};
			mydata.rows = myDate
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
			alert("条件查询失败")
		}
	});
}

function search1() {
	var rowNum1 = $("td select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	$("#edit").hide()
	var myDate = {};
	var Data = {
		"reqHead": reqhead,
		"reqBody": {
			"provrClsId": provrClsId,
			"pageNo": page,
			"pageSize": rowNum
		}
	}
	var changeData = JSON.stringify(Data)
	$.ajax({
		type: "post",
		url: url + "/mis/purc/ProvrDoc/selectProvrDocList",
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
			var arr = eval(data); //数组
			var list = arr.respBody.list;
			var arr1 = [];
			var length = list.length;
			for(var i = 0; i < length; i++) {
				var provrDocData = list[i].provrDoc;
				arr1.push(list[i].provrDoc);
			}
			var arr2 = [];

			function fun(arr) {
				for(var i = 0; i < arr.length; i++) {
					if(Array.isArray(arr[i])) {
						fun(arr[i]);
					} else {
						arr2.push(arr[i]);
					}
				}
			}
			fun(arr1);
			//			var arr2 = arr1.flat(Infinity)
			var rootData = JSON.stringify(arr2);

			var obj = JSON.parse(rootData)
			myDate = obj;
			var mydata = {};
			mydata.rows = myDate
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
			alert("条件查询失败")
		}
	});
}

//新增供应商档案
$(function() {
	$(".addOrder").click(function() {
		$("#edit input").val("");
		$("#edit select").val("")
		mType = 1;
		$("#edit").show()
		$('h3').html('新增供应商档案  ')

		$(".cancel").click(function() {
			$("#edit").hide();
		})

		$("select").change(function() {
			//如果这样由于id一样，排在第二个位置的下拉框，当change的时候 取不到值 ？  
			options = $(this).val();
		})
	});
})

$(function() {
	$(".saveOrder").click(function() {
		if(mType == 1) {
			addData()
		} else if(mType == 2) {
			editData2()
		}
	})
})

function addData() {
	var provrId = $(".provrId").val();
	var provrNm = $(".provrNm").val();
	var provrShtNm = $(".provrShtNm").val();
	var provrClsId = $("#select").val();
	var devDt = $(".devDt").val();
	var provrTotlCorp = $('.provrTotlCorp').val();
	var belgZone = $(".belgZone").val();
	var belgCls = $(".belgCls").val();
	var contcr = $(".contcr").val();
	var opnBnk = $(".opnBnk").val();
	var rgstCap = $(".rgsCap").val();
	var bkatNum = $(".bkatNum").val();
	var bankEncd = $(".bankEncd").val();
	var lpr = $(".lpr").val();
	var taxNum = $(".taxNum").val();
	var addr = $(".addr").val();
	var tel = $(".tel").val();
	var zipCd = $(".zipCd").val();
	var taxRate = $(".taxRate").val();
	var isNtPurs = $(".isNtPurs").val();
	var isOutsource = $(".isOut").val();
	var isNtServ = $(".isNtServ").val();
	var setupPers = $(".setupPers").val();
	var memo = $(".memo").val();

	var save = {
		"reqHead": reqhead,
		"reqBody": {
			"provrId": provrId,
			"provrNm": provrNm,
			"provrShtNm": provrShtNm,
			"provrClsId": provrClsId,
			"devDt": devDt,
			"provrTotlCorp": provrTotlCorp,
			"belgZone": belgZone,
			"belgCls": belgCls,
			"contcr": contcr,
			"bkatNum": bkatNum,
			"bankEncd": bankEncd,
			"lpr": lpr,
			"taxNum": taxNum,
			"addr": addr,
			"tel": tel,
			"zipCd": zipCd,
			"taxRate": taxRate,
			"isNtPurs": isNtPurs,
			"isOutsource": isOutsource,
			"isNtServ": isNtServ,
			"setupPers": setupPers,
			"memo": memo
		}
	}
	var saveJson = JSON.stringify(save);
	$.ajax({
		type: "post",
		url: url + "/mis/purc/ProvrDoc/insertProvrDoc",
		async: true,
		data: saveJson,
		dataType: 'json',
		contentType: 'application/json',
		error: function(err) {
			alert("新增失败")
		},
		success: function(msgAdd) {
			alert(msgAdd.respHead.message)
			if(search == 1) {
				search1()
			} else if(search == 2) {
				search2()
			}
		},
	});

}

//删除行
$(function() {
	$(".delOrder").click(function() {
		//获得选中行的行号
		var ids = $('#jqgrids').jqGrid('getGridParam', 'selarrrow');

		//获取选择行的provrId
		var rowData = [];
		for(var i = 0; i < ids.length; i++) {
			var gr = $("#jqgrids").getGridParam('selrow');
			//选中行的id
			var jstime = $("#jqgrids").getCell(ids[i], "provrId");
			//建一个数组，把选中行的id添加到这个数组中去。
			rowData[i] = jstime;
		}
		var rowDatas = rowData.toString();
		if(rowDatas.length == 0) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {

			var deleteAjax = {
				"reqHead": reqhead,
				"reqBody": {
					"provrId": rowDatas
				}
			}
			var deleteData = JSON.stringify(deleteAjax);
			$.ajax({
				type: "post",
				url: url + "/mis/purc/ProvrDoc/deleteProvrDocList",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(remover) {
					alert(remover.respHead.message)
					if(search == 1) {
						search1()
					} else if(search == 2) {
						search2()
					}
				},
				error: function() {
					alert("删除失败")
				}
			});
		}
	})
})

function editData() {
	//获得行号
	var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowDatas = $("#jqgrids").jqGrid('getRowData', gr);
	mType = 2;
	$("#edit").show();
	$('h3').html('编辑供应商档案  ');
	$(".cancel").click(function() {
		$("#edit").hide()
	})
	var edit = {
		"reqHead": reqhead,
		"reqBody": {
			"provrId": rowDatas.provrId
		}
	}
	var editJson = JSON.stringify(edit);
	$.ajax({
		type: "post",
		url: url + "/mis/purc/ProvrDoc/selectProvrDocByProvrId",
		async: true,
		data: editJson,
		dataType: 'json',
		contentType: 'application/json',
		error: function(err) {
			alert("详细信息查询失败")
		},
		success: function(data) {
			$(".provrId").val(data.respBody.provrDoc[0].provrId);
			$(".provrNm").val(data.respBody.provrDoc[0].provrNm);
			$(".provrShtNm").val(data.respBody.provrDoc[0].provrShtNm);
			$("#select").val(data.respBody.provrDoc[0].provrClsId);
			$(".devDt").val(data.respBody.provrDoc[0].devDt);
			$('.provrTotlCorp').val(data.respBody.provrDoc[0].provrTotlCorp);
			$(".belgZone").val(data.respBody.provrDoc[0].belgZone);
			$(".belgCls").val(data.respBody.provrDoc[0].belgCls);
			$(".contcr").val(data.respBody.provrDoc[0].contcr);
			$(".opnBnk").val(data.respBody.provrDoc[0].opnBnk);
			$(".rgsCap").val(data.respBody.provrDoc[0].rgsCap);
			$(".bkatNum").val(data.respBody.provrDoc[0].bkatNum);
			$(".bankEncd").val(data.respBody.provrDoc[0].bankEncd);
			$(".lpr").val(data.respBody.provrDoc[0].lpr);
			$(".taxNum").val(data.respBody.provrDoc[0].taxNum);
			$(".addr").val(data.respBody.provrDoc[0].addr);
			$(".tel").val(data.respBody.provrDoc[0].tel);
			$(".zipCd").val(data.respBody.provrDoc[0].zipCd);
			$(".taxRate").val(data.respBody.provrDoc[0].taxRate);
			$(".isNtPurs").val(data.respBody.provrDoc[0].isNtPurs);
			$(".isOut").val(data.respBody.provrDoc[0].isOut);
			$(".isNtServ").val(data.respBody.provrDoc[0].isNtServ);
			$(".mdfr").val(data.respBody.provrDoc[0].mdfr);
			$(".memo").val(data.respBody.provrDoc[0].memo);
		},
	});
}

function editData2() {
	var provrId = $(".provrId").val();
	var provrNm = $(".provrNm").val();
	var provrShtNm = $(".provrShtNm").val();
	var provrClsId = $("#select").val();
	var devDt = $(".devDt").val();
	var provrTotlCorp = $('.provrTotlCorp').val();
	var belgZone = $(".belgZone").val();
	var belgCls = $(".belgCls").val();
	var contcr = $(".contcr").val();
	var opnBnk = $(".opnBnk").val();
	var rgstCap = $(".rgsCap").val();
	var bkatNum = $(".bkatNum").val();
	var bankEncd = $(".bankEncd").val();
	var lpr = $(".lpr").val();
	var taxNum = $(".taxNum").val();
	var addr = $(".addr").val();
	var tel = $(".tel").val();
	var zipCd = $(".zipCd").val();
	var taxRate = $(".taxRate").val();
	var isNtPurs = $(".isNtPurs").val();
	var isOutsource = $(".isOut").val();
	var isNtServ = $(".isNtServ").val();
	var mdfr = $(".mdfr").val();
	var memo = $(".memo").val();

	var save = {
		"reqHead": reqhead,
		"reqBody": {
			"provrId": provrId,
			"provrNm": provrNm,
			"provrShtNm": provrShtNm,
			"provrClsId": provrClsId,
			"devDt": devDt,
			"provrTotlCorp": provrTotlCorp,
			"belgZone": belgZone,
			"belgCls": belgCls,
			"contcr": contcr,
			"bkatNum": bkatNum,
			"bankEncd": bankEncd,
			"lpr": lpr,
			"taxNum": taxNum,
			"addr": addr,
			"tel": tel,
			"zipCd": zipCd,
			"taxRate": taxRate,
			"isNtPurs": isNtPurs,
			"isOutsource": isOutsource,
			"isNtServ": isNtServ,
			"mdfr": mdfr,
			"memo": memo
		}
	}
	var saveJson = JSON.stringify(save);
	$.ajax({
		type: "post",
		url: url + "/mis/purc/ProvrDoc/updateProvrDocByProvrId",
		async: true,
		data: saveJson,
		dataType: 'json',
		contentType: 'application/json',
		error: function(err) {
			alert("更新失败")
		},
		success: function(msgAdd) {
			alert(msgAdd.respHead.message)
			if(search == 1) {
				search1()
			} else if(search == 2) {
				search2()
			}
		},
	});

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
				url: url + "/mis/purc/ProvrDoc/uploadProvrDocFile",
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

//导出
$(document).on('click', '.exportExcel', function() {
	var provrId1 = $("input[class='provrId1']").val();
	var provrNm1 = $("input[class='provrNm1']").val();
	var provClsId1 = $(".provClsId1").val();
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"provrId": provrId1,
			"provrNm": provrNm1,
			"provrClsId": provClsId1
		}
	}
	var Data = JSON.stringify(savedata);
	$.ajax({
		url: url + '/mis/purc/ProvrDoc/printingProvrDocList',
		type: 'post',
		data: Data,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		beforeSend: function() {
			$("#mengban").css("display", "block");
			$("#loader").css("display", "block");
		},
		complete: function() {
			$("#mengban").css("display", "none");
			$("#loader").css("display", "none");
		},
		success: function(data) {
			var obj = {}
			var arr = data.respBody.list;
			var myData = [];
			for(var i = 0; i < arr.length; i++) {
				if((arr[i].provrDoc != null) && (arr[i].provrDoc.length > 0)) {
					for(var j = 0; j < arr[i].provrDoc.length; j++) {
						myData.push(arr[i].provrDoc[j]);
					}
				} else {
					myData.push(arr[i]);
				}
			}
			obj = myData;
			daochu(obj)
		},
		error: function() {
			alert("导出失败")
		}
	})

})

function daochu(JSONData) {
    var str = '供应商编码,供应商名称,供应商简称,供应商分类编码,发展日期,供应商总公司,所属地区,所属分类,联系人,开户银行,注册资金,银行账号,所属银行编码,法人,税号,地址,电话,邮编,税率,是否采购,是否委外,是否服务,创建人,创建时间,修改人,修改时间,备注\n';

    for(let i=0;i<JSONData.length;i++){
        var result ='';
        if (JSONData[i].orderStatusc=='0'){
            result="是";
        } else {
            result="否";
        }
		for(let item in JSONData[i]) {
			if(JSONData[i][item]==null){
				JSONData[i][item]="";
			}
			str += `${JSONData[i][item] + '\t'},`;
		}
		str += '\n';
    }
    var blob = new Blob([str], {type: "text/plain;charset=utf-8"});
    //解决中文乱码问题
    blob =  new Blob([String.fromCharCode(0xFEFF), blob], {type: blob.type});
    object_url = window.URL.createObjectURL(blob);
    var link = document.createElement("a");
    link.href = object_url;
    link.download =  "供应商档案.csv";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}