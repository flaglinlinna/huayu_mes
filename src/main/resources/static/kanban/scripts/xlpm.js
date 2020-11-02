$(function() {
	dealData();
})
console.log(kanbanDataList);
function dealData() {
	var kanbanData = kanbanDataList.data.List_table;
	setTable(kanbanData);//表格数据
	
	var kanbanData_t=kanbanDataList.data.List_line;
	
	
	var done = parseInt(kanbanData_t[0].QTY_DONE);
	var plan = parseInt(kanbanData_t[0].QTY_PLAN);
	var doneRate=kanbanData_t[0].RATE_DONE;
	getChart3(done, plan,doneRate);
	
	var hr_abn = kanbanData_t[0].HOUR_ABN==null?0:parseFloat(kanbanData_t[0].HOUR_ABN);
	var hr_act = kanbanData_t[0].HOUR_ACT==null?0:parseFloat(kanbanData_t[0].HOUR_ACT);
	var hr_st = kanbanData_t[0].HOUR_ST==null?0:parseFloat(kanbanData_t[0].HOUR_ST);
	var eff_rate=kanbanData_t[0].RATE_EFFICIENCY==null?"0":kanbanData_t[0].RATE_EFFICIENCY;
	
	getChart2(hr_abn, hr_act,hr_st,eff_rate);
	
	/*	
	$("#showLine").text(
			"开线数：" + kanbanDataList.data.LINE_NUM_NOW + "     " + "总线体数："
					+ kanbanDataList.data.LINE_NUM_PLN);
	*/
	
	
}

function getChart2(hr_abn, hr_act,hr_st,eff_rate) {
	
	option = {
			title : {
				text : '效率:' + eff_rate + '%',
				textStyle : {
					color : '#FFFFFF' // 图例文字颜色
				},
				x : 'right'
			},
			color : [ '#993300','#0066FF', '#66CCCC' ],
			legend : {
				data : [ '异常工时', '标准工时', '实际工时'],
				textStyle : {
					color : '#FFFFFF' // 图例文字颜色
				},
				x : 'left',
				y : 'top',
			},
			grid : {
				left : '3%',
				right : '4%',
				bottom : '3%',
				containLabel : true
			},
			xAxis : {
				type : 'value',
				boundaryGap : [ 0, 0.01 ],
				splitLine : {
					show : false
				}, // 去除网格线
				axisPointer : {
					type : 'shadow'
				},
				axisLabel : {
					show : true,
					textStyle : {
						color : '#ffffff'
					}
				},
				axisLine : {
					lineStyle : {
						color : '#FFFFFF',
					}
				},
			},
			yAxis : {
				type : 'category',
				data : [ '' ],
				// splitLine:{show: false}, //去除网格线
				axisLine : {
					lineStyle : {
						color : '#FFFFFF'
					}
				},
				axisLabel : {
					show : true,
					textStyle : { // 其余属性默认使用全局文本样式，详见TEXTSTYLE
						color : '#FFFFFF'
					}
				},
			},
			series : [ {
				name : '异常工时',
				type : 'bar',
				data : [  hr_abn ],
				label : {
					show : true,
					position : 'right'
				},
			}, {
				name : '实际工时',
				type : 'bar',
				data : [ hr_act ],
				label : {
					show : true,
					position : 'right'
				},
			}, {
				name : '标准工时',
				type : 'bar',
				data : [ hr_st ],
				label : {
					show : true,
					position : 'right'
				},
			} ]
		};

	// 创建echarts对象在哪个节点上
	var myCharts1 = echarts.init(document.getElementById('echart2'));
	// 将选项对象赋值给echarts对象。
	myCharts1.setOption(option, true);
}
function getChart3(done, plan,doneRate) {
	option = {
		title : {
			text : '完工率:' + doneRate + '%',
			textStyle : {
				color : '#FFFFFF' // 图例文字颜色
			},
			x : 'right'
		},
		color : [ '#0066FF', '#66CCCC' ],
		legend : {
			data : [ '计划产量', '完工产量' ],
			textStyle : {
				color : '#FFFFFF' // 图例文字颜色
			},
			x : 'left',
			y : 'top',
		},
		grid : {
			left : '3%',
			right : '4%',
			bottom : '3%',
			containLabel : true
		},
		xAxis : {
			type : 'value',
			boundaryGap : [ 0, 0.01 ],
			splitLine : {
				show : false
			}, // 去除网格线
			axisPointer : {
				type : 'shadow'
			},
			axisLabel : {
				show : true,
				textStyle : {
					color : '#ffffff'
				}
			},
			axisLine : {
				lineStyle : {
					color : '#FFFFFF',
				}
			},
		},
		yAxis : {
			type : 'category',
			data : [ '' ],
			// splitLine:{show: false}, //去除网格线
			axisLine : {
				lineStyle : {
					color : '#FFFFFF'
				}
			},
			axisLabel : {
				show : true,
				textStyle : { // 其余属性默认使用全局文本样式，详见TEXTSTYLE
					color : '#FFFFFF'
				}
			},
		},
		series : [ {
			name : '完工产量',
			type : 'bar',
			data : [ done ],
			label : {
				show : true,
				position : 'right'
			},
		}, {
			name : '计划产量',
			type : 'bar',
			data : [ plan ],
			label : {
				show : true,
				position : 'right'
			},
		} ]
	};

	// 创建echarts对象在哪个节点上
	var myCharts1 = echarts.init(document.getElementById('echart3'));
	// 将选项对象赋值给echarts对象。
	myCharts1.setOption(option, true);
}

function setTable(kanbanData) {
	var html = "";
	for (var j = 0; j < kanbanData.length; j++) {
		var arr = kanbanData[j];
		html += '<tr><td>' + arr.FROWNUM + '</td><td>' + arr.LINER_NAME
				+ '</td><td>' + arr.NUM_EMP_ON + '</td><td>' + arr.NUM_LINE_ON
				+ '</td><td>' + arr.HOUR_ST + '</td><td>' + arr.HOUR_ACT
				+ '</td><td>' + arr.HOUR_ABN + '</td><td>' + arr.RATE_EFFICIENCY
				+ '%</td></tr> ';
	}
	$("#tableList").empty();
	$("#tableList").append(html);
}
