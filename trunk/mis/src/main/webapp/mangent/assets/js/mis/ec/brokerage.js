$(function() {
	//页面加载完成之后执行	
	var pageNo = 1;
	var rowNum = 10;
	pageInit();
	//点击右边条数修改显示行数
	$(".ui-pg-selbox.ui-widget-content.ui-corner-all").click(function() {
		pageNo = $("#jqgrids").jqGrid("getGridParam", "page");
		rowNum = $("select[class='ui-pg-selbox ui-widget-content ui-corner-all']")[0].value >> 0;
		var data3 = {
			reqHead,
			"reqBody": {
				"pageSize": rowNum,
				"pageNo": pageNo
			}
		};
		var postD3 = JSON.stringify(data3);
		jQuery("#jqgrids").jqGrid({
			url: url3 + "/mis/ec/brokerage/queryList", //组件创建完成之后请求数据的url
			mtype: "post",
			datatype: "json", //请求数据返回的类型。可选json,xml,txt
			postData: postD3,
			ajaxGridOptions: {
				contentType: 'application/json; charset=utf-8'
			},
			colNames: ['佣金扣点编码', '佣金扣点名称', '备注'], //jqGrid的列显示名字
			colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
				{
					name: 'brokId',
					align: "center",
					index: 'invdate',
					editable: true,
				},
				{
					name: 'brokName',
					align: "center",
					index: 'id',
					editable: true,
				},
				{
					name: 'memo',
					align: "center",
					index: 'invdate',
					editable: true,
				},

			],
			rowList: [10, 20, 30], //可供用户选择一页显示多少条
			autowidth: true,
			pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
			sortname: 'brokId', //初始化的时候排序的字段
			sortorder: "desc", //排序方式,可选desc,asc
			viewrecords: true,
			rowNum: rowNum, //一页显示多少条
			pageNo: pageNo,
			jsonReader: {
				root: "respBody.list", // json中代表实际模型数据的入口
				records: "respBody.count", // json中代表数据行总数的数据		            
				total: "respBody.pages", // json中代表页码总数的数据
				repeatitems: true,
			},
			onPaging: function(pgButton) {
				pageNo = $("#jqgrids").jqGrid("getGridParam", "page");
				rowNum = $("select[class='ui-pg-selbox ui-widget-content ui-corner-all']")[0].value >> 0;
				if(pgButton === 'prev') {
					pageNo -= 1;
				} else if(pgButton === 'next') {
					pageNo += 1;

				} else if(pgButton === 'records') {
					pageNo = 1;
				}
			}
		});
	})

	//条件查询
	$('#find').click(function() {
		var brokId = $('input[name="brokId1"]').val();
		//		var brokName = $('input[class="brokName"]').val();
		//		var memo = $("input[class='memo']").val();

		var data2 = {
			reqHead,
			"reqBody": {
				"brokId": brokId,
				"pageSize": rowNum,
				"pageNo": pageNo,
			}
		};
		var postD2 = JSON.stringify(data2);
		$.ajax({
			type: "post",
			url: url3 + "/mis/ec/brokerage/queryList",
			async: true,
			data: postD2,
			dataType: 'json',
			contentType: 'application/json; charset=utf-8',
			success: function(data) {
				var rowNum = $("#_input").val()
				var list = data.respBody.list;
				myDate = list;

				$("#jqgrids").jqGrid('clearGridData');
				$("#jqgrids").jqGrid('setGridParam', {
					rowNum: rowNum,
					datatype: 'local',
					data: myDate, //newData是符合格式要求的重新加载的数据
					page: 1 //哪一页的值
				}).trigger("reloadGrid")
			},
			error: function() {
				console.log(error)
			}
		});
	})
});

//增行   保存
$(function() {
	$(".addOrder").click(function() {
		var newrowid;
		var selectedId = $("#jqgrids").jqGrid("getGridParam", "selrow");
		var ids = jQuery("#jqgrids").jqGrid('getDataIDs');

		//获得当前最大行号（数据编码）
		var rowid = Math.max.apply(Math, ids);
		//获得新添加行的行号（数据编码）
		newrowid = rowid + 1;
		var dataRow = {
			brokId: "",
			brokName: '',
			memo: ''

		};
		$("#jqgrids").setColProp('brokId', {
			editable: true
		}); //设置editable属性由true改为false

		//将新添加的行插入到第一列
		$("#jqgrids").jqGrid("addRowData", newrowid, dataRow, "first");
		//设置grid单元格可编辑
		$('#jqgrids').jqGrid('editRow', newrowid, false);

		$(document).keyup(function(event) {
			if(event.keyCode == 13) {
				$(".saveOrder").trigger("click");
			}
		});
	})
	$(".saveOrder").click(function() {
		var brokId = $("input[name='brokId']").val();
		var brokName = $("input[name='brokName']").val();
		var memo = $("input[name='memo']").val();

		var save = {
			reqHead,
			"reqBody": {
				"brokId": brokId,
				"brokName": brokName,
				"memo": memo,

			}
		}
		var saveJson = JSON.stringify(save);
		$.ajax({
			type: "post",
			url: url3 + "/mis/ec/brokerage/add",
			async: true,
			data: saveJson,
			dataType: 'json',
			contentType: 'application/json',
			success: function(msgAdd) {
				alert(msgAdd.respHead.message)
				window.location.reload();
				$("#find").trigger('click');
				$('#jqgrids').css("visibility", "true");
			},
			error: function(err) {
				console.log("失败")
			}
		});
	})
})

//删除行
$(function() {
	$(".delOrder").click(function() {
		var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
		var rowDatas = $("#jqgrids").jqGrid('getRowData', gr); //获取行数据    	
		var brokId = $('input[name="brokId"]').val();
		var brokName = $('input[name="brokName"]').val();
		var memo = $('input[name="memo"]').val();

		var deleteAjax = {
			reqHead,
			"reqBody": {
				"brokId": rowDatas.brokId,
				"brokName": rowDatas.brokName,
				"memo": rowDatas.memo,
			}
		};
		var deleteData = JSON.stringify(deleteAjax)
		if(gr == null) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url3 + "/mis/ec/brokerage/delete",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(remover) {
					alert("删除成功");
					window.location.reload();
					$("#find").trigger('click')
					$('#jqgrids').css("visibility", "true");
				},
				error: function() {
					console.log("删除失败")

				}
			});

		}
	})
})

function pageInit() {
	allHeight()
	pageNo = 1;
	rowNum = 10;

	//	//创建jqGrid组件	
	var data3 = {
		reqHead,
		"reqBody": {
			"brokId": "",
			"brokName": "",
			"memo": "", //当前页数
			"pageNo": pageNo,
			"pageSize": rowNum
		}
	};
	var postD3 = JSON.stringify(data3);
	//初始化表格
	jQuery("#jqgrids").jqGrid({
		url: url3 + "/mis/ec/brokerage/queryList",
		mtype: "post",
		//				height:330,
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		postData: postD3,
		ajaxGridOptions: {
			contentType: 'application/json; charset=utf-8'
		},
		colNames: ['佣金扣点编码', '佣金扣点名称', '备注'], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'brokId',
				align: "center",
				index: 'invdate',
				editable: false,
			},
			{
				name: 'brokName',
				align: "center",
				index: 'id',
				editable: true,
			},
			{
				name: 'memo',
				align: "center",
				index: 'invdate',
				editable: true,
			},

		],
		rowNum: 10, //一页显示多少条
		rowList: [10, 20, 30], //可供用户选择一页显示多少条			
		//				autowidth:true,
		rownumbers: true,
		autowidth: true,
		height: height,
		rownumWidth: 15, //序列号列宽度
		autoScroll: true,
		shrinkToFit: false,
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'brokId', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		jsonReader: {
			records: "respBody.count", // json中代表数据行总数的数据	
			root: "respBody.list", // json中代表实际模型数据的入口
			total: "respBody.pages", // json中代表页码总数的数据
			repeatitems: true

		},

		caption: "佣金扣点列表查询", //表格的标题名字	
		ondblClickRow: function() {
			$('.saveOrder')[0].disabled = true;
			var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow'); //获取行id
			var rowDatas = $("#jqgrids").jqGrid('getRowData', gr); //获取行数据
			jQuery('#jqgrids').editRow(gr, true);
			//点击更新按钮
			$(".update").click(function() {
				var brokId = rowDatas.brokId;
				var brokName = $("input[name='brokName']").val();
				var memo = $("input[name='memo']").val();

				var edit = {
					reqHead,
					"reqBody": {
						"brokId": brokId,
						"brokName": brokName,
						"memo": memo,
					}
				}
				editJson = JSON.stringify(edit);

				$.ajax({
					type: "post",
					url: url3 + "/mis/ec/brokerage/edit",
					async: true,
					data: editJson,
					dataType: 'json',
					contentType: 'application/json',
					colNames: ['佣金扣点编码', '佣金扣点名称', '备注'], //jqGrid的列显示名字
					colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
						{
							name: 'brokId',
							align: "center",
							index: 'invdate',
							editable: true,
						},
						{
							name: 'brokName',
							align: "center",
							index: 'id',
							editable: true,
						},
						{
							name: 'memo',
							align: "center",
							index: 'invdate',
							editable: true,
						},

					],

					success: function(editMsg) {
						alert(editMsg.respHead.message);
						window.location.reload();
					},
					error: function() {
						console.log("更新失败")
					}
				});
			})

		}

	});
	jQuery("#jqgrids").navGrid('#jqGridPager', {
		edit: true,
		add: true,
		del: true
	}, {
		id: 'editOrder'
	}, {
		id: 'addOrder'
	}, {
		id: 'delOrder'
	});
	$(function() {
		$(window).resize(function() {
			$("#jqgrids").setGridWidth($(window).width());
		});
	});
}