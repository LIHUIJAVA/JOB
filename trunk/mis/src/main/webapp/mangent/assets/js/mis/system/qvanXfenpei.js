$(function() {
	var data3 = {
		"reqHead": reqhead,
		"reqBody": {
			"roleId": 'root',
		}
	};
	var postD3 = JSON.stringify(data3);
	$.ajax({
		type: "post",
		data: postD3,
		url: url + "/mis/system/misUser/userTree",
		async: true,
		dataType: 'json',
		contentType: "application/json; charset=utf-8",
		success: function(data) {
			var obj = data.respBody.tree;
			var arr1 = Array.from(obj)
			paintingTree(arr1, "tree")
		},
		error: function() {
			alert("获取失败")
		},
	});
})

$(function() {
	$("#allCheck").change(function() {
		var isCheckAll = $("#allCheck:checked").val();
		if(isCheckAll == 1) {
			$(".seleYes .seleYes2 input").prop("checked", true);
		} else {
			$(".seleYes .seleYes2 input").prop("checked", false);
		}
	})
	$("#allCheck1").change(function() {
		var isCheckAll = $("#allCheck1:checked").val();
		if(isCheckAll == 1) {
			$(".seleNo .seleNo2 input").prop("checked", true);
		} else {
			$(".seleNo .seleNo2 input").prop("checked", false);
		}
	})
})

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
						str += "<div open='true'><span class='close'></span><span class='openTrue click_span']><span class='provrClsId'>" + children[j]["id"] + "</span><span class='pid'>" + ")&nbsp&nbsp" + "</span><span class='provrClsNm'>" + children[j]["name"] + "</span></div>";
					} else {
						str += "<div open='false'><span class='open'></span><span class='openTrue click_span'><span class='provrClsId'>" + children[j]["id"] + "</span><span class='pid'>" + ")&nbsp&nbsp" + "</span><span class='provrClsNm'>" + children[j]["name"] + "</span></div>";
					}

				} else {
					str += "<div><span class='leaf'></span><span class='leafName click_span'><span class='provrClsId'>" + children[j]["id"] + "</span><span class='pid'>" + ")&nbsp&nbsp" + "</span><span class='provrClsNm'>" + children[j]["name"] + "</div>";
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
	$("#" + id).show()
}

//点击变更确认修改权限
$(document).on('click', '.click_span', function() {
	//点击改变颜
	$(".addColor").removeClass("addColor")
	$(this).addClass("addColor")
	bb = $(this)['context'].firstElementChild.innerText;

	//二级菜单变更
	if($(this)[0].className = 'openTrue click_span addColor') {
		$('.confirm').click(function() {
			var menuIds = [];
			//拿到选中状态的    id
			var data = $('input:checked');
			let menuIds2;
			if(menuIds == '') {
				menuIds2 = ""
			}
			for(var i = 0; i < data.length; i++) {
				menuIds.push(data[i].id);
				menuIds2 = menuIds.toString();
			}
			var up = {
				reqhead,
				"reqBody": {
					"roleId": bb,
					"menuId": menuIds2
				}
			};
			var update = JSON.stringify(up);
			$.ajax({
				type: "post",
				async: false,
				contentType: 'text/josn;charset=utf-8',
				dataType: 'json',
				data: update,
				url: url + "/mis/system/role/permAss",
				success: function(data) {
					alert("权限变更成功")
					window.location.reload();
				},
				error: function() {
					alert("变更失败")
				}
			})
			return false;
		})
	} else if($(this)[0].className = 'leafName click_span addColor') {
		//三级菜单变更
		$('.confirm').click(function() {
			var menuIds = [];
			//拿到选中状态的    id
			var data = $('input:checked');
			let menuIds2;
			if(menuIds == '') {
				menuIds2 = ""
			}
			for(var i = 0; i < data.length; i++) {
				menuIds.push(data[i].id);
				menuIds2 = menuIds.toString();
			}
			var up = {
				reqhead,
				"reqBody": {
					"userId": bb,
					"menuId": menuIds2
				}
			};
			var update = JSON.stringify(up);
			$.ajax({
				type: "post",
				async: false,
				contentType: 'text/josn;charset=utf-8',
				dataType: 'json',
				data: update,
				url: url + "/mis/system/misUser/permAss",
				success: function(data) {
					alert("权限变更成功")
					window.location.reload();
				},
				error: function() {
					alert("变更失败");
				}
			})

			return false;
		})
	}
})

//点击菜单显示右侧数据
$(function() {
	var aa = '';
	$(document).on('click', '.click_span', function() {
		//点击改变颜色
		$(".addColor").removeClass("addColor");
		$(this).addClass("addColor");
		aa = $(this)['context'].firstElementChild.innerText;

		//点击三级用户菜单
		$('span[class="leafName click_span addColor"]').click(function(e) {
			$('.seleNo2').html('')
			$('.seleYes2').html('')
			var Data = {
				reqhead,
				"reqBody": {
					"userId": aa,

				}
			}
			var addData = JSON.stringify(Data)
			$.ajax({
				type: "post",
				url: url + "/mis/system/menu/userMenuList",
				async: true,
				data: addData,
				dataType: 'json',
				contentType: 'application/json; charset=utf-8',
				success: function(data) {
					let result = data.respBody;
					let seleyes = result.userMenuList; //无权限
					let seleno = result.noUserMenuList; //有权限的数据
					let html11 = '';
					let html22 = "";
					for(var i = 0; i < seleyes.length; i++) {
						html11 += `
									<div class="">
										<input type="checkbox" checked id="${seleyes[i].id}" />
										<span>${seleyes[i].name}</span>
									</div>`;
					}
					$('.seleYes2').html(html11);

					for(var j = 0; j < seleno.length; j++) {

						html22 += `
									<div class="">
										<input type="checkbox" id="${seleno[j].id}" />
										<span>${seleno[j].name}</span>
									</div>`;

					}
					$('.seleNo2').html(html22);

				},
				error: function() {
					alert("增加失败")
				}
			});
		})

		//点击二级角色菜单
		$('span[class="openTrue click_span addColor"]').click(function(e) {
			//			e.stopPropagation();
			var Data = {
				reqhead,
				"reqBody": {
					"roleId": aa,

				}
			}
			var addData = JSON.stringify(Data)

			$.ajax({
				type: "post",
				url: url + "/mis/system/menu/roleMenuList",
				async: true,
				data: addData,
				dataType: 'json',
				contentType: 'application/json; charset=utf-8',
				success: function(data) {
					//						alert(data.respHead.message);

					let result = data.respBody;
					let seleyes = result.roleMenuList; //无权限
					let seleno = result.noRoleMenuList; //有权限的数据
					let html = "";
					var html2 = "";
					for(var i = 0; i < seleyes.length; i++) {

						html += `
									<div class="">
										<input type="checkbox" checked id="${seleyes[i].id}" />
										<span>${seleyes[i].name}</span>
									</div>`;

					}

					$('.seleYes2').html(html);

					for(var j = 0; j < seleno.length; j++) {

						html2 += `
									<div class="">
										<input type="checkbox" id="${seleno[j].id}" />
										<span>${seleno[j].name}</span>
									</div>`;

					}
					$('.seleNo2').html(html2);
					//						window.location.reload(true);

				},
				error: function() {
					alert("增加失败")
				}
			});
		})
	});
})