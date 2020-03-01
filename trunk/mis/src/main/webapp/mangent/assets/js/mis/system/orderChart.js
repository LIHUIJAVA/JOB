
var counts=[];
var moneys=[];
var dates=[];
$(function() {
	var edit = {
		"reqHead": reqhead,
		"reqBody": {}
	}
	var editJson = JSON.stringify(edit);
	$.ajax({
		type: "post",
		url: url + "/mis/ec/platOrder/near15DaysOrder",
		async: true,
		data: editJson,
		dataType: 'json',
		contentType: 'application/json',
		success: function(data) {
			var list = data.respBody.list;
			for(var i = 0;i<list.length;i++){
				counts.push(list[i].count)
			}
			for(var i = 0;i<list.length;i++){
				moneys.push(list[i].money)
			}
			for(var i = 0;i<list.length;i++){
				dates.push(list[i].date)
			}
			
			 myChart.setOption({
		        xAxis: {
		            data: dates
		        },
		        series: [{
		            // 根据名字对应到相应的系列
		            name: '数量',
		            data: counts
		        },
		        {
		            // 根据名字对应到相应的系列
		            name: '金额',
		            data: moneys
		        }]
		    });
			
		},
		error: function(err) {
			alert("详细信息查询失败")
		},
	})
	var dom = document.getElementById("container");
	var myChart = echarts.init(dom);
	var app = {};
	option = null;
	option = {
		title: {
			text: '订单金额与数量统计',
//			subtext: '纯属虚构'
		},
		tooltip: {
			trigger: 'axis'
		},
		legend: {
			data: ['金额', '数量']
		},
		toolbox: {
			show: true,
			feature: {
				dataView: {
					show: true,
					readOnly: false
				},
				magicType: {
					show: true,
					type: ['line', 'bar']
				},
				restore: {
					show: true
				},
				saveAsImage: {
					show: true
				}
			}
		},
		calculable: true,
		xAxis: [{
			type: 'category',
			data: []
		}],
		yAxis: [{
			type: 'value'
		}],
		series: [{
				name: '金额',
				type: 'bar',
				data: counts,
				markPoint: {
					data: [{
							type: 'max',
							name: '最大值'
						},
						{
							type: 'min',
							name: '最小值'
						}
					]
				},
				markLine: {
					data: [{
						type: 'average',
						name: '平均值'
					}]
				}
			},
			{
				name: '数量',
				type: 'bar',
				data: [],
				markPoint: {
					data: [{
							type: 'max',
							name: '最大值'
						},
						{
							type: 'min',
							name: '最小值'
						}
					]
				},
				markLine: {
					data: [{
						type: 'average',
						name: '平均值'
					}]
				}
			}
		]
	};;
	if(option && typeof option === "object") {
		myChart.setOption(option, true);
	}
})