
$(function(){
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"pageNo": 1,
			"pageSize": 500
		}
	};
	var postData = JSON.stringify(data);
	jQuery("#jqgrids").jqGrid({
		url: url + '/mis/whs/invty_tab/outIntoWhsTyp', //组件创建完成之后请求数据的url
		mtype: 'post',
		height: 500,
		ajaxGridOptions: {
			contentType: 'application/json; charset=utf-8'
		},
		postData: postData,
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['编码', '出入库类型', '单号', '来源单据号'], //jqGrid的列显示名字
		colModel: [{
				name: "id",
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: "nm",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: "fn",
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: "sfn",
				align: "center",
				editable: true,
				sortable: false
			}
		],
		jsonReader: {
			root: "respBody.list",
			repeatitems: true,
			records: "respBody.count", // json中代表数据行总数的数据		            
			total: "respBody.pages", // json中代表页码总数的数据
		},
		autowidth: true,
		viewrecords: true,
		autoScroll: true,
		shrinkToFit: false,
		rownumbers: true,
		loadonce: true,
		forceFit: true,
		pager: '#jqGridPager',
		rowList: [100, 300, 500],
		multiselect: true, //复选框
		caption: "出入库类型", //表格的标题名字
	})
})



//出入库类型
$(function() {
		//确定
	$(".true").click(function() {
		//获得行数据 
		var ids = $("#jqgrids").jqGrid('getGridParam', 'selarrrow');
		if(ids.length == 0) {
			alert("请选择单据")
		} else  {
			var nm = [];
			var encd = [];
			for(i = 0; i < ids.length; i++) {
				var rowDatas = $("#jqgrids").jqGrid('getRowData', ids[i]); //获取行数据
				encd[i] = rowDatas.id;
				nm[i] = rowDatas.nm;
			}
			var nms = nm.join(',')
			var encds = encd.join(',')
			window.parent.opener.document.getElementById("formCode").value = encds;
			window.parent.opener.document.getElementById("formCode1").value = nms;
			window.close();
		}
	})
	//	取消
	$(".cancel").click(function() {
		window.parent.opener.document.getElementById("formCode1").value = '';
		window.parent.opener.document.getElementById("formCode1").value = '';
		window.close();
	})
})