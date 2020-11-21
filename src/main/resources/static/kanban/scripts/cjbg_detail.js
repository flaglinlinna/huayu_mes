var action = true;// 请求结果，fasle 不执行定时器
var interval_do = null;// 页面刷新定时器
var MyMarhq = null;// 表格滚动定时器

$(function() {
	// getDepList(deptList);//获取部门下来菜单
	var kanbanList = kanbanDataList;
	dealData(kanbanList);

	/*var intervaldata = interval.data;
	intervaldata = intervaldata[0].A;// 获取系统设置的刷新间隔时间
	interval_do = setInterval(getList, intervaldata * 1000); // 启动,执行默认方法
*/

})

function dealData(kanbanList) {
	console.log(kanbanList)
	if (!kanbanList.result) {// 报错时的初始化
		toClean();
		$("#tableList").empty();
		return false;
	}
	var title=kanbanList.data.Title==null?"":kanbanList.data.Title
	$("#title").text(title+"•效率看板");
	
	var kanbanData = kanbanList.data.List;
	if (kanbanData.length > 0) {
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
		$("#showLine").text("总开线数：" + kanbanList.data.LineNum);
		setTable(kanbanData);
	} else {
		toClean();
		$("#tableList").empty();
	}
}


function setTable(kanbanData) {
	var html = "";
	for (var j = 0; j < kanbanData.length; j++) {
		var arr = kanbanData[j];
		// if(j==kanbanData.length-1){
		// html += '<tr style="position:relative;"><td>' + arr.LINER_NAME +
		// '</td><td>' + arr.HOUR_ST
		// + '</td><td>' + arr.HOUR_ACT + '</td><td>' + arr.HOUR_ABN
		// + '</td><td>' + arr.NUM_EMP_ON + '</td><td>'
		// + arr.EFFICIENCY_RATE + '%</td></tr> ';
		// break;
		// }
		html += '<tr><td><a href="toCjbgDetail?liner=+'+arr.LINER_NAME+'" target="_blank">' + arr.LINER_NAME + '</a></td><td>' + arr.HOUR_ST
				+ '</td><td>' + arr.HOUR_ACT + '</td><td>' + arr.HOUR_ABN
				+ '</td><td>' + arr.NUM_EMP_ON + '</td><td>'
				+ arr.EFFICIENCY_RATE + '%</td></tr> ';
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
	var date = $("#date").val();
	var class_no = $("#class_select").val();
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
function getDetailList(liner) {
	alert(liner)
	var params = {
		"liner" : liner
	};
	$.ajax({
		type : "GET",
		url : context + "/kanban/getCjbgDetailList",
		data : params,
		dataType : "json",
		success : function(res) {
			console.log(res)
		}
	});
}
