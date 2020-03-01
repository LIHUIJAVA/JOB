//结算成本处理
//表格初始化
$(function() {
//	allHeight();
	var rowNum = $("#_input").val()
	$("#jqGrids").jqGrid({
		datatype: "json",
		url: '../../assets/js/json/order.json',
		colNames: ['结算单号', '仓库编码', '仓库名称', '入库单号'
		,'入库日期', '存货编码', '存货名称', '计量单位'
		,'数量', '暂估单价', '暂估金额', '结算数量'
		,'结算单价', '结算金额', '收发类别名称', '材料费'
		,'加工费', '单据类型', '业务类型', '供应商编码'
		,'供应商名称'],
		colModel: [
			{
				name: 'ordrNum',
				editable: true,
				align: 'center',
//				hidden: true
			},
			{
				name: 'momEncd',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'momNm',
				editable: true,
				align: 'center'

			},
			{
				name: 'momSpc',
				editable: true,
				align: 'center'

			},
			{
				name: 'ordrNum',
				editable: true,
				align: 'center',
//				hidden: true
			},
			{
				name: 'momEncd',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'momNm',
				editable: true,
				align: 'center'

			},
			{
				name: 'momSpc',
				editable: true,
				align: 'center'

			},
			{
				name: 'ordrNum',
				editable: true,
				align: 'center',
//				hidden: true
			},
			{
				name: 'momEncd',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'momNm',
				editable: true,
				align: 'center'

			},
			{
				name: 'momSpc',
				editable: true,
				align: 'center'

			},
			{
				name: 'ordrNum',
				editable: true,
				align: 'center',
//				hidden: true
			},
			{
				name: 'momEncd',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'momNm',
				editable: true,
				align: 'center'

			},
			{
				name: 'momSpc',
				editable: true,
				align: 'center'

			},
			{
				name: 'ordrNum',
				editable: true,
				align: 'center',
//				hidden: true
			},
			{
				name: 'momEncd',
				editable: true,
				align: 'center',
				sortable: false,
			},
			{
				name: 'momNm',
				editable: true,
				align: 'center'

			},
			{
				name: 'momSpc',
				editable: true,
				align: 'center'

			},
			{
				name: 'momSpc',
				editable: true,
				align: 'center'

			}
		],
		autowidth: true,
		rownumbers: true,
		loadonce: false,
		height:350,
		//		height:'100%',
		forceFit: true,
		pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		viewrecords: true,
		multiselect: true, //复选框
//		multiboxonly: true,
		autoScroll:true,
		shrinkToFit:false,
		caption: "结算成本处理", //表格的标题名字
		//双击弹出产品结构
//		ondblClickRow: function(rowid) {
//			order(rowid);
//		},
	})
})

//导入--url不对
//$(function () {
//  $(".importExcel").click(function () {
//  	var files = $("#FileUpload").val()
//  	var fileObj = document.getElementById("FileUpload").files[0];
//      var formFile = new FormData();
//     	formFile.append("action", "UploadVMKImagePath");  
//     	formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
//      var data = formFile;
//      if(files != "") {
//      	$.ajax({
//	            type: 'post',
//	            url: url + "/mis/purc/CustDoc/uploadCustDocFile",
//	            data:data,
//	          	dataType: "json",
//	           	cache: false,//上传文件无需缓存
//	           	processData: false,//用于对data参数进行序列化处理 这里必须false
//	           	contentType: false, //必须
//	           	success: function(data) {
//	           		alert(data.respHead.message)
//	           		window.location.reload()
//	           	}
//	        });
//      } else {
//      	alert("请选择文件")
//      }   
//  });
//});