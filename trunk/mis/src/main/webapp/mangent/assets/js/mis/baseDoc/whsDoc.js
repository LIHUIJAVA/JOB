//省市互联
//请求所有的省
$(function() {
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"pageNo": 1,
			"pageSize": 9999999
		}
	};
	var postData = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/whs/whs_doc/selectProvinces',
		type: 'post',
		data: postData,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		error: function() {
		}, //错误执行方法
		success: function(data) {
			var provinceList = data.respBody.list;

			var province = $("#province"),
				city = $("#city"),
				town = $("#town");
			for(var i = 0; i < provinceList.length; i++) {
				addEle(province, provinceList[i].codeName, provinceList[i].codeId);
			}

			function addEle(ele, value, id) {
				var optionStr = "";
				optionStr = "<option value=" + id + ">" + value + "</option>";
				ele.append(optionStr);
			}

			function removeEle(ele) {
				ele.find("option").remove();
				var optionStar = "<option value=" + "请选择" + ">" + "请选择" + "</option>";
				ele.append(optionStar);
			}
			//选择省出来对应的市
			province.on("change", function() {
				codeId = $(this).val();
				removeEle(city);
				removeEle(town);
				var data = {
					"reqHead": reqhead,
					"reqBody": {
						"pageNo": 1,
						"pageSize": 9999999,
						"codeId": codeId
					}
				};
				var postData = JSON.stringify(data);
				$.ajax({
					url: url + '/mis/whs/whs_doc/selectCities',
					type: 'post',
					data: postData,
					dataType: 'json',
					async: true,
					contentType: 'application/json;charset=utf-8',
					error: function() {
					}, //错误执行方法
					success: function(data) {
						var citiesList = data.respBody.list;
						var city = $("#city");
						for(var i = 0; i < citiesList.length; i++) {
							addEle(city, citiesList[i].codeName, citiesList[i].codeId);
						}

						function addEle(ele, value, id) {
							var optionStr = "";
							optionStr = "<option value=" + id + ">" + value + "</option>";
							ele.append(optionStr);
						}

						function removeEle(ele) {
							ele.find("option").remove();
							var optionStar = "<option value=" + "请选择" + ">" + "请选择" + "</option>";
							ele.append(optionStar);
						}

						//选择市出来对应的县
						city.on("change", function() {
							codeId = $(this).val();
							var data = {
								"reqHead": reqhead,
								"reqBody": {
									"pageNo": 1,
									"pageSize": 9999999,
									"codeId": codeId
								}
							};
							var postData = JSON.stringify(data);
							$.ajax({
								url: url + '/mis/whs/whs_doc/selectCounties',
								type: 'post',
								data: postData,
								dataType: 'json',
								async: true,
								contentType: 'application/json;charset=utf-8',
								error: function() {
								}, //错误执行方法
								success: function(data) {
									var citiesList = data.respBody.list;
									var city = $("#town");
									for(var i = 0; i < citiesList.length; i++) {
										addEle(city, citiesList[i].codeName);
									}

									function addEle(ele, value) {
										var optionStr = "";
										optionStr = "<option value=" + value + ">" + value + "</option>";
										ele.append(optionStr);
									}

									function removeEle(ele) {
										ele.find("option").remove();
										var optionStar = "<option value=" + "请选择" + ">" + "请选择" + "</option>";
										ele.append(optionStar);
									}
								}

							})
						});

					}
				})
			})
		}
	})
})
//弹框
$(function() {
	$(".biaoge").click(function() {
		window.open("../../Components/baseDoc/userList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	})
	$(".bm_biaoge").click(function() {
		window.open("../../Components/baseDoc/deptDocList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	})
	$(".zc_biaoge").click(function() {
		window.open("../../Components/baseDoc/insertRealWhsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	})
})
//取当前登陆人
$(function() {
	$("#userName").val(localStorage.loginName)
})
//计价方式下拉选择框
$(function() {
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"pageNo": 1,
			"pageSize": 9999999
		}
	};
	var postData = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/whs/whs_doc/selectValtnMode',
		type: 'post',
		data: postData,
		dataType: 'json',
		async: false,
		contentType: 'application/json;charset=utf-8',
		error: function() {
		}, //错误执行方法
		success: function(data) {

			var valtnModeList = data.respBody.list;

			var valtnMode = $(".valtnMode")
			for(var i = 0; i < valtnModeList.length; i++) {
				addEle(valtnMode, valtnModeList[i].valtnModeNm);
			}

			function addEle(ele, value) {
				var optionStr = "";
				optionStr = "<option>" + value + "</option>";
				ele.append(optionStr);
			}

			function removeEle(ele) {
				ele.find("option").remove();
				var optionStar = "<option value=" + "请选择" + ">" + "请选择" + "</option>";
				ele.append(optionStar);
			}

		}
	})
})
//仓库属性下拉选择框
$(function() {
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"pageNo": 1,
			"pageSize": 9999999
		}
	};
	var postData = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/whs/whs_doc/selectWhsAttr',
		type: 'post',
		data: postData,
		dataType: 'json',
		async: false,
		contentType: 'application/json;charset=utf-8',
		error: function() {
		}, //错误执行方法
		success: function(data) {

			var whsAttrList = data.respBody.list;

			var whsAttr = $(".whsAttr")
			for(var i = 0; i < whsAttrList.length; i++) {
				addEle(whsAttr, whsAttrList[i].whsAttrNm);
			}

			function addEle(ele, value) {
				var optionStr = "";
				optionStr = "<option>" + value + "</option>";
				ele.append(optionStr);
			}

			function removeEle(ele) {
				ele.find("option").remove();
				var optionStar = "<option value=" + "请选择" + ">" + "请选择" + "</option>";
				ele.append(optionStar);
			}

		}
	})
})
//可用量控制方式下拉选择框
$(function() {
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"pageNo": 1,
			"pageSize": 9999999
		}
	};
	var postData = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/whs/whs_doc/selectAMode',
		type: 'post',
		data: postData,
		dataType: 'json',
		async: false,
		contentType: 'application/json;charset=utf-8',
		error: function() {
		}, //错误执行方法
		success: function(data) {

			var avalQtyCtrlNmList = data.respBody.list;

			var invtyAvalQtyCtrlMode = $(".invtyAvalQtyCtrlMode")
			for(var i = 0; i < avalQtyCtrlNmList.length; i++) {
				addEle(invtyAvalQtyCtrlMode, avalQtyCtrlNmList[i].avalQtyCtrlNm);
			}

			var sellAvalQtyCtrlMode = $(".sellAvalQtyCtrlMode")
			for(var i = 0; i < avalQtyCtrlNmList.length; i++) {
				addEle(sellAvalQtyCtrlMode, avalQtyCtrlNmList[i].avalQtyCtrlNm);
			}

			function addEle(ele, value) {
				var optionStr = "";
				optionStr = "<option>" + value + "</option>";
				ele.append(optionStr);
			}

			function removeEle(ele) {
				ele.find("option").remove();
				var optionStar = "<option value=" + "请选择" + ">" + "请选择" + "</option>";
				ele.append(optionStar);
			}
		}
	})
})

var count;
var pages;
var page = 1;
var rowNum;
//右列表格的操作
$(function() {
	pageInit();
})
var mType = 0;

function pageInit() {
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	allHeight()
	jQuery("#jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['仓库编码', '仓库名称', '部门名称', '仓库地址', '总仓', '电话', '负责人'], //jqGrid的列显示名字
		colModel: [{
				name: 'whsEncd',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: "whsNm",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: "deptName",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: "whsAddr",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: "realWhs",
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
				name: 'princ',
				align: "center",
				editable: true,
				sortable: false
			}
		],
		autowidth: true,
		height: height,
		autoScroll: true,
		shrinkToFit: false,
		rowNum: 100, //一页显示多少条
		rownumbers: true,
		loadonce: false,
		forceFit: true,
		rowList: [100, 300, 500],
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		multiboxonly: true,
		caption: "仓库档案", //表格的标题名字
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
			search()
		},
		ondblClickRow: function() {
			//获得行号
			var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow');
			//获得行数据
			var rowDatas = $("#jqgrids").jqGrid('getRowData', gr);
			var whsEncd = rowDatas.whsEncd;
			if(whsEncd==""||whsEncd==null){
				alert("请选择正确单据")
			}else{
				ondbClick();
			}
			mType = 2;
		}
	});
}

//条件查询
$(document).on('click', '.find', function() {
	search()
})

function search() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var whsEncd = $(".whsEncd").val();
	var whsNm = $(".whsNm1").val();
	var deptEncd = $(".deptEncd").val();
	var Data = {
		"reqHead": reqhead,
		"reqBody": {
			"whsEncd": whsEncd,
			"whsNm": whsNm,
			"deptEncd": deptEncd,
			"pageNo": page,
			"pageSize": rowNum
		}
	}
	var changeData = JSON.stringify(Data)
	$.ajax({
		type: "post",
		url: url + "/mis/whs/whs_doc/queryList",
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
			var mydata = {};
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#jqgrids").jqGrid("clearGridData");
			$("#jqgrids").jqGrid("setGridParam", {
				data: mydata.rows,
				jsonReader: {
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

$(function() {
	$("#edit .cancel").click(function() {
		$("#edit").hide();
	});
})
//单选框后台默认
function initradio(rName, rValue) {
	var rObj = document.getElementsByName(rName);
	for(var i = 0; i < rObj.length; i++) {
		if(rObj[i].value == rValue) {
			rObj[i].checked = 'checked';
		}
	}
}
var list;
function ondbClick() {
	$("h3").html("编辑仓库档案")
	$("#edit").show();

	//获得行号
	var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowDatas = $("#jqgrids").jqGrid('getRowData', gr);
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			'whsEncd': rowDatas.whsEncd,
		}
	};
	var postData = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/whs/whs_doc/selectWhsDocList',
		type: 'post',
		data: postData,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		error: function() {
			alert("获取失败")
		}, //错误执行方法
		success: function(data) {
			list = data.respBody.list[0];
			$("#whsEncdDoc").val(list.whsEncd);
			$("#whsEncdDoc").attr("readonly", "readonly")
			$("#whsNmDoc").val(list.whsNm);
			$("#deptName").val(list.deptName);
			$("#deptId").val(list.deptEncd);
			$(".tel").val(list.tel);
			$(".princ").val(list.princ);
			$(".valtnMode").val(list.valtnMode);
			
			$("input[name='whsNm']").val(list.whsNm);
			$("input[name='whsAddr']").val(list.whsAddr);
			$("input[name='crspdBarCd']").val(list.crspdBarCd);
			$("select[name='isNtPrgrGdsBitMgmt']").val(list.isNtPrgrGdsBitMgmt);
			$("select[name='isNtShop']").val(list.isNtShop);
			$("select[name='dumyWhs']").val(list.dumyWhs);
			$("select[name='whsAttr']").val(list.whsAttr);
			
			$(".sellAvalQtyCtrlMode").val(list.sellAvalQtyCtrlMode);
			$(".invtyAvalQtyCtrlMode").val(list.invtyAvalQtyCtrlMode);
			$(".memo").val(list.memo);
			$("input[name='realWhs']").val(list.realWhs);
			$(".stpUseDt").val(list.stpUseDt);
			$("#prov").text(list.prov);
			$("#cty").text(list.cty);
			$("#cnty").text(list.cnty);
			$(".setupPers").val(list.setupPers);
			$(".valtnModel").val(list.valtnModeNm);
		}
	});
}
//增加仓库档案
$(function() {
	$(".addOrder").click(function() {
		$(".saveOrder").removeClass("gray")
		$(".saveOrder").attr("disabled", false)
		mType = 1;
		$("h3").html("新增仓库档案")
		$("#edit").show();
		$("#edit input").val("");
		$("#edit select").val("");
	})
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


function IsCheckValue(whsEncd, whsNm, whsAddr, princ, deptName, tel, prov, cty, cnty, dumyWhs, realWhs) {
	if(whsEncd == '') {
		alert("仓库编号不能为空")
		return false;
	} else if(whsNm == '') {
		alert("仓库名称不能为空")
		return false;
	} else if(whsAddr == '') {
		alert("仓库地址不能为空")
		return false;
	} else if(princ == '') {
		alert("负责人不能为空")
		return false;
	} else if(deptName == '') {
		alert("部门编码不能为空")
		return false;
	} else if(tel == '') {
		alert("电话不能为空")
		return false;
	} else if(dumyWhs == 1 && realWhs == '') {
		alert("总仓编码不能为空")
		return false;
	}
	return true;
}




function editData() {
	var crspdBarCd = $("#crspdBarCd").val();

	var whsEncd = $("#whsEncdDoc").val();
	var whsNm = $("#whsNmDoc").val();
	var stpUseDt = $("#stpUseDt").val();
	var deptName = $("#deptName").val();
	var deptEncd = $("#deptId").val();
	var tel = $("#tel").val();
	var setupPers = localStorage.loginName;
	var whsAddr = $(".whsAddr").val();
	var princ = $(".princ").val();
	var memo = $(".memo").val();

	var prov1 = $("select[name='province'] option:selected").text();
	var cty1 = $("select[name='city'] option:selected").text();
	var cnty1 = $("select[name='town'] option:selected").text();
	var prov = $("select[name='province']").val();
	var cty = $("select[name='city']").val();
	var cnty = $("select[name='town']").val();
	var whsAttr = $("select[name='whsAttr']").val();
	var valtnMode = $("select[name='valtnMode']").val();
	var sellAvalQtyCtrlMode = $("select[name='sellAvalQtyCtrlMode']").val();
	var invtyAvalQtyCtrlMode = $("select[name='invtyAvalQtyCtrlMode']").val();

	var isNtPrgrGdsBitMgmt = $("select[name='isNtPrgrGdsBitMgmt']").val();
	var isNtShop = $("select[name='isNtShop']").val();
	var dumyWhs = $("select[name='dumyWhs']").val();
	var realWhs = $("input[name='realWhs']").val();
	console.log(prov,cty,cnty)
	if(IsCheckValue(whsEncd, whsNm, whsAddr, princ, deptName, tel, prov, cty, cnty, dumyWhs, realWhs) == true) {
		
		var save = {
			"reqHead": reqhead,
			"reqBody": {
				'whsEncd': whsEncd,
				'whsNm': whsNm,
				'realWhs': realWhs,
				'deptEncd': deptEncd,
				'whsAddr': whsAddr,
				'tel': tel,
				'princ': princ,
				'valtnMode': valtnMode,
				'crspdBarCd': crspdBarCd,
				'isNtPrgrGdsBitMgmt': isNtPrgrGdsBitMgmt,
				'whsAttr': whsAttr,
				'sellAvalQtyCtrlMode': sellAvalQtyCtrlMode,
				'invtyAvalQtyCtrlMode': invtyAvalQtyCtrlMode,
				'memo': memo,
				'isNtShop': isNtShop,
				'stpUseDt': stpUseDt,
				'prov': prov1,
				'cty': cty1,
				'cnty': cnty1,
				'dumyWhs': dumyWhs,
				'setupPers': setupPers,
			}
		}
		var saveJson = JSON.stringify(save);
		$.ajax({
			type: "post",
			url: url + "/mis/whs/whs_doc/updateWhsDoc",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(msgAdd) {
				alert(msgAdd.respHead.message)
				if(msgAdd.respHead.isSuccess==true){
					search();
					$("#edit").hide();
				}
			},
			error: function(err) {
				alert("更新失败")
			},
		});
	}

}

function addData() {
	var crspdBarCd = $("#crspdBarCd").val();

	var whsEncd = $("#whsEncdDoc").val();
	var whsNm = $("#whsNmDoc").val();
	var stpUseDt = $("#stpUseDt").val();
	var deptName = $("#deptName").val();
	var deptEncd = $("#deptId").val();
	var tel = $("#tel").val();
	var setupPers = localStorage.loginName;
	var whsAddr = $(".whsAddr").val();
	var princ = $(".princ").val();
	var memo = $(".memo").val();

	var prov1 = $("select[name='province'] option:selected").text();
	var cty1 = $("select[name='city'] option:selected").text();
	var cnty1 = $("select[name='town'] option:selected").text();
	var prov = $("select[name='province']").val();
	var cty = $("select[name='city']").val();
	var cnty = $("select[name='town']").val();
	var whsAttr = $("select[name='whsAttr']").val();
	var valtnMode = $("select[name='valtnMode']").val();
	var sellAvalQtyCtrlMode = $("select[name='sellAvalQtyCtrlMode']").val();
	var invtyAvalQtyCtrlMode = $("select[name='invtyAvalQtyCtrlMode']").val();

	var isNtPrgrGdsBitMgmt = $("select[name='isNtPrgrGdsBitMgmt']").val();
	var isNtShop = $("select[name='isNtShop']").val();
	var dumyWhs = $("select[name='dumyWhs']").val();
	var realWhs = $("input[name='realWhs']").val();
	console.log(prov,cty,cnty)
	if(IsCheckValue(whsEncd, whsNm, whsAddr, princ, deptName, tel, prov, cty, cnty, dumyWhs, realWhs) == true) {
		
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				'crspdBarCd': crspdBarCd,
				'whsEncd': whsEncd,
				'whsNm': whsNm,
				'realWhs': realWhs,
				'deptEncd': deptEncd,
				'whsAddr': whsAddr,
				'tel': tel,
				'princ': princ,
				'valtnMode': valtnMode,
				'isNtPrgrGdsBitMgmt': isNtPrgrGdsBitMgmt,
				'whsAttr': whsAttr,
				'sellAvalQtyCtrlMode': sellAvalQtyCtrlMode,
				'invtyAvalQtyCtrlMode': invtyAvalQtyCtrlMode,
				'memo': memo,
				'isNtShop': isNtShop,
				'stpUseDt': stpUseDt,
				'prov': prov1,
				'cty': cty1,
				'cnty': cnty1,
				'dumyWhs': dumyWhs,
				'setupPers': setupPers,
			}
		};
		var postData = JSON.stringify(data);
		$.ajax({
			url: url + '/mis/whs/whs_doc/insertWhsDoc',
			type: 'post',
			data: postData,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			success: function(msgAdd) {
				console.log(msgAdd)
				if(msgAdd.respHead.isSuccess==true){
					search();
					$("#edit").hide();
				}
				alert(msgAdd.respHead.message)
			},
			error: function() {
				alert(error);
			}
		})
	}

}

//删除仓库档案
$(function() {
	$(".delOrder").click(function() {
		//获得选中行的行号
		var ids = $('#jqgrids').jqGrid('getGridParam', 'selarrrow');

		//获取选择行的provrId
		var rowData = [];
		for(var i = 0; i < ids.length; i++) {
			var gr = $("#jqgrids").getGridParam('selrow');
			//选中行的id
			var jstime = $("#jqgrids").getCell(ids[i], "whsEncd");
			//建一个数组，把选中行的id添加到这个数组中去。
			rowData[i] = jstime;
		}
		var rowDatas = rowData.toString();

		var deleteAjax = {
			"reqHead": reqhead,
			"reqBody": {
				"whsEncd": rowDatas
			}
		}
		var deleteData = JSON.stringify(deleteAjax);
		if(ids.length == 0) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + "/mis/whs/whs_doc/deleteWDocList",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(remover) {
					alert(remover.respHead.message)
					if(remover.respHead.isSuccess==true){
						search();
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
				url: url + "/mis/whs/whs_doc/uploadFileAddDb",
				data: data,
				dataType: "json",
				cache: false, //上传文件无需缓存
				processData: false, //用于对data参数进行序列化处理 这里必须false
				contentType: false, //必须
				success: function(data) {
					alert(data.respHead.message)
					if(data.respHead.isSuccess==true){
						search();
					}
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

//导出--url不对
$(document).on('click', '.exportExcel', function() {
	var data = {
		"reqHead": reqhead,
		"reqBody": {}
	}
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + "/mis/whs/whs_doc/queryListDaYin",
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
			var list = data.respBody.list;
			obj = list;
			for(var i = 0; i < obj.length; i++) {
				delete obj[i].ordrNum;
				delete obj[i].movBitList;
			}
			daochu(obj)
		},
		error: function() {
			alert("error")
		}
	})

})

function daochu(JSONData) {
    var str = '仓库编码,仓库名称,部门编码,部门名称,仓库地址,电话,负责人,计价方式,对应条形码,是否进行货位管理,仓库属性,销售可用量控制方式,库存可用量控制方式,备注,是否门店,停用时间,省,市,县,是否虚拟仓,创建人,创建时间,修改人,修改时间,销售类型编码,\n';

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
    link.download =  "仓库档案.csv";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}