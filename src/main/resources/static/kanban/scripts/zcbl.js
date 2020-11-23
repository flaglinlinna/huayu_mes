var action = true;// 请求结果，fasle 不执行定时器
var interval_do = null;// 页面刷新定时器
var MyMarhq = null;// 表格滚动定时器
$(function() {
	// getDepList(deptList);
	var kanbanList = kanbanDataList;

	var intervaldata = interval.data;
	intervaldata = intervaldata[0].A;// 获取系统设置的刷新间隔时间

	dealData(kanbanList);
	interval_do = setInterval(getList, intervaldata * 1000); // 启动,执行默认方法

	$("#searchBtn").click(function() {
		if (interval_do != null) {// 判断计时器是否为空-关闭
			clearInterval(interval_do);
			interval_do = null;
		}
		if (MyMarhq != null) {// 判断计时器是否为空-关闭
			clearInterval(MyMarhq);
			MyMarhq = null;
		}
		getList()
		if (action) {// action 为 fasle 不调用定时器
			interval_do = setInterval(getList, intervaldata * 1000); // 重新新循环-启动
		}
	});
})

function dealData(kanbanList) {
	console.log(kanbanList);
	if (kanbanList.data != null) {
		var title = kanbanList.data.DeptName == null ? ""
				: kanbanList.data.DeptName
		$("#title").text(title + "•制程不良看板");

		var kanbanData = kanbanList.data.List;
		setTable(kanbanData);
	}
}

function setTable(kanbanData) {
	var html = "";
	for (var j = 0; j < kanbanData.length; j++) {
		var arr = kanbanData[j];
		html += '<tr><td>' + arr.TIME_INTERVAL + '</td><td>' + arr.FQC_SJ
				+ '</td><td>' + arr.FQC_OK + '</td><td>' + arr.FQC_NG
				+ '</td><td>' + arr.FQC_RATE + '</td><td>' + arr.SQC_SJ
				+ '</td><td>' + arr.SQC_OK + '</td><td>' + arr.SQC_NG
				+ '</td><td>' + arr.SQC_RATE + '</td><td>' + arr.KJ1_SJ
				+ '</td><td>' + arr.KJ1_OK + '</td><td>' + arr.KJ1_NG
				+ '</td><td>' + arr.KJ1_RATE + '</td><td>' + arr.KJ2_SJ
				+ '</td><td>' + arr.KJ2_OK + '</td><td>' + arr.KJ2_NG
				+ '</td><td>' + arr.KJ2_RATE + '</td></tr>';
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

	if (item > 10) {
		$('.tbl-body tbody').html(
				$('.tbl-body tbody').html() + $('.tbl-body tbody').html());
		$('.tbl-body').css('top', '0');
		var tblTop = 0;
		var speedhq = 50; // 数值越大越慢
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
	$("#dep_select").empty();
	var html = "<option value=''>请选择部门</option>";
	for (j = 0, len = res.data.length; j < len; j++) {
		var arr = res.data[j];
		html += "<option value='" + arr.ID + "'>" + arr.LEAD_BY + "</option>";
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
		url : context + "kanban/getZcblList",
		data : params,
		dataType : "json",
		success : function(res) {
			//console.log(res)
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
