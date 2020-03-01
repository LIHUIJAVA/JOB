$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");
	$("#box").hide();
})
var count;
var pages;
var page = 1;
var rowNum;
$(function() {
	pageInit();
})
function getJQAllData1() {
	//拿到grid对象
	var obj = $("#stic_jqgrids");
	//获取grid表中所有的rowid值
	var rowIds = obj.getDataIDs();
	//初始化一个数组arrayData容器，用来存放rowData
	var arrayData = new Array();
	if(rowIds.length > 0) {
		for(var i = 0; i < rowIds.length; i++) {
			if(obj.getRowData(rowIds[i]).deptName == "总计") {
				arrayData.push(obj.getRowData(rowIds[i]));
			}
		}
	}
	return arrayData;
}
//表格初始化
function pageInit() {
	allHeight()
//	rowNum = $("#_input").val();
	$("#stic_jqgrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['部门名称', '业务员名称', '客户名称','存货编码', '存货名称', '数量', '无税单价', '无税金额',
			'含税单价', '价税合计', '税额', '成本', '毛利', '毛利率'
		],
		colModel: [{
				name: 'deptName',
				editable: true,
				align: 'center',
			},
			{
				name: 'accNum',
				editable: true,
				align: 'center',
			},
			{
				name: 'custNm',
				editable: true,
				align: 'center',
			},
			{
				name: 'invtyEncd',
				editable: true,
				align: 'center',
			},
			{
				name: 'invtyNm',
				editable: false,
				align: 'center',

			},
			{
				name: 'qty',
				editable: true,
				align: 'center',
				sorttype:'integer', // 数量排序

			},
			{
				name: 'noTaxUprc',
				editable: false,
				align: 'center',
				sorttype:'integer', // 数量排序
			},
			{
				name: 'amt',
				editable: false,
				align: 'center',
				sorttype:'integer', 
			},
			{
				name: 'cntnTaxUprc',
				editable: false,
				align: 'center',
				sorttype:'integer', 
			},
			{
				name: 'sellInAmt',
				editable: true,
				align: 'center',
				sorttype:'integer', 
			},
			{
				name: 'sellAmt',
				editable: true,
				align: 'center',
				sorttype:'integer', 
			},
			{
				name: 'sellCost',
				editable: true,
				align: 'center',
				sorttype:'integer', 
			},
			{
				name: 'gross',
				editable: false,
				align: 'center',
				sorttype:'integer', 
			},
			{
				name: 'grossRate',
				editable: false,
				align: 'center',
				sorttype:'integer', 
			}
		],
		sortable:true,
		autowidth: true,
		height: height,
		autoScroll: true,
		shrinkToFit: false,
		sortable:true,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 500,
		pager: '#stic_jqgridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
		multiboxonly: true,
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条
		caption: "销售统计表", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#stic_jqgrids").getGridParam('records'); //获取返回的记录数
			page = $("#stic_jqgrids").getGridParam('page'); //获取返回的当前页
			var rowNum1 = $("#stic_jqgrids").getGridParam('rowNum'); //获取显示配置记录数量

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
			search();
		},
		footerrow: true,
		gridComplete: function() { 
			var list = getJQAllData1()
			for(var i = 0;i<list.length;i++){ 
				var qty1 = list[0].qty
				var amt1 = list[0].amt
				var sellInAmt1 = list[0].sellInAmt
				var sellAmt1 = list[0].sellAmt
				var sellCost1 = list[0].sellCost
				var gross1 = list[0].gross
			}
			var qty = $("#stic_jqgrids").getCol('qty', false, 'sum');
			var sellInAmt = $("#stic_jqgrids").getCol('sellInAmt', false, 'sum');
			var amt = $("#stic_jqgrids").getCol('amt', false, 'sum');
			var sellAmt = $("#stic_jqgrids").getCol('sellAmt', false, 'sum');
			var sellCost = $("#stic_jqgrids").getCol('sellCost', false, 'sum');
			var gross = $("#stic_jqgrids").getCol('gross', false, 'sum');
			var num = gross.toFixed(precision)
			var sum = amt.toFixed(precision)
			var grossRate = num / sum * 100
			if(isNaN(grossRate)) {
				grossRate = 0
			}
			var qty2 = qty - qty1
			var sellInAmt2 = sellInAmt - sellInAmt1
			var amt2 = amt - amt1
			var sellAmt2 = sellAmt - sellAmt1
			var sellCost2 = sellCost - sellCost1
			var gross2 = gross - gross1
			if(isNaN(qty2)) {
				qty2 = 0
			}
			if(isNaN(sellInAmt2)) {
				sellInAmt2 = 0
			}
			if(isNaN(amt2)) {
				amt2 = 0
			}
			if(isNaN(sellAmt2)) {
				sellAmt2 = 0
			}
			if(isNaN(sellCost2)) {
				sellCost2 = 0
			}
			if(isNaN(gross2)) {
				gross2 = 0
			}
			$("#stic_jqgrids").footerData('set', {
				"deptName": "本页合计",
				"qty": qty2.toFixed(prec),
				"sellInAmt": precision(sellInAmt2,2),
				"amt": precision(amt2,2),
				"sellAmt": precision(sellAmt2,2),
				"sellCost": precision(sellCost2,2),
				"gross": gross2.toFixed(prec),
				"grossRate": grossRate.toFixed(prec) + '%',

			}          );    
		},
		onSortCol: function(index, colindex, sortorder) {
			search(index, sortorder)
		}
	});
}

//查询按钮
$(document).on('click', '.find', function() {
	search();
})
//查询按钮
$(document).on('click', '.sure', function() {
	var bllgDt1 = $("input[name='bllgDt3']").val()
    $("input[name='bllgDt1']").val(bllgDt1)
    
    var bllgDt2 = $("input[name='bllgDt4']").val()
    $("input[name='bllgDt2']").val(bllgDt2)
    $("#big_wrap").hide()
	$("#box").hide()
	search()
	
	$("input[name='invtyEncd']").attr('id', 'invtyEncd');
	$("input[name='invtyEncd1']").attr('id', '');
	$("input[name='invtyNm']").attr('id', 'invtyNm');
	$("input[name='invtyNm1']").attr('id', '');

	$("input[name='custId']").attr('id', 'custId');
	$("input[name='custId1']").attr('id', '');
	$("input[name='custNm']").attr('id', 'custNm');
	$("input[name='custNm1']").attr('id', '');
})

$(function() {
	$(".more").click(function() {
		$("#big_wrap").show()
		$("#box").show()
        $("input[name='invtyEncd1']").attr('id','invtyEncd');
        $("input[name='invtyEncd']").attr('id','');
        $("input[name='invtyNm1']").attr('id','invtyNm');
        $("input[name='invtyNm']").attr('id','');
		var invtyEncd = $("input[name='invtyEncd']").val()
		var invtyEncd1 = $("input[name='invtyEncd1']").val(invtyEncd)
		var invtyNm = $("input[name='invtyNm']").val()
		var invtyNm1 = $("input[name='invtyNm1']").val(invtyNm)
        
		$("input[name='custId1']").attr('id','custId');
        $("input[name='custId']").attr('id','');
        var custId = $("input[name='custId']").val()
        var custId1 = $("input[name='custId1']").val(custId)
        
        $("input[name='custNm1']").attr('id','custNm');
        $("input[name='custNm']").attr('id','');
        var custNm = $("input[name='custNm']").val()
        var custNm1 = $("input[name='custNm1']").val(custNm)
        
        var bllgDt1 = $("input[name='bllgDt1']").val()
        $("input[name='bllgDt3']").val(bllgDt1)
        
        var bllgDt2 = $("input[name='bllgDt2']").val()
        $("input[name='bllgDt4']").val(bllgDt2)
        
		$("input[name='bllgDt3']").attr('class','bllgDt1 date date1');
        $("input[name='bllgDt1']").attr('class','date date1');
        
		$("input[name='bllgDt4']").attr('class','bllgDt2 date date2');
        $("input[name='bllgDt2']").attr('class','date date2');
	})
	$(".cancel").click(function(e) {
		$("#big_wrap").hide()
		$("#box").hide()
       	back()
   	});
})

function back(){
	$("input[name='invtyEncd1']").attr('id','');
    $("input[name='invtyEncd']").attr('id','invtyEncd');
    $("input[name='invtyNm1']").attr('id','');
    $("input[name='invtyNm']").attr('id','invtyNm');
    
	$("input[name='custId1']").attr('id','');
    $("input[name='custId']").attr('id','custId');
   
	$("input[name='bllgDt3']").attr('class','date date1');
    $("input[name='bllgDt1']").attr('class','bllgDt1 date date1');
    
	$("input[name='bllgDt4']").attr('class','date date2');
    $("input[name='bllgDt2']").attr('class','bllgDt2 date date2');
}


function search(index, sortorder){
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var invtyEncd = $("#invtyEncd").val();
	var custId = $("#custId").val();
	var bllgDt1 = $(".bllgDt1").val();
	var bllgDt2 = $(".bllgDt2").val();

	var invtyCd = $("input[name='invtyCd']").val();
	var whsEncd = $("input[name='whsEncd']").val();
	var batNum = $("input[name='batNum']").val();
	var accNum = $("input[name='accNum']").val();
//	var accNum = localStorage.getItem("accNum")
	var deptId = $("input[name='deptId']").val();
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": invtyEncd,
			"custId": custId,
			"bllgDt1": bllgDt1,
			"bllgDt2": bllgDt2,
			
			"invtyCd": invtyCd,
			"whsEncd": whsEncd,
			"batNum": batNum,
			"accNum": accNum,
			"deptId": deptId,
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
		url: url + '/mis/purc/SellSngl/selectXSTJList',
		async: true,
		data: saveData,
		dataType: 'json',
		beforeSend: function() {
			$(".zhezhao").css("display", "block");
			$("#loader").css("display", "block");

		},
		success: function(data) {
			var list = data.respBody.list;
			var mydata = {};
			mydata.rows = list;
			mydata.page = page;
			mydata.records = data.respBody.count;
			mydata.total = data.respBody.pages;
			$("#stic_jqgrids").jqGrid("clearGridData");
			$("#stic_jqgrids").jqGrid("setGridParam", {
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
				url: url + "/mis/account/TermBgnBal/uploadTermBgnBalFile",
				data: data,
				dataType: "json",
				cache: false, //上传文件无需缓存
				processData: false, //用于对data参数进行序列化处理 这里必须false
				contentType: false, //必须
				success: function(data) {
					alert(data.respHead.message);
					if(data.respHead.isSuccess == true) {
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

//导出
var arr = [];
var obj = {}
$(document).on('click', '.exportExcel', function() {
	var data = {
		"reqHead": reqhead,
		"reqBody": {}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/purc/IntoWhs/printingIntoWhsList',
		type: 'post',
		data: Data,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		success: function(data) {
			var list = data.respBody.list;
			var execlName = '销售统计表'
			ExportData(list, execlName)
		},
		error: function() {
			alert("导出失败")
		}
	})
})
