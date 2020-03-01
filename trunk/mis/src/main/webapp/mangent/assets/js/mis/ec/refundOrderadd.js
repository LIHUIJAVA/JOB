$(function(){
	//页面加载完成之后执行	
	var pageNo=1;
	var rowNum=10;
	pageInit();	
//点击右边条数修改显示行数
$(".ui-pg-selbox.ui-widget-content.ui-corner-all").click(function(){
		pageNo = $("#jqgrids").jqGrid("getGridParam","page"); 
		rowNum = $("select[class='ui-pg-selbox ui-widget-content ui-corner-all']")[0].value >> 0;
	var data3 = {
		reqHead,
		"reqBody":{
			"pageSize":rowNum,
			"pageNo":pageNo
		}
	};
	var postD3 = JSON.stringify(data3);
		jQuery("#jqgrids").jqGrid({			
			url : url3+"/mis/ec/refundOrder/queryList",//组件创建完成之后请求数据的url
			mtype:"post",
			datatype : "json",//请求数据返回的类型。可选json,xml,txt
			postData:postD3,
			ajaxGridOptions: { contentType: 'application/json; charset=utf-8' },			   	            
			rowList : [10,20,30],//可供用户选择一页显示多少条
			autowidth:true,
			pager : '#jqGridPager',//表格页脚的占位符(一般是div)的id
			sortname : 'orderId',//初始化的时候排序的字段
			sortorder : "desc",//排序方式,可选desc,asc
			viewrecords : true,
			rowNum : rowNum,//一页显示多少条
			pageNo:pageNo,
			jsonReader: {  
				root: "respBody.list",// json中代表实际模型数据的入口
				records: "respBody.count",// json中代表数据行总数的数据		            
	            total: "respBody.pages",  // json中代表页码总数的数据
	            repeatitems: true,     		
			},			
		onPaging: function(pgButton) {
			pageNo = $("#jqgrids").jqGrid("getGridParam","page"); 
			rowNum = $("select[class='ui-pg-selbox ui-widget-content ui-corner-all']")[0].value >> 0;
			if (pgButton === 'prev') {
				pageNo -= 1;
			} else if (pgButton === 'next') {
				pageNo += 1;
						
			} else if (pgButton === 'records') {
				pageNo = 1;
			}			
		}	
	});	
})

//条件查询
$('#find').click(function(){
		var orderId =$('input[name="orderId1"]').val();
		var storeId = $('input[class="storeId1"]').val();
		var storeName = $("input[class='storeName1']").val();
		var isAudit =$('input[name="isAudit1"]').val();
		var buyerId =$('input[name="buyerId1"]').val();
		var recName =$('input[name="recName1"]').val();
		var recMobile =$('input[name="recMobile1"]').val();
		var ecOrderId =$('input[name="ecOrderId1"]').val();
		var isInvoice =$('input[name="isInvoice1"]').val();
		var onteFlag =$('input[name="onteFlag1"]').val();
		var isClose =$('input[name="isClose1"]').val();
		var isShip =$('input[name="isShip1"]').val();
		var orderStatus =$('input[name="orderStatus1"]').val();
		var returnStatus =$('input[name="returnStatus1"]').val();
		var hasGift =$('input[name="hasGift1"]').val();
		var startDate =$('input[name="startDate1"]').val();
		var endDate =$('input[name="endDate1"]').val();
		
		
		var data2 = {			
			reqHead,
			"reqBody":{
				"orderId":orderId,			
				"storeId":storeId,
				"storeName":storeName,	
				"isAudit":isAudit,
				"buyerId":buyerId,
				"recName":recName,
				"recMobile":recMobile,
				"ecOrderId":ecOrderId,
				"isInvoice":isInvoice,
				"onteFlag":onteFlag,
				"isClose":isClose,
				"isShip":isShip,
				"orderStatus":orderStatus,
				"returnStatus":returnStatus,
				"hasGift":hasGift,
				"startDate":startDate,
				"endDate":endDate,
				"pageNo":pageNo,
				"pageSize":rowNum
			}			
		};
	var postD2 = JSON.stringify(data2);
		$('#jqgrids').jqGrid('setGridParam',{
			url:url3+"/mis/ec/platOrder/queryList",
			mtype:"post",
			datatype : "json",//请求数据返回的类型。可选json,xml,txt
			postData:postD2,
			rowNum:rowNum,
			ajaxGridOptions: { contentType: 'application/json; charset=utf-8' }
		}).trigger('reloadGrid')	
	})	  


});


//增行   保存
$(function(){
	$(".addOrder").click(function(){
		
			var newrowid ;
	        var selectedId = $("#jqgrids").jqGrid("getGridParam", "selrow");
            var ids = jQuery("#jqgrids").jqGrid('getDataIDs');

	        //获得当前最大行号（数据编码）
	        var rowid = Math.max.apply(Math,ids);
	 		//获得新添加行的行号（数据编码）
	        newrowid = rowid+1;
			var dataRow = { 
			    	orderId:"",
				    
			};   
	    //将新添加的行插入到第一列
	    $("#jqgrids").jqGrid("addRowData", newrowid, dataRow, "first");
	    //设置grid单元格可编辑
	    $('#jqgrids').jqGrid('editRow', newrowid, false);	 
		$(document).keyup(function(event){
			  if(event.keyCode ==13){
			    $(".saveOrder").trigger("click");
			    }
		});
	})
})
function SaveNewData() {
	var orderId = $('input[name="orderId1"]').val();
	var storeId = $('input[name="storeId1"]').val();
	var storeName = $('input[name="storeName1"]').val();
	var payTime = $('input[name="payTime1"]').val();
	var waif = $('input[name="waif1"]').val();
	var isAudit = $('input[name="isAudit1"]').val();
	var auditHNumber = $('input[name="auditHNumber1"]').val();
	var goodNum = $('input[name="goodNum1"]').val();
	var goodMoney = $('input[name="goodMoney1"]').val();
	var payMoney = $('input[name="payMoney1"]').val();
	var buyerNote = $('input[name="buyerNote1"]').val();
	var sellerNote = $('input[name="sellerNote1"]').val();
	var recAddress = $('input[name="recAddress1"]').val();
	var buyerId = $('input[name="buyerId1"]').val();
	var recName = $('input[name="recName1"]').val();
	var recMobile = $('input[name="recMobile1"]').val();
	var ecOrderId = $('input[name="ecOrderId1"]').val();
	var isInvoice = $('input[name="isInvoice1"]').val();
	var invoiceTiel = $('input[name="invoiceTiel1"]').val();
	var onteFlag = $('input[name="onteFlag1"]').val();
	var isClose = $('input[name="isClose1"]').val();
	var isShip = $('input[name="isShip1"]').val();
	var adjustMoney = $('input[name="adjustMoney1"]').val();
	var discountMoney = $('input[name="discountMoney1"]').val();
	var adjustStatus = $('input[name="adjustStatus1"]').val();
	var tradeDt = $('input[name="tradeDt1"]').val();
	var bizTypId = $('input[name="bizTypId1"]').val();
	var sellTypId = $('input[name="sellTypId1"]').val();
	var recvSendCateId = $('input[name="recvSendCateId1"]').val();
	var orderStatus = $('input[name="orderStatus1"]').val();
	var returnStatus = $('input[name="returnStatus1"]').val();
	var hasGift = $('input[name="hasGift1"]').val();
	var memo = $('input[name="memo1"]').val();
	
	var goodPrice = $('input[name="goodPrice"]').val();				
	var goodId = $('input[name="goodId"]').val();
	var goodNum = $('input[name="goodNum"]').val();
	var goodMoney = $('input[name="goodMoney"]').val();
	var payMoney = $('input[name="payMoney"]').val();
	var goodSku = $('input[name="goodSku"]').val();
	var orderId = $('input[name="orderId"]').val();
	var batchNo = $('input[name="batchNo"]').val();
	var expressCom = $('input[name="expressCom"]').val();
	var deliverWhs = $('input[name="deliverWhs"]').val();
	var proActId = $('input[name="proActId"]').val();
	var discountMoney = $('input[name="discountMoney"]').val();
	
	
	var adjustMoney = $('input[name="adjustMoney"]').val();
	var payPrice = $('input[name="payPrice"]').val();
	var memo = $('input[name="memo"]').val();
	
	var save = {
		reqHead,
		"reqBody":{
			"orderId":orderId,			
			"storeId":storeId,
			"storeName":storeName,
			"payTime":payTime,
			"waif":waif,
			"isAudit":isAudit,
			"auditHint":auditHNumber,
			"goodNum":goodNum,
			"goodMoney":goodMoney,
			"payMoney":payMoney,
			"buyerNote":buyerNote,
			"sellerNote":sellerNote,
			"recAddress":recAddress,								
			"buyerId":buyerId,
			"recName":recName,
			"recMobile":recMobile,
			"ecOrderId":ecOrderId,
			"isInvoice":isInvoice,
			"invoiceTitle":invoiceTiel,
			"noteFlag":onteFlag,
			"isClose":isClose,
			"isShip":isShip,
			"adjustMoney":adjustMoney,
			"discountMoney":discountMoney,
			
			"adjustStatus":adjustStatus,
			"tradeDt":tradeDt,
			"bizTypId":bizTypId,
			"sellTypId":sellTypId,
			"recvSendCateId":recvSendCateId,
			
			"orderStatus":orderStatus,
			"returnStatus":returnStatus,
			"hasGift":hasGift,
			"memo":memo,
			"list":[{
				"goodPrice":goodPrice,
				"payPrice":payPrice,
				"goodId":goodId,
				"goodNum":goodNum,
				"goodMoney":goodMoney,
				"payMoney":payMoney,
				"goodSku":goodSku,
				"orderId":orderId,
				"batchNo":batchNo,
				"expressCom":expressCom,
				"deliverWhs":deliverWhs,
				"proActId":proActId,
				"discountMoney":discountMoney,
				"adjustMoney":adjustMoney,
				"memo":memo
			}]								
		}
	}
	var saveJson = JSON.stringify(save);
	$.ajax({
		type:"post",
		url:url3+"/mis/ec/platOrder/add",
		async:true,
		data:saveJson,
		dataType:'json',
		contentType: 'application/json',
		success:function(msgAdd){
			alert(msgAdd.respHead.message)
			window.location.reload();
			$("#searchAll").trigger('click');
			$('#jqgrids').css("visibility", "true");
		},
		error:function(err){
			console.log("失败")
		}
	});
}
var isclick = true;
$(function() {
	$(".saveOrder").click(function() {
		if(isclick) {
			isclick = false;
			SaveNewData();
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	})
})
function pageInit(){	
	pageNo=1;
	rowNum=10;	
	let name = JSON.parse(localStorage.getItem("name"));

	//创建jqGrid组件	
	var data3 = {
		reqHead,
		"reqBody":{
			"pageNo":pageNo,
			"pageSize":rowNum

		}
	};
	var postD3 = JSON.stringify(data3);	
	 
//初始化表格

	jQuery("#jqgrids").jqGrid({
				url :url3+"/mis/ec/platOrder/queryList",//组件创建完成之后请求数据的url
				mtype:"post",				
				height:380,
				loadComplete: function (data) {
		       		let req = data.respBody;

				},
				datatype : "local",//请求数据返回的类型。可选json,xml,txt
				postData:postD3,
				ajaxGridOptions: { contentType: 'application/json; charset=utf-8' },
				colNames : [ '商品编码','商品数量','商品金额','实付金额','商品sku','订单编码','批次','快递公司',
				'发货仓库','促销活动编码','系统优惠金额','卖家调整金额','商品单价','实付单价','备注'],//jqGrid的列显示名字
				colModel : [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
				   {name : 'goodId',align:"center",index : 'orderId',editable:true,},
				   {name : 'goodNum',align:"center",index : 'storeId',editable:true,},				             
				   {name : 'goodMoney',align:"center",index : 'storeName',editable:true,},
				   {name : 'payMoney',align:"center",index : 'isAudit',editable:true,},				             
				   {name : 'goodSku',align:"center",index : 'buyerId',editable:true,},				             
				   {name : 'orderId',align:"center",index : 'recName',editable:true,},				             
				   {name : 'batchNo',align:"center",index : 'recMobile',editable:true,},				             
				   {name : 'expressCom',align:"center",index : 'ecOrderId',editable:true,},				             
				   {name : 'deliverWhs',align:"center",index : 'ecOrderId',editable:true,},				             
				   {name : 'proActId',align:"center",index : 'isInvoice',editable:true,},
				   {name : 'discountMoney',align:"center",index : 'onteFlag',editable:true,},				             
				   {name : 'adjustMoney',align:"center",index : 'isClose',editable:true,},				             
				   {name : 'goodPrice',align:"center",index : 'isClose',editable:true,},				             
				   {name : 'payPrice',align:"center",index : 'isClose',editable:true,},				             
				   {name : 'memo',align:"center",index : 'isShip',editable:true,},				             
				      
				],				
				rowNum : 10,//一页显示多少条
				rowList : [10,20,30],//可供用户选择一页显示多少条			
//				autowidth:true,
				rownumbers: true,
				pager : '#jqGridPager',//表格页脚的占位符(一般是div)的id
				sortname : 'orderId',//初始化的时候排序的字段
				sortorder : "desc",//排序方式,可选desc,asc
				viewrecords : true,
				jsonReader: {  
					records: "respBody.count",// json中代表数据行总数的数据	
		            root: "respBody.list",// json中代表实际模型数据的入口
		            total: "respBody.pages",  // json中代表页码总数的数据
		            repeatitems: true    		

				},
			caption : "订单列表查询",//表格的标题名字	
			onPaging: function(pgButton) {
				 pageNo = $("#jqgrids").jqGrid("getGridParam","page");
				 rowNum = $("select[class='ui-pg-selbox ui-widget-content ui-corner-all']")[0].value >> 0;
		 		 
					if (pgButton === 'prev') {
						pageNo -= 1;

					} else if (pgButton === 'next') {
						pageNo += 1;
						
					} else if (pgButton === 'records' ) {
						$('.ui-pg-input.ui-corner-all').value=1
									
					}else if (pgButton === 'last' ) {
					//	pageNo=pages
					}else if (pgButton === 'first' ) {
						pageNo=1
					}								
				data3.reqBody.pageNo=pageNo;
				data3.reqBody.pageSize=rowNum;
				postD3=JSON.stringify(data3);
		jQuery("#jqgrids").jqGrid('setGridParam',{					
				url : url3+"/mis/ec/platOrder/queryList",//组件创建完成之后请求数据的url
				mtype:"post",
				datatype : "json",//请求数据返回的类型。可选json,xml,txt
				postData:postD3,				
			}).trigger("reloadGrid");	
			
		},
		ondblClickRow: function(){
			$('.saveOrder')[0].disabled=true;
				var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow');//获取行id
				var rowDatas = $("#jqgrids").jqGrid('getRowData', gr);//获取行数据
				jQuery('#jqgrids').editRow(gr, true);
			//点击更新按钮
			$(".update").click(function(){
					var orderId =$('input[name="orderId1"]').val();
					var storeId = $('input[class="storeId1"]').val();
					var storeName = $("input[class='storeName1']").val();
					var isAudit =$('input[name="isAudit1"]').val();
					var buyerId =$('input[name="buyerId1"]').val();
					var recName =$('input[name="recName1"]').val();
					var recMobile =$('input[name="recMobile1"]').val();
					var ecOrderId =$('input[name="ecOrderId1"]').val();
					var isInvoice =$('input[name="isInvoice1"]').val();
					var onteFlag =$('input[name="onteFlag1"]').val();
					var isClose =$('input[name="isClose1"]').val();
					var isShip =$('input[name="isShip1"]').val();
					var orderStatus =$('input[name="orderStatus1"]').val();
					var returnStatus =$('input[name="returnStatus1"]').val();
					var hasGift =$('input[name="hasGift1"]').val();
					var startDate =$('input[name="startDate1"]').val();
					var endDate =$('input[name="endDate1"]').val();			
				   var edit = {
				   		reqHead,
						"reqBody":{
							"orderId":orderId,			
							"storeId":storeId,
							"storeName":storeName,	
							"isAudit":isAudit,
							"buyerId":buyerId,
							"recName":recName,
							"recMobile":recMobile,
							"ecOrderId":ecOrderId,
							"isInvoice":isInvoice,
							"onteFlag":onteFlag,
							"isClose":isClose,
							"isShip":isShip,
							"orderStatus":orderStatus,
							"returnStatus":returnStatus,
							"hasGift":hasGift,
							"startDate":startDate,
							"endDate":endDate,
							"pageNo":pageNo,
							"pageSize":rowNum												
						}
					}
				   	  
				    	$.ajax({
				    		type:"post",
							url:url3+"/mis/ec/ecPlatform/edit",
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
				    })				
		
	} 
		
});
	

	jQuery("#jqgrids").navGrid('#jqGridPager',{edit : true,add : true,del : true}, {id:'editOrder'}, {id:'addOrder'}, {id:'delOrder'});
 

    $(function(){
        $(window).resize(function(){
            $("#jqgrids").setGridWidth($(window).width());
        });
    });
    
//      jQuery("#jqgrids").jqGrid("clearGridData");//清空数据
//		jQuery("#jqgrids").CSS({"display":'none'});
}
jQuery("#jqgrids").jqGrid("clearGridData");//清空数据