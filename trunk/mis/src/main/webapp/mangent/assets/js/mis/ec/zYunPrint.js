var adata;
var orderType;
var anum;
$(function() {
	$(".yunPrint").click(function() {
		var num = [];
		var grs = $("#print_wuLiu_jqgrids").jqGrid('getGridParam', 'selarrrow');
		var gr = $("#print_wuLiu_jqgrids").jqGrid('getGridParam', 'selrow');
		var rowData = $("#print_wuLiu_jqgrids").jqGrid('getRowData', gr);
		for(var l = 0; l < grs.length; l++) {
			//获得行数据
			var rowDatas = $("#print_wuLiu_jqgrids").jqGrid('getRowData', grs[l]);
			var platId = rowDatas.platId;
			num.push(platId)
		}
		if(getResault(num, rowData) == true) {
			var num1 = [];
			var grs1 = $("#print_wuLiu_jqgrids").jqGrid('getGridParam', 'selarrrow');
			for(var l = 0; l < grs1.length; l++) {
				//获得行数据
				var rowDatas = $("#print_wuLiu_jqgrids").jqGrid('getRowData', grs1[l]);
				var ordrNum = rowDatas.ordrNum;
				num1.push(ordrNum)
			}
			anum = num1.join(",");
			var data = {
				"reqHead": reqHead,
				"reqBody": {
					"logisticsNum": anum
				}
			}
			var postData = JSON.stringify(data);
			$.ajax({
				type: "post",
				url: url3 + "/mis/ec/logisticsTab/achieveECExpressCloudPrint",
				async: true,
				data: postData,
				dataType: 'json',
				contentType: 'application/json',
				success: function(getDatas) {
					if(getDatas.respHead.isSuccess == true) {
						adata = getDatas.respBody.list;
						sendCommand()
					} else {
						alert(getDatas.respHead.message)
					}
				},
				error: function(err) {
					alert("云打印数据获取失败，请检查选择的单据")
				}
			})
		}
	})
})

function getResault(num, rowData) {
	if(num.length == 1) {
		orderType = rowData.platId;
		return true;
	} else if(num.length > 1) {
		for(var i = 0; i < num.length; i++) {
			if(num[i] == "PDD") {
				orderType = "PDD";
				return true;
			} else if(num[i] == "TM") {
				orderType = "TM";
				return true;
			}
		}
	} else if(num.length == 0) {
		alert("请选择要打印的单据");
		return false;
	}
}

var socket;
var isTrusted;

function doConnect(callback) {

	if(orderType == "TM") {
		var serviceUrl = "ws://localhost:13528"; //cainiao
	} else if(orderType == "PDD") {
		var serviceUrl = "ws://localhost:5000"; //pdd
	}
	socket = new WebSocket(serviceUrl);

	// 监听消息
	socket.onmessage = function(event) {
		isTrusted = event.isTrusted;
		if(isTrusted==true){
			prints()
		}
	};

	socket.onopen = function(event) {
		if(callback != null) {
			callback();
		}
	}

	socket.onerror = function(error) {
	}

	// 监听Socket的关闭
	socket.onclose = function(event) {
	};
}

function getRandomCode(length) {
	if(length > 0) {
		var data = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"];
		var nums = "";
		for(var i = 0; i < length; i++) {
			var r = parseInt(Math.random() * 61);
			nums += data[r];
		}
		return nums;
	} else {
		return false;
	}
}

function sendCommand() {
	if(typeof socket == "undefined" || socket.readyState == WebSocket.CLOSED) {
		doConnect(sendCommand);
		return;
	}

	if(socket.readyState != WebSocket.OPEN) {
		alert("无效连接: " + socket.readyState);
		return;
	}

	if(orderType == "TM") { //cainiao
		for(var i = 0; i < adata.length; i++) {
			var newnewArry = adata[i].list;
//						newnewArry[0].templateURL = "http://cloudprint.cainiao.com/template/standard/301/210"
			var taskID = getRandomCode(3);
			var reciveData = {
				"cmd": "print",
				"requestID": "ebs7g1f6ha1z",
				"version": "1.0",
				"task": {
					"taskID": "123456",
					"preview": false,
					"printer": "",
					"notifyMode": "allInOne",
					"previewType": "pdf",
					"documents": [{
						"documentID": "0123456789",
						"contents": newnewArry
					}]
				}
			}
			var obj = reciveData;
//			console.log(reciveData)
			var time = new Date().getTime();
			if(obj["task"]) {
				obj["task"]["taskID"] = time.toString() + "_" + taskID;
			}
			var newMsg = JSON.stringify(obj);
			socket.send(newMsg);
		}

	} else if(orderType == "PDD") { //pdd
		for(var i = 0; i < adata.length; i++) {
			var taskID = getRandomCode(3);
			var newnewArry = adata[i].list;
			var reciveData = {
				"cmd": "print",
				"requestID": "123458976",
				"version": "1.0",
				"task": {
					"taskID": "7293666",
					"printer": "",
					"documents": [{
						"documentID": "9876543210",
						"contents": newnewArry
					}]
				}
			}
			var time = new Date().getTime();
			var obj = reciveData;
			if(obj["task"]) {
				obj["task"]["taskID"] = time.toString() + "_" + taskID;
			}

			var newMsg = JSON.stringify(obj);
			socket.send(newMsg);
		}
	}
}

function prints() {
	var savedata = {
		"reqHead": reqHead,
		"reqBody": {
			"logisticsNum": anum
		}
	};
	var saveData = JSON.stringify(savedata);
	$.ajax({
		type: "post",
		contentType: 'application/json; charset=utf-8',
		url: url + '/mis/ec/logisticsTab/print',
		async: true,
		data: saveData,
		dataType: 'json',
		success: function(data) {
//			console.log(data)
		}
	})
}