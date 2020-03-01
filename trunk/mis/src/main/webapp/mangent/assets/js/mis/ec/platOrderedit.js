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
			url : url3+"/mis/ec/PlatOrder/queryList",//组件创建完成之后请求数据的url
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
	$(".saveOrder").click(function(){
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
    	
		var save = {
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
								
			}
		}
		var saveJson = JSON.stringify(save);
		$.ajax({
			type:"post",
			url:url3+"/mis/ec/ecPlatform/add",
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
	})
})


function pageInit(){	
	allHeight()
	pageNo=1;
	rowNum=10;	
	let name = JSON.parse(localStorage.getItem("name"));

	//创建jqGrid组件	
	var data3 = {
		reqHead,
		"reqBody":{
				"orderId":name,			

		}
	};
	var postD3 = JSON.stringify(data3);	
	
//初始化表格
	jQuery("#jqgrids").jqGrid({
		url :url3+"/mis/ec/platOrder/query",//组件创建完成之后请求数据的url
		mtype:"post",
		height:height,
		loadComplete: function (data) {
       let req = data.respBody;
		$('input[name="orderId1"]').val(req.orderId);
		$('input[name="storeId1"]').val(req.storeId);
		$('input[name="storeName1"]').val(req.storeName);
		$('input[name="payTime1"]').val(req.payTime);
		$('input[name="waif1"]').val(req.waif);
		$('input[name="isAudit1"]').val(req.isAudit);
		$('input[name="auditHNumber1"]').val(req.auditHNumber);
		$('input[name="goodNum1"]').val(req.goodNum);
		$('input[name="goodMoney1"]').val(req.goodMoney);
		$('input[name="payMoney1"]').val(req.payMoney);
		$('input[name="buyerNote1"]').val(req.buyerNote);
		$('input[name="sellerNote1"]').val(req.sellerNote);
		$('input[name="recAddress1"]').val(req.recAddress);
		$('input[name="buyerId1"]').val(req.buyerId);
		$('input[name="recName1"]').val(req.recName);
		$('input[name="recMobile1"]').val(req.recMobile);
		$('input[name="ecOrderId1"]').val(req.ecOrderId);
		$('input[name="isInvoice1"]').val(req.isInvoice);
		$('input[name="invoiceTiel1"]').val(req.invoiceTiel);
		$('input[name="onteFlag1"]').val(req.onteFlag);
		$('input[name="isCloce1"]').val(req.isCloce);
		$('input[name="isShip1"]').val(req.isShip);
		$('input[name="adjustMoney1"]').val(req.adjustMoney);
		$('input[name="discountMoney1"]').val(req.discountMoney);
		$('input[name="goodPrice1"]').val(req.goodPrice);
		$('input[name="payPrice1"]').val(req.payPrice);
		$('input[name="orderStatus1"]').val(req.orderStatus);
		$('input[name="returnStatus1"]').val(req.returnStatus);
		$('input[name="hasGift1"]').val(req.hasGift);
		$('input[name="memo1"]').val(req.memo);
					
		},
		datatype : "json",//请求数据返回的类型。可选json,xml,txt
		postData:postD3,
		ajaxGridOptions: { contentType: 'application/json; charset=utf-8' },
		colNames : [ '商品编码','商品数量','商品金额','实付金额','商品sku','订单编码','批次','快递公司','发货仓库','促销活动编码','系统优惠金额','卖家调整金额','备注'],//jqGrid的列显示名字
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
		   {name : 'memo',align:"center",index : 'isShip',editable:true,},				             
		      
		],				
		rowNum : 10,//一页显示多少条
		rowList : [10,20,30],//可供用户选择一页显示多少条			
		autowidth:true,
		rownumbers: true,
		autoScroll:true,
		shrinkToFit:false,
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
//				multiselect: true,
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
		
		} 
		
	});
	jQuery("#jqgrids").navGrid('#jqGridPager',{edit : true,add : true,del : true}, {id:'editOrder'}, {id:'addOrder'}, {id:'delOrder'});  
    $(function(){
        $(window).resize(function(){
            $("#jqgrids").setGridWidth($(window).width());
        });
    });
    

}



$(function(){
//点击更新按钮
$(".update").click(function(){
	var orderId =$('input[name="orderId1"]').val();
	var storeId = $('input[class="storeId1"]').val();
	var storeName = $("input[class='storeName1']").val();
	var payTime =$('input[name="payTime1"]').val();
	var waif =$('input[name="waif1"]').val();
	var isAudit =$('input[name="isAudit1"]').val();
	var auditHint =$('input[name="auditHint1"]').val();
	var goodNum =$('input[name="goodNum1"]').val();
	var goodMoney =$('input[name="goodMoney1"]').val();
	var payMoney =$('input[name="payMoney1"]').val();
	var buyerNote =$('input[name="buyerNote1"]').val();
	var sellerNote =$('input[name="sellerNote1"]').val();
	var recAddress =$('input[name="recAddress1"]').val();
	var buyerId =$('input[name="buyerId1"]').val();
	var recName =$('input[name="recName1"]').val();
	var recMobile =$('input[name="recMobile1"]').val();
	var ecOrderId =$('input[name="ecOrderId1"]').val();	
	var isInvoice =$('input[name="isInvoice1"]').val();	
	var invoiceTitle =$('input[name="invoiceTitle1"]').val();	
	var noteFlag =$('input[name="noteFlag1"]').val();	
	var isClose =$('input[name="isClose1"]').val();	
	var isShip =$('input[name="isShip1"]').val();	
	var adjustMoney =$('input[name="adjustMoney1"]').val();	
	var discountMoney =$('input[name="discountMoney1"]').val();	
	var adjustStatus =$('input[name="adjustStatus1"]').val();	
	var tradeDt =$('input[name="tradeDt1"]').val();	
	var bizTypId =$('input[name="bizTypId1"]').val();	
	var sellTypId =$('input[name="sellTypId1"]').val();	
	var recvSendCateId =$('input[name="recvSendCateId1"]').val();	
	var orderStatus =$('input[name="orderStatus1"]').val();	
	var returnStatus =$('input[name="returnStatus1"]').val();	
	var hasGift =$('input[name="hasGift1"]').val();	
	var memo =$('input[name="memo1"]').val();	
	
	
	var goodIds =$('input[name="goodId"]').val();
	var goodNums = $('input[class="goodNum"]').val();
	var goodMoneys = $("input[class='goodMoney']").val();
	var payMoneys =$('input[name="payMoney"]').val();
	var goodSkus =$('input[name="goodSku"]').val();
	var orderIds =$('input[name="orderId"]').val();
	var batchNos =$('input[name="batchNo"]').val();
	var expressComs =$('input[name="expressCom"]').val();
	var deliverWhss =$('input[name="deliverWhs"]').val();
	var proActIds =$('input[name="proActId"]').val();
	var discountMoneys =$('input[name="discountMoney"]').val();
	var adjustMoneys =$('input[name="adjustMoney"]').val();
	var goodPrices =$('input[name="goodPrice"]').val();
	var payPrices =$('input[name="payPrice"]').val();
	var memos =$('input[name="memo"]').val();
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
			"noteFlag":noteFlag,
			"isClose":isClose,
			"isShip":isShip,
			"orderStatus":orderStatus,
			"returnStatus":returnStatus,
			"hasGift":hasGift,
			"memo":memo,
			"list":[{
				"goodId":goodIds,
				"goodNum":goodNums,
				"goodMoney":goodMoneys,
				"payMoney":payMoneys,
				"goodSku":goodSkus,
				"orderId":orderIds,
				"batchNo":batchNos,
				"expressCom":expressComs,
				"deliverWhs":deliverWhss,
				"proActId":proActIds,
				"discountMoney":discountMoneys,
				"adjustMoney":adjustMoneys,
				"goodPrice":goodPrices,
				"payPrice":payPrices,
				"memo":memos
				
			}],
														
		}
	}
   	    editJson = JSON.stringify(edit);
   	    console.log(editJson)
    	$.ajax({
    		type:"post",
			url:url3+"/mis/ec/platOrder/edit",
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
})
