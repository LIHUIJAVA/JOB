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

			}
		},
		error: function() {
			alert("获取失败")
		},
	});
})


//客户
$(document).on('click', '.biao_custId1', function() {
	localStorage.setItem("cust","1")
	window.open("../../Components/baseDoc/custCust.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
});
$(document).on('click', '.biao_custId2', function() {
	localStorage.setItem("cust","2")
	window.open("../../Components/baseDoc/custCust.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
});
var show = 0;
//新增客户档案
$(function() {
	$(".addOrder").click(function() {
		$("#edit").show()
		$('h3').html('新增客户档案  ')
		$(".update").hide();
		$(".saveOrder").show();
		$("input").val("")
		$(".cancel").click(function() {
			$("#edit").hide();
		})

		$("select").change(function() {
			//如果这样由于id一样，排在第二个位置的下拉框，当change的时候 取不到值 ？  
			var options = $(this).val();
			//显示下拉框中已选文本值 ，同上 

		})
	});
})

$(function() {
	$(".saveOrder").click(function() {
		var cust = $("#custTotlCorpId").val();
		var arr = cust.split("/");
		var custTotlCorpId = arr[0];
		var aa = [];
		var custTotlCorp = [];
		for(var i = 1;i<arr.length;i++){
			aa.push(arr[i])
		}
		custTotlCorp = aa.join("-");

		var custId = $(".custId").val();
		var custNm = $(".custNm").val();
		var custShtNm = $(".custShtNm").val();
		var clsId = $("#select").val();
		var recvblBal = $('.recvblBal').val();
		var ltstInvTm = $('.ltstInvTm').val();
		var ltstInvAmt = $('.ltstInvAmt').val();
		var ltstRecvTm = $('.ltstRecvTm').val();
		var ltstRecvAmt = $('.ltstRecvAmt').val();
		var bizLicsPic = $('.bizLicsPic').val();
		var bankOpenAcctLics = $('.bankOpenAcctLics').val();

		var contcr = $(".contcr").val();
		var tel = $(".tel").val();
		var addr = $(".addr").val();
		var sctyCrdtCd = $('.sctyCrdtCd').val();
		var bllgCorp = $('.bllgCorp').val();
		var opnBnk = $('.opnBnk').val();
		var bkatNum = $('.bkatNum').val();
		var bankEncd = $('.bankEncd').val();
		var delvAddr = $('.delvAddr').val();
		var devDt = $('.devDt').val();
		var setupPers = $('.setupPers').val();
		var setupDt = $('.setupDt').val();
		var mdfr = $('.mdfr').val();
		var modiDt = $('.modiDt').val();
		var taxRate = $('.taxRate').val();

		var memo = $(".memo").val();
		var save = {
			"reqHead": reqhead,
			"reqBody": {
				'custId': custId,
				'custNm': custNm,
				'custShtNm': custShtNm,
				'clsId': clsId,
				'recvblBal': recvblBal,
				'ltstInvTm': ltstInvTm,
				'ltstInvAmt': ltstInvAmt,
				'ltstRecvTm': ltstRecvTm,
				'ltstRecvAmt': ltstRecvAmt,
				'bizLicsPic': bizLicsPic,
				'bankOpenAcctLics': bankOpenAcctLics,

				'contcr': contcr,
				'tel': tel,
				'addr': addr,
				'sctyCrdtCd': sctyCrdtCd,
				'bllgCorp': bllgCorp,
				'opnBnk': opnBnk,
				'bkatNum': bkatNum,
				'bankEncd': bankEncd,
				'delvAddr': delvAddr,
				'devDt': devDt,
				'setupPers': setupPers,
				'setupDt': setupDt,
				'mdfr': mdfr,
				'modiDt': modiDt,
				'taxRate': taxRate,
				'custTotlCorpId': custTotlCorpId,
				'custTotlCorp':custTotlCorp,
				"memo": memo
			}
		}
		var saveJson = JSON.stringify(save);
		$.ajax({
			type: "post",
			url: url + "/mis/purc/CustDoc/insertCustDoc",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			error: function(err) {
				alert("新增失败")
			},
			success: function(msgAdd) {
				alert(msgAdd.respHead.message)
				if(msgAdd.respHead.isSuccess == true) {
					$("#edit").hide()
					if(show == 1) {
						search1()
					} else if(show == 2) {
						search2()
					}
				}
			},
		});
	})
})
var count;
var pages;
var page = 1;
var rowNum;

$(function() {
	pageInit();
})
//页面初始化
function pageInit() {
	//加载动画html 添加到初始的时候
	$("body").append("<div id='mengban' class='zhezhao'></div>");
	$("body").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div> ");
	$("#mengban").addClass("zhezhao");
	jQuery("#jqgrids").jqGrid({
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		url: '../../assets/js/json/order.json',
		colNames: ['客户分类编码', '客户编码', '客户名称', '客户简称', '发货地址', '联系人', '联系方式', '地址', '备注'], //jqGrid的列显示名字
		colModel: [{
				name: 'clsId',
				align: "center",
				editable: true,
			},
			{
				name: 'custId',
				align: "center",
				editable: true,
			},
			{
				name: "custNm",
				align: "center",
				editable: true,
			},
			{
				name: "custShtNm",
				align: "center",
				editable: true,
			},
			{
				name: "delvAddr",
				align: "center",
				editable: true,
			},
			{
				name: 'contcr',
				align: "center",
				editable: true,
			},
			{
				name: 'tel',
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
		autowidth: true,
		height: 400,
		autoScroll: true,
		shrinkToFit: true,
		rowNum: 100, //一页显示多少条
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowList: [100, 300, 500],
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		multiboxonly: true,
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
			if(show == 1) {
				search1()
			} else if(show == 2 || show == 0) {
				search2()
			}
		},
		ondblClickRow: function() {
			editData()
		}
	});
}

function editData() {
	$("#edit").show();
	$('h3').html('编辑客户档案  ');

	$(".update").show();
	$(".saveOrder").hide();
	$(".cancel").click(function() {
		$("#edit").hide()
	})
	//获得行号
	var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow');
	//			获得行数据
	var rowDatas = $("#jqgrids").jqGrid('getRowData', gr);
	var edit = {
		"reqHead": reqhead,
		"reqBody": {
			"custId": rowDatas.custId
		}
	}
	var editJson = JSON.stringify(edit);
	$.ajax({
		type: "post",
		url: url + "/mis/purc/CustDoc/selectCustDocByCustId",
		async: true,
		data: editJson,
		dataType: 'json',
		contentType: 'application/json',
		error: function(err) {
			alert("详细信息查询失败")
		},
		success: function(data) {
			$("#select").val(data.respBody.custDoc[0].clsId)
			$(".custNm").val(data.respBody.custDoc[0].custNm);
			$(".custShtNm").val(data.respBody.custDoc[0].custShtNm);
			$(".custId").val(data.respBody.custDoc[0].custId);
			$('.recvblBal').val(data.respBody.custDoc[0].recvblBal);
			$('.ltstInvTm').val(data.respBody.custDoc[0].ltstInvTm);
			$('.ltstInvAmt').val(data.respBody.custDoc[0].ltstInvAmt);
			$('.ltstRecvTm').val(data.respBody.custDoc[0].ltstRecvTm);
			$('.ltstRecvAmt').val(data.respBody.custDoc[0].ltstRecvAmt);
			$('.bizLicsPic').val(data.respBody.custDoc[0].bizLicsPic);
			$('.bankOpenAcctLics').val(data.respBody.custDoc[0].bankOpenAcctLics);

			$(".contcr").val(data.respBody.custDoc[0].contcr);
			$(".tel").val(data.respBody.custDoc[0].tel);
			$(".addr").val(data.respBody.custDoc[0].addr);
			$('.sctyCrdtCd').val(data.respBody.custDoc[0].sctyCrdtCd);
			$('.bllgCorp').val(data.respBody.custDoc[0].bllgCorp);
			$('.opnBank').val(data.respBody.custDoc[0].opnBank);
			$('.bkatNum').val(data.respBody.custDoc[0].bkatNum);
			$('.bankEncd').val(data.respBody.custDoc[0].bankEncd);
			$('.delvAddr').val(data.respBody.custDoc[0].delvAddr);
			$('.devDt').val(data.respBody.custDoc[0].devDt);
			$('.setupPers').val(data.respBody.custDoc[0].setupPers);
			$('.setupDt').val(data.respBody.custDoc[0].setupDt);
			$('.mdfr').val(data.respBody.custDoc[0].mdfr);
			$('.modiDt').val(data.respBody.custDoc[0].modiDt);
			$('.taxRate').val(data.respBody.custDoc[0].taxRate);
			var custTotlCorpId = data.respBody.custDoc[0].custTotlCorpId;
			var custTotlCorp = data.respBody.custDoc[0].custTotlCorp;
			$('#custTotlCorpId').val(custTotlCorpId+"-"+custTotlCorp)
			$(".memo").val(data.respBody.custDoc[0].memo);
		},
	});

}

$(function() {
	$('.update').click(function() {
		var cust = $("#custTotlCorpId").val();
		var arr = cust.split("-");
		var custTotlCorpId = arr[0];
		var aa = [];
		var custTotlCorp = [];
		for(var i = 1;i<arr.length;i++){
			aa.push(arr[i])
		}
		custTotlCorp = aa.join("-");
		
		//如果这样由于id一样，排在第二个位置的下拉框，当change的时候 取不到值 ？  
		var clsIds = $("#select option:selected");
		var clsId = $("#select").val();
		var custId = $(".custId").val();
		var custNm = $(".custNm").val();
		var custShtNm = $(".custShtNm").val();
		var recvblBal = $('.recvblBal').val();
		var ltstInvTm = $('.ltstInvTm').val();
		var ltstInvAmt = $('.ltstInvAmt').val();
		var ltstRecvTm = $('.ltstRecvTm').val();
		var ltstRecvAmt = $('.ltstRecvAmt').val();
		var bizLicsPic = $('.bizLicsPic').val();
		var bankOpenAcctLics = $('.bankOpenAcctLics').val();
		var contcr = $(".contcr").val();
		var tel = $(".tel").val();
		var addr = $(".addr").val();
		var sctyCrdtCd = $('.sctyCrdtCd').val();
		var bllgCorp = $('.bllgCorp').val();
		var opnBnk = $('.opnBnk').val();
		var bkatNum = $('.bkatNum').val();
		var bankEncd = $('.bankEncd').val();
		var delvAddr = $('.delvAddr').val();
		var devDt = $('.devDt').val();
		var setupPers = $('.setupPers').val();
		var setupDt = $('.setupDt').val();
		var mdfr = $('.mdfr').val();
		var modiDt = $('.modiDt').val();
		var taxRate = $('.taxRate').val();
		var custTotlCorpId = $("#custTotlCorpId").val();
		var memo = $(".memo").val();
		var save = {
			"reqHead": reqhead,
			"reqBody": {
				'custId': custId,
				'custNm': custNm,
				'custShtNm': custShtNm,
				'clsId': clsId,
				'recvblBal': recvblBal,
				'ltstInvTm': ltstInvTm,
				'ltstInvAmt': ltstInvAmt,
				'ltstRecvTm': ltstRecvTm,
				'ltstRecvAmt': ltstRecvAmt,
				'bizLicsPic': bizLicsPic,
				'bankOpenAcctLics': bankOpenAcctLics,
				'contcr': contcr,
				'tel': tel,
				'addr': addr,
				'sctyCrdtCd': sctyCrdtCd,
				'bllgCorp': bllgCorp,
				'opnBnk': opnBnk,
				'bkatNum': bkatNum,
				'bankEncd': bankEncd,
				'delvAddr': delvAddr,
				'devDt': devDt,
				'setupPers': setupPers,
				'setupDt': setupDt,
				'mdfr': mdfr,
				'modiDt': modiDt,
				'taxRate': taxRate,
				'custTotlCorpId': custTotlCorpId,
				'custTotlCorp':custTotlCorp,
				"memo": memo
			}
		}
		var saveJson = JSON.stringify(save);
		$.ajax({
			type: "post",
			url: url + "/mis/purc/CustDoc/updateCustDocByCustId",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			error: function(err) {
				alert("更新失败")
			},
			success: function(msgAdd) {
				alert(msgAdd.respHead.message)
				if(msgAdd.respHead.isSuccess == true) {
					$("#edit").hide()
					if(show == 1) {
						search1()
					} else if(show == 2) {
						search2()
					}
				}
			},
		});

	})
})
var clsId;
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
	var myDate = {};
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
			myDate = obj;
			
			var mydata = {};
			mydata.rows = myDate;
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

//输入条件查询

$(document).on('click', '.find', function() {
	show = 2;
	$('.click_span').removeClass("addColor")
	search2()
})

function search2() {
	var rowNum1 = $("td select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var myDate = {};
	var custId = $(".custId1").val();
	var custNm = $(".custNm1").val();
	var clsId = $(".clsId1").val();
	var Data = {
		"reqHead": reqhead,
		"reqBody": {
			"custId": custId,
			"custNm": custNm,
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
			myDate = obj;
			var mydata = {};
			mydata.rows = myDate;
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
			var jstime = $("#jqgrids").getCell(ids[i], "custId");
			//建一个数组，把选中行的id添加到这个数组中去。
			rowData[i] = jstime;
		}
		var rowDatas = rowData.toString();

		var deleteAjax = {
			"reqHead": reqhead,
			"reqBody": {
				"custId": rowDatas
			}
		}
		var deleteData = JSON.stringify(deleteAjax);
		if(ids.length == 0) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + "/mis/purc/CustDoc/deleteCustDocList",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(remover) {
					if(remover.respHead.isSuccess == true) {
						alert(remover.respHead.message)
						$("#edit").hide()
						if(show == 1) {
							search1()
						} else if(show == 2) {
							search2()
						}
					}
				},
				error: function() {
					alert("删除失败")
				}
			});
		}
	})
})

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
				url: url + "/mis/purc/CustDoc/uploadCustDocFile",
				data: data,
				dataType: "json",
				cache: false, //上传文件无需缓存
				processData: false, //用于对data参数进行序列化处理 这里必须false
				contentType: false, //必须
				success: function(data) {
					alert(data.respHead.message)
					window.location.reload()
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
	var custId = $(".custId1").val();
	var custNm = $(".custNm1").val();
	var clsId = $(".clsId1").val();
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"custId": custId,
			"custNm": custNm,
			"clsId": clsId
		}
	}
	var Data = JSON.stringify(savedata);
	$.ajax({
		url: url + '/mis/purc/CustDoc/printingCustDocList',
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
			var arr = [];
			var obj = {}
			var list = data.respBody.list[0].custDoc;
			obj = list;
			daochu(obj)
		},
		error: function() {
			alert("导出失败")
		}
	})

})

function daochu(JSONData) {
    var str = '客户编码,客户名称,客户简称,客户分类编码,应收余额,最近发票时间,最近发票金额,最近收款时间,最近收款金额,营业执照照片,银行开户许可证,联系人,电话,地址,统一社会信用代码,备注,开票单位,开户银行,银行账号,所属银行编码,发货地址,发展日期,创建人,创建时间,修改人,修改时间,税率\n';

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
    link.download =  "客户档案.csv";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}