//弹框
$(function() {
	$(".biaoge").click(function() {
		window.open("../../Components/baseDoc/userList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	})
	$(".bm_biaoge").click(function() {
		window.open("../../Components/baseDoc/deptDocList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	})
})
//取当前登陆人
$(function() {
	$("#userName").val(localStorage.loginName)
})

var count;
var pages;
var page = 1;
var rowNum;
//右列表格的操作
$(function() {
	pageInit3();
})
var mType = 0;

function pageInit3() {
	//加载动画html 添加到初始的时候
//	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
//	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
//	$("#mengban").addClass("zhezhao");
	allHeight()
	jQuery("#zc_jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['总仓编码', '总仓名称', '部门名称', '仓库地址', '电话', '负责人'], //jqGrid的列显示名字
		colModel: [{
				name: 'realWhs',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: "realNm",
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
		pager: '#zc_jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
//		multiselect: true, //复选框
//		multiboxonly: true,
		caption: "仓库档案", //表格的标题名字
		//cellEdit:true, //单元格是否可编辑
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#zc_jqgrids").getGridParam('records'); //获取返回的记录数
			page = $("#zc_jqgrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#zc_jqgrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			search3()
		}
	});
}

//条件查询
$(document).on('click', '.finding', function() {
	search3()
})

function search3() {
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var realWhs = $(".realWhs").val();
	var realNm = $(".realNm").val();
	var deptEncd = $(".deptEncd").val();
	var Data = {
		"reqHead": reqhead,
		"reqBody": {
			"realWhs": realWhs,
			"realNm": realNm,
			"deptEncd": deptEncd,
			"pageNo": page,
			"pageSize": rowNum
		}
	}
	var changeData = JSON.stringify(Data)
	$.ajax({
		type: "post",
		url: url + "/mis/whs/whs_doc/queryRealWhsList",
		async: true,
		data: changeData,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		//开始加载动画  添加到ajax里面
//		beforeSend: function() {
//			$(".zhezhao").css("display", "block");
//			$("#loader").css("display", "block");
//
//		},
		success: function(data) {
			var mydata = {};
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#zc_jqgrids").jqGrid("clearGridData");
			$("#zc_jqgrids").jqGrid("setGridParam", {
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
//		complete: function() {
//			$(".zhezhao").css("display", "none");
//			$("#loader").css("display", "none");
//		},
		error: function() {
			alert("条件查询失败")
		}
	});
}


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
						search3();
					}
				}
			});
		} else {
			alert("请选择文件")
		}
	});
});

//导出--url不对
var obj = {}
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
			var list = data.respBody.list;
			obj = list;
			for(var i = 0; i < obj.length; i++) {
				delete obj[i].ordrNum;
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
    var str = '仓库编码,仓库名称,部门编码,仓库地址,电话,负责人,计价方式,对应条形码,是否进行货位管理,仓库属性,销售可用量控制方式,库存可用量控制方式,备注,是否门店,停用时间,省,市,县,是否虚拟仓,创建人,创建时间,修改人,修改时间,销售名称,\n';

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
$(document).on('click', '.yes', function() {
	//获得行号
	var gr = $("#zc_jqgrids").jqGrid('getGridParam', 'selrow');
	if(gr == null) {
		alert("未选择")
	} else  {
		//获得行数据
		var rowDatas = $("#zc_jqgrids").jqGrid('getRowData', gr);
//		window.parent.opener.document.getElementById("realNm").value= rowDatas.realNm;
		window.parent.opener.document.getElementById("realWhs").value= rowDatas.realWhs;
		localStorage.setItem("expressEncd",rowDatas.realWhs)
		window.close()
	}
})

$(document).on('click', '.cancel', function() {
	localStorage.clear();
//	window.parent.opener.document.getElementById("realNm").value= "";
	window.parent.opener.document.getElementById("realWhs").value= "";
	window.close()
})