//左列树的渲染
$(function() {
	var data3 = {
		"reqHead": reqhead,
		"reqBody": {}
	};
	var postD3 = JSON.stringify(data3);
	$.ajax({
		type: "post",
		data: postD3,
		url: url + "/mis/purc/CustCls/selectCustCls",
		async: true,
		dataType: 'json',
		contentType: "application/json; charset=utf-8",
		success: function(data) {
			var obj = data.respBody.list;
			var arr1 = Array.from(obj)
			paintingTree(arr1, "tree")

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
									str += "<div open='true'><span class='close'></span><span class='openTrue click_span']><span class='provrClsId'>" + children[j]["clsId"] + "</span><span class='pid'>" + ")&nbsp&nbsp" + "</span><span class='provrClsNm'>" + children[j]["clsNm"] + "</span><span class='level'>" + children[j]["level"] + "</span></div>";
								} else {
									str += "<div open='false'><span class='open'></span><span class='openTrue click_span'><span class='provrClsId'>" + children[j]["clsId"] + "</span><span class='pid'>" + ")&nbsp&nbsp" + "</span><span class='provrClsNm'>" + children[j]["clsNm"] + "</span><span class='level'>" + children[j]["level"] + "</span></div>";
								}

							} else {
								str += "<div><span class='leaf'></span><span class='leafName click_span'><span class='provrClsId'>" + children[j]["clsId"] + "</span><span class='pid'>" + ")&nbsp&nbsp" + "</span><span class='provrClsNm'>" + children[j]["clsNm"] + "</span><span class='level'>" + children[j]["level"] + "</div>";
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
				$('#tree>ul>li>div span').parent().next().show()
				$('#tree>ul>li>div span').eq(0).addClass("close").removeClass("open")
				$("#" + id).show()
			}
		},
		error: function() {
			alert("加载失败")
		},
	});

})

$(function() {
	//删除按钮
	$(".delOrder").click(function() {
		if($("#provrClsEncd").val() == 0) {
			alert("请选择")
		} else {
			var roleid = $("#provrClsEncd").val();
			var deleteData = {
				"reqHead": reqhead,
				"reqBody": {
					"clsId": roleid
				}
			}
			var deleteData = JSON.stringify(deleteData)
			if(roleid == null) {
				alert("请选择行")
			} else if(confirm("确定删除？")) {
				$.ajax({
					type: "post",
					url: url + "/mis/purc/CustCls/deleteCustClsByClsId",
					async: true,
					data: deleteData,
					dataType: 'json',
					contentType: 'application/json; charset=utf-8',
					success: function(remover) {
						if(remover.respHead.isSuccess == true) {
							window.location.reload()
						}
					},
					error: function() {
						alert("删除失败")
					}
				});
			}
		}
	})

})

//点击显示右侧数据
var level, levelNum, levelString, provrClsEncd

$(function() {
	$(document).on('click', '.click_span', function() {
		$(".addColor").removeClass("addColor")
		$(this).addClass("addColor")
		provrClsEncd = $(this).children().first().text();
		$("#provrClsEncd").val($(this).children().first().text());  
		$("#provrClsNm").val($(this).children().first().next().next().text()); 
		$("#level").val($(this).children().last().text());  
		level = $(this).children().last().text();
		levelNum = parseInt(level) + 1;
		levelString = levelNum.toString();

	});
})

//增加客户分类
$(function() {
	$('.addOrder').click(function() {
		$(".dialog").show();
		$(".dialog .cancel").click(function() {
			$(".dialog").hide();
		})
	})
})

$(function() {
	//保存按钮保存
	$(".saveOrder").click(function() {
		var provrClsId = $("#addprovrClsEncd").val();
		var provrClsNm = $("#addprovrClsNm").val();
		if(provrClsEncd == undefined) {
			alert("请选择客户分类")
		} else {
			var Data = {
				"reqHead": reqhead,
				"reqBody": {
					"clsId": provrClsId,
					"clsNm": provrClsNm,
					"level": levelString,
					"pid": provrClsEncd,
					"memo": "",
					"ico": ""
				}
			}
			var addData = JSON.stringify(Data)
			$.ajax({
				type: "post",
				url: url + "/mis/purc/CustCls/insertCustCls",
				async: true,
				data: addData,
				dataType: 'json',
				contentType: 'application/json; charset=utf-8',
				success: function(data) {
					alert(data.respHead.message);
					if(data.respHead.isSuccess == true) {
						window.location.reload()
					}
				},
				error: function() {
					alert("增加失败")
				}
			});
		}
	})
})

$(function() {
	//编辑客户分类
	$('.editOrder').click(function() {
		if($("#provrClsEncd").val() == 0 || $("#provrClsEncd").val() == "") {
			alert("请选择客户分类")
		} else {
			$(".edit").show();
			$("#updateprovrClsEncd").val($("#provrClsEncd").val())
			$("#updateprovrClsNm").val($("#provrClsNm").val())

			//更新按钮保存
			$(".update").click(function() {
				var provrClsNm = $("#updateprovrClsNm").val();
				var provrClsId = $("#provrClsEncd").val();
				var Data = {
					"reqHead": reqhead,
					"reqBody": {
						"clsId": provrClsId,
						"clsNm": provrClsNm
					}
				}
				var updateData = JSON.stringify(Data)
				$.ajax({
					type: "post",
					url: url + "/mis/purc/CustCls/updateCustClsByClsId",
					async: true,
					data: updateData,
					dataType: 'json',
					contentType: 'application/json; charset=utf-8',
					success: function(data) {
						if(data.respHead.isSuccess == true) {
							alert(data.respHead.message);
							window.location.reload()
							$(".edit").hide()
						}
					},
					error: function() {
						alert("编辑失败")
					}
				});

			})
		}
		$(".edit .cancel").click(function() {
			$(".edit").hide();
		})
	})
})

//导入
$(function() {
	$(".importExcel").click(function() {
		var files = $("#FileUpload").val()
		var fileObj = document.getElementById("FileUpload").files[0];
		var formFile = new FormData();
		formFile.append("action", "UploadVMKImagePath");
		formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
		var data = formFile;
		if(files != "") {
			$.ajax({
				type: 'post',
				url: url + "/mis/purc/CustCls/uploadCustClsFile",
				data: data,
				dataType: "json",
				cache: false, //上传文件无需缓存
				processData: false, //用于对data参数进行序列化处理 这里必须false
				contentType: false, //必须
				success: function(data) {
					alert(data.respHead.message);
					window.location.reload()
				}
			});
		} else {
			alert("请选择文件")
		}
	});
});


//导出
$(document).on('click', '.exportExcel', function() {
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {}
	}
	var Data = JSON.stringify(savedata);
	$.ajax({
		url: url + '/mis/purc/CustCls/selectCustCls',
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
			var arr = [];
			var obj = {}
			var list = data.respBody.list[0].children;
			obj = list;
			daochu(obj)
		},
		error: function() {
			alert("导出失败")
		}
	})

})

function daochu(JSONData) {
    var str = '客户分类编码,ico图标,级别,备注,对应父级编码,客户分类名称\n';

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
    link.download =  "客户分类.csv";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}