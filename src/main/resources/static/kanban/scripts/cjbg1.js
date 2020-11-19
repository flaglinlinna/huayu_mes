$(function() {
	var kanbanList=kanbanDataList;
	dealData(kanbanList);
	//getDepList(deptList);
	$("#searchBtn").click(function() {
		getList();
		
	});
})

function dealData(kanbanList) {
	console.log(kanbanList)
	
		var kanbanData = kanbanList.data.List;
		var xAxis = [];
		var series1 = [];
		var series2 = [];
		var series3 = [];
		for (var i = 0; i < kanbanData.length - 1; i++) {
			xAxis.push(kanbanData[i].LINER_NAME);
			series1.push(kanbanData[i].HOUR_ST);// 标准工时
			series2.push(kanbanData[i].HOUR_ACT);// 实际工时
			series3.push(kanbanData[i].EFFICIENCY_RATE);// 生产效率
		}
		// console.log(kanbanList.data.LineNum);
		$("#showLine").text("总开线数：" + kanbanList.data.LineNum);
		chartDiv(xAxis, series1, series2, series3);
		setTable(kanbanData);
	
}
function chartDiv(xAxis_data, series1_data, series2_data, series3_data) {
	option = {
		tooltip : {
			trigger : 'axis',
			axisPointer : {
				type : 'cross',
				crossStyle : {
					color : '#999'
				}
			}
		},
		grid : {
			x : 70,// 左边距
			y : 20,// 上边距
			x2 : 60,// 右边距
			y2 : 50,// 下边距
			borderWidth : 10
		},
		legend : {
			x : 'center',
			y : 'bottom',
			data : [ '标准工时', '实际工时', '生产效率' ],
			textStyle : {
				fontSize : 12,// 字体大小
				color : '#ffffff'// 字体颜色
			},
		},
		color : [ '#66FFCC', '#6699FF', '#9966FF' ],
		xAxis : [ {
			type : 'category',
			data : xAxis_data,
			axisPointer : {
				type : 'shadow'
			},
			axisLabel : {
				show : true,
				textStyle : {
					color : '#ffffff'
				}
			}
		} ],
		yAxis : [ {
			type : 'value',
			splitLine : {
				show : false
			},
			axisLabel : {
				formatter : '{value}小时',
				textStyle : {
					color : '#ffffff'
				}
			},
		}, {
			type : 'value',
			splitLine : {
				show : false
			},
			axisLabel : {
				formatter : '{value}%',
				textStyle : {
					color : '#ffffff'
				}
			}
		} ],
		series : [ {
			name : '标准工时',
			type : 'bar',
			data : series1_data,
			label : {
				show : true,
				position : 'top'
			},
		}, {
			name : '实际工时',
			type : 'bar',
			data : series2_data,
			label : {
				show : true,
				position : 'top'
			},
		}, {
			name : '生产效率',
			type : 'line',
			yAxisIndex : 1,
			data : series3_data,
			label : {
				show : true,
				position : 'top',
				formatter: '{c}%'
			},
		} ]
	};
	// 创建echarts对象在哪个节点上
	var myCharts1 = echarts.init(document.getElementById('echart1'));
	// 将选项对象赋值给echarts对象。
	myCharts1.setOption(option, true);
}

function setTable(kanbanData) {
	var html = "";
	for (var j = 0; j < kanbanData.length; j++) {
		var arr = kanbanData[j];
		html += '<tr><td>' + arr.LINER_NAME + '</td><td>' + arr.HOUR_ST
				+ '</td><td>' + arr.HOUR_ACT + '</td><td>' + arr.HOUR_ABN
				+ '</td><td>' + arr.NUM_EMP_ON + '</td><td>'
				+ arr.EFFICIENCY_RATE + '%</td></tr> ';
	}
	$("#tableList").empty();
	$("#tableList").append(html);
}
function getDepList(deptList) {
	var res=deptList;
	console.log(res)
	$("#dep_select").empty();
	var html = "<option value=''>请选择部门</option>";
	for (j = 0, len = res.data.length; j < len; j++) {
		var arr = res.data[j];
		html += "<option value='" + arr.ID + "'>" + arr.ORG_NAME
				+ "</option>";
	}

	$("#dep_select").append(html);
}
function getList() {
	var date = $("#date").val();
	//var sdata = date.substring(0, date.indexOf(" "))
//	var edata = date.substring(date.indexOf(" ") + 3, date.length);
	// console.log(sdata)
	// console.log(edata)
	var class_no=$("#class_select").val();
	//var dep_id=$("#dep_select").val();
	var params = {
		"class_nos" : class_no,
		"dep_id" : "",
		"sdata" : date
	};
	$.ajax({
		type : "GET",
		url : context + "kanban/getCjbgList",
		data : params,
		dataType : "json",
		success : function(res) {
			console.log(res)
			if (res.result) {
				dealData(res)
			}else{
           	 alert(res.msg);
            }

		}
	});

}
