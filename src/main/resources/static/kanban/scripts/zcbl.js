
$(function(){
  dealData();
  getDepList();
	$("#searchBtn").click(function(){
		getList();
	});
})
console.log(kanbanDataList);
function dealData(){	
	if(kanbanDataList.data!=null){
		var kanbanData=kanbanDataList.data.List;
		console.log(kanbanData)
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
	console.log(html)
	 $("#tableList").empty();
	 $("#tableList").append(html);	
}
function getDepList(){
	$.ajax({
        type: "GET",
        url: context+"kanban/getCjbgDepList",
        data: {},
        dataType: "json",
        success: function(res){
            console.log(res)
            if(res.result){
            	$("#dep_select").empty();
            	var html = "<option value=''>请选择部门</option>";
            	for(j = 0,len=res.data.length; j < len; j++) {
            		var arr = res.data[j];
            		html += "<option value='"+arr.ID+"'>"+arr.ORG_NAME+"</option>";
            	}
            	
           	   $("#dep_select").append(html);
            }
         }
    });
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
         url: context+"kanban/getZcblList",
         data: params,
         dataType: "json",
         success: function(res){
             console.log(res)
             if(res.result){
            	 var datalist=res.data;
            	 setTable(datalist);
             }else{
            	 alert(res.msg);
             }
          }
     });
}
