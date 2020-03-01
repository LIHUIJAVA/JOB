var count;
var pages;
var page = 1;
var rowNum;
$(function() {
	var formNum = getQueryString("formNum");
	var formTypEncd = getQueryString("formTypEncd");
	var savedata = {
		"reqHead": reqhead,
		"reqBody": {
			"formNum": formNum,
			"formTypEncd": formTypEncd
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/whs/out_into_whs/wholeSingleLianZha',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
			var list = data.respBody.list;
			if(list == null) {
				alert("该单据没有关联数据")
			} else if(list != null) {
				for(var i = 0; i < list.length; i++) {
					if(list[i].isNtChk == 0) {
						list[i].isNtChk = "否"
					} else if(list[i].isNtChk == 1) {
						list[i].isNtChk = "是"
					}
				}
				$("#jqGrids").jqGrid({
					datatype: "local",
					data: data.respBody.list,
					colNames: ['单据类型名称', '单据类型编码', '单据编码', "是否审核"], //jqGrid的列显示名字
					colModel: [{
							name: "FormTypName",
							align: "center",
							editable: true,
							sortable: false,
						},
						{
							name: "FormTypEncd",
							align: "center",
							editable: true,
							sortable: false
						},
						{
							name: "FormNum",
							align: "center",
							editable: true,
							sortable: false,
						},
						{
							name: "isNtChk",
							align: "center",
							editable: true,
							sortable: false
						}
					],
					autowidth: true,
					height: 500,
					autoScroll: true,
					shrinkToFit: false,
					rownumbers: true,
					loadonce: false,
					forceFit: true,
					rowNum: 20,
					pager: '#jqGridPager', //表格页脚的占位符(一般是div)的id
					sortname: 'id', //初始化的时候排序的字段
					sortorder: "desc", //排序方式,可选desc,asc
					viewrecords: true,
					caption: "联查单据列表", //表格的标题名字
					ondblClickRow: function(rowid) {
						order(rowid);
					},
				})
			}
		}

	})
})

function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var reg_rewrite = new RegExp("(^|/)" + name + "/([^/]*)(/|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	var q = window.location.pathname.substr(1).match(reg_rewrite);
	if(r != null) {
		return unescape(r[2]);
	} else if(q != null) {
		return unescape(q[2]);
	} else {
		return null;
	}
}

function order(rowid) {
	//获得行数据
	var rowDatas = $("#jqGrids").jqGrid('getRowData', rowid);
	localStorage.setItem("isNtChk", rowDatas.isNtChk);
	var FormTypEncd = rowDatas.FormTypEncd;
	if(FormTypEncd=="011"){//调拨单
		localStorage.setItem("formNum", rowDatas.FormNum);
		window.open("../../Components/whs/cannibSngl.html?1");
	}else if(FormTypEncd=="012"||FormTypEncd=="013"){//组装单 拆卸单
		localStorage.setItem("formNum", rowDatas.FormNum);
		window.open("../../Components/whs/ambDisamb.html?1");
	}else if(FormTypEncd=="014"||FormTypEncd=="015"){//其他入 其他出
		localStorage.setItem("formNum", rowDatas.FormNum);
		window.open("../../Components/whs/otherOutIntoWhs.html?1");
	}else if(FormTypEncd=="028"){//盘点单
		localStorage.setItem("checkFormNum", rowDatas.FormNum);
		window.open("../../Components/whs/checkSngl.html?1");
	}else if(FormTypEncd=="029"){//损益单
		localStorage.setItem("checkFormNum", rowDatas.FormNum);
		window.open("../../Components/whs/checkSnglLoss.html?1");
	}
}