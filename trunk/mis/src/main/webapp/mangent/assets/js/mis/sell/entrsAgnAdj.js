//委托代销调整单
$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban").addClass("zhezhao");

	//点击表格图标显示存货列表
	$(document).on('click', '.custId', function() {
		window.open("../../Components/baseDoc/custDocList.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	//点击表格图标显示仓库列表
	$(document).on('click', '#whsNm', function() {
		window.open("../../Components/baseDoc/whsList.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})

//表格初始化
$(function() {
	var rowNum = $("#_input").val()
	localStorage.clear();
	allHeight()
	$("#en_jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ["发货单号", '发货日期', "销售类型", "销售类型id", '客户名称', '客户id', '部门名称', '业务员', '业务员编码', '存货编码',
			'存货名称', '规格型号', '税率', '数量', '原结算价', '现结算价', '调整单金额'
		], //jqGrid的列显示名字
		colModel: [{
				name: 'delvSnglId',
				editable: false,
				align: 'center',
				sortable: false

			},
			{
				name: 'delvSnglDt',
				editable: false,
				align: 'center',
				sortable: false

			},
			{
				name: 'bizTypNm',
				editable: false,
				align: 'center',
				sortable: false

			},
			{
				name: 'bizTypId',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'custNm',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'custId',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'deptName',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'userName',
				editable: false,
				align: 'center',
				hidden: true,
				sortable: false
			},
			{
				name: 'accNum',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'invtyEncd',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'invtyNm',
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
				name: 'taxRate',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'qty',
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'cntnTaxUprc',//原结算价
				editable: false,
				align: 'center',
				sortable: false
			},
			{
				name: 'nowTaxUprc',//现结算价
				editable: true,
				align: 'center',
				sortable: false
			},
			{
				name: 'stlPrcTaxSum',//调整单金额
				editable: false,
				align: 'center',
				sortable: false
			},
		],
		loadonce: false,
		autowidth: true,
		height: height,
		autoScroll: true,
		viewrecords: true,
		autoScroll: true,
		shrinkToFit: false,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		footerrow: true,
		rowNum: 10,
		rowList: [10, 20, 30], //可供用户选择一页显示多少条
		pager: '#jqgridPager', //表格页脚的占位符(一般是div)的id
		multiselect: true, //复选框
		caption: "委托代销调整单", //表格的标题名字
		ondblClickRow: function() {
			var gr = $("#en_jqGrids").jqGrid('getGridParam', 'selrow'); //获取行id
			$('#en_jqGrids').editRow(gr, true);
		},
		gridComplete: function() { 
			var qty = 0;
			var cntnTaxUprc = 0;
			var nowTaxUprc = 0;
			var stlPrcTaxSum = 0;
			var ids = $("#en_jqGrids").getDataIDs();
			var rowData = $("#en_jqGrids").getRowData(ids[i]);
			for(var i = 0; i < ids.length; i++) {
				qty += parseFloat($("#en_jqGrids").getCell(ids[i], "qty"));
				cntnTaxUprc += parseFloat($("#en_jqGrids").getCell(ids[i], "cntnTaxUprc"));
				nowTaxUprc += parseFloat($("#en_jqGrids").getCell(ids[i], "nowTaxUprc"));
				stlPrcTaxSum += parseFloat($("#en_jqGrids").getCell(ids[i], "stlPrcTaxSum"));
			}; 
			if(isNaN(qty)){
				qty=0
			}
			if(isNaN(cntnTaxUprc)){
				cntnTaxUprc=0
			}
			if(isNaN(nowTaxUprc)){
				nowTaxUprc=0
			}
			if(isNaN(stlPrcTaxSum)){
				stlPrcTaxSum=0
			}
			cntnTaxUprc = precision(cntnTaxUprc,2)
			nowTaxUprc = precision(nowTaxUprc,2)
			stlPrcTaxSum = precision(stlPrcTaxSum,2)
			$("#en_jqGrids").footerData('set', { 
				"delvSnglId": "本页合计",
				"qty": qty,
				"cntnTaxUprc": cntnTaxUprc,
				"nowTaxUprc": nowTaxUprc,
				"stlPrcTaxSum": stlPrcTaxSum,

			}          );    
		},
	})
})


//查询按钮
$(function() {
	$(".search").click(function() {
		var myData = {};

		var custId = localStorage.cust;
		var whsEncd = localStorage.whsEncd;
		var sellSnglDt1 = $(".sellSnglDt1").val();
		var sellSnglDt2 = $(".sellSnglDt2").val();

		var savedata = {
			"reqHead": reqhead,
			"reqBody": {
				"isNtChk": 1, //已审核
				"custId": custId,
				"whsEncd": whsEncd,
				"sellSnglDt1": sellSnglDt1,
				"sellSnglDt2": sellSnglDt2,
				"pageNo": 1,
				"pageSize": 100
			}
		};
		var saveData = JSON.stringify(savedata);
		$.ajax({
			type: "post",
			contentType: 'application/json; charset=utf-8',
			url: url + '/mis/purc/EntrsAgnAdj/queryEntrsAgnAdjOrEntrsAgn',
			async: true,
			data: saveData,
			dataType: 'json',
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
				//执行深度克隆
				for(var i = 0; i < list.length; i++) {
					if(list[i].entrsAgnDelvSub != null) {
						var entrs = list[i].entrsAgnDelvSub
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
				myData = arr;
				var rowNum = $("#_input").val()
				$("#en_jqGrids").jqGrid('clearGridData');
				$("#en_jqGrids").jqGrid('setGridParam', {
					datatype: 'local',
					rowNum: rowNum,
					data: myData, //newData是符合格式要求的重新加载的数据
					page: 1 //哪一页的值
				}).trigger("reloadGrid")
			},
			error: function() {
				alert("查询失败")
			}
		});
	})
})

//保存按钮
$(function(){
	$(".saveOrder").click(function() {
		addData()
	})
})

function addData(){
	var ids = $("#en_jqGrids").jqGrid('getGridParam', 'selarrrow'); //获取选中行id
	var rowData = {};
	for(i = 0; i < ids.length; i++) {
		//选中行的id
		rowData[i] = $('#en_jqGrids').jqGrid('getRowData',ids[i]);
		var jstime = $("#en_jqGrids").getCell(ids[i], "nowTaxUprc");
		rowData[i].cntnTaxUprc=$("#"+ids[i]+"_nowTaxUprc").val()
		delete rowData[i].nowTaxUprc;
	}
	var rowDatas = rowData.toString();
	var data = {
		"reqHead":reqhead,
		"reqBody": rowDatas
	}
	var postData = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/purc/EntrsAgnAdj/addEntrsAgnAdj',
		type: 'post',
		data: postData,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		error: function() {
			alert("获取数据错误");
		}, //错误执行方法
		success: function(data) {
			alert(data.respHead.message)
		}
	})
}



