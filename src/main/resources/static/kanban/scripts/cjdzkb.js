var action = true;// 请求结果，fasle 不执行定时器
var interval_do = null;// 页面刷新定时器

$(function() {

	// getDepList(deptList)

	var intervaldata = interval.data;
	intervaldata = intervaldata[0].A;// 获取系统设置的刷新间隔时间
	getServerTime();
	dealScdzData(scdz_data);
	dealCjbgData(cjbg_data);
	interval_do = setInterval(getList, intervaldata * 1000); // 启动,执行默认方法

	$("#searchBtn").click(function() {
		if (interval_do != null) {// 判断计时器是否为空-关闭
			clearInterval(interval_do);
			interval_do = null;
		}
		getList();
		if (action) {// action 为 fasle 不调用定时器
			interval_do = setInterval(getList, intervaldata * 1000); // 重新新循环-启动
		}
	});
})

function dealScdzData(kanbanList) {
	console.log(kanbanList)
	if (kanbanList.data != null) {
		var kanbanData = kanbanList.data.List;
		var title = kanbanList.data.Title == null ? "" : kanbanList.data.Title
		$("#title").text(title + "•车间电子看板");
		if (kanbanData.length > 0) {
			var xAxis = [];
			var series1 = [];
			var series2 = [];
			var series3 = [];
			for (var i = 0; i < kanbanData.length; i++) {
				xAxis.push(kanbanData[i].LINER_NAME);
				series1.push(kanbanData[i].QTY_PLAN);// 计划数量
				series2.push(kanbanData[i].QTY_DONE);// 完成数量
				series3.push(kanbanData[i].RATE_DONE);// 完工率
			}
			chartScdzDiv(xAxis, series1, series2, series3);
		} else {
			chartScdzDiv([], 0, 0, 0);
		}
		var emp_plan = parseInt(kanbanList.data.EMP_NUM_PLN);
		var emp_now = parseInt(kanbanList.data.EMP_NUM_NOW);
		var emp_off = parseInt(kanbanList.data.PO_NUM_EMP_OFF);
		getChart2(emp_plan, emp_now, emp_off);

		var done = parseInt(kanbanList.data.PRD_NUM_DONE);
		var plan = parseInt(kanbanList.data.PRD_NUM_PLN);
		var doneRate = kanbanList.data.PRD_RATE_DONE;
		
		$("#done").text(done);
		$("#plan").text(plan);
		$("#done_rate").text(doneRate+"%");
		//getChart3(done, plan, doneRate);
		

		$("#showLine").text("开线数：" + kanbanList.data.LINE_NUM_NOW);
		$("#showLine1").text("总线体数：" + kanbanList.data.LINE_NUM_PLN);

	} else {
		getChart2(0, 0, 0)
		getChart3(0, 0, 0);
		$("#showLine").text("开线数：0");
		$("#showLine1").text("总线体数：0");
	}
}
function dealCjbgData(kanbanList) {
	console.log(kanbanList)
	if (!kanbanList.result) {// 报错时的初始化
		toClean();
		
		return false;
	}
	var kanbanData = kanbanList.data.List;
	if (kanbanData.length > 0) {
		var xAxis = [];
		var series1 = [];
		var series2 = [];
		var series3 = [];
		for (var i = 0; i < kanbanData.length ; i++) {
			if(kanbanData[i].LINER_NAME=="总体"){
				$("#stdtime").text(kanbanData[i].HOUR_ST);
				$("#facttime").text(kanbanData[i].HOUR_ACT);
				$("#prd_eff").text(kanbanData[i].EFFICIENCY_RATE+"%");
				continue;
			}
			xAxis.push(kanbanData[i].LINER_NAME+"\n"+"第"+kanbanData[i].FROWNUM+"名");
			series1.push(kanbanData[i].HOUR_ST);// 标准工时
			series2.push(kanbanData[i].HOUR_ACT);// 实际工时
			series3.push(kanbanData[i].EFFICIENCY_RATE);// 生产效率
		}
		$("#showLine").text("总开线数：" + kanbanList.data.LineNum);
		chartCjbgDiv(xAxis, series1, series2, series3);
	} else {
		toClean();
	}
}

function toClean() {
	$("#showLine").text("总开线数：0");
	chartCjbgDiv([], 0, 0, 0);
}

function getServerTime() {
	CoreUtil.sendAjax("/sys/report", "",
		function(data) {
				if (data.result) {
					console.log(data);
				}
		}, "GET", false, function(res) {
			layer.alert(res.msg);
		});
}

function chartScdzDiv(xAxis_data, series1_data, series2_data, series3_data) {
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
			x : 80,// 左边距
			y : 60,// 上边距
			x2 : 80,// 右边距
			y2 : 50,// 下边距
			borderWidth : 10
		},
		legend : {
			//orient: 'vertical',
	        x:'center',      //可设定图例在左、右、居中
	        top:15,   
			data : [ '计划产量', '达成产量', '完工率' ],
			textStyle : {
				fontSize : fontSize(0.24),// 字体大小
				color : '#ffffff'// 字体颜色
			},
		},
		color : [ '#6699FF', '#66FFCC', '#9966FF' ],
		xAxis : [ {
			type : 'category',
			data : xAxis_data,
			triggerEvent: true,//横坐标点击事件
			axisPointer : {
				type : 'shadow'
			},
			axisLabel : {
				show : true,
				textStyle : {
					color : '#ffffff',
					fontSize:fontSize(0.23),
				}
			},axisLine : {
				lineStyle : {
					color : '#FFFFFF'
				}
			},
		} ],
		yAxis : [ {
			type : 'value',
			name:'(个)',
			nameTextStyle:{
				 fontSize :fontSize(0.23)
			 },
			splitLine : {
				show : false
			},
			axisLabel : {
				//formatter : '{value} ',
				textStyle : {
					color : '#ffffff',
					fontSize : fontSize(0.23),// 字体大小
				}
			},axisLine : {
				lineStyle : {
					color : '#FFFFFF'
				}
			},
		}, {
			type : 'value',
			name:'(%)',
			nameTextStyle:{
				 fontSize :fontSize(0.23)
			 },
			fontSize : fontSize(0.23),// 字体大小
			splitLine : {
				show : false
			},
			axisLabel : {
				//formatter : '{value} %',
				textStyle : {
					color : '#ffffff',
					fontSize : fontSize(0.23),// 字体大小
				}
			},axisLine : {
				lineStyle : {
					color : '#FFFFFF'
				}
			},
		} ],
		series : [ {
			name : '计划产量',
			type : 'bar',
			data : series1_data,
			label : {
				show : true,
				position : 'top',
				textStyle : {
					fontSize : fontSize(0.24),// 字体大小
				}
			},
		}, {
			name : '达成产量',
			type : 'bar',
			data : series2_data,
			label : {
				show : true,
				position : 'top',
				textStyle : {
					fontSize : fontSize(0.24),// 字体大小
				}
			},
		}, {
			name : '完工率',
			type : 'line',
			yAxisIndex : 1,
			data : series3_data,
			label : {
				show : true,
				position : 'top',
				formatter : '{c}%',
				textStyle : {
					fontSize : fontSize(0.24),// 字体大小
				}
			},
		} ]
	};
	// 创建echarts对象在哪个节点上
	var myCharts1 = echarts.init(document.getElementById('echart_scdz'));
	// 将选项对象赋值给echarts对象。
	myCharts1.setOption(option, true);
	myCharts1.on('click', function (params) {	
		if(params.componentType=="xAxis"){			
			var liner=params.value
			var url="toZzdzkb?inType=apk&liner="+liner;
			window.open(url);
		}
	});
}

function getChart2(emp_plan, emp_now, emp_off) {
	// 指定图表的配置项和数据
	var option = {
		color : [ '#6699FF', '#66FFCC' ],
		title : {
			text : emp_plan + "\n" + "￣￣",// 手动增加下划线
			link : 'toScdzDetail?liner=&fieldword=PO_EMP_NUM_PLN',// 主标题文本超链接,默认值true
			target : 'blank',// 指定窗口打开主标题超链接，'self' | 'blank'，不指定为'blank'
			left : "center",
			top : "50%",
			textStyle : {
				color : "#ffffff",
				fontSize : fontSize(0.43),
				align : "center"
			},
		},
		graphic : {
			type : "text",
			left : "center",
			top : "40%",
			style : {
				text : "应到人数",
				textAlign : "center",
				fill : "#ffffff",
				fontSize : fontSize(0.31),
				fontWeight : 700
			}
		},
		series : [ {
			name : '出勤',
			type : 'pie',
			radius : [ '60%', '75%' ],
			center: ['50%', '50%'],
			avoidLabelOverlap : false,
			label : {
				formatter : '{hr|}\n  {b|{b}：}{c} ',
				fontSize : fontSize(0.22),
				//borderColor : '#aaa',
				//borderWidth : 1,
				//borderRadius : 4,
				rich : {
					a : {
						color : '#999',
						lineHeight : 20,
						align : 'center'
					},
					hr : {
						borderColor : '#000066',
						width : '90%',
						//borderWidth : 0.5,
						height : 0
					},
					b : {
						fontSize : fontSize(0.22),
						lineHeight : 30
					},
					per : {
						color : '#eee',
						backgroundColor : '#334455',
						padding : [ 2, 3 ],
						borderRadius : 2
					}
				}
			},
			data : [ {
				value : emp_off,
				name : '缺勤人数'
			}, {
				value : emp_now,
				name : '在线人数',
				url : "toScdzDetail?liner=&fieldword=PO_EMP_NUM_NOW"
			},
			]
		} ]
	};

	// 创建echarts对象在哪个节点上
	var myCharts1 = echarts.init(document.getElementById('echart2'));
	// 将选项对象赋值给echarts对象。
	myCharts1.setOption(option, true);
	// 饼图点击跳转到指定页面
	myCharts1.on('click', function(param) {
		window.open(param.data.url);
	});
}

function chartCjbgDiv(xAxis_data, series1_data, series2_data, series3_data) {
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
			x : 80,// 左边距
			y : 60,// 上边距
			x2 : 80,// 右边距
			y2 : 50,// 下边距
			borderWidth : 1
		},
		legend : {
			//orient: 'vertical',
	        x:'center',      //可设定图例在左、右、居中
	        top:15,   
			data : [ '标准工时', '实际工时', '生产效率' ],
			textStyle : {
				fontSize : fontSize(0.24),// 字体大小
				color : '#ffffff'// 字体颜色
			},
		},
		// color : [ '#66FFCC', '#6699FF', '#9966FF' ],
		color : [ '#CCCCFF', '#FFCC99', '#99FFCC' ],
		xAxis : [ {
			type : 'category',
			data : xAxis_data,
			triggerEvent: true,//横坐标点击事件
			axisPointer : {
				type : 'shadow'
			},
			axisLabel : {
				show : true,
				textStyle : {
					color : '#ffffff',
					fontSize:fontSize(0.23)
				}
			},
			axisLine : {
				lineStyle : {
					color : '#FFFFFF'
				}
			},
		} ],
		yAxis : [ {
			type : 'value',
			name:'(小时)',
			nameTextStyle:{
				 fontSize :fontSize(0.23)
			 },
			splitLine : {
				show : false
			},
			axisLabel : {
				formatter : '{value}',
				textStyle : {
					color : '#ffffff',
					fontSize:fontSize(0.23)
				}
			},axisLine : {
				lineStyle : {
					color : '#FFFFFF'
				}
			},
		}, {
			type : 'value',
			name:'(%)',
			nameTextStyle:{
				 fontSize :fontSize(0.23)
			 },
			splitLine : {
				show : false
			},
			axisLabel : {
				formatter : '{value}',
				textStyle : {
					color : '#ffffff',
					fontSize:fontSize(0.23)
				}
			},axisLine : {
				lineStyle : {
					color : '#FFFFFF'
				}
			},
		} ],
		series : [ {
			name : '标准工时',
			type : 'bar',
			data : series1_data,
			label : {
				show : true,
				position : 'top',
				textStyle : {
					fontSize : fontSize(0.24),// 字体大小
				}
			},
		}, {
			name : '实际工时',
			type : 'bar',
			data : series2_data,
			label : {
				show : true,
				position : 'top',
				textStyle : {
					fontSize : fontSize(0.24),// 字体大小
				}
			},
		}, {
			name : '生产效率',
			type : 'line',
			yAxisIndex : 1,
			data : series3_data,
			itemStyle:{
	            normal:{
	                color:function(params){
	                    var d=params.value;
	                    if(d<100){
	                        return '#fe4365';
	                    }else{
	                    	return '#99FFCC';
	                    }
	                },
	                lineStyle: {
						color: '#99FFCC' //改变折线颜色
					}
		
	            }
	        },
			label : {
				show : true,
				position : 'top',
				formatter : '{c}%',
				textStyle : {
					fontSize : fontSize(0.24),// 字体大小
				}	
			},
		} ]
	};
	// 创建echarts对象在哪个节点上
	var myCharts1 = echarts.init(document.getElementById('echart_cjbg'));
	// 将选项对象赋值给echarts对象。
	myCharts1.setOption(option, true);
	myCharts1.on('click', function (params) {	
		if(params.componentType=="xAxis"){			
			var liner=params.value
			liner=liner.substring(0,liner.indexOf("\n"))
			var url="toZzdzkb?inType=apk&liner="+liner;
			window.open(url);
		}
	});
}
//此echart暂时未使用
function getChart3(done, plan, doneRate) {
	option = {
		title : {
			text : '完工率:' + doneRate + '%',
			textStyle : {
				color : '#FFFFFF',// 图例文字颜色
			},
			left : '15px',
			top:'10px'
		},
		color : [ '#0066FF', '#66CCCC' ],
		legend : {
			data : [ '计划产量', '完工产量' ],
			textStyle : {
				color : '#FFFFFF' // 图例文字颜色
			},
			orient : 'vertical',
			x : 'right',
			y : 'top',
		},
		grid : {
			//left : '3%',
			// right : '4%',
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

//获取屏幕宽度并计算比例-设置echarts文字
function fontSize(res){
	  var docEl = document.documentElement,
	  clientWidth = window.innerWidth||document.documentElement.clientWidth||document.body.clientWidth;
	  if (!clientWidth) return;
	  var fontSize = 100 * (clientWidth / 2880);//原：1920/2400/2880
	  return res*fontSize;
}

function getDepList(deptList) {
	var res = deptList;
	console.log(res)
	$("#dep_select").empty();
	var html = "<option value=''>请选择部门</option>";
	for (j = 0, len = res.data.length; j < len; j++) {
		var arr = res.data[j];
		html += "<option value='" + arr.ID + "'>" + arr.ORG_NAME + "</option>";
	}
	$("#dep_select").append(html);
}
function getList() {
	var class_no = $("#class_select").val();
	// var dep_id=$("#dep_select").val();
	var date = $("#date").val();
	var params = {
		"class_nos" : class_no,
		"dep_id" : "",
		"sdata" : date
	};
	$.ajax({
		type : "GET",
		url : context + "kanban/toCjdzkbList",
		data : params,
		dataType : "json",
		success : function(res) {
			// console.log(res)
			if (res.result) {
				var data=res.data;
				action = true;
				dealScdzData(data.scdz_data);
				dealCjbgData(data.cjbg_data);
			} else {
				action = false;
				clearInterval(interval_do);// 错误-关闭定时器
				alert(res.msg);
			}
		}
	});
}
