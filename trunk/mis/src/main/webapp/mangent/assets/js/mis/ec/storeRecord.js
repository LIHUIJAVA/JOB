//刚开始时可点击的按钮
$(function() {
	$('button').addClass("gray");
	$(".refer").removeClass("gray") //参照
	$('.addOrder').removeClass("gray") //增加
	$(".addWhs").removeClass("gray") //确定
	$(".cancel").removeClass("gray") //取消
	$(".find").removeClass("gray") //查询
	$("#find").removeClass("gray") //查询
	$("#findes").removeClass("gray") //查询
	$(".falses").removeClass("gray")
	$(".de").removeClass("gray")
	$(".saveOrder").addClass("gray")
	$(".print").removeClass("gray")
	$(".exportExcel").removeClass("gray")
	$(".importExcel").removeClass("gray")
	$(".falses").attr("disabled", false) //
	$(".gray").attr("disabled", true) //
})


$(function() {
	//页面加载完成之后执行	
	var pageNo = 1;
	var rowNum = 10;
});

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
			ecNum += "0" + ":" + "请选择" + ";";
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
//

//获取面审策略名称下拉框数据
function getGroupContractNum() {
	var GroupContractNum = "";
	var data2 = {
		reqHead,
		"reqBody": {
			"pageNo": 1,
			"pageSize": 500

		}
	}
	var postD2 = JSON.stringify(data2);
	$.ajax({
		url: url3 + "/mis/ec/auditStrategy/selectList",
		type: "post",
		async: false,
		dataType: 'json', //请求数据返回的类型。可选json,xml,txt
		data: postD2,
		contentType: 'application/json',
		success: function(result) {
			var result = result.respBody.list;
			GroupContractNum += "0" + ":" + "请选择" + ";";
			for(var i = 0; i < result.length; i++) {
				if(i != result.length - 1) {
					GroupContractNum += result[i].id + ":" + result[i].name + ";";
					ggg=result[i].id;
					ccc=result[i].name
				} else {
					GroupContractNum += result[i].id + ":" + result[i].name;
					ggg=result[i].id;
					ccc=result[i].name
				}
			}
		}
	});
	return GroupContractNum; //必须有此返回值		
}
var myData = {};
var mType;
//页面初始化
$(function() {
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
		//初始化表格
	allHeight()
	jQuery("#f_jqgrids").jqGrid({
		height: height,
		autoScroll:true,
		datatype: "local", //请求数据返回的类型。可选json,xml,txt
		shrinkToFit:false,
		colNames: ['店铺编码', '店铺名称', '电商平台编码', '电商平台名称', '免审策略编码',
		'免审策略名称', '销售类型', '业务类型', '佣金扣点编码', '佣金扣点名称', '发货模式', '默认退货仓名称', '默认退货仓编码', '安全库存',
		'结算方式', '客户编码', '客户名称', '负责部门编码','部门', '负责人编码', '负责人', '卖家会员号', '支付宝账号', '联系手机', '联系电话',
		'联系人', '邮箱地址', '备注'], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'storeId',
				align: "center",
				index: 'invdate',
				editable: true,
			},
			{
				name: 'storeName',
				align: "center",
				index: 'id',
				editable: true,
			},
			{
				name: 'ecId',
				align: "center",
				index: 'invdate',
				editable: false,
			},
			{
				name: 'ecName',
				align: "center",
				id: 'ecName',
				index: 'id',
				editable: true,
				edittype: 'select',
				editoptions: {
					value: getEcNum()
				}
			},
			{
				name: 'noAuditId',
				align: "center",
				index: 'invdate',
				editable: false,
				hidden:true
			},
			{
				name: 'noAuditName',
				align: "center",
				index: 'id',
				editable: true,
				edittype: 'select',
				editoptions: {
					value: getGroupContractNum()
				},
			},

			{
				name: 'salesType',
				align: "center",
				index: 'id',
				editable: true,
				edittype: 'select',
				formatter:'select',
				editoptions: {value: "0:请选择; 1:普通销售; 2:委托滞销" },
			},
			{
				name: 'business',
				align: "center",
				index: 'id',
				editable: true,
				edittype: 'select',
				formatter:'select',
				editoptions: {value: "0:请选择; 1:B2C; 2:B2B" },
			},
			{
				name: 'brokId',
				align: "center",
				index: 'id',
				editable: false,
			},
			{
				name: 'brokName',
				align: "center",
				index: 'id',
				editable: true,
				hidden: true,
			},
			{
				name: 'deliverMode',
				align: "center",
				index: 'id',
				editable: true,
				edittype: 'select',
//				formatter:'select',
				editoptions: {value: "0:请选择; 1:电商发货; 2:企业发货" },
			},
			{
				name: 'defaultRefWhsNm',
				align: "center",
				index: 'id',
				editable: false,
				hidden:true
			},
			{
				name: 'defaultRefWhs',
				align: "center",
				index: 'id',
				editable: true,
			},
			{
				name: 'safeInv',
				align: "center",
				index: 'id',
				editable: true,
				hidden: true,
			},
			{
				name: 'clearingForm',
				align: "center",
				index: 'id',
				editable: true,
				edittype: 'select',
//				formatter:'select',
				editoptions: {value: "0:请选择; 1:银行转账" },
			},
			{
				name: 'customerId',
				align: "center",
				index: 'id',
				editable: false,
			},
			{
				name: 'customerName',
				align: "center",
				index: 'id',
				editable: true,
			},
			{
				name: 'respDep',
				align: "center",
				index: 'id',
				editable: false,
			},
			{
				name: 'deptName',
				align: "center",
				index: 'id',
				editable: true,
//						hidden:true
			},
			{
				name: 'respPerson',
				align: "center",
				index: 'id',
				editable: false,
			},
			{
				name: 'personName',
				align: "center",
				index: 'id',
				editable: true,
//						hidden:true
			},
			{
				name: 'sellerId',
				align: "center",
				index: 'id',
				editable: true,
			},
			{
				name: 'alipayNo',
				align: "center",
				index: 'id',
				editable: true,
			},
			{
				name: 'mobile',
				align: "center",
				index: 'id',
				editable: true,
				editoptions : {maxlength : "11"}
			},
			{
				name: 'phone',
				align: "center",
				index: 'id',
				editable: true,
				editoptions : {maxlength : "11"}
			},
			{
				name: 'linkman',
				align: "center",
				index: 'id',
				editable: true,
			},
			{
				name: 'email',
				align: "center",
				index: 'id',
				editable: true,
			},
			{
				name: 'memo',
				align: "center",
				index: 'invdate',
				editable: true,
			},

		],
		autowidth: true,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 100,
		scrollrows:true,
		rowList: [100, 300, 500], //可供用户选择一页显示多少条
		sortname: 'id', //初始化的时候排序的字段
		pager : '#f_jqGridPager',//表格页脚的占位符(一般是div)的id
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		multiboxonly: true,
//				onSelectRow: EditSelectRow,
		//离开编辑状态
		afterRestoreCell: function(rowid, cellname, val, iRow, iCol) {
			$(".saveOrder").removeClass("gray");
			$('button').attr('disabled', false);
			$(".gray").attr('disabled', true)
		
		},
		gridComplete: function() {                  
			$("#f_jqgrids").hideCol("ecId");  
//			$("#f_jqgrids").hideCol("noAuditId");   
			$("#f_jqgrids").hideCol("brokId");            
		},
		caption: "店铺档案", //表格的标题名字	
		ondblClickRow: function(){
			if(mType == 1) {
				mType = 1
			} else {
				mType = 2;
			}
			$(".saveOrder").removeClass("gray")
			var gr = $("#f_jqgrids").jqGrid('getGridParam', 'selrow');//获取行id
			var rowDatas = $("#f_jqgrids").jqGrid('getRowData', gr);//获取行数据

			$("#f_jqgrids").setColProp("storeId",{editable:false});
			$("#f_jqgrids").editRow(gr,true);
			grr(gr)
		}

	});
	search4()

})



function grr(gr) {
	$('button').addClass("gray");
	$(".saveOrder").removeClass("gray");
	$(".save").removeClass("gray");
	$(".cancel").removeClass("gray");
	$(".find").removeClass("gray");
	$("#find").removeClass("gray");
	$(".true1").removeClass("gray");
	$(".true2").removeClass("gray");
	$(".false2").removeClass("gray");
	$(".true").removeClass("gray");
	$(".de").removeClass("gray");
	$(".importExcel").removeClass("gray");
	$(".exportExcel").removeClass("gray");
	$(".print").removeClass("gray");
	$(".falses").removeClass("gray");
	$(".falsees").removeClass("gray");
	$(".upOrder").removeClass("gray");
	$('button').attr('disabled', false);
	$(".addOrder").attr('disabled', true)
	$("#findes").attr('disabled', true)
	$('.addOrder').addClass("gray");
	$("#mengban").hide();
	$("#"+gr+"_deptName").on("dblclick", function() {
		$("#purchaseOrder").hide()
		$("#order_list").show()
		$("#wwrap").hide()
		$("#whs").hide()
		$("#order_list").css("opacity",1)
		$(".box").css("opacity",0)
		$(".box").hide()
	})
	$("#"+gr+"_personName").on("dblclick", function() {
		$("#find").removeClass("gray");
		$('#find').attr('disabled', false);
		$("#purchaseOrder").hide()
		$("#order_list").hide()
		$("#wwrap").hide()
		$("#whs").hide()
		$(".box").show()
		$(".box").css("opacity",1)
	})
	$("#"+gr+"_customerName").on("dblclick", function() {
		$("#find").removeClass("gray");
		$('#find').attr('disabled', false);
		$("#purchaseOrder").hide()
		$("#order_list").hide()
		$(".box").hide()
		$(".box").css("opacity",0)
		$("#whs").hide()
		$("#whs").css("opacity",0)
		$("#wwrap").show()
		$("#wwrap").css("opacity",1)
	})
	$("#"+gr+"_defaultRefWhs").on("dblclick", function() {
		$("#find").removeClass("gray");
		$('#find').attr('disabled', false);
		$("#purchaseOrder").hide()
		$("#order_list").hide()
		$(".box").hide()
		$(".box").css("opacity",0)
		$("#wwrap").hide()
		$("#wwrap").css("opacity",0)
		$("#whs").show()
		$("#whs").css("opacity",1)
	})
}


function IsCheckValue(storeId, storeName, ecName, noAuditName,salesType,deliverMode,clearingForm, business,respDep,respPerson,sellerId,customerId,mobile,phone,defaultRefWhs) {
	if(storeId == "") {
		alert("店铺编码不能为空")
		return false;
	} else if(storeName == "") {
		alert("店铺名称不能为空")
		return false;
	} else if(ecName == "请选择") {
		alert("电商平台名称未选择")
		return false;
	} else if(noAuditName == "请选择") {
		alert("免审策略名称未选择")
		return false;
	} else if(salesType == 0) {
		alert("销售类型未选择")
		return false;
	} else if(deliverMode == 0) {
		alert("发货模式未选择")
		return false;
	} else if(clearingForm == 0) {
		alert("结算方式未选择")
		return false;
	} else if(business == 0) {
		alert("业务类型未选择")
		return false;
	} else if(respDep == "") {
		alert("部门不能为空")
		return false;
	} else if(customerId == "") {
		alert("客户名称不能为空")
		return false;
	} else if(respPerson == "") {
		alert("负责人不能为空")
		return false;
	} else if(sellerId == "") {
		alert("买家会员号不能为空")
		return false;
	} else if(mobile.length < 11 && mobile.length > 0) {
		alert("联系手机格式错误")
		return false;
	} else if(phone.length < 11 && phone.length > 0) {
		alert("联系电话格式错误(区号加座机号)")
		return false;
	} else if(defaultRefWhs == '') {
		alert("默认退货仓不能为空")
		return false;
	}
	return true;
}


//打开部门档案后点击确定取消
$(function() {
	//确定
	$(".save").click(function() {
		var rowid;
		//	部门
		//	获得行号
		var gr = $("#order_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowDatas = $("#order_jqgrids").jqGrid('getRowData', gr);
		window.deptName = rowDatas.deptName
		$("input[name='deptName']").val(deptName)
		if(mType == 1) {
			rowid = newrowid
		} else if(mType == 2) {
			//获得行号
			rowid = $("#f_jqgrids").jqGrid('getGridParam', 'selrow');
		}
		$("#f_jqgrids").setRowData(rowid, {
			respDep: rowDatas.deptId
		})

		$("#order_list").css("opacity", 0);
		$("#purchaseOrder").show();
	})
	//	取消
	$(".cancel").click(function() {
		$("#order_list").css("opacity", 0);
		$("#purchaseOrder").show();
		$("#order_list").hide();
		$("#wwrap").hide()
		//到货单
		//获得行号
		var rowid = $("#f_jqgrids").jqGrid('getGridParam', 'selrow');
	})
	
	
	//	负责人
	//确定
	$(".true").click(function() {
		//获得行号
		var rowid;

		//	用户
		//	获得行号
		var ids = $("#user_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowData = $("#user_jqgrids").jqGrid('getRowData', ids);
		window.personName = rowData.userName
		$("input[name='personName']").val(personName)
		if(mType == 1) {
			rowid = newrowid
		} else if(mType == 2) {
			//获得行号
			rowid = $("#f_jqgrids").jqGrid('getGridParam', 'selrow');
		}
		$("#f_jqgrids").setRowData(rowid, {
			respPerson: rowData.accNum
		})

		$(".box").css("opacity", 0);
		$("#purchaseOrder").show();
	})
	//	取消
	$(".falses").click(function() {
		$(".box").css("opacity", 0);
		$("#purchaseOrder").show();
		$(".box").hide();
		$("#wwrap").hide()
		//获得行号
		var rowid = $("#f_jqgrids").jqGrid('getGridParam', 'selrow');
	})
	
	//	客户档案
	//确定
	$(".true1").click(function() {
		//获得行号
		var rowid;

		//	用户
		//	获得行号
		var ids = $("#cust_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowData = $("#cust_jqgrids").jqGrid('getRowData', ids);
		window.customerName = rowData.custNm
		$("input[name='customerName']").val(customerName)
		if(mType == 1) {
			rowid = newrowid
		} else if(mType == 2) {
			//获得行号
			rowid = $("#f_jqgrids").jqGrid('getGridParam', 'selrow');
		}
		$("#f_jqgrids").setRowData(rowid, {
			customerId: rowData.custId
		})

		$("#wwrap").css("opacity", 0);
		$("#purchaseOrder").show();
	})
	//	取消
	$(".falsees").click(function() {
		$("#wwrap").css("opacity", 0);
		$("#purchaseOrder").show();
		$("#wwrap").hide();
		$("#order_list").hide();
		$(".box").hide();
		//获得行号
		var rowid = $("#f_jqgrids").jqGrid('getGridParam', 'selrow');
	})
	
	//	仓库档案
	//确定
	$(".true2").click(function() {
		//获得行号
		var rowid;

		//	用户
		//	获得行号
		var ids = $("#whs_jqgrids").jqGrid('getGridParam', 'selrow');
		//获得行数据
		var rowData = $("#whs_jqgrids").jqGrid('getRowData', ids);
		
		$("input[name='defaultRefWhs']").val(rowData.whsEncd)
		if(mType == 1) {
			rowid = newrowid
		} else if(mType == 2) {
			//获得行号
			rowid = $("#f_jqgrids").jqGrid('getGridParam', 'selrow');
		}
		$("#f_jqgrids").setRowData(rowid, {
			defaultRefWhsNm: rowData.whsNm
		})

		$("#whs").css("opacity", 0);
		$("#purchaseOrder").show();
	})
	//	取消
	$(".false2").click(function() {
		$("#wwrap").css("opacity", 0);
		$("#whs").css("opacity", 0);
		$("#purchaseOrder").show();
		$("#whs").hide();
		$("#wwrap").hide();
		$("#order_list").hide();
		$(".box").hide();
		//获得行号
		var rowid = $("#f_jqgrids").jqGrid('getGridParam', 'selrow');
	})
})

// 点击保存，传送数据给后台
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
		$("#mengban").hide();
		mType = 1;
		var selectedId = $("#f_jqgrids").jqGrid("getGridParam", "selrow");
		var ids = $("#f_jqgrids").jqGrid('getDataIDs');

		// 获得当前最大行号（数据编码）
		var rowid = Math.max.apply(Math, ids);
		// 获得新添加行的行号（数据编码）
		newrowid = rowid + 1;
		var dataRow = {
			"storeId": "",
			"storeName": "",
			"ecId": "",
			"ecName": "",
			"noAuditId": "",
			"noAuditName": "",
			"salesType": "",
			"brokId": "",
			"brokName": "",
			"deliverMode": "",
			"safeInv": "",
			"clearingForm": "",
			"respDep": "",
			"respPerson": "",
			"sellerId": "",
			"alipayNo": "",
			"mobile": "",
			"phone": "",
			"linkman": "",
			"email": "",
			"business": "",
			"defaultRefWhs": "",
			"memo": "", //当前页数

		};
		$("#f_jqgrids").setColProp('storeId', {
			editable: true
		}); //设置editable属性由true改为false

		//将新添加的行插入到第一列
		$("#f_jqgrids").jqGrid("addRowData", newrowid, dataRow, "first");
		//设置grid单元格可编辑
		$('#f_jqgrids').jqGrid('editRow', newrowid, true);
		$("#f_jqgrids").jqGrid('setSelection',newrowid);
		grr(newrowid)
		$("#" + newrowid + "_customerName").attr("readonly", "readonly");
		$("#" + newrowid + "_deptName").attr("readonly", "readonly");
		$("#" + newrowid + "_personName").attr("readonly", "readonly");
		$("#" + newrowid + "_defaultRefWhs").attr("readonly", "readonly");
	})

})
//添加新数据
function addNewData() {
	//获得行号
	var gr = $("#f_jqgrids").jqGrid('getGridParam', 'selrow');
	
	//获得行数据
	var rowDatas = $("#f_jqgrids").jqGrid('getRowData', gr);
	
	var ecId=$("#" + gr + "_ecName option:selected").val();
	var ecName=$("#" + gr + "_ecName option:selected").text();
	var noAuditName=$("#" + gr + "_noAuditName option:selected").text();
	var noAuditId=$("#" + gr + "_noAuditName option:selected").val();
	
	
	
	var salesType=$("#" + gr + "_salesType option:selected").val();
	var deliverMode=$("#" + gr + "_deliverMode option:selected").val();
	var clearingForm=$("#" + gr + "_clearingForm option:selected").val();
	var business=$("#" + gr + "_business option:selected").val();
	
	var storeId = $("#" + gr + "_storeId").val()
	var storeName = $("#" + gr + "_storeName").val()
//	var ecName = $("input[name='ecName']").val()
	var brokId = $("input[name='brokId']").val()
	var brokName = $("input[name='brokName']").val()
	var safeInv = $("input[name='safeInv']").val()
	var respDep = rowDatas.respDep
	var customerId = rowDatas.customerId
	var respPerson = rowDatas.respPerson
	var customerId = rowDatas.customerId
	var personName = $("input[name='personName']").val()
	var customerName = $("input[name='customerName']").val()
	var sellerId = $("input[name='sellerId']").val()
	var alipayNo = $("input[name='alipayNo']").val()
	var mobile = $("input[name='mobile']").val()
	var phone = $("input[name='phone']").val()
	var linkman = $("input[name='linkman']").val()
	var email = $("input[name='email']").val()
	var defaultRefWhs = $("input[name='defaultRefWhs']").val()
	var memo = $("input[name='memo']").val()
	var deptName = $("input[name='deptName']").val()
	if(IsCheckValue(storeId, storeName, ecName, noAuditName,salesType,deliverMode,clearingForm, business,respDep,respPerson,sellerId,customerId,mobile,phone,defaultRefWhs) == true) {
		var save = {
			reqHead,
			"reqBody": {
				"storeId": storeId,
				"storeName": storeName,
				"ecId": ecId,
				"ecName": ecName,
				"noAuditId": noAuditId,
				"customerId": customerId,
				"customerName": customerName,
				"noAuditName": noAuditName,
				"personName": personName,
				"salesType":salesType,
				"brokId": brokId,
				"brokName": brokName,
				"deliverMode": deliverMode,
				"safeInv": safeInv,
				"clearingForm": clearingForm,
				"respDep": respDep,
				"respPerson": respPerson,
				"sellerId": sellerId,
				"alipayNo": alipayNo,
				"mobile": mobile,
				"phone": phone,
				"linkman": linkman,
				"email": email,
				"business": business,
				"deptName": deptName,
				"defaultRefWhs": defaultRefWhs,
				"memo": memo,
	
	
			}
		}
		var saveJson = JSON.stringify(save);
		$.ajax({
			type: "post",
			url: url3 + "/mis/ec/storeRecord/add",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(msgAdd) {
				mType = 0
				alert(msgAdd.respHead.message)
				$("#searchAll").trigger('click');
				$('#f_jqgrids').css("visibility", "true");
				$('button').addClass("gray");
				$('.addOrder').removeClass("gray") //增加
				$(".addWhs").removeClass("gray") //确定
				$(".cancel").removeClass("gray") //取消
				$(".find").removeClass("gray") //查询
				$("#mengban").show()
				$('button').attr('disabled', false);
				$(".gray").attr('disabled', true);
				$("#f_jqgrids").jqGrid().trigger("reloadGrid");
				search4()
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
	var gr = $("#f_jqgrids").jqGrid('getGridParam', 'selrow');
	
	//获得行数据
	var rowDatas = $("#f_jqgrids").jqGrid('getRowData', gr);
	
	var ecId=$("#" + gr + "_ecName option:selected").val();
	var ecName=$("#" + gr + "_ecName option:selected").text();
	var noAuditName=$("#" + gr + "_noAuditName option:selected").text();
	
	
	
	var salesType=$("#" + gr + "_salesType option:selected").val();
	var deliverMode=$("#" + gr + "_deliverMode option:selected").val();
	var clearingForm=$("#" + gr + "_clearingForm option:selected").val();
	var business=$("#" + gr + "_business option:selected").val();
	
	var storeId = rowDatas.storeId
//	var storeId = $("#" + gr + "_storeId").val()
	var noAuditId = rowDatas.noAuditId
	var storeName = $("#" + gr + "_storeName").val()
//	var ecName = $("input[name='ecName']").val()
	var brokId = $("input[name='brokId']").val()
	var brokName = $("input[name='brokName']").val()
	var safeInv = $("input[name='safeInv']").val()
	var respDep = rowDatas.respDep
	var customerId = rowDatas.customerId
	var respPerson = rowDatas.respPerson
	var personName = $("input[name='personName']").val()
	var customerName = $("input[name='customerName']").val()
	var sellerId = $("input[name='sellerId']").val()
	var alipayNo = $("input[name='alipayNo']").val()
	var mobile = $("input[name='mobile']").val()
	var phone = $("input[name='phone']").val()
	var linkman = $("input[name='linkman']").val()
	var email = $("input[name='email']").val()
	var defaultRefWhs = $("input[name='defaultRefWhs']").val()
	var memo = $("input[name='memo']").val()
	var deptName = $("input[name='deptName']").val()
	if(IsCheckValue(storeId, storeName, ecName, noAuditName,salesType,deliverMode,clearingForm, business,respDep,respPerson,sellerId,customerId,mobile,phone,defaultRefWhs) == true) {
		var save = {
			reqHead,
			"reqBody": {
				"storeId": storeId,
				"storeName": storeName,
				"ecId": ecId,
				"ecName": ecName,
				"personName": personName,
				"noAuditId": noAuditId,
				"noAuditName": noAuditName,
				"salesType":salesType,
				"brokId": brokId,
				"brokName": brokName,
				"customerId": customerId,
				"customerName": customerName,
				"deliverMode": deliverMode,
				"safeInv": safeInv,
				"clearingForm": clearingForm,
				"respDep": respDep,
				"respPerson": respPerson,
				"deptName": deptName,
				"sellerId": sellerId,
				"alipayNo": alipayNo,
				"mobile": mobile,
				"phone": phone,
				"linkman": linkman,
				"email": email,
				"business": business,
				"defaultRefWhs": defaultRefWhs,
				"memo": memo,
			}
		}
		var saveJson = JSON.stringify(save);
		$.ajax({
			type: "post",
			url: url3 + "/mis/ec/storeRecord/edit",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(editMsg) {
				mType = 0
				alert(editMsg.respHead.message);
				$("#findes").removeClass("gray") //取消
				$(".saveOrder").addClass("gray") //取消
				$("#mengban").show()
				$("#f_jqgrids").jqGrid().trigger("reloadGrid");
				search4()
				$(".addOrder").removeClass("gray")
				$(".addOrder").attr("disabled", false)
				$(".saveOrder").attr("disabled", false)
				$("#findes").attr("disabled", false)
			},
			error: function() {
				console.log("更新失败")
			}
		});
	}
}





//查询按钮
$(document).on('click', '#findes', function() {
	search4()
})


//条件查询
function search4() {
	var storeId = $("input[name='storeId']").val();
	var storeName = $("input[name='storeName']").val();
	var ecName = $("input[name='ecName']").val();
	var data2 = {
		reqHead,
		"reqBody": {
			"storeId": storeId,
			"storeName": storeName,
			"ecName": ecName,
			"pageNo": 1,
			"pageSize": 500

		}

	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		url: url3 + "/mis/ec/storeRecord/queryList",
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
			var rowNum = $("#_input").val()
			var list = data.respBody.list;
			myDate = list;
			
			for(var i =0;i<myDate.length;i++) {
				if(myDate[i].deliverMode == 1) {
					myDate[i].deliverMode = '电商发货'
				} else if(myDate[i].deliverMode == 2) {
					myDate[i].deliverMode = '企业发货'
				}
				if(myDate[i].clearingForm == 1) {
					myDate[i].clearingForm = '银行转账'
				}
			}
			
			$("#f_jqgrids").jqGrid('clearGridData');
			$("#f_jqgrids").jqGrid('setGridParam', {
				rowNum: rowNum,
				datatype: 'local',
				data: myDate, //newData是符合格式要求的重新加载的数据
				page: 1 //哪一页的值
			}).trigger("reloadGrid")
			
			
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
//删除行
$(function() {
	$(".de").click(function() {
		var gr = $("#f_jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
		var rowDatas = $("#f_jqgrids").jqGrid('getRowData', gr); //获取行数据    	
		var storeId = $("input[name='storeId']").val();

		var deleteAjax = {
			reqHead,
			"reqBody": {
				"storeId": rowDatas.storeId,
			}
		};
		var deleteData = JSON.stringify(deleteAjax)
		if(gr == null) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url3 + "/mis/ec/storeRecord/delete",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(remover) {
					alert(remover.respHead.message);
					
					$("#f_jqgrids").jqGrid().trigger("reloadGrid");
					search4()
					$("#searchAll").trigger('click')
					$('#f_jqgrids').css("visibility", "true");
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
