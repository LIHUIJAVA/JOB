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

$(function() {
	var data1 = {
		"reqHead": reqhead,
		"reqBody": {
			"pageNo": 1,
			"pageSize": 99999999
		}
	};
	var postData = JSON.stringify(data1);
	$.ajax({
		type: "post",
		dataType: 'json',
		url: url + "/mis/purc/MeasrCorpDoc/selectMeasrCorpDocList",
		data: postData,
		async: true,
		contentType: 'application/json',
		success: function(data) {
			var list = data.respBody.list;
			for(var i in list) {
				$("#measrCorpId").append("<option value = " + list[i].measrCorpId + ">" + list[i].measrCorpNm + "</option>")
			}
		}
	})
})

var count;
var pages;
var page = 1;
var rowNum;

var mType = 0;
var options;
var search = 0;
$(function() {
	//页面加载完成之后执行
	pageInit();
	$(window).resize(function() {　　
		$("#jqgrids").setGridWidth($(window).width() * 0.75);　　
		$("#jqgrids").setGridWidth(document.body.clientWidth * 0.75);
	});
})
//获取浏览器高度
function pageInit() {
	//加载动画html 添加到初始的时候
	$("body").append("<div id='mengban' class='zhezhao'></div>");
	$("body").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	jQuery("#jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['存货分类编码', '存货分类名称', '存货编码', '存货名称', '存货代码', '计量单位', '规格型号', '箱规', '保质期', '保质期预警天数'], //jqGrid的列显示名字
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
		rowNum: 500,
		height: 400,
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		multiboxonly: true,
		caption: "存货档案", //表格的标题名字
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
			editData2();
		}
	});
}

//新增存货档案
$(function() {
	$(".addOrder").click(function() {
		mType = 1;
		$("#edit input").val("");
		$('#edit select').val("");
		$("#mengban").show()
		$("#edit").show();
		$('h3').html('新增存货档案  ');
		$(".cancel").click(function() {
			$("#edit").hide();
			$("#mengban").hide()
		})

		$("#select").val(invtyClsEncd2)
		$("select").change(function() {
			options = $(this).val();
		})
	});
})

var isclick = true;
$(function() {
	$(".saveOrder").click(function() {
		if(isclick) {
			isclick = false;
			if(mType == 1) {
				addData();
			}
			if(mType == 2) {
				editData();
			}
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	})
})
function addData() {
	var invtyClsEncd = $("#select").val()
	var invtyEncd = $(".invtyEncd").val()
	var invtyNm = $(".invtyNm").val()
	var spcModel = $(".spcModel").val()
	var invtyCd = $(".invtyCd").val()
	var provrId = $(".provrId").val()
	var measrCorpId = $(".measrCorpId").val()
	var vol = $(".vol").val()
	var weight = $(".weight").val()
	var longs = $(".longs").val()
	var wide = $(".wide").val()
	var hght = $(".hght").val()
	var bxRule = $(".bxRule").val()
	var baoZhiQiEarWarn = $(".baoZhiQiEarWarn").val()
	var baoZhiQiDt = $(".baoZhiQiDt").val()
	var placeOfOrigin = $(".placeOfOrigin").val()
	var rgstBrand = $(".rgstBrand").val()
	var cretNum = $(".cretNum").val()
	var makeLicsNum = $(".makeLicsNum").val()
	var traySize = $(".traySize").val()
	var quantity = $(".quantity").val()
	var valtnMode = $(".valtnMode").val()
	var iptaxRate = $(".iptaxRate").val()
	var optaxRate = $(".optaxRate").val()
	var loSellPrc = $(".loSellPrc").val()
	var ltstCost = $(".ltstCost").val()
	var refCost = $(".refCost").val()
	var refSellPrc = $(".refSellPrc").val()
	var isNtSell = $(".isNtSell").val()
	var isNtPurs = $(".isNtPurs").val()
	var isDomSale = $(".isDomSale").val()
	var pto = $(".pto").val()
	var isQuaGuaPer = $(".isQuaGuaPer").val()
	var allowBomMain = $(".allowBomMain").val()
	var allowBomMinor = $(".allowBomMinor").val()
	var isNtBarCdMgmt = $(".isNtBarCdMgmt").val()
	var crspdBarCd = $(".crspdBarCd").val()
	var projDocInd = $(".projDocInd").val()
	var setupPers = localStorage.loginName;
	var isNtDiscnt = $(".isNtDiscnt").val();
	var shdTaxLabour = $(".shdTaxLabour").val();
	var highestPurPrice = $(".highestPurPrice").val()
	var memo = $(".memo").val();
	if(invtyClsEncd == null || invtyClsEncd == "") {
		alert("请选择存货分类")
	} else if(bxRule == null || bxRule == "") {
		alert("请输入箱规")
	} else if(measrCorpId == null || measrCorpId == "") {
		alert("请输入计量单位")
	} else if(baoZhiQiDt == null || baoZhiQiDt == "") {
		alert("请输入保质期天数")
	} else if(refCost == null || refCost == "") {
		alert("请输入参考成本")
	} else if(refSellPrc == null || refSellPrc == "") {
		alert("请输入参考售价")
	} else {
		var save = {
			"reqHead": reqhead,
			"reqBody": {
				'invtyClsEncd': invtyClsEncd,
				'invtyEncd': invtyEncd,
				'invtyNm': invtyNm,
				'spcModel': spcModel,
				'invtyCd': invtyCd,
				'provrId': provrId,
				'measrCorpId': measrCorpId,
				'vol': vol,
				'weight': weight,
				'longs': longs,
				'wide': wide,
				'hght': hght,
				'bxRule': bxRule,
				'baoZhiQiEarWarn': baoZhiQiEarWarn,
				'baoZhiQiDt': baoZhiQiDt,
				'placeOfOrigin': placeOfOrigin,
				'rgstBrand': rgstBrand,
				'cretNum': cretNum,
				'makeLicsNum': makeLicsNum,
				'traySize': traySize,
				'quantity': quantity,
				'valtnMode': valtnMode,
				'iptaxRate': iptaxRate,
				'optaxRate': optaxRate,
				'loSellPrc': loSellPrc,
				'ltstCost': ltstCost,
				'refCost': refCost,
				'refSellPrc': refSellPrc,
				'isNtSell': isNtSell,
				'isNtPurs': isNtPurs,
				'isDomSale': isDomSale,
				'pto': pto,
				'isQuaGuaPer': isQuaGuaPer,
				'allowBomMain': allowBomMain,
				'allowBomMinor': allowBomMinor,
				'isNtBarCdMgmt': isNtBarCdMgmt,
				'crspdBarCd': crspdBarCd,
				'projDocInd': projDocInd,
				'setupPers': setupPers,
				'isNtDiscnt': isNtDiscnt,
				'shdTaxLabour': shdTaxLabour,
				'highestPurPrice': highestPurPrice,
				'memo': memo
			}
		}
		var saveJson = JSON.stringify(save);
		$.ajax({
			type: "post",
			url: url + "/mis/purc/InvtyDoc/insertInvtyDoc",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(msgAdd) {
				alert(msgAdd.respHead.message)
				if(msgAdd.respHead.isSuccess == true) {
					if(search == 1) {
						search1()
					} else if(search == 2) {
						search2()
					}
					$("#edit").hide()
					$("#mengban").hide()
				}
			},
			error: function() {
				alert("新增失败")
			},
		});
	}

}

function editData() {
	var invtyClsEncd = $("#select").val()
	var invtyEncd = $(".invtyEncd").val()
	var invtyNm = $(".invtyNm").val()
	var spcModel = $(".spcModel").val()
	var invtyCd = $(".invtyCd").val()
	var provrId = $(".provrId").val()
	var measrCorpId = $(".measrCorpId").val()
	var vol = $(".vol").val()
	var weight = $(".weight").val()
	var longs = $(".longs").val()
	var wide = $(".wide").val()
	var hght = $(".hght").val()
	var bxRule = $(".bxRule").val()
	var baoZhiQiEarWarn = $(".baoZhiQiEarWarn").val()
	var baoZhiQiDt = $(".baoZhiQiDt").val()
	var placeOfOrigin = $(".placeOfOrigin").val()
	var rgstBrand = $(".rgstBrand").val()
	var cretNum = $(".cretNum").val()
	var makeLicsNum = $(".makeLicsNum").val()
	var traySize = $(".traySize").val()
	var quantity = $(".quantity").val()
	var valtnMode = $(".valtnMode").val()
	var iptaxRate = $(".iptaxRate").val()
	var optaxRate = $(".optaxRate").val()
	var loSellPrc = $(".loSellPrc").val()
	var ltstCost = $(".ltstCost").val()
	var refCost = $(".refCost").val()
	var refSellPrc = $(".refSellPrc").val()
	var isNtSell = $(".isNtSell").val()
	var isNtPurs = $(".isNtPurs").val()
	var isDomSale = $(".isDomSale").val()
	var pto = $(".pto").val()
	var isQuaGuaPer = $(".isQuaGuaPer").val()
	var allowBomMain = $(".allowBomMain").val()
	var allowBomMinor = $(".allowBomMinor").val()
	var isNtBarCdMgmt = $(".isNtBarCdMgmt").val()
	var crspdBarCd = $(".crspdBarCd").val()
	var projDocInd = $(".projDocInd").val()
	var mdfr = localStorage.loginName;
	var isNtDiscnt = $(".isNtDiscnt").val();
	var shdTaxLabour = $(".shdTaxLabour").val();
	var highestPurPrice = $(".highestPurPrice").val();
	var memo = $(".memo").val()
	if(invtyClsEncd == null || invtyClsEncd == "") {
		alert("请选择存货分类")
	} else if(bxRule == null || bxRule == "") {
		alert("请输入箱规")
	} else if(measrCorpId == null || measrCorpId == "") {
		alert("请输入计量单位")
	} else if(baoZhiQiDt == null || baoZhiQiDt == "") {
		alert("请输入保质期天数")
	} else if(refCost == null || refCost == "") {
		alert("请输入参考成本")
	} else if(refSellPrc == null || refSellPrc == "") {
		alert("请输入参考售价")
	} else {
		var save = {
			"reqHead": reqhead,
			"reqBody": {
				'invtyClsEncd': invtyClsEncd,
				'invtyEncd': invtyEncd,
				'invtyNm': invtyNm,
				'spcModel': spcModel,
				'invtyCd': invtyCd,
				'provrId': provrId,
				'measrCorpId': measrCorpId,
				'vol': vol,
				'weight': weight,
				'longs': longs,
				'wide': wide,
				'hght': hght,
				'bxRule': bxRule,
				'baoZhiQiEarWarn': baoZhiQiEarWarn,
				'baoZhiQiDt': baoZhiQiDt,
				'placeOfOrigin': placeOfOrigin,
				'rgstBrand': rgstBrand,
				'cretNum': cretNum,
				'makeLicsNum': makeLicsNum,
				'traySize': traySize,
				'quantity': quantity,
				'valtnMode': valtnMode,
				'iptaxRate': iptaxRate,
				'optaxRate': optaxRate,
				'loSellPrc': loSellPrc,
				'ltstCost': ltstCost,
				'refCost': refCost,
				'refSellPrc': refSellPrc,
				'isNtSell': isNtSell,
				'isNtPurs': isNtPurs,
				'isDomSale': isDomSale,
				'pto': pto,
				'isQuaGuaPer': isQuaGuaPer,
				'allowBomMain': allowBomMain,
				'allowBomMinor': allowBomMinor,
				'isNtBarCdMgmt': isNtBarCdMgmt,
				'crspdBarCd': crspdBarCd,
				'projDocInd': projDocInd,
				'mdfr': mdfr,
				'isNtDiscnt': isNtDiscnt,
				'shdTaxLabour': shdTaxLabour,
				'highestPurPrice': highestPurPrice,
				'memo': memo
			}
		}
		var saveJson = JSON.stringify(save);
		$.ajax({
			type: "post",
			url: url + "/mis/purc/InvtyDoc/updateInvtyDocByInvtyDocEncd",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(msgAdd) {
				alert(msgAdd.respHead.message)
				if(msgAdd.respHead.isSuccess == true) {
					if(search == 1) {
						search1()
					} else if(search == 2) {
						search2()
					}
					$("#edit").hide()
				}
			},
			error: function(err) {
				alert("新增失败")
			},
		});
	}
}

//右列表格的操作
function editData2() {
	mType = 2;
	$("#edit").show();
	$("#mengban").show()
	$('h3').html('编辑存货档案  ');
	$(".cancel").click(function() {
		$("#edit").hide()
		$("#mengban").hide()
	})
	//获得行号
	var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowDatas = $("#jqgrids").jqGrid('getRowData', gr);
	var edit = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": rowDatas.invtyEncd
		}
	}
	var editJson = JSON.stringify(edit);
	$.ajax({
		type: "post",
		url: url + "/mis/purc/InvtyDoc/selectInvtyDocByInvtyDocEncd",
		async: true,
		data: editJson,
		dataType: 'json',
		contentType: 'application/json',
		error: function(err) {
			alert("详细信息查询失败")
		},
		success: function(data) {
			$("#select").val(data.respBody.invtyClsEncd);
			$(".invtyEncd").val(data.respBody.invtyEncd);
			$(".invtyNm").val(data.respBody.invtyNm);
			$(".spcModel").val(data.respBody.spcModel);
			$(".invtyCd").val(data.respBody.invtyCd);
			$(".measrCorpId").val(data.respBody.measrCorpId);
			$(".provrId").val(data.respBody.provrId);
			$(".vol").val(data.respBody.vol);
			$(".weight").val(data.respBody.weight);
			$(".longs").val(data.respBody.longs);
			$(".wide").val(data.respBody.wide);
			$('.hght').val(data.respBody.hght);
			$(".bxRule").val(data.respBody.bxRule);
			$(".baoZhiQiEarWarn").val(data.respBody.baoZhiQiEarWarn);
			$(".baoZhiQiDt").val(data.respBody.baoZhiQiDt);
			$(".placeOfOrigin").val(data.respBody.placeOfOrigin);
			$(".rgstBrand").val(data.respBody.rgstBrand);
			$(".cretNum").val(data.respBody.cretNum);
			$(".makeLicsNum").val(data.respBody.makeLicsNum);
			$(".traySize").val(data.respBody.traySize);
			$(".quantity").val(data.respBody.quantity);
			$(".iptaxRate").val(data.respBody.iptaxRate);
			$(".optaxRate").val(data.respBody.optaxRate);
			$(".loSellPrc").val(data.respBody.loSellPrc);
			$(".ltstCost").val(data.respBody.ltstCost);
			$(".refCost").val(data.respBody.refCost);
			$(".refSellPrc").val(data.respBody.refSellPrc);
			$(".isNtSell").val(data.respBody.isNtSell);
			$(".isNtPurs").val(data.respBody.isNtPurs);
			$(".isDomSale").val(data.respBody.isDomSale);
			$(".pto").val(data.respBody.pto);
			$(".isQuaGuaPer").val(data.respBody.isQuaGuaPer);
			$(".allowBomMain").val(data.respBody.allowBomMain);
			$(".allowBomMinor").val(data.respBody.allowBomMinor);
			$(".crspdBarCd").val(data.respBody.crspdBarCd);
			$(".projDocInd").val(data.respBody.projDocInd)
			$(".mdfr").val(data.respBody.mdfr);
			$(".pto").val(data.respBody.pto);
			$(".isNtDiscnt").val(data.respBody.isNtDiscnt);
			$(".shdTaxLabour").val(data.respBody.shdTaxLabour);
			$(".highestPurPrice").val();
			$(".memo").val(data.respBody.memo);
		},
	});
}
var invtyClsEncd2;
//按分类条件查询供应商档案
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
		//结束加载动画
		complete: function() {
			$(".zhezhao").css("display", "none");
			$("#loader").css("display", "none");
		},
		error: function() {
			alert("条件查询失败")
		}
	});
	$("#jqgrids").jqGrid('clearGridData')
}

function search2() {
	var rowNum1 = $("td select").val() //获取显示配置记录数量 
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
		success: function(data) {
			if(data.respHead.isSuccess == false) {
				alert(data.respHead.message)
			} else {
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
			}

		},
		error: function() {
			alert("条件查询失败")
		}
	});
	$("#jqgrids").jqGrid('clearGridData')
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
			var jstime = $("#jqgrids").getCell(ids[i], "invtyEncd");
			//建一个数组，把选中行的id添加到这个数组中去。
			rowData[i] = jstime;
		}
		var rowDatas = rowData.toString();

		if(rowDatas == 0) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			var deleteAjax = {
				"reqHead": reqhead,
				"reqBody": {
					"invtyEncd": rowDatas
				}
			}
			var deleteData = JSON.stringify(deleteAjax);
			$.ajax({
				type: "post",
				url: url + "/mis/purc/InvtyDoc/deleteInvtyDocList",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(data) {
					alert(data.respHead.message)
					if(data.respHead.isSuccess == true) {
						if(search == 1) {
							search1()
						} else if(search == 2) {
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

function importExcel(x) {
	var files = $("#FileUpload").val()
	var fileObj = document.getElementById("FileUpload").files[0];
	var formFile = new FormData();
	formFile.append("action", "UploadVMKImagePath");
	formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
	var data = formFile;
	if(files != "") {
		$.ajax({
			type: 'post',
			url: url + x,
			data: data,
			dataType: "json",
			cache: false, //上传文件无需缓存
			processData: false, //用于对data参数进行序列化处理 这里必须false
			contentType: false, //必须
			success: function(data) {
				alert(data.respHead.message)
				search2();
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
}

//导入
$(function() {
	$(".importExcel1").click(function() {
		var x = "/mis/purc/InvtyDoc/uploadInvtyDocFile"
		importExcel(x)
	});
	$(".importExcel").click(function() {
		var x = "/mis/purc/InvtyDoc/uploadInvtyDocFileU8"
		importExcel(x)
	});
});

//导出
$(document).on('click', '.exportExcel', function() {
	var invtyEncd1 = $(".invtyEncd1").val();
	var invtyNm1 = $(".invtyNm1").val();
	var invtyClsEncd1 = $(".invtyClsEncd1").val();
	var invtyClsNm1 = $(".invtyClsNm1").val();

	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": invtyEncd1,
			"invtyNm": invtyNm1,
			"invtyClsEncd": invtyClsEncd1,
			"invtyClsNm": invtyClsNm1
		}
	}
	var Data = JSON.stringify(savedata);
	$.ajax({
		url: url + '/mis/purc/InvtyDoc/printingInvtyDocList',
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
			var list = data.respBody.list;
			obj = list;
			for(var i = 0; i < obj.length; i++) {
				delete obj[i].invtyCls;
				delete obj[i].measrCorpDoc;
				delete obj[i].stat;
				delete obj[i].movBitList;
			}
			daochu(obj)
		},
		error: function() {
			alert("导出失败")
		}
	})

})

function daochu(JSONData) {
	var str = '存货分类编码,存货编码,存货名称,规格型号,存货代码,供应商编码,计量单位编码,体积,重量,长,宽,高,箱规,保质期预警天数,保质期天数,产地,注册商标,合格证号,生产许可证,托架尺寸,数量/拖,计价方式,进项税率,销项税率,最高进价,最低售价,最新成本,参考成本,参考售价,是否销售,是否采购,是否内销,PTO,是否保质期管理,允许BOM主件,允许BOM子件,是否条形码管理,对应条形码,项目档案标识,创建人,创建日期,修改人,修改日期,备注,计量单位名称,应税劳务,是否折扣,存货分类名称\n';

	for(let i = 0; i < JSONData.length; i++) {
		var result = '';
		if(JSONData[i].orderStatusc == '0') {
			result = "是";
		} else {
			result = "否";
		}
		for(let item in JSONData[i]) {
			if(JSONData[i][item] == null) {
				JSONData[i][item] = "";
			}
			str += `${JSONData[i][item] + '\t'},`;
		}
		str += '\n';
	}
	var blob = new Blob([str], {
		type: "text/plain;charset=utf-8"
	});
	//解决中文乱码问题
	blob = new Blob([String.fromCharCode(0xFEFF), blob], {
		type: blob.type
	});
	object_url = window.URL.createObjectURL(blob);
	var link = document.createElement("a");
	link.href = object_url;
	link.download = "存货档案.csv";
	document.body.appendChild(link);
	link.click();
	document.body.removeChild(link);
}