var action = true;// 请求结果，fasle 不执行定时器
var interval_do = null;// 页面刷新定时器
var MyMarhq = null;// 表格滚动定时器

$(function() {
	// dealData();
	// getDepList(deptList);
	getLinerList(linerList);

	var kanbanList = kanbanDataList;

	var intervaldata = interval.data;
	intervaldata = intervaldata[0].A;// 获取系统设置的刷新间隔时间

	dealData(kanbanList);
	interval_do = setInterval(getList, intervaldata * 1000); // 启动,执行默认方法

	$("#searchBtn").click(function(e) {
		if (interval_do != null) {// 判断计时器是否为空-关闭
			clearInterval(interval_do);
			interval_do = null;
		}
		getList()
		if (action) {// action 为 fasle 不调用定时器
			interval_do = setInterval(getList, intervaldata * 1000); // 重新新循环-启动
		}
	});

})
console.log(kanbanDataList);
function dealData(kanbanList) {
	if (kanbanList.result) {
		var kanbanData_t = kanbanList.data.List_line;
		var kanbanData = kanbanList.data.List_table;

		var title = kanbanList.data.Title == null ? "" : kanbanList.data.Title
		$("#title").text(title + "•效率排名看板");

		if (kanbanData.length > 0) {
			setTable(kanbanData);// 表格数据
		} else {
			$("#tableList").empty();
		}

		if (kanbanData.length > 0) {
			var done = kanbanData_t[0].QTY_DONE==null?0:parseInt(kanbanData_t[0].QTY_DONE);
			var plan = kanbanData_t[0].QTY_PLAN==null?0:parseInt(kanbanData_t[0].QTY_PLAN);
			var doneRate = kanbanData_t[0].RATE_DONE==null?0:kanbanData_t[0].RATE_DONE;
			getChart3(done, plan, doneRate);

			var hr_abn = kanbanData_t[0].HOUR_ABN == null ? 0
					: parseFloat(kanbanData_t[0].HOUR_ABN);
			var hr_act = kanbanData_t[0].HOUR_ACT == null ? 0
					: parseFloat(kanbanData_t[0].HOUR_ACT);
			var hr_st = kanbanData_t[0].HOUR_ST == null ? 0
					: parseFloat(kanbanData_t[0].HOUR_ST);
			var eff_rate = kanbanData_t[0].RATE_EFFICIENCY == null ? "0"
					: kanbanData_t[0].RATE_EFFICIENCY;

			getChart2(hr_abn, hr_act, hr_st, eff_rate);

			var liner = kanbanData_t[0].FLINER;// 组长
			var rownum = kanbanData_t[0].FROWNUM == null ? "无"
					: kanbanData_t[0].FROWNUM;// 排名
			var onlineEmp = kanbanData_t[0].NUM_EMP_ON == null ? "0"
					: kanbanData_t[0].NUM_EMP_ON;// 在线人数
			// console.log(liner)
			var card_all = kanbanData_t[0].NUM_CARD_ALL == null ? "0"
					: kanbanData_t[0].NUM_CARD_ALL;// 组长
			var card_ass = kanbanData_t[0].NUM_CARD_ASS == null ? "0"
					: kanbanData_t[0].NUM_CARD_ASS;// 排名
			var card_unass = kanbanData_t[0].NUM_CARD_UNASS == null ? "0"
					: kanbanData_t[0].NUM_CARD_UNASS;// 在线人数

			$("#liner").text(liner)
			$("#rownum").text(rownum)
			$("#onlineEmp").text(onlineEmp)
			$("#card_all").text(card_all)
			$("#card_ass").text(card_ass)
			$("#card_unass").text(card_unass)
		} else {
			toClean();
		}
	} else {
		toClean();
		$("#tableList").empty();
	}
}
function toClean() {
	getChart3(0, 0, 0);
	getChart2(0, 0, 0, 0);
	$("#liner").text("")
	$("#rownum").text("")
	$("#onlineEmp").text("")
	$("#card_all").text("")
	$("#card_ass").text("")
	$("#card_unass").text("")
}

function getChart2(hr_abn, hr_act, hr_st, eff_rate) {

	option = {
		title : {
			text : '效率:' + eff_rate + '%',
			textStyle : {
				color : '#FFFFFF' // 图例文字颜色
			},
			left : '15px',
			top : '5px'
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
			left : '15px',
			top : '5px'
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
		html += '<tr><td>' + arr.FROWNUM + '</td><td>' + arr.LINER_NAME
				+ '</td><td>' + arr.NUM_EMP_ON + '</td><td>' + arr.NUM_LINE_ON
				+ '</td><td>' + arr.HOUR_ST + '</td><td>' + arr.HOUR_ACT
				+ '</td><td>' + arr.HOUR_ABN + '</td><td>'
				+ arr.RATE_EFFICIENCY + '%</td></tr> ';
	}
	$("#tableList").empty();
	$("#tableList").append(html);
	$("#tableList1").empty();//不加此数据表头会歪
	$("#tableList1").append(html);//不加此数据表头会歪
	
	if (MyMarhq != null) {// 判断计时器是否为空-关闭
		clearInterval(MyMarhq);
		MyMarhq = null;
	}
	// clearInterval(MyMarhq);
	var item = $('.tbl-body tbody tr').length
	console.log(item)

	if (item > 4) {
		$('.tbl-body tbody').html(
				$('.tbl-body tbody').html() + $('.tbl-body tbody').html());
		$('.tbl-body').css('top', '0');
		var tblTop = 0;
		var speedhq = 60; // 数值越大越慢
		var outerHeight = $('.tbl-body tbody').find("tr").outerHeight();
		function Marqueehq() {
			if (tblTop <= -outerHeight * item) {
				tblTop = 0;
			} else {
				tblTop -= 1;
			}
			$('.tbl-body').css('top', tblTop + 'px');
		}

		MyMarhq = setInterval(Marqueehq, speedhq);
	} else {
		$('.tbl-body').css('top', '0');// 内容少时不滚动
	}
}
function getDepList(deptList) {

	var res = deptList;
	// console.log(res)
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
		if(arr.LEAD_BY==nowLiner){
			html += "<option value='" + arr.LEAD_BY + "' selected>" + arr.LEAD_BY
			+ "</option>";		
		}else{
			html += "<option value='" + arr.LEAD_BY + "'>" + arr.LEAD_BY
			+ "</option>";
		}	
	}

	$("#liner_select").append(html);
}
function getList() {
	var date = $("#date").val();
	// var sdata = date.substring(0, date.indexOf(" "))
	// var edata = date.substring(date.indexOf(" ") + 3, date.length);
	var class_no = $("#class_select").val();
	// var dep_id=$("#dep_select").val();
	var liner = $("#liner_select").val();
	var params = {
		"class_nos" : class_no,
		"dep_id" : "",
		"sdata" : date,
		"liner" : liner
	};
	$.ajax({
		type : "GET",
		url : context + "kanban/getXlpmList",
		data : params,
		dataType : "json",
		success : function(res) {
			console.log(res)
			if (res.result) {
				action = true;
				dealData(res)
			} else {
				action = false;
				clearInterval(interval_do);// 错误-关闭定时器
				alert(res.msg);
			}

		}
	});

}
