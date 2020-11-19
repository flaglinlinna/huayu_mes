var action = true;
var interval_do = null;// 定时器
$(function() {
	getDepList(deptList);
	getLinerList(linerList);
	var kanbanList = kanbanDataList;

	var intervaldata = interval.data;
	intervaldata = intervaldata[0].A;// 获取系统设置的刷新间隔时间

	dealData(kanbanList);
	interval_do = setInterval(getList, intervaldata * 1000); // 启动,执行默认参数

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

console.log(kanbanDataList);
function dealData(kanbanList) {

	if (!kanbanList.result) {// 报错时的初始化
		toClean();
		$("#tableList").empty();
		return false;
	}
	var kanbanData = kanbanList.data.List_result2;
	var kanbanData_t = kanbanList.data.List_result1;

	if (kanbanData.length > 0) {
		setTable(kanbanData);// 表格数据
	} else {
		$("#tableList").empty();
	}

	if (kanbanData_t.length > 0) {
		var liner = kanbanData_t[0].LINER_NAME;
		var line = kanbanData_t[0].FLINE_NAME;
		var taskNo = kanbanData_t[0].TASK_NO;
		var empOnline = kanbanData_t[0].NUM_EMP_ON;
		var capacity = kanbanData_t[0].CAPACITY;

		var itemNo = kanbanData_t[0].ITEM_NO;
		var itemName = kanbanData_t[0].ITEM_NAME;
		var planQty = kanbanData_t[0].QTY_PLAN == null ? 0
				: kanbanData_t[0].QTY_PLAN;
		var inputQty = kanbanData_t[0].QTY_NPT == null ? 0
				: kanbanData_t[0].QTY_NPT;
		var doneQty = kanbanData_t[0].QTY_DONE == null ? 0
				: kanbanData_t[0].QTY_DONE; 
		var doneRate =  kanbanData_t[0].RATE_DONE == null ? 0
				: kanbanData_t[0].RATE_DONE;

		$("#liner").text(liner);
		$("#line").text(line);
		$("#taskNo").text(taskNo);
		$("#empOnline").text(empOnline);
		$("#capacity").text(capacity);

		$("#itemNo").text(itemNo);
		$("#itemName").text(itemName);
		$("#planQty").text(planQty);
		;
		$("#inputQty").text(inputQty);
		$("#doneQty").text(doneQty);
		
		getChart3(doneRate);

	} else {
		toClean();
	}
}
function toClean() {
	getChart3(0)
	$("#liner").text("");
	$("#line").text("");
	$("#taskNo").text("");
	$("#empOnline").text("");
	$("#capacity").text("");
	$("#itemNo").text("");
	$("#itemName").text("");
	$("#planQty").text("");
	$("#inputQty").text("");
	$("#doneQty").text("");
}

function getChart3(doneRate) {
	option = {
		series : [ {
			name : '完工率',
			type : 'gauge',
			min : 0,
			max : 100,
			splitNumber : 5,
			radius : '99%',
			axisLine : { // 坐标轴线
				lineStyle : { // 属性lineStyle控制线条样式
					color : [ [ 0.09, 'lime' ], [ 0.82, '#1e90ff' ],
							[ 1, '#ff4500' ] ],
					width : 3,
					shadowColor : '#fff', // 默认透明
					shadowBlur : 10
				}
			},
			axisLabel : { // 坐标轴小标记
				fontWeight : 'bolder',
				color : '#fff',
				shadowColor : '#fff', // 默认透明
				shadowBlur : 10
			},
			axisTick : { // 坐标轴小标记
				length : 15, // 属性length控制线长
				lineStyle : { // 属性lineStyle控制线条样式
					color : 'auto',
					shadowColor : '#fff', // 默认透明
					shadowBlur : 10
				}
			},
			splitLine : { // 分隔线
				length : 25, // 属性length控制线长
				lineStyle : { // 属性lineStyle（详见lineStyle）控制线条样式
					width : 3,
					color : '#fff',
					shadowColor : '#fff', // 默认透明
					shadowBlur : 10
				}
			},
			pointer : { // 分隔线
				shadowColor : '#fff', // 默认透明
				shadowBlur : 5
			},
			title : {
				textStyle : { // 其余属性默认使用全局文本样式，详见TEXTSTYLE
					fontWeight : 'bolder',
					fontSize : 12,
					fontStyle : 'italic',
					color : '#fff',
					shadowColor : '#fff', // 默认透明
					shadowBlur : 10
				}
			},
			detail : {
				formatter : '{value}%'
			},
			data : [ {
				value : doneRate,
				name : '完工率'
			} ]
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
		html += '<tr><td>' + arr.FTIME + '</td><td>' + arr.QTY_NPT
				+ '</td><td>' + arr.QTY_DONE + '</td><td>' + arr.QTY_OK
				+ '</td><td>' + arr.QTY_NG + '</td><td>' + arr.RATE_OK
				+ '%</td></tr> ';
	}
	$("#tableList").empty();
	$("#tableList").append(html);
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
		if (j == 0) {
			html += "<option value='" + arr.LEAD_BY + "' selected>"
					+ arr.LEAD_BY + "</option>";
		} else {
			html += "<option value='" + arr.LEAD_BY + "'>" + arr.LEAD_BY
					+ "</option>";
		}

	}

	$("#liner_select").append(html);
}
function getList() {
	var taskNo = $("#taskNo").text();
	var deptId = "";
	var liner = $("#liner_select").val();
	var interval = "2";
	var params = {
		"taskNo" : taskNo,
		"deptId" : deptId,
		"liner" : liner,
		"interval" : interval
	};
	$.ajax({
		type : "GET",
		url : context + "kanban/getCxscList",
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
				alert(res.msg)
			}
		}
	});

}
