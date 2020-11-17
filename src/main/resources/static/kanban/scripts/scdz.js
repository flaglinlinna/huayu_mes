$(function() {
	var kanbanList=kanbanDataList
	getDepList(deptList)
	dealData(kanbanList);
		$("#searchBtn").click(function(){
			getList();
		});
})
console.log(kanbanDataList);
function dealData(kanbanList) {
	console.log(kanbanList)
	if(kanbanList.data!=null){
		var kanbanData = kanbanList.data.List;
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
		chartDiv(xAxis, series1, series2, series3);
		setTable(kanbanData);//表格数据
		var emp_plan = parseInt(kanbanList.data.EMP_NUM_PLN);
		var emp_now = parseInt(kanbanList.data.EMP_NUM_NOW);
		var emp_off = parseInt(kanbanList.data.PO_NUM_EMP_OFF);
		getChart2(emp_plan, emp_now, emp_off);
		var done = parseInt(kanbanList.data.PRD_NUM_DONE);
		var plan = parseInt(kanbanList.data.PRD_NUM_PLN);
		var doneRate = kanbanList.data.PRD_RATE_DONE;
		getChart3(done, plan, doneRate);
		$("#showLine").text(
				"开线数：" + kanbanList.data.LINE_NUM_NOW + "     " + "总线体数："
						+ kanbanList.data.LINE_NUM_PLN);
	}
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
			x : 50,// 左边距
			y : 20,// 上边距
			x2 : 50,// 右边距
			y2 : 50,// 下边距
			borderWidth : 10
		},
		legend : {
			x : 'center',
			y : 'bottom',
			data : [ '计划产量', '达成产量', '完工率' ],
			textStyle : {
				fontSize : 12,// 字体大小
				color : '#ffffff'// 字体颜色
			},
		},
		color : [  '#6699FF', '#66FFCC','#9966FF' ],
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
				formatter : '{value} ',
				textStyle : {
					color : '#ffffff',
					fontSize : 10,// 字体大小
				}
			},
		}, {
			type : 'value',
			splitLine : {
				show : false
			},
			axisLabel : {
				formatter : '{value} %',
				textStyle : {
					color : '#ffffff',
					fontSize : 10,// 字体大小
				}
			}
		} ],
		series : [ {
			name : '计划产量',
			type : 'bar',
			data : series1_data,
			label : {
				show : true,
				position : 'top'
			},
		}, {
			name : '达成产量',
			type : 'bar',
			data : series2_data,
			label : {
				show : true,
				position : 'top'
			},
		}, {
			name : '完工率',
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

function getChart2(emp_plan, emp_now, emp_off) {
	/*option = {
		title : {
			text : '应到人数：' + emp_plan,
			textStyle : {
				color : '#FFFFFF', // 图例文字颜色
				//fontSize:'28px'
			},
			x : 'left',
			y : 'bottom',
		},
		tooltip : {
			trigger : 'item',
			formatter : '{a} <br/>{b} : {c} ({d}%)'
		},
		color : [ '#6699FF', '#66FFCC' ],
		series : [ {
			name : '出勤信息',
			type : 'pie',
			radius : '55%',
			center : [ '45%', '50%' ],
			label : {
				formatter : '{hr|}\n  {b|{b}：}{c}  {per|{d}%}  ',
				// backgroundColor: '#eee',
				borderColor : '#aaa',
				borderWidth : 1,
				borderRadius : 4,
				rich : {
					a : {
						color : '#999',
						lineHeight : 20,
						align : 'center'
					},
					hr : {
						borderColor : '#000066',
						width : '90%',
						borderWidth : 0.5,
						height : 0
					},
					b : {
						fontSize : 13,
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
				name : '缺勤人数',
				selected : true
			}, {
				value : emp_now,
				name : '在线人数'
			} ],
			emphasis : {
				itemStyle : {
					shadowBlur : 10,
					shadowOffsetX : 0,
					shadowColor : 'rgba(0, 0, 0, 0.5)'
				}
			}
		} ]
	};*/
	// 指定图表的配置项和数据
    var option = {
        
        color:['#6699FF', '#66FFCC'],
        title:{
            text:emp_plan,
            left:"center",
            top:"50%",
            textStyle:{
                color:"#ffffff",
                fontSize:36,
                align:"center"
            }
        },
        graphic:{
            type:"text",
            left:"center",
            top:"40%",
            style:{
                text:"应到人数",
                textAlign:"center",
                fill:"#ffffff",
                fontSize:20,
                fontWeight:700
            }
        },
        series: [
            {
                name: '出勤',
                type: 'pie',
                radius: ['60%', '70%'],
                avoidLabelOverlap: false,
                label : {
    				formatter : '{hr|}\n  {b|{b}：}{c} ',
    				// backgroundColor: '#eee',
    				borderColor : '#aaa',
    				borderWidth : 1,
    				borderRadius : 4,
    				rich : {
    					a : {
    						color : '#999',
    						lineHeight : 20,
    						align : 'center'
    					},
    					hr : {
    						borderColor : '#000066',
    						width : '90%',
    						borderWidth : 0.5,
    						height : 0
    					},
    					b : {
    						fontSize : 13,
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
                data: [
                    { value: emp_off, name: '缺勤人数' },
                    { value: emp_now, name: '在线人数' },
                   
                ]
            }
        ]
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
				color : '#FFFFFF' ,// 图例文字颜色
				
			},
			left:'15px',
		},
		color : [ '#0066FF', '#66CCCC' ],
		legend : {
			data : [ '计划产量', '完工产量' ],
			textStyle : {
				color : '#FFFFFF' // 图例文字颜色
			},
			orient: 'vertical',
			x : 'right',
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
		html += '<tr><td>' + arr.LINER_NAME + '</td><td>' + arr.NUM_EMP_ON
				+ '</td><td>' + arr.NUM_EMP_PL + '</td><td>' + arr.QTY_PLAN
				+ '</td><td>' + arr.QTY_DONE + '</td><td>' + arr.RATE_DONE
				+ '%</td></tr> ';
	}
	$("#tableList").empty();
	$("#tableList").append(html);
}
function getDepList(deptList){
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
function getList(){
	var class_no=$("#class_select").val();
	var dep_id=$("#dep_select").val();
	var date=$("#date").val();
	var params = {
			"class_nos":class_no,
			"dep_id":dep_id,
			"sdata":date
		};
	$.ajax({
         type: "GET",
         url: context+"kanban/getScdzList",
         data: params,
         dataType: "json",
         success: function(res){
             console.log(res)
             if(res.result){
            	 dealData(res)
             }else{
            	 alert(res.msg);
             }
          }
     });
}
