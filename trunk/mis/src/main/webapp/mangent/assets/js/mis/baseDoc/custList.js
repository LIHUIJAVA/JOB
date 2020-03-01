//左列树的渲染
$(function() {
	var data3 = {
		"reqHead": reqhead,
		"reqBody": {},

	};
	var postD3 = JSON.stringify(data3);
	$.ajax({
		type: "post",
		data: postD3,
		url: url + "/mis/purc/CustCls/selectCustCls",
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
									str += "<div open='true'><span class='close'></span><span class='openTrue click_span']><span class='provrClsId'>" + children[j]["clsId"] + "</span><span class='pid'>" + ")&nbsp&nbsp" + "</span><span class='provrClsNm'>" + children[j]["clsNm"] + "</span><span class='level'>" + children[j]["level"] + "</span></div>";
								} else {
									str += "<div open='false'><span class='open'></span><span class='openTrue click_span'><span class='provrClsId'>" + children[j]["clsId"] + "</span><span class='pid'>" + ")&nbsp&nbsp" + "</span><span class='provrClsNm'>" + children[j]["clsNm"] + "</span><span class='level'>" + children[j]["level"] + "</span></div>";
								}

							} else {
								str += "<div><span class='leaf'></span><span class='leafName click_span'><span class='provrClsId'>" + children[j]["clsId"] + "</span><span class='pid'>" + ")&nbsp&nbsp" + "</span><span class='provrClsNm'>" + children[j]["clsNm"] + "</span><span class='level'>" + children[j]["level"] + "</div>";
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
var show = 0;
$(function() {
	pageInit();
	search3()
	$(window).resize(function() {　　
		$("#cust_jqgrids").setGridWidth($(window).width() * 0.99);　　
		$("#cust_jqgrids").setGridWidth(document.body.clientWidth * 0.99);
	})
})
//页面初始化
function pageInit() {
	jQuery("#cust_jqgrids").jqGrid({
		datatype: "local", //请求数据返回的类型。可选json,xml,txt
		colNames: ['客户分类编码', '客户编码', '客户名称', '客户简称', '发货地址', '联系人', '联系方式', '地址', '备注'], //jqGrid的列显示名字
		colModel: [{
				name: 'clsId',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'custId',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: "custNm",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: "custShtNm",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: "delvAddr",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'contcr',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'tel',
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
		loadonce: false,
		forceFit: true,
		height: 300,
		rowNum: 100,
		rowList: [100, 300, 500],
		pager: '#cust_jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
//		multiboxonly: true,
		caption: "部门档案", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#cust_jqgrids").getGridParam('records'); //获取返回的记录数
			page = $("#cust_jqgrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#cust_jqgrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			if(show == 1) {
				search1()
			} else if(show == 2 || show == 0) {
				search3()
			}
		},
	});
}
var clsId
//条件查询客户档案
$(document).on('click', '.click_span', function() {
	show = 1;
	//点击改变颜色
	$(".addColor").removeClass("addColor");
	$(this).addClass("addColor");
	clsId = $(this).children().first().text().toString();
	search1()
})

function search1() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var myData = {};
	var Data = {
		"reqHead": reqhead,
		"reqBody": {
			"clsId": clsId,
			"pageNo": page,
			"pageSize": rowNum
		}
	}
	var changeData = JSON.stringify(Data)
	$.ajax({
		type: "post",
		url: url + "/mis/purc/CustDoc/selectCustDocList",
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
				var provrDocData = list[i].custDoc;
				arr1.push(list[i].custDoc);
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
			$("#cust_jqgrids").jqGrid("clearGridData");
			$("#cust_jqgrids").jqGrid("setGridParam", {
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

//输入条件查询
$(document).on('click', '.find', function() {
	show = 2;
	search3()
})

function search3() {
	var rowNum1 = $("#cust_jqgrids").getGridParam('rowNum'); //获取显示配置记录数量
	rowNum = parseInt(rowNum1)
	var myData = {};
	var custId = $(".custId1").val();
	var custNm = $(".custNm1").val();
	var clsId = $(".clsId1").val();
	var start_pro = $("input[name='start_pro']").val();
	var end_pro = $("input[name='end_pro']").val();
	var Data = {
		"reqHead": reqhead,
		"reqBody": {
			"custId": custId,
			"custNm": custNm,
			"clsId": clsId,
			"start": start_pro,
			"end": end_pro,
			"pageNo": page,
			"pageSize": rowNum
		}
	}
	var changeData = JSON.stringify(Data)
	console.log(changeData)
	$.ajax({
		type: "post",
		url: url + "/mis/purc/CustDoc/selectCustDocList",
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
				var provrDocData = list[i].custDoc;
				arr1.push(list[i].custDoc);
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
			mydata.rows = myData;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#cust_jqgrids").jqGrid("clearGridData");
			$("#cust_jqgrids").jqGrid("setGridParam", {
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
	var ids = $("#cust_jqgrids").jqGrid('getGridParam', 'selarrrow');
	if(ids.length == 0) {
		alert("请选择单据")
	} else {
		var encd = [];
		var nm = [];
		for(i = 0; i < ids.length; i++) {
			var rowDatas = $("#cust_jqgrids").jqGrid('getRowData', ids[i]); //获取行数据
			nm[i] = rowDatas.custNm;
			encd[i] = rowDatas.custId;
		}
		var encds = encd.join(',');
		var nms = nm.join(',')
		window.parent.opener.document.getElementById("custId").value = encds;
		window.parent.opener.document.getElementById("custId1").value = encds;
		window.parent.opener.document.getElementById("custNm").value = nms;
		window.parent.opener.document.getElementById("custNm1").value = nms;
		localStorage.setItem("tel", rowDatas.tel);
		localStorage.setItem("contcr", rowDatas.contcr);
		localStorage.setItem("addr", rowDatas.delvAddr);
		window.parent.opener.document.getElementById("addr").value = rowDatas.delvAddr;
		window.close()
	}
})
$(document).on('click', '.false', function() {
	window.parent.opener.document.getElementById("custId").value = "";
	window.parent.opener.document.getElementById("custId1").value = "";
	window.parent.opener.document.getElementById("custNm").value = "";
	window.parent.opener.document.getElementById("custNm1").value = "";
	window.parent.opener.document.getElementById("addr").value = '';
	window.close()
})