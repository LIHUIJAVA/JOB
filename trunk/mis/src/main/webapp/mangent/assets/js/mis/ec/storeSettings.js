var count;
var pages;
var page = 1;
var rowNum;
$(function() {
	$(document).on('click', '.storeId_biaoge', function() {
		window.open("../../Components/baseDoc/storeRecordList.html", 'newwindow', 'height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})

$(function() {
	//页面加载完成之后执行	
	pageInit();
})

//条件查询
$(document).on('click', '#find', function() {
	search()
})

//条件查询
function search() {
	var storeId = $("input[name='storeId']").val();
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量
	rowNum = parseInt(rowNum1)

	var data2 = {
		reqHead,
		"reqBody": {
			"storeId": storeId,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url3 + "/mis/ec/storeSettings/queryList",
		async: true,
		data: postD2,
		dataType: 'json',
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
			console.log(error)
		}
	});
}
function pageInit() {
	allHeight()
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	//初始化表格
	jQuery("#jqgrids").jqGrid({
		datatype: "local", //请求数据返回的类型。可选json,xml,txt
		colNames: ['店铺编码', '店铺名称', 'appKey', 'appSecret', 'accessToken', 'token失效时间', '商家id', '店铺id'], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'storeId',
				align: "center",
				index: 'invdate',
				editable: true,
				sortable: false,
			},
			{
				name: 'storeName',
				align: "center",
				index: 'id',
				editable: true,
				sortable: false,
			},
			{
				name: 'appKey',
				align: "center",
				index: 'invdate',
				editable: true,
				sortable: false,
			},
			{
				name: 'appSecret',
				align: "center",
				index: 'id',
				editable: true,
				sortable: false,
			},
			{
				name: 'accessToken',
				align: "center",
				index: 'invdate',
				editable: true,
				sortable: false,
			},
			{
				name: 'tokenDate',
				align: "center",
				index: 'invdate',
				editable: true,
				editoptions: {
					dataInit: function(element) {
						$(element).datepicker({
							dateFormat: 'yy-mm-dd'
						})
					}
				},
				sortable: false
			},
			{
				name: 'venderId',
				align: "center",
				index: 'invdate',
				editable: true,
				sortable: false
			},
			{
				name: 'shopId',
				align: "center",
				index: 'invdate',
				editable: true,
				sortable: false
			},

		],
		rowNum: 100, //一页显示多少条
		rowList: [100, 300, 500, 1000], //可供用户选择一页显示多少条			
		autowidth: true,
		height: height,
		autoScroll: true,
		shrinkToFit: false,
		sortable:true,
		loadonce:true,
		rownumbers: true,
		multiselect: true, //复选框
		multiboxonly: true,
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'storeId', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "店铺设置", //表格的标题名字	
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
			var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
			var rowDatas = $("#jqgrids").jqGrid('getRowData', gr); //获取行数据
			$("#jqgrids").setColProp("storeId",{editable:false});
			$("#jqgrids").setColProp("storeName",{editable:false});
			if($("#jqgrids").find(".editable").length > 0){ //存在编辑状态的行
				alert("存在未保存的数据，请操作完成后进行编辑！");
				return;
			}
			$("#jqgrids").editRow(gr,true);
			$("#" + gr + "_tokenDate").attr("readonly", "readonly");

		}

	});
	search()
}
function SaveModifyData() {
	var ids = $("#jqgrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowData = $("#jqgrids").jqGrid('getRowData', ids);
	var storeName = rowData.storeName;
	var storeId = rowData.storeId;
	var appKey = $("#" + ids + "_appKey").val();
	var appSecret = $("#" + ids + "_appSecret").val();
	var accessToken = $("#" + ids + "_accessToken").val();
	var venderId = $("#" + ids + "_venderId").val();
	var shopId = $("#" + ids + "_shopId").val();
	var tokenDate = $("#" + ids + "_tokenDate").val();
	if(ids == null) {
		alert("未选择数据")
	}else {
	   	var edit = {
			reqHead,
			"reqBody":{
				"storeId" : storeId,
				"storeName" : storeName,
				"appKey" : appKey,	
				"appSecret" : appSecret,
				"accessToken" : accessToken,
				"venderId" : venderId,
				"shopId" : shopId,
				"tokenDate" : tokenDate,
			}
		}
	    editJson = JSON.stringify(edit);
	    console.log(editJson)
		$.ajax({
			type:"post",
			url:url3+"/mis/ec/storeSettings/edit",
			async:true,
			data:editJson,
			dataType:'json',
			contentType: 'application/json',
			
			success:function(editMsg){
				alert(editMsg.respHead.message);
		    	window.location.reload();
			},
			error:function(){
				console.log("更新失败")
			}
		});
	}
}

var isclick = true;
$(function() {
	$(".update").click(function() {
		if(isclick) {
			isclick = false;
			SaveModifyData();
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	})
})


//导出
$(document).on('click', '.exportExcel', function() {
	var storeId = $("input[name='storeId']").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"storeId": storeId,
			"pageNo": 1,
			"pageSize": 500
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url3 + "/mis/ec/storeSettings/queryList",
		type: 'post',
		data: Data,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		success: function(data) {
			var arr=[];
			var obj={}
			var list = data.respBody.list;
			obj=list;
			daochu(obj)
		},
		error: function() {
			console.log(error)
		}
	})
	
})

function daochu(JSONData) {
    var str = '店铺编码,店铺名称,appKey,appSecret,accessToken,token过期时间,商家ID,店铺ID\n';

    for(let i=0;i<JSONData.length;i++){
        var result ='';
        if (JSONData[i].orderStatusc=='0'){
            result="是";
        } else {
            result="否";
        }
//      str += (i+1).toString()+','+JSONData[i].orderId+'\t'+','+formateOrderTime(JSONData[i].orderTime)+'\t'+','+JSONData[i].p1+'\t'+','+JSONData[i].userName+'\t'+','+JSONData[i].recMobile+'\t'+','+JSONData[i].productName+'\t'+','+result+'\t'+',\n'
		for(let item in JSONData[i]) {
			str += `${JSONData[i][item] + '\t'},`;
	    	if(JSONData[i][item]==null){
				JSONData[i][item]="";
			}
		}
		str += '\n';
    }
    var blob = new Blob([str], {type: "text/plain;charset=utf-8"});
    //解决中文乱码问题
    blob =  new Blob([String.fromCharCode(0xFEFF), blob], {type: blob.type});
    object_url = window.URL.createObjectURL(blob);
    var link = document.createElement("a");
    link.href = object_url;
    link.download =  "店铺设置.csv";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}
