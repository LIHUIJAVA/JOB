//根据存货编码查询存货详细信息
function ctrl_GetGoodsInfo(rowid, goods) {
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": goods
		}
	};
	if(goods) {
		var postData = JSON.stringify(data);
		$.ajax({
			url: url + '/mis/purc/InvtyDoc/printingInvtyDocList',
			type: 'post',
			data: postData,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			error: function() {
				alert("获取数据错误");
			}, //错误执行方法
			success: function(data) {
				var list = data.respBody.list;
				if(list.length == 0) {
					alert("无此存货,请重新输入")
					$("#jqgrids").setRowData(rowid, {
						invtyEncd: "",
						invtyNm: "",
						spcModel: "",
						bxRule: "",
						measrCorpNm: "",
						crspdBarCd: "",
						cntnTaxUprc: '',
						taxRate: '',
						measrCorpId: "",
						baoZhiQi: "",
						prcTaxSum: "",
					});
				}
				$("#" + rowid + "_invtyEncd").val(list[0].invtyEncd);
				if(list.length == 1) {
					$("#jqgrids").setRowData(rowid, {
						invtyNm: list[0].invtyNm,
						spcModel: list[0].spcModel,
						bxRule: list[0].bxRule,
						measrCorpNm: list[0].measrCorpNm,
						crspdBarCd: list[0].crspdBarCd,
						cntnTaxUprc: list[0].refSellPrc,
						taxRate: list[0].optaxRate,
						measrCorpId: list[0].measrCorpId,
						baoZhiQi: list[0].baoZhiQiDt,
						prcTaxSum: list[0].prcTaxSum,
						isQuaGuaPer:list[0].isQuaGuaPer,
					});
				} else {
					for(var i = 1; i < list.length; i++) {
						$("#jqgrids").setRowData(rowid, {
							invtyNm: list[0].invtyNm,
							spcModel: list[0].spcModel,
							bxRule: list[0].bxRule,
							measrCorpNm: list[0].measrCorpNm,
							crspdBarCd: list[0].crspdBarCd,
							cntnTaxUprc: list[0].refSellPrc,
							taxRate: list[0].optaxRate,
							measrCorpId: list[0].measrCorpId,
							baoZhiQi: list[0].baoZhiQiDt,
							prcTaxSum: list[0].prcTaxSum,
							isQuaGuaPer:list[0].isQuaGuaPer,
						});
						//设置页面数据展示
						$("#jqgrids").setRowData(++rowid, {
							invtyEncd: list[i].invtyEncd,
							invtyNm: list[i].invtyNm,
							spcModel: list[i].spcModel,
							bxRule: list[i].bxRule,
							measrCorpNm: list[i].measrCorpNm,
							crspdBarCd: list[i].crspdBarCd,
							cntnTaxUprc: list[i].refSellPrc,
							taxRate: list[i].optaxRate,
							measrCorpId: list[i].measrCorpId,
							baoZhiQi: list[i].baoZhiQiDt,
							prcTaxSum: list[i].prcTaxSum,
							isQuaGuaPer:list[0].isQuaGuaPer,
						});
					}
				}
			}
		})
	}
}


//根据存货编码查询存货详细信息
function GetGoodsInfo_can(rowid, goods) {
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": goods
		}
	};
	if(goods) {
		var postData = JSON.stringify(data);
		$.ajax({
			url: url + '/mis/purc/InvtyDoc/printingInvtyDocList',
			type: 'post',
			data: postData,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			error: function() {
				alert("获取数据错误");
			}, //错误执行方法
			success: function(data) {
				var list = data.respBody.list;
				if(list.length == 0) {
					alert("无此存货,请重新输入")
					$("#jqgrids").setRowData(rowid, {
						invtyEncd: "",
						invtyNm: "",
						spcModel: "",
						bxRule: "",
						measrCorpNm: "",
						crspdBarCd: "",
						cntnTaxUprc: '',
						taxRate: '',
						measrCorpId: "",
						baoZhiQi: "",
						prcTaxSum: "",
					});
				}
				$("input[name='invtyEncd']").val(list[0].invtyEncd);
				if(list.length == 1) {
					$("#jqgrids").setRowData(rowid, {
						invtyNm: list[0].invtyNm,
						spcModel: list[0].spcModel,
						bxRule: list[0].bxRule,
						measrCorpNm: list[0].measrCorpNm,
						crspdBarCd: list[0].crspdBarCd,
						cntnTaxUprc: list[0].refCost,
						taxRate: list[0].iptaxRate,
						measrCorpId: list[0].measrCorpId,
						baoZhiQi: list[0].baoZhiQiDt,
						prcTaxSum: list[0].prcTaxSum,
						isQuaGuaPer:list[0].isQuaGuaPer,
					});
				} else {
					for(var i = 1; i < list.length; i++) {
						$("#jqgrids").setRowData(rowid, {
							invtyNm: list[0].invtyNm,
							spcModel: list[0].spcModel,
							bxRule: list[0].bxRule,
							measrCorpNm: list[0].measrCorpNm,
							crspdBarCd: list[0].crspdBarCd,
							cntnTaxUprc: list[0].refCost,
							taxRate: list[0].iptaxRate,
							measrCorpId: list[0].measrCorpId,
							baoZhiQi: list[0].baoZhiQiDt,
							prcTaxSum: list[0].prcTaxSum,
							isQuaGuaPer:list[0].isQuaGuaPer,
						});
						//设置页面数据展示
						$("#jqgrids").setRowData(++rowid, {
							invtyEncd: list[i].invtyEncd,
							invtyNm: list[i].invtyNm,
							spcModel: list[i].spcModel,
							bxRule: list[i].bxRule,
							measrCorpNm: list[i].measrCorpNm,
							crspdBarCd: list[i].crspdBarCd,
							cntnTaxUprc: list[i].refCost,
							taxRate: list[i].iptaxRate,
							measrCorpId: list[i].measrCorpId,
							baoZhiQi: list[i].baoZhiQiDt,
							prcTaxSum: list[i].prcTaxSum,
							isQuaGuaPer:list[0].isQuaGuaPer,
						});
					}
				}
			}
		})
	}
}

//根据存货编码查询存货详细信息
function whs_GetGoodsInfo(rowid, goods) {
	var data = {
		"reqHead": reqhead,
		"reqBody": {
			"invtyEncd": goods
		}
	};
	if(goods) {
		var postData = JSON.stringify(data);
		$.ajax({
			url: url + '/mis/purc/InvtyDoc/printingInvtyDocList',
			type: 'post',
			data: postData,
			dataType: 'json',
			async: true,
			contentType: 'application/json;charset=utf-8',
			error: function() {
				alert("获取数据错误");
			}, //错误执行方法
			success: function(data) {
				var list = data.respBody.list;
				if(list.length == 0) {
					alert("无此存货,请重新输入")
					$("#jqgrids").setRowData(rowid, {
						invtyEncd: "",
						invtyNm: "",
						spcModel: "",
						bxRule: "",
						measrCorpNm: "",
						crspdBarCd: "",
						cntnTaxUprc: '',
						taxRate: '',
						measrCorpId: "",
						baoZhiQi: "",
						prcTaxSum: "",
					});
				}
				$("#" + rowid + "_invtyEncd").val(list[0].invtyEncd);
				if(list.length == 1) {
					$("#jqgrids").setRowData(rowid, {
						invtyNm: list[0].invtyNm,
						spcModel: list[0].spcModel,
						bxRule: list[0].bxRule,
						measrCorpNm: list[0].measrCorpNm,
						crspdBarCd: list[0].crspdBarCd,
						cntnTaxUprc: list[0].refSellPrc,
						taxRate: list[0].optaxRate,
						measrCorpId: list[0].measrCorpId,
						baoZhiQi: list[0].baoZhiQiDt,
						prcTaxSum: list[0].prcTaxSum,
						isQuaGuaPer:list[0].isQuaGuaPer,
//						prdcDt:"",
//						invldtnDt:"",
//						qty: '',
//						bxQty: '',
//						noTaxUprc: '',
//						noTaxAmt: '',
//						taxAmt: '',
					});
				} else {
					for(var i = 1; i < list.length; i++) {
						$("#jqgrids").setRowData(rowid, {
							invtyNm: list[0].invtyNm,
							spcModel: list[0].spcModel,
							bxRule: list[0].bxRule,
							measrCorpNm: list[0].measrCorpNm,
							crspdBarCd: list[0].crspdBarCd,
							cntnTaxUprc: list[0].refSellPrc,
							taxRate: list[0].optaxRate,
							measrCorpId: list[0].measrCorpId,
							baoZhiQi: list[0].baoZhiQiDt,
							prcTaxSum: list[0].prcTaxSum,
							isQuaGuaPer:list[0].isQuaGuaPer,
						});
						//设置页面数据展示
						$("#jqgrids").setRowData(++rowid, {
							invtyEncd: list[i].invtyEncd,
							invtyNm: list[i].invtyNm,
							spcModel: list[i].spcModel,
							bxRule: list[i].bxRule,
							measrCorpNm: list[i].measrCorpNm,
							crspdBarCd: list[i].crspdBarCd,
							cntnTaxUprc: list[i].refSellPrc,
							taxRate: list[i].optaxRate,
							measrCorpId: list[i].measrCorpId,
							baoZhiQi: list[i].baoZhiQiDt,
							prcTaxSum: list[i].prcTaxSum,
							isQuaGuaPer:list[0].isQuaGuaPer,
						});
					}
				}
			}
		})
	}
}