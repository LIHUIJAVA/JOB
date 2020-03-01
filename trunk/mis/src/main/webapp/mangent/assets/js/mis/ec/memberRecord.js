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
			url : url3+"/mis/ec/storeRecord/queryList",//组件创建完成之后请求数据的url
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
		var memId= $("input[name='memId1']").val();
		var ecId= $("input[name='ecId1']").val();
		var nick = $("input[name='nick1']").val();
		var name = $("input[name='name1']").val();
		var startDate = $("input[name='startDate1']").val();
		var endDate = $("input[name='endDate1']").val();
		
		var data2 = {			
			reqHead,
			"reqBody":{
				"memId":memId,
				"ecId":ecId,
				"nick": nick ,
				"name":name,
				"startDate":"",
				"endDate":"",
				"pageNo":pageNo,
				"pageSize":rowNum
			}
					   			
		};
	var postD2 = JSON.stringify(data2);
		$('#jqgrids').jqGrid('setGridParam',{
			url:url3+"/mis/ec/memberRecord/queryList",
			mtype:"post",
			datatype : "json",//请求数据返回的类型。可选json,xml,txt
			postData:postD2,
			rowNum:rowNum,
			ajaxGridOptions: { contentType: 'application/json; charset=utf-8' }
		}).trigger('reloadGrid')	
	})	  
});


/*//查询全部
$(function(){
	var pageNo;
	var rowNum;
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
			url : url3+"/ec/storeRecord/queryList",//组件创建完成之后请求数据的url
			mtype:"post",
			datatype : "json",//请求数据返回的类型。可选json,xml,txt
			postData:postD3,
			ajaxGridOptions: { contentType: 'application/json; charset=utf-8' },
			colNames : ['店铺编码','店铺名称', '电商平台编码','电商平台名称','免审策略编码','免审策略名称','销售类型','佣金扣点编码','佣金扣点名称','发货模式','安全库存','结算方式','负责部门','负责人','买家会员号','支付宝账号','联系手机','联系电话','联系人','邮箱地址','业务类型','默认退货仓','备注'],//jqGrid的列显示名字
				colModel : [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
				   {name : 'storeId',align:"center",index : 'invdate',editable:true,},
				   {name : 'storeName',align:"center",index : 'id',editable:true,},				             
				   {name : 'ecId',align:"center",index : 'invdate',editable:true,},
				   {name : 'ecName',align:"center",index : 'id',editable:true,},				             
				   {name : 'noAuditId',align:"center",index : 'invdate',editable:true,},
				   {name : 'noAuditName',align:"center",index : 'id',editable:true,},				             
				   {name : 'salesType',align:"center",index : 'id',editable:true,},				             
				   {name : 'brokId',align:"center",index : 'id',editable:true,},				             
				   {name : 'brokName',align:"center",index : 'id',editable:true,},				             
				   {name : 'deliverMode',align:"center",index : 'id',editable:true,},				             
				   {name : 'safeInv',align:"center",index : 'id',editable:true,},				             
				   {name : 'clearingForm',align:"center",index : 'id',editable:true,},				             
				   {name : 'respDep',align:"center",index : 'id',editable:true,},				             
				   {name : 'respPerson',align:"center",index : 'id',editable:true,},				             
				   {name : 'sellerId',align:"center",index : 'id',editable:true,},				             
				   {name : 'alipayNo',align:"center",index : 'id',editable:true,},				             
				   {name : 'mobile',align:"center",index : 'id',editable:true,},				             
				   {name : 'phone',align:"center",index : 'id',editable:true,},				             
				   {name : 'linkman',align:"center",index : 'id',editable:true,},				             
				   {name : 'email',align:"center",index : 'id',editable:true,},				             
				   {name : 'business',align:"center",index : 'id',editable:true,},				             
				   {name : 'defaultRefWhs',align:"center",index : 'id',editable:true,},				             
				   {name : 'memo',align:"center",index : 'invdate',editable:true,},
				        
				],		            
			rowList : [10,20,30],//可供用户选择一页显示多少条
			autowidth:true,
			pager : '#jqGridPager',//表格页脚的占位符(一般是div)的id
			sortname : 'memId',//初始化的时候排序的字段
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

*/
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
					"memId":"",
					"ecId":"",
					"nick": "" ,
					"name":"",
					"memRegDate":"",
					"memLevName":"",
					"mobile":"",
					"qq":"",
					"email":"",
					"wechat":"",
					"province":"",
					"city":"",
					"county":"",
					"detAddress":"",
					"validDocType":"",
					"validDocNo":"",
					"stopDate":"",
					"alipayNo":"",
					"mempPoints":"",
					"memTimes":"",
					"memo":"",
					"pageNo":pageNo,
					"pageSize":rowNum
								    
			};  
			
			$("#jqgrids").setColProp('memId',{editable:true});//设置editable属性由true改为false
		
			
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
		var memId= $("input[name='memId']").val();
		var ecId= $("input[name='ecId']").val();
		var nick = $("input[name='nick']").val();
		var name = $("input[name='name']").val();
		var memLevName = $("input[name='memLevName']").val();
		var mobile = $("input[name='mobile']").val();
		var qq = $("input[name='qq']").val();
		var email = $("input[name='email']").val();
		var wechat = $("input[name='wechat']").val();
		var province = $("input[name='province']").val();
		var city = $("input[name='city']").val();				
		var county = $("input[name='county']").val();
		var detAddress = $("input[name='detAddress']").val();
		var validDocType = $("input[name='validDocType']").val();
		var validDocNo = $("input[name='validDocNo']").val();
		var stopDate = $("input[name='stopDate']").val();
		var alipayNo = $("input[name='alipayNo']").val();
		var memPoints = $("input[name='memPoints']").val();
		var memTimes = $("input[name='memTimes']").val();
		var memo = $("input[name='memo']").val();
    	
		var save = {
			reqHead,
			"reqBody":{
					"memId":memId,
					"ecId":ecId,
					"nick": nick ,
					"name":name,
					"memLevName":memLevName,
					"mobile":mobile,
					"qq":qq,
					"email":email,
					"wechat":wechat,
					"province":province,
					"city":city,
					"county":county,
					"detAddress":detAddress,
					"validDocType":validDocType,
					"validDocNo":validDocNo,
					"stopDate":stopDate,
					"alipayNo":alipayNo,
					"memPoints":memPoints,
					"memTimes":memTimes,
					"memo":memo,
				}
		}
		var saveJson = JSON.stringify(save);
	
		$.ajax({
			type:"post",
			url:url3+"/mis/ec/memberRecord/add",
			async:true,
			data:saveJson,
			dataType:'json',
			contentType: 'application/json',
			success:function(msgAdd){
				alert(msgAdd.respHead.message)
				window.location.reload();
//				$("#searchAll").trigger('click');
//				$('#jqgrids').css("visibility", "true");
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
    	var goodId= $("input[name='goodId']").val();
		
		var deleteAjax = {			
			reqHead,
			"reqBody":{
						"memId":rowDatas.memId,
											
					}		
		};	
		var deleteData = JSON.stringify(deleteAjax)
		if(gr == null ){
			alert("请选择行")
		}else if(confirm("确定删除？")){
			$.ajax({
				type:"post",
				url:url3+"/mis/ec/memberRecord/delete",
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
			"memId":"",
			"ecId":"",
			"nick": "" ,
			"name":"",
			"memRegDate":"",
			"memLevName":"",
			"mobile":"",
			"qq":"",
			"email":"",
			"wechat":"",
			"province":"",
			"city":"",
			"county":"",
			"detAddress":"",
			"validDocType":"",
			"validDocNo":"",
			"stopDate":"",
			"alipayNo":"",
			"memPoints":"",
			"memTimes":"",
			"memo":"",
			"pageNo":pageNo,
			"pageSize":rowNum
		}
	};
	var postD3 = JSON.stringify(data3);	
//初始化表格
	jQuery("#jqgrids").jqGrid({
				url : url3+"/mis/ec/memberRecord/queryList",//组件创建完成之后请求数据的url
				mtype:"post",
				datatype : "json",//请求数据返回的类型。可选json,xml,txt
				postData:postD3,
				ajaxGridOptions: { contentType: 'application/json; charset=utf-8' },
				colNames : ['会员编码', '电商平台编码','昵称','姓名','注册日期','会员等级','联系手机','qq','邮箱','微信','省','市','县（区）','详细地址','有效证件类型','证件编码','停用日期','支付宝账号','会员积分','购物次数','备注'],//jqGrid的列显示名字
				colModel : [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
				   {name : 'memId',align:"center",index : 'invdate',editable:false},
				   {name : 'ecId',align:"center",index : 'id',editable:true,},				             
				   {name : 'nick',align:"center",index : 'invdate',editable:true,},
				   {name : 'name',align:"center",index : 'id',editable:true,},				             
				   {name : 'memRegDate',align:"center",index : 'invdate',editable:true,},
				   {name : 'memLevName',align:"center",index : 'id',editable:true,},				             
				   {name : 'mobile',align:"center",index : 'id',editable:true,},				             
				   {name : 'qq',align:"center",index : 'id',editable:true,},				             
				   {name : 'email',align:"center",index : 'id',editable:true,},				             
				   {name : 'wechat',align:"center",index : 'id',editable:true,},				             
				   {name : 'province',align:"center",index : 'id',editable:true,},				             
				   {name : 'city',align:"center",index : 'id',editable:true,},
				   {name : 'county',align:"center",index : 'id',editable:true,},				             
				   {name : 'detAddress',align:"center",index : 'id',editable:true,},	
				   {name : 'validDocType',align:"center",index : 'id',editable:true,},				             
				   {name : 'validDocNo',align:"center",index : 'id',editable:true,},				             
				   {name : 'stopDate',align:"center",index : 'id',editable:true,},				             
				   {name : 'alipayNo',align:"center",index : 'id',editable:true,},				             
				   {name : 'memPoints',align:"center",index : 'id',editable:true,},				             
				   {name : 'memTimes',lign:"center",index : 'id',editable:true,},				             
				   {name : 'memo',align:"center",index : 'id',editable:true,},				             
				        
				],				
				rowNum : 10,//一页显示多少条
				rowList : [10,20,30],//可供用户选择一页显示多少条			
				autowidth:true,
				width:"100%",
				height:height,
				autoScroll:true,
				shrinkToFit:false,
				rownumbers: true,
				pager : '#jqGridPager',//表格页脚的占位符(一般是div)的id
				sortname : 'memId',//初始化的时候排序的字段
				sortorder : "desc",//排序方式,可选desc,asc
				viewrecords : true,
				jsonReader: {  
					records: "respBody.count",// json中代表数据行总数的数据	
		            root: "respBody.list",// json中代表实际模型数据的入口
		            total: "respBody.pages",  // json中代表页码总数的数据
		            repeatitems: true    		

				},
//				gridComplete:function(){    
//					$("#jqgrids").hideCol("storeId");         
//                 	$("#jqgrids").hideCol("ecId");  
//					$("#jqgrids").hideCol("noAuditId");     
//					$("#jqgrids").hideCol("brokId");       
//         		},
					
			caption : "会员档案列表查询",//表格的标题名字	
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
				url : url3+"/mis/ec/memberRecord/queryList",//组件创建完成之后请求数据的url
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
				var memId = $("input[name='memId']").val();
				var ecId= $("input[name='ecId']").val();
				var nick = $("input[name='nick']").val();
				var name = $("input[name='name']").val();
				var memLevName = $("input[name='memLevName']").val();
				var mobile = $("input[name='mobile']").val();
				var qq = $("input[name='qq']").val();
				var email = $("input[name='email']").val();
				var wechat = $("input[name='wechat']").val();
				var province = $("input[name='province']").val();
				var city = $("input[name='city']").val();
				var county = $("input[name='county']").val();
				var detAddress = $("input[name='detAddress']").val();
				var validDocType = $("input[name='validDocType']").val();
				var validDocNo = $("input[name='validDocNo']").val();
				var alipayNo = $("input[name='alipayNo']").val();
				var memPoints = $("input[name='memPoints']").val();
				var memTimes = $("input[name='memTimes']").val();
				var memo = $("input[name='memo']").val();
				
				   var edit = {
				   		reqHead,
						"reqBody":{
								"memId":memId,
								"ecId":ecId,
								"nick": nick ,
								"name": name ,
								"memLevName":memLevName,
								"mobile":mobile,
								"qq": qq ,
								"email":email,
								"wechat":wechat,
								"province":province,
								"city":city,
								"county":county,
								"detAddress":detAddress,
								"validDocType":validDocType,
								"validDocNo":validDocNo,
								"alipayNo":alipayNo,
								"memPoints":memPoints,
								"memTimes":memTimes,
								"memo":memo
								
							}
					   }
				   	    editJson = JSON.stringify(edit);
				   	    
				    	$.ajax({
				    		type:"post",
							url:url3+"/mis/ec/memberRecord/edit",
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



//导入
$(function () {
    $(".importExcel").click(function () {
    	var files = $("#FileUpload").val()
    	var fileObj = document.getElementById("FileUpload").files[0];
        var formFile = new FormData();
       	formFile.append("action", "UploadVMKImagePath");  
       	formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
        var data = formFile;
        if(files != "") {
        	$.ajax({
	            type: 'post',
	            url: url + "/mis/purc/IntoWhs/uploadIntoWhsFile",
	            data:data,
	          	dataType: "json",
	           	cache: false,//上传文件无需缓存
	           	processData: false,//用于对data参数进行序列化处理 这里必须false
	           	contentType: false, //必须
	           	success: function(data) {
	           		alert(data.respHead.message)
	           	}
	        });
        } else {
        	alert("请选择文件")
        }   
    });
});


//导出
$(document).on('click', '.exportExcel', function() {
	var data = {
		"reqHead": reqhead,
		"reqBody": {}
	};
	var Data = JSON.stringify(data);
	$.ajax({
		url: url + '/mis/purc/IntoWhs/printingIntoWhsList',
		type: 'post',
		data: Data,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		success: function(data) {
			var arr = [];
			var obj = {}
			var list = data.respBody.list;
			//执行深度克隆
			for(var i = 0; i < list.length; i++) {
				if(list[i].intoWhsSub != null) {
					var entrs = list[i].intoWhsSub
					for(var j = 0; j < entrs.length; j++) {
						(function(j) {
							var newObj = cloneObj(data.respBody.list);
							for(var k in entrs[j]) {
								newObj[i][k] = entrs[j][k]
							}
							arr.push(newObj[i])
						})(j)
					}
				}
			}
			obj=arr;
			for(var i=0;i<obj.length;i++){
				delete obj[i].baoZhiQiDt;
				delete obj[i].pursOrdrNum;//采购订单id
				delete obj[i].depName;//部门名称
				delete obj[i].intoWhsDt1;
				delete obj[i].intoWhsDt2;
				delete obj[i].iptaxRate;//进项税率
				delete obj[i].optaxRate;//销项税率
				delete obj[i].highestPurPrice;//最高进价
				delete obj[i].refCost;//参考成本
				delete obj[i].refSellPrc;//参考售价
				delete obj[i].loSellPrc;//最低售价
				delete obj[i].ltstCost;//最新成本
				delete obj[i].intoWhsSub;//子表
				delete obj[i].iList;//子表
				delete obj[i].ordrNum;//序号 
				delete obj[i].pursOrdrSubTabInd;
				delete obj[i].pursToGdsSnglSubTabInd;
				delete obj[i].returnMemo;//拒收备注
				delete obj[i].returnQty;//拒收数量
				delete obj[i].formDt1;
				delete obj[i].formDt2;
				delete obj[i].chkTm1;//拒收备注
				delete obj[i].chkTm2;//拒收数量
			}
			daochu(obj)
		},
		error: function() {
			console.log(error)
		}
	})
	
})

function daochu(JSONData) {
    var str = '入库单号,入库日期,采购类型编码,单据类型编码,出入库类型编码,收发类别编码,供应商编码,用户编码,用户名称,部门编码,部门名称,采购订单编码,到货单编码,供应商订单号,是否开票,是否结算,是否审核,审核人,审核时间,是否关闭,是否完成,创建人,创建时间,修改人,修改时间,是否记账,记账人,记账时间,检验检疫报告,备注,是否有退货,供应商名称,部门名称,采购类型名称,收发类别,出入库类型名称,存货编码,仓库编码,数量,箱数,税率,无税单价,无税金额,税额,含税单价,价税合计,结算数量,结算单价,结算金额,未开票数量,未开票单价,未开票金额,暂估数量,暂估单价,暂估金额,对应发票号,保质期,国际批次,批次,生产日期,失效日期,货位编码,是否赠品,是否退货,存货名称,规格型号,存货代码,箱规,计量单位编码,计量单位名称,仓库名称,对应条形码,存货分类编码,存货分类名称,\n';

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
    link.download =  "采购入库列表.csv";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}