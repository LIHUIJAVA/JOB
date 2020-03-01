$(function(){
	var orderId;
	$(document).click(function(){

//订单编码$('input:checked').parent().parent().children()[2].innerHTML
		orderId =$('input:checked').parent().parent().children()[2].innerText;

	})

})


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

});

function pageInit(){	
	allHeight()
	 pageNo=1;
	 rowNum=10;	
	 let refundname = JSON.parse(localStorage.getItem("refundname"));
	
	//创建jqGrid组件	
	var data3 = {
		reqHead,
		"reqBody":{
				"refId":refundname,	
				
		}
	};
	var postD3 = JSON.stringify(data3);	

//初始化表格
	jQuery("#jqgrids").jqGrid({
				url :url3+"/mis/ec/refundOrder/query",//组件创建完成之后请求数据的url
				mtype:"post",
				height:380,
				datatype : "json",//请求数据返回的类型。可选json,xml,txt
				postData:postD3,			
				ajaxGridOptions: { contentType: 'application/json; charset=utf-8' },
				colNames : [ '退款单编码','商品编码','商品名称','商品sku','可退货数量','可退货金额','退货金额','退货数量','批次','退货仓库','备注'],//jqGrid的列显示名字
				colModel : [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
				   {name : 'refId',align:"center",index : 'orderId',editable:false,},
				   {name : 'goodId',align:"center",index : 'storeId',editable:true,},				             
				   {name : 'goodName',align:"center",index : 'storeName',editable:true,},
				   {name : 'goodSku',align:"center",index : 'isAudit',editable:true,},	
				   {name : 'canRefNum',align:"center",index : 'buyerId',editable:true,},
				   {name : 'canRefMoney',align:"center",index : 'buyerId',editable:true,},				             
				   {name : 'refMoney',align:"center",index : 'recName',editable:true,},				             
				   {name : 'refNum',align:"center",index : 'recMobile',editable:true,},				             
				   {name : 'batchNo',align:"center",index : 'ecOrderId',editable:true,},				             
				   {name : 'refWhs',align:"center",index : 'isInvoice',editable:true,},				             
				   {name : 'memo',align:"center",index : 'onteFlag',editable:true,},				             
				    
				],				
				rowNum : 10,//一页显示多少条
				rowList : [10,20,30],//可供用户选择一页显示多少条			
				autowidth:true,
				height:height,
				autoScroll:true,
				shrinkToFit:false,
				rownumbers: true,
				pager : '#jqGridPager',//表格页脚的占位符(一般是div)的id
				sortname : 'refId',//初始化的时候排序的字段
				sortorder : "desc",//排序方式,可选desc,asc
				viewrecords : true,
				
				loadComplete: function (data) {
				    let req = data.respBody.list;
				   
				    var refId2;
				    $(document).click(function(){
				    
				    	refId2 =$('input:checked').parent().parent().children()[2].innerHTML;
								  
					    for(let i=0;i<req.length;i++){					    
					    	if(req[i].refId==refId2){
					    		$('input[name="refId1"]').val(req[i].refId);
					    		$('input[name="orderId1"]').val(req[i].orderId);
					    		$('input[name="ecId1"]').val(req[i].ecId);
					    		$('input[name="ecOrderId1"]').val(req[i].ecOrderId);
					    		$('input[name="storeId1"]').val(req[i].storeId);
					    		$('input[name="storeName1"]').val(req[i].storeName);
					    		$('input[name="ecRefId1"]').val(req[i].ecRefId);
					    		$('input[name="applyDate1"]').val(req[i].applyDate);
					    		$('input[name="buyerId1"]').val(req[i].buyerId);
					    		$('input[name="isRefNum1"]').val(req[i].isRefNum);
					    		$('input[name="allRefNum1"]').val(req[i].allRefNum);
					    		$('input[name="allRefMoney1"]').val(req[i].allRefMoney);
					    		$('input[name="refReason1"]').val(req[i].refReason);
					    		$('input[name="refExplain1"]').val(req[i].refExplain);
					    		$('input[name="refStatus1"]').val(req[i].refStatus);
					    		$('input[name="downTime1"]').val(req[i].downTime);
					    		$('input[name="treDate1"]').val(req[i].treDate);
					    		$('input[name="operator1"]').val(req[i].operator);
					    		$('input[name="isAudit1"]').val(req[i].isAudit);
					    		$('input[name="memo1"]').val(req[i].memo);
					    		
					    	}
					    }
					    
				    })
		
				},
				jsonReader: {  
					records: "respBody.count",// json中代表数据行总数的数据	
		            root: "respBody.list",// json中代表实际模型数据的入口
		            total: "respBody.pages",  // json中代表页码总数的数据
		            repeatitems: true    		

				},
			multiselect: true,
			caption : "退款单查询",//表格的标题名字	
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
				url : url3+"/mis/ec/refundOrder/queryList",//组件创建完成之后请求数据的url
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
		   	    editJson = JSON.stringify(edit);
		   	    
		    	$.ajax({
		    		type:"post",
					url:url3+"/mis/ec/refundOrder/edit",
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
}
