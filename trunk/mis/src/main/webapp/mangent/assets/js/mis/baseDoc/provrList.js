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

		},
		error: function() {
			alert("获取失败")
		},
	});
})

var search = 0;
var count;
var pages;
var page = 1;
var rowNum;
//右列表格的操作
$(function() {
	//页面加载完成之后执行
	pageInit();
})

//页面初始化
function pageInit() {
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
		autowidth: true,
		rownumbers: true,
		height: 320,
		autoScroll: true,
		shrinkToFit: false,
		loadonce: true,
		forceFit: true,
		rowNum: 100, //一页显示多少条
		rowList: [100, 300, 500],
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
//		multiboxonly: true,
		caption: "部门档案", //表格的标题名字
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
	var start_pro = $("input[name='start_pro']").val();
	var end_pro = $("input[name='end_pro']").val();
	var myDate = {};
	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"provrId": provrId1,
			"provrNm": provrNm1,
			"provrClsId": provClsId1,
			"start": start_pro,
			"end": end_pro,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var postD2 = JSON.stringify(data2);
	console.log(postD2)
	$.ajax({
		type: "post",
		url: url + "/mis/purc/ProvrDoc/selectProvrDocList",
		async: true,
		data: postD2,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
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
		error: function() {
			alert("条件查询失败")
		}
	});
}

$(document).on('click', '.true', function() {
	var ids = $("#jqgrids").jqGrid('getGridParam', 'selarrrow');
	var rowData = $("#jqgrids").jqGrid('getRowData', 1);
	if(ids.length==0||rowData.provrId=="") {
		alert("请选择正确单据")
	} else {
		var encd = [];
		var nm = [];
		for(i = 0; i < ids.length; i++) {
			var rowDatas = $("#jqgrids").jqGrid('getRowData', ids[i]); //获取行数据
			nm[i] = rowDatas.provrNm;
			encd[i] = rowDatas.provrId;
		}
		var encds = encd.join(',');
		var nms = nm.join(',')
		window.parent.opener.document.getElementById("provrId").value = encds;
		window.parent.opener.document.getElementById("provrId1").value = encds;
		window.parent.opener.document.getElementById("provrNm").value = nms;
		window.parent.opener.document.getElementById("provrNm1").value = nms;
		window.close()
	}
})
$(document).on('click', '.false', function() {
	window.close();
	window.parent.opener.document.getElementById("provrId").value = "";
	window.parent.opener.document.getElementById("provrNm1").value = "";
	window.parent.opener.document.getElementById("provrNm").value = "";
})