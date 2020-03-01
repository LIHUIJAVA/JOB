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
			url : url3+"/mis/ec/refundOrderStatus/queryList",//组件创建完成之后请求数据的url
			mtype:"post",
			datatype : "json",//请求数据返回的类型。可选json,xml,txt
			postData:postD3,
			ajaxGridOptions: { contentType: 'application/json; charset=utf-8' },
			colNames : [ '状态编码','状态名称','备注'],//jqGrid的列显示名字
				colModel : [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
				   {name : 'refStatusId',align:"center",index : 'invdate',editable:false, },
				   {name : 'refStatusName',align:"center",index : 'id',editable:true},				             
				   {name : 'memo',align:"center",index : 'invdate',editable:true},
				        
				],		            
			rowList : [10,20,30],//可供用户选择一页显示多少条
			autowidth:true,
			pager : '#jqGridPager',//表格页脚的占位符(一般是div)的id
			sortname : 'refStatusId',//初始化的时候排序的字段
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
			caption : "用户列表查询",//表格的标题名字			
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
		var refStatusId =$('input[name="refStatusId1"]').val();
		
		var data2 = {			
			reqHead,
			"reqBody":{
				"refStatusId":refStatusId,
				"pageSize":rowNum,			
				"pageNo":pageNo,	
			}			
		};
	var postD2 = JSON.stringify(data2);
		$('#jqgrids').jqGrid('setGridParam',{
			url:url3+"/mis/ec/refundOrderStatus/queryList",
			mtype:"post",
			datatype : "json",//请求数据返回的类型。可选json,xml,txt
			postData:postD2,
			rowNum:rowNum,
			ajaxGridOptions: { contentType: 'application/json; charset=utf-8' }
		}).trigger('reloadGrid')	
	})	  
});


//查询全部
$(function(){
	var pageNo;
	var rowNum;
//整单查询
	$('#searchAll').click(function() {		
 
	var data3 = {
		reqHead,
		"reqBody":{
			"pageSize":rowNum,
			"pageNo":pageNo
		}
	};
	var postD3 = JSON.stringify(data3);	
	
	$('#jqgrids').css("visibility", "");	
jQuery("#jqgrids").jqGrid({	
			url : url3+"/ec/refundOrderStatus/queryList",//组件创建完成之后请求数据的url
			mtype:"post",
			datatype : "json",//请求数据返回的类型。可选json,xml,txt
			postData:postD3,
			ajaxGridOptions: { contentType: 'application/json; charset=utf-8' },
			colNames : [ '状态编码','状态名称','备注'],//jqGrid的列显示名字
			colModel : [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
			 	{name : 'refStatusId',index : 'invdate',editable:false,},
				{name : 'refStatusName',index : 'id',},				             
				{name : 'memo',index : 'invdate',},
				],	            
			rowList : [10,20,30],//可供用户选择一页显示多少条
			autowidth:true,
			pager : '#jqGridPager',//表格页脚的占位符(一般是div)的id
			sortname : 'refStatusId',//初始化的时候排序的字段
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
			caption : "退款状态列表查询",//表格的标题名字
			onPaging: function(pgButton) {
				pageNo = $('.ui-pg-input.ui-corner-all')[0].value >> 0;
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
	});

})


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
			    	refStatusId:"",
				    refStatusName:'',
				    memo:''
				    
			};   
			
			$("#jqgrids").setColProp('refStatusId',{editable:true});//设置editable属性由true改为false
		
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
    	var refStatusId = $("input[name='refStatusId']").val();
    	var refStatusName = $("input[name='refStatusName']").val();
    	var memo = $("input[name='memo']").val();
    	
		var save = {
			reqHead,
			"reqBody":{
				"refStatusId" : refStatusId,
				"refStatusName":refStatusName,
				"memo" : memo,
								
			}
		}
		var saveJson = JSON.stringify(save);
		
		$.ajax({
			type:"post",
			url:url3+"/mis/ec/refundOrderStatus/add",
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

 
//删除行
$(function(){
    $(".delOrder").click(function(){
    	var gr = $("#jqgrids").jqGrid('getGridParam', 'selrow');//获取行id
    	var rowDatas = $("#jqgrids").jqGrid('getRowData', gr);//获取行数据    	
    	var refStatusId = $('input[name="refStatusId"]').val();
    	var refStatusName = $('input[name="refStatusName"]').val();
    	var memo = $('input[name="memo"]').val();
    	
		var deleteAjax = {			
			reqHead,
			"reqBody":{	
				"refStatusId":rowDatas.refStatusId,
				"refStatusName":rowDatas.refStatusName,
				"memo":rowDatas.memo,
			}			
		};	
		var deleteData = JSON.stringify(deleteAjax)
		if(gr == null ){
			alert("请选择行")
		}else if(confirm("确定删除？")){
			$.ajax({
				type:"post",
				url:url3+"/mis/ec/refundOrderStatus/delete",
				async:true,
				data:deleteData,
				dataType:'json',
				contentType: 'application/json',
				success:function(remover){
					alert("删除成功");
					window.location.reload();
					$("#searchAll").trigger('click')
					$('#jqgrids').css("visibility", "true");
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
	 pageNo=1;
	 rowNum=10;	
	
	//创建jqGrid组件	
	var data3 = {
		reqHead,
		"reqBody":{
			"refStatusId":"",
			"refStatusName": "" ,
			"memo": "" ,//当前页数
			"pageNo":pageNo,
			"pageSize":rowNum
		}
	};
	var postD3 = JSON.stringify(data3);	
//初始化表格
	jQuery("#jqgrids").jqGrid({
				url : url3+"/mis/ec/refundOrderStatus/queryList",//组件创建完成之后请求数据的url
				mtype:"post",
				datatype : "json",//请求数据返回的类型。可选json,xml,txt
				postData:postD3,
				ajaxGridOptions: { contentType: 'application/json; charset=utf-8' },
				colNames : [ '状态编码','状态名称','备注'],//jqGrid的列显示名字
				colModel : [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
				   {name : 'refStatusId',align:"center",index : 'invdate',editable:false,},
				   {name : 'refStatusName',align:"center",index : 'id',editable:true,},				             
				   {name : 'memo',align:"center",index : 'invdate',editable:true,},
				        
				],				
				rowNum : 10,//一页显示多少条
				rowList : [10,20,30],//可供用户选择一页显示多少条			
				autowidth:true,
				height:height,
				autoScroll:true,
				rownumWidth: 15,  //序列号列宽度
				shrinkToFit:false,
				rownumbers: true,
				pager : '#jqGridPager',//表格页脚的占位符(一般是div)的id
				sortname : 'refStatusId',//初始化的时候排序的字段
				sortorder : "desc",//排序方式,可选desc,asc
				viewrecords : true,
				jsonReader: {  
					records: "respBody.count",// json中代表数据行总数的数据	
		            root: "respBody.list",// json中代表实际模型数据的入口
		            total: "respBody.pages",  // json中代表页码总数的数据
		            repeatitems: true    		

				},
					
			caption : "退款状态列表查询",//表格的标题名字	
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
				url : url3+"/mis/ec/refundOrderStatus/queryList",//组件创建完成之后请求数据的url
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
				var refStatusId= rowDatas.refStatusId;
				var refStatusName = $("input[name='refStatusName']").val();
				var memo = $("input[name='memo']").val();
				
				   var edit = {
				   		reqHead,
						"reqBody":{
							"refStatusId" : refStatusId,
							"refStatusName" : refStatusName,
							"memo" : memo,															
						}
					   }
				   	    editJson = JSON.stringify(edit);
				 
				    	$.ajax({
				    		type:"post",
							url:url3+"/mis/ec/refundOrderStatus/edit",
							async:true,
							data:editJson,
							dataType:'json',
							contentType: 'application/json',
							colNames : [ '状态编码','状态名称','备注'],//jqGrid的列显示名字
							colModel : [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
							   {name : 'refStatusId',align:"center",index : 'invdate',editable:false,},
							   {name : 'refStatusName',align:"center",index : 'id',editable:true,},				             
							   {name : 'memo',align:"center",index : 'invdate',editable:true,},
							        
							],	
							
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
