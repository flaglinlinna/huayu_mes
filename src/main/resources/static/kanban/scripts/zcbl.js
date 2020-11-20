
$(function(){
  dealData();
  getDepList(deptList);
	$("#searchBtn").click(function(){
		getList();
	});
})
console.log(kanbanDataList);
function dealData(){	
	if(kanbanDataList.data!=null){
		var title=kanbanDataList.data.DeptName==null?"":kanbanDataList.data.DeptName
 				$("#title").text(title+"•制程不良看板");
		
		var kanbanData=kanbanDataList.data.List;
		
		setTable(kanbanData);
	}
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
	 $("#tableList").empty();
	 $("#tableList").append(html);	
}
function getDepList(deptList){
	var res=deptList;
	$("#dep_select").empty();
	var html = "<option value=''>请选择部门</option>";
	for (j = 0, len = res.data.length; j < len; j++) {
		var arr = res.data[j];
		html += "<option value='" + arr.ID + "'>" + arr.LEAD_BY
				+ "</option>";
	}

	$("#dep_select").append(html);
	
}
function getList(){
	var class_no=$("#class_select").val();
	//var dep_id=$("#dep_select").val();
	var date=$("#date").val();
	var params = {
			"class_nos":class_no,
			"dep_id":"5253",
			"sdata":date
		};
	$.ajax({
         type: "GET",
         url: context+"kanban/getZcblList",
         data: params,
         dataType: "json",
         success: function(res){
             console.log(res)
             if(res.result){
            	 var title=res.data.DeptName==null?"":res.data.DeptName
         				$("#title").text(title+"•制程不良看板");
            	 
            	 var datalist=res.data.List;
            	 setTable(datalist);
             }else{
            	 alert(res.msg);
             }
          }
     });
}
