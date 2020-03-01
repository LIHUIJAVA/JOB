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

$(function() {
	$("body").append("<div id='mengban1' class='zhezhao'></div>");
	$("body").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");

	jQuery("#jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['存货编码', '存货名称', '计量单位', '箱规', '地址', '备注'], //jqGrid的列显示名字
		colModel: [{
				name: 'invtyEncd',
				align: "center",
				editable: true,
			},
			{
				name: "invtyNm",
				align: "center",
				editable: true,
			},
			{
				name: "measrCorpNm",
				align: "center",
				editable: true,
			},
			{
				name: "bxRule",
				align: "center",
				editable: true,
			},
			{
				name: 'addr',
				align: "center",
				editable: true,
			},
			{
				name: 'memo',
				align: "center",
				editable: true,
			}
		],
		height:400,
		shrinkToFit:false,
		autowidth: true,
		autoScroll:true,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 500,
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
//		viewrecords: true,
		multiselect: true, //复选框
		caption: "期末处理", //表格的标题名字
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
			seacher()
		},
	});
})

var invtyClsEncd;
$(function(){
	$(".find").click(function(){
		invtyClsEncd="";
		seacher();
	})
})
//条件查询供应商档案
$(document).on('click', '.click_span', function() {
	//点击改变颜色
	$(".addColor").removeClass("addColor");
	$(this).addClass("addColor");
	invtyClsEncd = $(this).children().first().text().toString();
	seacher();
})

function seacher(){
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var Data = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyClsEncd": invtyClsEncd,
			"isFinalDeal":'1',
			"pageNo": page,
			"pageSize": rowNum
		}
	}
	var changeData = JSON.stringify(Data)
	$.ajax({
		type: "post",
		url: url + "/mis/account/form/final/noDeal",
		async: true,
		data: changeData,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");

		},
		//结束加载动画
		complete: function() {
			$(".zhezhao").css("display", "none");
			$("#loader").css("display", "none");
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
		error: function() {
			alert("条件查询失败")
		}
	});
}

$(function() {
	$(".recovery").click(function() {
		var rowid = $("#jqgrids").jqGrid('getGridParam', 'selarrrow');
		var invty=[];
		for(var i = 0;i<rowid.length;i++){
			var rowData = $("#jqgrids").jqGrid('getRowData', rowid[i]);
			invty[i] = rowData.invtyEncd;
		}
		
		var invtyEncd = invty.join(",");
		var Data = {
			"reqHead": reqhead,
			"reqBody": {
				"invtyEncd": invtyEncd
			}
		}
		var changeData = JSON.stringify(Data)
		$.ajax({
			type: "post",
			url: url + "/mis/account/form/final/backDeal",
			async: true,
			data: changeData,
			dataType: 'json',
			contentType: 'application/json; charset=utf-8',
			beforeSend: function() {
				$(".zhezhao").css("display", "block");
				$("#loader").css("display", "block");
	
			},
			//结束加载动画
			complete: function() {
				$(".zhezhao").css("display", "none");
				$("#loader").css("display", "none");
			},
			success: function(data) {
				alert(data.respHead.message)
				if(data.respHead.isSuccess==true){
					seacher()
				}
			},
			error: function(data) {
				alert("error")
			}
		});
	})
})