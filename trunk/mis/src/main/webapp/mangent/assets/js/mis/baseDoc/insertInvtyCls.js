$(function() {
	var data3 = {
		"reqHead": reqhead,
		"reqBody": {}
	};
	var postD3 = JSON.stringify(data3);
	$.ajax({
		type: "post",
		data: postD3,
		url: url + "/mis/purc/InvtyCls/selectInvtyCls",
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
									str += "<div open='true'><span class='close'></span><span class='openTrue click_span']><span class='provrClsId'>" + children[j]["invtyClsEncd"] + "</span><span class='pid'>" + ")&nbsp&nbsp" + "</span><span class='provrClsNm'>" + children[j]["invtyClsNm"] + "</span><span class='level'>" + children[j]["level"] + "</span></div>";
								} else {
									str += "<div open='false'><span class='open'></span><span class='openTrue click_span'><span class='provrClsId'>" + children[j]["invtyClsEncd"] + "</span><span class='pid'>" + ")&nbsp&nbsp" + "</span><span class='provrClsNm'>" + children[j]["invtyClsNm"] + "</span><span class='level'>" + children[j]["level"] + "</span></div>";
								}

							} else {
								str += "<div><span class='leaf'></span><span class='leafName click_span'><span class='provrClsId'>" + children[j]["invtyClsEncd"] + "</span><span class='pid'>" + ")&nbsp&nbsp" + "</span><span class='provrClsNm'>" + children[j]["invtyClsNm"] + "</span><span class='level'>" + children[j]["level"] + "</div>";
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
				$('#tree>ul>li>div').next().show()
				$('#tree>ul>li>div span').eq(0).addClass("close").removeClass("open")
				$("#" + id).show()
			}
		},
		error: function() {
			alert("获取失败")
		},
	});

	//  [
	//		0: {provrClsId: "1", ico: null, level: 1, children: Array(1), provrClsNm: "1"}
	//		1: {provrClsId: "1a", ico: null, level: 1, provrClsNm: "1a", memo: null}
	//		
	//	]
})

$(function() {
	//删除按钮
	$(".delOrder").click(function() {
		var roleid = $("#provrClsEncd").val();
		var deleteData = {
			"reqHead": reqhead,
			"reqBody": {
				"invtyClsEncd": roleid
			}
		}
		var deleteData = JSON.stringify(deleteData)
		if(roleid == null) {
			alert("请选择行")
		} else if(confirm("确定删除？")) {
			$.ajax({
				type: "post",
				url: url + "/mis/purc/InvtyCls/deleteInvtyClsByInvtyClsEncd",
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

var mType;

var isclick = true;
$(function() {
	$(".invty_saveOrder").click(function() {
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
var lev= "";
var pid = "";
//点击显示右侧数据
$(function() {
	$(document).on('click', '.click_span', function() {
		$(".addColor").removeClass("addColor")
		$(this).addClass("addColor")
		var provrClsEncd = $(this).children().first().text();
		$("#provrClsEncd").val($(this).children().first().text());  
		$("#provrClsNm").val($(this).children().first().next().next().text()); 
		$("#level").val($(this).children().last().text());  
		var level = $(this).children().last().text();
		var levelNum = parseInt(level) + 1;
		var levelString = levelNum.toString();
		lev = levelString;
		pid = provrClsEncd;
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

$(function() {
	//编辑存货分类
	$('.editOrder').click(function() {
		mType = 2;
		$(".edit").show();
		$(".edit .cancel").click(function() {
			$(".edit").hide();
		});
		$("#updateprovrClsEncd").val($("#provrClsEncd").val());
		$("#updateprovrClsNm").val($("#provrClsNm").val());
	})
})

//更新按钮保存
function addEditData() {
	var provrClsNm = $("#updateprovrClsNm").val();
	var provrClsId = $("#provrClsEncd").val();
	var Data = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyClsEncd": provrClsId,
			"invtyClsNm": provrClsNm
		}
	}
	var updateData = JSON.stringify(Data)
	$.ajax({
		type: "post",
		url: url + "/mis/purc/InvtyCls/updateInvtyClsByInvtyClsEncd",
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

function addNewData() {
	var provrClsId = $("#addprovrClsEncd").val();
	var provrClsNm = $("#addprovrClsNm").val();
	var Data = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyClsEncd": provrClsId,
			"invtyClsNm": provrClsNm,
			"level": lev,
			"pid": pid,
			"memo": "",
			"ico": ""
		}
	}
	var addData = JSON.stringify(Data)
	if (provrClsId==''&&provrClsNm=='') {
		alert('请重新输入内容')
	} else {
	$.ajax({
		type: "post",
		url: url + "/mis/purc/InvtyCls/insertInvtyCls",
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
	});
	
	}

}

//导入
$(function () {
    $(".invtyCls_importExcel").click(function () {
    	var files = $("#FileUpload").val()
    	var fileObj = document.getElementById("FileUpload").files[0];
        var formFile = new FormData();
       	formFile.append("action", "UploadVMKImagePath");  
       	formFile.append("file", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。
        var data = formFile;
        if(files != "") {
        	$.ajax({
	            type: 'post',
	            url: url + "/mis/purc/InvtyCls/uploadInvtyClsFile",
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
$(document).on('click', '.invtyCls_exportExcel', function() {
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {}
	}
	var Data = JSON.stringify(savedata);
	$.ajax({
		url: url + '/mis/purc/InvtyCls/selectInvtyCls',
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
			var myData = [];
			funa(list);
			//第一级A
			function funa(arr) {
				for(var i = 0; i < arr.length; i++) {
					if((arr[i].children != null) && (arr[i].children.length > 0)) {
						myData.push(arr[i]);
						funa(arr[i].children)
					} else {
						myData.push(arr[i]);
					}
				}
			}
			obj = myData;
			for(var i = 0; i < obj.length; i++) {
				delete obj[i].children;
			}
			daochu(obj)
		},
		error: function() {
			alert("导出失败")
		}
	})

})

function daochu(JSONData) {
    var str = '存货分类编码,存货分类名称,ico图标,级别,备注,对应父级编码\n';

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
    link.download =  "存货分类.csv";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}