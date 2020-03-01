var page = 1;
var rowNum;

//左侧树的渲染
$(function() {
	var data3 = {
		"reqHead": reqhead,
		"reqBody": {
			"isSelctRealWhs": "1",
			"isSelctWhs": "",
			"realWhs": ""
		}
	};
	var postD3 = JSON.stringify(data3);
	$.ajax({
		type: "post",
		data: postD3,
		url: url + "/mis/system/misUser/userWhsList",
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
							if(children[j]["realWhsList"]) {
								if(children[j]["open"]) {
									str += "<div open='true'><span class='close'></span><span class='openTrue click_span']><span class='provrClsId'>" + children[j]["realWhs"] + "</span><span class='pid'>" + ")&nbsp&nbsp" + "</span><span class='provrClsNm'>" + children[j]["realNm"] + "</span><span class='level'>" + children[j]["level"] + "</span></div>";
								} else {
									str += "<div open='false'><span class='open'></span><span class='openTrue click_span'><span class='provrClsId'>" + children[j]["realWhs"] + "</span><span class='pid'>" + ")&nbsp&nbsp" + "</span><span class='provrClsNm'>" + children[j]["realNm"] + "</span><span class='level'>" + children[j]["level"] + "</span></div>";
								}

							} else {
								str += "<div><span class='leaf'></span><span class='leafName click_span'><span class='provrClsId'>" + children[j]["realWhs"] + "</span><span class='pid'>" + ")&nbsp&nbsp" + "</span><span class='provrClsNm'>" + children[j]["realNm"] + "</span><span class='level'>" + children[j]["level"] + "</div>";
							}

							createTree(children[j]["realWhsList"])
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
		},
		error: function() {
			alert("获取大仓失败")
		},
	});
})
var bigWhs = "";
$(document).on('click', '.click_span', function() {
	//点击改变颜色
	$(".addColor").removeClass("addColor");
	$(this).addClass("addColor");

	bigWhs = $(this).children().first().text().toString();
	search2()
})

$(function() {
	$(".add").click(function() {
		var ids = $("#jqGrids_list").jqGrid('getGridParam', 'selarrrow'); //获取行id
		var rowData = [];
		for(i = 0; i < ids.length; i++) {
			//选中行的id
			var whs = $("#jqGrids_list").getCell(ids[i], "whsEncd");
			rowData[i] = whs
		}
		var rowDatas = rowData.toString();
		var accNum = $("#user").val();
		if(rowData.length == 0) {
			alert("请选择可添加的仓库")
		} else {
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"accNum": accNum,
					"whsEncd": rowDatas
				}
			};
			var post = JSON.stringify(data);
			$.ajax({
				type: "post",
				url: url + "/mis/system/misUser/userWhsAdd",
				async: true,
				data: post,
				dataType: 'json',
				contentType: 'application/json; charset=utf-8',
				//开始加载动画  添加到ajax里面
				beforeSend: function() {
					$(".zhezhao").css("display", "block");
					$("#loader").css("display", "block");
				},
				success: function(data) {
					initGrid()
					search2()
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
	})
})

$(function() {
	$(".del").click(function() {
		var ids = $("#deteil_list").jqGrid('getGridParam', 'selarrrow'); //获取行id
		var rowData = [];
		for(i = 0; i < ids.length; i++) {
			//选中行的id
			var whs = $("#deteil_list").getCell(ids[i], "whsEncd");
			rowData[i] = whs
		}
		var rowDatas = rowData.toString();
		var accNum = $("#user").val();
		if(rowData.length == 0) {
			alert("请选择已添加的仓库")
		} else if(confirm("确定删除？")) {
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"accNum": accNum,
					"whsEncd": rowDatas
				}
			};
			var post = JSON.stringify(data);
			$.ajax({
				type: "post",
				url: url + "/mis/system/misUser/userWhsUpdate",
				async: true,
				data: post,
				dataType: 'json',
				contentType: 'application/json; charset=utf-8',
				//开始加载动画  添加到ajax里面
				beforeSend: function() {
					$(".zhezhao").css("display", "block");
					$("#loader").css("display", "block");
				},
				success: function(data) {
					initGrid()
					search2()
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
	})
})

$(function() {
	$("#user").change(function() {
		search2();
		initGrid()
	});
});

//可添加仓库查询
function search2() {
	var accNum = $("#user").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"isSelctRealWhs": "",
			"isSelctWhs": "0",
			"accNum": accNum,
			"realWhs": bigWhs
		}
	};
	var post = JSON.stringify(data);
	$.ajax({
		type: "post",
		url: url + "/mis/system/misUser/userWhsList",
		async: true,
		data: post,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		//开始加载动画  添加到ajax里面
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");
		},
		success: function(data) {
			if(data.respHead.isSuccess==false){
				alert(data.respHead.message)
			}
			var list = data.respBody.list;
			var mydata = {};
			mydata.rows = list
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#jqGrids_list").jqGrid("clearGridData");
			$("#jqGrids_list").jqGrid("setGridParam", {
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

//表格初始化--已添加仓库
function initGrid() {
	var accNum = $("#user").val();
	var data3 = {
		"reqHead": reqhead,
		"reqBody": {
			"isSelctRealWhs": "",
			"isSelctWhs": "1",
			"accNum": accNum,
			"realWhs": ""
		}
	};
	var postD3 = JSON.stringify(data3);
	$.ajax({
		type: "post",
		data: postD3,
		url: url + "/mis/system/misUser/userWhsList",
		async: true,
		dataType: 'json',
		contentType: "application/json; charset=utf-8",
		success: function(data) {
			var list = data.respBody.list;
			var mydata = {};
			mydata.rows = list
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#deteil_list").jqGrid("clearGridData");
			$("#deteil_list").jqGrid("setGridParam", {
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
	})
}

$(function() {
	//可添加仓库
	$("#jqGrids_list").jqGrid({
		datatype: "local",
		colNames: ['仓库编码', '仓库名称'],
		colModel: [{
				name: 'whsEncd',
				editable: false,
				align: 'center',
				sortable: false,
			},
			{
				name: 'whsNm',
				editable: false,
				align: 'center'

			}
		],
		autowidth: true,
		rownumbers: true,
		height: 400,
		autoScroll: true,
		shrinkToFit: false,
		loadonce: false,
		forceFit: true,
		rowNum: 1000,
		pager: '#jqGridPager_list', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		multiselect: true, //复选框
		//		multiboxonly: true,
		caption: "可添加仓库", //表格的标题名字
	});
	//已添加仓库
	$("#deteil_list").jqGrid({
		datatype: "local",
		colNames: ['仓库编码', '仓库名称'],
		colModel: [{
				name: 'whsEncd',
				editable: false,
				align: 'center',
				sortable: false,
			},
			{
				name: 'whsNm',
				editable: false,
				align: 'center'

			}
		],
		autowidth: true,
		rownumbers: true,
		height: 400,
		autoScroll: true,
		shrinkToFit: false,
		loadonce: false,
		forceFit: true,
		rowNum: 1000,
		pager: '#deteilPager_list', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		multiselect: true, //复选框
		//		multiboxonly: true,
		caption: "已添加仓库", //表格的标题名字
	})
})