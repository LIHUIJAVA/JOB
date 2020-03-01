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
		url: url + "/mis/purc/InvtyCls/selectInvtyCls",
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
									str += "<div open='true'><span class='close'></span><span class='openTrue click_span']><span class='provrClsId'>" + children[j]["invtyClsEncd"] + "</span><span class='pid'>" + ")&nbsp&nbsp" + "</span><span class='provrClsNm'>" + children[j]["invtyClsNm"] + "</span><span class='level'>" + children[j]["level"] + "</span></div>";
								} else {
									str += "<div open='false'><span class='open'></span><span class='openTrue click_span'><span class='provrClsId'>" + children[j]["invtyClsEncd"] + "</span><span class='pid'>" + ")&nbsp&nbsp" + "</span><span class='provrClsNm'>" + children[j]["invtyClsNm"] + "</span><span class='level'>" + children[j]["level"] + "</span></div>";
								}

							} else {
								str += "<div><span class='leaf'></span><span class='leafName click_span'><span class='provrClsId'>" + children[j]["invtyClsEncd"] + "</span><span class='pid'>" + ")&nbsp&nbsp" + "</span><span class='provrClsNm'>" + children[j]["invtyClsNm"] + "</span><span class='level'>" + children[j]["level"] + "</div>";
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
		},
		error: function() {
			alert("获取失败")
		},
	});
})

var count;
var pages;
var page = 1;
var rowNum;

var search = 0;
$(function() {
	jQuery("#jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['存货分类编码','存货分类名称','存货编码', '存货名称','存货代码', '计量单位', '规格型号', '箱规', '保质期', '保质期预警天数'], //jqGrid的列显示名字
		colModel: [{
				name: 'invtyClsEncd',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: "invtyClsNm",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'invtyEncd',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: "invtyNm",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: "invtyCd",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: "measrCorpNm",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'spcModel',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: "bxRule",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'baoZhiQiDt',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'baoZhiQiEarWarn',
				align: "center",
				editable: true,
				sortable: false
			}
		],
		autowidth: true,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		height: 320,
		rowNum: 500,
		rowList: [500,1000, 3000,5000], //可供用户选择一页显示多少条
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "存货档案", //表格的标题名字
		multiselect: true, //复选框
//		multiboxonly: true,
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
	});

})
var invtyClsEncd;
//条件查询供应商档案
$(document).on('click', '.click_span', function() {
	search = 1;
	//点击改变颜色
	$(".addColor").removeClass("addColor");
	$(this).addClass("addColor");
	invtyClsEncd2 = $(this).children().first().text().toString();
	search1()
})

//输入条件查询供应商档案
$(document).on('click', '.find', function() {
	search = 2;
	$('.click_span').removeClass("addColor");
	search2()
})

function search1() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var myData = {};
	var Data = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyClsEncd": invtyClsEncd2,
			"pageNo": page,
			"pageSize": rowNum
		}
	}
	var changeData = JSON.stringify(Data)
	$.ajax({
		type: "post",
		url: url + "/mis/purc/InvtyDoc/selectInvtyDocList",
		async: true,
		data: changeData,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		success: function(data) {
			var arr = eval(data); //数组
			var list = arr.respBody.list;
			var arr1 = [];
			var length = list.length;
			for(var i = 0; i < length; i++) {
				arr1.push(list[i]);
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
			myData = obj;
			var mydata = {};
			mydata.rows = myData
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
		error: function() {
			alert("条件查询失败")
		}
	});
}

function search2() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var invtyEncd1 = $(".invtyEncd1").val();
	var invtyNm1 = $(".invtyNm1").val();
	var invtyClsEncd1 = $(".invtyClsEncd1").val();
	var invtyClsNm1 = $(".invtyClsNm1").val();

	var myData = {};
	var Data = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": invtyEncd1,
			"invtyNm": invtyNm1,
			"invtyClsEncd": invtyClsEncd1,
			"invtyClsNm": invtyClsNm1,
			"pageNo": page,
			"pageSize": rowNum
		}
	}
	var changeData = JSON.stringify(Data)
	$.ajax({
		type: "post",
		url: url + "/mis/purc/InvtyDoc/selectInvtyDocList",
		async: true,
		data: changeData,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		success: function(data) {
			var arr = eval(data); //数组
			var list = arr.respBody.list;
			var arr1 = [];
			var length = list.length;
			for(var i = 0; i < length; i++) {
				arr1.push(list[i]);
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
			myData = obj;
			var mydata = {};
			mydata.rows = myData
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
		error: function() {
			alert("条件查询失败")
		}
	});
}

$(document).on('click', '.true', function() {
	//获得行号
	var ids = $("#jqgrids").jqGrid('getGridParam', 'selarrrow');
	if(ids.length == 0) {
		alert("请选择单据")
	} else {
		var encd = [];
		var nm = [];
		for(i = 0; i < ids.length; i++) {
			var rowDatas = $("#jqgrids").jqGrid('getRowData', ids[i]); //获取行数据
			nm[i] = rowDatas.invtyNm;
			encd[i] = rowDatas.invtyEncd;
		}
		var encds = encd.join(',');
		var nms = nm.join(',');
		window.parent.opener.document.getElementById("invtyEncds").value = encds;
		window.parent.opener.document.getElementById("invtyNms").value = nms;
		window.close()
	}
})
$(document).on('click', '.false', function() {
	window.parent.opener.document.getElementById("invtyEncds").value = "";
	window.parent.opener.document.getElementById("invtyNms").value = "";
	window.close()
})