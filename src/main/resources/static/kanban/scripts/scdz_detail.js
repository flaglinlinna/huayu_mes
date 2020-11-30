var action = true;// 请求结果，fasle 不执行定时器
var interval_do = null;// 页面刷新定时器
var MyMarhq = null;// 表格滚动定时器

$(function() {
	var kanbanList = kanbanDataList;
	dealData(kanbanList);
	//alert(screen.height)
	/*var intervaldata = interval.data;
	intervaldata = intervaldata[0].A;// 获取系统设置的刷新间隔时间
	interval_do = setInterval(getList, intervaldata * 1000); // 启动,执行默认方法
*/

})

function dealData(kanbanList) {
	console.log(kanbanList)
	if(!kanbanList.result){
		alert(kanbanList.msg)
		return false;
	}
	var kanbanData = kanbanList.data;
	if (kanbanData.length > 0) {
		setTable(kanbanData);
	} else {
		$("#tableList").empty();
	}
}


function setTable(kanbanData) {
	var html = "";
	//console.log(kanbanData)
	//在此判断数据格式
	if(fieldword=="PO_RESULT"){
		for (var j = 0; j < kanbanData.length; j++) {
			var arr = kanbanData[j];
			html += '<tr>'+
			        '<td>' + arr.LINER_NAME + '</td>'+
			        '<td>' + arr.NUM_EMP_PL+ '</td>'+
			        '<td>' + arr.TASK_NO + '</td>'+
			        '<td>' + arr.QTY_PLAN+ '</td>'+
					'<td>' + arr.QTY_DONE + '</td>'+
					'<td>'+ arr.EMP_NAME + '</td></tr> ';
		}
	}else if(fieldword=="PO_LINE_NUM_PLN"||fieldword=="PO_EMP_NUM_PLN"){
		for (var j = 0; j < kanbanData.length; j++) {
			var arr = kanbanData[j];
			html += '<tr>'+
	        '<td>' + arr.LEAD_BY + '</td>'+
			'<td>'+ arr.EMP_NUM + '</td></tr> ';
		}	
	}else if(fieldword=="PO_LINE_NUM_NOW"||fieldword=="PO_EMP_NUM_NOW"
		||fieldword=="PO_PRD_NUM_PLN"||fieldword=="PO_PRD_NUM_DONE"){
		var arr = kanbanData[j];
		html += '<tr>'+
		        '<td>' + arr.LINER_NAME + '</td>'+
		        '<td>' + arr.NUM_EMP_ON+ '</td>'+
		        '<td>' + arr.NUM_EMP_PL + '</td>'+
		        '<td>' + arr.QTY_PLAN+ '</td>'+
				'<td>' + arr.QTY_DONE + '</td>'+
				'<td>'+ arr.RATE_DONE + '</td></tr> ';
	}
	$("#tableList").empty();
	$("#tableList").append(html);
	$("#tableList1").empty();//不加此数据表头会歪
	$("#tableList1").append(html);//不加此数据表头会歪

	if (MyMarhq != null) {// 判断计时器是否为空-关闭
		clearInterval(MyMarhq);
		MyMarhq = null;
	}
	
	var item = $('.tbl-body tbody tr').length
	console.log(item)

	if (item > 10) {
		$('.tbl-body tbody').html(
				$('.tbl-body tbody').html() + $('.tbl-body tbody').html());
		$('.tbl-body').css('top', '0');
		var tblTop = 0;
		var speedhq = 80; // 数值越大越慢
		var outerHeight = $('.tbl-body tbody').find("tr").outerHeight();
		console.log(speedhq)
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

function getDetailList(liner) {
	var params = {
		"liner" : liner,
		"fieldword":fieldword
	};
	$.ajax({
		type : "GET",
		url : context + "/kanban/getScdzDetailList",
		data : params,
		dataType : "json",
		success : function(res) {
			console.log(res)
		}
	});
}
