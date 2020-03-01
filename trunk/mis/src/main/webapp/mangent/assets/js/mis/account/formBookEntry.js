$(function() {
	isSealBook(2)
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
})

var page = 1;
var rowNum;
$(function() {
	jQuery("#jqGrids").jqGrid({
		url: '../../assets/js/json/order.json', //组件创建完成之后请求数据的url
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['单据日期', '单据号', '存货编码', '存货名称', '单据类型编码', '单据类型',
			'数量', '单价', '无税金额', '单据类型编码'
		], //jqGrid的列显示名字
		colModel: [{
				name: "formDt",
				align: "center",
				editable: true,
				sorttype: 'date'
			},
			{
				name: "formNum",
				align: "center",
				editable: true,
				sorttype: 'integer'
			},
			{
				name: "invtyEncd",
				align: "center",
				editable: true,
			},
			{
				name: 'invtyNm',
				align: "center",
				editable: true,
			},
			{
				name: 'outIntoWhsTypId',
				align: "center",
				editable: true,
				sorttype: 'integer'
			},
			{
				name: 'outIntoWhsTypNm',
				align: "center",
				editable: true,
			},
			{
				name: 'qty',
				align: "center",
				editable: true,
				sorttype: 'integer'
			},
			{
				name: 'noTaxUprc',
				align: "center",
				editable: true,
				sorttype: 'integer'
			},
			{
				name: 'noTaxAmt',
				align: "center",
				editable: true,
				sorttype: 'integer'
			},
			{
				name: 'outIntoWhsTypId',
				align: "center",
				editable: true,
				hidden: true
			}
		],
		//		sortable:true,
		autowidth: true,
		viewrecords: true,
		autoScroll: true,
		shrinkToFit: false,
		height: 380,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 500,
		rowList: [100, 300, 500, 1000], //可供用户选择一页显示多少条
		footerrow: true,
		pager: '#jqgridPager', //表格页脚的占位符(一般是div)的id
		multiboxonly: true,
		multiselect: true, //复选框
		caption: "正常单据记账", //表格的标题名字
		gridComplete: function() {                   
			var qty = $("#jqGrids").getCol('qty', false, 'sum');  
			var noTaxAmt = $("#jqGrids").getCol('noTaxAmt', false, 'sum');    
			noTaxAmt = precision(noTaxAmt, 2);
			$("#jqGrids").footerData('set', { 
				"formDt": "本页合计",
				qty: qty,
				noTaxAmt: noTaxAmt
			}          );        
		},
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
			search()
		},
		//双击弹出采购订单
		ondblClickRow: function(rowid) {
			var rowDatas = $("#jqGrids").jqGrid('getRowData', rowid);
			var outIntoWhsTypId = rowDatas.outIntoWhsTypId;
			var formNum = rowDatas.formNum;
			if(outIntoWhsTypId) {
				order(rowid, outIntoWhsTypId, formNum);
			}

		},
	})
})

function order(rowid, outIntoWhsTypId, formNum) {
	//获得行数据
	if(outIntoWhsTypId == 9) {
		localStorage.setItem("intoWhsSnglId", formNum);
		window.open("../../Components/purs/intoWhs.html?2", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	} else if(outIntoWhsTypId == 10) {
		localStorage.setItem("outWhsId", formNum);
		window.open("../../Components/sell/sellOutWhs.html?2", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	} else {
		localStorage.setItem("formNum", formNum);
		window.open("../../Components/whs/otherOutIntoWhs.html?2", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	}
}

//条件查询
$(function() {
	$('#find').click(function() {
		search()
	})
})

function search() {
	var myData = {};
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var whsEncd = $("#whsEncd").val();
	var invtyClsEncd = $("input[name='invtyClsEncd']").val();
	var formType = $("#formCode").val();
	var invtyEncds = $("#invtyEncd").val();
	var formSDt = $("#formSDt").val();
	var formEDt = $("#formEDt").val();
	var loginTime = localStorage.loginTime;
	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"whsEncd": whsEncd,
			"invtyEncds": invtyEncds,
			"invtyClsEncd": invtyClsEncd,
			"formType": formType,
			"formSDt": formSDt,
			"formEDt": formEDt,
			"loginTime": loginTime,
			"pageSize": rowNum,
			"pageNo": page,
		}
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		url: url + "/mis/account/form/normal/list",
		async: true,
		data: postD2,
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
			var list = data.respBody.list;
			var arr = [];
			for(var i = 0; i < list.length; i++) {
				if(list[i].bookEntrySub != null) {
					var entrs = list[i].bookEntrySub
					for(var j = 0; j < entrs.length; j++) {
						(function(j) {
							var newObj = cloneObj(data.respBody.list);
							for(var k in entrs[j]) {
								newObj[i][k] = entrs[j][k]
							}
							arr.push(newObj[i])
						})(j)
					}
				}
			}
			var mydata = {};
			mydata.rows = arr;
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
		error: function() {
			alert("条件查询失败")
		}
	});

}
$(function() {
	$(".account").click(function() {
		if(isMthSeal == 1) {
			var gr = $("#jqGrids").jqGrid('getGridParam', 'selarrrow');
			var num = [];
			for(i = 0; i < gr.length; i++) {
				var arr = {}
				//获得行数据
				var rowData = $("#jqGrids").jqGrid('getRowData', gr[i]);
				arr.formNum = rowData.formNum
				arr.outIntoWhsTypId = rowData.outIntoWhsTypId
				num.push(arr)
			}
			var data2 = {
				"reqHead": reqhead,
				"reqBody": {
					"list": num
				}
			};
			var postD2 = JSON.stringify(data2);
			if(num.length == 0) {
				alert("请选择要记帐的单据")
			} else {
				$.ajax({
					type: "post",
					url: url + "/mis/account/form/normal/book",
					async: true,
					data: postD2,
					dataType: 'json',
					contentType: 'application/json; charset=utf-8',
					beforeSend: function() {
						$(".zhezhao").css("display", "block");
						$("#loader").css("display", "block");
					},
					success: function(data) {
						alert(data.respHead.message)
						if(data.respHead.isSuccess == true) {
							search();
						}
					},
					//结束加载动画
					complete: function() {
						$(".zhezhao").css("display", "none");
						$("#loader").css("display", "none");
					},
				})
			}
		} else {
			alert("记账前请先封账")
		}
	})
})
var isMthSeal;
//封账
$(function() {
	$('.sealBook').click(function() {
		isSealBook(isMthSeal)
	})
})

function isSealBook(BookStat) {
	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"isSealBook": BookStat
		}
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		url: url + "/mis/account/form/mth/sealBook",
		async: true,
		data: postD2,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");
		},
		success: function(data) {
			isMthSeal = data.respBody.list[0].isMthSeal;
			if(isMthSeal == 0) {
				$(".sealBook").html("点击封账");
			} else if(isMthSeal == 1) {
				$(".sealBook").html("点击解封账");
			}
			if(BookStat == 1 || BookStat == 0) {
				alert(data.respHead.message)
			}
		},
		//结束加载动画
		complete: function() {
			$(".zhezhao").css("display", "none");
			$("#loader").css("display", "none");
		},
		error: function() {
			alert("请检查网络")
		}
	})
}

////导入--url不对
//$(function() {
//	$(".importExcel").click(function() {
//		var files = $("#FileUpload").val()
//		var fileObj = document.getElementById("FileUpload").files[0];
//		var formFile = new FormData();
//		formFile.append("action", "UploadVMKImagePath");
//		formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
//		var data = formFile;
//		if(files != "") {
//			$.ajax({
//				type: 'post',
//				url: url + "/mis/purc/CustDoc/uploadCustDocFile",
//				data: data,
//				dataType: "json",
//				cache: false, //上传文件无需缓存
//				processData: false, //用于对data参数进行序列化处理 这里必须false
//				contentType: false, //必须
//				success: function(data) {
//					alert(data.respHead.message)
//					window.location.reload()
//				},
//             开始加载动画  添加到ajax里面
//             beforeSend: function() {
//		            $(".zhezhao").css("display", "block");
//		            $("#loader").css("display", "block");
//	           },
//             结束加载动画
//	           complete: function() {
//		            $(".zhezhao").css("display", "none");
//		            $("#loader").css("display", "none");
//	           },
//			});
//		} else {
//			alert("请选择文件")
//		}
//	});
//});