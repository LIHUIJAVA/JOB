var page = 1;
var rowNum1;
$(function() {
	$(".purchaseTit").append("<div id='mengban' class='zhezhao'></div>");
	$("#mengban").addClass("zhezhao");
})

$(function() {
	// 点击更多给弹筐内重复的赋值
	$(".more").click(function() {
		$("#big_wrap").show();
		$("#box").show();
		var invtyEncd = $("input[name='invtyEncd']").val();
		$("input[name='invtyEncd1']").val(invtyEncd);
		var invtyNm = $("input[name='invtyNm']").val();
		$("input[name='invtyNm1']").val(invtyNm);
		
		$("input[name='whsEncd2']").attr('id','whsEncd');
        $("input[name='whsEncd1']").attr('id','');
        var whsEncd1 = $("input[name='whsEncd1']").val()
        $("input[name='whsEncd2']").val(whsEncd1)

		var formCode = $("select[name='formCode1']").val();
		$("select[name='formCode2']").val(formCode);	
		$("select[name='formCode2']").attr('id', 'formCode'); // 给弹框中重复的加id
		$("select[name='formCode1']").attr('id', '');
		$("select[name='formCode2']").fSelect();
		
		var formSDt1 = $("input[name='formSDt1']").val();
		$("input[name='formSDt3']").val(formSDt1);
		
		var formSDt2 = $("input[name='formSDt2']").val();
		$("input[name='formSDt4']").val(formSDt2);
		
		var bookOkSDt1 = $("input[name='bookOkSDt1']").val();
		$("input[name='bookOkSDt3']").val(bookOkSDt1);
		
		var bookOkSDt2 = $("input[name='bookOkSDt2']").val();
		$("input[name='bookOkSDt4']").val(bookOkSDt2);

		$("input[name='invtyEncd1']").attr('id', 'invtyEncd'); // 给弹框中重复的加id
		$("input[name='invtyEncd']").attr('id', ''); // 给弹框外重复的去除id
		$("input[name='invtyNm1']").attr('id', 'invtyNm'); // 给弹框中重复的加id
		$("input[name='invtyNm']").attr('id', ''); // 给弹框外重复的去除id
	})
	$(".cancel_more").click(function() {
		$("#big_wrap").hide();
		$("#box").hide();
		$("input[name='invtyEncd1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='invtyEncd']").attr('id', 'invtyEncd'); // 给弹框外重复的去除id
		$("input[name='invtyNm1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='invtyNm']").attr('id', 'invtyNm'); // 给弹框外重复的去除id
		
		$("select[name='formCode1']").attr('id', 'formCode'); // 给弹框中重复的加id
		$("select[name='formCode2']").attr('id', '');

		$("#box input").val("");
	})
	$(".sure_more").click(function(){
		$("#big_wrap").hide();
		$("#box").hide();
		
		var invtyEncd = $("input[name='invtyEncd1']").val();
		$("input[name='invtyEncd']").val(invtyEncd);
		var invtyNm = $("input[name='invtyNm1']").val();
		$("input[name='invtyNm']").val(invtyNm);
		
		$("input[name='whsEncd2']").attr('id','whsEncd');
        $("input[name='whsEncd1']").attr('id','');
        var whsEncd = $("input[name='whsEncd2']").val()
        $("input[name='whsEncd1']").val(whsEncd)
		
		var formCode = $("select[name='formCode2']").val()
        $("select[name='formCode1']").val(formCode)
		$("select[name='formCode1']").attr('id', 'formCode'); // 给弹框中重复的加id
		$("select[name='formCode2']").attr('id', '');
		
		var toGdsSnglId = $("input[name='toGdsSnglId1']").val();
		$("input[name='toGdsSnglId']").val(toGdsSnglId);
		
		var bookOkSDt1 = $("input[name='bookOkSDt3']").val();
		$("input[name='bookOkSDt1']").val(bookOkSDt1);
		
		var bookOkSDt2 = $("input[name='bookOkSDt4']").val();
		$("input[name='bookOkSDt2']").val(bookOkSDt2);
		
		var formSDt1 = $("input[name='formSDt3']").val();
		$("input[name='formSDt1']").val(formSDt1);
		
		var formSDt2 = $("input[name='formSDt4']").val();
		$("input[name='formSDt']").val(formSDt2);
		
		$("input[name='invtyEncd1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='invtyEncd']").attr('id', 'invtyEncd'); // 给弹框外重复的去除id
		$("input[name='invtyNm1']").attr('id', ''); // 给弹框中重复的加id
		$("input[name='invtyNm']").attr('id', 'invtyNm'); // 给弹框外重复的去除id

		loadLocalData()
//		$("#box input").val("");
	})
})

$(function() {
	jQuery("#order_jqGrids").jqGrid({
		url: '../../assets/js/json/order.json', //组件创建完成之后请求数据的url
		height: '100%',
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['单据日期', '记账日期','单据类型id',  '单据类型', '单据号', '仓库名称', '收发类别', '记账人', '业务单号', 
		'业务类型', '供应商', '客户', '备注','存货编码', '存货名称', '存货代码', '规格型号'], //jqGrid的列显示名字
		colModel: [{
				name: "formDt",
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: "bookOkDt",
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: "formTypEncd",
				align: "center",
				editable: true,
				sortable: false,
				hidden:true
			},
			{
				name: "formTypName",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: "formNum",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'whsNm',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'recvSendCateNm',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'bookOkAcc',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'bizTypId', //业务单号
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'bizTypNm',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'provrNm',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'custNm',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'memo',
				align: "center",
				editable: true,
				sortable: false
			},
			
			{
				name: "invtyEncd",
				align: "center",
				sortable: false,
			},
			{
				name: 'invtyNm',
				align: "center",
				editable: false,
				sortable: false,
			},
			{
				name: "invtyCd",
				align: "center",
				sortable: false,
			},
			{
				name: 'spcModel',
				align: "center",
				editable: false,
				sortable: false,
			}
		],
		height: 320,
		autowidth: true,
		viewrecords: true,
		autoScroll: true,
		shrinkToFit: false,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		rowNum: 100,
		rowList: [100, 200, 300], //可供用户选择一页显示多少条
		pager: '#order_jqGridPager', //表格页脚的占位符(一般是div)的id
		multiboxonly: true,
		multiselect: true, //复选框
		caption: "选单列表", //表格的标题名字
		onPaging: function(pageBtn) { //翻页实现 
			var records = $("#order_jqGrids").getGridParam('records'); //获取返回的记录数
			page = $("#order_jqGrids").getGridParam('page'); //获取返回的当前页
			var rowNum2 = $("#order_jqGrids").getGridParam('rowNum'); //获取显示配置记录数量

			rowNum1 = parseInt(rowNum2)
			var total = Math.ceil(records / rowNum1); //$("#jqGrid").getGridParam('total');//获取总页数
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
			loadLocalData()
		},
		ondblClickRow: function() {
			order();
		},
	})
})

function order() {
	//获得行号
	var gr = $("#order_jqGrids").jqGrid('getGridParam', 'selrow');
	//获得行数据
	var rowDatas = $("#order_jqGrids").jqGrid('getRowData', gr);
	localStorage.setItem("formTypEncd", rowDatas.formTypEncd);
	localStorage.setItem("formNum", rowDatas.formNum);
	window.open("../../Components/account/detilForm.html?1");

}

function loadLocalData() {
	var rowNum2 = $("#gbox_order_jqGrids select").val(); //获取显示配置记录数量
	rowNum1 = parseInt(rowNum2)
	var myData = {};
	var whsEncd = $("#whsEncd").val();
	var formType = $("#formCode").val();
	if(formType){
		var formType1 = formType.join(",");
	}
	var invtyEncds = $("#invtyEncd").val();
	var bookOkSDt = $("#bookOkSDt").val();
	var bookOkEDt = $("#bookOkEDt").val();
	var formSDt = $("#formSDt").val();
	var formEDt = $("#formEDt").val();
	var custId = $("#custId").val();
	var provrId = $("#provrId").val();
	var recvSendCateId = $("#recvSendCateId").val()
	if(formType==""||formType==null) {
		alert("请选择单据类型")
	} else {
		var data2 = {
			"reqHead": reqhead,
			"reqBody": {
				"isSelect":"1",
				"whsEncd": whsEncd,
				"invtyEncds": invtyEncds,
				"formType": formType1,
				"bookOkSDt": bookOkSDt,
				"bookOkEDt": bookOkEDt,
				"formSDt": formSDt,
				"formEDt": formEDt,
				"custId":custId,
				"provrId":provrId,
				"recvSendCateId":recvSendCateId,
				"pageSize": rowNum1,
				"pageNo": page
			}
		};
		var postD2 = JSON.stringify(data2);
		$.ajax({
			type: "post",
			url: url + "/mis/account/form/novoucher/list",
			async: true,
			data: postD2,
			dataType: 'json',
			contentType: 'application/json; charset=utf-8',
			beforeSend: function() {
				$(".zhezhao").css("display", "block");
				$(" #loader").css("display", "block");
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
				$("#order_jqGrids").jqGrid('clearGridData');
				var mydata = {}
				mydata.rows = arr;
				mydata.page = page;
				mydata.records = data.respBody.count;
				mydata.total = data.respBody.pages;
				$("#order_jqGrids").jqGrid("setGridParam", {
					data: mydata.rows,
					localReader: {
						root: function(object) {
							return mydata.rows
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
}

//条件查询
$(function() {
	$('#find').click(function() {
		loadLocalData()
	})
})