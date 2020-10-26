
$(function(){
  //chartDiv();
	getDepList();
	$("#searchBtn").click(function(){
		getList();
		});
  
})
function chartDiv(xAxis_data,series1_data,series2_data,series3_data){
	option = {
		    tooltip: {
		        trigger: 'axis',
		        axisPointer: {
		            type: 'cross',
		            crossStyle: {
		                color: '#999'
		            }
		        }
		    },
		    legend: {
		    	 x:'center',
		    	 y:'bottom',
		        data: ['标准工时', '实际工时','生产效率'],
		        textStyle:{
                    fontSize: 18,//字体大小
                    color: '#ffffff'//字体颜色
                },
		    },
		    xAxis: [
		        {
		            type: 'category',
		            data: xAxis_data,//['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
		            axisPointer: {
		                type: 'shadow'
		            },
		            axisLabel: {
                        show: true,
                        textStyle: {
                            color: '#ffffff'
                        }
                    }
		            
		        }
		    ],
		    yAxis: [
		        {
		            type: 'value',
		            /*name: '水量',
		            min: 0,
		            max: 250,*/
		            interval: 50,
		            splitLine:{show:false},
		            axisLabel: {
		                formatter: '{value} 个',
		                textStyle: {
                            color: '#ffffff'
                        }
		            },
		        },
		        {
		            type: 'value',
		            /*name: '温度',
		            min: 0,
		            max: 25,
		            interval: 5,*/
		            splitLine:{show:false},
		            axisLabel: {
		                formatter: '{value} %',
		                textStyle: {
                            color: '#ffffff'
                        }
		            }
		        }
		    ],
		    series: [
		        {
		            name: '标准工时',
		            type: 'bar',
		            data: series1_data//[2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3]
		        },
		        {
		            name: '实际工时',
		            type: 'bar',
		            data: series2_data//[2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3]
		        },
		        {
		            name: '生产效率',
		            type: 'line',
		            yAxisIndex: 1,
		            data: series3_data//[2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2]
		        }
		    ]
		};
	//创建echarts对象在哪个节点上
    var myCharts1 = echarts.init(document.getElementById('chartDiv'));
	   //将选项对象赋值给echarts对象。
	  myCharts1.setOption(option,true);
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
	var params = {"class_nos":'999',"dep_id":'',"sdata":'',"edata":''};
	$.ajax({
         type: "GET",
         url: context+"kanban/getCjbgList",
         data: params,
         dataType: "json",
         success: function(res){
             console.log(res)
             if(res.result){
            	 var html = "";var xAxis=[];var series1=[];var series2=[];var series3=[];
            	 for(j = 0,len=res.data.List.length; j < len; j++) {
            		 var arr = res.data.List[j]
            		 html += '<div class="layui-col-md3"> '+
						       ' <div class="div_any_child"> '+
					                '<div class="div_any_title">'+arr.LINER_NAME+'</div> '+
					                 ' <div  class="tip-container"> '+
										   '<div class="tip-content" > '+
										     '<div  class="speed-container"> '+
										      '<div class="speed-content"> '+
										       '<ul> '+
										       '<li><span class="zhzb-li-name">在线人数</span><span class="zhzb-li-num">'+arr.NUM_LINE_ON+'</span></li> '+
										       '<li><span class="zhzb-li-name">标准工时</span><span class="zhzb-li-num">'+arr.HOUR_ST+'</span></li> '+
										       '<li><span class="zhzb-li-name">实际工时</span><span class="zhzb-li-num">'+arr.HOUR_ACT+'</span></li> '+
										       '<li><span>生产效率</span><span class="speed-line"><span  class="speed-num" style="background-color: rgb(85, 182, 237); width: '+arr.EFFICIENCY_RATE+'%;"></span><span class="numText">'+arr.EFFICIENCY_RATE+'%</span></span></li> '+
										       '</ul> '+
										      '</div> '+
										     '</div> '+
										   '</div> '+
					                   '</div>  '+
					            '</div> '+
						    '</div> ';
            		 
            		 xAxis[j] = arr.LINER_NAME;
            		 series1[j] = arr.HOUR_ST;
            		 series2[j] = arr.HOUR_ACT;
            		 series3[j] = arr.EFFICIENCY_RATE;
                 }
            	 $("#rowDiv").empty();
            	 $("#rowDiv").append(html);
            	 
            	 //图表
            	 chartDiv(xAxis,series1,series2,series3);
             }
             
          }
     });

}