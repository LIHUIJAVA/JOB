localStorage.setItem("outInEncd", 0)
$(function() {
	//1.仓库
	$(document).on('keypress', '#tranOutWhsEncd', function(event) {
		if(event.keyCode == '13') {
			$('#tranOutWhsEncd').blur();
		}
	})

	$(document).on('blur', '#tranOutWhsEncd', function() {
		var tranOutWhsEncd = $("#tranOutWhsEncd").val();
		if(tranOutWhsEncd != '') {
			dev({
				doc1: $("#tranOutWhsEncd"),
				doc2: $("#tranOutWhsNm"),
				showData: {
					"whsEncd": tranOutWhsEncd
				},
				afunction: function(data) {
					if(data.respHead.isSuccess == false) {
						alert("不存在此转出仓库请确认！")
						$("#tranOutWhsNm").val("");
						$("#tranOutWhsEncd").val("");
					} else if(data.respHead.isSuccess == true) {
						var tranOutWhsNm = data.respBody.list[0].whsNm;
						$("#tranOutWhsNm").val(tranOutWhsNm)
					}
				},
				url: url + "/mis/whs/whs_doc/selectWhsDocList"
			})
			
		}else if(tranOutWhsEncd == '') {
			$("#tranOutWhsNm").val('')
		}
	});
	$(document).on('blur', '#tranInWhsEncd', function() {
		var tranInWhsEncd = $("#tranInWhsEncd").val();
		if(tranInWhsEncd != '') {
			dev({
				doc1: $("#tranInWhsEncd"),
				doc2: $("#tranInWhsNm"),
				showData: {
					"whsEncd": tranInWhsEncd
				},
				afunction: function(data) {
					if(data.respHead.isSuccess == false) {
						alert("不存在此转入仓库请确认！")
						$("#tranInWhsNm").val("");
						$("#tranInWhsEncd").val("");
					} else if(data.respHead.isSuccess == true) {
						var tranInWhsNm = data.respBody.list[0].whsNm;
						$("#tranInWhsNm").val(tranInWhsNm)
					}
				},
				url: url + "/mis/whs/whs_doc/selectWhsDocList"
			})
			
		}else if(tranInWhsEncd == '') {
			$("#tranInWhsNm").val('')
		}
	});
})

//整单联查
$(document).on('click', '.joint_search', function() {
	var gr = $("#jqGrids").jqGrid('getGridParam', 'selrow');
	if(gr == null) {
		alert("请选择单据")
	} else {
		//获得行数据
		var rowDatas = $("#jqGrids").jqGrid('getRowData', gr);
		var formNum = rowDatas.formNum;
		var formTypEncd = rowDatas.formTypEncd;
		window.open("../../Components/system/jointSearch.html?formNum=" + formNum + "&formTypEncd=" + formTypEncd, 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	}
})

//点击转出仓库表格图标显示仓库列表
$(function() {

	$(document).on('click', '.tranOutWhsEncd1', function() {
		localStorage.setItem("outInEncd", 1)
		window.open("../../Components/baseDoc/whsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
});
//点击转入表格图标显示业务员列表
$(function() {
	$(document).on('click', '.tranInWhsEncd1', function() {
		localStorage.setItem("outInEncd", 2)
		window.open("../../Components/baseDoc/whsList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
//	存货分类
	$(document).on('click', '.invtyClsEncd_biaoge', function() {
		window.open("../../Components/baseDoc/invtyTree.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})
var count;
var pages;
var rowNum;
//表格初始化
$(function() {
	localStorage.removeItem("index")
	localStorage.removeItem("sortorder")
	allHeight()
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	localStorage.removeItem("tranInWhsEncd");
	localStorage.removeItem("tranOutWhsEncd");
	$("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['单据号', '单据日期','调拨日期','转出仓库编码', '转入仓库编码', '转出仓库名称', '转入仓库名称','调拨状态',
		'表头备注','是否向WMS上传','是否审核','是否完成','是否关闭','打印次数','创建人','创建时间','修改人','修改时间','审核人','审核时间','单据类型编码','单据类型名称','序号','存货编号','调拨数量',
		'实收数量','批号','失效日期','保质期','生产日期','税率', '税额','含税单价','未税单价','含税金额','未税金额','存货名称','规格型号','箱规','箱数',
		'计量单位名称','存货代码','对应条形码'],
		colModel: [{
				name: 'formNum',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'formDt',
				editable: true,
				align: 'center',
			},
			{
				name: 'cannibDt',
				editable: true,
				align: 'center',
				sortable: false,
				hidden:true

			},
			{
				name: 'tranOutWhsEncd',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'tranInWhsEncd',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'tranOutWhsEncdName',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'tranInWhsEncdName',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'cannibStatus',
				editable: true,
				align: 'center',
				hidden:true,
				sortable: false
			},
			{
				name: 'memo',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'isNtWms',
				editable: true,
				align: 'center',
				hidden:true,
				sortable: false
			},
			{
				name: 'isNtChk',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'isNtCmplt',
				editable: true,
				align: 'center',
				hidden:true,
				sortable: false
			},
			{
				name: 'isNtClos',
				editable: true,
				align: 'center',
				hidden:true,
				sortable: false
			},
			{
				name: 'printCnt',
				editable: true,
				align: 'center',
				hidden:true,
				sortable: false
			},
			{
				name: 'setupPers',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'setupTm',
				editable: true,
				align: 'center',
			},
			{
				name: 'mdfr',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'modiTm',
				editable: true,
				align: 'center',
			},
			{
				name: 'chkr',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'chkTm',
				editable: true,
				align: 'center',
			},
			{
				name: 'formTypEncd',
				editable: true,
				sortable: false,
				align: 'center',
			},
			{
				name: 'formTypName',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'ordrNum',
				editable: true,
				align: 'center',
				sortable: false,
				hidden:true
			},
			{
				name: 'invtyId',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'cannibQty',
				editable: true,
				align: 'center',
			},
			{
				name: 'recvQty',
				editable: true,
				align: 'center',
				sortable: false,
				hidden:true
			},
			{
				name: 'batNum',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'invldtnDt',
				editable: true,
				align: 'center',
			},
			{
				name: 'baoZhiQi',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'prdcDt',
				editable: true,
				align: 'center',
			},
			{
				name: 'taxRate',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'taxAmt',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'cntnTaxUprc',
				editable: true,
				align: 'center',
			},
			{
				name: 'unTaxUprc',
				editable: true,
				align: 'center',
			},
			{
				name: 'cntnTaxAmt',
				editable: true,
				align: 'center',
			},
			{
				name: 'unTaxAmt',
				editable: true,
				align: 'center',
			},
			{
				name: 'invtyNm',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'spcModel',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'bxRule',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'bxQty',
				editable: true,
				align: 'center',
			},
			{
				name: 'measrCorpNm',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'invtyCd',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'crspdBarCd',
				editable: true,
				align: 'center',
				sortable: false
			}
		],
		autowidth: true,
		height:height,
		sortable:true,
		autoScroll:true,
		shrinkToFit:false,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowList: [500, 1000, 3000,5000],
		rowNum: 500,
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
//		multiboxonly: true,
		caption: "调拨单列表", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#jqGrids").getGridParam('records'); //获取返回的记录数
			page = $("#jqGrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#jqGrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			var index = localStorage.getItem("index")
			var sortorder = localStorage.getItem("sortorder")
			if(index == null) {
				var index = ''
			}
			if(sortorder == null) {
				var sortorder = ''
			}
			window.newPage = page;
			search(index, sortorder, page)
		},
		//双击弹出采购订单
		ondblClickRow: function(rowid) {
			order(rowid);
		},
		footerrow: true,
		gridComplete: function() { 
			var cannibQty = $("#jqGrids").getCol('cannibQty', false, 'sum');
			var recvQty = $("#jqGrids").getCol('recvQty', false, 'sum');
			var cntnTaxAmt = $("#jqGrids").getCol('cntnTaxAmt', false, 'sum');
			var unTaxAmt = $("#jqGrids").getCol('unTaxAmt', false, 'sum');
			var bxQty = $("#jqGrids").getCol('bxQty', false, 'sum');
			$("#jqGrids").footerData('set', { 
				"formNum": "本页合计",
				cannibQty: cannibQty.toFixed(prec),
				recvQty:recvQty.toFixed(prec),
				cntnTaxAmt : precision(cntnTaxAmt,2),
				unTaxAmt : precision(unTaxAmt,2),
				bxQty: bxQty.toFixed(prec),
				
			}          );    
		},
		onSortCol: function(index, colindex, sortorder) {
			switch(index) {
				case "formDt":
					index = "cSngl.form_dt"
					break;
				case "setupTm":
					index = "cSngl.setup_tm"
					break;
				case "modiTm":
					index = "cSngl.modi_tm"
					break;
				case "chkTm":
					index = "cSngl.chk_tm"
					break;
				case "cannibQty":
					index = "cSubTab.cannib_qty"
					break;
				case "invldtnDt":
					index = "cSubTab.invldtn_dt"
					break;
				case "prdcDt":
					index = "cSubTab.prdc_dt"
					break;
				case "cntnTaxUprc":
					index = "cSubTab.cntn_tax_uprc"
					break;
				case "unTaxUprc":
					index = "cSubTab.un_tax_uprc"
					break;
				case "cntnTaxAmt":
					index = "cSubTab.cntn_tax_amt"
					break;
				case "unTaxAmt":
					index = "cSubTab.un_tax_amt"
					break;
				case "bxQty":
					index = "cSubTab.bx_qty"
					break;
			}
			localStorage.setItem("index",index)
			localStorage.setItem("sortorder",sortorder)
			window.index = index;
			window.sortorder = sortorder;
			search(index, sortorder, newPage)
		}
	})
})


//查询按钮
$(document).on('click', '.search', function() {
	var initial = 1;
	window.newPage = initial;
	var index = '';
	var sortorder = '';
	search(index, sortorder,initial)
})

function search(index, sortorder, page){
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var formNum = $(".formNum").val();
	var tranOutWhsEncd = $("input[name='tranOutWhsEncd']").val()
	var tranInWhsEncd = $("input[name='tranInWhsEncd']").val()
	var setupPers = $("input[name='userName']").val()
	var formNumStart = $("input[name='formNumStart']").val()
	var formNumEnd = $("input[name='formNumEnd']").val()
	var formDt1 = $(".formDt1").val();
	var formDt2 = $(".formDt2").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var invtyEncd = $("input[name='invtyEncd']").val();
	var batNum = $("input[name='batNum']").val();
	var memo = $("input[name='memo']").val();
	var isNtChk = $("#isNtChk").val();
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"formNum": formNum,
			"tranOutWhsEncd": tranOutWhsEncd,
			"tranInWhsEncd": tranInWhsEncd,
			"formDt1": formDt1,
			"formDt2": formDt2,
			"formNumStart": formNumStart,
			"formNumEnd": formNumEnd,
			"setupPers": setupPers,
			"invtyClsEncd": invtyClsEncd,
			"invtyEncd": invtyEncd,
			"batNum": batNum,
			"memo": memo,
			'isNtChk':isNtChk,
			"sort":index,
			"sortOrder":sortorder,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	console.log(saveData)
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/whs/cannib_sngl/queryList',
		async: true,
		data: saveData,
		dataType: 'json',
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");

		},
		success: function(data) {
			console.log(data)
			var list = data.respBody.list;
			console.log(list)
			for(var i = 0;i<list.length;i++){
				if(list[i].isNtChk == 0) {
					list[i].isNtChk = "否"
				} else if(list[i].isNtChk == 1) {
					list[i].isNtChk = "是"
				}
				if(list[i].isNtWms == 0) {
					list[i].isNtWms = "否"
				} else if(list[i].isNtWms == 1) {
					list[i].isNtWms = "是"
				}
				if(list[i].isNtBookEntry == 0) {
					list[i].isNtBookEntry = "否"
				} else if(list[i].isNtBookEntry == 1) {
					list[i].isNtBookEntry = "是"
				}
				if(list[i].isNtCmplt == 0) {
					list[i].isNtCmplt = "否"
				} else if(list[i].isNtCmplt == 1) {
					list[i].isNtCmplt = "是"
				}
				if(list[i].isNtClos == 0) {
					list[i].isNtClos = "否"
				} else if(list[i].isNtClos == 1) {
					list[i].isNtClos = "是"
				}
			}
			myDate = list;
			var mydata = {};
			mydata.rows = list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#jqGrids").jqGrid("clearGridData");
			$("#jqGrids").jqGrid("setGridParam", {
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
			alert("查询失败")
		}
	});

}

function order(rowid) {
	//获得行数据
	var rowDatas = $("#jqGrids").jqGrid('getRowData', rowid);
	localStorage.setItem("formNum", rowDatas.formNum);
	localStorage.setItem("isNtChk", rowDatas.isNtChk);
	window.open("../../Components/whs/cannibSngl.html?1",'height=700, width=1000, top=200, left=300,location=no, status=no');
}


var chk1 = '/mis/whs/cannib_sngl/updateCSnglChk';
var chk2 = '/mis/whs/cannib_sngl/updateCSnglNoChk';

function ntChk(x,chk) {
	//获得选中行的行号
	var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
	//获取选择行的provrId
	//对象数组
	var rowData = [];
	for(var i = 0; i < ids.length; i++) {
		var obj = {}; //对象
		//选中行的id
		//把选中行的i添加到数据对象中
		obj.formNum = $("#jqGrids").getCell(ids[i], "formNum").toString();
		obj.isNtChk = x;
		//建一个数组，把单个对象添加到数组中
		rowData[i] = obj;
	}
	if(rowData.length == 0) {
		alert("请选择单据!")
	} else {
		var data = {
			"reqHead": reqhead,
			"reqBody": {
				"list": rowData
			}
		};
		var Data = JSON.stringify(data);
		$.ajax({
			url: url + chk,
			type: 'post',
			data: Data,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
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
				alert(data.respHead.message);
				if(data.respHead.isSuccess==true){
					var initial = 1;
					window.newPage = initial;
					var index = '';
					var sortorder = '';
					search(index, sortorder,initial)
				}
			},
			error: function() {
				alert("审核失败")
			}
		})
	}

}
var isclick = true;
//审核与弃审
$(function() {
	//审核
	$(".toExamine").click(function() {
		if(isclick) {
			isclick = false;
			ntChk(1, chk1);
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	});
	//弃审
	$(".noTo").click(function() {
		if(isclick) {
			isclick = false;
			ntChk(0, chk2);
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	})
})

//删除
$(function() {
	$(".delOrder").click(function() {
		//获得选中行的行号
		var ids = $('#jqGrids').jqGrid('getGridParam', 'selarrrow');
		//获取选择行的provrId
		var rowData = [];
		for(var i = 0; i < ids.length; i++) {
			var gr = $("#jqGrids").getGridParam('selrow');
			//选中行的id
			var data = $("#jqGrids").getCell(ids[i], "formNum");
			//建一个数组，把选中行的id添加到这个数组中去。
			rowData[i] = data;
		}
		if(rowData.length == 0) {
			alert("请选择单据!")
		} else if(confirm("确定删除？")) {
			var formNum = rowData.toString();
			var data = {
				"reqHead": reqhead,
				"reqBody": {
					"formNum": formNum,
				}
			};
			var Data = JSON.stringify(data);
			$.ajax({
				url: url + '/mis/whs/cannib_sngl/deleteAllCSngl',
				type: 'post',
				data: Data,
				dataType: 'json',
				async: true,
				contentType: 'application/json;charset=utf-8',
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
					alert(data.respHead.message);
					if(data.respHead.isSuccess==true){
						var initial = 1;
						window.newPage = initial;
						var index = '';
						var sortorder = '';
						search(index, sortorder,initial)
					}
				},
				error: function() {
					alert("删除失败")
				}
			})
		}
	})
})

// 导入
$(document).ready(function(){
    $('.dropdown-menu li').click(function(){
        var rs=$(this).text();
        $('#dropdownMenu2').text(rs);
    });
    $('.dropdown-menu .li1').click(function(){
//  	alert("U8导入")
    	var files = $("#FileUpload").val()
    	var fileObj = document.getElementById("FileUpload").files[0];
        var formFile = new FormData();
       	formFile.append("action", "UploadVMKImagePath");  
       	formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
        var data = formFile;
        if(files != "") {
        	$.ajax({
	            type: 'post',
	            url: url + "/mis/whs/cannib_sngl/uploadFileAddDbU8",
	            data:data,
	          	dataType: "json",
	           	cache: false,//上传文件无需缓存
	           	processData: false,//用于对data参数进行序列化处理 这里必须false
	           	contentType: false, //必须
	           	success: function(data) {
	           		alert(data.respHead.message)
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
    $('.dropdown-menu .li2').click(function(){
//  	alert("新系统导入")
    	var files = $("#FileUpload").val()
    	var fileObj = document.getElementById("FileUpload").files[0];
        var formFile = new FormData();
       	formFile.append("action", "UploadVMKImagePath");  
       	formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
        var data = formFile;
        if(files != "") {
        	$.ajax({
	            type: 'post',
	            url: url + "/mis/whs/cannib_sngl/uploadFileAddDb",
	            data:data,
	          	dataType: "json",
	           	cache: false,//上传文件无需缓存
	           	processData: false,//用于对data参数进行序列化处理 这里必须false
	           	contentType: false, //必须
	           	success: function(data) {
	           		alert(data.respHead.message)
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

var arr=[];
var obj={}
//导出
$(document).on('click', '.exportExcel', function() {
	var formNum = $(".formNum").val();
	var tranOutWhsEncd = $("input[name='tranOutWhsEncd']").val()
	var tranInWhsEncd = $("input[name='tranInWhsEncd']").val()
	var formNumStart = $("input[name='formNumStart']").val()
	var formNumEnd = $("input[name='formNumEnd']").val()
	var formDt1 = $(".formDt1").val();
	var formDt2 = $(".formDt2").val();
	var isNtChk = $("#isNtChk").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();

	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"formNum": formNum,
			"tranOutWhsEncd": tranOutWhsEncd,
			"tranInWhsEncd": tranInWhsEncd,
			"formDt1": formDt1,
			"formDt2": formDt2,
			"formNumStart": formNumStart,
			"formNumEnd": formNumEnd,
			"invtyClsEncd": invtyClsEncd,
			'isNtChk':isNtChk
		}
	};
	var Data = JSON.stringify(savedata);
	$.ajax({
		url: url + '/mis/whs/cannib_sngl/queryListDaYin',
		type: 'post',
		data: Data,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
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
			var list = data.respBody.list;
			var execlName = "调拨单"
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
})
