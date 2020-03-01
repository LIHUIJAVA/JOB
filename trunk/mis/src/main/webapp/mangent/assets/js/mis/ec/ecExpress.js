var count;
var pages;
var page = 1;
var rowNum;

$(function() {
	
		$("#mengban").hide()
		$(".down").hide()
})


$(function() {
	pageInit();
});

//var height = $(window).height() * 0.6;

function pageInit() {
	//加载动画html 添加到初始的时候
	$(".purchaseTit").append("<div id='mengban1' class='zhezhao'></div>");
	$(".purchaseTit").append("<div id='loader'><div>lo</div><div>ad</div><div>ing </div></div > ");
	$("#mengban1").addClass("zhezhao");
	allHeight()
	jQuery("#jqGrids").jqGrid({
		url: '../../assets/js/json/order.json', //组件创建完成之后请求数据的url

		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		colNames: ['主键id', '平台id', '平台名称', '承运商编码', '承运商id',
		'承运商名称', '承运商类型', '网点编码',  '网点名称','单量余额','省id', '省','市id','市',
		'区id','区','镇id','镇','详细地址'], //jqGrid的列显示名字
		colModel: [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			{
				name: 'id',
				align: "center",
				editable: true,
				hidden:true,
				sortable: false
			},
			{
				name: 'platId',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true,

			},
			{
				name: 'platName',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'providerCode',
				align: "center",
				editable: true,
				sortable: false,
			},
			{
				name: 'providerId',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true,
			},
			{
				name: 'providerName',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'providerType',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true,
			},
			{
				name: 'branchCode',
				align: "center",
				editable: true,
				sortable: false,

			},
			{
				name: 'branchName',
				align: "center",
				editable: true,
				sortable: false,

			},
			{
				name: 'amount',
				align: "center",
				editable: true,
//				sortable: false
				sorttype:'integer',
			},
			{
				name: 'provinceId',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true,
			},
			{
				name: 'provinceName',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'cityId',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true,
			},
			{
				name: 'cityName',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'countryId',
				align: "center",
				editable: true,
//				sortable: false,
				hidden:true,
			},
			{
				name: 'countryName',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'countrysideId',
				align: "center",
				editable: true,
				sortable: false,
				hidden:true,
			},
			{
				name: 'countrysideName',
				align: "center",
				editable: true,
				sortable: false
			},
			{
				name: 'address',
				align: "center",
				editable: true,
				sortable: false
			}
		],
		autowidth: true,
		height:height,
		sortable:true,
		viewrecords: true,
		rownumbers: true,
		autoScroll:true,
		shrinkToFit:false,
		loadonce: true,
		forceFit: true,
		rowNum: 500, //一页显示多少条
		rowList: [500, 1000, 3000, 5000], //可供用户选择一页显示多少条	
		pager: '#jqgridPager', //表格页脚的占位符(一般是div)的id
		caption: "电子面单账户", //表格的标题名字
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
			search()
		},
	})
}

$(function() {
	$('#find').click(function() {
		search()
	})
})

//条件查询
function search() {

	var ecId = $("select[name='ecId'] option:selected").val();
	
	var rowNum1 = $("td[dir='ltr'] select").val() //获取显示配置记录数量 
	rowNum = parseInt(rowNum1)
	var data2 = {
		"reqHead": reqhead,
		"reqBody": {
			"ecId": ecId,
			"pageNo": page,
			"pageSize": rowNum
		}
	};
	var postD2 = JSON.stringify(data2);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/ec/ecExpress/queryList',
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
			console.log(error)
		}
	});

}


//获取下拉框
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
			option_html += '<option value="" selected>' + "请选择" + "</option>"
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


$(function() {
	$('.download').click(function() {
		$(".down").css("opacity", 1)
		$(".down").show()
		$("#mengban").show()
/*------点击下载按钮加载动态下拉电商平台-------*/
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
				option_html += '<option value="0" disabled selected>' + "请选择" + "</option>"
				for(i = 0; i < list.length; i++) {
					option_html += '<option value="' + list[i].ecId + '"' + 'id="ab">' + list[i].ecName + "</option>"
//					var proCriteria = list[i].proConditionEncd;
				}
				window.pro = $(".ecId1").first().children("option").val()
				$(".ecId1").html(option_html);
				$(".ecId1").change(function(e) {
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

	$(".true").click(function() {
		var ecId = $("select[name='ecId1'] option:selected").val();

		var download = {
			"reqHead": reqhead,
			"reqBody": {
				'platId': ecId
			}
		};
		var downloadData = JSON.stringify(download)
		$.ajax({
			type: "post",
			url: url3 + "/mis/ec/ecExpress/download",
			async: true,
			data: downloadData,
			dataType: 'json',
			contentType: 'application/json',
			//开始加载动画  添加到ajax里面
			beforeSend: function() {
				$(".zhezhao").css("display", "block");
				$("#loader").css("display", "block");
	
			},
			success: function(msgAdd) {
				alert(msgAdd.respHead.message)
				//				window.location.reload();
				$("#mengban").hide()
				$(".down").css("opacity", 0)
				$(".down").hide()
			},
			//结束加载动画
			complete: function() {
				$(".zhezhao").css("display", "none");
				$("#loader").css("display", "none");
			},
			error: function() {
				console.log("下载失败")

			}
		});
	})


	$(".falses").click(function() {
		$("#mengban").hide()
		$(".down").css("opacity", 0)
		$(".down").hide()

	})
})


//导出
$(document).on('click', '.exportExcel', function() {
	var ecId = $("select[name='ecId'] option:selected").val();
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"ecId": ecId,
			"pageNo": 1,
			"pageSize": 500
		}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/ec/ecExpress/queryList',
		type: 'post',
		data: Data,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		success: function(data) {
			var obj = {}
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
    var str = '序号,平台编号,承运商编码,承运商ID,承运商名称,承运商类型,网点编码,网点名称,单量余额,省,省ID,市,市ID,区,区ID,镇,镇ID,详细地址,平台名称\n';

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
    link.download =  "电子面单签约公司.csv";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}