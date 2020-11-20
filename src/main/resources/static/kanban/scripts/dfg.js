
$(function(){
  dealData();
  getDepList(deptList);
	$("#searchBtn").click(function(){
		getList();
	});
	
})
//console.log(kanbanDataList);
function dealData(){	
	if(kanbanDataList.data!=null){
		var title=kanbanDataList.data.List_dept==null?"":kanbanDataList.data.List_dept
				$("#title").text(title+"•制程不良看板");
		var kanbanData=kanbanDataList.data.List_table;
		console.log(kanbanData)
		setTable(kanbanData); 
	}
}

function setTable(kanbanData){
	var html = "";
	for(var j = 0; j < kanbanData.length; j++) {
		 var arr = kanbanData[j];		 
		 html += '<tr><td>'+arr.CHK_DATE+'</td><td>'
		 +arr.PROC_NAME+'</td><td>'+arr.DEPT_NAME+'</td><td>'+arr.LINER_NAME+'</td><td>'+arr.DEFECT_NAME+'</td><td>'
		 +arr.QTY_PROC+'</td><td>'+arr.DEFECT_NUM+'</td><td>'+arr.SAMPLE_QTY+'</td></tr>';
    }
	//console.log(html)
	 $("#tableList").empty();
	 $("#tableList").append(html);	
}
function getDepList(deptList){
	
	var res=deptList;
	console.log(res)
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
         url: context+"kanban/getDfgList",
         data: params,
         dataType: "json",
         success: function(res){
            // console.log(res)
             if(res.result){
            	 var datalist=res.data.List_table;
            	 console.log(datalist)
            	 setTable(datalist);
             }else{
            	 alert(res.msg);
             }
          }
     });
}
