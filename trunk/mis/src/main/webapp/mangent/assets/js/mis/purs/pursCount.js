//订收货采购统计表
var pages;
var page = 1;
var rowNum;
var tableSums = {};
$(function() {
	//加载动画html
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
})
//表格初始化
$(function() {
	localStorage.removeItem("index")
	localStorage.removeItem("sortorder")
	allHeight()
	$("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['存货编码','存货名称', '供应商编码','供应商名称', '规格型号', '订单数量', '退货数量','入库数量'], //jqGrid的列显示名字
		colModel: [{
				name: 'invtyEncd',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'invtyNm',
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'provrId',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'provrNm',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'spcModel',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'pursQty',
				editable: false,
				align: 'center',
				sortable: true
			},
			{
				name: 'returnQty',
				editable: false,
				align: 'center',
				sortable: true
			},
			{
				name: 'intoQty',
				editable: true,
				align: 'center',
				sortable: true
			}
		],
		sortable:true,
		loadonce: true,
		autowidth: true,
		height:height,
		autoScroll:true,
		shrinkToFit:false,
		rownumbers: true,
		forceFit: true,
		rowNum: 500,
		rowList: [500,1000, 3000, 5000],
		footerrow: true,
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		caption: "采购订收货统计表", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#jqGrids").getGridParam('records'); //获取返回的记录数
			page = $("#jqGrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#jqGrids").getGridParam('rowNum');

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
				var sortorder = 'desc'
			}
			search(index, sortorder)  
		},
		gridComplete: function() {
			$("#jqGrids").footerData('set', { 
				"invtyEncd": "总计",
				"intoQty":tableSums. intoQty,
				"pursQty":tableSums.pursQty,
				"returnQty": tableSums.returnQty,

			}          ); 
		},
		onSortCol: function(index, colindex, sortorder) {
			localStorage.setItem("index",index)
			localStorage.setItem("sortorder",sortorder)
			window.index = index;
			window.sortorder = sortorder;
			search(index, sortorder)  
		}
	})
})


$(document).on('click', '.search', function() {
	var index = ''
	var sortorder = 'desc'
	search(index, sortorder)
})

//查询按钮
function search(index, sortorder) {
	var invtyEncd = $("#invtyEncd").val();
	var provrId = $("#provrId").val();
	var intWhsDt1 = $(".intWhsDt1").val();
	var intWhsDt2 = $(".intWhsDt2").val();
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"isNtChk":1,
			"invtyEncd": invtyEncd,
			"provrId":provrId,
			"intoWhsDt1":intWhsDt1,
			"intoWhsDt2":intWhsDt2,
			"sort":index,
			"sortOrder":sortorder,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/purc/IntoWhs/selectIntoWhsAndPursOrdr',
		async: true,
		data: saveData,
		dataType: 'json',
		//开始加载动画
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
			tableSums = data.respBody.tableSums;
			var mydata = {};
			mydata.rows = data.respBody.list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#jqGrids").jqGrid("clearGridData");
			$("#jqGrids").jqGrid("setGridParam", {
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
		error: function() {
			alert("error")
		}
	});
}


//导出--url不对
var arr=[];
var obj={}
$(document).on('click', '.exportExcel', function() {
	var data = {
		"reqHead": reqhead,
		"reqBody": {}
	};
	var Data = JSON.stringify(data);
	$.ajax({
//		url: url + '/mis/purc/IntoWhs/printingIntoWhsList',
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
			//执行深度克隆
			for(var i = 0; i < list.length; i++) {
				if(list[i].intoWhsSub != null) {
					var entrs = list[i].intoWhsSub
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
			obj=arr;
			for(var i=0;i<obj.length;i++){
				delete obj[i].baoZhiQiDt;
				delete obj[i].pursOrdrNum;//采购订单id
				delete obj[i].deptName;//部门名称
				delete obj[i].intoWhsDt1;
				delete obj[i].intoWhsDt2;
				delete obj[i].iptaxRate;//进项税率
				delete obj[i].optaxRate;//销项税率
				delete obj[i].highestPurPrice;//最高进价
				delete obj[i].refCost;//参考成本
				delete obj[i].refSellPrc;//参考售价
				delete obj[i].loSellPrc;//最低售价
				delete obj[i].ltstCost;//最新成本
				delete obj[i].intoWhsSub;//子表
				delete obj[i].iList;//子表
				delete obj[i].ordrNum;//序号 
				delete obj[i].pursOrdrSubTabInd;
				delete obj[i].pursToGdsSnglSubTabInd;
				delete obj[i].returnMemo;//拒收备注
				delete obj[i].returnQty;//拒收数量
				delete obj[i].formDt1;
				delete obj[i].formDt2;
				delete obj[i].chkTm1;//拒收备注
				delete obj[i].chkTm2;//拒收数量
			}
//			daochu(obj)
		},
		error: function() {
			alert("error")
		}
	})
	
})

function daochu(JSONData) {
    var str = '入库单号,入库日期,采购类型编码,单据类型编码,出入库类型编码,收发类别编码,供应商编码,用户编码,用户名称,部门编码,部门名称,采购订单编码,到货单编码,供应商订单号,是否开票,是否结算,是否审核,审核人,审核时间,是否关闭,是否完成,创建人,创建时间,修改人,修改时间,是否记账,记账人,记账时间,检验检疫报告,备注,是否有退货,供应商名称,采购类型名称,收发类别,出入库类型名称,存货编码,仓库编码,数量,箱数,税率,无税单价,无税金额,税额,含税单价,价税合计,结算数量,结算单价,结算金额,未开票数量,未开票单价,未开票金额,暂估数量,暂估单价,暂估金额,对应发票号,保质期,国际批次,批次,生产日期,失效日期,货位编码,是否赠品,是否退货,存货名称,规格型号,存货代码,箱规,计量单位编码,计量单位名称,仓库名称,对应条形码,存货分类编码,存货分类名称,\n';

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
    link.download =  "采购入库明细表.csv";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}