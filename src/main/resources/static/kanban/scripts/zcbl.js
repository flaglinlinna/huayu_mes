
$(function(){
  dealData();
})
console.log(kanbanDataList);
function dealData(){	
	var kanbanData=kanbanDataList.data.List;
	console.log(kanbanData)
	setTable(kanbanData);
	
}

function setTable(kanbanData){
	var html = "";
	for(var j = 0; j < kanbanData.length; j++) {
		 var arr = kanbanData[j];		 
		 html += '<tr><td>'+arr.TIME_INTERVAL+'</td><td>'
		 +arr.FQC_SJ+'</td><td>'+arr.FQC_OK+'</td><td>'+arr.FQC_NG+'</td><td>'+arr.FQC_RATE+'</td><td>'
		 +arr.SQC_SJ+'</td><td>'+arr.SQC_OK+'</td><td>'+arr.SQC_NG+'</td><td>'+arr.SQC_RATE+'</td><td>'
		 +arr.KJ1_SJ+'</td><td>'+arr.KJ1_OK+'</td><td>'+arr.KJ1_NG+'</td><td>'+arr.KJ1_RATE+'</td><td>'
		 +arr.KJ2_SJ+'</td><td>'+arr.KJ2_OK+'</td><td>'+arr.KJ2_NG+'</td><td>'+arr.KJ2_RATE
		 +'</td></tr>';
    }
	console.log(html)
	 $("#tableList").empty();
	 $("#tableList").append(html);
	
}
