//保留2位小数且带千分位
var prec = 2;

function precision(num, length) {
	return(num.toFixed(length) + '').replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g, '$&,');
}

function delcommafy(num) {　　
	if(num && num != 'undefined' && num != 'null') {　　　　
		let numS = num;　　　　
		numS = numS.toString();　　　　
		numS = numS.replace(/,/gi, '');　　　　
		return numS;　　
	} else {　　　　
		return num;　　
	}
}

function dev(obj) {
	var allEncd = obj.doc1.val();
	if(allEncd == "") {
		obj.doc2.val("")
	}
	var showData = {
		"reqHead": reqHead,
		"reqBody": obj.showData
	};
	var postData = JSON.stringify(showData);
	$.ajax({
		type: "post",
		url: obj.url, //列表
		async: true,
		data: postData,
		dataType: 'json',
		contentType: 'application/json',
		success: obj.afunction
	})
}

$(function() {
	//1.存货
	$(document).on('keypress', '#invtyEncd', function(event) {
		if(event.keyCode == '13') {
			$('#invtyEncd').blur();
		}
	})

	$(document).on('blur', '#invtyEncd', function() {
		var invtyEncd = $("#invtyEncd").val();
		dev({
			doc1: $("#invtyEncd"),
			doc2: $("#invtyNm"),
			showData: {
				"invtyEncd": invtyEncd
			},
			afunction: function(data) {
				var invtyNm = data.respBody.invtyNm;
				$("#invtyNm").val(invtyNm);
				if(data.respHead.isSuccess == false && invtyEncd != "") {
					alert("此存货不存在")
					$("#invtyNm").val("");
					$("#invtyEncd").val("");
				}
			},
			url: url + "/mis/purc/InvtyDoc/selectInvtyDocByInvtyDocEncd"
		})
	});
	//2.仓库
	$(document).on('keypress', '#whsEncd', function(event) {
		if(event.keyCode == '13') {
			$('#whsEncd').blur();
		}
	})

	$(document).on('blur', '#whsEncd', function() {
		var whsEncd = $("#whsEncd").val();
		if(whsEncd != '') {
			dev({
				doc1: $("#whsEncd"),
				doc2: $("#whsNm"),
				showData: {
					"whsEncd": whsEncd
				},
				afunction: function(data) {
					if(data.respHead.isSuccess == true && whsEncd != "") {
						var whsNm = data.respBody.list[0].whsNm;
						$("#whsNm").val(whsNm)
					} else if(data.respHead.isSuccess == false && whsEncd != "") {
						alert("此仓库不存在")
						$("#whsNm").val("");
						$("#whsEncd").val("");
					}
				},
				url: url + "/mis/whs/whs_doc/selectWhsDocList"
			})
		} else if(whsEncd == '') {
			$("#whsNm").val("");
		}
	});
	//2.店铺
	$(document).on('keypress', '#storeId', function(event) {
		if(event.keyCode == '13') {
			$('#storeId').blur();
		}
	})
	$(document).on('keypress', '#storeId1', function(event) {
		if(event.keyCode == '13') {
			$('#storeId1').blur();
		}
	})

	$(document).on('blur', '#storeId', function() {
		var storeId = $("#storeId").val();
		dev({
			doc1: $("#storeId"),
			doc2: $("#storeNm"),
			doc2: $("#storeName"),
			showData: {
				"storeId": storeId
			},
			afunction: function(data) {
				var storeNm = data.respBody.storeName;
				$("#storeNm").val(storeNm)
				var storeName = data.respBody.storeName;
				$("#storeName").val(storeName)
				if(data.respHead.isSuccess == false && storeId != "") {
					alert("此店铺不存在")
					$("#storeNm").val("");
					$("#storeName").val("");
					$("#storeId").val("");
				}
			},
			url: url + "/mis/ec/storeRecord/query"
		})
	});
	//	$(document).on('blur', '#storeId', function() {
	//		var storeId = $("#storeId").val();
	//		dev({
	//			doc1: $("#storeId"),
	//			doc2: $("#storeName"),
	//			showData: {
	//				"storeId": storeId
	//			},
	//			afunction: function(data) {
	//				var storeName = data.respBody.storeName;
	//				$("#storeName").val(storeName)
	//				if(data.respHead.isSuccess == false && storeId != "") {
	//					alert("此店铺不存在")
	//					$("#storeName").val("");
	//					$("#storeId").val("");
	//				}
	//			},
	//			url: url + "/mis/ec/storeRecord/query"
	//		})
	//	});
	$(document).on('blur', '#storeId1', function() {
		var storeId = $("#storeId1").val();
		dev({
			doc1: $("#storeId1"),
			doc2: $("#storeName1"),
			showData: {
				"storeId": storeId
			},
			afunction: function(data) {
				var storeName = data.respBody.storeName;
				$("#storeName1").val(storeName)
				if(storeId == '') {
					$("#storeName1").val('')
				}
			},
			url: url + "/mis/ec/storeRecord/query"
		})
	});
	//3.快递公司
	$(document).on('keypress', '#expressId', function(event) {
		if(event.keyCode == '13') {
			$('#expressId').blur();
		}
	})

	$(document).on('blur', '#expressId', function() {
		var expressId = $("#expressId").val();
		dev({
			doc1: $("#expressId"),
			doc2: $("#expressName"),
			showData: {
				"expressEncd": expressId
			},
			afunction: function(data) {
				var expressName = data.respBody.expressNm;
				$("#expressName").val(expressName)
				if(data.respHead.isSuccess == false && expressId != "") {
					alert("此快递公司不存在")
					$("#expressName").val("");
					$("#expressId").val("");
				}
			},
			url: url + "/mis/whs/express_crop/selectExpressCorp",
		})
	});
	//2.存货分类
	$(document).on('keypress', '#invtyCls', function(event) {
		if(event.keyCode == '13') {
			$('#invtyCls').blur();
		}
	})

	$(document).on('blur', '#invtyCls', function() {
		var invtyCls = $("#invtyCls").val();
		dev({
			doc1: $("#invtyCls"),
			doc2: $("#invtyClsNm"),
			showData: {
				"invtyClsEncd": invtyCls
			},
			afunction: function(data) {
				var invtyClsNm = data.respBody.invtyClsNm;
				$("#invtyClsNm").val(invtyClsNm)
				if(data.respHead.isSuccess == false && invtyCls != "") {
					alert("此存货分类不存在")
					$("#invtyClsNm").val("");
					$("#invtyCls").val("");
				}
			},
			url: url + "/mis/purc/InvtyCls/selectInvtyClsByInvtyClsEncd"
		})
	});
	//3.供应商
	$('.provrId').bind('keypress', function(event) {
		if(event.keyCode == '13') {
			$('.provrId').blur();
		}
	})
	$(".provrId").blur(function() {
		var provrId = $(".provrId").val();
		dev({
			doc1: $(".provrId"),
			doc2: $(".provrNm"),
			showData: {
				"provrId": provrId,
			},
			afunction: function(data) {
				if(data.respHead.isSuccess == false && provrId != "") {
					alert("此供应商不存在")
					$(".provrNm").val("");
					$(".provrId").val("");
				}
				var provrNm = data.respBody.provrDoc[0].provrNm;
				$(".provrNm").val(provrNm);
			},
			url: url + "/mis/purc/ProvrDoc/selectProvrDocByProvrId"
		})
	})
	$('.provrId1').bind('keypress', function(event) {
		if(event.keyCode == '13') {
			$('.provrId1').blur();
		}
	})
	$(".provrId1").blur(function() {
		var provrId = $(".provrId1").val();
		dev({
			doc1: $(".provrId1"),
			doc2: $(".provrNm1"),
			showData: {
				"provrId": provrId,
			},
			afunction: function(data) {
				if(data.respHead.isSuccess == false && provrId != "") {
					alert("此供应商不存在")
					$(".provrNm1").val("");
					$(".provrId1").val("");
				}
				var provrNm = data.respBody.provrDoc[0].provrNm;
				$(".provrNm1").val(provrNm)
			},
			url: url + "/mis/purc/ProvrDoc/selectProvrDocByProvrId"
		})
	})
	//4.部门
	$(document).on('keypress', '#deptId', function(event) {
		if(event.keyCode == '13') {
			$('#deptId').blur();
		}
	})
	$(document).on('blur', '#deptId', function() {
		var deptId = $("#deptId").val();
		dev({
			doc1: $("#deptId"),
			doc2: $("#deptName"),
			showData: {
				"deptId": deptId,
			},
			afunction: function(data) {
				var deptName = data.respBody.deptName;
				$("#deptName").val(deptName)
				if(data.respHead.isSuccess == false && deptId != "") {
					alert("此部门不存在")
					$("#deptName").val("");
					$("#deptId").val("");
				}
			},
			url: url + "/mis/purc/DeptDoc/selectDeptDocByDeptEncd"
		})
	})
	//5.客户
	$('.custId').bind('keypress', function(event) {
		if(event.keyCode == '13') {
			$('.custId').blur();
		}
	})
	$(".custId").blur(function() {
		var custId = $(".custId").val();
		dev({
			doc1: $(".custId"),
			doc2: $(".custNm"),
			showData: {
				"custId": custId,
			},
			afunction: function(data) {
				if(data.respHead.isSuccess == false && custId != "") {
					alert("此客户不存在")
					$("#custNm").val("");
					$("#custId").val("");
				}
				var custNm = data.respBody.custDoc[0].custNm;
				$(".custNm").val(custNm);
			},
			url: url + "/mis/purc/CustDoc/selectCustDocByCustId"
		})
	})
	$('.custId1').bind('keypress', function(event) {
		if(event.keyCode == '13') {
			$('.custId1').blur();
		}
	})
	$(".custId1").blur(function() {
		var custId = $(".custId1").val();
		dev({
			doc1: $(".custId1"),
			doc2: $(".custNm1"),
			showData: {
				"custId": custId,
			},
			afunction: function(data) {
				if(data.respHead.isSuccess == false && custId != "") {
					alert("此客户不存在")
					$("#custNm1").val("");
					$("#custId1").val("");
				}
				var custNm = data.respBody.custDoc[0].custNm;
				$(".custNm1").val(custNm);
			},
			url: url + "/mis/purc/CustDoc/selectCustDocByCustId"
		})
	})
	//6.用户
	$(document).on('keypress', '#user', function(event) {
		if(event.keyCode == '13') {
			$('#user').blur();
		}
	})
	$(document).on('blur', '#user', function() {
		var user = $("#user").val();
		dev({
			doc1: $("#user"),
			doc2: $("#userName"),
			showData: {
				"accNum": user,
			},
			afunction: function(data) {
				var userName = data.respBody.userName;
				$("#userName").val(userName);
				if(data.respHead.isSuccess == false && user != "") {
					alert("此用户不存在")
					$("#userName").val("");
					$("#user").val("");
				}
			},
			url: url + "/mis/system/misUser/query"
		})
	})
		
	//6.收发类别
	$(document).on('keypress', '#recvSendCateId', function(event) {
		if(event.keyCode == '13') {
			$('#recvSendCateId').blur();
		}
	})
	$(document).on('blur', '#recvSendCateId', function() {
		var recvSendCateId = $("#recvSendCateId").val();
		dev({
			doc1: $("#recvSendCateId"),
			doc2: $("#recvSendCateNm"),
			showData: {
				"recvSendCateId": recvSendCateId,
			},
			afunction: function(data) {
				console.log(data)
				var recvSendCateNm = data.respBody.recvSendCateNm;
				$("#recvSendCateNm").val(recvSendCateNm);
				if(data.respHead.isSuccess == false && recvSendCateId != "") {
					alert("此收发类别不存在")
					$("#recvSendCateNm").val("");
					$("#recvSendCateId").val("");
				}
			},
			url: url + "/mis/purc/RecvSendCate/selectRecvSendCateByRecvSendCateId"
		})
	})
})

//部门
$(document).on('click', '.biao_deptId', function() {
	window.open("../../Components/baseDoc/deptDocList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
});
//用户/ 业务员
$(document).on('click', '.biao_userId', function() {
	window.open("../../Components/baseDoc/userList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
});
//批次
$(document).on('click', '.biao_batNum', function() {
	window.open("../../Components/baseDoc/batNum.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
})
//存货
$(document).on('click', '.biao_invtyEncd', function() {
	window.open("../../Components/baseDoc/invtyList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
});
//供应商
$(document).on('click', '.biao_provrId', function() {
	window.open("../../Components/baseDoc/provrList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
});
//存货分类
$(document).on('click', '.invtyClsEncd_biaoge', function() {
	window.open("../../Components/baseDoc/invtyTree.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
});
//客户
$(document).on('click', '.biao_custId', function() {
	window.open("../../Components/baseDoc/custDocList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
});
//收发类别
$(document).on('click', '.biao_recv', function() {
	window.open("../../Components/baseDoc/recvSendCateTree.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
});
//仓库
$(document).on('click', '.biao_whsEncd', function() {
	window.open("../../Components/baseDoc/whsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
});
//出入库类别
$(document).on('click', '.biao_formType', function() {
	window.open("../../Components/baseDoc/outIntoWhs.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
});
//出入库类别
$(document).on('click', '.pro_biaoge', function() {
	window.open("../../Components/baseDoc/ProjClsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
});

//放弃按钮点击前提示请勿删除!!!!!
$(function() {
	$(".upOrder").click(function() {
		if(confirm("确定放弃此单据?")) {
			window.location.reload()
		}
	})
})

$(function() {
	$(".buttons .print").remove();
	$(".buttons").append('<p class="ui-jqgrid-headlink ui-icon ui-icon-circle-triangle-n" id="dropDown" style="margin:auto;cursor: pointer"></p>');
	$("#box .buttons p").removeClass('ui-icon')
});
var current = 0;
var acc = 0;
$(document).on('click', '#dropDown', function() {
	if(current == 180) {
		window.acc = 1
		current = current - 180;
	} else {
		window.acc = 2
		current = current + 180;
	}
	allHeight()
	this.style.transform = 'rotate(' + current + 'deg)';
	this.style.transition = 'transform 0.2s';
	$(".order-title").slideToggle();
	$("#box .order-title").show()

	$("#sales_jqGrids").jqGrid('setGridHeight', height);
	$("#bzq_jqGrids").jqGrid('setGridHeight', height);
	$("#bsl_jqGrids").jqGrid('setGridHeight', height);
	$("#zi_jqgrids").jqGrid('setGridHeight', height - 148);
	$("#deteil_list").jqGrid('setGridHeight', height - 148);
	$("#query_jqGrids").jqGrid('setGridHeight', height);
	$("#f_jqgrids").jqGrid('setGridHeight', height);
	$("#platWhsSpecial_jqgrids").jqGrid('setGridHeight', height);
	$("#jqgrids").jqGrid('setGridHeight', height);
	$("#add_jqGrids").jqGrid('setGridHeight', height);
	$("#return_jqgrids").jqGrid('setGridHeight', height);
	$("#jqGrids").jqGrid('setGridHeight', height);
	$("#proAc_jqgrids").jqGrid('setGridHeight', height);
	$("#present_jqgrids").jqGrid('setGridHeight', height);
	$("#pro_jqGrids").jqGrid('setGridHeight', height);
	$("#present_list_jqgrids").jqGrid('setGridHeight', height);
	$("#print_jqgrids").jqGrid('setGridHeight', height);
	$("#sellProcurement_jqGrids").jqGrid('setGridHeight', height);
	$("#sellNowStokReport_jqGrids").jqGrid('setGridHeight', height);
	$("#stic_jqgrids").jqGrid('setGridHeight', height);
	$("#term_jqGrids").jqGrid('setGridHeight', height);
	$("#order_jqGrids").jqGrid('setGridHeight', height);
	$("#send_jqgrids").jqGrid('setGridHeight', height - 30);
	$("#sub_jqGrids").jqGrid('setGridHeight', height - 30);
	$("#duct_jqGrids").jqGrid('setGridHeight', height - 30);
	$("#summary_jqGrids").jqGrid('setGridHeight', height - 30);
	$("#r_jqgrids").jqGrid('setGridHeight', height);
	$("#good_jqgrids").jqGrid('setGridHeight', height);
	$("#ex_jqgrids").jqGrid('setGridHeight', height);
	$("#invtyWhs_jqGrids").jqGrid('setGridHeight', height);
	$("#jqGrids_list_b").jqGrid('setGridHeight', height - 170 - 160);
	$("#store_jqGrids").jqGrid('setGridHeight', height);
	$("#user_jqgrids").jqGrid('setGridHeight', height);
	$("#whs_jqgrids").jqGrid('setGridHeight', height);
	$("#print_guanLian_jqgrids").jqGrid('setGridHeight', height);
	$("#print_muBan_jqgrids").jqGrid('setGridHeight', height);
	$("#print_muBanList_jqgrids").jqGrid('setGridHeight', height);
	$("#List_jqgrids").jqGrid('setGridHeight', height);
	$("#en_jqGrids").jqGrid('setGridHeight', height);
//	$("#deteil_list").jqGrid('setGridHeight', height - 170 - 160);
	$("#selectecGooId_jqgrids").jqGrid('setGridHeight', height);
	$("#insertProjCls_jqgrids").jqGrid('setGridHeight', height);
	$("#whsGds_jqgrids").jqGrid('setGridHeight', height);
	$("#deteil_list").jqGrid('setGridHeight', height);
	//	$("#canzhao_jqGrids_b").jqGrid('setGridHeight', height - 320);
});

function allHeight() {
	$(window).resize(function() {
		$("#jqGrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#jqgrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#r_jqgrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#sales_jqGrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#sellNowStokReport_jqGrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#stic_jqgrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#bzq_jqGrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#bsl_jqGrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#f_jqgrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#platWhsSpecial_jqgrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#add_jqGrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#return_jqgrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#pro_jqGrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#proAc_jqgrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#print_jqgrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#present_jqgrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#present_list_jqgrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#term_jqGrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#sellProcurement_jqGrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#order_jqGrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#send_jqgrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119 - 30);
		$("#sub_jqGrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119 - 30);
		$("#query_jqGrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119 - 30);
		$("#summary_jqGrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119 - 30);
		$("#inserR_jqgrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#good_jqgrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#ex_jqgrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#duct_jqGrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119 - 30);
		$("#invtyWhs_jqGrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119 - 30);
		$("#jqGrids_list_b").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119 - 170);
		$("#good_jqgrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#zc_jqgrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#user_jqgrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#whs_jqgrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#print_guanLian_jqgrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#print_muBan_jqgrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#print_muBanList_jqgrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#List_jqgrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#en_jqGrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#selectecGooId_jqgrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#insertProjCls_jqgrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		$("#whsGds_jqgrids").setGridHeight($(window).height() - $('.purchaseTit').outerHeight(true) - $('.order-title').outerHeight(true) - 119);
		//		$("#deteil_list").setGridHeight($(window).height() - $('#purs_list .purchaseTit').outerHeight(true) - $('#purs_list .order-title').outerHeight(true) - 119);
		//		$("#canzhao_jqGrids_b").setGridHeight($(window).height() - $('.purchaseTits').outerHeight(true) - $('.order-title').outerHeight(true) - 119 - 200);
	});
	var height1 = $('.purchaseTit').outerHeight(true);
	var height2 = $('.order-title').outerHeight(true);
	if(acc == 1) {
		height2 = height2
	} else if(acc == 2) {
		height2 = 0
	}
	var height3 = $(window).height() // 浏览器当前窗口可视区域高度
	window.height = height3 - height1 - height2 - 119;
}

$(function() {
	//宽度根据窗口大小自适应
	$(window).resize(function() {
		$("#jqGrids").setGridWidth($(window).width());
		$("#jqgrids").setGridWidth($(window).width());
		//		$("#r_jqgrids").setGridWidth($(window).width());
		$("#insert_jqgrids").setGridWidth($("#wrap").width());
		$("#mu_jqgrids").setGridWidth($(window).width());
		$("#zi_jqgrids").setGridWidth($(window).width());
		$("#in_jqgrids").setGridWidth($(window).width());
		$("#whs_jqgrids").setGridWidth($(window).width());
		$("#print_jqgrids").setGridWidth($(window).width());
		$("#print_guanLian_jqgrids").setGridWidth($(window).width());
		$("#print_wuLiu_jqgrids").setGridWidth($(window).width());
		$("#order_jqGrids").setGridWidth($(window).width());
		$("#canzhao_jqGrids").setGridWidth($(window).width());
		$("#duct_jqGrids").setGridWidth($(window).width());
		$("#send_jqgrids").setGridWidth($(window).width());
		$("#sub_jqGrids").setGridWidth($(window).width());
		$("#term_jqGrids").setGridWidth($(window).width());
		$("#batNum_jqgrids").setGridWidth($(window).width());
		$("#order_jqgrids").setGridWidth($(window).width());
		$("#good_jqgrids").setGridWidth($(window).width());
		$("#inserR_jqgrids").setGridWidth($(".right").width());
		$("#zc_jqgrids").setGridWidth($(window).width());
		$("#store_jqGrids").setGridWidth($(window).width());
		$("#user_jqgrids").setGridWidth($(window).width());
		$("#platWhsSpecial_jqgrids").setGridWidth($(window).width());
		$("#plat_jqgrids").setGridWidth($(window).width());
		$("#platAudit_jqgrids").setGridWidth($(window).width());
		$("#sales_jqGrids").setGridWidth($(window).width());
		$("#present_jqgrids").setGridWidth($(window).width());
		$("#present_list_jqgrids").setGridWidth($(window).width());
		$("#print_guanLian_jqgrids").setGridWidth($(window).width());
		$("#print_muBan_jqgrids").setGridWidth($(window).width());
		$("#print_muBanList_jqgrids").setGridWidth($(window).width());
		$("#print_jqgrids1").setGridWidth($(window).width());
		$("#print_jqgrids2").setGridWidth($(window).width());
		$("#proAc_jqgrids").setGridWidth($(window).width());
		$("#pro_jqGrids").setGridWidth($(window).width());
		$("#return_jqgrids").setGridWidth($(window).width());
		$("#f_jqgrids").setGridWidth($(window).width());
		$("#List_jqgrids").setGridWidth($(window).width());
		$("#en_jqGrids").setGridWidth($(window).width());
		$("#stic_jqgrids").setGridWidth($(window).width());
		$("#sellNowStokReport_jqGrids").setGridWidth($(window).width());
		$("#sellProcurement_jqGrids").setGridWidth($(window).width());
		$("#bzq_jqGrids").setGridWidth($(window).width());
		$("#batn_jqGrids").setGridWidth($(window).width());
		$("#bat_jqgrids").setGridWidth($(window).width());
		$("#bsl_jqGrids").setGridWidth($(window).width());
		$("#ex_jqgrids").setGridWidth($(window).width());
		$("#invtyWhs_jqGrids").setGridWidth($(window).width());
		$("#r_jqgrids").setGridWidth($(window).width());
		$("#summary_jqGrids").setGridWidth($(window).width());
		$("#selectecGooId_jqgrids").setGridWidth($(window).width());
	});
});

function GetWhsInfo(rowid, val) {
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			'whsEncd': val,
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
			console.log(error);
		}, //错误执行方法
		success: function(data) {
			if(data.respHead.isSuccess == false) {
				alert("无此仓库,请重新输入");
				$("#jqgrids").setRowData(rowid, {
					whsEncd: "",
					whsNm: ""
				});
			} else {
				var list = data.respBody.list[0];
				$("#jqgrids").setRowData(rowid, {
					whsNm: list.whsNm,
				});
			}
		}
	})
}

//根据存货编码查询存货详细信息
function GetGoodsInfo(rowid, goods) {
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": goods
		}
	};
	if(goods) {
		var postData = JSON.stringify(data);
		$.ajax({
			url: url + '/mis/purc/InvtyDoc/printingInvtyDocList',
			type: 'post',
			data: postData,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			error: function() {
				alert("获取数据错误");
			}, //错误执行方法
			success: function(data) {
				var list = data.respBody.list;
				if(list.length == 0) {
					alert("无此存货,请重新输入")
					$("#jqgrids").setRowData(rowid, {
						invtyEncd: "",
						invtyNm: "",
						spcModel: "",
						bxRule: "",
						measrCorpNm: "",
						crspdBarCd: "",
						cntnTaxUprc: '',
						taxRate: '',
						measrCorpId: "",
						baoZhiQi: "",
						prcTaxSum: "",
						isQuaGuaPer:''
					});
				}
				$("input[name='invtyEncd']").val(list[0].invtyEncd);
				if(list.length == 1) {
					$("#jqgrids").setRowData(rowid, {
						invtyNm: list[0].invtyNm,
						spcModel: list[0].spcModel,
						bxRule: list[0].bxRule,
						measrCorpNm: list[0].measrCorpNm,
						crspdBarCd: list[0].crspdBarCd,
						cntnTaxUprc: list[0].refCost,
						taxRate: list[0].iptaxRate,
						measrCorpId: list[0].measrCorpId,
						baoZhiQi: list[0].baoZhiQiDt,
						prcTaxSum: list[0].prcTaxSum,
						isQuaGuaPer:list[0].isQuaGuaPer,
						prdcDt:"",
						invldtnDt:"",
						qty: '',
						bxQty: '',
						noTaxUprc: '',
						noTaxAmt: '',
						taxAmt: '',
					});
				} else {
					for(var i = 1; i < list.length; i++) {
						$("#jqgrids").setRowData(rowid, {
							invtyNm: list[0].invtyNm,
							spcModel: list[0].spcModel,
							bxRule: list[0].bxRule,
							measrCorpNm: list[0].measrCorpNm,
							crspdBarCd: list[0].crspdBarCd,
							cntnTaxUprc: list[0].refCost,
							taxRate: list[0].iptaxRate,
							measrCorpId: list[0].measrCorpId,
							baoZhiQi: list[0].baoZhiQiDt,
							prcTaxSum: list[0].prcTaxSum,
							isQuaGuaPer:list[0].isQuaGuaPer,
						});
						//设置页面数据展示
						$("#jqgrids").setRowData(++rowid, {
							invtyEncd: list[i].invtyEncd,
							invtyNm: list[i].invtyNm,
							spcModel: list[i].spcModel,
							bxRule: list[i].bxRule,
							measrCorpNm: list[i].measrCorpNm,
							crspdBarCd: list[i].crspdBarCd,
							cntnTaxUprc: list[i].refCost,
							taxRate: list[i].iptaxRate,
							measrCorpId: list[i].measrCorpId,
							baoZhiQi: list[i].baoZhiQiDt,
							prcTaxSum: list[i].prcTaxSum,
							isQuaGuaPer:list[0].isQuaGuaPer,
						});
					}
				}
			}
		})
	}
}

//根据存货编码查询存货详细信息
function sell_GetGoodsInfo(rowid, goods) {
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": goods
		}
	};
	if(goods) {
		var postData = JSON.stringify(data);
		$.ajax({
			url: url + '/mis/purc/InvtyDoc/printingInvtyDocList',
			type: 'post',
			data: postData,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			error: function() {
				alert("获取数据错误");
			}, //错误执行方法
			success: function(data) {
				var list = data.respBody.list;
				if(list.length == 0) {
					alert("无此存货,请重新输入")
					$("#jqgrids").setRowData(rowid, {
						invtyEncd: "",
						invtyNm: "",
						spcModel: "",
						bxRule: "",
						measrCorpNm: "",
						crspdBarCd: "",
						cntnTaxUprc: '',
						taxRate: '',
						measrCorpId: "",
						baoZhiQi: "",
						prcTaxSum: "",
					});
				}
				$("#" + rowid + "_invtyEncd").val(list[0].invtyEncd);
				if(list.length == 1) {
					$("#jqgrids").setRowData(rowid, {
						invtyNm: list[0].invtyNm,
						spcModel: list[0].spcModel,
						bxRule: list[0].bxRule,
						measrCorpNm: list[0].measrCorpNm,
						crspdBarCd: list[0].crspdBarCd,
						cntnTaxUprc: list[0].refSellPrc,
						taxRate: list[0].optaxRate,
						measrCorpId: list[0].measrCorpId,
						baoZhiQi: list[0].baoZhiQiDt,
						prcTaxSum: list[0].prcTaxSum,
						isQuaGuaPer:list[0].isQuaGuaPer,
						prdcDt:"",
						invldtnDt:"",
						qty: '',
						bxQty: '',
						noTaxUprc: '',
						noTaxAmt: '',
						taxAmt: '',
					});
				} else {
					for(var i = 1; i < list.length; i++) {
						$("#jqgrids").setRowData(rowid, {
							invtyNm: list[0].invtyNm,
							spcModel: list[0].spcModel,
							bxRule: list[0].bxRule,
							measrCorpNm: list[0].measrCorpNm,
							crspdBarCd: list[0].crspdBarCd,
							cntnTaxUprc: list[0].refSellPrc,
							taxRate: list[0].optaxRate,
							measrCorpId: list[0].measrCorpId,
							baoZhiQi: list[0].baoZhiQiDt,
							prcTaxSum: list[0].prcTaxSum,
							isQuaGuaPer:list[0].isQuaGuaPer,
						});
						//设置页面数据展示
						$("#jqgrids").setRowData(++rowid, {
							invtyEncd: list[i].invtyEncd,
							invtyNm: list[i].invtyNm,
							spcModel: list[i].spcModel,
							bxRule: list[i].bxRule,
							measrCorpNm: list[i].measrCorpNm,
							crspdBarCd: list[i].crspdBarCd,
							cntnTaxUprc: list[i].refSellPrc,
							taxRate: list[i].optaxRate,
							measrCorpId: list[i].measrCorpId,
							baoZhiQi: list[i].baoZhiQiDt,
							prcTaxSum: list[i].prcTaxSum,
							isQuaGuaPer:list[0].isQuaGuaPer,
						});
					}
				}
			}
		})
	}
}

function GetProInfo(rowid, val) {
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			'invtyClsEncd': val,
		}
	};
	var postData = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/purc/InvtyCls/selectInvtyClsByInvtyClsEncd',
		type: 'post',
		data: postData,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		error: function() {
			console.log(error);
		}, //错误执行方法
		success: function(data) {
			var list = data.respBody;
			//设置页面数据展示
			$("#jqgrids").setRowData(rowid, {
				projNm: list.invtyClsNm,
			});
		}
	})
}

//设置日期相关
function setProductDate(rowid,val,baoZhiQi) {
	var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow');
	var rowDatas = $("#jqgrids").jqGrid('getRowData', gr);
	//计算生产日期
	var prdcDt = val //生产日期

	//保质期
	var invldtnDt = getNewDay(prdcDt, baoZhiQi);
	if(invldtnDt == "NaN-NaN-NaN") {
		invldtnDt = "";
	} else {
		invldtnDt = invldtnDt
	};
	$("#jqgrids").setRowData(rowid, {
		invldtnDt: invldtnDt,
	});
}

//失效日期
function getNewDay(dateTemp, days) {
	var dateTemp = dateTemp.split("-");
	var nDate = new Date(dateTemp[1] + '-' + dateTemp[2] + '-' + dateTemp[0]); //转换为MM-DD-YYYY格式  
	var millSeconds = Math.abs(nDate) + (days * 24 * 60 * 60 * 1000);
	var rDate = new Date(millSeconds);
	var year = rDate.getFullYear();
	var month = rDate.getMonth() + 1;
	if(month < 10) month = "0" + month;
	var date = rDate.getDate();
	if(date < 10) date = "0" + date;
	return(year + "-" + month + "-" + date);
}

//数值转换
function toDecimal(x) {
	var f = parseFloat(x);
	var s = f.toFixed(jingdu)
	return s;
}

//设置数量和金额
function purs_SetNums(rowid, cellname, val) {
	$("#jqgrids").setColProp("cntnTaxUprc", {
		editable: true
	});
	var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
	var bxRule = parseFloat(rowDatas.bxRule); //箱规
	var qty = parseFloat(rowDatas.qty); //数量
	if(isNaN(qty)) {
		qty = ""
	}
	var bxQty = parseFloat(rowDatas.bxQty); //箱数
	var taxRate = parseFloat(rowDatas.taxRate); //税率
	if(isNaN(taxRate)) {
		taxRate = ""
	}
	if(cellname == "qty") {
		bxQty = parseFloat(qty / bxRule); //箱数
	} else if(cellname == "bxQty") {
		qty = parseFloat(bxQty * bxRule); //数量
	}
	var noTaxUprc, noTaxAmt, taxAmtNum, prcTaxSum, cntnTaxUprc;
	if(isNaN(bxQty)) {
		bxQty = ""
	}
	if(cellname == "qty") { //==================================数量
		cntnTaxUprc = toDecimal(rowDatas.cntnTaxUprc); //含税单价
		if(isNaN(cntnTaxUprc) || cntnTaxUprc == Infinity) {
			cntnTaxUprc = ""
		}
		prcTaxSum = (cntnTaxUprc * qty).toFixed(2); //价税合计
		if(isNaN(prcTaxSum)) {
			prcTaxSum = ""
		}

		noTaxAmt = (prcTaxSum/(taxRate * 0.01 + 1)).toFixed(2);
		if(isNaN(noTaxAmt)) {
			noTaxAmt = ""
		}
		noTaxUprc = toDecimal(noTaxAmt/qty); //无税单价
		if(isNaN(noTaxUprc)) {
			noTaxUprc = ""
		}
		taxAmtNum = (prcTaxSum-noTaxAmt).toFixed(2);
		if(isNaN(taxAmtNum)) {
			taxAmtNum = ""
		}
	} else if(cellname == "cntnTaxUprc") { //==================================含税单价
		cntnTaxUprc = toDecimal(rowDatas.cntnTaxUprc);
		prcTaxSum = (cntnTaxUprc * qty).toFixed(2); //价税合计
		noTaxUprc = toDecimal(cntnTaxUprc / (taxRate * 0.01 + 1)); //无税单价
		noTaxAmt = (prcTaxSum / (taxRate * 0.01 + 1)).toFixed(2); //无税金额
		taxAmtNum = (prcTaxSum-noTaxAmt).toFixed(2);
	} else if(cellname == "prcTaxSum") { //===================================价税合计
		prcTaxSum = parseFloat(rowDatas.prcTaxSum).toFixed(2);
		if(isNaN(prcTaxSum)) {
			prcTaxSum = ""
		}
		cntnTaxUprc = toDecimal(prcTaxSum / qty); //含税单价
		if(isNaN(cntnTaxUprc) || cntnTaxUprc == Infinity) {
			cntnTaxUprc = ""
		}

		noTaxAmt = (prcTaxSum / (taxRate * 0.01 + 1)).toFixed(2); //无税金额
		if(isNaN(noTaxAmt)) {
			noTaxAmt = ""
		}

		noTaxUprc = toDecimal(noTaxAmt / qty); //无税单价
		if(isNaN(noTaxUprc) || noTaxUprc == Infinity) {
			noTaxUprc = ""
		} else if(noTaxUprc < 0) {
			noTaxUprc = -noTaxUprc
		}

		taxAmtNum = (prcTaxSum-noTaxAmt).toFixed(2);
		if(isNaN(taxAmtNum)) {
			taxAmtNum = ""
		}
	} else if(cellname == "noTaxUprc") { //=================================无税单价
		noTaxUprc = toDecimal(rowDatas.noTaxUprc);
		cntnTaxUprc = toDecimal(noTaxUprc * (taxRate * 0.01 + 1)); //含税单价
		if(isNaN(cntnTaxUprc)) {
			cntnTaxUprc = ""
		}
		taxAmtNum = (cntnTaxUprc/(1+taxRate * 0.01)*taxRate).toFixed(2);
		if(isNaN(taxAmtNum)) {
			taxAmtNum = ""
		}
		prcTaxSum = (cntnTaxUprc * qty).toFixed(2); //价税合计
		if(isNaN(prcTaxSum)) {
			prcTaxSum = ""
		}
		noTaxAmt = (prcTaxSum-taxAmtNum).toFixed(2);
		if(isNaN(noTaxAmt)) {
			noTaxAmt = ""
		}
	} else if(cellname == "noTaxAmt") { //===================================无税金额
		noTaxAmt = parseFloat(rowDatas.noTaxAmt).toFixed(2);
		noTaxUprc = (noTaxAmt / qty).toFixed(2);
		taxAmtNum = (noTaxAmt * taxRate * 0.01).toFixed(2);
		cntnTaxUprc = (taxAmtNum/qty).toFixed(2);
		prcTaxSum = taxAmtNum+noTaxUprc;
	} else if(cellname == "taxRate") { //===================================税率
		var taxRate = toDecimal(rowDatas.taxRate);
		taxAmtNum = (cntnTaxUprc/(1+taxRate * 0.01)*taxRate).toFixed(2);
		noTaxAmt = (prcTaxSum-taxAmtNum).toFixed(2); //无税金额
		cntnTaxUprc = toDecimal(rowDatas.cntnTaxUprc);
		prcTaxSum = (noTaxAmt+taxAmtNum).toFixed(2); //价税合计
		noTaxUprc = toDecimal(noTaxAmt/qty); //无税单价
	}
	$("#jqgrids").setRowData(rowid, {
		taxRate: taxRate,
		bxQty: bxQty,
		qty: qty,
		noTaxAmt: noTaxAmt,
		taxAmt: taxAmtNum,
		cntnTaxUprc: cntnTaxUprc,
		prcTaxSum: prcTaxSum, //价税合计
		noTaxUprc: noTaxUprc, //无税单价
	});
}

//参照后数量的计算
function referSure(num) {
	for(var i = 0; i < num.length; i++) {
		num[i].noTaxUprc = toDecimal(num[i].cntnTaxUprc / (num[i].taxRate * 0.01 + 1)); //无税单价
		if(isNaN(num[i].noTaxUprc)) {
			num[i].noTaxUprc = ""
		}

		num[i].noTaxAmt = (num[i].noTaxUprc * num[i].qty).toFixed(2);
		if(isNaN(num[i].noTaxAmt)) {
			num[i].noTaxAmt = ""
		}
		num[i].taxAmtNum = (num[i].noTaxAmt * num[i].taxRate * 0.01).toFixed(2);
		if(isNaN(num[i].taxAmtNum)) {
			num[i].taxAmtNum = ""
		}

		num[i].prcTaxSum = (num[i].cntnTaxUprc * num[i].qty).toFixed(2); //价税合计
		if(isNaN(num[i].prcTaxSum)) {
			num[i].prcTaxSum = ""
		}
	}
}

//新增表格行
function addRows() {
	var gr = $("#jqgrids").jqGrid('getDataIDs');
	if(gr.length == 0) {
		var rowid = 0;
	} else if(gr.length != 0) {
		var rowid = Math.max.apply(Math, gr);
	}
	window.newrowid = rowid + 1;
	var dataRow = {};
	$("#jqgrids").jqGrid("addRowData", newrowid, dataRow, "last");
}
//删除表格行
function removeRows() {
	var gr = $("#jqgrids").jqGrid('getGridParam', 'selarrrow');
	if(gr.length == 0) {
		alert("请选择要删除的行");
		return;
	} else if(confirm("确定删除此行数据吗")){
		var length = gr.length;
		for(var i = 0; i < length + 1; i++) {
			$("#jqgrids").jqGrid("delRowData", gr[0]);
		}
	}
}
//复制表格行
function copyRows() {
	var ids = $("#jqgrids").jqGrid('getGridParam', 'selarrrow');
	var dataRow = $("#jqgrids").jqGrid('getRowData', ids);
	var count = $("#jqgrids").getGridParam("reccount");
	if($("#jqgrids tbody tr[role='row'] td[role='gridcell']").children().length>count) {
		alert("在编辑状态下不能复制")
	} else {
		if(ids.length == 0) {
			alert("请选择要复制的行");
			return;
		} else if(ids.length > 1) {
			alert("每次只能复制一行");
			return;
		} else {
			var gr = $("#jqgrids").jqGrid('getDataIDs');
			// 选中行实际表示的位置
			var rowid = Math.max.apply(Math, gr);
			// 新插入行的位置
			var newrowid = rowid + 1;
			// 插入一行
			$("#jqgrids").jqGrid("addRowData", newrowid, dataRow, "last");
		}
	}
}

//设置数量和金额--无税单价
function SetNums(rowid, cellname) {
	var rowDatas = $("#jqgrids").jqGrid('getRowData', rowid);
	var bxRule = parseFloat(rowDatas.bxRule);
	var qty = parseFloat(rowDatas.qty);
	var bxQty = parseFloat(rowDatas.bxQty);
	var taxRate = parseFloat(rowDatas.taxRate);
	if(cellname == "qty") {
		bxQty = parseFloat(qty / bxRule); //箱数
	} else if(cellname == "bxQty") {
		qty = parseFloat(bxQty * bxRule);
	}
	if(!isNaN(bxQty)) {
		bxQty = parseFloat(bxQty);
	} //数量
	else {
		bxQty = "";
	}
	if(!isNaN(qty)) {
		qty = qty
	} //盒数量
	else {
		qty = ""
	}
	if(!isNaN(taxRate)) {
		taxRate = taxRate
	} //税率
	else {
		taxRate = ""
	}
	var noTaxUprc, noTaxAmt, taxAmtNum, prcTaxSum, cntnTaxUprc
	noTaxUprc = parseFloat(rowDatas.noTaxUprc);

	noTaxAmt = toDecimal(noTaxUprc * qty);
	if(isNaN(noTaxAmt)) {
		noTaxAmt = ""
	}
	taxAmtNum = toDecimal(noTaxAmt * taxRate * 0.01);
	if(isNaN(taxAmtNum)) {
		taxAmtNum = ""
	}
	cntnTaxUprc = toDecimal(noTaxUprc * (taxRate * 0.01 + 1)); //含税单价
	if(isNaN(cntnTaxUprc)) {
		cntnTaxUprc = ""
	}
	prcTaxSum = toDecimal(noTaxAmt * (taxRate * 0.01 + 1)); //价税合计
	if(isNaN(prcTaxSum)) {
		prcTaxSum = ""
	}
	$("#jqgrids").setRowData(rowid, {
		taxRate: taxRate,
		bxQty: bxQty,
		qty: qty,
		noTaxAmt: noTaxAmt,
		taxAmt: taxAmtNum,
		cntnTaxUprc: cntnTaxUprc,
		prcTaxSum: prcTaxSum,
	});
}

function isSame(encd) {
	var pursTypNm = [],
		provrId = [],
		deptId = [],
		accNum = [];
	for(var i = 0; i < encd.length; i++) {
		pursTypNm[i] = encd[i].pursTypNm;
		provrId[i] = encd[i].provrId;
		deptId[i] = encd[i].deptId;
		accNum[i] = encd[i].accNum
	}
	for(var i = 0; i < accNum.length; i++) {
		if(pursTypNm[0] != pursTypNm[i]) {
			alert("采购类型不一致无法查询");
			return false
		} else if(accNum[0] != accNum[i]) {
			alert("业务员不一致无法查询");
			return false
		} else if(provrId[0] != provrId[i]) {
			alert("供应商不一致无法查询");
			return false
		} else if(deptId[0] != deptId[i]) {
			alert("部门不一致无法查询");
			return false
		}
	}
}
var toGds;

function deteil_search() {

	//获得选中行的行号
	var ids = $('#jqGrids_list').jqGrid('getGridParam', 'selarrrow');
	var encd = [];
	for(var i = 0; i < ids.length; i++) {
		rowDatas = $("#jqGrids_list").jqGrid('getRowData', ids[i]);
		encd[i] = rowDatas;
	}
	if(toGds) {
		if(encd.length == 1) {
			if(toGds == 2) { //ruku
				var rtn = "purc/ToGdsSngl/selectUnIntoWhsQtyByByToGdsSnglId"
				deteil(rtn)
			} else if(toGds == 1) { //jushou
				var rtn = "purc/ToGdsSngl/selectUnReturnQtyByToGdsSnglId"
				deteil(rtn)
			}
		} else if(encd.length > 1 && isSame(encd) != false) {
			if(toGds == 2) { //ruku
				var rtn = "purc/ToGdsSngl/selectUnIntoWhsQtyByByToGdsSnglId"
				deteil(rtn)
			} else if(toGds == 1) { //jushou
				var rtn = "purc/ToGdsSngl/selectUnReturnQtyByToGdsSnglId"
				deteil(rtn)
			}
		}
	} else {
		if(encd.length == 1) {
			deteil()
		} else if(encd.length > 1 && isSame(encd) != false) {
			deteil()
		}
	}
}
var rowId;
$(function() {
	jQuery("#find_jqgrids").jqGrid({
		datatype: "json",
		colNames: ['存货编码', '存货名称', '规格型号', '存货代码', '对应条形码'], //jqGrid的列显示名字
		colModel: [{
				name: 'invtyEncd',
				align: 'center',
			},
			{
				name: 'invtyNm',
				align: 'center',
			},
			{
				name: 'spcModel',
				align: 'center',
			},
			{
				name: 'invtyCd',
				align: 'center',
			},
			{
				name: 'crspdBarCd',
				align: 'center',
			}
		],
		autowidth: true,
		viewrecords: true,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		height: 200,
		caption: "存货查询", //表格的标题名字
		onSelectRow: function(rowid) {
			getInvtyEncd = $("#find_jqgrids").getCell(rowid, "invtyEncd");
			$("#" + rowId + "_invtyEncd").val(getInvtyEncd);
			$("#" + rowId + "_subKitEncd").val(getInvtyEncd);
			$("#findGrid").hide();
		},
	})
})

let filter = (condition, data) => {
	return data.filter(item => {
		return Object.keys(condition).every(key => {
			return String(item[key]).toLowerCase().includes(
				String(condition[key]).trim().toLowerCase())
		})
	})
}

var concat = function(arr1, arr2, arr3) {
	if(arguments.length <= 1) {
		return false;
	}
	var concat_ = function(arr1, arr2) {
		var arr = arr1.concat();
		for(var i = 0; i < arr2.length; i++) {
			arr.indexOf(arr2[i]) === -1 ? arr.push(arr2[i]) : 0;
		}
		return arr;
	}
	var result = concat_(arr1, arr2);
	for(var i = 2; i < arguments.length; i++) {
		result = concat_(result, arguments[i]);
	}
	return result;
}

function findGrid(rowid, cellname, val) {
	var batch = $("#" + rowid + "_" + cellname).val();
	var a = $("#" + rowid + "_" + cellname).offset().top + 40;
	if(batch == "") {
		$("#findGrid").hide();
	} else {
		$("#findGrid").show();
	}
	$("#findGrid").css("top", a);
	var invtyEncd = invtyData.invtyEncd,
		invtyCd = invtyData.invtyCd,
		crspdBarCd = invtyData.crspdBarCd;
	var cond1 = {
		invtyEncd: batch
	};
	var cond2 = {
		invtyCd: batch
	};
	var cond3 = {
		crspdBarCd: batch
	};
	var list1 = filter(cond1, invtyData)
	var list2 = filter(cond2, invtyData)
	var list3 = filter(cond3, invtyData)

	var newArr = concat(list1, list2, list3);

	$("#find_jqgrids").jqGrid("clearGridData");
	$("#find_jqgrids").jqGrid("setGridParam", {
		datatype: 'local',
		data: newArr, //newData是符合格式要求的重新加载的数据
		page: 1 //哪一页的值
	}).trigger("reloadGrid");
	rowId = rowid;
}

function BillDate() {
	var Time = new Date().format(" hh:mm:ss");
	var time = localStorage.getItem("loginDate");
	var BillDate = time + Time;
	return BillDate;
}

function ExportData(list, execlName) {
	//  创建worksheet
	var ws = XLSX.utils.json_to_sheet(list);
	//  新建空workbook，然后加入worksheet
	var wb = XLSX.utils.book_new();
	XLSX.utils.book_append_sheet(wb, ws, execlName);
	//  生成xlsx文件
	XLSX.writeFile(wb, execlName + ".xlsx");
}

function getInvtyData() {
	//拿到grid对象
	var obj = $("#jqgrids");
	//获取grid表中所有的rowid值
	var rowIds = obj.getDataIDs();
	//初始化一个数组arrayData容器，用来存放rowData
	var arrayData = new Array();
	if(rowIds.length > 0) {
		for(var i = 0; i < rowIds.length; i++) {
			obj.getRowData(rowIds[i]).isNtRtnGoods = 0;
			if(obj.getRowData(rowIds[i]).invtyEncd == "") {
				continue;
			} else {
				arrayData.push(obj.getRowData(rowIds[i]));
			}
		}
	}
	return arrayData;
}