//会计科目
var page = 1;
var rowNum;
//表格初始化
$(function() {
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	jQuery("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['级别', '父级科目编码', '科目编码', '科目名称', '是否银行科目', '是否现金科目', '科目类型',
			'是否银行帐', '是否日记账', '余额方向'    ,'是否个人往来核算', '是否客户往来核算', '是否供应商往来核算',
			'是否部门核算', '是否项目核算',   
		], //jqGrid的列显示名字
		colModel: [{
				name: "encdLvlSub",
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: "pId",
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: "subjId",
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: "subjNm", //科目名称
				align: "left",
				editable: true,
				sortable: false,
			},
			{
				name: 'isNtBankSubj',
				align: "center",
				editable: true,
				sortable: false,
				edittype: 'select',
				editoptions: {
					value: {
						0: "否",
						1: "是"
					},
					dataInit: function(element) { 
						$(element).keydown(function(event) {            
							if(event.keyCode == 13) {
								$(".saveOrder").click();
							}           
						});        
					}
				}
			},
			{
				name: 'isNtCashSubj',
				align: "center",
				editable: true,
				sortable: false,
				edittype: 'select',
				editoptions: {
					value: {
						0: "否",
						1: "是"
					},
					dataInit: function(element) { 
						$(element).keydown(function(event) {            
							if(event.keyCode == 13) {
								$(".saveOrder").click();
							}           
						});        
					}
				}
			},
			{
				name: "subjTyp",
				align: "center",
				editable: true,
				sortable: false,
				edittype: 'select',
				editoptions: {
					value: {
						'资产': "资产",
						'负债': "负债",
						'共同': "共同",
						'权益': "权益",
						'成本': "成本",
						'损益': "损益"
					},
					dataInit: function(element) { 
						$(element).keydown(function(event) {            
							if(event.keyCode == 13) {
								$(".saveOrder").click();
							}           
						});        
					}
				}
			},
			{
				name: 'isNtBkat',
				align: "center",
				editable: true,
				sortable: false,
				edittype: 'select',
				editoptions: {
					value: {
						0: "否",
						1: "是"
					},
					dataInit: function(element) { 
						$(element).keydown(function(event) {            
							if(event.keyCode == 13) {
								$(".saveOrder").click();
							}           
						});        
					}
				}
			},
			{
				name: 'isNtDayBookEntry',
				align: "center",
				editable: true,
				sortable: false,
				edittype: 'select',
				editoptions: {
					value: {
						0: "否",
						1: "是"
					},
					dataInit: function(element) { 
						$(element).keydown(function(event) {            
							if(event.keyCode == 13) {
								$(".saveOrder").click();
							}           
						});        
					}
				}
			},
			{
				name: 'balDrct',
				align: "center",
				editable: true,
				sortable: false,
				edittype: 'select',
				editoptions: {
					value: {
						0: "借",
						1: "贷"
					},
					dataInit: function(element) { 
						$(element).keydown(function(event) {            
							if(event.keyCode == 13) {
								$(".saveOrder").click();
							}           
						});        
					}
				}
			},
			
			
			{
				name: 'isNtIndvRecoAccti',//是否个人往来核算
				align: "center",
				editable: true,
				sortable: false,
				edittype: 'select',
				editoptions: {
					value: {
						0: "否",
						1: "是"
					},
					dataInit: function(element) { 
						$(element).keydown(function(event) {            
							if(event.keyCode == 13) {
								$(".saveOrder").click();
							}           
						});        
					}
				}
			},
			{
				name: 'isNtCustRecoAccti',
				align: "center",
				editable: true,
				sortable: false,
				edittype: 'select',
				editoptions: {
					value: {
						0: "否",
						1: "是"
					},
					dataInit: function(element) { 
						$(element).keydown(function(event) {            
							if(event.keyCode == 13) {
								$(".saveOrder").click();
							}           
						});        
					}
				}
			},
			{
				name: 'isNtProvrRecoAccti',
				align: "center",
				editable: true,
				sortable: false,
				edittype: 'select',
				editoptions: {
					value: {
						0: "否",
						1: "是"
					},
					dataInit: function(element) { 
						$(element).keydown(function(event) {            
							if(event.keyCode == 13) {
								$(".saveOrder").click();
							}           
						});        
					}
				}
			},
			{
				name: 'isNtDeptAccti',
				align: "center",
				editable: true,
				sortable: false,
				edittype: 'select',
				editoptions: {
					value: {
						0: "否",
						1: "是"
					},
					dataInit: function(element) { 
						$(element).keydown(function(event) {            
							if(event.keyCode == 13) {
								$(".saveOrder").click();
							}           
						});        
					}
				}
			},
			{
				name: 'isNtProjAccti',
				align: "center",
				editable: true,
				sortable: false,
				edittype: 'select',
				editoptions: {
					value: {
						0: "否",
						1: "是"
					},
					dataInit: function(element) { 
						$(element).keydown(function(event) {            
							if(event.keyCode == 13) {
								$(".saveOrder").click();
							}           
						});        
					}
				}
			}
		],
		height: 320,
		autowidth: true,
		autoScroll: true,
		shrinkToFit: false,
		viewrecords: true,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 500,
		rowList: [500, 1000, 3000], //可供用户选择一页显示多少条
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		multiselect: true, //复选框
		multiboxonly: true,
		cellsubmit: 'clientArray',
		caption: "会计科目", //表格的标题名字
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
			loadLocalData();
		},
		ondblClickRow: function() {
			var gr = $("#jqGrids").jqGrid('getGridParam', 'selrow'); //获取行id
			var encdLvlSub = $("#jqGrids").getCell(gr, "encdLvlSub");
			if(encdLvlSub == 1) {
				$("#jqGrids").setColProp("pId", {
					editable: false
				});
			}else{
				$("#jqGrids").setColProp("pId", {
					editable: true
				});
			}
			if(encdLvlSub) {
				mType = 2;
				$('#jqGrids').editRow(gr, true);
				$("#" + gr + "_encdLvlSub").attr("readonly", "readonly");
				$("#" + gr + "_subjId").attr("readonly", "readonly");
			}
		}
	})
})

$(function() {
	$(".search").click(function(){
		loadLocalData()
	})
})

function loadLocalData() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var subjId = $(".subjId").val();
	var subjTyp = $("#subjTyp").val();
	var showData = {
		"reqHead": reqhead,
		"reqBody": {
			"subjId":subjId,
			"subjTyp":subjTyp,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var postData = JSON.stringify(showData);
	$.ajax({
		type: "post",
		url: url + "/mis/account/acctItmDoc/queryAcctItmDocList", //列表
		async: true,
		data: postData,
		dataType: 'json',
		contentType: 'application/json',
		//开始加载动画  添加到ajax里面
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");
		},
		success: function(data) {
			var data1 = data.respBody.list;
			for(var i = 0; i < data1.length; i++) {
				if(data1[i].isNtBankSubj == 0) {
					data1[i].isNtBankSubj = "否"
				} else if(data1[i].isNtBankSubj == 1) {
					data1[i].isNtBankSubj = "是"
				}
				if(data1[i].isNtCashSubj == 0) {
					data1[i].isNtCashSubj = "否"
				} else if(data1[i].isNtCashSubj == 1) {
					data1[i].isNtCashSubj = "是"
				}
				if(data1[i].isNtBkat == 0) {
					data1[i].isNtBkat = "否"
				} else if(data1[i].isNtBkat == 1) {
					data1[i].isNtBkat = "是"
				}
				if(data1[i].isNtDayBookEntry == 0) {
					data1[i].isNtDayBookEntry = "否"
				} else if(data1[i].isNtDayBookEntry == 1) {
					data1[i].isNtDayBookEntry = "是"
				}
				if(data1[i].balDrct == 0) {
					data1[i].balDrct = "借"
				} else if(data1[i].balDrct == 1) {
					data1[i].balDrct = "贷"
				}
				
				
				
				
				if(data1[i].isNtIndvRecoAccti == 0) {
					data1[i].isNtIndvRecoAccti = "否"
				} else if(data1[i].isNtIndvRecoAccti == 1) {
					data1[i].isNtIndvRecoAccti = "是"
				}
				if(data1[i].isNtCustRecoAccti == 0) {
					data1[i].isNtCustRecoAccti = "否"
				} else if(data1[i].isNtCustRecoAccti == 1) {
					data1[i].isNtCustRecoAccti = "是"
				}
				if(data1[i].isNtProvrRecoAccti == 0) {
					data1[i].isNtProvrRecoAccti = "否"
				} else if(data1[i].isNtProvrRecoAccti == 1) {
					data1[i].isNtProvrRecoAccti = "是"
				}
				if(data1[i].isNtDeptAccti == 0) {
					data1[i].isNtDeptAccti = "否"
				} else if(data1[i].isNtDeptAccti == 1) {
					data1[i].isNtDeptAccti = "是"
				}
				if(data1[i].isNtProjAccti == 0) {
					data1[i].isNtProjAccti = "否"
				} else if(data1[i].isNtProjAccti == 1) {
					data1[i].isNtProjAccti = "是"
				}
			}
			var myData = [];
			funa(data1);
			//第一级A
			function funa(arr) {
				for(var i = 0; i < arr.length; i++) {
					if((arr[i].children != null) && (arr[i].children.length > 0)) {
						myData.push(arr[i]); //添加Grid
					} else {
						myData.push(arr[i]); //添加Grid
					}
				}
			}
			var mydata = {};
			mydata.rows = myData;
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
			alert("加载失败")
		}
	})
}

var mType = 0;
$(function() {
	$(".saveOrder").click(function() {
		$("#info_dialog").empty()
		if(mType == 1) {
			$(".addOrder").css("background-color", 'black')
			addNewData(); //添加新数据
		} else if(mType == 2) {
			addEditData(); //编辑按钮
		}
	})
})

//声明新行号
var newrowid;

//点击增加按钮
$(function() {
	$(".addOrder").click(function() {
		mType = 1;
		var selectedId = $("#jqGrids").jqGrid("getGridParam", "selrow");
		var gr = $("#jqGrids").jqGrid('getDataIDs');
		//获得当前最大行号（数据编码）
		var rowid = Math.max.apply(Math, gr);
		//获得新添加行的行号（数据编码）
		newrowid = rowid + 1;
		var dataRow = {
			subjId: "",
			subjNm: '',
			encdLvlSub: '',
			isNtCashSubj: 0,
			isNtBankSubj: 0,
			subjTyp: "",
			isNtBkat: 0,
			isNtDayBookEntry: 0,
		};

		//将新添加的行插入到第一列
		$("#jqGrids").jqGrid("addRowData", newrowid, dataRow, "first");
		//设置grid单元格可编辑
		$('#jqGrids').jqGrid('editRow', newrowid, true);
		$(".addOrder").addClass("gray");
		$('button').attr('disabled', false);
		$(".gray").attr('disabled', true)

	})
})

function addNewData() {
	var encdLvlSub = $("#" + newrowid + "_encdLvlSub").val();
	var subjId = $("#" + newrowid + "_subjId").val();
	var subjNm = $("#" + newrowid + "_subjNm").val();
	var isNtCashSubj = parseInt($("#" + newrowid + "_isNtCashSubj").val());
	var isNtBankSubj = parseInt($("#" + newrowid + "_isNtBankSubj").val());
	var subjTyp = $("#" + newrowid + "_subjTyp").val();
	var isNtBkat = parseInt($("#" + newrowid + "_isNtBkat").val());
	var isNtDayBookEntry = parseInt($("#" + newrowid + "_isNtDayBookEntry").val());
	var balDrct = $("#" + newrowid + "_balDrct").val();
	var pId = $("#" + newrowid + "_pId").val();
	
	var isNtIndvRecoAccti = $("#" + newrowid + "_isNtIndvRecoAccti").val();
	var isNtCustRecoAccti = parseInt($("#" + newrowid + "_isNtCustRecoAccti").val());
	var isNtProvrRecoAccti = parseInt($("#" + newrowid + "_isNtProvrRecoAccti").val());
	var isNtDeptAccti = $("#" + newrowid + "_isNtDeptAccti").val();
	var isNtProjAccti = $("#" + newrowid + "_isNtProjAccti").val();
	var save = {
		"reqHead": reqhead,
		"reqBody": {
			'pId': pId,
			'encdLvlSub': encdLvlSub,
			'subjId': subjId,
			'subjNm': subjNm,
			'isNtCashSubj': isNtCashSubj,
			'isNtBankSubj': isNtBankSubj,
			'subjTyp': subjTyp,
			'isNtBkat': isNtBkat,
			'isNtDayBookEntry': isNtDayBookEntry,
			'balDrct': balDrct,
			
			'isNtIndvRecoAccti': isNtIndvRecoAccti,
			'isNtCustRecoAccti': isNtCustRecoAccti,
			'isNtProvrRecoAccti': isNtProvrRecoAccti,
			'isNtDeptAccti': isNtDeptAccti,
			'isNtProjAccti': isNtProjAccti,
		}
	}
	var saveJson = JSON.stringify(save);
	$.ajax({
		type: "post",
		url: url + "/mis/account/acctItmDoc/insertAcctItmDoc ",
		async: true,
		data: saveJson,
		dataType: 'json',
		contentType: 'application/json',
		success: function(data) {
			alert(data.respHead.message)
			if(data.respHead.isSuccess==true){
				loadLocalData();
			}
		},
		error: function() {
			alert("增加失败")
		}
	});
}

//编辑函数
function addEditData() {
	var gr = $("#jqGrids").jqGrid('getGridParam', 'selarrrow');
	var rowData = [];
	for(var i = 0; i < gr.length; i++) {
		var obj = {};
		obj.pId = $("#" + gr[i] + "_pId").val();
		obj.encdLvlSub = $("#" + gr[i] + "_encdLvlSub").val();
		obj.subjId = $("#" + gr[i] + "_subjId").val();
		obj.subjNm = $("#" + gr[i] + "_subjNm").val();
		obj.isNtCashSubj = parseInt($("#" + gr[i] + "_isNtCashSubj").val());
		obj.isNtBankSubj = parseInt($("#" + gr[i] + "_isNtBankSubj").val());
		obj.subjTyp = $("#" + gr[i] + "_subjTyp").val();
		obj.isNtBkat = parseInt($("#" + gr[i] + "_isNtBkat").val());
		obj.isNtDayBookEntry = parseInt($("#" + gr[i] + "_isNtDayBookEntry").val());
		obj.balDrct = $("#" + gr[i] + "_balDrct").val();
		
		obj.isNtIndvRecoAccti = parseInt($("#" + gr[i] + "_isNtIndvRecoAccti").val());
		obj.isNtCustRecoAccti = $("#" + gr[i] + "_isNtCustRecoAccti").val();
		obj.isNtProvrRecoAccti = parseInt($("#" + gr[i] + "_isNtProvrRecoAccti").val());
		obj.isNtDeptAccti = parseInt($("#" + gr[i] + "_isNtDeptAccti").val());
		obj.isNtProjAccti = $("#" + gr[i] + "_isNtProjAccti").val();

		rowData[i] = obj;
	}
	var edit = {
		"reqHead": reqhead,
		"reqBody": {
			'list': rowData
		}
	}
	var editJson = JSON.stringify(edit);
	$.ajax({
		type: "post",
		url: url + "/mis/account/acctItmDoc/updateAcctItmDoc",
		async: true,
		data: editJson,
		dataType: 'json',
		contentType: 'application/json',
		success: function(data) {
			alert(data.respHead.message)
			if(data.respHead.isSuccess==true){
				loadLocalData();
			}
		},
		error: function() {
			alert("修改失败")
		}
	});
}

//删除行
$(function() {
	$(".delOrder").click(function() {
		var ids = $("#jqGrids").jqGrid('getGridParam', 'selarrrow'); //获取选中=行id
		var rowData = []
		for(i = 0; i < ids.length; i++) {
			var gr = $("#jqGrids").getGridParam('selrow');
			//选中行的id
			var jstime = $("#jqGrids").getCell(ids[i], "subjId");
			rowData[i] = jstime
		}
		var rowDatas = rowData.toString();
		var deleteAjax = {
			"reqHead": reqhead,
			"reqBody": {
				"subjId": rowDatas
			}
		}
		var deleteData = JSON.stringify(deleteAjax)
		if(ids.length == 0) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + "/mis/account/acctItmDoc/deleteAcctItmDoc",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(data) {
					alert(data.respHead.message)
					if(data.respHead.isSuccess==true){
						loadLocalData();
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
				url: url + "/mis/account/acctItmDoc/uploadFileAddDb",
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
	var subjId = $(".subjId").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"subjId":subjId
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/account/acctItmDoc/queryAcctItmDocPrint',
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
			var list = data.respBody.list;
			obj = list;
//			console.log(obj)
			daochu(obj)
		},
		error: function() {
			alert("error")
		}
	})
})

function daochu(JSONData) {
    var str = '级别,父级科目编码,科目编码,科目名称,是否银行科目,是否现金科目,科目类型,是否银行帐,是否日记账,余额方向,是否个人往来核算,是否客户往来核算,是否供应商往来核算,是否部门核算,是否项目核算\n';

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
    link.download =  "会计科目.csv";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}
