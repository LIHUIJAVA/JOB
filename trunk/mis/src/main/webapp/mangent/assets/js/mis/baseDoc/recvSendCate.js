//树的渲染
$(function() {
	var data3 = {
		"reqHead": reqhead,
		"reqBody": {}
	};
	var postD3 = JSON.stringify(data3);
	$.ajax({
		type: "post",
		data: postD3,
		url: url + "/mis/purc/RecvSendCate/selectRecvSendCate",
		async: true,
		dataType: 'json',
		contentType: "application/json; charset=utf-8",
		success: function(data) {
			var obj = data.respBody.list;
			var arr1 = Array.from(obj)
			paintingTree(arr1, "recv_tree")

			function paintingTree(arr1, id) {
				if(arr1[0]["pId"] !== undefined) {
					arr1 = removeEmptyFromPaintData(arr1)
				}
				var str = ""
				//渲染树
				function createTree(arr1) {
					if(arr1) {
						var children = arr1;
						str += "<ul>";
						for(var j = 0; j < children.length; j++) {
							str += "<li>"
							if(children[j]["children"]) {
								if(children[j]["open"]) {
									str += "<div open='true'><span class='close'></span><span class='openTrue click_span']><span class='provrClsId'>" + children[j]["recvSendCateId"] + "</span><span class='pid'>" + ")&nbsp&nbsp" + "</span><span class='provrClsNm'>" + children[j]["recvSendCateNm"] + "</span><span class='level'>" + children[j]["level"] + "</span></div>";
								} else {
									str += "<div open='false'><span class='open'></span><span class='openTrue click_span'><span class='provrClsId'>" + children[j]["recvSendCateId"] + "</span><span class='pid'>" + ")&nbsp&nbsp" + "</span><span class='provrClsNm'>" + children[j]["recvSendCateNm"] + "</span><span class='level'>" + children[j]["level"] + "</span></div>";
								}

							} else {
								str += "<div><span class='leaf'></span><span class='leafName click_span'><span class='provrClsId'>" + children[j]["recvSendCateId"] + "</span><span class='pid'>" + ")&nbsp&nbsp" + "</span><span class='provrClsNm'>" + children[j]["recvSendCateNm"] + "</span><span class='level'>" + children[j]["level"] + "</div>";
							}

							createTree(children[j]["children"])
							str += "</li>"
						}
						str += "</ul>";
					}
				}

				createTree(arr1)
				$("#" + id).hide()
				$("#" + id).html(str)
				$("[open=true]").each(function() {
					$(this).next().show()
				})
				$("[open=false]").each(function() {
					$(this).next().hide()
				})
				$(document).on("click", ".close", function() {
					$(this).parent().next().hide()
					$(this).addClass("open").removeClass("close")
				})
				$(document).on("click", ".open", function() {
					$(this).parent().next().show()
					$(this).addClass("close").removeClass("open")
				})
				$('#recv_tree>ul>li>div span').parent().next().show()
				$('#recv_tree>ul>li>div span').eq(0).removeClass("open").addClass("close")
				$("#" + id).show()
			}
		},
		error: function() {
			alert("获取失败")
		},
	});

})
//条件查询
$(function() {
	$(document).on('click', '.click_span', function() {
		$(".addColor").removeClass("addColor")
		$(this).addClass("addColor")

		var addId = $(this).children().first().text();
		var myData = {};
		var Data = {
			"reqHead": reqhead,
			"reqBody": {
				"recvSendCateId": addId
			}
		}
		var addData = JSON.stringify(Data)
		$.ajax({
			type: "post",
			url: url + "/mis/purc/RecvSendCate/selectRecvSendCateByRecvSendCateId",
			async: true,
			data: addData,
			dataType: 'json',
			contentType: 'application/json; charset=utf-8',
			success: function(data) {
				$("#recvSendCateId").val(data.respBody.recvSendCateId);
				$("#recvSendCateNm").val(data.respBody.recvSendCateNm);
				$("#cntPtySubjEncd").val(data.respBody.cntPtySubjEncd);
				$("#memo").val(data.respBody.memo);
				var recv = data.respBody.recvSendInd;
				var ha = recv == 1 ? "收" : "发";
				$("#recvSendInd").val(ha);
			},
			error: function() {
				alert("查询失败")
			}
		});

	})
})
var lev = '';
var pid = '';
//新增收发类别档案
$(function() {
	$(document).on('click', '.click_span', function() {
		//点击改变颜色
		$(".addColor").removeClass("addColor")
		$(this).addClass("addColor")

		var pid1 = $(this).children().first().text();  
		var level = $(this).children().last().text();
		var levelNum = parseInt(level) + 1;
		var levelString = levelNum.toString();
		lev = levelString;
		pid = pid1;
	});
})
$(function() {
	//增加存货分类
	$('.addOrder').click(function() {
		mType = 1;
		$(".dialog").show();
		$(".dialog .cancel").click(function() {
			$(".dialog").hide();
		})

	})

})
//保存按钮保存
function addNewData() {
	var recvSendCateId = $("#addId").val();
	var recvSendCateNm = $("#addNm").val();
	var recvSendInd = $("input[name='recv']:checked").val();
	var cntPtySubjEncd = $("#addEncd").val();
	var memo = $("#addMemo").val()

	var Data = {
		"reqHead": reqhead,
		"reqBody": {
			"recvSendCateId": recvSendCateId,
			"recvSendCateNm": recvSendCateNm,
			"recvSendInd": recvSendInd,
			"cntPtySubjEncd": cntPtySubjEncd,
			"level": lev,
			"pid": pid,
			"memo": memo,
		}
	}
	var addData = JSON.stringify(Data)
	if(recvSendCateId == '' && recvSendCateNm == '' && cntPtySubjEncd == '' && memo == '') {
		alert('请重新输入内容')
	} else {
		$.ajax({
			type: "post",
			url: url + "/mis/purc/RecvSendCate/insertRecvSendCate",
			async: true,
			data: addData,
			dataType: 'json',
			contentType: 'application/json; charset=utf-8',
			success: function(data) {
				alert(data.respHead.message);
				window.location.reload()
			},
			error: function() {
				alert("增加失败")
			}
		})

	}

}

$(function() {
	$('.editOrder').click(function() {
		mType = 2;
		$(".edit").show();
		$(".edit .cancel").click(function() {
			$(".edit").hide();
		})
		$("#updateId").val($(".recvSendCateId").val());
		$("#updateNm").val($(".recvSendCateNm").val());
		//		$("#updateInd").val($(".recvSendInd").val());
		$("#updateEncd").val($(".cntPtySubjEncd").val());
		$("#updateMemo").val($(".memo").val())
		$("#updateId").val($(".recvSendCateId").val());

	})
})

//更新按钮保存
function addEditData() {
	var recvSendCateId = $("#updateId").val();
	var recvSendCateNm = $("#updateNm").val();
	var recvSendInd = $("input[name='updateRecv']:checked").val();
	var cntPtySubjEncd = $("#updateEncd").val();
	var memo = $("#updateMemo").val()
	var Data = {
		"reqHead": reqhead,
		"reqBody": {
			"recvSendCateId": recvSendCateId,
			"recvSendCateNm": recvSendCateNm,
			"recvSendInd": recvSendInd,
			"cntPtySubjEncd": cntPtySubjEncd,
			"memo": memo,
		}
	}
	var updateData = JSON.stringify(Data)
	$.ajax({
		type: "post",
		url: url + "/mis/purc/RecvSendCate/updateRecvSendCateByRecvSendCateId",
		async: true,
		data: updateData,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		success: function(data) {
			alert(data.respHead.message);
			window.location.reload()
		},
		error: function() {
			alert("编辑失败")
		}
	});

}

var mType;
var isclick = true;
$(function() {
	$(".recv_saveOrder").click(function() {
		if(isclick) {
			isclick = false;
			if(mType == 1) {
				$(".addOrder").css("background-color", 'black')
				addNewData();
			}
			if(mType == 2) {
				addEditData();
			}
			setTimeout(function() {
				isclick = true;
			}, 1000);
		}
	})
})
//删除按钮
$(function() {

	//删除按钮
	$(".delOrder").click(function() {
		var roleid = $(".recvSendCateId").val();
		var deleteData = {
			"reqHead": reqhead,
			"reqBody": {
				"recvSendCateId": roleid
			}
		}
		var deleteData = JSON.stringify(deleteData)
		if(roleid == '') {
			alert("请选择")
		} else if(roleid == 01) {
			alert("不能删除")
		}else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + "/mis/purc/RecvSendCate/deleteRecvSendCateByRecvSendCateId",
				async: true,
				data: deleteData,
				dataType: 'json',
				contentType: 'application/json; charset=utf-8',
				success: function(remover) {
					window.location.reload()
				},
				error: function() {
					alert("删除失败")
				}
			});
		}
	})
})

//导入
$(function () {
    $(".recv_importExcel").click(function () {
    	var files = $("#FileUpload").val()
    	var fileObj = document.getElementById("FileUpload").files[0];
        var formFile = new FormData();
       	formFile.append("action", "UploadVMKImagePath");  
       	formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
        var data = formFile;
        if(files != "") {
        	$.ajax({
	            type: 'post',
	            url: url + "/mis/purc/RecvSendCate/uploadRecvSendCateFile",
	            data:data,
	          	dataType: "json",
	           	cache: false,//上传文件无需缓存
	           	processData: false,//用于对data参数进行序列化处理 这里必须false
	           	contentType: false, //必须
	           	success: function(data) {
	           		alert(data.respHead.message)
	           		window.location.reload()
	           	}
	        });
        } else {
        	alert("请选择文件")
        }   
    });
});

//导出
$(document).on('click', '.recv_exportExcel', function() {
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {}
	}
	var Data = JSON.stringify(savedata);
	$.ajax({
		url: url + '/mis/purc/RecvSendCate/selectRecvSendCate',
		type: 'post',
		data: Data,
		dataType: 'json',
		async: true,
		contentType: 'application/json;charset=utf-8',
		beforeSend: function() {
			$("#mengban").css("display", "block");
			$("#loader").css("display", "block");
		},
		complete: function() {
			$("#mengban").css("display", "none");
			$("#loader").css("display", "none");
		},
		success: function(data) {
			var obj = {}
			var arr = data.respBody.list[0].children;
			var myData = [];
			for(var i = 0; i < arr.length; i++) {
				if((arr[i].children != null) && (arr[i].children.length > 0)) {
					for(var j = 0; j < arr[i].children.length; j++){
						myData.push(arr[i].children[j]);
					}
				} else {
					myData.push(arr[i]);
				}
			}
			obj = myData;
			daochu(obj)
		},
		error: function() {
			alert("导出失败")
		}
	})

})

function daochu(JSONData) {
    var str = '对方科目编码,ico图片,级别,收发类别名称,备注,对应父级编码,收发类别编码,收发标识\n';

    for(let i=0;i<JSONData.length;i++){
        var result ='';
        if (JSONData[i].orderStatusc=='0'){
            result="是";
        } else {
            result="否";
        }
		for(let item in JSONData[i]) {
			if(JSONData[i][item]==null){
				JSONData[i][item]="";
			}
			str += `${JSONData[i][item] + '\t'},`;
		}
		str += '\n';
    }
    var blob = new Blob([str], {type: "text/plain;charset=utf-8"});
    //解决中文乱码问题
    blob =  new Blob([String.fromCharCode(0xFEFF), blob], {type: blob.type});
    object_url = window.URL.createObjectURL(blob);
    var link = document.createElement("a");
    link.href = object_url;
    link.download =  "收发类别档案.csv";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}