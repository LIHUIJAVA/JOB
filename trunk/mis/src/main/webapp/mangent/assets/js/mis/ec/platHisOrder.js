
$(function(){
	var orderId;
	$(document).click(function(){

//订单编码$('input:checked').parent().parent().children()[2].innerHTML
//		orderId =$('input:checked').parent().parent().children()[2].innerHTML;

	})
	
	//联查
	$('.jSearch').click(function(){
	
		var data = {
			reqHead,
			"reqBody":{
				"orderId":orderId
			}
		};
		var postData = JSON.stringify(data);
		$.ajax({
			type:"post",
			url:url3+"/mis/ec/platOrder/unionQuery",
			async:true,
			data:postData,
			dataType:'json',
			contentType: 'application/json',
			success:function(msgAdd){
				alert(msgAdd.respHead.message)
					
//				window.location.reload();
//				$("#searchAll").trigger('click');
//				$('#jqgrids').css("visibility", "true");
			},
			error:function(err){
				console.log("失败")
			}
		});
		
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
			url : url3+"/mis/ec/ecPlatform/queryList",//组件创建完成之后请求数据的url
			mtype:"post",
			datatype : "json",//请求数据返回的类型。可选json,xml,txt
			postData:postD3,
			ajaxGridOptions: { contentType: 'application/json; charset=utf-8' },
			   	            
			rowList : [10,20,30],//可供用户选择一页显示多少条
			autowidth:true,
			pager : '#jqGridPager',//表格页脚的占位符(一般是div)的id
			sortname : 'ecId',//初始化的时候排序的字段
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
		var storeId = $('input[name="storeId1"]').val();
		var isAudit =$('input[name="isAudit1"]').val();
		var buyerId =$('input[name="buyerId1"]').val();
		var recName =$('input[name="recName1"]').val();
		var recMobile =$('input[name="recMobile1"]').val();
		var ecOrderId =$('input[name="ecOrderId1"]').val();
		var isInvoice =$('input[name="isInvoice1"]').val();
		var onteFlag =$('input[name="onteFlag1"]').val();
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
				"isAudit":isAudit,
				"buyerId":buyerId,
				"recName":recName,
				"recMobile":recMobile,
				"ecOrderId":ecOrderId,
				"isInvoice":isInvoice,
				"onteFlag":onteFlag,
				"isShip":isShip,
				"orderStatus":orderStatus,
				"returnStatus":returnStatus,
				"hasGift":hasGift,
				"startDate":startDate,
				"endDate":endDate,
				"pageNo":pageNo,
				"page": 1,
				"pageSize": 500

			}			
		};
		var postD2 = JSON.stringify(data2);
		$.ajax({
			type: "post",
			contentType: 'application/json; charset=utf-8',
			url : url3+"/mis/ec/platHisOrder/queryList",
			async: true,
			data: postD2,
			dataType: 'json',
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
//		$('#jqgrids').jqGrid('setGridParam',{
//			url:url3+"/mis/ec/ecPlatform/queryList",
//			mtype:"post",
//			datatype : "json",//请求数据返回的类型。可选json,xml,txt
//			postData:postD2,
//			rowNum:rowNum,
//			ajaxGridOptions: { contentType: 'application/json; charset=utf-8' }
//		}).trigger('reloadGrid')	
	})	  
});



//增行   保存
$(function(){
//	$(".addOrder").click(function(){
//			var newrowid ;
//	        var selectedId = $("#jqgrids").jqGrid("getGridParam", "selrow");
//          var ids = jQuery("#jqgrids").jqGrid('getDataIDs');
//
//	        //获得当前最大行号（数据编码）
//	        var rowid = Math.max.apply(Math,ids);
//	 		//获得新添加行的行号（数据编码）
//	        newrowid = rowid+1;
//			var dataRow = { 
//			    	ecId:"",
//				    ecName:'',
//				    memo:''
//				    
//			};   
//			
//			
//		$("#jqgrids").setColProp('ecId',{editable:true});//设置editable属性由true改为false
//		$("#jqgrids").setColProp('storeId',{editable:true});//设置editable属性由true改为false
//
//	    //将新添加的行插入到第一列
//	    $("#jqgrids").jqGrid("addRowData", newrowid, dataRow, "first");
//	    //设置grid单元格可编辑
//	    $('#jqgrids').jqGrid('editRow', newrowid, false);	 
//		$(document).keyup(function(event){
//			  if(event.keyCode ==13){
//			    $(".saveOrder").trigger("click");
//			  }
//		});
//	})
//	$(".saveOrder").click(function(){
//  	var ecId = $("input[name='ecId']").val();
//  	var ecName = $("input[name='ecName']").val();
//  	var memo = $("input[name='memo']").val();
//  	
//		var save = {
//			reqHead,
//			"reqBody":{
//				"ecId" : ecId,
//				"ecName":ecName,
//				"memo" : memo,
//								
//			}
//		}
//		var saveJson = JSON.stringify(save);
//		console.log(saveJson);
//		$.ajax({
//			type:"post",
//			url:url3+"/mis/ec/ecPlatform/add",
//			async:true,
//			data:saveJson,
//			dataType:'json',
//			contentType: 'application/json',
//			success:function(msgAdd){
//				alert(msgAdd.respHead.message)
//				window.location.reload();
//				$("#searchAll").trigger('click');
//				$('#jqgrids').css("visibility", "true");
//			},
//			error:function(err){
//				console.log("失败")
//			}
//		});
//	})
})

 
//删除行
$(function(){
    $(".delOrder").click(function(){
    	var gr = $("#jqgrids").jqGrid('getGridParam', 'selarrrow');//获取行id
    	var num = []
    	for(var i =0; i<gr.length;i++) {
    		var rowDatas = $("#jqgrids").jqGrid('getRowData', gr[i]);//获取行数据    	
    		console.log(rowDatas)
    		var orderId = rowDatas.ecOrderId
    		console.log(orderId)
    		num.push(orderId)
    	}
        console.log(num)
		var deleteData = num.toString()
		console.log(deleteData)
		var deleteAjax = {			
			reqHead,
			"reqBody":{	
				"orderId":deleteData,
				
			}			
		};
		var saveJson = JSON.stringify(deleteAjax);
		console.log(saveJson)
		if(gr == null ){
			alert("请选择行")
		}else if(confirm("确定删除？")){
			$.ajax({
				type:"post",
				url:url3+"/mis/ec/platHisOrder/delete",
				async:true,
				data:saveJson,
				dataType:'json',
				contentType: 'application/json',
				success:function(remover){
					alert("删除成功");
//					window.location.reload();
//					$("#searchAll").trigger('click')
//					$('#jqgrids').css("visibility", "true");
				},
				error:function(){
					console.log("删除失败")
					
				}
			});
			
		}
    })
})


function pageInit(){	
	allHeight()
//初始化表格
	jQuery("#jqgrids").jqGrid({
		url: '../../assets/js/json/order.json',
		datatype : "json",//请求数据返回的类型。可选json,xml,txt
		colNames : [ '订单编码','店铺编码','店铺名称','付款时间','旗标','是否客审',
		'审核提示','商品数量','商品金额','实付金额','买家留言','卖家备注','收货地址',
		'买家会员','收货人名','收货电话','电商订单号','是否开发票','发票抬头','卖家备注旗帜',
		'是否关闭','是否发货','卖家调整金额','系统优惠金额','商品单价','实付单价','订单状态','退货状态','是否含赠品','备注'],//jqGrid的列显示名字
		colModel : [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
		   {name : 'orderId',align:"center",index : 'invdate',editable:false,},
		   {name : 'storeId',align:"center",index : 'id',editable:true,},				             
		   {name : 'storeName',align:"center",index : 'invdate',editable:true,},
		   {name : 'payTime',align:"center",index : 'invdate',editable:true,},
		   {name : 'waif',align:"center",index : 'invdate',editable:true,},
		   {name : 'isAudit',align:"center",index : 'invdate',editable:true,},
		   {name : 'auditHNumber',align:"center",index : 'invdate',editable:true,},
		   {name : 'goodNum',align:"center",index : 'invdate',editable:true,},
		   {name : 'goodMoney',align:"center",index : 'invdate',editable:true,},
		   {name : 'payMoney',align:"center",index : 'invdate',editable:true,},
		   {name : 'buyerNote',align:"center",index : 'invdate',editable:true,},
		   {name : 'sellerNote',align:"center",index : 'invdate',editable:true,},
		   {name : 'recAddress',align:"center",index : 'invdate',editable:true,},
		   {name : 'buyerId',align:"center",index : 'invdate',editable:true,},
		   {name : 'recName',align:"center",index : 'invdate',editable:true,},
		   {name : 'recMobile',align:"center",index : 'invdate',editable:true,},
		   {name : 'ecOrderId',align:"center",index : 'invdate',editable:true,},
		   {name : 'isInvoice',align:"center",index : 'invdate',editable:true,},
		   {name : 'invoiceTiel',align:"center",index : 'invdate',editable:true,},
		   {name : 'onteFlag',align:"center",index : 'invdate',editable:true,},
		   {name : 'isClose',align:"center",index : 'invdate',editable:true,},
		   {name : 'isShip',align:"center",index : 'invdate',editable:true,},
		   {name : 'adjustMoney',align:"center",index : 'invdate',editable:true,},
		   {name : 'discountMoney',align:"center",index : 'invdate',editable:true,},
		   {name : 'goodPrice',align:"center",index : 'invdate',editable:true,},
		   {name : 'payPrice',align:"center",index : 'invdate',editable:true,},
		   {name : 'orderStatus',align:"center",index : 'invdate',editable:true,},
		   {name : 'returnStatus',align:"center",index : 'invdate',editable:true,},
		   {name : 'hasGift',align:"center",index : 'invdate',editable:true,},
		   {name : 'memo',align:"center",index : 'invdate',editable:true,},
		       
		],				
		rowNum : 10,//一页显示多少条
		rowList : [10,20,30],//可供用户选择一页显示多少条			
		width:'123%',
		autowidth:true,
		height:height,
		autoScroll:true,
		shrinkToFit:false,
		multiselect: true, //复选框
		multiboxonly: true,
		cellEdit: true,
		cellsubmit: "clientArray",
		rownumbers: true,
		pager : '#jqGridPager',//表格页脚的占位符(一般是div)的id
		sortname : 'orderId',//初始化的时候排序的字段
		sortorder : "desc",//排序方式,可选desc,asc
		viewrecords : true,
			
		caption : "电商平台列表查询",//表格的标题名字	
//			onPaging: function(pgButton) {
//				 pageNo = $("#jqgrids").jqGrid("getGridParam","page");
//				 rowNum = $("select[class='ui-pg-selbox ui-widget-content ui-corner-all']")[0].value >> 0;
//		 		 
//					if (pgButton === 'prev') {
//						pageNo -= 1;
//
//					} else if (pgButton === 'next') {
//						pageNo += 1;
//						
//					} else if (pgButton === 'records' ) {
//						$('.ui-pg-input.ui-corner-all').value=1
//									
//					}else if (pgButton === 'last' ) {
//					//	pageNo=pages
//					}else if (pgButton === 'first' ) {
//						pageNo=1
//					}								
//				data3.reqBody.pageNo=pageNo;
//				data3.reqBody.pageSize=rowNum;
//				postD3=JSON.stringify(data3);
//		jQuery("#jqgrids").jqGrid('setGridParam',{					
//				url : url3+"/mis/ec/platHisOrder/queryList",//组件创建完成之后请求数据的url
//				mtype:"post",
//				datatype : "json",//请求数据返回的类型。可选json,xml,txt
//				postData:postD3,				
//			}).trigger("reloadGrid");	
//		},
		ondblClickRow: function(){
			
		
		}
//	jQuery("#jqgrids").navGrid('#jqGridPager',{edit : true,add : true,del : true}, {id:'editOrder'}, {id:'addOrder'}, {id:'delOrder'});  

	})
}


$(function(){
	

	//点击更新按钮
	$(".update").click(function(){
		var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow');
		
		//获得行数据
		var rowDatas = $("#jqgrids").jqGrid('getRowData', gr);
	//	var ecId= rowDatas.ecId;
	//	var ecName = $("input[name='ecName']").val();
	//	var memo = $("input[name='memo']").val();
		
		   var edit = {
		   		reqHead,
	//			"reqBody":{
	//				"ecId" : ecId,
	//				"ecName" : ecName,
	//				"memo" : memo,															
	//			}
				"reqBody":rowDatas
		   	}
		   	editJson = JSON.stringify(edit);
		   	console.log(editJson)
//	  	$.ajax({
//	  		type:"post",
//				url:url3+"/mis/ec/ecPlatform/edit",
//				async:true,
//				data:editJson,
//				dataType:'json',
//				contentType: 'application/json',
//				success:function(editMsg){
//					alert(editMsg.respHead.message);
//			    	window.location.reload();
//				},
//				error:function(){
//					console.log("更新失败")
//				}
//	  	});
	})
})