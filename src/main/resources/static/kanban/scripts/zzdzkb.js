var action = true;// 请求结果，fasle 不执行定时器
var interval_do = null;// 页面刷新定时器

//var action_task = true;// 请求结果，fasle 不执行定时器 2020-12-03废除制令单刷新
//var interval_do_task = null;// 制令单刷新定时器

var MyMarhq_cxdz = null;// 表格滚动定时器1
var MyMarhq = null;// 表格滚动定时器2

var taskNo="";//制令单参数
$(function() {
	// getDepList(deptList);
	getLinerList(linerList);

	var intervaldata = interval.data;
	intervaldata = intervaldata[0].A;// 获取系统设置的刷新间隔时间-制令单切换
	
	 var rotationdata = rotation.data;
	 rotationdata = rotationdata[0].R;// 获取系统设置的刷新间隔时间 -页面更新
	 
	dealXlpmData(xlpm_data);
	dealCxdzData(cxdz_data);
	dealCxscData(cxsc_data);
	//interval_do_task = setInterval(getTaskNoList, intervaldata * 1000); // 启动-制令单切换
	interval_do = setInterval(getList, rotationdata * 1000); // 启动,执行默认方法

	$("#searchBtn").click(function(e) {
		if (interval_do != null) {// 判断计时器是否为空-关闭
			clearInterval(interval_do);
			interval_do = null;
		}
		//if (interval_do_task != null) {// 判断计时器是否为空-关闭
		//	clearInterval(interval_do_task);
		//	interval_do_task = null;
		//}
		getList();		
		if (action) {// action 为 fasle 不调用定时器
			interval_do = setInterval(getList, rotationdata * 1000); // 重新新循环-启动
		}
		//if(action_task){//2020-12-03废除
		//	interval_do_task = setInterval(getTaskNoList, intervaldata * 1000); // 重新新循环-启动
		//}
	});

})

function dealXlpmData(kanbanList) {
	
	console.log(kanbanList)
	if (kanbanList.result) {
		var kanbanData_t = kanbanList.data.List_line;
		var title = kanbanList.data.Title == null ? "" : kanbanList.data.Title
		$("#title").text(title + "•"+nowLiner+"线电子看板");		
		
		if (kanbanData_t.length > 0) {
			//图片路径
			$("#name1").text(kanbanData_t[0].NAM_PE);
			if(kanbanData_t[0].IMG_PE!=null){
				$("#img1").attr('src',"../base/employee/viewByUrl?empImg="+kanbanData_t[0].IMG_PE);
			}
			$("#job1").text(kanbanData_t[0].POS_PE);
			
			$("#name2").text(kanbanData_t[0].NAM_QC);
			if(kanbanData_t[0].IMG_QC!=null){
				$("#img2").attr('src',"../base/employee/viewByUrl?empImg="+kanbanData_t[0].IMG_QC);
			}
			$("#job2").text(kanbanData_t[0].POS_QC);
			
			$("#name3").text(kanbanData_t[0].NAME_LINER);
			if(kanbanData_t[0].IMG_LINER!=null){
				$("#img3").attr('src',"../base/employee/viewByUrl?empImg="+kanbanData_t[0].IMG_LINER);
			}
			$("#job3").text(kanbanData_t[0].POS_LINER);
			
			var done = kanbanData_t[0].QTY_DONE==null?0:parseInt(kanbanData_t[0].QTY_DONE);
			var plan = kanbanData_t[0].QTY_PLAN==null?0:parseInt(kanbanData_t[0].QTY_PLAN);
			var doneRate = kanbanData_t[0].RATE_DONE==null?0:kanbanData_t[0].RATE_DONE;
			getChartXlpm3(done, plan, doneRate);

			var hr_abn = kanbanData_t[0].HOUR_ABN == null ? 0
					: parseFloat(kanbanData_t[0].HOUR_ABN);
			var hr_act = kanbanData_t[0].HOUR_ACT == null ? 0
					: parseFloat(kanbanData_t[0].HOUR_ACT);
			var hr_st = kanbanData_t[0].HOUR_ST == null ? 0
					: parseFloat(kanbanData_t[0].HOUR_ST);
			var eff_rate = kanbanData_t[0].RATE_EFFICIENCY == null ? "0"
					: kanbanData_t[0].RATE_EFFICIENCY;
			getChartXlpm2(hr_abn, hr_act, hr_st, eff_rate);

			var liner = kanbanData_t[0].FLINER;// 组长
			var rownum = kanbanData_t[0].FROWNUM == null ? "无"
					: kanbanData_t[0].FROWNUM;// 排名
			var onlineEmp = kanbanData_t[0].NUM_EMP_ON == null ? "0"
					: kanbanData_t[0].NUM_EMP_ON;// 在线人数
			var card_all = kanbanData_t[0].NUM_CARD_ALL == null ? "0"
					: kanbanData_t[0].NUM_CARD_ALL;// 组长
			var card_ass = kanbanData_t[0].NUM_CARD_ASS == null ? "0"
					: kanbanData_t[0].NUM_CARD_ASS;// 排名
			var card_unass = kanbanData_t[0].NUM_CARD_UNASS == null ? "0"
					: kanbanData_t[0].NUM_CARD_UNASS;// 在线人数
			$("#liner").text(liner)
			$("#rownum").text(rownum)
			if(kanbanData_t[0].FCOLOR=="FROWNUM:RED"){
				$("#rownum").addClass("redword");
			}else{
				$("#rownum").removeClass("redword");
			}
			$("#onlineEmp").text(onlineEmp)
			$("#card_all").text(card_all)
			$("#card_ass").text(card_ass)
			$("#card_unass").text(card_unass)
		} else {
			toCleanXlpm();
		}
	} else {
		toCleanXlpm();
	}
}

function dealCxdzData(kanbanList){
	console.log(kanbanList)
	if(!kanbanList.result){//报错时的初始化
		$("#tableCxdzList").empty();
		return false;
	}
	var kanbanData = kanbanList.data.List_table;
	if (kanbanData.length > 0) {//无数据时要初始化
		setCxdzTable(kanbanData);// 表格数据
	}else{
		$("#tableCxdzList").empty();
	}
}

function dealCxscData(kanbanList){
	console.log(kanbanList)
	if (!kanbanList.result) {// 报错时的初始化
		$("#tableCxscList").empty();
		return false;
	}
	//var kanbanData_t = kanbanList.data.List_result1;
	//taskNo = kanbanData_t[0].TASK_NO;
	
	var kanbanData = kanbanList.data.List_result;
	if (kanbanData.length > 0) {
		setCxscTable(kanbanData);// 表格数据
	} else {
		$("#tableCxscList").empty();
	}
}

function toCleanXlpm() {
	getChartXlpm3(0, 0, 0);
	getChartXlpm2(0, 0, 0, 0);
	$("#liner").text("")
	$("#rownum").text("")
	$("#onlineEmp").text("")
	$("#card_all").text("")
	$("#card_ass").text("")
	$("#card_unass").text("")
}
function getChartXlpm2(hr_abn, hr_act, hr_st, eff_rate) {
	option = {
		title : {
			text : '效率:' + eff_rate + '%',
			textStyle : {
				color : '#FFFFFF' ,// 图例文字颜色
				fontSize : 17,// 字体大小
			},
			left : '5px',
			top : '5px'
		},
		color : [ '#993300', '#0066FF', '#66CCCC' ],
		legend : {
			data : [ '异常工时', '标准工时', '实际工时' ],
			textStyle : {
				color : '#FFFFFF', // 图例文字颜色
				fontSize : 13,// 字体大小
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
					color : '#ffffff',
					fontSize : 13,// 字体大小
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
					color : '#FFFFFF',
					fontSize : 13,// 字体大小
				}
			},
		},
		series : [ {
			name : '异常工时',
			type : 'bar',
			data : [ hr_abn ],
			label : {
				show : true,
				position : 'insideLeft',
				fontSize : 13,// 字体大小
				color : '#FFFFFF',
			},
		}, {
			name : '实际工时',
			type : 'bar',
			data : [ hr_act ],
			label : {
				show : true,
				position : 'insideLeft',
				fontSize : 13,// 字体大小
				color : '#FFFFFF',
			},
		}, {
			name : '标准工时',
			type : 'bar',
			data : [ hr_st ],
			label : {
				show : true,
				position : 'insideLeft',
				fontSize : 13,// 字体大小
				color : '#FFFFFF',
			},
		} ]
	};
	var myCharts1 = echarts.init(document.getElementById('echart2'));
	myCharts1.setOption(option, true);
}
function getChartXlpm3(done, plan, doneRate) {
	option = {
		title : {
			text : '完工率:' + doneRate + '%',
			textStyle : {
				color : '#FFFFFF', // 图例文字颜色
				fontSize : 17,// 字体大小
			},
			left : '5px',
			top : '5px'
		},
		color : [ '#0066FF', '#66CCCC' ],
		legend : {
			data : [ '计划产量', '完工产量' ],
			textStyle : {
				color : '#FFFFFF', // 图例文字颜色
				fontSize : 13,// 字体大小
			},
			x : 'right',
			y : 'top',
			orient : 'vertical',
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
					color : '#ffffff',
					fontSize : 13,// 字体大小
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
					color : '#FFFFFF',
				}
			},
			axisLabel : {
				show : true,
				textStyle : { // 其余属性默认使用全局文本样式，详见TEXTSTYLE
					color : '#FFFFFF',
					fontSize : 13,// 字体大小
				}
			},
		},
		series : [ {
			name : '完工产量',
			type : 'bar',
			data : [ done ],
			label : {
				show : true,
				position : 'insideLeft',
				color : '#FFFFFF',
				fontSize : 13,// 字体大小
			},
		}, {
			name : '计划产量',
			type : 'bar',
			data : [ plan ],
			label : {
				show : true,
				position : 'insideLeft',
				color : '#FFFFFF',
				fontSize : 13,// 字体大小
			},
		} ]
	};
	var myCharts1 = echarts.init(document.getElementById('echart3'));
	myCharts1.setOption(option, true);
}
//中层的产线电子表格
function setCxdzTable(kanbanData) {
	var html = "";
	for (var j = 0; j < kanbanData.length; j++) {
		var arr = kanbanData[j];
	/*	html += '<tr><td>' + arr.TASK_NO + '</td><td>' + arr.ITEM_NAME+ '</td><td>' + arr.ITEM_NAME1
				+ '</td><td>' + arr.PROD_DATE + '</td><td>' + arr.CLASS_NO
				+ '</td><td>' + arr.QTY_PLAN + '</td><td>' + arr.QTY_DONE+ '</td><td>' + arr.QTY_OK
				+ '</td><td>' + arr.MANPOWER + '</td><td>' + arr.CAPACITY
				+ '</td><td>' + arr.HOUR_ST + '</td><td>' + arr.HOUR_ACT
				+ '</td><td>' + arr.HOUR_ABN +'</td><td>' + arr.RATE_OK + '%</td><td>' + arr.RATE_DONE
				+ '%</td><td>' + arr.RATE_EFF + '%</td></tr> ';
	*/
		html += '<tr><td>' + arr.TASK_NO + '</td><td>' + arr.ITEM_NAME+ '</td><td>' + arr.ITEM_NAME1
		+ '</td><td>' + arr.QTY_PLAN+ '</td><td>' + arr.QTY_MAIN 
		+ '</td><td>' + arr.QTY_DONE+ '</td><td>' + arr.QTY_OK
		+ '</td><td>' + arr.QTY_ITEM_NG
		+ '</td><td>' + arr.MANPOWER + '</td><td>' + arr.CAPACITY
		+ '</td><td>' + arr.HOUR_ST + '</td><td>' + arr.HOUR_ACT
		+ '</td><td>' + arr.HOUR_ABN +'</td><td>' + arr.RATE_OK + '%</td><td>' + arr.RATE_DONE
		+ '%</td><td>' + arr.RATE_EFF + '%</td></tr> ';

	}
	$("#tableCxdzList").empty();
	$("#tableCxdzList").append(html);
	$("#tableCxdzList1").empty();//不加此数据表头会歪
	$("#tableCxdzList1").append(html);//不加此数据表头会歪
	
	if (MyMarhq_cxdz != null) {// 判断计时器是否为空-关闭
		clearInterval(MyMarhq_cxdz);
		MyMarhq_cxdz = null;
	}
	// clearInterval(MyMarhq);
	var item = $('.tbl-body-cxdz tbody tr').length
	//console.log(item)
	if (item > 2) {
		$('.tbl-body-cxdz tbody').html(
				$('.tbl-body-cxdz tbody').html() + $('.tbl-body-cxdz tbody').html());
		$('.tbl-body-cxdz').css('top', '0');
		var tblTop = 0;
		var speedhq = 60; // 数值越大越慢
		var outerHeight = $('.tbl-body-cxdz tbody').find("tr").outerHeight();
		function Marqueehq() {
			if (tblTop <= -outerHeight * item) {
				tblTop = 0;
			} else {
				tblTop -= 1;
			}
			$('.tbl-body-cxdz').css('top', tblTop + 'px');
		}
		MyMarhq_cxdz = setInterval(Marqueehq, speedhq);
	} else {
		$('.tbl-body-cxdz').css('top', '0');// 内容少时不滚动
	}
}
//下层的产线生产表格
function setCxscTable(kanbanData){
	var html = "";
	for (var j = 0; j < kanbanData.length; j++) {
		var arr = kanbanData[j];
		html += '<tr><td>' + arr.FTIME + '</td><td>' + arr.QTY_NPT
				+ '</td><td>' + arr.QTY_DONE + '</td><td>' + arr.QTY_OK
				+ '</td><td>' + arr.QTY_NG + '</td></tr> ';
		if(j==kanbanData.length-1){
			html+='<tr height=25></tr>'
		}
	}
	$("#tableCxscList").empty();
	$("#tableCxscList").append(html);
	$("#tableCxscList1").empty();//不加此数据表头会歪
	$("#tableCxscList1").append(html);//不加此数据表头会歪
	
	if (MyMarhq != null) {// 判断计时器是否为空-关闭
		clearInterval(MyMarhq);
		MyMarhq = null;
	}
	// clearInterval(MyMarhq);
	var item = $('.tbl-body tbody tr').length
	console.log(item)

	if (item > 7) {
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

//基础数据-部门、组长的下拉框
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
//刷新制令单
function getTaskNoList() {
	var task = "";
	var deptId = "";
	var liner = nowLiner;
	var interval = "1";
	var params = {
		"taskNo" : task,
		"deptId" : deptId,
		"liner" : liner,
		"interval" : interval
	};
	$.ajax({
		type : "GET",
		url : context + "kanban/getCxscList2",
		data : params,
		dataType : "json",
		success : function(res) {
			//console.log(res)
			if (res.result) {
				//action_task = true;
				dealCxscData(res);
			} else {
				//action = false;
				//clearInterval(interval_do_task);// 错误-关闭定时器
				alert(res.msg)
			}
		}
	});
}
//刷新整个页面的数据
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
	console.log(params)
	$.ajax({
		type : "GET",
		url : context + "kanban/toZzdzkbList",
		data : params,
		dataType : "json",
		success : function(res) {
			console.log(res)
			if (res.result) {
				var data=res.data;
				action = true;
				dealXlpmData(data.xlpm_data);
				dealCxdzData(data.cxdz_data);
				getTaskNoList();
			} else {
				action = false;
				clearInterval(interval_do);// 错误-关闭定时器
				alert(res.msg);
			}
		}
	});
}

