$(function() {
	// dealData();
	getDepList(deptList);
	getLinerList(linerList);
	var kanbanList = kanbanDataList;
	dealData(kanbanList);
	$("#searchBtn").click(function() {
		getList();
	});

})
console.log(kanbanDataList);
function dealData(kanbanList) {
	if (kanbanList.result) {
		var kanbanData_t = kanbanList.data.List_line;
		var kanbanData = kanbanList.data.List_table;
		if (kanbanData_t.length>0) {
			var done = parseInt(kanbanData_t[0].QTY_DONE);
			var plan = parseInt(kanbanData_t[0].QTY_PLAN);
			var doneRate = kanbanData_t[0].RATE_DONE;
			getChart3(done, plan, doneRate);

			var hr_abn = kanbanData_t[0].HOUR_ABN == null ? 0
					: parseFloat(kanbanData_t[0].HOUR_ABN);
			var hr_act = kanbanData_t[0].HOUR_ACT == null ? 0
					: parseFloat(kanbanData_t[0].HOUR_ACT);
			var hr_st = kanbanData_t[0].HOUR_ST == null ? 0
					: parseFloat(kanbanData_t[0].HOUR_ST);
			var eff_rate = kanbanData_t[0].RATE_EFFICIENCY == null ? "0"
					: kanbanData_t[0].RATE_EFF;

			getChart2(hr_abn, hr_act, hr_st, eff_rate);

			var classNo = kanbanData_t[0].CLASS_NO;//
			// var
			// rownum=kanbanData_t[0].FROWNUM==null?"无":kanbanData_t[0].FROWNUM;//排名
			var onlineEmp = kanbanData_t[0].NUM_EMP_ON == null ? "0"
					: kanbanData_t[0].NUM_EMP_ON;// 在线人数

			$("#classNo").text(classNo)
			// $("#rownum").text(rownum)
			$("#onlineEmp").text(onlineEmp)
		}
		if (kanbanData.length>0) {
			setTable(kanbanData);// 表格数据
		}
	}
}

function getChart2(hr_abn, hr_act, hr_st, eff_rate) {

	option = {
		title : {
			text : '效率:' + eff_rate + '%',
			textStyle : {
				color : '#FFFFFF' // 图例文字颜色
			},
			x : 'left'
		},
		color : [ '#993300', '#0066FF', '#66CCCC' ],
		legend : {
			data : [ '异常工时', '标准工时', '实际工时' ],
			textStyle : {
				color : '#FFFFFF' // 图例文字颜色
			},
			orient : 'vertical',
			x : 'right',
			y : 'top',
		},
		grid : {
			left : '3%',
			//right : '4%',
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
			data : [ hr_abn ],
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
function getChart3(done, plan, doneRate) {
	option = {
		title : {
			text : '完工率:' + doneRate + '%',
			textStyle : {
				color : '#FFFFFF' // 图例文字颜色
			},
			x : 'left'
		},
		color : [ '#0066FF', '#66CCCC' ],
		legend : {
			data : [ '计划产量', '完工产量' ],
			textStyle : {
				color : '#FFFFFF' // 图例文字颜色
			},
			x : 'right',
			y : 'top',
			orient : 'vertical',
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
		html += '<tr><td>' + arr.TASK_NO + '</td><td>' + arr.ITEM_NAME
				+ '</td><td>' + arr.PROD_DATE + '</td><td>' + arr.CLASS_NO
				+ '</td><td>' + arr.QTY_PLAN + '</td><td>' + arr.QTY_DONE
				+ '</td><td>' + arr.MANPOWER + '</td><td>' + arr.CAPACITY
				+ '</td><td>' + arr.HOUR_ST + '</td><td>' + arr.HOUR_ACT
				+ '%</td></tr> ';
	}
	$("#tableList").empty();
	$("#tableList").append(html);
}
function getDepList(deptList) {
	var res = deptList
	$("#dep_select").empty();
	var html = "<option value=''>请选择部门</option>";
	for (j = 0, len = res.data.length; j < len; j++) {
		var arr = res.data[j];
		html += "<option value='" + arr.ID + "'>" + arr.ORG_NAME + "</option>";
	}
	$("#dep_select").append(html);
}
function getLinerList(linerList) {

	var res = linerList;
	$("#liner_select").empty();
	var html = "<option value=''>请选择组长</option>";
	for (j = 0, len = res.data.length; j < len; j++) {
		var arr = res.data[j];
		html += "<option value='" + arr.LEAD_BY + "'>" + arr.LEAD_BY
				+ "</option>";
	}

	$("#liner_select").append(html);
}
function getList() {
	// var date = $("#date").val();
	// var sdata = date.substring(0, date.indexOf(" "))
	// var edata = date.substring(date.indexOf(" ") + 3, date.length);
	// console.log(sdata)
	// console.log(edata)
	var class_no = $("#class_select").val();
	// var dep_id=$("#dep_select").val();
	var liner = $("#liner_select").val();
	var params = {
		"class_nos" : class_no,
		"dep_id" : "",
		"sdata" : "",
		"liner" : liner
	};
	$.ajax({
		type : "GET",
		url : context + "kanban/getCxdzList",
		data : params,
		dataType : "json",
		success : function(res) {
			console.log(res)
			if (res.result) {
				dealData(res)
			} else {
				alert(res.msg);
			}
		}
	});

}
