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
var recvSendCateId;
var recvSendCateNm;
//条件查询供应商档案
$(document).on('click', '.click_span', function() {
	//点击改变颜色
	$(".addColor").removeClass("addColor");
	$(this).addClass("addColor");
	recvSendCateId = $(this).children().first().text().toString();
	recvSendCateNm = $(this).children().first().next().next().text().toString();
})

$(document).on('click', '.sure', function() {
	if(recvSendCateId == undefined) {
		alert("未选择")
	} else  {
		window.parent.opener.document.getElementById("recvSendCateId").value = recvSendCateId;
		window.parent.opener.document.getElementById("recvSendCateNm").value = recvSendCateNm;
		window.close()
	}
})
$(document).on('click', '.cancel', function() {
	window.parent.opener.document.getElementById("recvSendCateId").value = "";
	window.parent.opener.document.getElementById("recvSendCateNm").value = "";
	window.close()
})