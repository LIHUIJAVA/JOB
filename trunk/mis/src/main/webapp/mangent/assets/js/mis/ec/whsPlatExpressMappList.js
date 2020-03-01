//刚开始时可点击的按钮
$(function() {
	$(".yes").removeClass("gray")
	$(".no").removeClass("gray")
	$("#findes").removeClass("gray")
	$(".yes").attr("disabled", false)
	$(".no").attr("disabled", false)
	$("#findes").attr("disabled", false)
})


$(function() {
	//页面加载完成之后执行	
	var pageNo = 1;
	var rowNum = 10;
});

$(function() {
	//点击表格图标显示存货列表
	$(document).on('click', '.biao_whsId', function() {
		window.open("../../Components/baseDoc/whsList.html",'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
	$(document).on('click', '.biao_expressId', function() {
		window.open("../../Components/whs/express_cropList.html", 'newwindow','height=700, width=1000, top=200, left=300,location=no, status=no');
	});
})

$(function() {
	var savedata = {
		'reqHead': reqhead,
		'reqBody': {
			'ecId': "",
			'pageNo': 1,
			'pageSize': 500
		},
	};
	var saveData = JSON.stringify(savedata)
	$.ajax({
		type: 'post',
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/ec/ecPlatform/queryList',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			list = data.respBody.list;
			var option_html = '';
			option_html += '<option value="" disabled selected>' + "" + "</option>"
			for(i = 0; i < list.length; i++) {
				option_html += '<option value="' + list[i].ecId + '"' + 'id="ab">' + list[i].ecName + "</option>"
			}
			window.pro = $(".ecId").first().children("option").val()
			$(".ecId").html(option_html);
			$(".ecId").change(function(e) {
				window.val = this.value;
				pro = this.value
				window.localStorage.setItem("pro",pro);
			})
		},
		error: function() {
			console.log(error)
		}
	})
})





var myData = {};
//页面初始化
$(function() {
	var data2 = {
		reqHead,
		"reqBody": {
			"pageNo": 1,
			"pageSize": 500
		}
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		url: url3 + "/mis/ec/whsPlatExpressMapp/selectList",
		async: true,
		data: postD2,
		dataType: 'json',
		contentType: 'application/json',
		success: function(data) {
			var data = data.respBody.list;
			myData=data
			allHeight()
	//初始化表格
			jQuery("#f_jqgrids").jqGrid({
				height: height,
				autoScroll:true,
				data: data,
				datatype: "local", //请求数据返回的类型。可选json,xml,txt
				shrinkToFit:false,
				colNames: ['主键id', '仓库id', '仓库名称', '平台id',
				'平台名称', '快递公司id', '快递公司名称', '最小重量', '最大重量','打印模板编码','打印模板名称'], //jqGrid的列显示名字
				colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
					{
						name: 'id',
						align: "center",
						index: 'invdate',
						editable: false
					},
					{
						name: 'whsId',
						align: "center",
						index: 'id',
						editable: true,
					},
					{
						name: 'whsName',
						align: "center",
						index: 'invdate',
						editable: false,
					},
					{
						name: 'platId',
						align: "center",
						id: 'ecName',
						index: 'id',
						editable: false,
					},
					{
						name: 'platName',
						align: "center",
						index: 'invdate',
						editable: true,
					},
					{
						name: 'expressId',
						align: "center",
						index: 'id',
						editable: true,
					},
					{
						name: 'expressName',
						align: "center",
						index: 'id',
						editable: false,
					},
					{
						name: 'weightMin',
						align: "center",
						index: 'id',
						editable: true,
					},
					{
						name: 'weightMax',
						align: "center",
						index: 'id',
						editable: true,
					},
					{
						name: 'templateId',
						align: "center",
						index: 'id',
						editable: true,
					},
					{
						name: 'templateName',
						align: "center",
						index: 'id',
						editable: false,
					}
				],
				autowidth: true,
				rownumbers: true,
				loadonce: true,
				forceFit: true,
				rowNum: 10,
				scrollrows:true,
				rowList: [10, 20, 30], //可供用户选择一页显示多少条
				sortname: 'id', //初始化的时候排序的字段
				pager : '#f_jqGridPager',//表格页脚的占位符(一般是div)的id
				sortorder: "desc", //排序方式,可选desc,asc
				viewrecords: true,
				multiselect: true, //复选框
				multiboxonly: true,
				caption: "仓库平台快递公司映射", //表格的标题名字	
			});
		}
	})

})

//查询按钮
$(document).on('click', '#findes', function() {
	search4()
})


//条件查询
function search4() {
	var whsId = $("input[name='whsId1']").val();
	var whsName = $("input[name='whsName1']").val();
	var expressId = $("input[name='expressId1']").val();
	var expressName = $("input[name='expressName1']").val();
	var platName = $("select[name='ecId1'] option:selected").text();
	var platId = $("select[name='ecId1']").val();

	var data2 = {
		reqHead,
		"reqBody": {
			"whsId": whsId,
			"whsName": whsName,
			"expressId": expressId,
			"expressName": expressName,
			"platName": platName,
			"platId": platId,
			"pageNo": 1,
			"pageSize": 500

		}

	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		url: url3 + "/mis/ec/whsPlatExpressMapp/selectList",
		async: true,
		data: postD2,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		success: function(data) {
			var rowNum = $("#_input").val()
			var list = data.respBody.list;
			myDate = list;

			$("#f_jqgrids").jqGrid('clearGridData');
			$("#f_jqgrids").jqGrid('setGridParam', {
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

}

$(document).on('click', '.yes', function() {
	//获得行号
	var gr = $("#f_jqgrids").jqGrid('getGridParam', 'selrow');
	if(gr == null) {
		alert("未选择")
	} else  {
		window.localStorage.removeItem("whsEncd")
		//获得行数据
		var rowDatas = $("#f_jqgrids").jqGrid('getRowData', gr);
		window.parent.opener.document.getElementById("ckys_kd").value= rowDatas.expressId;
		window.parent.opener.document.getElementById("ckys_mb").value= rowDatas.templateId;
		window.parent.opener.document.getElementById("expressName").value= rowDatas.expressName;
//		localStorage.setItem("whsEncd",rowDatas.whsEncd)
		window.close()
//		localStorage.setItem("outInEncd", 0)
	}
})

$(document).on('click', '.no', function() {
	localStorage.clear();
	window.parent.opener.document.getElementById("ckys_kd").value= "";
	window.parent.opener.document.getElementById("ckys_mb").value= "";
	window.parent.opener.document.getElementById("expressName").value= "";
	window.close()
})