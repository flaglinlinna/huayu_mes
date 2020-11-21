var action = true;// 请求结果，fasle 不执行定时器
var interval_do = null;// 页面刷新定时器
var MyMarhq = null;// 表格滚动定时器

$(function() {
	var kanbanList = kanbanDataList;
	// getDepList(deptList);
	var intervaldata = interval.data;
	intervaldata = intervaldata[0].A;// 获取系统设置的刷新间隔时间

	dealData(kanbanList);
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
// console.log(kanbanDataList);
function dealData(kanbanList) {
	console.log(kanbanList)
	if (kanbanList.data != null) {
		var title = kanbanList.data.List_dept == null ? ""
				: kanbanList.data.List_dept
		$("#title").text(title + "•待返工看板");
		var kanbanData = kanbanList.data.List_table;
		setTable(kanbanData);
	}
}

function setTable(kanbanData) {
	var html = "";
	for (var j = 0; j < kanbanData.length; j++) {
		var arr = kanbanData[j];
		html += '<tr><td>' + arr.CHK_DATE + '</td><td>' + arr.PROC_NAME
				+ '</td><td>' + arr.DEPT_NAME + '</td><td>' + arr.LINER_NAME
				+ '</td><td>' + arr.DEFECT_NAME + '</td><td>' + arr.QTY_PROC
				+ '</td><td>' + arr.DEFECT_NUM + '</td><td>' + arr.SAMPLE_QTY
				+ '</td></tr>';
	}
	// console.log(html)
	$("#tableList").empty();
	$("#tableList").append(html);
	$("#tableList1").empty();// 不加此数据表头会歪
	$("#tableList1").append(html);// 不加此数据表头会歪
	
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
		html += "<option value='" + arr.ID + "'>" + arr.LEAD_BY + "</option>";
	}

	$("#dep_select").append(html);
}
function getList() {
	var class_no = $("#class_select").val();
	// var dep_id=$("#dep_select").val();
	var date = $("#date").val();
	var params = {
		"class_id" : class_no,
		"dep_id" : "",
		"sdata" : date
	};
	$.ajax({
		type : "GET",
		url : context + "kanban/getDfgList",
		data : params,
		dataType : "json",
		success : function(res) {
			// console.log(res)
			if (res.result) {
				action = true;
				dealData(res)
			} else {
				action = false;
				if (interval_do != null) {// 错误-关闭定时器
					clearInterval(interval_do);
					interval_do = null;
				}
				alert(res.msg);
			}
		}
	});
}
